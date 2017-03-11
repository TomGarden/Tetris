package com.farbox.androidbyeleven;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 杨铭 Created by Tom on 2016/11/1.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        _Log.d(_Log.msg() + this + "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 如果拦截了事件 return true 所拦截的事件交由本类中的 onTouchEvent处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        _Log.d(_Log.msg() + this + "onInterceptTouchEvent");
        //return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        _Log.d(_Log.msg() + this + "onTouchEvent");
        boolean result = super.onTouchEvent(event);
        _Log.d(_Log.msg() + "本级别的return = " + result);
        return result;
    }
}
