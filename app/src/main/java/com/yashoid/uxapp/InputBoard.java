package com.yashoid.uxapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InputBoard extends ListView implements AdapterView.OnItemClickListener {

    private int mHeight;

    private Input mInput;

    private Input mLastEditingInput = null;

    public InputBoard(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public InputBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public InputBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mHeight = getResources().getDimensionPixelSize(R.dimen.keyboard_height);

        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        setOnItemClickListener(this);
        setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void show(Input input) {

        if (mLastEditingInput != null) {
            mLastEditingInput.stopEditing();
            mLastEditingInput = null;
        }
        else if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);

            startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.inputboard_in));
        }

        mInput = input;
        setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_option, input.getOptions()));
    }

    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hide();

        if (position == mInput.getTypeIndex()) {
            mInput.startEditing();

            mLastEditingInput = mInput;
        }
        else {
            mInput.setValue(StringUtils.toPersianNumber(getItemAtPosition(position).toString()));
            mInput.setTag(position);

            moveToNext(mInput);
        }
    }

    public void moveToNext(Input input) {
        if (input == null) {
            return;
        }

        ViewGroup parent = (ViewGroup) getParent();

        int index;

        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == input) {
                if (index + 1 < parent.getChildCount()) {
                    View nextChild = parent.getChildAt(index + 1);

                    if (nextChild instanceof Input) {
                        ((Input) nextChild).onClick(nextChild);
                    }
                }
                return;
            }
        }
    }

}
