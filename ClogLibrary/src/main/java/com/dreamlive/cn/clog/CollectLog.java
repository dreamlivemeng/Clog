package com.dreamlive.cn.clog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * 捕获android程序崩溃日志<br>
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序.<br>
 * 将错误日志写入到sd卡,默认为Android/data/包名/files/logs下面，放这个目录下主要是为了不需要权限
 * <p>
 * Created by dreamlivemeng on 2014/11/11.
 */

public class CollectLog implements UncaughtExceptionHandler {

    public static final String TAG = CollectLog.class.getCanonicalName();

    // 系统默认的UncaughtException处理类 
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例 
    private static CollectLog INSTANCE = new CollectLog();
    // 程序的Context对象 
    private Context mContext;
    // 用来存储设备信息和异常信息 
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分 
    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //传入的路径
    private String filePath = "";

    /**
     * 保证只有一个实例
     */
    private CollectLog() {
    }

    /**
     * 获取实例 ，单例模式
     */
    public static CollectLog getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器 
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器 
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 初始化
     *
     * @param context
     * @param path
     */
    public void init(Context context, String path) {
        init(context);
        filePath = path;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
            }
            // 退出程序 
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
/*
    因为权限问题，不建议使用Toast弹出
        // 使用Toast来显示异常信息 
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常。", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();
*/
        // 收集设备参数信息 
        collectDeviceInfo(mContext);
        // 保存日志文件 
        String str = saveCrashInfo2File(ex);
        Log.e(TAG, str);

        return false;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }

        } catch (NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                //打印收集的参数
//                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
//                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("[" + key + ", " + value + "]\n");
        }
        sb.append("\n" + getStackTraceString(ex));
        try {
            String time = formatter.format(new Date());
            String fileName = "CRS_" + time + ".txt";
            File sdDir = null;
            sdDir = mContext.getExternalFilesDir("logs").getAbsoluteFile();
            File file = null;
            if (!TextUtils.isEmpty(filePath)) {
                File files = new File(filePath);
                if (!files.exists()) {
                    //创建目录
                    files.mkdirs();
                }
                file = new File(filePath + File.separator + fileName);
            } else {
                file = new File(sdDir + File.separator + fileName);
            }
            if (file == null) {
                file = new File(sdDir + File.separator + fileName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
//            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 获取捕捉到的异常的字符串
     */
    public static String getStackTraceString(Throwable tr) {
        try {
            if (tr == null) {
                return "";
            }
            Throwable t = tr;
            while (t != null) {
                if (t instanceof UnknownHostException) {
                    return "";
                }
                t = t.getCause();
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "";
        }
    }
}