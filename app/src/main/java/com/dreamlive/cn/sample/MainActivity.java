package com.dreamlive.cn.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dreamlive.cn.clog.sample.R;

/**
 * 测试
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int i = 10 / 0;
    }
}
