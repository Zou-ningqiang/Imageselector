package com.znq.imageselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.znq.imageselector.R;
import com.znq.imageselector.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择相册目录的适配器
 * Created by Administrator on 2017/10/15 0015.
 */

public class SelectDirectoryAdapter extends BaseAdapter {

    private Context context;
    private List<JSONObject> list = new ArrayList<>();
    private JSONArray jsonArray = new JSONArray();

    public SelectDirectoryAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<JSONObject> list) {
        this.list = list;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.getJSONArray(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = Utils.LoadXmlView(context, R.layout.list_selectdrectory_item);
        }

        ImageView selectdrectory_item_iv = Utils.getListViewHolder(convertView, R.id.selectdrectory_item_iv);
        LinearLayout selectdrectory_item_ll = Utils.getListViewHolder(convertView, R.id.selectdrectory_item_ll);
        TextView selectdrectory_item_name = Utils.getListViewHolder(convertView, R.id.selectdrectory_item_name);
        TextView selectdrectory_item_number = Utils.getListViewHolder(convertView, R.id.selectdrectory_item_number);

        try {

            if (position == 0) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) selectdrectory_item_ll.getLayoutParams();
                lp.setMargins(0, Utils.dp2px(context, 13), 0, 0);
                selectdrectory_item_ll.setLayoutParams(lp);
            }

            JSONObject jsonObject = list.get(position);

            Glide.with(context)
                    .load(jsonObject.getString("fisrtImg"))
                    .into(selectdrectory_item_iv);
            selectdrectory_item_name.setText(jsonObject.getString("imgPath"));

            selectdrectory_item_number.setText(jsonArray.getJSONArray(position).length() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
