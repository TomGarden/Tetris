package com.farbox.androidbyeleven.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.Controller;
import com.farbox.androidbyeleven.R;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    /**
     * 开始暂停按钮
     */
    private Switch gameSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Controller(this);
        this.initView();
    }

    private void initView() {
        this.gameSwitch = (Switch) findViewById(R.id.switchBtn);
        this.gameSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * 监听Switch按钮状态的变化，为做出合适的响应
     *
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Toast.makeText(getApplicationContext(), "" + b, Toast.LENGTH_SHORT).show();
    }
}
