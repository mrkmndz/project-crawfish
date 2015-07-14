// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.IconButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;




public class MeFragment extends ProfileDialog implements View.OnClickListener {

    public MeFragment() {
        // Required empty public constructor
    }



    IconButton mContactFb;
    IconButton mContactLinkedIn;
    IconButton mSave;
    IconButton mEdit;
    @Bind(R.id.name_switcher) ViewSwitcher nameSwitcher;
    @Bind(R.id.position_switcher) ViewSwitcher positionSwitcher;
    @Bind(R.id.email_switcher) ViewSwitcher emailSwitcher;
    @Bind(R.id.number_switcher) ViewSwitcher numberSwitcher;
    @Bind(R.id.contact_name)
    TextView mContactName;
    @Bind(R.id.contact_position)
    TextView mContactPosition;
    @Bind(R.id.contact_email)
    TextView mContactEmail;
    @Bind(R.id.contact_number)
    TextView mContactNumber;
    @Bind(R.id.contact_name_edit)
    EditText mEditName;
    @Bind(R.id.contact_position_edit)
    EditText mEditPosition;
    @Bind(R.id.contact_number_edit)
    EditText mEditNumber;
    @Bind(R.id.contact_email_edit)
    EditText mEditEmail;


    public static MeFragment newInstance(String ContactID) {
        MeFragment frag = new MeFragment();
        Bundle args = getBundleFromID(ContactID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onClick(View v) {
        nameSwitcher.showPrevious();
        positionSwitcher.showPrevious();
        emailSwitcher.showPrevious();
        numberSwitcher.showPrevious();

        mContactFb.setClickable(true);
        mContactLinkedIn.setClickable(true);

        mEdit.setEnabled(false);
        mEdit.setVisibility(View.GONE);
        mSave.setEnabled(true);
        mSave.setVisibility(View.VISIBLE);
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        ButterKnife.bind(this, v);

        mContactFb = (IconButton) v.findViewById(R.id.contact_fb);
        mContactLinkedIn = (IconButton) v.findViewById(R.id.contact_linkedin);
        mSave = (IconButton) v.findViewById(R.id.save_button);
        mEdit = (IconButton) v.findViewById(R.id.edit_button);
        mEdit.setOnClickListener(this);

        updateUI();

        return v;
    }



    @OnClick(R.id.save_button)
    public void saveCard() {
        nameSwitcher.showNext();
        positionSwitcher.showNext();
        emailSwitcher.showNext();
        numberSwitcher.showNext();

        mContactFb.setClickable(false);
        mContactLinkedIn.setClickable(false);

        mSave.setEnabled(false);
        mSave.setVisibility(View.GONE);
        mEdit.setEnabled(true);
        mEdit.setVisibility(View.VISIBLE);
        updateUI();
    }

    private void updateUI() {
        mContactName.setText(mFirstName + " " + mLastName);
        mContactPosition.setText(mPosition);
        mContactNumber.setText(mNumber);
        mContactEmail.setText(mEmail);
        mEditName.setText(mFirstName + " " + mLastName);
        mEditPosition.setText(mPosition);
        mEditEmail.setText(mEmail);
        mEditNumber.setText(mNumber);
    }


    /*

   @OnClick(R.id.contact_fb)
    public void linkFb(){
        LinkFacebookFragment linkFbDialog = new LinkFacebookFragment();
        linkFbDialog.show(getChildFragmentManager(), "Link Facebook");
    }

    @OnClick(R.id.contact_linkedin)
    public void linkLinkedIn(){

    }
    */
    @OnTextChanged(R.id.contact_name_edit)
    void onNameChanged(CharSequence text) {
        String fullName = text.toString();
        String[] name = fullName.split(" ");

        if (name.length > 2) {
            mFirstName = (name[0] + " " + name[1]);
            mLastName = (name[name.length - 1]);
        } else {
            mFirstName = (name[0]);
            mLastName = (name[name.length - 1]);
        }
    }

    @OnTextChanged(R.id.contact_position_edit)
    void onPositionChanged(CharSequence text) {
        mPosition = (text.toString());
    }

    @OnTextChanged(R.id.contact_number_edit)
    void onNumberChanged(CharSequence text) {
        mNumber = (text.toString());
    }

    @OnTextChanged(R.id.contact_email_edit)
    void onEmailChanged(CharSequence text) {
        mEmail = (text.toString());
    }
}
