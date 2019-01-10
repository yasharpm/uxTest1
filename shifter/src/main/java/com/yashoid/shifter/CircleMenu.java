package com.yashoid.shifter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class CircleMenu extends ViewGroup {

    private static final float INNER_CIRCLE_STROKE = 0.7f;
    private static final float OUTER_CIRCLE_STROKE = 1f;
    private static final float CHILD_SIZE_RATIO = 0.1333f;

    private float mCenterXRatio = 1;
    private float mCenterYRatio = 0.5f;
    private float mRadiusRatio = 0.4f;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private Range mIncludedRange = new Range();

    private Paint mInnerCirclePaint;
    private Paint mOuterCirclePaint;

    private Range mHelperRange = new Range();

    public CircleMenu(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleMenu, defStyleAttr, 0);

        mRadiusRatio = a.getFloat(R.styleable.CircleMenu_radiusRatio, mRadiusRatio);

        boolean willNotDraw = !a.getBoolean(R.styleable.CircleMenu_drawCircle, true);

        a.recycle();

        setClipChildren(false);
        setClipToPadding(false);

        setWillNotDraw(willNotDraw);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setStrokeWidth(INNER_CIRCLE_STROKE * displayMetrics.density);
        mInnerCirclePaint.setColor(0xff9f9f9f);

        mOuterCirclePaint = new Paint(mInnerCirclePaint);
        mOuterCirclePaint.setStrokeWidth(OUTER_CIRCLE_STROKE * displayMetrics.density);
        mOuterCirclePaint.setColor(0xffffffff);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mCenterX = event.getX();
//        mCenterY = event.getY();
//
//        requestLayout();
//
//        return true;
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float width = getWidth();
        float height = getHeight();

        if (mCenterX == 0) {
            mCenterX = width * mCenterXRatio;
            mCenterY = height * mCenterYRatio;
        }

        mRadius = width * mRadiusRatio;

        invalidate();

        final int childCount = getChildCount();

        if (childCount == 0) {
            return;
        }

        final int childSize = (int) (width * CHILD_SIZE_RATIO);
        final int childMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);

        getStartAndEndAngles(mIncludedRange);

        if (mIncludedRange.start == -2 * Math.PI && mIncludedRange.end == 2 * Math.PI) {
            mIncludedRange.start = 0;
            mIncludedRange.end = 2 * Math.PI;
        }

        double angleBetweenChildren = mIncludedRange.range() / childCount;
        double startAngle = mIncludedRange.start + angleBetweenChildren / 2;

        double angle = startAngle;

        for (int i = 0; i < childCount; i++) {
            int childCenterX = (int) (mCenterX*1 + mRadius * Math.cos(angle));
            int childCenterY = (int) (mCenterY*1 + mRadius * Math.sin(angle));

            View child = getChildAt(i);

            child.measure(childMeasureSpec, childMeasureSpec);
            child.layout(
                    childCenterX - childSize / 2, childCenterY - childSize / 2,
                    childCenterX + childSize / 2, childCenterY + childSize / 2);

            angle += angleBetweenChildren;
        }
    }

    private void getStartAndEndAngles(Range range) {
        float left = 0;
        float top = 0;
        float right = getWidth();
        float bottom = getHeight();

        intersectHorizontal(left, true, range);

        intersectVertical(top, true, mHelperRange);
        range.intersect(mHelperRange);

        intersectHorizontal(right, false, mHelperRange);
        range.intersect(mHelperRange);

        intersectVertical(bottom, false, mHelperRange);
        range.intersect(mHelperRange);
    }

    private void intersectHorizontal(float x, boolean positiveInside, Range range) {
        if (mCenterX > x) {
            if (mCenterX - x > mRadius) {
                if (positiveInside) {
                    range.start = 0;
                    range.end = 2 * Math.PI;
                }
                else {
                    range.start = 0;
                    range.end = 0;
                }
            }
            else {
                float dx = mCenterX - x;
                float cos = dx / mRadius;

                double angle = Math.acos(cos);

                if (positiveInside) {
                    range.start = -Math.PI + angle;
                    range.end = Math.PI - angle;
                }
                else {
                    range.start = Math.PI - angle;
                    range.end = Math.PI + angle;
                }
            }
        }
        else {
            if (x - mCenterX > mRadius) {
                if (positiveInside) {
                    range.start = 0;
                    range.end = 0;
                }
                else {
                    range.start = 0;
                    range.end = 2 * Math.PI;
                }
            }
            else {
                float dx = x - mCenterX;
                float cos = dx / mRadius;

                double angle = Math.acos(cos);

                if (positiveInside) {
                    range.start = -angle;
                    range.end = angle;
                }
                else {
                    range.start = angle;
                    range.end = 2 * Math.PI - angle;
                }
            }
        }
    }

    private void intersectVertical(float y, boolean positiveInside, Range range) {
        if (mCenterY > y) {
            if (mCenterY - y > mRadius) {
                if (positiveInside) {
                    range.start = 0;
                    range.end = 2 * Math.PI;
                }
                else {
                    range.start = 0;
                    range.end = 0;
                }
            }
            else {
                float dy = mCenterY - y;
                float cos = dy / mRadius;

                double angle = Math.acos(cos);

                if (positiveInside) {
                    range.start = - Math.PI / 2 + angle;
                    range.end = 3 * Math.PI / 2 - angle;
                }
                else {
                    range.start = 3 * Math.PI / 2 - angle;
                    range.end = 3 * Math.PI / 2 + angle;
                }
            }
        }
        else {
            if (y - mCenterY > mRadius) {
                if (positiveInside) {
                    range.start = 0;
                    range.end = 0;
                }
                else {
                    range.start = 0;
                    range.end = 2 * Math.PI;
                }
            }
            else {
                float dy = y - mCenterY;
                float cos = dy / mRadius;

                double angle = Math.acos(cos);

                if (positiveInside) {
                    range.start = Math.PI / 2 - angle;
                    range.end = Math.PI / 2 + angle;
                }
                else {
                    range.start = - 3 * Math.PI / 2 + angle;
                    range.end = Math.PI / 2 - angle;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mInnerCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mRadius + mInnerCirclePaint.getStrokeWidth(), mOuterCirclePaint);
    }

    public static class Range {

        public double start;
        public double end;

        public void intersect(Range range) {
            RangeIntersectUtil.intersect(this, range);
        }

        public double range() {
            return Math.max(end - start, 0);
        }

    }

}
