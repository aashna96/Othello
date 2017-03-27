package OTHELLO;

import java.util.Scanner;

public class Game {
	
	public static void main(String args[]) throws InvalidInputException{
		start();
	}

	private static void start() throws InvalidInputException {
		
		Player p1 = takePlayerInput(1);
		Player p2 = takePlayerInput(2);
		while(p1.symbol == p2.symbol){
			System.out.println("Symbol already taken !!!");
			p2 = takePlayerInput(2);
		}
		Scanner s = new Scanner(System.in);
		int x;
		int y;
		Board b = new Board(p1.symbol, p2.symbol);
		boolean player1turn = true;
		while(b.gameStatus() == 4){
			if(player1turn){
				System.out.println("Player "+p1.name+" turn ");
				System.out.println("Enter x :");
				x = s.nextInt();
				System.out.println("Enter y :");
				y = s.nextInt();
				b.showValidMove(p1.symbol, x, y);
				player1turn = false;
			}
			else{

				System.out.println("Player "+p1.name+" turn ");
				System.out.println("Enter x :");
				x = s.nextInt();
				System.out.println("Enter y :");
				y = s.nextInt();
				b.showValidMove(p2.symbol, x, y);
				player1turn = true;
			}
			b.printboard();
		}
		
	}
	
	private static Player takePlayerInput(int i) {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter player "+i+" name ");
		String name = s.nextLine();
		System.out.println("Enter player "+i+" symbol ");
		char symbol = s.nextLine().charAt(0);
		Player p = new Player(name, symbol);
		return p;
	}

}
