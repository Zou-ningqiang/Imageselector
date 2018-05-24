package com.znq.imageselector.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.znq.imageselector.R;
import com.znq.imageselector.adapter.SelectDirectoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private SelectDirectoryAdapter adapter;
    private ListView main_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查所需要的权限是否开启
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        } else {
            initView();
            initData();
        }
    }

    private void initView() {
        main_list = findViewById(R.id.main_list);
        adapter = new SelectDirectoryAdapter(this);
        main_list.setAdapter(adapter);
    }

    private void initData() {
        try {
            initImages();
            adapter.setJsonArray(photos);
            adapter.setList(photosDir);
            adapter.notifyDataSetChanged();

            main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //得到某个相册的所有图片
                    JSONArray jsonArray = (JSONArray) adapter.getItem(position);
                    Intent intent = new Intent(MainActivity.this, SelectPhotosActivity.class);
                    intent.putExtra("json", jsonArray.toString());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //存放相册名以及第一张图片用于当封面
    private ArrayList<JSONObject> photosDir = new ArrayList<>();
    //存放所有的照片；根据相册分组存放
    private JSONArray photos = new JSONArray();
    private void initImages() {
        //获取存储在外部存储器（SD卡）上的图片文件的资源
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;
        //内容解析器
        ContentResolver mContentResolver = getContentResolver();
        //query所有图片
        Cursor cursor = mContentResolver.query(mImageUri, new String[] { key_DATA },
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",//条件语句
                new String[] { "image/jpg", "image/jpeg", "image/png" },//条件
                MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null) {
            //遍历cursor内容的时候从最后一条遍历；这样便于处理最新照片
            if (cursor.moveToLast()) {
                //相册名Set
                HashSet<String> cachePath = new HashSet<String>();
                //初始化相册List
                photosDir = new ArrayList<JSONObject>();
                //创建一个最新相册
                JSONObject jo_ = new JSONObject();
                try {
                    //设置最新相册的显示名称
                    jo_.put("imgPath", "最新照片");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                photosDir.add(jo_);
                photos = new JSONArray();
                //创建一个用于存储最新照片的jsonArray
                JSONArray newArrayStrs = new JSONArray();
                photos.put(newArrayStrs);
                //图片数组
                JSONArray arrayStrs = null;
                int i = 0;
                do {
                    //获取图片路径
                    String imagePath = cursor.getString(0);
                    File f = new File(imagePath);
                    if (f.exists() == true && f.length() != 0) {
                        //获取相册名[得到该文件的父文件夹的名字]
                        String parentPath = f.getParentFile().getName();
                        if (cachePath.contains(parentPath)) {
                            //如果相册set中存在该图片父文件夹名称；则只把该图片存入图片数组中
                            arrayStrs.put(imagePath);
                        } else {
                            //如果相册set中不存在该图片父文件夹名称；把文件夹名称存入相册set中
                            cachePath.add(parentPath);
                            //创建一个相册名jsonObject；设置相册名称以及封面
                            JSONObject jo = new JSONObject();
                            try {
                                jo.put("imgPath", parentPath);
                                jo.put("fisrtImg", imagePath);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //把相册名jsonObject放入相册名数组中
                            photosDir.add(jo);
                            //初始化图片数组；并把图片放入图片数组中再把图片数组放入所有的图片数组集合中
                            arrayStrs = new JSONArray();
                            arrayStrs.put(imagePath);
                            photos.put(arrayStrs);
                        }
                        //最新相册最多只存放80张图片
                        if (i < 80) {
                            if (i == 0) {
                                try {
                                    //把第一张图片设置为最新相册的封面
                                    jo_.put("fisrtImg", imagePath);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            newArrayStrs.put(imagePath);
                            i++;
                        }
                    }
                } while (cursor.moveToPrevious() == true);
            }
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] != -1) {
            initView();
            initData();
        }
    }

}
