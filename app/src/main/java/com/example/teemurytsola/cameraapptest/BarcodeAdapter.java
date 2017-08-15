package com.example.teemurytsola.cameraapptest;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by teemurytsola on 02/08/2017.
 */

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeAdapterViewHolder> {

    ArrayList<String> mBarcodeList;

    public BarcodeAdapter(ArrayList<String> data){
        mBarcodeList = data;

    }

    @Override
    public BarcodeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.barcode_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BarcodeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarcodeAdapterViewHolder holder, int position) {
        String barcode = mBarcodeList.get(position);
        holder.mBarcodeTextView.setText(barcode);
    }


    @Override
    public int getItemCount() {

        if (null == mBarcodeList) return 0;
        return mBarcodeList.size();
    }
    public class BarcodeAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mBarcodeTextView;

        public BarcodeAdapterViewHolder(View itemView) {
            super(itemView);
            mBarcodeTextView = itemView.findViewById(R.id.tv_barcode_data);
        }


    }
    public void setBarcodeData(ArrayList<String> data) {
        mBarcodeList = data;
        notifyDataSetChanged();
    }

}
