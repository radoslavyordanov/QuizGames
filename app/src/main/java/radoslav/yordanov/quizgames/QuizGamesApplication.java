package radoslav.yordanov.quizgames;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by radoslav on 4/15/16.
 */
public class QuizGamesApplication extends Application {
    private static QuizGamesAPI quizGamesService;
    private static final String endpoint = "http://46.40.127.6:3000";

    public static QuizGamesAPI getQuizGamesService() {

        if (quizGamesService == null) {

            OkHttpClient client = new OkHttpClient();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

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
