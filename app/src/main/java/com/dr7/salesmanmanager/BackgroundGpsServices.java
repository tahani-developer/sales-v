package com.dr7.salesmanmanager;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.dr7.salesmanmanager.Modles.SalesmanStations;

public  class BackgroundGpsServices extends Service implements LocationListener {
    private LocationManager mLocationManager;
    public final long UPDATE_INTERVAL = 500;  /* 0.5 sec */
    public static final int NOTIFICATION_ID = 200;
    boolean isLocationUpdateRunning=false;
    SalesmanStations salesmanStations;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        //sendNotification(this, false);
        salesmanStations=new SalesmanStations();
        startLocationUpdates();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startLocationUpdates() {
        if (!isLocationUpdateRunning) {
            isLocationUpdateRunning = true;
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (mLocationManager != null) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //sendNotification(BackgroundGpsServices.this, true);
        salesmanStations.setSalesmanNo("102020");
        salesmanStations.setLatitude("" + location.getLatitude());
        salesmanStations.setLongitude("" + location.getLongitude());


//                Handler h = new Handler(Looper.getMainLooper());
//                h.post(new Runnable() {
//                    public void run() {
        Log.e("STICK_LOCATION2", "  nnn");
        Log.e("STICK_LOCATION1", "  " + salesmanStations.getJSONObject());
        ImportJason importJason = new ImportJason(this);
        importJason.updateLocation(salesmanStations.getJSONObject());
//                    }
//                });
//                checkOutLong=longitude;
//                checkOutLat=latitude;

        Log.e("STICK_LOCATION", "  " + salesmanStations.getJSONObject());
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

//    public static void sendNotification(Service service, boolean isUpdate) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Intent intent = new Intent(service, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_NO_CREATE);
//            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(service)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("INFO_NOTIFICATION_TITLE")
//                    .setOngoing(true)
//                    .setAutoCancel(false)
//                    .setContentText("INFO_NOTIFICATION_MESSAGE")
//                    .setContentIntent(pendingIntent);
//            Notification notification = mNotifyBuilder.build();
//
//            if (isUpdate) {
//                NotificationManager notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
//                if (notificationManager != null) {
//                    notificationManager.notify(NOTIFICATION_ID, notification);
//                }
//            } else {
//                service.startForeground(NOTIFICATION_ID, notification);
//            }
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    /* Remove the locationlistener updates when Services is stopped */
    @Override
    public void onDestroy() {
        try {
            stopLocationUpdates();
            stopForeground(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        isLocationUpdateRunning = false;
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }
}