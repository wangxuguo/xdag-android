package com.xdag.wallet.ui.adapter;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.xdag.wallet.R;
import com.xdag.wallet.model.XdagTransactionModel;

import static com.xdag.wallet.ui.adapter.TransactionsRecordBRVAdapter.TYPE_TIME;

/**
 * Created by wangxuguo on 2018/7/10.
 */

public class TransactionsRecordTimeProvider extends BaseItemProvider {
    @Override
    public int viewType() {
        return TYPE_TIME;
    }

    @Override
    public int layout() {
        return R.layout.rv_item_transaction_time;
    }

    @Override
    public void convert(BaseViewHolder helper, Object data, int position) {
        if(data instanceof XdagTransactionModel){
            XdagTransactionModel model = (XdagTransactionModel) data;
            helper.setText(R.id.tv_transaction_time,model.getUTCTime());
        }
    }
}
