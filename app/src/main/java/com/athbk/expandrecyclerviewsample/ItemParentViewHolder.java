package com.athbk.expandrecyclerviewsample;

import android.view.View;
import android.widget.TextView;

import com.athbk.expandablerecyclerview.ParentViewHolder;

/**
 * Created by athbk on 4/13/17.
 */

public class ItemParentViewHolder extends ParentViewHolder<ParentModel, ChildModel> {

    TextView tvParent;

    public ItemParentViewHolder(View itemView) {
        super(itemView);
        tvParent = (TextView)itemView.findViewById(R.id.tvParent);
    }
}
