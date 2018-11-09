package com.example.nicole.fitness_predictor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

        FitnessApplication application = (FitnessApplication)getApplicationContext();
        EndomondoSession session = application.getEndomondoSession();
        EndomondoQueryTask task = new EndomondoQueryTask(session);
        task.execute((Void)null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean isSameDay(DateTime a, DateTime b) {
        boolean isSameYear = a.getYear() == b.getYear();
        boolean isSameDayOfYear = a.getDayOfYear() == b.getDayOfYear();

        return isSameYear && isSameDayOfYear;
    }

    private List<Workout> sortAndFilter(List<Workout> workouts) {
        if (workouts.isEmpty()) {
            return workouts;
        }

        Collections.sort(workouts, new Comparator<Workout>() {
            @Override
            public int compare(Workout a, Workout b) {
                return a.getStartTime().compareTo(b.getStartTime());
            }
        });

        // FIXME TODO: For now we remove workouts that are on the same dates
        ArrayList<Workout> result = new ArrayList<>(workouts.size());
        result.add(workouts.get(0));

        DateTime currentTime = result.get(0).getStartTime();

        for (int i = 1; i < workouts.size(); i++) {
            Workout workout = workouts.get(i);

            if (isSameDay(currentTime, workout.getStartTime())) {
                continue;
            }

            result.add(workout);
            currentTime = workout.getStartTime();
        }

        return result;
    }

    // TODO: Display graph first and then asynchronously add the data ?
    private void displayFitnessActivity(List<Workout> workouts) {
        if (workouts.isEmpty()) {
            return;
        }

        workouts = sortAndFilter(workouts);

        int size = workouts.size();
        ArrayList<Double> averageSpeedData = new ArrayList<>(size);
        ArrayList<Double> durationData = new ArrayList<>(size);
        ArrayList<Date> xAxisData = new ArrayList<>(size);

        /**
         * This disgusting piece of code fills the date that are missing
         * (eg: We display 2018-06-01 even if there was no workout that day)
         */
        DateTime currentTime = workouts.get(0).getStartTime();
        for (int i = 0; i < size; i++) {
            Workout workout = workouts.get(i);
            //add workout data to respective ArrayLists
            while (!isSameDay(currentTime, workout.getStartTime())) {
                averageSpeedData.add(Double.valueOf(0));
                durationData.add(Double.valueOf(0));

                xAxisData.add(currentTime.toDate());

                currentTime = currentTime.plusDays(1);
            }

            Double averageSpeed = workout.getSpeedAvg();
            DateTime startTime = workout.getStartTime();
            Duration duration = workout.getDuration();

            Log.d("FITPREDLOG", "speed: " + averageSpeed + ", duration: " + duration + ", start: " + startTime);

            averageSpeedData.add(averageSpeed.doubleValue());
            durationData.add(Double.valueOf(duration.getStandardMinutes()));
            xAxisData.add(currentTime.toDate());
            currentTime = currentTime.plusDays(1);
        }

        String title = getString(R.string.fitness_graph_average_speed_title);
        String yAxisLabel = getString(R.string.fitness_graph_average_speed_axis);
        String xAxisLabel = getString(R.string.fitness_graph_date_axis);

        String title2 = getString(R.string.fitness_graph_duration_title);
        String yAxisLabel2 = getString(R.string.fitness_graph_duration_axis);
        String xAxisLabel2 = getString(R.string.fitness_graph_date_axis);

        //Create graph 1
        GraphFragment graphFragment = GraphFragment.newInstance(toDate(xAxisData),
                                                                toPrimitive(averageSpeedData),
                                                                title,
                                                                yAxisLabel,
                                                                xAxisLabel);
        graphFragment.toBar();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.graphContainer, graphFragment).commit();

        //Create graph 2
        GraphFragment graphFragment2 = GraphFragment.newInstance(toDate(xAxisData),
                                                                 toPrimitive(durationData),
                                                                 title2,
                                                                 yAxisLabel2,
                                                                 xAxisLabel2);
        graphFragment2.toLine();
        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        ft2.add(R.id.graphContainer, graphFragment2).commit();
    }

    private double[] toPrimitive(ArrayList<Double> list) {
        double[] result = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    //NEED BETTER SOLUTION TO NOT REPEAT CODE
    private Date[] toDate(ArrayList<Date> list){
        Date[] result = new Date[list.size()];

        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }
        return result;
    }

    private class EndomondoQueryTask extends AsyncTask<Void, List<Workout>, List<Workout>> {
        private EndomondoSession session;

        EndomondoQueryTask(@NonNull EndomondoSession session) {
            this.session = session;
        }

        @Override
        protected List<Workout> doInBackground(Void... params) {
            Log.d("FITPREDLOG", "Fetching Endomondo workouts");

            List<Workout> workouts;

            try {
                workouts = session.getWorkouts();
                Log.d("FITPREDLOG", "Found " + workouts.size() + " workouts!");
            } catch (InvocationException exception) {
                Log.d("FITPREDLOG", "Error getting workouts " + exception);
                workouts = new ArrayList<>();
            }

            return workouts;
        }

        @Override
        protected void onPostExecute(final List<Workout> workouts) {
            if (workouts.size() > 0) {
                displayFitnessActivity(workouts);
            } else {
                // TODO: Unsure what do to.. Do we display a graph without any points ?
                Log.d("FITPREDLOG", "No workout found from Endomondo");
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
