package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagWalletModel;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.WalletsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class WalletManageActivity extends BaseActivity {
    TextView textView;
    ImageView ivTitleLeft;
    RecyclerView recyclerView;
    List<XdagWalletModel> list = new ArrayList<>();
    private WalletsAdapter adapter;
    private TextView tvLoadWallet;
    private TextView tvCreateWallet;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manage);
        findViews();
        initViews();
        loadData();
    }

    private void loadData() {
        SQLite.select()
                .from(XdagWalletModel.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<XdagWalletModel>() {
                    @Override
                    public void onListQueryResult(QueryTransaction queryTransaction, @NonNull List<XdagWalletModel> l) {
                        if (l != null && l.size() > 0) {
                            list.addAll(l);
                            adapter.setData(list);
                            Log.d(Constants.TAG, "list size: " + list.size());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }

    private void initViews() {
        textView.setText(getString(R.string.wallet_management));
        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new WalletsAdapter(WalletManageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(WalletManageActivity.this));
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<XdagWalletModel>() {
            @Override
            public void onItemClick(View view, XdagWalletModel data) {
                Intent intent = new Intent(WalletManageActivity.this, WalletDetailActivity.class);
                intent.putExtra(Constants.WALLET_DETAIL, data);
                startActivity(intent);
            }
        });

        adapter.setItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener<XdagWalletModel>() {
            @Override
            public void onItemLongClick(View view, XdagWalletModel data) {

            }
        });
        tvCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletManageActivity.this, CreateWalletActivity.class);
                startActivity(intent);
            }
        });
        tvLoadWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletManageActivity.this, LoadWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        textView = (TextView) findViewById(R.id.tv_title);
        tvCreateWallet = (TextView) findViewById(R.id.tv_create_wallet);
        tvLoadWallet = (TextView) findViewById(R.id.tv_load_wallet);
        ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
