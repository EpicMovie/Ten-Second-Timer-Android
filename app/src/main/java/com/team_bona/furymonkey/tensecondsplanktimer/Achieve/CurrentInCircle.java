package com.team_bona.furymonkey.tensecondsplanktimer.Achieve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

/**
 * Created by furymonkey on 2017-02-28.
 */

public class CurrentInCircle extends View{
    private static final float STROKE_WIDTH = 10f;

    private int mPassedTime;
    private float mWidthPadding;
    private float mHeightPadding;

    private Rect mMinutesBound;
    private Rect mSecondsBound;
    private Rect mBunBound;
    private Rect mChoBound;

    private Paint mCirclePaint;
    private Paint mWordTextPaint;
    private Paint mTimeTextPaint;
    private RectF mCircleBound;

    private Context mContext;

    public CurrentInCircle(Context context){
        this(context, null);
    }

    public CurrentInCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.circleColor));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(STROKE_WIDTH);

        mTimeTextPaint = new Paint();
        mTimeTextPaint.setColor(ContextCompat.getColor(mContext, R.color.wordColor));
        mTimeTextPaint.setAntiAlias(true);
        mTimeTextPaint.setTypeface(FontClass.bold);

        mWordTextPaint = new Paint();
        mWordTextPaint.setColor(ContextCompat.getColor(mContext, R.color.wordColor));
        mWordTextPaint.setAntiAlias(true);
        mWordTextPaint.setTypeface(FontClass.regular);

        mMinutesBound = new Rect(); mSecondsBound = new Rect();
        mBunBound = new Rect(); mChoBound = new Rect();
    }

    public void setPassedTime(int passedTime) {
        mPassedTime = passedTime;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int)(MeasureSpec.getSize(widthMeasureSpec) *  5 / 7 + STROKE_WIDTH * 2);
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidthPadding = getWidth() / 7;
        mHeightPadding = STROKE_WIDTH;
        setTextSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCircleBound = new RectF(mWidthPadding, mHeightPadding, getWidth() - mWidthPadding, getHeight() - mHeightPadding);
        // mCircleBound = new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding);

        canvas.drawArc(mCircleBound, 0, 360, false, mCirclePaint);
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        String minutes, seconds;
        String bun = "  분";
        String cho = "  초";

        int x, y;

        if(mPassedTime / 60f >= 1) {
            minutes = String.format("%d", mPassedTime / 60);
            seconds = String.format(" %d", mPassedTime % 60);

            mTimeTextPaint.getTextBounds(minutes, 0, minutes.length(), mMinutesBound);
            mTimeTextPaint.getTextBounds(seconds, 0, seconds.length(), mSecondsBound);
            mWordTextPaint.getTextBounds(bun, 0, bun.length(), mBunBound);
            mWordTextPaint.getTextBounds(cho, 0, cho.length(), mChoBound);

            x = (getWidth() - (mMinutesBound.width() + mSecondsBound.width() + mBunBound.width() + mChoBound.width())) / 2;
            // Height의 경우 큰 글자를 기준으로 그 아래에서 부터 시작하므로 큰 글자의 높이만을 계싼하면 된다.
            // y = getHeight() / 2 + mMinutesBound.height() / 2 - (int)mPadding;
            y = (getHeight() + mMinutesBound.height()) / 2 - (int) mHeightPadding;

            canvas.drawText(minutes, x, y, mTimeTextPaint);
            canvas.drawText(bun, x + mMinutesBound.width(), y, mWordTextPaint);
            canvas.drawText(seconds, x + mMinutesBound.width() + mBunBound.width(), y, mTimeTextPaint);
            canvas.drawText(cho, x + mMinutesBound.width() + mBunBound.width() + mSecondsBound.width() + mChoBound.width(), y, mWordTextPaint);
        }else {
            seconds = String.format("%d", mPassedTime % 60);

            mTimeTextPaint.getTextBounds(seconds, 0, seconds.length(), mSecondsBound);
            mWordTextPaint.getTextBounds(cho, 0, cho.length(), mChoBound);

            x = (getWidth() - (mSecondsBound.width() +  mChoBound.width())) / 2;
            // Height의 경우 큰 글자를 기준으로 그 아래에서 부터 시작하므로 큰 글자의 높이만을 계싼하면 된다.
            y = (getHeight() + mSecondsBound.height()) / 2 - (int) mHeightPadding;

            canvas.drawText(seconds, x, y, mTimeTextPaint);
            canvas.drawText(cho, x + mSecondsBound.width(), y, mWordTextPaint);
        }
    }

    private void setTextSize(){
        float timeTextSize = getWidth() / 5f;
        float wordTextSize = timeTextSize / 5f;

        mTimeTextPaint.setTextSize(timeTextSize);
        mWordTextPaint.setTextSize(wordTextSize);
    }
}
