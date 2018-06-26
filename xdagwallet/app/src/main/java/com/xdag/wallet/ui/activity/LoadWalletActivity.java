package com.xdag.wallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.FileUtils;
import com.xdag.wallet.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class LoadWalletActivity extends BaseActivity {
    private static final int REQUESTCODE_FROM_ACTIVITY = 1000;

    private static final int MSG_CONNECT_TO_POOL = 1;
    private static final int MSG_DISCONNECT_FROM_POOL = 2;
    private static final int MSG_XFER_XDAG_COIN = 3;
    private static final int MSG_SHOW_PROGRESS = 4;
    private static final int MSG_DISSMIS_PROGRESS = 5;

    private static final String TAG = Constants.TAG;
    ImageView iv_title_left;
    TextView tv_title;
    TextView tv_back_file_info;
    EditText et_create_wallet_pwd;
    EditText et_create_wallet_repwd;
    CheckBox ckb_service_srivacy;
    TextView tv_wallet_notice;
    Button btn_choose_file;
    Button btn_load_wallet;
    String wallet_file = null;
    String dnet_key_file = null;
    private boolean isCopied;
    private XdagProgressDialog xdagProgressDialog;
    private HandlerThread xdagProcessThread;
    private Handler xdagHandler;
    private String repwd;
    private String pwd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_load_wallet);
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
        findViews();
        initViews();
    }
    private void initXdagFiles() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    private void initViews() {
        ckb_service_srivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_load_wallet.setEnabled(isChecked);
            }
        });
        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(LoadWalletActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withStartPath(FileUtils.rootPath)
//                        .withIsGreater(false)
                        .withTitle(getString(R.string.choose_wallet_file))//标题文字
//                        .withTitleColor("#FF99CC")//文字颜色
                        .withMutilyMode(true)
                        .withIconStyle(Constant.ICON_STYLE_BLUE)
//                        .withFileSize(500 * 1024)
                        .start();
            }
        });
        btn_load_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd  = et_create_wallet_pwd.getText().toString();
                repwd  = et_create_wallet_repwd.getText().toString();
                if(!ckb_service_srivacy.isChecked()){
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.please_grant_permission));
                    return;
                }

                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(repwd)){
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.name_is_empty));
                    return;
                }
                if(!pwd.equals(repwd)){
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.pwd_not_same));
                }
                initXdagFiles();
                int walletResult = FileUtils.copyFile(wallet_file,FileUtils.WALLET_FILE);
                int dnetKeyResult = FileUtils.copyFile(dnet_key_file,FileUtils.DNET_KEY_FILE);
                if(dnetKeyResult == 0 && walletResult == 0){
                    isCopied = true;
                    if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                        xdagProgressDialog.dismiss();
                    }
                    xdagProgressDialog =  new XdagProgressDialog(LoadWalletActivity.this,getString(R.string.loading_wallet));
                    xdagProgressDialog.setProgressInfo(getString(R.string.loading_wallet_tips));
                    xdagProgressDialog.show();
                    connectToPool();
                }else {
                    ToastUtil.showShort(LoadWalletActivity.this,getString(R.string.load_wallet_error));
                }

            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        Log.i(TAG, "process msg in Thread " + Thread.currentThread().getId());
        Log.i(TAG, "event event type is " + event.eventType);
        Log.i(TAG, "event account is " + event.address);
        Log.i(TAG, "event balace is " + event.balance);
        Log.i(TAG, "event state is " + event.state);

        switch (event.eventType) {
            case XdagEvent.en_event_type_pwd:
                xdagWrapper.XdagNotifyMsg(pwd);
                break;
            case XdagEvent.en_event_set_pwd:
                if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                    xdagProgressDialog.dismiss();
                }
                ToastUtil.showShort(this,getString(R.string.load_wallet_error));
                break;
            case XdagEvent.en_event_retype_pwd:
                xdagWrapper.XdagNotifyMsg(repwd);
                break;
            case XdagEvent.en_event_set_rdm:
                ToastUtil.showShort(this,getString(R.string.load_wallet_error));
                break;

            case XdagEvent.en_event_update_state:

                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {
                    if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                        xdagProgressDialog.dismiss();
                    }
                    if(event.address!=null&&!TextUtils.isEmpty(event.address)){
                        ModelAdapter<XdagWalletModel> adapter = FlowManager.getModelAdapter(XdagWalletModel.class);
                        XdagWalletModel wallet = new XdagWalletModel();
                        wallet.setAddress(event.address);
                        wallet.setAmount(0);
                        wallet.setDeleted(false);
                        wallet.setIcon("");
                        String filepathName = new File(wallet_file).getParent();
                        wallet.setName(filepathName);
                        wallet.setLocalPath("");
                        wallet.setBankPath("");
                        wallet.setWalletMd5(wallet_file);
                        wallet.setType(3);
                        adapter.insert(wallet);
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                }else {
                }
                Log.i(TAG, "update xdag  ui ");
                break;

        }
    }
    private void connectToPool() {
//        String poolAddr = getContext().getSharedPreferences(Constants.SPSetting, Context.MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS,Constants.DefaultPoolAddress);
        String poolAddr = Constants.DefaultPoolAddress;
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString("pool", poolAddr);
        msg.arg1 = MSG_CONNECT_TO_POOL;
        msg.setData(data);
        xdagHandler.sendMessage(msg);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);

                for(int i =0 ; i < list.size();i ++){
                    String str = list.get(i);
                    if(str.endsWith(File.separator+FileUtils.WALLET_NAME)){
                        wallet_file = str;
                    }
                    if(str.endsWith(File.separator+FileUtils.DNET_KEY_NAME)){
                        dnet_key_file = str;
                    }

                }
                if(wallet_file!=null&&dnet_key_file!=null){
                    String path =  new File(wallet_file).getPath();
                    tv_back_file_info.setText(wallet_file);
                }

//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back_file_info = (TextView) findViewById(R.id.tv_back_file_info);
        et_create_wallet_pwd = (EditText) findViewById(R.id.et_create_wallet_pwd);
        et_create_wallet_repwd = (EditText) findViewById(R.id.et_create_wallet_repwd);
        ckb_service_srivacy = (CheckBox) findViewById(R.id.ckb_service_srivacy);
        tv_wallet_notice = (TextView) findViewById(R.id.tv_wallet_notice);
        btn_load_wallet = (Button) findViewById(R.id.btn_load_wallet);
        btn_choose_file = (Button) findViewById(R.id.btn_choose_file);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
