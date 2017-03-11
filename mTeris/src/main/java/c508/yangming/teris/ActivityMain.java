package c508.yangming.teris;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import c508.yangming.teris.CustomView.TetrisBeaker;
import c508.yangming.teris.Logic.LogicTetris;
import c508.yangming.teris.Util.Global;
import c508.yangming.teris.Util.LocalizeLog;
import c508.yangming.teris.Util._Log;

public class ActivityMain extends Activity implements View.OnClickListener
{
    LogicTetris logicTetris = new LogicTetris();
    TetrisBeaker beaker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Global.applicationContext = getApplicationContext();
        Global.localizeLog = new LocalizeLog();
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {

        beaker = (TetrisBeaker) findViewById(R.id.tb);
    }

    @Override
    public void onClick(View v)
    {
    }
}
