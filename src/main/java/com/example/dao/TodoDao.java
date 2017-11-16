package com.example.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.example.Constants;
import com.example.entity.Todo;

/**
 * csvのIOを行なうクラスです 本来なら、DBのCRUDを行なうクラスです
 * 
 */
@Repository
public class TodoDao {

    @Autowired
    protected ResourceLoader resourceLoader;

    private final String FILE_NAME = "data/todo_list.csv";

    /**
     * csvの内容をentityに詰めて返します
     * 
     * @return
     */
    public List<Todo> findAll() {
        List<Todo> resultList = new ArrayList<>();
        Resource fileResource = resourceLoader.getResource("classpath:" + FILE_NAME);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileResource.getInputStream()))) {
            String line;
            boolean isHeader = true;
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

            while ((line = br.readLine()) != null) {
                // ヘッダ行はスキップする
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                Todo entity = new Todo();
                String[] spltLine = line.split(",");
                // id
                entity.setId(Long.parseLong(spltLine[0]));
                // value
                entity.setValue(spltLine[1]);
                // limitDate
                entity.setLimitDate(new Timestamp(sdf.parse(spltLine[2]).getTime()));
                // priority
                entity.setPriorityId(Integer.parseInt(spltLine[3]));
                // done
                entity.setDoneFlg(Integer.parseInt(spltLine[4]));

                resultList.add(entity);
            }
        } catch (IOException e) {
            // ファイルIOに失敗
            e.printStackTrace();
        } catch (ParseException e) {
            // 日時フォーマットの変換に失敗
            e.printStackTrace();
        }

        return resultList;
    }

    public void insert(Todo todo) {
        writeString(todo.toString() + "\r\n");
    }

    /**
     * entityの内容をcsvに書き込みます
     * 
     * @param list
     *            entityのリスト
     */
    public void updateList(List<Todo> list) {
        // 単一行の書き換えが面倒なので、全部消して全部入れる
        reCreateFile();

        StringBuilder sb = new StringBuilder(Constants.CSV_HEADER + "\r\n");
        for (Todo todo : list) {
            sb.append(todo.toString());
            sb.append("\r\n");
        }

        writeString(sb.toString());
    }

    /**
     * Stringの内容をcsvファイルに書き込みます
     * 
     * @param csvLine
     *            csv形式のString文字列
     */
    private void writeString(String csvLine) {

        Resource fileResource = resourceLoader.getResource("classpath:" + FILE_NAME);
        try {
            File file = fileResource.getFile();
            try (FileWriter fileWriter = new FileWriter(file, true)) {
                fileWriter.write(csvLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * csvファイルを削除して生成します
     * 
     */
    private void reCreateFile() {

        Resource fileResource = resourceLoader.getResource("classpath:" + FILE_NAME);
        try {
            String filePath = fileResource.getURI().getPath();
            fileResource.getFile().delete();
            new File(filePath).createNewFile();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
