package nm.droid.com.numericalmethods.differences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class EquidistantInterpolation {

    private double mX;
    private double[][] mDifferences;
    private String[][] mDifferencesString;
    private DecimalFormat mDecimalFormat;
    private InterpolationOutputActivity mInterpolationOutputActivity;
    private List<InterpolationRow> mInterpolationRows;
    private double mStepSize;
    private double mP;
    private double mY;
    private boolean mBackward;
    private int i;
    private int j;

    EquidistantInterpolation(InterpolationOutputActivity interpolationOutputActivity, String[] xvalues, String[] yvalues, double x, DecimalFormat decimalFormat, boolean backward) {
        mInterpolationOutputActivity = interpolationOutputActivity;
        mX = x;
        mDecimalFormat = decimalFormat;
        mInterpolationRows = new ArrayList<>();
        generateDifferences(xvalues, yvalues);
        mBackward = backward;
    }

    private void generateDifferences(String[] xvalues, String[] yvalues) {
        mDifferences = new double[xvalues.length + 1][xvalues.length];
        for (i = 0; i < xvalues.length; i++) {
            mDifferences[0][i] = Double.parseDouble(xvalues[i]);
            mDifferences[1][i] = Double.parseDouble(yvalues[i]);
        }
        int nextRowLength = xvalues.length - 1;
        for (i = 2; i <= xvalues.length; i++) {
            for (j = 0; j < nextRowLength; j++) {
                mDifferences[i][j] = mDifferences[i-1][j+1] - mDifferences[i-1][j];
            }
            nextRowLength--;
        }
    }

    void executor() {
        mDifferencesString = new String[mDifferences.length][mDifferences[0].length];
        for (i = 0; i < mDifferences.length; i++) {
            for (j = 0; j < mDifferences[0].length; j++) {
                mDifferencesString[i][j] = mDecimalFormat.format(mDifferences[i][j]);
            }
        }

        String notation;
        if (mBackward) {
            notation = "\u2207";
        } else {
            notation = "\u0394";
        }

        InterpolationRow row = new InterpolationRow("x\u1D62", mDifferencesString[0], 0);
        mInterpolationRows.add(row);
        row = new InterpolationRow("y\u1D62", mDifferencesString[1], 0);
        mInterpolationRows.add(row);
        for (i = 2; i < mDifferencesString.length; i++) {
            row = new InterpolationRow(notation + getOrder(i) + "y\u1D62", mDifferencesString[i], i-1);
            mInterpolationRows.add(row);
        }
        mInterpolationOutputActivity.fillRecyclerView(mInterpolationRows);
    }

    void newtonForward() {
        mStepSize = mDifferences[0][1] - mDifferences[0][0];
        mP = ((mX - mDifferences[0][0]) / mStepSize);
        mP = Double.parseDouble(mDecimalFormat.format(mP));
        mY = mDifferences[1][0];
        for (int i = 1; i < 5; i++) {
            mY = mY + Double.parseDouble(mDecimalFormat.format((getNewtonPTerms(i, -1) * mDifferences[i+1][0]) / getFactorial(i)));
        }
        mInterpolationOutputActivity.insertAnswerIntoChip(mY);
    }

    void newtonBackward() {
        mStepSize = mDifferences[0][1] - mDifferences[0][0];
        mP = ((mX - mDifferences[0][mDifferences[0].length - 1]) / mStepSize);
        mP = Double.parseDouble(mDecimalFormat.format(mP));
        mY = mDifferences[1][mDifferences[0].length - 1];
        int lastIndex = mDifferences[0].length - 2;
        for (i = 1; i < mDifferences[0].length; i++) {
            mY = mY + Double.parseDouble(mDecimalFormat.format((getNewtonPTerms(i, 1) * mDifferences[i+1][lastIndex]) / getFactorial(i)));
            lastIndex--;
        }
        mInterpolationOutputActivity.insertAnswerIntoChip(mY);
    }

    private double getNewtonPTerms(int term, int sign) {
        double temporaryP = mP;
        int changeBy = 1;
        while (term > 1) {
            temporaryP = temporaryP * (mP + (sign * changeBy));
            temporaryP = Double.parseDouble(mDecimalFormat.format(temporaryP));
            changeBy++;
            term--;
        }
        return temporaryP;
    }

    void stirling() {
        mStepSize = mDifferences[0][1] - mDifferences[0][0];
        int pivotIndex = getPivotIndex();
        mP = (mX - mDifferences[0][pivotIndex]) / mStepSize;
        mP = Double.parseDouble(mDecimalFormat.format(mP));
        mY = mDifferences[1][pivotIndex];
        for (i = 1; i < mDifferences[1].length; i++) {
            if ((i > 1) && ((i & 1) == 1)) {
                pivotIndex--;
            }
            try {
                if ((i & 1) == 1) {
                    mY = mY + (getStirlingPTerms(i) * ((mDifferences[i+1][pivotIndex-1] + mDifferences[i+1][pivotIndex]) / (2 * getFactorial(i))));
                } else {
                    mY = mY + (getStirlingPTerms(i) * (mDifferences[i+1][pivotIndex-1]) / getFactorial(i));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
            mY = Double.parseDouble(mDecimalFormat.format(mY));
        }
        mInterpolationOutputActivity.insertAnswerIntoChip(mY);
    }

    private int getPivotIndex() {
        int index = 0;
        if ((mDifferences[0].length & 1) == 1) {
            index = mDifferences[0].length / 2;
        } else {
            double lowestDifference = Math.abs(mX - mDifferences[0][0]);
            for (i = 1; i < mDifferences[0].length; i++) {
                if (lowestDifference > Math.abs(mX - mDifferences[0][i])) {
                    lowestDifference = Math.abs(mX - mDifferences[0][i]);
                    index = i;
                }
            }
        }
        return index;
    }

    private double getStirlingPTerms(int term) {
        double temporaryP;
        double pSquare = Math.pow(mP, 2);
        pSquare = Double.parseDouble(mDecimalFormat.format(pSquare));
        int toSubtract = 1;
        if ((term & 1) == 1) {
            temporaryP = mP;
        } else {
            temporaryP = pSquare;
        }
        while (term > 2) {
            temporaryP = temporaryP * (pSquare - Math.pow(toSubtract, 2));
            temporaryP = Double.parseDouble(mDecimalFormat.format(temporaryP));
            toSubtract++;
            term = term - 2;
        }
        return temporaryP;
    }

    private int getFactorial(int number) {
        int factorial = 1;
        while (number > 1) {
            factorial = factorial * number;
            number--;
        }
        return factorial;
    }

    private String getOrder(int row) {
        row--;
        switch (row) {
            case 1:
                return "";

            case 2:
                return "\u00B2";

            case 3:
                return "\u00B3";

            case 4:
                return "\u2074";

            case 5:
                return "\u2075";

            case 6:
                return "\u2076";

            case 7:
                return "\u2077";

            case 8:
                return "\u2078";

            case 9:
                return "\u2079";
        }
        return "";
    }
}
