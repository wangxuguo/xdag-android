package com.xdag.wallet.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xdag.wallet.db.DataBaseManager;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/6/14.
 */
@Table(database = DataBaseManager.XdagDatabase.class)
public class XdagWalletModel extends BaseModel implements Serializable {
    @Column
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

    @Override
    public String toString() {
        return "XdagContactsModel{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                '}';
    }
}
