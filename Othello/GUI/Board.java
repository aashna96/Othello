package Othello;


/*Board extends javax.swing.JComponent, which makes Board a Swing component. As such, you can directly 
 * add a Boardcomponent to a Swing application’s content pane.

 Board declares SQUAREDIM and BOARDDIM constants that identify the pixel dimensions of a square and the 
 checkboard. When initializing SQUAREDIM, I invoke Checker.getDimension() instead of accessing an 
 equivalent public Checker constant. Joshua Block answers why I do this in Item #30 (Use enums 
 instead of int constants) of the second edition of his book, Effective Java: “Programs that use the int 
 enum pattern are brittle. Because int enums are compile-time constants, they are compiled into the 
 clients that use them. If the int associated with an enum constant is changed, its clients must be 
 recompiled. If they aren’t, they will still run, but their behavior will be undefined.”

 Because of the extensive comments, I haven’t much more to say about Board. However, note the nested
 PosCheck class, which describes a positioned checker by storing a Checker reference and its center 
 coordinates, which are relative to the upper-left corner of the Board component. When you add a Checker 
 object to the Board, it’s stored in a new PosCheck object along with the center position of the checker, 
 which is calculated from the specified row and column. 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;


public class Board extends JComponent {

	// dimension of each othello board square (25% bigger than checker)
	private final static int SQUAREDIM = (int) (Checker.getDimension() * 1.25);

	// dimension of othello board (width of 8 squares & height of 8+1 squares)
	private final int BOARDDIM = 8 * SQUAREDIM;
	
	//variables to keep count of number of checkers of each player
	private int player1, player2;
	
	// array list to store where all valid moves are there
	private ArrayList<Square> validMoves;

	// variable to keep mark of which players turn it is
	private int turn;

	// preferred size of Board component
	private Dimension dimPrefSize;

	// list of all squares contained
	private ArrayList<Square> allSquares;
	
	// count of all checkers on board
	private int countCheckers;

	public Board() {
		dimPrefSize = new Dimension(BOARDDIM, BOARDDIM + SQUAREDIM);
		allSquares = new ArrayList<>();
		countCheckers = 0;
		turn = 0;
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				allSquares.add(new Square(i, j));
		player1 = player2 = 2;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {

				// Obtain mouse coordinates at time of press.
				int x = me.getX();
				int y = me.getY();

				for (Square s: allSquares)
					if (thisSquare(x, y, s.cx, s.cy)) {
						if(s.checker != null)	return;
						boolean b = isMoveValid(s.rowNum, s.colNum);
						if(b == false)	return;
						addChecker(s.rowNum, s.colNum);
						if(turn == 0){ 
							turn = 1;
							player1 += 1;
						}
						else {
							turn = 0;
							player2 += 1;
						}
						countCheckers += 1;
						if(countCheckers == 64){
							printResult();
						}
						repaint();
						
						// Check if next user has a valid move if "NO" then change turn to this user & check if this 
						// user has valid move. If next user has valid move then just change turn to it.
						validMoves = getValidMoves();
						if(validMoves.size() != 0)	return;
						if(turn == 0)	turn = 1;
						else	turn = 0;
						
						// If next user doesn't have valid move then check if this user has valid move
						// If 'yes' just shuffle the turns
						// If 'No' then declare result
						validMoves = getValidMoves();
						if(validMoves.size() > 0)
							repaint();
						else 
							printResult();
					}
			}
		});
	}

	// Print the result as per who has maximum number of checkers
	private void printResult(){
		String message;
		if(player1 > player2)
			message = "Player 1 wins!!";
		else if(player1 < player2)
			message = "Player 2 wins!!";
		else
			message = "Game draw!!";
		JOptionPane.showMessageDialog(this,
			    message,
			    "Result",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	// Returns all valid moves of player whose turn it is
	private ArrayList<Square> getValidMoves(){
		ArrayList<Square> myValidMoves = new ArrayList<>();
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				Checker checker = allSquares.get(i*8+j).checker;
				if(checker == null && isMoveValid(i, j))
						myValidMoves.add(allSquares.get(i*8+j));
				
			}
		}
		return myValidMoves;
	}
	
	// Tells if the move of current player is valid at position i, j
	private boolean isMoveValid(int x, int y) {
		int XDIR[] = {-1, -1, 0, 1, 1,  1,  0, -1};
		int YDIR[] = { 0,  1, 1, 1, 0, -1, -1, -1};
		for(int k = 0; k < XDIR.length; k++){
			int xStep = XDIR[k] ,yStep = YDIR[k];
			int curX = x + xStep , curY = y + yStep;
			CheckerType symbol;
			if(turn == 0) symbol = CheckerType.RED;
			else symbol = CheckerType.GREEN;
			boolean anotherSymbol = false;
			while(curX >=0 && curX < 8 && curY >=0 && curY < 8){
				int index = curX * 8 + curY;
				if(allSquares.get(index).checker == null) break;
				CheckerType c = allSquares.get(index).checker.getCheckerType();
				if(c != symbol)
					anotherSymbol = true;
				if(c == symbol && anotherSymbol)
					return true;
				curX += xStep;
				curY += yStep;
			}
		}
		return false;
	}

	// Add checker of current user to mentioned position and flips other checkers according to that
	private void addChecker(int rowNum, int colNum) {
		Checker c;
		if(turn == 0) c = new Checker(CheckerType.RED);
		else c = new Checker(CheckerType.GREEN);
		add(c, rowNum+1, colNum+1);

		CheckerType n;
		if(turn == 0) n = CheckerType.RED;
		else n = CheckerType.GREEN;

		int XDIR[] = {-1, -1, 0, 1, 1,  1,  0, -1};
		int YDIR[] = { 0,  1, 1, 1, 0, -1, -1, -1};
		for(int k=0; k<XDIR.length; k++){
			int xStep = XDIR[k] ,yStep = YDIR[k];
			int lastMeX = rowNum, lastMeY = colNum;
			CheckerType symbol = allSquares.get(rowNum*8 + colNum).checker.getCheckerType();
			int currentX = rowNum + xStep , currentY = colNum + yStep;
			boolean exist = false;
			while(currentX >=0 && currentX < 8 && currentY >=0 && currentY < 8){
				int index = currentX * 8 + currentY;
				if(allSquares.get(index).checker == null)	break;
				if(allSquares.get(index).checker.getCheckerType() == symbol){
					lastMeX = currentX;
					lastMeY = currentY;
				}
				else 
					exist = true;
				currentX += xStep;
				currentY += yStep;
			}
			currentX = rowNum + xStep;
			currentY = colNum + yStep;
			if(exist){
				if(xStep >= 0 && yStep >= 0)
					for(int i=currentX, j=currentY; i<=lastMeX && j<=lastMeY; i+=xStep, j+=yStep ){
						int index = currentX * 8 + currentY;
						CheckerType color = allSquares.get(index).checker.getCheckerType();
						if(color != n){
							if(turn == 0){ 
								player1 += 1;
								player2 -= 1;
							}
							else {
								player2 += 1;
								player1 -= 1;
							}
						}
						add(new Checker(n), i+1, j+1);
					}
				else if(xStep >= 0)
					for(int i=currentX, j=currentY; i<=lastMeX && j>=lastMeY; i+=xStep, j+=yStep ){
						int index = currentX * 8 + currentY;
						CheckerType color = allSquares.get(index).checker.getCheckerType();
						if(color != n){
							if(turn == 0){ 
								player1 += 1;
								player2 -= 1;
							}
							else {
								player2 += 1;
								player1 -= 1;
							}
						}
						add(new Checker(n), i+1, j+1);
					}
				else if(yStep >= 0)
					for(int i=currentX, j=currentY; i>=lastMeX && j<=lastMeY; i+=xStep, j+=yStep ){
						int index = currentX * 8 + currentY;
						CheckerType color = allSquares.get(index).checker.getCheckerType();
						if(color != n){
							if(turn == 0){ 
								player1 += 1;
								player2 -= 1;
							}
							else {
								player2 += 1;
								player1 -= 1;
							}
						}
						add(new Checker(n), i+1, j+1);
					}
				else
					for(int i=currentX, j=currentY; i>=lastMeX && j>=lastMeY; i+=xStep, j+=yStep ){
						int index = currentX * 8 + currentY;
						CheckerType color = allSquares.get(index).checker.getCheckerType();
						if(color != n){
							if(turn == 0){ 
								player1 += 1;
								player2 -= 1;
							}
							else {
								player2 += 1;
								player1 -= 1;
							}
						}
						add(new Checker(n), i+1, j+1);
					}
			}
		}
	}

	// Add new checker to allSquares
	public void add(Checker checker, int row, int col) {
		if (row < 1 || row > 8)
			throw new IllegalArgumentException("row out of range: " + row);
		if (col < 1 || col > 8)
			throw new IllegalArgumentException("col out of range: " + col);
		allSquares.get((row-1)*8 + col-1).checker = checker;
	}

	@Override
	public Dimension getPreferredSize() {
		return dimPrefSize;
	}

	@Override
	protected void paintComponent(Graphics g) {
		paintCheckerBoard(g);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Checker cur = allSquares.get(row*8 + col).checker;
				int cx = allSquares.get(row*8 + col).cx;
				int cy =  allSquares.get(row*8 + col).cy;
				if(cur != null)
					cur.draw(g, cx, cy);
			}
		}
		if(turn == 0){
			g.setColor(Color.RED);
			g.drawString("Player 1 turn!!!", SQUAREDIM*2, BOARDDIM + SQUAREDIM/2);
		}
		else{
			g.setColor(Color.GREEN);
			g.drawString("Player 2 turn!!!", SQUAREDIM*2, BOARDDIM + SQUAREDIM/2);
		}
	}

	// Paints the checker board
	private void paintCheckerBoard(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Paint checkerboard.
		for (int row = 0; row < 8; row++) {
			g.setColor(((row & 1) != 0) ? Color.BLACK : Color.WHITE);
			for (int col = 0; col < 8; col++) {
				g.fillRect(col * SQUAREDIM, row * SQUAREDIM, SQUAREDIM, SQUAREDIM);
				g.setColor((g.getColor() == Color.BLACK) ? Color.WHITE : Color.BLACK);
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.fillRect(0, SQUAREDIM*8, SQUAREDIM * 8, SQUAREDIM);
	}

	// Check if x and y exist in mentioned square with center coordinates at cx and cy
	public static boolean thisSquare(int x, int y, int cx, int cy) {
		return (cx - x) * (cx - x) + (cy - y) * (cy - y) < SQUAREDIM / 2 * SQUAREDIM / 2;
	}

	private class Square{
		public int rowNum, colNum;
		public int cx, cy;
		public Checker checker;
		Square(int i, int j){
			rowNum = i;
			colNum = j;
			cx = i * SQUAREDIM + SQUAREDIM / 2;
			cy = j * SQUAREDIM + SQUAREDIM / 2;
			checker = null;
		}
	}
}