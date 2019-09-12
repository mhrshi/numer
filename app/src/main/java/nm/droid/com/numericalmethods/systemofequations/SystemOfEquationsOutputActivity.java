package nm.droid.com.numericalmethods.systemofequations;

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

public class SystemOfEquationsOutputActivity extends SystemOfEquationsInputActivity {

    @Arg public String mSystemMatrixString;
    @Arg public int mPrecision;
    @Arg public int mButtonId;

    private DecimalFormat mDecimalFormat;

    private TextView mEquationOneChip;
    private TextView mEquationTwoChip;
    private TextView mEquationThreeChip;
    private TextView mPrecisionChip;
    private TextView mAnswerChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private SystemOfEquationsMethod mSystemOfEquationsMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_of_equations_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        switch(mButtonId) {
            case 3:
                toolbar.setTitle(R.string.title_gauss_jacobi);
                break;

            case 4:
                toolbar.setTitle(R.string.title_gauss_seidel);
                break;
        }

        mEquationOneChip = findViewById(R.id.chip_equation_one);
        mEquationTwoChip = findViewById(R.id.chip_equation_two);
        mEquationThreeChip = findViewById(R.id.chip_equation_three);
        mPrecisionChip = findViewById(R.id.chip_precision);
        mAnswerChip = findViewById(R.id.chip_answer);

        String decimalFormatString = "#0." + new String(new char[mPrecision + 1]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setDecimalSeparatorAlwaysShown(true);
        mDecimalFormat.setMinimumFractionDigits(mPrecision + 1);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        startMethod();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
    
    public void fillChips(String eq1, String eq2, String eq3) {
        mEquationOneChip.setText(eq1);
        mEquationTwoChip.setText(eq2);
        mEquationThreeChip.setText(eq3);
        mPrecisionChip.setText("with .+" + mPrecision);
    }

    private void startMethod() {

        mSystemOfEquationsMethod = new SystemOfEquationsMethod(SystemOfEquationsOutputActivity.this, mSystemMatrixString, mDecimalFormat, mPrecision);
        if (mSystemOfEquationsMethod.canBeApplied()) {
            mSystemOfEquationsMethod.formEquations();
            mSystemOfEquationsMethod.formFunctions();
            if (mButtonId == 3) {
                mSystemOfEquationsMethod.stepExecutorGJ();
            } else if (mButtonId == 4) {
                mSystemOfEquationsMethod.stepExecutorGS();
            }
        }
    }

    public void fillRecyclerView(List<SystemOfEquationsRow> items) {
        mRecyclerView = findViewById(R.id.system_of_equations_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SystemOfEquationsAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void insertAnswerIntoChip(double x, double y, double z) {
        int compareHelper = (int) Math.pow(10, mPrecision);
        mAnswerChip.setText("x = " + (Math.floor(x * compareHelper) / compareHelper) + "\u0009\u0009\u0009\u0009y = " +
                                    (Math.floor(y * compareHelper) / compareHelper) + "\u0009\u0009\u0009\u0009z = " +
                                    (Math.floor(z * compareHelper) / compareHelper));
    }

    public void systemTransformed() {
        CafeBar.builder(SystemOfEquationsOutputActivity.this)
                .content("System transformed to diagonally dominant")
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }

    public void systemCouldNotBeTransformed() {
        CafeBar.builder(SystemOfEquationsOutputActivity.this)
                .content("Given system cannot be transformed to diagonally dominant")
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }

    public void abortShip() {
        CafeBar.builder(SystemOfEquationsOutputActivity.this)
                .content(R.string.error)
                .maxLines(1)
                .autoDismiss(false)
                .neutralText("DISMISS")
                .fitSystemWindow()
                .show();
    }
}
