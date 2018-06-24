package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.XdagApplication;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.MultilingualAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class XdagPoolSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    RecyclerView recyclerView;
    Button btn_save;
    private MultilingualAdapter adapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdag_pool_setting);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        list = new ArrayList<>();
        try {
            BufferedInputStream inputStream = new BufferedInputStream(getAssets().open("netdb-white.txt"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            do {
                list.add(bufferedReader.readLine());
            } while (bufferedReader.read() != -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new MultilingualAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.setShowFootView(false);
       String str =  getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS,null);
        int selectedIndex = 0;
       if(!TextUtils.isEmpty(str)){
           if(list.contains(str)){
               selectedIndex = list.indexOf(str);
           }
       }
        adapter.setSeleceted(selectedIndex);
        adapter.notifyDataSetChanged();
        adapter.setNofootbar();
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String data) {
                adapter.setSeleceted(list.indexOf(data));
            }
        });
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_save:
                String str = list.get(adapter.getSeleceted());
                getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).edit().putString(Constants.XDAG_POOL_ADDRESS, str).apply();
                finish();
                break;
        }
    }
}