package com.yashoid.shifter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class GigilDrawable extends Drawable {

    private static final long FAKE_DURATION = 200;

    private static final float BUMP_RATIO = 24f / 360f;

    private static final float MAXIMUM_SPRING_SPEED = 2f;
    private static final float MAXIMUM_SPEED = 3f;

    private static final float GIGIL_ICON_SIZE_RATIO = 0.5f;

    public interface OnGigilMovedListener {

        void onGigilMoved(int position);

    }

    private OnGigilMovedListener mListener = null;

    private Handler mHandler;

    private boolean mRtl;

    private boolean mDefaultIsOpen = false;

    private Spring mSpring = new Spring();

    private Drawable mGigilIcon;

    private Paint mPaint;

    private Path mPath = new Path();

    private float mBump;

    private float mMaximumSpeed;

    private int mPosition;
    private float mTargetX;

    private float mDestinationX;

    private long mTimeTracker = -1;

    private boolean mFaking = false;
    private float mFakeProgress = 0;

    public GigilDrawable(Context context, boolean rtl, int color, int iconResId) {
        mHandler = new Handler();

        mRtl = rtl;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        mGigilIcon = ContextCompat.getDrawable(context, iconResId);
    }

    public void setDefaultIsOpen(boolean isOpen) {
        mDefaultIsOpen = isOpen;
    }

    public void setOnGigilMovedListener(OnGigilMovedListener listener) {
        mListener = listener;
    }

    public int getPosition() {
        return mPosition;
    }

    public float getBump() {
        return mSpring.getSize();
    }

    public float getDefaultBumpSize() {
        return mBump;
    }

    public float getTargetX() {
        return mDestinationX;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        float height = bounds.height();

        mBump = height * BUMP_RATIO;

        int gigilIconWidth = (int) (mBump * GIGIL_ICON_SIZE_RATIO);
        int gigilIconHeight = gigilIconWidth * mGigilIcon.getIntrinsicHeight() / mGigilIcon.getIntrinsicWidth();
        mGigilIcon.setBounds(0, 0, gigilIconWidth, gigilIconHeight);

        if (mRtl) {
            if (mDefaultIsOpen) {
                mTargetX = -mBump;
            }
            else {
                mTargetX = bounds.right - mBump;
            }
        }
        else {
            if (mDefaultIsOpen) {
                mTargetX = bounds.right + mBump;
            }
            else {
                mTargetX = mBump;
            }
        }

        mSpring.setMinimumSize(0);
        mSpring.setMaximumSize(mBump * 6);
        mSpring.setDefaultSize(mBump);
        mSpring.setSize(mBump);

        mSpring.setMaximumSpeed(bounds.width() * MAXIMUM_SPRING_SPEED);

        mMaximumSpeed = bounds.width() * MAXIMUM_SPEED;

        measureCurve();
    }

    public void setTargetX(float targetX) {
        mDestinationX = targetX;

        invalidateSelf();
    }

    public void setInstantX(float targetX) {
        mDestinationX = targetX;
        mTargetX = targetX;
        mSpring.setSize(mBump);

        measureCurve(false);

        invalidateSelf();

        if (mListener != null) {
            mListener.onGigilMoved(mPosition);
        }
    }

    private ValueAnimator mFakeAnimator = null;

    public void startFake() {
        if (mFakeAnimator != null) {
            mFakeAnimator.cancel();
        }

        mFaking = true;
        mFakeProgress = 0;

        mFakeAnimator = new ValueAnimator();
        mFakeAnimator.setDuration(FAKE_DURATION);
        mFakeAnimator.setFloatValues(0, 1);
        mFakeAnimator.setInterpolator(new DecelerateInterpolator());

        mFakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFakeProgress = (float) animation.getAnimatedValue();

                measureCurve();
                invalidateSelf();
            }
        });

        mFakeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFaking = false;
                mFakeAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }

        });

        mFakeAnimator.start();
    }

    private void measureCurve() {
        measureCurve(true);
    }

    private void measureCurve(boolean shouldNotify) {
        if (mRtl) {
            measureCurveRtl(shouldNotify);
        }
        else {
            measureCurveLtr(shouldNotify);
        }
    }

    private void measureCurveLtr(boolean shouldNotify) {
        final Rect bounds = getBounds();

        mPath.reset();

        float bump = mTargetX - bounds.left;
        int left = (int) (bump - mSpring.getSize());

        float right = bump;

        if (mFaking) {
            right = left + mSpring.getSize() * mFakeProgress;
        }

        mPosition = left;

        if (shouldNotify) {
            notifyOnGigilMoved();
        }

        mPath.moveTo(left - 5, bounds.top);

        mPath.lineTo(left, bounds.top);

        mPath.cubicTo(
                left, bounds.centerY(),
                right, bounds.centerY() - mBump,
                right, bounds.centerY());

        mPath.cubicTo(
                right, bounds.centerY() + mBump,
                left, bounds.centerY(),
                left, bounds.bottom
        );

        mPath.lineTo(left - 5, bounds.bottom);

        mPath.close();
    }

    private void measureCurveRtl(boolean shouldNotify) {
        final Rect bounds = getBounds();

        mPath.reset();

        float left = mTargetX;
        int right = (int) (left + mSpring.getSize());

        if (mFaking) {
            left = right - mFakeProgress * mSpring.getSize();
        }

        mPosition = right;

        if (shouldNotify) {
            notifyOnGigilMoved();
        }

        mPath.moveTo(right + 5, bounds.top);

        mPath.lineTo(right, bounds.top);

        mPath.cubicTo(
                right, bounds.centerY(),
                left, bounds.centerY() - mBump,
                left, bounds.centerY());

        mPath.cubicTo(
                left, bounds.centerY() + mBump,
                right, bounds.centerY(),
                right, bounds.bottom
        );

        mPath.lineTo(right + 5, bounds.bottom);

        mPath.close();
    }

    private void notifyOnGigilMoved() {
        if (mListener != null) {
            mHandler.post(mOnGigilMovedNotifier);
        }
    }

    private Runnable mOnGigilMovedNotifier = new Runnable() {

        @Override
        public void run() {
            mListener.onGigilMoved(mPosition);
        }

    };

    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect bounds = getBounds();

//        canvas.drawRect(bounds.left, bounds.top, mPosition, bounds.bottom, mPaint);

        canvas.drawPath(mPath, mPaint);

        canvas.save();
        int gigilIconWidth = mGigilIcon.getBounds().width();
        int gigilIconHeight = mGigilIcon.getBounds().height();

        if (mRtl) {
            float gigilIconLeft = mPosition - mSpring.getSize() + (mBump - gigilIconWidth) / 2;
            canvas.translate(gigilIconLeft, bounds.centerY() - gigilIconHeight / 2);
        }
        else {
            float gigilIconRight = mPosition + mSpring.getSize() - (mBump - gigilIconWidth) / 2;
            canvas.translate(gigilIconRight - gigilIconWidth, bounds.centerY() - gigilIconHeight / 2);
        }

        mGigilIcon.draw(canvas);

        canvas.restore();

        if (mTimeTracker == -1) {
            mTimeTracker = System.nanoTime();
            return;
        }

        boolean curveInvalidated = false;

        long now = System.nanoTime();

        if (mTargetX != mDestinationX) {
            long passedTime = now - mTimeTracker;

            if (passedTime > 17_000_000) {
                passedTime = 17_000_000;
            }

            float targetMovement = passedTime / 1_000_000_000f * mMaximumSpeed;

            if (mTargetX > mDestinationX) {
                targetMovement = -targetMovement;
            }

            if (Math.abs(targetMovement) > Math.abs(mTargetX - mDestinationX)) {
                targetMovement = mDestinationX - mTargetX;
            }

            mTargetX += targetMovement;

            if (mRtl) {
                mSpring.setSize(mSpring.getSize() - targetMovement);
            }
            else {
                mSpring.setSize(mSpring.getSize() + targetMovement);
            }

            curveInvalidated = true;
        }

        mTimeTracker = now;


        float sizeChange = mSpring.step();

        if (Math.abs(sizeChange) > 0 || curveInvalidated) {
            measureCurve();

            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
