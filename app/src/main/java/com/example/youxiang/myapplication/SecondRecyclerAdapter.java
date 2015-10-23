package com.example.youxiang.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder>{
	private LayoutInflater mInflater;
	private List<Integer> mDatas;
    private Context context;

	public SecondRecyclerAdapter(Context context, List<Integer> datats)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);  
        mDatas = datats;
        setHasStableIds(true);
    }

    public void reloadData(List<Integer> datats){
        mDatas = datats;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {  
        public ViewHolder(View arg0)  
        {  
            super(arg0);  
        }  
  
        ImageView mImg;  
        TextView mTxt;  
    }

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.DRAWABLE.wrap("R.drawable.icon_home_welcome_1"),viewHolder.mImg);
        ImageLoader.getInstance().displayImage("drawable://" + mDatas.get(i),viewHolder.mImg);
        Log.i("onBindViewHolder", i + "");
        viewHolder.mTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MainActivity) context).removeItem(i);
            }
        });
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mOnItemClickLitener.onItemLongClick(view,i);
                }
            });
        }
//		viewHolder.mImg.setImageResource(mDatas.get(i));


	}



	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        Log.i("onCreateViewHolder",i+"");
        final View view = mInflater.inflate(R.layout.main_recycler_view_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);

        return viewHolder;
	}  
	
	/**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        boolean onItemLongClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

