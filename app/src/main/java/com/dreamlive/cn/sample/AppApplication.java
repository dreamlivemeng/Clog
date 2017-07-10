package com.dreamlive.cn.sample;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.dreamlive.cn.clog.CollectLog;

import java.io.File;

/**
 * Application,初始化
 * Created by dreamlivemeng on 2014/11/11.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化手机日志类
        CollectLog clog = CollectLog.getInstance();
        clog.init(this);//android6.0不需要动态sd权限， 将错误日志写入到sd卡,默认为Android/data/包名/files/logs下面，放这个目录下主要是为了不需要权限

//        或者下面这种自己传入路径目录,这儿示例就只传了sd根目录下的dreamlivemeng(/storage/emulated/0/dreamlivemeng)，希望把错误日志写到这个目录
//        clog.init(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dreamlivemeng");
    }
}
