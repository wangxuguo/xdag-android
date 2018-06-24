package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.utils.ToastUtil;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class CreateWalletActivity extends BaseActivity {
    ImageView iv_title_left;
    TextView tv_title;
    EditText et_create_wallet_name;
    EditText et_create_wallet_pwd;
    EditText et_create_wallet_repwd;
    EditText et_create_wallet_keyword;
    CheckBox ckb_service_srivacy;
    TextView tv_wallet_notice;
    Button btn_create_wallet;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        findViews();
        initViews();
    }

    private void initViews() {
        ckb_service_srivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_create_wallet.setEnabled(isChecked);
            }
        });
        btn_create_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd  = et_create_wallet_pwd.getText().toString();
                String repwd  = et_create_wallet_repwd.getText().toString();
                String keyword  = et_create_wallet_keyword.getText().toString();
                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(repwd)||TextUtils.isEmpty(keyword)){
                    ToastUtil.showShort(CreateWalletActivity.this,getString(R.string.name_is_empty));
                    return;
                }
                if(!pwd.equals(repwd)){
                    ToastUtil.showShort(CreateWalletActivity.this,getString(R.string.pwd_not_same));
                }

            }
        });
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_create_wallet_name = (EditText) findViewById(R.id.et_create_wallet_name);
        et_create_wallet_pwd = (EditText) findViewById(R.id.et_create_wallet_pwd);
        et_create_wallet_repwd = (EditText) findViewById(R.id.et_create_wallet_repwd);
        et_create_wallet_keyword = (EditText) findViewById(R.id.et_create_wallet_keyword);
        ckb_service_srivacy = (CheckBox) findViewById(R.id.ckb_service_srivacy);
        tv_wallet_notice = (TextView) findViewById(R.id.tv_wallet_notice);
        btn_create_wallet = (Button) findViewById(R.id.btn_create_wallet);
    }
}
