package study.cp.datastoreanalisys;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(v -> finish());
    }
}
