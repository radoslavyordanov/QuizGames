package radoslav.yordanov.quizgames.Controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopScoresFragment extends Fragment {
    private ArrayList<Result> resultsList = new ArrayList<>();
    private ListView listView;

    public TopScoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_topscores, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        new ResultTask().execute("cars");
        return rootView;
    }

    private class ResultTask extends AsyncTask<String, Void, Boolean> {
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
                        resultModel.setDate(parseDateTime(responseList.get(i).getDate()));
                        resultModel.setType(responseList.get(i).getType());
                        resultsList.add(resultModel);
                    }

                    return true;
                }
            } catch (IOException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                listView.setAdapter(new TopScoresAdapter(getActivity(), R.layout.topscores_row, resultsList));
            } else {
                // false show dialog
            }
        }

    }

    public static String parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        if (dateString.contains("T")) dateString = dateString.replace('T', ' ');
        if (dateString.contains("Z")) dateString = dateString.replace("Z", "+0000");
        else
            dateString = dateString.substring(0, dateString.lastIndexOf(':')) + dateString.substring(dateString.lastIndexOf(':') + 1);
        try {
            return fmt.parse(dateString).toString();
        } catch (ParseException e) {
            //Log.e(Const.TAG, "Could not parse datetime: " + dateString);
            return null;
        }
    }

}
