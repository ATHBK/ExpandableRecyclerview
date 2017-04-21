package com.athbk.expandrecyclerviewsample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.athbk.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import java.util.List;

/**
 * Created by athbk on 4/13/17.
 */

public class Adapter extends ExpandableRecyclerViewAdapter<ParentModel, ChildModel, ItemParentViewHolder, ItemChildViewHolder> {

    List<ParentModel> mParentList;

    public Adapter(List<ParentModel> mParentList) {
        super(mParentList);
        this.mParentList = mParentList;
    }

    @Override
    protected ItemParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemParentViewHolder(view);
    }

    @Override
    protected ItemChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        return new ItemChildViewHolder(view);
    }

    @Override
    protected void onBindParentViewHolder(ItemParentViewHolder parentHolder, int parentPosition, ParentModel mParent) {
//        Log.e("TAG", ""+parentPosition);
        parentHolder.tvParent.setText(mParentList.get(parentPosition).getTitleParent());
    }

    @Override
    protected void onBindChildViewHolder(ItemChildViewHolder childHolder, int parentPosition, int childPosition, ChildModel children) {
        ParentModel parentModel = mParentList.get(parentPosition);
        ChildModel childModel = parentModel.getListChild().get(childPosition);
        childHolder.tvChild.setText(childModel.getTitle());
        childHolder.tvChild2.setText(childModel.getTitle());
    }



}
