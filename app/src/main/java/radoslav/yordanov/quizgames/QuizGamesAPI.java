package radoslav.yordanov.quizgames;

import java.util.List;

import radoslav.yordanov.quizgames.model.QuizAPI;
import radoslav.yordanov.quizgames.model.Result;
import radoslav.yordanov.quizgames.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<Void>  postResult(@Path("quizType") String quizType, @Body Result result);

    @Headers({
            "content-type: application/json"
    })
    @GET("/results/{resultType}")
    Call<List<Result>> getResults(@Path("resultType") String resultType);

    @POST("/login")
    Call<User> postLogin(@Body User user);
}
