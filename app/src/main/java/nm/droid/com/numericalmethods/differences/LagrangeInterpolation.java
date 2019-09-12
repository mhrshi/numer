package nm.droid.com.numericalmethods.differences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class LagrangeInterpolation {

    private double mX;
    private double[][] mDifferences;
    private String[][] mDifferencesString;
    private DecimalFormat mDecimalFormat;
    private InterpolationOutputActivity mInterpolationOutputActivity;
    private List<InterpolationRow> mInterpolationRows;
    private double mY;
    private int i;
    private int j;

    LagrangeInterpolation(InterpolationOutputActivity interpolationOutputActivity, String[] xvalues, String[] yvalues, double x, DecimalFormat decimalFormat) {
        mInterpolationOutputActivity = interpolationOutputActivity;
        mX = x;
        mDecimalFormat = decimalFormat;
        mInterpolationRows = new ArrayList<>();
        generateTabularForm(xvalues, yvalues);
    }

    private void generateTabularForm(String[] xvalues, String[] yvalues) {
        mDifferences = new double[2][xvalues.length];
        for (i = 0; i < xvalues.length; i++) {
            mDifferences[0][i] = Double.parseDouble(xvalues[i]);
            mDifferences[1][i] = Double.parseDouble(yvalues[i]);
        }
    }

    void executor() {
        mDifferencesString = new String[3][mDifferences[0].length];
        for (i = 0; i < 2; i++) {
            for (j = 0; j < mDifferences[0].length; j++) {
                mDifferencesString[i][j] = mDecimalFormat.format(mDifferences[i][j]);
            }
        }

        InterpolationRow row = new InterpolationRow("x\u1D62", mDifferencesString[0], 0);
        mInterpolationRows.add(row);
        row = new InterpolationRow("y\u1D62", mDifferencesString[1], 0);
        mInterpolationRows.add(row);

        mY = 0;
        for (i = 0; i < mDifferences[0].length; i++) {
            mY = mY + getTermValue(i);
            mY = Double.parseDouble(mDecimalFormat.format(mY));
        }
        row = new InterpolationRow("y(" + mX + ")", mDifferencesString[2], 0);
        mInterpolationRows.add(row);
        mInterpolationOutputActivity.fillRecyclerView(mInterpolationRows);
        mInterpolationOutputActivity.insertAnswerIntoChip(mY);
    }

    private double getTermValue(int term) {
        double numerator = 1;
        double denominator = 1;
        for (j = 0; j < mDifferences[0].length; j++) {
            if (j == term) {
                continue;
            }
            numerator = numerator * (mX - mDifferences[0][j]);
            numerator = Double.parseDouble(mDecimalFormat.format(numerator));
            denominator = denominator * (mDifferences[0][term] - mDifferences[0][j]);
            denominator = Double.parseDouble(mDecimalFormat.format(denominator));
        }
        double termValue = numerator / denominator * mDifferences[1][term];
        mDifferencesString[2][term] = mDecimalFormat.format(termValue);
        return Double.parseDouble(mDecimalFormat.format(termValue));
    }

}
