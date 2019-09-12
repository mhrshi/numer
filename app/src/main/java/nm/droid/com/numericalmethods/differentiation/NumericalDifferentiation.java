package nm.droid.com.numericalmethods.differentiation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class NumericalDifferentiation {

    private double mX;
    private double[][] mDifferences;
    private String[][] mDifferencesString;
    private DecimalFormat mDecimalFormat;
    private DifferentiationOutputActivity mDifferentiationOutputActivity;
    private List<DifferentiationRow> mDifferentiationRows;
    private double mStepSize;
    private double mP;
    private double mYFO;
    private double mYSO;
    private boolean mBackward;
    private int i;
    private int j;

    NumericalDifferentiation(DifferentiationOutputActivity differentiationOutputActivity, String[] xvalues, String[] yvalues, double x, DecimalFormat decimalFormat, boolean backward) {
        mDifferentiationOutputActivity = differentiationOutputActivity;
        mX = x;
        mDecimalFormat = decimalFormat;
        mDifferentiationRows = new ArrayList<>();
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

        DifferentiationRow row = new DifferentiationRow("x\u1D62", mDifferencesString[0], 0);
        mDifferentiationRows.add(row);
        row = new DifferentiationRow("y\u1D62", mDifferencesString[1], 0);
        mDifferentiationRows.add(row);
        for (i = 2; i < mDifferencesString.length; i++) {
            row = new DifferentiationRow(notation + getOrder(i) + "y\u1D62", mDifferencesString[i], i-1);
            mDifferentiationRows.add(row);
        }
        mDifferentiationOutputActivity.fillRecyclerView(mDifferentiationRows);
    }

    void performNewtonForward() {
        mStepSize = mDifferences[0][1] - mDifferences[0][0];
        if (mX == mDifferences[0][0]) {
            newtonForward(0);
        } else if (mX == mDifferences[0][1]) {
            newtonForward(1);
        } else if ((mX > mDifferences[0][0]) && (mX < mDifferences[0][1])) {
            implicitNewtonForward();
        }
    }

    private void newtonForward(int pivotIndex) {
        double outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / mStepSize));
        double innerTermValue = 0;
        for (i = 1; i < mDifferences[1].length; i++) {
            if ((i & 1) == 1) {
                innerTermValue = innerTermValue + (mDifferences[i+1][pivotIndex] / i);
            } else {
                innerTermValue = innerTermValue - (mDifferences[i+1][pivotIndex] / i);
            }
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
        }
        mYFO = outerTermValue * innerTermValue;
        mYFO = Double.parseDouble(mDecimalFormat.format(mYFO));

        outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / (mStepSize * mStepSize)));
        innerTermValue = 0;
        for (i = 1; (i < mDifferences[1].length - 1) && (i <= 4); i++) {
            innerTermValue = innerTermValue + getInnerTermValue(i, pivotIndex, -1);
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
        }
        mYSO = outerTermValue * innerTermValue;
        mYSO = Double.parseDouble(mDecimalFormat.format(mYSO));

        mDifferentiationOutputActivity.insertAnswerIntoChips(mYFO, mYSO);
    }

    private void implicitNewtonForward() {
        mP = (mX - mDifferences[0][0]) / mStepSize;
        mP = Double.parseDouble(mDecimalFormat.format(mP));
        double outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / mStepSize));
        double innerTermValue = mDifferences[2][0];
        for (i = 2; i < 4; i++) {
            switch (i) {
                case 2:
                    innerTermValue = innerTermValue + (((2 * mP) - 1) * mDifferences[3][0] / 2.0);
                    break;

                case 3:
                    innerTermValue = innerTermValue + (((2 * mP * mP) - (6 * mP) + 2) * mDifferences[4][0] / 6.0);
            }
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
        }
        mYFO = outerTermValue * innerTermValue;
        mYFO = Double.parseDouble(mDecimalFormat.format(mYFO));

        mDifferentiationOutputActivity.insertAnswerIntoChips(mYFO, 0);
    }

    void performNewtonBackward() {
        mStepSize = mDifferences[0][1] - mDifferences[0][0];
        if (mX == mDifferences[0][mDifferences[0].length - 1]) {
            newtonBackward(mDifferences[0].length - 2);
        } else if (mX == mDifferences[0][mDifferences[0].length - 2]) {
            newtonBackward(mDifferences[0].length - 3);
        } else if ((mX > mDifferences[0][mDifferences[0].length - 2]) && (mX < mDifferences[0][mDifferences[0].length - 1])) {
            implicitNewtonBackward();
        }
    }

    private void newtonBackward(int pivotIndex) {
        double outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / mStepSize));
        double innerTermValue = 0;
        int lastIndex = pivotIndex;
        for (i = 1; i < mDifferences[1].length; i++) {
            if (lastIndex >= 0) {
                innerTermValue = innerTermValue + (mDifferences[i+1][lastIndex] / i);
            }
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
            lastIndex--;
        }
        mYFO = outerTermValue * innerTermValue;
        mYFO = Double.parseDouble(mDecimalFormat.format(mYFO));

        outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / (mStepSize * mStepSize)));
        innerTermValue = 0;
        for (i = 1; (i < mDifferences[1].length - 1) && (i <= 4); i++) {
            pivotIndex--;
            if (pivotIndex >= 0) {
                innerTermValue = innerTermValue + getInnerTermValue(i, pivotIndex, 1);
            }
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
        }
        mYSO = outerTermValue * innerTermValue;
        mYSO = Double.parseDouble(mDecimalFormat.format(mYSO));

        mDifferentiationOutputActivity.insertAnswerIntoChips(mYFO, mYSO);
    }

    private void implicitNewtonBackward() {
        mP = (mX - mDifferences[0][mDifferences[0].length - 1]) / mStepSize;
        mP = Double.parseDouble(mDecimalFormat.format(mP));
        double outerTermValue = Double.parseDouble(mDecimalFormat.format(1 / mStepSize));
        int lastIndex = mDifferences[1].length - 2;
        double innerTermValue = mDifferences[2][lastIndex];
        for (i = 2; i < 4; i++) {
            lastIndex--;
            switch (i) {
                case 2:
                    innerTermValue = innerTermValue + (((2 * mP) + 1) * mDifferences[3][lastIndex] / 2.0);
                    break;

                case 3:
                    innerTermValue = innerTermValue + (((3 * mP * mP) + (6 * mP) + 2) * mDifferences[4][lastIndex] / 6.0);
            }
            innerTermValue = Double.parseDouble(mDecimalFormat.format(innerTermValue));
        }
        mYFO = outerTermValue * innerTermValue;
        mYFO = Double.parseDouble(mDecimalFormat.format(mYFO));

        mDifferentiationOutputActivity.insertAnswerIntoChips(mYFO, 0);
    }

    private double getInnerTermValue(int term, int pivotIndex, int sign) {
        switch (term) {
            case 1:
                return mDifferences[3][pivotIndex];

            case 2:
                return mDifferences[4][pivotIndex] * sign;

            case 3:
                return Double.parseDouble(mDecimalFormat.format(mDifferences[5][pivotIndex] * Double.parseDouble((mDecimalFormat.format(11.0 / 12.0)))));

            case 4:
                return Double.parseDouble(mDecimalFormat.format(mDifferences[6][pivotIndex] * Double.parseDouble(mDecimalFormat.format(sign * 5.0 / 6.0))));
        }
        return 0;
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
