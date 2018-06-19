package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.ui.widget.XdagPwdConfirmPopWindow;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SendCoinActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUESTCODE_SCAN = 0x01;
    private static final int REQUESTCODE_RECEIVE = 0x02;
    ImageView ivTitleLeft;
    TextView tvTitle;
    ImageView ivTitleRight;
    EditText etReceiverAddress;
    EditText etTransferAccount;
    EditText etTransferNote;
    Button btnNext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_coin);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        findViews();
        initViews();
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

    private void findViews() {

        ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivTitleRight = (ImageView) findViewById(R.id.iv_title_right);
        etReceiverAddress = (EditText) findViewById(R.id.et_receiver_address);
        etTransferAccount = (EditText) findViewById(R.id.et_transfer_account);
        etTransferNote = (EditText) findViewById(R.id.et_transfer_note);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                Intent receive = new Intent(this,MineAddressQRActivity.class);
                startActivityForResult(receive,REQUESTCODE_SCAN);
                break;
            case R.id.btn_next:
                XdagPwdConfirmPopWindow window = new XdagPwdConfirmPopWindow(SendCoinActivity.this,"dd","aa",0.2f);
                window.showAtLocation(btnNext, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
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
                        etReceiverAddress.setText(resultString);
                        etReceiverAddress.setEnabled(false);
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
