package nm.droid.com.numericalmethods.singleequation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.mariuszgromada.math.mxparser.Function;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

public class SingleEquationInputActivity extends MethodListActivity {

    private TextView mSyntaxTipTextView;

    private ViewSwitcher mViewSwitcher;
    private LinearLayout mNormalView;
    private LinearLayout mReciprocalView;

    private MaterialEditText mFunctionEditText;
    private MaterialEditText mIntervalEditText;
    private MaterialEditText mPrecisionEditText;

    private SwitchCompat mReciprocalSwitch;
    private MaterialEditText mReciprocalOfEditText;
    private MaterialEditText mInitialApproxEditText;

    private Button mJumpToFaqButton;
    private Button mCalculateButton;

    @Arg public int mMethodId;

    private String mFunctionString;
    private Function mFunction;

    private String mIntervalString;
    private double mLeftEndPoint;
    private double mRightEndPoint;

    private String mReciprocalOfString;
    private String mInitialApproxString;

    private int mPrecision;

    private boolean mAllInputsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_equation_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mViewSwitcher = findViewById(R.id.view_switcher);
        mNormalView = findViewById(R.id.view_normal);
        mReciprocalView = findViewById(R.id.view_reciprocal);

        mReciprocalSwitch = findViewById(R.id.switch_reciprocal);

        switch(mMethodId) {
            case 0:
                toolbar.setTitle(R.string.title_bisection);
                mReciprocalSwitch.setVisibility(View.GONE);
                break;

            case 1:
                toolbar.setTitle(R.string.title_newton_raphson);
                mReciprocalSwitch.setChecked(false);
                break;

            case 2:
                toolbar.setTitle(R.string.title_regula_falsi);
                mReciprocalSwitch.setVisibility(View.GONE);
                break;
        }

        mSyntaxTipTextView = findViewById(R.id.syntax_tip);
        mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_function));
        mFunctionEditText = findViewById(R.id.edit_text_function);
        mIntervalEditText = findViewById(R.id.edit_text_interval);
        mPrecisionEditText = findViewById(R.id.edit_text_precision);

        mReciprocalOfEditText = findViewById(R.id.edit_text_reciprocal_of);
        mInitialApproxEditText = findViewById(R.id.edit_text_reciprocal_initial_approximation);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(SingleEquationInputActivity.this));

        mPrecisionEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(mPrecisionEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });

        mReciprocalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_reciprocal_of));
                mViewSwitcher.showNext();
                mReciprocalOfEditText.setText("");
                mInitialApproxEditText.setText("");
                mPrecisionEditText.setText("");
                mReciprocalOfEditText.requestFocus();
            } else {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_function));
                mViewSwitcher.showPrevious();
                mFunctionEditText.setText("");
                mIntervalEditText.setText("");
                mPrecisionEditText.setText("");
                mFunctionEditText.requestFocus();
            }
        });

        mCalculateButton = findViewById(R.id.button_calculate);
        mCalculateButton.setOnClickListener(view -> {
            if (mReciprocalSwitch.isChecked()) {
                validateReciprocalInputs();
                if (mAllInputsValid) {
                    ReciprocalOutputActivityStarter.start(SingleEquationInputActivity.this, Double.parseDouble(mReciprocalOfString), Double.parseDouble(mInitialApproxString), mPrecision);
                }
            } else {
                validateNormalInputs();
                if (mAllInputsValid) {
                    SingleEquationOutputActivityStarter.start(SingleEquationInputActivity.this, mFunctionString, mLeftEndPoint, mRightEndPoint, mPrecision, mMethodId);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateNormalInputs() {

        mAllInputsValid = true;

        mFunctionString = mFunctionEditText.getText().toString();
        mFunction = new Function("mFunction", mFunctionString, "x");
        if (Double.isNaN(mFunction.calculate(1))) {
            mAllInputsValid = false;
            mFunctionEditText.setError("Invalid syntax");
        }

        mIntervalString = mIntervalEditText.getText().toString();
        if (!mIntervalString.matches("([+-]?\\d+(\\.\\d+)?)\\s*,\\s*([+-]?\\d+(\\.\\d+)?)")) {
            mAllInputsValid = false;
            mIntervalEditText.setError("Invalid syntax");
        } else {
            String[] coordinate = mIntervalString.split("\\s*,\\s*");
            mLeftEndPoint = Double.parseDouble(coordinate[0]);
            mRightEndPoint = Double.parseDouble(coordinate[1]);
            if (mLeftEndPoint > mRightEndPoint) {
                mAllInputsValid = false;
                mIntervalEditText.setError("REP must be greater than LEP");
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

    private void validateReciprocalInputs() {

        mAllInputsValid = true;

        mReciprocalOfString = mReciprocalOfEditText.getText().toString();
        double reciprocalOf = mReciprocalOfString.trim().isEmpty() ? 0.0 : Double.parseDouble(mReciprocalOfString);
        if (!mReciprocalOfString.matches("[+-]?\\d+(\\.\\d+)?") || (reciprocalOf >= -1 && reciprocalOf <= 1)) {
            mAllInputsValid = false;
            mReciprocalOfEditText.setError("Invalid syntax");
        }

        mInitialApproxString = mInitialApproxEditText.getText().toString();
        double initialApprox = mInitialApproxString.trim().isEmpty() ? 0.0 : Double.parseDouble(mInitialApproxString);
        if (!mInitialApproxString.matches("[+-]?(0.)\\d+") || (initialApprox == 0.0)) {
            mAllInputsValid = false;
            mInitialApproxEditText.setError("Invalid syntax");
        } else {
            @SuppressLint("DefaultLocale") StringBuilder actualReciprocal = new StringBuilder(String.format("%.25f", 1/reciprocalOf));
            for (int i = 2; i < 10; i++) {
                if (actualReciprocal.charAt(i) != '0') {
                    actualReciprocal.setLength(i+1);
                    break;
                }
            }
            if (actualReciprocal.length() > mInitialApproxString.length()) {
                mAllInputsValid = false;
                mInitialApproxEditText.setError("Please input some nearer value");
            }
            if ((reciprocalOf * initialApprox) < 0) {
                mAllInputsValid = false;
                mInitialApproxEditText.setError("Opposite signs found");
            }
        }

        if (!mPrecisionEditText.isValid("\\d")) {
            mAllInputsValid = false;
            mPrecisionEditText.setError("Invalid syntax");
        } else {
            mPrecision = Integer.parseInt(mPrecisionEditText.getText().toString());
            if (!(mPrecision >= 3 && mPrecision <= 9)) {
                mAllInputsValid = false;
                mPrecisionEditText.setError("Invalid syntax");
            }
        }
    }


    private void attachListenersOnInputs() {

        mFunctionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_function));
            }
        });

        mIntervalEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_interval));
            }
        });

        mReciprocalOfEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_reciprocal_of));
            }
        });

        mInitialApproxEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_reciprocal_initial_approximation));
            }
        });

        mPrecisionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mReciprocalSwitch.isChecked()) {
                    mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_reciprocal_precision));
                } else {
                    mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_precision));
                }
            }
        });
    }
}
