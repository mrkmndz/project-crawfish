// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;// Copyright 2004-present Facebook. All Rights Reserved.

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

public class ContactListFragment extends Fragment implements CustomViewPQA.ClickEventListener{
    CustomViewPQA mAdapter;

    @Bind(R.id.list_view)
    ListView mListView;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, v);

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        return new ParseQuery<>(Contact.CLASS_NAME);
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
        mListView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void OnClick(ParseObject obj) {
        //nothing
    }
}
