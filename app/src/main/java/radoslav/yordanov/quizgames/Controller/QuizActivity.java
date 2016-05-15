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

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import radoslav.yordanov.quizgames.adapter.QuizAdapter;
import radoslav.yordanov.quizgames.model.Quiz;
import radoslav.yordanov.quizgames.model.QuizAPI;
import radoslav.yordanov.quizgames.model.QuizChoice;
import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.view.NetworkDialog;
import radoslav.yordanov.quizgames.view.NonSwipeViewPager;
import retrofit2.Call;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_quizType = "quizType";
    public static final String EXTRA_timed = "timed";
    private String quizType;
    private int timedQuiz;
    private static final int MAX_TIME = 20;
    private static final int MAX_POINTS = 100;
    private static final int MAX_QUESTIONS = 20;
    private int score = 0;
    private int correctAnswers = 0;
    private int elapsedTime = 0;
    private Resources res;
    private NonSwipeViewPager mViewPager;
    private ArrayList<Quiz> quizList;
    private int previousQuizId = -1;
    private TextView stopWatchTV;
    private TextView scoreTV;
    private TextView questionPositionTV;
    private ProgressBar spinner;
    private RepeatingThread repeatingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        res = getResources();
        mViewPager = (NonSwipeViewPager) findViewById(R.id.quizPager);
        quizType = getIntent().getStringExtra(EXTRA_quizType);
        timedQuiz = getIntent().getIntExtra(EXTRA_timed, 1);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        new QuizTask().execute(quizType);
        stopWatchTV = (TextView) findViewById(R.id.stopWatchTV);
        String timeLeftText = String.format(res.getString(R.string.timeLeft), 0);
        stopWatchTV.setText(timeLeftText);
        if (timedQuiz == 0)
            stopWatchTV.setVisibility(View.INVISIBLE);
        scoreTV = (TextView) findViewById(R.id.scoreTV);
        String scoreText = String.format(res.getString(R.string.score), score);
        scoreTV.setText(scoreText);
        questionPositionTV = (TextView) findViewById(R.id.questionPositionTV);
        String questionPos = String.format(res.getString(R.string.questionPosition), 1, MAX_QUESTIONS);
        questionPositionTV.setText(questionPos);

        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

    }

    public void onSelectionClick(View view) {
        if ((int) view.getTag() == 1) {
            score += (MAX_POINTS / MAX_TIME) * (MAX_TIME - elapsedTime);
            correctAnswers++;
            String scoreText = String.format(res.getString(R.string.score), score);
            scoreTV.setText(scoreText);
        }
        if (mViewPager.getCurrentItem() == MAX_QUESTIONS - 1) {
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_score, score);
            intent.putExtra(ScoreActivity.EXTRA_quizType, quizType);
            intent.putExtra(ScoreActivity.EXTRA_correctAnswers, correctAnswers);
            TaskStackBuilder sBuilder = TaskStackBuilder.create(this);
            sBuilder.addParentStack(ScoreActivity.class);
            sBuilder.addNextIntent(intent);
            sBuilder.startActivities();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            String questionPos = String.format(res.getString(R.string.questionPosition), mViewPager.getCurrentItem() + 1, MAX_QUESTIONS);
            questionPositionTV.setText(questionPos);
            elapsedTime = 0;
        }

    }

    private class QuizTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

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
                    return true;
                }
            } catch (IOException | IllegalStateException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            spinner.setVisibility(View.GONE);
            if (result) {
                //success
                Collections.shuffle(quizList);
                mViewPager.setAdapter(new QuizAdapter(getSupportFragmentManager(), quizList));
                repeatingThread = new RepeatingThread();
                Thread t = new Thread(repeatingThread);
                t.start();
            } else {
                new NetworkDialog().show(getFragmentManager(), "networkDialog");
            }
        }
    }

    private class RepeatingThread implements Runnable {

        private Handler mHandler = new Handler();

        public RepeatingThread() {
        }

        public void stopHandler() {
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (elapsedTime == MAX_TIME) {
                if (mViewPager.getCurrentItem() == MAX_QUESTIONS - 1) {
                    Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                    intent.putExtra(ScoreActivity.EXTRA_score, score);
                    intent.putExtra(ScoreActivity.EXTRA_quizType, quizType);
                    intent.putExtra(ScoreActivity.EXTRA_correctAnswers, correctAnswers);
                    TaskStackBuilder sBuilder = TaskStackBuilder.create(QuizActivity.this);
                    sBuilder.addParentStack(ScoreActivity.class);
                    sBuilder.addNextIntent(intent);
                    sBuilder.startActivities();
                } else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    String questionPos = String.format(res.getString(R.string.questionPosition), mViewPager.getCurrentItem() + 1, MAX_QUESTIONS);
                    questionPositionTV.setText(questionPos);
                    elapsedTime = 0;
                }

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String timeLeftText = String.format(res.getString(R.string.timeLeft), MAX_TIME - elapsedTime);
                    stopWatchTV.setText(timeLeftText);

                    if (timedQuiz == 1)
                        elapsedTime += 1;
                }
            });

            mHandler.postDelayed(this, 1000);
        }
    }

    public void onSingleAnswerClick(String correctAnswer, String userInput) {
        if (correctAnswer.toLowerCase().equals(userInput.toLowerCase())) {
            score += 150;
            correctAnswers++;
            String scoreText = String.format(res.getString(R.string.score), score);
            scoreTV.setText(scoreText);
        }
        if (mViewPager.getCurrentItem() == MAX_QUESTIONS - 1) {
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_score, score);
            intent.putExtra(ScoreActivity.EXTRA_quizType, quizType);
            intent.putExtra(ScoreActivity.EXTRA_correctAnswers, correctAnswers);
            TaskStackBuilder sBuilder = TaskStackBuilder.create(this);
            sBuilder.addParentStack(ScoreActivity.class);
            sBuilder.addNextIntent(intent);
            sBuilder.startActivities();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            String questionPos = String.format(res.getString(R.string.questionPosition), mViewPager.getCurrentItem() + 1, MAX_QUESTIONS);
            questionPositionTV.setText(questionPos);
            elapsedTime = 0;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repeatingThread != null)
            repeatingThread.stopHandler();
    }
}