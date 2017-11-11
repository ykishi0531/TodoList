package com.example.form;

public class TodoForm {

  private long id;
  
	private String value;
	
	private String limitDate;
	
	private int priorityId;
	
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

	public int getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(int priorityId) {
		this.priorityId = priorityId;
	}
	
}
