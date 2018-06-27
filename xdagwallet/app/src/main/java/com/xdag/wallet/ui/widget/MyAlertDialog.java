package com.xdag.wallet.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;

public class MyAlertDialog extends Dialog {

    public MyAlertDialog(Context context) {
        super(context);
    }

    public MyAlertDialog(Context context, int theme) {
        super(context, theme);
    }


    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private int titleIcon;
        private String title;
        private CharSequence message;
        private View customView;
        private String positiveButtonText;
        private int positiveTextColor;
        private String negativeButtonText;
        private int negativeTextColor;
        private int messageGravity = MESSAGE_LEFT_GRAVITY;//内容居中还是居左
        public static int MESSAGE_LEFT_GRAVITY = 0;
        public static int MESSAGE_CENTER_GRAVITY = 1;

        private boolean canceledOnTouchOutside = true;
        private OnClickListener positiveButtonClickListener, negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from String
         *
         * @param message 描述信息
         * @return builder
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message 描述信息
         * @return builder
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置内容居中还是居左
         *
         * @param gravity MESSAGE_LEFT_GRAVITY(居左)  MESSAGE_CENTER_GRAVITY（居中）
         * @return
         */
        public Builder setMessageGravity(int gravity) {
            messageGravity = gravity;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param titleIcon
         * @return builder
         */
        public Builder setTitleIcon(int titleIcon) {
            this.titleIcon = titleIcon;
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title 标题
         * @return builder
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean status){
            this.canceledOnTouchOutside = status;
            return this;
        }

        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v 设置View
         * @return builder
         */
        public Builder setContentView(View v) {
            customView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText 确认按钮文案
         * @param listener           监听器
         * @return builder
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText 确认按钮文案
         * @param listener           监听器
         * @return builder
         */
        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }



        public Builder setPositiveTextColor(@ColorInt int color){
            this.positiveTextColor = color;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText 取消按钮文案
         * @param listener           监听器
         * @return builder
         */
        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText 取消按钮文案
         * @param listener           监听器
         * @return builder
         */
        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeTextColor(@ColorInt int color){
            this.negativeTextColor = color;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public MyAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final MyAlertDialog dialog = new MyAlertDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.alert_dialog, null);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            View llTitle = layout.findViewById(R.id.llTitle);
            int count = 0;
            TextView positiveBtn = (TextView) layout.findViewById(R.id.dialog_confirm);
            if (positiveButtonText != null) {
                positiveBtn.setText(positiveButtonText);
                if(positiveTextColor != 0){
                    positiveBtn.setTextColor(positiveTextColor);
                }
                if (positiveButtonClickListener != null) {
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                count++;
                positiveBtn.setVisibility(View.GONE);
            }
            // set the cancel button
            TextView negativeBtn = (TextView) layout.findViewById(R.id.dialog_cancel);
            if (negativeButtonText != null) {
                negativeBtn.setText(negativeButtonText);
                if(negativeTextColor != 0){
                    negativeBtn.setTextColor(negativeTextColor);
                }
                if (negativeButtonClickListener != null) {
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                count++;
                negativeBtn.setVisibility(View.GONE);
            }
            //TODO
//            ImageView v_line = (ImageView) layout.findViewById(R.id.v_center_line);
//            if (count > 0) {
//                v_line.setVisibility(View.GONE);
//            } else {
//                v_line.setVisibility(View.VISIBLE);
//            }
            // set the content message
            if (message != null) {
                TextView tvContent = ((TextView) layout.findViewById(R.id.dialog_content));
                tvContent.setGravity(0 == messageGravity ? Gravity.LEFT : Gravity.CENTER);
                tvContent.setText(message);
            } else {
                if(customView != null){
                    View customContenLayout = layout.findViewById(R.id.custom_content_layout);
                    customContenLayout.setVisibility(View.VISIBLE);
                    ((ViewGroup)customContenLayout).addView(customView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            }
            if (titleIcon == 0 && title == null) {
                llTitle.setVisibility(View.GONE);
            } else {
                llTitle.setVisibility(View.VISIBLE);
                if (titleIcon != 0) {
                    ((ImageView) layout.findViewById(R.id.iv_title_icon)).setImageResource(titleIcon);
                }
                if (title != null) {
                    ((TextView) layout.findViewById(R.id.tvTitle)).setText(title);
                }
            }
            Window window = dialog.getWindow();
            Rect displayRectangle = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
            window.setGravity(Gravity.CENTER);
            dialog.setContentView(layout);
            dialog.setCancelable(false);
            return dialog;
        }
    }
}
