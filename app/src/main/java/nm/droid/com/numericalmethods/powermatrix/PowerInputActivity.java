package nm.droid.com.numericalmethods.powermatrix;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

@MakeActivityStarter
public class PowerInputActivity extends MethodListActivity {

    private TextView mSyntaxTipTextView;
    private MaterialEditText mMatrixEditText;
    private MaterialEditText mPrecisionEditText;
    private Button mJumpToFaqButton;
    private Button mCalculateButton;

    private String mMatrixString;
    private int mPrecision;

    private boolean mAllInputsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_power);
        setSupportActionBar(toolbar);

        mSyntaxTipTextView = findViewById(R.id.syntax_tip);
        mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_matrix));
        mMatrixEditText = findViewById(R.id.edit_text_matrix);
        mPrecisionEditText = findViewById(R.id.edit_text_precision);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(PowerInputActivity.this));

        mCalculateButton = findViewById(R.id.button_calculate);
        mCalculateButton.setOnClickListener(view -> {
            validateInputs();
            if (mAllInputsValid) {
                PowerOutputActivityStarter.start(PowerInputActivity.this, mMatrixString, mPrecision);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateInputs() {

        mAllInputsValid = true;

        mMatrixString = mMatrixEditText.getText().toString();
        if (!mMatrixString.matches("([+-]?\\d+)\\s*([+-]?\\d+)[\\r\\n]+([+-]?\\d+)\\s*([+-]?\\d+)") && (!mMatrixString.matches("([+-]?\\d+)\\s*([+-]?\\d+)\\s*([+-]?\\d+)[\\r\\n]+([+-]?\\d+)\\s*([+-]?\\d+)\\s*([+-]?\\d+)[\\r\\n]+([+-]?\\d+)\\s*([+-]?\\d+)\\s*([+-]?\\d+)"))) {
            mAllInputsValid = false;
            mMatrixEditText.setError("Invalid syntax");
        }

        if (!mPrecisionEditText.isValid("\\d")) {
            mAllInputsValid = false;
            mPrecisionEditText.setError("Invalid syntax");
        } else {
            mPrecision = Integer.parseInt(mPrecisionEditText.getText().toString());
            if (!(mPrecision >= 2 && mPrecision <= 5)) {
                mAllInputsValid = false;
                mPrecisionEditText.setError("Invalid syntax");
            }
        }
    }

    private void attachListenersOnInputs() {

        mMatrixEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_matrix));
            }
        });

        mPrecisionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_precision));
            }
        });
    }
}
