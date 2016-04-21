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
    private static final String endpoint = "http://quizgames-ryordanov.rhcloud.com";

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