package com.yashoid.shifter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

public class ServiceView extends AppCompatImageView {

    private static final float TEXT_SIZE_RATIO = 10f / 66f;
    private static final float TEXT_MARGIN = 8f / 66f;
    private static final float PADDING_RATIO = 11f / 66f;

    private Paint mPaint;

    private String mName;

    private float mTextX;
    private float mTextY;

    public ServiceView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ServiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ServiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setScaleType(ScaleType.FIT_CENTER);

        ViewCompat.setElevation(this, getResources().getDisplayMetrics().density * 6);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ServiceView, defStyleAttr, 0);

        mName = a.getString(R.styleable.ServiceView_name);
        int color = a.getColor(R.styleable.ServiceView_color, Color.BLACK);

        if (a.getBoolean(R.styleable.ServiceView_isRed, false)) {
            setBackgroundResource(R.drawable.pinned_service_background);
        }
        else {
            setBackgroundResource(R.drawable.service_background);
        }

        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mPaint.setTypeface(ResourcesCompat.getFont(context, R.font.iransansmobile));
        mPaint.setTextAlign(Paint.Align.RIGHT);

        int padding = (int) (getResources().getDisplayMetrics().density * 11);
        setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float width = getWidth();

        mPaint.setTextSize(TEXT_SIZE_RATIO * width);

        mTextX = - TEXT_MARGIN * width;

        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mTextY = getHeight() / 2 - (fm.descent + fm.ascent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mName, mPaint.getAlpha() * mTextX / 255, mTextY, mPaint);
    }

    public void setVisibleAmount(int visibleAmount) {
        mPaint.setAlpha(visibleAmount);
        invalidate();
    }

}
