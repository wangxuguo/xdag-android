package com.xdag.wallet.model;

import java.io.Serializable;

/**
 * Created by wangxuguo on 2018/6/23.
 */

public class CurrencyModel implements Serializable{
    private String name;
    private String symbol;

    public CurrencyModel(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyModel that = (CurrencyModel) o;

        if (!name.equals(that.name)) return false;
        return symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
