package radoslav.yordanov.quizgames.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import radoslav.yordanov.quizgames.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizTimeFragment extends Fragment {


    public QuizTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_time, container, false);
    }

}
