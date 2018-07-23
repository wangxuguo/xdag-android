package com.xdag.wallet.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdag.wallet.R;

/**
 *
 */
public class XdagProgressDialog extends Dialog implements View.OnClickListener {
    private final Animation imageRotateAnimation;
    // private TextView tv_message;
    private OnCancelListener onCancelListener;
    //    private static AnimationDrawable drawable;
    private Context context;
    private ImageView iv_loading;
    private TextView tv_message;
    private TextView tv_progress_info_tips;
    private String message;

    public XdagProgressDialog(Context context,String message) {
        super(context, R.style.dialog_router);
        this.context = context;
        this.message = message;
        imageRotateAnimation = AnimationUtils.loadAnimation(context,
                R.anim.xdag_loading_animation);
        init();
    }

    private void init() {

        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

//        layout.findViewById(R.id.ll_dialog).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        iv_loading = (ImageView) layout.findViewById(R.id.iv_loading);
//        iv_loading.setImageResource(R.drawable.loading);///

        tv_message = (TextView) layout.findViewById(R.id.tv_message);
        tv_progress_info_tips = (TextView) layout.findViewById(R.id.tv_progress_info_tips);
//        if (!TextUtils.isEmpty(title)) {
//            setTitle(title);
//        }
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        } else {
            tv_message.setText(context.getString(R.string.xdag_sending));
        }
//        Window window = getWindow();
//        window.setGravity(Gravity.CENTER);
//			window.setWindowAnimations(R.style.dialog_progress_style_);
//        dialog.setContentView(layout);
        addContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void setMessage(String message) {
        this.message = message;
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }
    }

    public void setProgressInfo(String info){
        if(tv_progress_info_tips!=null){
            tv_progress_info_tips.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(info)) {
                tv_progress_info_tips.setText(info);
            }
        }
    }
    @Override
    public void show() {
        super.show();
        iv_loading.startAnimation(imageRotateAnimation);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            imageRotateAnimation.cancel();
            super.dismiss();
        }
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

}
