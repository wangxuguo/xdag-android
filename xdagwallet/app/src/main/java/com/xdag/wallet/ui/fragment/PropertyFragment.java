package com.xdag.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.ui.activity.SendCoinActivity;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class PropertyFragment extends BaseFragment implements View.OnClickListener {
    private TextView title;
    private TextView sendCoin;
    private TextView receiveCoin;
    private TextView scan;
    private TextView account;
    private TextView accountTxt;
    private TextView xdagAccount;

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
                Intent intent = new Intent(getActivity(),SendCoinActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_receive_coin:
                break;
            case R.id.tv_scan:
                break;
        }
    }
}
