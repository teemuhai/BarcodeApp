package com.example.teemurytsola.cameraapptest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<String> barcodesList = new ArrayList<>();
    private BarcodeAdapter mBarcodeAdapter;
    private RecyclerView barcodeListView;
    public static final int MY_PERMISSIONS_CAMERA = 9915;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA);
        }

        barcodeListView = (RecyclerView) findViewById(R.id.barcode_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        barcodeListView.setLayoutManager(layoutManager);


        mBarcodeAdapter = new BarcodeAdapter(barcodesList);
        barcodeListView.setAdapter(mBarcodeAdapter);

        findViewById(R.id.scan_button).setOnClickListener(this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "This app needs to use camera to read barcodes", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.scan_button){
            Intent intent = new Intent(this, CameraView.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("On Destroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("On resume");
        mBarcodeAdapter.setBarcodeData(barcodesList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("On pause");
    }
}
