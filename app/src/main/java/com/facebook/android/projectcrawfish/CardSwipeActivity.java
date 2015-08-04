// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CardSwipeActivity extends AppCompatActivity {

    private static final int BASE_SIZE = 40;
    private static final int SIZE_SLOPE = 50;
    private static final String PDIS = "PDIS";
    private CardAdapter mCardAdapter;
    private ArrayList<ProfileDisplayInstance> mPDIs;

    @Bind(R.id.xButton)
    Button mXButton;
    @Bind(R.id.checkButton)
    Button mCheckButton;

    @Bind(R.id.frame)
    protected SwipeFlingAdapterView flingContainer;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


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

        mToolbar.setTitle("bïnder");
        this.setSupportActionBar(mToolbar);

        mCheckButton.setTextSize(BASE_SIZE);
        mXButton.setTextSize(BASE_SIZE);

        //noinspection unchecked
        mPDIs = (ArrayList<ProfileDisplayInstance>) getIntent().getSerializableExtra(PDIS);

        mCardAdapter = new CardAdapter(this, mPDIs);


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
                    ParseUser user = ParseObject.createWithoutData(ParseUser.class, pdi.getProfile().getUserID());
                    Event event = ParseObject.createWithoutData(Event.class, pdi.getEventID());
                    Swipe.sendSwipe(true, user, event);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                mCheckButton.setTextSize(BASE_SIZE);
                mXButton.setTextSize(BASE_SIZE);
                ProfileDisplayInstance pdi = (ProfileDisplayInstance) dataObject;
                    ParseUser user = ParseObject.createWithoutData(ParseUser.class, pdi.getProfile().getUserID());
                    Event event = ParseObject.createWithoutData(Event.class, pdi.getEventID());
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

        private final float mWiggleY;
        private final float mWiggleX;
        private final String mEventID;
        private final PublicProfile mProfile;

        public ProfileDisplayInstance(Event event, PublicProfile profile) {
            mEventID = event.getObjectId();
            mProfile = profile;
            Random random = new Random();
            mWiggleX = RANGE * (random.nextFloat() * 2 - 1);
            mWiggleY = RANGE * (random.nextFloat() * 2 - 1);
        }

        public PublicProfile getProfile() {
            return mProfile;
        }

        public float getWiggleY() {
            return mWiggleY;
        }

        public float getWiggleX() {
            return mWiggleX;
        }

        public String getEventID(){return mEventID;}

    }

    private class CardAdapter extends ArrayAdapter<ProfileDisplayInstance> {
        private final LayoutInflater mInflater;
        private final List<ProfileDisplayInstance> mProfileDisplayInstances;

        public CardAdapter(Context context, List<ProfileDisplayInstance> objects) {
            super(context, R.layout.fragment_contact, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mProfileDisplayInstances = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_contact, parent, false);
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

            final ProgressSwitcher switcher = (ProgressSwitcher) view.findViewById(R.id.switcher);
            switcher.showContent();

            ImageView profilePic = (ImageView) view.findViewById(R.id.profile_picture);

            Picasso.with(view.getContext()).load(pdi.getProfile().getProfilePictureUrl())
                    .into(profilePic);

            return view;
        }
    }

}
