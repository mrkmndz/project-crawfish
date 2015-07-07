// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

public class NewEventActivity extends AppCompatActivity {

    public static final String KEY_ID = "ID";
    EventEditorFragment mFragment;
    String mEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragment = fm.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            if (savedInstanceState != null) {
                mEventID = savedInstanceState.getString(KEY_ID);
            } else {
                Event event = new Event();
                Date now = new Date();
                event.setStartDate(now);

                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.HOUR, Event.STANDARD_DURATION_HOURS);
                event.setEndDate(cal.getTime());

                event.setIsAllDay(false);
                try {
                    event.pin();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mEventID = event.getObjectId();
            }
            mFragment = EventEditorFragment.newInstance(mEventID);
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_save) {
            final Event event = Event.getLocalEvent(mEventID);
            if (event != null) {
                event.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        try {
                            event.unpin();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ID, mEventID);
    }
}
