// Copyright 2004-present Facebook. All Rights Reserved.
 package com.facebook.android.projectcrawfish;

        import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
        import com.parse.ParseObject;
        import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
        import java.util.Map;

// Copyright 2004-present Facebook. All Rights Reserved.


public class Profile implements Serializable{

    public static final String FIRST_NAME = "pr_first_name";
    public static final String LAST_NAME = "pr_last_name";
    public static final String POSITION = "pr_position";
    public static final String PHONE_NUMBER = "pr_phone_number";
    public static final String EMAIL = "pr_email";
    protected String mUserID;
    private String mFirstName;
    private String mLastName;
    private String mPosition;
    private String mPhoneNumber;
    private String mEmail;

    private static Profile defaultProfile(ParseUser user){
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getString("name"));
        profile.setPosition("");
        profile.setPhoneNumber("");
        return profile;
    }

    public static Profile fromUser(ParseUser user){
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
        }
        profile.mUserID = user.getObjectId();
        return profile;
    }

    public void save(SaveCallback callback) {
        ParseUser user = ParseObject.createWithoutData(ParseUser.class,mUserID);
        user.put(FIRST_NAME,mFirstName);
        user.put(LAST_NAME, mLastName);
        user.put(POSITION,mPosition);
        user.put(PHONE_NUMBER,mPhoneNumber);
        user.put(EMAIL,mEmail);
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

    public void setFullName(String fullName){
        String[] name = fullName.split(" ");

        if (name.length > 2) {
            this.setFirstName(name[0] + " " + name[1]);
            this.setLastName(name[name.length - 1]);
        } else {
            this.setFirstName(name[0]);
            this.setLastName(name[name.length - 1]);
        }
    }
}
