package com.example.nicole.fitness_predictor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import java.util.Date;

public class AnalysisFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView estimateText;
    private static LinearRegression regression = new LinearRegression();
    public AnalysisFragment() { }

    public static void haveWorkouts(List<Workout> workouts, Context myContext)
    {
        regression.fitnessDataReader(workouts);
        //String in = loadJSONFromAsset();
        //regression.weatherDataReader(in);
        regression.doLearning(myContext);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AnalysisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalysisFragment newInstance(String param1) {
        AnalysisFragment fragment = new AnalysisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        Random generator = new Random(new Date().getDate()*10000);
        double todaysTemp = generator.nextDouble()*20-15;
        FitnessApplication application = (FitnessApplication)getActivity().getApplicationContext();
        EndomondoSession session = application.getEndomondoSession();
        AnalysisFragment.EndomondoQueryTask task = new AnalysisFragment.EndomondoQueryTask(session);
        estimateText = (TextView) view.findViewById(R.id.estimateText);
        double estimatedDuration = Math.round(regression.estimate(todaysTemp)*100)/100.0;

        if(estimatedDuration > regression.averageDuration())
            estimateText.setText("Todays estimated exercise duration is " + estimatedDuration + " minutes, which is higher than your average!");
        else
            estimateText.setText("Todays estimated exercise duration is " + estimatedDuration + " minutes, which is lower than your average!");
        return view;
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
                regression.fitnessDataReader(workouts);
                //regression.doLearning();
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
