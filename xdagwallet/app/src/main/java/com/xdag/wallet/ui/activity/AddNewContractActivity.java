package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;
import com.xdag.wallet.utils.ToastUtil;

/**
 *
 */
public class AddNewContractActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_title_left;
    TextView tv_title;
    TextView tv_title_right;
    EditText etName;
    EditText etAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contract);
        findViews();
        initViews();

    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        etName = (EditText) findViewById(R.id.et_name);
        etAddress = (EditText) findViewById(R.id.et_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent();
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show(this, R.string.name_is_empty);
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.show(this, R.string.address_is_empty);
                    return;
                }
                XdagContactsModel user = new XdagContactsModel();
                user.name = name;
                user.icon = "";
                user.address = address;

                ModelAdapter<XdagContactsModel> adapter = FlowManager.getModelAdapter(XdagContactsModel.class);
                adapter.insert(user);
                adapter.save(user);
                intent.putExtra(Constants.CONTRACT_NAME, name);
                intent.putExtra(Constants.CONTRACT_ADDRESS, address);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
