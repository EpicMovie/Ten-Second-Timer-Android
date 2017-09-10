package com.team_bona.furymonkey.tensecondsplanktimer.Timer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

import java.util.concurrent.TimeUnit;

/**
 * Created by furymonkey on 2016-10-25.
 */

public class TimerView extends View {
    private static final int ARC_START_ANGLE = 270;
    private static final float STROKE_WIDTH = 10f;
    private static final float ROUND_CIRCLE_RADIUS = STROKE_WIDTH * 3f;

    private boolean isTenClicked = false;
    private boolean isRunning = false;
    private int wholeTime = 0;              // 총 시간
    private int passedTime = 0;             // Animation이 동작한 시간
    private float mPadding;
    private float mInnerCirclePadding;

    private Paint mRectPaint;
    private RectF mCircleBounds;
    private Paint mCirclePaint;
    private Paint mRunAroundCirclePaint;
    private float mCircleSweepAngle;                                            // 시시각으로 변하는 각의 값을 저장하는 변수이다.
    private Paint mTextPaint;                                                     // View 가운데 그려지는 Timer의 Paint특성을 저장하는 변수이다.
    private Paint mWholeTimeTextPaint;                                           // View 위쪽의 총 시간을 나타내기 위해 필요한 Paint Value
    private Path mPath;
    private Paint mAdditionalCirclePaint;

    private Context mContext;

    private ValueAnimator mTimerAnimator;

//    private OnTenSecondsLeftListener mOnTenSecondsLeftListener;
    private OnCompleteListener mOnCompleteListener;

    public TimerView(Context context){
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        mContext = context;

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.circleColor));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(STROKE_WIDTH);

        mAdditionalCirclePaint = new Paint();
        mAdditionalCirclePaint.setAntiAlias(true);
        mAdditionalCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.circleColor));
        mAdditionalCirclePaint.setStyle(Paint.Style.STROKE);
        mAdditionalCirclePaint.setStrokeWidth(STROKE_WIDTH);

        mRunAroundCirclePaint = new Paint();
        mRunAroundCirclePaint.setAntiAlias(true);
        mRunAroundCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.circleColor));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(FontClass.bold);
        mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.wordColor));

        mWholeTimeTextPaint = new Paint();
        mWholeTimeTextPaint.setAntiAlias(true);
        mWholeTimeTextPaint.setTypeface(FontClass.thin);
        mWholeTimeTextPaint.setColor(ContextCompat.getColor(mContext, R.color.wordColor));

        mPath = new Path();
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(ContextCompat.getColor(mContext, R.color.fulfillColor));
    }

    public interface OnTenSecondsLeftListener{
        void onLeft();
    }

    public interface OnCompleteListener{
        void onComplete();
    }
/*
    public void setOnTenSecondsLeftListener(OnTenSecondsLeftListener listener){
        mOnTenSecondsLeftListener = listener;
    }
*/
    public void setOnCompleteListener(OnCompleteListener listener){
        mOnCompleteListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mPadding = getWidth() / 7;
        mInnerCirclePadding = mPadding + ROUND_CIRCLE_RADIUS * 6 / 5f;
        updateBounds();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        final int prev_state = canvas.save();

        if(!isTenClicked)
            canvas.drawArc(mCircleBounds, ARC_START_ANGLE, mCircleSweepAngle, false, mCirclePaint);
/*        else{
            canvas.drawArc(mCircleBounds, ARC_START_ANGLE, 360, false, mCirclePaint);
            canvas.drawArc(mAdditionalCircleBounds, ARC_START_ANGLE, mCircleSweepAngle, false, mAdditionalCirclePaint);
        }*/

        if(!isTenClicked) {
            canvas.drawCircle(circlePosition_X(mCircleBounds.centerX(), mCircleBounds.width(), false),
                    circlePosition_Y(mCircleBounds.centerY(), mCircleBounds.height(), false),
                    ROUND_CIRCLE_RADIUS, mRunAroundCirclePaint);
        } else {
            if(passedTime >= wholeTime - 10)
                canvas.drawCircle(circlePosition_X(mCircleBounds.centerX(), mCircleBounds.width(), true),
                        circlePosition_Y(mCircleBounds.centerY(), mCircleBounds.height(), true),
                        ROUND_CIRCLE_RADIUS, mRunAroundCirclePaint);
            else
                canvas.drawCircle(circlePosition_X(mCircleBounds.centerX(), mCircleBounds.width(), false),
                        circlePosition_Y(mCircleBounds.centerY(), mCircleBounds.height(), false),
                        ROUND_CIRCLE_RADIUS, mRunAroundCirclePaint);
        }

        setTextSize();
        canvas.clipPath(mPath);
        drawProgressRect(canvas);
        drawPassedTimeText(canvas);
        canvas.restoreToCount(prev_state);
        drawWholeTimeText(canvas);
    }

    public void start(int secs){
        wholeTime = secs;

        updateBounds();

        // 모든 원들의 색칠이 완료됬을 때 다시 클릭을 할 경우 모든 원들이 사라지지 않는다!
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));// Animation의 길이를 설정한다.
        mTimerAnimator.setInterpolator(new LinearInterpolator());    // linear interpolator로 설정하므로써 애니메이션이 선형적인 변화를 하도록 만들었다.
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            public void onAnimationUpdate(ValueAnimator animation){    // Animation의 수명 동안에 어떻게 반응을 할 것인지를 정의한 listener를 추가해준다.
                setProgress(mTimerAnimator.getCurrentPlayTime());
            }
        });
        isRunning = true;
        mTimerAnimator.start();     // Animation을 시작한다.
    }

    public void stop(){
//        mOnCompleteListener.onComplete();
        isRunning = false;
    }

    public void pause() {
        if(mTimerAnimator != null && mTimerAnimator.isRunning())
            mTimerAnimator.pause();
    }

    public void resume(){
        if(mTimerAnimator != null && mTimerAnimator.isPaused())
            mTimerAnimator.resume();
    }

    public int getPassedTime(){
        return passedTime;
    }

    public int getTotalTime(){
        return wholeTime;
    }

    public void setisTenClicked(boolean b){
        isTenClicked = b;
    }

    public boolean getisTenClicked(){
        return isTenClicked;
    }

    private void setProgress(long mili_secs){
        passedTime = (int) mili_secs / 1000;

        if(mili_secs % 1000 < 10) {
            Log.v("알림", Long.toString(mili_secs / 1000) + "초 애니메이션 동작중");
            Log.v("알림", "PassedTime : " + Integer.toString(passedTime) + " 동작중");
            Log.v("알림", "WholeTime : " + Integer.toString(wholeTime) + " 동작중");
        }

        mCircleSweepAngle = 36 * ((float)(mili_secs % 10000) / 1000);
//        mOnTenSecondsLeftListener.onLeft();

        if (passedTime == wholeTime) {
            Log.v("알림", "완료! WholeTime : " + Integer.toString(wholeTime) + "  PassedTime : " + Integer.toString(passedTime) + " 동작중");
            stop();
        }

        invalidate();       // 화면을 갱신한다. 이 상황에서 onDraw가 호출이 되어 원이 그려지게 된다.
    }

    private void drawPassedTimeText(Canvas canvas){
        Rect bounds = new Rect();
        int x, y;

        if(isRunning) {
            String textPassedTime = String.format("%01d", 10 - passedTime % 10);
            mTextPaint.getTextBounds(textPassedTime, 0, textPassedTime.length(), bounds);
            x = getWidth() / 2 - bounds.width() / 2;
            y = getHeight() / 2 + bounds.height() / 2;

            canvas.drawText(String.valueOf(textPassedTime), x, y, mTextPaint);
        }else{
            String textStartButton = "시작";
            mTextPaint.getTextBounds(textStartButton, 0, textStartButton.length(), bounds);
            x = getWidth() / 2 - bounds.width() / 2;
            y = getHeight() / 2 + bounds.height() / 2;

            canvas.drawText(String.valueOf(textStartButton), x, y, mTextPaint);
        }
    }

    private void drawWholeTimeText(Canvas canvas){
        String textWholeTime = String.format("%02d:%02d", wholeTime >= 60 ? wholeTime / 60 : 0,
                wholeTime < 60 ? wholeTime : wholeTime % 60);
        Rect bounds = new Rect();
        int x, y;

        mWholeTimeTextPaint.getTextBounds(textWholeTime, 0, textWholeTime.length(), bounds);

        x = getWidth() / 2 - bounds.width() / 2;
        y = (int)mInnerCirclePadding + 2 * bounds.height() ;

        canvas.drawText(String.valueOf(textWholeTime), x, y, mWholeTimeTextPaint);
    }

    private void drawProgressRect(Canvas canvas){
        // Eventhough whole time is not setted, onDraw will be called and make error
        if(wholeTime != 0)
            canvas.drawRect(mInnerCirclePadding, getHeight() - mInnerCirclePadding - ((getHeight() - 2 * mInnerCirclePadding) * (float) mTimerAnimator.getCurrentPlayTime() / (1000 * wholeTime)), getWidth() - mInnerCirclePadding, getHeight() - mInnerCirclePadding, mRectPaint);
    }

    private void updateBounds(){
        mCircleBounds = new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding);
        // mAdditionalCircleBounds = new RectF(mInnerCirclePadding, mInnerCirclePadding, getWidth() - mInnerCirclePadding, getHeight() - mInnerCirclePadding);
        mPath.addCircle(getWidth() / 2, getHeight() / 2, (getWidth() - 2 * mInnerCirclePadding) / 2, Path.Direction.CW);
        invalidate();       // 화면을 갱신한다.
    }

    private float circlePosition_X(float center, float width, boolean additional){
        if(additional)
            return center + (mPadding / 2 + width / 2) * (float)Math.cos(Math.toRadians(mCircleSweepAngle - 90));
        else
            return center + width / 2 * (float)Math.cos(Math.toRadians(mCircleSweepAngle - 90));
    }

    private float circlePosition_Y(float center, float height, boolean additional){
        if(additional)
            return center + (mPadding / 2 + height / 2) * (float)Math.sin(Math.toRadians(mCircleSweepAngle - 90));
        else
            return center + height / 2 * (float)Math.sin(Math.toRadians(mCircleSweepAngle - 90));
    }

    private void setTextSize(){
        float textSize = getWidth() / 10f;
        float wholeTimeTextSize = textSize / 2f;

        mTextPaint.setTextSize(textSize);
        mWholeTimeTextPaint.setTextSize(wholeTimeTextSize);
    }
}