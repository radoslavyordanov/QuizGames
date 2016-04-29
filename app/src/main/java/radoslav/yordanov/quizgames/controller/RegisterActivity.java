package radoslav.yordanov.quizgames.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        passwordConfirm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSubmitClick(null);
                    return true;
                }
                return false;
            }
        });
        submit = (Button) findViewById(R.id.submit);
    }

    public void onSubmitClick(View view) {
        String tempUsername = username.getText().toString().replaceAll("\\s", "");
        if (tempUsername.isEmpty() || tempUsername.equals("")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.nicknameEmpty));
            dialog.setPositiveButton("Okay", null);
            dialog.show();
        } else if (password.getText().toString().isEmpty() || password.getText().toString().equals("")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.passwordEmpty));
            dialog.setPositiveButton("Okay", null);
            dialog.show();
        } else if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.passwordDifferent));
            dialog.setPositiveButton("Okay", null);
            dialog.show();
        } else {
            submit.setEnabled(false);
            new RegisterTask().execute(username.getText().toString(), password.getText().toString());
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        private boolean usernameExists;

        @Override
        protected Boolean doInBackground(String... params) {
            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();
            User userAuth = new User();
            userAuth.setUsername(params[0]);
            userAuth.setPassword(params[1]);
            Call<User> postRegister = quizService.postRegister(userAuth);
            try {
                Response<User> getRegisterResponse = postRegister.execute();
                if (getRegisterResponse.isSuccessful()) {
                    usernameExists = getRegisterResponse.body().getStatus().equals("failure");
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
            submit.setEnabled(true);
            if (result) {
                if (usernameExists) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setMessage(getResources().getString(R.string.usernameExists));
                    dialog.setPositiveButton("Okay", null);
                    dialog.show();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else
                new NetworkDialog().show(getFragmentManager(), "networkDialog");

        }
    }

}
