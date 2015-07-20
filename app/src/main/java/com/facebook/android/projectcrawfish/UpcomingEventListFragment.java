// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by markamendoza on 7/9/15.
 */
public class UpcomingEventListFragment extends Fragment implements CustomViewPQA.ClickEventListener<Event>{

    private OnFragmentInteractionListener mListener;

    CustomViewPQA<Event> mAdapter;

    @Bind(R.id.list_view)
    ListView mListView;
    @Bind(R.id.progress_switcher)
    ProgressSwitcher mSwitcher;

    public void refreshList() {
        mAdapter.loadObjects();
    }

    @Override
    public void OnClick(Event obj) {
        mListener.openUpcomingEventDetails(obj);
    }

    interface OnFragmentInteractionListener{
        void openUpcomingEventDetails(Event event);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQueryAdapter.QueryFactory<Event> factory =
                new ParseQueryAdapter.QueryFactory<Event>() {
                    public ParseQuery<Event> create() {
                        ParseQuery<Event> query = new ParseQuery<>(Event.CLASS_NAME);
                        query.orderByAscending(Event.START_DATE);
                        query.whereGreaterThan(Event.END_DATE, new Date());//Not over
                        return query;
                    }
                };

        mAdapter = new CustomViewPQA<>(getActivity(), factory, this,
                new CustomViewPQA.CustomViewHolderFactory<Event>() {
                    @Override
                    public CustomViewPQA.CustomViewHolder<Event> create(View v, CustomViewPQA.CustomViewHolder.ClickEventListener<Event> listener) {
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
        ButterKnife.bind(this, v);

        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Event>() {
            @Override
            public void onLoading() {
                mSwitcher.showBar();
            }

            @Override
            public void onLoaded(List<Event> list, Exception e) {
                mSwitcher.showContent();
            }
        });

        mListView.setAdapter(mAdapter);

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


}
