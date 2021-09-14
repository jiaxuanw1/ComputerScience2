package imagerecognition;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class Camera {

    private FrameGrabber grabber;
    private Frame frame;

    private CanvasFrame canvas;

    public Camera() {
        grabber = new OpenCVFrameGrabber(0);
        try {
            grabber.start();
        } catch (Exception e) {
            System.out.println("Could not start camera!");
            e.printStackTrace();
        }
    }

    public void startCapture() {
        canvas = new CanvasFrame("Camera");
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setLayout(new FlowLayout());

        new CameraCaptureThread().start();
    }

    public BufferedImage captureImage() {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.convert(frame);
        converter.close();
        return image;
    }

    private class CameraCaptureThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    frame = grabber.grab();
                    canvas.showImage(frame);
                } catch (Exception e) {
                    System.out.println("Could not capture frame!");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        Camera camera = new Camera();
        camera.startCapture();

    }

}
