package radoslav.yordanov.quizgames.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private int score;
    private String quizType;
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


