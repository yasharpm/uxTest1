package com.yashoid.uxapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

public class Input extends FrameLayout implements View.OnClickListener {

    private TextView mTextTitle;
    private EditText mEditValue;

    private CharSequence[] mOptions;
    private int mTypeIndex;

    public Input(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public Input(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public Input(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.input, null);
        d.setColorFilter(0xffe6e6e6, PorterDuff.Mode.MULTIPLY);
        setBackground(d);
//        setBackgroundResource(R.drawable.input);

        mTextTitle = new TextView(context);
        mTextTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mTextTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTextTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        addView(mTextTitle);
        ((LayoutParams) mTextTitle.getLayoutParams()).gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        mEditValue = createEditText(context);
        mEditValue.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mEditValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mEditValue.setMaxLines(1);
        mEditValue.setSingleLine();
        mEditValue.setGravity(Gravity.LEFT);
        mEditValue.setBackground(null);
        mEditValue.setTypeface(ResourcesCompat.getFont(context, R.font.iran_sans_regular));
        mEditValue.setEnabled(false);
        mEditValue.setInputType(InputType.TYPE_CLASS_TEXT);
        addView(mEditValue);
        ((LayoutParams) mEditValue.getLayoutParams()).width = LayoutParams.MATCH_PARENT;
        ((LayoutParams) mEditValue.getLayoutParams()).gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

        setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Input, defStyleAttr, 0);

        setTitle(a.getString(R.styleable.Input_android_title));
        mEditValue.setInputType(a.getInt(R.styleable.Input_android_inputType, InputType.TYPE_NULL));
        setOptions(a.getTextArray(R.styleable.Input_options));
        setTypeIndex(a.getInteger(R.styleable.Input_typeIndex, -1));

        a.recycle();

        mEditValue.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
                if (actionId == 5) {
                    stopEditing();

                    findInputBoard().moveToNext(Input.this);

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mEditValue.isEnabled() ? super.onInterceptTouchEvent(ev) : true;
    }

    protected EditText createEditText(Context context) {
        return new EditText(context);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setOptions(CharSequence... options) {
        mOptions = options;
    }

    public CharSequence[] getOptions() {
        return mOptions;
    }

    public void setTypeIndex(int index) {
        mTypeIndex = index;
    }

    public int getTypeIndex() {
        return mTypeIndex;
    }

    public void startEditing() {
        mEditValue.setEnabled(true);

        mEditValue.requestFocusFromTouch();

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditValue, InputMethodManager.SHOW_IMPLICIT);

        mEditValue.setSelection(0, mEditValue.getText().length());
    }

    public void stopEditing() {
        mEditValue.setEnabled(false);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(mEditValue.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void setValueIndex(int index) {
        setTag(index);
        setValue(StringUtils.toPersianNumber(mOptions[index].toString()));
    }

    public void setValue(String value) {
        mEditValue.setText(value);
    }

    public String getValue() {
        return mEditValue.getText().toString();
    }

    private InputBoard findInputBoard() {
        ViewGroup parent = (ViewGroup) getParent();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof InputBoard) {
                return (InputBoard) child;
            }
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        if (mOptions.length == 1 && mTypeIndex == 0) {
            startEditing();
        }
        else {
            findInputBoard().show(this);
        }
    }

    public EditText getInnerEditText() {
        return mEditValue;
    }

}
