package fractals;

import util.Turtle;

public class KochSnowflake extends Turtle {

    private final int min = 4;

    public static void main(String[] args) {
        KochSnowflake koch = new KochSnowflake();
        koch.drawSnowflake(500);
    }

    /**
     * Draws one side (line segment) according to the specified direction and
     * length, with all the subdivisions to create the fractal.
     * 
     * @param degrees the number of degrees to rotate counterclockwise by
     * @param length  the length of the line segment to draw, in pixels
     */
    public void drawSide(int degrees, int length) {
        // Base case - minimum side length has been reached, so the entire line segment
        // is drawn with no additional equilateral triangles
        if (length <= min) {
            paint(degrees, length);
            sleep(1);
            return;
        }

        // Split line segment into the 4 sub-segments needed to draw an equilateral
        // triangle in the inner third. Recursively calls this method for each
        // sub-segment to be divided as needed.
        drawSide(degrees, length / 3);
        drawSide(60, length / 3);
        drawSide(-120, length / 3);
        drawSide(60, length / 3);
    }

    /**
     * Draws a Koch Snowflake fractal to the screen with a specified size.
     * 
     * @param size the starting side length of each side, in pixels
     */
    public void drawSnowflake(int size) {
        // Move turtle into position so the fractal is in frame
        move(180, 250); // west
        move(90, 130); // south
        move(-90, 0); // face west

        // First stage is an equilateral triangle
        for (int i = 0; i < 3; i++) {
            drawSide(-120, size);
        }
    }

}
