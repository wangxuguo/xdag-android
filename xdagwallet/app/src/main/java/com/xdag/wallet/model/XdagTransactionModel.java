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
    private int page;  //与page 公用   -2 接口获取
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XdagTransactionModel model = (XdagTransactionModel) o;

        if (Double.compare(model.amount, amount) != 0) return false;
        if (Double.compare(model.fee, fee) != 0) return false;
        if (time != model.time) return false;
        if (address != null ? !address.equals(model.address) : model.address != null) return false;
        if (mine != null ? !mine.equals(model.mine) : model.mine != null) return false;
        if (method != null ? !method.equals(model.method) : model.method != null) return false;
        if (hash != null ? !hash.equals(model.hash) : model.hash != null) return false;
        if (block != null ? !block.equals(model.block) : model.block != null) return false;
        return UTCTime.equals(model.UTCTime);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (mine != null ? mine.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        temp = Double.doubleToLongBits(fee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        result = 31 * result + (block != null ? block.hashCode() : 0);
        result = 31 * result + UTCTime.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
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
