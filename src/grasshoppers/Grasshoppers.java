package grasshoppers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Grasshoppers extends JPanel {

    private final int JUMP_DIST = 30;
    private final int SIDE_LENGTH = 300;
    private final int IMAGE_SIZE = 600;

    private Pixel[][] pixelGrid;
    private List<Pixel> allSpaces;
    private List<Pixel> lawnSpaces;
    private Image image;

    private int lawnColor = 0xFF00CC00;

    public static void main(String[] args) {
        Grasshoppers grasshoppers = new Grasshoppers();

        JFrame frame = new JFrame("Grasshoppers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(grasshoppers);
        frame.setSize(grasshoppers.getPreferredSize());
        frame.pack();
        frame.setVisible(true);

        while (true) {
            grasshoppers.iterate();
        }
    }

    public Grasshoppers() {
        setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        setVisible(true);

        pixelGrid = new Pixel[SIDE_LENGTH][SIDE_LENGTH];
        allSpaces = new ArrayList<Pixel>();

        for (int i = 0; i < pixelGrid.length; i++) {
            for (int j = 0; j < pixelGrid[0].length; j++) {
                Pixel pixel = new Pixel(i, j);
                pixelGrid[i][j] = pixel;
                allSpaces.add(pixel);
            }
        }

        lawnSpaces = new ArrayList<Pixel>();
        BufferedImage lawnImage = new BufferedImage(SIDE_LENGTH, SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);

        // Start with a 100 by 100 square lawn
        int lowerBound = SIDE_LENGTH / 2 - 50;
        int upperBound = lowerBound + 100;
        for (int x = lowerBound; x < upperBound; x++) {
            for (int y = lowerBound; y < upperBound; y++) {
                lawnSpaces.add(pixelGrid[x][y]);
                lawnImage.setRGB(x, y, lawnColor);
            }
        }

        image = lawnImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_DEFAULT);
    }

    public void iterate() {
        // Set each pixel's grasshopper count to 0
        resetGrasshoppers();

        for (Pixel p : lawnSpaces) {
            for (int i = 0; i < 20000000 / lawnSpaces.size(); i++) {
                // Jump in a random direction
                double direction = Math.toRadians(Math.random() * 360.0);
                int newX = p.getX() + (int) (JUMP_DIST * Math.cos(direction));
                int newY = p.getY() + (int) (JUMP_DIST * Math.sin(direction));

                // Increment grasshopper count for pixel landed on
                try {
                    pixelGrid[newX][newY].incrementGrasshopper();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(newX + ", " + newY);
                }
            }
        }

        // Sort all pixels by number of grasshoppers in descending order
        Collections.sort(allSpaces, (a, b) -> b.getNumGrasshoppers() - a.getNumGrasshoppers());

        // First 10,000 pixels in sorted list are the most landed-on spaces
        lawnSpaces = allSpaces.subList(0, 10000);

        // Re-draw new lawn
        BufferedImage newImage = new BufferedImage(SIDE_LENGTH, SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);
        for (Pixel p : lawnSpaces) {
            newImage.setRGB(p.getX(), p.getY(), lawnColor);
        }
        image = newImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_DEFAULT);
        repaint();
    }

    public void resetGrasshoppers() {
        for (Pixel p : allSpaces) {
            p.resetGrasshoppers();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
