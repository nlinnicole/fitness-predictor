package com.example.nicole.fitness_predictor;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FitnessApplication application = (FitnessApplication)getApplicationContext();
        EndomondoSession session = application.getEndomondoSession();
        EndomondoQueryTask task = new EndomondoQueryTask(session);
        task.execute((Void)null);
    }

    private class EndomondoQueryTask extends AsyncTask<Void, Void, Boolean> {
        private EndomondoSession session;

        EndomondoQueryTask(@NonNull EndomondoSession session) {
            this.session = session;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("FITPREDLOG", "Fetching Endomondo information");
            try {
                Log.d("FITPREDLOG", "Found " + session.getWorkouts().size() + " workouts!");
                return true;
            } catch (InvocationException exception) {
                Log.d("FITPREDLOG", "Error getting workouts " + exception);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            return;
        }

        @Override
        protected void onCancelled() {
        }
    }
}