// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.IconButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class MeFragment extends ProfileDialog implements View.OnClickListener {

    @Bind(R.id.contact_fb)
    IconButton mContactFb;
    @Bind(R.id.contact_linkedin)
    IconButton mContactLinkedIn;
    @Bind(R.id.save_button)
    IconButton mSave;
    @Bind(R.id.edit_button)
    IconButton mEdit;

    @Bind(R.id.name_switcher)
    ViewSwitcher nameSwitcher;
    @Bind(R.id.position_switcher)
    ViewSwitcher positionSwitcher;
    @Bind(R.id.email_switcher)
    ViewSwitcher emailSwitcher;
    @Bind(R.id.number_switcher)
    ViewSwitcher numberSwitcher;

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


    public static MeFragment newInstance() {
        MeFragment frag = new MeFragment();
        Bundle args = getBundleFromUser(ParseUser.getCurrentUser());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateCustomView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    OnLinkingSelectedListener mFbCallback;
    OnLinkedInListener mLinkedInCallback;

    // Container Activity must implement this interface
    public interface OnLinkingSelectedListener {
        public void onFbSelected(ParseUser user);
    }

    // Container Activity must implement this interface
    public interface OnLinkedInListener {
        public void onLinkedInSelected(ParseUser user);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mFbCallback = (OnLinkingSelectedListener) activity;
            mLinkedInCallback = (OnLinkedInListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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

        mProfile.save(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mEdit.setEnabled(true);
                mEdit.setVisibility(View.VISIBLE);
            }
        });
        mSave.setEnabled(false);
        mSave.setVisibility(View.GONE);

        updateUI();
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

    private boolean isValidNumber(CharSequence phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    public void validateInput() {
        if (isValidEmail(mProfile.getEmail())) {
            mContactEmail.setError(null);
        } else {
            mContactEmail.setError("Please enter a valid email.");
            Snackbar.make(mContactEmail, "Please enter a valid email.", Snackbar.LENGTH_LONG)
                    .show();
        }

        if (isValidNumber(mProfile.getPhoneNumber())) {
            mContactNumber.setError(null);
        } else {
            mContactNumber.setError("Please enter a valid phone number.");
            Snackbar.make(mContactEmail, "Please enter a valid phone number.", Snackbar.LENGTH_LONG)
                    .show();
        }

        if (!isValidEmail(mProfile.getEmail()) && !isValidNumber(mProfile.getPhoneNumber())) {
            Snackbar.make(mContactEmail, "Please enter a valid email & phone number.", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditNumber.getWindowToken(), 0);
    }


    @OnClick(R.id.contact_fb)
    public void linkFb() {

        mFbCallback.onFbSelected(ParseUser.getCurrentUser());
    }

    @OnClick(R.id.contact_linkedin)
    public void linkLinkedIn() {

        mLinkedInCallback.onLinkedInSelected(ParseUser.getCurrentUser());
    }

    @OnTextChanged(R.id.contact_name_edit)
    void onNameChanged(CharSequence text) {
        String[] name = text.toString().split(" ");

        if (name.length < 2) {
            mSave.setEnabled(false);
            Snackbar snack = Snackbar.make(mContactEmail, "Please enter your full name.", Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        } else {
            mSave.setEnabled(true);
            mProfile.setFullName(text.toString());
        }
    }

    @OnTextChanged(R.id.contact_position_edit)
    void onPositionChanged(CharSequence text) {
        mProfile.setPosition(text.toString());
    }

    @OnTextChanged(R.id.contact_number_edit)
    void onNumberChanged(CharSequence text) {
        mProfile.setPhoneNumber(text.toString());
    }

    @OnTextChanged(R.id.contact_email_edit)
    void onEmailChanged(CharSequence text) {
        String formattedEmail = text.toString().replaceAll("\\s", "");
        mProfile.setEmail(formattedEmail);
    }
}
