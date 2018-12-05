package com.example.nicole.fitness_predictor;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.json.JSONObject;
import java.io.InputStream;

import android.content.Context;


public class LinearRegression extends AppCompatActivity {

    double a;
    double b;

    ArrayList<Date> weatherDates;
    ArrayList<Double> averageTempData;
    ArrayList<Double> windSpeedData;

    ArrayList<Double> averageSpeedData;
    ArrayList<Double> durationData;
    ArrayList<Date> fitnessDates;

    public LinearRegression()
    {
        initiateMachineLearning();
    }

    public void initiateMachineLearning()
    {


    }
    double avgDuration;
    public double averageDuration()
    {
        return avgDuration;
    }

    public void doLearning(Context myContext)
    {
        weatherDataReader(myContext);
        matchDates();
        calculateLinearRelation(averageTempData, durationData);
    }

    private void matchDates()
    {
        int fitnessDatesSize = fitnessDates.size();
        int weatherDatesSize = weatherDates.size()
        if(fitnessDatesSize == weatherDatesSize)
            return;
        if(fitnessDatesSize < weatherDatesSize)
        {
            while(fitnessDatesSize != weatherDatesSize)
            {
                weatherDates.remove(weatherDates.size()-1);
                averageTempData.remove(averageTempData.size()-1);
                windSpeedData.remove(windSpeedData.size()-1);
                weatherDatesSize = weatherDates.size();
            }
        }
        else // if(fitnessDatesSize > weatherDatesSize)
        {
            while(weatherDatesSize != fitnessDatesSize)
            {
                fitnessDates.remove(weatherDates.size()-1);
                averageSpeedData.remove(averageTempData.size()-1);
                durationData.remove(windSpeedData.size()-1);
                fitnessDatesSize = fitnessDates.size();
            }
        }
    }

    private void calculateLinearRelation(List<Double> x, List<Double> y)
    {

        int size = x.size();
        double sumOfX = 0;
        double sumOfY = 0;
        double sumOfXY = 0;
        double sumOfXSquared = 0;
        double sumOfYSquared = 0;

        for(int i = 0; i < size; ++i)
        {
            double currentX = x.get(i);
            double currentY = y.get(i);

            sumOfX += currentX;
            sumOfY += currentY;
            sumOfXY += currentX*currentY;
            sumOfXSquared += currentX*currentX;
            sumOfYSquared += currentY*currentY;
        }

        a = (sumOfY*sumOfXSquared - sumOfX*sumOfXY) / (size*sumOfXSquared - sumOfX*sumOfX);
        b = (size*sumOfXY - sumOfX*sumOfY) / (size*sumOfXSquared - sumOfX*sumOfX);
        //double rSquared = (size)
        avgDuration = sumOfY/size;

    }

    public double estimate(double weatherStat)
    {
        return a + b*weatherStat;
    }


    public String loadJSONFromAsset(Context myContext) {
        String json = null;
        try {
            InputStream is = myContext.getAssets().open("weatherData.json");
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

    public void weatherDataReader(Context myContext) {
        weatherDates = new ArrayList<>();
        averageTempData = new ArrayList<>();
        windSpeedData = new ArrayList<>();
        String chosenY = "";

        String in = loadJSONFromAsset(myContext);

        JSONObject reader = null;
        try {
            reader = new JSONObject(in);
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        Date startDate = new Date(118, 3, 1);
        Date todaysDate = new Date();
        while(!(startDate.getYear() == todaysDate.getYear() && startDate.getMonth() == todaysDate.getMonth() && startDate.getDate() == todaysDate.getDate()))
        {
            try {
                String dateInString;
                dateInString = startDate.getDate() + "/" + (startDate.getMonth()+1) + "/" + (startDate.getYear() -100);
                JSONObject jsonObject = reader.getJSONObject(dateInString);
                weatherDates.add(new Date(startDate.getYear(), startDate.getMonth(),startDate.getDate()));
                averageTempData.add(jsonObject.getDouble("Avg Temp"));
                windSpeedData.add(jsonObject.getDouble("Wind Speed"));
                startDate.setDate(startDate.getDate() + 1);
            }
            catch (Exception e) {
                startDate.setDate(startDate.getDate() + 1);
            }
        }

        //save weatherDates, averageTempData and windSpeedData
    }

    public void fitnessDataReader(List<Workout> workouts)
    {
        workouts = sort(workouts);

        int size = workouts.size();
        averageSpeedData = new ArrayList<>(size);
        durationData = new ArrayList<>(size);
        fitnessDates = new ArrayList<>(size);

        DateTime currentTime = workouts.get(0).getStartTime();
        for (int i = 0; i < size; i++) {
            Workout workout = workouts.get(i);
            Double averageSpeed = workout.getSpeedAvg();
            Duration duration = workout.getDuration();


            //Filter data
            // TODO: Decide on filtering limits
            if (averageSpeed <= 0 || averageSpeed >= 50) {
                //assume error, automatically set to 0
                averageSpeedData.add(0.0);
            } else {
                averageSpeedData.add(averageSpeed.doubleValue());
            }

            if (duration.getStandardMinutes() <= 0 || duration.getStandardMinutes() >= 100) {
                //assume error, automatically set to 0
                durationData.add(0.0);
            } else {
                durationData.add(Double.valueOf(duration.getStandardMinutes()));
            }

            fitnessDates.add(currentTime.toDate());
            currentTime = currentTime.plusDays(1);
        }

        //store or return averageSpeedData, durationData and dateData

    }

    private List<Workout> sort(List<Workout> workouts) {

        Collections.sort(workouts, new Comparator<Workout>() {
            @Override
            public int compare(Workout a, Workout b) {
                return a.getStartTime().compareTo(b.getStartTime());
            }
        });

        return workouts;
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
                fitnessDataReader(workouts);
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
