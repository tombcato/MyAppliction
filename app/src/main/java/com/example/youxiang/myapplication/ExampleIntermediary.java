package com.example.youxiang.myapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.youxiang.myapplication.headerfooterrecyclerview.IRecyclerViewIntermediary;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Will on 2/8/2015.
 */
public class ExampleIntermediary implements IRecyclerViewIntermediary  {

    private Context context;
    private List<MainActivity.Model> mItems;
    private LayoutInflater mInflater;

    public ExampleIntermediary(Context context,List<MainActivity.Model> items){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mItems=items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int type) {
        View view = mInflater.inflate(R.layout.main_recycler_view_item,viewGroup, false);
        TestViewHolder viewHolder = new TestViewHolder(view ,mOnItemClickLitener);
        return viewHolder;
//        switch (type){
//            case 1:
//                v = View.inflate(viewGroup.getContext(), R.layout.item_string_1, null);
//                break;
//            case 2:
//                v = View.inflate(viewGroup.getContext(), R.layout.item_string_2, null);
//                break;
//            case 3:
//                v = View.inflate(viewGroup.getContext(), R.layout.item_string_3, null);
//                break;
//            default:
//                v = View.inflate(viewGroup.getContext(), R.layout.item_string_4, null);
//                break;
//        }
//        return new TestViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return 100;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        Uri uri = Uri.parse("res://com.example.youxiang.myapplication/" + mItems.get(position).i);
        ((TestViewHolder) viewHolder).mImg.setImageURI(uri);
        ((TestViewHolder) viewHolder).mTxt.setText(mItems.get(position).id + "=======");
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
    }


    private class TestViewHolder extends RecyclerView.ViewHolder{
        TextView mTxt;
        SimpleDraweeView mImg;
        public TestViewHolder(View viewHolder, OnItemClickLitener mOnItemClickLitener) {
            super(viewHolder);

            mImg = (SimpleDraweeView) viewHolder.findViewById(R.id.id_index_gallery_item_image);
            mTxt = (TextView) viewHolder.findViewById(R.id.id_index_gallery_item_text);

            if (ExampleIntermediary.this.mOnItemClickLitener != null)
            {
                viewHolder.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ExampleIntermediary.this.mOnItemClickLitener.onItemClick(view, getAdapterPosition());
                    }
                });
                viewHolder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return ExampleIntermediary.this.mOnItemClickLitener.onItemLongClick(view, getAdapterPosition());
                    }
                });
                mTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExampleIntermediary.this.mOnItemClickLitener.onItemTextClick(view, getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[] { ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f) ,
                ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f)};
    }

    @Override
    public Interpolator getInterpolator() {
        return new LinearInterpolator();
    }

    @Override
    public int getDuration() {
        return 500;
    }

    @Override
    public boolean isFirstOnly() {
        return true;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        boolean onItemLongClick(View view,int position);
        void onItemTextClick(View view,int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
