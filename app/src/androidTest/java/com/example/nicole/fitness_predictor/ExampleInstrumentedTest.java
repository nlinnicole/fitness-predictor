package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.moomeen.endo2java.model.Workout;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<Workout> workouts = new ArrayList<>();

        Workout w1 = new Workout();
        w1.setDuration(10L);
        w1.setStartTime("2018-10-14 14:30:34 UTC");
        w1.setDistance(10L);

        Workout w2 = new Workout();
        w2.setDuration(15L);
        w2.setStartTime("2018-10-17 17:45:12 UTC");
        w2.setDistance(12L);

        workouts.add(w1);
        workouts.add(w2);

        int size = workouts.size();
        ArrayList<Double> averageSpeedData = new ArrayList<>(size);
        ArrayList<Double> durationData = new ArrayList<>(size);
        ArrayList<Date> xAxisData = new ArrayList<>(size);

        fragment.fillDates(workouts, averageSpeedData, durationData, xAxisData);

        ArrayList<Double> expectedDurationData = new ArrayList<Double>(4);

        expectedDurationData.add(Double.valueOf(workouts.get(0).getDuration().getStandardMinutes()));
        expectedDurationData.add(0d);
        expectedDurationData.add(0d);
        expectedDurationData.add(Double.valueOf(workouts.get(1).getDuration().getStandardMinutes()));

        assertEquals(durationData.size(), expectedDurationData.size());
        for(int i = 0; i < expectedDurationData.size(); i++) {
            assertEquals(durationData.get(i), expectedDurationData.get(i));
        }
    }
}
