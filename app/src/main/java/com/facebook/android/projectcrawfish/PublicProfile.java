// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.support.v4.app.FragmentActivity;

import com.parse.Parse;
import com.parse.ParseFile;

import java.util.Map;

/**
 * Created by markamendoza on 7/29/15.
 */
public class PublicProfile extends Profile{

    public static final String USER_ID = "user_id";
    public static final String PICTURE_URL = "picture_url";


    PublicProfile(Map<String, String> attrMap){
        this.mUserID = attrMap.get(USER_ID);
        this.setFirstName( attrMap.get(FIRST_NAME));
        this.setLastName(attrMap.get(LAST_NAME));
        this.setProfilePictureUrl(attrMap.get(PICTURE_URL));
        this.setPosition("****************");
        this.setEmail("*******@*****.***");
        this.setPhoneNumber("(***) ***-*****");
    }
}
