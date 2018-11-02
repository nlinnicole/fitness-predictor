package com.example.nicole.fitness_predictor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.LoginException;

/**
 * A login screen that offers login to Endomondo via email/password.
 *
 * Based on Android Studio's Login Activity template
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationLoginTask authTask = null;

    // TODO: This should be global
    private EndomondoSession session;

    private LoginController loginController;

    // UI references.
    private EditText emailView;
    private EditText passwordView;
    private TextView errorView;
    private View progressView;
    private View loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = LoginController.getInstance(this);

        // Set up the login form.
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        errorView = (TextView) findViewById(R.id.login_error);
        hideLoginError();

        Button emailSigninButton = (Button) findViewById(R.id.email_sign_in_button);
        emailSigninButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }

    private void hideLoginError() {
        errorView.setVisibility(View.GONE);
        errorView.setText("");
        errorView.setError(null);
    }

    private void showLoginError() {
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(R.string.error_sign_in_failed);
        errorView.setError("");
    }

    private void attemptLogin() {
        if (authTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);
            authTask = new AuthenticationLoginTask(email, password);
            authTask.execute((Void) null);
            showProgress(true);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void authenticationLoginSuccess() {
        // TODO: Implement logic on success
        hideLoginError();

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AuthenticationLoginTask extends AsyncTask<Void, Void, EndomondoSession> {

        private final String email;
        private final String password;

        AuthenticationLoginTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected EndomondoSession doInBackground(Void... params) {
            EndomondoSession session;

            try {
                Log.d("FITPREDLOG", "Trying to log in with email '" + email + "'");
                session = loginController.attemptLogin(email, password);
            } catch (LoginException exception) {
                Log.d("FITPREDLOG", "Error: " + exception);
                return null;
            }

            return session;
        }

        @Override
        protected void onPostExecute(final EndomondoSession session) {
            authTask = null;
            showProgress(false);

            if (session != null) {
                Log.d("FITPREDLOG","Successfully logged in to Endomondo with email '" + email + "'");
                authenticationLoginSuccess();
            } else {
                showLoginError();
            }
        }


        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }
}

