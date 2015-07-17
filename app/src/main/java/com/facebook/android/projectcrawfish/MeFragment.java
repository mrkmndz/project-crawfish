// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.IconButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;




public class MeFragment extends ProfileDialog implements View.OnClickListener {

    @Bind(R.id.contact_fb) IconButton mContactFb;
    @Bind(R.id.contact_linkedin) IconButton mContactLinkedIn;
    @Bind(R.id.save_button) IconButton mSave;
    @Bind(R.id.edit_button) IconButton mEdit;

    @Bind(R.id.name_switcher) ViewSwitcher nameSwitcher;
    @Bind(R.id.position_switcher) ViewSwitcher positionSwitcher;
    @Bind(R.id.email_switcher) ViewSwitcher emailSwitcher;
    @Bind(R.id.number_switcher) ViewSwitcher numberSwitcher;

    @Bind(R.id.contact_name) TextView mContactName;
    @Bind(R.id.contact_position) TextView mContactPosition;
    @Bind(R.id.contact_email) TextView mContactEmail;
    @Bind(R.id.contact_number) TextView mContactNumber;

    @Bind(R.id.contact_name_edit) EditText mEditName;
    @Bind(R.id.contact_position_edit) EditText mEditPosition;
    @Bind(R.id.contact_number_edit) EditText mEditNumber;
    @Bind(R.id.contact_email_edit) EditText mEditEmail;


    public static MeFragment newInstance() {
        MeFragment frag = new MeFragment();
        Bundle args = getBundleFromUser(ParseUser.getCurrentUser());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    @OnClick(R.id.edit_button)
    public void onClick(View v) {
        nameSwitcher.showPrevious();
        positionSwitcher.showPrevious();
        emailSwitcher.showPrevious();
        numberSwitcher.showPrevious();

        mContactFb.setClickable(true);
        mContactLinkedIn.setClickable(true);

        mEditNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        mEdit.setEnabled(false);
        mEdit.setVisibility(View.GONE);
        mSave.setEnabled(true);
        mSave.setVisibility(View.VISIBLE);
        updateUI();
    }

    @OnClick(R.id.save_button)
    public void saveCard() {
        nameSwitcher.showNext();
        positionSwitcher.showNext();
        emailSwitcher.showNext();
        numberSwitcher.showNext();
        mContactFb.setClickable(false);
        mContactLinkedIn.setClickable(false);

        // TODO save to parse only if email & phone number are valid and name is not blank
        mProfile.saveToParse();
        mSave.setEnabled(false);
        mSave.setVisibility(View.GONE);

        mEdit.setEnabled(true);
        mEdit.setVisibility(View.VISIBLE);

        updateUI();
    }

    private boolean isValidNumber(CharSequence phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    private void updateUI() {
        mContactName.setText(mProfile.getFullName());
        mContactPosition.setText(mProfile.getPosition());
        mContactNumber.setText(mProfile.getPhoneNumber());
        mContactEmail.setText(mProfile.getEmail());
        mEditName.setText(mProfile.getFullName());
        mEditPosition.setText(mProfile.getPosition());
        mEditEmail.setText(mProfile.getEmail());
        mEditNumber.setText(mProfile.getPhoneNumber());

        hideKeyboard();
        validateInput();
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void validateInput() {

        if (isValidEmail(mProfile.getEmail())) {
            mContactEmail.setError(null);
            mEditEmail.setError(null);
        } else {
            mContactEmail.setError("Please enter a valid email.");
            mEditEmail.setError("Please enter a valid email.");
        }

        // TODO add other validation, eg phone number, whether name field is blank, etc.

        // if (TextUtils.isEmpty(mContactName.getText())) {
        //      mContactName.setError("This field cannot be blank");
        // }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditNumber.getWindowToken(), 0);
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
        mProfile.setFullName(text.toString());
    }

    @OnTextChanged(R.id.contact_position_edit)
    void onPositionChanged(CharSequence text) {
        mProfile.setPosition(text.toString());
    }

    @OnTextChanged(R.id.contact_number_edit)
    void onNumberChanged(CharSequence text) {

        mProfile.setPhoneNumber(text.toString());
        if (isValidNumber(mProfile.getPhoneNumber())) {
            mContactNumber.setError(null);
            mEditNumber.setError(null);
        } else {
            mContactNumber.setError("Please enter a valid phone number.");
            mEditNumber.setError("Please enter a valid phone number.");
        }
    }

    @OnTextChanged(R.id.contact_email_edit)
    void onEmailChanged(CharSequence text) {
        String formattedEmail = text.toString().replaceAll("\\s", "");
        validateInput();
        mProfile.setEmail(formattedEmail);
    }
}
