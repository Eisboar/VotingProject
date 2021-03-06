package com.example.votingapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import com.example.votingapp.data.Question;
import com.example.votingapp.data.QuestionSheet;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private Scanner scanner = new Scanner(System.in);
	Toast toast;
	// DATA
	private final String studentID = "10000";

	// GUI
	private EditText msg;
	private Button getQuestionButton;
	// TextView convo;
	private LayoutInflater mInflater;
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

				QuestionSheet questionSheet = null;
				// if (questionSheet == null)

				try {
					String questionID = msg.getText().toString();
					String[] args = { "0", studentID, questionID };
					Vector<String> unparsedQuestions = new RequestQuestionTask()
							.execute(args).get();
					for (String line : unparsedQuestions) {
						question.setText(question.getText().toString() + "\n"
								+ line);
					}

					questionSheet = parseQuestionSheet(unparsedQuestions);
					buildQuestion(questionSheet.getQuestions().firstElement());
					// status.setText(unparsedQuestion.firstElement());
					// convo.setText(new RequestQuestionTask().execute(
					// msg.getText().toString()).get());

				} catch (Exception ee) {
					// if we have a problem, simply return null return null;
				}

				// return;
				TabHost.TabSpec tspec = th.newTabSpec("Tab1");
				QuestionTab questionTab = new QuestionTab(questionSheet);
				tspec.setContent(questionTab);
				Resources res = getResources();
				tspec.setIndicator("Clock");
				th.addTab(tspec);
			}

		});

	}

	private QuestionSheet parseQuestionSheet(Vector<String> unparsedQuestions) {
		QuestionSheet questionSheet = new QuestionSheet();
		String line;
		Iterator<String> it = unparsedQuestions.iterator();
		if (it.hasNext()) {
			line = it.next();
			// Toast toast = Toast.makeText(MainActivity.this,line , 1);
			// toast.show();
			if (!line.startsWith("qID=")) {
				// Toast toast = Toast.makeText(MainActivity.this, "fuuu", 1);
				// toast.show();
				return null;
			}

			questionSheet.setqID(line.substring(line.indexOf("=") + 1));

			line = it.next();
			// Toast toast = Toast.makeText(MainActivity.this, line, 1);
			// toast.show();
			if (!line.startsWith("count="))
				return null;
			int count = Integer.parseInt(line.substring(line.indexOf("=") + 1));

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
		question.setQuestionString(line.substring(line.indexOf("=") + 1));
		// Toast toast = Toast.makeText(MainActivity.this,
		// question.getQuestionString(), 1);
		// toast.show();
		line = it.next();
		if (!line.startsWith("count="))
			return null;
		int count = Integer.parseInt(line.substring(line.indexOf("=") + 1));
		HashMap<Integer, String> answers = new HashMap<Integer, String>();
		for (int i = 0; i < count; i++) {
			line = it.next();
			answers.put(Integer.parseInt(line.substring(0, line.indexOf("="))),
					line.substring(line.indexOf("=") + 1));
		}
		question.setAnswers(answers);
		return question;
	}

	private void buildQuestion(Question question) {

	}

	class QuestionTab implements TabContentFactory {
		// private final View preExisting;
		private Vector<RadioButton> radioButtons;
		private QuestionSheet questionSheet;
		private Button sendAnswers;
		private Vector<Pair<RadioGroup, Integer>  > choiceFields;

		protected QuestionTab(QuestionSheet questionSheet) {
			this.questionSheet = questionSheet;
		}

		public View createTabContent(String tag) {
			ScrollView sv = new ScrollView(MainActivity.this);
			LinearLayout linearLayout = new LinearLayout(MainActivity.this);
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			choiceFields = new Vector<Pair<RadioGroup, Integer>>();
			for (Question question : questionSheet.getQuestions()) {
				TextView questionField = new TextView(MainActivity.this);
				questionField.setText(question.getQuestionString());
				linearLayout.addView(questionField);

				final RadioButton[] rb = new RadioButton[question.getAnswers()
						.size()];
				RadioGroup rg = new RadioGroup(MainActivity.this); // create the
																	// RadioGroup
				rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
				for (int i = 0; i < rb.length; i++) {
					String answerString = question.getAnswers().get(i);
					rb[i] = new RadioButton(MainActivity.this);
					rb[i].setText(answerString);
					rb[i].setId(i);
					Resources res = getResources();
					rg.addView(rb[i]); // the RadioButtons are added to the
				}
				choiceFields.add(new Pair<RadioGroup, Integer>(rg,question.getAnswers()
						.size()));
				linearLayout.addView(rg);
			}
			sendAnswers = new Button(MainActivity.this);
			sendAnswers.setText("send");
			linearLayout.addView(sendAnswers);
			sendAnswers.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Vector<String> unparsedCorrection;
					String[] args = buildAnswers();
					try {
						toast = Toast.makeText(MainActivity.this, "ha", 1);
						unparsedCorrection = new RequestQuestionTask().execute(
								args).get();
						for (String line : unparsedCorrection) {
							question.setText(question.getText().toString()
									+ "\n" + line);
						}

						// toast.show();
					} catch (Exception ee) {

						return;
						// if we have a problem, simply return null return null;
					}
					// Toast toast = Toast.makeText(MainActivity.this,"hio" ,
					// 1);
					// toast.show();
					int i = 0;
					for (Pair<RadioGroup, Integer> p : choiceFields) {
						int radioButtonID = p.first.getCheckedRadioButtonId();
						// toast = Toast.makeText(MainActivity.this,
						// Integer.toString(radioButtonID), 1);
						// toast.show();
						if (radioButtonID != -1) {
							if (radioButtonID != Integer
									.parseInt(unparsedCorrection.elementAt(i))) {
								RadioButton rb = (RadioButton) p.first
										.getChildAt(radioButtonID);
								rb.setBackgroundColor(Color.RED);
							}
						}
						RadioButton rb = (RadioButton) p.first
								.getChildAt(Integer
										.parseInt(unparsedCorrection.elementAt(i)));
						rb.setBackgroundColor(Color.GREEN);
						i++;
						for (int j=0; j<p.second; j++){
							RadioButton radiobutton = (RadioButton) p.first
									.getChildAt(j);
							radiobutton.setEnabled(false);
						}
					}

				}
			});

			sv.addView(linearLayout);
			return sv;
		}

		private String[] buildAnswers() {
			String results = "";
			for (Pair<RadioGroup, Integer> p : choiceFields) {
				int radioButtonID = p.first.getCheckedRadioButtonId();
				results += "\n" + Integer.toString(radioButtonID);
			}
			String[] args = { "1", studentID, questionSheet.getqID(), results };
			return args;
		}

	}

}