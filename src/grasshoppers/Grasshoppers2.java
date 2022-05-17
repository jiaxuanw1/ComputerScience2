package grasshoppers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Grasshoppers2 extends JPanel {

    private final int IMAGE_DIM = 600;

    private final int lawnDim;
    private final int lawnSize;
    private final int spaceDim;
    private final double jumpDist;

    private Pixel[][] pixelGrid;
    private List<Pixel> nonLawnSpaces;
    private Set<Pixel> lawnSpaces;
    private Image image;
    private int numNewTrades;

    private int lawnColor = 0xFF00CC00;

    public static void main(String[] args) {
        Grasshoppers2 grasshoppers = new Grasshoppers2(100, 0.3);

        JFrame frame = new JFrame("Grasshoppers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(grasshoppers);
        frame.setSize(grasshoppers.getPreferredSize());
        frame.pack();
        frame.setVisible(true);

        grasshoppers.anneal(0.95);
    }

    public Grasshoppers2(int lawnDim, double jumpDist) {
        this.lawnDim = lawnDim;
        this.lawnSize = lawnDim * lawnDim;
        this.spaceDim = lawnDim * 5;
        this.jumpDist = jumpDist;

        setPreferredSize(new Dimension(IMAGE_DIM, IMAGE_DIM));
        setVisible(true);

        pixelGrid = new Pixel[spaceDim][spaceDim];
        nonLawnSpaces = new ArrayList<Pixel>();

        for (int i = 0; i < pixelGrid.length; i++) {
            for (int j = 0; j < pixelGrid[0].length; j++) {
                Pixel pixel = new Pixel(i, j);
                pixelGrid[i][j] = pixel;
            }
        }

        lawnSpaces = getSquareLawn();
//        lawnSpaces = getRandomLawn();
        BufferedImage newImage = new BufferedImage(spaceDim, spaceDim, BufferedImage.TYPE_INT_RGB);
        for (Pixel p : lawnSpaces) {
            newImage.setRGB(p.getX(), p.getY(), lawnColor);
        }
        image = newImage.getScaledInstance(IMAGE_DIM, IMAGE_DIM, Image.SCALE_DEFAULT);

        numNewTrades = 10000;
    }

    public void anneal(double coolingRate) {
        while (numNewTrades >= 1) {
            // Set each pixel's grasshopper count to 0
            resetGrasshoppers();

            int stayCount = 0;
            int thetaSteps = 2000;
            double thetaIncrement = 2 * Math.PI / thetaSteps;
            for (Pixel p : lawnSpaces) {
                for (int i = 0; i < thetaSteps; i++) {
                    // Jump in current direction
                    double direction = i * thetaIncrement;
                    int newX = p.getX() + (int) (jumpDist * lawnDim * Math.cos(direction));
                    int newY = p.getY() + (int) (jumpDist * lawnDim * Math.sin(direction));

                    // Increment grasshopper count for pixel landed on
                    try {
                        pixelGrid[newX][newY].incrementGrasshopper();
                        if (lawnSpaces.contains(pixelGrid[newX][newY])) {
                            stayCount++;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
//                        System.out.println(newX + ", " + newY);
                    }
                }
            }

            // Calculate score for current lawn
            double score = ((double) stayCount) / (thetaSteps * lawnSize);
            System.out.println("Score: " + score + ", " + numNewTrades + " new trades");

            // Generate sorted list of patches not in the current lawn
            nonLawnSpaces.clear();
            for (int i = 0; i < pixelGrid.length; i++) {
                for (int j = 0; j < pixelGrid[0].length; j++) {
                    if (!lawnSpaces.contains(pixelGrid[i][j])) {
                        nonLawnSpaces.add(pixelGrid[i][j]);
                    }
                }
            }
            Collections.sort(nonLawnSpaces, (a, b) -> a.getNumGrasshoppers() - b.getNumGrasshoppers());

            // Generate sorted list of patches in the current lawn
            List<Pixel> lawnList = new ArrayList<Pixel>(lawnSpaces);
            Collections.sort(lawnList, (a, b) -> a.getNumGrasshoppers() - b.getNumGrasshoppers());

            // Swap lowest-probability lawn patches with highest-probability non-lawn
            // patches if the latter has a higher probability
            for (int i = 0; i < numNewTrades; i++) {
                Pixel newPixel = nonLawnSpaces.get(nonLawnSpaces.size() - i - 1);
                if (lawnList.get(i).getNumGrasshoppers() < newPixel.getNumGrasshoppers()) {
                    lawnList.set(i, newPixel);
                }
            }
            lawnSpaces = new HashSet<Pixel>(lawnList);

            // Re-draw new lawn
            BufferedImage newImage = new BufferedImage(spaceDim, spaceDim, BufferedImage.TYPE_INT_RGB);
            for (Pixel p : lawnSpaces) {
                newImage.setRGB(p.getX(), p.getY(), lawnColor);
            }
            image = newImage.getScaledInstance(IMAGE_DIM, IMAGE_DIM, Image.SCALE_DEFAULT);
            repaint();

            // Update number of new trades by the cooling factor
            numNewTrades = (int) Math.floor(numNewTrades * coolingRate);
        }
    }

    public void resetGrasshoppers() {
        for (int i = 0; i < pixelGrid.length; i++) {
            for (int j = 0; j < pixelGrid[0].length; j++) {
                pixelGrid[i][j].resetGrasshoppers();
            }
        }
    }

    public Set<Pixel> getSquareLawn() {
        int lowerBound = (spaceDim - lawnDim) / 2;
        int upperBound = lowerBound + lawnDim;
        Set<Pixel> lawn = new HashSet<Pixel>();
        for (int x = lowerBound; x < upperBound; x++) {
            for (int y = lowerBound; y < upperBound; y++) {
                lawn.add(pixelGrid[x][y]);
            }
        }
        return lawn;
    }

    public Set<Pixel> getRandomLawn() {
        Set<Pixel> lawn = new HashSet<Pixel>();
        while (lawn.size() < lawnSize) {
            int x = (int) (Math.random() * spaceDim);
            int y = (int) (Math.random() * spaceDim);
            lawn.add(pixelGrid[x][y]);
        }
        return lawn;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
