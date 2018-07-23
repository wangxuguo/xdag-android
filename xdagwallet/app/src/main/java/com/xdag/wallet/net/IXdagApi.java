package com.xdag.wallet.net;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wangxuguo on 2018/7/19.
 */

public interface IXdagApi {
    @GET("/api/block/{address_or_hash}")
    Call<BlockInfoModel> getXdagInfo(@Path("address_or_hash") String address_or_hash);
    @GET("/api/block/{address_or_hash}")
    Flowable<BlockInfoModel> getXdagInfoModel(@Path("address_or_hash") String address_or_hash);
}
