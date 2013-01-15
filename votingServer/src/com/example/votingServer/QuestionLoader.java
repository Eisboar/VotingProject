package com.example.votingServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.example.votingServer.data.MultipleChoiceQuestion;
import com.example.votingServer.data.Question;
import com.example.votingServer.data.QuestionSheet;

public class QuestionLoader {

	private QuestionSheet questionSheet = null;
	
	public QuestionLoader (File file){
		
		String filename = file.getName();
		String fileContent = loadContent(file);
		if (fileContent == null) return;
		
	    questionSheet = parseQuestions(fileContent, filename);
	    System.out.println("question: "+filename+ " loaded.");
	}

	public QuestionSheet getQuestionSheet(){
		return questionSheet;
	}
	
	private String loadContent(File file) {
		try {
			FileReader fr = new FileReader(file);

			char[] temp = new char[(int) file.length()];

			fr.read(temp);

			String fileContent = new String(temp);

			//System.out.println(fileContent);

			fr.close();

			return fileContent;

		} catch (FileNotFoundException e1) {
			System.err.println("file not found: " + file);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		return null;

	}

	private QuestionSheet parseQuestions (String content, String filename){
		
		Vector<Question> questions = new Vector<Question>();
		
		String[] lines = content.split("\n");
		
		Vector<String> questionStrings = new Vector<String>();
		String questionString="";
		
		for (int i=0; i<lines.length; i++){
			
			//ignore comments
			if (lines[i].startsWith("//")){
				continue;
			}
			
			if (!lines[i].equals("")){
				questionString +=" " + lines[i];
				continue;
			}
			
			if (!questionString.equals("")){
				questionStrings.add(questionString);
		    }
			questionString="";
		
			//System.out.println(lines[i]);
		}
		if (!questionString.equals("")){
			questionStrings.add(questionString);
	    }
		
		//print questionsstrings
		for (String qs  :questionStrings){
			Question question = parseSingleQuestion(qs);
			System.out.println(question);
			questions.add(question);
		}
		
		QuestionSheet questionSheet = new QuestionSheet(filename,questions);

		
		return questionSheet;
	}
	
	private Question parseSingleQuestion(String questionString){
		MultipleChoiceQuestion question = new MultipleChoiceQuestion();
		
		HashMap<Integer,String> answers = new HashMap<Integer,String>();
	    Vector<Integer> correctAnswers = new Vector<Integer>();
		
	    
		MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
		String frontString = questionString.substring(0, questionString.indexOf('{'));
		//System.out.println(frontString);
		String answerString = questionString.substring(questionString.indexOf('{')+1,questionString.indexOf('}')); 
		String[] answerfield = answerString.trim().split("((?<=~|=)|(?=~|=))");
//		for (int i =0; i<answerfield.length; i++){
//			System.out.println(answerfield[i]);
//		}
		for (int i=1; i<answerfield.length;i+=2){
			answers.put(i/2,answerfield[i+1]);
			if (answerfield[i].equals("="))
				correctAnswers.add(i/2);
		}
		//System.out.println(answerfield.length);
		String backString = questionString.substring(questionString.indexOf('}')+1);
		if (!backString.trim().equals(""))
			frontString = frontString.trim()+ " <?> "+ backString.trim();
		//System.out.println(frontString);
		
		question.setDescription("");
		question.setAnswers(answers);
		question.setCorrectAnswers(correctAnswers);
		question.setQuestionString(frontString);
		
		
		return question;
	}
	
	
}
