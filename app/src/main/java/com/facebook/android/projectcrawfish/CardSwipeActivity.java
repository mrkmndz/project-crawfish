// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CardSwipeActivity extends AppCompatActivity {

    private CardAdapter mCardAdapter;
    private int mCurrentCardIndex;
    private ArrayList<ProfileDisplayInstance> mPDIs;

    @Bind(R.id.frame)
    SwipeFlingAdapterView flingContainer;

    /*TEST CODE */
        private static final String[] PEOPLE = new String[] {
                "Grace Kao", "Maria Zlatkova", "Mark Mendoza",  "Sheryl Sandberg", "Mark Zuckerberg", "Josh Skeen"
        };
        private ProfileDisplayInstance testFill(String name){
            Profile profile = new Profile();
            profile.setName(name);
            return new ProfileDisplayInstance(profile);
        }
        private void fillPDIs(){
            mPDIs = new ArrayList<>();
            for(String name : PEOPLE){
                mPDIs.add(testFill(name));
            }
        }
        private void infiniteConnies(){
            mPDIs.add(testFill("Connie ".concat(String.valueOf(mCurrentCardIndex))));
            mCardAdapter.notifyDataSetChanged();

            mCurrentCardIndex++;
        }

        private class Profile{
            private String mName;
            public String getName() {
                return mName;
            }
            public void setName(String name) {
                mName = name;
            }
        }
    /**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_swipe);
        ButterKnife.bind(this);

        fillPDIs();//TODO actual parse pulldown here

        mCardAdapter = new CardAdapter(this,mPDIs);

        flingContainer.setAdapter(mCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mPDIs.remove(0);
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

            }

            @Override
            public void onRightCardExit(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                infiniteConnies();//TODO end of deck handling
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                if(view != null) {
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
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

    private class ProfileDisplayInstance{

        private final float RANGE = 10;

        private final Profile mProfile;
        private float mWiggleAngle;

        public ProfileDisplayInstance(Profile profile){
            mProfile = profile;
            Float randomOver1 = (new Random()).nextFloat();
            mWiggleAngle=RANGE*(randomOver1*2-1);
        }

        public Profile getProfile() {
            return mProfile;
        }

        public float getWiggleAngle() {
            return mWiggleAngle;
        }
    }

    private class CardAdapter extends ArrayAdapter {
        private final LayoutInflater mInflater;
        private final List<ProfileDisplayInstance> mProfileDisplayInstances;

        public CardAdapter(Context context, List<ProfileDisplayInstance> objects) {
            super(context, R.layout.card, objects);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mProfileDisplayInstances = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            TextView nameField;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.card, parent, false);
            } else {
                view = convertView;
            }

            ProfileDisplayInstance pdi = mProfileDisplayInstances.get(position);
            CardView card = (CardView) view.findViewById(R.id.actual_card);
            card.setRotation(pdi.getWiggleAngle());

            nameField = (TextView) view.findViewById(R.id.contact_name);
            nameField.setText(pdi.getProfile().getName());

            return view;
        }
    }

}
