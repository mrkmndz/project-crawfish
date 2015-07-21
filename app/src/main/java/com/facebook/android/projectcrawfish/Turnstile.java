// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by markamendoza on 7/21/15.
 */
public class Turnstile {
    private static Turnstile sTurnstile;
    private Map<Event,Integer> mOutstandingSavers;
    private Map<Event, List<OnZeroListener> > mListeners;
    private Turnstile(){
        this.mOutstandingSavers = new HashMap<>();
        this.mListeners = new HashMap<>();
    }

    public static Turnstile get(){
        if (sTurnstile == null){
            sTurnstile = new Turnstile();
        }
        return sTurnstile;
    }

    public void checkout(Event event){
        Integer old = mOutstandingSavers.get(event);
        if (old==null){
            old = 0;
            List<OnZeroListener> listeners = mListeners.get(event);
            if (listeners!=null){
                for (OnZeroListener listener : listeners){
                    listener.onNonZero();
                }
            }
        }
        mOutstandingSavers.put(event, old+ 1);
    }

    public void checkin(Event event){
        Integer old = mOutstandingSavers.get(event);
        if (old==null) throw new IllegalStateException("Checking in past 0");
        mOutstandingSavers.put(event, old-1);
        if (old.equals(1)){
            List<OnZeroListener> listeners = mListeners.get(event);
            if (listeners!=null){
                for (OnZeroListener listener : listeners){
                    listener.onZero();
                }
            }
        }
    }

    public boolean isClear(Event event){
        Integer old = mOutstandingSavers.get(event);
        return (old==null||old.equals(0));
    }

    public void registerListener(OnZeroListener listener, Event event){
        List<OnZeroListener> listeners = mListeners.get(event);
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
        mListeners.put(event,listeners);
        if (isClear(event)){
            listener.onZero();
        }else {
            listener.onNonZero();
        }
    }

    interface OnZeroListener{
        void onZero();
        void onNonZero();
    }
}
