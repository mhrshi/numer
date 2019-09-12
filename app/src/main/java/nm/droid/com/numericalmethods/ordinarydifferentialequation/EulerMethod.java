package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class EulerMethod {

    private Function mFunction;
    private DecimalFormat mDecimalFormat;
    private double mPreviousX;
    private double mNextX;
    private double mPreviousY;
    private double mNextY;
    private double mXI;
    private double mStepSize;

    private OrdinaryDifferentialOutputActivity mOrdinaryDifferentialOutputActivity;

    private List<OrdinaryDifferentialRow> mOrdinaryDifferentialRows;

    EulerMethod(OrdinaryDifferentialOutputActivity ordinaryDifferentialOutputActivity, String functionString, double x0, double y0, double xI, double stepSize, DecimalFormat decimalFormat) {
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
        OrdinaryDifferentialRow row = new OrdinaryDifferentialRow("itr.", "x", "y");
        mOrdinaryDifferentialRows.add(row);
        int step = 1;
        while (mNextX != mXI) {
            mPreviousX = mNextX;
            mPreviousY = mNextY;
            calculateStepValues();
            row = new OrdinaryDifferentialRow(Integer.toString(step), mDecimalFormat.format(mNextX), mDecimalFormat.format(mNextY));
            mOrdinaryDifferentialRows.add(row);
            step++;
            if (step == 51) {
                mOrdinaryDifferentialOutputActivity.abortShip();
                break;
            }
        }
        mOrdinaryDifferentialOutputActivity.insertAnswerIntoChip(mNextY);
        mOrdinaryDifferentialOutputActivity.fillRecyclerViewEuler(mOrdinaryDifferentialRows);
    }

    private void calculateStepValues() {
        mNextX = mPreviousX + mStepSize;
        mNextX = Double.parseDouble(mDecimalFormat.format(mNextX));
        mNextY = mPreviousY + (mStepSize*(mFunction.calculate(mPreviousX, mPreviousY)));
        mNextY = Double.parseDouble(mDecimalFormat.format(mNextY));
    }
}
