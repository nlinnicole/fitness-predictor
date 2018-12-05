package com.example.nicole.fitness_predictor;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.io.InputStream;

public class WeatherFragment extends Fragment implements GraphFragment.OnFragmentInteractionListener {
    public enum graphType {
        AVG_TEMP,
        WIND_SPEED,
        NONE
    }

    private GraphFragment graphFragment;
    private GraphFragment graphFragment2;

    private TextView graphTitle1 = null;
    private TextView graphTitle2 = null;

    public static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_weather, null);

        /**
         * We display both graph ALWAYS to be consistent with FitnessFragment
         */
        weatherDataReader(graphType.AVG_TEMP);
        weatherDataReader(graphType.WIND_SPEED);

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
    public void onFragmentInteraction(Uri uri) {
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("weatherData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void weatherDataReader(graphType chosenType) {
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Double> yAxis = new ArrayList<>();
        String chosenY = "";
        switch (chosenType) {
            case AVG_TEMP:
                chosenY = "Avg Temp";
                break;
            case WIND_SPEED:
                chosenY = "Wind Speed";
                break;
        }
        String in = loadJSONFromAsset();

        JSONObject reader = null;
        try {
            reader = new JSONObject(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date startDate = new Date(118, 3, 1);
        Date todaysDate = new Date();
        while(!(startDate.getYear() == todaysDate.getYear() && startDate.getMonth() == todaysDate.getMonth() && startDate.getDate() == todaysDate.getDate()))
        {
            try {
                String dateInString;
                dateInString = startDate.getDate() + "/" + (startDate.getMonth() + 1) + "/" + (startDate.getYear() - 100);
                JSONObject jsonObject = reader.getJSONObject(dateInString);
                dates.add(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate()));
                yAxis.add(jsonObject.getDouble(chosenY));
                startDate.setDate(startDate.getDate() + 1);
            } catch (Exception e) {
                startDate.setDate(startDate.getDate() + 1);
            }
        }
        //System.out.println(dates);
        switch (chosenType) {
            case AVG_TEMP:
                graphAvgTemp(dates, yAxis);
                break;
            case WIND_SPEED:
                graphWindSpeed(dates, yAxis);
                break;
        }
    }

    public void graphWindSpeed(ArrayList<Date> dates, ArrayList<Double> windSpeeds) {
        if (dates.isEmpty() || windSpeeds.isEmpty()) {
            return;
        }

        //Create graph 1
        graphFragment = GraphFragment.newInstance(toDate(dates),
                toPrimitive(windSpeeds),
                "Wind Speed",
                "km/h",
                "Day");
        graphFragment.toLine();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graphContainer, graphFragment).commitNow();
    }

    public void graphAvgTemp(ArrayList<Date> dates, ArrayList<Double> avgTemps) {
        if (dates.isEmpty() || avgTemps.isEmpty()) {
            return;
        }

        //Create graph 1
        graphFragment2 = GraphFragment.newInstance(toDate(dates),
                toPrimitive(avgTemps),
                "Average Temperature",
                "Degrees Celsius",
                "Day");
        graphFragment2.toLine();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graph2Container, graphFragment2).commitNow();
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
}
