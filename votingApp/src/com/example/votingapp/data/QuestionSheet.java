package com.example.votingapp.data;

import java.util.Vector;

public class QuestionSheet {
	private String qID;
	private Vector<Question> questions;
	
	public String getqID() {
		return qID;
	}
	public void setqID(String qID) {
		this.qID = qID;
	}

	public Vector<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(Vector<Question> questions) {
		this.questions = questions;
	}
}
