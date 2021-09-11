package secrets;

import java.awt.image.BufferedImage;

public class Tester {

    public static void main(String... args) {
//        BufferedImage img = ImageUtil.loadImage("cube.png");
//        for (int x = 0; x < img.getWidth(); x++) {
//            for (int y = 0; y < img.getHeight(); y++) {
//                int rgb = img.getRGB(x, y);
//                img.setRGB(x, y, rgb & 0xFFFF00FF);
//            }
//        }
//
//        new ImagesDisplay(img).show();

        BufferedImage bimg = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        for (int x = 0; x < bimg.getWidth(); x++) {
            for (int y = 0; y < bimg.getHeight(); y++) {
                bimg.setRGB(x, y, 0xFF0000FF);
            }
        }
        new ImagesDisplay(bimg).show();
    }

}
