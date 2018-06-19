package com.xdag.wallet.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 *
 */
public class AddNewContractActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_title_left;
    TextView tv_title;
    TextView tv_title_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contract);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.tv_title_right:
                break;
        }
    }
}
