package radoslav.yordanov.quizgames.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import radoslav.yordanov.quizgames.Model.QuizChoice;
import radoslav.yordanov.quizgames.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizChoiceFragment extends Fragment {
    private static final String QUIZ_CHOICES = "QUIZ_CHOICES";
    private static final String QUIZ_IMAGE = "QUIZ_IMAGE";

    private ArrayList<QuizChoice> quizChoices;
    private String quizImage;

    public QuizChoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quizChoices Quiz choices.
     * @return A new instance of fragment QuizChoiceFragment.
     */
    public static QuizChoiceFragment newInstance(ArrayList<QuizChoice> quizChoices, String quizImage) {
        QuizChoiceFragment fragment = new QuizChoiceFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(QUIZ_CHOICES, quizChoices);
        args.putString(QUIZ_IMAGE, quizImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizImage = getArguments().getString(QUIZ_IMAGE);
            quizChoices = getArguments().getParcelableArrayList(QUIZ_CHOICES);
            Collections.shuffle(quizChoices);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz_choice, container, false);

        ImageView questionImg = (ImageView) rootView.findViewById(R.id.questionImg);
        int id = getResources().getIdentifier(quizImage, "drawable", getActivity().getPackageName());
        questionImg.setImageResource(id);

        Button selection1 = (Button) rootView.findViewById(R.id.selection1);
        Button selection2 = (Button) rootView.findViewById(R.id.selection2);
        Button selection3 = (Button) rootView.findViewById(R.id.selection3);
        Button selection4 = (Button) rootView.findViewById(R.id.selection4);
        Button[] selections = {selection1, selection2, selection3, selection4};
        for (int i = 0; i < 4; i++) {
            selections[i].setText(quizChoices.get(i).getChoice());
            selections[i].setTag(quizChoices.get(i).getIsRightChoice());
        }
        return rootView;
    }

}
