// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        UpcomingEventListFragment.OnFragmentInteractionListener,
        PastEventList.OnFragmentInteractionListener,
        UpcomingEventDetailsFragment.OnFragmentInteractionListener,
        ContactListFragment.OnFragmentInteractionListener,
        PastEventDetailsFragment.OnFragmentInteractionListener,
        UpcomingEventsFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        EventEditorFragment.OnFragmentInteractionListener,
        MyProfileTab.MeFragment.OnFragmentInteractionListener{

    private static final int NEW_EVENT = 1;
    public static final int DISCOVERABLE = 3;
    private static final int SWIPES = 5;
    private static final String DIALOG_CHECK_IN = "CheckInDialog";
    private static final String PAST_EVENT_DETAILS = "PastEventDetails";
    private static final String DIALOG_CONTACT_DETAILS = "DialogContactDetails";

    private int mSelectedFrag;
    private static final int FRAG_HOME = 0;
    private static final int FRAG_CHECK_IN = 1;
    private static final int FRAG_MY_EVENTS = 2;
    private static final int FRAG_MY_PROFILE = 3;
    private static final int TAB_CONTACTS = -1;

    @Bind(R.id.content)
    FrameLayout mFrameLayout;


    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        mNavigationView.setNavigationItemSelectedListener(this);

        String jsonData = getIntent().getStringExtra("com.parse.Data");
        if (jsonData != null) {
            try {
                getIntent().putExtra("com.parse.Data", (String) null);
                JSONObject jObject = new JSONObject(jsonData);
                String userID = jObject.getString("userID");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.getInBackground(userID, new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        mSelectedFrag = TAB_CONTACTS;
                        refreshView();
                        HomeFragment fragment = (HomeFragment) getDisplayedFragment();
                        fragment.mViewPager.setCurrentItem(HomeFragment.CONTACT_LIST_TAB);
                        openContactDetails(user);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (getDisplayedFragment() == null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mSelectedFrag = FRAG_HOME;
            refreshView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SWIPES) {
            FragmentManager manager = getSupportFragmentManager();
            PastEventDetailsFragment fragment = (PastEventDetailsFragment) manager.findFragmentByTag(PAST_EVENT_DETAILS);
            fragment.refresh();
        } else if (requestCode == DISCOVERABLE){
            if (resultCode == Activity.RESULT_CANCELED){
                Log.e("BluetoothTest", "Chose No Discoverability");
            }
        } else if (requestCode == MyProfileTab.MeFragment.SELECT_SINGLE_PICTURE && resultCode == Activity.RESULT_OK){
            if (mSelectedFrag == FRAG_MY_PROFILE){
                MyProfileTab fragment = (MyProfileTab) getDisplayedFragment();
                fragment.onChangedPicture(data.getData());
            }
        }
    }

    @Override
    public void onSaveNewEvent(){
        FragmentManager manager = getSupportFragmentManager();
        MyEventsFragment fragment = (MyEventsFragment) manager.findFragmentById(mFrameLayout.getId());
        fragment.refreshList();
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

    @Override
    public void checkInToEvent(String eventID) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(mFrameLayout.getId());
        try {
            final UpcomingEventsFragment newEventFragment = (UpcomingEventsFragment) fragment;
            newEventFragment.startCheckIn();
            Event event = ParseObject.createWithoutData(Event.class, eventID);
            event.checkIn(new Event.CheckInCallback() {
                @Override
                public void checkedIn(Attendance attendance) {
                    startPinging(attendance);
                    newEventFragment.confirmCheckIn(attendance);
                }
            });
        } catch (ClassCastException e){
            Log.e("MA","not a HomeFragment");
        }
    }

    @Override
    public void onCheckOut() {
        Intent intent = new Intent(this, BluetoothPingService.class);
        stopService(intent);
    }

    private void startPinging(Attendance attendance) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            Intent intent = BluetoothPingService.newIntent(this,attendance.getObjectId());
            startService(intent);
            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                presentDiscoverableDialog();
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

    public Fragment getDisplayedFragment(){
        return getSupportFragmentManager().findFragmentById(mFrameLayout.getId());
    }

    public void refreshView(){
        invalidateOptionsMenu();
        FragmentManager fm = getSupportFragmentManager();
        switch (mSelectedFrag){
            case FRAG_HOME:
                fm.beginTransaction()
                        .replace(mFrameLayout.getId(),
                                HomeFragment.newInstance(HomeFragment.PAST_EVENT_TAB_INDEX))
                        .commit();
                break;
            case TAB_CONTACTS:
                fm.beginTransaction()
                        .replace(mFrameLayout.getId(),
                                HomeFragment.newInstance(HomeFragment.CONTACT_LIST_TAB))
                        .commit();
                mSelectedFrag = FRAG_HOME;
                break;
            case FRAG_CHECK_IN:
                fm.beginTransaction()
                        .replace(mFrameLayout.getId(), new UpcomingEventsFragment())
                        .commit();
                break;
            case FRAG_MY_EVENTS:
                fm.beginTransaction()
                        .replace(mFrameLayout.getId(), new MyEventsFragment())
                        .commit();
                break;
            case FRAG_MY_PROFILE:
                fm.beginTransaction()
                        .replace(mFrameLayout.getId(), new MyProfileTab())
                        .commit();
                break;
        }
    }
    public void logOut(){
        ParseUser.logOut();
        // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
        // logs out on older devices, we'll just exit.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Intent i = new Intent(this,
                    MainDispatchActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                mSelectedFrag = FRAG_HOME;
                break;
            case R.id.nav_check_in:
                mSelectedFrag = FRAG_CHECK_IN;
                break;
            case R.id.nav_new:
                mSelectedFrag = FRAG_MY_EVENTS;
                break;
            case R.id.nav_me:
                mSelectedFrag = FRAG_MY_PROFILE;
                break;
            case R.id.nav_log_out:
                logOut();
                return true;
            default:
                return false;
        }
        refreshView();
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
        switch (mSelectedFrag){
            case FRAG_HOME:
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            case FRAG_MY_EVENTS:
                getMenuInflater().inflate(R.menu.menu_my_events, menu);
                return true;
            case FRAG_CHECK_IN:
                getMenuInflater().inflate(R.menu.menu_check_in, menu);
                return true;
            case FRAG_MY_PROFILE:
                getMenuInflater().inflate(R.menu.menu_check_in, menu);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_item_add:
                if (mSelectedFrag == FRAG_MY_EVENTS){
                    EventEditorFragment fragment = EventEditorFragment.newInstance(null);
                    fragment.show(getSupportFragmentManager(),"NEW_EVENT");
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType(MyProfileTab.MeFragment.IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), MyProfileTab.MeFragment.SELECT_SINGLE_PICTURE);
    }
}
