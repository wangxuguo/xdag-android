package com.xdag.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xdag.wallet.R;
import com.xdag.wallet.XdagService;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.ui.activity.AboutUs;
import com.xdag.wallet.ui.activity.CurrencyUnitSettingActivity;
import com.xdag.wallet.ui.activity.MultilingualSettingsActivity;
import com.xdag.wallet.ui.activity.TransactionRecordActivity;
import com.xdag.wallet.ui.activity.WalletDetailActivity;
import com.xdag.wallet.ui.activity.WalletManageActivity;
import com.xdag.wallet.ui.activity.XdagMainActivity;
import com.xdag.wallet.ui.activity.XdagPoolSettingActivity;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    TextView tv_title;
    TextView tv_manager_wallet;
    TextView tv_transaction_record;
    LinearLayout li_message_center;
    TextView tv_message_count;
    LinearLayout li_multi_language;
    LinearLayout li_xdag_pool;
    LinearLayout li_currency_unit;
    LinearLayout li_help_center;
    LinearLayout li_about_us;
    LinearLayout li_personal_center;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_manager_wallet = (TextView) rootView.findViewById(R.id.tv_manager_wallet);
        tv_transaction_record = (TextView) rootView.findViewById(R.id.tv_transaction_record);
        li_message_center = (LinearLayout) rootView.findViewById(R.id.li_message_center);
        tv_message_count = (TextView) rootView.findViewById(R.id.tv_message_count);
        li_multi_language = (LinearLayout) rootView.findViewById(R.id.li_multi_language);
        li_currency_unit = (LinearLayout) rootView.findViewById(R.id.li_currency_unit);
        li_xdag_pool = (LinearLayout) rootView.findViewById(R.id.li_xdag_pool);
        li_help_center = (LinearLayout) rootView.findViewById(R.id.li_help_center);
        li_about_us = (LinearLayout) rootView.findViewById(R.id.li_about_us);
        li_personal_center = (LinearLayout) rootView.findViewById(R.id.li_personal_center);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.tab_main_settings));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tv_manager_wallet.setOnClickListener(this);
        tv_transaction_record.setOnClickListener(this);
        li_message_center.setOnClickListener(this);
        tv_message_count.setOnClickListener(this);
        li_multi_language.setOnClickListener(this);
        li_xdag_pool.setOnClickListener(this);
        li_currency_unit.setOnClickListener(this);
        li_help_center.setOnClickListener(this);
        li_about_us.setOnClickListener(this);
        li_personal_center.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_manager_wallet:
                Intent intent = new Intent(getContext(),WalletDetailActivity.class);
                List<XdagWalletModel> list = SQLite.select()
                        .from(XdagWalletModel.class)
                        .queryList();
                XdagService service = ((XdagMainActivity)getActivity()).getService();
                String address;
                double amount;
                if(service!=null){
                    address  = service.getAddress();
                    amount = service.getBalance();
                }else {
                    address = "";
                    amount = 0f;
                }
                if (list != null && list.size() > 0) {
                    XdagWalletModel model = list.get(0);
                    intent.putExtra(Constants.WALLET_DETAIL,model);
                    startActivity(intent);
                }else {
                    XdagWalletModel model = new XdagWalletModel();
                    model.setAddress(address);
                    model.setAmount(amount);
                    model.setName(getString(R.string.default_wallet_name));
                    intent.putExtra(Constants.WALLET_DETAIL,model);
                    startActivity(intent);
                }
                break;
            case R.id.tv_transaction_record:
                Intent transactionRecordIntent = new Intent(getContext(), TransactionRecordActivity.class);
                String address2  = ((XdagMainActivity)getActivity()).getService().getAddress();
                transactionRecordIntent.putExtra(Constants.XDAG_ADDRESS,address2);
                startActivity(transactionRecordIntent);
                break;
            case R.id.li_message_center:
                break;
            case R.id.tv_message_count:
                break;
            case R.id.li_xdag_pool:
                startActivity(new Intent(getContext(), XdagPoolSettingActivity.class));
                break;
            case R.id.li_multi_language:
                startActivity(new Intent(getContext(), MultilingualSettingsActivity.class));
                break;
            case R.id.li_currency_unit:
                startActivity(new Intent(getContext(), CurrencyUnitSettingActivity.class));
                break;
            case R.id.li_help_center:
                break;
            case R.id.li_about_us:
                startActivity(new Intent(getContext(), AboutUs.class));
                break;
            case R.id.li_personal_center:
                break;
        }
    }
}
