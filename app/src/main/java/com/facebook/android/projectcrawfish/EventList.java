// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


abstract class EventList extends Fragment implements CustomViewPQA.ClickEventListener{

    abstract protected ParseQuery<ParseObject> query();
    abstract  protected void onReceiveClickEvent(Event event);
    //abstract protected CustomViewPQA.CustomViewHolder holder();


    CustomViewPQA mAdapter;

    @Bind(R.id.list_view) ListView mListView;

    public void refreshList() {
        mAdapter.loadObjects();
    }



    public EventList() {
        // Required empty public constructor
    }


    @Override
    public void OnClick(ParseObject object) {
        Event event = (Event) object;
        onReceiveClickEvent(event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        return query();
                    }
                };

        mAdapter = new CustomViewPQA(getActivity(), factory, this,
                new CustomViewPQA.CustomViewHolderFactory() {
                    @Override
                    public CustomViewPQA.CustomViewHolder create(View v, CustomViewPQA.CustomViewHolder.ClickEventListenerd listener) {
                        return new EventViewHolder(v, listener);
                    }
                }
                ,R.layout.event_list_item
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this,v);

        mListView.setAdapter(mAdapter);

        return v;
    }

}
