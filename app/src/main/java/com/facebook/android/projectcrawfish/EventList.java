// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


abstract class EventList extends Fragment {

    abstract protected ParseQuery<ParseObject> query();
    abstract  protected void onRecieveClickEvent(Event event);


    EventListAdapter mAdapter;

    @Bind(R.id.list_view) ListView mListView;

    public void refreshList() {
        mAdapter.loadObjects();
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
                        return query();
                    }
                };

        mAdapter = new EventListAdapter(getActivity(), factory,
                new EventListAdapter.ClickEventListener() {
            @Override
            public void OnClick(Event event) {
                onRecieveClickEvent(event);
            }
        });
        mListView.setAdapter(mAdapter);

        return v;
    }

}
