package com.guoxingyuan.happynewyear;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingActivity extends Activity {
    TextView textView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControl();
        setContentView(R.layout.activity_loading);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/MyFont.ttf");
        textView = findViewById(R.id.load);
        textView.setTypeface(typeface);

        //测试
        start();

//        start();

    }

    private void initControl() {
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

    private void test() {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void start() {
        SendmailUtil.send("打开了。");
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy*MM*dd");
        String str = sdf.format(date);

        String[] strings = str.split("\\*");

        int year = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]);
        int day = Integer.parseInt(strings[2]);

        if (year != 2023) {
            textView.setText("好像那时我们都在。");
        } else {

            if (month == 1) {

                if (day >= 21 && day <= 27) {

                    if(day==21){
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
                        String s = sdf1.format(date);
                        int i = Integer.parseInt(s);
                        if (i>=19) {
                            new CountDownTimer(3000, 1000) {
                                @SuppressLint("SetTextI18n")

                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }.start();
                        }else{
                            textView.setText("晚上再来吧。");
                        }
                    }else{
                        new CountDownTimer(4000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @SuppressLint("SetTextI18n")

                            public void onFinish() {
                                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }.start();
                    }

                } else {

                    if (day < 20) {
                        textView.setText("请至少除夕夜再来。");
                    } else {
                        textView.setText("海内存知己，天涯若比邻。");
                    }

                }

            } else {
                textView.setText("这不是该来的时间。");
            }

        }
    }

}