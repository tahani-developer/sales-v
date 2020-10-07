package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.VisitRate;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.Reports;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.item_serial;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CustomerCheckInFragment.CustomerCheckInInterface, CustomerListShow.CustomerListShow_interface {

    private static final String TAG = "MainActivity";
    public static int menuItemState;
    static public TextView mainTextView;
    LinearLayout checkInLinearLayout, checkOutLinearLayout;
    public static ImageView checkInImageView, checkOutImageView;
    static int checknum;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private DatabaseHandler mDbHandler;
     public   LocationManager locationManager;
    LocationListener locationListener;

    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;


    public  static  double latitude_main, longitude_main;
    boolean isPosted = true,isPostedCustomerMaster=true;

    public static final int PICK_IMAGE = 1;
    Bitmap itemBitmapPic = null;
    ImageView logo;
    Calendar myCalendar;
    Bitmap visitPic = null;
    ImageView visitPicture;
    int amountOfmaxDiscount = 0;
    public static TextView tvresult;
    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();
    int sum_chech_export_lists=0;

     DrawerLayout drawer_layout;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    private FusedLocationProviderClient fusedLocationClient;
    public  static CustomerLocation customerLocation_main;
    public  static Location location_main;
    public  int first=0,isClickLocation=0;
    public  static  double latitudeCheckIn=0,longtudeCheckIn=0;

    public static void settext2() {
        mainTextView.setText(CustomerListShow.Customer_Name);
    }

    @Override
    public void showCustomersList() {
        CustomerListFragment customerListFragment = new CustomerListFragment();
        customerListFragment.show(getSupportFragmentManager(), "");


    }

    @Override
    public void displayCustomerListShow() {
        try {
            CustomerListShow customerListShow = new CustomerListShow();
            customerListShow.setCancelable(true);
            customerListShow.setListener(this);
            customerListShow.show(getSupportFragmentManager(), "");
        } catch (Exception e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleAppUtils.setConfigChange(MainActivity.this);

//        finish();
//        startActivity(getIntent());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDbHandler = new DatabaseHandler(MainActivity.this);
        drawer_layout=findViewById(R.id.drawer_layout);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        first=1;
        isClickLocation=1;
        try {
            if(mDbHandler.getAllSettings().get(0).getAllowOutOfRange()==1)
            {
//                if(isNetworkAvailable())
//                {
                    getlocationForCheckIn();
//                }
//                else {
//                    Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
//                }


            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(languagelocalApp.equals("ar"))
            {
                drawer_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    drawer_layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e){
            drawer_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        tvresult = (TextView) findViewById(R.id.tvresult);

        Button btn = (Button) findViewById(R.id.btn);

//        requestLocationUpdates();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });




        mainTextView = (TextView) findViewById(R.id.mainTextView);
        settext2();
        checkInLinearLayout = (LinearLayout) findViewById(R.id.checkInLinearLayout);
        checkOutLinearLayout = (LinearLayout) findViewById(R.id.checkOutLinearLayout);
        checkInImageView = (ImageView) findViewById(R.id.checkInImageView);
        checkOutImageView = (ImageView) findViewById(R.id.checkOutImageView);
        if (!CustomerListShow.Customer_Name.equals("No Customer Selected !"))//test after change language
        {
            checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
            checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
        }

        checkInLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in));
                    if (CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
                        checknum = 1;
                        menuItemState = 1;
                        openSelectCustDialog();
                    } else {
                        Toast.makeText(MainActivity.this, CustomerListShow.Customer_Name + " is checked in", Toast.LENGTH_SHORT).show();
                        checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in_hover));
                }
                return true;
            }
        });

        checkOutLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
                    if (!CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
                        openCustCheckOut();
                    } else {
                        Toast.makeText(MainActivity.this, "No Customer Selected !", Toast.LENGTH_SHORT).show();
                        checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out_hover));
                }
                return true;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                openReadBarcode();
              openAddCustomerDialog();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.sales_man_name);
        navUsername.setText(Login.salesMan);
        navigationView.setNavigationItemSelectedListener(this);
        menuItemState = 0;

    }

    private void openReadBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(SmallCaptureActivity.class);
            integrator.initiateScan();
//        new IntentIntegrator(MainActivity.this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    public  void getlocationForCheckIn() {


            LocationListener locationListener;

            locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {// Not granted permission
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    latitudeCheckIn  = location.getLatitude();
                    longtudeCheckIn = location.getLongitude();
                    Log.e("onLocationChanged",""+latitudeCheckIn+""+longtudeCheckIn);


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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);//test
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            catch (Exception e)
            {
                Log.e("locationManager",""+e.getMessage());
            }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public static boolean textContainsArabic(String text) {
        for (char charac : text.toCharArray()) {
            if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
                return true;
            }
        }
        return false;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mFusedLocationClient != null) {
//            requestLocationUpdates();
//        }
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//        }
//    }
    public void requestLocationUpdates() {
        Log.e("requestLocationUpdates","");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openPasswordDialog(1);
//        } else if (id == R.id.action_cust_check_in) {
//            checknum = 1;
//            menuItemState = 1;
//            openSelectCustDialog();
//
//        } else if (id == R.id.action_cust_check_out) {
//            openCustCheckOut();

        } else if (id == R.id.action_print_voucher) {
            Intent intent = new Intent(MainActivity.this, PrintVoucher.class);
            startActivity(intent);

        } else if (id == R.id.action_print_payment) {
            Intent intent = new Intent(MainActivity.this, PrintPayment.class);
            startActivity(intent);

//        } else if (id == R.id.action_add_cust) {
//            openAddCustomerDialog();

        } else if (id == R.id.action_company_info) {
            openPasswordDialog(2);

        } else if (id == R.id.de_export) {
            openPasswordDialog(3);
        } else if (id == R.id.printerSetting) {
            openPasswordDialog(4);
        }
        else if (id == R.id.saveLocation) {
            try {
                if(isNetworkAvailable())
                {

                    if( !CustomerListShow.Customer_Name.equals("No Customer Selected !"))
                    {
                        saveCurrentLocation();
                    }
                    else {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.warning_message))
                                .setContentText(getResources().getString(R.string.pleaseSelectUser))
                                .show();
                    }

                }
                else {
                    Log.e("isNetworkAvailable","NOT");
                    Toast.makeText(this, ""+getResources().getString(R.string.enternetConnection), Toast.LENGTH_SHORT).show();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return super.

                onOptionsItemSelected(item);
    }

    public void saveCurrentLocation() throws InterruptedException {
        first=2;
        isClickLocation=2;
//        requestSingleUpdate();
        Log.e("saveCurrentLocation",""+isClickLocation);
        getlocattTest();
//        if(CustomerListShow.Customer_Account.equals(""))
//        {
//            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText(getResources().getString(R.string.warning_message))
//                    .setContentText(getResources().getString(R.string.pleaseSelectUser))
//                    .show();
//
//        } else {
//
//
//            if(isNetworkAvailable()){
//                String latitude = CustomerListShow.latitude;
//                final String longitude = CustomerListShow.longtude;
//
//                if(!latitude.equals("")&&!longitude.equals("")){
//
//                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(getResources().getString(R.string.warning_message))
//                            .setContentText(getResources().getString(R.string.customerHaveLocation))
//                            .show();
//                }
//                else {
//                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {// Not granted permission
//
//                        ActivityCompat.requestPermissions(this, new String[]
//                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//
//                    }
//
//
//                    locationListener = new LocationListener() {
//                        @Override
//                        public void onLocationChanged(Location location) {
//                            latitude_main = location.getLatitude();
//                            longitude_main = location.getLongitude();
//                            customerLocation_main = new CustomerLocation();
//                                        customerLocation_main.setCUS_NO(CustomerListShow.Customer_Account);
//                                        customerLocation_main.setLONG(longitude_main + "");
//                                        customerLocation_main.setLATIT(latitude_main + "");
//                                        mDbHandler.addCustomerLocation(customerLocation_main);
//
//                            mDbHandler.addCustomerLocation(customerLocation_main);
//                                        mDbHandler.updateCustomerMasterLocation(CustomerListShow.Customer_Account,latitude_main+"",longitude_main+"");
//                                        CustomerListShow.latitude=latitude_main+"";
//                                        CustomerListShow.longtude=longitude_main+"";
//                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                                .setTitleText(getResources().getString(R.string.succsesful))
//                                                .setContentText(getResources().getString(R.string.LocationSaved))
//                                                .show();
//                            Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
////                            Toast.makeText(MainActivity.this, "latitude="+latitude_main+"long="+longitude_main, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                        }
//
//                        @Override
//                        public void onProviderEnabled(String provider) {
//                        }
//
//                        @Override
//                        public void onProviderDisabled(String provider) {
//
//                        }
//                    };
//
//
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//
//
//
//
//                    //****************************************************************
////                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//                  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {// Not granted permission
//
//                        ActivityCompat.requestPermissions(this, new String[]
//                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//
//                    }
////                    Thread.sleep(1000);
//
//
//                    /////////////////////////////////////////**********************************
////                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
////                    fusedLocationClient.getLastLocation()
////                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
////                                @Override
////                                public void onSuccess(Location location) {
////                                    // Got last known location. In some rare situations this can be null.
////                                    if (location != null) {
////                                        location_main=new Location(location);
////                                        latitude_main = location.getLatitude();
////                                        longitude_main = location.getLongitude();
////                                        location_main.setLatitude(latitude_main);
////                                        location_main.setLongitude(longitude_main);
////                                        customerLocation_main = new CustomerLocation();
////                                        customerLocation_main.setCUS_NO(CustomerListShow.Customer_Account);
////                                        customerLocation_main.setLONG(longitude_main + "");
////                                        customerLocation_main.setLATIT(latitude_main + "");
////                                        mDbHandler.addCustomerLocation(customerLocation_main);
////                                        mDbHandler.updateCustomerMasterLocation(CustomerListShow.Customer_Account,latitude_main+"",longitude_main+"");
////                                        CustomerListShow.latitude=latitude_main+"";
////                                        CustomerListShow.longtude=longitude_main+"";
////                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
////                                                .setTitleText(getResources().getString(R.string.succsesful))
////                                                .setContentText(getResources().getString(R.string.LocationSaved))
////                                                .show();
////                                        Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
////                                        Toast.makeText(MainActivity.this, "latitude="+latitude_main+"long="+longitude_main, Toast.LENGTH_SHORT).show();
////                                    }
////                                    // Logic to handle location object
////
////                                }
////                            });
//                }
//            }
//            else {
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText(getResources().getString(R.string.warning_message))
//                        .setContentText(getResources().getString(R.string.enternetConnection))
//                        .show();
//            }
//
//
//        }// END ELSE

    }//end

    private void getlocattTest() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {// Not granted permission

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//
                    }
        if (mFusedLocationClient != null) {
            Log.e("mFusedLocationClient","");
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            requestLocationUpdates();
        }
        else {
            Log.e("mFusedLocationClient",""+mFusedLocationClient);
        }

    }
    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.e("onLocationResult",""+locationResult);
                    if(CustomerListShow.Customer_Account.equals(""))
        {
            if(first!=1)
            {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getResources().getString(R.string.warning_message))
                        .setContentText(getResources().getString(R.string.pleaseSelectUser))
                        .show();
            }


        } else {


            if(isNetworkAvailable()){
                String latitude="",  longitude="" ;
                try {
                     latitude = CustomerListShow.latitude;
                   longitude = CustomerListShow.longtude;
                   Log.e("latitude",""+latitude+longitude);
                }
                catch (Exception e)
                {
                    latitude="";
                    longitude="";

                }
                Log.e("latitude",""+latitude+longitude);


                if(!latitude.equals("")&&!longitude.equals("")&&isClickLocation==2)
                {

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getResources().getString(R.string.warning_message))
                            .setContentText(getResources().getString(R.string.customerHaveLocation))
                            .show();
                }
                else {
                    if (isClickLocation == 2) {
                        for (Location location : locationResult.getLocations()) {
                            Log.e("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                            latitude_main = location.getLatitude();
                            longitude_main = location.getLongitude();
                            customerLocation_main = new CustomerLocation();
                            customerLocation_main.setCUS_NO(CustomerListShow.Customer_Account);
                            customerLocation_main.setLONG(longitude_main + "");
                            customerLocation_main.setLATIT(latitude_main + "");

                            mDbHandler.addCustomerLocation(customerLocation_main);
                            mDbHandler.updateCustomerMasterLocation(CustomerListShow.Customer_Account, latitude_main + "", longitude_main + "");
                            CustomerListShow.latitude = latitude_main + "";
                            CustomerListShow.longtude = longitude_main + "";
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getResources().getString(R.string.succsesful))
                                    .setContentText(getResources().getString(R.string.LocationSaved))
                                    .show();


                            Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);



                        }

                    }

                }

            }
            else {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getResources().getString(R.string.warning_message))
                        .setContentText(getResources().getString(R.string.enternetConnection))
                        .show();
            }


        }// END ELSE
            isClickLocation=1;

        };

    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    try {
                        saveCurrentLocation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "check permission location ", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (menuItemState == 1) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(true);
            menu.getItem(0).getIcon().setAlpha(130);
            menu.getItem(1).getIcon().setAlpha(255);
        } else if (menuItemState == 0) {
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(false);
            menu.getItem(0).getIcon().setAlpha(255);
            menu.getItem(1).getIcon().setAlpha(130);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_activities) {
            Intent intent = new Intent(this, Activities.class);
            startActivity(intent);

        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(this, Reports.class);
            startActivity(intent);

        } else if (id == R.id.nav_exp_data) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to post data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                            Log.e("sumExport",""+sum_chech_export_lists);
                            if(mDbHandler.getAllSettings().get(0).getPassowrd_data()==1) {
                                openPasswordDialog(6);
                            }
                            else{
                                ExportJason obj = null;
                            try {
                                obj = new ExportJason(MainActivity.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            obj.startExportDatabase();

                            }





                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.customers_location) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.sales_man_map) {
            Intent intent = new Intent(this, SalesmanMap.class);
            startActivity(intent);

        }

//                else{
//
//                        LocaleAppUtils.setLocale(new Locale("en"));
//                        LocaleAppUtils.setConfigChange(MainActivity.this);
//                        finish();
//                        startActivity(getIntent());
//                }



        else if (id == R.id.nav_imp_data) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                if (mDbHandler.getAllSettings().get(0).getPassowrd_data() == 1) {
                                    openPasswordDialog(5);
                                } else {
                                    if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1)
                                    {
                                        isPostedCustomerMaster=mDbHandler.isCustomerMaster_posted();
                                    }
                                    else {isPostedCustomerMaster=true;}


                                    isPosted=mDbHandler.isAllVoucher_posted();
                                    if(isPostedCustomerMaster)
                                    {
                                        if(isPosted==true)
                                        {
                                            ImportJason obj = new ImportJason(MainActivity.this);
                                            obj.startParsing();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,R.string.failImpo_export_data , Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,R.string.failImpo_export_dataCustomerMaster , Toast.LENGTH_SHORT).show();

                                    }



                                }
                            }catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                                Log.e("ExceptionMain",""+e.getMessage());
                            }



                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.nav_refreshdata) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Confirm Update")
//                    .setMessage("Are you sure you want to refresh data ? This will take few minutes !")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {

            RefreshData obj = new RefreshData(MainActivity.this);
            obj.startParsing();
            //obj.storeInDatabase();

//                        }
//                    })
//                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.nav_sign_out) {
//            Intent intent = new Intent(this, CPCL2Menu.class);
//            startActivity(intent);

        } else if (id == R.id.nav_clear_local) {

            mDbHandler.deleteAllPostedData();

        }
        else if (id == R.id.nav_backup_data) {


            try {
                verifyStoragePermissions(MainActivity.this);
                copyFile();
            }
            catch (Exception e)
            {verifyStoragePermissions(MainActivity.this);


                Toast.makeText(this, ""+getResources().getString(R.string.backup_failed), Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void openSelectCustDialog() {
        CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment();
        customerCheckInFragment.setCancelable(false);
        customerCheckInFragment.setListener(this);
        customerCheckInFragment.show(getFragmentManager(), "");
    }

    public void openMaxDiscount() {
        Log.e("openMaxDiscount", "yes");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.max_discount_credit);

        final EditText amount = (EditText) dialog.findViewById(R.id.amount_discount_cridit);
        Button okButton = (Button) dialog.findViewById(R.id.okBut_discount);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut_discount);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amount.getText().toString().equals("")) {
                    amountOfmaxDiscount = Integer.parseInt(amount.getText().toString());
                    Log.e("amountOfmaxDiscount", "" + amountOfmaxDiscount);
                    //  mDbHandler.getAllSettings().get(0).setAmountOfMaxDiscount(amountOfmaxDiscount);
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Incorrect Input !", Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void openAddCustomerDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_customer_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();


        final EditText addCus = (EditText) dialog.findViewById(R.id.custEditText);
        final EditText remark = (EditText) dialog.findViewById(R.id.remarkEditText);
        Button done = (Button) dialog.findViewById(R.id.doneButton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude_main = location.getLatitude();
                longitude_main = location.getLongitude();
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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addCus.getText().toString().equals("")) {
                    mDbHandler.addAddedCustomer(new AddedCustomer(addCus.getText().toString(), remark.getText().toString(),
                            latitude_main, longitude_main, Login.salesMan, 0, Login.salesManNo));
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please add customer name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public void openCustCheckOut() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checknum = 0;
                CustomerListShow.Customer_Name = "No Customer Selected !";
                CustomerListShow.longtude="";
                CustomerListShow.latitude="";
                CustomerListShow.Customer_Account="0";
                settext2();
                menuItemState = 0;

                checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in));
                checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));

                CustomerCheckInFragment obj = new CustomerCheckInFragment();
                obj.editCheckOutTimeAndDate();

               // openVisitRateDialog();  stopped just for new customer
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.app_no), null);
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void openVisitRateDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.visit_rate);
        dialog.setCanceledOnTouchOutside(true);

        final RadioGroup visitPurpose = dialog.findViewById(R.id.visit_purpose);
        RadioButton payment = dialog.findViewById(R.id.payment);
        RadioButton bankUp = dialog.findViewById(R.id.bank_up);
        RadioButton enterCat = dialog.findViewById(R.id.entering_categories);
        RadioButton vistSpase = dialog.findViewById(R.id.visit_presentation_space);
        RadioButton other = dialog.findViewById(R.id.other);

        final RadioButton r1 = dialog.findViewById(R.id.rate1);
        final RadioButton r2 = dialog.findViewById(R.id.rate2);
        final RadioButton r3 = dialog.findViewById(R.id.rate3);
        final RadioButton r4 = dialog.findViewById(R.id.rate4);
        final RadioButton r5 = dialog.findViewById(R.id.rate5);

        final CheckBox custRegard = dialog.findViewById(R.id.cust_regard);
        final CheckBox checkStore = dialog.findViewById(R.id.check_store);
        final CheckBox PromoCheckStore = dialog.findViewById(R.id.promotion_check_stock);
        final CheckBox selectProposedRequest = dialog.findViewById(R.id.select_proposed_request);
        final CheckBox display = dialog.findViewById(R.id.display_and_persuasion);

        visitPicture = dialog.findViewById(R.id.image);
        Button save = dialog.findViewById(R.id.save);
        Button cancel = dialog.findViewById(R.id.cancel);

        final int[] rate = {0};
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 1;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(false);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 2;
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 3;
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(true);
                r5.setChecked(false);
                rate[0] = 4;
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(true);
                r5.setChecked(true);
                rate[0] = 5;
            }
        });

        visitPicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1888);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int visitPurpos = 0;
                switch (visitPurpose.getCheckedRadioButtonId()) {
                    case R.id.payment:
                        visitPurpos = 0;
                        break;
                    case R.id.bank_up:
                        visitPurpos = 1;
                        break;
                    case R.id.entering_categories:
                        visitPurpos = 2;
                        break;
                    case R.id.visit_presentation_space:
                        visitPurpos = 3;
                        break;
                    case R.id.other:
                        visitPurpos = 4;
                        break;
                }

                int custRegar = custRegard.isChecked() ? 1 : 0;
                int checkStor = checkStore.isChecked() ? 1 : 0;
                int PromoCheckStor = PromoCheckStore.isChecked() ? 1 : 0;
                int selectProposedReques = selectProposedRequest.isChecked() ? 1 : 0;
                int displa = display.isChecked() ? 1 : 0;

                mDbHandler.addVisitRate(new VisitRate(visitPurpos, custRegar, checkStor, PromoCheckStor, selectProposedReques,
                        displa, rate[0], visitPic, CustomerListShow.Customer_Account, CustomerListShow.Customer_Name, Login.salesManNo));

                dialog.dismiss();
              //  Toast.makeText(MainActivity.this, "Saved !", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openPasswordDialog(final int flag) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.password_dialog);

        final EditText password = (EditText) dialog.findViewById(R.id.editText1);
        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);
        final CheckBox cb_show = (CheckBox) dialog.findViewById(R.id.checkBox_showpass);
//        EditText et1 = (EditText) this.findViewById(R.id.editText1);
        cb_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_show.isChecked()) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(129);
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("303090")) {
                    dialog.dismiss();

                    if (flag == 1) {
                        openSetting alert = new openSetting();
                        alert.showDialog(MainActivity.this, "Error de conexión al servidor");
                    } else if (flag == 2)
                        openCompanyInfoDialog();

                    else if (flag == 3) {
                        openDeExportDialog();
                    } else if (flag == 4) {
                        openPrintSetting();
                    }
                    else if (flag == 5) {

                        if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1)
                        {
                            isPostedCustomerMaster=mDbHandler.isCustomerMaster_posted();
                        }
                        else {isPostedCustomerMaster=true;}


                        isPosted=mDbHandler.isAllVoucher_posted();
                        if(isPostedCustomerMaster)
                        {
                            if(isPosted==true)
                            {
                                ImportJason obj = new ImportJason(MainActivity.this);
                                obj.startParsing();
                            }
                            else{
                                Toast.makeText(MainActivity.this,R.string.failImpo_export_data , Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this,R.string.failImpo_export_dataCustomerMaster , Toast.LENGTH_SHORT).show();

                        }

                    }
                    else if (flag == 6) {
                        ExportJason obj = null;
                        try {
                            obj = new ExportJason(MainActivity.this);

                            obj.startExportDatabase();
                        }
                        catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                    }
                } else
                    Toast.makeText(MainActivity.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class openSetting {
        boolean validSerial=false,validReturn=false,validOrder=false;
        @SuppressLint("SetTextI18n")
        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_setting);


            final EditText linkEditText = (EditText) dialog.findViewById(R.id.link);
            final EditText numOfCopy = (EditText) dialog.findViewById(R.id.num_of_copy);
            final EditText invoicEditText = (EditText) dialog.findViewById(R.id.invoice_serial);
            final EditText returnEditText = (EditText) dialog.findViewById(R.id.return_serial);
            final EditText orderEditText = (EditText) dialog.findViewById(R.id.order_serial);
            final EditText paymentEditTextCash = (EditText) dialog.findViewById(R.id.payments_serial_cash);
            final EditText paymentEditTextCheque = (EditText) dialog.findViewById(R.id.payments_serial_cheque);
            final EditText paymentEditTextCredit = (EditText) dialog.findViewById(R.id.payments_serial_creditCard);
            final EditText salesmanNmae = (EditText) dialog.findViewById(R.id.salesman_name_text);
            final RadioGroup taxCalc = (RadioGroup) dialog.findViewById(R.id.taxTalc);

            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.price_by_cust);
            final CheckBox checkBox2 = (CheckBox) dialog.findViewById(R.id.use_weight_case);
            final RadioGroup printMethod = (RadioGroup) dialog.findViewById(R.id.printMethod);
            final RadioButton bluetooth = (RadioButton) dialog.findViewById(R.id.bluetoothRadioButton);
            final RadioButton wifi = (RadioButton) dialog.findViewById(R.id.wifiRadioButton);
            final CheckBox allowMinus = (CheckBox) dialog.findViewById(R.id.allow_sale_with_minus);
            final CheckBox salesManCustomersOnly = (CheckBox) dialog.findViewById(R.id.salesman_customers_only);
            final CheckBox minSalePrice = (CheckBox) dialog.findViewById(R.id.min_sale_price);
            final CheckBox allowOutOfRange = (CheckBox) dialog.findViewById(R.id.allow_cust_check_out_range);
            final RadioButton exclude = (RadioButton) dialog.findViewById(R.id.excludeRadioButton);
            final RadioButton include = (RadioButton) dialog.findViewById(R.id.includeRadioButton);
            final CheckBox checkBox_canChangePrice = (CheckBox) dialog.findViewById(R.id.can_change_price);
            final CheckBox readDiscount = (CheckBox) dialog.findViewById(R.id.read_discount);
            final CheckBox workOnline = (CheckBox) dialog.findViewById(R.id.work_online);
            final CheckBox paymetod_check = (CheckBox) dialog.findViewById(R.id.checkBox_paymethod_check);
            final CheckBox bonusNotAlowed = (CheckBox) dialog.findViewById(R.id.checkBox_bonus_notallowed);
            final CheckBox noOfferForCredit = (CheckBox) dialog.findViewById(R.id.checkBox_NoOffer_forCredit);
            final CheckBox customerAuthor = (CheckBox) dialog.findViewById(R.id.CustomerAuthorize_checkbox);
            final CheckBox passowrdData_checkbox = (CheckBox) dialog.findViewById(R.id.PassowrdData_checkbox);
            final CheckBox arabicLanguage_checkbox = (CheckBox) dialog.findViewById(R.id.ArabicLanguage_checkbox);
            final CheckBox hideQty_checkbox = (CheckBox) dialog.findViewById(R.id.hideQty_checkbox);
            final CheckBox lockcash_checkbox = (CheckBox) dialog.findViewById(R.id.lockcash_checkbox);
            final CheckBox preventNew_checkbox = (CheckBox) dialog.findViewById(R.id.preventNewOrder_checkbox);
            final CheckBox note_checkbox = (CheckBox) dialog.findViewById(R.id.note_checkbox);
            final CheckBox ttotalDisc_checkbox = (CheckBox) dialog.findViewById(R.id.preventtotalDisc_checkbox);
            final CheckBox automaticCheck_checkbox = (CheckBox) dialog.findViewById(R.id.automatic_cheque_checkbox);
            final CheckBox tafqit_checkbox = (CheckBox) dialog.findViewById(R.id.tafqit_checkbox);
            final CheckBox preventChange_checkbox = (CheckBox) dialog.findViewById(R.id.preventChangePay_checkbox);
            final CheckBox showCustomerList_checkbox = (CheckBox) dialog.findViewById(R.id.showCustomerList_checkbox);
            final CheckBox noReturn_checkbox = (CheckBox) dialog.findViewById(R.id.noReturn_checkbox);
            final CheckBox workSerial_checkbox = (CheckBox) dialog.findViewById(R.id.workSerial_checkbox);



            Button okButton = (Button) dialog.findViewById(R.id.okBut);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

            invoicEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().equals("")) {
                        int vouchNo=Integer.parseInt(charSequence.toString());

                        int validateVoucherNo = mDbHandler.checkVoucherNo(vouchNo+1,504);
                        Log.e("onTextChanged",""+validateVoucherNo);
                        if(validateVoucherNo != 0)
                        {
                            invoicEditText.setError("Duplicated Voucher No");
                            validSerial=false;
                        }
                        else {invoicEditText.setError(null);
                            validSerial=true;}
//

                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            returnEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().equals("")) {
                        int vouchNo=Integer.parseInt(charSequence.toString());

                        int validateVoucherNo = mDbHandler.checkVoucherNo(vouchNo+1,506);
                        Log.e("onTextChanged",""+validateVoucherNo);
                        if(validateVoucherNo != 0)
                        {
                            returnEditText.setError("Duplicated Voucher No");
                            validReturn=false;
                        }
                        else {
                            returnEditText.setError(null);
                        validReturn=true;
                        }
//

                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            orderEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().equals("")) {
                        int vouchNo=Integer.parseInt(charSequence.toString());

                        int validateVoucherNo = mDbHandler.checkVoucherNo(vouchNo+1,508);
                        Log.e("onTextChanged",""+validateVoucherNo);
                        if(validateVoucherNo != 0)
                        {
                            orderEditText.setError("Duplicated Voucher No");
                            validOrder=false;
                        }
                        else {
                            orderEditText.setError(null);
                            validOrder=true;}
//

                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (mDbHandler.getAllSettings().size() != 0) {
                linkEditText.setText("" + mDbHandler.getAllSettings().get(0).getIpAddress());
                numOfCopy.setText("" + mDbHandler.getAllSettings().get(0).getNumOfCopy());
                invoicEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(504) + 1));
                returnEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(506) + 1));
                orderEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(508) + 1));

                paymentEditTextCash.setText("" + (mDbHandler.getMaxSerialNumber(1) + 1));//test
                paymentEditTextCheque.setText("" + (mDbHandler.getMaxSerialNumber(4) + 1));
                try {
                    paymentEditTextCredit.setText("" + (mDbHandler.getMaxSerialNumber(2) + 1));
                }
                catch (Exception e)
                {
                    paymentEditTextCredit.setText("");

                }

                salesmanNmae.setText(mDbHandler.getAllSettings().get(0).getSalesMan_name()+"");

                if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0)
                    bluetooth.setChecked(true);
                else
                    wifi.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0)
                    exclude.setChecked(true);
                else
                    include.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 1)
                    checkBox.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1)
                    checkBox2.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getAllowMinus() == 1)
                    allowMinus.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getSalesManCustomers() == 1)
                    salesManCustomersOnly.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getMinSalePric() == 1)
                    minSalePrice.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1)
                    allowOutOfRange.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getCanChangePrice() == 1)
                    checkBox_canChangePrice.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1)
                    readDiscount.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getBonusNotAlowed() == 1)
                    bonusNotAlowed.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1)
//                    workOnline.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getPaymethodCheck() == 1)
                    paymetod_check.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getCustomer_authorized() == 1)
                    customerAuthor.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getPassowrd_data() == 1)
                    passowrdData_checkbox.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getArabic_language() == 1) {
                    arabicLanguage_checkbox.setChecked(true);
                    languagelocalApp="ar";
                }
                else{
                    languagelocalApp="en";
                }
                if (mDbHandler.getAllSettings().get(0).getHide_qty() == 1) {
                    hideQty_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getLock_cashreport() == 1) {
                    lockcash_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getPriventOrder() == 1) {
                    preventNew_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getRequiNote() == 1) {
                    note_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getPreventTotalDisc() == 1) {
                    ttotalDisc_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getAutomaticCheque() == 1) {
                    automaticCheck_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getTafqit() == 1) {
                    tafqit_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getPreventChangPayMeth() == 1) {
                    preventChange_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getShowCustomerList() == 1) {
                    showCustomerList_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getNoReturnInvoice() == 1) {
                    noReturn_checkbox.setChecked(true);
                }
                if (mDbHandler.getAllSettings().get(0).getWork_serialNo() == 1) {
                    workSerial_checkbox.setChecked(true);
                }










//                if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1)
//                    noOfferForCredit.setChecked(true);


            }
            arabicLanguage_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if( arabicLanguage_checkbox.isChecked())
                   {

                        LocaleAppUtils.setLocale(new Locale("ar"));
                       languagelocalApp="ar";
                    }
                   else {
                       LocaleAppUtils.setLocale(new Locale("en"));
                             languagelocalApp="en";
                   }

                }
            });
            noOfferForCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (noOfferForCredit.isChecked()) {
                        Log.e("noOfferForCredit", "yes");
                        openMaxDiscount();
                    }
                }
            });

            okButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

//                    LocaleAppUtils.setConfigChange(MainActivity.this);
//                    finish();
//                    startActivity(getIntent());
                    settext2();



                    int numOfCopys=0,invoice=0,return1=0,order=0,paymentCash=0,paymentCheque=0,paymentCredit=0;

                    if (!(linkEditText.getText().toString().equals(""))) {
                        if ((!numOfCopy.getText().toString().equals("")) && !invoicEditText.getText().toString().equals("") && !returnEditText.getText().toString().equals("") &&
                                !orderEditText.getText().toString().equals("") && !paymentEditTextCash.getText().toString().equals("")
                                && !paymentEditTextCheque.getText().toString().equals("")
                                && !paymentEditTextCredit.getText().toString().equals("")) {

//                           if(validSerial&&validOrder&&validReturn)
//                           {
                               if (Integer.parseInt(numOfCopy.getText().toString()) < 5) {
                                   String link = linkEditText.getText().toString().trim();
                                   try {
                                       numOfCopys = Integer.parseInt(numOfCopy.getText().toString());
                                       invoice = Integer.parseInt(invoicEditText.getText().toString()) - 1;
                                       return1 = Integer.parseInt(returnEditText.getText().toString()) - 1;
                                       order = Integer.parseInt(orderEditText.getText().toString()) - 1;
                                       paymentCash = Integer.parseInt(paymentEditTextCash.getText().toString()) - 1;
                                       paymentCheque = Integer.parseInt(paymentEditTextCheque.getText().toString()) - 1;
                                       paymentCredit = Integer.parseInt(paymentEditTextCredit.getText().toString()) - 1;
                                   }
                                   catch (Exception e)
                                   {
                                       Toast.makeText(MainActivity.this, "Invalid Input Number", Toast.LENGTH_SHORT).show();
                                       Log.e("SettingException",""+e.getMessage());

                                   }


                                   int taxKind = taxCalc.getCheckedRadioButtonId() == R.id.excludeRadioButton ? 0 : 1;
                                   int pprintMethod = printMethod.getCheckedRadioButtonId() == R.id.bluetoothRadioButton ? 0 : 1;
                                   int priceByCust = checkBox.isChecked() ? 1 : 0;
                                   int useWeightCase = checkBox2.isChecked() ? 1 : 0;
                                   int alowMinus = allowMinus.isChecked() ? 1 : 0;
                                   int salesManCustomers = salesManCustomersOnly.isChecked() ? 1 : 0;
                                   int minSalePric = minSalePrice.isChecked() ? 1 : 0;
                                   int alowOutOfRange = allowOutOfRange.isChecked() ? 1 : 0;
                                   int canChangPrice = checkBox_canChangePrice.isChecked() ? 1 : 0;
                                   int readDiscountFromoffer = readDiscount.isChecked() ? 1 : 0;
                                   int workOnlin = workOnline.isChecked() ? 1 : 0;
                                   int paymethodCheck = paymetod_check.isChecked() ? 1 : 0;
                                   int bonusNotalow = bonusNotAlowed.isChecked() ? 1 : 0;
                                   int noOffer_Credit = noOfferForCredit.isChecked() ? 1 : 0;
                                   int Customerauthorized = customerAuthor.isChecked() ? 1 : 0;
                                   int passordData = passowrdData_checkbox.isChecked() ? 1 : 0;
                                   int arabicLanguage = arabicLanguage_checkbox.isChecked() ? 1 : 0;
                                   int hideqty = hideQty_checkbox.isChecked() ? 1 : 0;
                                   int lockcashReport = lockcash_checkbox.isChecked() ? 1 : 0;
                                   int preventOrder = preventNew_checkbox.isChecked() ? 1 : 0;
                                   int requiredNote = note_checkbox.isChecked() ? 1 : 0;
                                   int totalDiscPrevent = ttotalDisc_checkbox.isChecked() ? 1 : 0;
                                   int automaticCheque = automaticCheck_checkbox.isChecked() ? 1 : 0;
                                   int tafqitCheckbox = tafqit_checkbox.isChecked() ? 1 : 0;
                                   int preventChangPay = preventChange_checkbox.isChecked() ? 1 : 0;
                                   int showCustlist = showCustomerList_checkbox.isChecked() ? 1 : 0;
                                   int noReturnInvoice = noReturn_checkbox.isChecked() ? 1 : 0;

                                   int workSerial = workSerial_checkbox.isChecked() ? 1 : 0;


                                   String salesmanname=salesmanNmae.getText().toString();
                                   mDbHandler.deleteAllSettings();
                                   mDbHandler.addSetting(link, taxKind, 504, invoice, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);
                                   mDbHandler.addSetting(link, taxKind, 506, return1, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);
                                   mDbHandler.addSetting(link, taxKind, 508, order, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);
                                   /*cash*/mDbHandler.addSetting(link, taxKind, 1, paymentCash, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);
                                   /*chequ*/mDbHandler.addSetting(link, taxKind, 4, paymentCheque, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);
                                   /*credit card*/mDbHandler.addSetting(link, taxKind, 2, paymentCredit, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial);

                                   finish();
                                   startActivity(getIntent());
                                   dialog.dismiss();
                               }
                               else
                               {
                                   Toast.makeText(MainActivity.this, "Number of copies must be maximum 4 !", Toast.LENGTH_SHORT).show();

                               }
//                           }
//                           else {
//                               Toast.makeText(MainActivity.this, "Invalid Serial Number", Toast.LENGTH_SHORT).show();
//                           }


                        }
                        else {
                            Toast.makeText(MainActivity.this, "Please enter All Enformation Filed", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else {
                        Toast.makeText(MainActivity.this, "Please enter IP address", Toast.LENGTH_SHORT).show();
//                        linkEditText.setError("Required");
                    }


                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            //  else
            //                               {
            //                             }

        }

    }

    @SuppressLint("SetTextI18n")
    public void openCompanyInfoDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.company_info_dialog);

        final EditText name = (EditText) dialog.findViewById(R.id.com_name);
        final EditText tel = (EditText) dialog.findViewById(R.id.com_tel);
        final EditText tax = (EditText) dialog.findViewById(R.id.tax_no);
        final EditText noteInvoice = (EditText) dialog.findViewById(R.id.notes);
        logo = (ImageView) dialog.findViewById(R.id.logo);

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

        if (mDbHandler.getAllCompanyInfo().size() != 0) {
            name.setText("" + mDbHandler.getAllCompanyInfo().get(0).getCompanyName());
            tel.setText("" + mDbHandler.getAllCompanyInfo().get(0).getcompanyTel());
            tax.setText("" + mDbHandler.getAllCompanyInfo().get(0).getTaxNo());
            logo.setImageDrawable(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
            noteInvoice.setText(""+mDbHandler.getAllCompanyInfo().get(0).getNoteForPrint());
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                intent.putExtra("crop", "false");
//                intent.putExtra("scale", true);
//                intent.putExtra("outputX", 256);
//                intent.putExtra("outputY", 256);
//                intent.putExtra("aspectX", 0);
//                intent.putExtra("aspectY", 0);
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, 1);

//                Intent intent = new Intent();
//                //******call android default gallery
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                //******code for crop image
//                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 0);
//                intent.putExtra("aspectY", 0);
//                try {
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(
//                            Intent.createChooser(intent,"Complete action using"),
//                            2);
//                } catch (ActivityNotFoundException e) {}

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !tel.getText().toString().equals("") && !tax.getText().toString().equals("")) {
                    String comName = name.getText().toString().trim();
                    int comTel = 0, taxNo = 0;
                    try {
                        comTel = Integer.parseInt(tel.getText().toString());
                        taxNo = Integer.parseInt(tax.getText().toString());
                        String companyNote = noteInvoice.getText().toString();

                        mDbHandler.deleteAllCompanyInfo();
                        mDbHandler.addCompanyInfo(comName, comTel, taxNo, itemBitmapPic, companyNote);

                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        if (comTel == 0) {
                            tel.setError("Invalid No");
                        }
                        if (taxNo == 0) {
                            tax.setError("Invalid Tax");
                        }
                    }


                } else
                    Toast.makeText(MainActivity.this, "Please ensure your inputs", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void openPrintSetting() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.printer_setting);

        final RadioButton lk30, lk32, lk31, qs,dotMatrix,MTPPrinter,normalnam,large_name;
        lk30 = (RadioButton) dialog.findViewById(R.id.LK30);
        lk31 = (RadioButton) dialog.findViewById(R.id.LK31);

        lk32 = (RadioButton) dialog.findViewById(R.id.LK32);
        qs = (RadioButton) dialog.findViewById(R.id.QS);

        dotMatrix=(RadioButton) dialog.findViewById(R.id.dotMatrix);
        MTPPrinter=(RadioButton) dialog.findViewById(R.id.MTP);
        Button save = (Button) dialog.findViewById(R.id.save);
        normalnam=(RadioButton) dialog.findViewById(R.id.radioButton_normalnam);
        large_name=(RadioButton) dialog.findViewById(R.id.radioButton_large_name);
        List<PrinterSetting> printer = mDbHandler.getPrinterSetting_();
//        Log.e("printer_Seting",""+printer.get(0).getPrinterName()+"   "+printer.get(0).getPrinterShape());
if(printer.size()!=0) {
    switch (printer.get(0).getPrinterName()) {
        case 0:
            lk30.setChecked(true);
            break;
        case 1:
            lk31.setChecked(true);
            break;
        case 2:
            lk32.setChecked(true);
            break;
        case 3:
            qs.setChecked(true);
            break;
        case 4:
            dotMatrix.setChecked(true);
            break;
        case 5:
            MTPPrinter.setChecked(true);
            break;

    }

    switch (printer.get(0).getPrinterShape()){
        case 0:
            normalnam.setChecked(true);
            break;
        case 1:
            large_name.setChecked(true);
            break;
    }
}else {
    lk30.setChecked(true);
    normalnam.setChecked(true);
}

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHandler.deleteAllPrinterSetting();
                PrinterSetting printerSetting = new PrinterSetting();

                if(normalnam.isChecked()){
                    printerSetting.setPrinterShape(0);
                }else  if(large_name.isChecked()){
                    printerSetting.setPrinterShape(1);
                }

                if (lk30.isChecked()) {
                    printerSetting.setPrinterName(0);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk30");
                } else if (lk31.isChecked()) {
                    printerSetting.setPrinterName(1);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk31");
                } else if (lk32.isChecked()) {
                    printerSetting.setPrinterName(2);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk32");
                } else if (qs.isChecked()) {
                    printerSetting.setPrinterName(3);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "qs");
                }else if (dotMatrix.isChecked()) {
                    printerSetting.setPrinterName(4);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "dotMatrix");
                }else if (MTPPrinter.isChecked()) {
                printerSetting.setPrinterName(5);
                mDbHandler.addPrinterSeting(printerSetting);
                Log.e("click ", "mtp");
            }
dialog.dismiss();
            }

        });


        dialog.show();


    }


    public void openDeExportDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.de_export_dialog);

        final EditText from_date = (EditText) dialog.findViewById(R.id.from_date);
        final EditText to_date = (EditText) dialog.findViewById(R.id.to_date);
        final RadioGroup exportTerm = (RadioGroup) dialog.findViewById(R.id.export_term);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        from_date.setText(convertToEnglish(today));
        to_date.setText(convertToEnglish(today));

        myCalendar = Calendar.getInstance();
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(from_date), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(to_date), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirm Update")
                            .setMessage("Are you sure you want to post data ? This will take few minutes !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    int flag;

                                    if (exportTerm.getCheckedRadioButtonId() == R.id.invoiceRadioButton)
                                        flag = 0;
                                    else if (exportTerm.getCheckedRadioButtonId() == R.id.paymentRadioButton)
                                        flag = 1;
                                    else
                                        flag = 2;

                                    DeExportJason obj = new DeExportJason(MainActivity.this, from_date.getText().toString(),
                                            to_date.getText().toString(), flag);

                                    obj.startExportDatabase();
                                    //obj.storeInDatabase();

                                }
                            })
                            .setNegativeButton("Cancel", null).show();

                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please select date !", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final EditText editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }


    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                itemBitmapPic = bitmap;
                logo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                visitPic = extras.getParcelable("data");
                visitPicture.setImageDrawable(new BitmapDrawable(getResources(), visitPic));
            }
        }

        //************************************************************
        Log.e("MainActivity", ""+requestCode);
//        if (requestCode == 0x0000c0de) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.e("MainActivity", "cancelled scan");
                Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("MainActivity", "" + Result.getContents());
//                    Toast.makeText(this, "Scan ___" + Result.getContents(), Toast.LENGTH_SHORT).show();
//                TostMesage(getResources().getString(R.string.scan)+Result.getContents());
//                barCodTextTemp.setText(Result.getContents() + "");
//                openEditerCheck();

                String serialBarcode = Result.getContents();


            }
        }
    }

    private void openCheckInDialog() {
        RelativeLayout myLayout = new RelativeLayout(this);
        TextView enterCustTextView = new TextView(this);
        enterCustTextView.setText(getResources().getString(R.string.app_enter_cust_code));
        EditText custCodeEditText = new EditText(this);
        Button confirmButton = new Button(this);
        confirmButton.setText(getResources().getString(R.string.app_Confirm));
        Button searchButton = new Button(this);
        searchButton.setText(getResources().getText(R.string.app_search));
        myLayout.setPadding(50, 40, 50, 10);
        enterCustTextView.setId('1');
        custCodeEditText.setId('2');
        confirmButton.setId('3');
        searchButton.setId('4');

        RelativeLayout.LayoutParams textViewDetails =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        textViewDetails.addRule(RelativeLayout.ALIGN_LEFT);

        RelativeLayout.LayoutParams editDetails =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        editDetails.addRule(RelativeLayout.ALIGN_LEFT);

        editDetails.addRule(RelativeLayout.BELOW, enterCustTextView.getId());

        RelativeLayout.LayoutParams buttonDetails1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        buttonDetails1.addRule(RelativeLayout.ALIGN_RIGHT);
        buttonDetails1.addRule(RelativeLayout.ALIGN_BOTTOM);
        buttonDetails1.addRule(RelativeLayout.BELOW, custCodeEditText.getId());
        buttonDetails1.addRule(RelativeLayout.RIGHT_OF, searchButton.getId());

        RelativeLayout.LayoutParams buttonDetails2 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        buttonDetails2.addRule(RelativeLayout.ALIGN_RIGHT);
        buttonDetails2.addRule(RelativeLayout.ALIGN_BOTTOM);
        buttonDetails2.addRule(RelativeLayout.BELOW, custCodeEditText.getId());
        //buttonDetails2.addRule(RelativeLayout.LEFT_OF, confirmButton.getId());

        myLayout.addView(enterCustTextView, textViewDetails);
        myLayout.addView(custCodeEditText, editDetails);
        myLayout.addView(confirmButton, buttonDetails1);
        myLayout.addView(searchButton, buttonDetails2);

        AlertDialog.Builder enterCustDialog = new AlertDialog.Builder(this);
        enterCustDialog.setTitle(getResources().getString(R.string.app_select_cust));
        enterCustDialog.setCancelable(false);
        enterCustDialog.setNegativeButton("Cancel", null);
        enterCustDialog.setView(myLayout);
        enterCustDialog.create().show();
    }//openCheckInDialog

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    @TargetApi(16)
    public void requestSingleUpdate() {
        // TODO: Comment-out this line.
        // Looper.prepare();

        Log.e("requestSingleUpdate",""+android.os.Build.VERSION.SDK_INT);
        // only works with SDK Version 23 or higher
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // permission is not granted
                Log.e("SiSoLocProvider", "Permission not granted.");
                return;
            } else {
                Log.e("SiSoLocProvider", "Permission granted.");
            }
        } else {
            Log.e("SiSoLocProvider", "SDK < 23, checking permissions should not be necessary");
        }

        final long startTime = System.currentTimeMillis();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // TODO: These lines of code will run on UI thread.
                if ((locationResult.getLastLocation() != null) && (System.currentTimeMillis() <= startTime + 30 * 1000)) {
                    Log.e("LOCATION: " , locationResult.getLastLocation().getLatitude() + "|" + locationResult.getLastLocation().getLongitude());
                    Log.e("ACCURACY: " , ""+locationResult.getLastLocation().getAccuracy());
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                } else {
                    Log.e("LastKnownNull? :: " ,""+ (locationResult.getLastLocation() == null));
                    Log.e("Time over? :: " ,""+ (System.currentTimeMillis() > startTime + 30 * 1000));
                }

                // TODO: After receiving location result, remove the listener.
                mFusedLocationClient.removeLocationUpdates(this);
            }
        };

        LocationRequest req = new LocationRequest();
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        req.setFastestInterval(2000);
        req.setInterval(2000);
        // Receive location result on UI thread.
        mFusedLocationClient.requestLocationUpdates(req, mLocationCallback, Looper.getMainLooper());
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void copyFile()
    {
        try
        {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File data = Environment.getDataDirectory();
            boolean isPresent = true;
            if (!sd.canWrite())
            {
                isPresent= sd.mkdir();

            }



                String backupDBPath = "VanSalesDatabase_backup";

                File currentDB= getApplicationContext().getDatabasePath("VanSalesDatabase");
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()&&isPresent) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(MainActivity.this, "Backup Succesfulley", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(MainActivity.this, "Backup Failed", Toast.LENGTH_SHORT).show();
                }
            isPresent=false;


        }
        catch (Exception e) {
            Log.e("Settings Backup", e.getMessage());
        }
    }
}
