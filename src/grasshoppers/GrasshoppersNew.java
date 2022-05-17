package grasshoppers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Java implementation of solution submitted by Jacob to fivethirtyeight.org
 */
public class GrasshoppersNew extends JPanel {

    private final int lawnDim;
    private final int lawnSize;
    private final int spaceDim;
    private final double jumpDist;

    private Random random;
    private Set<List<Integer>> lawn;
    private BufferedImage image;

    public static void main(String[] args) {
        GrasshoppersNew grasshoppers = new GrasshoppersNew(100, 0.3);

        JFrame frame = new JFrame("Grasshoppers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(grasshoppers);
        frame.setSize(grasshoppers.getPreferredSize());
        frame.pack();
        frame.setVisible(true);

        grasshoppers.anneal(0.95);
    }

    public GrasshoppersNew(int lawnDim, double jumpDist) {
        this.lawnDim = lawnDim;
        this.lawnSize = lawnDim * lawnDim;
        this.spaceDim = lawnDim * 5;
        this.jumpDist = jumpDist;

        setPreferredSize(new Dimension(spaceDim, spaceDim));
        setVisible(true);

        random = new Random();

        lawn = new HashSet<List<Integer>>();
        while (lawn.size() < lawnSize) {
            lawn.add(Arrays.asList(random.nextInt(spaceDim), random.nextInt(spaceDim)));
        }

        image = new BufferedImage(spaceDim, spaceDim, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Calculates jump probability distribution from a given patch to any other
     * patch. This distribution is a circular ring centered at (0,0) with radius
     * equal to the jump distance.
     * 
     * Returns a map whose keys represent (dx, dy) pairs indicating the location of
     * a patch relative to the given patch (the given patch is (0,0)). The map
     * values represent the probability of landing on the corresponding patch.
     */
    public Map<List<Integer>, BigDecimal> jumpProbs() {
        // Consider a single patch at (31, 31) which can jump to patches from (0,0)
        // through (61,61)
        int jumpDim = 2 * (int) (Math.ceil(jumpDist * lawnDim)) + 2; // 62
        int center = (int) (Math.ceil(jumpDist * lawnDim)) + 1; // 31
        int[][] jumpCounts = new int[jumpDim][jumpDim];
        int thetaSteps = 1000;
        double thetaInc = 2 * Math.PI / thetaSteps;
        int xySteps = 10;
        for (int xIdx = 0; xIdx < xySteps; xIdx++) {
            double x0 = (xIdx + 0.5) / xySteps + (center - 1); // [0.05, 1.05] + 30
            for (int yIdx = 0; yIdx < xySteps; yIdx++) {
                double y0 = (yIdx + 0.5) / xySteps + (center - 1); // [0.05, 1.05] + 30
                for (int thetaIdx = 0; thetaIdx < thetaSteps; thetaIdx++) {
                    double theta = thetaIdx * thetaInc;
                    double x1 = x0 + Math.cos(theta) * (center - 1);
                    double y1 = y0 + Math.sin(theta) * (center - 1);
                    jumpCounts[(int) Math.floor(x1)][(int) Math.floor(y1)]++;
                }
            }
        }

        // Generate map associating a (dx, dy) pair with its probability
        int sumJumpCounts = Arrays.stream(jumpCounts).flatMapToInt(Arrays::stream).sum();
        Map<List<Integer>, BigDecimal> jumpProbs = new HashMap<List<Integer>, BigDecimal>();
        for (int x = 0; x < jumpDim; x++) {
            for (int y = 0; y < jumpDim; y++) {
                jumpProbs.put(Arrays.asList(x - center, y - center),
                        new BigDecimal(jumpCounts[x][y]).divide(new BigDecimal(sumJumpCounts)));
            }
        }

        return jumpProbs;
    }

    public void anneal(double coolingRate) {
        Map<List<Integer>, BigDecimal> jumpProbs = jumpProbs();
        BigDecimal[][] probGrid = new BigDecimal[spaceDim][spaceDim];

        double portionNew = 1.0;
        int generation = 0;

        while (portionNew * lawnSize >= 1.0) {
            // Calculate the probability distribution of final patches given current lawn
            probGrid = new BigDecimal[spaceDim][spaceDim];
            for (int i = 0; i < spaceDim; i++) {
                for (int j = 0; j < spaceDim; j++) {
                    probGrid[i][j] = BigDecimal.ZERO;
                }
            }
            for (List<Integer> point : lawn) {
                for (List<Integer> diff : jumpProbs.keySet()) {
                    int x1 = point.get(0) + diff.get(0); // x1 = x0 + dx
                    int y1 = point.get(1) + diff.get(1); // y1 = y0 + dy
                    if (Math.min(x1, y1) >= 0 && Math.max(x1, y1) < spaceDim) {
                        probGrid[x1][y1] = probGrid[x1][y1].add(jumpProbs.get(diff));
                    }
                }
            }

            // Divide all probabilities by total to make them true probabilities and
            // calculate score for old lawn
            BigDecimal oldLawnScore = BigDecimal.ZERO;
            BigDecimal sumOfProbs = Arrays.stream(probGrid).flatMap(Arrays::stream).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            for (int x = 0; x < probGrid.length; x++) {
                for (int y = 0; y < probGrid[0].length; y++) {
                    probGrid[x][y] = probGrid[x][y].divide(sumOfProbs, 30, RoundingMode.HALF_DOWN);
                    if (lawn.contains(Arrays.asList(x, y))) {
                        oldLawnScore = oldLawnScore.add(probGrid[x][y]);
                    }
                }
            }

            // Generate sorted list of all patches not in the current lawn
            List<List<BigDecimal>> nonLawnList = new ArrayList<List<BigDecimal>>();
            for (int x = 0; x < spaceDim; x++) {
                for (int y = 0; y < spaceDim; y++) {
                    if (!lawn.contains(Arrays.asList(x, y))) {
                        // {x, y, prob}
                        nonLawnList.add(Arrays.asList(new BigDecimal(x), new BigDecimal(y), probGrid[x][y]));
                    }
                }
            }
            Collections.sort(nonLawnList, (a, b) -> a.get(2).compareTo(b.get(2)));

            // Generate sorted list of all patches in the current lawn
            int numNewPatches = (int) Math.floor(portionNew * lawnSize);
            List<List<BigDecimal>> oldLawnList = new ArrayList<List<BigDecimal>>();
            for (List<Integer> patch : lawn) {
                int x = patch.get(0);
                int y = patch.get(1);
                oldLawnList.add(Arrays.asList(new BigDecimal(x), new BigDecimal(y), probGrid[x][y])); // {x, y, prob}
            }
            Collections.sort(oldLawnList, (a, b) -> a.get(2).compareTo(b.get(2)));

            // Swap lowest-probability lawn patches with highest-probability non-lawn
            // patches if the latter has a higher probability
            for (int i = 0; i < numNewPatches; i++) {
                List<BigDecimal> oldLawnPatch = oldLawnList.get(i);
                List<BigDecimal> nonLawnPatch = nonLawnList.get(nonLawnList.size() - i - 1);
                if (oldLawnPatch.get(2).compareTo(nonLawnPatch.get(2)) < 0) {
                    lawn.remove(Arrays.asList(oldLawnPatch.get(0).intValue(), oldLawnPatch.get(1).intValue()));
                    lawn.add(Arrays.asList(nonLawnPatch.get(0).intValue(), nonLawnPatch.get(1).intValue()));
                }
            }

            // Calculate score for newly-generated lawn
            BigDecimal newLawnScore = BigDecimal.ZERO;
            for (List<Integer> p : lawn) {
                newLawnScore = newLawnScore.add(probGrid[p.get(0)][p.get(1)]);
            }

            System.out.println("Generation " + generation + " (temp=" + numNewPatches + "): "
                    + newLawnScore.round(MathContext.DECIMAL32) + " vs " + oldLawnScore.round(MathContext.DECIMAL32));

            // Re-draw new lawn
            image = new BufferedImage(spaceDim, spaceDim, BufferedImage.TYPE_INT_RGB);
            for (List<Integer> p : lawn) {
                image.setRGB(p.get(0), p.get(1), 0xFF0000FF);
            }
            repaint();

            // Every generation, "cool" the annealing process and select a smaller
            // proportion of new patches
            portionNew *= coolingRate;
            generation++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

}
