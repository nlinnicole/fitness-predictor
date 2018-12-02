package com.example.nicole.fitness_predictor;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener {
    private static final int REQUEST_ENDOMONDO_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleLogin();
    }

    private void showDashboard() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void handleLogin() {
        Intent endomondoLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(endomondoLoginIntent, REQUEST_ENDOMONDO_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENDOMONDO_LOGIN) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                Log.d("FITPREDLOG", "Login from Endomondo success");
                showDashboard();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    /**
     * The PageAdapter handles the fragments to show (So the tabs)
     */
    private class PageAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] {
                getString(R.string.tab_fitness_fragment),
                getString(R.string.tab_weather_fragment),
                getString(R.string.tab_analysis_fragment)
        };
        private Context context;

        public PageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FitnessFragment.newInstance();
                case 1:
                    return WeatherFragment.newInstance();
                case 2:
                    return AnalysisFragment.newInstance("Param 1");
                default:
                    return FitnessFragment.newInstance();
            }
        }
    }
}
