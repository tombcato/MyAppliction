package com.example.youxiang.myapplication.headerfooterrecyclerview;

import android.animation.Animator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.example.youxiang.myapplication.animators.internal.ViewHelper;
import com.example.youxiang.myapplication.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 2/8/2015.
 */
public class RecyclerViewHeaderFooterAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    public static final int TYPE_MANAGER_OTHER = 0;
    public static final int TYPE_MANAGER_LINEAR = 1;
    public static final int TYPE_MANAGER_GRID = 2;
    public static final int TYPE_MANAGER_STAGGERED_GRID = 3;


    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_FOOTER = 7899;

    private List<View> mHeaders = new ArrayList<>();
    private List<View> mFooters = new ArrayList<>();

    private int mManagerType;
    private RecyclerView.LayoutManager mManager;
    private IRecyclerViewIntermediary mIntermediary;

    /**
     * 动画相关参数
     */
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean isFirstOnly = true;

    public RecyclerViewHeaderFooterAdapter(RecyclerView.LayoutManager manager, IRecyclerViewIntermediary intermediary){
        setManager(manager);
        this.mIntermediary = intermediary;
    }

    public int getHeaderSize(){
        return mHeaders.size();
    }
    public int getFooterSize(){
        return mFooters.size();
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager){
        setManager(manager);
    }

    private void setManager(RecyclerView.LayoutManager manager){
        mManager = manager;
        if(mManager instanceof GridLayoutManager){
            mManagerType = TYPE_MANAGER_GRID;
            ((GridLayoutManager) mManager).setSpanSizeLookup(mSpanSizeLookup);
        }else if(mManager instanceof LinearLayoutManager){
            mManagerType = TYPE_MANAGER_LINEAR;
        }else if(mManager instanceof StaggeredGridLayoutManager){
            mManagerType = TYPE_MANAGER_STAGGERED_GRID;
            ((StaggeredGridLayoutManager) mManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }else{
            mManagerType = TYPE_MANAGER_OTHER;
        }
    }

    public int getManagerType(){
        return mManagerType;
    }

    public int getGridSpan(int position){
        if(isHeader(position) || isFooter(position)){
            return getSpan();
        }
        position -= mHeaders.size();
        if(mIntermediary.getItem(position) instanceof IGridItem){
            return ((IGridItem) mIntermediary.getItem(position)).getGridSpan();
        }
        return 1;
    }

    private int getSpan(){
        if(mManager instanceof GridLayoutManager){
            return ((GridLayoutManager) mManager).getSpanCount();
        }else if(mManager instanceof StaggeredGridLayoutManager){
            return ((StaggeredGridLayoutManager) mManager).getSpanCount();
        }
        return 1;
    }

    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup(){
        @Override
        public int getSpanSize(int position) {
            return getGridSpan(position);
        }
    };


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if(type !=  TYPE_HEADER && type != TYPE_FOOTER) {
            return mIntermediary.getViewHolder(viewGroup, type);
            //else we have a header/footer
        }else{
            //create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new HeaderFooterViewHolder(frameLayout);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if(isHeader(position)){
            View v = mHeaders.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        }else if(isFooter(position)){
            View v = mFooters.get(position-mIntermediary.getItemCount()-mHeaders.size());
            //add our view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        }else {
            //it's one of our items, display as required
            mIntermediary.populateViewHolder(vh, position-mHeaders.size());
        }

        //ITEM ANIM
        if (!mIntermediary.isFirstOnly() || position > mLastPosition) {
            for (Animator anim : mIntermediary.getAnimators(vh.itemView)) {
                anim.setDuration(mIntermediary.getDuration()).start();
                anim.setInterpolator(mIntermediary.getInterpolator());
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(vh.itemView);
        }
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view){

        //if it's a staggered grid, span the whole layout
        if(mManagerType == TYPE_MANAGER_STAGGERED_GRID){
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            vh.itemView.setLayoutParams(layoutParams);
        }

        //if the view already belongs to another layout, remove it
        if(view.getParent() != null){
            ((ViewGroup)view.getParent()).removeView(view);
        }

        //empty out our FrameLayout and replace with our header/footer
        vh.base.removeAllViews();
        vh.base.addView(view);

    }

    public boolean isHeader(int position){
        return(position < mHeaders.size());
    }
    public boolean isFooter(int position){
        return(position >= mHeaders.size() + mIntermediary.getItemCount());
    }



    @Override
    public int getItemCount() {
        return mHeaders.size() + mIntermediary.getItemCount() + mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        //check what type our position is, based on the assumption that the order is headers > items > footers
        if(isHeader(position)){
            return TYPE_HEADER;
        }else if(isFooter(position)){
            return TYPE_FOOTER;
        }
        int type = mIntermediary.getItemViewType(position);
        if(type == TYPE_HEADER || type == TYPE_FOOTER){
            throw new IllegalArgumentException("Item type cannot equal "+TYPE_HEADER+" or "+TYPE_FOOTER);
        }
        return type;
    }

    //add a header to the adapter
    public void addHeader(View header){
        if(!mHeaders.contains(header)){
            mHeaders.add(header);
            //animate
            notifyItemInserted(mHeaders.size()-1);
        }
    }

    //remove a header from the adapter
    public void removeHeader(View header){
        if(mHeaders.contains(header)){
            //animate
            notifyItemRemoved(mHeaders.indexOf(header));
            mHeaders.remove(header);
        }
    }

    //add a footer to the adapter
    public void addFooter(View footer){
        if(!mFooters.contains(footer)){
            mFooters.add(footer);
            //animate
            notifyItemInserted(mHeaders.size()+mIntermediary.getItemCount()+mFooters.size()-1);
        }
    }

    //remove a footer from the adapter
    public void removeFooter(View footer){
        if(mFooters.contains(footer)) {
            //animate
            notifyItemRemoved(mHeaders.size()+mIntermediary.getItemCount()+mFooters.indexOf(footer));
            mFooters.remove(footer);
        }
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder{
        FrameLayout base;
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
            base = (FrameLayout) itemView;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mItems, fromPosition, toPosition);
        if(mIntermediary != null){
            if(isHeader(fromPosition) || isFooter(fromPosition)){
                return false;
            }
            if(mIntermediary.onItemMove(fromPosition,toPosition)){
                mIntermediary.onItemMove(fromPosition - mHeaders.size(),toPosition - mHeaders.size());
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
//        .remove(position);
        if(mIntermediary != null){
            if(isHeader(position) || isFooter(position)) {
                //nothing
            }else{
                mIntermediary.onItemDismiss(position - mHeaders.size());
                notifyItemRemoved(position);
            }
        }

    }
}
