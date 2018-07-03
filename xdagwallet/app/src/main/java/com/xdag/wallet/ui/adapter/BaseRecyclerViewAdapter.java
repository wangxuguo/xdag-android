package com.xdag.wallet.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xdag.wallet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/20.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;

    private int mItemLayoutRes = -1;
    protected Context mContext;
    protected List<T> mList = new ArrayList<>();
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> itemLongClickListener;
    protected OnReloadClickListener mOnReloadClickListener;

    private FooterViewHolder mFooterViewHolder;
    private boolean isShowFootView;
    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mItemLayoutRes = getLayoutRes();
        isShowFootView = false;
    }

    public BaseRecyclerViewAdapter(Context context,List<T> list) {
        this.mContext = context;
        this.mList = list;
        this.mItemLayoutRes = getLayoutRes();
        isShowFootView = false;
    }

    protected abstract int getLayoutRes();

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener<T> itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setOnReloadClickListener(OnReloadClickListener onReloadClickListener) {
        this.mOnReloadClickListener = onReloadClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = getView(parent, R.layout.rv_item_footer);
            if(isShowFootView){
                mFooterViewHolder = new FooterViewHolder(view);
            }else {
                mFooterViewHolder = new FooterViewHolder(view);
                setNofootbar();
            }
            return mFooterViewHolder;
        } else {
            View view = getView(parent, mItemLayoutRes);
            final BaseViewHolder baseViewHolder = new BaseViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        int position = baseViewHolder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(view, mList.get(position));
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemLongClickListener != null) {
                        int position = baseViewHolder.getLayoutPosition();
                        itemLongClickListener.onItemLongClick(view, mList.get(position));
                        return true;
                    }
                    return false;
                }
            });
            return baseViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder && mList.size() != 0 && mList.size() != position) {
            BaseViewHolder viewHolder = (BaseViewHolder) holder;
            onBindViewHolder(viewHolder, mList.get(position), position);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof
                    StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params =
                        (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
    }

    /**
     * item
     *
     * @param holder   ViewHolder
     * @param data     实体数据
     * @param position 索引
     */
    protected abstract void onBindViewHolder(BaseViewHolder holder, T data, int position);

    protected View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        int itemCount = mList.size();
//        if(isShowFootView) {
            return itemCount == 0 ? 0 : itemCount + 1;
//        }else {
//            return itemCount;
//        }
    }

    public boolean isShowFootView() {
        return isShowFootView;
    }

    public void setShowFootView(boolean showFootView) {
        isShowFootView = showFootView;
//        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mList.size() > 0) {
//            if(isShowFootView){
//                return TYPE_ITEM;
//            }
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public void setData(List<T> data) {
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void addMoreData(List<T> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void deleteData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        TextView prompt;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            prompt = (TextView) itemView.findViewById(R.id.tv_prompt);
        }
    }

    public void setLoading() {
        mFooterViewHolder.prompt.setText("正在加载更多");
        mFooterViewHolder.prompt.setVisibility(View.VISIBLE);
        mFooterViewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    public void setNotMore() {
        mFooterViewHolder.prompt.setText("没有更多了");
        mFooterViewHolder.progressBar.setVisibility(View.GONE);
    }
    public void setNofootbar() {
        if(mFooterViewHolder!=null&&mFooterViewHolder.prompt!=null) {
            mFooterViewHolder.prompt.setVisibility(View.GONE);
            mFooterViewHolder.progressBar.setVisibility(View.GONE);
        }
    }
    public void setNetError() {
        mFooterViewHolder.prompt.setText("加载失败，点击重试");
        mFooterViewHolder.prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnReloadClickListener != null) {
                    mOnReloadClickListener.onClick();
                }
            }
        });
        mFooterViewHolder.progressBar.setVisibility(View.GONE);
    }

    public interface OnItemClickListener<T> {

        /**
         * 长击事件
         *
         * @param view itemView
         * @param data 实体类数据
         */
        void onItemClick(View view, T data);

    }

    public interface OnItemLongClickListener<T> {

        /**
         * 单击事件
         *
         * @param view itemView
         * @param data 实体类数据
         */
        void onItemLongClick(View view, T data);

    }

    public interface OnReloadClickListener {

        /**
         * 重试点击事件
         */
        void onClick();
    }
}