package radoslav.yordanov.quizgames.Controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import radoslav.yordanov.quizgames.Adapter.TopScoresAdapter;
import radoslav.yordanov.quizgames.Model.Result;
import radoslav.yordanov.quizgames.QuizGamesAPI;
import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.View.NetworkDialog;
import retrofit2.Call;
import retrofit2.Response;

public class TopScoresFragment extends Fragment {
    public static final String EXTRA_resultsType = "resultsType";
    private ArrayList<Result> resultsList = new ArrayList<>();
    private ListView listView;
    private ProgressBar spinner;

    public TopScoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resultsType Results type.
     * @return A new instance of fragment QuizChoiceFragment.
     */
    public static TopScoresFragment newInstance(String resultsType) {
        TopScoresFragment fragment = new TopScoresFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_resultsType, resultsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_topscores, container, false);
        listView = (ListView) rootView.findViewById(R.id.topScoresList);
        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        new ResultTask().execute(getArguments().getString(EXTRA_resultsType));
        return rootView;
    }

    private class ResultTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {


            QuizGamesAPI quizService = QuizGamesApplication.getQuizGamesService();


            Call<List<Result>> getResultCall = quizService.getResults(params[0]);
            try {
                Response<List<Result>> getResultResponse = getResultCall.execute();
                if (getResultResponse.isSuccessful()) {
                    List<Result> responseList = getResultResponse.body();
                    for (int i = 0; i < responseList.size(); i++) {
                        Result resultModel = new Result();
                        resultModel.setName(responseList.get(i).getName());
                        resultModel.setScore(responseList.get(i).getScore());
                        String prettyTimeString = new PrettyTime().format(parseDateTime(responseList.get(i).getDate()));
                        resultModel.setDate(prettyTimeString);
                        resultModel.setType(responseList.get(i).getType());
                        resultsList.add(resultModel);
                    }

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
                listView.setAdapter(new TopScoresAdapter(getActivity(), R.layout.topscores_row, resultsList));
            } else {
                new NetworkDialog().show(getActivity().getFragmentManager(), "networkDialog");
            }
        }

    }

    public static Date parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        if (dateString.contains("T")) dateString = dateString.replace('T', ' ');
        if (dateString.contains("Z")) dateString = dateString.replace("Z", "+0000");
        else
            dateString = dateString.substring(0, dateString.lastIndexOf(':')) + dateString.substring(dateString.lastIndexOf(':') + 1);
        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            //Log.e(Const.TAG, "Could not parse datetime: " + dateString);
            return null;
        }
    }

}
