package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Operator;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;
import com.xdag.wallet.transaction.GetTranscationAddressRunnable;
import com.xdag.wallet.ui.widget.XdagProgressDialog;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import static com.xdag.wallet.transaction.GetTranscationAddressRunnable.GETTRANSACTIONADDRESS;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class TransactionRecordDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final int GETTRANSACTIONADDRESSTIMEOUT = 0x31;
    private ImageView ivTitleLeft;
    TextView tvTitle;
    TextView tvAmount;
    TextView tvTransactionState;
    TextView tvTransactionSender;
    TextView tvTransactionReceiver;
    TextView tvTransactionFee;
    TextView tvTransactionTime;
    TextView tvTransactionHash;
    private XdagTransactionModel xdagTransaction;
    private String address;
    private Handler handler;
    private XdagProgressDialog xdagProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record_detail);
        findViews();
        initViews();

    }

    private void initViews() {
        ivTitleLeft.setOnClickListener(this);
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GETTRANSACTIONADDRESS:
                        XdagTransactionModel model = (XdagTransactionModel) msg.obj;
                        if (model != null) {
                            tvTransactionHash.setText(model.getHash());
                            if (!TextUtils.isEmpty(xdagTransaction.getMethod()) && "input".equalsIgnoreCase(xdagTransaction.getMethod())) {
                                tvTransactionSender.setText(xdagTransaction.getAddress());
                            } else {
                                tvTransactionReceiver.setText(xdagTransaction.getAddress());
                            }
                        }
                        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                            xdagProgressDialog.dismiss();
                        }

                        break;
                    case GETTRANSACTIONADDRESSTIMEOUT:
                        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                            xdagProgressDialog.dismiss();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        if (intent != null) {
            xdagTransaction = (XdagTransactionModel) intent.getSerializableExtra(Constants.XDAG_TRANSACTION_MODEL);
            address = intent.getStringExtra(Constants.XDAG_ADDRESS);
            DecimalFormat df = new DecimalFormat("0.#########");
            tvAmount.setText(df.format(xdagTransaction.getAmount()) + " ");
            tvTransactionState.setText(getString(R.string.success));
            if (!TextUtils.isEmpty(xdagTransaction.getMethod()) && "input".equalsIgnoreCase(xdagTransaction.getMethod())) {
                tvTransactionSender.setText(xdagTransaction.getAddress());
                tvTransactionReceiver.setText(address);
            } else {
                tvTransactionSender.setText(address);
                tvTransactionReceiver.setText(xdagTransaction.getAddress());
            }
            tvTransactionFee.setText(df.format(xdagTransaction.getFee()) + "   ");
            tvTransactionHash.setText(xdagTransaction.getHash());
            tvTransactionTime.setText(xdagTransaction.getUTCTime());
            if (xdagTransaction.getBlock() != null && xdagTransaction.getAddress().equals(xdagTransaction.getBlock())) {
                xdagProgressDialog = new XdagProgressDialog(this, getString(R.string.loading_transaction_record));
                xdagProgressDialog.show();
                new Thread(new GetTranscationAddressRunnable(address, xdagTransaction, handler)).start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(GETTRANSACTIONADDRESSTIMEOUT);
                    }
                },3000);
            }
        }
    }

    private void findViews() {
        ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvTransactionState = (TextView) findViewById(R.id.tv_transaction_state);
        tvTransactionSender = (TextView) findViewById(R.id.tv_transaction_sender);
        tvTransactionReceiver = (TextView) findViewById(R.id.tv_transaction_receiver);
        tvTransactionFee = (TextView) findViewById(R.id.tv_transaction_fee);
        tvTransactionHash = (TextView) findViewById(R.id.tv_transaction_hash);
        tvTransactionTime = (TextView) findViewById(R.id.tv_transaction_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeMessages(GETTRANSACTIONADDRESSTIMEOUT);
            handler = null;
        }
    }
}