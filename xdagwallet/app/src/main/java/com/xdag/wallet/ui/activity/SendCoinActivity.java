package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.AuthDialogFragment;
import com.xdag.wallet.MyActivityManager;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.XdagWrapper;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagState;
import com.xdag.wallet.ui.widget.XdagConfirmXferInfoPopWindow;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.StringUtils;
import com.xdag.wallet.utils.ToastUtil;
import com.xdag.wallet.utils.XdagUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SendCoinActivity extends XdagBaseActivity implements View.OnClickListener, AuthDialogFragment.AuthInputListener {
    private static final int REQUESTCODE_SCAN = 0x01;
    private static final int REQUESTCODE_RECEIVE = 0x02;
    private static final String TAG = Constants.TAG;
    ImageView ivTitleLeft;
    TextView tvTitle;
    ImageView ivTitleRight;
    EditText etReceiverAddress;
    EditText etTransferAccount;
    //    EditText etTransferNote;
    Button btnNext;
    private XdagProgressDialog xdagProgressDialog;
    private XdagConfirmXferInfoPopWindow.OkListener listener = new XdagConfirmXferInfoPopWindow.OkListener() {
        @Override
        public void onClick(String address, double account) {


            if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                xdagProgressDialog.dismiss();
            }
            xdagProgressDialog = new XdagProgressDialog(SendCoinActivity.this, getString(R.string.xdag_sending));
            xdagProgressDialog.show();
            mService.XdagTransferTo(address,String.valueOf(account));
//            XdagWrapper xdagWrapper = XdagWrapper.getInstance();
//            xdagWrapper.XdagXferToAddress(address, String.valueOf(account));
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_send_coin);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        findViews();
        initViews();
        if (intent != null) {
            if (intent.hasExtra(Constants.CONTRACT_ADDRESS)) {
                String address = intent.getStringExtra(Constants.CONTRACT_ADDRESS);
                if (!TextUtils.isEmpty(address)) {
                    etReceiverAddress.setText(address);
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(XdagState event) {
        if(event!=null){
            return;
        }

       if (event.eventType == XdagEvent.en_event_type_pwd || event.eventType == XdagEvent.en_event_type_pwd
                || event.eventType == XdagEvent.en_event_retype_pwd || event.eventType == XdagEvent.en_event_set_rdm) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence("title", XdagUtils.GetAuthHintString(this, event.eventType));
            AuthDialogFragment authDialogFragment = new AuthDialogFragment();
            authDialogFragment.setArguments(bundle);
            authDialogFragment.setCancelable(false);
            authDialogFragment.setAuthHintInfo(XdagUtils.GetAuthHintString(this, event.eventType));
            authDialogFragment.show(getFragmentManager(), "Auth Dialog");
        }else {
           Log.e(TAG,"onEvent  XdagState" +event.toString());
       }


    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void ProcessXdagEvent(XdagEvent event) {
////        Log.i(TAG, "process msg in Thread " + Thread.currentThread().getId()+ "type " + event.eventType+"account "
////                + event.address+"balace " + event.balance+" state " + event.state);
//        if (!MyActivityManager.getInstance().getCurrentActivity().getLocalClassName().equals(this.getLocalClassName())) {
//            Log.i(TAG, "CurrentActivity:" + MyActivityManager.getInstance().getCurrentActivity().getLocalClassName() + "!equals " + this.getLocalClassName());
//            return;
//        }
//        switch (event.eventType) {
//            case XdagEvent.en_event_set_pwd:
////                ToastUtil.showShort(this, getString(R.string.please_check_wallet_file));
////                break;
//            case XdagEvent.en_event_type_pwd:
//            case XdagEvent.en_event_retype_pwd:
//            case XdagEvent.en_event_set_rdm:
//                Bundle bundle = new Bundle();
//                bundle.putCharSequence("title", XdagUtils.GetAuthHintString(this, event.eventType));
//                AuthDialogFragment authDialogFragment = new AuthDialogFragment();
//                authDialogFragment.setArguments(bundle);
//                authDialogFragment.setAuthHintInfo(XdagUtils.GetAuthHintString(this, event.eventType));
//                authDialogFragment.show(getFragmentManager(), "Auth Dialog");
//
//                break;
//
//            case XdagEvent.en_event_update_state:
//                if (event != null && event.balance != null && !event.balance.equals("Not ready")) {
////                    if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
////                        xdagProgressDialog.dismiss();
////                    }
//                } else {
//                }
//                break;
//            case XdagEvent.en_event_nothing_transfer:
//            case XdagEvent.en_event_balance_too_small:
//            case XdagEvent.en_event_invalid_recv_address:
//            case XdagEvent.en_event_xdag_transfered:
//                ToastUtil.showShort(SendCoinActivity.this,getString(R.string.xdag_xfer_error));
//                if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
//                    xdagProgressDialog.dismiss();
//                }
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        tvTitle.setText(getString(R.string.send_coin));
        btnNext.setOnClickListener(this);
        ivTitleLeft.setOnClickListener(this);
        ivTitleRight.setOnClickListener(this);
        etReceiverAddress.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = etReceiverAddress.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > etReceiverAddress.getWidth()
                        - etReceiverAddress.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    onContractsClick();
                }
                return false;
            }
        });
    }

    private void onContractsClick() {
    }

//    @Override
//    public void onAuthInputComplete(String authInfo) {
//        Log.i(Constants.TAG, "auth info is " + authInfo);
//        //notify native thread
//        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
//        xdagWrapper.XdagNotifyMsg(authInfo);
//    }
    @Override
    public void onAuthInputComplete(String authInfo) {
        Log.i(Constants.TAG,"auth info is " + authInfo);
        //notify native thread
        if (mService != null) {
            mService.xdagNotifyMsg(authInfo);
        }
//        Intent intent = new Intent(this,XdagService.class);
//        intent.putExtra(Constants.XDAG_EVENT_TYPE,XdagService.MSG_XdagNotifyMsg);
//        intent.putExtra(Constants.XDAG_NOTIFY_MSG,authInfo);
//        startService(intent);
    }
    private void findViews() {

        ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivTitleRight = (ImageView) findViewById(R.id.iv_title_right);
        etReceiverAddress = (EditText) findViewById(R.id.et_receiver_address);
        etTransferAccount = (EditText) findViewById(R.id.et_transfer_account);
//        etTransferNote = (EditText) findViewById(R.id.et_transfer_note);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                Intent receive = new Intent(this, MipcaActivityCapture.class);
                startActivityForResult(receive, REQUESTCODE_SCAN);
                break;
            case R.id.btn_next:
                String address = etReceiverAddress.getText().toString();
                String my_address = getSharedPreferences(Constants.SPSetting, MODE_PRIVATE).getString(Constants.XDAG_ADDRESS, "");
                String accStr = etTransferAccount.getText().toString();
                double account = 0;
                if (!TextUtils.isEmpty(accStr)&&StringUtils.isNumericDouble(accStr) && StringUtils.isDouble(accStr)) {
                    account = Double.parseDouble(accStr);
                } else {
                    ToastUtil.showShort(SendCoinActivity.this, getString(R.string.please_check_xfer_accout));
                    return;
                }
                if (account == 0) {
                    ToastUtil.showShort(SendCoinActivity.this, getString(R.string.xfer_accout_cannot_zero));
                    return;
                }
                XdagConfirmXferInfoPopWindow window = new XdagConfirmXferInfoPopWindow(SendCoinActivity.this, address, my_address, account, listener);
                window.showAtLocation(btnNext, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_SCAN:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String resultString = data.getStringExtra("result");
                        Log.e(Constants.TAG, "scan resultString: " + resultString);
                        etReceiverAddress.setText(resultString);
                        etReceiverAddress.setEnabled(false);
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
