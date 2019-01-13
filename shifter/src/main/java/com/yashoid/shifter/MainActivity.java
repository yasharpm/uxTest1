package com.yashoid.shifter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements Toggle.OnToggleSwitchedListener {

    private GigilView mGigil;
    private GigilView mGigilBack;
    private SideMenu mSideMain;
    private SideMenu mSideMenu;
    private Toggle mToggle;
    private ViewGroup mOuterCircle;
    private ViewGroup mInnerCircle;

    private boolean mGigilOpenFlag = false;
    private boolean mGigilBackOpenFlag = false;

    private ValueAnimator mAnimator = null;

    private int mCurrentVisibilityAmount;

    private boolean mBacking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        setContentView(R.layout.activity_main);



        mSideMain = findViewById(R.id.side_main);

        ViewPager pager = mSideMain.findViewById(R.id.pager_header);
        pager.setAdapter(new BannerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(pager.getAdapter().getCount() - 1, false);

        mGigil = findViewById(R.id.gigil);
        mGigilBack = findViewById(R.id.gigil_back);
        mSideMenu = findViewById(R.id.side_menu);

        mGigil.setOnGigilMovedListener(new GigilDrawable.OnGigilMovedListener() {
            @Override
            public void onGigilMoved(int position) {
                mSideMenu.onGigilMoved(position);

                if (mGigil.isOpen()) {
                    if (!mGigilOpenFlag) {
                        mGigilOpenFlag = true;

                        mGigilBack.closeImmediately();

                        switchOrder(mSideMain, mSideMenu);
                        switchOrder(mGigilBack, mGigil);
                    }
                }
                else {
                    mGigilOpenFlag = false;
                }
            }
        });

        mGigilBack.setOnGigilMovedListener(new GigilDrawable.OnGigilMovedListener() {
            @Override
            public void onGigilMoved(int position) {
                if (mBacking) {
                    return;
                }

                mSideMain.onGigilMoved(position);

                if (mGigilBack.isOpen()) {
                    if (!mGigilBackOpenFlag) {
                        mGigilBackOpenFlag = true;

                        mGigil.closeImmediately();

                        switchOrder(mSideMenu, mSideMain);
                        switchOrder(mGigil, mGigilBack);
                    }
                }
                else {
                    mGigilBackOpenFlag = false;
                }
            }
        });

        mGigilBack.openImmediately();

        mToggle = findViewById(R.id.toggle);
        mOuterCircle = findViewById(R.id.outercircle);
        mInnerCircle = findViewById(R.id.innercircle);

        mToggle.setOnToggleSwitchedListener(this);
        update(mToggle.isSelected() ? 0 : 255);
    }

    @Override
    public void onBackPressed() {
        if (mGigil.isOpen()) {
            mBacking = true;

            mGigilBack.openImmediately();
            mGigil.close();

            switchOrder(mSideMenu, mSideMain);
            switchOrder(mGigil, mGigilBack);

            mBacking = false;
            return;
        }
        else if (mGigil.isOpenOrOpening()) {
            mGigil.close();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onToggleSwitched(boolean isSelected) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = new ValueAnimator();
        mAnimator.setDuration(Toggle.ANIMATION_DURATION);
        mAnimator.setIntValues(mCurrentVisibilityAmount, isSelected ? 0 : 255);
        mAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                update((Integer) animation.getAnimatedValue());
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }

        });

        mAnimator.start();
    }

    private void update(int amount) {
        update(mOuterCircle, amount);
        update(mInnerCircle, amount);
    }

    private void update(ViewGroup group, int amount) {
        final int count = group.getChildCount();

        for (int i = 0; i < count; i++) {
            update(group.getChildAt(i), amount);
        }
    }

    private void update(View view, int amount) {
        ((ServiceView) view).setVisibleAmount(amount);

        mCurrentVisibilityAmount = amount;
    }

    private void switchOrder(View a, View b) {
        ViewGroup parent = (ViewGroup) a.getParent();

        int aIndex = findIndex(parent, a);
        int bIndex = findIndex(parent, b);

        if (aIndex > bIndex) {
            return;
        }

        parent.removeView(a);
        parent.removeView(b);

        parent.addView(b, aIndex);
        parent.addView(a, bIndex);
    }

    private int findIndex(ViewGroup parent, View view) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) == view) {
                return i;
            }
        }

        return -1;
    }

    private static class BannerAdapter extends FragmentPagerAdapter {

        public BannerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ImageFragment.newInstance(R.drawable.banner1);
                case 1:
                    return ImageFragment.newInstance(R.drawable.banner2);
                case 2:
                    return ImageFragment.newInstance(R.drawable.banner3);
                case 3:
                    return ImageFragment.newInstance(R.drawable.banner4);
            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}
