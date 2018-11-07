package com.example.nicole.fitness_predictor;

import android.app.Application;
import android.support.annotation.NonNull;

import com.moomeen.endo2java.EndomondoSession;

/**
 * This class has some global states such as EndomondoSession that can be used across the whole
 * application.
 *
 * Make sure to start the Login Activity whenever the application is restarted. (LoginActivity should
 * be the first activity displayed.
 */
public class FitnessApplication extends Application {
    private EndomondoSession session;


    public void setEndomondoSession(@NonNull EndomondoSession session) {
        this.session = session;
    }

    public EndomondoSession getEndomondoSession() {
        return session;
    }
}
