package com.yashoid.uxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wonderkiln.camerakit.CameraView;

public class RapidTransactionsFragment extends BaseFragment {

    private CameraView mCamera;
    private RecyclerView mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rapidtransactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCamera = view.findViewById(R.id.camera);
        mList = view.findViewById(R.id.list);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext() , 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mCamera.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        mCamera.stop();
    }

    private RecyclerView.Adapter mAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return viewType == 0 ? RapidTransactionIntroItem.newInstance(parent) : RapidTransactionItem.newInstance(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 8;
        }
    };

}
