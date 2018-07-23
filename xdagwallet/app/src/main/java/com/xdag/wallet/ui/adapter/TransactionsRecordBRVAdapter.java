package com.xdag.wallet.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.xdag.wallet.model.XdagTransactionModel;

import java.util.List;

/**
 * Created by wangxuguo on 2018/7/10.
 */
public class TransactionsRecordBRVAdapter extends MultipleItemRvAdapter<XdagTransactionModel, BaseViewHolder> {

    public static final int TYPE_TIME = 100;
    public static final int TYPE_NORMAL = 200;
    public TransactionsRecordBRVAdapter(@Nullable List<XdagTransactionModel> data) {
        super(data);
        finishInitialize();
    }

    @Override
    protected int getViewType(XdagTransactionModel xdagTransactionModel) {
        if(xdagTransactionModel.getPage() == -1){
            return TYPE_TIME;
        }
        return TYPE_NORMAL;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new TransactionsRecordTimeProvider());
        mProviderDelegate.registerProvider(new TransactionsRecordNormalProvider());
    }
}
