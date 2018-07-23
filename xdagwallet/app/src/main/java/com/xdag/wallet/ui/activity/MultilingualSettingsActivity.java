package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagApplication;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.MultilingualAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class MultilingualSettingsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    RecyclerView recyclerView;
    Button btn_save;
    private MultilingualAdapter adapter;
    private ArrayList<String> list;
    private ArrayList<String> listCountry;

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
        list.add(getString(R.string.app_default));
        list.add(getString(R.string.app_zh_CN));
//        list.add(getString(R.string.app_zh_TW));
        list.add(getString(R.string.app_en));
        adapter = new MultilingualAdapter(R.layout.item_multi_language, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        String str =getSharedPreferences(Constants.SPSetting,MODE_PRIVATE).getString(Constants.LANGUAGE, "");
        int selectedIndex  = 0;
        if (str != null && str.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            selectedIndex = 1;
//        } else if (str != null && str.equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
//            selectedIndex = 2;
        } else if (str != null && str.equals(Locale.ENGLISH.getLanguage())) {
            selectedIndex = 2;
        } else {
            selectedIndex = 0;
        }
        adapter.setSeleceted(selectedIndex);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.setSeleceted(position);
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
                String str =null;
                switch (adapter.getSeleceted()) {
                    case 1:
                        str = Locale.SIMPLIFIED_CHINESE.getLanguage();
                        break;
//                    case 2:
//                        str = Locale.TRADITIONAL_CHINESE.getLanguage();
//                        break;
                    case 2:
                        str = Locale.ENGLISH.getLanguage();
                        break;
                    case 0:
                    default:
                        str = Locale.getDefault().getLanguage();
                        break;
                }
                getSharedPreferences(Constants.SPSetting,MODE_PRIVATE).edit().putString(Constants.LANGUAGE,str).apply();
                XdagApplication application = (XdagApplication) getApplication();
                application.initMutilLilingual();
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }
}
