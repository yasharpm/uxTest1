package com.yashoid.uxapp;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

public class ExpEditText extends AppCompatEditText {

    private String mFormattedText;

    private boolean changedBySelf = false;
    private boolean mShowPlaceholdersWhenEmpty = true;

    public ExpEditText(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ExpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ExpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setText(getText());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (null == mFormattedText) {
            return;
        }
        int textLength = getText().length();
        if (textLength != mFormattedText.length()) {
            mFormattedText = format(getText().toString());
        }
        int length = mFormattedText.replaceAll("[^0-9۰-۹]", "").length();
        if (length > 2)
            length++;

        if (length <= textLength) {
            setSelection(length);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        if (TextUtils.isEmpty(text) && !hasFocus() && !mShowPlaceholdersWhenEmpty) {
            return;
        }

        if (changedBySelf) {
            changedBySelf = false;
            return;
        }

        changedBySelf = true;

        mFormattedText = format(text.toString());
        invalidate();
        setText(mFormattedText);

        String trimmed = mFormattedText.replaceAll("[^0-9۰-۹]", "").trim();

        if (trimmed.length() > 0) {
            if (!TextUtils.isEmpty(mFormattedText)) {
                int length = mFormattedText.replaceAll("-", "").length();
                if (length < 4 && mFormattedText.contains("/")) {
                    length--;
                }
                setSelection(length);
            }
        }
    }

    public static String format(String input) {
        input = input.replaceAll("[^0-9۰-۹]", "");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= 3; i++) {

            if (i == 2) {
                builder.append("/");
            }

            if (i < input.length()) {
                builder.append(input.charAt(i));
            }
            else {
                builder.append("_");
            }
        }
        return StringUtils.toPersianNumber(builder.toString());
    }


}
