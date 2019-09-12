package nm.droid.com.numericalmethods.systemofequations;

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

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import nm.droid.com.numericalmethods.FaqActivityStarter;
import nm.droid.com.numericalmethods.MethodListActivity;
import nm.droid.com.numericalmethods.R;

public class SystemOfEquationsInputActivity extends MethodListActivity {

    private TextView mSyntaxTipTextView;
    private MaterialEditText mSystemMatrixEditText;
    private MaterialEditText mPrecisionEditText;
    private Button mJumpToFaqButton;
    private Button mCalculateButton;

    @Arg public int mMethodId;

    private String mSystemMatrixString;
    private int mPrecision;

    private boolean mAllInputsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_of_equations_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        switch(mMethodId) {
            case 3:
                toolbar.setTitle(R.string.title_gauss_jacobi);
                break;

            case 4:
                toolbar.setTitle(R.string.title_gauss_seidel);
                break;
        }

        mSyntaxTipTextView = findViewById(R.id.syntax_tip);
        mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_system_matrix));
        mSystemMatrixEditText = findViewById(R.id.edit_text_system_matrix);
        mPrecisionEditText = findViewById(R.id.edit_text_precision);

        attachListenersOnInputs();

        mJumpToFaqButton = findViewById(R.id.button_jump_to_faq);
        mJumpToFaqButton.setOnClickListener(v -> FaqActivityStarter.start(SystemOfEquationsInputActivity.this));

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
                SystemOfEquationsOutputActivityStarter.start(SystemOfEquationsInputActivity.this, mSystemMatrixString, mPrecision, mMethodId);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void validateInputs() {

        mAllInputsValid = true;

        mSystemMatrixString = mSystemMatrixEditText.getText().toString();
        if (!mSystemMatrixString.matches("([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)[\\r\\n]+([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)[\\r\\n]+([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)\\s+([+-]?\\d+)")) {
            mAllInputsValid = false;
            mSystemMatrixEditText.setError("Invalid syntax");
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

        mSystemMatrixEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_system_matrix));
            }
        });

        mPrecisionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mSyntaxTipTextView.setText(getResources().getText(R.string.syntax_tip_precision));
            }
        });
    }
}
