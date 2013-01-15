package com.example.votingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import android.os.AsyncTask;

public class RequestQuestionTask extends AsyncTask<String, Void, Vector<String>> {

	private PrintWriter outp = null;
	private BufferedReader inp = null;

	private String buildQuestionRequest(String studentID, String QuestionID) {
		String request = "";
		request += "sID=" + studentID + "\n" + "qID=" + QuestionID;
		return request;
	}

	private void initIOStreams(Socket s) throws IOException {
		outp = new PrintWriter(s.getOutputStream(), true);
		inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	protected Vector<String> doInBackground(String... args) {
		try {
			Socket s = new Socket("192.168.0.122", 8080);
			Vector<String> answerLines = new Vector<String>();
			initIOStreams(s);
			// if args "0" -> request a question sheet
			if (args[0].equals("0")) {
				String request = buildQuestionRequest(args[1], args[2]);
				outp.println(request);
				outp.println("QUIT");
				//return  inp.readLine();
				
				//gatter the answer
				String line;
				line = inp.readLine();
				while (line!=null){
					answerLines.add(line);
					line = inp.readLine();
				}
			}

			s.close();
			return answerLines;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
