package com.xdag.wallet.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 *
 */
public class XdagPwdConfirmPopWindow extends PopupWindow implements View.OnClickListener, Animation.AnimationListener {
    private Context mContext;
    private Animation mIn_PopupwindAnimation;
    private Animation mOut_PopupwindAnimation;
    private String my_address;
    private String address;
    private double account;
    private RelativeLayout mMain;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            dismiss();
        }
    };
    private ImageView ivClose;
    private TextView tvOperationInformation;
    private TextView tvReceiveAddress;
    private TextView tvSendAddress;
    private TextView tvAmount;
    private Button btnConfirm;


    public XdagPwdConfirmPopWindow(Context context, String address, String my_address, double account) {
        super(context);
        mContext = context;
        this.address = address;
        this.my_address = my_address;
        this.account = account;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        mIn_PopupwindAnimation = AnimationUtils.loadAnimation(mContext, R.anim.in_popupwind);
        mOut_PopupwindAnimation = AnimationUtils.loadAnimation(mContext, R.anim.out_popupwindow);
        initView();
        setListener();
    }

    private void initView() {
        findView();
        // 添加菜单视图
        this.setContentView(mMain);
        tvOperationInformation.setText(mContext.getString(R.string.receive_coin));
        tvReceiveAddress.setText(address);
        tvSendAddress.setText(my_address);
        tvAmount.setText(account+""+mContext.getString(R.string.xdag));
        btnConfirm.setOnClickListener(this);
    }

    private void findView() {
        mMain = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_confirm_pay, null);

        ivClose = (ImageView) mMain.findViewById(R.id.iv_close);
        tvOperationInformation = (TextView) mMain.findViewById(R.id.tv_operation_information);
        tvReceiveAddress = (TextView) mMain.findViewById(R.id.tv_receive_address);
        tvSendAddress = (TextView) mMain.findViewById(R.id.tv_send_address);
        tvAmount = (TextView) mMain.findViewById(R.id.tv_amount);
        btnConfirm = (Button) mMain.findViewById(R.id.btn_confirm);
    }

    private void setListener() {
        mOut_PopupwindAnimation.setAnimationListener(this);
    }

    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        mMain.startAnimation(mIn_PopupwindAnimation);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:

                break;
        }
    }

    public void dismissAnimation() {
        mMain.startAnimation(mOut_PopupwindAnimation);
    }


}
