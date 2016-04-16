package radoslav.yordanov.quizgames;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import radoslav.yordanov.quizgames.Adapter.QuizAdapter;
import radoslav.yordanov.quizgames.Model.Quiz;
import radoslav.yordanov.quizgames.Model.QuizAPI;
import radoslav.yordanov.quizgames.Model.QuizChoice;
import retrofit2.Call;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_quizType = "quizType";
    private ViewPager mViewPager;
    private ArrayList<Quiz> quizList;
    private int previousQuizId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mViewPager = (ViewPager) findViewById(R.id.quizPager);
        new QuizTask().execute(getIntent().getStringExtra(EXTRA_quizType));
    }

    private class QuizTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {


            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();


            Call<List<QuizAPI>> getQuizCall = quizService.getQuiz(params[0]);
            try {
                Response<List<QuizAPI>> getQuizResponse = getQuizCall.execute();
                if (getQuizResponse.isSuccessful()) {
                    List<QuizAPI> responseList = getQuizResponse.body();
                    quizList = new ArrayList<>();
                    Quiz quizModel = null;
                    for (int i = 0; i < responseList.size(); i++) {

                        if (responseList.get(i).getQuiz_id() == previousQuizId) {
                            QuizChoice quizChoiceModel = new QuizChoice();
                            quizChoiceModel.setChoice(responseList.get(i).getChoice());
                            quizChoiceModel.setChoiceId(responseList.get(i).getChoice_id());
                            quizChoiceModel.setIsRightChoice(responseList.get(i).getIs_right_choice());

                            if (quizModel != null)
                                quizModel.addQuizChoice(quizChoiceModel);
                        } else {
                            if (quizModel != null)
                                quizList.add(quizModel);
                            previousQuizId = responseList.get(i).getQuiz_id();
                            quizModel = new Quiz();
                            quizModel.setQuizId(responseList.get(i).getQuiz_id());
                            quizModel.setQuizImage(responseList.get(i).getQuiz_image());
                            quizModel.setQuizType(responseList.get(i).getQuiz_type());

                            QuizChoice quizChoiceModel = new QuizChoice();
                            quizChoiceModel.setChoice(responseList.get(i).getChoice());
                            quizChoiceModel.setChoiceId(responseList.get(i).getChoice_id());
                            quizChoiceModel.setIsRightChoice(responseList.get(i).getIs_right_choice());

                            quizModel.addQuizChoice(quizChoiceModel);

                        }

                    }
                    // Add the last item
                    quizList.add(quizModel);
                }
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
                mViewPager.setAdapter(new QuizAdapter(getSupportFragmentManager(), quizList));
            } else {
                // false show dialog
            }
        }
    }

}
