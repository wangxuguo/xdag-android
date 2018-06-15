package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.xdag.wallet.R;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMain();
                finish();
            }
        },3000);
    }

    private void gotoMain() {
        Intent intent = new Intent(this, XdagMainActivity.class);
        startActivity(intent);
    }

}
