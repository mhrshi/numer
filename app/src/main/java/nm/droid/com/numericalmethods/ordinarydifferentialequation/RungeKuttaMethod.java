package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class RungeKuttaMethod {
    private Function mFunction;
    private DecimalFormat mDecimalFormat;
    private double mPreviousX;
    private double mNextX;
    private double mPreviousY;
    private double mNextY;
    private double mK1;
    private double mK2;
    private double mK3;
    private double mK4;
    private double mXI;
    private double mStepSize;

    private OrdinaryDifferentialOutputActivity mOrdinaryDifferentialOutputActivity;

    private List<OrdinaryDifferentialRow> mOrdinaryDifferentialRows;

    RungeKuttaMethod(OrdinaryDifferentialOutputActivity ordinaryDifferentialOutputActivity, String functionString, double x0, double y0, double xI, double stepSize, DecimalFormat decimalFormat) {
        mOrdinaryDifferentialOutputActivity = ordinaryDifferentialOutputActivity;
        mFunction = new Function("f(x,y)=" + functionString);
        mNextX = x0;
        mNextY = y0;
        mXI = xI;
        mStepSize = stepSize;
        mDecimalFormat = decimalFormat;
        mOrdinaryDifferentialRows = new ArrayList<>();
    }

    void stepExecutor() {
        OrdinaryDifferentialRow row = new OrdinaryDifferentialRow("itr.", "x", "k1", "k2", "k3", "k4", "y");
        mOrdinaryDifferentialRows.add(row);
        int step = 1;
        while (mNextX != mXI) {
            mPreviousX = mNextX;
            mPreviousY = mNextY;
            calculateStepValues();
            row = new OrdinaryDifferentialRow(Integer.toString(step), mDecimalFormat.format(mNextX),
                                                mDecimalFormat.format(mK1), mDecimalFormat.format(mK2),
                                                mDecimalFormat.format(mK3), mDecimalFormat.format(mK4), mDecimalFormat.format(mNextY));
            mOrdinaryDifferentialRows.add(row);
            step++;
            if (step == 51) {
                mOrdinaryDifferentialOutputActivity.abortShip();
                break;
            }
        }
        mOrdinaryDifferentialOutputActivity.insertAnswerIntoChip(mNextY);
        mOrdinaryDifferentialOutputActivity.fillRecyclerViewRK(mOrdinaryDifferentialRows);
    }

    private void calculateStepValues() {
        mNextX = Double.parseDouble(mDecimalFormat.format(mPreviousX + mStepSize));
        mK1 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mPreviousX, mPreviousY)));
        mK2 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate((mPreviousX + (mStepSize / 2)), (mPreviousY + ((mK1 * mStepSize)/ 2)))));
        mK3 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate((mPreviousX + (mStepSize / 2)), (mPreviousY + ((mK2 * mStepSize)/ 2)))));
        mK4 = Double.parseDouble(mDecimalFormat.format(mFunction.calculate((mPreviousX + mStepSize), (mPreviousY + (mK3 * mStepSize)))));
        mNextY = Double.parseDouble(mDecimalFormat.format(mPreviousY + (mStepSize * ((mK1 / 6.0) + (mK2 / 3.0) + (mK3 / 3.0) + (mK4 / 6.0)))));
    }
}
