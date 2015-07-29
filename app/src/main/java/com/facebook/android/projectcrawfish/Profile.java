// Copyright 2004-present Facebook. All Rights Reserved.
package com.facebook.android.projectcrawfish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    protected String mUserID;
    private String mFirstName;
    private String mLastName;
    private String mPosition;
    private String mPhoneNumber;
    private String mEmail;
    private byte[] mProfilePicture;

    private static Profile defaultProfile(ParseUser user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getString("name"));
        profile.setPosition("");
        profile.setPhoneNumber("");
        profile.mProfilePicture = null;
        return profile;
    }

    public static Profile fromUser(ParseUser user) {
        Profile profile;
        if (user.get(FIRST_NAME) == null) {
            profile = Profile.defaultProfile(user);
        } else {
            profile = new Profile();
            profile.mFirstName = (String) user.get(FIRST_NAME);
            profile.mLastName = (String) user.get(LAST_NAME);
            profile.mPosition = (String) user.get(POSITION);
            profile.mPhoneNumber = (String) user.get(PHONE_NUMBER);
            profile.mEmail = (String) user.get(EMAIL);
            profile.mProfilePicture = null;//TODO convert to bitmap in usages
        }
        profile.mUserID = user.getObjectId();
        return profile;
    }

    public void save(SaveCallback callback) {
        ParseUser user = ParseObject.createWithoutData(ParseUser.class, mUserID);
        user.put(FIRST_NAME, mFirstName);
        user.put(LAST_NAME, mLastName);
        user.put(POSITION, mPosition);
        user.put(PHONE_NUMBER, mPhoneNumber);
        user.put(EMAIL, mEmail);
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

    public void getProfilePicture(final ProfilePictureGetListener listener) {
        if (mProfilePicture != null) {
            listener.onGet(BitmapFactory.decodeByteArray(mProfilePicture, 0, mProfilePicture.length));
        } else {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(mUserID, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser.get(PROFILE_PICTURE) != null) {
                        ParseFile file = (ParseFile) parseUser.get(PROFILE_PICTURE);
                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    mProfilePicture = bytes;
                                    listener.onGet(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public interface ProfilePictureSaveListener {
        void onSave(Bitmap bitmap);
    }

    public void setProfilePicture(final Bitmap bitmap, final ProfilePictureSaveListener listener) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        mProfilePicture = data;
        ParseFile file = new ParseFile("profile_picture.png", data);
        ParseUser user = ParseObject.createWithoutData(ParseUser.class, mUserID);
        user.put(PROFILE_PICTURE, file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                listener.onSave(bitmap);
            }
        });
    }

}
