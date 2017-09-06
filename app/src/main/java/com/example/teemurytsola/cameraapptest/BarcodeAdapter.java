package com.example.teemurytsola.cameraapptest;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by teemurytsola on 02/08/2017.
 */

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeAdapterViewHolder> {

    ArrayList<BarcodeItem> mBarcodeList;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    ArrayList<BarcodeItem> mPendingRemoval;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<BarcodeItem, Runnable> pendingRunnables = new HashMap<>();

    public BarcodeAdapter(ArrayList<BarcodeItem> data){
        mBarcodeList = data;
        mPendingRemoval = new ArrayList<>();
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
        //String barcode = mBarcodeList.get(position);
        //holder.mBarcodeTextView.setText(barcode);

        BarcodeAdapterViewHolder viewHolder = holder;
        final BarcodeItem item = mBarcodeList.get(position);

        if (mPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.mBarcodeTextView.setVisibility(View.GONE);
            viewHolder.countButton.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    mPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mBarcodeList.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.mBarcodeTextView.setVisibility(View.VISIBLE);
            viewHolder.mBarcodeTextView.setText(item.barcode);
            viewHolder.countButton.setVisibility(View.VISIBLE);
            viewHolder.countButton.setText(Integer.toString(item.itemCount));
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
            viewHolder.countButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle("Count of items");

                    alertDialogBuilder.setMessage("Add or subtract the amount of items").setCancelable(true);
                    final NumberPicker numberPicker = new NumberPicker(view.getContext());
                    numberPicker.setMaxValue(350);
                    numberPicker.setMinValue(1);
                    alertDialogBuilder.setView(numberPicker);
                    numberPicker.setValue(item.itemCount);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //OK button click
                            item.itemCount = numberPicker.getValue();
                            notifyDataSetChanged();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Cancel button click

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {

        if (null == mBarcodeList) return 0;
        return mBarcodeList.size();
    }

    public void pendingRemoval(int position) {
        final BarcodeItem barcodeItem = mBarcodeList.get(position);
        if (!mPendingRemoval.contains(barcodeItem)) {
            mPendingRemoval.add(barcodeItem);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mBarcodeList.indexOf(barcodeItem));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(barcodeItem, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        String item = mBarcodeList.get(position).barcode;
        if (mPendingRemoval.contains(item)) {
            mPendingRemoval.remove(item);
        }
        if (mBarcodeList.get(position).barcode == (item)) {
            Toast.makeText(MainActivity.mainContext, "Removed barcode: " + item, Toast.LENGTH_SHORT).show();
            mBarcodeList.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }


    public class BarcodeAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mBarcodeTextView;
        Button undoButton;
        Button countButton;

        public BarcodeAdapterViewHolder(View itemView) {
            super(itemView);
            mBarcodeTextView = itemView.findViewById(R.id.tv_barcode_data);
            undoButton = itemView.findViewById(R.id.undo_button);
            countButton = itemView.findViewById(R.id.count_button);

        }


    }
    public void setBarcodeData(ArrayList<BarcodeItem> data) {
        mBarcodeList = data;
        notifyDataSetChanged();
    }

}
