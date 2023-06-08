package com.dr7.salesmanmanager.Modles;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
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
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Interface.LocationDao;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.MyServices;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.Restarter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MyServicesForloc  extends Service {
    private static final String TAG = "BackgroundMyServicesForloc";
    MediaPlayer player;

    static String id;
    String activity;
    public static final String ServiceIntent = "MyServicesForloc";
    public static boolean ServiceWork = true;
    double    latitude,longitude;
    Timer timer;
    DatabaseReference databaseReference;
    public int LOCATIONTRACK =-1;
    double v1,v2;
    public static String  Firebase_ipAddress;
    DatabaseHandler mHandler;
    FirebaseDatabase dbroot;
    String ipAddress="";
public int count=0;
    String userNo="0";
    String salesName="";
    LocationDao daoRequsts;
    SalesMenLocation salesMenLocation;
    List<Settings> settings=new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(MyServicesForloc.this);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        settings = db.getAllSettings();
        userNo= db.getAllUserNo();
        daoRequsts =new LocationDao(MyServicesForloc.this);
        salesMenLocation=new SalesMenLocation();
        LOCATIONTRACK= settings.get(settings.size()-1).getLocationtracker();
        timer = new Timer();
        mHandler = new DatabaseHandler(MyServicesForloc.this);
if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O)
    startMyOenForeground();
else
    startForeground(1,new Notification());


      starttimer();


    }
@RequiresApi(Build.VERSION_CODES.O)
private  void   startMyOenForeground(){
  String  channelId = "Channel.permanence";
    CharSequence channelName = "Application_name";
    // The user-visible description of the channel.
    String channelDescription = "Application_name Alert";
    NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName,  NotificationManager.IMPORTANCE_NONE);
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    assert notificationManager != null;
    notificationManager.createNotificationChannel(notificationChannel);

    NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId);
    Notification notification=builder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE).build();
    startForeground(2,notification);

}
    public int onStartCommand(Intent intent, int flags, int startId) {
        //requestLocationUpdates();

     //   allTaskInFireBase();
   //     onDestroy();
        starttimer();
        //  Log.e(TAG, "onStartCommand() , MyServicesForNotification started..."+id);

        return START_NOT_STICKY;

    }
    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
           Log.e(TAG, "onStop() , MyServicesForloc Stop...");
        return super.stopService(name);

    }

    public void onPause() {
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        stoptimer();
        Log.e("myserviceforloc,onDestroy","onDestroy");
       // allTaskInFireBasewithoutnotify();
        db = new DatabaseHandler(MyServicesForloc.this);
        settings = db.getAllSettings();
        LOCATIONTRACK= settings.get(settings.size()-1).getLocationtracker();
        Log.e("onDestroyLOCATIONTRACK==",LOCATIONTRACK+"");
        if(LOCATIONTRACK==1) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);//  Log.e(TAG, "onCreated() , MyServicesForNotification stopped..."+id);
            super.onDestroy();
        }else
        {
            stopService(new Intent(this, MyServicesForloc.class));
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("stopservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);//
        }
    }


    //       fillData(MainActivity.this);
    //   updateSeenOfRow();
    public  void starttimer(){
        Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("count","" +count++);
                        allTaskInFireBasewithoutnotify();
                    }
                }, 5000 );

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Handler handler = new Handler(Looper.getMainLooper());
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        Log.e("count","" +count++);
//                        allTaskInFireBasewithoutnotify();
//                    }
//                }, 5000 );
//
////                Handler     mHandler = new Handler(Looper.getMainLooper()) {
////                    @Override
////                    public void handleMessage(Message message) {
////                        Log.e("count","" +count++);
////                        allTaskInFireBasewithoutnotify();
////                    }
////                };
//
//            }
//
//        }, 0, 5000);

    }
    public  void stoptimer(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }
    @Override
    public void onLowMemory() {
        //   Log.e(TAG, "onLowMemory()");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        //    Log.e(TAG, "In onTaskRemoved");
    }
    void allTaskInFireBase() {

        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) MyServicesForloc.this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyServicesForloc.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "false");
        }

        try {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Log.e("Location,== ", "latitude"+latitude+"longitude"+""+longitude);



                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged" );
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled" );
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled" );
                }
            };


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
            Log.e("Exceptioninservice", "  "+e.getMessage());
        }


    }
    void allTaskInFireBasewithoutnotify() {
//        Log.e("allTaskInFireBasewithoutnotify==", "allTaskInFireBasewithoutnotify");
        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) MyServicesForloc.this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyServicesForloc.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "false");
        }
//        Log.e("befottry==", "allTaskInFireBasewithoutnotify");
        try {
//            Log.e("intry==", "allTaskInFireBasewithoutnotify");
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                 Log.e("onLocationChanged==", "allTaskInFireBasewithoutnotify");

                    salesMenLocation.setSalesmanNo(userNo + "");
                    if(mHandler==null )  mHandler=new DatabaseHandler(MyServicesForloc.this);
                    if(daoRequsts==null ) daoRequsts=new LocationDao(MyServicesForloc.this);
                    salesName = mHandler.getSalesmanName_fromSalesTeam();
                    salesMenLocation.setSalesmanName(salesName);
                    salesMenLocation.setLatitude(latitude + "");
                    salesMenLocation.setLongitude(longitude + "");
                    try {  // code for track salesman location in FireBase Added by aya
                        //        Log.e("salesMan==", " salesMan " + userNo);

                        if (latitude != 0 || longitude != 0) {
                            daoRequsts.addLocation(salesMenLocation);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", "" + e.getMessage());
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged" );
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled" );
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled" );
                }
            };


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
            Log.e("Exceptioninservice", "  "+e.getMessage());
        }
    }
}
