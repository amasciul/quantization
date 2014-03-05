import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("apple.jpg"));

            ColorQuantizer quantizer = new ColorQuantizer().load(bufferedImage).quantize();

            ArrayList<Integer> result = quantizer.getQuantizedColors();

            for (int i = 0; i < 10 && i < result.size(); i++) {
                int color = result.get(i);
                Color colorObject = new Color(color);
                System.out.println("color " + i + " : " + colorObject.getRed() + " " + colorObject.getGreen() + " " + colorObject.getBlue());
            }

            for (int i = 0; i < 4; i++) {
                Color max = new Color(result.get(i));

                JFrame frame = new JFrame();
                frame.setTitle("color " + i + ", " + quantizer.getDivisionNumber() + " divisions");

                frame.setSize(500, 500);
                frame.getContentPane().setBackground(new Color(max.getRed(), max.getGreen(), max.getBlue()));

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
