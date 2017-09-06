package com.example.teemurytsola.cameraapptest;

/**
 * Created by teemurytsola on 24/08/2017.
 */

public class BarcodeItem {

    String barcode;
    public final int id;
    int itemCount;
    private static int counter = 0;

    public BarcodeItem(String barcode){
        this.barcode = barcode;
        this.itemCount = 1;
        this.id = counter++;
    }
}
