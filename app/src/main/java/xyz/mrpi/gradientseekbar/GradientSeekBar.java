package xyz.mrpi.gradientseekbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;



/**
 *
 * Created by MrPi on 2016/12/26.
 */

public class GradientSeekBar extends View {

    private Paint mPaint;
    private Paint mCirclePaint;
    private Path mPath;
    private Paint mTextPaint;
    private LinearGradient mLinearGradient;
    private String mText;
    private float mProgressHeight;
    private float mCircle;
    private float mTextSize;
    private float height ;
    private RectF mRectF;

    private float mProgress = 0;

    public GradientSeekBar(Context context) {
        this(context,null);
    }

    public GradientSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        initSize();
        initPaint();
    }

    private void initSize() {

        mProgressHeight = RudenessScreenHelper.dp2px(getContext(),2);
        mTextSize = RudenessScreenHelper.pt2px(getContext(),24);

        height = (int) (mProgressHeight + mTextSize + RudenessScreenHelper.pt2px(getContext(),10));
        mCircle = RudenessScreenHelper.dp2px(getContext(),3);

        mRectF = new RectF();
        mRectF.left = 0;
        mRectF.top = height - mProgressHeight;
        mRectF.bottom = height;

    }

    private void initPaint() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.WHITE);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        mRectF.right = getMeasuredWidth()*mProgress;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mLinearGradient = new LinearGradient(0, 0, getMeasuredWidth() * mProgress, 0, new int[]{0xff2092c2, 0xff48edf0,0xfffbfbfd}, new float[]{0,1,0.5f}, LinearGradient.TileMode.CLAMP);
        }

        mPaint.setShader(mLinearGradient);

        mPath.addRect(mRectF, Path.Direction.CCW);
        canvas.drawPath(mPath,mPaint);

        mPath.reset();
        mPath.addCircle(mProgress*getMeasuredWidth(),mRectF.centerY() ,mCircle, Path.Direction.CCW);
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPath,mPaint);

        mText = "已售"+String.valueOf(Math.floor(mProgress*100f))+"%";

        if(mProgress<=getXValue()) {
            canvas.drawText(mText,mProgress*getMeasuredWidth(),RudenessScreenHelper.pt2px(getContext(),24),mTextPaint);
        }else {
            canvas.drawText(mText,getXValue()*getMeasuredWidth(),RudenessScreenHelper.pt2px(getContext(),24),mTextPaint);
        }

    }


    private float getXValue() {
      return (getMeasuredWidth()-mTextPaint.measureText(mText))/getMeasuredWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int height = (int) (mProgressHeight + mTextSize + RudenessScreenHelper.pt2px(getContext(),10) + mCircle);
        int width = wm.getDefaultDisplay().getWidth();
        setMeasuredDimension(width,height);

    }

    ValueAnimator valueAnimator;

    public void setProgress(float progress) {
        valueAnimator = ValueAnimator.ofFloat(progress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration((long) (1500*progress));
        valueAnimator.start();
    }

}
