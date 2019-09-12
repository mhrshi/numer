package nm.droid.com.numericalmethods.integration;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.R;

public class IntegrationOutputActivity extends IntegrationInputActivity {

    @Arg public String functionString;
    @Arg public int steps;
    @Arg public String[] integrationLimits;
    @Arg public String[] xValues;
    @Arg public String[] yValues;
    @Arg public boolean tabulatedValues;
    @Arg public int checkedRadio;

    private TextView mFunctionChip;
    private TextView mLimitsChip;
    private TextView mAnswerChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DecimalFormat mDecimalFormat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mFunctionChip = findViewById(R.id.chip_integration_function);
        mLimitsChip = findViewById(R.id.chip_limits);
        mAnswerChip = findViewById(R.id.chip_answer);

        mFunctionChip.setText("\u222B " + functionString + "dx");
        mLimitsChip.setText("from " + integrationLimits[0] + " to " + integrationLimits[1]);

        mDecimalFormat = new DecimalFormat("#0.####");
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        NumericalIntegration numericalIntegration;
        if (tabulatedValues) {
            ConstraintSet constraintSet = new ConstraintSet();
            ConstraintLayout constraintLayout = findViewById(R.id.integration_output_constraint_layout);
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.chip_answer, ConstraintSet.TOP);
            constraintSet.connect(R.id.chip_answer, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 160);
            constraintSet.applyTo(constraintLayout);
            mFunctionChip.setVisibility(View.GONE);
            mLimitsChip.setVisibility(View.GONE);
            numericalIntegration = new NumericalIntegration(IntegrationOutputActivity.this, xValues, yValues, mDecimalFormat);
        } else {
            numericalIntegration = new NumericalIntegration(IntegrationOutputActivity.this, functionString, steps, integrationLimits, mDecimalFormat);
        }
        switch (checkedRadio) {
            case R.id.radio_trapezoidal:
                toolbar.setTitle("Trapezoidal Rule");
                numericalIntegration.trapezoidalRule();
                break;

            case R.id.radio_s1by3:
                toolbar.setTitle("Simpson's \u2153 Rule");
                numericalIntegration.s1By3Rule();
                break;

            case R.id.radio_s3by8:
                toolbar.setTitle("Simpson's \u215C Rule");
                numericalIntegration.s3By8Rule();
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void fillRecyclerView(List<IntegrationRow> items) {
        mRecyclerView = findViewById(R.id.integration_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new IntegrationAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    @SuppressLint("SetTextI18n")
    public void insertAnswerIntoChip(double ans) {
        mAnswerChip.setText("I = " + ans);
    }
}
