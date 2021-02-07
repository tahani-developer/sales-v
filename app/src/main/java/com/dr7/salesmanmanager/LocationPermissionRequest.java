package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationPermissionRequest   extends Activity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Context context;
    SweetAlertDialog dialogTem, sweetAlertDialog;
    Message msg = new Message();
    int OpenFlag = 1;
    LocationManager locationManager;
    Timer timer;
    private LocationRequest mLocationRequest;
    GPSTracker gps;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    ImportJason importJason;
    String userCountry, userAddress;
    DatabaseHandler mHandler;
boolean flag=true;
int req=0;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    FusedLocationProviderClient mFusedLocationClient;
SalesmanStations salesmanStations;
    public  static  boolean openDialog=false;
public static double checkOutLong=0,checkOutLat=0;
int  approveAdmin=-1;
    List<com.dr7.salesmanmanager.Modles.Settings> settings;
    public LocationPermissionRequest(Context context) {
        this.context = context;
       // sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        getlocattTest();
        importJason=new ImportJason(context);
        salesmanStations=new SalesmanStations();
//        getLoc();
        mHandler=new DatabaseHandler(context);
        settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if (settings.size() != 0) {
           approveAdmin= settings.get(0).getApproveAdmin();
        }
        timer = new Timer();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location", "ooooo");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                msg.arg1 = 1;
//                handler.sendMessage(msg);
                Log.e("Location", "oo111ooo  "+OpenFlag);
                runOnUiThread(new Runnable() {
                    public void run() {
//                        if (OpenFlag == 1) {
                            dialogLoc();

//                        }

                    }
                });



            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Log.e("Location", "explanation need");

                runOnUiThread(new Runnable() {
                    public void run() {
//                        if (OpenFlag == 1) {
                            dialogLoc();

//                        }
                        openAppSettings();
                    }
                });
            }
            return false;
        } else {

            try {
                sweetAlertDialog.dismissWithAnimation();
                dialogTem.dismissWithAnimation();

            } catch (Exception e) {
                Log.e("Location", "true need2");
            }

            runOnUiThread(new Runnable() {
                public void run() {
                boolean iso=    isLocationEnabled(context);
                    Log.e("Location", "hh  "+iso);


                    if(!iso && !openDialog){
                        openDialog=true;
                       flag=false;
                        displayLocationSettingsRequest(context);
                    }
                    getLoc();
                }
            });

            Log.e("Location", "true need");
            return true;
        }
    }





    private void dialogLoc() {
        OpenFlag = 0;
//        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//
//        sweetAlertDialog.setTitleText(R.string.title_location_permission);
//        sweetAlertDialog.setContentText(String.valueOf(R.string.text_location_permission));
//        sweetAlertDialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
////                sweetAlertDialog.dismissWithAnimation();
////                 finish();
//            }
//        });
//        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
              //  dialogTem = sweetAlertDialog;

//            }
//        });
//        sweetAlertDialog.setCancelable(false);
//        sweetAlertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("Locationss", "iiiii");
        Log.e("Locationss", "granted"+requestCode);
        OpenFlag = 1;

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Location", "granted");
                    try {
                        sweetAlertDialog.dismissWithAnimation();
                        dialogTem.dismissWithAnimation();
                    } catch (Exception r) {

                    }
                    OpenFlag = 1;
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        OpenFlag=1;
                        Log.e("Location", "granted updates");

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }

                } else {

                    Log.e("Location", "Deny");

                    OpenFlag=1;
                    // permission, denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void timerLocation(){



            timer.schedule(new TimerTask() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    Log.e("locationApp", "" +approveAdmin);
                    approveAdmin= settings.get(0).getApproveAdmin();
                    if(approveAdmin==1) {
//                        if (OpenFlag==1) {
                            Log.e("locationRec", "" + req);
                            checkLocationPermission();
//                        }

                    }
                }

            }, 0, 1000);


    }


    public void openAppSettings() {

        Uri packageUri = Uri.fromParts( "package", "com.dr7.salesmanmanager", null );

        Intent applicationDetailsSettingsIntent = new Intent();

        applicationDetailsSettingsIntent.setAction( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
        applicationDetailsSettingsIntent.setData( packageUri );
        applicationDetailsSettingsIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

        context.startActivity( applicationDetailsSettingsIntent );

    }
    private void turnGPSOn(){


        try {
//            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            if (!provider.contains("gps")) { //if gps is disabled
//                final Intent poke = new Intent();
//                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//                poke.setData(Uri.parse("3"));
//                sendBroadcast(poke);
//
//            }

//            Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
//            intent.putExtra("enabled", true);
//            sendBroadcast(intent);

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }catch (Exception e){

            Log.e("Location", "error for open gps location" + e.toString());
        }
    }
    private void displayLocationSettingsRequest(Context context) {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        Log.e("Locationnnnn", "bbb");

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        Log.e("Locationnnnn", "bbb"+result.toString());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("Location", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) context, 10001);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    public void getLoc(){

        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]
                    {ACCESS_FINE_LOCATION}, 1);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                salesmanStations.setSalesmanNo(Login.salesMan);
                salesmanStations.setLatitude(""+latitude);
                salesmanStations.setLongitude(""+longitude);
                runOnUiThread(new Runnable() {
                    public void run() {
                        importJason.updateLocation(salesmanStations.getJSONObject());
                    }
                });

                checkOutLong=longitude;
                checkOutLat=latitude;

                Log.e("location123456","  "+salesmanStations.getJSONObject());
                Log.e("location12345","    la= "+latitude +"  lo = "+longitude);
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

    }

    public void closeLocation() {
        if (timer!=null) {
            timer.cancel();
        }else {
            Log.e("timerIsNull","kkk");
        }
           }


//    private void getlocattTest() {
//
//        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {// Not granted permission
//
//            ActivityCompat.requestPermissions((Activity) context, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
////
//        }
//        if (mFusedLocationClient != null) {
//            Log.e("mFusedLocationClient","");
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
////            requestLocationUpdates();
//        }
//        else {
//            Log.e("mFusedLocationClient",""+mFusedLocationClient);
//        }
//
//    }

//    public void requestLocationUpdates() {
//        Log.e("requestLocationUpdates","ll");
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000); // two minute interval
//        mLocationRequest.setFastestInterval(10000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//        }
//    }

//    LocationCallback mLocationCallback = new LocationCallback(){
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Log.e("onLocationResult",""+locationResult);
//
//                for (Location location : locationResult.getLocations()) {
////                    Log.e("MainActivity", "getLocationComp: " + location.getLatitude() + " " + location.getLongitude());
//
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//
//                        }
//
//                    Log.e("locationPer", "" + latitude + "\t" + longitude);
//
//                }};



//   public void location(){
//
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//
//        try {
//
//            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            Log.e("location123"," g= "+gps_loc+"    n= "+network_loc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (gps_loc != null) {
//            final_loc = gps_loc;
//            latitude = final_loc.getLatitude();
//            longitude = final_loc.getLongitude();
//
//            Log.e("location1234"," f= "+final_loc+"    la= "+latitude +"  lo = "+longitude);
//
//        }
//        else if (network_loc != null) {
//            final_loc = network_loc;
//            latitude = final_loc.getLatitude();
//            longitude = final_loc.getLongitude();
//
//            Log.e("location12345"," f= "+final_loc+"    la= "+latitude +"  lo = "+longitude);
//
//        }
//        else {
//            latitude = 0.0;
//            longitude = 0.0;
//        }
//
//
//        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
//
//        try {
//
//            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && addresses.size() > 0) {
//                userCountry = addresses.get(0).getCountryName();
//                userAddress = addresses.get(0).getAddressLine(0);
////                tv.setText(userCountry + ", " + userAddress);
//            }
//            else {
//                userCountry = "Unknown";
////                tv.setText(userCountry);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
