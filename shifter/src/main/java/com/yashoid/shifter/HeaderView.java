package com.yashoid.shifter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HeaderView extends FrameLayout {

    private static final float ELLIPSE_SIZE_RATIO = 1.7f;

    private RectF mRect = new RectF();

    private Path mPath = new Path();

    public HeaderView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);

        super.dispatchDraw(canvas);

        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        float halfHeight = getHeight();
        float height = halfHeight * 2;
        float width = height * ELLIPSE_SIZE_RATIO;

        mRect.set(-width / 2, -halfHeight, width / 2, halfHeight);
        mRect.offset(getWidth() / 2, 0);

        mPath.reset();

        mPath.addOval(mRect, Path.Direction.CW);
    }

}
