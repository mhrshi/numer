package nm.droid.com.numericalmethods.integration;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.mariuszgromada.math.mxparser.Function;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

@MakeActivityStarter
public class IntegrationInputActivity extends MethodListActivity {

    private TextView mSyntaxTipTextView;
    private Button mJumpToFaqButton;

    private SwitchCompat mSwitch;
    private ViewSwitcher mViewSwitcher;
    private LinearLayout mFunctionView;
    private LinearLayout mTabulatedValuesView;

    private RadioGroup mRadioGroup;
    private AppCompatRadioButton mTrapezoidalRadio;
    private AppCompatRadioButton mS1By3Radio;
    private AppCompatRadioButton mS3By8Radio;

    private MaterialEditText mFunctionEditText;
    private MaterialEditText mLimitsEditText;

    private MaterialEditText mXValuesEditText;
    private MaterialEditText mYValuesEditText;
    private String[] mXValues;
    private String[] mYValues;

    private Button mCalculateButton;

    private String mFunctionString;
    private Function mFunction;
    private String mLimitsString;
    private String[] mLimits = new String[2];
    private int mSteps;

    private boolean mAllInputsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Numerical Integration");
        setSupportActionBar(toolbar);

        mSyntaxTipTextView = findViewById(R.id.syntax_tip_integration);
        mSyntaxTipTextView.setText(R.string.syntax_tip_function);
        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mRadioGroup = findViewById(R.id.radio_integration);
        mTrapezoidalRadio = findViewById(R.id.radio_trapezoidal);
        mTrapezoidalRadio.setChecked(true);
        mS1By3Radio = findViewById(R.id.radio_s1by3);
        mS3By8Radio = findViewById(R.id.radio_s3by8);

        mFunctionEditText = findViewById(R.id.edit_text_integration_function);
        mLimitsEditText = findViewById(R.id.edit_text_integration_limits);

        mXValuesEditText = findViewById(R.id.edit_text_x_values);
        mYValuesEditText = findViewById(R.id.edit_text_y_values);

        mSwitch = findViewById(R.id.switch_tabulated_values);
        mSwitch.setText(R.string.switch_function);
        mSwitch.setChecked(false);
        mViewSwitcher = findViewById(R.id.view_switcher);
        mFunctionView = findViewById(R.id.view_function);
        mTabulatedValuesView = findViewById(R.id.view_tabulated_values);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(IntegrationInputActivity.this));

        mLimitsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(mLimitsEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSwitch.setText(R.string.switch_tabulated_values);
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_x_values));
                mViewSwitcher.showNext();
                mXValuesEditText.setText("");
                mYValuesEditText.setText("");
                mXValuesEditText.requestFocus();
            } else {
                mSwitch.setText(R.string.switch_function);
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_function));
                mViewSwitcher.showPrevious();
                mFunctionEditText.setText("");
                mLimitsEditText.setText("");
                mFunctionEditText.requestFocus();
            }
        });

        mCalculateButton = findViewById(R.id.button_calculate);
        mCalculateButton.setOnClickListener(view -> {
            if (mSwitch.isChecked()) {
                validateTabulatedValueInputs();
            } else {
                validateFunctionInputs();
            }
            if (mAllInputsValid) {
                IntegrationOutputActivityStarter.start(IntegrationInputActivity.this,
                        mFunctionString, mSteps, mLimits,
                        mXValues, mYValues,
                        mSwitch.isChecked(), mRadioGroup.getCheckedRadioButtonId());
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateFunctionInputs() {

        mAllInputsValid = true;

        mFunctionString = mFunctionEditText.getText().toString();
        mFunction = new Function("f(x)=" + mFunctionString);
        if (Double.isNaN(mFunction.calculate(1))) {
            mAllInputsValid = false;
            mFunctionEditText.setError("Invalid syntax");
        }

        mLimitsString = mLimitsEditText.getText().toString();
        if (!mLimitsString.matches("(\\d+),\\s+([+-]?\\d+(\\.\\d+)?)\\s+->\\s+([+-]?\\d+(\\.\\d+)?)")) {
            mAllInputsValid = false;
            mLimitsEditText.setError("Invalid syntax");
        } else {
            String[] splitByComa = mLimitsString.split(",\\s+");
            String[] splitByArrow = splitByComa[1].split("\\s+->\\s+");
            mSteps = Integer.parseInt(splitByComa[0]);
            mLimits[0] = splitByArrow[0];
            mLimits[1] = splitByArrow[1];
            if (Double.parseDouble(mLimits[0]) > Double.parseDouble(mLimits[1])) {
                mAllInputsValid = false;
                mLimitsEditText.setError("Please recheck limits");
            }
            if (mSteps < 3) {
                mAllInputsValid = false;
                mLimitsEditText.setError("Too few steps");
            }
        }
    }

    private void validateTabulatedValueInputs() {

        mAllInputsValid = true;

        String xValues = mXValuesEditText.getText().toString();
        if (!xValues.matches("([+-]?\\d+(\\.\\d+)?\\s*){2,}")) {
            mAllInputsValid = false;
            mXValuesEditText.setError("Invalid syntax");
        } else {
            mXValues = xValues.split("\\s+");
        }

        String yValues = mYValuesEditText.getText().toString();
        if (!yValues.matches("([+-]?\\d+(\\.\\d+)?\\s*){2,}")) {
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
    }

    private void attachListenersOnInputs() {

        mFunctionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_function));
            }
        });

        mLimitsEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_integration_limits));
            }
        });

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
    }
}
