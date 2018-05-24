package com.znq.imageselector.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.znq.imageselector.R;
import com.znq.imageselector.adapter.PhotosRecycleAdapter;
import com.znq.imageselector.adapter.SelectPhotosAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SelectPhotosActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private GridView selectphotos_gv;
    private SelectPhotosAdapter adapter;//所有图片的适配器
    private RecyclerView selectphotos_rl;
    private PhotosRecycleAdapter recycleAdapter;//选中图片的适配器
    private Button selectphotos_determine;//确定
    private TextView selectphotos_number;//数目

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectphotos);

        initView();
        initData();
    }

    private void initView() {
        try {
            selectphotos_number = findViewById(R.id.selectphotos_number);
            jsonArray = new JSONArray(getIntent().getStringExtra("json"));


            selectphotos_gv = findViewById(R.id.selectphotos_gv);
            selectphotos_rl = findViewById(R.id.selectphotos_rl);
            selectphotos_determine = findViewById(R.id.selectphotos_determine);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            selectphotos_rl.setLayoutManager(layoutManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        try {
            adapter = new SelectPhotosAdapter(this, new myCallBack() {
                @Override
                public void updateList(List<String> pathList) {//回调函数：用于选中照片之后更新下面的展示
                    recycleAdapter.setList(pathList);
                    selectphotos_number.setText(pathList.size() + "");
                }
            });
            recycleAdapter = new PhotosRecycleAdapter(this, new myCallBack() {
                @Override
                public void updateList(List<String> pathList) {//回调函数：用于选中照片之后更新下面的展示
                    recycleAdapter.setList(pathList);
                    selectphotos_number.setText(pathList.size() + "");
                }
            });
            selectphotos_rl.setAdapter(recycleAdapter);

            selectphotos_gv.setAdapter(adapter);
            adapter.setJsonArray(jsonArray);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回调函数：用于选中照片之后更新下面的展示
    public interface myCallBack {
        void updateList(List<String> pathList);
    }

}
