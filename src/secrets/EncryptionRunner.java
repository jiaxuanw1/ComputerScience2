package secrets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This tests the encryption features!
 */
public class EncryptionRunner {

    public static void main(String[] args) {
//        testImageEncryption();

        try {
            testTextEncryption(Files.readString(Path.of("src/secrets/text.txt")));
        } catch (IOException e) {
            System.out.println("Unable to read text file!");
        }
    }

    public static void testImageEncryption() {
        EncryptionEngine e = new EncryptionEngine();

        BufferedImage origImg = ImageUtil.loadImage("cube.png");

        BufferedImage encryptImg = ImageUtil.loadImage("cube.png");
        BufferedImage bwImg1 = ImageUtil.loadImage("bw1.png");
        BufferedImage bwImg2 = ImageUtil.loadImage("bw2.png");
        BufferedImage bwImg3 = ImageUtil.loadImage("bw3.png");

        e.encryptImage(encryptImg, bwImg1, 1);
        e.encryptImage(encryptImg, bwImg2, 2);
        e.encryptImage(encryptImg, bwImg3, 3);

        ImageUtil.saveImage(encryptImg, "encrypted.png");

        ImagesDisplay display = new ImagesDisplay(origImg, encryptImg);
        display.show();
    }

    public static void testTextEncryption(String text) {
        EncryptionEngine e = new EncryptionEngine();

        BufferedImage origImg = ImageUtil.loadImage("cube.png");

        BufferedImage encryptImg = ImageUtil.loadImage("cube.png");
        e.encryptText(text, encryptImg);
        ImageUtil.saveImage(encryptImg, "encryptedText.png");

        ImagesDisplay display = new ImagesDisplay(origImg, encryptImg);
        display.show();
    }

}