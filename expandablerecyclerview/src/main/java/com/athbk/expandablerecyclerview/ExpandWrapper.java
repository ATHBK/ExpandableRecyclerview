package com.athbk.expandablerecyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athbk on 4/12/17.
 */

public class ExpandWrapper<P extends Parent, C> {

    private P mParent;
    private C mChild;

    private boolean isParent;
    private boolean isExpand;

    private List<ExpandWrapper<P, C>> mExpandWrapperList;


    public ExpandWrapper(P mParent) {
        this.mParent = mParent;
        this.isParent = true;
        getFromParent(mParent);
        this.isExpand = mParent.isExpand();
    }

    public ExpandWrapper(C mChild) {
        this.mChild = mChild;
        this.isParent = false;
        this.isExpand = false;
    }

    private void getFromParent(P mParent){
        this.mExpandWrapperList = new ArrayList<>();
        List<C> childList = mParent.getListChild();
        for (C child : childList){
            mExpandWrapperList.add(new ExpandWrapper<P, C>(child));
        }
    }

    public boolean isParent() {
        return isParent;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public P getParent() {
        return mParent;
    }

    public C getChild() {
        return mChild;
    }

    public List<ExpandWrapper<P, C>> getExpandWrapperList() {
        return mExpandWrapperList;
    }

    public void setExpandWrapperList(List<ExpandWrapper<P, C>> mExpandWrapperList) {
        this.mExpandWrapperList = mExpandWrapperList;
    }

    public void setParent(P mParent) {
        this.mParent = mParent;
        getFromParent(mParent);
    }
}
