package com.example.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Constants;
import com.example.dao.TodoDao;
import com.example.dto.TodoDto;
import com.example.entity.Todo;
import com.example.enums.PriorityEnum;
import com.example.form.TodoForm;

/**
 * TODOリストのサービスクラスです
 * 
 */
@Service
public class TodoService {

  @Autowired
  protected TodoDao todoDao;
  
  /**
   * TODOリストをします
   * 並び順が指定されていた場合は、指定の順序で並び替えます
   * 
   * @param orderType ソート対象のカラム
   * @param sortType 順降順
   * @param ignoreDone 完了したTODOを含めるか否か
   * @return
   */
  public List<TodoDto> getList(String orderType, String sortType, boolean ignoreDone) {
    // mock
    List<TodoDto> resultDtoList = new ArrayList<>();
    
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    // リストを取得して
    List<Todo> dataList = todoDao.findAll();
    // 日付をStringにしてかえす
    for (Todo entity: dataList) {
      TodoDto dto = new TodoDto();
      dto.setId(entity.getId());
      dto.setValue(entity.getValue());
      dto.setLimitDate(sdf.format(entity.getLimitDate()));
      dto.setPriority(PriorityEnum.idOf(entity.getPriorityId()));
      dto.setDone(BooleanUtils.toBoolean(entity.getDoneFlg()));
      
      resultDtoList.add(dto);
    }
    
    // 並べ替え条件が無い場合はそのままリストを返す
    if (StringUtils.isEmpty(orderType)
        && StringUtils.isEmpty(sortType)) {
      return resultDtoList;
    }
    
    // ignoreDoneが設定されていたら、完了したTODOをリストから除去する
    if (ignoreDone) {
      // StreamAPIに置き替え
      resultDtoList = resultDtoList.stream().filter(todo -> !todo.isDone()).collect(Collectors.toList());
    }
    
    // 並べ替える
    if (!orderType.equals(Constants.ORDER_INPUT)
            || (sortType.equals(Constants.SORT_DESC))) {
      // ここもJava8化 (StreamAPI)
      // 並び順
      Comparator<TodoDto> order;
      switch (orderType) {
        case Constants.ORDER_INPUT:
          order = Comparator.comparing(TodoDto::getId);
          break;
        case Constants.ORDER_DATE:
          order =  Comparator.comparing(TodoDto::getLimitDate);
          break;
        default:
          order = Comparator.comparing(TodoDto::getPriority);
      }
      // 昇順降順
      if(sortType.equals(Constants.SORT_DESC)) {
        order = order.reversed();
      }
      resultDtoList = resultDtoList.stream().sorted(order).collect(Collectors.toList());
      
    }
    return resultDtoList;
  }
  
  /**
   * TODOを追加します
   * 
   * @param form
   */
  public void addTodo(TodoForm form) {
    // 日付をTimestampに変換してentityに詰める
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.INPUT_DATE_FORMAT);
    Todo todo = new Todo();
    try {
      todo.setId(form.getId());
      todo.setValue(form.getValue());
      todo.setLimitDate(new Timestamp(sdf.parse(form.getLimitDate()).getTime()));
      todo.setPriorityId(form.getPriorityId());
      todo.setDoneFlg(0);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    todoDao.insert(todo);
  }
  
  /**
   * TODOを完了します
   * @param id
   */
  public void doneTodo(long id) {
    List<TodoDto> resultDtoList = getList(null, null, false);
    List<Todo>entityList = new ArrayList<Todo>();
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    
    try {
      for (TodoDto dto: resultDtoList) {
        if (dto.getId() == id) {
          dto.setDone(true);
        }
        Todo todo = new Todo();
        todo.setId(dto.getId());
        todo.setValue(dto.getValue());
        todo.setLimitDate(new Timestamp(sdf.parse(dto.getLimitDate()).getTime()));
        todo.setPriorityId(dto.getPriority().getId());
        todo.setDoneFlg(BooleanUtils.toInteger(dto.isDone()));
        
        entityList.add(todo);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    todoDao.updateList(entityList);
  }
}
