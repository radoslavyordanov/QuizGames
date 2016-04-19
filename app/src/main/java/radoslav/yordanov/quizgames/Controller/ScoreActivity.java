package radoslav.yordanov.quizgames.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import radoslav.yordanov.quizgames.Model.Result;
import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.View.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {

    public static final String EXTRA_score = "score";
    public static final String EXTRA_quizType = "quizType";
    public static final String EXTRA_correctAnswers = "correctAnswers";
    private int score;
    private String quizType;
    private float scale;
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
        scale = getResources().getDisplayMetrics().density;
    }

    public void onSubmitScoreClick(View view) {
        // Dialog to enter nickname
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(ScoreActivity.this);
        // Dialog to show error if nickname is empty
        final AlertDialog.Builder errorDialog = new AlertDialog.Builder(ScoreActivity.this);

        editDialogBuilder.setTitle(getResources().getString(R.string.enterNickname));
        final EditText input = new EditText(ScoreActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout layout = new LinearLayout(ScoreActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int) scale * 5, 0, (int) scale * 5, 0);
        layout.addView(input);
        editDialogBuilder.setView(layout);
        editDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        String nickname = input.getText().toString();
                        String tempNick = nickname.replaceAll("\\s", "");
                        if (tempNick.isEmpty() || tempNick.equals("")) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                            errorDialog.show();
                        } else {
                            progressDialog = ProgressDialog.show(ScoreActivity.this, "",
                                    getResources().getString(R.string.loading), true);
                            new ResultTask().execute(nickname);
                        }
                    }
                }
        );
        editDialogBuilder.setNegativeButton(getString(R.string.cancel), null);

        // Create the dialog to enter nickname
        final AlertDialog editDialog = editDialogBuilder.create();
        editDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        editDialog.show();

        errorDialog.setMessage(getResources().getString(R.string.nicknameEmpty));
        errorDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editDialog.show();
            }
        });
    }

    private class ResultTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {


            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();

            Result resultModel = new Result();
            resultModel.setName(params[0]);
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

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if (result) {
                        onBackPressed();
                        Toast.makeText(ScoreActivity.this, getResources().getString(R.string.scoreSubmitted), Toast.LENGTH_LONG).show();
                    } else {
                        new NetworkDialog().show(getFragmentManager(), "networkDialog");
                    }
                }
            }, 1000);
        }

    }

}
