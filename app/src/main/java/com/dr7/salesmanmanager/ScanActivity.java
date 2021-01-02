package com.dr7.salesmanmanager;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.Serial_No;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.item_serial;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialValue;

public class ScanActivity extends AppCompatActivity
    implements ZXingScannerView.ResultHandler{

        private ZXingScannerView mScannerView;
        private  String type="";

        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            type = getIntent().getStringExtra("key");
            Log.e("typeonCreate",""+type);
            mScannerView = new ZXingScannerView(this);
            List<BarcodeFormat> formats = new ArrayList<>();
            formats.add(BarcodeFormat.CODE_128);
//            formats.add(BarcodeFormat.QR_CODE);

            setContentView(mScannerView);// Programmatically initialize the scanner view
//            setContentView(mScannerView);                // Set the scanner view as the content view
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
                item_serial.setText(rawResult.getText());
                Log.e("RESULT",""+rawResult.getText());
            }
            else {
                if(type.equals("3"))
                {
//                    Serial_No.setText(rawResult.getText());
                    serialValue.setText(rawResult.getText());
                }
                else if(type.equals("1"))
                {
                    AddItemsFragment2.barcode.setText(""+rawResult.getText());
                    Log.e("RESULT",""+rawResult.getText());
                }

            }

            onBackPressed();

            // If you would like to resume scanning, call this method below:
            //mScannerView.resumeCameraPreview(this);
        }
}
