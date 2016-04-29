package radoslav.yordanov.quizgames.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;

import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.model.User;
import radoslav.yordanov.quizgames.view.InvalidCredentialsDialog;
import radoslav.yordanov.quizgames.view.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private CheckBox rememberMe;
    private SharedPreferences appPreferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        int userId = appPreferences.getInt(QuizGamesApplication.USER_ID_PREF, -1);
        if (userId != -1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onLoginClick(View view) {
        new LoginTask().execute(username.getText().toString(), password.getText().toString());
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private boolean invalidCredentials;

        @Override
        protected Boolean doInBackground(String... params) {
            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();
            User userAuth = new User();
            userAuth.setUsername(params[0]);
            userAuth.setPassword(params[1]);
            Call<User> postLoginCall = quizService.postLogin(userAuth);
            try {
                Response<User> getLoginResponse = postLoginCall.execute();
                if (getLoginResponse.isSuccessful()) {
                    invalidCredentials = getLoginResponse.body().getStatus().equals("failure");
                    user = getLoginResponse.body();
                    return true;
                }
            } catch (IOException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (invalidCredentials)
                    new InvalidCredentialsDialog().show(getFragmentManager(), "invalidCredentialsDialog");
                else {
                    if (rememberMe.isChecked()) {
                        appPreferences.edit().putInt(QuizGamesApplication.USER_ID_PREF, user.getId()).apply();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else
                new NetworkDialog().show(getFragmentManager(), "networkDialog");

        }
    }

}
