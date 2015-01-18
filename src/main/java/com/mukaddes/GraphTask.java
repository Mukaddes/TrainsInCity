package com.mukaddes;

/**
 * 
 * @author Mukaddes Büyükkavut Ertaş
 *
 */
public class GraphTask {
	private GraphTaskType taskType;
	private String taskData;
	
	public GraphTask(){
		this.taskType = null;
		this.taskData = null;
	}
	
	public GraphTask(GraphTaskType taskType, String taskData){
		this.taskType = taskType;
		this.taskData = taskData;
	}

	public GraphTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(GraphTaskType taskType) {
		this.taskType = taskType;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}
	
	public String toString(){
		return "Task: " + taskType.name() + " - " + taskData;
	}
}
