package secrets;

import java.awt.image.BufferedImage;

/**
 * This class is filled with decryption methods for the Secrets in Images
 * project
 */
public class DecryptionEngine {

    /**
     * Takes an image and returns the b/w image hidden within its specified LSB
     */
    public BufferedImage decryptImage(BufferedImage img, int lsbNumber) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage bw = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int lsb = ImageUtil.getLsb(rgb, lsbNumber);
                bw.setRGB(x, y, 0xFF000000 + 0x00FFFFFF * lsb);
            }
        }

        return bw;
    }

    /**
     * Takes an image and returns the three b/w images hidden within its red, green,
     * and blue LSBs
     */
    public BufferedImage[] decryptAllImages(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage[] decryptedImages = new BufferedImage[3];

        // Reconstruct b/w image for each byte (red, green, blue)
        for (int byteNumber = 1; byteNumber <= 3; byteNumber++) {
            BufferedImage bw = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int rgb = img.getRGB(x, y);
                    int lsb = ImageUtil.getLsb(rgb, byteNumber);
                    bw.setRGB(x, y, 0xFF000000 + 0x00FFFFFF * lsb);
                }
            }
            decryptedImages[byteNumber - 1] = bw;
        }

        return decryptedImages;
    }

    /**
     * Takes an image and returns the String of Text hidden in it.
     */
    public String decryptText(BufferedImage b) {
        int w = b.getWidth();
        int h = b.getHeight();

        StringBuilder binary = new StringBuilder();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int rgb = b.getRGB(col, row);
                for (int byteNum = 0; byteNum <= 3; byteNum++) {
                    binary.append(ImageUtil.getLsb(rgb, byteNum));
                }
            }
        }

        StringBuilder text = new StringBuilder();
        // Group every 8 bits and convert to text character
        for (int i = 0; i < binary.length(); i += 8) {
            text.append((char) Integer.parseInt(binary.substring(i, i + 8), 2));
        }

        return text.toString().trim();
    }

}
