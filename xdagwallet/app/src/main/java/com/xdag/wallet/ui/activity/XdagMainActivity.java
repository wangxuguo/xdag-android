package com.xdag.wallet.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import com.xdag.wallet.XdagService;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagState;
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

public class XdagMainActivity extends XdagBaseActivity implements AuthDialogFragment.AuthInputListener{

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

    private XdagProgressDialog xdagProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdag_main);
//        isConnected = getIntent().getBooleanExtra("isStartXdagProcess",false);
        EventBus.getDefault().register(this);
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
        if(mService == null || !mService.IsConnected()) {
            connectToPool();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(XdagState event) {
        if(event.isConnect){
            if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                xdagProgressDialog.dismiss();
            }
            if (event.eventType == XdagEvent.en_event_type_pwd || event.eventType == XdagEvent.en_event_type_pwd
                    || event.eventType == XdagEvent.en_event_retype_pwd || event.eventType == XdagEvent.en_event_set_rdm) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("title", XdagUtils.GetAuthHintString(this, event.eventType));
                AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                authDialogFragment.setArguments(bundle);
                authDialogFragment.setAuthHintInfo(XdagUtils.GetAuthHintString(this, event.eventType));
                authDialogFragment.show(getFragmentManager(), "Auth Dialog");
            }
        }

    }

    private void connectToPool() {
//        String poolAddr = getContext().getSharedPreferences(Constants.SPSetting, Context.MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS,Constants.DefaultPoolAddress);
        String poolAddr = Constants.DefaultPoolAddress;

        Message remoteMsg = Message.obtain(null, XdagService.MSG_CONNECT_TO_POOL, 0, 0,poolAddr);
        try {
            messenger.send(remoteMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for(int i =0;i < list.size() ; i ++){
            list.get(i).onActivityResult(requestCode,resultCode,data);
        }
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
        Message remoteMsg = Message.obtain(null, XdagService.MSG_DISCONNECT_FROM_POOL, 0, 0);
        try {
            messenger.send(remoteMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
