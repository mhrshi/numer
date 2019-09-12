package nm.droid.com.numericalmethods.singleequation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.danimahardhika.cafebar.CafeBar;

import org.mariuszgromada.math.mxparser.Function;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.R;

public class SingleEquationOutputActivity extends SingleEquationInputActivity {

    @Arg public String mFunctionString;
    @Arg public double mLeftEndPoint;
    @Arg public double mRightEndPoint;
    @Arg public int mPrecision;
    @Arg public int mButtonId;

    private Function mFunction;
    private DecimalFormat mDecimalFormat;

    private TextView mFunctionChip;
    private TextView mIntervalChip;
    private TextView mPrecisionChip;
    private TextView mAnswerChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private BisectionMethod mBisectionMethod;
    private NewtonRaphsonMethod mNewtonRaphsonMethod;
    private RegulaFalsiMethod mRegulaFalsiMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_equation_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        switch(mButtonId) {
            case 0:
                toolbar.setTitle(R.string.title_bisection);
                break;

            case 1:
                toolbar.setTitle(R.string.title_newton_raphson);
                break;

            case 2:
                toolbar.setTitle(R.string.title_regula_falsi);
                break;
        }

        mFunctionChip = findViewById(R.id.chip_function);
        mFunctionChip.setText("root of f(x) = " + mFunctionString);

        mIntervalChip = findViewById(R.id.chip_interval);
        mIntervalChip.setText("in (" + mLeftEndPoint + ", " + mRightEndPoint + ")");

        mPrecisionChip = findViewById(R.id.chip_precision);
        mPrecisionChip.setText("with .+" + mPrecision);

        mAnswerChip = findViewById(R.id.chip_answer);

        mFunction = new Function("mFunction", mFunctionString, "x");
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

        switch (mButtonId) {

            case 0:
                mBisectionMethod = new BisectionMethod(SingleEquationOutputActivity.this, mFunction, mLeftEndPoint, mRightEndPoint, mDecimalFormat, mPrecision);
                mBisectionMethod.stepExecutor();
                break;

            case 1:
                mNewtonRaphsonMethod = new NewtonRaphsonMethod(SingleEquationOutputActivity.this, mFunction, mLeftEndPoint, mRightEndPoint, mDecimalFormat, mPrecision);
                mNewtonRaphsonMethod.stepExecutor();
                break;

            case 2:
                mRegulaFalsiMethod = new RegulaFalsiMethod(SingleEquationOutputActivity.this, mFunction, mLeftEndPoint, mRightEndPoint, mDecimalFormat, mPrecision);
                mRegulaFalsiMethod.stepExecutor();
                break;

            default:
        }
    }

    public void fillRecyclerView(List<SingleEquationRow> items) {
        mRecyclerView = findViewById(R.id.single_equation_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SingleEquationAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void fillRecyclerViewNR(List<NewtonRaphsonRow> items) {
        mRecyclerView = findViewById(R.id.single_equation_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewtonRaphsonAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void insertAnswerIntoChip(double ans) {
        int compareHelper = (int) Math.pow(10, mPrecision);
        mAnswerChip.setText("is x = " + (Math.floor(ans * compareHelper) / compareHelper));
    }

    public void reciprocalSnackBar() {
        CafeBar.builder(SingleEquationOutputActivity.this)
                .content(R.string.reciprocal_snackbar)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();

        mRecyclerView = findViewById(R.id.single_equation_output_recycler_view);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mAnswerChip.setText("is not defined");
    }

    public void abortShip() {
        CafeBar.builder(SingleEquationOutputActivity.this)
                .content(R.string.error)
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }
}
