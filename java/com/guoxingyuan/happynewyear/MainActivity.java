package com.guoxingyuan.happynewyear;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;

public class MainActivity extends Activity {

    Typeface typeface;

    TextView textView;
    TextView textView_1;

    Button button;
    Button button_1;


//    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化控件
        initControl();

        setContentView(R.layout.activity_main);
        //创建Typeface
        typeface = Typeface.createFromAsset(getAssets(), "fonts/MyFont.ttf");

        button = findViewById(R.id.btn);
        button_1 = findViewById(R.id.mybtn);
        textView = findViewById(R.id.delete);
        textView_1 = findViewById(R.id.open);
        textView.setTypeface(typeface);
        textView_1.setTypeface(typeface);
        button_1.setOnClickListener(v -> restartActivity());

        button.setTypeface(typeface);
        button_1.setTypeface(typeface);
        button.setOnClickListener(v -> {
            File file = Environment.getExternalStorageDirectory();
            String path = file.getAbsolutePath();
            String myPath = path + "/happynewyear/";
            File dir = new File(myPath);
            File myFile = new File(myPath + "index.html");
            boolean a = false, b = false;
            if (myFile.exists()) {
                a = myFile.delete();
            }
            if (dir.exists()) {
                b = dir.delete();
            }
            if (a && b) Toast.makeText(MainActivity.this, "清除成功。", Toast.LENGTH_SHORT).show();
            else Toast.makeText(MainActivity.this, "清除失败。", Toast.LENGTH_SHORT).show();
        });

        //启动
        start();
    }


    /**
     * 适应异形屏
     */
    private void initControl() {
        //去标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        int uiVisibility = window.getDecorView().getSystemUiVisibility();
        uiVisibility |= flags;
        window.getDecorView().setSystemUiVisibility(uiVisibility);
        WindowManager.LayoutParams params = window.getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        window.setAttributes(params);
    }

    public void start() {
        //创建根目录File对象
        File file = Environment.getExternalStorageDirectory();

        String path = file.getAbsolutePath();
        String myPath = path + "/happynewyear/";
        String TAG = "FUCK";
        Log.d(TAG, myPath);

        //HappyNewYear目录对象
        File dir = new File(myPath);
        //文件对象
        File myFile = new File(myPath + "index.html");
        if (!dir.exists()) {
            //文件夹不存在，检查有没有权限，通过创建文件夹测试
            boolean mkdir = dir.mkdir();
            if (!mkdir) {
                //没有权限，去申请
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("权限申请");
                builder.setMessage("我需要获取“允许管理所有文件”权限来进行下一步操作，出于Android R系统的限制，该项权限必须跳转到设置页面获取。如果拒绝，则无法提供正常功能。");
                builder.setPositiveButton("前去设置", (dialog, which) -> checkStorageManagerPermission());
                builder.setNegativeButton("拒绝", (dialog, which) -> finish());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //有权限，写文件
                try {
                    boolean newFile = myFile.createNewFile();
                    if (!newFile) finish();
                    String content = FileUtil.readAssetsTxt(MainActivity.this, "index.html");
                    FileUtil.save(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            //如果目录存在，那么检查文件是否已经存在
            String filePath = myPath + "index.html";
            File html = new File(filePath);
            if (!html.exists()) {
                try {
                    Log.d(TAG, "文件不存在");
                    if (html.createNewFile()) {
                        Log.d(TAG, "已经创建成功");
                        String content = FileUtil.readAssetsTxt(MainActivity.this, "index.html");
                        FileUtil.save(content);
                    } else {
                        Log.d(TAG, "文件创建失败");
                    }
                    //文件不存在，调用FileUtil从Assets写文件

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //文件已经存在，直接选择浏览器打开
                Log.d(TAG, "文件已经存在");
                openFile(html);
            }
        }
    }

    /**
     * 跳转到设置详情页面
     */
    public void checkStorageManagerPermission() {
        SendmailUtil.send("准备申请权限");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
        }
    }

    /**
     * 申请权限的回调函数
     *
     * @param requestCode 请求码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
//            Toast.makeText(MainActivity.this, "回调", Toast.LENGTH_LONG).show();
            File file = Environment.getExternalStorageDirectory();
            String path = file.getAbsolutePath();
            String myPath = path + "/happynewyear/";
            File myFile = new File(myPath + "index.html");
            File dir = new File(myPath);
            if (dir.mkdir()) {
                SendmailUtil.send("申请权限成功。");
                Toast.makeText(MainActivity.this, "申请成功", Toast.LENGTH_LONG).show();
                String content = FileUtil.readAssetsTxt(MainActivity.this, "index.html");
                FileUtil.save(content);
                openFile(myFile);
            } else {
                SendmailUtil.send("申请权限失败");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("权限申请失败");
                builder.setMessage("此权限是必要的，如果拒绝，则无法提供正常功能。");
                builder.setPositiveButton("前去设置", (dialog, which) -> {
                    checkStorageManagerPermission();
                });
                builder.setNegativeButton("拒绝", (dialog, which) -> finish());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**
     * 打开文件
     *
     * @param file 文件
     */
    public void openFile(File file) {
        Log.d("FUCK", "打开文件。");
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = "text/html";
            //设置intent的data和Type属性。android 7.0以上crash,改用provider
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri fileUri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".fileprovider", file);//android 7.0以上
            intent.setDataAndType(fileUri, type);
            SendmailUtil.send("准备打开网页。");
            //跳转
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
