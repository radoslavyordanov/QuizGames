package radoslav.yordanov.quizgames;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import radoslav.yordanov.quizgames.Adapter.QuizAdapter;
import radoslav.yordanov.quizgames.Model.Quiz;
import radoslav.yordanov.quizgames.Model.QuizAPI;
import radoslav.yordanov.quizgames.Model.QuizChoice;
import radoslav.yordanov.quizgames.Util.Stopwatch;
import radoslav.yordanov.quizgames.View.NonSwipeViewPager;
import retrofit2.Call;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_quizType = "quizType";
    private NonSwipeViewPager mViewPager;
    private ArrayList<Quiz> quizList;
    private int previousQuizId = -1;
    private Stopwatch stopWatch;
    private TextView stopWatchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mViewPager = (NonSwipeViewPager) findViewById(R.id.quizPager);
        new QuizTask().execute(getIntent().getStringExtra(EXTRA_quizType));
        stopWatchView = (TextView) findViewById(R.id.stopWatchView);

     /*   Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (stopWatchView != null) {
                            if (stopWatch.getElapsedTimeSecs() == 20) {
                                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                                stopWatch = new Stopwatch();
                                stopWatch.start();
                            }
                            stopWatchView.setText(String.valueOf(20 - stopWatch.getElapsedTimeSecs()));
                        }
                    }
                });

            }
        }, 0, 1000);*/


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
                final Thread t = new Thread(new RepeatingThread());
                t.start();
            } else {
                // false show dialog
            }
        }
    }

    public class RepeatingThread implements Runnable {

        private final Handler mHandler = new Handler();

        public RepeatingThread() {

        }

        @Override
        public void run() {
            if (stopWatch == null) {
                stopWatch = new Stopwatch();
                stopWatch.start();
            }
            if (stopWatch.getElapsedTimeSecs() == 20) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                stopWatch = new Stopwatch();
                stopWatch.start();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String timeLeftText = String.format(getResources().getString(R.string.timeLeft), 20 - stopWatch.getElapsedTimeSecs());
                    stopWatchView.setText(timeLeftText);
                }
            });

            mHandler.postDelayed(this, 1000);
        }
    }

}
