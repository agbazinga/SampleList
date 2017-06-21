package com.example.bazinga.samplelist.ui.activity;

import android.animation.Animator;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.ui.fragment.FragmentOne;

/**
 * Created by Bazinga on 5/27/2017.
 */

public class FabAnimationDemoActivity extends AppCompatActivity implements FragmentOne.OnFragmentInteractionListener, View.OnClickListener {

    private static int NUM_PAGES = 3;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;

    private FloatingActionButton mFab;

    private TabLayout mTabLayout;

    private LinearLayout mTabItem;
    private TextView mTabTitle;
    private TextView mTabBadge[] = new TextView[NUM_PAGES];

    private static final long FAB_FADE_DURATION = 400L;

    private String title[] = new String[]{"One", "Two", "Three"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_animation_demo);
        ActionBar mActionBar;
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setElevation(0f);
        mFab = (FloatingActionButton) findViewById(R.id.fab_email);
        mFab.setOnClickListener(this);
        mTabLayout = (TabLayout) findViewById(R.id.fab_animation_demo_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.fab_animation_demo_pager);
        mAdapter = new FabFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("fabdemo", "onPageSelected");
                updateFab(position);
                hideBadge(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupTabs();
    }

    private void setupTabs() {
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < NUM_PAGES; i++) {
            mTabItem = (LinearLayout) LayoutInflater.from(FabAnimationDemoActivity.this)
                    .inflate(R.layout.tab_layout_custom_item, null);
            mTabTitle = (TextView) mTabItem.findViewById(R.id.tab_title);
            mTabTitle.setText(title[i]);
            mTabLayout.getTabAt(i).setCustomView(mTabItem);
        }
        resetBadge();
    }

    private void resetBadge() {
        for (int i = 0; i < NUM_PAGES; i++) {
            mTabBadge[i] = (TextView) mTabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_badge);
            mTabBadge[i].setScaleX(1.0f);
            mTabBadge[i].setScaleY(1.0f);
            mTabBadge[i].setVisibility(View.VISIBLE);
            int badgeCount = (int) Math.pow(8, i + 1);
            mTabBadge[i].setText(Integer.toString(badgeCount));
        }

    }

    private void hideBadge(final int position) {
        mTabBadge[position].clearAnimation();
        mTabBadge[position].animate()
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setDuration(400L)
                .setStartDelay(500L).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTabBadge[position].setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_email:
                resetBadge();
                break;
        }
    }


    private class FabFragmentPagerAdapter extends FragmentPagerAdapter {

        public FabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentOne.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }


    private void updateFab(int position) {
        if (position % 2 == 0) {
            showFab();
        } else {
            hideFab();
        }
    }

    private void showFab() {
        mFab.setVisibility(View.VISIBLE);
        mFab.clearAnimation();
        mFab.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(FAB_FADE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void hideFab() {
        mFab.clearAnimation();
        mFab.animate()
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setDuration(FAB_FADE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

}
