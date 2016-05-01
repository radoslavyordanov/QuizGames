package radoslav.yordanov.quizgames.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;

import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.model.Quiz;
import radoslav.yordanov.quizgames.model.QuizChoice;
import radoslav.yordanov.quizgames.view.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class AddQuizActivity extends AppCompatActivity {

    private Spinner quizType;
    private EditText quizImg;
    private CheckBox multipleChoices;
    private EditText singleAnswer;
    private EditText choice1;
    private EditText choice2;
    private EditText choice3;
    private EditText choice4;
    private Spinner correctChoice;
    private View singleAnswerContainer;
    private View multipleAnswerContainer;
    private Quiz quiz;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        quizType = (Spinner) findViewById(R.id.quizType);
        quizImg = (EditText) findViewById(R.id.quizImg);
        multipleChoices = (CheckBox) findViewById(R.id.multipleChoices);
        singleAnswer = (EditText) findViewById(R.id.singleAnswer);
        choice1 = (EditText) findViewById(R.id.choice1);
        choice2 = (EditText) findViewById(R.id.choice2);
        choice3 = (EditText) findViewById(R.id.choice3);
        choice4 = (EditText) findViewById(R.id.choice4);
        correctChoice = (Spinner) findViewById(R.id.correctChoice);
        String[] correctChoiceItems = getResources().getStringArray(R.array.correctChoices);
        ArrayAdapter<String> correctChoiceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, correctChoiceItems);
        if (correctChoice != null) {
            correctChoice.setAdapter(correctChoiceAdapter);
        }

        String[] items = getResources().getStringArray(R.array.quizTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        if (quizType != null) {
            quizType.setAdapter(adapter);
        }
        singleAnswerContainer = findViewById(R.id.singleAnswerContainer);
        multipleAnswerContainer = findViewById(R.id.multipleAnswerContainer);
    }

    public void onChangeQuestionTypeClick(View view) {
        if (((CheckBox) view).isChecked()) {
            multipleAnswerContainer.setVisibility(View.VISIBLE);
            singleAnswerContainer.setVisibility(View.GONE);
        } else {
            multipleAnswerContainer.setVisibility(View.GONE);
            singleAnswerContainer.setVisibility(View.VISIBLE);
        }
    }

    public void onAddClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddQuizActivity.this);
        dialog.setPositiveButton("Okay", null);

        String tempQuizImg = quizImg.getText().toString().replaceAll("\\s", "");
        if (tempQuizImg.isEmpty() || tempQuizImg.equals("")) {
            dialog.setMessage(getResources().getString(R.string.quizImgEmpty));
            dialog.show();
            return;
        }
        if (multipleChoices.isChecked()) {
            String tempChoice1 = choice1.getText().toString().replaceAll("\\s", "");
            String tempChoice2 = choice2.getText().toString().replaceAll("\\s", "");
            String tempChoice3 = choice3.getText().toString().replaceAll("\\s", "");
            String tempChoice4 = choice4.getText().toString().replaceAll("\\s", "");

            if (tempChoice1.isEmpty() || tempChoice1.equals("")) {
                dialog.setMessage(getResources().getString(R.string.quizChoiceEmpty));
                dialog.show();
                return;
            }
            if (tempChoice2.isEmpty() || tempChoice2.equals("")) {
                dialog.setMessage(getResources().getString(R.string.quizChoiceEmpty));
                dialog.show();
                return;
            }
            if (tempChoice3.isEmpty() || tempChoice3.equals("")) {
                dialog.setMessage(getResources().getString(R.string.quizChoiceEmpty));
                dialog.show();
                return;
            }
            if (tempChoice4.isEmpty() || tempChoice4.equals("")) {
                dialog.setMessage(getResources().getString(R.string.quizChoiceEmpty));
                dialog.show();
                return;
            }

            int correctChoiceId = Integer.parseInt(correctChoice.getSelectedItem().toString());
            QuizChoice quizChoice1 = new QuizChoice();
            quizChoice1.setChoice(choice1.getText().toString());
            quizChoice1.setIsRightChoice(correctChoiceId == 1 ? 1 : 0);

            QuizChoice quizChoice2 = new QuizChoice();
            quizChoice2.setChoice(choice2.getText().toString());
            quizChoice2.setIsRightChoice(correctChoiceId == 2 ? 1 : 0);

            QuizChoice quizChoice3 = new QuizChoice();
            quizChoice3.setChoice(choice3.getText().toString());
            quizChoice3.setIsRightChoice(correctChoiceId == 3 ? 1 : 0);

            QuizChoice quizChoice4 = new QuizChoice();
            quizChoice4.setChoice(choice4.getText().toString());
            quizChoice4.setIsRightChoice(correctChoiceId == 4 ? 1 : 0);

            quiz = new Quiz();
            quiz.setQuizImage(quizImg.getText().toString());
            quiz.setQuizType(quizType.getSelectedItem().toString().toLowerCase());
            quiz.addQuizChoice(quizChoice1);
            quiz.addQuizChoice(quizChoice2);
            quiz.addQuizChoice(quizChoice3);
            quiz.addQuizChoice(quizChoice4);
            new AddQuizTask().execute("");
        } else {
            String tempSingleAnswer = singleAnswer.getText().toString().replaceAll("\\s", "");

            if (tempSingleAnswer.isEmpty() || tempSingleAnswer.equals("")) {
                dialog.setMessage(getResources().getString(R.string.quizAnswerEmpty));
                dialog.show();
                return;
            }
            quiz = new Quiz();
            quiz.setQuizImage(quizImg.getText().toString());
            quiz.setQuizType(quizType.getSelectedItem().toString().toLowerCase());
            QuizChoice quizChoice1 = new QuizChoice();
            quizChoice1.setChoice(singleAnswer.getText().toString());
            quizChoice1.setIsRightChoice(1);
            quiz.addQuizChoice(quizChoice1);
            new AddQuizTask().execute("");
        }
    }

    private class AddQuizTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AddQuizActivity.this, "",
                    getResources().getString(R.string.loading), true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();
            Call<Quiz> addQuizCall = quizService.addQuiz(quiz);
            try {
                Response<Quiz> getAddQuizResponse = addQuizCall.execute();
                if (getAddQuizResponse.isSuccessful()) {
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
            if (result)
                onBackPressed();
            else
                new NetworkDialog().show(getFragmentManager(), "networkDialog");
        }
    }

}
