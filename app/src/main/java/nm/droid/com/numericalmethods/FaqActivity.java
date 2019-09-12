package nm.droid.com.numericalmethods;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.view.Menu;
import android.widget.TextView;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class FaqActivity extends MethodListActivity {

    private TextView mFunctionFaqContent;
    private TextView mRKFAQTextView;
    private TextView mGreetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.faq_toolbar);
        toolbar.setTitle(R.string.action_FAQ);
        setSupportActionBar(toolbar);

        mFunctionFaqContent = findViewById(R.id.function_faq_content);
        CharSequence bulletedList = BulletListUtil.makeBulletListFromStringResources(15, FaqActivity.this,
                R.string.function_faq_line1,
                R.string.function_faq_line2,
                R.string.function_faq_line3,
                R.string.function_faq_line4,
                R.string.function_faq_line5,
                R.string.function_faq_line6,
                R.string.function_faq_line7);
        mFunctionFaqContent.setText(bulletedList);

        mRKFAQTextView = findViewById(R.id.rk_faq_content);
        mRKFAQTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mGreetTextView = findViewById(R.id.greet);
        mGreetTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public static class BulletListUtil {

        public static CharSequence makeBulletListFromStringArrayResource(int leadingMargin, Context context, int stringArrayResId) {
            return makeBulletList(leadingMargin, context.getResources().getTextArray(stringArrayResId));
        }

        static CharSequence makeBulletListFromStringResources(int leadingMargin, Context context, int... linesResIds) {
            int len = linesResIds.length;
            CharSequence[] cslines = new CharSequence[len];
            for (int i = 0; i < len; i++) {
                cslines[i] = context.getResources().getText(linesResIds[i]);
            }
            return makeBulletList(leadingMargin, cslines);
        }

        static CharSequence makeBulletList(int leadingMargin, CharSequence... lines) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            for (int i = 0; i < lines.length; i++) {
                CharSequence line = lines[i] + (i < lines.length-1 ? "\n" : "");
                Spannable spannable = new SpannableString(line);
                spannable.setSpan(new BulletSpan(leadingMargin), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append(spannable);
            }
            return sb;
        }

    }
}
