package radoslav.yordanov.quizgames;

import java.util.List;

import radoslav.yordanov.quizgames.Model.Quiz;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by radoslav on 4/15/16.
 */
public interface QuizGamesAPI {

    @Headers({
            "content-type: application/json"
    })
    @GET("/quiz/{quizType}")
    Call<List<Quiz>> getQuiz(@Path("quizType") String quizType);
}
