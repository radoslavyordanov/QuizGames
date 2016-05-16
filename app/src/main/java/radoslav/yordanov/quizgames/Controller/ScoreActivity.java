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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import radoslav.yordanov.quizgames.model.Result;
import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.view.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {

    public static final String EXTRA_score = "score";
    public static final String EXTRA_quizType = "quizType";
    public static final String EXTRA_correctAnswers = "correctAnswers";
    public static final String EXTRA_timed = "timed";
    private int score;
    private String quizType;
    private int timedQuiz;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_activity);
        TextView scoreResult = (TextView) findViewById(R.id.scoreResult);
        score = getIntent().getIntExtra(EXTRA_score, 0);
        quizType = getIntent().getStringExtra(EXTRA_quizType);
        if (scoreResult != null) {
            String scoreText = String.format(getResources().getString(R.string.scoreResult), score);
            scoreResult.setText(scoreText);
        }
        TextView answeredQuestions = (TextView) findViewById(R.id.answeredQuestions);
        int correctAnswers = getIntent().getIntExtra(EXTRA_correctAnswers, 0);
        if (answeredQuestions != null) {
            String ansQuestionsText = String.format(getResources().getString(R.string.answeredQuestions), correctAnswers);
            answeredQuestions.setText(ansQuestionsText);
        }

        Button submit = (Button) findViewById(R.id.submitScore);
        timedQuiz = getIntent().getIntExtra(EXTRA_timed, 1);
        if (submit != null) {
            if (timedQuiz == 0)
                submit.setVisibility(View.INVISIBLE);
            else submit.setVisibility(View.VISIBLE);
        }

    }

    public void onSubmitScoreClick(View view) {
        progressDialog = ProgressDialog.show(ScoreActivity.this, "",
                getResources().getString(R.string.loading), true);
        new ResultTask().execute("");
    }

    private class ResultTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {


            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();

            Result resultModel = new Result();
            resultModel.setUser_id(QuizGamesApplication.userId);
            resultModel.setScore(score);
            Call<Void> postResultCall = quizService.postResult(quizType, resultModel);
            try {
                Response<Void> getResultResponse = postResultCall.execute();
                if (getResultResponse.isSuccessful()) {
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
            progressDialog.dismiss();
            if (result) {
                onBackPressed();
                Toast.makeText(ScoreActivity.this, getResources().getString(R.string.scoreSubmitted), Toast.LENGTH_LONG).show();
            } else
                new NetworkDialog().show(getFragmentManager(), "networkDialog");
        }
    }

}


