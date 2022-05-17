package fractals;

import java.awt.Color;

public class ColorLookupTable {

    private double[] positions;
    private Color[] colors;

    public ColorLookupTable(double[] positions, Color[] colors) {
        this.positions = positions;
        this.colors = colors;
    }

    public Color getColor(double pos) {
        int fin = -1;

        if (pos == positions[positions.length - 1]) {
            return colors[colors.length - 1];
        }

        for (int i = 0; i < colors.length; i++) {
            if (positions[i] > pos) {
                fin = i - 1;
                break;
            }
        }

        double lowerPos = positions[fin];
        double upperPos = positions[fin + 1];

        int lowerR = colors[fin].getRed();
        int upperR = colors[fin + 1].getRed();

        int lowerG = colors[fin].getGreen();
        int upperG = colors[fin + 1].getGreen();

        int lowerB = colors[fin].getBlue();
        int upperB = colors[fin + 1].getBlue();

        int interpolatedR = (int) (lowerR + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperR - lowerR)));
        int interpolatedG = (int) (lowerG + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperG - lowerG)));
        int interpolatedB = (int) (lowerB + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperB - lowerB)));

        return new Color(interpolatedR, interpolatedG, interpolatedB);

    }

}