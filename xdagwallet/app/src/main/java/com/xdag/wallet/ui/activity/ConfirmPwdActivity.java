package com.xdag.wallet.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 *
 */
public class ConfirmPwdActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_title_left;
    TextView tv_title;
    EditText et_enter_pwd;
    Button btn_confirm_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confim_pwd);
        findViews();
        initViews();
    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
//                et_enter_pwd
        btn_confirm_pwd.setOnClickListener(this);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_enter_pwd = (EditText) findViewById(R.id.et_enter_pwd);
        btn_confirm_pwd = (Button) findViewById(R.id.btn_confirm_pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.btn_confirm_pwd:
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();
                break;
        }
    }
}
