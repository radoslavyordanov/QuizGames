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

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizGamesApplication extends Application {
    private static QuizGamesAPI quizGamesService;
    //private static final String endpoint = "http://quizgames-ryordanov.rhcloud.com";
    private static final String endpoint = "http://quizgames-ryordanov.rhcloud.com";

    public static final String USER_ID_PREF = "USER_ID";
    public static final String USER_ROLE_ID = "USER_ROLE_ID";
    public static int userId;
    public static int userRoleId;

    public static QuizGamesAPI getQuizGamesService() {

        if (quizGamesService == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit builder = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            quizGamesService = builder.create(QuizGamesAPI.class);
        }

        return quizGamesService;
    }
}