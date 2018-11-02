package com.example.nicole.fitness_predictor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.moomeen.endo2java.EndomondoSession;

public class MainActivity extends AppCompatActivity {

    // TODO: This should be accessible globally
    private EndomondoSession session;

    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginController = LoginController.getInstance(this);
        KeystoreLoginTask task = new KeystoreLoginTask();
        task.execute((Void) null);
    }

    private void keystoreLoginSuccess() {
        // TODO: Implement logic on success
        Log.d("FITPREDLOG","Successfully logged in to Endomondo with keystore.");
    }

    private void keystoreLoginFailed() {
        Log.d("FITPREDLOG", "Failed to log in to Endomondo with keystore. Displaying login activity.");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private class KeystoreLoginTask extends AsyncTask<Void, Void, EndomondoSession> {
        KeystoreLoginTask() { }

        @Override
        protected EndomondoSession doInBackground(Void... params) {
            return loginController.attemptLoginWithKeystore();
        }

        @Override
        protected void onPostExecute(final EndomondoSession session) {
            if (session != null) {
                keystoreLoginSuccess();
            } else {
                keystoreLoginFailed();
            }
        }

        @Override
        protected void onCancelled() {
            keystoreLoginFailed();
        }
    }

}