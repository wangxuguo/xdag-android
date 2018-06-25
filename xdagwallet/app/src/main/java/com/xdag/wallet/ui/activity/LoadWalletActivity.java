package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xdag.wallet.R;
import com.xdag.wallet.utils.FileUtils;
import com.xdag.wallet.utils.ToastUtil;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class LoadWalletActivity extends BaseActivity {
    private static final int REQUESTCODE_FROM_ACTIVITY = 1000;
    ImageView iv_title_left;
    TextView tv_title;
    TextView tv_back_file_info;
    EditText et_create_wallet_pwd;
    EditText et_create_wallet_repwd;
    CheckBox ckb_service_srivacy;
    TextView tv_wallet_notice;
    Button btn_choose_file;
    Button btn_load_wallet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_wallet);
        findViews();
        initViews();
    }

    private void initViews() {
        ckb_service_srivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_load_wallet.setEnabled(isChecked);
            }
        });
        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(LoadWalletActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withStartPath(FileUtils.rootPath)
//                        .withIsGreater(false)
                        .withTitle(getString(R.string.choose_wallet_file))//标题文字
//                        .withTitleColor("#FF99CC")//文字颜色
                        .withMutilyMode(false)
                        .withIconStyle(Constant.ICON_STYLE_BLUE)
//                        .withFileSize(500 * 1024)
                        .start();
            }
        });
        btn_load_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd  = et_create_wallet_pwd.getText().toString();
                String repwd  = et_create_wallet_repwd.getText().toString();
                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(repwd)){
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.name_is_empty));
                    return;
                }
                if(!pwd.equals(repwd)){
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.pwd_not_same));
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                String path =  list.get(0);
                tv_back_file_info.setText(path);
//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back_file_info = (TextView) findViewById(R.id.tv_back_file_info);
        et_create_wallet_pwd = (EditText) findViewById(R.id.et_create_wallet_pwd);
        et_create_wallet_repwd = (EditText) findViewById(R.id.et_create_wallet_repwd);
        ckb_service_srivacy = (CheckBox) findViewById(R.id.ckb_service_srivacy);
        tv_wallet_notice = (TextView) findViewById(R.id.tv_wallet_notice);
        btn_load_wallet = (Button) findViewById(R.id.btn_load_wallet);
        btn_choose_file = (Button) findViewById(R.id.btn_choose_file);
    }
}
