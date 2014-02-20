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

    int[] getMainDivision() {
        // returns an array containing the red green and blue components of the division containing the most pixels

        int r=0, g=0, b=0;



        for (int i = 0; i < mDivisionsNumber; i++) for (int j = 0; j < mDivisionsNumber; j++) for (int k = 0; k < mDivisionsNumber; k++) {
            System.out.println("["+i+"]["+j+"]["+k+"] = " + mHistogram[i][j][k]);
            if (mHistogram[i][j][k] >= mHistogram[r][g][b]) {
                System.out.println("found new max");
                r = i;
                g = j;
                b = k;
            }
        }

        int[] result = new int[3];
        result[0] = r;
        result[1] = g;
        result[2] = b;
        System.out.println(" max ["+result[0]+"]["+result[1]+"]["+result[2]+"]");


        return result;

    }

    public String toString() {
        String s = "";
        for (int i = 0; i < mDivisionsNumber; i++) for (int j = 0; j < mDivisionsNumber; j++) for (int k = 0; k < mDivisionsNumber; k++) {
            s+= "[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k] + "\n";
        }

        return s;
    }
}
