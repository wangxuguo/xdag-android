package com.xdag.wallet.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.AuthDialogFragment;
import com.xdag.wallet.MyActivityManager;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.ui.fragment.ContactsFragment;
import com.xdag.wallet.ui.fragment.PropertyFragment;
import com.xdag.wallet.ui.fragment.SettingsFragment;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.ToastUtil;
import com.xdag.wallet.utils.XdagUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class XdagMainActivity extends AppCompatActivity implements AuthDialogFragment.AuthInputListener{

    private static final String TAG = Constants.TAG;

    private static final int MSG_CONNECT_TO_POOL = 1;
    private static final int MSG_DISCONNECT_FROM_POOL = 2;
    private static final int MSG_XFER_XDAG_COIN = 3;
    private static final int MSG_SHOW_PROGRESS = 4;
    private static final int MSG_DISSMIS_PROGRESS = 5;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View loadingLayout;
    private TextView tv_message;
    private ImageView iv_loading;
    private ArrayList<Fragment> list = new ArrayList<>();
    private PropertyFragment propertyFragment;
    private ContactsFragment contactsFragment;
    private SettingsFragment settingsFragment;

    private HandlerThread xdagProcessThread;
    private Handler xdagHandler;
    private boolean isConnected;
    private XdagProgressDialog xdagProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdag_main);
        isConnected = getIntent().getBooleanExtra("isStartXdagProcess",false);
        EventBus.getDefault().register(this);
        xdagProcessThread = new HandlerThread("XdagProcessThread");
        xdagProcessThread.start();
        xdagHandler = new Handler(xdagProcessThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handleMessage  " + msg.what);
                switch (msg.arg1) {
                    case MSG_CONNECT_TO_POOL:
                        Log.i(TAG, "receive msg connect to the pool thread id " + Thread.currentThread().getId());
                        Bundle data = msg.getData();
                        String poolAddr = data.getString("pool");
                        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
                        xdagWrapper.XdagConnectToPool(poolAddr);
                        xdagHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);

                        break;
                    case MSG_DISCONNECT_FROM_POOL:


                        break;
                    case MSG_XFER_XDAG_COIN:


                        break;
                    case MSG_DISSMIS_PROGRESS:
//                        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
//                            xdagProgressDialog.dismiss();
//                        }
                    case MSG_SHOW_PROGRESS:
//                        XdagProgressDialog.Builder builder = new XdagProgressDialog.Builder(XdagMainActivity.this);
////                        builder.setTitle()
//                        builder.setMessage(getString(R.string.wallet_connecting_to_pool));
//                        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
//                            xdagProgressDialog.dismiss();
//                            xdagProgressDialog = null;
//                        }
//                        xdagProgressDialog = builder.create();
//                        xdagProgressDialog.show();
                        break;
                    default: {
                        Log.e(TAG, "unkown command from ui");
                    }
                    break;
                }
            }
        };

        propertyFragment = new PropertyFragment();
        contactsFragment = new ContactsFragment();
        settingsFragment = new SettingsFragment();
        list.add(propertyFragment);
        list.add(contactsFragment);
        list.add(settingsFragment);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        loadingLayout = findViewById(R.id.loading_layout);
        tv_message = (TextView) findViewById(R.id.tv_message);
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        initXdagFiles();
//        connectToPool();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        initXdagFiles();
        if(!isConnected) {
            connectToPool();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        Log.i(TAG, "process msg in Thread " + Thread.currentThread().getId());
        Log.i(TAG, "event event type is " + event.eventType);
        Log.i(TAG, "event account is " + event.address);
        Log.i(TAG, "event balace is " + event.balance);
        Log.i(TAG, "event state is " + event.state);
        if(!MyActivityManager.getInstance().getCurrentActivity().getLocalClassName().equals(this.getLocalClassName())){
            Log.i(TAG, "CurrentActivity:"+MyActivityManager.getInstance().getCurrentActivity().getLocalClassName()+"!equals " + this.getLocalClassName());
            return;
        }
        switch (event.eventType) {
            case XdagEvent.en_event_set_pwd:
                ToastUtil.showShort(this,getString(R.string.please_check_wallet_file));
                break;
            case XdagEvent.en_event_type_pwd:
            case XdagEvent.en_event_retype_pwd:
            case XdagEvent.en_event_set_rdm:
                Bundle bundle = new Bundle();
                bundle.putCharSequence("title", XdagUtils.GetAuthHintString(this,event.eventType));
                AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                authDialogFragment.setArguments(bundle);
                authDialogFragment.setAuthHintInfo(XdagUtils.GetAuthHintString(this,event.eventType));
                authDialogFragment.show(getFragmentManager(), "Auth Dialog");

                break;

            case XdagEvent.en_event_update_state:

                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {
//                    mHandler.sendEmptyMessage(MSG_DISSMIS_PROGRESS);
//                    if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
//                        xdagProgressDialog.dismiss();
//                        xdagProgressDialog = null;
//                    }
                    if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                        xdagProgressDialog.dismiss();
                    }
                    Log.i(TAG, "loadingLayout GONE");
//                    loadingLayout.setVisibility(View.GONE);
                    isConnected = true;
                }else {
                    isConnected = false;
                }
                Log.i(TAG, "update xdag  ui ");
//                account.setText(event.balance);
//                xdagAccount.setText(event.balance);
//                if(is)
//                tvBalance.setText(event.balance);
//                tvStatus.setText(event.state);

                break;

        }
    }





    private void connectToPool() {
//        loadingLayout.setVisibility(View.VISIBLE);
//        String poolAddr = getContext().getSharedPreferences(Constants.SPSetting, Context.MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS,Constants.DefaultPoolAddress);
        String poolAddr = Constants.DefaultPoolAddress;
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString("pool", poolAddr);
        msg.arg1 = MSG_CONNECT_TO_POOL;
        msg.setData(data);
        xdagHandler.sendMessage(msg);

//        XdagProgressDialog.Builder builder = new XdagProgressDialog.Builder(this);
//        builder.setMessage(getString(R.string.wallet_connecting_to_pool));
        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
            xdagProgressDialog.dismiss();
        }
        xdagProgressDialog =  new XdagProgressDialog(this,getString(R.string.wallet_connecting_to_pool));
        xdagProgressDialog.show();
    }

    private void initXdagFiles() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xdag_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthInputComplete(String authInfo) {
        Log.i(Constants.TAG,"auth info is " + authInfo);
        //notify native thread
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagNotifyMsg(authInfo);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {

        EventBus.getDefault().unregister(this);
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagDisConnectFromPool();
        super.onDestroy();
    }
}
