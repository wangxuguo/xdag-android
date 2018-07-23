package com.xdag.wallet.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangxuguo on 2018/7/21.
 */

public class BlockInfoModel extends BaseResponse{


    /**
     * time : 2018-05-02 15:42:03.810
     * timestamp : 16ba7712f3e
     * flags : 1c
     * state : Accepted
     * file_pos : 0
     * hash : a529b7f4974d96066f2035b1ef5204dba4e6ad5fd09f766b8f7953bdab4d3be0
     * difficulty : 0000000018ccbc568
     * balance_address : 4DtNq71TeY9rdp/QX63mpNsEUu+xNSBv
     * balance : 11395012.818918678
     * block_as_transaction : [{"direction":"fee","address":"du8rgQcOpClxKlCPy1VuzCKHKc/bLNhq","amount":"0.000000000"}]
     * block_as_address : [{"direction":"input","address":"i71T6twWP7DP22F79q9Na3uo7ZV+82IU","amount":"1.000000000","time":"2018-05-03 07:51:22.296"},{"direction":"output","address":"oDV5PsZ8l+dhKKD2Ih/3NSLS7UMyzkye","amount":"1.000000000","time":"2018-05-03 15:26:54.859"},{"direction":"input","address":"J/L+PXpP1uCzRQVDJ9GvOlNmzqv2u0fX","amount":"1000.064137000","time":"2018-05-06 08:43:42.952"},{"direction":"input","address":"FbUluXCxemwo2FfagK95qQMUNUMAcgy6","amount":"100.000000000","time":"2018-05-06 10:40:17.133"},{"direction":"output","address":"lkY8fCmoHnntMetoB8fPhkp6GkthFL0u","amount":"0.023446214","time":"2018-06-08 19:03:37.392"},{"direction":"input","address":"aYRt3aiVA253V00OLlt17ZhTo/VjASQ1","amount":"0.027059822","time":"2018-06-09 01:40:22.919"}]
     * balances_last_week : [{"2018-07-12":"0"},{"2018-07-13":"0"},{"2018-07-14":"0"},{"2018-07-15":"0"},{"2018-07-16":"0"},{"2018-07-17":"0"},{"2018-07-18":"0"}]
     * earnings_last_week : [{"2018-07-12":"0"},{"2018-07-13":"0"},{"2018-07-14":"0"},{"2018-07-15":"0"},{"2018-07-16":"0"},{"2018-07-17":"0"},{"2018-07-18":"0"}]
     * spendings_last_week : [{"2018-07-12":"0"},{"2018-07-13":"0"},{"2018-07-14":"0"},{"2018-07-15":"0"},{"2018-07-16":"0"},{"2018-07-17":"0"},{"2018-07-18":"0"}]
     * balance_change_last_24_hours : 0
     * earnings_change_last_24_hours : 0
     * spendings_change_last_24_hours : 0
     * total_earnings : 1.8878819600184206E7
     * total_spendings : 7483806.781266
     * kind : Wallet
     */

    private String time;
    private String timestamp;
    private String flags;
    private String state;
    private String file_pos;
    private String hash;
    private String difficulty;
    private String balance_address;
    private String balance;
    private int balance_change_last_24_hours;
    private int earnings_change_last_24_hours;
    private int spendings_change_last_24_hours;
    private double total_earnings;
    private double total_spendings;
    private String kind;
    private List<BlockAsTransactionBean> block_as_transaction;
    private List<BlockAsAddressBean> block_as_address;
    private List<Map<String, Integer>> balances_last_week;
    private List<Map<String, Integer>> earnings_last_week;
    private List<Map<String, Integer>> spendings_last_week;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFile_pos() {
        return file_pos;
    }

    public void setFile_pos(String file_pos) {
        this.file_pos = file_pos;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBalance_address() {
        return balance_address;
    }

    public void setBalance_address(String balance_address) {
        this.balance_address = balance_address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getBalance_change_last_24_hours() {
        return balance_change_last_24_hours;
    }

    public void setBalance_change_last_24_hours(int balance_change_last_24_hours) {
        this.balance_change_last_24_hours = balance_change_last_24_hours;
    }

    public int getEarnings_change_last_24_hours() {
        return earnings_change_last_24_hours;
    }

    public void setEarnings_change_last_24_hours(int earnings_change_last_24_hours) {
        this.earnings_change_last_24_hours = earnings_change_last_24_hours;
    }

    public int getSpendings_change_last_24_hours() {
        return spendings_change_last_24_hours;
    }

    public void setSpendings_change_last_24_hours(int spendings_change_last_24_hours) {
        this.spendings_change_last_24_hours = spendings_change_last_24_hours;
    }

    public double getTotal_earnings() {
        return total_earnings;
    }

    public void setTotal_earnings(double total_earnings) {
        this.total_earnings = total_earnings;
    }

    public double getTotal_spendings() {
        return total_spendings;
    }

    public void setTotal_spendings(double total_spendings) {
        this.total_spendings = total_spendings;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<BlockAsTransactionBean> getBlock_as_transaction() {
        return block_as_transaction;
    }

    public void setBlock_as_transaction(List<BlockAsTransactionBean> block_as_transaction) {
        this.block_as_transaction = block_as_transaction;
    }

    public List<BlockAsAddressBean> getBlock_as_address() {
        return block_as_address;
    }

    public void setBlock_as_address(List<BlockAsAddressBean> block_as_address) {
        this.block_as_address = block_as_address;
    }

    public List<Map<String, Integer>> getBalances_last_week() {
        return balances_last_week;
    }

    public void setBalances_last_week(List<Map<String, Integer>> balances_last_week) {
        this.balances_last_week = balances_last_week;
    }

    public List<Map<String, Integer>> getEarnings_last_week() {
        return earnings_last_week;
    }

    public void setEarnings_last_week(List<Map<String, Integer>> earnings_last_week) {
        this.earnings_last_week = earnings_last_week;
    }

    public List<Map<String, Integer>> getSpendings_last_week() {
        return spendings_last_week;
    }

    public void setSpendings_last_week(List<Map<String, Integer>> spendings_last_week) {
        this.spendings_last_week = spendings_last_week;
    }

    public static class BlockAsTransactionBean {
        /**
         * direction : fee
         * address : du8rgQcOpClxKlCPy1VuzCKHKc/bLNhq
         * amount : 0.000000000
         */

        private String direction;
        private String address;
        private String amount;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }

    public static class BlockAsAddressBean {
        /**
         * direction : input
         * address : i71T6twWP7DP22F79q9Na3uo7ZV+82IU
         * amount : 1.000000000
         * time : 2018-05-03 07:51:22.296
         */

        private String direction;
        private String address;
        private String amount;
        private String time;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Override
    public String toString() {
        return "BlockInfoModel{" +
                "error='" + error + '\'' +
                "message='" + message + '\'' +
                "time='" + time + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", flags='" + flags + '\'' +
                ", state='" + state + '\'' +
                ", file_pos='" + file_pos + '\'' +
                ", hash='" + hash + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", balance_address='" + balance_address + '\'' +
                ", balance='" + balance + '\'' +
                ", balance_change_last_24_hours=" + balance_change_last_24_hours +
                ", earnings_change_last_24_hours=" + earnings_change_last_24_hours +
                ", spendings_change_last_24_hours=" + spendings_change_last_24_hours +
                ", total_earnings=" + total_earnings +
                ", total_spendings=" + total_spendings +
                ", kind='" + kind + '\'' +
                ", block_as_transaction=" + block_as_transaction +
                ", block_as_address=" + block_as_address +
                ", balances_last_week=" + balances_last_week +
                ", earnings_last_week=" + earnings_last_week +
                ", spendings_last_week=" + spendings_last_week +
                '}';
    }
}
