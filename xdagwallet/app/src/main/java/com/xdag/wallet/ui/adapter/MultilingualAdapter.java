package com.xdag.wallet.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xdag.wallet.R;

import java.util.List;

/**
 * Created by wangxuguo on 2018/6/22.
 */

public class MultilingualAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> list;
    private int seleceted;

    public MultilingualAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        this.list = data;
    }
    public int getSeleceted() {
        return seleceted;
    }

    public void setSeleceted(int seleceted) {
        this.seleceted = seleceted;
        notifyDataSetChanged();
    }
    @Override
    protected void convert(BaseViewHolder helper, String data) {
        helper.setText(R.id.tv_language, data);
        int position = 0;
        if (list.contains(data)) {
            position = list.indexOf(data);
        }
        if (position == seleceted) {
            helper.setVisible(R.id.iv_selected, true);
        } else {
            helper.setVisible(R.id.iv_selected, false);
        }
    }
}
