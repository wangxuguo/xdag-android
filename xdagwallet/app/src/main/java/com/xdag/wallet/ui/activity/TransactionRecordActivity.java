package com.xdag.wallet.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;
import com.xdag.wallet.model.XdagTransactionModel_Table;
import com.xdag.wallet.net.ApiSubscriberCallBack;
import com.xdag.wallet.net.BlockInfoModel;
import com.xdag.wallet.net.IXdagApi;
import com.xdag.wallet.net.RestClient;
import com.xdag.wallet.transaction.TransactionManager;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.TransactionsRecordAdapter;
import com.xdag.wallet.ui.adapter.TransactionsRecordBRVAdapter;
import com.xdag.wallet.ui.widget.XdagProgressDialog;
import com.xdag.wallet.utils.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_title_left;
    TextView tv_title;
    RecyclerView recyclerView;
//    private TransactionManager transactionManager;
    private XdagProgressDialog xdagProgressDialog;
    private List<XdagTransactionModel> list = new ArrayList<>();
    private TransactionsRecordBRVAdapter adapter;
    //    private TransactionsRecordAdapter adapter;
    private String address;
    private int curOffet = 0;
    private View footerView;
    private ProgressBar footProgressBar;
    private TextView footPrompt;
    private ModelAdapter<XdagTransactionModel> modelAdapter;
    private List<String>  dateStrLists = new ArrayList<>();

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
        Flowable<BlockInfoModel> flowable = RestClient.getService(IXdagApi.class).getXdagInfoModel(address);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<BlockInfoModel, BlockInfoModel>() {
                    @Override
                    public BlockInfoModel apply(BlockInfoModel blockInfoModel) throws Exception {
                        if (!TextUtils.isEmpty(blockInfoModel.getError()) && !TextUtils.isEmpty(blockInfoModel.getMessage())) {
                            Log.e(Constants.TAG, "blockInfoModel error: " + blockInfoModel.toString());
                        } else {
                            Log.e(Constants.TAG, "blockInfoModel: " + blockInfoModel.toString());
                            if (blockInfoModel != null && blockInfoModel.getBlock_as_transaction() != null) {
                                List<BlockInfoModel.BlockAsAddressBean> blocks = blockInfoModel.getBlock_as_address();
                                process(blocks);
                            }
                        }
                        return blockInfoModel;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriberCallBack<BlockInfoModel>() {
                    @Override
                    public void onSuccess(BlockInfoModel blockInfoModel) {
                        if (!TextUtils.isEmpty(blockInfoModel.getError()) && !TextUtils.isEmpty(blockInfoModel.getMessage())) {
                            Log.e(Constants.TAG, "blockInfoModel error: " + blockInfoModel.toString());
                        } else {
                            list.clear();
                            dateStrLists.clear();
                            List<XdagTransactionModel> li = SQLite.select()
                                    .from(XdagTransactionModel.class)
                                    .where(XdagTransactionModel_Table.mine.eq(address))
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
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(Constants.TAG, "onFailure   ");
                        t.printStackTrace();
                    }
                });
        modelAdapter = FlowManager.getModelAdapter(XdagTransactionModel.class);
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
//            int maxPage = li.get(0).getPage();
//            transactionManager.checkAddressRecord(address, maxPage <= 0 ? 0 : maxPage);
        } else {
//            transactionManager.checkAddressRecord(address, 0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionsRecordBRVAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TransactionRecordActivity.this, TransactionRecordDetailActivity.class);
                intent.putExtra(Constants.XDAG_ADDRESS, address);
                intent.putExtra(Constants.XDAG_TRANSACTION_MODEL, list.get(position));
                startActivity(intent);
            }
        });
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
                if (li != null && li.size() > 0) {
                    list.addAll(li);
                    adapter.addData(li);
                }
                if (li != null && li.size() == 20) {

                } else {
                    if (footPrompt != null) {
                        footPrompt.setText(R.string.load_no_more);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.addFooterView(footerView, 0);
        if (li != null && li.size() == 20) {

        } else {
            if (footPrompt != null) {
                footPrompt.setText(R.string.load_no_more);
            }
        }
    }

    private void process(List<BlockInfoModel.BlockAsAddressBean> blocks) {
        if (blocks == null) {
            return;
        }
        for (BlockInfoModel.BlockAsAddressBean block : blocks) {
            XdagTransactionModel xdagTransactionModel = new XdagTransactionModel(Double.parseDouble(block.getAmount()), block.getDirection(), block.getAddress(), block.getTime());
            xdagTransactionModel.setPage(-2);
            xdagTransactionModel.setMine(address);
            xdagTransactionModel.setAddress(block.getAddress());
            String utcTime = block.getTime().substring(0, block.getTime().indexOf("."));
            SimpleDateFormat localFormater = new SimpleDateFormat(DateTimeUtils.NORMAT_FORMAT);
            localFormater.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
            try {
                Date date = localFormater.parse(utcTime);
                xdagTransactionModel.setTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e(Constants.TAG, "GETTRANSACTIONADDRESS  model  " + xdagTransactionModel.toString());

            try {

                Log.e(Constants.TAG, "modelAdapter  insert  " + xdagTransactionModel.toString());
                long id = modelAdapter.insert(xdagTransactionModel);
            }catch (SQLiteConstraintException ex){
                Log.e(Constants.TAG, "modelAdapter  insert  " + xdagTransactionModel.toString());
                ex.printStackTrace();
//                return;
            } catch (Exception e) {
                e.printStackTrace();
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

    private List<XdagTransactionModel> addListDateStr(List<XdagTransactionModel> listModels) {
        if (listModels == null || listModels.size() == 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        long curMonthMaxMills = calendar.getTimeInMillis();
//        calendar.setTimeInMillis(list.get(0).getTime());
//        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getMinimum(Calendar.DAY_OF_MONTH),0,0,0);
        String strDate  = DateTimeUtils.getFORMAT_yyyy_MM(calendar.getTime());
        XdagTransactionModel model1 = new XdagTransactionModel(0, "", "", strDate);
        long monthMinMills = DateTimeUtils.getMinMillsForCurMonth(calendar);
        //DateTimeUtils.getPreOneMonthByTimMills(curMonthMaxMills);// DateTimeUtils.getPreOneMonthByTimMills(curMonthMills);
        model1.setPage(-1);
        model1.setTime(0);
        if(dateStrLists.contains(model1)){

        }else {
            long modelTime = listModels.get(0).getTime();
            if(modelTime >= monthMinMills && modelTime <= curMonthMaxMills){
                dateStrLists.add(strDate);
                listModels.add(0, model1);
            }
        }

        for (int i = 0; i < listModels.size(); i++) {
            if (listModels.get(i) != null && listModels.get(i).getTime() > 0) {
                XdagTransactionModel model = listModels.get(i);
                long modelTime = model.getTime();
                if (modelTime >= monthMinMills && modelTime <= curMonthMaxMills) {

                } else {
                    while (modelTime < monthMinMills || modelTime > curMonthMaxMills) {
                        calendar.add(Calendar.MONTH, -1);
                        curMonthMaxMills = DateTimeUtils.getMaxMillsForCurMonth(calendar);
                        monthMinMills = DateTimeUtils.getMinMillsForCurMonth(calendar);
                    }
                    String str  = DateTimeUtils.getFORMAT_yyyy_MM(calendar.getTime());
                    XdagTransactionModel modelAdd = new XdagTransactionModel(0, "", "",str );
                    modelAdd.setPage(-1);
                    modelAdd.setTime(0);
                    if(dateStrLists.contains(str)){

                    }else {
                        dateStrLists.add(str);
                        listModels.add(i, modelAdd);
                    }
                    i++;
                }
            }
        }
        return listModels;
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