// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markamendoza on 7/21/15.
 */
public class Turnstile {
    private static Turnstile sTurnstile;
    private int mOutstandingSavers;
    private final List<OnZeroListener> mListeners;

    private Turnstile() {
        this.mOutstandingSavers = 0;
        this.mListeners = new ArrayList<>();
    }

    public static Turnstile get() {
        if (sTurnstile == null) {
            sTurnstile = new Turnstile();
        }
        return sTurnstile;
    }

    public void checkout() {
        if (mOutstandingSavers == 0) {
            for (OnZeroListener listener : mListeners) {
                listener.onNonZero();
            }
        }
        mOutstandingSavers++;
    }

    public void checkin() {
        if (mOutstandingSavers == 0) throw new IllegalStateException("Checking in past 0");
        if (mOutstandingSavers == 1) {
            for (OnZeroListener listener : mListeners) {
                listener.onZero();
            }
        }
        mOutstandingSavers--;
    }

    public boolean isClear() {
        return (mOutstandingSavers==0);
    }

    public void registerListener(OnZeroListener listener) {
        mListeners.add(listener);
        if (isClear()) {
            listener.onZero();
        } else {
            listener.onNonZero();
        }
    }

    public void deregisterListener(OnZeroListener listener){
        mListeners.remove(listener);
    }

    interface OnZeroListener {
        void onZero();

        void onNonZero();
    }
}
