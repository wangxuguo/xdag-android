package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class AboutUs extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    private ImageView iv_xdag_icon;
    private LinearLayout li_use_agreement;
    private TextView tv_xdag_link_info;
    private LinearLayout li_version_log;
    private LinearLayout li_check_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        li_use_agreement.setOnClickListener(this);
        li_version_log.setOnClickListener(this);
        li_check_update.setOnClickListener(this);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_xdag_icon = (ImageView) findViewById(R.id.iv_xdag_icon);
        tv_xdag_link_info = (TextView) findViewById(R.id.tv_xdag_link_info);
        li_use_agreement = (LinearLayout) findViewById(R.id.li_use_agreement);
        li_version_log = (LinearLayout) findViewById(R.id.li_version_log);
        li_check_update = (LinearLayout) findViewById(R.id.li_check_update);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.li_use_agreement:
                break;
            case R.id.li_version_log:
                break;
            case R.id.li_check_update:
                break;
        }
    }
}
