package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

// settings = db.getAllSettings();
//                    if (settings.size() != 0) {
//                        approveAdmin= settings.get(0).getApproveAdmin();
//                    }
//
//                    db.getAllSettings();
//                Log.e("StickyLocation","approveAdminnn = "+approveAdmin+"   ="+contextG);
//
//                Log.e("StickyLocation","approveAdmin = "+approveAdmin+"   ="+getApplicationContext().toString());
//
//                    if(approveAdmin==1) {
//                        Log.e("StickyLocation","approveAdmin IN  = "+approveAdmin);
//
////                        LocationPermissionRequest locationPermissionRequest = new LocationPermissionRequest(MyService.this);
////                        locationPermissionRequest.checkLocationPermission();
//                        Handler h = new Handler(Looper.getMainLooper());
//                        h.post(new Runnable() {
//                            public void run() {
//                                Log.e("StickyLocation","getLoc = "+approveAdmin);
//                                getLoc();
//                            }
//                        });
//
//                    }else {
//                        Log.e("StickyLocation","no approveAdmin = "+approveAdmin);
//
//                    }


/**
 * Created by Rawan on 2019.
 */

public class MyService extends Service  {
    //creating a mediaplayer object
    DatabaseHandler db = new DatabaseHandler(MyService.this);
    Timer T;
    public static Context contextG;
//    List<OrderHeader> OrderHeaderObj;
//    List<OrderTransactions> OrderTransactionsObj;
//    List<PayMethod> PayMethodObj;
List<Settings>settings;
  int approveAdmin=-1;
    SalesmanStations salesmanStations;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        OrderHeaderObj = new ArrayList<>();
//        OrderTransactionsObj = new ArrayList<>();
//        PayMethodObj = new ArrayList<>();
        settings=new ArrayList<>();
        T = new Timer();
        salesmanStations=new SalesmanStations();


        T.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
//                try {
//                    OrderHeaderObj.clear();
//                    OrderTransactionsObj.clear();
//                    PayMethodObj.clear();
//
//                    OrderHeaderObj = db.getAllOrderHeader();
//                    OrderTransactionsObj = db.getAllOrderTransactions();
//                    PayMethodObj = db.getAllExistingPay();
//
//                    for (int i = 0; i < OrderHeaderObj.size(); i++) {
//                        if (OrderHeaderObj.get(i).getIsPost() != 1) {
//                            sendToServer(OrderHeaderObj.get(i), OrderTransactionsObj, PayMethodObj);
//                        }
//                    }

                    settings = db.getAllSettings();
                    if (settings.size() != 0) {
                        approveAdmin= settings.get(0).getApproveAdmin();
                    }

                    db.getAllSettings();
                Log.e("StickyLocation","approveAdminnn = "+approveAdmin+"   ="+contextG);

                Log.e("StickyLocation","approveAdmin = "+approveAdmin+"   ="+getApplicationContext().toString());

                    if(approveAdmin==1) {
                        Log.e("StickyLocation","approveAdmin IN  = "+approveAdmin);

//                        LocationPermissionRequest locationPermissionRequest = new LocationPermissionRequest(MyService.this);
//                        locationPermissionRequest.checkLocationPermission();
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            public void run() {
                                Log.e("StickyLocation","getLoc = "+approveAdmin);
                                getLoc();
                            }
                        });

                    }else {
                        Log.e("StickyLocation","no approveAdmin = "+approveAdmin);

                    }
//                } catch (Exception e) {
//                    Log.e("error in sticky", "no data in order header ...");
//                }
//                message();

            }
        }, 10000, 3000);

        //START_REDELIVER_INTENT

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed

    }


//    void sendToServer(OrderHeader OrderHeaderObj, List<OrderTransactions> OrderTransactionsObj, List<PayMethod> PayMethodObj) {
//        try {
////            JSONArray obj2 = new JSONArray();
////            for (int i = 0; i < OrderTransactionsObj.size(); i++) {
////                if (OrderHeaderObj.getVoucherNumber().equals(OrderTransactionsObj.get(i).getVoucherNo()))
////                    obj2.put(OrderTransactionsObj.get(i).getJSONObject2());
////            }
////            JSONObject obj1 = OrderHeaderObj.getJSONObject2();
////
////            JSONArray obj3 = new JSONArray();
////            for (int i = 0; i < PayMethodObj.size(); i++) {
////                if (OrderHeaderObj.getVoucherNumber().equals(PayMethodObj.get(i).getVoucherNumber()))
////                    obj3.put(PayMethodObj.get(i).getJSONObject2());
////            }
////            JSONObject obj = new JSONObject();
////            obj.put("ORDERHEADER", obj1);
////            obj.put("ORDERTRANSACTIONS", obj2);
////            obj.put("PAYMETHOD", obj3);
////
////            Log.e("log trans =", obj2.toString());
////
////            Log.e("save successful =", obj.toString());
////
////            SendCloud sendCloud = new SendCloud(com.tamimi.sundos.restpos.MyService.this, obj);
////            sendCloud.startSending("Order");
//
//        } catch (JSONException e) {
//            Log.e("Tag", "JSONException");
//        }
//    }

    public void message() {
        Toast.makeText(MyService.this, "is send succ", Toast.LENGTH_SHORT).show();

    }

    public void getLoc(){

            Log.e("STICK_LOCATION", " first ");
            Log.e("StickyLocation", "getLocinin = " + approveAdmin);

            LocationManager locationManager;
            LocationListener locationListener;

            locationManager = (LocationManager) MyService.this.getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(MyService.this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("StickyLocation", "getLocFalse = " + approveAdmin);

//
//            ActivityCompat.requestPermissions( (Activity) this,
//                    new String[]
//                            {ACCESS_FINE_LOCATION},
//                    1);

               // Intent intent = new Intent(MyService.this, noThingNotifi.class);
                //startActivity(intent);
                //showWarningAlert(MyService.this);
                Log.e("requestPermissions", "false");
            }

            try {
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
                        salesmanStations.setSalesmanNo("1");
                        salesmanStations.setLatitude("" + location.getLatitude());
                        salesmanStations.setLongitude("" + location.getLongitude());


//                Handler h = new Handler(Looper.getMainLooper());
//                h.post(new Runnable() {
//                    public void run() {
                        Log.e("STICK_LOCATION2", "  nnn");
                        Log.e("STICK_LOCATION1", "  " + salesmanStations.getJSONObject());
                        ImportJason importJason = new ImportJason(MyService.this);
                        importJason.updateLocation(salesmanStations.getJSONObject());
//                    }
//                });
//                checkOutLong=longitude;
//                checkOutLat=latitude;

                        Log.e("STICK_LOCATION", "  " + salesmanStations.getJSONObject());
//                Log.e("location12345","    la= "+latitude +"  lo = "+longitude);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };


                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } catch (Exception e) {
                Log.e("STICK_LOCATION", " hhhh ");
            }

    }

    private void showWarningAlert(Context context) { //Added argument
        AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Use context
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("You are currently in a battle");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}