package com.yashoid.shifter;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HeaderDrawable extends Drawable {

    private static final float ELLIPSE_SIZE_RATIO = 1.7f;

    private Paint mPaint;

    private RectF mRect = new RectF();

    public HeaderDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff164ba0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        float halfHeight = bounds.height();
        float height = halfHeight * 2;
        float width = height * ELLIPSE_SIZE_RATIO;

        mRect.set(-width / 2, -halfHeight, width / 2, halfHeight);
        mRect.offset(bounds.left + bounds.width() / 2, bounds.top);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawOval(mRect, mPaint);
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
