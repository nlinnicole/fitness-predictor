package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.nicole.fitness_predictor", appContext.getPackageName());
    }

    public  void testWindSpeedGraph() {
        Date currentDate = new Date(118, 9, 25);
        WeatherFragment weatherFragment = new WeatherFragment();
        // empty arraylist
        ArrayList<Date> emptyDates = new ArrayList<>();
        ArrayList<Double> emptyWindSpeeds = new ArrayList<>();

        // filled arraylist
        ArrayList<Date> filledDates = new ArrayList<>();
        ArrayList<Double> filledWindSpeeds = new ArrayList<>();
        filledDates.add(new Date(currentDate.getYear(), currentDate.getMonth(),currentDate.getDate()));
        filledWindSpeeds.add(23.2);

        // test the early return.
        weatherFragment.graphWindSpeed(emptyDates, emptyWindSpeeds);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test the early return.
        weatherFragment.graphWindSpeed(emptyDates, filledWindSpeeds);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test the early return.
        weatherFragment.graphWindSpeed(filledDates, emptyWindSpeeds);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test without the return.
        weatherFragment.graphWindSpeed(filledDates, filledWindSpeeds);
        assertEquals(1, weatherFragment.getChildFragmentManager().getFragments().size());

    }

    public  void testAvgTempGraph() {
        Date currentDate = new Date(118, 9, 25);
        WeatherFragment weatherFragment = new WeatherFragment();
        // empty arraylist
        ArrayList<Date> emptyDates = new ArrayList<>();
        ArrayList<Double> emptyAvgTemps = new ArrayList<>();

        // filled arraylist
        ArrayList<Date> filledDates = new ArrayList<>();
        ArrayList<Double> filledAvgTemps = new ArrayList<>();
        filledDates.add(new Date(currentDate.getYear(), currentDate.getMonth(),currentDate.getDate()));
        filledAvgTemps.add(12.0);

        // test the early return.
        weatherFragment.graphAvgTemp(emptyDates, emptyAvgTemps);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test the early return.
        weatherFragment.graphAvgTemp(emptyDates, filledAvgTemps);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test the early return.
        weatherFragment.graphAvgTemp(filledDates, emptyAvgTemps);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());

        // test without the return.
        weatherFragment.graphAvgTemp(filledDates, filledAvgTemps);
        assertEquals(0, weatherFragment.getChildFragmentManager().getFragments().size());
    }
}
