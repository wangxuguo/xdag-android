package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangxuguo on 2018/7/7.
 */

public class TransactionsRecordAdapter extends BaseRecyclerViewAdapter<XdagTransactionModel>{

    private static final int TYPE_ITEM_DATE_TIME = 2;

    public TransactionsRecordAdapter(Context context, List<XdagTransactionModel> list, boolean b) {
        super(context,list,b);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_transaction_record;
    }


    @Override
    public int getItemViewType(int position) {
        if (mList != null && position < mList.size() && mList.get(position) != null) {
            if(mList.get(position).getPage()==-1){
                return TYPE_ITEM_DATE_TIME;
            }
        }
        return super.getItemViewType(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM_DATE_TIME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_transaction_time, parent, false);
            TimeViewHolder mTimeViewHolder = new TimeViewHolder(view);
            return mTimeViewHolder;
        }
        return super.onCreateViewHolder(parent,viewType);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TimeViewHolder || getItemViewType(position) == TYPE_ITEM_DATE_TIME){
            TimeViewHolder mTimeHolder = (TimeViewHolder) holder;
            if(mTimeHolder!=null){
                mTimeHolder.tv_transaction_time.setText(mList.get(position).getUTCTime());
            }
        }else {
            super.onBindViewHolder(holder,position);
        }

    }
    @Override
    protected void onBindViewHolder(BaseViewHolder holder, XdagTransactionModel data, int position) {
        Log.d(Constants.TAG,"onBindViewHolder  "+data.toString());
        if(!TextUtils.isEmpty(data.getMethod())&&"input".equalsIgnoreCase(data.getMethod())){
            holder.setImageResource(R.id.iv_transaction_icon,R.drawable.ic_transaction_input);
            DecimalFormat df = new DecimalFormat("0.#########");
            holder.setTextView(R.id.tv_xdag_amount, "+"+df.format(data.getAmount())+" "+mContext.getString(R.string.xdag));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.setTextViewColor(R.id.tv_xdag_amount,mContext.getColor(R.color.color_transaction_input));
            }else {
                holder.setTextViewColor(R.id.tv_xdag_amount,mContext.getResources().getColor(R.color.color_transaction_input));
            }
        }else {
            holder.setImageResource(R.id.iv_transaction_icon,R.drawable.ic_transaction_output);
            DecimalFormat df = new DecimalFormat("0.#########");
            holder.setTextView(R.id.tv_xdag_amount, "-"+df.format(data.getAmount())+" "+mContext.getString(R.string.xdag));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.setTextViewColor(R.id.tv_xdag_amount,mContext.getColor(R.color.color_transaction_output));
            }else {
                holder.setTextViewColor(R.id.tv_xdag_amount,mContext.getResources().getColor(R.color.color_transaction_output));
            }
        }
        if (position == mList.size()||position == mList.size()-1 || (position != mList.size() - 1 && getItemViewType(position + 1) == TYPE_ITEM_DATE_TIME)) {
            holder.setVisibility(R.id.divider, View.GONE);
        } else {
            holder.setVisibility(R.id.divider, View.VISIBLE);
        }
        holder.setTextView(R.id.tv_address, data.getAddress());
        holder.setTextView(R.id.tv_utc_time, data.getUTCTime().substring(0,data.getUTCTime().indexOf(".")-1));

    }
    private class TimeViewHolder extends BaseViewHolder {
        TextView tv_transaction_time;
        public TimeViewHolder(View itemView) {
            super(itemView);
            tv_transaction_time = (TextView) itemView.findViewById(R.id.tv_transaction_time);
        }
    }
}
