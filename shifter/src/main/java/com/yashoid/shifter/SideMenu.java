package com.yashoid.shifter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SideMenu extends ViewGroup implements GigilDrawable.OnGigilMovedListener {

    private boolean mRtl;

    private int mGigilPosition = 0;

    public SideMenu(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public SideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public SideMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SideMenu, defStyleAttr, 0);

        mRtl = a.getBoolean(R.styleable.SideMenu_rtl, false);
        int layoutResId = a.getResourceId(R.styleable.SideMenu_layout, 0);

        a.recycle();

        LayoutInflater.from(context).inflate(layoutResId, this, true);
    }

    @Override
    public void onGigilMoved(int position) {
        mGigilPosition = position;

        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        View view = getChildAt(0);

        view.measure(
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY)
        );

        if (mRtl) {
            int childLeft = mGigilPosition;

            view.layout(childLeft, 0, childLeft + getWidth(), getHeight());
        }
        else {
            int childRight = mGigilPosition;

            view.layout(childRight - getWidth(), 0, childRight, getHeight());
        }
    }

}
