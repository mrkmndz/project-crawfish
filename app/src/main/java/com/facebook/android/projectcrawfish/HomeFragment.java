// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/31/15.
 */
public class HomeFragment extends Fragment {

    public static final int PAST_EVENT_TAB_INDEX = 0;
    public static final int CONTACT_LIST_TAB = 1;
    public static final String STARTING_TAB = "STARTING_TAB";

    @Bind(R.id.main_pager)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    public static HomeFragment newInstance(int startingTab) {

        Bundle args = new Bundle();
        args.putInt(STARTING_TAB,startingTab);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, v);

        mToolbar.setTitle("bïnder");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mViewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));

        int startingTab = getArguments().getInt(STARTING_TAB,-1);
        if (startingTab != -1){
            mViewPager.setCurrentItem(startingTab);
            getArguments().putInt(STARTING_TAB,-1);
        }

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_calendar).actionBarSize().colorRes(R.color.offWhite));
        mTabLayout.getTabAt(1).setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_users).actionBarSize().colorRes(R.color.offWhite));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        return v;
    }

    public PastEventList getPastEventsTab() {
        FragmentManager manager = getChildFragmentManager();
        String tag = makeFragmentName(PAST_EVENT_TAB_INDEX);
        return (PastEventList) manager.findFragmentByTag(tag);
    }

    private static String makeFragmentName(int index) {
        return "android:switcher:" + R.id.main_pager + ":" + index;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case PAST_EVENT_TAB_INDEX:
                    return new PastEventList();
                case CONTACT_LIST_TAB:
                    return new ContactListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
