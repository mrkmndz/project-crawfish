// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        UpcomingEventListFragment.OnFragmentInteractionListener,
        UpcomingEventsFragment.OnFragmentInteractionListener,
        PastEventList.OnFragmentInteractionListener,
        UpcomingEventDetailsFragment.OnFragmentInteractionListener
{

    public static final int NEW_EVENT = 1;
    public static final int PAST_EVENTS = 3;
    public static final String DIALOG_CHECK_IN = "CheckInDialog";
    public static final String PAST_EVENT_DETAILS = "PastEventDetails";

    @Bind(R.id.main_pager)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(new IconDrawable(this, Iconify.IconValue.fa_users).actionBarSize());
        mTabLayout.getTabAt(1).setIcon(new IconDrawable(this, Iconify.IconValue.fa_link).actionBarSize());
        mTabLayout.getTabAt(2).setIcon(new IconDrawable(this, Iconify.IconValue.fa_user).actionBarSize());

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public void createNewEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivityForResult(intent, NEW_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_EVENT && resultCode == Activity.RESULT_OK) {
            getUpcomingEventsTab().refreshList();
        }
    }

    @Override
    public void checkIntoEvent(Event event) {
        FragmentManager manager = getSupportFragmentManager();
        UpcomingEventDetailsFragment cf = UpcomingEventDetailsFragment.newInstance(event.getObjectId());
        cf.show(manager, DIALOG_CHECK_IN);
    }

    @Override
    public void openEventDetails(Event event) {
        FragmentManager manager = getSupportFragmentManager();
        PastEventDetailsFragment detailsFragment = PastEventDetailsFragment.newInstance(event.getObjectId());
        detailsFragment.show(manager, PAST_EVENT_DETAILS);
    }

    private UpcomingEventsFragment getUpcomingEventsTab() {
        FragmentManager manager = this.getSupportFragmentManager();
        String tag = makeFragmentName(R.id.main_pager, 0);
        return (UpcomingEventsFragment) manager.findFragmentByTag(tag);
    }

    private static String makeFragmentName(int viewPagerId, int index) {
        return "android:switcher:" + viewPagerId + ":" + index;
    }

    @Override
    public void confirmCheckIn(String eventID, String cachedTitle) {
        getUpcomingEventsTab().confirmCheckIn(eventID, cachedTitle);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UpcomingEventsFragment();
                case 1:
                    return new PastEventList();
                case 2:
                    return MeFragment.newInstance();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout:
                ParseUser.logOut();
                // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
                // logs out on older devices, we'll just exit.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Intent intent = new Intent(this,
                            MainDispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
