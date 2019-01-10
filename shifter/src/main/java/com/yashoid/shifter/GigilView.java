package com.yashoid.shifter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class GigilView extends View implements GigilDrawable.OnGigilMovedListener {

    private static final float TEXT_SIZE_RATIO = 11f / 24f;
    private static final float LEFT_MARGIN_RATIO = 8f / 24f;
    private static final float RIGHT_MARGIN_RATIO = LEFT_MARGIN_RATIO / 2;
    private static final float SWIPE_WIDTH_RATIO = 18.3f / 24f;

    private static final String TEXT = "همه خدمات";

    private GigilDrawable.OnGigilMovedListener mOnGigilMovedListener = null;

    private GestureDetector mGestureDetector;

    private GigilDrawable mGigil;

    private boolean mIsFlinging = false;

    private Paint mTextPaint;
    private float mTextLeft;
    private float mTextY;

    private Drawable mSwipe;

    public GigilView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public GigilView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public GigilView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mGigil = new GigilDrawable(context);
        mGigil.setOnGigilMovedListener(this);

        setBackground(mGigil);

        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.iransansmobile));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(0xff164ba0);

        mSwipe = ResourcesCompat.getDrawable(getResources(), R.drawable.swipe, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getWidth() == 0) {
            return;
        }

        mGigil.setBounds(0, 0, getWidth(), getHeight());

        float bump = mGigil.getDefaultBumpSize();

        float textLeftMargin = bump * LEFT_MARGIN_RATIO;
        float textRightMargin = bump * RIGHT_MARGIN_RATIO;
        float textSize = bump * TEXT_SIZE_RATIO;

        mTextPaint.setTextSize(textSize);
        mTextLeft = bump + textLeftMargin;

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        mTextY = getHeight() / 2 - (fm.descent + fm.ascent);

        int swipeLeft = (int) (mTextLeft + mTextPaint.measureText(TEXT) + textRightMargin);
        int swipeWidth = (int) (bump * SWIPE_WIDTH_RATIO);
        int swipeHeight = swipeWidth * mSwipe.getIntrinsicHeight() / mSwipe.getIntrinsicWidth();
        int swipeTop = (getHeight() - swipeHeight) / 2;

        mSwipe.setBounds(swipeLeft, swipeTop, swipeLeft + swipeWidth, swipeTop + swipeHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mGigil.getLeft(), 0, getWidth(), getHeight());
        canvas.drawText(TEXT, mTextLeft, mTextY, mTextPaint);
        mSwipe.draw(canvas);
        canvas.restore();

        super.draw(canvas);
    }

    public int getGigilLeft() {
        return mGigil.getLeft();
    }

    public void setOnGigilMovedListener(GigilDrawable.OnGigilMovedListener listener) {
        mOnGigilMovedListener = listener;
    }

    private ValueAnimator mAlphaAnimator = null;
    private int mAlphaDestination = 255;
    private boolean mFirstZero = false;

    @Override
    public void onGigilMoved(int left) {
        boolean startAlphaAnimator = false;

        if (left == 0) {
            if (mFirstZero || mAlphaDestination != 0) {
                mFirstZero = false;
            }
            else {
                int startAlpha = 0;

                if (mAlphaAnimator != null) {
                    startAlpha = (int) mAlphaAnimator.getAnimatedValue();

                    mAlphaAnimator.cancel();
                }

                mAlphaAnimator = new ValueAnimator();
                mAlphaAnimator.setIntValues(startAlpha, 255);

                mAlphaDestination = 255;

                startAlphaAnimator = true;
            }
        }
        else {
            if (mAlphaAnimator == null && mAlphaDestination != 0) {
                mAlphaAnimator = new ValueAnimator();
                mAlphaAnimator.setIntValues(255, 0);

                mAlphaDestination = 0;

                startAlphaAnimator = true;
            }
            else if (mAlphaDestination == 255) {
                int startAlpha = (int) mAlphaAnimator.getAnimatedValue();

                mAlphaAnimator.cancel();

                mAlphaAnimator = new ValueAnimator();
                mAlphaAnimator.setIntValues(startAlpha, 0);

                mAlphaDestination = 0;

                startAlphaAnimator = true;
            }
        }

        if (startAlphaAnimator) {
            mAlphaAnimator.setDuration(500);
            mAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int alpha = (Integer) animation.getAnimatedValue();

                    mTextPaint.setAlpha(alpha);
                    mSwipe.setAlpha(alpha);

                    invalidate();
                }
            });
            mAlphaAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAlphaAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) { }

                @Override
                public void onAnimationRepeat(Animator animation) { }

            });
            mAlphaAnimator.start();
        }

        if (mOnGigilMovedListener != null) {
            mOnGigilMovedListener.onGigilMoved(left);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);

        if (!mIsFlinging) {
            if (event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE) {
                if (mGigil.getLeft() + mGigil.getBump() < getWidth() / 2) {
                    close();
                }
                else {
                    open();
                }
            }
        }

        return result;
    }

    public boolean isOpenOrOpening() {
        return mGigil.getTargetX() > getWidth() / 2;
    }

    public void open() {
        mGigil.setTargetX(getWidth() + mGigil.getDefaultBumpSize());
    }

    public void close() {
        mGigil.setTargetX(mGigil.getDefaultBumpSize());
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            float left = mGigil.getLeft();
            float right = left + 2 * mGigil.getBump();

            if (e.getX() > left && e.getX() < right) {
                setGigilTarget(e.getX());

                mIsFlinging = false;

                return true;
            }

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) { }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mGigil.getLeft() < getWidth() / 2 && e.getX() < mGigil.getDefaultBumpSize() && e.getY() > getHeight() / 3 && e.getY() < 2 * getHeight() / 3) {
                mIsFlinging = true;

                open();

                return true;
            }

            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            setGigilTarget(e2.getX());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) { }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mIsFlinging = true;

            if (velocityX > 0) {
                open();
            }
            else {
                close();
            }

            return true;
        }

    };

    private void setGigilTarget(float x) {
        mGigil.setTargetX(Math.max(mGigil.getDefaultBumpSize(), x));
    }

}
