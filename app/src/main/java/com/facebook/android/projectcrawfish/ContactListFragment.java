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

abstract class ContactListFragment extends Fragment implements CustomViewPQA.ClickEventListener {

    abstract protected ParseQuery<ParseObject> query();

    abstract protected void onReceiveClickContact(Contact contact);

    CustomViewPQA mAdapter;

    @Bind(R.id.list_view)
    ListView mListView;

    public void refreshList() {
        mAdapter.loadObjects();
    }

    public ContactListFragment() {
        // Required empty public constructor
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
        Contact contact = (Contact) obj;
        onReceiveClickContact(contact);
    }
}
