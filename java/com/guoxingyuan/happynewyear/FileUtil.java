package com.guoxingyuan.happynewyear;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 往外部存储写文件
     *
     * @param content 文件内容
     */
    public static void save(String content) {
        String path = Environment.getExternalStorageDirectory()+"/happynewyear/"+"index.html";
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //目录
        Log.d("FUCK","紫萼文件");
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 从assets目录下读取文件
     * @param context 程序上下文
     * @param fileName 文件绝对路径
     * @return
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String readAssetsTxt(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }
}
