package nm.droid.com.numericalmethods.differentiation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

@MakeActivityStarter
public class DifferentiationInputActivity extends MethodListActivity {

    private TextView mSyntaxTipTextView;
    private Button mJumpToFaqButton;

    private SwitchCompat mForwardBackwardSwitch;

    private MaterialEditText mXValuesEditText;
    private MaterialEditText mYValuesEditText;
    private MaterialEditText mXEditText;

    private Button mCalculateButton;

    private String[] mXValues;
    private String[] mYValues;
    private double mX;

    private boolean mAllInputsValid;

    private boolean mSwitchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differentiation_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Numerical Differentiation");
        setSupportActionBar(toolbar);

        mForwardBackwardSwitch = findViewById(R.id.switch_forward_backward);
        mForwardBackwardSwitch.setText(getResources().getText(R.string.switch_differentiation_forward));
        mSwitchState = false;

        mSyntaxTipTextView = findViewById(R.id.syntax_tip_differentiation);
        mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_x_values));
        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);

        mXValuesEditText = findViewById(R.id.edit_text_x_values);
        mYValuesEditText = findViewById(R.id.edit_text_y_values);
        mXEditText = findViewById(R.id.edit_text_differentiate_at);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(DifferentiationInputActivity.this));

        mXEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(mXEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });

        mCalculateButton = findViewById(R.id.button_calculate);
        mCalculateButton.setOnClickListener(v -> {
            validateInputs();
            if (mAllInputsValid) {
                DifferentiationOutputActivityStarter.start(DifferentiationInputActivity.this, mXValues, mYValues, mX, mSwitchState);
            }
        });

        mForwardBackwardSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mForwardBackwardSwitch.setText(getResources().getText(R.string.switch_differentiation_backward));
                mSwitchState = true;
            } else {
                mForwardBackwardSwitch.setText(getResources().getText(R.string.switch_differentiation_forward));
                mSwitchState = false;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateInputs() {

        mAllInputsValid = true;

        String xValues = mXValuesEditText.getText().toString();
        if (!(xValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){3}([+-]?\\d+(\\.\\d+)?)") ||
                xValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){4}([+-]?\\d+(\\.\\d+)?)") ||
                xValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){5}([+-]?\\d+(\\.\\d+)?)") ||
                xValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){6}([+-]?\\d+(\\.\\d+)?)"))) {
            mAllInputsValid = false;
            mXValuesEditText.setError("Invalid syntax");
        } else {
            mXValues = xValues.split("\\s+");
        }

        String yValues = mYValuesEditText.getText().toString();
        if (!(yValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){3}([+-]?\\d+(\\.\\d+)?)") ||
                yValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){4}([+-]?\\d+(\\.\\d+)?)") ||
                yValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){5}([+-]?\\d+(\\.\\d+)?)") ||
                yValues.matches("([+-]?\\d+(\\.\\d+)?\\s+){6}([+-]?\\d+(\\.\\d+)?)"))) {
            mAllInputsValid = false;
            mYValuesEditText.setError("Invalid syntax");
        } else {
            mYValues = yValues.split("\\s+");
        }

        if (mAllInputsValid) {
            if (mXValues.length != mYValues.length) {
                mAllInputsValid = false;
                mXValuesEditText.setError("Number of values don't match");
                mYValuesEditText.setError("Number of values don't match");
            }
        }

        String x = mXEditText.getText().toString();
        if (!x.matches("[+-]?\\d+(\\.\\d+)?")) {
            mAllInputsValid = false;
            mXEditText.setError("Invalid syntax");
        } else {
            mX = Double.parseDouble(x);
        }
    }

    private void attachListenersOnInputs() {

        mXValuesEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_x_values));
            }
        });

        mYValuesEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_y_values));
            }
        });

        mXEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_differentiate_at));
            }
        });
    }
}
