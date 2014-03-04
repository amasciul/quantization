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

            ColorHistogram histogram = new ColorHistogram(bufferedImage);

            ArrayList<Color> result = histogram.getAllDivisionsInOrder();

            for (int i = 0; i < 10 && i < result.size(); i++) {
                Color color = result.get(i);
                System.out.println("color " + i + " : " + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
            }

            Color max = result.get(2);

            JFrame frame = new JFrame();
            frame.setTitle(histogram.getDivisionNumber() + " divisions");

            frame.setSize(500, 500);
            frame.getContentPane().setBackground(new Color(max.getRed(), max.getGreen(), max.getBlue()));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
