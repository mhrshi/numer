package nm.droid.com.numericalmethods.differences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class NewtonDividedDifference {

    private double mX;
    private double[][] mDifferences;
    private String[][] mDifferencesString;
    private DecimalFormat mDecimalFormat;
    private InterpolationOutputActivity mInterpolationOutputActivity;
    private List<InterpolationRow> mInterpolationRows;
    private double mY;
    private int i;
    private int j;

    NewtonDividedDifference(InterpolationOutputActivity interpolationOutputActivity, String[] xvalues, String[] yvalues, double x, DecimalFormat decimalFormat) {
        mInterpolationOutputActivity = interpolationOutputActivity;
        mX = x;
        mDecimalFormat = decimalFormat;
        mInterpolationRows = new ArrayList<>();
        generateDifferences(xvalues, yvalues);
    }

    private void generateDifferences(String[] xvalues, String[] yvalues) {
        int minuendIndex;
        mDifferences = new double[xvalues.length + 1][xvalues.length];
        for (i = 0; i < xvalues.length; i++) {
            mDifferences[0][i] = Double.parseDouble(xvalues[i]);
            mDifferences[1][i] = Double.parseDouble(yvalues[i]);
        }
        int nextRowLength = xvalues.length - 1;
        for (i = 2; i <= xvalues.length; i++) {
            minuendIndex = i - 1;
            for (j = 0; j < nextRowLength; j++, minuendIndex++) {
                mDifferences[i][j] = (mDifferences[i-1][j+1] - mDifferences[i-1][j]) / (mDifferences[0][minuendIndex] - mDifferences[0][j]);
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

        InterpolationRow row = new InterpolationRow("x\u1D62", mDifferencesString[0], 0);
        mInterpolationRows.add(row);
        row = new InterpolationRow("y\u1D62", mDifferencesString[1], 0);
        mInterpolationRows.add(row);
        for (i = 2; i < mDifferencesString.length; i++) {
            row = new InterpolationRow("dd" + getOrder(i-1) , mDifferencesString[i], i-1);
            mInterpolationRows.add(row);
        }
        mInterpolationOutputActivity.fillRecyclerView(mInterpolationRows);

        mY = mDifferences[1][0];
        for (i = 1; i < mDifferences[1].length; i++) {
            mY = mY + (getXTerms(i) * mDifferences[i+1][0]);
            mY = Double.parseDouble(mDecimalFormat.format(mY));
        }
        mInterpolationOutputActivity.insertAnswerIntoChip(mY);
    }

    private double getXTerms(int term) {
        double xTermsValue = 1;
        for (j = 0; j < term; j++) {
            xTermsValue = xTermsValue * (mX - mDifferences[0][j]);
            xTermsValue = Double.parseDouble(mDecimalFormat.format(xTermsValue));
        }
        return xTermsValue;
    }

    private String getOrder(int row) {
        switch (row) {
            case 1:
                return "\u00B9";

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
