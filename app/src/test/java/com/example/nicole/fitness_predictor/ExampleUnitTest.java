package com.example.nicole.fitness_predictor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
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