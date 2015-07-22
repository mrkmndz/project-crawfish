// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.IconButton;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileTab extends FrameFragment<MyProfileTab.MeFragment> {


    @Override
    protected MeFragment getNewFragmentInstance() {
        return MeFragment.newInstance();
    }

    public void onChangedPicture(Uri selectedImageUri) {
        getFragment().onChangedPicture(selectedImageUri);
    }

    @Override
    protected String getTitle() {
        return "My Profile";
    }

    public static class MeFragment extends Fragment implements View.OnClickListener {

        @Bind(R.id.contact_fb)
        IconButton mContactFb;
        @Bind(R.id.contact_linkedin)
        IconButton mContactLinkedIn;
        @Bind(R.id.save_button)
        IconButton mSave;
        @Bind(R.id.edit_button)
        IconButton mEdit;
        @Bind(R.id.camera_button)
        IconButton mCamera;

        @Bind(R.id.name_text_edit)
        TextEditSwitcher nameTextEditSwitcher;
        @Bind(R.id.position_text_edit)
        TextEditSwitcher positionTextEditSwitcher;
        @Bind(R.id.email_text_edit)
        TextEditSwitcher emailTextEditSwitcher;
        @Bind(R.id.number_text_edit)
        TextEditSwitcher numberTextEditSwitcher;

        @Bind(R.id.profile_picture)
        ImageView mProfilePictureView;

        @Bind(R.id.switcher)
        ProgressSwitcher mSwitcher;

        protected Profile mProfile;

        public static boolean editMode;

        OnFragmentInteractionListener mListener;

        public static final String PROFILE = "PROFILE";
        public static final String SAVE_EDIT = "SAVE_EDIT";
        public static final String IMAGE_TYPE = "image/*";
        public static final int SELECT_SINGLE_PICTURE = 101;

        public static MeFragment newInstance() {
            MeFragment frag = new MeFragment();
            Bundle args = getBundleFromUser(ParseUser.getCurrentUser());
            frag.setArguments(args);
            return frag;
        }

        protected static Bundle getBundleFromUser(ParseUser user) {
            Bundle bundle = new Bundle();
            Profile profile = Profile.fromUser(user);
            bundle.putSerializable(PROFILE, profile);
            return bundle;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle;

            if (savedInstanceState != null) {
                bundle = savedInstanceState;
                editMode = bundle.getBoolean(SAVE_EDIT);
            } else {
                bundle = getArguments();
            }

            mProfile = (Profile) bundle.getSerializable(PROFILE);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable(PROFILE, mProfile);
            outState.putBoolean(SAVE_EDIT, editMode);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View v = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, v);

            nameTextEditSwitcher.showText();
            positionTextEditSwitcher.showText();
            emailTextEditSwitcher.showText();
            numberTextEditSwitcher.showText();

            nameTextEditSwitcher.setNameDisplay();
            positionTextEditSwitcher.setDisplay();
            emailTextEditSwitcher.setDisplay();
            numberTextEditSwitcher.setDisplay();

            mContactFb.setClickable(false);
            mContactLinkedIn.setClickable(false);
            updateUI();
            return v;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            try {
                mListener = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }

        @Override
        public void onResume() {
            super.onResume();

            if (editMode) {
                editText();
            } else {
                saveText();
            }
        }

        @OnClick(R.id.edit_button)
        public void onClick(View v) {
            editText();
        }

        @OnClick(R.id.save_button)
        public void saveCard() {
            saveText();
        }

        public void editText() {
            editMode = true;

            nameTextEditSwitcher.showEdit();
            positionTextEditSwitcher.showEdit();
            emailTextEditSwitcher.showEdit();
            numberTextEditSwitcher.showEdit();

            mContactFb.setClickable(true);
            mContactLinkedIn.setClickable(true);

            mCamera.setEnabled(true);
            mCamera.setVisibility(View.VISIBLE);

            numberTextEditSwitcher.getEditText().addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            nameTextEditSwitcher.getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String[] name = s.toString().split(" ");

                    if (name.length < 2) {
                        Snackbar snack = Snackbar.make(nameTextEditSwitcher, "Please enter your full name.", Snackbar.LENGTH_LONG);
                        View view = snack.getView();
                        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        params.topMargin = 250;
                        view.setLayoutParams(params);
                        snack.show();
                        mSave.setEnabled(false);
                    } else {
                        mSave.setEnabled(true);
                        mProfile.setFullName(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            positionTextEditSwitcher.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mProfile.setPosition(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            emailTextEditSwitcher.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String formattedEmail = s.toString().replaceAll("\\s", "");
                    mProfile.setEmail(formattedEmail);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            numberTextEditSwitcher.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mProfile.setPhoneNumber(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mEdit.setEnabled(false);
            mEdit.setVisibility(View.GONE);
            mSave.setEnabled(true);
            mSave.setVisibility(View.VISIBLE);
            updateUI();
        }

        public void saveText() {
            editMode = false;

            hideKeyboard();

            if (isValidEmail(mProfile.getEmail()) && isValidNumber(mProfile.getPhoneNumber())) {

                nameTextEditSwitcher.showText();
                positionTextEditSwitcher.showText();
                emailTextEditSwitcher.showText();
                numberTextEditSwitcher.showText();

                mContactFb.setClickable(false);
                mContactLinkedIn.setClickable(false);

                mCamera.setEnabled(false);
                mCamera.setVisibility(View.GONE);

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
            } else {
                validateInput();
            }
        }

        private void updateUI() {

            nameTextEditSwitcher.setDisplayedText(mProfile.getFullName());
            positionTextEditSwitcher.setDisplayedText(mProfile.getPosition());
            emailTextEditSwitcher.setDisplayedText(mProfile.getEmail());
            numberTextEditSwitcher.setDisplayedText(mProfile.getPhoneNumber());

            String linkedIn = mProfile.getLinkedIn();

            if (linkedIn == null || linkedIn.equals("") ||
                    linkedIn.equals("https://www.linkedin.com/in/")) {
                mContactLinkedIn.setTextColor(getResources().getColor(R.color.greyed_out));
            } else {
                mContactLinkedIn.setTextColor(getResources().getColor(R.color.linkedIn));
            }

            String fbId = mProfile.getFbId();

            if (fbId == null || mProfile.getIsFbPublic() == null || !mProfile.getIsFbPublic()) {
                mContactFb.setTextColor(getResources().getColor(R.color.greyed_out));
            } else {
                mContactFb.setTextColor(getResources().getColor(R.color.com_facebook_blue));
            }

            mProfile.loadProfilePictureIntoImageView(mProfilePictureView, mSwitcher);

            hideKeyboard();
            validateInput();
        }

        @OnClick(R.id.camera_button)
        public void setPicture() {
            mListener.openGallery();

        }

        private void onChangedPicture(Uri selectedImageUri) {
            mSwitcher.showBar();
            Bitmap profilePicture;
            try {
                profilePicture = new UserPicture(selectedImageUri, getActivity().getApplicationContext().getContentResolver()).getBitmap();
                mProfile.setProfilePicture(profilePicture, new Profile.ProfilePictureSaveListener() {
                    @Override
                    public void onSave(Bitmap bitmap) {
                        mProfilePictureView.setImageBitmap(bitmap);
                        mSwitcher.showContent();
                    }
                });
            } catch (IOException e) {
                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
            }
        }

        private boolean isValidEmail(CharSequence email) {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        private boolean isValidNumber(CharSequence phone) {
            return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
        }

        public void validateInput() {
            if (!isValidEmail(mProfile.getEmail())) {
                Snackbar.make(emailTextEditSwitcher.getTextView(), "Please enter a valid email.", Snackbar.LENGTH_LONG)
                        .show();
            }

            if (!isValidNumber(mProfile.getPhoneNumber())) {
                Snackbar.make(emailTextEditSwitcher.getTextView(), "Please enter a valid phone number.", Snackbar.LENGTH_LONG)
                        .show();
            }

            if (!isValidEmail(mProfile.getEmail()) && !isValidNumber(mProfile.getPhoneNumber())) {
                Snackbar.make(emailTextEditSwitcher.getTextView(), "Please enter a valid email & phone number.", Snackbar.LENGTH_LONG)
                        .show();
            }
        }

        private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(numberTextEditSwitcher.getEditText().getWindowToken(), 0);
        }

        @OnClick(R.id.contact_fb)
        public void linkFb() {
            mListener.onFbSelected(mProfile);
        }

        @OnClick(R.id.contact_linkedin)
        public void linkLinkedIn() {
            mListener.onLinkedInSelected(mProfile);
        }

        interface OnFragmentInteractionListener {
            void openGallery();
            void onFbSelected(Profile profile);
            void onLinkedInSelected(Profile profile);
        }


        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }
    }
}
