package com.athbk.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by athbk on 4/12/17.
 */

public class ChildViewHolder<C> extends RecyclerView.ViewHolder{

    private C mChild;

    public ChildViewHolder(View itemView) {
        super(itemView);
    }

    public C getChild() {
        return mChild;
    }

    public void setChild(C child) {
        this.mChild = child;
    }
}
