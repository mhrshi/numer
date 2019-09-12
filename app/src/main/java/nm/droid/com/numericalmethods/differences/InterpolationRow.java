package nm.droid.com.numericalmethods.differences;

class InterpolationRow {

    String mHeader;
    String[] mValues;

    InterpolationRow(String header, String[] values, int rowNumber) {
        mHeader = header;
        mValues = values;
        for (int i = values.length - 1; rowNumber >= 1; i--, rowNumber--) {
            mValues[i] = " ";
        }
    }
}
