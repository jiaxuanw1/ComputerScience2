package fractals;

import java.awt.Color;

public class ColorPalette {

    private double[] positions;
    private Color[] colors;

    public ColorPalette(double[] positions, Color[] colors) {
        this.positions = positions;
        this.colors = colors;
    }

    public Color getColor(double pos) {
        int lowerIndex = -1;

        if (pos == positions[positions.length - 1]) {
            return colors[colors.length - 1];
        }

        for (int i = 0; i < colors.length; i++) {
            if (positions[i] > pos) {
                lowerIndex = i - 1;
                break;
            }
        }

        double lowerPos = positions[lowerIndex];
        double upperPos = positions[lowerIndex + 1];

        int lowerR = colors[lowerIndex].getRed();
        int upperR = colors[lowerIndex].getRed();

        int lowerG = colors[lowerIndex].getGreen();
        int upperG = colors[lowerIndex + 1].getGreen();

        int lowerB = colors[lowerIndex].getBlue();
        int upperB = colors[lowerIndex + 1].getBlue();

        int interpolatedR = (int) (lowerR + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperR - lowerR)));
        int interpolatedG = (int) (lowerG + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperG - lowerG)));
        int interpolatedB = (int) (lowerB + (((pos - lowerPos) / (upperPos - lowerPos)) * (upperB - lowerB)));

        return new Color(interpolatedR, interpolatedG, interpolatedB);

    }

}