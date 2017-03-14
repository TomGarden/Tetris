package com.farbox.androidbyeleven.View.Weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TableRow;
import android.widget.TextView;

import com.farbox.androidbyeleven.Controller.Control.MGestureDetector;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;

/**
 * Created by Tom on 2016/11/3.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 用来拦截触摸事件的父容器
 */
public class MyTableRow extends TableRow {
    private GestureDetector mGestureDetector;

    public MyTableRow(Context context) {
        this(context, null);
    }

    public MyTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 直接返回 true 把事件交给本类处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (Global.gameState) {
            case moving:
                return true;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * 处理触屏事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Global.gameState == null) {
            LogUtil.e(LogUtil.msg() + "这里曾经出现过一次，但是为什么会是空我没理解");
        }

        switch (Global.gameState) {
            case moving:
                return this.mGestureDetector.onTouchEvent(event);
            default:
                return false;
        }
    }

    public void setParam(ITetrisMoveGetterService serverGetter, ITetrisMoveInteractiveService serverInteractive, Beaker beaker, TextView hiScore) {
        this.mGestureDetector = new GestureDetector(this.getContext(), new MGestureDetector(beaker, hiScore, serverGetter, serverInteractive));
    }
}
