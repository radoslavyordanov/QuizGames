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

package radoslav.yordanov.quizgames.controller;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import radoslav.yordanov.quizgames.adapter.TopScoresViewPagerAdapter;
import radoslav.yordanov.quizgames.R;
import radoslav.yordanov.quizgames.view.SlidingTabLayout;

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
