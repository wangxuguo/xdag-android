package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.util.Log;

import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;
import com.xdag.wallet.model.XdagWalletModel;

/**
 * Created by wangxuguo on 2018/6/14.
 */

public class WalletsAdapter extends BaseRecyclerViewAdapter<XdagWalletModel> {


    public WalletsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_wallet_manage;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, XdagWalletModel data, int position) {
        Log.d(Constants.TAG,"onBindViewHolder  "+data.toString());
//        holder.setTextView(R.id.iv_avator, data.getName());
        holder.setTextView(R.id.tv_name, data.getName());
        holder.setTextView(R.id.tv_address, data.getAddress());
//        holder.setTextView(R.id.iv_right_arrow, data.getAddress());
//        holder.setTextView(R.id.tv_xdag, data.getAddress());
        holder.setTextView(R.id.tv_xdag_amount, data.getAmount()+"");
    }
}
