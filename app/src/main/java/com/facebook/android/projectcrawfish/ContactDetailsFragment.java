// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactDetailsFragment extends CustomViewDialog {

    public static final String PROFILE = "PROFILE";

    @Bind(R.id.contact_name)
    TextView mNameField;
    @Bind(R.id.contact_position)
    TextView mPositionField;
    @Bind(R.id.contact_number)
    TextView mNumberField;
    @Bind(R.id.contact_email)
    TextView mEmailField;
    @Bind(R.id.profile_picture)
    ImageView mPictureField;
    @Bind(R.id.contact_fb)
    IconButton mContactFb;
    @Bind(R.id.contact_linkedin)
    IconButton mContactLinkedIn;

    @Bind(R.id.switcher)
    ProgressSwitcher mSwitcher;

    protected Profile mProfile;

    public static ContactDetailsFragment newInstance(ParseUser user) {
        ContactDetailsFragment frag = new ContactDetailsFragment();
        Bundle args = getBundleFromUser(user);
        frag.setArguments(args);
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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
        } else {
            bundle = getArguments();
        }

        mProfile = (Profile) bundle.getSerializable(PROFILE);
    }

    @Override
    public View onCreateCustomView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, v);

        updateUI();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROFILE, mProfile);
    }

    private void updateUI() {
        mNameField.setText(mProfile.getFullName());
        mPositionField.setText(mProfile.getPosition());
        mNumberField.setText(mProfile.getPhoneNumber());
        mEmailField.setText(mProfile.getEmail());
        mProfile.loadProfilePictureIntoImageView(mPictureField,mSwitcher);


        String linkedIn = mProfile.getLinkedIn();

        if (linkedIn == null || linkedIn.equals("") ||
                linkedIn.equals("https://www.linkedin.com/in/")) {
            mContactLinkedIn.setEnabled(false);
            mContactLinkedIn.setTextColor(getResources().getColor(R.color.greyed_out));
        }

        if (mProfile.getFbId() == null || mProfile.getIsFbPublic() == null || !mProfile.getIsFbPublic()) {
            mContactFb.setEnabled(false);
            mContactFb.setTextColor(getResources().getColor(R.color.greyed_out));
        }
    }

    @OnClick(R.id.contact_number)
    public void onNumberClick(View view) {
        if (mProfile.getPhoneNumber() != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mProfile.getPhoneNumber()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.contact_email)
    public void onEmailClick(View view) {
        if (mProfile.getEmail() != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", mProfile.getEmail(), null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    }

    @OnClick(R.id.contact_fb)
    public void onFbClick(View view) {
        if (mProfile.getFbId() != null) {
            openFacebookIntent(getActivity());
        }
    }

    @OnClick(R.id.contact_linkedin)
    public void onLinkedInClick(View view) {
        openLinkedInIntent(getActivity());

    }

    public void openFacebookIntent(Context context) {
        String facebookUrl = "https://www.facebook.com/" + mProfile.getFbId();
        String fbId = mProfile.getFbId();

        try {
            int versionCode = context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookUrl)));
            } else {
                // open the Facebook app using the old method
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + fbId)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browsers
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    public void openLinkedInIntent(Context context) {

        String linkedIn = mProfile.getLinkedIn();
        if (linkedIn != null && !linkedIn.equals("") && !linkedIn.equals("https://www.linkedin.com/in/")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(linkedIn));
            startActivity(i);
        }
    }
}
