package com.example.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Todo {

    private long id;

    private String value;

    private Timestamp limitDate;

    private int priorityId;

    private int doneFlg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Timestamp limitDate) {
        this.limitDate = limitDate;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getDoneFlg() {
        return doneFlg;
    }

    public void setDoneFlg(int doneFlg) {
        this.doneFlg = doneFlg;
    }

    @Override
    public String toString() {
        return this.id + "," + this.value + "," + this.limitDate + "," + this.priorityId + "," + this.doneFlg;
    }
}
