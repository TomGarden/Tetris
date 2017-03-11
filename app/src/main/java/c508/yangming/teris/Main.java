package c508.yangming.teris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import c508.yangming.teris.GameView.ShowNextBlockView;
import c508.yangming.teris.GameView.TetrisView;
import c508.yangming.teris.Model.BlockUnit;
import c508.yangming.teris.Model.TetrisBlock;

public class Main extends Activity
{
    public Button left, right, rotate, start, speedUp;   //按钮

    public TextView score, maxScore, level, speed;       //标签

    public int scoreValue, maxScoreValue, levelValue, speedValue;     //标签值

    public String scoreString = "分数：", maxScoreString = "最高分：", levelString = "等级：", speedString
            = "速度：";

    public TetrisView view;

    public ShowNextBlockView nextBlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取各组件和标签值
        view = (TetrisView) findViewById(R.id.tetrisView);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        rotate = (Button) findViewById(R.id.rotate);
        start = (Button) findViewById(R.id.start);
        speedUp = (Button) findViewById(R.id.speedUp);
        nextBlockView = (ShowNextBlockView) findViewById(R.id.nextBlockView);
        nextBlockView.invalidate();
        score = (TextView) findViewById(R.id.score);
        maxScore = (TextView) findViewById(R.id.maxScore);
        level = (TextView) findViewById(R.id.level);
        speed = (TextView) findViewById(R.id.speed);
        scoreValue = maxScoreValue = 0;
        levelValue = speedValue = 1;
        score.setText(scoreString + scoreValue);
        level.setText(levelString + levelValue);
        speed.setText(speedString + speedValue);
        maxScore.setText(maxScoreString + maxScoreValue);

        //设置各按钮的监听器
        left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view.canMove)
                {
                    view.getFallingBlock().move(-1);
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.invalidate();
                    }
                });
            }
        });
        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view.canMove)
                {
                    view.getFallingBlock().move(1);
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.invalidate();
                    }
                });
            }
        });
        rotate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view.canMove == false)
                {
                    return;
                }
                TetrisBlock copyOfFallingBlock = view.getFallingBlock().clone();
                copyOfFallingBlock.rotate();
                if (copyOfFallingBlock.canRotate())
                {
                    TetrisBlock fallinBlock = view.getFallingBlock();
                    fallinBlock.rotate();
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.invalidate();
                    }
                });
            }
        });
        speedUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view.canMove)
                {
                    view.getFallingBlock().setY(view.getFallingBlock().getY() + BlockUnit.UNITSIZE);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.invalidate();
                        }
                    });
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                view.init();
            }
        });
        view.setFather(this);
        view.invalidate();

    }

}