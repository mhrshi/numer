package nm.droid.com.numericalmethods.differentiation;

class DifferentiationRow {

    String mHeader;
    String[] mValues;

    DifferentiationRow(String header, String[] values, int rowNumber) {
        mHeader = header;
        mValues = values;
        for (int i = values.length - 1; rowNumber >= 1; i--, rowNumber--) {
            mValues[i] = " ";
        }
    }
}
