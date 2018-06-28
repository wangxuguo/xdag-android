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

/**
 * Created by wangxuguo on 2018/6/28.
 */

public class XdagService extends Service {

    private static final String TAG = Constants.TAG;

    public static final int MSG_CONNECT_TO_POOL = 1;
    public static final int MSG_DISCONNECT_FROM_POOL = 2;
    public static final int MSG_XFER_XDAG_COIN = 3;

    private static final int ONGOING_NOTIFICATION_ID = 0x001;
    LocalBinder mBinder;
    private boolean mAllowRebind;
    private XdagEvent mEvent;


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
                    Bundle data = msg.getData();
                    String poolAddr = data.getString("pool");
                    xdagWrapper.XdagConnectToPool(poolAddr);
                    break;
                case MSG_DISCONNECT_FROM_POOL:
                    xdagWrapper.XdagDisConnectFromPool();
                    break;
                case MSG_XFER_XDAG_COIN:

                    break;
                default: {
                    Log.e(TAG, "unkown command from ui");
                }
                break;
            }
        }
    };


    XdagHandler xdagHandler ;
    private boolean isConnected;
    private XdagProgressDialog xdagProgressDialog;
        private HandlerThread xdagProcessThread = new HandlerThread("XdagProcessThread");
    final Messenger mMessenger = new Messenger(new XdagHandler(xdagProcessThread.getLooper()));
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        if(xdagProcessThread!=null) {
            xdagProcessThread.start();
        }
        xdagHandler = new XdagHandler(xdagProcessThread.getLooper());//

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = xdagHandler.obtainMessage();
        msg.arg1 = startId;
        xdagHandler.sendMessage(msg);
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public boolean IsConnected() {
        return isConnected;
    }

    public class LocalBinder extends Binder {
        public XdagService getService() {
            // Return this instance of LocalService so clients can call public methods
            return XdagService.this;
        }
    }
    XdagState lastUIState;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        if(event!=null){
            mEvent = event;
        }
        Log.i(TAG, "ProcessXdagEvent " + Thread.currentThread().getId()+ " type " + event.eventType+" account "
                + event.address +" balace " + event.balance+" state " + event.state);
        switch (event.eventType) {
            case XdagEvent.en_event_set_pwd:
            case XdagEvent.en_event_type_pwd:
            case XdagEvent.en_event_retype_pwd:
            case XdagEvent.en_event_set_rdm:
                XdagState xdagState = new XdagState(isConnected,event.state,event.eventType,event.address,event.balance,event.errorMsg);
                EventBus.getDefault().post(xdagState);
                break;
            case XdagEvent.en_event_update_state:
                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {
                    Log.i(TAG, "loadingLayout GONE");
                    isConnected = true;
                }else {
                    isConnected = false;
                }
                XdagState xdagState2 = new XdagState(isConnected,event.state,event.eventType,event.address,event.balance,event.errorMsg);
                if(lastUIState!=null&&lastUIState.equals(xdagState2)){

                }else {
                    lastUIState = xdagState2;
                    EventBus.getDefault().post(xdagState2);
                }
                break;

        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
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
