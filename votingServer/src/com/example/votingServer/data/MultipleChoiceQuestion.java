package com.example.votingServer.data;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class MultipleChoiceQuestion extends Question {

	private Map<Integer,String> answers;
	
	private Vector<Integer> correctAnswers;

	public Vector<Integer> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Vector<Integer> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public Map<Integer,String> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<Integer,String> answers) {
		this.answers = answers;
	}
	
	@Override
	public String toString() {
		String returnString="";
		returnString+="MultipleChoiceQuestion:"
				+ "\nDescription: " + getDescription()
				+ "\nQuestion: " + getQuestionString();
		returnString+=  "\nAnswers: ";
		for ( Entry<Integer, String> answer : answers.entrySet()){
			returnString+="\n"+ answer.getKey()+ ": " + answer.getValue();
		}
		returnString+=  "\ncorrect Answers: ";
		for ( Integer  correctAnswerID : correctAnswers){
			returnString+="\n"+ correctAnswerID;
		}
				
		return returnString+"\n";
	}
	
}
