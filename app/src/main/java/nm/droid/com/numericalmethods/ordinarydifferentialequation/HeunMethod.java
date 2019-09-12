package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class HeunMethod {
    private Function mFunction;
    private DecimalFormat mDecimalFormat;
    private double mPreviousX;
    private double mNextX;
    private double mPreviousY;
    private double mNextY;
    private double mNextYStar;
    private double mXI;
    private double mStepSize;

    private OrdinaryDifferentialOutputActivity mOrdinaryDifferentialOutputActivity;

    private List<OrdinaryDifferentialRow> mOrdinaryDifferentialRows;

    HeunMethod(OrdinaryDifferentialOutputActivity ordinaryDifferentialOutputActivity, String functionString, double x0, double y0, double xI, double stepSize, DecimalFormat decimalFormat) {
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
        OrdinaryDifferentialRow row = new OrdinaryDifferentialRow("itr.", "x", "y*", "y");
        mOrdinaryDifferentialRows.add(row);
        int step = 1;
        while (mNextX != mXI) {
            mPreviousX = mNextX;
            mPreviousY = mNextY;
            calculateStepValues();
            row = new OrdinaryDifferentialRow(Integer.toString(step), mDecimalFormat.format(mNextX), mDecimalFormat.format(mNextYStar), mDecimalFormat.format(mNextY));
            mOrdinaryDifferentialRows.add(row);
            step++;
            if (step == 51) {
                mOrdinaryDifferentialOutputActivity.abortShip();
                break;
            }
        }
        mOrdinaryDifferentialOutputActivity.insertAnswerIntoChip(mNextY);
        mOrdinaryDifferentialOutputActivity.fillRecyclerViewHeun(mOrdinaryDifferentialRows);
    }

    private void calculateStepValues() {
        mNextX = Double.parseDouble(mDecimalFormat.format(mPreviousX + mStepSize));
        double fPreviousXY = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mPreviousX, mPreviousY)));
        mNextYStar = mPreviousY + (mStepSize * fPreviousXY);
        mNextYStar = Double.parseDouble(mDecimalFormat.format(mNextYStar));
        double fNextXYStar = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mNextX, mNextYStar)));
        mNextY = mPreviousY + ((mStepSize / 2) * (fPreviousXY + fNextXYStar));
        mNextY = Double.parseDouble(mDecimalFormat.format(mNextY));
    }
}
