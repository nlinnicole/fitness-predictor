package com.example.nicole.fitness_predictor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FitnessFragment extends Fragment implements GraphFragment.OnFragmentInteractionListener {

    final public int MIN_DURATION_IN_MINUTES = 0;
    final public int MAX_DURATION_IN_MINUTES = 100;
    final public int MIN_AVG_SPEED = 0;
    final public int MAX_AVG_SPEED = 50;

    private TextView boldText = null;
    private GraphFragment graphFragment;
    private GraphFragment graphFragment2;

    private TextView graphTitle1 = null;
    private TextView graphTitle2 = null;


    public static FitnessFragment newInstance() {
        Bundle args = new Bundle();
        FitnessFragment fragment = new FitnessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fitness);
        View  view = inflater.inflate(R.layout.fragment_fitness,null);

        FitnessApplication application = (FitnessApplication)getActivity().getApplicationContext();
        EndomondoSession session = application.getEndomondoSession();
        EndomondoQueryTask task = new EndomondoQueryTask(session);
        task.execute((Void)null);

        return view;
    }

    @Override
    public void onDestroyView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (graphFragment != null) {
            ft.remove(graphFragment);
        }
        if (graphFragment2 != null) {
            ft.remove(graphFragment2);
        }
        /**
         * TODO FIXME: I'd rather not allow state loss here, but I'm getting an exception
         */
        ft.commitAllowingStateLoss();
        super.onDestroyView();
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    private boolean isSameDay(DateTime a, DateTime b) {
        boolean isSameYear = a.getYear() == b.getYear();
        boolean isSameDayOfYear = a.getDayOfYear() == b.getDayOfYear();

        return isSameYear && isSameDayOfYear;
    }

    public List<Workout> sortAndFilter(List<Workout> workouts) {
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

    public void fillDates(List<Workout> workouts,
                                  ArrayList<Double> averageSpeedData,
                                  ArrayList<Double> durationData,
                                  ArrayList<Date> xAxisData)
    {
        if (workouts.isEmpty()) {
            return;
        }
        DateTime currentTime = workouts.get(0).getStartTime();
        for (int i = 0; i < workouts.size(); i++) {
            Workout currentWorkout = workouts.get(i);
            //add workout data to respective ArrayLists
            while (!isSameDay(currentTime, currentWorkout.getStartTime())) {
                averageSpeedData.add(0d);
                durationData.add(0d);

                xAxisData.add(currentTime.toDate());
                Log.d("FITPREDLOG", "Day with no workout data: " + currentTime);
                currentTime = currentTime.plusDays(1);
            }

            Double averageSpeed = currentWorkout.getSpeedAvg();
            DateTime startTime = currentWorkout.getStartTime();
            Duration duration = currentWorkout.getDuration();

            Log.d("FITPREDLOG", "speed: " + averageSpeed + ", duration: " + duration + ", start: " + startTime);

            //Filter data
            // TODO: Decide on filtering limits
            if (averageSpeed <= MIN_AVG_SPEED || averageSpeed >= MAX_AVG_SPEED) {
                //assume error, automatically set to 0
                averageSpeedData.add(0.0);
            } else {
                averageSpeedData.add(averageSpeed);
            }

            if (duration.getStandardMinutes() <= MIN_DURATION_IN_MINUTES|| duration.getStandardMinutes() >= MAX_DURATION_IN_MINUTES) {
                //assume error, automatically set to 0
                durationData.add(0.0);
            } else {
                durationData.add(Double.valueOf(duration.getStandardMinutes()));
            }
            xAxisData.add(currentTime.toDate());
            currentTime = currentTime.plusDays(1);
        }
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

        fillDates(workouts, averageSpeedData, durationData, xAxisData);

        //Moving Average
        ArrayList<Double> speedMovAvg = getMovingAverage(averageSpeedData, 3);
        ArrayList<Double> durationMovAvg = getMovingAverage(durationData, 3);

        //Create average speed graph
        graphFragment = GraphFragment.newInstance(toDate(xAxisData),
                toPrimitive(averageSpeedData),
                "Average Speed per Day",
                "Average Speed (km/h)",
                "Day");
        graphFragment.toBar();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graphContainer, graphFragment).commitNow();
        graphFragment.addSeries(toDate(xAxisData), toPrimitive(speedMovAvg));

        boldText = getView().findViewById(R.id.avgText);
        boldText.setText("Overall Average Speed: " + getAverage(averageSpeedData) + "km/h");

        graphTitle1 = getView().findViewById(R.id.graphTitle);
        graphTitle1.setText("Average Speed per Day");

        //Create average duration graph
        graphFragment2 = GraphFragment.newInstance(toDate(xAxisData),
                toPrimitive(durationData),
                "Duration per Day",
                "Duration (min)",
                "Day");
        graphFragment2.toLine();

        FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
        ft2.add(R.id.graph2Container, graphFragment2).commitNow();
        graphFragment2.addSeries(toDate(xAxisData), toPrimitive(durationMovAvg));

        graphTitle2 = getView().findViewById(R.id.graphTitle2);
        graphTitle2.setText("Duration per Day");

        boldText = getView().findViewById(R.id.avgText2);
        boldText.setText("Overall Average Duration: " + getAverage(durationData) + "min");
    }

    private double[] toPrimitive(ArrayList<Double> list) {
        double[] result = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private Date[] toDate(ArrayList<Date> list) {
        Date[] result = new Date[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private double getAverage(ArrayList<Double> list) {
        double sum = 0;
        if (!list.isEmpty()) {
            for (Double value : list) {
                sum += value;
            }
            double avg = sum / list.size();
            return round(avg);
        }
        return sum;
    }

    private ArrayList<Double> getMovingAverage(ArrayList<Double> list, int size) {
        ArrayList<Double> result = new ArrayList<Double>();
        int window = size;

        //moving average offset
        for (int i = 0; i < window; ++i) {
            result.add(0.0);
        }
        for (int i = 0; i < list.size(); i++) {
            if ((i + (window-1)) >= 0 && (i + (window-1)) < list.size()) {
                double sum = 0;
                for (int j = 0; j < window; j++) {
                    sum += list.get(i + j);
                }
                double avg = sum / window;
                result.add(round(avg));
            }
        }
        return result;
    }

    private double round(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        return Double.valueOf(decimalFormat.format(d));
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
