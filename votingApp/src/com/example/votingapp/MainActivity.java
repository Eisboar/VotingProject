package com.example.votingapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import com.example.votingapp.data.Question;
import com.example.votingapp.data.QuestionSheet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private Scanner scanner = new Scanner(System.in);

	// DATA
	private final String studentID = "10000";

	// GUI
	private EditText msg;
	private Button getQuestionButton;
	// TextView convo;
	private TextView status;
	private TextView question;
	private TabSpec spec1;
	private TabSpec spec2;
	private TabHost th;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		th = (TabHost) findViewById(R.id.tabhost);
		th.setup();

		spec1 = th.newTabSpec("tag1");

		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Question Loading");
		
			
		th.addTab(spec1);

		spec2 = th.newTabSpec("tag2");

		spec2.setContent(R.id.tab2);
		spec2.setIndicator("Current Questions");

		th.addTab(spec2);

		// TabHost.TabSpec spec2 = tabs.newTabSpec("tag2");
		//
		//
		// tabs.addTab(spec2);

		question = (TextView) findViewById(R.id.Question);
		msg = (EditText) findViewById(R.id.etMsg);
		msg.setHint("QuestionID");
		getQuestionButton = (Button) findViewById(R.id.bSend);
		// convo = (TextView) findViewById(R.id.tvConvo);
		status = (TextView) findViewById(R.id.tvStatus);
		msg.setText("test.gift");
		getQuestionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					String questionID = msg.getText().toString();
					String[] args = { "0", studentID, questionID };
					Vector<String> unparsedQuestions = new RequestQuestionTask()
							.execute(args).get();
					for (String line : unparsedQuestions) {
						question.setText(question.getText().toString() + "\n"
								+ line);
					}
					QuestionSheet questionSheet = parseQuestionSheet(unparsedQuestions);
					buildQuestion(questionSheet.getQuestions().firstElement());
					// status.setText(unparsedQuestion.firstElement());
					// convo.setText(new RequestQuestionTask().execute(
					// msg.getText().toString()).get());
				} catch (Exception ee) {
					// if we have a problem, simply return null return null;
				}

			}

		});

	}

	private QuestionSheet parseQuestionSheet(Vector<String> unparsedQuestions) {
		QuestionSheet questionSheet = new QuestionSheet();
		String line;
		Iterator<String> it = unparsedQuestions.iterator();
		if (it.hasNext()) {
			line = it.next();
			if (!line.startsWith("qID="))
				return null;
			questionSheet.setqID(line.substring(line.indexOf("=" + 1)));
			line = it.next();
			if (!line.startsWith("count="))
				return null;
			int count = Integer.parseInt(line.substring(line.indexOf("=" + 1)));
			Vector<Question> questions = new Vector<Question>();
			for (int i = 0; i < count; i++) {
				Question question = parseQuestion(it);
				if (question == null)
					return null;
				questions.add(question);
			}
			questionSheet.setQuestions(questions);
		}
		return questionSheet;
	}

	private Question parseQuestion(Iterator<String> it) {
		Question question = new Question();
		String line = it.next();
		if (!line.startsWith("q="))
			return null;
		question.setQuestionString(line.substring(line.indexOf("=" + 1)));
		line = it.next();
		if (!line.startsWith("count="))
			return null;
		int count = Integer.parseInt(line.substring(line.indexOf("=" + 1)));
		HashMap<Integer, String> answers = new HashMap<Integer, String>();
		for (int i = 0; i < count; i++) {
			line = it.next();
			answers.put(Integer.parseInt(line.substring(
					0, line.indexOf("="))),line.substring(line.indexOf("=" + 1)));
		}
		question.setAnswers(answers);
		return question;
	}

	private void buildQuestion(Question question) {
		spec2.setContent(R.layout.question_view);
		th.addTab(spec2);
	}
}