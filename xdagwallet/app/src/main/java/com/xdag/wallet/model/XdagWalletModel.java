package com.xdag.wallet.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xdag.wallet.db.XdagDatabase;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/6/14.
 */
@Table(database = XdagDatabase.class)
public class XdagWalletModel extends BaseModel implements Serializable {
    @Column
    @Unique
    public String address;
    @Column
    public String name;
    @Column
    public String icon;
    @PrimaryKey(autoincrement = true)//ID自增
    public long id;
    @Column
    public String localPath;
    @Column
    public String bankPath;
    @Column
    public double amount;
    @Column
    public String sourcePath;
    @Column
    public boolean isDeleted;
    @Column
    @Unique
    public String walletMd5;
    @Column
    public int type;  //钱包类型  0 创建  1 导入  3 备份文件中恢复  2.钱包位置存放，app可能卸载后再次安装的
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getBankPath() {
        return bankPath;
    }

    public void setBankPath(String bankPath) {
        this.bankPath = bankPath;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getWalletMd5() {
        return walletMd5;
    }

    public void setWalletMd5(String walletMd5) {
        this.walletMd5 = walletMd5;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "XdagWalletModel{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                ", localPath='" + localPath + '\'' +
                ", bankPath='" + bankPath + '\'' +
                ", amount=" + amount +
                ", sourcePath='" + sourcePath + '\'' +
                ", isDeleted=" + isDeleted +
                ", walletMd5='" + walletMd5 + '\'' +
                ", type=" + type +
                '}';
    }
}
