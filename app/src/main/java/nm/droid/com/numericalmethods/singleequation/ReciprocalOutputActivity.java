package nm.droid.com.numericalmethods.singleequation;

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

public class ReciprocalOutputActivity extends SingleEquationInputActivity {

    @Arg public double mReciprocalOf;
    @Arg public double mInitialApprox;
    @Arg public int mPrecision;

    private TextView mReciprocalOfChip;
    private TextView mPrecisionChip;
    private TextView mAnswerChip;

    private DecimalFormat mDecimalFormat;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private NewtonRaphsonMethod mNewtonRaphsonMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciprocal_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Newton-Raphson");
        setSupportActionBar(toolbar);

        mReciprocalOfChip = findViewById(R.id.chip_reciprocal_of);
        mReciprocalOfChip.setText("reciprocal of " + mReciprocalOf);

        mPrecisionChip = findViewById(R.id.chip_precision);
        mPrecisionChip.setText("with .+" + mPrecision);

        mAnswerChip = findViewById(R.id.chip_answer);

        String decimalFormatString = "#0." + new String(new char[mPrecision + 1]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setDecimalSeparatorAlwaysShown(true);
        mDecimalFormat.setMinimumFractionDigits(mPrecision + 1);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        mNewtonRaphsonMethod = new NewtonRaphsonMethod(ReciprocalOutputActivity.this, mReciprocalOf, mInitialApprox, mDecimalFormat, mPrecision);
        mNewtonRaphsonMethod.stepExecutor();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void fillRecyclerViewNR(List<NewtonRaphsonRow> items) {
        mRecyclerView = findViewById(R.id.single_equation_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewtonRaphsonAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void insertAnswerIntoChip(double ans) {
        mAnswerChip.setText(String.format("is %." + mPrecision + "f", ans));
    }

    public void abortShip() {
        CafeBar.builder(ReciprocalOutputActivity.this)
                .content(R.string.error)
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }
}
