package com.example.dto;

import com.example.enums.PriorityEnum;

public class TodoDto {

  private long id;
  
  private String value;
  
  private String limitDate;
  
  private PriorityEnum priority;
  
  private boolean done;

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

  public String getLimitDate() {
    return limitDate;
  }

  public void setLimitDate(String limitDate) {
    this.limitDate = limitDate;
  }

  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }
  
  
}
