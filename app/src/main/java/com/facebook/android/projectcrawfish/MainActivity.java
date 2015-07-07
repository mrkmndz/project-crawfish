// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        UpcomingEventListFragment.OnFragmentInteractionListener,
        UpcomingEventsFragment.OnFragmentInteractionListener,
        PastEventList.OnFragmentInteractionListener{

    public static final int CHECK_IN = 2;
    public static final String DIALOG_CHECK_IN = "CheckInDialog";
    @Bind(R.id.main_pager) ViewPager mViewPager;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;

    SectionsPagerAdapter mSectionsPagerAdapter;

    private UpcomingEventsFragment mEventListFrag;
    private PastEventList mPastEventFrag;
    private MeFragment mMeFragment;

    public static final int NEW_EVENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout.setTabsFromPagerAdapter(new SectionsPagerAdapter(this.getSupportFragmentManager()));
        mTabLayout.getTabAt(0).setIcon(new IconDrawable(this, Iconify.IconValue.fa_users).actionBarSize());
        mTabLayout.getTabAt(1).setIcon(new IconDrawable(this, Iconify.IconValue.fa_link).actionBarSize());
        mTabLayout.getTabAt(2).setIcon(new IconDrawable(this, Iconify.IconValue.fa_user).actionBarSize());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Nothing
            }
        });

    }

    @Override
    public void createNewEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivityForResult(intent, NEW_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==NEW_EVENT&& resultCode== Activity.RESULT_OK){
            mEventListFrag.refreshList();
        }
    }

    @Override
    public void checkIntoEvent(Event event) {
        FragmentManager manager = getSupportFragmentManager();
        EventEditorFragment cf = EventEditorFragment.newInstance(event.getObjectId());
        cf.setTargetFragment(mEventListFrag, CHECK_IN);
        cf.show(manager, DIALOG_CHECK_IN);
    }

    @Override
    public void openEventDetails(Event event) {
        //Something
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mEventListFrag==null) {
                        mEventListFrag = new UpcomingEventsFragment();
                    }
                    return mEventListFrag;
                case 1:
                    if (mPastEventFrag ==null) {
                        mPastEventFrag = new PastEventList();
                    }
                    return mPastEventFrag;
                case 2:
                    if (mMeFragment==null) {
                        mMeFragment = new MeFragment();
                    }
                    return mMeFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
