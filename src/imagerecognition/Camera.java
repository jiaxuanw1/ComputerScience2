package imagerecognition;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIManager;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class Camera {

    private final double FPS;

    private FrameGrabber grabber;
    private BufferedImage frame;

    private CanvasFrame canvas;
    private Java2DFrameConverter converter;

    private boolean bwOn = false;
    private boolean mirrored = false;
    private int bwThreshold = 150;

    /**
     * Starts the frame grabber.
     */
    public Camera() {
        grabber = new OpenCVFrameGrabber(0);
        converter = new Java2DFrameConverter();

        try {
            grabber.start();
            frame = converter.convert(grabber.grab());
        } catch (Exception e) {
            System.out.println("Could not start camera!");
        }

        FPS = grabber.getFrameRate();
    }

    /**
     * Starts capturing frames and launches the camera display.
     */
    public void init() {
        // Initialize display window
        canvas = new CanvasFrame("Camera");
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setLayout(new FlowLayout());

        // Set system theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting system theme!");
        }

        // Take Picture button
        JButton shutterButton = new JButton("Take Picture!");
        shutterButton.addActionListener((e) -> captureImage());
        canvas.add(shutterButton);

        // Toggle BW/Color check box
        JCheckBox bwCheckBox = new JCheckBox("Black/White");
        bwCheckBox.addActionListener((e) -> bwOn = bwCheckBox.isSelected());
        canvas.add(bwCheckBox);

        // Mirror Image check box
        JCheckBox mirrorButton = new JCheckBox("Mirrored");
        mirrorButton.addActionListener((e) -> mirrored = mirrorButton.isSelected());
        canvas.add(mirrorButton);

        // Black/White Threshold slider
        JSlider bwSlider = new JSlider(0, 255, bwThreshold);
        bwSlider.setBorder(BorderFactory.createTitledBorder("Black/White Threshold"));
        bwSlider.addChangeListener((e) -> bwThreshold = bwSlider.getValue());
        canvas.add(bwSlider);

        // Start camera capture
        new CameraCaptureThread().start();
    }

    public BufferedImage captureImage() {
        try {
            ImageIO.write(frame, "PNG", new File("photo.png"));
        } catch (IOException e) {
            System.out.println("Error writing image!");
        }
        return frame;
    }

    /**
     * Receives a BufferedImage and returns a new BufferedImage with the original
     * image in pure black and white with a given threshold.
     */
    public BufferedImage toBlackAndWhite(BufferedImage bimg, int threshold) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        BufferedImage gray = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = gray.createGraphics();
        g2d.drawImage(frame, 0, 0, null);
        g2d.dispose();

        BufferedImage bw = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if ((gray.getRGB(x, y) & 0xFF) > threshold) {
                    bw.setRGB(x, y, 0xFFFFFFFF);
                } else {
                    bw.setRGB(x, y, 0xFF000000);
                }
            }
        }
        return bw;
    }

    /**
     * Receives a BufferedImage and returns a new BufferedImage with the original
     * image mirrored horizontally.
     */
    public BufferedImage mirrorImage(BufferedImage bimg) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-bimg.getWidth(), 0));

        BufferedImage mirrored = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mirrored.createGraphics();
        g2d.transform(at);
        g2d.drawImage(bimg, 0, 0, null);
        g2d.dispose();
        return mirrored;
    }

    private class CameraCaptureThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    frame = converter.convert(grabber.grab());
                    if (mirrored) {
                        frame = mirrorImage(frame);
                    }
                    if (bwOn) {
                        frame = toBlackAndWhite(frame, bwThreshold);
                    }
                    canvas.showImage(frame);
                    sleep((int) (1000 / FPS));
                } catch (Exception e) {
                    System.out.println("Error capturing frame!");
                }
            }
        }
    }

    public static void main(String[] args) {

        Camera camera = new Camera();
        camera.init();

    }

}
