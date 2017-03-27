package Othello;

public class AlreadyOccupiedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyOccupiedException(String msg)
	{
		super(msg);
	}
}
