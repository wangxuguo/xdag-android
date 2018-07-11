package com.xdag.wallet.ui.adapter;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;

import java.text.DecimalFormat;

import static com.xdag.wallet.ui.adapter.TransactionsRecordBRVAdapter.TYPE_NORMAL;

/**
 * Created by wangxuguo on 2018/7/10.
 */

public class TransactionsRecordNormalProvider extends BaseItemProvider {
    @Override
    public int viewType() {
        return TYPE_NORMAL;
    }

    @Override
    public int layout() {
        return R.layout.item_transaction_record;
    }

    @Override
    public void convert(BaseViewHolder holder, Object obj, int position) {
        if (obj instanceof XdagTransactionModel) {
            XdagTransactionModel data = (XdagTransactionModel) obj;

            Log.d(Constants.TAG, "onBindViewHolder  " + data.toString());
            if (!TextUtils.isEmpty(data.getMethod()) && "input".equalsIgnoreCase(data.getMethod())) {
                holder.setImageResource(R.id.iv_transaction_icon, R.drawable.ic_transaction_input);
                DecimalFormat df = new DecimalFormat("0.#########");
                holder.setText(R.id.tv_xdag_amount, "+" + df.format(data.getAmount()) + " " + mContext.getString(R.string.xdag));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.setTextColor(R.id.tv_xdag_amount, mContext.getColor(R.color.color_transaction_input));
                } else {
                    holder.setTextColor(R.id.tv_xdag_amount, mContext.getResources().getColor(R.color.color_transaction_input));
                }
            } else {
                holder.setImageResource(R.id.iv_transaction_icon, R.drawable.ic_transaction_output);
                DecimalFormat df = new DecimalFormat("0.#########");
                holder.setText(R.id.tv_xdag_amount, "-" + df.format(data.getAmount()) + " " + mContext.getString(R.string.xdag));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.setTextColor(R.id.tv_xdag_amount, mContext.getColor(R.color.color_transaction_output));
                } else {
                    holder.setTextColor(R.id.tv_xdag_amount, mContext.getResources().getColor(R.color.color_transaction_output));
                }
            }
//            if (position == mList.size() || position == mList.size() - 1 || (position != mList.size() - 1 && getItemViewType(position + 1) == TYPE_ITEM_DATE_TIME)) {
//                holder.setVisible(R.id.divider, false);
//            } else {
//                holder.setVisible(R.id.divider,true);
//            }
            holder.setText(R.id.tv_address, data.getAddress());
            holder.setText(R.id.tv_utc_time, data.getUTCTime().substring(0, data.getUTCTime().indexOf(".") - 1));
        }
    }
}
