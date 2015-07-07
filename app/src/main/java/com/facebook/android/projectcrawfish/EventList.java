// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventList extends Fragment {

    OnFragmentInteractionListener mListener;
    @Bind(R.id.list_view) ListView mListView;

    interface OnFragmentInteractionListener {
        void createNewEvent();
    }


    public EventList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this,v);

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery<ParseObject> query = new ParseQuery<>(Event.CLASS_NAME);
                        query.orderByAscending(Event.START_DATE);
                        return query;
                    }
                };

        ParseQueryAdapter<ParseObject> mainAdapter = new ParseQueryAdapter<>(getActivity(), factory);
        mListView.setAdapter(mainAdapter);
        mainAdapter.setTextKey(Event.TITLE);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    @OnClick(R.id.new_event_button)
    public void OnCreateButton(View v){
        mListener.createNewEvent();
    }


}
