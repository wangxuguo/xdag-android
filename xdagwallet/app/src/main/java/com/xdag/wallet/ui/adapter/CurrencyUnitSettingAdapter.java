package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.CurrencyModel;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/22.
 */

public class CurrencyUnitSettingAdapter extends BaseRecyclerViewAdapter<CurrencyModel> {

    private int seleceted;
    public CurrencyUnitSettingAdapter(Context context, List<CurrencyModel> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_multi_language;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, CurrencyModel data, int position) {
       TextView tvLanguage = holder.getView(R.id.tv_language);
       ImageView ivSelected = holder.getView(R.id.iv_selected);
       tvLanguage.setText(data.getName());
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
