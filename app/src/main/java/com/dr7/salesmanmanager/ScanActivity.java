package com.dr7.salesmanmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.Serial_No;

public class ScanActivity extends AppCompatActivity
    implements ZXingScannerView.ResultHandler{

        private ZXingScannerView mScannerView;
        private  String type="";

        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            type = getIntent().getStringExtra("key");
            Log.e("typeonCreate",""+type);
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        }

        @Override
        public void onResume() {
            super.onResume();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
        }

        @Override
        public void onPause() {
            super.onPause();
            mScannerView.stopCamera();           // Stop camera on pause
        }

        @Override
        public void handleResult(Result rawResult) {
            // Do something with the result here
            // Log.v("tag", rawResult.getText()); // Prints scan results
            // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            if(type.equals("2"))
            {
                Serial_No.setText(rawResult.getText());
                Log.e("RESULT",""+rawResult.getText());
            }
            else {
                AddItemsFragment2.barcode.setText(""+rawResult.getText());
            Log.e("RESULT",""+rawResult.getText());
            }
//            AddItemsFragment2.barcode.setText(""+rawResult.getText());
//            Log.e("RESULT",""+rawResult.getText());
//           AddItemsFragment2.searchByBarcodeNo(rawResult.getText());

//            MainActivity.tvresult.setText(rawResult.getText());
            onBackPressed();

            // If you would like to resume scanning, call this method below:
            //mScannerView.resumeCameraPreview(this);
        }
}
