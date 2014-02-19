import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("apple.jpg"));

            int[] image = convertToTable(bufferedImage);;
            Color[] colorImage = new Color[image.length];

            for (int i = 0; i < colorImage.length; i++) {
                Color color = new Color(image[i]);
                colorImage[i] = color;
            }

            ColorHistogram histogram = new ColorHistogram(4, colorImage);
            System.out.println(histogram.toString());
        } catch (IOException e) {
        }
    }

    private static int[] convertToTable(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[] result = new int[height*width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[pixel/pixelLength] = argb;
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[pixel/pixelLength] = argb;
            }
        }

        return result;
    }
}
