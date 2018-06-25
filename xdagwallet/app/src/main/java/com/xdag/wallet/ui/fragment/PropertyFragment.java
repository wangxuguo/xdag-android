package com.xdag.wallet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdag.wallet.AuthDialogFragment;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.ui.activity.MineAddressQRActivity;
import com.xdag.wallet.ui.activity.MipcaActivityCapture;
import com.xdag.wallet.ui.activity.SendCoinActivity;
import com.xdag.wallet.ui.widget.XdagProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class PropertyFragment extends BaseFragment implements View.OnClickListener {

    private static final int MSG_CONNECT_TO_POOL = 1;
    private static final int MSG_DISCONNECT_FROM_POOL = 2;
    private static final int MSG_XFER_XDAG_COIN = 3;
    private static final int MSG_SHOW_PROGRESS = 4;
    private static final int MSG_DISSMIS_PROGRESS = 5;


    private static final int REQUESTCODE_SCAN = 0x01;
    private static final int REQUESTCODE_RECEIVE = 0x02;
    private static final String TAG = Constants.TAG;
    private TextView title;
    private TextView sendCoin;
    private TextView receiveCoin;
    private TextView scan;
    private TextView account;
    private TextView accountTxt;
    private TextView xdagAccount;

    private HandlerThread xdagProcessThread;
    private Handler xdagMessageHandler;
    private XdagProgressDialog xdagProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        xdagProcessThread = new HandlerThread("XdagProcessThread");
        xdagProcessThread.start();

        xdagMessageHandler = new Handler(xdagProcessThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG,"handleMessage  "+msg.what);
                switch (msg.arg1){
                    case MSG_CONNECT_TO_POOL:
                    {
                        Log.i(TAG,"receive msg connect to the pool thread id " + Thread.currentThread().getId());
                        Bundle data = msg.getData();
                        String poolAddr = data.getString("pool");
                        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
                        xdagWrapper.XdagConnectToPool(poolAddr);
                        xdagMessageHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);
                    }
                    break;
                    case MSG_DISCONNECT_FROM_POOL:
                    {

                    }
                    break;
                    case MSG_XFER_XDAG_COIN:
                    {

                    }
                    break;
                    case MSG_DISSMIS_PROGRESS:
                        xdagProgressDialog.dismiss();
                    case MSG_SHOW_PROGRESS:
                        XdagProgressDialog.Builder builder = new XdagProgressDialog.Builder(getActivity());
                        builder.setMessage(getString(R.string.wallet_connecting_to_pool));
                        if(xdagProgressDialog!=null&&xdagProgressDialog.isShowing()){
                            xdagProgressDialog.dismiss();
                            xdagProgressDialog = null;
                        }
                        xdagProgressDialog =  builder.create();
                        xdagProgressDialog.show();
                        break;
                    default:
                    {
                        Log.e(TAG,"unkown command from ui");
                    }
                    break;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_property, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        title = (TextView) rootView.findViewById(R.id.title);
        sendCoin = (TextView) rootView.findViewById(R.id.tv_send_coin);
        receiveCoin = (TextView) rootView.findViewById(R.id.tv_receive_coin);
        scan = (TextView) rootView.findViewById(R.id.tv_scan);
        account = (TextView) rootView.findViewById(R.id.tv_account);
        accountTxt = (TextView) rootView.findViewById(R.id.tv_account_txt);
        xdagAccount = (TextView) rootView.findViewById(R.id.tv_xdag_account);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initXdagFiles();
        connectToPool();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        Log.i(TAG,"process msg in Thread " + Thread.currentThread().getId());
        Log.i(TAG,"event event type is " + event.eventType);
        Log.i(TAG,"event account is " + event.address);
        Log.i(TAG,"event balace is " + event.balance);
        Log.i(TAG,"event state is " + event.state);

        switch (event.eventType){
            case XdagEvent.en_event_type_pwd:
            case XdagEvent.en_event_set_pwd:
            case XdagEvent.en_event_retype_pwd:
            case XdagEvent.en_event_set_rdm:
            {
                //show dialog and ask user to type in password
                AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                authDialogFragment.setAuthHintInfo(GetAuthHintString(event.eventType));
                authDialogFragment.show(getActivity().getFragmentManager(), "Auth Dialog");
            }
            break;

            case XdagEvent.en_event_update_state:
            {
                xdagMessageHandler.sendEmptyMessage(MSG_DISSMIS_PROGRESS);
                Log.i(TAG,"update xdag  ui ");
                account.setText(event.balance);
                xdagAccount.setText(event.balance);
//                if(is)
//                tvBalance.setText(event.balance);
//                tvStatus.setText(event.state);
            }
            break;

        }
    }
    private String GetAuthHintString(final int eventType){
        switch (eventType){
            case XdagEvent.en_event_set_pwd:
                return "set password";
            case XdagEvent.en_event_type_pwd:
                return "input password";
            case XdagEvent.en_event_retype_pwd:
                return "retype password";
            case XdagEvent.en_event_set_rdm:
                return "set random keys";
            default:
                return "input password";
        }
    }
    private void connectToPool() {
//        String poolAddr = getContext().getSharedPreferences(Constants.SPSetting, Context.MODE_PRIVATE).getString(Constants.XDAG_POOL_ADDRESS,Constants.DefaultPoolAddress);
        String poolAddr = Constants.DefaultPoolAddress;
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString("pool",poolAddr);
        msg.arg1 = MSG_CONNECT_TO_POOL;
        msg.setData(data);
        xdagMessageHandler.sendMessage(msg);
//        XdagProgressDialog.Builder builder = new XdagProgressDialog.Builder(getActivity());
//        builder.setMessage(getString(R.string.wallet_connecting_to_pool));
//        if(xdagProgressDialog!=null&&xdagProgressDialog.isShowing()){
//            xdagProgressDialog.dismiss();
//            xdagProgressDialog = null;
//        }
//        xdagProgressDialog =  builder.create();
//        xdagProgressDialog.show();
    }

    private void initXdagFiles() {
        String xdagFolderPath = "/sdcard/xdag";
        File file = new File(xdagFolderPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private void initViews() {
        title.setText(getString(R.string.tab_main_property));
        xdagAccount.setText(getString(R.string.connect_to_pool));
        accountTxt.setText(getString(R.string.total_assets) + "(" + getString(R.string.currency_symbol) + ")");
        account.setText(getString(R.string.connect_to_pool));

        sendCoin.setOnClickListener(this);
        receiveCoin.setOnClickListener(this);
        scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_coin:
                Intent intent = new Intent(getActivity(),SendCoinActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_receive_coin:
                Intent receive = new Intent(getActivity(),MineAddressQRActivity.class);
                startActivityForResult(receive,REQUESTCODE_SCAN);
                break;
            case R.id.tv_scan:
                Intent scan = new Intent(getActivity(),MipcaActivityCapture.class);
                startActivityForResult(scan,REQUESTCODE_SCAN);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUESTCODE_SCAN:
                if(resultCode == RESULT_OK) {
                    if (data != null) {
                        String resultString = data.getStringExtra("result");
                        Log.e(Constants.TAG, "scan resultString: " + resultString);
                    }
                }
                break;
            case REQUESTCODE_RECEIVE:
                if(resultCode == RESULT_OK) {

                }
                break;
        }
    }
}
