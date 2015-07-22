package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LinkedInFragment extends DialogFragment {

    public static final String PROFILE = "PROFILE";

    protected Profile mProfile;

    public static LinkedInFragment newInstance(Profile profile) {
        LinkedInFragment frag = new LinkedInFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROFILE, profile);
        frag.setArguments(args);
        return frag;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);

        editText.setText("https://www.linkedin.com/in/");
        if (mProfile.getLinkedIn() != null) {
            if (mProfile.getLinkedIn().equals("https://www.linkedin.com/in/")) {
                editText.setText("https://www.linkedin.com/in/");
            } else {
                editText.setText(mProfile.getLinkedIn());
            }
        }
        Selection.setSelection(editText.getText(), editText.getText().length());


        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("https://www.linkedin.com/in/")){
                    editText.setText("https://www.linkedin.com/in/");
                    Selection.setSelection(editText.getText(), editText.getText().length());
                }
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String linkedIn = editText.getText().toString();
                        mProfile.setLinkedIn(linkedIn);

                        Toast
                                .makeText(getActivity(),
                                        "Your LinkedIn url is now saved. To edit or remove it, press the button again.",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PROFILE, mProfile);
    }
}
