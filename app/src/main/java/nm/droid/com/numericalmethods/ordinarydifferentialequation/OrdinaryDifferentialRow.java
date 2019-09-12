package nm.droid.com.numericalmethods.ordinarydifferentialequation;

class OrdinaryDifferentialRow {

    String mIteration;
    String mX;
    String mY;
    String mYStar;
    String mK1;
    String mK2;
    String mK3;
    String mK4;

    OrdinaryDifferentialRow(String iteration, String x, String y) {
        mIteration = iteration;
        mX = x;
        mY = y;
    }

    OrdinaryDifferentialRow(String iteration, String x, String yStar, String y) {
        mIteration = iteration;
        mX = x;
        mYStar = yStar;
        mY = y;
    }

    OrdinaryDifferentialRow(String iteration, String x, String k1, String k2, String k3, String k4, String y) {
        mIteration = iteration;
        mX = x;
        mK1 = k1;
        mK2 = k2;
        mK3 = k3;
        mK4 = k4;
        mY = y;
    }
}
