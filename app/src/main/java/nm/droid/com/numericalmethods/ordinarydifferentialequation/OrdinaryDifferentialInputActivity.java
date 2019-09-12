package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.mariuszgromada.math.mxparser.Function;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

public class OrdinaryDifferentialInputActivity extends MethodListActivity {

    @Arg public int mMethodId;

    private TextView mSyntaxTipTextView;
    private MaterialEditText mFunctionEditText;
    private MaterialEditText mInitialValuesEditText;
    private MaterialEditText mStepSizeXEditText;
    private MaterialEditText mPrecisionEditText;
    private Button mJumpToFaqButton;
    private Button mCalculateButton;

    private String mFunctionString;
    private Function mFunction;
    private String mInitialValuesString;
    private double mXZero;
    private double mYZero;
    private String mStepSizeXString;
    private double mStepSize;
    private double mXI;
    private int mPrecision;

    private boolean mAllInputsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordinary_differential_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        switch(mMethodId) {
            case 6:
                toolbar.setTitle(R.string.title_euler);
                break;

            case 7:
                toolbar.setTitle(R.string.title_heun);
                break;

            case 8:
                toolbar.setTitle(R.string.title_runge_kutta);
                break;
        }

        mSyntaxTipTextView = findViewById(R.id.syntax_tip_ode);
        mSyntaxTipTextView.setText(R.string.syntax_tip_ode_function);
        mFunctionEditText = findViewById(R.id.edit_text_ode_function);
        mInitialValuesEditText = findViewById(R.id.edit_text_ode_initial_values);
        mStepSizeXEditText = findViewById(R.id.edit_text_ode_step_size_x);
        mPrecisionEditText = findViewById(R.id.edit_text_precision);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(OrdinaryDifferentialInputActivity.this));

        mPrecisionEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(mPrecisionEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });

        mCalculateButton = findViewById(R.id.button_calculate);
        mCalculateButton.setOnClickListener(view -> {
            validateInputs();
            if (mAllInputsValid) {
                OrdinaryDifferentialOutputActivityStarter.start(OrdinaryDifferentialInputActivity.this, mFunctionString, mXZero, mYZero, mStepSize, mXI, mPrecision, mMethodId);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateInputs() {

        mAllInputsValid = true;

        mFunctionString = mFunctionEditText.getText().toString();
        mFunction = new Function("f(x,y)=" + mFunctionString);
        if (Double.isNaN(mFunction.calculate(1, 1))) {
            mAllInputsValid = false;
            mFunctionEditText.setError("Invalid syntax");
        }

        mInitialValuesString = mInitialValuesEditText.getText().toString();
        if (!mInitialValuesString.matches("([+-]?\\d+(\\.\\d+)?)\\s*,\\s*([+-]?\\d+(\\.\\d+)?)")) {
            mAllInputsValid = false;
            mInitialValuesEditText.setError("Invalid syntax");
        } else {
            String[] values = mInitialValuesString.split("\\s*,\\s*");
            mXZero = Double.parseDouble(values[0]);
            mYZero = Double.parseDouble(values[1]);
        }

        mStepSizeXString = mStepSizeXEditText.getText().toString();
        if (!mStepSizeXString.matches("([+-]?\\d+(\\.\\d+)?)\\s*->\\s*([+-]?\\d+(\\.\\d+)?)")) {
            mAllInputsValid = false;
            mStepSizeXEditText.setError("Invalid syntax");
        } else {
            String[] values = mStepSizeXString.split("\\s*->\\s*");
            mStepSize = Double.parseDouble(values[0]);
            mXI = Double.parseDouble(values[1]);
            if (mXI % mStepSize != 0.0) {
                mAllInputsValid = false;
                mStepSizeXEditText.setError("Invalid syntax");
            }
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

        mFunctionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_ode_function));
            }
        });

        mInitialValuesEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_ode_initial_values));
            }
        });

        mStepSizeXEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_ode_step_size_x));
            }
        });

        mPrecisionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_precision));
            }
        });
    }
}
