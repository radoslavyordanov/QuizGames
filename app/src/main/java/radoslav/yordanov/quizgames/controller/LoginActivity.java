/*
 *  Copyright 2016 Radoslav Yordanov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package radoslav.yordanov.quizgames.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.model.User;
import radoslav.yordanov.quizgames.view.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private CheckBox rememberMe;
    private Button login;
    private SharedPreferences appPreferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginClick(null);
                    return true;
                }
                return false;
            }
        });
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        login = (Button) findViewById(R.id.login);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        int userId = appPreferences.getInt(QuizGamesApplication.USER_ID_PREF, -1);
        if (userId != -1) {
            QuizGamesApplication.userId = userId;
            QuizGamesApplication.userRoleId = appPreferences.getInt(QuizGamesApplication.USER_ROLE_ID, -1);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onLoginClick(View view) {
        login.setEnabled(false);
        new LoginTask().execute(username.getText().toString(), password.getText().toString());
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
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
            login.setEnabled(true);
            if (result) {
                if (invalidCredentials) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setMessage(getResources().getString(R.string.invalidCredentials));
                    dialog.setPositiveButton("Okay", null);
                    dialog.show();
                } else {
                    if (rememberMe.isChecked()) {
                        appPreferences.edit().putInt(QuizGamesApplication.USER_ID_PREF, user.getId()).apply();
                        appPreferences.edit().putInt(QuizGamesApplication.USER_ROLE_ID, user.getUser_roleId()).apply();
                    }
                    QuizGamesApplication.userId = user.getId();
                    QuizGamesApplication.userRoleId = user.getUser_roleId();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else
                new NetworkDialog().show(getFragmentManager(), "networkDialog");

        }
    }

}
