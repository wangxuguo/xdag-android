package com.xdag.wallet.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xdag.wallet.db.XdagDatabase;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/6/14.
 */
@Table(database = XdagDatabase.class)
public class XdagContactsModel extends BaseModel implements Serializable {
    @Column
    public String address;
    @Column
    public String name;
    @Column
    public String icon;
    @PrimaryKey(autoincrement = true)//ID自增
    public long id;

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
