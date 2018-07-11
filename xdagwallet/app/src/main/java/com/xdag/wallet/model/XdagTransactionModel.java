package com.xdag.wallet.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xdag.wallet.db.XdagDatabase;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/7/4.
 */
@Table(database = XdagDatabase.class)
public class XdagTransactionModel  extends BaseModel implements Serializable{
    @Column
    private double amount;
    @Column
    private String address;
    @Column
    private String mine;
    @Column
    private String method;
    @Column
    private double fee;
    @Column
    @Unique
    private String hash;
    @Column
    @Unique
    private String block;
    @Column
    private String UTCTime;
    @Column
    private long time;
    @PrimaryKey(autoincrement = true)//ID自增
    public long id;
    @Column
    private int page;  //与page 公用
    public XdagTransactionModel() {
    }

    /**
     *
     * @param amount
     * @param method
     * @param block
     * @param UTCTime
     */
    public XdagTransactionModel(double amount, String method, String block, String UTCTime) {
        this.amount = amount;
        this.method = method;
        this.block = block;
        this.UTCTime = UTCTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUTCTime() {
        return UTCTime;
    }

    public void setUTCTime(String UTCTime) {
        this.UTCTime = UTCTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "XdagTransactionModel{" +
                "amount=" + amount +
                ", address='" + address + '\'' +
                ", mine='" + mine + '\'' +
                ", method='" + method + '\'' +
                ", fee=" + fee +
                ", hash='" + hash + '\'' +
                ", block='" + block + '\'' +
                ", UTCTime='" + UTCTime + '\'' +
                ", time=" + time +
                ", id=" + id +
                ", page=" + page +
                '}';
    }
}
