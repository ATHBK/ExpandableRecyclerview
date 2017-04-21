package com.athbk.expandrecyclerviewsample;

import com.athbk.expandablerecyclerview.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athbk on 4/13/17.
 */

public class ParentModel implements Parent<ChildModel> {

    private String titleParent;
    private List<ChildModel> listChilds;
    private boolean isExpand = false;

    public ParentModel(String titleParent, List<ChildModel> listChilds) {
        this.titleParent = titleParent;
        this.listChilds = listChilds;
    }

    @Override
    public List<ChildModel> getListChild() {
        return listChilds;
    }

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    public String getTitleParent() {
        return titleParent;
    }

    public void setListChilds(List<ChildModel> listChilds) {
        this.listChilds = listChilds;
    }

    public void setTitleParent(String titleParent) {
        this.titleParent = titleParent;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
