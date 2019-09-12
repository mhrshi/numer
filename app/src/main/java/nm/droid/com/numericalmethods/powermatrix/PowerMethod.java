package nm.droid.com.numericalmethods.powermatrix;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class PowerMethod {

    private int[][] mMatrix;
    double[][] mXBar;
    double[][] mPreviousXBar;
    double mDominant;
    double mPreviousDominant;
    private int mSize;

    private int mPrecision;
    private int mCompareHelper;
    private DecimalFormat mDecimalFormat;
    private PowerOutputActivity mPowerOutputActivity;

    private List<PowerRow> mPowerRows;

    PowerMethod(PowerOutputActivity powerOutputActivity, String matrixString, DecimalFormat decimalFormat, int precision) {
        mPowerOutputActivity = powerOutputActivity;
        mDecimalFormat = decimalFormat;
        mPrecision = precision;
        mCompareHelper = (int) Math.pow(10, precision);
        mPreviousDominant = -1;
        activateMatrices(matrixString);
        mPowerRows = new ArrayList<>();
    }

    private void activateMatrices(String matrixString) {
        int i, j, k;
        i = j = k = 0;
        String[] elements = matrixString.split("\\s+");
        mSize = (int) Math.sqrt(elements.length);
        mMatrix = new int[mSize][mSize];
        for (i = 0; i < mSize; i++) {
            for (j = 0; j < mSize; j++) {
                mMatrix[i][j] = Integer.parseInt(elements[k]);
                k++;
            }
        }
        mXBar = new double[mSize][1];
        mPreviousXBar = new double[mSize][1];
        for (i = 0; i < mSize; i++) {
            mPreviousXBar[i][0] = 1;
        }
    }

    void startCorrespondingStepExecutor() {
        if (mSize == 2) {
            stepExecutorTwo();
        } else {
            stepExecutorThree();
        }
    }

    private void stepExecutorTwo() {
        int i;
        int step = 1;
        PowerRow row;
        while ((Math.floor(mDominant * mCompareHelper) / mCompareHelper) != (Math.floor(mPreviousDominant * mCompareHelper) / mCompareHelper)) {
            mPreviousDominant = mDominant;
            matrixMultiply();
            mDominant = findDominant();
            generateVector();
            String[] rowZero = {Integer.toString(mMatrix[0][0]), Integer.toString(mMatrix[0][1]),
                                "x", mDecimalFormat.format(mPreviousXBar[0][0]),
                                "=", mDecimalFormat.format(mDominant), "x", mDecimalFormat.format(mXBar[0][0])};
            String[] rowOne = {Integer.toString(mMatrix[1][0]), Integer.toString(mMatrix[1][1]),
                                " ", mDecimalFormat.format(mPreviousXBar[1][0]),
                                " ", " ", " ", mDecimalFormat.format(mXBar[1][0])};
            row = new PowerRow("Iteration " + step, rowZero, rowOne);
            mPowerRows.add(row);
            step++;
            if (step == 51) {
                mPowerOutputActivity.abortShip();
                break;
            }
            for (i = 0; i < mSize; i++) {
                mPreviousXBar[i][0] = mXBar[i][0];
            }
        }
        mPowerOutputActivity.insertAnswerIntoChip(mDominant);
        mPowerOutputActivity.fillRecyclerViewTwo(mPowerRows);
    }

    private void stepExecutorThree() {
        int i;
        int step = 1;
        PowerRow row;
        while ((Math.floor(mDominant * mCompareHelper) / mCompareHelper) != (Math.floor(mPreviousDominant * mCompareHelper) / mCompareHelper)) {
            mPreviousDominant = mDominant;
            matrixMultiply();
            mDominant = findDominant();
            generateVector();
            String[] rowZero = {Integer.toString(mMatrix[0][0]), Integer.toString(mMatrix[0][1]), Integer.toString(mMatrix[0][2]),
                                " ", mDecimalFormat.format(mPreviousXBar[0][0]),
                                " ", " ", "", mDecimalFormat.format(mXBar[0][0])};
            String[] rowOne = {Integer.toString(mMatrix[1][0]), Integer.toString(mMatrix[1][1]), Integer.toString(mMatrix[1][2]),
                                "x", mDecimalFormat.format(mPreviousXBar[1][0]),
                                "=",  mDecimalFormat.format(mDominant), "x", mDecimalFormat.format(mXBar[1][0])};
            String[] rowTwo = {Integer.toString(mMatrix[2][0]), Integer.toString(mMatrix[2][1]), Integer.toString(mMatrix[2][2]),
                                " ", mDecimalFormat.format(mPreviousXBar[2][0]),
                                " ", " ", " ", mDecimalFormat.format(mXBar[2][0])};
            row = new PowerRow("Iteration " + step, rowZero, rowOne, rowTwo);
            mPowerRows.add(row);
            step++;
            if (step == 51) {
                mPowerOutputActivity.abortShip();
                break;
            }
            for (i = 0; i < mSize; i++) {
                mPreviousXBar[i][0] = mXBar[i][0];
            }
        }
        mPowerOutputActivity.insertAnswerIntoChip(mDominant);
        mPowerOutputActivity.fillRecyclerViewThree(mPowerRows);
    }

    private void matrixMultiply() {
        int i, j;
        for (i = 0; i < mSize; i++) {
            mXBar[i][0] = 0;
            for (j = 0; j < mSize; j++) {
                mXBar[i][0] = mXBar[i][0] + Double.parseDouble(mDecimalFormat.format(mMatrix[i][j] * mPreviousXBar[j][0]));
                mXBar[i][0] = Double.parseDouble(mDecimalFormat.format(mXBar[i][0]));
            }
        }
    }

    private double findDominant() {
        int i;
        double greatest = Math.abs(mXBar[0][0]);
        for (i = 1; i < mSize; i++) {
            if (Math.abs(mXBar[i][0]) > greatest) {
                greatest = Math.abs(mXBar[i][0]);
            }
        }
        return greatest;
    }

    private void generateVector() {
        int i;
        for (i = 0; i < mSize; i++) {
            mXBar[i][0] = (mXBar[i][0] / mDominant);
            mXBar[i][0] = Double.parseDouble(mDecimalFormat.format(mXBar[i][0]));
        }
    }
}
