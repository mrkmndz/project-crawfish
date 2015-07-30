// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONObject;


public class LinkFacebookFragment extends DialogFragment {

    public static final String PROFILE = "PROFILE";

    protected Profile mProfile;

    public static LinkFacebookFragment newInstance(ParseUser user) {
        LinkFacebookFragment frag = new LinkFacebookFragment();
        Bundle args = getBundleFromUser(user);
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
        } else {
            bundle = getArguments();
        }

        mProfile = (Profile) bundle.getSerializable(PROFILE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            return new AlertDialog.Builder(getActivity())
                    //.setIcon()?
                    .setTitle("Link Facebook")
                    .setMessage("Do you want to link Facebook to profile card?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                ParseFacebookUtils
                                        .linkWithReadPermissionsInBackground(ParseUser.getCurrentUser(),
                                                getActivity(), null, new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException ex) {
                                                        if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {

                                                            linkUserFb();
                                                            Log.d("MyApp", "Woohoo, user linked to Facebook!");
                                                        } else {
                                                            Log.d("MyApp", ex.getMessage());
                                                        }
                                                    }
                                                });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
        }
        else {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Make visible")
                    .setMessage("Would you like to make your Facebook profile visible to contacts or keep it private?")
                    .setPositiveButton("Visible", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            mProfile.setIsFbPublic(true);

                            mProfile.save(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                }
                            });

                            Toast
                                    .makeText(getActivity(),
                                            "Your Facebook profile is now visible. To make it private, press the button again.",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    })
                    .setNegativeButton("Private", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            mProfile.setIsFbPublic(false);

                            mProfile.save(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                }
                            });

                            Toast
                                    .makeText(getActivity(),
                                            "Your Facebook profile is now private. To make it visible, press the button again.",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    })
                    .create();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROFILE, mProfile);
    }

    public void linkUserFb() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse graphResponse) {
                        if (object != null) {
                            JSONObject userProfile = new JSONObject();

                            try {
                                userProfile.put("facebookId", object.getString("id"));
                                userProfile.put("link", object.getString("link"));
                                String fbId = object.getString("id");
                                mProfile.setFbId(fbId);

                                mProfile.save(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                    }
                                });

                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("fb_profile", userProfile);
                                currentUser.save();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        request.executeAsync();
    }
}
