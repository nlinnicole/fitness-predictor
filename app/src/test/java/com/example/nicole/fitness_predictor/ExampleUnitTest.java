package com.example.nicole.fitness_predictor;

import com.moomeen.endo2java.model.Workout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
<<<<<<< HEAD
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

=======

    @Test
    public void testSortAndFilter() {
        FitnessFragment fragment = new FitnessFragment();
        List<Workout> workouts = new ArrayList<>();

        Workout workout1 = new Workout();
        workout1.setDuration(10L);
        workout1.setStartTime("2018-10-10 00:00:00 UTC");

        Workout workout2 = new Workout();
        workout2.setDuration(10L);
        workout2.setStartTime("2018-10-09 00:00:00 UTC");

        Workout workout3 = new Workout();
        workout3.setDuration(10L);
        workout3.setStartTime("2018-10-09 00:00:01 UTC");

        workouts.add(workout1);
        workouts.add(workout2);
        workouts.add(workout3);

        List<Workout> result = fragment.sortAndFilter(workouts);
        /* Workout 3 should have been removed because it is the same day as workout2 */
        assertEquals(workouts.size() - 1, result.size());
        assertEquals(workout2, result.get(0));
        assertFalse(result.contains(workout3));
    }

    @Test
    public void testGetMovingAverage(){
        FitnessFragment fragment = new FitnessFragment();
        ArrayList<Double> result = new ArrayList<Double>();
        ArrayList<Double> expectedResult = new ArrayList<Double>();

        ArrayList<Double> list = new ArrayList<Double>();
        list.add(15.3);
        list.add(20.5);
        list.add(23.6);
        list.add(24.3);
        list.add(25.3);
        list.add(14.8);
        list.add(20.3);
        list.add(13.3);

        // Test with a subset size of 0. This should simply yield a copy of the original list
        int size1 = 0;
        expectedResult = new ArrayList<Double>(list);

        result = fragment.getMovingAverage(list, size1);
        assertArrayEquals(expectedResult.toArray(), result.toArray());

        // Test with a subset size of 3. This should yield a list of 6 "partial" averages.
        int size2 = 3;
        expectedResult = new ArrayList<Double>();
        expectedResult.add(0.0);
        expectedResult.add(0.0);
        expectedResult.add(0.0);
        expectedResult.add(19.8);
        expectedResult.add(22.8);
        expectedResult.add(24.4);
        expectedResult.add(21.47);
        expectedResult.add(20.13);
        expectedResult.add(16.13);

        result = fragment.getMovingAverage(list, size2);
        assertArrayEquals(expectedResult.toArray(), result.toArray());

        // Test with a subset size equal to the size of the list.
        // This should simply yield a copy of the original list
        int size3 = list.size();
        expectedResult = new ArrayList<Double>(list);

        result = fragment.getMovingAverage(list, size3);
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }
>>>>>>> 0e23f9c7e8466200d158bda182aa5e679804c08f
}