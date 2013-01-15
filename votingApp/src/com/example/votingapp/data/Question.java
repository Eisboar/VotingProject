package com.example.votingapp.data;

import java.util.Map;

public class Question {
	private String questionString;
	private Map<Integer,String> answers;
	public Map<Integer,String> getAnswers() {
		return answers;
	}
	public void setAnswers(Map<Integer,String> answers) {
		this.answers = answers;
	}
	public String getQuestionString() {
		return questionString;
	}
	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}
}
