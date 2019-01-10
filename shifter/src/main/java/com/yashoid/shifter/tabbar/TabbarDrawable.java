package com.yashoid.shifter.tabbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.yashoid.shifter.Shadow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TabbarDrawable extends Drawable {

    private float mRadius; // TODO
    private float mDepth; // TODO

    private Paint mPaint;

    private float mCenterX;
    private float mCenterY;
    private float mSideDx;
    private float mSideY;
    private float mDegrees;

    private Path mPath = new Path();

    private Shadow.LinearShadow mLinearShadow;
    private Shadow.CircularShadow mCenterShadow;
    private Shadow.CircularShadow mSideShadow;

    private RectF mHelper = new RectF();

    public TabbarDrawable(int color, float radius, float depth, float elevation) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        mRadius = radius;
        mDepth = depth;

        mLinearShadow = Shadow.LinearShadow.newInstance(elevation);
        mCenterShadow = Shadow.CircularShadow.newInstance(elevation, false);
        mSideShadow = Shadow.CircularShadow.newInstance(elevation, true);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mCenterX = bounds.centerX();
        mCenterY = bounds.top + mDepth - mRadius;

        mSideY = bounds.top + mRadius;

        float centersDy = mRadius + bounds.top - mCenterY;
        mSideDx = (float) Math.sqrt(4 * mRadius * mRadius - centersDy * centersDy);

        double angle = Math.acos(Math.abs(centersDy) / (2 * mRadius));
        mDegrees = (float) Math.toDegrees(angle);

        mPath.reset();

        mPath.moveTo(bounds.left, bounds.top);
        mPath.lineTo(bounds.left + mCenterX - mSideDx, bounds.top);

        mHelper.set(-mRadius, -mRadius, mRadius, mRadius);
        mHelper.offset(mCenterX - mSideDx, mSideY);
        mPath.arcTo(mHelper, -90, mDegrees, false);

        mHelper.offset(mSideDx, -centersDy);
        mPath.arcTo(mHelper, 90 + mDegrees, -2 * mDegrees, false);

        mHelper.offset(mSideDx, centersDy);
        mPath.arcTo(mHelper, -90 - mDegrees, mDegrees, false);

        mPath.lineTo(bounds.right, bounds.top);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.top);
        mPath.close();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();

        mLinearShadow.draw(bounds.left, bounds.top, -90, mCenterX - mSideDx - bounds.left, canvas);
        mLinearShadow.draw(mCenterX + mSideDx, bounds.top, -90, mCenterX - mSideDx - bounds.left, canvas);

        mCenterShadow.draw(mCenterX, mCenterY, mRadius,90 - mDegrees, 2 * mDegrees, canvas);

        mSideShadow.draw(mCenterX - mSideDx, mSideY, mRadius,-90, mDegrees, canvas);
        mSideShadow.draw(mCenterX + mSideDx, mSideY, mRadius,-90, -mDegrees, canvas);

        canvas.drawPath(mPath, mPaint);
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
