package com.athbk.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by athbk on 4/12/17.
 */

public class ParentViewHolder<P extends Parent<C>, C> extends RecyclerView.ViewHolder implements View.OnClickListener{


    interface ParentVHCollapseExpandListener {

        void parentExpand(int parentPosition);

        void parentCollapse(int parentPosition);
    }

    private ParentVHCollapseExpandListener parentVHCollapseExpandListener;
    private P mParent;
    private boolean isExpand;

    public ParentViewHolder(View itemView) {super(itemView);}

    @Override
    public void onClick(View v) {
        if (isExpand){
            collapseView();
        }
        else {
            expandView();
        }
    }

    private void expandView(){
        setExpand(true);
        if (parentVHCollapseExpandListener == null) return;
        parentVHCollapseExpandListener.parentExpand(getAdapterPosition());
    }

    private void collapseView(){
        setExpand(false);
        if (parentVHCollapseExpandListener == null) return;
        parentVHCollapseExpandListener.parentCollapse(getAdapterPosition());
    }

    public void setParentVHCollapseExpandListener(ParentVHCollapseExpandListener parentVHCollapseExpandListener) {
        this.parentVHCollapseExpandListener = parentVHCollapseExpandListener;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public ParentVHCollapseExpandListener getParentVHCollapseExpandListener() {
        return parentVHCollapseExpandListener;
    }

    public P getParent() {
        return mParent;
    }

    public void setParent(P mParent) {
        this.mParent = mParent;
        this.isExpand = mParent.isExpand();
    }

    public void setParentOnClick(){
        itemView.setOnClickListener(this);
    }
}
