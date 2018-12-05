package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.moomeen.endo2java.model.Workout;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 0e23f9c7e8466200d158bda182aa5e679804c08f

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /**
     *  TODO: Should probably be in configuration file / environment variable AND from a dummy account
     */
    public static final String TEST_ENDOMONDO_USERNAME = "comp354_2018@yahoo.com";
    public static final String TEST_ENDOMONDO_PASSWORD = "Concordia";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.nicole.fitness_predictor", appContext.getPackageName());
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
=======
    @Test
    public void testAttemptLogin() {
        Context context = InstrumentationRegistry.getTargetContext();
        LoginController controller = LoginController.getInstance(context);

        /* Test empty username */
        controller.saveCredentials("", "Password");
        assertNull(controller.attemptLoginWithKeystore());

        /* Test null username */
        controller.saveCredentials(null, "Password");
        assertNull(controller.attemptLoginWithKeystore());

        /* Test empty password */
        controller.saveCredentials("Username", "");
        assertNull(controller.attemptLoginWithKeystore());

        /* Test null password */
        controller.saveCredentials("Username", null);
        assertNull(controller.attemptLoginWithKeystore());

        /* Test wrong credential */
        controller.saveCredentials("Username", "Password");
        assertNull(controller.attemptLoginWithKeystore());

        /* Test valid credential */
        controller.saveCredentials(TEST_ENDOMONDO_USERNAME, TEST_ENDOMONDO_PASSWORD);
        assertNotNull(controller.attemptLoginWithKeystore());
    }

    @Test
    public void testFitnessFragmentFillDates()
    {
        FitnessFragment fragment = new FitnessFragment();

        /* Test fillDates method with no workout in the workouts list. Nothing should happen.*/
        List<Workout> workouts = new ArrayList<>();
        int size = workouts.size();
        ArrayList<Double> averageSpeedData = new ArrayList<>(size);
        ArrayList<Double> durationData = new ArrayList<>(size);
        ArrayList<Date> xAxisData = new ArrayList<>(size);

        fragment.fillDates(workouts, averageSpeedData, durationData, xAxisData);
        assertEquals(size, 0);
        assertEquals(averageSpeedData.size(), 0);
        assertEquals(durationData.size(), 0);
        assertEquals(xAxisData.size(), 0);

        /* Test 2 workouts with a 2 days gap between them.*/
        // The 2 days without a workout should still be added to the corresponding array lists,
        // with default data.
        Workout w1 = new Workout();
        w1.setDuration(10L);
        w1.setStartTime("2018-10-14 14:30:34 UTC");
        w1.setSpeedAvg(10d);

        Workout w2 = new Workout();
        w2.setDuration(15L);
        w2.setStartTime("2018-10-17 17:45:12 UTC");
        w2.setSpeedAvg(15d);

        workouts.add(w1);
        workouts.add(w2);

        size = workouts.size();

        fragment.fillDates(workouts, averageSpeedData, durationData, xAxisData);

        ArrayList<Double> expectedDurationData = new ArrayList<Double>(4);
        expectedDurationData.add(Double.valueOf(workouts.get(0).getDuration().getStandardMinutes()));
        expectedDurationData.add(0d);
        expectedDurationData.add(0d);
        expectedDurationData.add(Double.valueOf(workouts.get(1).getDuration().getStandardMinutes()));

        ArrayList<Double> expectedAvgSpdData = new ArrayList<Double>(4);
        expectedAvgSpdData.add(10d);
        expectedAvgSpdData.add(0d);
        expectedAvgSpdData.add(0d);
        expectedAvgSpdData.add(15d);

        assertEquals(durationData.size(), expectedDurationData.size());
        assertEquals(averageSpeedData.size(), expectedAvgSpdData.size());
        for(int i = 0; i < expectedDurationData.size(); i++) {
            assertEquals(durationData.get(i), expectedDurationData.get(i));
            assertEquals(averageSpeedData.get(i), expectedAvgSpdData.get(i));
        }
        // Check that the value on the x axis is the proper one for the recorded workout days.
        assertEquals(xAxisData.get(0), workouts.get(0).getStartTime().toDate());

        /* Test workouts with out of bounds data.*/
        workouts = new ArrayList<>();

        w1 = new Workout();
        w1.setDuration(10L);
        w1.setStartTime("2018-10-14 14:30:34 UTC");
        w1.setSpeedAvg(fragment.MAX_AVG_SPEED + 1d);

        w2 = new Workout();
        w2.setDuration(15L);
        w2.setStartTime("2018-10-15 17:45:12 UTC");
        w2.setSpeedAvg(fragment.MIN_AVG_SPEED - 1d);

        workouts.add(w1);
        workouts.add(w2);

        size = workouts.size();
        averageSpeedData = new ArrayList<>(size);
        durationData = new ArrayList<>(size);
        xAxisData = new ArrayList<>(size);

        fragment.fillDates(workouts, averageSpeedData, durationData, xAxisData);

        expectedDurationData = new ArrayList<Double>(2);
        expectedDurationData.add(Double.valueOf(workouts.get(0).getDuration().getStandardMinutes()));
        expectedDurationData.add(Double.valueOf(workouts.get(1).getDuration().getStandardMinutes()));

        expectedAvgSpdData = new ArrayList<Double>(2);
        expectedAvgSpdData.add(0d);
        expectedAvgSpdData.add(0d);

        assertEquals(durationData.size(), expectedDurationData.size());
        assertEquals(averageSpeedData.size(), expectedAvgSpdData.size());
        for(int i = 0; i < expectedDurationData.size(); i++) {
            assertEquals(durationData.get(i), expectedDurationData.get(i));
            assertEquals(averageSpeedData.get(i), expectedAvgSpdData.get(i));
        }
        // Check that the value on the x axis is the proper for the recorded workout days.
        assertEquals(xAxisData.get(0), workouts.get(0).getStartTime().toDate());
>>>>>>> 0e23f9c7e8466200d158bda182aa5e679804c08f
    }
}
