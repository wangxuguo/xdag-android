package com.xdag.wallet.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 * Created by wangxuguo on 2018/6/15.
 */

public class SendCoinActivity extends BaseActivity implements View.OnClickListener {
    ImageView ivTitleLeft;
    TextView tvTitle;
    ImageView ivTitleRight;
    EditText etReceiverAddress;
    EditText etTransferAccount;
    EditText etTransferNote;
    Button btnNext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_send_coin);
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
                break;
            case R.id.iv_title_right:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
