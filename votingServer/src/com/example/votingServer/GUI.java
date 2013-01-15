package com.example.votingServer;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.Action;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;

import com.example.votingServer.data.QuestionSheet;

public class GUI extends JFrame {
	/**
	 * 
	 */
	//current loaded QuestionSheets
	private Vector<QuestionSheet> questionSheets = new Vector<QuestionSheet>();
	
	private JTextPane textPane;
	
	public QuestionSheet getQuestionSheet(String title){
		for (QuestionSheet questionSheet: questionSheets){
			if (questionSheet.getTitle().equals(title))
				return questionSheet;
		}
		return null;
	}
	
	public GUI() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnGeneral = new JMenu("general");
		menuBar.add(mnGeneral);
		
		JMenuItem mntmOpenQuestion = new JMenuItem("open Question");
		mntmOpenQuestion.setAction(action);
		mnGeneral.add(mntmOpenQuestion);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("New tab", null, layeredPane, null);
		
		textPane = new JTextPane();
		textPane.setBounds(10, 11, 99, 190);
		layeredPane.add(textPane);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("New tab", null, layeredPane_1, null);
	}

	private static final long serialVersionUID = 1L;

	public void addText(String s){
		textPane.setText(textPane.getText()+s);
	}
	
	
	private JPanel contentPane;
	private final Action action = new SwingAction();
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
					Thread t = new Server(frame);
					t.start();
					//frame.addText("test");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "open Question");
			putValue(SHORT_DESCRIPTION, "Dialog to open a .gift-Question file");
		}
		public void actionPerformed(ActionEvent e) {
			final JFileChooser fc = new JFileChooser("questions/");
			fc.showOpenDialog(null);
            File file = fc.getSelectedFile();
            
            QuestionLoader questionLoader = new QuestionLoader(file);
            questionSheets.add(questionLoader.getQuestionSheet());
		}
	}
}