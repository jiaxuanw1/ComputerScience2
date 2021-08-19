package fractals;

import java.util.concurrent.TimeUnit;

import util.Turtle;

public class TreeFractal extends Turtle {

    private int min = 4;

    public static void main(String[] args) {
        TreeFractal fractal = new TreeFractal();
        fractal.drawTree(150);
    }

    /**
     * Draws a branch with the specified direction and length, with all the
     * sub-branches to create the tree fractal.
     * 
     * @param degrees the number of degrees to rotate counterclockwise by
     * @param length  the length of the branch to draw, in pixels
     */
    public void drawBranch(int degrees, int length) {
        // Base case - minimum branch length has been reached, so the branch is drawn as
        // one line segment without sub-branches
        if (length <= min) {
            // Wait 5 ms between each stroke
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            paint(degrees, length);
            move(0, -length);
            return;
        }

        // Draw the stem in the specified direction
        paint(degrees, length);
        // Draw a shorter branch angled left
        drawBranch(35, 7 * length / 10);
        // Draw a shorter branch angled right
        drawBranch(-70, 7 * length / 10);
        // Return to starting position
        move(35, -length);
    }

    /**
     * Draws a tree fractal to the screen with a specified size.
     * 
     * @param size the starting length of the stem, in pixels
     */
    public void drawTree(int size) {
        // Move turtle into position so the fractal is in frame and pointing north
        move(90, -250);
        drawBranch(0, size);
    }

}
