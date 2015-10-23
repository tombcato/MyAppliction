package com.example.youxiang.myapplication.headerfooterrecyclerview;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;


/**
 * Created by Will on 2/8/2015.
 */
public interface IRecyclerViewIntermediary {
    public int getItemCount();
    public Object getItem(int position);
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int type);
    public int getItemViewType(int position);
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position);
    public boolean onItemMove(int fromPosition, int toPosition);
    public void onItemDismiss(int position);
    public Animator[] getAnimators(View view);
    public Interpolator getInterpolator();
    public int getDuration();
    public boolean isFirstOnly();
}
