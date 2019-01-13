package com.yashoid.shifter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class ServiceItem extends ViewGroup {

    private static final float TEXT_SIZE_RATIO = 12f / 48f;

    private ImageView mImageIcon;
    private TextView mTextName;

    public ServiceItem(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ServiceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ServiceItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.item_service, this, true);

        setClipChildren(false);
        setClipToPadding(false);

        mImageIcon = findViewById(R.id.image_icon);
        mTextName = findViewById(R.id.text_name);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ServiceItem, defStyleAttr, 0);

        int iconResId = a.getResourceId(R.styleable.ServiceItem_icon, R.drawable.item_services);
        String name = a.getString(R.styleable.ServiceItem_name);

        a.recycle();

        if (name == null) {
            name = "خدمات";
        }

        mImageIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconResId, null));
        mTextName.setText(name);

        int padding = (int) (getResources().getDisplayMetrics().density * 11);
        mImageIcon.setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float width = getWidth();
        float textSize = TEXT_SIZE_RATIO * width;

        if (mTextName.getTextSize() != textSize) {
            mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        int imageMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);

        mImageIcon.measure(imageMeasureSpec, imageMeasureSpec);
        mImageIcon.layout(0, 0, getWidth(), getWidth());

        int unspecifiedSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mTextName.measure(unspecifiedSpec, unspecifiedSpec);

        int nameWidth = mTextName.getMeasuredWidth();
        int nameHeight = mTextName.getMeasuredHeight();

        int nameLeft = (getWidth() - nameWidth) / 2;

        mTextName.layout(nameLeft, getHeight() - nameHeight, nameLeft + nameWidth, getHeight());
    }

}
