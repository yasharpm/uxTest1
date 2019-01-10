package com.yashoid.uxapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabWidget;

@ViewPager.DecorView
public class TabHost extends FrameLayout implements ViewPager.OnAdapterChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private TabWidget mTabWidget;

    private ViewPager mPager;
    private PagerAdapter mAdapter;

    public TabHost(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public TabHost(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public TabHost(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mTabWidget = new TabWidget(context);
        mTabWidget.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTabWidget.setOrientation(LinearLayout.HORIZONTAL);

        addView(mTabWidget);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (getParent() instanceof ViewPager) {
            setupPager((ViewPager) getParent());
        }
    }

    public void setupPager(ViewPager pager) {
        if (mPager != null) {
            mPager.removeOnAdapterChangeListener(this);
            mPager.removeOnPageChangeListener(this);
        }

        mPager = pager;

        mPager.addOnAdapterChangeListener(this);
        mPager.addOnPageChangeListener(this);

        setupAdapter();
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        setupAdapter();
    }

    private void setupAdapter() {
        mAdapter = mPager.getAdapter();

        mTabWidget.removeAllViews();

        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = createTabButton(i);
            mTabWidget.addView(view);
            view.setOnClickListener(this);
        }

        mTabWidget.focusCurrentTab(mPager.getCurrentItem());
    }

    private View createTabButton(int position) {
        TextView textView = new TextView(getContext()) {

            @Override
            public void setSelected(boolean selected) {
                super.setSelected(selected);

                if (selected) {
                    setBackgroundDrawable(new Drawable() {

                        private Paint paint = null;
                        private Rect bounds = new Rect();
                        private int height;
                        private int widthPadding;

                        @Override
                        public void draw(@NonNull Canvas canvas) {
                            if (paint == null) {
                                paint = new Paint();
                                paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                                paint.setStyle(Paint.Style.FILL);

                                height = (int) (getResources().getDisplayMetrics().density * 4);
                                widthPadding = (int) (getResources().getDisplayMetrics().density * 16);
                            }

                            bounds.set(getBounds());
                            bounds.top = bounds.bottom - height;

                            int width = (int) getPaint().measureText(getText().toString()) + 2 * widthPadding;
                            int margin = (bounds.width() - width) / 2;
                            bounds.left += margin;
                            bounds.right -= margin;

                            canvas.drawRect(bounds, paint);
                        }

                        @Override
                        public void setAlpha(int alpha) { }

                        @Override
                        public void setColorFilter(@Nullable ColorFilter colorFilter) { }

                        @Override
                        public int getOpacity() {
                            return PixelFormat.OPAQUE;
                        }
                    });
                }
                else {
                    setBackgroundColor(0);
                }
            }

        };
        textView.setText(mAdapter.getPageTitle(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        textView.setFocusable(true);
        textView.setTag(position);
        return textView;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        mTabWidget.focusCurrentTab(position);
        mPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        mTabWidget.focusCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

}
