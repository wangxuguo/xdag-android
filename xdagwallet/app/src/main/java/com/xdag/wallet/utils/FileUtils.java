package com.xdag.wallet.utils;

import android.os.Environment;

import com.xdag.wallet.XdagApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by wangxuguo on 2018/6/24.
 */

public class FileUtils {
    public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String XDAG_PATH = "/sdcard/xdag";
    public static final String WALLET_FILE = "/sdcard/xdag/wallet.dat";
    public static final String WALLET_NAME = "wallet.dat";
    public static final String DNET_KEY_FILE = "/sdcard/xdag/dnet_key.dat";
    public static final String STORAGE_FILEDIR = "/sdcard/xdag/storage";
    public static final String DNET_KEY_NAME = "dnet_key.dat";
    public static final String XDAG_DATA_PATH = "xdag_data";
    public static final String XDAG_BACKUP_PATH = "xdag_backup";

    //默认为wallet  其他为wallet1,wallet2...
    public static final String WALLET_BANK_PATH_Pre = "wallet";
    /**
     *
     * @param destDirName
     * @return  已存在 或者创建成功 true   创建失败 false
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            System.out.println("创建目录失败，目标目录已存在！");
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            System.out.println("创建目录成功！" + destDirName);
            return true;
        } else {
            System.out.println("创建目录失败！");
            return false;
        }
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：data/video/xxx.mp4
     * @param newPath String 复制后路径 如：data/oss/xxx.mp4
     * @return int 0 成功 1,-1 失败
     */
    public static int copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return 0;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return 1;

        }

        return -1;
    }

    public static int deleteFile(String filepath){
        File file = new File(filepath);
        if(file.exists()&&file.isFile()){
            boolean isDeleted = file.delete();
            if(isDeleted){
                return 1;
            }else {
                return -1;
            }
        }
        return 0;
    }
    public static int deleteFiles(String path){
        File file = new File(path);
        if(file.exists()&&file.isDirectory()){
            File[] files = file.listFiles();
            for (File f:files){
                deleteFiles(f.getAbsolutePath());
            }
        }else if(file.exists()&&file.isFile()){
            deleteFile(file.getAbsolutePath());
        }else {

        }
        return 0;
    }
}
