package fractals;

import java.util.ArrayList;
import java.util.List;

import util.Turtle;

public class DragonCurve extends Turtle {

	public static void main(String[] args) {
		DragonCurve fractal = new DragonCurve();
		fractal.drawDragon(16, 1);
	}

	/**
	 * Draws a dragon curve to the screen with a specified side length and number of
	 * iterations.
	 * 
	 * @param iterations the number of iterations
	 * @param sideLength the length of each line segment, in pixels
	 */
	public void drawDragon(int iterations, int sideLength) {
		// Create a list to store the sequence of turns (as 0's and 1's)
		List<Integer> sequence = new ArrayList<Integer>();

		// For every iteration, add a 1 to the sequence, then add the reverse of the
		// previous sequence with 0's and 1's swapped.
		for (int i = 0; i < iterations; i++) {
			List<Integer> inverse = reverseInverseSequence(sequence);
			sequence.add(0);
			sequence.addAll(inverse);
		}

		// Move into position so the fractal is in frame
		move(90, -100);
		// 0's represent right turns and 1's represent left turns
		for (int turn : sequence) {
			paint(turn == 0 ? -90 : 90, sideLength);
		}
	}

	/**
	 * Reverses a specified sequence of 0's and 1's and inverts each number (so 0's
	 * become 1's and vice versa). Each element in the list must be a single digit
	 * (0 or 1).
	 * 
	 * @param sequence list containing the sequence of 0's and 1's
	 * @return list containing the reversed inverse sequence of 0's and 1's
	 */
	public List<Integer> reverseInverseSequence(List<Integer> sequence) {
		List<Integer> newSequence = new ArrayList<Integer>();
		for (int i = sequence.size() - 1; i >= 0; i--) {
			newSequence.add(sequence.get(i) == 0 ? 1 : 0);
		}
		return newSequence;
	}

}
