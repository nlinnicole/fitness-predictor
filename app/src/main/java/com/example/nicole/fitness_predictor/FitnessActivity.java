package com.example.nicole.fitness_predictor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class FitnessActivity extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
//
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        double[] xAxisData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] yAxisData = {14.5, 13.8, 12.9, 14.2, 14.0, 13.2, 13.7, 14.2, 15.2, 14.8};
        String title = "Average Speed vs. Day";
        String yAxisLabel = "Average Speed";
        String xAxisLabel = "Day";

        double[] xAxisData2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] yAxisData2 = {40.3, 39.8, 40.8, 35.2, 34.7, 41.2, 36.5, 33.9, 43.2, 40.2};
        String title2 = "Average Time vs. Day";
        String yAxisLabel2 = "Average Time";
        String xAxisLabel2 = "Day";

        GraphFragment graphFragment = GraphFragment.newInstance(xAxisData, yAxisData, title, yAxisLabel, xAxisLabel);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.graphContainer, graphFragment).commit();

        GraphFragment graphFragment2 = GraphFragment.newInstance(xAxisData2, yAxisData2, title2, yAxisLabel2, xAxisLabel2);

        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        ft2.add(R.id.graphContainer, graphFragment2).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
