// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CardSwipeActivity extends AppCompatActivity {

    public static final int BASE_SIZE = 40;
    public static final int SIZE_SLOPE = 50;
    public static final String PDIS = "PDIS";
    private CardAdapter mCardAdapter;
    private int mOutstandingPins;
    private boolean mIsEmpty;
    private ArrayList<ProfileDisplayInstance> mPDIs;

    @Bind(R.id.xButton)
    Button mXButton;
    @Bind(R.id.checkButton)
    Button mCheckButton;

    @Bind(R.id.frame)
    SwipeFlingAdapterView flingContainer;


    public static Intent newIntent(Context context, ArrayList<CardSwipeActivity.ProfileDisplayInstance> PDIs) {
        Intent intent = new Intent(context, CardSwipeActivity.class);
        intent.putExtra(PDIS, PDIs);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_swipe);
        ButterKnife.bind(this);

        mCheckButton.setTextSize(BASE_SIZE);
        mXButton.setTextSize(BASE_SIZE);

        mPDIs = (ArrayList<ProfileDisplayInstance>) getIntent().getSerializableExtra(PDIS);

        mCardAdapter = new CardAdapter(this, mPDIs);

        mIsEmpty = false;
        mOutstandingPins=0;

        flingContainer.setAdapter(mCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (!mPDIs.isEmpty()) {
                    mPDIs.remove(0);
                    mCardAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                mCheckButton.setTextSize(BASE_SIZE);
                mXButton.setTextSize(BASE_SIZE);
                ProfileDisplayInstance pdi = (ProfileDisplayInstance) dataObject;
                    ParseUser user = ParseObject.createWithoutData(ParseUser.class, pdi.getUserID());
                    Event event = ParseObject.createWithoutData(Event.class, pdi.getEventID());
                mOutstandingPins++;
                    Swipe.sendSwipe(true, user, event);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                mCheckButton.setTextSize(BASE_SIZE);
                mXButton.setTextSize(BASE_SIZE);
                ProfileDisplayInstance pdi = (ProfileDisplayInstance) dataObject;
                    ParseUser user = ParseObject.createWithoutData(ParseUser.class, pdi.getUserID());
                    Event event = ParseObject.createWithoutData(Event.class, pdi.getEventID());
                mOutstandingPins++;
                    Swipe.sendSwipe(false, user, event);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {
                    finish();
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                    mCheckButton.setTextSize(scrollProgressPercent * SIZE_SLOPE + BASE_SIZE);
                    mXButton.setTextSize(-scrollProgressPercent * SIZE_SLOPE + BASE_SIZE);


            }
        });
    }


    @OnClick(R.id.checkButton)
    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.xButton)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    public static class ProfileDisplayInstance implements Serializable {

        private final float RANGE = 50;

        private float mWiggleY;
        private float mWiggleX;
        private String mEventID;
        private String mUserID;
        private Profile mProfile;

        public ProfileDisplayInstance(Attendance attendance) {
            mEventID = attendance.getEvent().getObjectId();
            mUserID = attendance.getUser().getObjectId();
            mProfile = Profile.fromUser(attendance.getUser());
            Random random = new Random();
            mWiggleX = RANGE * (random.nextFloat() * 2 - 1);
            mWiggleY = RANGE * (random.nextFloat() * 2 - 1);
        }

        public Profile getProfile() {
            return mProfile;
        }

        public float getWiggleY() {
            return mWiggleY;
        }

        public float getWiggleX() {
            return mWiggleX;
        }

        public String getUserID(){return mUserID;}

        public String getEventID(){return mEventID;}
    }

    private class CardAdapter extends ArrayAdapter<ProfileDisplayInstance> {
        private final LayoutInflater mInflater;
        private final List<ProfileDisplayInstance> mProfileDisplayInstances;

        public CardAdapter(Context context, List<ProfileDisplayInstance> objects) {
            super(context, R.layout.card, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mProfileDisplayInstances = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.card, parent, false);
            } else {
                view = convertView;
            }

            ProfileDisplayInstance pdi = mProfileDisplayInstances.get(position);
            view.setTranslationY(pdi.getWiggleY());
            view.setTranslationX(pdi.getWiggleX());

            TextView nameField = (TextView) view.findViewById(R.id.contact_name);
            nameField.setText(pdi.getProfile().getFullName());

            TextView positionField = (TextView) view.findViewById(R.id.contact_position);
            positionField.setText(pdi.getProfile().getPosition());

            TextView emailField = (TextView) view.findViewById(R.id.contact_email);
            emailField.setText(pdi.getProfile().getEmail());

            TextView phoneField = (TextView) view.findViewById(R.id.contact_number);
            phoneField.setText(pdi.getProfile().getPhoneNumber());
            return view;
        }
    }

}
