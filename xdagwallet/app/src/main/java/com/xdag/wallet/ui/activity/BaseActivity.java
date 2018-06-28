package com.xdag.wallet.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.xdag.wallet.ui.widget.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class BaseActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 0x01;
    private boolean isPaused;
    private boolean mIsJump2Settings;


    @Override
    protected void onResume() {
        super.onResume();

        if (mIsJump2Settings) {
            onRecheckPermission();
            mIsJump2Settings = false;
        }

        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    //多个权限的检查
    public void checkPermissions(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < 23) return;
        //用于记录权限申请被拒绝的权限集合
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            requestPermission(deniedPermissions);
        }
    }
    //申请权限被允许的回调
    public void onPermissionGranted(String permission) {

    }
    //调用系统API完成权限申请
    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
    }

    //弹出系统权限询问对话框，用户交互后的结果回调
    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0) {
                    //用于记录是否有权限申请被拒绝的标记
                    boolean isDenied = false;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        } else {
                            isDenied = true;
                            onPermissionDenied(permissions[i]);
                        }
                    }
                    if (isDenied) {
                        isDenied = false;
                        //如果有权限申请被拒绝，则弹出对话框提示用户去修改权限设置。
                        showPermissionSettingsDialog(permissions, grantResults);
                    }

                } else {
                    onPermissionFailure();
                }
                break;
        }
    }
    public void showPermissionSettingsDialog(@NonNull String[] permissions, @NonNull int[] grantResults) {
        MyAlertDialog.Builder builder = new MyAlertDialog.Builder(this);
        builder.setMessageGravity(MyAlertDialog.Builder.MESSAGE_CENTER_GRAVITY);
        builder.setMessage("缺少必要权限\n将导致部分功能无法正常使用");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                jump2PermissionSettings();
            }
        });

        Dialog mDialog = builder.create();
        mDialog.setCancelable(false);
        mDialog.show();
    }
    //申请权限被拒绝的回调
    public void onPermissionDenied(String permission) {

    }

    //申请权限的失败的回调
    public void onPermissionFailure() {

    }
    /**
     * 跳转到应用程序信息详情页面
     */
    public void jump2PermissionSettings() {
        mIsJump2Settings = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
    //如果从设置界面返回，则重新申请权限
    public void onRecheckPermission() {

    }
}
