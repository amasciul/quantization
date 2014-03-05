import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ColorQuantizer {
    public static final int DEFAULT_DIVISIONS_NUMBER = 2;

    private ArrayList<Integer>[][][] mHistogram;
    private int mDivisionsNumber;
    private ArrayList<Color> mQuantizedColors;

    public ColorQuantizer(int divisions) {
        mDivisionsNumber = divisions;
        mHistogram = new ArrayList[divisions][divisions][divisions];
        for (int i = 0; i < divisions; i++)
            for (int j = 0; j < divisions; j++)
                for (int k = 0; k < divisions; k++) {
                    mHistogram[i][j][k] = new ArrayList<Integer>();
                }
    }

    public ColorQuantizer() {
        this(DEFAULT_DIVISIONS_NUMBER);
    }

    public ColorQuantizer load(BufferedImage image) {
        int[] imagePixels = convertToTable(image);

        for (int i = 0; i < imagePixels.length; i++) {
            addPixel(imagePixels[i]);
        }
        return this;
    }

    private void addPixel(int color) {
        int divisionLength = 256 / mDivisionsNumber;
        Color colorObject = new Color(color);
        mHistogram[colorObject.getRed() / divisionLength][colorObject.getGreen() / divisionLength][colorObject.getBlue() / divisionLength].add(color);
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

    public ColorQuantizer quantize() {
        ArrayList<ColorBox> boxResult = new ArrayList<ColorBox>();
        mQuantizedColors = new ArrayList<Color>();

        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    if (mHistogram[i][j][k].size() == 0) break;

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            Color colorA = new Color(a);
                            Color colorB = new Color(b);
                            return ((Integer)colorA.getRed()).compareTo(colorB.getRed());
                        }
                    });
                    int redMedian = new Color(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2)).getRed();

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            Color colorA = new Color(a);
                            Color colorB = new Color(b);
                            return ((Integer)colorA.getGreen()).compareTo(colorB.getGreen());
                        }
                    });
                    int greenMedian = new Color(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2)).getGreen();

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            Color colorA = new Color(a);
                            Color colorB = new Color(b);
                            return ((Integer)colorA.getBlue()).compareTo(colorB.getBlue());
                        }
                    });
                    int blueMedian = new Color(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2)).getBlue();

                    boxResult.add(new ColorBox(redMedian, greenMedian, blueMedian, mHistogram[i][j][k].size()));
                }

        Collections.sort(boxResult, new ColorBoxReverseComparator());

        for (ColorBox colorBox : boxResult) {
            mQuantizedColors.add(new Color(colorBox.red, colorBox.green, colorBox.blue));
        }

        return this;
    }

    public int getDivisionNumber() {
        return mDivisionsNumber;
    }

    public ArrayList<Color> getQuantizedColors() {
        return mQuantizedColors;
    }

    private class ColorBox {

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

    private class ColorBoxReverseComparator implements Comparator<ColorBox> {
        @Override
        public int compare(ColorBox a, ColorBox b) {
            return ((Integer)b.count).compareTo(a.count);
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
