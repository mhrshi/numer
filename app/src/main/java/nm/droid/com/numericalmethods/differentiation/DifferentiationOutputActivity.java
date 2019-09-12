package nm.droid.com.numericalmethods.differentiation;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.R;

public class DifferentiationOutputActivity extends DifferentiationInputActivity {

    @Arg
    String[] XValues;
    @Arg String[] YValues;
    @Arg double YAtX;
    @Arg boolean switchState;

    private TextView mFirstOrderChip;
    private TextView mSecondOrderChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DecimalFormat mDecimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differentiation_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        int precision = 3;
        int digitsAfterPoint;
        for (String value : YValues) {
            if (value.indexOf('.') == -1) {
                continue;
            }
            digitsAfterPoint = value.length() - 1 - value.indexOf('.');
            if (precision < digitsAfterPoint) {
                precision = digitsAfterPoint;
            }
        }
        mFirstOrderChip = findViewById(R.id.chip_dy_dx);
        mSecondOrderChip = findViewById(R.id.chip_d2y_dx2);

        String decimalFormatString = "#0." + new String(new char[precision]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        NumericalDifferentiation numericalDifferentiation = new NumericalDifferentiation(DifferentiationOutputActivity.this, XValues, YValues, YAtX, mDecimalFormat, switchState);
        numericalDifferentiation.executor();
        if (switchState) {
            toolbar.setTitle("Newton Backward Differentiation");
            numericalDifferentiation.performNewtonBackward();
        } else {
            toolbar.setTitle("Newton Forward Differentiation");
            numericalDifferentiation.performNewtonForward();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void fillRecyclerView(List<DifferentiationRow> items) {
        mRecyclerView = findViewById(R.id.differentiation_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DifferentiationAdapter(this, items, YValues.length);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    @SuppressLint("SetTextI18n")
    public void insertAnswerIntoChips(double dydx, double d2ydx2) {
        mFirstOrderChip.setText("y'(" + YAtX + ") = " + dydx);
        mSecondOrderChip.setText("y''(" + YAtX + ") = " + d2ydx2);
    }
}
