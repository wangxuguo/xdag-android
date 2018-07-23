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
import com.xdag.wallet.XdagService;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagState;
import com.xdag.wallet.ui.activity.MineAddressQRActivity;
import com.xdag.wallet.ui.activity.MipcaActivityCapture;
import com.xdag.wallet.ui.activity.SendCoinActivity;
import com.xdag.wallet.ui.activity.XdagMainActivity;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.XdagUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.NumberFormat;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class PropertyFragment extends BaseFragment implements View.OnClickListener {
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        XdagService service = ((XdagMainActivity) getActivity()).getService();
        if (service != null) {
            double amount = service.getBalance();
            account.setText(amount + "");
            xdagAccount.setText(amount + "");
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
                Intent intent = new Intent(getActivity(), SendCoinActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_receive_coin:
                Intent receive = new Intent(getActivity(), MineAddressQRActivity.class);
                startActivityForResult(receive, REQUESTCODE_RECEIVE);
                break;
            case R.id.tv_scan:
                Intent scan = new Intent(getActivity(), MipcaActivityCapture.class);
                startActivityForResult(scan, REQUESTCODE_SCAN);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagState event) {
        if(event.isConnect){
            account.setText(event.balance);
            xdagAccount.setText(event.balance);
        }else if (event.eventType == XdagEvent.en_event_type_pwd || event.eventType == XdagEvent.en_event_type_pwd
                || event.eventType == XdagEvent.en_event_retype_pwd || event.eventType == XdagEvent.en_event_set_rdm) {
        }


    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void ProcessXdagEvent(XdagEvent event) {
//        switch (event.eventType) {
//            case XdagEvent.en_event_type_pwd:
//            case XdagEvent.en_event_set_pwd:
//            case XdagEvent.en_event_retype_pwd:
//            case XdagEvent.en_event_set_rdm:
//                break;
//            case XdagEvent.en_event_update_state:
//                if (event != null && event.balance != null && !event.balance.equals("Not Ready")) {
//
//                    account.setText(event.balance);
//                    xdagAccount.setText(event.balance);
//                }
//                break;
//
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_SCAN:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String resultString = data.getStringExtra("result");
                        Log.e(Constants.TAG, "scan resultString: " + resultString);
                        Intent intent = new Intent(getActivity(), SendCoinActivity.class);
                        intent.putExtra(Constants.CONTRACT_ADDRESS,resultString);
                        startActivity(intent);
                    }
                }
                break;
            case REQUESTCODE_RECEIVE:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }
}
