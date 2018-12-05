package com.example.nicole.fitness_predictor;

import com.moomeen.endo2java.model.Workout;

import org.junit.Test;

import java.util.ArrayList;
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
}