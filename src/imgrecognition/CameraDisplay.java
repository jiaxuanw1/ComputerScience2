package imgrecognition;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.UIManager;

import org.bytedeco.javacv.CanvasFrame;

import nu.pattern.OpenCV;

public class CameraDisplay {

    private Camera camera;
    private CanvasFrame canvas;

    public static void main(String[] args) {
        new CameraDisplay();
    }

    public CameraDisplay() {
        OpenCV.loadLocally();

        camera = new Camera();

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
        JButton scanQRButton = new JButton("Scan QR Code!");
        scanQRButton.addActionListener((e) -> {
            BufferedImage qrImage = camera.scanQR();
            if (qrImage != null) {
                try {
                    String text = QRUtil.readAndDecode(qrImage);
                    JOptionPane.showMessageDialog(null, text);
                } catch (InvalidQRException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        canvas.add(scanQRButton);

        // Take Picture button
        JButton shutterButton = new JButton("Take Picture!");
        shutterButton.addActionListener((e) -> camera.displayFrame());
        canvas.add(shutterButton);

        // Toggle BW/Color check box
        JCheckBox bwCheckBox = new JCheckBox("Black/White");
        bwCheckBox.addActionListener((e) -> camera.setBw(bwCheckBox.isSelected()));
        canvas.add(bwCheckBox);

        // Mirror Image check box
        JCheckBox mirrorCheckBox = new JCheckBox("Mirrored");
        mirrorCheckBox.addActionListener((e) -> camera.setMirrored(mirrorCheckBox.isSelected()));
        canvas.add(mirrorCheckBox);

        // Black/White Threshold slider
        JSlider bwSlider = new JSlider(0, 255, 150);
        bwSlider.setBorder(BorderFactory.createTitledBorder("Black/White Threshold: 150"));
        bwSlider.addChangeListener((e) -> {
            camera.setBwThreshold(bwSlider.getValue());
            bwSlider.setBorder(BorderFactory.createTitledBorder("Black/White Threshold: " + bwSlider.getValue()));
        });
        canvas.add(bwSlider);

        // Start camera capture
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            BufferedImage currentFrame = camera.getCurrentFrame();
            canvas.showImage(currentFrame);
        }, 0, 1000 / camera.getFPS(), TimeUnit.MILLISECONDS);

    }

}
