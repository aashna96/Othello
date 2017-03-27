package Othello;

/*
 * As with Board, there isn’t much to say about Checker. In the draw() method, expressions 
 * int x = cx - DIMENSION / 2; and int y = cy - DIMENSION / 2; convert from a checker’s center 
 * coordinates to the coordinates of its upper-left corner. These coordinates are required by the 
 * subsequent fillOval() and drawOval() method calls as their first two arguments. Also,contains() uses 
 * the Pythagorean theorem to help it determine if an arbitrary point (x, y) lies within the circle 
 * centered at (cx, cy) and having a radius of DIMENSION / 2. Essentially, the difference between these 
 * points is determined and used by the Pythagorean equation to determine if (x, y) lies within the 
 * circle.
 */

import java.awt.Color;
import java.awt.Graphics;

public final class Checker {
	private final static int DIMENSION = 50;

	private CheckerType checkerType;

	public Checker(CheckerType checkerType) {
		this.checkerType = checkerType;
	}

	public void draw(Graphics g, int cx, int cy) {
		int x = cx - DIMENSION / 2;
		int y = cy - DIMENSION / 2;

		// Set checker color.
		g.setColor(checkerType == CheckerType.GREEN ? Color.GREEN : Color.RED);

		// Paint checker.
		g.fillOval(x, y, DIMENSION, DIMENSION);
		g.setColor(Color.WHITE);
		g.drawOval(x, y, DIMENSION, DIMENSION);
	}

	public void drawHint(Graphics g, int cx, int cy) {
		int x = cx - DIMENSION / 2;
		int y = cy - DIMENSION / 2;

		// Set checker color.
		g.setColor(checkerType == CheckerType.GREEN_HINT ? Color.BLUE : Color.CYAN);

		// Paint checker.
		g.fillOval(x, y, DIMENSION, DIMENSION);
		g.setColor(Color.WHITE);
		g.drawOval(x, y, DIMENSION, DIMENSION);
	}
	
	public static boolean contains(int x, int y, int cx, int cy) {
		return (cx - x) * (cx - x) + (cy - y) * (cy - y) < DIMENSION / 2 * DIMENSION / 2;
	}

	// The dimension is returned via a method rather than by accessing the
	// DIMENSION constant directly to avoid brittle code. If the constant was
	// accessed directly and I changed its value in Checker and recompiled only
	// this class, the old DIMENSION value would be accessed from external 
	// classes whereas the new DIMENSION value would be used in Checker. The
	// result would be erratic code.

	public static int getDimension() {
		return DIMENSION;
	}
	public CheckerType getCheckerType() {
		return checkerType;
	}
}