package nm.droid.com.numericalmethods.powermatrix;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

public class PowerOutputActivity extends PowerInputActivity {

    @Arg public String mMatrixString;
    @Arg public int mPrecision;

    private DecimalFormat mDecimalFormat;

    private TextView mPrecisionAnswerChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private PowerMethod mPowerMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_power);
        setSupportActionBar(toolbar);

        mPrecisionAnswerChip = findViewById(R.id.chip_precision_answer);

        String decimalFormatString = "#0." + new String(new char[mPrecision + 1]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setDecimalSeparatorAlwaysShown(true);
        mDecimalFormat.setMinimumFractionDigits(mPrecision + 1);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        mPowerMethod = new PowerMethod(PowerOutputActivity.this, mMatrixString, mDecimalFormat, mPrecision);
        mPowerMethod.startCorrespondingStepExecutor();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void fillRecyclerViewThree(List<PowerRow> items) {
        mRecyclerView = findViewById(R.id.power_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PowerThreeAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void fillRecyclerViewTwo(List<PowerRow> items) {
        mRecyclerView = findViewById(R.id.power_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PowerTwoAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void insertAnswerIntoChip(double dominant) {
        int compareHelper = (int) Math.pow(10, mPrecision);
        mPrecisionAnswerChip.setText("dominant eigen value with .+" + mPrecision + " is " + (Math.floor(dominant * compareHelper) / compareHelper));
    }

    public void abortShip() {
        CafeBar.builder(PowerOutputActivity.this)
                .content(R.string.error)
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }
}
