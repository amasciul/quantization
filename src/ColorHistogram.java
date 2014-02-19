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
        int divisionLength = 256/mDivisionsNumber;
        mHistogram[color.getRed()/divisionLength][color.getGreen()/divisionLength][color.getBlue()/divisionLength]++;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < mDivisionsNumber; i++) for (int j = 0; j < mDivisionsNumber; j++) for (int k = 0; k < mDivisionsNumber; k++) {
            s+= "[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k] + "\n";
        }

        return s;
    }
}
