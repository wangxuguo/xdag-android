package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;
import com.xdag.wallet.model.XdagTransactionModel_Table;
import com.xdag.wallet.transaction.TransactionManager;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.TransactionsRecordAdapter;
import com.xdag.wallet.ui.adapter.TransactionsRecordBRVAdapter;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    RecyclerView recyclerView;
    private TransactionManager transactionManager;
    private XdagProgressDialog xdagProgressDialog;
    private List<XdagTransactionModel> list = new ArrayList<>();
    private TransactionsRecordBRVAdapter adapter;
//    private TransactionsRecordAdapter adapter;
    private String address;
    private int curOffet = 0;
    private View footerView;
    private ProgressBar footProgressBar;
    private TextView footPrompt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.XDAG_ADDRESS)) {
            address = getIntent().getStringExtra(Constants.XDAG_ADDRESS);
        }
        findViews();
        initViews();
        transactionManager = TransactionManager.getInstance();
        transactionManager.setOnNewRecordAddListener(new TransactionManager.OnNewRecordAddListener() {
            @Override
            public void OnNewRecordAddListener() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        List<XdagTransactionModel> li = SQLite.select()
                                .from(XdagTransactionModel.class)
                                .where(XdagTransactionModel_Table.mine.eq(address) )
                                .orderBy(XdagTransactionModel_Table.time, false)
                                .limit(20)
                                .queryList();
                        curOffet = 20;
                        addListDateStr(li);
                        if (li != null) {
                            list.addAll(li);
                            adapter.setNewData(list);
                        }
                        if (adapter == null) {
                            adapter = new TransactionsRecordBRVAdapter(list);
                            recyclerView.setAdapter(adapter);
                        }
                        if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                            xdagProgressDialog.dismiss();
                        }
                    }
                });
            }
        });
        ModelAdapter<XdagTransactionModel> modelAdapter = FlowManager.getModelAdapter(XdagTransactionModel.class);
        xdagProgressDialog = new XdagProgressDialog(this, getString(R.string.loading_transaction_record));
        xdagProgressDialog.show();
        List<XdagTransactionModel> li = SQLite.select()
                .from(XdagTransactionModel.class)
                .orderBy(XdagTransactionModel_Table.time, false)
                .limit(20)
                .queryList();
        curOffet = 20;
        if (li != null && li.size() > 0) {
            if (xdagProgressDialog != null && xdagProgressDialog.isShowing()) {
                xdagProgressDialog.dismiss();
            }
            addListDateStr(li);
            if (li != null) {
                list.addAll(li);
            }
            int maxPage = li.get(0).getPage();
            transactionManager.checkAddressRecord(address, maxPage <= 0 ? 0 : maxPage);
        } else {
            transactionManager.checkAddressRecord(address, 0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionsRecordBRVAdapter(list);
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<XdagTransactionModel>() {
//            @Override
//            public void onItemClick(View view, XdagTransactionModel data) {
//                Intent intent = new Intent(TransactionRecordActivity.this, TransactionRecordDetailActivity.class);
//                intent.putExtra(Constants.XDAG_ADDRESS, address);
//                intent.putExtra(Constants.XDAG_TRANSACTION_MODEL, data);
//                startActivity(intent);
//            }
//        });
//        adapter.setLoadMore();
//        adapter.setOnLoadMoreClickListener(loadMoreListener);
        footerView = getFooterView(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<XdagTransactionModel> li = SQLite.select()
                        .from(XdagTransactionModel.class)
                        .orderBy(XdagTransactionModel_Table.time, false)
                        .limit(20)
                        .offset(curOffet)
                        .queryList();
                curOffet += 20;
                addListDateStr(li);
                if (li != null&&li.size()>0) {
                    list.addAll(li);
                    adapter.addData(li);
                }
                if (li != null && li.size() == 20) {

                }else {
                    if(footPrompt!=null){
                        footPrompt.setText(R.string.load_no_more);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.addFooterView(footerView, 0);
        if (li != null && li.size() == 20) {

        }else {
            if(footPrompt!=null){
                footPrompt.setText(R.string.load_no_more);
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(adapter!=null){
//            adapter.setLoadMore();
//        }
//    }

    private View getFooterView(int type, View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.rv_item_footer, (ViewGroup) recyclerView.getParent(), false);
        footProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        footPrompt = (TextView) view.findViewById(R.id.tv_prompt);
        footPrompt.setVisibility(View.VISIBLE);
        footPrompt.setText(getString(R.string.loading_more));
        view.setOnClickListener(listener);
        return view;
    }
    private List<XdagTransactionModel> addListDateStr(List<XdagTransactionModel> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        long curMonthMaxMills = calendar.getTimeInMillis();
//        calendar.setTimeInMillis(list.get(0).getTime());
//        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getMinimum(Calendar.DAY_OF_MONTH),0,0,0);

        XdagTransactionModel model1 = new XdagTransactionModel(0, "", "", DateTimeUtils.getFORMAT_yyyy_MM(calendar.getTime()));
        long monthMinMills = DateTimeUtils.getMinMillsForCurMonth(calendar);
        //DateTimeUtils.getPreOneMonthByTimMills(curMonthMaxMills);// DateTimeUtils.getPreOneMonthByTimMills(curMonthMills);
        model1.setPage(-1);
        model1.setTime(0);
        list.add(0, model1);
        for (int i = 2; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).getTime() > 0) {
                XdagTransactionModel model = list.get(i);
                long modelTime = model.getTime();
                if (modelTime >= monthMinMills && modelTime <= curMonthMaxMills) {

                } else {
                    while (modelTime < monthMinMills || modelTime > curMonthMaxMills) {
                        calendar.add(Calendar.MONTH, -1);
                        curMonthMaxMills = DateTimeUtils.getMaxMillsForCurMonth(calendar);
                        monthMinMills = DateTimeUtils.getMinMillsForCurMonth(calendar);
                    }
                    XdagTransactionModel modelAdd = new XdagTransactionModel(0, "", "", DateTimeUtils.getFORMAT_yyyy_MM(calendar.getTime()));
                    modelAdd.setPage(-1);
                    modelAdd.setTime(0);
                    list.add(i, modelAdd);
                    i++;
                }
            }
        }
        return list;
    }

    private void initViews() {
        iv_title_left.setOnClickListener(this);
        tv_title.setText(R.string.transaction_record);

    }

    private void findViews() {
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
        }
    }
}