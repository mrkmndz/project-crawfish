// Copyright 2004-present Facebook. All Rights Reserved.
 package com.facebook.android.projectcrawfish;

        import com.parse.ParseClassName;

import com.parse.ParseObject;

// Copyright 2004-present Facebook. All Rights Reserved.


@ParseClassName("Profile")
public class Profile extends ParseObject {
    public static final String CLASS_NAME = "Profile";

    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String POSITION = "POSITION";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String EMAIL = "EMAIL";

    public Profile() {
        // Required default constructor
    }

    public String getFirstName() {
        return (String) get(FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        put(FIRST_NAME, firstName);
    }

    public String getLastName() {
        return (String) get(LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(LAST_NAME, lastName);
    }

    public String getPosition() {
        return (String) get(POSITION);
    }

    public void setPosition(String position) {
        put(POSITION, position);
    }

    public String getPhoneNumber() {
        return (String) get(PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(PHONE_NUMBER, phoneNumber);
    }

    public String getEmail() {
        return (String) get(EMAIL);
    }

    public void setEmail(String email) {
        put(EMAIL, email);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public void getProfilePicture(){}

    public void setProfilePicture(){}
}
