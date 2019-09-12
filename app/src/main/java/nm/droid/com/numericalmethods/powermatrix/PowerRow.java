package nm.droid.com.numericalmethods.powermatrix;

class PowerRow {

    String mIteration;
    String[] mInnerRowZero;
    String[] mInnerRowOne;
    String[] mInnerRowTwo;

    PowerRow(String iteration, String[] innerRowZero, String[] innerRowOne, String[] innerRowTwo) {
        mIteration = iteration;
        mInnerRowZero = innerRowZero;
        mInnerRowOne = innerRowOne;
        mInnerRowTwo = innerRowTwo;
    }

    PowerRow(String iteration, String[] innerRowZero, String[] innerRowOne) {
        mIteration = iteration;
        mInnerRowZero = innerRowZero;
        mInnerRowOne = innerRowOne;
    }
}
