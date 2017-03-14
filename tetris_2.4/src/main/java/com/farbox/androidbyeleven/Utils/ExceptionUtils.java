package com.farbox.androidbyeleven.Utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * describe: 捕获全局异常
 * time: 2017/3/13 11:18
 * email: tom.work@foxmail.com
 */
public class ExceptionUtils implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler = null;

    /**
     * 存储异常和参数信息
     */
    private Map<String, String> paramsMap = new HashMap<>();

    private static ExceptionUtils instance;

    private ExceptionUtils() {

    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized ExceptionUtils getInstance() {
        if (null == instance) {
            instance = new ExceptionUtils();
        }
        return instance;
    }

    public void init() {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException 回调函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.i(LogUtil.msg() + "uncaughtException");
        if (ex == null && mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, ex);
        } else {
            this.handleException(ex);
            LogUtil.i(LogUtil.msg() + "本次异常交给ExceptionUtil处理了");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.exit(1);
        }
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private void handleException(Throwable ex) {
        //收集设备参数信息
        this.collectDeviceInfo();
        //添加自定义信息
        this.addCustomInfo();
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(Global.applicationContext, "程序开小差了呢..", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        //保存日志文件
        this.saveCrashInfo2File(ex);
    }

    private void addCustomInfo() {
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        String time = Global.getCurrentTime();
        sb.append("====================================================================" + time + "\n");
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        LogUtil.e(LogUtil.msg() + sb.toString());

        try {
            long timestamp = System.currentTimeMillis();
            String fileName = time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/";
                LogUtil.i(LogUtil.msg() + path);
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                    LogUtil.i(LogUtil.msg() + "not exits");
                } else {
                    LogUtil.i(LogUtil.msg() + "have exits");
                }

                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(LogUtil.TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo() {
        //获取versionName,versionCode
        try {
            PackageManager pm = Global.applicationContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Global.applicationContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                paramsMap.put("versionName", versionName);
                paramsMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LogUtil.TAG, "an error occured when collect package info", e);
        }
        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(LogUtil.TAG, "an error occured when collect crash info", e);
            }
        }
    }
}
