package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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
}
