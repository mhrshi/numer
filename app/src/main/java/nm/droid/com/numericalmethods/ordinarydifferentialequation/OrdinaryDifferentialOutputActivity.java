package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.danimahardhika.cafebar.CafeBar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.R;

public class OrdinaryDifferentialOutputActivity extends OrdinaryDifferentialInputActivity {

    @Arg public String mFunctionString;
    @Arg public double mXZero;
    @Arg public double mYZero;
    @Arg public double mStepSize;
    @Arg public double mXI;
    @Arg public int mPrecision;
    @Arg public int mButtonId;

    private DecimalFormat mDecimalFormat;

    private TextView mFunctionChip;
    private TextView mInitialValuesStepSizeChip;
    private TextView mPrecisionAnswerChip;

    private EulerMethod mEulerMethod;
    private HeunMethod mHeunMethod;
    private RungeKuttaMethod mRungeKuttaMethod;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordinary_differential_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        switch(mButtonId) {
            case 5:
                toolbar.setTitle(R.string.title_euler);
                break;

            case 6:
                toolbar.setTitle(R.string.title_heun);
                break;

            case 7:
                toolbar.setTitle(R.string.title_runge_kutta);
                break;
        }

        mFunctionChip = findViewById(R.id.chip_function);
        mFunctionChip.setText("dy/dx = " + mFunctionString);
        mInitialValuesStepSizeChip = findViewById(R.id.chip_initial_values_step_size);
        mInitialValuesStepSizeChip.setText("y(" + mXZero + ") = " + mYZero + " & h = " + mStepSize);
        mPrecisionAnswerChip = findViewById(R.id.chip_precision_answer);

        String decimalFormatString = "#0." + new String(new char[mPrecision + 1]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setDecimalSeparatorAlwaysShown(true);
        mDecimalFormat.setMinimumFractionDigits(mPrecision + 1);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        startCorrespondingMethod();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void startCorrespondingMethod() {

        switch(mButtonId) {
            case 5:
                mEulerMethod = new EulerMethod(OrdinaryDifferentialOutputActivity.this, mFunctionString, mXZero, mYZero, mXI, mStepSize, mDecimalFormat);
                mEulerMethod.stepExecutor();
                break;

            case 6:
                mHeunMethod = new HeunMethod(OrdinaryDifferentialOutputActivity.this, mFunctionString, mXZero, mYZero, mXI, mStepSize, mDecimalFormat);
                mHeunMethod.stepExecutor();
                break;

            case 7:
                mRungeKuttaMethod = new RungeKuttaMethod(OrdinaryDifferentialOutputActivity.this, mFunctionString, mXZero, mYZero, mXI, mStepSize, mDecimalFormat);
                mRungeKuttaMethod.stepExecutor();
                break;

            default:
        }
    }

    public void fillRecyclerViewEuler(List<OrdinaryDifferentialRow> items) {
        mRecyclerView = findViewById(R.id.ordinary_differential_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EulerAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void fillRecyclerViewHeun(List<OrdinaryDifferentialRow> items) {
        mRecyclerView = findViewById(R.id.ordinary_differential_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HeunAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void fillRecyclerViewRK(List<OrdinaryDifferentialRow> items) {
        mRecyclerView = findViewById(R.id.ordinary_differential_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RungeKuttaAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void insertAnswerIntoChip(double ans) {
        int compareHelper = (int) Math.pow(10, mPrecision);
        mPrecisionAnswerChip.setText("y(" + mXI + ") = " + ((Math.floor(ans * compareHelper)) / compareHelper));
    }

    public void abortShip() {
        CafeBar.builder(OrdinaryDifferentialOutputActivity.this)
                .content(R.string.error)
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }
}
