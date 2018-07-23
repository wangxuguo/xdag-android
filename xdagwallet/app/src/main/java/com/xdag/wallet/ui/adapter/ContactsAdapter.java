package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/14.
 */

public class ContactsAdapter extends BaseQuickAdapter<XdagContactsModel,BaseViewHolder> {


    public ContactsAdapter(int layoutResId, @Nullable List<XdagContactsModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XdagContactsModel data) {
        Log.d(Constants.TAG,"onBindViewHolder  "+data.toString());
        helper.setText(R.id.tv_name, data.getName());
        helper.setText(R.id.tv_address, data.getAddress());
    }

}
