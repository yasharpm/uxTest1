package com.yashoid.uxapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class Toolbar extends ConstraintLayout implements TabChangeReceiver {

    private View mButtonSettings;
    private View mButtonGame;

    public Toolbar(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.toolbar, this, true);

        mButtonSettings = findViewById(R.id.button_settings);
        mButtonGame = findViewById(R.id.button_game);

        setBackgroundColor(0xffffffff);
        ViewCompat.setElevation(this, getResources().getDimension(R.dimen.elevation));

        onTabChanged(TAB_RAPID_TRANSACTION);
    }

    @Override
    public void onTabChanged(int tab) {
        if (tab == TAB_HOME) {
            mButtonSettings.setVisibility(VISIBLE);
            mButtonGame.setVisibility(VISIBLE);
        }
        else {
            mButtonSettings.setVisibility(INVISIBLE);
            mButtonGame.setVisibility(INVISIBLE);
        }
    }

    public void showSettings() {
        mButtonSettings.setVisibility(VISIBLE);
    }

    public void hideSettings() {
        mButtonSettings.setVisibility(INVISIBLE);
    }

    public void showGame() {
        mButtonGame.setVisibility(VISIBLE);
    }

    public void hideGame() {
        mButtonGame.setVisibility(INVISIBLE);
    }

    public void reset() {
        setVisibility(VISIBLE);

        hideSettings();
        hideGame();
    }

}
