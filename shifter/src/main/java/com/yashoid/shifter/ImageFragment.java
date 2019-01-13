package com.yashoid.shifter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {

    public static ImageFragment newInstance(int imageResId) {
        ImageFragment fragment = new ImageFragment();

        Bundle args = new Bundle();
        args.putInt("image", imageResId);

        fragment.setArguments(args);

        return fragment;
    }

    private ImageView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new ImageView(inflater.getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImage = (ImageView) view;

        mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Bundle args = getArguments();

        int imageResId = args.getInt("image");
        mImage.setImageResource(imageResId);
    }

}
