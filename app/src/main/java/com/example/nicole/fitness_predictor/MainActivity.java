package com.example.nicole.fitness_predictor;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri) { }

    private class PageAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] {
                getString(R.string.tab_fitness_activity),
                getString(R.string.tab_weather1_activity),
                getString(R.string.tab_weather2_activity)
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
                    return WeatherGrapher.newInstance(WeatherGrapher.graphType.AVG_TEMP);
                case 2:
                    return WeatherGrapher.newInstance(WeatherGrapher.graphType.WIND_SPEED);
                default:
                    return FitnessFragment.newInstance();
            }
        }
    }
}
