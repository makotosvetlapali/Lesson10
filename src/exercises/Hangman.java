package exercises;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import examples.FileHelper;

public class Hangman extends KeyAdapter {

	Stack<String> puzzles = new Stack<String>();
	ArrayList<JLabel> boxes = new ArrayList<JLabel>();
	int lives = 9;
	int correctBox = 0;

	JLabel livesLabel = new JLabel("" + lives);

	List<String> list = FileHelper.loadFileContentsIntoArrayList("resource/words.txt");

	public static void main(String[] args) {
		Hangman hangman = new Hangman();
		hangman.addPuzzles();
		hangman.createUI();
	}

	private void addPuzzles() {
//		puzzles.push("defenestrate");
//		puzzles.push("fancypants");
//		puzzles.push("elements");

		for (int i = 0; i < list.size(); i++) {
			puzzles.push(list.get(i));
		}

	}

	JPanel panel = new JPanel();
	private String puzzle;

	private void createUI() {
		// playDeathKnell();
		JFrame frame = new JFrame("June's Hangman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(livesLabel);

		loadNextPuzzle();
		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
		frame.addKeyListener(this);
	}

	private void loadNextPuzzle() {

		removeBoxes();
		lives = 9;
		livesLabel.setText("" + lives);
		// puzzle = puzzles.pop();
		int index = new Random().nextInt(list.size());
		puzzle = list.get(index);
		System.out.println("puzzle is now " + puzzle);
		System.out.println(puzzle.length());
		createBoxes();

	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println(arg0.getKeyChar());
		updateBoxesWithUserInput(arg0.getKeyChar());
		if (lives == 0) {

			playDeathKnell();
			System.out.println("Sorry,you lose! Try again.");
			correctBox = 0;
			stopMusic();
			loadNextPuzzle();

		}
		if (correctBox == puzzle.length()) {
			System.out.println("Congratulations, you win. Play again.");
			correctBox = 0;

			loadNextPuzzle();

		}

	}

	private void updateBoxesWithUserInput(char keyChar) {

		boolean gotOne = false;
		for (int i = 0; i < puzzle.length(); i++) {
			if ((Character.toLowerCase(puzzle.charAt(i)) == Character.toLowerCase(keyChar)
					|| Character.toLowerCase(puzzle.charAt(i)) == Character.toUpperCase(keyChar))
					&& boxes.get(i).getText().toString().equals("_")) {

				boxes.get(i).setText("" + keyChar);
				gotOne = true;
				correctBox++;

			}
		}
		if (!gotOne)
			livesLabel.setText("" + --lives);

	}

	void createBoxes() {
		for (int i = 0; i < puzzle.length(); i++) {
			JLabel textField = new JLabel("_");
			boxes.add(textField);
			panel.add(textField);
		}
	}

	void removeBoxes() {
		for (JLabel box : boxes) {
			panel.remove(box);
		}
		boxes.clear();
	}

	public void playDeathKnell() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resource/funeral-march.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			Thread.sleep(8400);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stopMusic() {

		return;
	}

}
