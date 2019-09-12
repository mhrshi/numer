package nm.droid.com.numericalmethods;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import activitystarter.ActivityStarter;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.about_toolbar);
        toolbar.setTitle(R.string.action_about);
        setSupportActionBar(toolbar);

        TextView mChannelLinkTextView = findViewById(R.id.telegram_channel);
        mChannelLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        AppCompatImageView AboutEmailImageView = findViewById(R.id.about_email);
        AboutEmailImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("mailto:bhavsarm99@gmail.com"));
            startActivity(intent);
        });

        AppCompatImageView AboutTelegramImageView = findViewById(R.id.about_telegram);
        AboutTelegramImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://t.me/cooldroid7"));
            startActivity(intent);
        });

        LinearLayout LicensesLinearLayout = findViewById(R.id.licenses);
        LicensesLinearLayout.setOnClickListener(v -> new MaterialDialog.Builder(AboutActivity.this)
                .title(R.string.licenses)
                .customView(R.layout.dialog_license_content, true)
                .positiveText("OK")
                .show());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
