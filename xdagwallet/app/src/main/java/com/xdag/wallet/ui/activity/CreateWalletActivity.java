package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.AuthDialogFragment;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;
import com.xdag.wallet.model.XdagState;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.ToastUtil;
import com.xdag.wallet.utils.XdagUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class CreateWalletActivity extends XdagBaseActivity {
    private static final String TAG = Constants.TAG;
    private static final int MSG_CONNECT_TO_POOL = 1;
    private static final int MSG_DISCONNECT_FROM_POOL = 2;
    private static final int MSG_XFER_XDAG_COIN = 3;
    private static final int MSG_SHOW_PROGRESS = 4;
    private static final int MSG_DISSMIS_PROGRESS = 5;
    ImageView iv_title_left;
    TextView tv_title;
    EditText et_create_wallet_name;
    EditText et_create_wallet_pwd;
    EditText et_create_wallet_repwd;
    EditText et_create_wallet_keyword;
    CheckBox ckb_service_srivacy;
    TextView tv_wallet_notice;
    Button btn_create_wallet;
    private XdagProgressDialog xdagProgressDialog;
    private String pwd;
    private String repwd;
    private String keyword;
    private long createTime;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        EventBus.getDefault().register(this);

        findViews();
        initViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagState event) {
        Log.i(TAG, "CreateWallet onEvent " + Thread.currentThread().getId() +" event.isConnect:  "+event.isConnect+ " type " + event.eventType + " account "
                + event.address + " balace " + event.balance + " state " + event.state);

        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        switch (event.eventType) {
            case XdagEvent.en_event_type_pwd:
                xdagWrapper.XdagNotifyMsg(pwd);
                break;
            case XdagEvent.en_event_set_pwd:
                Log.i(TAG, "CreateWallet onEvent en_event_set_pwd");
                xdagWrapper.XdagNotifyMsg(pwd);
                Log.i(TAG, "CreateWallet onEvent XdagNotifyMsg");
                break;
            case XdagEvent.en_event_retype_pwd:
                xdagWrapper.XdagNotifyMsg(repwd);
                break;
            case XdagEvent.en_event_set_rdm:
                xdagWrapper.XdagNotifyMsg(keyword);
                createTime = System.currentTimeMillis();
            case XdagEvent.en_event_update_state:
                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {

                    if (event.address != null && !TextUtils.isEmpty(event.address)) {
                        ModelAdapter<XdagWalletModel> adapter = FlowManager.getModelAdapter(XdagWalletModel.class);
                        XdagWalletModel wallet = new XdagWalletModel();
                        wallet.setAddress(event.address);
                        wallet.setAmount(0);
                        wallet.setDeleted(false);
                        wallet.setIcon("");
                        wallet.setName(et_create_wallet_name.getText().toString());
                        wallet.setLocalPath("");
                        wallet.setBankPath("");
                        wallet.setType(0);
                        try {
                            adapter.insert(wallet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (event.isConnect) {
                            if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                                xdagProgressDialog.dismiss();
                            }

                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                }
                break;
        }
    }

    private void initViews() {
        ckb_service_srivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_create_wallet.setEnabled(isChecked);
            }
        });
        btn_create_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = et_create_wallet_pwd.getText().toString();
                repwd = et_create_wallet_repwd.getText().toString();
                keyword = et_create_wallet_keyword.getText().toString();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd) || TextUtils.isEmpty(keyword)) {
                    ToastUtil.showShort(CreateWalletActivity.this, getString(R.string.name_is_empty));
                    return;
                }
                if (!pwd.equals(repwd)) {
                    ToastUtil.showShort(CreateWalletActivity.this, getString(R.string.pwd_not_same));
                    return;
                }
                Log.i(TAG, "start create wallet: pwd " + pwd + " repwd: " + repwd + " keyword: " + keyword);
                initXdagFiles();
                boolean isStartCreate = connectToPool();
                if(isStartCreate){
                    Log.i(TAG, "isStartCreate"+isStartCreate);
                }else {
                    Log.i(TAG, "isStartCreate  error"+isStartCreate);
                }
                if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                    xdagProgressDialog.dismiss();
                }
                xdagProgressDialog = new XdagProgressDialog(CreateWalletActivity.this, getString(R.string.creating_wallet));
                xdagProgressDialog.setProgressInfo(getString(R.string.creating_wallet_tips));
                xdagProgressDialog.show();
            }
        });
    }

    private boolean connectToPool() {
        String poolAddr = getSharedPreferences(Constants.SPSetting, Context.MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS, Constants.DefaultPoolAddress);
//        String poolAddr = Constants.DefaultPoolAddress;
        if (mService != null) {
             mService.startToCreateWallet(poolAddr);
            return true;
        }
        return false;
    }

    private void initXdagFiles() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_create_wallet_name = (EditText) findViewById(R.id.et_create_wallet_name);
        et_create_wallet_pwd = (EditText) findViewById(R.id.et_create_wallet_pwd);
        et_create_wallet_repwd = (EditText) findViewById(R.id.et_create_wallet_repwd);
        et_create_wallet_keyword = (EditText) findViewById(R.id.et_create_wallet_keyword);
        ckb_service_srivacy = (CheckBox) findViewById(R.id.ckb_service_srivacy);
        tv_wallet_notice = (TextView) findViewById(R.id.tv_wallet_notice);
        btn_create_wallet = (Button) findViewById(R.id.btn_create_wallet);
    }

    @Override
    protected void onDestroy() {

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
