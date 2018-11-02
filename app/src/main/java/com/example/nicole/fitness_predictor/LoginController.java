package com.example.nicole.fitness_predictor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.LoginException;

/**
 * The login controller is a singleton (for now).
 *
 * The login controller saves the username and password encrypted in Android's Keystore to be able
 * to login without user interaction.
 */
public class LoginController {

    private SharedPreferences preferences;

    private static LoginController instance;
    private static final String ENDOMONDO_USERNAME = "ENDOMONDO_USERNAME";
    private static final String ENDOMONDO_PASSWORD = "ENDOMONDO_PASSWORD";

    private LoginController(final SharedPreferences sharedPreferences) {
        this.preferences = sharedPreferences;
    }

    // Returns null if could not find the credentials in the keystore

    public EndomondoSession attemptLoginWithKeystore() {
        String encryptedUsername = preferences.getString(ENDOMONDO_USERNAME, "");
        String encryptedPassword = preferences.getString(ENDOMONDO_PASSWORD, "");

        // Not stored in the keystore
        if (encryptedUsername.isEmpty() || encryptedPassword.isEmpty()) {
            return null;
        }

        String username = decryptUsername(encryptedUsername);
        String password = decryptPassword(encryptedPassword);

        try {
            return attemptLogin(username, password);
        } catch (LoginException exception) {
            return  null;
        }
    }

    public EndomondoSession attemptLogin(String username, String password) throws LoginException {
        EndomondoSession session = new EndomondoSession(username, password);

        session.login();
        /**
         * Since login was success (No exception thrown), we store the username & password into the
         * shared preferences
         */
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ENDOMONDO_USERNAME, encryptUsername(username));
        editor.putString(ENDOMONDO_PASSWORD, encryptPassword(password));
        editor.apply();

        return session;
    }

    private String decryptUsername(String encryptedUsername) {
        String username;
        // TODO Implement
        username = encryptedUsername;

        return username;
    }

    private String decryptPassword(String encryptedPassword) {
        String password;
        // TODO Implement
        password = encryptedPassword;

        return password;
    }

    private String encryptUsername(String username) {
        String encryptedUsername;
        // TODO Implement
        encryptedUsername = username;

        return encryptedUsername;
    }

    private String encryptPassword(String password) {
        String encryptedPassword;
        // TODO Implement
        encryptedPassword = password;

        return encryptedPassword;
    }


    public static LoginController getInstance(Activity activity) {
        if (instance == null) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.shared_preferences),
                    Context.MODE_PRIVATE
            );

            instance = new LoginController(sharedPreferences);
        }

        return instance;
    }
}
