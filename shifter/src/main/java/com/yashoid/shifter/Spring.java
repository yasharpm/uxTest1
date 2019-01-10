package com.yashoid.shifter;

public class Spring {

    private float mMinimumSize;
    private float mMaximumSize;

    private float mMaximumSpeed;

    private float mDefaultSize;

    private float mSize;

    private long mTimeTracker = -1;

    public Spring() {

    }

    public void setMinimumSize(float minimumSize) {
        mMinimumSize = minimumSize;

        adjustSize();
    }

    public void setMaximumSize(float maximumSize) {
        mMaximumSize = maximumSize;

        adjustSize();
    }

    public void setDefaultSize(float defaultSize) {
        mDefaultSize = defaultSize;
    }

    public void setMaximumSpeed(float maximumSpeed) {
        mMaximumSpeed = maximumSpeed;
    }

    public void setSize(float size) {
        mSize = size;

        adjustSize();
    }

    /**
     *
     * @return Change in size.
     */
    public float step() {
        if (mTimeTracker == -1) {
            mTimeTracker = System.nanoTime();

            return 0;
        }

        float speed;

        if (mSize == mDefaultSize) {
            speed = 0;
        }
        else if (mSize > mDefaultSize) {
            speed = - mMaximumSpeed * (mSize - mDefaultSize) / (mMaximumSize - mDefaultSize);
        }
        else {
            speed = mMaximumSpeed * (mDefaultSize - mSize) / (mDefaultSize - mMinimumSize);
        }

        long now = System.nanoTime();
        long timeChange = now - mTimeTracker;

        if (timeChange > 17_000_000) {
            timeChange = 17_000_000;
        }

        mTimeTracker = now;

        float sizeChange = timeChange / 1_000_000_000f * speed;

        if (mSize + sizeChange > mMaximumSize) {
            sizeChange = mMaximumSize - mSize;
        }
        else if (mSize + sizeChange < mMinimumSize) {
            sizeChange = mMinimumSize - mSize;
        }

        if (Math.abs(mSize + sizeChange - mDefaultSize) < 0.5f) {
            sizeChange = mDefaultSize - mSize;
        }

        mSize += sizeChange;

        return sizeChange;
    }

    private void adjustSize() {
        if (mSize < mMinimumSize) {
            mSize = mMinimumSize;
        }
        else if (mSize > mMaximumSize) {
            mSize = mMaximumSize;
        }
    }

    public float getSize() {
        return mSize;
    }

}
