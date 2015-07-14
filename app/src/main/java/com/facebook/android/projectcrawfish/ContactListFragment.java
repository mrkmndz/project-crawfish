// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactListFragment extends Fragment implements CustomViewPQA.ClickEventListener {

    public interface OnFragmentInteractionListener {
        void openContactDetails(Profile profile);
    }

    public void refreshList() {
        mAdapter.loadObjects();
    }

    private CustomViewPQA mAdapter;
    private OnFragmentInteractionListener mListener;

    @Bind(R.id.list_view)
    protected ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery<ParseObject> query = new ParseQuery<>(Profile.CLASS_NAME);
                        query.orderByDescending(Profile.LAST_NAME);
                        return query;
                    }
                };

        mAdapter = new CustomViewPQA(getActivity(), factory, this,
                new CustomViewPQA.CustomViewHolderFactory() {
                    @Override
                    public CustomViewPQA.CustomViewHolder create(View v, CustomViewPQA.CustomViewHolder.ClickEventListenerd listener) {
                        return new ContactViewHolder(v, listener);
                    }
                }
                , R.layout.list_item_contact);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void OnClick(ParseObject obj) {
        Profile profile = (Profile) obj;
        mListener.openContactDetails(profile);
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
