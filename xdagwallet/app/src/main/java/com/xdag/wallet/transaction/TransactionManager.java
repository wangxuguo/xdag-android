package com.xdag.wallet.transaction;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xdag.wallet.model.XdagTransactionModel;
import com.xdag.wallet.utils.DateTimeUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangxuguo on 2018/7/6.
 */

public class TransactionManager {
    private static final int GETTRANSACTIONINFO = 0x01;
    private static final int GETTRANSACTIONADDRESS = 0x02;
    private static final int STARTGETTRANSACTIONINFO = 0x03;
    private static final int TRANSACTIONINFO_PERLISTEND = 0x04;
    private final ExecutorService threadpool;
    private final HandlerThread handerThread;
    private final Handler handler;
    private final ModelAdapter<XdagTransactionModel> modelAdapter;
    private String baseUrl = "https://explorer.xdag.io";
    private static final String TAG = "Transactions";
    private String address;
    private OnNewRecordAddListener onNewRecordAddListener;
    private static final TransactionManager ourInstance = new TransactionManager();
    private int addr_page;
    private Runnable lastListModelsRunable;

    public static TransactionManager getInstance() {
        return ourInstance;
    }

    private TransactionManager() {
        threadpool = Executors.newFixedThreadPool(1);
        handerThread = new HandlerThread("transcations");
        handerThread.start();
        modelAdapter = FlowManager.getModelAdapter(XdagTransactionModel.class);
        lastListModelsRunable = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TRANSACTIONINFO_PERLISTEND);
            }
        };
        handler = new Handler(handerThread.getLooper()) {
            private int curModelListIndex = 0;
            private int curIndex = 0;
            private int max = 0;
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case STARTGETTRANSACTIONINFO:
                        max = msg.arg1;
                        Log.e(TAG, "STARTGETTRANSACTIONINFO  max  " + max);
                        curModelListIndex = addr_page;
//                        for (int i = addr_page; i < max; i++) {
                            Log.e(TAG, "submit  GetTransactionInfoRunnable  " + curModelListIndex);
                            threadpool.submit(new GetTransactionInfoRunnable(address, curModelListIndex));
//                        }
                        break;
                    case GETTRANSACTIONINFO:
                        List<XdagTransactionModel> xdagTransactionModels = (List<XdagTransactionModel>) msg.obj;
                        Log.e(TAG, "GETTRANSACTIONINFO  xdagTransactionModels  " + xdagTransactionModels.size());
                        for (XdagTransactionModel model : xdagTransactionModels) {
                            if (model != null && !TextUtils.isEmpty(model.getBlock())) {
                                Log.e(TAG, "submit  GetTranscationAddressRunnable  " + model.toString());
                                threadpool.submit(new GetTranscationAddressRunnable(model));
                            }
                        }
                        threadpool.submit(lastListModelsRunable);
                        break;
                    case TRANSACTIONINFO_PERLISTEND:
                        if(onNewRecordAddListener!=null){
                            onNewRecordAddListener.OnNewRecordAddListener();
                        }
                        curModelListIndex ++;
                        if(curModelListIndex < max){
                            Log.e(TAG, "submit  GetTransactionInfoRunnable  " + curModelListIndex);
                            threadpool.submit(new GetTransactionInfoRunnable(address, curModelListIndex));
                        }
                        break;
                    case GETTRANSACTIONADDRESS:
                        XdagTransactionModel model = (XdagTransactionModel) msg.obj;
                        if(TextUtils.isEmpty(model.getMine())){
                            model.setMine(address);
                        }
                        String utcTime = model.getUTCTime().substring(0,model.getUTCTime().indexOf(".")-1);//.replaceAll(" UTC","");
                        SimpleDateFormat localFormater = new SimpleDateFormat(DateTimeUtils.NORMAT_FORMAT);
                        localFormater.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
                        try {
                            Date date = localFormater.parse(utcTime);
                            model.setTime(date.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "GETTRANSACTIONADDRESS  model  " + model.toString());

                        try {
                            Log.e(TAG, "modelAdapter  insert  " + model.toString());
                            long id = modelAdapter.insert(model);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    public void checkAddressRecord(String address,int addr_page) {
        this.address = address;
        this.addr_page = addr_page;
        if (threadpool != null) {
            threadpool.submit(new GetTransactionInfoRunnable(address, 0));
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OnNewRecordAddListener getOnNewRecordAddListener() {
        return onNewRecordAddListener;
    }

    public void setOnNewRecordAddListener(OnNewRecordAddListener onNewRecordAddListener) {
        this.onNewRecordAddListener = onNewRecordAddListener;
    }

    private class GetTranscationAddressRunnable implements Runnable {
        private final XdagTransactionModel block;

        private GetTranscationAddressRunnable(XdagTransactionModel block) {
            this.block = block;
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
                        Log.e(TAG, "Hash   td: " + hash );
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

                        }
                        if (dAmount == mBlockAmount) {
                            addressDefault = addr;
                        }
                        Log.e(TAG, "cells   method: " + method + "  block  " + block + "   dAmount  " + dAmount);
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


    private class GetTransactionInfoRunnable implements Runnable {
        private int addr_page;
        private String address;

        //        ?addr_page=39#address
        public GetTransactionInfoRunnable(String address, int addr_page) {
            this.addr_page = addr_page;
            this.address = address;
        }

        @Override
        public void run() {
            Document doc = null;
            String urlBlock = baseUrl + "/block/" + address + (addr_page == 0 ? "" : "?addr_page=" + addr_page + "#address");
            Log.e(TAG, "GetTransactionInfoRunnable    " + urlBlock);
            try {
                doc = Jsoup.connect(urlBlock).get();
                Log.e(TAG, doc.title());
                Elements tables2 = doc.getElementsByClass("table");
                Elements tbodyTrs = tables2.get(0).select("tbody").select("tr");  //select("table[class=table]")
                for (Element tr : tbodyTrs) {
//                    Log.e(TAG, "th: " + tr.select("th").text());
                    String str = tr.select("th").text();
                    switch (str) {
                        case "Time":
                            Log.e(TAG, "Time  td: " + tr.select("td").text());
                            break;
                        case "Timestamp":
                            Log.e(TAG, "Timestamp  td: " + tr.select("td").text());
                            break;
                        case "Flags":
                            Log.e(TAG, "Flags  td: " + tr.select("td").text());
                            break;
                        case "State":
                            Log.e(TAG, "State   td: " + tr.select("td").text());
                            break;
                        case "File pos":
                            Log.e(TAG, "File pos  td: " + tr.select("td").text());
                            break;
                        case "Hash":
                            Log.e(TAG, "Hash   td: " + tr.select("td").text());
                            break;
                        case "Difficulty":
                            Log.e(TAG, "Difficulty   td: " + tr.select("td").text());
                            break;
                        case "Balance":
                            Log.e(TAG, "Balance  td: " + tr.select("td").text());
                            String balence = tr.select("td").text();
                            if (balence.contains("(")) {
                                String ba = (String) balence.substring(0, balence.indexOf("("));
                                Double b = Double.parseDouble(ba);
                                Log.e(TAG, "Balance Double:" + b);
                            }
                            break;
                    }
                }


                Elements Transactions = tables2.get(2).select("tbody").select("tr");
                List<XdagTransactionModel> xdagTransactionModels = new ArrayList<>();
                for (Element tr : Transactions) {
                    Elements cells = tr.getElementsByTag("td");
                    if (cells.size() == 4) {
                        String method = cells.get(0).text();
                        String block = cells.get(1).text();
                        String amout = cells.get(2).text();
                        double dAmount = Double.parseDouble(amout);
                        String UTCtime = cells.get(3).text();
                        Log.e(TAG, "cells   method: " + method + "  block  " + block + "   dAmount  " + dAmount + " UTCtime " + UTCtime);
                        XdagTransactionModel xdagTransactionModel = new XdagTransactionModel(dAmount, method, block, UTCtime);
                        xdagTransactionModel.setPage(addr_page);
                        xdagTransactionModel.setMine(address);
                        xdagTransactionModels.add(xdagTransactionModel);
                    }
                }

                Elements page_links = doc.select("a[class=page-link]");
                int max = 0;
                for (Element pagelink : page_links) {
                    String text = pagelink.text();
                    if (!text.contains("Next")) {
                        int page = Integer.parseInt(text);
                        if (max < page) {
                            max = page;
                        }
                    }
                }
                Log.e(TAG, "Max page_links: " + max);
                if (max > 0 && max >= addr_page) {
                    Message msg0 = handler.obtainMessage(STARTGETTRANSACTIONINFO);
                    msg0.arg1 = max;
                    handler.sendMessage(msg0);
                }
                Message msg = handler.obtainMessage(GETTRANSACTIONINFO);
                msg.obj = xdagTransactionModels;
                msg.arg1 = max;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnNewRecordAddListener {
        void OnNewRecordAddListener();
    }
}
