/*
 *  Copyright 2016 Radoslav Yordanov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package radoslav.yordanov.quizgames;

import java.util.List;

import radoslav.yordanov.quizgames.model.Quiz;
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
    Call<Void> postResult(@Path("quizType") String quizType, @Body Result result);

    @Headers({
            "content-type: application/json"
    })
    @GET("/results/{resultType}")
    Call<List<Result>> getResults(@Path("resultType") String resultType);

    @POST("/login")
    Call<User> postLogin(@Body User user);

    @POST("/register")
    Call<User> postRegister(@Body User user);

    @POST("/addQuiz")
    Call<Quiz> addQuiz(@Body Quiz quiz);
}
