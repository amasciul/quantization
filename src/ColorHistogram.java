import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ColorHistogram {
    private ArrayList<Color>[][][] mHistogram;
    private int mDivisionsNumber;

    public ColorHistogram(BufferedImage image, int divisions) {
        mDivisionsNumber = divisions;
        mHistogram = new ArrayList[divisions][divisions][divisions];
        for (int i = 0; i < divisions; i++)
            for (int j = 0; j < divisions; j++)
                for (int k = 0; k < divisions; k++) {
                    mHistogram[i][j][k] = new ArrayList<Color>();
                }

        int[] imagePixels = convertToTable(image);
        Color[] imageColors = new Color[imagePixels.length];

        for (int i = 0; i < imageColors.length; i++) {
            Color color = new Color(imagePixels[i]);
            imageColors[i] = color;
        }

        for (Color color : imageColors) {
            addPixel(color);
        }
    }

    private void addPixel(Color color) {
        int divisionLength = 256 / mDivisionsNumber;
        mHistogram[color.getRed() / divisionLength][color.getGreen() / divisionLength][color.getBlue() / divisionLength].add(color);
    }

    ColorBox getMainDivision() {
        ColorBox result = new ColorBox();

        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    System.out.println("[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k]);
                    if (mHistogram[i][j][k].size() >= mHistogram[result.red][result.green][result.blue].size()) {
                        System.out.println(mHistogram[i][j][k] +" >= " + mHistogram[result.red][result.green][result.blue]);
                        System.out.println("found new max " + mHistogram[i][j][k]);
                        result.red = i;
                        result.green = j;
                        result.blue = k;
                        result.count = mHistogram[i][j][k].size();
                    }
                }
        System.out.println(" max [" + result.red + "][" + result.green + "][" + result.blue + "]");
        return result;

    }

    public String toString() {
        String s = "";
        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    s += "[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k] + "\n";
                }

        return s;
    }

    public ArrayList<ColorBox> getAllDivisionsInOrder() {
        ArrayList<ColorBox> result = new ArrayList<ColorBox>();
        int l = 0;

        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    if (mHistogram[i][j][k].size() == 0) break;
                    Collections.sort(mHistogram[i][j][k], new Comparator<Color>() {
                        @Override
                        public int compare(Color a, Color b) {
                            return ((Integer)a.getRed()).compareTo(b.getRed());
                        }
                    });
                    int redMedian = mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2).getRed();

                    Collections.sort(mHistogram[i][j][k], new Comparator<Color>() {
                        @Override
                        public int compare(Color a, Color b) {
                            return ((Integer)a.getGreen()).compareTo(b.getGreen());
                        }
                    });
                    int greenMedian = mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2).getGreen();

                    Collections.sort(mHistogram[i][j][k], new Comparator<Color>() {
                        @Override
                        public int compare(Color a, Color b) {
                            return ((Integer)a.getBlue()).compareTo(b.getBlue());
                        }
                    });
                    int blueMedian = mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2).getBlue();

                    result.add(new ColorBox(redMedian,greenMedian,blueMedian, mHistogram[i][j][k].size()));
                }

        Collections.sort(result, new ColorBoxComparator());

        return result;
    }

    public class ColorBox {

        public ColorBox() {

        }

        public ColorBox(int r, int g, int b, int count) {
            red = r;
            green = g;
            blue = b;
            this.count = count;
        }

        public int red = 0;
        public int green = 0;
        public int blue = 0;

        public int count = 0;
    }

    private class ColorBoxComparator implements Comparator<ColorBox> {
        @Override
        public int compare(ColorBox a, ColorBox b) {
            return ((Integer)a.count).compareTo(b.count);
        }
    }

    private int[] convertToTable(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[] result = new int[height * width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[pixel / pixelLength] = argb;
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[pixel / pixelLength] = argb;
            }
        }

        return result;
    }
}
