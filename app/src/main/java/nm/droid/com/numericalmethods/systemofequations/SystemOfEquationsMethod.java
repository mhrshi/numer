package nm.droid.com.numericalmethods.systemofequations;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class SystemOfEquationsMethod {

    private int[][] mSystemMatrix = new int[3][4];
    private boolean[] mStateOfRow = new boolean[3];
    private Function mFunctionOne;
    private Function mFunctionTwo;
    private Function mFunctionThree;

    private double mX;
    private double mPreviousX;
    private double mY;
    private double mPreviousY;
    private double mZ;
    private double mPreviousZ;

    private int mCompareHelper;
    private DecimalFormat mDecimalFormat;
    private SystemOfEquationsOutputActivity mSystemOfEquationsOutputActivity;

    private List<SystemOfEquationsRow> mSystemOfEquationsRows;

    SystemOfEquationsMethod(SystemOfEquationsOutputActivity systemOfEquationsOutputActivity, String systemMatrixString, DecimalFormat decimalFormat, int precision) {
        mSystemOfEquationsOutputActivity = systemOfEquationsOutputActivity;
        mDecimalFormat = decimalFormat;
        mCompareHelper = (int) Math.pow(10, precision);
        stringToSystemMatrix(systemMatrixString);
        mPreviousX = mPreviousY = mPreviousZ = -1;
        mX = mY = mZ = 0;
        mSystemOfEquationsRows = new ArrayList<>();
    }

    private void stringToSystemMatrix(String systemMatrixString) {
        String[] elements = systemMatrixString.split("\\s+");
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                mSystemMatrix[i][j] = Integer.parseInt(elements[k]);
                k++;
            }
        }
    }

    boolean canBeApplied() {
        if (isDiagonallyDominant()) {
            return true;
        } else {
            arrangeSystem();
            if (isDiagonallyDominant()) {
                mSystemOfEquationsOutputActivity.systemTransformed();
                return true;
            } else {
                mSystemOfEquationsOutputActivity.systemCouldNotBeTransformed();
                return false;
            }
        }
    }
    
    void formEquations() {
        DecimalFormat df = new DecimalFormat("+ #;- #");
        String eq1 = mSystemMatrix[0][0] + "x " + df.format(mSystemMatrix[0][1]) + "y " + df.format(mSystemMatrix[0][2]) + "z = " + mSystemMatrix[0][3];
        String eq2 = mSystemMatrix[1][0] + "x " + df.format(mSystemMatrix[1][1]) + "y " + df.format(mSystemMatrix[1][2]) + "z = " + mSystemMatrix[1][3];
        String eq3 = mSystemMatrix[2][0] + "x " + df.format(mSystemMatrix[2][1]) + "y " + df.format(mSystemMatrix[2][2]) + "z = " + mSystemMatrix[2][3];
        mSystemOfEquationsOutputActivity.fillChips(eq1, eq2, eq3);
    }

    void formFunctions() {
        mFunctionOne = new Function("f(y,z) = ((" + mSystemMatrix[0][3] + ")-(" + mSystemMatrix[0][1] + "*y)-(" + mSystemMatrix[0][2] + "*z))/(" + mSystemMatrix[0][0] + ")");
        mFunctionTwo = new Function("f(x,z) = ((" + mSystemMatrix[1][3] + ")-(" + mSystemMatrix[1][0] + "*x)-(" + mSystemMatrix[1][2] + "*z))/(" + mSystemMatrix[1][1] + ")");
        mFunctionThree = new Function("f(x,y) = ((" + mSystemMatrix[2][3] + ")-(" + mSystemMatrix[2][0] + "*x)-(" + mSystemMatrix[2][1] + "*y))/(" + mSystemMatrix[2][2] + ")");
    }

    private boolean isDiagonallyDominant() {
        int row, column;
        int otherSum;
        mStateOfRow[0] = mStateOfRow[1] = mStateOfRow[2] = false;
        for (row = 0; row < 3; row++) {
            otherSum = 0;
            for (column = 0; column < 3; column++) {
                if (row != column) {
                    otherSum = otherSum + Math.abs(mSystemMatrix[row][column]);
                }
            }
            if (Math.abs(mSystemMatrix[row][row]) > otherSum) {
                mStateOfRow[row] = true;
            }
        }
        for (boolean x : mStateOfRow) {
            if (!x) {
                return false;
            }
        }
        return true;
    }

    private void arrangeSystem() {
        int row, column;
        int sumOfRow;
        for (row = 0; row < 3; row++) {
            if(!mStateOfRow[row]) {
                sumOfRow = Math.abs(mSystemMatrix[row][0]) + Math.abs(mSystemMatrix[row][1]) + Math.abs(mSystemMatrix[row][2]);
                for (column = 0; column < 3; column++) {
                    int thisElement = Math.abs(mSystemMatrix[row][column]);
                    if (thisElement > (sumOfRow - thisElement)) {
                        swapRows(row, column);
                    }
                }
            }
        }
    }

    private void swapRows(int a, int b) {
        int temp;
        for (int i = 0; i < 4; i++) {
            temp = mSystemMatrix[a][i];
            mSystemMatrix[a][i] = mSystemMatrix[b][i];
            mSystemMatrix[b][i] = temp;
        }
    }

    void stepExecutorGJ() {
        SystemOfEquationsRow row = new SystemOfEquationsRow("itr.", "x", "y", "z");
        mSystemOfEquationsRows.add(row);
        int step = 1;
        while (((Math.ceil(mPreviousX * mCompareHelper) / mCompareHelper) != (Math.ceil(mX * mCompareHelper) / mCompareHelper)) ||
                ((Math.ceil(mPreviousY * mCompareHelper) / mCompareHelper) != (Math.ceil(mY * mCompareHelper) / mCompareHelper)) ||
                ((Math.ceil(mPreviousZ * mCompareHelper) / mCompareHelper) != (Math.ceil(mZ * mCompareHelper) / mCompareHelper))) {
            mPreviousX = mX;
            mPreviousY = mY;
            mPreviousZ = mZ;
            calculateStepValuesGJ();
            row = new SystemOfEquationsRow(Integer.toString(step),
                    mDecimalFormat.format(mX),
                    mDecimalFormat.format(mY),
                    mDecimalFormat.format(mZ));
            mSystemOfEquationsRows.add(row);
            step++;
            if (step == 51) {
                mSystemOfEquationsOutputActivity.abortShip();
                break;
            }
        }
        mSystemOfEquationsOutputActivity.insertAnswerIntoChip(mX, mY, mZ);
        mSystemOfEquationsOutputActivity.fillRecyclerView(mSystemOfEquationsRows);
    }

    void stepExecutorGS() {
        SystemOfEquationsRow row = new SystemOfEquationsRow("itr.", "x", "y", "z");
        mSystemOfEquationsRows.add(row);
        int step = 1;
        while (((Math.ceil(mPreviousX * mCompareHelper) / mCompareHelper) != (Math.ceil(mX * mCompareHelper) / mCompareHelper)) ||
                ((Math.ceil(mPreviousY * mCompareHelper) / mCompareHelper) != (Math.ceil(mY * mCompareHelper) / mCompareHelper)) ||
                ((Math.ceil(mPreviousZ * mCompareHelper) / mCompareHelper) != (Math.ceil(mZ * mCompareHelper) / mCompareHelper))) {
            mPreviousX = mX;
            mPreviousY = mY;
            mPreviousZ = mZ;
            calculateStepValuesGS();
            row = new SystemOfEquationsRow(Integer.toString(step),
                    mDecimalFormat.format(mX),
                    mDecimalFormat.format(mY),
                    mDecimalFormat.format(mZ));
            mSystemOfEquationsRows.add(row);
            step++;
            if (step == 51) {
                mSystemOfEquationsOutputActivity.abortShip();
                break;
            }
        }
        mSystemOfEquationsOutputActivity.insertAnswerIntoChip(mX, mY, mZ);
        mSystemOfEquationsOutputActivity.fillRecyclerView(mSystemOfEquationsRows);
    }

    private void calculateStepValuesGJ() {
        mX = Double.parseDouble(mDecimalFormat.format(mFunctionOne.calculate(mPreviousY, mPreviousZ)));
        mY = Double.parseDouble(mDecimalFormat.format(mFunctionTwo.calculate(mPreviousX, mPreviousZ)));
        mZ = Double.parseDouble(mDecimalFormat.format(mFunctionThree.calculate(mPreviousX, mPreviousY)));
    }

    private void calculateStepValuesGS() {
        mX = Double.parseDouble(mDecimalFormat.format(mFunctionOne.calculate(mY, mZ)));
        mY = Double.parseDouble(mDecimalFormat.format(mFunctionTwo.calculate(mX, mZ)));
        mZ = Double.parseDouble(mDecimalFormat.format(mFunctionThree.calculate(mX, mY)));
    }

}
