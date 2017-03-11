package com.farbox.androidbyeleven.mtetris_20;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.farbox.androidbyeleven.mtetris_20.CustomView.TetrisSquare;
import com.farbox.androidbyeleven.mtetris_20.Util._Log;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
        int[] array = new int[3];
        _Log.i(_Log.msg() + "current array value = " + array[0]);
        test1();

    }

    private void init() {
        findViewById(R.id.btn_sp).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
        this.ll = (LinearLayout) findViewById(R.id.ll);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sp:
                break;
            case R.id.btn_stop:
                break;
            case R.id.btn_test:
                test();
                break;
        }
    }

    private void test() {
        TetrisSquare tetrisSquare = new TetrisSquare(this);
        this.ll.addView(tetrisSquare);
    }

    private void test1() {
    }
}
