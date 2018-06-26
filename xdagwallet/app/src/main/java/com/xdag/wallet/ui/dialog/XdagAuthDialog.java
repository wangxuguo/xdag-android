package com.xdag.wallet.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by wangxuguo on 2018/6/25.
 */

public class XdagAuthDialog extends Dialog {

    protected XdagAuthDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
