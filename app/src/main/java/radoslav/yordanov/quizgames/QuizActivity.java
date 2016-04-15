package radoslav.yordanov.quizgames;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import radoslav.yordanov.quizgames.Model.Quiz;
import retrofit2.Call;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_quizType = "quizType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        new QuizTask().execute(getIntent().getStringExtra(EXTRA_quizType));
    }

    private class QuizTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {


            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();


            Call<List<Quiz>> getQuizCall = quizService.getQuiz(params[0]);
            try {
                Response<List<Quiz>> getQuizResponse = getQuizCall.execute();

            } catch (IOException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                //success
            } else {
                // false show dialog
            }
        }
    }

}
