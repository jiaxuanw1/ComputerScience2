package secrets;

import java.awt.image.BufferedImage;

/**
 * This tests the decryption features!
 */
public class DecryptionRunner {

    public static void main(String[] args) {
//        testImageDecryption();
        testTextDecryption();
    }

    public static void testImageDecryption() {
        DecryptionEngine d = new DecryptionEngine();

        BufferedImage encryptedImg = ImageUtil.loadImage("encrypted.png");
        BufferedImage[] decrypted = d.decryptAllImages(encryptedImg);

        ImagesDisplay display = new ImagesDisplay(encryptedImg, decrypted[0], decrypted[1], decrypted[2]);
        display.show();
    }

    public static void testTextDecryption() {
        DecryptionEngine d = new DecryptionEngine();

        BufferedImage encryptedImg = ImageUtil.loadImage("encryptedText.png");
        String text = d.decryptText(encryptedImg);
        System.out.println(text);
    }

}
