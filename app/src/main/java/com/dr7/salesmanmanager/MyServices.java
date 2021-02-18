package com.dr7.salesmanmanager;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MyServices extends Service {
    private static final String TAG = "BackgroundSoundServiceS";
    MediaPlayer player;
    int i=0;
    Timer T;
    int approveAdmin=-1;
    DatabaseHandler db = new DatabaseHandler(MyServices.this);
    List<Settings> settings=new ArrayList<>();
    SalesmanStations salesmanStations=new SalesmanStations();
    public static double checkOutLong=0,checkOutLat=0;
    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        player = MediaPlayer.create(this, R.raw.jorgesys_song);
//        player.setLooping(true);
//        player.setVolume(100, 100);
//        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onCreate() , service started...");

    }
    public int onStartCommand(Intent intent, int flags, int startId) {


            Timer();

            return START_STICKY;

    }

    void Timer(){
        if(T==null) {
            T = new Timer();
        }

        T.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                Log.e(TAG, "onStartCommand() , service started..."+i++);
                settings = db.getAllSettings();
                if (settings.size() != 0) {
                    approveAdmin= settings.get(0).getApproveAdmin();
                }

                db.getAllSettings();
                Log.e(TAG,"approveAdminnn = "+approveAdmin+"   ");

                Log.e(TAG,"approveAdmin = "+approveAdmin+"   ="+getApplicationContext().toString());

                if(approveAdmin==1) {
                    Log.e(TAG,"approveAdmin IN  = "+approveAdmin);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            Log.e(TAG,"getLoc = "+approveAdmin);
                            getLoc();
                        }
                    });

                }else {
                    Log.e(TAG,"no approveAdmin = "+approveAdmin);

                }
            }
        }, 10, 30000);
    }

    public IBinder onUnBind(Intent arg0) {
        Log.e(TAG, "onUnBind()");
        return null;
    }

    public  void onStop() {

        Log.e(TAG, "onStop()");
    }
    public void onPause() {
        Log.e(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
//        player.stop();
//        player.release();
//        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show();
        Timer();
        Log.e(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, "onLowMemory()");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
      //  onStop();
//        startForegroundService(new Intent(MyService.this,MyService.class));

        Log.e(TAG, "In onTaskRemoved");
    }

    public void getLoc(){

        Log.e(TAG, " first ");
        Log.e(TAG, "getLocinin = " + approveAdmin);

        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) MyServices.this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyServices.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "getLocFalse = " + approveAdmin);

//
//            ActivityCompat.requestPermissions( (Activity) this,
//                    new String[]
//                            {ACCESS_FINE_LOCATION},
//                    1);

//            Intent intent = new Intent(MyServices.this, noThingNotifi.class);
//            startActivity(intent);
            //showWarningAlert(MyService.this);
            Log.e(TAG, "false");
        }

        try {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
                    String userNo= db.getAllUserNo();
                    try {
                        if (TextUtils.isEmpty(userNo)) {

                            userNo="";

                        }
                    }catch (Exception e){
                        userNo="";
                    }
                    salesmanStations.setSalesmanNo(userNo);
                    salesmanStations.setLatitude("" + location.getLatitude());
                    salesmanStations.setLongitude("" + location.getLongitude());
                    checkOutLong=location.getLongitude();
                    checkOutLat=location.getLatitude();

                    settings = db.getAllSettings();
                    if (settings.size() != 0) {
                        approveAdmin= settings.get(0).getApproveAdmin();
                    }

                    db.getAllSettings();
                    Log.e(TAG,"approveAdminnn = "+approveAdmin+"   "+userNo);

                    Log.e(TAG,"approveAdmin = "+approveAdmin+"   ="+getApplicationContext().toString());

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        Log.e(TAG, "  tttt");
                        if(approveAdmin==1) {
                            Log.e(TAG, "  nnn");
                            Log.e(TAG, "  " + salesmanStations.getJSONObject());
                            ImportJason importJason = new ImportJason(MyServices.this);
                            importJason.updateLocation(salesmanStations.getJSONObject());
                        }else {
                            Log.e(TAG, "  no App Import");
                        }
                    }
                });


                 //   Log.e(TAG, "  " + salesmanStations.getJSONObject());
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
            Log.e(TAG, " hhhh ");
        }

    }
}