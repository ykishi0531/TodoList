package com.example.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.TodoDto;
import com.example.enums.PriorityEnum;
import com.example.form.TodoForm;
import com.example.service.TodoService;

/**
 * TODO一覧のContorollerクラスです
 * 
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TodoService todoService;

    private String orderType;
    private String sortType;
    private boolean ignoreDone;

    /**
     * リスト一覧を表示します
     * 
     * @param orderType ソート対象のカラム
     * @param sortType 昇順降順
     * @param ignoreDone 完了したTODOを含めるか否か
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam(defaultValue = "0") String orderType,
            @RequestParam(defaultValue = "0") String sortType, @RequestParam(defaultValue = "false") String ignoreDone,
            Model model) {
        TodoForm form = new TodoForm();

        this.orderType = orderType;
        this.sortType = sortType;
        this.ignoreDone = BooleanUtils.toBoolean(ignoreDone);

        List<TodoDto> todoDtoList = todoService.getList(this.orderType, this.sortType, this.ignoreDone);

        form.setId(todoDtoList.size() + 1);
        model.addAttribute("form", form);
        model.addAttribute("priorities", Arrays.asList(PriorityEnum.values()));
        model.addAttribute("todoDtoList", todoDtoList);
        return "index";
    }

    /**
     * 入力された内容を登録します
     * 
     * @param form 入力情報
     * @param model
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute TodoForm form, Model model) {
        // 登録処理
        todoService.addTodo(form);
        return "redirect:/" + generateRedirectParam();
    }

    /**
     * 送信したIDのTODOを完了します
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String done(@RequestParam long id, Model model) {
        // 完了処理
        todoService.doneTodo(id);
        return "redirect:/" + generateRedirectParam();
    }

    /**
     * リダイレクト用のパラメータを生成します
     * 
     * @return
     */
    private String generateRedirectParam() {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        sb.append("orderType=" + orderType);
        sb.append("&");
        sb.append("sortType=" + sortType);
        String ignoreStr = ignoreDone ? "true" : "false";
        sb.append("&");
        sb.append("ignoreDone=" + ignoreStr);
        return sb.toString();
    }
}
