package com.xdag.wallet.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xdag.wallet.XdagService;

import static com.xdag.wallet.XdagService.ACTION_BIND_XDAG_SERVICE;

public class XdagBaseActivity extends BaseActivity {
    private static final String TAG = "XdagBase";
    XdagService mService;
//    Messenger messenger = null;
    boolean mBound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, XdagService.class);
         startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        Intent intent = new Intent(this, XdagService.class);
//        intent.setComponent(new ComponentName("com.xdag.wallet","com.xdag.wallet.XdagService"));
//        intent.setAction(ACTION_BIND_XDAG_SERVICE);
        boolean isBinded = bindService(intent, mConnection, 0);
        Log.d(TAG,"onStart  isBinded:"+isBinded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.d(TAG,"onServiceConnected");
            XdagService.LocalBinder binder = (XdagService.LocalBinder) service;
            mService = binder.getService();
//            messenger = new Messenger(service);
            mBound = true;
            onServiceConnectedToBinder();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG,"onServiceDisconnected");
            mBound = false;
//            messenger = null;
            mService = null;
            onServiceDisConnectedToBinder();
        }
    };

    public XdagService getService() {
        return mService;
    }

    public void onServiceDisConnectedToBinder() {
        Log.d(TAG,"onServiceDisConnectedToBinder");
    }

    public void onServiceConnectedToBinder() {
        Log.d(TAG,"onServiceConnectedToBinder");
    }
}
