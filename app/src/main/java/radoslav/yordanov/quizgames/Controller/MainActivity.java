package radoslav.yordanov.quizgames.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import radoslav.yordanov.quizgames.QuizGamesApplication;
import radoslav.yordanov.quizgames.R;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getResources().getString(R.string.logoutAsk));
                dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        appPreferences.edit().putInt(QuizGamesApplication.USER_ID_PREF, -1).apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton(getResources().getString(R.string.no), null);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Intent intent = new Intent(this, TopScoresActivity.class);
        startActivity(intent);
    }

    public void onAboutClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.activity_main, new AboutFragment());
        ft.addToBackStack("quiz");
        ft.commit();
    }

    public void onCarsQuizClick(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_quizType, "cars");
        startActivity(intent);
        // overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void onLogosQuizClick(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_quizType, "logos");
        startActivity(intent);
    }

    public void onCitiesQuizClick(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_quizType, "cities");
        startActivity(intent);
    }


}


