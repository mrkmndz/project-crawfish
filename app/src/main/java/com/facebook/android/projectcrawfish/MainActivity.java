// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements EventList.OnFragmentInteractionListener{
    public static final int NEW_EVENT = 1;
    SectionsPagerAdapter mSectionsPagerAdapter;
    EventList mEventList;

    ViewPager mViewPager;
    private EventList mEventListFrag;
    private ConnectionsFragment mConnectionsFragment;
    private MeFragment mMeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(new SectionsPagerAdapter(this.getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            ParseQueryAdapter<ParseObject> adapter = (ParseQueryAdapter<ParseObject>) mEventListFrag.mListView.getAdapter();
            adapter.loadObjects();
        }
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
                        mEventListFrag = new EventList();
                    }
                    return mEventListFrag;
                case 1:
                    if (mConnectionsFragment==null) {
                        mConnectionsFragment = new ConnectionsFragment();
                    }
                    return mConnectionsFragment;
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

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Curr";
                case 1:
                    return "Conn";
                case 2:
                    return "Me";
                default:
                    return null;
            }
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
