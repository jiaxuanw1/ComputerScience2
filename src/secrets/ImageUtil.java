package secrets;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtil {

    private static final String imgDirectory = "src/secrets/images/";

    // { alpha, red, green, blue }
    private static final int[] lsbOff = { 0xFEFFFFFF, 0xFFFEFFFF, 0xFFFFFEFF, 0xFFFFFFFE };
    private static final int[] lsbOn = { 0x01000000, 0x00010000, 0x00000100, 0x00000001 };

    public static BufferedImage loadImage(String ref) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(imgDirectory + ref));
        } catch (Exception e) {
            System.out.println("LOAD IMAGE FAILED!! " + ref);
            e.printStackTrace();
        }
        return bimg;
    }

    public static void saveImage(BufferedImage b, String fileName) {
        try {
            ImageIO.write(b, "PNG", new File(imgDirectory + fileName));
        } catch (Exception e) {
            System.out.println("WRITE IMAGE FAILED!! " + fileName);
            e.printStackTrace();
        }
    }

    public static void stripeImage(BufferedImage b, Color color) {
        int w = b.getWidth();
        int h = b.getHeight();

        for (int i = 0; i < w; i += 2) {
            for (int j = 0; j < h; j++) {
                b.setRGB(i, j, color.getRGB());
            }
        }
    }

    public static int[][] getImageData(BufferedImage b) {
        int w = b.getWidth();
        int h = b.getHeight();
        int[][] pixels = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pixels[i][j] = b.getRGB(i, j);
            }
        }

        return pixels;
    }

    // Byte numbers: 0 - alpha, 1 - red, 2 - green, 3 - blue

    public static int lsbOff(int rgb, int byteNumber) {
        return rgb & lsbOff[byteNumber];
    }

    public static int lsbOn(int rgb, int byteNumber) {
        return rgb | lsbOn[byteNumber];
    }

    public static int getLsb(int rgb, int byteNumber) {
        int lsb = (rgb >> ((3 - byteNumber) * 8)) % 2;
        return Math.abs(lsb);
    }

}