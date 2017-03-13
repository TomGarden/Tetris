package com.farbox.androidbyeleven.View.Activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.farbox.androidbyeleven.Utils.Global;

/**
 * Created by Tom on 2017/1/15.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * Activity基类，便于统一操作整体风格，和共享特定资源实现代码复用
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutId());
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        /*透明导航栏*/
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Global.applicationContext = getApplicationContext();

        this.beforeView();
        this.initView();
        this.initData();
    }

    /**在初始化控件之前执行本方法*/
    protected void beforeView() {
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
