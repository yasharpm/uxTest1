package com.yashoid.shifter.tabbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yashoid.shifter.R;
import com.yashoid.shifter.TextStyle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

public class TabbarView extends ViewGroup {

    public interface OnTabSelectedListener {

        void onTabSelected(TabbarView view, int selectedTab);

    }

    private ImageView mImageBackground;
    private ImageView mImageMid;

    private TextView[] mTabTitles;
    private ImageView[] mTabIcons;

    private float mDepth;

    private int mMidRadius;
    private int mMidYOffset;

    private TextStyle mTextStyle;

    private int mIconSize;
    private int mIconColor;
    private int mSelectedTabColor;
    private int mMiddleIconColor;

    private Tab[] mTabs;

    private int mSelectedTabIndex = -1;

    private OnTabSelectedListener mOnTabSelectedListener = null;

    private GestureDetector mGestureDetector;

    private static int color() {
        return 0xffffffff;
    }

    private static float radius() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        return 27.5f / 360 * widthPixels;
    }

    private static float depth() {
        return radius() * 1.2f;
    }

    private static int midRadius() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (23f / 360 * widthPixels);
    }

    private static int midYOffset() {
        return (int) (midRadius() * 0.25f);
    }

    private static int midColor() {
        return 0xffe8165d;
    }

    private static float elevation() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        return 3f / 360 * widthPixels;
    }

    private static Tab[] tabs() {
        Tab[] tabs = new Tab[5];

        tabs[0] = new Tab("0", "کلوب", R.drawable.tab_club, "");
        tabs[1] = new Tab("1", "پرداخت", R.drawable.tab_payment, "");
        tabs[2] = new Tab("2", "تراکنش سریع", R.drawable.tab_repeat, "");
        tabs[3] = new Tab("3", "جایزه و کمپین", R.drawable.tab_campaign, "");
        tabs[4] = new Tab("4", "خانه", R.drawable.tab_home, "");

        return tabs;
    }

    private static TextStyle textStyle() {
        return TextStyle.parseTextStyle("10;#011e2e;iransansmobile");
    }

    private static int iconSize() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        return 20 * widthPixels / 360;
    }

    private static int iconColor() {
        return 0xff011e2e;
    }

    private static int middleIconColor() {
        return 0xffffffff;
    }

    private static int selectedTabColor() {
        return midColor();
    }

    public TabbarView(Context context) {
        this(context, color(), radius(), depth(),
                midRadius(), midYOffset(), midColor(), elevation(),
                tabs(), textStyle(),
                iconSize(), iconColor(), middleIconColor(),
                selectedTabColor());

        setSelectedTab(4);
    }

    public TabbarView(Context context, AttributeSet attrs) {
        this(context);
    }

    public TabbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }

    public TabbarView(Context context, int color, float radius, float depth,
                      int midRadius, int midYOffset, int midColor, float elevation,
                      Tab[] tabs, TextStyle textStyle,
                      int iconSize, final int iconColor, int middleIconColor,
                      int selectedTabColor) {
        super(context);

        setClipChildren(false);
        setClipToPadding(false);

        mGestureDetector = new GestureDetector(context, mOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);

        mDepth = depth;

        mMidRadius = midRadius;
        mMidYOffset = midYOffset;

        mTextStyle = textStyle;

        mIconSize = iconSize;
        mIconColor = iconColor;
        mSelectedTabColor = selectedTabColor;
        mMiddleIconColor = middleIconColor;

        mImageMid = new ImageView(context);
        ShapeDrawable midBackground = new ShapeDrawable();
        midBackground.setShape(new OvalShape());
        midBackground.setColorFilter(midColor, PorterDuff.Mode.SRC_IN);
        mImageMid.setBackground(midBackground);
//        mImageMid.setScaleType(ImageView.ScaleType.CENTER);
//        mImageMid.setPadding(4,4,4,4);
        ViewCompat.setElevation(mImageMid, elevation);
        addView(mImageMid);

        mImageBackground = new ImageView(context);
        mImageBackground.setImageDrawable(new TabbarDrawable(color, radius, depth, elevation));
        addView(mImageBackground);

        mTabs = tabs;

        mTabTitles = new TextView[5];
        mTabIcons = new ImageView[5];

        for (int i = 0; i < 5; i++) {
            Tab tab = mTabs[i];

            mTabTitles[i] = createTabTextView(context, mTextStyle);
            mTabTitles[i].setText(tab.getTitle());
            addView(mTabTitles[i]);

            mTabIcons[i] = createTabImageView(context);
            mTabIcons[i].setImageResource(tab.getIconResId());

            addView(mTabIcons[i]);
        }

        ViewCompat.setElevation(mTabIcons[2], elevation);
    }

    private TextView createTabTextView(Context context, TextStyle textStyle) {
        TextView textView = new TextView(context);

        if (textStyle != null) {
            textStyle.apply(textView);
        }

        textView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        return textView;
    }

    private ImageView createTabImageView(Context context) {
        AppCompatImageView imageView = new AppCompatImageView(context);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }

    public Tab[] getTabs() {
        return mTabs;
    }

    public void setSelectedTab(int index) {
        if (mSelectedTabIndex != -1) {
            mTabTitles[mSelectedTabIndex].setTextColor(mTextStyle.getTextColor());

            Drawable d = mTabIcons[mSelectedTabIndex].getDrawable();

            if (d != null) {
                d.setColorFilter(mSelectedTabIndex == 2 ? mMiddleIconColor : mIconColor, PorterDuff.Mode.SRC_IN);
            }
        }

        mSelectedTabIndex = index;

        if (mSelectedTabIndex >= 0) {
            mTabTitles[mSelectedTabIndex].setTextColor(mSelectedTabColor);

            Drawable d = mTabIcons[mSelectedTabIndex].getDrawable();

            if (d != null) {
                d.setColorFilter(mSelectedTabIndex == 2 ? mIconColor : mSelectedTabColor, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public int getSelectedTab() {
        return mSelectedTabIndex;
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) { }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float touchX = e.getX();

            int selectedTabIndex = -1;

            for (int i = 1; i <= 5; i++) {
                if (touchX < i * getWidth() / 5) {
                    selectedTabIndex = i - 1;
                    break;
                }
            }

            if (selectedTabIndex >= 0) {
                setSelectedTab(selectedTabIndex);

                if (mOnTabSelectedListener != null) {
                    mOnTabSelectedListener.onTabSelected(TabbarView.this, mSelectedTabIndex);
                }
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) { }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    };

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), mSelectedTabIndex);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;

        super.onRestoreInstanceState(savedState.getSuperState());

        setSelectedTab(savedState.selectedTabIndex);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();

        mImageMid.layout(width / 2 - mMidRadius, 0, width / 2 + mMidRadius, 2 * mMidRadius);

        int backgroundHeight = height - mMidRadius + mMidYOffset;
        mImageBackground.layout(0, height - backgroundHeight, width, height);

        int tabWidth = getWidth() / mTabs.length;

        int titlesHeight = (int) (backgroundHeight - mDepth);

        int iconPossibleTop = height - backgroundHeight;
        int iconTop = (int) (iconPossibleTop + (mDepth - mIconSize) / 2);
        int iconBottom = iconTop + mIconSize;

        for (int i = 0; i < mTabs.length; i++) {
            mTabTitles[i].measure(MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int titleRequiredHeight = mTabTitles[i].getMeasuredHeight();

            int titleHeight = Math.max(titleRequiredHeight, titlesHeight);

            if (titleHeight > titleRequiredHeight) {
                mTabTitles[i].measure(MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(titleHeight, MeasureSpec.EXACTLY));
            }

            mTabTitles[i].layout(tabWidth * i, height - titleHeight, tabWidth * (i + 1), height);

            mTabIcons[i].layout(tabWidth * i, iconTop, tabWidth * (i + 1), iconBottom);
        }

        mTabIcons[2].layout((int) (width / 2 - mMidRadius), 0, (int) (width / 2 + mMidRadius), 2 * mMidRadius);
    }

    public static class SavedState extends BaseSavedState {

        private int selectedTabIndex;

        private SavedState(Parcel source) {
            super(source);

            selectedTabIndex = source.readInt();
        }

        public SavedState(Parcelable superState, int selectedTabIndex) {
            super(superState);

            this.selectedTabIndex = selectedTabIndex;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(selectedTabIndex);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

    }

}
