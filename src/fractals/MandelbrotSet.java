package fractals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotSet extends JPanel {

    private final int maxIterations = 255;

    private final int width = 960;
    private final int height = 720;

    private BufferedImage image;

    private Color[] colors = new Color[] { 
            new Color(66, 30, 15), 
            new Color(25, 7, 26), 
            new Color(9, 1, 47),
            new Color(4, 4, 73), 
            new Color(0, 7, 100), 
            new Color(12, 44, 138), 
            new Color(24, 82, 177),
            new Color(57, 125, 209), 
            new Color(134, 181, 229), 
            new Color(211, 236, 248), 
            new Color(241, 233, 191),
            new Color(248, 201, 95), 
            new Color(255, 170, 0), 
            new Color(204, 128, 0), 
            new Color(153, 87, 0),
            new Color(106, 52, 3) };

    public static void main(String[] args) {
        MandelbrotSet mandelbrot = new MandelbrotSet();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mandelbrot);
        frame.setSize(mandelbrot.getPreferredSize());
        frame.setVisible(true);
    }

    public MandelbrotSet() {
        setPreferredSize(new Dimension(960, 720));
        setVisible(true);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawMandelbrot();
    }

    public void drawMandelbrot() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double re = (col - width / 2.0) * 2.5 / height - 0.4;
                double im = (row - height / 2.0) * 2.5 / height;
                int iterations = numIterations(new Complex(re, im));

                if (iterations < maxIterations) {
                    image.setRGB(col, row, colors[iterations % colors.length].getRGB());
                } else {
                    image.setRGB(col, row, Color.BLACK.getRGB());
                }
            }
        }
    }

    public int numIterations(Complex c) {
        Complex z = Complex.ZERO;
        int n = 0;

        while (z.abs() <= 2 && n < maxIterations) {
            z = z.multiply(z).add(c);
            n++;
        }

        return n;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

}
