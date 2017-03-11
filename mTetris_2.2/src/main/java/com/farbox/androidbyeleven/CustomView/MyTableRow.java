package com.farbox.androidbyeleven.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TableRow;

/**
 * 杨铭 Created by Tom on 2016/11/3.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public class MyTableRow extends TableRow {
    private GestureDetector mGestureDetector;

    public MyTableRow(Context context) {
        this(context, null);
    }

    public MyTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mGestureDetector = new GestureDetector(this.getContext(), new MGestureDetector());
    }

    /**
     * 直接返回 true 把事件交给本类处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 处理触屏事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mGestureDetector.onTouchEvent(event);
    }
}
