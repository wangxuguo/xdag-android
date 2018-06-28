package com.xdag.wallet.model;

import java.util.Objects;

/**
 *
 */
public class XdagState {
    public XdagState(boolean isConnect, String state, int eventType, String address, String balance, String errorMsg) {
        this.isConnect = isConnect;
        this.state = state;
        this.eventType = eventType;
        this.address = address;
        this.balance = balance;
        this.errorMsg = errorMsg;
    }

    public boolean isConnect;
    public String state;
    public int eventType;
    public String address;
    public String balance;
    public String errorMsg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XdagState xdagState = (XdagState) o;
        return isConnect == xdagState.isConnect &&
                eventType == xdagState.eventType &&
                Objects.equals(state, xdagState.state) &&
                Objects.equals(address, xdagState.address) &&
                Objects.equals(balance, xdagState.balance) &&
                Objects.equals(errorMsg, xdagState.errorMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isConnect, state, eventType, address, balance, errorMsg);
    }
}
