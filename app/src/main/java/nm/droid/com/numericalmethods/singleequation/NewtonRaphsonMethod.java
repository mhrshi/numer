package nm.droid.com.numericalmethods.singleequation;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class NewtonRaphsonMethod {

    private String mFunctionString;
    private Function mFunction;
    private static double mLeftEndPoint;
    private static double mRightEndPoint;
    private static double mXN1;
    private static double mXN2;
    private int mCompareHelper;
    private DecimalFormat mDecimalFormat;
    private List<NewtonRaphsonRow> mNewtonRaphsonRows;

    private SingleEquationOutputActivity mSingleEquationOutputActivity;
    private ReciprocalOutputActivity mReciprocalOutputActivity;

    NewtonRaphsonMethod(SingleEquationOutputActivity singleEquationOutputActivity, Function function, double leftEndPoint, double rightEndPoint, DecimalFormat decimalFormat, int precision) {
        mSingleEquationOutputActivity = singleEquationOutputActivity;
        mFunction = function;
        mFunctionString = mFunction.getFunctionExpressionString();
        mLeftEndPoint = leftEndPoint;
        mRightEndPoint = rightEndPoint;
        mDecimalFormat = decimalFormat;
        mCompareHelper = (int) Math.pow(10, precision);
        mXN1 = -1;
        mXN2 = Double.parseDouble(mDecimalFormat.format((mLeftEndPoint + mRightEndPoint) / 2));
        mNewtonRaphsonRows = new ArrayList<>();
    }

    NewtonRaphsonMethod(ReciprocalOutputActivity reciprocalOutputActivity, double reciprocalOf, double initialApprox, DecimalFormat decimalFormat, int precision) {
        mReciprocalOutputActivity = reciprocalOutputActivity;
        mFunctionString = "(1/x)-(" + reciprocalOf + ")";
        mFunction = new Function("mFunction", mFunctionString, "x");
        mXN1 = -1;
        mXN2 = initialApprox;
        mDecimalFormat = decimalFormat;
        mCompareHelper = (int) Math.pow(10, precision);
        mNewtonRaphsonRows = new ArrayList<>();
    }

    void stepExecutor() {
        NewtonRaphsonRow row = new NewtonRaphsonRow("itr.", "x");
        mNewtonRaphsonRows.add(row);
        int step = 1;
        while ((Math.floor(mXN1 * mCompareHelper) / mCompareHelper) != (Math.floor(mXN2 * mCompareHelper) / mCompareHelper)) {
            mXN1 = mXN2;
            try {
                calculateStepValues();
            } catch (NumberFormatException e) {
                mSingleEquationOutputActivity.reciprocalSnackBar();
                return;
            }
            row = new NewtonRaphsonRow(Integer.toString(step),
                    mDecimalFormat.format(mXN2));
            mNewtonRaphsonRows.add(row);
            step++;
            if (step == 51) {
                if (mSingleEquationOutputActivity != null) {
                    mSingleEquationOutputActivity.abortShip();
                } else {
                    mReciprocalOutputActivity.abortShip();
                }
                break;
            }
        }
        if (mSingleEquationOutputActivity != null) {
            mSingleEquationOutputActivity.insertAnswerIntoChip(mXN2);
            mSingleEquationOutputActivity.fillRecyclerViewNR(mNewtonRaphsonRows);
        } else {
            mReciprocalOutputActivity.insertAnswerIntoChip(mXN2);
            mReciprocalOutputActivity.fillRecyclerViewNR(mNewtonRaphsonRows);
        }
    }

    private void calculateStepValues() {
        double fXN1 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXN1)));
        String derivativeString = "der(" + mFunctionString + ", x, " + mXN1 + ")";
        Expression derivative = new Expression(derivativeString);
        double fDXN1 = Double.parseDouble(mDecimalFormat.format(derivative.calculate()));
        mXN2 = Double.parseDouble(mDecimalFormat.format(mXN1 - Double.parseDouble(mDecimalFormat.format(fXN1 / fDXN1))));
    }
}
