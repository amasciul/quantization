import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("apple.jpg"));

            int divisions = 4;

            ColorHistogram histogram = new ColorHistogram(bufferedImage, divisions);

            ArrayList<ColorHistogram.ColorBox> result = histogram.getAllDivisionsInOrder();

            for (int i = 0; i < 10; i++) {
                ColorHistogram.ColorBox color = result.get(result.size() - 1 - i);
                System.out.println("color " + i + " : " + color.red + " " + color.green + " " + color.blue);
            }

            ColorHistogram.ColorBox max = result.get(result.size() - 1);

            System.out.println("main color : [" + max.red + "][" + max.green + "][" + max.blue + "]");

            JFrame frame = new JFrame();

            frame.setSize(500, 500);
            frame.getContentPane().setBackground(new Color(max.red, max.green, max.blue));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
