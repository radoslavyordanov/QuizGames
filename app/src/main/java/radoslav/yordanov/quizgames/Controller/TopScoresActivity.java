package radoslav.yordanov.quizgames.Controller;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import radoslav.yordanov.quizgames.Adapter.TopScoresViewPagerAdapter;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.View.SlidingTabLayout;

public class TopScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topscores);

        ViewPager mPager = (ViewPager) findViewById(R.id.topScoresPager);
        TopScoresViewPagerAdapter mPagerAdapter = new TopScoresViewPagerAdapter(getSupportFragmentManager(), getResources());
        if (mPager != null) {
            mPager.setAdapter(mPagerAdapter);
        }


        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setDistributeEvenly(true);
            mSlidingTabLayout.setViewPager(mPager);
        }

    }
}
