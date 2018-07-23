package com.xdag.wallet.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangxuguo on 2018/7/19.
 */

public class BaseResponse {
    @SerializedName("error")
    String error;

    @SerializedName("message")
    String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
