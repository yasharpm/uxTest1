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

public class MainActivity extends AppCompatActivity implements Toggle.OnToggleSwitchedListener {

    private GigilView mGigil;
    private SideMenu mSideMenu;
    private Toggle mToggle;
    private ViewGroup mOuterCircle;
    private ViewGroup mInnerCircle;

    private ValueAnimator mAnimator = null;

    private int mCurrentVisibilityAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);

        findViewById(R.id.header).setBackground(new HeaderDrawable());

        mGigil = findViewById(R.id.gigil);
        mSideMenu = findViewById(R.id.side_menu);

        mGigil.setOnGigilMovedListener(mSideMenu);

        mToggle = findViewById(R.id.toggle);
        mOuterCircle = findViewById(R.id.outercircle);
        mInnerCircle = findViewById(R.id.innercircle);

        mToggle.setOnToggleSwitchedListener(this);
        update(mToggle.isSelected() ? 0 : 255);
    }

    @Override
    public void onBackPressed() {
        if (mGigil.isOpenOrOpening()) {
            mGigil.close();
            return;
        }

        return;
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

}
