// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    //private Contact mContact;



    public MeFragment() {
        // Required empty public constructor
    }


    /*@Bind(R.id.contact_fb) ImageButton mFacebook;
    @Bind(R.id.contact_linkedin) ImageButton mLinkedIn;
    @Bind(R.id.edit_button) ImageButton mEdit;
    //@Bind(R.id.save_button) Button mSave;

    @Bind(R.id.contact_name) EditText mContactName;
    @Bind(R.id.contact_position) EditText mContactPosition;
    @Bind(R.id.contact_number) EditText mContactNumber;
    @Bind(R.id.contact_email) EditText mContactEmail;

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContact = Event.getLocalEvent(getArguments().getString(ARG));
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_me, container, false);

        ButterKnife.bind(this, v);


        //updateUI();

        return v;
    }

   /* private void updateUI() {
        /*mContactName.setText();
        mContactPosition.setText();
        mContactNumber.setText();
        mContactEmail.setText();
    }

    @OnClick(R.id.edit_button)
    public void editCard() {

    }

    @OnTextChanged(R.id.contact_name)
    void onNameChanged(CharSequence text) {
        mContact.setFullName(text.toString());
    }

    @OnTextChanged(R.id.contact_position)
    void onPositionChanged(CharSequence text) {
        mContact.setPosition(text.toString());
    }

    @OnTextChanged(R.id.contact_number)
    void onNumberChanged(CharSequence text) {
        mContact.setPhoneNumber(text.toString());
    }

    @OnTextChanged(R.id.contact_email)
    void onEmailChanged(CharSequence text) {
        mContact.setEmail(text.toString());
    }
*/

}
