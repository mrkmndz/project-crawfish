// Copyright 2004-present Facebook. All Rights Reserved.


package com.facebook.android.projectcrawfish;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

// Copyright 2004-present Facebook. All Rights Reserved.


public class Profile implements Serializable {

    public static final String FIRST_NAME = "pr_first_name";
    public static final String LAST_NAME = "pr_last_name";
    public static final String POSITION = "pr_position";
    public static final String PHONE_NUMBER = "pr_phone_number";
    public static final String EMAIL = "pr_email";
    public static final String PROFILE_PICTURE = "pr_profile_picture";
    public static final String FBID = "pr_fb_id";
    public static final String LINKEDIN = "pr_linked_in";
    public static final String IS_PUBLIC = "isFbPublic";

    protected String mUserID;
    private String mFirstName;
    private String mLastName;
    private String mPosition;
    private String mPhoneNumber;
    private String mEmail;
    private String mProfilePictureUrl;
    private String mFbId;
    private String mLinkedIn;
    private Boolean mIsFbPublic;

    private static Profile defaultProfile(ParseUser user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getString("name"));
        profile.setPosition("");
        profile.setPhoneNumber("");
        profile.mProfilePictureUrl = null;
        profile.setFbId("");
        profile.setLinkedIn("https://www.linkedin.com/in/");
        return profile;
    }

    public static Profile fromUser(ParseUser user) {
        Profile profile;


        if (user.get(FIRST_NAME) == null ){
            profile = Profile.defaultProfile(user);
        } else {
            profile = new Profile();
            profile.mFirstName = (String) user.get(FIRST_NAME);
            profile.mLastName = (String) user.get(LAST_NAME);
            profile.mPosition = (String) user.get(POSITION);
            profile.mPhoneNumber = (String) user.get(PHONE_NUMBER);
            profile.mEmail = (String) user.get(EMAIL);
            ParseFile file = (ParseFile) user.get(PROFILE_PICTURE);
            if (file ==null){
                profile.mProfilePictureUrl = null;
            } else {
                profile.mProfilePictureUrl = file.getUrl();
            }
            profile.mFbId = (String) user.get(FBID);
            profile.mLinkedIn = (String) user.get(LINKEDIN);
            profile.mIsFbPublic = (Boolean) user.get(IS_PUBLIC);
        }
        profile.mUserID = user.getObjectId();
        return profile;
    }

    public void save(SaveCallback callback) {
        ParseUser user = ParseObject.createWithoutData(ParseUser.class, mUserID);
        user.put(FIRST_NAME, mFirstName);
        user.put(LAST_NAME, mLastName);
        user.put(POSITION,mPosition);
        user.put(PHONE_NUMBER,mPhoneNumber);
        user.put(EMAIL,mEmail);

        if (mFbId != null) {
            user.put(FBID, mFbId);
        }

        if (mLinkedIn != null) {
            user.put(LINKEDIN, mLinkedIn);
        }

        if (mIsFbPublic != null) {
            user.put(IS_PUBLIC, mIsFbPublic);
        }

        user.saveInBackground(callback);
    }

    public String getUserID() {
        return mUserID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public void setFullName(String fullName) {
        String[] name = fullName.split(" ");

        if (name.length > 2) {
            this.setFirstName(name[0] + " " + name[1]);
            this.setLastName(name[name.length - 1]);
        } else {
            this.setFirstName(name[0]);
            this.setLastName(name[name.length - 1]);
        }
    }

    interface ProfilePictureGetListener {
        void onGet(Bitmap bitmap);
    }

    public String getFbId() {
        return mFbId;
    }

    public void setFbId(String fbId) {
        mFbId = fbId;
    }

    public String getProfilePictureUrl() {
        return mProfilePictureUrl;
    }

    protected void setProfilePictureUrl(String url){
        mProfilePictureUrl = url;
    }

    public interface ProfilePictureSaveListener {
        void onSave(Bitmap bitmap);
    }

    public void setProfilePicture(final Bitmap bitmap, final ProfilePictureSaveListener listener) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        final ParseFile file = new ParseFile("profile_picture.png", data);
        ParseUser user = ParseObject.createWithoutData(ParseUser.class, mUserID);
        user.put(PROFILE_PICTURE, file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                listener.onSave(bitmap);
                setProfilePictureUrl(file.getUrl());
            }
        });
    }

    public void loadProfilePictureIntoImageView(ImageView view, final ProgressSwitcher switcher){
        if (this.getProfilePictureUrl() == null){
            switcher.showContent();
            return;
        }

        switcher.showBar();
        Picasso.with(view.getContext()).load(this.getProfilePictureUrl())
                .into(view, new Callback() {
            @Override
            public void onSuccess() {
                switcher.showContent();
            }

            @Override
            public void onError() {
                //TODO
            }
        });
    }

    public String getLinkedIn() {
        return mLinkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        mLinkedIn = linkedIn;
    }

    public void setIsFbPublic(Boolean isFbPublic) {
        mIsFbPublic = isFbPublic;
    }

    public Boolean getIsFbPublic() {
        return mIsFbPublic;
    }
}
