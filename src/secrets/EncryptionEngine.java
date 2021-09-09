package secrets;

import java.awt.image.BufferedImage;

/**
 * This class is filled with encryption methods for the Secrets in Images
 * project.
 */
public class EncryptionEngine {

    /*
     * Takes a String of text and hides it within an image's alpha, red, green, and
     * blue LSBs. The image will be modified in the process, but not appear
     * different to the eye, because the change is only in the LSBs.
     */
    public void encryptText(String text, BufferedImage b) {
        int w = b.getWidth();
        int h = b.getHeight();
        byte[] byteArray = text.getBytes();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                // One character is stored in every 2 pixels
                int pos = (row * w + col) / 2;

                // If pixel has corresponding character in text, encrypt the character
                if (pos < byteArray.length) {
                    String bin = toByteSizeString(byteArray[pos]);
                    // If column is even, get first 4 bits, otherwise get last 4 bits
                    bin = col % 2 == 0 ? bin.substring(0, 4) : bin.substring(4);
                    char[] binDigits = bin.toCharArray();

                    // Hide first bit in alpha LSB, second bit in red LSB, and so on...
                    for (int byteNum = 0; byteNum < binDigits.length; byteNum++) {
                        if (binDigits[byteNum] == '0') {
                            b.setRGB(col, row, ImageUtil.lsbOff(b.getRGB(col, row), byteNum));
                        } else {
                            b.setRGB(col, row, ImageUtil.lsbOn(b.getRGB(col, row), byteNum));
                        }
                    }
                }
                // If there are no more characters, encrypt null character
                else {
                    for (int byteNum = 0; byteNum <= 3; byteNum++) {
                        b.setRGB(col, row, ImageUtil.lsbOff(b.getRGB(col, row), byteNum));
                    }
                }
            }
        }
    }

    /**
     * Modifies an image to hide a second image within its LSBs
     */
    public void encryptImage(BufferedImage img, BufferedImage bw, int lsbNumber) {
        for (int x = 0; x < bw.getWidth(); x++) {
            for (int y = 0; y < bw.getHeight(); y++) {
                int rgb = img.getRGB(x, y);
                // If B/W pixel is white, turn on colored pixel's specified LSB
                if (bw.getRGB(x, y) == 0xFFFFFFFF) {
                    img.setRGB(x, y, ImageUtil.lsbOn(rgb, lsbNumber));
                }
                // If B/W pixel is black, turn off colored pixel's specified LSB
                else {
                    img.setRGB(x, y, ImageUtil.lsbOff(rgb, lsbNumber));
                }
            }
        }
    }

    /**
     * Returns a string representation of the given number in binary with the
     * appropriate leading 0's to make it 8 bits long.
     */
    public String toByteSizeString(int i) {
        String bin = Integer.toBinaryString(i);
        bin = "0".repeat(8 - bin.length()) + bin;
        return bin;
    }

}