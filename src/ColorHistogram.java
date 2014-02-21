import java.awt.*;

public class ColorHistogram {
    private int[][][] mHistogram;
    private int mDivisionsNumber;

    public ColorHistogram(int divisions, Color... colors) {
        mDivisionsNumber = divisions;
        mHistogram = new int[divisions][divisions][divisions];
        for (Color color : colors) {
            addPixel(color);
        }
    }

    private void addPixel(Color color) {
        int divisionLength = 256 / mDivisionsNumber;
        mHistogram[color.getRed() / divisionLength][color.getGreen() / divisionLength][color.getBlue() / divisionLength]++;
    }

    ColorBox getMainDivision() {
        ColorBox result = new ColorBox();

        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    System.out.println("[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k]);
                    if (mHistogram[i][j][k] >= mHistogram[result.red][result.green][result.blue]) {
                        System.out.println(mHistogram[i][j][k] +" >= " + mHistogram[result.red][result.green][result.blue]);
                        System.out.println("found new max " + mHistogram[i][j][k]);
                        result.red = i;
                        result.green = j;
                        result.blue = k;
                        result.count = mHistogram[i][j][k];
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

    public class ColorBox {
        public int red = 0;
        public int green = 0;
        public int blue = 0;

        public int count = 0;
    }
}
