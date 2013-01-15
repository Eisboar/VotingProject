package com.example.votingServer.data;

import java.util.Vector;

public class QuestionSheet {
	
	private String title;
	
	private Vector<Question> questions;
	
	public QuestionSheet(String title, Vector<Question> questions){
		this.questions=questions;
		this.title=title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Vector<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Vector<Question> questions) {
		this.questions = questions;
	} 
	
}
