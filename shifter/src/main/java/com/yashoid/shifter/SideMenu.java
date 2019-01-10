package com.yashoid.shifter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SideMenu extends ViewGroup implements GigilDrawable.OnGigilMovedListener {

    private int mGigilLeft = 0;

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
        LayoutInflater.from(context).inflate(R.layout.side_menu, this, true);
    }

    @Override
    public void onGigilMoved(int left) {
        mGigilLeft = left;

        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        int childRight = mGigilLeft;

        View view = getChildAt(0);

        view.layout(childRight - getWidth(), 0, childRight, getHeight());
    }

}
