package com.example.nicole.fitness_predictor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FitnessFragment extends Fragment implements GraphFragment.OnFragmentInteractionListener {

    private GraphFragment graphFragment;
    private GraphFragment graphFragment2;

    public static FitnessFragment newInstance() {
        Bundle args = new Bundle();
        FitnessFragment fragment = new FitnessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.activity_fitness,null);

        FitnessApplication application = (FitnessApplication)getActivity().getApplicationContext();
        EndomondoSession session = application.getEndomondoSession();
        EndomondoQueryTask task = new EndomondoQueryTask(session);
        task.execute((Void)null);

        return view;
    }

    @Override
    public void onDestroyView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(graphFragment);
        ft.remove(graphFragment2);
        graphFragment = null;
        graphFragment2 = null;
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
        graphFragment = GraphFragment.newInstance(toDate(xAxisData),
                                                                toPrimitive(averageSpeedData),
                                                                title,
                                                                yAxisLabel,
                                                                xAxisLabel);
        graphFragment.toBar();

        //Create graph 2
        graphFragment2 = GraphFragment.newInstance(toDate(xAxisData),
                toPrimitive(durationData),
                title2,
                yAxisLabel2,
                xAxisLabel2);
        graphFragment2.toLine();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graphContainer, graphFragment);
        ft.add(R.id.graphContainer, graphFragment2);
        ft.commit();
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