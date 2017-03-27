package Othello;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Checkers extends JFrame {
	public Checkers(String title){
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Board board = new Board();
		board.add(new Checker(CheckerType.GREEN), 4, 4);
		board.add(new Checker(CheckerType.RED), 4, 5);
		board.add(new Checker(CheckerType.RED), 5, 4);
		board.add(new Checker(CheckerType.GREEN), 5, 5);
		setContentPane(board);
		pack();
		setVisible(true);
	}

	public static void main(String[] args){
		Runnable r = new Runnable() {
			@Override
			public void run() {
				new Checkers("OTHELLO");
			}
		};
		EventQueue.invokeLater(r);
	}
}
