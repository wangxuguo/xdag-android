package com.xdag.wallet.net;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.SingleScheduler;

/**
 * Created by wangxuguo on 2018/7/19.
 */

public class DefaultSchedulerTransformer<T> implements ObservableTransformer<T, T> {
    @Override
    public ObservableSource<T> apply(io.reactivex.Observable<T> observable) {
        return observable
//                .compose(Transformers.switchSchedulers())
                .subscribeOn(new SingleScheduler())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}