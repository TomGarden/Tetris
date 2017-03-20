package com.farbox.androidbyeleven.View.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;

/**
 * describe: 菜单页面
 * time: 2017/3/19 15:37
 * email: tom.work@foxmail.com
 */
public class MenuActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout llGroup, llAboutAuthor, llOther, llSwitchSound;
    private Switch switchSound;
    private final String groupPwd = "皇上驾到";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initView() {
        setTitle("关于");
        llGroup = (LinearLayout) findViewById(R.id.ll_group);
        llAboutAuthor = (LinearLayout) findViewById(R.id.ll_aboutAuthor);
        llOther = (LinearLayout) findViewById(R.id.ll_other);
        llSwitchSound = (LinearLayout) findViewById(R.id.ll_sound);
        switchSound = (Switch) findViewById(R.id.switchSound);
        switchSound.setChecked(Global.soundSwitch);

        llAboutAuthor.setOnClickListener(this);
        llGroup.setOnClickListener(this);
        llOther.setOnClickListener(this);
        llSwitchSound.setOnClickListener(this);
        switchSound.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_aboutAuthor:
                String url = "http://www.jianshu.com/p/308d89122840";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.ll_group:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                ClipData clipData = ClipData.newPlainText("群号：密码", "340338226:皇上驾到");
                cm.setPrimaryClip(clipData);
                Toast.makeText(Global.applicationContext, "已复制群号和密码到剪贴板", Toast.LENGTH_LONG).show();
                break;
            case R.id.ll_other:
                String otherUrl = "http://www.jianshu.com/p/0dcf0f729113";
                Intent otherIntent = new Intent(Intent.ACTION_VIEW);
                otherIntent.setData(Uri.parse(otherUrl));
                startActivity(otherIntent);
                break;
            case R.id.ll_sound:
                switchSound.setChecked(!switchSound.isChecked());
                break;
        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Global.soundSwitch = isChecked;
    }
}
