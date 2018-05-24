package com.znq.imageselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.znq.imageselector.R;
import com.znq.imageselector.activity.SelectPhotosActivity;
import com.znq.imageselector.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 选择多张照片的适配器
 * Created by Administrator on 2017/10/16 0016.
 */

public class SelectPhotosAdapter extends BaseAdapter {

    private Context context;
    private JSONArray jsonArray = new JSONArray();
    public ArrayList<String> path_list = new ArrayList<>();
    private int screenWidth = Utils.getScreenWidth();
    private SelectPhotosActivity.myCallBack myCallBack;

    public SelectPhotosAdapter(Context context, SelectPhotosActivity.myCallBack myCallBack) {
        this.context = context;
        this.myCallBack = myCallBack;
        screenWidth = (screenWidth - Utils.dp2px(context, 20)) / 3;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = Utils.LoadXmlView(context, R.layout.grid_photos_item);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(screenWidth, screenWidth);
            convertView.setLayoutParams(lp);
        }

        ImageView photos_item_iv = Utils.getListViewHolder(convertView, R.id.photos_item_iv);
        final ImageView photos_item_ib = Utils.getListViewHolder(convertView, R.id.photos_item_ib);

        try {

            final String url = jsonArray.getString(position);
            Glide.with(context)
                    .load(url)
                    .override(screenWidth, screenWidth)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(photos_item_iv);

            if (path_list.contains(url)) {
                photos_item_ib.setVisibility(View.VISIBLE);
            } else {
                photos_item_ib.setVisibility(View.GONE);
            }

            photos_item_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path_list.contains(url)) {
                        path_list.remove(url);
                        photos_item_ib.setVisibility(View.GONE);
                    } else if (path_list.size() < 9) {
                        path_list.add(url);
                        photos_item_ib.setVisibility(View.VISIBLE);
                    }
                    myCallBack.updateList(path_list);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
