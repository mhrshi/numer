package nm.droid.com.numericalmethods.differences;

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

public class InterpolationOutputActivity extends InterpolationInputActivity {

    @Arg String[] XValues;
    @Arg String[] YValues;
    @Arg double YAtX;
    @Arg boolean switchState;
    @Arg int methodId;

    private TextView mAnswerChip;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DecimalFormat mDecimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpolation_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mAnswerChip = findViewById(R.id.chip_answer);

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
        String decimalFormatString = "#0." + new String(new char[precision]).replace("\0", "#");
        mDecimalFormat = new DecimalFormat(decimalFormatString);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        EquidistantInterpolation equidistantInterpolation;
        LagrangeInterpolation lagrangeInterpolation;
        NewtonDividedDifference newtonDividedDifference;

        switch (methodId) {
            case 9:
                equidistantInterpolation = new EquidistantInterpolation(InterpolationOutputActivity.this, XValues, YValues, YAtX, mDecimalFormat, switchState);
                equidistantInterpolation.executor();
                if (switchState) {
                    toolbar.setTitle("Newton Backward Interpolation");
                    equidistantInterpolation.newtonBackward();
                } else {
                    toolbar.setTitle("Newton Forward Interpolation");
                    equidistantInterpolation.newtonForward();
                }
                break;

            case 10:
                equidistantInterpolation = new EquidistantInterpolation(InterpolationOutputActivity.this, XValues, YValues, YAtX, mDecimalFormat, switchState);
                equidistantInterpolation.executor();
                toolbar.setTitle("Stirling Interpolation");
                equidistantInterpolation.stirling();
                break;

            case 11:
                toolbar.setTitle("Lagrange Interpolation");
                lagrangeInterpolation = new LagrangeInterpolation(InterpolationOutputActivity.this, XValues, YValues, YAtX, mDecimalFormat);
                lagrangeInterpolation.executor();
                break;

            case 12:
                toolbar.setTitle("Newton's Divided Difference");
                newtonDividedDifference = new NewtonDividedDifference(InterpolationOutputActivity.this, XValues, YValues, YAtX, mDecimalFormat);
                newtonDividedDifference.executor();
                break;

            case 14:

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void fillRecyclerView(List<InterpolationRow> items) {
        mRecyclerView = findViewById(R.id.interpolation_output_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InterpolationAdapter(this, items, YValues.length);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void insertAnswerIntoChip(double ans) {
        mAnswerChip.setText("y(" + YAtX + ") = " + ans);
    }
}
