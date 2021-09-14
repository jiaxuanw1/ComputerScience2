package fractals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotSet extends JPanel {

    private final int maxIterations = 256;

    private final int width = 960;
    private final int height = 720;

    private int scale; // pixels per unit
    private Complex center;

    private boolean fullyDrawn;

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

        JFrame frame = new JFrame("Mandelbrot Set");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mandelbrot);
        frame.setSize(mandelbrot.getPreferredSize());
        frame.pack();
        frame.setVisible(true);
    }

    public MandelbrotSet() {
        this.setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();

                // IMPORTANT -- update center before updating scale because pointToComplex
                // method uses the current scale
                center = pointToComplex(clickPoint);
                scale *= 2;

                new Thread(MandelbrotSet.this::generateMandelbrot).start();
                new DrawThread().start();
            }
        });

        scale = 275;
        center = new Complex(-0.3, 0);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        generateMandelbrot();
    }

    public void generateMandelbrot() {
        fullyDrawn = false;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int iterations = numIterations(pointToComplex(x, y));
                if (iterations < maxIterations) {
                    image.setRGB(x, y, colors[iterations % colors.length].getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        fullyDrawn = true;
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

    public Complex pointToComplex(double x, double y) {
        double re = (x - width / 2.0 + scale * center.getReal()) / scale;
        double im = (y - height / 2.0 + scale * center.getImaginary()) / scale;
        return new Complex(re, im);
    }

    public Complex pointToComplex(Point p) {
        return pointToComplex(p.getX(), p.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    private class DrawThread extends Thread {
        @Override
        public void run() {
            while (!fullyDrawn) {
                repaint();
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
