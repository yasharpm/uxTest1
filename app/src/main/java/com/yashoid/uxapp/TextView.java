package com.yashoid.uxapp;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextView extends AppCompatTextView {

    public TextView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setTypeface(ResourcesCompat.getFont(context, R.font.iran_sans_regular));
    }

    private boolean mIsChanging = false;

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (!mIsChanging) {
            mIsChanging = true;

            setText(StringUtils.toPersianNumber(text.toString()));

            mIsChanging = false;
        }
    }

}
