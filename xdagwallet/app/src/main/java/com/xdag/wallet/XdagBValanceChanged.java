package com.xdag.wallet;

/**
 * Created by wangxuguo on 2018/6/30.
 */

public class XdagBValanceChanged {
    private double balance;
    private double oldBalance;
    public XdagBValanceChanged(double balance, double oldBalance) {
        this.balance = balance;
        this.oldBalance = oldBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(double oldBalance) {
        this.oldBalance = oldBalance;
    }
}
