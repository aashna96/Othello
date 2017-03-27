import java.awt.*;

// Frame for the game Othello. The frame contains the game
// board and necessary buttons and information fields.
public class Othello extends Frame {

// Components that go in the frame
	OthelloSquare[][] board; // Will contain the 8 x 8 game board
	Label whitesLabel= new Label("white pieces ",Label.CENTER);
	Label blacksLabel= new Label("black pieces ",Label.CENTER);
	Label whites= new Label("0",Label.CENTER); // Number of white pieces on board
	Label blacks= new Label("0",Label.CENTER); // Number of black pieces on board
	Label whoPlays= new Label("White to play ",Label.LEFT);
	Label cursorKind= new Label("() ",Label.LEFT);
	TextArea helpText= null; // To contain an explanation, when requested
// Titles for buttons
	private final String[] buttons= {"New game", "Quit", "Help"};

// The layout and constraint variables for this frame
	GridBagLayout gbl;
	GridBagConstraints gbc;

// The x and y "weights", which indicate how much the components should be
// resized if the window is resized. 0 means not at all, 100 a lot. These weights
// are used in a relative fashion -if xweight is 1 for component A and 100 for
// component B, then when a frame is made bigger, component A gets 100 times more
// of the extra space than B. We set all weights to 100, so they all increase equally. 
	final int xweight= 100;
	final int yweight= 100;

final int bdSize= 8; //The board is bdSize x bdSize
	
int whiteCount= 0; 	// Number of white pieces on the board
int blackCount= 0; 	// Number of black pieces on the board

// These variables have values OthelloSquare.WHITE or OthelloSquare.BLACK
	int pieceToPlay; 	// piece to place next
	int pieceToTurn;	// piece to turn over if pieceToPlay is placed

// Each of the eight pairs (horiz[d], vertic[d]) gives a direction
// on the board.  E.g. For a square board[col][row],
// board[col+horiz[d]][row+vertic[d]] is its neighbor in direction d.
	private final int[] horiz= {-1, 0, 1, 1, 1, 0,-1,-1}; 
	private final int[] vertic={-1,-1,-1, 0, 1, 1, 1, 0};

// Constructor: An bdSize x bdSize Othello board, with three buttons and some
// pieces of information
public Othello() {
	super("Othello");
	gbl= new GridBagLayout();
	gbc= new GridBagConstraints();
	setFont(new Font("Dialog", Font.PLAIN, 10));
	setLayout(gbl);
	int i;
	int j;
	
	// Create and place the bdSize*bdSize squares in the frame
		gbc.fill  =  GridBagConstraints.BOTH;
		board= new OthelloSquare[bdSize][bdSize];
		for (i= 0; i != bdSize; i= i+1)
			for (j= 0; j != bdSize; j= j+1) {
				board[i][j]= new OthelloSquare(i,j);
				add(board[i][j], gbl, gbc, 2*i,2*j, 2,2, xweight,yweight);
				}
	
	// Create and place the buttons given by String buttons. Setting gbc.fill
	// to BOTH ensures that the buttons fill their entire space; without this
	// assignment, buttons would take the minimum space possible. Change BOTH
	// to NONE and execute to see this.
		gbc.fill=  GridBagConstraints.BOTH;	
		for (j= 0; j != buttons.length; j= j+1)
			add(new Button(buttons[j]), gbl, gbc, 2*bdSize,2*j, 2,2, xweight,yweight);
	
	// Place labels for the numbers of white and black pieces
		gbc.fill=  GridBagConstraints.HORIZONTAL;
		gbc.anchor= GridBagConstraints.SOUTHWEST;
		add(whitesLabel, gbl, gbc, 2*bdSize,2*4,   2,1, xweight,yweight);
		add(blacksLabel, gbl, gbc, 2*bdSize,2*5,   2,1, xweight,yweight);
		gbc.anchor= GridBagConstraints.NORTHWEST;
		add(whites,      gbl, gbc, 2*bdSize,2*4+1, 2,1, xweight,yweight);
		add(blacks,      gbl, gbc, 2*bdSize,2*5+1, 2,1, xweight,yweight);
	// Place the "whose play is it" and cursor label
		add(whoPlays,    gbl, gbc, 2*bdSize,2*3,   2,1, xweight,yweight);
		add(cursorKind,  gbl, gbc, 2*bdSize,2*3+1, 2,1, xweight,yweight);
	
	pack();
	move(150,50);
	newGame();
	show();
	//setResizable(false);
	}

// Add component c to gbl with constraints gbc at position (x,y).
// Component c takes w columns and r rows, and is weighted (wx, wy).
private void add(Component c, GridBagLayout gbl,
				GridBagConstraints gbc,
				int x, int y, int w, int h, int wx, int wy)
	{gbc.gridx= x;
	gbc.gridy= y;
	gbc.gridwidth= w;
	gbc.gridheight= h;
	gbc.weightx= wx;
	gbc.weighty= wy;
	gbl.setConstraints(c, gbc);
	add(c);
	}
	
// If a button was pressed, process it; otherwise, return false
public boolean action(Event e, Object arg) {
	if (arg.equals(buttons[0])) {
		// Handle press of "New game" and return true
			newGame(); 
			return true;
		}
	if (arg.equals(buttons[1])) {
		// Handle press of "Quit" and return true
			dispose();
			return true;
		}
	if (arg.equals(buttons[2])) {
		// Handle press of "Help" and return true
			if (helpText != null) {
				// Help field already exists, so remove it and return
					remove(helpText);
					helpText= null;
					pack();
					return true;
					}
			helpText= new TextArea("The object of the game Othello is to win!" +
				"\n\nThe game gets its name from Shakespeare's Othello, who kept changing" +
				"\nfrom bad to good and back again --from white to black, back and forth." +
				"\n\nThe game starts as given when you press \"New game\". There are two" +
				"\nplayers: white and black. White goes first, by placing a piece in some" +
				"\nempty square (do this by clicking the mouse in the square in which you" +
				"\n want to place a piece). However, you can't place a white piece in just" +
				"\nany square, but only in one that satisfies the following condition:" +
				"\n\n     In at least one direction (vertical, horizontal, or diagonal) emanating" +
				"\n     from the square, there is a sequence of one or more black pieces" +
				"\n     followed by a white piece." +
				"\n\nWhen a white piece is placed in such a square, all the black pieces" +
				"\nmentioned in this condition become white --in all directions. Try it!" +
				"\n\nAfter a white piece is placed, the other player places a black piece in the" +
				"\nsame manner (with the roles of white and black interchanged). Then it's" +
				"\nwhite's turn again, an so it goes." +
				"\n\nA player who cannot place a piece loses their turn. If no player can play," +
				"\nthe game is over, and the player with the most pieces wins.");
			helpText.setEditable(false);
			gbc.fill= GridBagConstraints.BOTH;
			add(helpText, gbl, gbc, 0,2*bdSize+1, 2*bdSize+2,1, xweight,yweight);
			resize(400,400);
			pack();
			return true;
		}
	return false;
	}

// Process press of WINDOW_DESTROY or mouse up in an Othello square
public boolean handleEvent(Event e) {
	//System.out.println("" + e);
	if (e.id == Event.WINDOW_DESTROY) {
		dispose();
		return true;
		}
	if (e.id == Event.MOUSE_UP &&
		e.target instanceof OthelloSquare) {
		processSquareClick((OthelloSquare)e.target);
		return true;
		}
	return super.handleEvent(e);
	}
	
// Set the board up for a new game
public void newGame() {
	// Pick up the pieces from all the squares
		for (int i= 0; i != bdSize; i= i+1)
			for (int j= 0; j != bdSize; j= j+1)
				pickUp(i,j);
	// Place the first four pieces
		putDown(OthelloSquare.WHITE, bdSize/2-1, bdSize/2-1);
		// YOU FILL IN CODE to place the other three pieces


		// END OF YOU FILL IN CODE

	//Set pieceToPlay and pieceToTurn so that it is Black's turn to play
		// YOU FILL IN CODE to set the pieces


		// END OF YOU FILL IN CODE
	changeTurn();
	}

// Store whiteCount and blackCount in their respective textFields
private void setCounts() {
	whites.setText(String.valueOf(whiteCount) + "  ");
	blacks.setText(String.valueOf(blackCount) + "  ");
	}

// Process a click in square sq
public void processSquareClick(OthelloSquare sq) {
if (sq.contents() != OthelloSquare.EMPTY) {
		// Assertion: Square already occupied
		return;
		}
	
	// Set numToTurn to the number of pieces that can be turned
	// over if a piece x is placed in square sq
		int numToTurn= 0;
		for (int d= 0; d != 8; d= d+1) {
			numToTurn= numToTurn + numberToTurn(d, sq.col, sq.row);
			}

	if (numToTurn == 0) {
		// Can't place piece here; return
			return;
		}
	
	// A piece can be turned; turn pieces over in each of the 8 directions
		for (int d= 0; d != 8; d= d+1) {
			if (numberToTurn(d, sq.col, sq.row) > 0)
				turnOver(d, sq.col, sq.row);
			}
	
	putDown(pieceToPlay, sq.col, sq.row);
	
	changeTurn();
	if (cannotPlay())
		changeTurn();
	if (cannotPlay())
		whoPlays.setText("Game over");
	}
	
// Yield "player pieceToPlay cannot make a play
// (because it cannot turn over a piece)
public boolean cannotPlay() {
	// For each empty board position, return false if
	// there is a direction for which numberToTurn > 0
		for (int col= 0; col != bdSize; col= col+1)
			for (int row= 0; row != bdSize; row= row+1)
				if (board[col][row].contents() == OthelloSquare.EMPTY) {
					// If a piece can be turned over in any of the 8
					// directions when a piece is played on board[col,row],
			        // return false.
					// YOU FILL IN CODE to do this


					// END OF YOU FILL IN CODE
					}
	return true;
	}
	
// Switch pieceToPlay and pieceToTurn frame
public void changeTurn() {
	// Swap pieceToPlay and pieceToTurn
		int temp= pieceToPlay;
		pieceToPlay= pieceToTurn;
		pieceToTurn= temp;
	if (pieceToPlay == OthelloSquare.WHITE) {
		whoPlays.setText("White to play");
		cursorKind.setText("(crosshair)");
		setCursor(CROSSHAIR_CURSOR);
		}
	else {
		whoPlays.setText("Black to play");
		cursorKind.setText("(resize cursor)");
		setCursor(N_RESIZE_CURSOR);
		}
	}
	
// Yield the number of pieces that could be turned over in
// direction d if a piece pieceToPlay were placed on board[col][row]
public int numberToTurn(int d, int col, int row) {
	int adjcol= col + horiz[d];
	int adjrow= row + vertic[d];
	int numy= 0;
	
	// invariant: square board[adjcol][adjrow] is in direction d
	//    from board[col][row] (but it may be off the board). All the pieces
	//	  strictly between the two board positions board[col][row] and
	//    board[adjcol][adjrow] are pieceToTurn pieces, and there are numy of them
	while (onBoard(adjcol,adjrow) &&
		   board[adjcol][adjrow].contents() == pieceToTurn) {
		// increase numy while keeping the invariant true
			// YOU FILL IN CODE for this



			// END OF YOU FILL IN CODE 
		}
	if ((!onBoard(adjcol,adjrow)) ||
		board[adjcol][adjrow].contents() != pieceToPlay)
		numy = 0;
	return numy;
	}
	
// At least one piece can be turned over in direction d, beginning at the
// position adjacent to board[col][row]. Turn them all over (in that direction).
public void turnOver(int d, int col, int row) {
	int adjcol= col + horiz[d];
	int adjrow= row + vertic[d];
	
	// invariant: square board[adjcol][adjrow] is in direction d
	//    from board[col][row], and all pieces strictly between the two
	//    board positions board[col][row] and board[adjcol][adjrow] were
	//    pieceToTurn and have been turned over
	while (board[adjcol][adjrow].contents() == pieceToTurn) {
		pickUp(adjcol,adjrow);
		putDown(pieceToPlay,adjcol, adjrow);
		adjcol= adjcol + horiz[d];
		adjrow= adjrow + vertic[d];
		}
	}
	
// Pick up the piece on board[col][row] --if there is a piece there
// --but maintain all the counts
public void pickUp(int col, int row) {
	if (board[col][row].contents() == OthelloSquare.EMPTY)
		return;
	if (board[col][row].contents() == OthelloSquare.WHITE)
		whiteCount= whiteCount-1;
	else
		blackCount= blackCount-1;
	board[col][row].pickUpPiece();
	setCounts();
	}
	
// board[col, row] is empty. Place piece p there (p is either
// Othello.WHITE or Othello.BLACK) --maintain all counts
public void putDown(int p, int col, int row) {
	// YOU FILL IN CODE for this



	// END OF YOU FILL IN CODE
	}
				
// Yield "board[col][row] is a valid square of the board"
public boolean onBoard(int col, int row) {
	return  0 <= col && col < bdSize && 0 <= row && row < bdSize;
	}

}

