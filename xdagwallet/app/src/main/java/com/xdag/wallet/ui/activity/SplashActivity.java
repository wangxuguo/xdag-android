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
import android.widget.TextView;
import android.widget.Toast;

import com.xdag.wallet.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SplashActivity extends BaseActivity {

    private static final int PERMISSION_REQUESTCODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initPermission();
        if (!checkCurrentWalletIsLegal()) {
            TextView tv_create_wallet = (TextView) findViewById(R.id.tv_create_wallet);
            TextView tv_load_wallet = (TextView) findViewById(R.id.tv_load_wallet);
            tv_create_wallet.setVisibility(View.VISIBLE);
            tv_load_wallet.setVisibility(View.VISIBLE);
            tv_create_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, CreateWalletActivity.class);
                    startActivity(intent);
                }
            });
            tv_load_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, LoadWalletActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain();
                    finish();
                }
            }, 3000);
        }

    }

    private boolean checkCurrentWalletIsLegal() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath+"/wallet.dat");
        return file.exists();
    }

    private void gotoMain() {
        Intent intent = new Intent(this, XdagMainActivity.class);
        startActivity(intent);
    }

    private void initPermission() {
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
        } else {
            Toast.makeText(this, "all permission is allowed", Toast.LENGTH_SHORT).show();
        }
    }

}
