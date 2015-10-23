package com.example.youxiang.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youxiang.myapplication.helper.ItemTouchHelperAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Collections;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter{
	private LayoutInflater mInflater;
	private List<MainActivity.Model> mDatas;
    private Context context;
    private Handler handler = new Handler();
    private DisplayImageOptions options;

    public MainRecyclerAdapter(Context context, List<MainActivity.Model> datats)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);  
        mDatas = datats;
//        setHasStableIds(true);
    }

    public void reloadData(List<MainActivity.Model> datats){
        mDatas = datats;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {  

  
        public ImageView mImg;
        public TextView mTxt;

        public ViewHolder(View viewHolder,final OnItemClickLitener mOnItemClickLitener)
        {
            super(viewHolder);
            mImg = (ImageView) viewHolder.findViewById(R.id.id_index_gallery_item_image);
            mTxt = (TextView) viewHolder.findViewById(R.id.id_index_gallery_item_text);

            if (mOnItemClickLitener != null)
            {
                viewHolder.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onItemClick(view, getAdapterPosition());
                    }
                });
                viewHolder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mOnItemClickLitener.onItemLongClick(view, getAdapterPosition());
                    }
                });
                mTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onItemTextClick(view,getAdapterPosition());
                    }
                });
            }
        }


    }

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.DRAWABLE.wrap("R.drawable.icon_home_welcome_1"),viewHolder.mImg);
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    .build();// 构建完成
        }
        ImageLoader.getInstance().displayImage("drawable://" + mDatas.get(i).i, viewHolder.mImg, options);
        Log.i("onBindViewHolder", i + "");

//		viewHolder.mImg.setImageResource(mDatas.get(i));


	}



	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        Log.i("onCreateViewHolder",i+"");
        View view = mInflater.inflate(R.layout.main_recycler_view_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view ,mOnItemClickLitener);
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
        boolean onItemLongClick(View view,int position);
        void onItemTextClick(View view,int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

//    @Override
//    public long getItemId(int position) {
//        Log.i("getItemId",position +"");
////        return mDatas.get(position).id;
//        return position;
//    }
}

