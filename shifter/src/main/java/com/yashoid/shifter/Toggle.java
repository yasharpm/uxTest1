package com.yashoid.shifter;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class Toggle extends View implements View.OnClickListener {

    public static final long ANIMATION_DURATION = 200;

    private static final float CENTER_X = 22f / 39f;
    private static final float SIDE_X = 27f / 39f;
    private static final float SIDE_Y = 29f / 88f;
    private static final float CIRCLE_RADIuS = 4f / 39f;
    private static final float LINE_WIDTH = 6f / 39f;
    private static final float LINE_HEIGHT = 2f / 39f;
    private static final float LINE_MARGIN = 3f / 39f;

    private static final int BACKGROUND_UNSELECTED = 0xffd4d4d4;
    private static final int BACKGROUND_SELECTED = 0xff164ba0;

    public interface OnToggleSwitchedListener {

        void onToggleSwitched(boolean isSelected);

    }

    private OnToggleSwitchedListener mOnToggleSwitchedListener = null;

    private Paint mFillPaint;
    private Paint mCirclePaint;
    private Paint mLinePaint;

    private float mCenterX;
    private float mCenterY;
    private float mSideX;
    private float mSideY1;
    private float mSideY2;
    private float mCircleRadius;
    private float mLineWidth;
    private float mLineHeight;
    private float mLineMargin;

    private float mSelectionRatio = 1f;
    private boolean mSelected = true;

    private ValueAnimator mAnimator = null;

    private ArgbEvaluator mColorEvaluator = new ArgbEvaluator();

    public Toggle(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public Toggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public Toggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint(mFillPaint);
        mCirclePaint.setColor(0xffffffff);

        mLinePaint = new Paint(mCirclePaint);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        setOnClickListener(this);

        update();
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setOnToggleSwitchedListener(OnToggleSwitchedListener listener) {
        mOnToggleSwitchedListener = listener;
    }

    private void update() {
        mFillPaint.setColor((Integer) mColorEvaluator.evaluate(mSelectionRatio, BACKGROUND_UNSELECTED, BACKGROUND_SELECTED));

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float width = getWidth() / 2;
        float height = getHeight();

        mCenterX = CENTER_X * width;
        mCenterY = height / 2;

        mSideX = SIDE_X * width;
        mSideY1 = SIDE_Y * height;
        mSideY2 = mCenterY + (mCenterY - mSideY1);

        mCircleRadius = CIRCLE_RADIuS * width;

        mLineWidth = LINE_WIDTH * width;
        mLineHeight = LINE_HEIGHT * width;
        mLineMargin = LINE_MARGIN * width;

        mLinePaint.setStrokeWidth(mLineHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mFillPaint);

        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);
        canvas.drawCircle(mSideX, mSideY1, mCircleRadius, mCirclePaint);
        canvas.drawCircle(mSideX, mSideY2, mCircleRadius, mCirclePaint);

        if (mSelectionRatio == 0) {
            return;
        }

        float lineWidth = mLineWidth * mSelectionRatio;

        canvas.drawLine(mCenterX - mCircleRadius - mLineMargin - lineWidth, mCenterY, mCenterX - mCircleRadius - mLineMargin, mCenterY, mLinePaint);
        canvas.drawLine(mSideX - mCircleRadius - mLineMargin - lineWidth, mSideY1, mSideX - mCircleRadius - mLineMargin, mSideY1, mLinePaint);
        canvas.drawLine(mSideX - mCircleRadius - mLineMargin - lineWidth, mSideY2, mSideX - mCircleRadius - mLineMargin, mSideY2, mLinePaint);
    }

    @Override
    public void onClick(View v) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = new ValueAnimator();
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.setFloatValues(mSelectionRatio, mSelected ? 0 : 1);
        mAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSelectionRatio = (float) animation.getAnimatedValue();
                update();
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }

        });

        mAnimator.start();

        mSelected = !mSelected;

        if (mOnToggleSwitchedListener != null) {
            mOnToggleSwitchedListener.onToggleSwitched(mSelected);
        }
    }

}
