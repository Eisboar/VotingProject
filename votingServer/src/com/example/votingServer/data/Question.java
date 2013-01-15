package com.example.votingServer.data;

public class Question {
	//basiclic stuff like 'Question 1:'
	private String description;

	//the pure question text
	private String questionString;

	public String getQuestionString() {
		return questionString;
	}

	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
