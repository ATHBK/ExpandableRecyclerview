package com.athbk.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athbk on 4/12/17.
 */

public abstract class ExpandableRecyclerViewAdapter<P extends Parent<C>, C, PVH extends ParentViewHolder<P, C>, CVH extends ChildViewHolder<C>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     *
     * @param parent : ViewGroup
     * @return ViewHolder class extend ParentViewHolder
     */
    protected abstract PVH onCreateParentViewHolder(ViewGroup parent);

    /**
     *
     * @param parent : ViewGroup
     * @return ViewHolder class extend ChildViewHolder
     */
    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);

    /**
     *
     * @param parentHolder : ParentViewHolder
     * @param parentPosition : position of parent in parent list
     * @param mParent : Object Parent
     */
    protected abstract void onBindParentViewHolder(PVH parentHolder, int parentPosition, P mParent);

    /**
     *
     * @param childHolder : ChildViewHolder
     * @param parentPosition : position of parent in parent list
     * @param childPosition : position of children in children list
     * @param children : Object Children
     */
    protected abstract void onBindChildViewHolder(CVH childHolder, int parentPosition, int childPosition, C children);


    private ParentViewHolder.ParentVHCollapseExpandListener parentVHCollapseExpandListener = new ParentViewHolder.ParentVHCollapseExpandListener() {
        @Override
        public void parentExpand(int parentPosition) {
            updateExpandParent(parentPosition);
        }

        @Override
        public void parentCollapse(int parentPosition) {
            updateCollapseParent(parentPosition);
        }
    };

    private final int TYPE_PARENT = 0;
    private final int TYPE_CHILD = 1;

    private List<P> mParentList;
    private List<ExpandWrapper<P, C>> mItemList;

    public ExpandableRecyclerViewAdapter(List<P> mParentList) {
        super();
        this.mParentList = mParentList;
        this.mItemList = getListFromParent(mParentList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT){
            PVH parentViewHolder = onCreateParentViewHolder(parent);
            return parentViewHolder;
        }
        else {
            CVH childViewHolder = onCreateChildViewHolder(parent);
            return childViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandWrapper<P, C> expandWrapper = mItemList.get(position);
        if (expandWrapper.isParent()){
            PVH parentHolder = (PVH)holder;
            parentHolder.setParent(expandWrapper.getParent());
            parentHolder.setParentVHCollapseExpandListener(parentVHCollapseExpandListener);
            parentHolder.setParentOnClick();
            parentHolder.setParent(expandWrapper.getParent());
            onBindParentViewHolder(parentHolder, getNearParentPositionInListParent(position), expandWrapper.getParent());
        }
        else {
            CVH childHolder = (CVH)holder;
            childHolder.setChild(expandWrapper.getChild());
            onBindChildViewHolder(childHolder, getNearParentPositionInListParent(position), getChildPositionInParent(position), expandWrapper.getChild());
        }
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) return 0;
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ExpandWrapper<P, C> expandWrapper = mItemList.get(position);
        if (expandWrapper.isParent()){
            return TYPE_PARENT;
        }
        else {
            return TYPE_CHILD;
        }
    }

    /**
     *
     * @param mParentList
     * @return List of ExpandWrapper - Data of RecyclerView.
     */
    private List<ExpandWrapper<P, C>> getListFromParent(List<P> mParentList){
        List<ExpandWrapper<P, C>> mList = new ArrayList<>();
        for (int i=0; i<mParentList.size(); i++){
            P mParent = mParentList.get(i);
            ExpandWrapper<P, C> expandWrapperParent = new ExpandWrapper<P, C>(mParent);
            mList.add(expandWrapperParent);
            if (expandWrapperParent.isExpand()){
                getListFromChild(mList, mParent);
            }
        }
        return mList;
    }

    /**
     *
     * @param mItemList : List of ExpandWrapper.
     * @param mParent : Parent
     */
    private void getListFromChild(List<ExpandWrapper<P, C>> mItemList, P mParent){
        List<C> mListChild = mParent.getListChild();
        if (mListChild == null) return;
        for (int i=0; i< mListChild.size(); i++){
            ExpandWrapper<P, C> expandWrapperChild = new ExpandWrapper<P, C>(mListChild.get(i));
            mItemList.add(expandWrapperChild);
        }
    }

    /**
     *
     * @param position : position in List<ExpandWrapper>. Actual position in recyclerview
     */
    private void updateExpandParent(int position){
        ExpandWrapper<P,C> mParentWrapper = mItemList.get(position);
        if (!mParentWrapper.isParent()) return;
        if (mParentWrapper.isExpand()) return;

        mParentWrapper.setExpand(true);

        List<ExpandWrapper<P, C>> listExpandChild = mParentWrapper.getExpandWrapperList();
        int count = listExpandChild.size();
        for (int i=0; i<count; i++){
            mItemList.add(position + i + 1, listExpandChild.get(i));
        }
        notifyItemRangeInserted(position + 1, count);
    }

    /**
     *   Expand All Parent
     */
    public void updateExpandAll(){
        mItemList = new ArrayList<>();
        for (int i=0; i<mParentList.size(); i++){
            P parent = mParentList.get(i);
            ExpandWrapper<P, C> parentWrapper = new ExpandWrapper<P, C>(parent);
            parentWrapper.setExpand(true);
            mItemList.add(parentWrapper);
            mItemList.addAll(parentWrapper.getExpandWrapperList());
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param position : position in List<ExpandWrapper>. Actual position in recyclerview
     */
    private void updateCollapseParent(int position){
        ExpandWrapper<P,C> mParentWrapper = mItemList.get(position);
        if (!mParentWrapper.isParent()) return;
        if (!mParentWrapper.isExpand()) return;

        mParentWrapper.setExpand(false);
        List<ExpandWrapper<P, C>> listExpandChild = mParentWrapper.getExpandWrapperList();
        int count = listExpandChild.size();
        for (int i = 0; i < count; i++){
            mItemList.remove(position + 1);
        }
        notifyItemRangeRemoved(position + 1, count);
    }

    /**
     *  Collapse All Parent
     */
    public void updateCollapseAll(){
        mItemList = new ArrayList<>();

        for (int i=0; i<mParentList.size(); i++){
            P parent = mParentList.get(i);
            ExpandWrapper<P, C> parentWrapper = new ExpandWrapper<P, C>(parent);
            parentWrapper.setExpand(false);
            mItemList.add(parentWrapper);
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param parentPosition : position in parent list
     * @return Actual position in recyclerView
     */
    private int getParentPositionInListItem(int parentPosition){
        if (parentPosition == 0){
            return 0;
        }

        int parentNumber = 0;
        for (int i=0; i< mItemList.size(); i++){
            if (mItemList.get(i).isParent()){
                parentNumber++;
                if (parentNumber > parentPosition){
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     *
     * @param position : Actual position in recyclerView
     * @return position of near parent
     */
    private int getNearParentPositionInListParent(int position){
        if (position == 0){
            return 0;
        }

        int flatPos = -1;

        for (int i = 0; i<=position; i++){
            ExpandWrapper<P, C> expandWrapper = mItemList.get(i);
            if (expandWrapper.isParent()){
                flatPos++;
            }
        }
        return flatPos;
    }

    /**
     *
     * @param position : actual position in recyclerview
     * @return position of list child.
     */
    private int getChildPositionInParent(int position){
        if (position == 0){
            return 0;
        }
        int flatPos = 0;
        for (int i = 0; i < position; i++){
            ExpandWrapper<P, C> expandWrapper = mItemList.get(i);
            if (expandWrapper.isParent()){
                flatPos = 0;
            }
            else {
                flatPos++;
            }
        }
        return flatPos;
    }


    /**
     *  update recyclerview when insert a parent
     *
     * @param position : position insert in list parent
     */
    public void notifyParentInsert(int position){
        P parent = mParentList.get(position);

        int parentPositionWrapper = 0;
        if (position <= mItemList.size() - 1){
            parentPositionWrapper = getParentPositionInListItem(position);
        }
        else {
            parentPositionWrapper = mItemList.size();
        }

        int range = addParentWrapper(parentPositionWrapper, parent);
        notifyItemRangeInserted(parentPositionWrapper, range);
    }

    /**
     *  update recyclerview when insert more than 1 parent.
     *
     * @param startPosition : position insert in list parent
     * @param count : number insert
     */
    public void notifyParentRangeInsert(int startPosition, int count){
        int startParentPositionWrapper;
        int parentPositionWrapper;
        int rangeWrapper = 0;
        P parent;

        if (startPosition  <= mItemList.size() - 1){
            startParentPositionWrapper = getParentPositionInListItem(startPosition);
        }
        else {
            startParentPositionWrapper = mItemList.size();
        }

        parentPositionWrapper = startParentPositionWrapper;
        for (int i=0; i<count; i++){
            parent = mParentList.get(startPosition);
            rangeWrapper += addParentWrapper(parentPositionWrapper, parent);
            parentPositionWrapper++;
            startPosition++;
        }

        notifyItemRangeInserted(startParentPositionWrapper, rangeWrapper);
    }

    /**
     *  Add parent into list expandWrapper.
     * @param position : position inseart in list wrapper
     * @param parent : Parent
     * @return : count (parent + child if parent will expand.)
     */
    private int addParentWrapper(int position, P parent){
        int range = 1;

        ExpandWrapper<P, C> parentWrapper = new ExpandWrapper<P, C>(parent);
        mItemList.add(position, parentWrapper);
        if (parentWrapper.isExpand()){
            List<ExpandWrapper<P, C>> childWrapper = parentWrapper.getExpandWrapperList();
            for (int i=0; i<childWrapper.size(); i++){
                mItemList.add(position+1+i, parentWrapper);
                range++;
            }
        }
        return range;
    }

    /**
     *  Update recyclerview when remove a parent
     *
     * @param position : position of parent in list parent
     */
    public void notifyParentRemove(int position){
        int initParentPosition = getParentPositionInListItem(position);
        int size = removeParentWrapper(position);
        notifyItemRangeRemoved(initParentPosition, size);
    }

    /**
     *  Update recyclerview when remove more than 1 parent
     *
     * @param startPosition : start position remove in list parent
     * @param count : number need to parent remove.
     */
    public void notifyParentRangeRemove(int startPosition, int count){
        int initParentPosition = getParentPositionInListItem(startPosition);
        int size = 0;
        for (int i = 0; i< count; i++){
            size += removeParentWrapper(startPosition);
        }
        notifyItemRangeRemoved(initParentPosition, size);
    }

    /**
     *  Remove parent in list wrapper
     * @param position : actual position of parent in list wrapper.
     * @return count remove.
     */
    private int removeParentWrapper(int position){
        int initParentPosition = getParentPositionInListItem(position);
        int range = 1;
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        if (parentWrapper.isExpand()){
            List<ExpandWrapper<P, C>> childWrappers = parentWrapper.getExpandWrapperList();
            int sizeChild = childWrappers.size();
            for (int i=0; i< sizeChild; i++){
                mItemList.remove(initParentPosition+1);
                range++;
            }
        }
        mItemList.remove(initParentPosition);
        return range;
    }

    /**
     *  Update recyclerview when change content a parent
     * @param position : position of parent in list parent
     */
    public void notifyParentChange(int position){
        int initParentPosition = getParentPositionInListItem(position);
        ExpandWrapper<P, C> expandWrapper = mItemList.get(initParentPosition);
        if (!expandWrapper.isParent()) return;

        int size = 1;
        if (expandWrapper.isExpand()){
            List<ExpandWrapper<P, C>> listChild = expandWrapper.getExpandWrapperList();
            size += listChild.size();
        }
        notifyItemRangeChanged(initParentPosition, size);
    }

    /**
     *  Update recyclerview when change more than 1 parent.
     * @param startPosition : start position of parent in list parent
     * @param count : number need to parent change.
     */
    public void notifyParentRangeChange(int startPosition, int count){
        int initParentPosition = 0;
        int size = count;
        for (int i=0; i<count; i++){
            initParentPosition = getParentPositionInListItem(startPosition + i);
            ExpandWrapper<P, C> expandWrapper = mItemList.get(initParentPosition);
            if (!expandWrapper.isParent()) return;
            if (expandWrapper.isExpand()){
                List<ExpandWrapper<P, C>> listChild = expandWrapper.getExpandWrapperList();
                size += listChild.size();
            }
        }

        notifyItemRangeChanged(getParentPositionInListItem(startPosition), size);
    }

    /**
     *  Update recyclerview when insert a child
     *
     * @param parentPosition : position of parent in list parent
     * @param childPosition : position of child in list child
     */
    public void notifyChildInsert(int parentPosition, int childPosition){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        parentWrapper.setParent(mParentList.get(parentPosition));
        mItemList.set(initParentPosition, parentWrapper);

        if (parentWrapper.isExpand()) {
            ExpandWrapper<P, C> child = parentWrapper.getExpandWrapperList().get(childPosition);
            mItemList.add(initParentPosition + childPosition + 1, child);
            notifyItemInserted(initParentPosition + childPosition + 1);
        }
    }

    /**
     *  Update recyclerview when insert more than 1 child
     *
     * @param parentPosition : position of parent in list parent
     * @param startChildPosition : position of child in list child
     * @param count : number need to child insert
     */
    public void notifyChildRangeInsert(int parentPosition, int startChildPosition, int count){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        P parent = mParentList.get(parentPosition);
        parentWrapper.setParent(parent);

        mItemList.set(initParentPosition, parentWrapper);
        if (parentWrapper.isExpand()){
            for (int i=0; i<count; i++){
                ExpandWrapper<P, C> childWrapper = parentWrapper.getExpandWrapperList().get(startChildPosition + i);
                mItemList.add(initParentPosition + startChildPosition + 1, childWrapper);
            }

            notifyItemRangeInserted(initParentPosition + startChildPosition + 1, count);
        }
    }

    /**
     *  Update recyclerview when remove a child
     *
     * @param parentPosition : position of parent in list parent
     * @param childPosition : position of child in list child
     */
    public void notifyChildRemove(int parentPosition, int childPosition){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);

        parentWrapper.setParent(mParentList.get(parentPosition));
        mItemList.set(initParentPosition, parentWrapper);

        if (parentWrapper.isExpand()){
            mItemList.remove(initParentPosition + childPosition + 1);
            notifyItemRemoved(initParentPosition + childPosition + 1);
        }
    }

    /**
     *  Update recyclerview when remove more than 1 child
     *
     * @param parentPosition : position of parent in list parent
     * @param startChildPosition : start position of child in list child
     * @param count : number need to child remove
     */
    public void notifyChildRangeRemove(int parentPosition, int startChildPosition, int count){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        parentWrapper.setParent(mParentList.get(parentPosition));
        mItemList.set(initParentPosition, parentWrapper);

        if (parentWrapper.isExpand()){
            for (int i=0; i<count; i++){
                mItemList.remove(initParentPosition + startChildPosition + 1);
            }
            notifyItemRangeRemoved(initParentPosition + startChildPosition + 1, count);
        }
    }

    /**
     *  Update recyclerview when change a child
     *
     * @param parentPosition : position of parent in list parent
     * @param childPosition : position of child in list child
     */
    public void notifyChildChange(int parentPosition, int childPosition){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        parentWrapper.setParent(mParentList.get(parentPosition));
        mItemList.set(initParentPosition, parentWrapper);
        if (parentWrapper.isExpand()){
            notifyItemChanged(initParentPosition + childPosition + 1);
        }
    }

    /**
     *  Update recyclerview when change more child.
     *
     * @param parentPosition : position of parent in list parent
     * @param startChildPosition : start position of child in list child
     * @param count : number need to child change.
     */
    public void notifyChildRangeChange(int parentPosition, int startChildPosition, int count){
        int initParentPosition = getParentPositionInListItem(parentPosition);
        ExpandWrapper<P, C> parentWrapper = mItemList.get(initParentPosition);
        parentWrapper.setParent(mParentList.get(parentPosition));
        mItemList.set(initParentPosition, parentWrapper);
        if (parentWrapper.isExpand()){
            notifyItemRangeChanged(initParentPosition + startChildPosition + 1, count);
        }
    }

}
