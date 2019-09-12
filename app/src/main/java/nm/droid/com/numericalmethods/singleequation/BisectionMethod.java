package nm.droid.com.numericalmethods.singleequation;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class BisectionMethod {

    private Function mFunction;
    private static double mLeftEndPoint;
    private static double mRightEndPoint;
    private static double mX;
    private static double mPreviousX;
    private static double mFX;
    private SingleEquationOutputActivity mSingleEquationOutputActivity;
    private int mCompareHelper;
    private DecimalFormat mDecimalFormat;
    private List<SingleEquationRow> mSingleEquationRows;

    BisectionMethod(SingleEquationOutputActivity singleEquationOutputActivity, Function function, double leftEndPoint, double rightEndPoint, DecimalFormat decimalFormat, int precision) {
        mSingleEquationOutputActivity = singleEquationOutputActivity;
        mFunction = function;
        mLeftEndPoint = leftEndPoint;
        mRightEndPoint = rightEndPoint;
        mDecimalFormat = decimalFormat;
        mX = -1;
        mCompareHelper = (int) Math.pow(10, precision);
        mSingleEquationRows = new ArrayList<>();
    }

    void stepExecutor() {
        SingleEquationRow row = new SingleEquationRow("itr.", "LEP", "REP", "x", "f(x)");
        mSingleEquationRows.add(row);
        int step = 1;
        while ((Math.floor(mPreviousX * mCompareHelper) / mCompareHelper) != (Math.floor(mX * mCompareHelper) / mCompareHelper)) {
            mPreviousX = mX;
            calculateStepValues();
            row = new SingleEquationRow(Integer.toString(step),
                            mDecimalFormat.format(mLeftEndPoint),
                            mDecimalFormat.format(mRightEndPoint),
                            mDecimalFormat.format(mX),
                            mDecimalFormat.format(mFX));
            mSingleEquationRows.add(row);
            determineNextInterval();
            step++;
            if (step == 51) {
                mSingleEquationOutputActivity.abortShip();
                break;
            }
        }
        mSingleEquationOutputActivity.insertAnswerIntoChip(mX);
        mSingleEquationOutputActivity.fillRecyclerView(mSingleEquationRows);
    }

    private void calculateStepValues() {
        mX = Double.parseDouble(mDecimalFormat.format(((mLeftEndPoint + mRightEndPoint) / 2)));
        mFX = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mX)));
    }

    private void determineNextInterval() {
        double fLeftEndPoint = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mLeftEndPoint)));
        double fRightEndPoint = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mRightEndPoint)));

        if ((mFX < 0) && (fLeftEndPoint < 0)) {
            mLeftEndPoint = (mFX > fLeftEndPoint) ? mX : mLeftEndPoint;
        } else if ((mFX > 0) && (fLeftEndPoint > 0)) {
            mLeftEndPoint = (mFX < fLeftEndPoint) ? mX : mLeftEndPoint;
        }

        if ((mFX > 0) && (fRightEndPoint > 0)) {
            mRightEndPoint = (mFX < fRightEndPoint) ? mX : mRightEndPoint;
        } else if ((mFX < 0) && (fRightEndPoint < 0)) {
            mRightEndPoint = (mFX > fRightEndPoint) ? mX : mRightEndPoint;
        }
    }
}
