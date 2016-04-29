package radoslav.yordanov.quizgames.controller;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.Collections;

import radoslav.yordanov.quizgames.model.QuizChoice;
import radoslav.yordanov.quizgames.R;

public class QuizChoiceFragment extends Fragment {
    private static final String QUIZ_CHOICES = "QUIZ_CHOICES";
    private static final String QUIZ_IMAGE = "QUIZ_IMAGE";

    private ArrayList<QuizChoice> quizChoices;
    private String quizImage;
    private ImageLoader imageLoader;
    private DisplayImageOptions defaultOptions;

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
            if (quizChoices != null)
                Collections.shuffle(quizChoices);
        }
        // Default options
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz_choice, container, false);

        ImageView questionImg = (ImageView) rootView.findViewById(R.id.questionImg);
        imageLoader.displayImage(quizImage, questionImg, defaultOptions);

        Button selection1 = (Button) rootView.findViewById(R.id.selection1);
        Button selection2 = (Button) rootView.findViewById(R.id.selection2);
        Button selection3 = (Button) rootView.findViewById(R.id.selection3);
        Button selection4 = (Button) rootView.findViewById(R.id.selection4);
        Button[] selections = {selection1, selection2, selection3, selection4};
        final EditText answer = (EditText) rootView.findViewById(R.id.answer);
        Button singleAnswer = (Button) rootView.findViewById(R.id.singleAnswer);

        if (quizChoices.size() == 4) {
            for (int i = 0; i < 4; i++) {
                selections[i].setText(quizChoices.get(i).getChoice());
                selections[i].setTag(quizChoices.get(i).getIsRightChoice());
            }
        } else {
            selection1.setVisibility(View.GONE);
            selection2.setVisibility(View.GONE);
            selection3.setVisibility(View.GONE);
            selection4.setVisibility(View.GONE);
            answer.setVisibility(View.VISIBLE);
            singleAnswer.setVisibility(View.VISIBLE);
            singleAnswer.setTag(quizChoices.get(0).getChoice());
            singleAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((QuizActivity) getActivity()).onSingleAnswerClick(v, answer.getText().toString());
                }
            });
        }

        return rootView;
    }

}
