package radoslav.yordanov.quizgames;

import java.util.List;

import radoslav.yordanov.quizgames.Model.Quiz;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by radoslav on 4/15/16.
 */
public interface QuizGamesAPI {

    @Headers({
            "content-type: application/json"
    })
    @GET("/quiz/cars")
    Call<List<Quiz>> getCarsQuiz();
}
