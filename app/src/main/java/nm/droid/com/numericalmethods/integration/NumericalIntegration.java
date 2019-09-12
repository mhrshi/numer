package nm.droid.com.numericalmethods.integration;

import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class NumericalIntegration {

    private Function mFunction;
    private int mSteps;
    private double mStepSize;
    private double mLowerLimit;
    private double mUpperLimit;
    private DecimalFormat mDecimalFormat;
    private IntegrationOutputActivity mIntegrationOutputActivity;
    private double[] mXIValues;
    private double[] mYIValues;
    private double mI;
    private List<IntegrationRow> mIntegrationRows;

    NumericalIntegration(IntegrationOutputActivity integrationOutputActivity, String functionString, int steps, String[] integrationLimits, DecimalFormat decimalFormat) {
        mIntegrationOutputActivity = integrationOutputActivity;
        mFunction = new Function("f(x)=" + functionString);
        mSteps = steps;
        mLowerLimit = Double.parseDouble(integrationLimits[0]);
        mUpperLimit = Double.parseDouble(integrationLimits[1]);
        mDecimalFormat = decimalFormat;
        mIntegrationRows = new ArrayList<>();
        generateTabularValues();
    }

    NumericalIntegration(IntegrationOutputActivity integrationOutputActivity, String[] xvalues, String[] yvalues, DecimalFormat decimalFormat) {
        mIntegrationOutputActivity = integrationOutputActivity;
        mDecimalFormat = decimalFormat;
        mIntegrationRows = new ArrayList<>();
        insertTabularValues(xvalues, yvalues);
    }

    private void insertTabularValues(String[] xvalues, String[] yvalues) {
        mXIValues = new double[xvalues.length];
        mYIValues = new double[yvalues.length];
        IntegrationRow row = new IntegrationRow("x\u1D62", "y\u1D62");
        mIntegrationRows.add(row);
        for (int i = 0; i < mXIValues.length; i++) {
            mXIValues[i] = Double.parseDouble(xvalues[i]);
            mYIValues[i] = Double.parseDouble(yvalues[i]);
            row = new IntegrationRow(mDecimalFormat.format(mXIValues[i]), mDecimalFormat.format(mYIValues[i]));
            mIntegrationRows.add(row);
        }
        mStepSize = Double.parseDouble(mDecimalFormat.format(mXIValues[1] - mXIValues[0]));
        mIntegrationOutputActivity.fillRecyclerView(mIntegrationRows);
    }

    private void generateTabularValues() {
        mXIValues = new double[mSteps + 1];
        mYIValues = new double[mSteps + 1];
        IntegrationRow row = new IntegrationRow("x\u1D62", "y\u1D62");
        mIntegrationRows.add(row);
        mStepSize = Double.parseDouble(mDecimalFormat.format((mUpperLimit - mLowerLimit) / mSteps));
        double currentXI = mLowerLimit;
        for (int i = 0; i < mXIValues.length; i++) {
            mXIValues[i] = currentXI;
            mYIValues[i] = Double.parseDouble(mDecimalFormat.format(mFunction.calculate(mXIValues[i])));
            currentXI = currentXI + mStepSize;
            currentXI = Double.parseDouble(mDecimalFormat.format(currentXI));
            row = new IntegrationRow(mDecimalFormat.format(mXIValues[i]), mDecimalFormat.format(mYIValues[i]));
            mIntegrationRows.add(row);
        }
        mIntegrationOutputActivity.fillRecyclerView(mIntegrationRows);
    }

    void trapezoidalRule() {
        double outerValue = mStepSize / 2;
        double firstTermValue = mYIValues[0] + mYIValues[mYIValues.length-1];
        double secondTermValue = 0;
        for (int i = 1; i < mYIValues.length - 1; i++) {
            secondTermValue = secondTermValue + mYIValues[i];
            secondTermValue = Double.parseDouble(mDecimalFormat.format(secondTermValue));
        }
        mI = outerValue * (firstTermValue + (2 * secondTermValue));
        mI = Double.parseDouble(mDecimalFormat.format(mI));
        mIntegrationOutputActivity.insertAnswerIntoChip(mI);
    }

    void s1By3Rule() {
        double outerValue = mStepSize / 3;
        double firstTermValue = mYIValues[0] + mYIValues[mYIValues.length-1];
        double secondTermValue = 0;
        double thirdTermValue = 0;
        for (int i = 1; i < mYIValues.length - 1; i++) {
            if ((i & 1) == 1) {
                thirdTermValue = thirdTermValue + mYIValues[i];
                thirdTermValue = Double.parseDouble(mDecimalFormat.format(thirdTermValue));
            } else {
                secondTermValue = secondTermValue + mYIValues[i];
                secondTermValue = Double.parseDouble(mDecimalFormat.format(secondTermValue));
            }
        }
        mI = outerValue * (firstTermValue + (2 * secondTermValue) + (4 * thirdTermValue));
        mI = Double.parseDouble(mDecimalFormat.format(mI));
        mIntegrationOutputActivity.insertAnswerIntoChip(mI);
    }

    void s3By8Rule() {
        double outerValue = 3 * mStepSize / 8;
        double firstTermValue = mYIValues[0] + mYIValues[mYIValues.length-1];
        double secondTermValue = 0;
        double thirdTermValue = 0;
        for (int i = 1; i < mYIValues.length - 1; i++) {
            if (i % 3 == 0) {
                thirdTermValue = thirdTermValue + mYIValues[i];
                thirdTermValue = Double.parseDouble(mDecimalFormat.format(thirdTermValue));
            } else {
                secondTermValue = secondTermValue + mYIValues[i];
                secondTermValue = Double.parseDouble(mDecimalFormat.format(secondTermValue));
            }
        }
        mI = outerValue * (firstTermValue + (3 * secondTermValue) + (2 * thirdTermValue));
        mI = Double.parseDouble(mDecimalFormat.format(mI));
        mIntegrationOutputActivity.insertAnswerIntoChip(mI);
    }
}
