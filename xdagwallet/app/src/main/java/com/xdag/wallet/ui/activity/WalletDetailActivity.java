package com.xdag.wallet.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.ui.widget.MyAlertDialog;
import com.xdag.wallet.utils.FileUtils;

import java.io.File;

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
        if (getIntent() != null && getIntent().hasExtra(Constants.WALLET_DETAIL)) {
            xdagWalletModel = (XdagWalletModel) getIntent().getSerializableExtra(Constants.WALLET_DETAIL);
        }
        findViews();
        initViews();
    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        if(xdagWalletModel!=null) {
            tv_title.setText(xdagWalletModel.getName());
            tv_xdag_amount.setText(xdagWalletModel.getAmount() + getString(R.string.xdag));
            tv_xdag_address.setText(xdagWalletModel.getAddress());
        }
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
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.li_change_wallet_name:
                startActivity(new Intent(this, ChangedWalletNameActivity.class));
                break;
            case R.id.li_change_pwd:
                startActivity(new Intent(this, ChangedWalletPwdActivity.class));
                break;
            case R.id.tv_delete_wallet:

                MyAlertDialog.Builder builder = new MyAlertDialog.Builder(this);
                builder.setTitle(getString(R.string.input_password));
                builder.setMessage(getString(R.string.pay_attention_can_not_revert));
                View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_wallet,null);
                TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
                tvContent.setText(getString(R.string.pay_attention_can_not_revert));
                final EditText etInputPwd = (EditText) view.findViewById(R.id.et_input_pwd);
                builder.setContentView(view);
                builder.setNegativeButton(R.string.make_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = etInputPwd.getText().toString();

                    }
                }).setNegativeTextColor(getResources().getColor(R.color.text_blue_bg))
                        .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveTextColor(getResources().getColor(R.color.text_blue_bg));
                break;
            case R.id.tv_backup_wallet:
                String backFilePath = FileUtils.rootPath+File.separator+FileUtils.XDAG_BACKUP_PATH;//+"/"+FileUtils.WALLET_NAME;
                if(TextUtils.isEmpty(xdagWalletModel.getBankPath())){
                    File backPathFile = new File(backFilePath);
                    if(backPathFile.exists()&&backPathFile.isDirectory()&&backPathFile.canWrite()){
                        int listCount = backPathFile.list().length; //默认为wallet  其他为wallet1,wallet2...
                        File targetFile = new File(backFilePath+File.separator+FileUtils.WALLET_BANK_PATH_Pre+listCount);
                        if(!targetFile.exists()){
                            targetFile.mkdir();
                        }
                        FileUtils.copyFile(FileUtils.WALLET_FILE,targetFile.getAbsolutePath()+File.separator+FileUtils.WALLET_NAME);
                        FileUtils.copyFile(FileUtils.DNET_KEY_FILE,targetFile.getAbsolutePath()+File.separator+FileUtils.DNET_KEY_NAME);
                    }
                }else { //已经备份？

                }

                break;
            case R.id.tv_set_default_wallet:
                String backFilePath2 = FileUtils.rootPath+File.separator+FileUtils.XDAG_BACKUP_PATH;//+"/"+FileUtils.WALLET_NAME;
                if(TextUtils.isEmpty(xdagWalletModel.getBankPath())){
                    File backPathFile = new File(backFilePath2);
                    if(backPathFile.exists()&&backPathFile.isDirectory()&&backPathFile.canWrite()){
                        int listCount = backPathFile.list().length; //默认为wallet  其他为wallet1,wallet2...
                        File targetFile = new File(backFilePath2+File.separator+FileUtils.WALLET_BANK_PATH_Pre+listCount);
                        if(!targetFile.exists()){
                            targetFile.mkdir();
                        }
                        FileUtils.copyFile(FileUtils.WALLET_FILE,targetFile.getAbsolutePath()+File.separator+FileUtils.WALLET_NAME);
                        FileUtils.copyFile(FileUtils.DNET_KEY_FILE,targetFile.getAbsolutePath()+File.separator+FileUtils.DNET_KEY_NAME);
                        xdagWalletModel.setBankPath(targetFile.getAbsolutePath());
                        xdagWalletModel.update();
                    }
                }else {  //已经备份，复制到默认的目录下即可
                    String defaultBackPath = FileUtils.rootPath+File.separator+FileUtils.XDAG_BACKUP_PATH+File.separator+FileUtils.WALLET_BANK_PATH_Pre;
                    File defaultBackFile = new File(defaultBackPath);
                    if(defaultBackFile.exists()){
                        String[] files = defaultBackFile.list();
                        for(int i = 0 ; i < files.length ; i ++){
                            new File(files[i]).delete();
                        }
                    }else {
                        defaultBackFile.mkdir();
                    }
                    FileUtils.copyFile(FileUtils.WALLET_FILE,defaultBackFile.getAbsolutePath()+File.separator+FileUtils.WALLET_NAME);
                    FileUtils.copyFile(FileUtils.DNET_KEY_FILE,defaultBackFile.getAbsolutePath()+File.separator+FileUtils.DNET_KEY_NAME);
                }
                break;
        }
    }
}
