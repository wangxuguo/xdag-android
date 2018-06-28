package com.xdag.wallet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.EventLog;
import android.util.Log;

import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagState;
import com.xdag.wallet.ui.activity.ResultActivity;
import com.xdag.wallet.ui.activity.XdagMainActivity;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.ToastUtil;
import com.xdag.wallet.utils.XdagUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/6/28.
 */

public class XdagService extends Service {

    private static final String TAG = Constants.ServiceTAG;
    public static final String ACTION_BIND_XDAG_SERVICE = "com.xdag.wallet.XdagService";
    public static final int MSG_CONNECT_TO_POOL = 1;
    public static final int MSG_DISCONNECT_FROM_POOL = 2;
    public static final int MSG_XFER_XDAG_COIN = 3;
    public static final int MSG_XdagNotifyMsg = 4;

    private static final int ONGOING_NOTIFICATION_ID = 0x001;
    private final LocalBinder mBinder = new LocalBinder();
    private boolean mAllowRebind = true;
    private XdagEvent mEvent;

    private XdagState lastUIState;
    private XdagHandler xdagHandler;
    private boolean isConnected;
    private HandlerThread xdagProcessThread;

    public void startConnectToPool(String poolAddr) {
        Message msg = xdagHandler.obtainMessage(MSG_CONNECT_TO_POOL, poolAddr);
        xdagHandler.sendMessage(msg);
    }

    public void disConnectToPool() {
        xdagHandler.sendEmptyMessage(MSG_DISCONNECT_FROM_POOL);
    }

    public void xdagNotifyMsg(String authInfo) {

        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagNotifyMsg(authInfo);
//        Message msg = xdagHandler.obtainMessage(MSG_XdagNotifyMsg, authInfo);
//        xdagHandler.sendMessage(msg);
    }

    public class XdagTransferModel implements Serializable{
        public XdagTransferModel(String address, String account) {
            this.address = address;
            this.account = account;
        }

        private String address;
        private String account;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }

    public void XdagTransferTo(String address, String account) {
//        Message msg = xdagHandler.obtainMessage(MSG_XFER_XDAG_COIN, new XdagTransferModel(address,account));
//        xdagHandler.sendMessage(msg);
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagXferToAddress(address,account);
    }

    class XdagHandler extends Handler {
        public XdagHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage  " + msg.what);
            XdagWrapper xdagWrapper = XdagWrapper.getInstance();
            switch (msg.what) {
                case MSG_CONNECT_TO_POOL:
                    Log.i(TAG, "receive msg connect to the pool thread id " + Thread.currentThread().getId());
                    String poolAddr = (String) msg.obj;
                    xdagWrapper.XdagConnectToPool(poolAddr);
                    break;
                case MSG_DISCONNECT_FROM_POOL:
                    xdagWrapper.XdagDisConnectFromPool();
                    break;
                case MSG_XFER_XDAG_COIN:
                    XdagTransferModel xdagTransferModel = (XdagTransferModel) msg.obj;
                    xdagWrapper.XdagXferToAddress(xdagTransferModel.getAddress(),xdagTransferModel.getAccount());
                    break;
                case MSG_XdagNotifyMsg:
                    String authInfo = (String) msg.obj;
                    xdagWrapper.XdagNotifyMsg(authInfo);
                    break;
                default: {
                    Log.e(TAG, "unkown command from ui");
                }
                break;
            }
        }
    }

    ;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Log.e(TAG, "onCreate");
        xdagProcessThread = new HandlerThread("XdagProcessThread", HandlerThread.NORM_PRIORITY + 1);
        xdagProcessThread.start();
        xdagHandler = new XdagHandler(xdagProcessThread.getLooper());//
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent != null) {
            int what = intent.getIntExtra(Constants.XDAG_EVENT_TYPE, 0);
            Message msg = xdagHandler.obtainMessage();
            if(what == MSG_CONNECT_TO_POOL){
                String poolAddr = intent.getStringExtra(Constants.XDAG_POOL_ADDRESS);
                msg.obj = poolAddr;
            }else if(what == MSG_DISCONNECT_FROM_POOL){

            }else if(what == MSG_XFER_XDAG_COIN){

            }else if(what == MSG_XdagNotifyMsg){
                String notify_msg = intent.getStringExtra(Constants.XDAG_NOTIFY_MSG);
                msg.obj = notify_msg;
            }else {
            }
            msg.arg1 = what;
            msg.what = what;
            xdagHandler.sendMessage(msg);
        } else {
            Message msg = xdagHandler.obtainMessage();
            msg.arg1 = startId;
            msg.what = startId;
            xdagHandler.sendMessage(msg);
        }

        return START_STICKY;
    }

    public boolean IsConnected() {
        return isConnected;
    }

    public class LocalBinder extends Binder {
        public XdagService getService() {
            return XdagService.this;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        if (event != null) {
            mEvent = event;
        }
        Log.i(TAG, "ProcessXdagEvent " + Thread.currentThread().getId() + " type " + event.eventType + " account "
                + event.address + " balace " + event.balance + " state " + event.state);
        switch (event.eventType) {
            case XdagEvent.en_event_set_pwd:
            case XdagEvent.en_event_type_pwd:
            case XdagEvent.en_event_retype_pwd:
            case XdagEvent.en_event_set_rdm:
                XdagState xdagState = new XdagState(isConnected, event.state, event.eventType, event.address, event.balance, event.errorMsg);
                EventBus.getDefault().post(xdagState);
                break;
            case XdagEvent.en_event_update_state:
                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {
                    Log.i(TAG, " Connected");
                    isConnected = true;
                } else {
                    isConnected = false;
                }
                XdagState xdagState2 = new XdagState(isConnected, event.state, event.eventType, event.address, event.balance, event.errorMsg);
                if (lastUIState != null && lastUIState.equals(xdagState2)) {

                } else {
                    lastUIState = xdagState2;
                    EventBus.getDefault().post(xdagState2);
                }
                break;

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, mBuilder.build());
    }
}
