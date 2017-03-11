package com.farbox.androidbyeleven;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 如果事件被消耗 return true
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        _Log.i(_Log.msg() + this + "::dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 如果事件被消耗 return true 默认 return false
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        _Log.i(_Log.msg() + this + "::onTouchEvent");
        return super.onTouchEvent(event);
    }
}
