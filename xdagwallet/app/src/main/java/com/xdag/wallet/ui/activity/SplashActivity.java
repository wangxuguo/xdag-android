package com.xdag.wallet.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.utils.FileEncryptUtils;
import com.xdag.wallet.utils.FileUtils;
import com.xdag.wallet.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SplashActivity extends BaseActivity {

    private static final int PERMISSION_REQUESTCODE = 1;
    private static final int REQUEST_CREATEWALLET = 10;
    private static final int REQUEST_LOADWALLET = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initPermission();
        boolean isCurrentLegal = checkCurrentWalletIsLegal();
        boolean isBackuped = hasBackupWalltet();
        if (!isCurrentLegal&&!isCurrentLegal) {
            TextView tv_create_wallet = (TextView) findViewById(R.id.tv_create_wallet);
            TextView tv_load_wallet = (TextView) findViewById(R.id.tv_load_wallet);
            ImageView iv_splash = (ImageView) findViewById(R.id.iv_splash);
            iv_splash.setImageResource(R.mipmap.loading_for_create_or_load_wallet);
            tv_create_wallet.setVisibility(View.VISIBLE);
            tv_load_wallet.setVisibility(View.VISIBLE);
            tv_create_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, CreateWalletActivity.class);
                    startActivityForResult(intent,REQUEST_CREATEWALLET);

                }
            });
            tv_load_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, LoadWalletActivity.class);
                    startActivityForResult(intent,REQUEST_LOADWALLET);
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain(false);
                    finish();
                }
            }, 3000);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CREATEWALLET:
                if(requestCode == RESULT_OK) {
                    gotoMain(true);
                    finish();
                }
                break;
            case REQUEST_LOADWALLET:
                if(requestCode == RESULT_OK) {
                    gotoMain(true);
                    finish();
                }
                break;
        }
    }
    private boolean hasBackupWalltet(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.showShort(this,getString(R.string.please_grant_permission));
            return false;
        }
        String backFilePath = FileUtils.rootPath+"/"+FileUtils.XDAG_BACKUP_PATH;
        File file = new File(backFilePath);
        if(file.exists()&&file.isDirectory()){
            File[] fileList = file.listFiles(); //默认备份的钱包可能不是连接的这个
            int length = fileList.length;
            for(int i =0;i< length;i++){
                if(fileList[i].exists()&&fileList[i].isDirectory()){
                    File file_wallet = new File(fileList[i].getAbsolutePath()+"/wallet.dat");
                    File file_net_key = new File(fileList[i].getAbsolutePath()+"/dnet_key.dat");
                    if(file.exists()&&file_net_key.exists()&&file.getTotalSpace()>0&&file_net_key.getTotalSpace()>0){

                        FileUtils.copyFile(file_wallet.getAbsolutePath(),FileUtils.WALLET_FILE);
                        FileUtils.copyFile(file_net_key.getAbsolutePath(),FileUtils.DNET_KEY_FILE);
                        ModelAdapter<XdagWalletModel> adapter = FlowManager.getModelAdapter(XdagWalletModel.class);
                        XdagWalletModel wallet = new XdagWalletModel();
                        wallet.setAddress("");
                        wallet.setWalletMd5(FileEncryptUtils.fileToMD5(file_wallet.getAbsolutePath()));
                        wallet.setAmount(0);
                        wallet.setDeleted(false);
                        wallet.setIcon("");
                        wallet.setName(fileList[i].getName());
                        wallet.setLocalPath("");
                        wallet.setType(3);
                        wallet.setBankPath(file_wallet.getAbsolutePath());
                        adapter.insert(wallet);
                        return true;
                    }
                }
            }
        }
        return  false;
    }


    private boolean checkCurrentWalletIsLegal() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath+"/wallet.dat");
        File file_net_key = new File(xdagFolderPath+"/dnet_key.dat");
        return file.exists()&&file_net_key.exists()&&file.getTotalSpace()>0&&file_net_key.getTotalSpace()>0;
    }

    private void gotoMain(boolean isStartXdagProcess) {
        Intent intent = new Intent(this, XdagMainActivity.class);
        intent.putExtra("isStartXdagProcess",isStartXdagProcess);
        startActivity(intent);
    }

    private boolean initPermission() {
        List<String> permissionLists = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.INTERNET);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.CAMERA);
        }

        if (!permissionLists.isEmpty()) {//说明肯定有拒绝的权限
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
            return false;
        } else {
            return true;
//            Toast.makeText(this, "all permission is allowed", Toast.LENGTH_SHORT).show();
        }
    }

}
