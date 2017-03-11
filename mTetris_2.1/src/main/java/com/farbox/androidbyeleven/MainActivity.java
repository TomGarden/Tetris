package com.farbox.androidbyeleven;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farbox.androidbyeleven.CustomView.TetrisSquare;
import com.farbox.androidbyeleven.Util._Log;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll;
    private FrameLayout fl_viewGroup;
    private TetrisSquare showTetris, moveTetris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }

    private void init() {
        findViewById(R.id.btn_sp).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_crateMyView).setOnClickListener(this);
        this.ll = (LinearLayout) findViewById(R.id.ll);
        this.fl_viewGroup = (FrameLayout) findViewById(R.id.fl_viewGroup);
    }

    TetrisSquare tetrisSquare;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sp:
                showTetris.setSquareScale(1);
                showTetris.requestLayout();

                _Log.i(_Log.msg() + "shuaixndiaodiaoy调用了刷新");
                break;
            case R.id.btn_stop:
                moveTetris.showScale();
                moveTetris.requestLayout();
                moveTetris.showScale();
                break;
            case R.id.btn_test:
                test();
                break;
            case R.id.btn_crateMyView:
                TextView textView = (TextView) findViewById(R.id.tv);
                _Log.e(_Log.msg() + "textView.getFilters().length=" + textView.getFilters().length);
                CharSequence text = textView.getText();
                if (text instanceof String) {
                    _Log.e(_Log.msg() + "text instanceof String");
                }
                if (text instanceof CharSequence) {
                    _Log.e(_Log.msg() + "text instanceof CharSequence");
                }
                if (text instanceof Spanned) {
                    _Log.e(_Log.msg() + "text instanceof Spanned");
                }
                textView.setText(text + "1");
                break;
        }
    }


    private void test() {
        //region 仅仅第一次调用
        if (this.showTetris == null) {
            this.showTetris = TetrisSquare.getShowInstance(this);
            this.ll.addView(this.showTetris);
            return;
        }
        //endregion
        if (this.moveTetris != null) {
            this.fl_viewGroup.removeView(this.moveTetris);
        }
        this.ll.removeView(this.showTetris);
        this.moveTetris = TetrisSquare.getMoveInstance(this);
        this.showTetris = TetrisSquare.getShowInstance(this);
        this.ll.addView(this.showTetris);
        moveTetris.setSquareScale(1);
        //region 置为0
        this.moveTetris.setTop(0);
        this.moveTetris.setRight(0);
        this.moveTetris.setBottom(0);
        this.moveTetris.setLeft(0);
        //endregion 置为0
        this.moveTetris.setX(this.moveTetris.getmPoint().x);
        this.moveTetris.setY(this.moveTetris.getmPoint().y);
        this.fl_viewGroup.addView(this.moveTetris);
        //this.moveTetris.requestLayout();
    }

    private void creatMyView() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}

