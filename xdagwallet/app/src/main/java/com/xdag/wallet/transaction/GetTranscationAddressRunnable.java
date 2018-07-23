package com.xdag.wallet.transaction;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagTransactionModel;
import com.xdag.wallet.net.XdagNetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by wangxuguo on 2018/7/23.
 */

public class GetTranscationAddressRunnable implements Runnable {
    private static final String TAG = Constants.TAG;
    public static final int GETTRANSACTIONADDRESS = 0x21;
    private final XdagTransactionModel block;
//    private String baseUrl = XdagNetManager.getBaseUrl();
    private String baseUrl = "http://xdagscan.com";
    private String address;
    private Handler handler;

    public GetTranscationAddressRunnable(String address,XdagTransactionModel block,Handler handler) {
        this.address = address;
        this.block = block;
        this.handler = handler;
    }

    @Override
    public void run() {
        if (block == null) {
            return;
        }
        Document doc = null;
        String addressOther = null;
        String addressDefault = null;
        String urlBlock = baseUrl + "/block/" + block.getBlock();
        Log.e(TAG, "GetTranscationAddressRunnable    " + urlBlock);
        String mBlockMethod = block.getMethod();
        double mBlockAmount = block.getAmount();
        try {
            doc = Jsoup.connect(urlBlock).get();
            Elements tables2 = doc.getElementsByClass("table");
            Elements Transactions = tables2.get(1).select("tbody").select("tr");
            Elements tbodyTrs = tables2.get(0).select("tbody").select("tr");  //select("table[class=table]")
            for (Element tr : tbodyTrs) {
//                    Log.e(TAG, "th: " + tr.select("th").text());
                String str = tr.select("th").text();
                if("Hash".equalsIgnoreCase(str)){
                    String hash = tr.select("td").text();
//                        Log.e(TAG, "Hash   td: " + hash );
                    block.setHash(hash);
                }
            }
            for (Element tr : Transactions) {
                Elements cells = tr.getElementsByTag("td");
                if (cells.size() == 3) {
                    String method = cells.get(0).text();
                    String addr = cells.get(1).text();
                    String amout = cells.get(2).text();
                    double dAmount = Double.parseDouble(amout);
                    if (mBlockMethod.equalsIgnoreCase(method)) {
                        if (dAmount == mBlockAmount) {
                            addressOther = addr;
                            Log.e(TAG, "OUTPUT  Block  Address   address: " + addressOther);
                            block.setAddress(addressOther);
                            Message msg = handler.obtainMessage(GETTRANSACTIONADDRESS);
                            msg.obj = block;
                            handler.sendMessage(msg);
                            return;
                        }
                    } else {
                        if (dAmount == mBlockAmount&&!addr.equals(address)) {
                            addressOther = addr;
                            Log.e(TAG, "OUTPUT  Block  Address   address: " + addressOther);
                            block.setAddress(addressOther);
                            Message msg = handler.obtainMessage(GETTRANSACTIONADDRESS);
                            msg.obj = block;
                            handler.sendMessage(msg);
                            return;
                        }
                    }
                    if (dAmount == mBlockAmount && !addr.equals(address)) {
                        addressDefault = addr;
                    }
//                        Log.e(TAG, "cells   method: " + method + "  block  " + block + "   dAmount  " + dAmount);
                }
//                                for (Element cell : cells) {
//                                    Log.e(TAG, "cells   td: " + cell.text());
//                                }
            }

            if (!TextUtils.isEmpty(addressDefault)) {
                Log.e(TAG, "OUTPUT  Block  addressDefault  : " + addressDefault);
                block.setAddress(addressDefault);
                Message msg = handler.obtainMessage(GETTRANSACTIONADDRESS);
                msg.obj = block;
                handler.sendMessage(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}