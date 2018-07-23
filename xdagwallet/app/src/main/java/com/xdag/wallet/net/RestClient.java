package com.xdag.wallet.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xdag.wallet.XdagApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the main entry point for network communication. Use this class for instancing REST services which do the
 * actual communication.
 */
public class RestClient {
    private static Retrofit s_retrofit;
    private static OkHttpClient mOkHttpClient;
    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(20, TimeUnit.SECONDS);//设置写入超时时间
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(XdagApplication.getContext().getCacheDir(), cacheSize);
        builder.cache(cache);
        builder.addInterceptor(interceptor);
        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        s_retrofit = new Retrofit.Builder()
                .baseUrl(XdagNetManager.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
    }

    public static <T> T getService(Class<T> serviceClass) {
        return s_retrofit.create(serviceClass);
    }
}
