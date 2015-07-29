// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        UpcomingEventListFragment.OnFragmentInteractionListener,
        PastEventList.OnFragmentInteractionListener,
        UpcomingEventDetailsFragment.OnFragmentInteractionListener,
        ContactListFragment.OnFragmentInteractionListener,
        PastEventDetailsFragment.OnFragmentInteractionListener,
        UpcomingEventsFragment.OnFragmentInteractionListener{

    private static final int NEW_EVENT = 1;
    public static final int DISCOVERABLE = 3;
    private static final int SWIPES = 5;
    private static final String DIALOG_CHECK_IN = "CheckInDialog";
    private static final String PAST_EVENT_DETAILS = "PastEventDetails";
    private static final String DIALOG_CONTACT_DETAILS = "DialogContactDetails";
    public static final int UPCOMING_EVENT_TAB_INDEX = 0;
    public static final int PAST_EVENT_TAB_INDEX = 1;
    public static final int CONTACT_LIST_TAB = 2;

    @Bind(R.id.main_pager)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(new IconDrawable(this, Iconify.IconValue.fa_users).actionBarSize().colorRes(R.color.offWhite));
        mTabLayout.getTabAt(1).setIcon(new IconDrawable(this, Iconify.IconValue.fa_link).actionBarSize().colorRes(R.color.offWhite));
        mTabLayout.getTabAt(2).setIcon(new IconDrawable(this, Iconify.IconValue.fa_user).actionBarSize().colorRes(R.color.offWhite));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_EVENT){
            getUpcomingEventsTab().refreshList();
        } else  if (requestCode == SWIPES) {
            FragmentManager manager = getSupportFragmentManager();
            PastEventDetailsFragment fragment = (PastEventDetailsFragment) manager.findFragmentByTag(PAST_EVENT_DETAILS);
            fragment.refresh();
        } else if (requestCode == DISCOVERABLE){
            if (resultCode != Activity.RESULT_CANCELED){
                Intent intent = new Intent(this, BluetoothPingService.class);
                startService(intent);
            } else {
                Log.e("BluetoothTest", "Chose No Discoverability");
            }
        }
    }

    @Override
    public void openUpcomingEventDetails(Event event) {
        FragmentManager manager = getSupportFragmentManager();
        UpcomingEventDetailsFragment cf = UpcomingEventDetailsFragment.newInstance(event);
        cf.show(manager, DIALOG_CHECK_IN);
    }

    @Override
    public void openPastEventDetails(Attendance attendance) {
        FragmentManager manager = getSupportFragmentManager();
        PastEventDetailsFragment detailsFragment = PastEventDetailsFragment.newInstance(attendance);
        detailsFragment.show(manager, PAST_EVENT_DETAILS);
    }

    @Override
    public void openContactDetails(ParseUser user) {
        FragmentManager manager = getSupportFragmentManager();
        ContactDetailsFragment contactDetailsFragment = ContactDetailsFragment.newInstance(user);
        contactDetailsFragment.show(manager, DIALOG_CONTACT_DETAILS);
    }

    @Override
    public void openSwipes(ArrayList<CardSwipeActivity.ProfileDisplayInstance> PDIs) {
        Intent intent = CardSwipeActivity.newIntent(this, PDIs);
        startActivityForResult(intent, SWIPES);
    }

    private UpcomingEventsFragment getUpcomingEventsTab() {
        FragmentManager manager = this.getSupportFragmentManager();
        String tag = makeFragmentName(UPCOMING_EVENT_TAB_INDEX);
        return (UpcomingEventsFragment) manager.findFragmentByTag(tag);
    }

    private PastEventList getPastEventsTab() {
        FragmentManager manager = this.getSupportFragmentManager();
        String tag = makeFragmentName(PAST_EVENT_TAB_INDEX);
        return (PastEventList) manager.findFragmentByTag(tag);
    }

    private static String makeFragmentName(int index) {
        return "android:switcher:" + R.id.main_pager + ":" + index;
    }

    @Override
    public void checkInToEvent(String eventID) {
        getUpcomingEventsTab().startCheckIn();
        Event event = ParseObject.createWithoutData(Event.class, eventID);
        event.checkIn(new Event.CheckInCallback() {
            @Override
            public void checkedIn(Attendance attendance) {
                startPinging(attendance);
                getPastEventsTab().refreshList();
                getUpcomingEventsTab().confirmCheckIn(attendance);
            }
        });
    }

    @Override
    public void onCheckOut() {
        Intent intent = new Intent(this, BluetoothPingService.class);
        stopService(intent);
    }

    private void startPinging(Attendance attendance) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                presentDiscoverableDialog();
            } else {
                Intent intent = BluetoothPingService.newIntent(this,attendance);
                startService(intent);
            }
        } else {
            Log.e("BluetoothTest", "No Bluetooth Capability");
        }
    }

    private void presentDiscoverableDialog() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivityForResult(discoverableIntent, DISCOVERABLE);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case UPCOMING_EVENT_TAB_INDEX:
                    return new UpcomingEventsFragment();
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
        switch (item.getItemId()) {
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
                return false;
            case R.id.action_my_profile:
                FragmentManager manager = getSupportFragmentManager();
                MeFragment meFragment = MeFragment.newInstance();
                meFragment.show(manager, DIALOG_CONTACT_DETAILS);
                return false;
            case R.id.action_search:
                return true;

            case R.id.action_add:
                Intent intent = new Intent(this, NewEventActivity.class);
                startActivityForResult(intent, NEW_EVENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
