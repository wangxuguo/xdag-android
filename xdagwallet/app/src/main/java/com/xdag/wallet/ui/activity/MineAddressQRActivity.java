package com.xdag.wallet.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.utils.DensityUtils;
import com.xdag.wallet.zxing.encoding.ZXingUtils;

/**
 *
 */
public class MineAddressQRActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_title_left;
    TextView tv_title;
    ImageView iv_qr;
    TextView tv_xdag_address;
    Button btn_copy_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_qr);
        findViews();
        initViews();

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
                break;

            case R.id.btn_copy_address:
                break;
        }
    }
}
