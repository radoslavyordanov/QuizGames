package radoslav.yordanov.quizgames.Adapter;

import android.content.res.Resources;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import radoslav.yordanov.quizgames.Controller.TopScoresFragment;
import radoslav.yordanov.quizgames.R;

/**
 * Created by radoslav on 4/16/16.
 */
public class TopScoresViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] navMenuTitles;

    public TopScoresViewPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        navMenuTitles = res.getStringArray(R.array.topScoresTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopScoresFragment.newInstance("cars");
            case 1:
                return TopScoresFragment.newInstance("logos");
            case 2:
                return TopScoresFragment.newInstance("cities");
            case 3:
                return TopScoresFragment.newInstance("all");
            default:
                return TopScoresFragment.newInstance("cars");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return navMenuTitles[position];
    }
}
