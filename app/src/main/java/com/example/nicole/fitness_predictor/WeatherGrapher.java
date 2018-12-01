package com.example.nicole.fitness_predictor;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.moomeen.endo2java.EndomondoSession;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.io.InputStream;

public class WeatherGrapher extends Fragment implements GraphFragment.OnFragmentInteractionListener  {
    public enum graphType {
        AVG_TEMP,
        WIND_SPEED,
        NONE
    }

    private GraphFragment graphFragment;

    private graphType chosenGraphType = graphType.NONE;

    public static WeatherGrapher newInstance(graphType type) {
        Bundle args = new Bundle();
        args.putSerializable("chosenGraphType", type);
        WeatherGrapher fragment = new WeatherGrapher();
        fragment.setChosenGraphType(type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.activity_weathergrapher,null);
        // chosenGraphType = (graphType) savedInstanceState.getSerializable("chosenGraphType");

        // mTextMessage = (TextView) findViewById(R.id.message);
        // BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //????????
        //WeatherGrapher application = (WeatherGrapher)getApplicationContext();

        weatherDataReader(chosenGraphType);
        return view;
    }


    @Override
    public void onDestroyView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(graphFragment);
        graphFragment = null;
        /**
         * TODO FIXME: I'd rather not allow state loss here, but I'm getting an exception
         */
        ft.commitAllowingStateLoss();
        super.onDestroyView();
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    public void setChosenGraphType(graphType type) {
        chosenGraphType = type;
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
        switch(chosenType) {
            case AVG_TEMP:
                chosenY = "Avg Temp";
            case WIND_SPEED:
                chosenY = "Wind Speed";
        }
        String in = loadJSONFromAsset();

        JSONObject reader = null;
        try {
            reader = new JSONObject(in);
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        Date currentDate = new Date(118, 9, 25);
        for(int i = 0; i < 14; ++i)
        {
            try {
                String dateInString;
                if(currentDate.getDate()<10)
                    dateInString = "0" + currentDate.getDate() + "/" + (currentDate.getMonth()+1) + "/" + (currentDate.getYear() -100);
                else
                    dateInString = currentDate.getDate() + "/" + (currentDate.getMonth()+1) + "/" + (currentDate.getYear() -100);
                JSONObject jsonObject = reader.getJSONObject(dateInString);
                dates.add(new Date(currentDate.getYear(), currentDate.getMonth(),currentDate.getDate()));
                yAxis.add(jsonObject.getDouble(chosenY));
                currentDate.setDate(currentDate.getDate() + 1);
            }
            catch (Exception e) {
                return;
            }
        }
        System.out.println(dates);
        switch(chosenType) {
            case AVG_TEMP:
                graphAvgTemp(dates, yAxis);
                break;
            case WIND_SPEED:
                graphWindSpeed(dates,yAxis);
                break;
        }

    }

    private void graphWindSpeed(ArrayList<Date> dates, ArrayList<Double> windSpeeds) {
        if (dates.isEmpty() || windSpeeds.isEmpty()) {
            return;
        }

        String title = "Wind Speed";
        String yAxisLabel = "km/h";
        String xAxisLabel = "Day";

        //Create graph 1
        graphFragment = GraphFragment.newInstance(toDate(dates),
                toPrimitive(windSpeeds),
                title,
                yAxisLabel,
                xAxisLabel);
        graphFragment.toLine();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graphWeatherContainer, graphFragment).commit();
    }

    private void graphAvgTemp(ArrayList<Date> dates, ArrayList<Double> avgTemps) {
        if (dates.isEmpty() || avgTemps.isEmpty()) {
            return;
        }

        String title = "Average Temperature";
        String yAxisLabel = "Degrees Celsius";
        String xAxisLabel = "Day";

        //Create graph 1
        graphFragment = GraphFragment.newInstance(toDate(dates),
                toPrimitive(avgTemps),
                title,
                yAxisLabel,
                xAxisLabel);
        graphFragment.toLine();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.graphWeatherContainer, graphFragment).commit();

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


}
