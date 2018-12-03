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

    @Test
    public void testFitnessFragmentFillDates()
    {
        FitnessFragment fragment = new FitnessFragment();
        List<Workout> workouts = new ArrayList<>();

        Workout w1 = new Workout();
        w1.setDuration(10L);
        w1.setStartTime("2018-10-14 14:30:34 UTC");

        Workout w2 = new Workout();
        w2.setDuration(15L);
        w2.setStartTime("2018-10-17 17:45:12 UTC");
        w2.setDistance(10L);

        workouts.add(w1);
        workouts.add(w2);

        int size = workouts.size();
        ArrayList<Double> averageSpeedData = new ArrayList<>(size);
        ArrayList<Double> durationData = new ArrayList<>(size);
        ArrayList<Date> xAxisData = new ArrayList<>(size);

        fragment.fillDates(workouts, averageSpeedData, durationData, xAxisData);

        ArrayList<Double> expectedDurationData = new ArrayList<Double>(4);

        expectedDurationData.add(Double.valueOf(workouts.get(0).getDuration().getStandardMinutes()));
        expectedDurationData.add(Double.valueOf(workouts.get(1).getDuration().getStandardMinutes()));

        for(int i = 0; i < expectedDurationData.size(); i++) {
            assertEquals(durationData.get(i), expectedDurationData.get(i));
        }
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
}