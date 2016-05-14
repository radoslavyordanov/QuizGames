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

package radoslav.yordanov.quizgames.adapter;

import android.content.res.Resources;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import radoslav.yordanov.quizgames.controller.TopScoresFragment;
import radoslav.yordanov.quizgames.R;

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
