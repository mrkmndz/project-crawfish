// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

/**
 * Created by markamendoza on 7/15/15.
 */
public class MainDispatchActivity extends Activity {

    private static final int LOGIN_REQUEST = 0;
    private static final int TARGET_REQUEST = 1;

    private static final String LOG_TAG = "ParseLoginDispatch";

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runDispatch();
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            runDispatch();
        } else {
            finish();
        }
    }

    private Intent getParseLoginIntent() {
        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        return builder.build();
    }

    private Intent getTargetIntent(){
        Intent intent  = new Intent(this, MainActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        return intent;
    }

    private void runDispatch() {
        if (ParseUser.getCurrentUser() != null) {
            debugLog(getString(com.parse.ui.R.string.com_parse_ui_login_dispatch_user_logged_in) + MainActivity.class);
            startActivityForResult(getTargetIntent(), TARGET_REQUEST);
        } else {
            debugLog(getString(com.parse.ui.R.string.com_parse_ui_login_dispatch_user_not_logged_in));
            startActivityForResult(getParseLoginIntent(), LOGIN_REQUEST);
        }
    }

    private void debugLog(String message) {
        if (Parse.getLogLevel() <= Parse.LOG_LEVEL_DEBUG &&
                Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(LOG_TAG, message);
        }
    }
}
