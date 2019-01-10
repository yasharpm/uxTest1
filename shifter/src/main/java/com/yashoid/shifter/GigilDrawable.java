package com.yashoid.shifter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class GigilDrawable extends Drawable {

    private static final float BUMP_RATIO = 24f / 360f;

    private static final float MAXIMUM_SPRING_SPEED = 2f;
    private static final float MAXIMUM_SPEED = 3f;

    private static final float GIGIL_ICON_SIZE_RATIO = 0.5f;

    public interface OnGigilMovedListener {

        void onGigilMoved(int left);

    }

    private OnGigilMovedListener mListener = null;

    private Handler mHandler;

    private Spring mSpring = new Spring();

    private Drawable mGigilIcon;

    private Paint mPaint;

    private Path mPath = new Path();

    private float mBump;

    private float mMaximumSpeed;

    private int mLeft;
    private float mTargetX;

    private float mDestinationX;

    private long mTimeTracker = -1;

    public GigilDrawable(Context context) {
        mHandler = new Handler();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xff002b71);
        mPaint.setStyle(Paint.Style.FILL);

        mGigilIcon = ContextCompat.getDrawable(context, R.drawable.ic_gigil);
    }

    public void setOnGigilMovedListener(OnGigilMovedListener listener) {
        mListener = listener;
    }

    public int getLeft() {
        return mLeft;
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

        int gigilIconSize = (int) (mBump * GIGIL_ICON_SIZE_RATIO);
        mGigilIcon.setBounds(0, 0, gigilIconSize, gigilIconSize);

        mTargetX = mBump;

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

    private void measureCurve() {
        final Rect bounds = getBounds();

        mPath.reset();

        float bump = mTargetX - bounds.left;
        int left = (int) (bump - mSpring.getSize());

        mLeft = left;

        notifyOnGigilMoved();

        mPath.moveTo(left, bounds.top);

        mPath.cubicTo(
                left, bounds.centerY(),
                bump, bounds.centerY() - mBump,
                bump, bounds.centerY());

        mPath.cubicTo(
                bump, bounds.centerY() + mBump,
                left, bounds.centerY(),
                left, bounds.bottom
        );

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
            mListener.onGigilMoved(mLeft);
        }

    };

    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect bounds = getBounds();

//        canvas.drawRect(bounds.left, bounds.top, mLeft, bounds.bottom, mPaint);

        canvas.drawPath(mPath, mPaint);

        canvas.save();
        int gigilIconSize = mGigilIcon.getBounds().width();
        float gigilIconRight = mLeft + mSpring.getSize() - (mBump - gigilIconSize) / 2;
        canvas.translate(gigilIconRight - gigilIconSize, bounds.centerY() - gigilIconSize / 2);
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

            mSpring.setSize(mSpring.getSize() + targetMovement);

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
