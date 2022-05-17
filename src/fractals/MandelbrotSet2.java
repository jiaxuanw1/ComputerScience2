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

public class MandelbrotSet2 extends JPanel {

    private final int maxIterations = 500;

    private final int width = 960;
    private final int height = 720;

    private int scale; // pixels per unit
    private Complex center;

    private boolean fullyDrawn;

    private BufferedImage image;

    private double[] positions = new double[] { 0.0, 0.16, 0.42, 0.6425, 0.8575, 1.0 };

    private Color[] colors = new Color[] { new Color(0, 7, 100), new Color(32, 107, 203), new Color(237, 255, 255),
            new Color(255, 170, 0), new Color(0, 2, 0), new Color(0, 7, 100) };

    private ColorLookupTable lookupTable = new ColorLookupTable(positions, colors);

    public static void main(String[] args) {
        MandelbrotSet2 mandelbrot = new MandelbrotSet2();

        JFrame frame = new JFrame("Mandelbrot Set");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mandelbrot);
        frame.setSize(mandelbrot.getPreferredSize());
        frame.pack();
        frame.setVisible(true);
    }

    public MandelbrotSet2() {
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

                new Thread(MandelbrotSet2.this::generateMandelbrot).start();
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
                double iterations = numIterations(pointToComplex(x, y));
                if (iterations < maxIterations) {
                    double position = (iterations % 150) / 150;
                    image.setRGB(x, y, lookupTable.getColor(position).getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        fullyDrawn = true;
    }

    public double numIterations(Complex c) {
        Complex z = Complex.ZERO;
        double n = 0;
        while (z.abs() <= 10 && n < maxIterations) {
            z = z.multiply(z).add(c);
            n++;
        }
//        if (n < maxIterations) {
//            double log_zn = Math.log(z.abs());
//            double nu = Math.log(log_zn / Math.log(2)) / Math.log(2);
//            n += 1 - nu;
////            n = Math.sqrt(n + 10 - nu) * 256;
//        }
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
