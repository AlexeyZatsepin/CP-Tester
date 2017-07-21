package study.cp.datastoreanalisys.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import study.cp.datastoreanalisys.R;
import study.cp.datastoreanalisys.fragments.InfoFragment;
import study.cp.datastoreanalisys.fragments.QueryFragment;
import study.cp.datastoreanalisys.fragments.SchemaFragment;

import static study.cp.datastoreanalisys.Utils.NUMBER_INFO;
import static study.cp.datastoreanalisys.Utils.NUMBER_QUERY;
import static study.cp.datastoreanalisys.Utils.NUMBER_SCHEMA;

public class DetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProviderInfo provider;

    private static String EXTRA_PROVIDER = "provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        provider = getIntent().getParcelableExtra(EXTRA_PROVIDER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case NUMBER_INFO:
                    return InfoFragment.newInstance(provider);
                case NUMBER_SCHEMA:
                    return SchemaFragment.newInstance(provider);
                case NUMBER_QUERY:
                    return QueryFragment.newInstance(provider);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case NUMBER_INFO:
                    return getString(R.string.tab_title_1);
                case NUMBER_SCHEMA:
                    return getString(R.string.tab_title_2);
                case NUMBER_QUERY:
                    return getString(R.string.tab_title_3);
            }
            return null;
        }
    }

    public static Intent newIntent(Context packageContext, ProviderInfo providerInfo) {
        Intent intent = new Intent(packageContext, DetailsActivity.class);
        intent.putExtra(EXTRA_PROVIDER, providerInfo);
        return intent;
    }
}
