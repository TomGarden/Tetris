package c508.yangming.getscreensize;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import c508.yangming.getscreensize.Util._Log;

/** 我们这个Module是用来获取屏幕信息的：物理尺寸、像素信息 */
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenPix();
        getPPI();
    }

    private void getScreenPix()
    {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        _Log.e(_Log.msg() + _Log.likeCoordinate("[decorWidth,decotHeight]", point.x, point.y));
    }

    private void getPPI()
    {
//        DisplayMetrics metrics;
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        _Log.d(_Log.msg() +
                       "Density is " + displayMetrics.density + " densityDpi is " +
                       displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                       " width: " + displayMetrics.widthPixels);
        _Log.d(_Log.msg() +"displayMetrics.xdpi="+displayMetrics.xdpi+"  displayMetrics.ydpi="+displayMetrics.ydpi);
    }


}
