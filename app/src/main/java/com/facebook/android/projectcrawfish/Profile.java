// Copyright 2004-present Facebook. All Rights Reserved.
 package com.facebook.android.projectcrawfish;

        import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;

// Copyright 2004-present Facebook. All Rights Reserved.


public class Profile implements Serializable{

    public static final String KEY = "Profile";
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("position")
    private String mPosition;
    @SerializedName("phone_number")
    private String mPhoneNumber;
    @SerializedName("email")
    private String mEmail;

    private static Profile defaultProfile(ParseUser user){
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getString("name"));
        profile.setPosition("");
        profile.setPhoneNumber("");
        return profile;
    }

    private static Gson getGson(){
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
    public static Profile fromUser(ParseUser user){
        String json = (String) user.get(KEY);
        if (json ==null )return Profile.defaultProfile(user);
        return getGson().fromJson(json,Profile.class);
    }

    public void saveToParse(SaveCallback callback){
        ParseUser.getCurrentUser().put(KEY, this.toJSON());
        ParseUser.getCurrentUser().saveInBackground(callback);
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

    private String toJSON(){
        return getGson().toJson(this);
    }
}
