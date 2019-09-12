package nm.droid.com.numericalmethods;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.List;

import activitystarter.ActivityStarter;
import nm.droid.com.numericalmethods.differences.InterpolationInputActivityStarter;
import nm.droid.com.numericalmethods.differentiation.DifferentiationInputActivityStarter;
import nm.droid.com.numericalmethods.integration.IntegrationInputActivityStarter;
import nm.droid.com.numericalmethods.ordinarydifferentialequation.OrdinaryDifferentialInputActivityStarter;
import nm.droid.com.numericalmethods.powermatrix.PowerInputActivityStarter;
import nm.droid.com.numericalmethods.singleequation.SingleEquationInputActivityStarter;
import nm.droid.com.numericalmethods.systemofequations.SystemOfEquationsInputActivityStarter;


public class MethodListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<MethodRow> mMethodRows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStarter.fill(this, savedInstanceState);
        Toolbar toolbar = findViewById(R.id.method_list_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        if (isFirstRun()) {
            TapTargetView.showFor(MethodListActivity.this, TapTarget.forToolbarOverflow(toolbar, "Changelog, FAQ and About", "Do read once before using any of the methods :)").cancelable(true));
        }

        generateMethodRows();

        mRecyclerView = findViewById(R.id.method_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MethodAdapter(this, mMethodRows, MethodListActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private boolean isFirstRun() {

        final String PREFS_NAME = "TTPrefs";
        final String PREF_VERSION_CODE_KEY = "VER_CODE";
        final String DOES_NOT_EXIST = "null";

        String currentVersionName = BuildConfig.VERSION_NAME;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedVersionName = prefs.getString(PREF_VERSION_CODE_KEY, DOES_NOT_EXIST);

        if (currentVersionName.equalsIgnoreCase(savedVersionName)) {
            return false;
        } else {
            prefs.edit().putString(PREF_VERSION_CODE_KEY, currentVersionName).apply();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_method_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changelog:
                new MaterialDialog.Builder(this)
                        .title(R.string.app_version)
                        .content(R.string.version_changelog)
                        .contentLineSpacing(1.3f)
                        .contentColor(Color.parseColor("#424242"))
                        .positiveText(R.string.changelog_noice)
                        .show();
                return true;

            case R.id.action_FAQ:
                FaqActivityStarter.start(MethodListActivity.this);
                return true;

            case R.id.action_about:
                AboutActivityStarter.start(MethodListActivity.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void generateMethodRows() {

        MethodRow row = new MethodRow(R.string.title_bisection, R.string.subtitle_method, R.string.inputs_single_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_newton_raphson, R.string.subtitle_method, R.string.inputs_single_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_regula_falsi, R.string.subtitle_method, R.string.inputs_single_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_gauss_jacobi, R.string.subtitle_method, R.string.inputs_system_of_equations);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_gauss_seidel, R.string.subtitle_method, R.string.inputs_system_of_equations);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_power, R.string.subtitle_power, R.string.inputs_power);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_euler, R.string.subtitle_method, R.string.inputs_ordinary_differential_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_heun, R.string.subtitle_method, R.string.inputs_ordinary_differential_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_runge_kutta, R.string.subtitle_runge_kutta, R.string.inputs_ordinary_differential_equation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_newton, R.string.subtitle_newton, R.string.inputs_interpolation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_stirling, R.string.subtitle_stirling, R.string.inputs_interpolation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_lagrange, R.string.subtitle_lagrange, R.string.inputs_interpolation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_newton_dd, R.string.subtitle_newton_dd, R.string.inputs_interpolation);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_integration, R.string.subtitle_integration, R.string.inputs_integration);
        mMethodRows.add(row);

        row = new MethodRow(R.string.title_differentiation, R.string.subtitle_differentiation, R.string.inputs_differentiation);
        mMethodRows.add(row);
    }

    void runCorrespondingMethod(int position) {
        switch (position) {
            case 0:
                SingleEquationInputActivityStarter.start(MethodListActivity.this, 0);
                break;

            case 1:
                SingleEquationInputActivityStarter.start(MethodListActivity.this, 1);
                break;

            case 2:
                SingleEquationInputActivityStarter.start(MethodListActivity.this, 2);
                break;

            case 3:
                SystemOfEquationsInputActivityStarter.start(MethodListActivity.this, 3);
                break;

            case 4:
                SystemOfEquationsInputActivityStarter.start(MethodListActivity.this, 4);
                break;

            case 5:
                PowerInputActivityStarter.start(MethodListActivity.this);
                break;

            case 6:
                OrdinaryDifferentialInputActivityStarter.start(MethodListActivity.this, 6);
                break;

            case 7:
                OrdinaryDifferentialInputActivityStarter.start(MethodListActivity.this, 7);
                break;

            case 8:
                OrdinaryDifferentialInputActivityStarter.start(MethodListActivity.this, 8);
                break;

            case 9:
                InterpolationInputActivityStarter.start(MethodListActivity.this, 9);
                break;

            case 10:
                InterpolationInputActivityStarter.start(MethodListActivity.this, 10);
                break;

            case 11:
                InterpolationInputActivityStarter.start(MethodListActivity.this, 11);
                break;

            case 12:
                InterpolationInputActivityStarter.start(MethodListActivity.this, 12);
                break;

            case 13:
                IntegrationInputActivityStarter.start(MethodListActivity.this);
                break;

            case 14:
                DifferentiationInputActivityStarter.start(MethodListActivity.this);
                break;
        }
    }
}
