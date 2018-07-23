package com.xdag.wallet.net;


import com.xdag.wallet.model.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .addHeader("Accept", Constants.CLINET_ACCEPT)
                .build();

        //alternative
        okhttp3.Headers moreHeaders = request.headers().newBuilder()
                .build();

        request = request.newBuilder().headers(moreHeaders).build();

        Response response = chain.proceed(request);
        return response;
    }
}
