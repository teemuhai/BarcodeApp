package com.example.teemurytsola.cameraapptest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by teemurytsola on 31/07/2017.
 */

public class CameraView extends AppCompatActivity implements View.OnClickListener {

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    SurfaceView cameraView;
    TextView barcodeValue;
    String currentBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_camera);

        //Intent intentThatStartedThisActivity = getIntent();


        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        barcodeValue = (TextView) findViewById(R.id.barcode_value);
        findViewById(R.id.addButton).setOnClickListener(this);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                //.setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() != 0){
                    barcodeValue.post(new Runnable(){
                        @Override
                        public void run() {
                            currentBarcode = barcodes.valueAt(0).displayValue;
                            barcodeValue.setText(currentBarcode);
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }

    @Override
    public void onClick(View view) {
        if(currentBarcode != null){
            boolean exists = false;
            BarcodeItem barcodeItem = new BarcodeItem(currentBarcode);
            for(BarcodeItem item : MainActivity.barcodesList){
                if(item.barcode.equals(barcodeItem.barcode)){
                    item.itemCount++;
                    exists = true;
                    Toast.makeText(this, "Increased count of: " + currentBarcode, Toast.LENGTH_SHORT).show();
                }
            }
            if(exists == false){
                MainActivity.barcodesList.add(barcodeItem);
                Toast.makeText(this, "Added barcode " + currentBarcode, Toast.LENGTH_SHORT).show();
            }
            System.out.println("Here should be list of barcodes" + MainActivity.barcodesList);
            currentBarcode = null;
            barcodeValue.setText(R.string.no_barcode);

        } else {
            Toast.makeText(this, "You have to scan a barcode before adding it to the list", Toast.LENGTH_SHORT).show();
        }
    }
}
