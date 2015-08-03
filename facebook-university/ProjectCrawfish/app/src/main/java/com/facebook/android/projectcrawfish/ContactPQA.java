package com.facebook.android.projectcrawfish;

import android.content.Context;
import android.widget.SectionIndexer;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;


/**
 * Created by markamendoza on 8/3/15.
 */
public class ContactPQA extends CustomViewPQA<ParseUser> implements SectionIndexer, ParseQueryAdapter.OnQueryLoadListener<ParseUser> {
    Map<String,Integer> sectionMap = new HashMap<>();
    List<String> sectionList= new ArrayList<>();

    public ContactPQA(Context context,
                      QueryFactory<ParseUser> queryFactory,
                      ClickEventListener<ParseUser> listener,
                      CustomViewHolderFactory<ParseUser> holderFactory,
                      int viewResource) {
        super(context, queryFactory, listener, holderFactory, viewResource);
        this.addOnQueryLoadListener(this);
    }

    @Override
    public Object[] getSections() {
        return sectionList.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionMap.get(sectionList.get(sectionIndex));
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < sectionList.size()-1; i++) {
            Integer start = sectionMap.get(sectionList.get(i));
            Integer finish = sectionMap.get(sectionList.get(i+1));
            if (position>=start&& position<finish){
                return i;
            }
        }
        return sectionList.size()-1;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(List<ParseUser> list, Exception e) {
        sectionMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            ParseUser user = list.get(i);
            Profile profile = Profile.fromUser(user);
            String lastName = profile.getLastName();
            String index = lastName.substring(0,1);
            index = index.toUpperCase();
            if (sectionMap.get(index)==null){
                sectionMap.put(index,i);
            }
        }

        Set<String> set = sectionMap.keySet();
        sectionList = new ArrayList<>(set);
        Collections.sort(sectionList);
    }
}
