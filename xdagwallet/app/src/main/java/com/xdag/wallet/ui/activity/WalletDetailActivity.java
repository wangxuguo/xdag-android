package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.XdagWalletModel;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class WalletDetailActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_title_left;
    TextView tv_title;
    TextView tv_xdag_amount;
    TextView tv_xdag_address;
    LinearLayout li_change_wallet_name;
    LinearLayout li_change_pwd;

    TextView tv_delete_wallet;
    TextView tv_backup_wallet;
    TextView tv_set_default_wallet;
    XdagWalletModel xdagWalletModel;
    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);
        findViews();
        initViews();
    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        tv_title.setText(xdagWalletModel.getName());
        tv_xdag_amount.setText(xdagWalletModel.getAmount()+getString(R.string.xdag));
        tv_xdag_address.setText(xdagWalletModel.getAddress());
        li_change_wallet_name.setOnClickListener(this);
        li_change_pwd.setOnClickListener(this);
        tv_delete_wallet.setOnClickListener(this);
        tv_backup_wallet.setOnClickListener(this);
        tv_set_default_wallet.setOnClickListener(this);

    }


    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_xdag_amount = (TextView) findViewById(R.id.tv_xdag_amount);

        tv_xdag_address = (TextView) findViewById(R.id.tv_xdag_address);

        li_change_wallet_name = (LinearLayout) findViewById(R.id.li_change_wallet_name);
        li_change_pwd = (LinearLayout) findViewById(R.id.li_change_pwd);

        tv_delete_wallet = (TextView) findViewById(R.id.tv_delete_wallet);
        tv_backup_wallet = (TextView) findViewById(R.id.tv_backup_wallet);
        tv_set_default_wallet = (TextView) findViewById(R.id.tv_set_default_wallet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.li_change_wallet_name:
                startActivity(new Intent(this,ChangedWalletNameActivity.class));
                break;
            case R.id.li_change_pwd:
                startActivity(new Intent(this,ChangedWalletPwdActivity.class));
                break;
            case R.id.tv_delete_wallet:
                break;
            case R.id.tv_backup_wallet:
                break;
            case R.id.tv_set_default_wallet:
                break;
        }
    }
}
