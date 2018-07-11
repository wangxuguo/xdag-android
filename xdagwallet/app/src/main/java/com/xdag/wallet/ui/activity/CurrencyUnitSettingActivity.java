package com.xdag.wallet.ui.activity;

import android.app.Activity;
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
import com.xdag.wallet.event.CurrencyUnitChanged;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.CurrencyModel;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.CurrencyUnitSettingAdapter;
import com.xdag.wallet.ui.adapter.MultilingualAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class CurrencyUnitSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    RecyclerView recyclerView;
    Button btn_save;
    private CurrencyUnitSettingAdapter adapter;
    private ArrayList<CurrencyModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multilingual_setting);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        list = new ArrayList<>();
        list.add(new CurrencyModel("CNY","￥"));
        list.add(new CurrencyModel("USD","$"));
        adapter = new CurrencyUnitSettingAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        adapter.setShowFootView(false);
        String name =  getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).getString(Constants.CURRENCY_NAME,null);
        String symbol =  getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).getString(Constants.CURRENCY_SYMBOL,null);
        int selectedIndex = 0;
        if(!TextUtils.isEmpty(name)){
            if(list.contains(new CurrencyModel(name,symbol))){
                selectedIndex = list.indexOf(new CurrencyModel(name,symbol));
            }
        }
        adapter.setSeleceted(selectedIndex);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<CurrencyModel>() {
            @Override
            public void onItemClick(View view, CurrencyModel data) {
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
                String name = list.get(adapter.getSeleceted()).getName();
                String symbol = list.get(adapter.getSeleceted()).getSymbol();
                getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).edit().putString(Constants.CURRENCY_NAME, name).apply();
                getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).edit().putString(Constants.CURRENCY_SYMBOL, symbol).apply();
                EventBus.getDefault().post(new CurrencyUnitChanged());
                finish();
                break;
        }
    }
}
