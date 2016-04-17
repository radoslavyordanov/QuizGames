package radoslav.yordanov.quizgames;

import java.util.List;

import radoslav.yordanov.quizgames.Model.QuizAPI;
import radoslav.yordanov.quizgames.Model.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by radoslav on 4/15/16.
 */
public interface QuizGamesAPI {

    @Headers({
            "content-type: application/json"
    })
    @GET("/quiz/{quizType}")
    Call<List<QuizAPI>> getQuiz(@Path("quizType") String quizType);

    @Headers({
            "content-type: application/json"
    })
    @POST("/results/{quizType}")
    Call<Void> postResult(@Path("quizType") String quizType, @Body Result result);
}
