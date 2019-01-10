package com.yashoid.uxapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ExpDateInput extends Input {

    public ExpDateInput(Context context) {
        super(context);
    }

    public ExpDateInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpDateInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected EditText createEditText(Context context) {
        return new ExpEditText(context);
    }

}
