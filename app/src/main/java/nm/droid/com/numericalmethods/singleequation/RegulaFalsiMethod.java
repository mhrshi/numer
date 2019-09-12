package nm.droid.com.numericalmethods.singleequation;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class RegulaFalsiMethod {

    private Function mFunction;
    private static double mXN1;
    private static double mXN2;
    private static double mXN3;
    private static double mFXN3;
    private static double mPreviousXN3;
    private SingleEquationOutputActivity mSingleEquationOutputActivity;
    private int mCompareHelper;
    private DecimalFormat mDecimalFormat;
    private List<SingleEquationRow> mSingleEquationRows;

    RegulaFalsiMethod(SingleEquationOutputActivity singleEquationOutputActivity, Function function, double xn1, double xn2, DecimalFormat decimalFormat, int precision) {
        mSingleEquationOutputActivity = singleEquationOutputActivity;
        mFunction = function;
        mXN1 = xn1;
        mXN2 = xn2;
        mDecimalFormat = decimalFormat;
        mCompareHelper = (int) Math.pow(10, precision);
        mPreviousXN3 = -1;
        mSingleEquationRows = new ArrayList<>();
    }

    void stepExecutor() {
        SingleEquationRow row = new SingleEquationRow("itr.", "LEP", "REP", "x", "f(x)");
        mSingleEquationRows.add(row);
        int step = 2;
        while ((Math.floor(mPreviousXN3 * mCompareHelper) / mCompareHelper) != (Math.floor(mXN3 * mCompareHelper) / mCompareHelper)) {
            mPreviousXN3 = mXN3;
            calculateStepValues();
            row = new SingleEquationRow(Integer.toString(step),
                    mDecimalFormat.format(mXN1),
                    mDecimalFormat.format(mXN2),
                    mDecimalFormat.format(mXN3),
                    mDecimalFormat.format(mFXN3));
            mSingleEquationRows.add(row);
            determineNextInterval();
            step++;
            if (step == 51) {
                mSingleEquationOutputActivity.abortShip();
                break;
            }
        }
        mSingleEquationOutputActivity.insertAnswerIntoChip(mXN3);
        mSingleEquationOutputActivity.fillRecyclerView(mSingleEquationRows);
    }

    private void calculateStepValues() {
        double fmXN1 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN1)));
        double fmXN2 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN2)));
        mXN3 = (((mXN1 * fmXN2) - (mXN2 * fmXN1)) / (fmXN2 - fmXN1));
        mXN3 = Double.parseDouble(mDecimalFormat.format(mXN3));
        mFXN3 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN3)));
    }

    private void determineNextInterval() {
        double fmXN1 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN1)));
        double fmXN2 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN2)));

        if ((mFXN3 < 0) && (fmXN1 < 0)) {
            mXN1 = (mFXN3 > fmXN1) ? mXN3 : mXN1;
        } else if ((mFXN3 > 0) && (fmXN1 > 0)) {
            mXN1 = (mFXN3 < fmXN1) ? mXN3 : mXN1;
        }

        if ((mFXN3 > 0) && (fmXN2 > 0)) {
            mXN2 = (mFXN3 < fmXN2) ? mXN3 : mXN2;
        } else if ((mFXN3 < 0) && (fmXN2 < 0)) {
            mXN2 = (mFXN3 > fmXN2) ? mXN3 : mXN2;
        }
    }
}
