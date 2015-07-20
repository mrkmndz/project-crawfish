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
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PastEventList extends Fragment implements CustomViewPQA.ClickEventListener<Attendance> {

    private OnFragmentInteractionListener mListener;

    CustomViewPQA<Attendance> mAdapter;

    @Bind(R.id.list_view)
    ListView mListView;

    @Bind(R.id.progress_switcher)
    ProgressSwitcher mSwitcher;

    public void refreshList() {
        mAdapter.loadObjects();
    }

    @Override
    public void OnClick(Attendance attendance) {
        mListener.openPastEventDetails(attendance);
    }

    interface OnFragmentInteractionListener {
        void openPastEventDetails(Attendance attendance);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQueryAdapter.QueryFactory<Attendance> factory =
                new ParseQueryAdapter.QueryFactory<Attendance>() {
                    public ParseQuery<Attendance> create() {
                        ParseQuery<Attendance> query = new ParseQuery<Attendance>(Attendance.CLASS_NAME);
                        query.whereEqualTo(Attendance.USER, ParseUser.getCurrentUser());
                        query.include(Attendance.EVENT);
                        return query;
                    }
                };

        mAdapter = new CustomViewPQA<>(getActivity(), factory, this,
                new CustomViewPQA.CustomViewHolderFactory<Attendance>() {
                    @Override
                    public CustomViewPQA.CustomViewHolder<Attendance> create(View v, CustomViewPQA.CustomViewHolder.ClickEventListener<Attendance> listener) {
                        return new AttendanceViewHolder(v, listener);
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


        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Attendance>() {
            @Override
            public void onLoading() {
                mSwitcher.showBar();
            }

            @Override
            public void onLoaded(List<Attendance> list, Exception e) {
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
        mListener = null;
    }
}
