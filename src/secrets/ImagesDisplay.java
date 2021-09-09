package secrets;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Takes care of the display of images for this project.
 */
public class ImagesDisplay {

    private List<BufferedImage> images = new ArrayList<BufferedImage>();

    private JFrame frame;

    public ImagesDisplay(BufferedImage... imgs) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        for (BufferedImage img : imgs) {
            images.add(img);
            frame.add(new JLabel(new ImageIcon(img)));
        }
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    public BufferedImage getImage(int index) {
        return images.get(index);
    }

    public void setImage(int index, BufferedImage img) {
        images.set(index, img);
    }

}
