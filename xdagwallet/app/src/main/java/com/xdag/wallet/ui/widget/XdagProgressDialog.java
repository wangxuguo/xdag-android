package com.xdag.wallet.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 *
 */
public class XdagProgressDialog extends Dialog implements View.OnClickListener {
    // private TextView tv_message;
    private OnCancelListener onCancelListener;
    private static AnimationDrawable drawable;


    public XdagProgressDialog(Context context) {
        super(context, R.style.dialog_router);
    }

    public XdagProgressDialog(Context context, int theme) {
        super(context, R.style.dialog_router);
    }

    @Override
    public void show() {
        super.show();
        drawable.start();
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
        drawable.stop();
    }

    @Override
    public void onClick(View v) {
        if (onCancelListener != null) // 取消
            onCancelListener.onCancel(this);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener) {
        super.setOnCancelListener(listener);
        this.onCancelListener = listener;
    }

    public static class Builder {

        private Context context;
        private ImageView iv_loading;
        private TextView tv_message;
        private String message;
        private String title;
        public Builder(Context context) {
            this.context = context;
        }
        public Builder setTitle(String title){
            this.title = title;
            return this;
        }
        public Builder setMessage(String message){
            this.message = message;
            return  this;
        }
        public XdagProgressDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final XdagProgressDialog dialog = new XdagProgressDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_loading, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.findViewById(R.id.ll_dialog).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            iv_loading = (ImageView) layout.findViewById(R.id.iv_loading);
            iv_loading.setImageResource(R.drawable.ic_xdag_small);

            tv_message = (TextView) layout.findViewById(R.id.tv_message);
            if(!TextUtils.isEmpty(message)){
                tv_message.setText(message);
            }else {
                tv_message.setText(context.getString(R.string.xdag_sending));
            }
            drawable = (AnimationDrawable) iv_loading.getDrawable();

            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
//			window.setWindowAnimations(R.style.dialog_progress_style_);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
