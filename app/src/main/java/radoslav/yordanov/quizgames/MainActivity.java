package radoslav.yordanov.quizgames;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import radoslav.yordanov.quizgames.Controller.MainFragment;
import radoslav.yordanov.quizgames.Controller.QuizSelectionFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_main, new MainFragment());
        ft.commit();
    }

    public void onPlayClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.activity_main, new QuizSelectionFragment());
        ft.addToBackStack("quiz");
        ft.commit();
    }

    public void onTopScoresClick(View view) {
    }

    public void onCarQuizClick(View view) {
    }

    public void onLogosQuizClick(View view) {
    }

    public void onCitiesQuizClick(View view) {
    }

}
