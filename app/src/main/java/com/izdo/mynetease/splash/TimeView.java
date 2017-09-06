package com.izdo.mynetease.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.izdo.mynetease.R;

/**
 * Created by iZdo on 2017/9/5.
 */

public class TimeView extends View {

    // 文字画笔
    private TextPaint mTextPaint;
    // 内圆画笔
    private Paint innerPaint;
    // 外圆画笔
    private Paint outerPaint;
    // 文字内容
    private String content = "跳过";
    //文字的间距
    private int padding = 2;
    //内圆的直径
    private int innerDiameter;
    //外圈的直径
    private int outerDiameter;
    //外圈的角度
    private int degrees;
    private RectF outerRect;

    OnTimeClickListener mListener;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取xml定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeView);
        int innerColor = array.getColor(R.styleable.TimeView_innerColor, Color.BLUE);
        int outerColor = array.getColor(R.styleable.TimeView_ringColor, Color.GREEN);

        mTextPaint = new TextPaint();
        // 抗锯齿
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(10);
        mTextPaint.setColor(Color.WHITE);

        innerPaint = new Paint();
        innerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(innerColor);

        outerPaint = new Paint();
        outerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(outerColor);
        // 空心 只绘制轮廓
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(padding);

        // 文字的宽度
        float text_Width = mTextPaint.measureText(content);

        // 计算内圆直径
        innerDiameter = (int) (text_Width + 2 * padding);
        // 计算外圆直径
        outerDiameter = innerDiameter + 2 * padding;

        outerRect = new RectF(padding / 2, padding / 2, outerDiameter - padding / 2, outerDiameter - padding / 2);
    }

    public void setListener(OnTimeClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(outerDiameter, outerDiameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制内圆
        canvas.drawCircle(outerDiameter / 2, outerDiameter / 2, innerDiameter / 2, innerPaint);

        canvas.save();
        canvas.rotate(-90, outerDiameter / 2, outerDiameter / 2);
        // 绘制外圆圆弧
        canvas.drawArc(outerRect, 0f, degrees, false, outerPaint);
        canvas.restore();

        float y = canvas.getHeight() / 2;

        canvas.drawText(content, padding * 2, y - (mTextPaint.descent() + mTextPaint.ascent()) / 2, mTextPaint);

    }

    /**
     * 设置当前角度旋转进度
     *
     * @param frequency    刷新总次数
     * @param nowFrequency 当前已刷新次数
     */
    public void setProgress(int frequency, int nowFrequency) {
        // 计算出刷新间隔
        int space = 360 / frequency;
        // 计算出当前需转的角度
        degrees = space * nowFrequency;
        // 重绘
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if (mListener != null) {
                    mListener.onClickTime(this);
                }
                break;
        }
        return true;
    }
}
