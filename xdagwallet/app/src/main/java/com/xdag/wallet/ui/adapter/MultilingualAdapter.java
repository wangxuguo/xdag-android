package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.ui.activity.MultilingualSettingsActivity;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/22.
 */

public class MultilingualAdapter extends BaseRecyclerViewAdapter<String> {

    private int seleceted;
    public MultilingualAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_multi_language;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, String data, int position) {
       TextView tvLanguage = holder.getView(R.id.tv_language);
       ImageView ivSelected = holder.getView(R.id.iv_selected);
       tvLanguage.setText(data);
       if(position == seleceted){
           ivSelected.setVisibility(View.VISIBLE);
       }else {
           ivSelected.setVisibility(View.GONE);
       }
    }

    public int getSeleceted() {
        return seleceted;
    }

    public void setSeleceted(int seleceted) {
        this.seleceted = seleceted;
        notifyDataSetChanged();
    }
}
