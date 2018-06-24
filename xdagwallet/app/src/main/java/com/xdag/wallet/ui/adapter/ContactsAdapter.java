package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/14.
 */

public class ContactsAdapter extends BaseRecyclerViewAdapter<XdagContactsModel> {


    public ContactsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_contracts;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, XdagContactsModel data, int position) {
        Log.d(Constants.TAG,"onBindViewHolder  "+data.toString());
        holder.setTextView(R.id.tv_name, data.getName());
        holder.setTextView(R.id.tv_address, data.getAddress());
    }
}
