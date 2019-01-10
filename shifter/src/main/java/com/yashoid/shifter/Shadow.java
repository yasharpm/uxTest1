package com.yashoid.shifter;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

public class Shadow {

    private static final int SHADOW_COLOR = 0x14000000; // 8% black opacity

    public static int getShadowInfo(float elevation, float[] shadowInfo) {
        shadowInfo[0] = 0;
        shadowInfo[1] = elevation / 3;
        shadowInfo[2] = elevation / 2;

        shadowInfo[3] = 0;
        shadowInfo[4] = elevation / 3;
        shadowInfo[5] = elevation / 1;

        return SHADOW_COLOR;
    }

    public static class CircularShadow {

        public static CircularShadow newInstance(float elevation, boolean outward) {
            float[] shadowInfo = new float[6];
            int shadowColor = getShadowInfo(elevation, shadowInfo);

            return new CircularShadow(shadowColor, shadowInfo, outward);
        }

        private Paint mPaint1;
        private Paint mPaint2;

        private boolean mOutward;

        private float[] si;
        private int mStartColor;
        private int mEndColor;

        private int[] mColors = new int[4];
        private float[] mStops = new float[4];

        private float mRadius = -1;

        private float mDrawRadius1;
        private float mDrawRadius2;

        private RectF mHelper = new RectF();

        public CircularShadow(int shadowColor, float[] shadowInfo, boolean outward) {
            mOutward = outward;

            si = shadowInfo;
            mStartColor = shadowColor;
            mEndColor = 0x00ffffff & mStartColor;

            mPaint1 = new Paint();
            mPaint1.setAntiAlias(true);
            mPaint1.setStyle(Paint.Style.FILL);

            mPaint2 = new Paint(mPaint1);
        }

        public void draw(float cx, float cy, float radius, float startAngle, float sweepAngle, Canvas canvas) {
//            if (mRadius == 0) {
//                return;
//            }

            if (radius != mRadius) {
                mRadius = radius;

                updateValues();
            }

            canvas.save();
            canvas.translate(cx, cy);

            canvas.save();
            canvas.translate(si[0], si[1]);
            mHelper.set(-mDrawRadius1, -mDrawRadius1, mDrawRadius1, mDrawRadius1);
            canvas.drawArc(mHelper, startAngle, sweepAngle, true, mPaint1);
            canvas.restore();

            canvas.save();
            canvas.translate(si[3], si[4]);
            mHelper.set(-mDrawRadius2, -mDrawRadius2, mDrawRadius2, mDrawRadius2);
            canvas.drawArc(mHelper, startAngle, sweepAngle, true, mPaint2);
            canvas.restore();

            canvas.restore();
        }

        private void updateValues() {
            if (mOutward) {
                mColors[0] = mEndColor;
                mColors[1] = mEndColor;
                mColors[2] = mStartColor;
                mColors[3] = mEndColor;

                mStops[0] = 0;
                mStops[1] = mRadius / (mRadius + si[2]);
                mStops[2] = mStops[1];
                mStops[3] = 1;

                mDrawRadius1 = mRadius + si[2];
                mPaint1.setShader(new RadialGradient(0, 0, mDrawRadius1, mColors, mStops, Shader.TileMode.CLAMP));

                mStops[1] = mRadius / (mRadius + si[5]);
                mStops[2] = mStops[1];
                mDrawRadius2 = mRadius + si[5];
                mPaint2.setShader(new RadialGradient(0, 0, mDrawRadius2, mColors, mStops, Shader.TileMode.CLAMP));
            }
            else {
                mColors[0] = mEndColor;
                mColors[1] = mEndColor;
                mColors[2] = mStartColor;
                mColors[3] = mEndColor;

                mStops[0] = 0;
                mStops[1] = (mRadius - si[2]) / mRadius;
                mStops[2] = 1;
                mStops[3] = 1;

                mDrawRadius1 = mRadius;
                mPaint1.setShader(new RadialGradient(0, 0, mDrawRadius1, mColors, mStops, Shader.TileMode.CLAMP));

                mStops[1] = (mRadius - si[5]) / mRadius;
                mDrawRadius2 = mRadius;
                mPaint2.setShader(new RadialGradient(0, 0, mDrawRadius2, mColors, mStops, Shader.TileMode.CLAMP));
            }
        }

    }

    public static class LinearShadow {

        public static LinearShadow newInstance(float elevation) {
            float[] shadowInfo = new float[6];
            int shadowColor = getShadowInfo(elevation, shadowInfo);

            return new LinearShadow(shadowColor, shadowInfo);
        }

        private Paint mPaint1;
        private Paint mPaint2;

        private float[] si;

        public LinearShadow(int shadowColor, float[] shadowInfo) {
            int startColor = shadowColor;
            int endColor = 0x00ffffff & startColor;

            si = shadowInfo;

            LinearGradient gradient1 = new LinearGradient(0, 0, si[2], 0, startColor, endColor, Shader.TileMode.CLAMP);
            LinearGradient gradient2 = new LinearGradient(0, 0, si[5], 0, startColor, endColor, Shader.TileMode.CLAMP);

            mPaint1 = new Paint();
            mPaint1.setAntiAlias(true);
            mPaint1.setStyle(Paint.Style.FILL);
            mPaint1.setShader(gradient1);

            mPaint2 = new Paint(mPaint1);
            mPaint2.setShader(gradient2);
        }

        public void draw(float x, float y, float degrees, float length, Canvas canvas) {
            canvas.save();
            canvas.translate(x, y);

            canvas.save();
            canvas.translate(si[0], si[1]);
            canvas.rotate(degrees);
            canvas.drawRect(0, 0, si[2], length, mPaint1);
            canvas.restore();

            canvas.save();
            canvas.translate(si[3], si[4]);
            canvas.rotate(degrees);
            canvas.drawRect(0, 0, si[5], length, mPaint2);
            canvas.restore();

            canvas.restore();
        }

    }

}
