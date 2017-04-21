package com.athbk.expandrecyclerviewsample;

import android.view.View;
import android.widget.TextView;

import com.athbk.expandablerecyclerview.ChildViewHolder;

/**
 * Created by athbk on 4/13/17.
 */

public class ItemChildViewHolder extends ChildViewHolder<ChildModel> {

    TextView tvChild;
    TextView tvChild2;

    public ItemChildViewHolder(View itemView) {
        super(itemView);

        tvChild = (TextView)itemView.findViewById(R.id.tvChild);
        tvChild2 = (TextView)itemView.findViewById(R.id.tvChild2);
    }
}
