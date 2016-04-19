package radoslav.yordanov.quizgames.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import radoslav.yordanov.quizgames.Controller.QuizChoiceFragment;
import radoslav.yordanov.quizgames.Model.Quiz;

public class QuizAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Quiz> quizList;

    public QuizAdapter(FragmentManager fm, ArrayList<Quiz> quizList) {
        super(fm);
        this.quizList = quizList;
    }

    @Override
    public Fragment getItem(int position) {
        return QuizChoiceFragment.newInstance(quizList.get(position).getQuizChoices(), quizList.get(position).getQuizImage());
    }

    @Override
    public int getCount() {
        return quizList.size();
    }
}
