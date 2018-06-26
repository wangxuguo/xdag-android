package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class TransactionRecordActivity extends BaseActivity  implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    TextView tv_title_right;
    EditText etName;
    EditText etAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        etName = (EditText) findViewById(R.id.et_name);
        etAddress = (EditText) findViewById(R.id.et_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
        }
    }
}