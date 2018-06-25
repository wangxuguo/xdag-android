package com.xdag.wallet.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.AuthDialogFragment;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagEvent;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.utils.DensityUtils;
import com.xdag.wallet.utils.ToastUtil;
import com.xdag.wallet.zxing.encoding.ZXingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *
 */
public class MineAddressQRActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = Constants.TAG;
    ImageView iv_title_left;
    TextView tv_title;
    ImageView iv_qr;
    TextView tv_xdag_address;
    Button btn_copy_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_address_qr);
        findViews();
        initViews();

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
            break;
            case XdagEvent.en_event_update_state:
            {
                Log.i(TAG,"update xdag  ui ");
                tv_xdag_address.setText(event.address);
//                tvBalance.setText(event.balance);
//                tvStatus.setText(event.state);
            }
            break;

        }
    }
    private void initViews() {
        iv_title_left.setOnClickListener(this);
        btn_copy_address.setOnClickListener(this);
        Bitmap bitmap = ZXingUtils.createQRImage("adka;dfjakd", DensityUtils.dp2px(this, 200), DensityUtils.dp2px(this, 200));

        iv_qr.setImageBitmap(bitmap);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_qr = (ImageView) findViewById(R.id.iv_qr);
        tv_xdag_address = (TextView) findViewById(R.id.tv_xdag_address);
        btn_copy_address = (Button) findViewById(R.id.btn_copy_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;

            case R.id.btn_copy_address:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                ClipData clipData = ClipData.newPlainText("simple text copy", tv_xdag_address.getText());
                //添加ClipData对象到剪切板中
                if(clipboardManager!=null) {
                    clipboardManager.setPrimaryClip(clipData);
                    ToastUtil.showShort(this,getString(R.string.copyed));
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
