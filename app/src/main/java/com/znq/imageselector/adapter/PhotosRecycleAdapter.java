package com.znq.imageselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.znq.imageselector.R;
import com.znq.imageselector.activity.SelectPhotosActivity;
import com.znq.imageselector.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PhotosRecycleAdapter extends RecyclerView.Adapter<PhotosRecycleAdapter.PhotosViewHodle> {

    private int screenWidth = 0;
    private Context context;
    private LayoutInflater inflater;
    private List<String> list = new ArrayList<>();
    private SelectPhotosActivity.myCallBack myCallBack;

    public PhotosRecycleAdapter(Context context, SelectPhotosActivity.myCallBack myCallBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        screenWidth = Utils.getScreenWidth();
        this.myCallBack = myCallBack;
        screenWidth = (screenWidth - Utils.dp2px(context, 20)) / 3;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public PhotosViewHodle onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotosViewHodle(inflater.inflate(R.layout.adapter_photos_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotosViewHodle holder, final int position) {
        Glide.with(context)
                .load(list.get(position))
                .into(holder.photos_item_icon);

        holder.photos_item_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(list.get(position));
                myCallBack.updateList(list);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PhotosViewHodle extends RecyclerView.ViewHolder {

        ImageView photos_item_icon;
        ImageView photos_item_cross;
        RelativeLayout photos_item_rlayout;

        public PhotosViewHodle(View itemView) {
            super(itemView);
            photos_item_icon = itemView.findViewById(R.id.photos_item_icon);
            photos_item_rlayout = itemView.findViewById(R.id.photos_item_rlayout);
            photos_item_cross = itemView.findViewById(R.id.photos_item_cross);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) photos_item_icon.getLayoutParams();
            layoutParams.height = screenWidth;
            layoutParams.width = screenWidth;
            photos_item_icon.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams rl_lp = photos_item_rlayout.getLayoutParams();
            int cross = Utils.getViewHeight(photos_item_cross, false) / 2;
            rl_lp.height = screenWidth + cross;
            rl_lp.width = screenWidth + cross;
            photos_item_rlayout.setLayoutParams(rl_lp);
        }

    }

}
