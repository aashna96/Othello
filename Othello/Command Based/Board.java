package OTHELLO;

public class Board {
	
	char Player1Symbol;
	char Player2Symbol;
	char board[][];
	private static final int boardSize = 8;
	
	Board(char Player1Symbol, char Player2Symbol){
		this.Player1Symbol = Player1Symbol;
		this.Player2Symbol = Player2Symbol;
		board = new char[boardSize][boardSize];
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				board[i][j] = ' ';
			}
		}
		board[3][3] = Player1Symbol;
		board[4][4] = Player1Symbol;
		board[4][3] = Player2Symbol;
		board[3][4] = Player2Symbol;
	}

	public int gameStatus() {
		// TODO Auto-generated method stub
		return 4;
	}

	public boolean showValidMove(char symbol, int x, int y) throws InvalidInputException {
		if( x<0 || x>boardSize || y<0 || y>boardSize || board[x][y] != ' '){
			InvalidInputException e = new InvalidInputException();
			throw e;
		}
		int XDIR[] = {-1, -1, 0, 1, 1,  1,  0, -1};
		int YDIR[] = { 0,  1, 1, 1, 0, -1, -1, -1};
		boolean  move = false;
		for(int k=0; k<XDIR.length; k++){
			int xStep = XDIR[k] ,yStep = YDIR[k];
			int currentX = x + xStep , currentY = y + yStep;
			boolean exist = false;
			while(currentX >=0 && currentX < boardSize && currentY >=0 && currentY < boardSize){
				if(board[currentX][currentY] == symbol){
					if(exist == true){
						move = true;
						if(xStep >= 0 && yStep >= 0){
							for(int i=x, j=y; i<=currentX && j<=currentY; i+=xStep, j+=yStep ){
								board[i][j] = symbol;
							}
						}
						else if(xStep >= 0){
							for(int i=x, j=y; i<=currentX && j>=currentY; i+=xStep, j+=yStep ){
								board[i][j] = symbol;
							}
						}
						else if(yStep >= 0){
							for(int i=x, j=y; i>=currentX && j<=currentY; i+=xStep, j+=yStep ){
								board[i][j] = symbol;
							}
						}
						else{
							for(int i=x, j=y; i>=currentX && j>=currentY; i+=xStep, j+=yStep ){
								board[i][j] = symbol;
							}
						}
					}
				}
				else if(board[currentX][currentY] == Player1Symbol || board[currentX][currentY] == Player2Symbol){
					exist = true;
				}
				else{
					break;
				}
				currentX += xStep;
				currentY += yStep;
			}
		}
		return move;
	}

	public void printboard() {
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				System.out.print("  "+board[i][j]+ "  ");
			}
			System.out.println("\n");
		}
	}

}
