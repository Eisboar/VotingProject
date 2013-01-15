package com.example.votingServer;

import java.io.*;
import java.net.*;
import java.util.Map.Entry;
import java.util.Vector;

import com.example.votingServer.data.MultipleChoiceQuestion;
import com.example.votingServer.data.Question;
import com.example.votingServer.data.QuestionSheet;

class ThreadHandler extends Thread {
	Socket newsock;
	int n;
	GUI gui;

	private BufferedReader inp;
	private PrintWriter outp;
	
	private String sID = null;
	private String qID = null;

	
	ThreadHandler(Socket s, int v, GUI gui) {
		newsock = s;
		n = v;
		this.gui = gui;
		gui.addText("init handlerthread\n");
	}
	
	private void initIOStreams() throws IOException{
		outp = new PrintWriter(newsock.getOutputStream(), true);
		inp = new BufferedReader(new InputStreamReader(
				newsock.getInputStream()));
	}
	
	public void run() {
		try {
			initIOStreams();
			//gui.addText("lalal\n");
			//System.out.println("see what he has written:");
			
			// read fullrequest
			Vector<String> requestLines = new Vector<String>();
			String line = inp.readLine();
			while (line != null && !line.equals("QUIT")) {
				gui.addText(line + "\n");
				System.out.println(line);
				requestLines.add(line);
				line = inp.readLine();
			}
		//////////////////test///////////////////
			System.out.println("lala");
		/////////////////////////////////////////
			analyseRequest(requestLines);

			System.out.println("Disconnected from client number: " + n);
			newsock.close();
			System.out.println("Disconnected from client number: " + n);
		} catch (Exception e) {
			System.out.println("IO error " + e);
		}

	}

	private String analyseRequest(Vector<String> requestLines) {
		// serach for student ID
//////////////////test///////////////////
	System.out.println("lala1");
/////////////////////////////////////////
		for (String line : requestLines) {
			if (sID==null)
				sID = parseID(line,"sID=");
			if (qID==null)
				qID = parseID(line, "qID=");
		}
		if (sID!=null)
			if (qID!=null){
				//////////////////test///////////////////
				System.out.println("lala3");
			/////////////////////////////////////////
				System.out.println("Stundent "+ sID + " requested: "+qID);
				QuestionSheet questionSheet = gui.getQuestionSheet(qID);
				if (questionSheet!=null){
	//////////////////test///////////////////
					System.out.println("lala2");
				/////////////////////////////////////////
					sendQuestionSheet(questionSheet);
				}
			}
		return null;
	}
	
	private void sendQuestionSheet(QuestionSheet questionSheet){
//////////////////test///////////////////
	System.out.println("lala");
/////////////////////////////////////////
		String response="";
		response += "qID="+questionSheet.getTitle()+
				"\ncount="+questionSheet.getQuestions().size();
		for(Question question: questionSheet.getQuestions()){
			response+="\nq="+question.getQuestionString()
					+ "\ncount="+((MultipleChoiceQuestion)question).getAnswers().size();
			for (Entry<Integer,String> answer: ((MultipleChoiceQuestion)question).getAnswers().entrySet()){
				response+="\n"+answer.getKey()+"="+answer.getValue();
			}
		}
		System.out.println(response);
		outp.println(response);
	}
	
	private String parseID(String line, String Pattern){
		String ID = null;
		if (line.startsWith(Pattern)) {
			ID = line.substring(line.indexOf("=") + 1);
			if (ID == null || ID.trim().length() == 0)
				return null;
		}
		return ID;
	}
}