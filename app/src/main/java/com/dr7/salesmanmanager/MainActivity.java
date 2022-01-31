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
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Flag_Settings;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.Settings;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.CustomerCheckInFragment.settext1;
import static com.dr7.salesmanmanager.LocationPermissionRequest.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.CustomerListShow.customerNameTextView;

import static com.dr7.salesmanmanager.Login.contextG;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.passwordSettingAdmin;
import static com.dr7.salesmanmanager.Login.rawahneh;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.Login.updateOnlySelectedCustomer;
import static com.dr7.salesmanmanager.Login.userNo;

public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
        CustomerCheckInFragment.CustomerCheckInInterface, CustomerListShow.CustomerListShow_interface {
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    RadioGroup radioGroup;
    private static final String TAG = "MainActivity";
    public static String    CusId;
    public static int menuItemState;
    String typeImport="";
    int  approveAdmin=-1,workOnLine=-1;
    public  static  EditText passwordFromAdmin, password ;
    static public TextView mainTextView,timeTextView,salesmanPlanRespon,getplan;
    LinearLayout checkInLinearLayout, checkOutLinearLayout;
    public static ImageView checkInImageView, checkOutImageView;
    static int checknum;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private DatabaseHandler mDbHandler;
    private static DatabaseHandler databaseHandler;
     public   LocationManager locationManager;
    LocationListener locationListener;

    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;

  public String text;
    int position=0;
    public  static  double latitude_main, longitude_main;
    boolean isPosted = true,isPostedCustomerMaster=true;
    public  static  int OrderTypeFlage;
    public static final int PICK_IMAGE = 1;
    Bitmap itemBitmapPic = null;
    boolean getLocationComp=false;
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
    public static ArrayList<SalesManPlan>DB_salesManPlanList = new ArrayList<>();
    public static ArrayList<AddedCustomer>customerArrayList = new ArrayList<>();

    int sum_chech_export_lists=0;
    static public Date currentTimeAndDate;
    static public SimpleDateFormat df, df2;
    static public String curentDate, curentTime;

     DrawerLayout drawer_layout;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    private FusedLocationProviderClient fusedLocationClient;
    public  static CustomerLocation customerLocation_main;
    public  static Location location_main;
    public  int first=0,isClickLocation=0;
    public  static  double latitudeCheckIn=0,longtudeCheckIn=0;
    LinearLayout checkInCheckOutLinear;
    public  static int time=30;
    Timer timer;
    LocationPermissionRequest locationPermissionRequest;
    Transaction transactionRealTime;
    boolean customerCheckInOk=false;

    List<Settings>settingsList;
    int NoLocationAsk=0;

    public  static TextView masterControlLoc;
    String ipAddress="";
    NavigationView navigationView;






    public static void settext2() {
        mainTextView.setText(CustomerListShow.Customer_Name);
        if(!CustomerListShow.Customer_Name.contains("No Customer"))
        {
            setTimeText();
            settext1();
        }

    }

    private static void setTimeText() {
        currentTimeAndDate = Calendar.getInstance().getTime();
        df2 = new SimpleDateFormat("hh:mm:ss");
        curentTime=df2.format(currentTimeAndDate);
        timeTextView.setText(curentTime);
        df= new SimpleDateFormat("dd/MM/yyyy");
        curentDate = df.format(currentTimeAndDate);


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




    //////////


    protected LocationManager locationManager1;
    protected LocationListener locationListener1;

    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;



    ////////







    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(MainActivity.this);
        mDbHandler = new DatabaseHandler(MainActivity.this);
        setContentView(R.layout.activity_main);






     try {
saveCurentLocation();

         databaseHandler = new DatabaseHandler(  MainActivity.this);

         databaseHandler.getSalsmanLoc();
         Log.e(" DatabaseHandler.SalmnLat",""+ DatabaseHandler.SalmnLat+"");
         if(  DatabaseHandler.SalmnLat==null && DatabaseHandler.SalmnLong==null) {

             databaseHandler. setSalsemanLocation(latitudeCheckIn + "", longtudeCheckIn + "");
         }
         else if(DatabaseHandler.SalmnLat.equals("") && DatabaseHandler.SalmnLong.equals(""))
             databaseHandler.setSalsemanLocation(latitudeCheckIn + "", longtudeCheckIn + "");






        }
      catch (Exception e){

          Log.e("Exception:",e.getMessage() );

      }
        hideItem();

//////////////// salesman plan for cake shop
        salesmanPlanRespon=findViewById(R.id.   salesmanPlanRespon);
    //    getplan=findViewById(R.id.     getplan);



     //   if(Login.SalsManPlanFlage!=1)getplan.setVisibility(View.GONE);

//            getplan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Login.SalsManPlanFlage==1) {
//                    int salesMan = 1;
//                    try {
//                        salesMan = Integer.parseInt(Login.salesMan);
//                    } catch (NumberFormatException e) {
//                        Log.e("NumberFormatException", "" + e.getMessage());
//                        salesMan = 1;
//                    }
//                    ImportJason obj = new ImportJason(MainActivity.this);
//
//                    obj.getSalesmanPlan(salesMan);
//                }
//            }
//        });






        salesmanPlanRespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (s.toString().equals("fill")) {

                        getSalesmanPlan(MainActivity.this);
                        //

                        if(IsDateInLocalDatabase())
                        {        // case when salesman get plan more than one time in same date
                            Log.e("changscase","changscase");


                            // update logoutstatus based on old plan in import list

                          for(int i=0;i<DB_salesManPlanList.size();i++) {
                              for (int j = 0; j < ImportJason.salesManPlanList.size(); j++)
                                  if (DB_salesManPlanList.get(i).getCustNumber().
                                          equals(ImportJason.salesManPlanList.get(j).getCustNumber())

                                  &&DB_salesManPlanList.get(i).getDate().
                                          equals(ImportJason.salesManPlanList.get(j).getDate()
                                  )) {
                                      ImportJason.salesManPlanList.get(j).setLogoutStatus(DB_salesManPlanList.get(i).getLogoutStatus());
                                      Log.e("changscase", DB_salesManPlanList.get(i).getCustNumber()+"   " + DB_salesManPlanList.get(i).getLogoutStatus());
                                  }
                          }

                                  //delete plane

                            Date currentTimeAndDate = Calendar.getInstance().getTime();
                            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                            String currentTime = convertToEnglish(tf.format(currentTimeAndDate));
                            String currentDate = convertToEnglish(df.format(currentTimeAndDate));
                            mDbHandler.deleteFromSalesMan_Plan(convertToEnglish(currentDate));

                            // update logoutstatus based on old plan
                            for (int i = 0; i < ImportJason.salesManPlanList.size(); i++) {
                                Log.e("detalis===",  ImportJason.salesManPlanList.get(i).getCustNumber()+"   " +  ImportJason.salesManPlanList.get(i).getLogoutStatus());

                                mDbHandler.addSalesmanPlan(ImportJason.salesManPlanList.get(i));

                            }
                            getSalesmanPlan(MainActivity.this);

                        } else { // case when salesman get plan in new  date

                            Log.e("normalcase","normalcase");
                            for (int i = 0; i < ImportJason.salesManPlanList.size(); i++) {

                                mDbHandler.addSalesmanPlan(ImportJason.salesManPlanList.get(i));

                            }
                            getSalesmanPlan(MainActivity.this);
                        }


                    }
                }
            }
        });
        if(Login.SalsManPlanFlage==1)
        {getSalesmanPlan(MainActivity.this);
            Log.e("eee==",""+ DB_salesManPlanList.size());
        }

        Log.e(" DB_salesManPlanList==",""+ DB_salesManPlanList.size());




     //////////////   end




        radioGroup=findViewById(R.id.radioGrp);
        checkInCheckOutLinear=findViewById(R.id.checkInCheckOutLinear);
        timeTextView=findViewById(R.id.timeTextView);
        Log.e("curentTimeMain",""+curentTime);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            checkInCheckOutLinear.setVisibility(View.GONE);
            //Do some stuff
        }
        else {
            checkInCheckOutLinear.setVisibility(View.VISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Login.salesMan=mDbHandler.getAllUserNo();
        drawer_layout=findViewById(R.id.drawer_layout);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        first=1;
        locationPermissionRequest=new LocationPermissionRequest(MainActivity.this);
        TextView textTimer = (TextView)findViewById(R.id.timerTextView);
        masterControlLoc=findViewById(R.id.masterControlLoc);

        settingsList= mDbHandler.getAllSettings();
        try {
            approveAdmin=settingsList.get(0).getApproveAdmin();
        }catch (Exception e){
            approveAdmin=0;
        }

        if(approveAdmin==1) {
            boolean locCheck = locationPermissionRequest.checkLocationPermission();

            Log.e("LocationIn", "Main1" + locCheck);

        }


        masterControlLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(masterControlLoc.getText().toString().equals("2")) {
//                    locationOPen();
                    masterControlLoc.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        isClickLocation=1;
        try {
            if(mDbHandler.getAllSettings().get(0).getAllowOutOfRange()==1)
            {
//                if(isNetworkAvailable())
//                {
//                    getlocationForCheckIn();
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
        catch ( Exception e)
        {
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
//        checkInImageView = (ImageView) findViewById(R.id.checkInImageView);
//        checkOutImageView = (ImageView) findViewById(R.id.checkOutImageView);
//        if (!CustomerListShow.Customer_Name.equals("No Customer Selected !"))//test after change language
//        {
//            checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
//            checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
//        }

        checkInLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    customerCheckInOk=true;
//                  checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in));

                    if(approveAdmin==1) {
                        boolean locCheck = locationPermissionRequest.checkLocationPermission();

                        Log.e("LocationIn", "GoToMain" + locCheck);
                        if (locCheck) {
                            customerCheckInDialog();
                        }else{
                           // customerCheckInDialog();
                        }
                    }else {
                        customerCheckInDialog();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in_hover));
                }
                return true;
            }
        });

        checkOutLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
                    if (!CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
                        openCustCheckOut();
                    } else {
                        Toast.makeText(MainActivity.this, "No Customer Selected !", Toast.LENGTH_SHORT).show();
//                        checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out_hover));
                }
                return true;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

//                   String priceListNo= mDbHandler.getPriceListNoMaster(convertToEnglish(curentDate));
//                   Log.e("curentDate",""+curentDate+"\t"+priceListNo);
                    openAddCustomerDialog();
                }
                catch (Exception e)
                {

                    Toast.makeText(MainActivity.this, "Check Location permission", Toast.LENGTH_SHORT).show();
                }


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
//        locationPermissionRequest=new LocationPermissionRequest(MainActivity.this);
//        locationPermissionRequest.timerLocation();


      //  getLocation();
    }

    private void openReadBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(SmallCaptureActivity.class);
            integrator.initiateScan();
//        new IntentIntegrator(MainActivity.this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    void customerCheckInDialog(){
        if (CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
            checknum = 1;
            menuItemState = 1;
            openSelectCustDialog();
        } else {
            Toast.makeText(MainActivity.this, CustomerListShow.Customer_Name + " is checked in", Toast.LENGTH_SHORT).show();
//                        checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
        }
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
                    latitudeCheckIn = 0;
                    longtudeCheckIn = 0;
                    latitudeCheckIn = location.getLatitude();
                    longtudeCheckIn = location.getLongitude();
                    Log.e("onLocationChanged", "" + latitudeCheckIn + "" + longtudeCheckIn);


                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged",""+provider.toString()+status+"\t extras"+extras.toString());

                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled",""+provider.toString());
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled",""+provider.toString());

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
//            locationPermissionRequest.closeLocation();
            finish();
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
            if(passwordSettingAdmin==1)// 1 ----> exist admin
            {

                            openPasswordDialog(10);// from admin
            }else {
                openPasswordDialog(1);
            }



        } else if (id == R.id.action_print_voucher) {
            Intent intent = new Intent(MainActivity.this, PrintVoucher.class);
            startActivity(intent);

        } else if (id == R.id.action_print_payment) {
            Intent intent = new Intent(MainActivity.this, PrintPayment.class);
            startActivity(intent);

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

        try {
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
        }catch (Exception e){

        }


    }
    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
          //  Log.e("onLocationResult",""+locationResult);
          //  Log.e("onLocationResultEn",""+convertToEnglish(locationResult+""));
            if(getLocationComp)
            {
                for (Location location : locationResult.getLocations()) {
                    Log.e("MainActivity", "getLocationComp: " + location.getLatitude() + " " + location.getLongitude());
                    if (mDbHandler.getAllCompanyInfo().size() != 0) {
                        if (mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany() == 0) {
                            latitude_main = location.getLatitude();
                            longitude_main = location.getLongitude();

                            Log.e("updatecompanyInfo", "" + mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany());
                            mDbHandler.updatecompanyInfo(latitude_main, longitude_main);
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getResources().getString(R.string.succsesful))
                                    .setContentText(getResources().getString(R.string.LocationSaved))
                                    .show();


                        }
                    }
                    else{

                    }




                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);



                }
                getLocationComp=false;
            }
            else {
                if(CustomerListShow.Customer_Account.equals("")&& isClickLocation == 2)
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


                        if(!latitude.equals("")&&!longitude.equals("")&&isClickLocation==2&&!latitude.equals("0")&&!longitude.equals("0"))
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
                                    Handler h2 = new Handler(Looper.getMainLooper());
                                    h2.post(new Runnable() {
                                        public void run() {

                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText(getResources().getString(R.string.succsesful))
                                                    .setContentText(getResources().getString(R.string.LocationSaved))
                                                    .show();
                                        }


                                    });



                                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);



                                }

                            }

                        }

                    }
//            else {
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText(getResources().getString(R.string.warning_message))
//                        .setContentText(getResources().getString(R.string.enternetConnection))
//                        .show();
//            }


                }// END ELSE
                isClickLocation=1;
            }


        };

    };

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
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
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Location", "granted");
                    Log.e("LocationIn","GoToMain 1");
                    if(customerCheckInOk){
                        customerCheckInDialog();
                        customerCheckInOk=false;
                    }

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(MainActivity                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  .this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location", "granted updates");
                        Log.e("LocationIn","GoToMain 2");

                    }

                } else {
                    Log.e("LocationIn","GoToMain 3");
                    Log.e("Location", "Deny");
                    // permission, denied, boo! Disable the
                    // functionality that depends on this permission.

//                    NoLocationAsk++;
//                    if(NoLocationAsk!=1) {
//                        locationPermissionRequest.checkLocationPermission();
//                    }
                }
                break;
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
    private void hideItem()
    {



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if(Login.SalsManPlanFlage!=1)     nav_Menu.findItem(R.id.getplan).setVisible(false);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id == R.id.getplan){
           if(Login.SalsManPlanFlage==1) {
               int salesMan = 1;
               try {
                   salesMan = Integer.parseInt(Login.salesMan.trim());
               } catch (NumberFormatException e) {
                   Log.e("NumberFormatException", "" + e.getMessage());
                   salesMan = 1;
               }
               ImportJason obj = new ImportJason(MainActivity.this);

               obj.getSalesmanPlan(salesMan);

           }else{

           }

       }else

        if (id == R.id.nav_activities) {
//            locationPermissionRequest.closeLocation();
            Intent intent = new Intent(this, Activities.class);
            startActivity(intent);

        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(this, Reports.class);
            startActivity(intent);

        } else if (id == R.id.nav_exp_data) {
//            locationPermissionRequest.closeLocation();
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to post data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ExportJason obj = null;
                            try {
                                obj = new ExportJason(MainActivity.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("sumExport", "" + sum_chech_export_lists);
                            if (mDbHandler.getAllSettings().get(0).getPassowrd_data() == 1) {
                                openPasswordDialog(6);
                            } else {
                                isPosted = mDbHandler.isAllposted();
                              //  Log.e("isPostedExport","1"+isPosted);
                                if (!isPosted) {

                                  //  Log.e("isPostedExport","2"+isPosted);

                                try {
//                                    obj.startExportDatabase();
                                    obj.startExport(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else {
                                    obj.saveVouchersAndExport();
                                }




//                                else {
//                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saveSuccessfuly), Toast.LENGTH_SHORT).show();
//                                }


                        }

                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        }
        else if (id == R.id.shelf_inventory) {
            if(!CustomerListShow.Customer_Name.equals("") && !CustomerListShow.Customer_Name.equals("No Customer Selected !") )
            {
                finish();
                Intent i=new Intent(MainActivity.this,Stock_Activity.class);
                i.putExtra("serial","read");
                startActivity(i);
            }else {
                SweetAlertDialog sweetMessage= new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);

                sweetMessage.setTitleText(getResources().getString(R.string.app_select_customer));
                sweetMessage .setConfirmText("Ok");
                sweetMessage.setCanceledOnTouchOutside(true);
                sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetMessage.dismissWithAnimation();
                    }
                })

                        .show();
            }


        }
//        else
//            if (id == R.id.customers_location) {
////            locationPermissionRequest.closeLocation();
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.sales_man_map) {
////            locationPermissionRequest.closeLocation();
//            Intent intent = new Intent(this, SalesmanMap.class);
//            startActivity(intent);
//
//        }

//                else{
//
//                        LocaleAppUtils.setLocale(new Locale("en"));
//                        LocaleAppUtils.setConfigChange(MainActivity.this);
//                        finish();
//                        startActivity(getIntent());
//                }



        else if (id == R.id.nav_imp_data) {
            Log.e("nav_imp_data","nav_imp_data");
         //   locationPermissionRequest.closeLocation();
//            locationPermissionRequest.closeLocation();
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
                                            Log.e("getAllSettings",""+mDbHandler.getAllSettings().get(0).getReadOfferFromAdmin());
                                            if(mDbHandler.getAllSettings().get(0).getReadOfferFromAdmin()==1)
                                            {
                                                ImportJason obj = new ImportJason(MainActivity.this);
//                                                obj.getPriceFromAdmin();
                                                obj.startParsing("");
                                            }
                                            else {
                                                ImportJason obj = new ImportJason(MainActivity.this);
                                                obj.startParsing("");
                                            }

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
//            locationPermissionRequest.closeLocation();
//            new AlertDialog.Builder(this)
//                    .setTitle("Confirm Update")
//                    .setMessage("Are you sure you want to refresh data ? This will take few minutes !")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {

            try {
                RefreshData obj = new RefreshData(MainActivity.this);
                obj.startParsing();
            }
            catch (Exception e)
            {
                Log.e("RefreshData",""+e.getMessage());
            }

            //obj.storeInDatabase();

//                        }
//                    })
//                    .setNegativeButton("Cancel", null).show();

        }
        //        else if (id == R.id.nav_sign_out) {
////            locationPermissionRequest.closeLocation();
////            Intent intent = new Intent(this, CPCL2Menu.class);
////            startActivity(intent);
//
//        }
        else if (id == R.id.nav_clear_local) {
//            locationPermissionRequest.closeLocation();

            passwordDataClearDialog();


        }
        else if (id == R.id.nav_unCollectedchecked) {
          //  locationPermissionRequest.closeLocation();
            finish();
          Intent in=new Intent(MainActivity.this,UnCollectedData.class);
          in.putExtra("type","1");
          startActivity(in);
            Log.e("nav_unCollectedchecked","nav_unCollectedchecked");


        }


        else if (id == R.id.nav_backup_data) {
//            locationPermissionRequest.closeLocation();

            try {
                verifyStoragePermissions(MainActivity.this);
                copyFile();
            }
            catch (Exception e)
            {verifyStoragePermissions(MainActivity.this);


                Toast.makeText(this, ""+getResources().getString(R.string.backup_failed), Toast.LENGTH_SHORT).show();
            }

        }

        else if (id == R.id.nav_stock) {
//            locationPermissionRequest.closeLocation();
           finish();
           Intent i=new Intent(MainActivity.this,Stock_Activity.class);
            i.putExtra("serial","stock");
           startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void passwordDataClearDialog() {
        final EditText editText = new EditText(MainActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.enter_password));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(editText.getText().toString().equals("2021000"))
                {
                    mDbHandler.deleteAllPostedData();
                    sweetAlertDialog.dismissWithAnimation();
                    openEditSettingSerialVoucher();
                }
                else {
                    editText.setError("Incorrect");
                }
            }
        })

                .show();
    }
    int lastVoucherNo=0;
    private void openEditSettingSerialVoucher() {
        isPosted=true;
        lastVoucherNo = mDbHandler.getMaxSerialNumber(504);
      //  isPosted = mDbHandler.isAllVoucher_posted();
        Log.e("openEditSettingSerial", "" + lastVoucherNo);
        final EditText editText = new EditText(MainActivity.this);
        editText.setText(lastVoucherNo + "");
        if (isPosted == false) {
            editText.setEnabled(false);
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.warning_message))
                    .setContentText(getResources().getString(R.string.toChangeSerialVoucherNumberExportData))
                    .show();
        } else {


        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SweetAlertDialog sweetMessage = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.lastVoucherNumber));
        sweetMessage.setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (!editText.getText().toString().equals("")) {
                    if (isPosted == false) {
                        sweetAlertDialog.dismissWithAnimation();
                        Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.thereisUnExportingData), Toast.LENGTH_SHORT).show();
                    } else {
                        if (editText.getText().toString().trim().equals(lastVoucherNo + "")) {
                            sweetAlertDialog.dismissWithAnimation();

                        } else {
                            try {
                                lastVoucherNo = Integer.parseInt(editText.getText().toString().trim());
                                mDbHandler.setMaxSerialNumber(504, lastVoucherNo);
                                mDbHandler.updateVoucherNo(lastVoucherNo,504,1);
                                Log.e("openEditSettingSerial", "" + lastVoucherNo);
                                sweetAlertDialog.dismissWithAnimation();
                            } catch (Exception e) {
                                editText.setError("invalid");
                            }


                        }

                    }

                } else {
                    editText.setError("*Required");
                }

            }
        })

                .show();
    }

    }
//
//      if(getTypeImport().equals("1"))
//    {
////                                               ImportJason obj = new ImportJason(MainActivity.this);
////                                               obj.setFlagImport();
//    }
//                                           else {
//        ImportJason obj = new ImportJason(MainActivity.this);
//        obj.startParsing();
//    }

    private String getTypeImport() {
         typeImport="0";
        SweetAlertDialog sweetMessage= new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.itemImportfromadmin));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                typeImport="1";
            }
        }).setCancelButton(getResources().getString(R.string.no), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                typeImport="2";
            }
        })

                .show();
        return  typeImport;
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
        CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment(MainActivity.this);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openAddCustomerDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_customer_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();


        final EditText addCus = (EditText) dialog.findViewById(R.id.custEditText);
        final EditText remark = (EditText) dialog.findViewById(R.id.remarkEditText);
        final EditText address = (EditText) dialog.findViewById(R.id.addressEditText);
        final EditText telephone = (EditText) dialog.findViewById(R.id.phoneEditText);
        final EditText contactPerson = (EditText) dialog.findViewById(R.id.person_contactEditText);

        Button done = (Button) dialog.findViewById(R.id.doneButton);
         RadioGroup paymentTermRadioGroup=dialog.findViewById(R.id.paymentTermRadioGroup);
         LinearLayout   linear = dialog.findViewById(R.id.linear);
        try {
            if (languagelocalApp.equals("ar")) {
                linear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    linear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch (Exception e){
            linear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);}


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            dialog.dismiss();
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
                    int payMethod=0;
                    mDbHandler.addAddedCustomer(new AddedCustomer(addCus.getText().toString(), remark.getText().toString(),
                            latitude_main, longitude_main, Login.salesMan, Login.salesMan,0,address.getText().toString(),telephone.getText().toString(),contactPerson.getText().toString()));
                  dialog.dismiss();
                  reFreshCustomerList();
//                    String customerId=getCustomerId();
//                    if(!customerId.equals(""))
//                    {
//                       int idRadioGroup= paymentTermRadioGroup.getCheckedRadioButtonId();
//                       if(idRadioGroup==R.id.cashRadioButton)
//                       {
//                           payMethod=1;
//                       }
//                       else {    payMethod=0;
//                       }
//                        mDbHandler.addCustomer(new Customer(123,customerId,addCus.getText().toString(),address.getText().toString(),0,"0",0,"1",0,payMethod,latitude_main+"",longitude_main+"",0.0,"0",0,0,customerId+""));
//                        dialog.dismiss();
//                    }

                } else
                {
                    addCus.setError(getResources().getString(R.string.reqired_filled));
                    Toast.makeText(MainActivity.this, "Please add customer name", Toast.LENGTH_SHORT).show();

                }

            }
        });


        dialog.show();
    }

    private void reFreshCustomerList() {
        try {


        List<Settings> settings = mDbHandler.getAllSettings();
        if (settings.size() != 0) {

//            if (isInternetAccessed()) {
                try {
                    if (typaImport == 0)//mysql
                    {
                       // new CustomerListShow.JSONTask().execute(URL_TO_HIT);
                    } else {
                        if (typaImport == 1)//IIOs
                        {
                            ExportJason importJason =new ExportJason(MainActivity.this);
                            importJason.startExportCustomer();
                        }
                    }



                } catch (Exception e) {
                    Log.e("updateCustomer", "" + e.getMessage());
                }

//            } else {
//                Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
//            }

        }
    }catch (Exception e){

        }

    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each: all) if (!set.add(each)) return true;
        return false;
    }
//    public static <T> boolean hasDuplicateSerial(Iterable<serialModel> all) {
//        Set<String> set = new HashSet<>();
//        // Set#add returns false if the set does not change, which
//        // indicates that a duplicate element has been added.
//        for (serialModel each: all) if (!set.add(each.getSerialCode())) return true;
//        return false;
//    }


    private String getCustomerId() {
        String custId="120120120";
        new JSONTask_getCustomerId().execute();

        return  custId;
    }
    private class JSONTask_getCustomerId extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                String link = "";
                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('3'), "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String finalJson = sb.toString();
                Log.e("finalJson*********", finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
//                try {
//
//                    JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS_BALANCE");
//                    customerList.clear();
//                    for (int i = 0; i < parentArrayCustomers.length(); i++) {
//                        JSONObject finalObject = parentArrayCustomers.getJSONObject(i);
//                        Customer Customer = new Customer();
//                        Customer.setCustId(finalObject.getString("CUSTID"));
//                        Customer.setCashCredit(finalObject.getInt("CASHCREDIT"));
//                        Customer.setCreditLimit(finalObject.getDouble("CREDITLIMIT"));
//                        customerList.add(Customer);
//                    }
//                } catch (Exception e) {
//                    Log.e("Refresh_data", "" + e.getMessage().toString());
//                }



            } catch (MalformedURLException e) {
                Log.e("Refresh_data", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Refresh_data", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Refresh_data", "********ex3  " + e.toString());
                e.printStackTrace();
            } finally {
                Log.e("Refresh_data", "********finally");
                if (connection != null) {
                    Log.e("Refresh_data", "********ex4");
                    // connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();

            if (result != null) {
//                storeInDatabase();
            } else {
                Toast.makeText(MainActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
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
                setTimeText();

//                checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in));
//                checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));

                CustomerCheckInFragment obj = new CustomerCheckInFragment();
                obj.editCheckOutTimeAndDate(MainActivity.this);
                List<Settings> settings = mDbHandler.getAllSettings();
                if (settings.size() != 0) {
                    workOnLine= settings.get(0).getWorkOnline();
                    Log.e("workOnLine",""+workOnLine);
                }
                if(workOnLine==1) {

                    isPosted = mDbHandler.isAllposted();
                    Log.e("isPostedExport","1"+isPosted);
                    if (!isPosted) {

                        Log.e("isPostedExport","2"+isPosted);
                        ExportJason objJson = null;
                        try {
                            objJson = new ExportJason(MainActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
//                        objJson.startExportDatabase();
                            objJson.startExport(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.saveSuccessfuly), Toast.LENGTH_SHORT).show();
                    }




                }




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
    boolean validPassowrdSetting=false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openPasswordDialog(final int flag) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.password_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        validPassowrdSetting=false;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        passwordFromAdmin=dialog.findViewById(R.id.passwordFromAdmin);

        passwordFromAdmin.setText("");
        passwordFromAdmin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()!=0) {
                    if (flag == 10) {


                    if (passwordFromAdmin.getText().toString().equals("")) {
                        getPassword();
                    }
                    if ((password.getText().toString().trim().equals(passwordFromAdmin.getText().toString())) && (!password.getText().toString().equals(""))) {
                        dialog.dismiss();
                        openSetting alert = new openSetting();
                        alert.showDialog(MainActivity.this, "Error de conexión al servidor");
                    } else {
                        password.setError(getResources().getString(R.string.invalidPassword));

                    }
                }
                }

            }
        });
        LinearLayout mainLinear=dialog.findViewById(R.id.linearPassword);
        try{
            if(languagelocalApp.equals("ar"))
            {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        password = (EditText) dialog.findViewById(R.id.editText1);

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
                if(flag == 10)
                {

                        getPassword();
                }
                else {

                    if (password.getText().toString().equals("303090")&&flag != 10) {
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
                                    obj.startParsing("");
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

//                                obj.startExportDatabase();
                                obj.startExport(0);
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();

                            }

                        }
                    } else
                        Toast.makeText(MainActivity.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
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
    }
    private void getPassword() {
        Log.e("getPassword","getPassword");
        ExportJason exportData = null;
        try {
            exportData = new ExportJason(MainActivity.this);
            exportData.getPassowrdSetting();
        } catch (JSONException e) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    password.setError(null);
                    passwordFromAdmin.setText("2021000");
                }
            });
        }

    }
    public class openSetting {
        boolean validSerial = false, validReturn = false, validOrder = false;
        EditText linkEditText,ip_withPort, cono,numOfCopy, invoicEditText, returnEditText, orderEditText, paymentEditTextCash, paymentEditTextCheque, paymentEditTextCredit,
                salesmanNmae,valueTotalDiscount,storNo_text;
        RadioGroup taxCalc, printMethod;
        CheckBox checkBox, checkBox2;
        RadioButton bluetooth, wifi, exclude, include;
        CheckBox allowMinus, salesManCustomersOnly, minSalePrice, allowOutOfRange;
        CheckBox checkBox_canChangePrice, readDiscount, workOnline, paymetod_check, bonusNotAlowed, noOfferForCredit, customerAuthor,
                passowrdData_checkbox, arabicLanguage_checkbox, hideQty_checkbox, lockcash_checkbox, preventNew_checkbox, note_checkbox, ttotalDisc_checkbox, automaticCheck_checkbox, tafqit_checkbox, preventChange_checkbox,
                showCustomerList_checkbox, noReturn_checkbox, workSerial_checkbox,
                showItemImage_checkbox,approveAdmin_checkbox,asaveOnly_checkbox,showSolidQty_checkbox,offerFromAdmin_checkbox,checkQtyServer,dontShowTax_checkbox
                ,continousReading_checkbox,totalDiscount_checkbox,itemUnit_checkBox,dontDuplicateItems_checkbox,sumCurentQty_checkbox;
        Dialog dialog;
        LinearLayout linearSetting,linearStore;
        TextView editIp;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @SuppressLint("SetTextI18n")
        public void showDialog(Activity activity, String msg) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_setting);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setAttributes(lp);
            linearSetting = (LinearLayout) dialog.findViewById(R.id.linearSetting);
            try {
                if (languagelocalApp.equals("ar")) {
                    linearSetting.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                } else {
                    if (languagelocalApp.equals("en")) {
                        linearSetting.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }

                }
            }
            catch (Exception e){
                linearSetting.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            sumCurentQty_checkbox= (CheckBox) dialog.findViewById(R.id.sumCurentQty_checkbox);
            dontDuplicateItems_checkbox= (CheckBox) dialog.findViewById(R.id.dontDuplicateItems_checkbox);
            continousReading_checkbox = (CheckBox) dialog.findViewById(R.id.continousReading_checkbox);
            itemUnit_checkBox= (CheckBox) dialog.findViewById(R.id.itemUnit_checkbox);
            totalDiscount_checkbox = (CheckBox) dialog.findViewById(R.id.totalDiscount_checkbox);
            valueTotalDiscount = (EditText) dialog.findViewById(R.id.valueTotalDiscount);
            linearStore = dialog.findViewById(R.id.linearStore);
            storNo_text = (EditText) dialog.findViewById(R.id.storNo_text);
            if (makeOrders == 0) {
                linearStore.setVisibility(View.GONE);
            }
            editIp = (TextView) dialog.findViewById(R.id.editIp);
            ip_withPort = (EditText) dialog.findViewById(R.id.ip_withPort);
            cono = (EditText) dialog.findViewById(R.id.cono);

            linkEditText = (EditText) dialog.findViewById(R.id.link);
            numOfCopy = (EditText) dialog.findViewById(R.id.num_of_copy);
            invoicEditText = (EditText) dialog.findViewById(R.id.invoice_serial);
            returnEditText = (EditText) dialog.findViewById(R.id.return_serial);
            orderEditText = (EditText) dialog.findViewById(R.id.order_serial);
            paymentEditTextCash = (EditText) dialog.findViewById(R.id.payments_serial_cash);
            paymentEditTextCheque = (EditText) dialog.findViewById(R.id.payments_serial_cheque);
            paymentEditTextCredit = (EditText) dialog.findViewById(R.id.payments_serial_creditCard);
            salesmanNmae = (EditText) dialog.findViewById(R.id.salesman_name_text);
            taxCalc = (RadioGroup) dialog.findViewById(R.id.taxTalc);

            checkBox = (CheckBox) dialog.findViewById(R.id.price_by_cust);
            checkBox2 = (CheckBox) dialog.findViewById(R.id.use_weight_case);
            printMethod = (RadioGroup) dialog.findViewById(R.id.printMethod);
            bluetooth = (RadioButton) dialog.findViewById(R.id.bluetoothRadioButton);
            wifi = (RadioButton) dialog.findViewById(R.id.wifiRadioButton);
            allowMinus = (CheckBox) dialog.findViewById(R.id.allow_sale_with_minus);
            salesManCustomersOnly = (CheckBox) dialog.findViewById(R.id.salesman_customers_only);
            if(updateOnlySelectedCustomer==1){
                salesManCustomersOnly.setChecked(true);
                salesManCustomersOnly.setEnabled(false);
            }

            minSalePrice = (CheckBox) dialog.findViewById(R.id.min_sale_price);
            allowOutOfRange = (CheckBox) dialog.findViewById(R.id.allow_cust_check_out_range);
            exclude = (RadioButton) dialog.findViewById(R.id.excludeRadioButton);
            include = (RadioButton) dialog.findViewById(R.id.includeRadioButton);
            checkBox_canChangePrice = (CheckBox) dialog.findViewById(R.id.can_change_price);
            readDiscount = (CheckBox) dialog.findViewById(R.id.read_discount);
            workOnline = (CheckBox) dialog.findViewById(R.id.work_online);
             paymetod_check = (CheckBox) dialog.findViewById(R.id.checkBox_paymethod_check);
             bonusNotAlowed = (CheckBox) dialog.findViewById(R.id.checkBox_bonus_notallowed);
             noOfferForCredit        = (CheckBox) dialog.findViewById(R.id.checkBox_NoOffer_forCredit);
             customerAuthor          = (CheckBox) dialog.findViewById(R.id.CustomerAuthorize_checkbox);
             passowrdData_checkbox   = (CheckBox) dialog.findViewById(R.id.PassowrdData_checkbox);
             arabicLanguage_checkbox = (CheckBox) dialog.findViewById(R.id.ArabicLanguage_checkbox);
             hideQty_checkbox     = (CheckBox) dialog.findViewById(R.id.hideQty_checkbox);
             lockcash_checkbox    = (CheckBox) dialog.findViewById(R.id.lockcash_checkbox);
             preventNew_checkbox   = (CheckBox) dialog.findViewById(R.id.preventNewOrder_checkbox);
             note_checkbox         = (CheckBox) dialog.findViewById(R.id.note_checkbox);
             ttotalDisc_checkbox   = (CheckBox) dialog.findViewById(R.id.preventtotalDisc_checkbox);
             automaticCheck_checkbox   = (CheckBox) dialog.findViewById(R.id.automatic_cheque_checkbox);
             tafqit_checkbox            = (CheckBox) dialog.findViewById(R.id.tafqit_checkbox);
             preventChange_checkbox     = (CheckBox) dialog.findViewById(R.id.preventChangePay_checkbox);
             showCustomerList_checkbox = (CheckBox) dialog.findViewById(R.id.showCustomerList_checkbox);
             noReturn_checkbox            = (CheckBox) dialog.findViewById(R.id.noReturn_checkbox);
             workSerial_checkbox         = (CheckBox) dialog.findViewById(R.id.workSerial_checkbox);
             showItemImage_checkbox        = (CheckBox) dialog.findViewById(R.id.showItemImage_checkbox);
            approveAdmin_checkbox= (CheckBox) dialog.findViewById(R.id.approveAdmin_checkbox);
            asaveOnly_checkbox= (CheckBox) dialog.findViewById(R.id.asaveOnly_checkbox);
            showSolidQty_checkbox= (CheckBox) dialog.findViewById(R.id.showSolidQty_checkbox);
            offerFromAdmin_checkbox= (CheckBox) dialog.findViewById(R.id.offerFromAdmin_checkbox);
            checkQtyServer= (CheckBox) dialog.findViewById(R.id.qtyFromServer_checkbox);
            dontShowTax_checkbox= (CheckBox) dialog.findViewById(R.id.dontShowTax_checkbox);
            FloatingActionButton okBut_floatingAction=dialog.findViewById(R.id.okBut_floatingAction);
            okBut_floatingAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSetting();
                }
            });

            Button okButton = (Button) dialog.findViewById(R.id.okBut);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

            salesmanNmae.setEnabled(false);
            getSalesManName();
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
                try {
                    ip_withPort.setText("" + mDbHandler.getAllSettings().get(0).getIpPort());
                    cono.setText("" + mDbHandler.getAllSettings().get(0).getCoNo());
                }
                catch (Exception e){}

                linkEditText.setClickable(false);
                linkEditText.setEnabled(false);
                linkEditText.setAlpha(0.5f);
                numOfCopy.setText("" + mDbHandler.getAllSettings().get(0).getNumOfCopy());
                invoicEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(504) + 1));
                returnEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(506) + 1));
                orderEditText.setText("" + (mDbHandler.getMaxSerialNumberFromVoucherMaster(508) + 1));
                storNo_text.setText(""+mDbHandler.getAllSettings().get(0).getStoreNo());

                paymentEditTextCash.setText("" + (mDbHandler.getMaxSerialNumber(1) + 1));//test
                paymentEditTextCheque.setText("" + (mDbHandler.getMaxSerialNumber(4) + 1));
                try {
                    paymentEditTextCredit.setText("" + (mDbHandler.getMaxSerialNumber(2) + 1));
                }
                catch (Exception e)
                {
                    paymentEditTextCredit.setText("");

                }
//                try {
//                    salesmanNmae.setText(mDbHandler.getAllSettings().get(0).getSalesMan_name()+"");
//                }
//                catch (Exception e)
//                {
//
//                }



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
                if (mDbHandler.getAllSettings().get(0).getArabic_language() == 1)
                {
                    arabicLanguage_checkbox.setChecked(true);
                    languagelocalApp= "ar";
                    LocaleAppUtils.setLocale(new Locale("ar"));
                    LocaleAppUtils.setConfigChange(MainActivity.this);
                }
                else{
                    if(mDbHandler.getAllSettings().get(0).getArabic_language() == 0)
                    {
                        languagelocalApp= "en";
                        arabicLanguage_checkbox.setChecked(false);
                        LocaleAppUtils.setLocale(new Locale("en"));
                        LocaleAppUtils.setConfigChange(MainActivity.this);
                    }

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

                if (mDbHandler.getAllSettings().get(0).getShowItemImage() == 1) {
                    showItemImage_checkbox.setChecked(true);
                }
                else {
                    showItemImage_checkbox.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getApproveAdmin() == 1) {
                    approveAdmin_checkbox.setChecked(true);
                }
                else {
                    approveAdmin_checkbox.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getSaveOnly() == 1) {
                    asaveOnly_checkbox.setChecked(true);
                }
                else {
                    asaveOnly_checkbox.setChecked(false);
                }

                if (mDbHandler.getAllSettings().get(0).getShow_quantity_sold() == 1) {
                    showSolidQty_checkbox.setChecked(true);
                }
                else {
                    showSolidQty_checkbox.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getReadOfferFromAdmin() == 1) {
                    offerFromAdmin_checkbox.setChecked(true);
                }
                else {
                    offerFromAdmin_checkbox.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getQtyServer() == 1) {
                    checkQtyServer.setChecked(true);
                }
                else {
                    checkQtyServer.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getDontShowtax() == 1) {
                    dontShowTax_checkbox.setChecked(true);
                }
                else {
                    dontShowTax_checkbox.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1) {
                    workOnline.setChecked(true);
                }
                else {
                    workOnline.setChecked(false);
                }
                if (mDbHandler.getAllSettings().get(0).getContinusReading() == 1) {
                    continousReading_checkbox.setChecked(true);
                }
                else {
                    continousReading_checkbox.setChecked(false);
                }

                if (mDbHandler.getAllSettings().get(0).getActiveTotalDiscount() == 1) {
                    totalDiscount_checkbox.setChecked(true);
                    valueTotalDiscount.setText(mDbHandler.getAllSettings().get(0).getValueOfTotalDiscount()+"");
                }
                else {
                    totalDiscount_checkbox.setChecked(false);
                    valueTotalDiscount.setText(mDbHandler.getAllSettings().get(0).getValueOfTotalDiscount()+"");
                }

                if (mDbHandler.getAllSettings().get(0).getItemUnit() == 1) {
                    itemUnit_checkBox.setChecked(true);

                }
                else {
                    itemUnit_checkBox.setChecked(false);

                }
                if (mDbHandler.getAllSettings().get(0).getDontduplicateItem() == 1) {
                    dontDuplicateItems_checkbox.setChecked(true);

                }
                else {
                    dontDuplicateItems_checkbox.setChecked(false);

                }
                if (mDbHandler.getAllSettings().get(0).getSumCurrentQty() == 1) {
                    sumCurentQty_checkbox.setChecked(true);

                }
                else {
                    sumCurentQty_checkbox.setChecked(false);

                }








            }else {
                arabicLanguage_checkbox.setChecked(true);
                LocaleAppUtils.setLocale(new Locale("ar"));
                languagelocalApp="ar";
                showCustomerList_checkbox.setChecked(true);
                editIp.setVisibility(View.INVISIBLE);
            }
            editIp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPasswordDialog();


                }
            });

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
     saveSetting();


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

        private void getSalesManName() {
            String salesName=mDbHandler.getSalesmanName_fromSalesTeam();
            if(salesName.equals(""))
            {
                salesmanNmae.setEnabled(true);
            }
            else {
                salesmanNmae.setEnabled(false);
                salesmanNmae.setText(salesName);
            }


        }

        private void showPasswordDialog() {
            final EditText editText = new EditText(MainActivity.this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            SweetAlertDialog sweetMessage= new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);

            sweetMessage.setTitleText(getResources().getString(R.string.enter_password));
            sweetMessage .setConfirmText("Ok");
            sweetMessage.setCanceledOnTouchOutside(true);
            sweetMessage.setCustomView(editText);
            sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(editText.getText().toString().equals("2021000"))
                    {
                        linkEditText.setAlpha(1f);
                        linkEditText.setEnabled(true);
                        linkEditText.requestFocus();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                    else {
                        editText.setError("Incorrect");
                    }
                }
            })

                    .show();
        }

        private void saveSetting() {
            settext2();
            int numOfCopys=0,invoice=0,return1=0,order=0,paymentCash=0,paymentCheque=0,paymentCredit=0;
            String storeNo="1";

            if (!(linkEditText.getText().toString().equals(""))) {
                if ((!numOfCopy.getText().toString().equals("")) && !invoicEditText.getText().toString().equals("") && !returnEditText.getText().toString().equals("") &&
                        !orderEditText.getText().toString().equals("") && !paymentEditTextCash.getText().toString().equals("")
                        && !paymentEditTextCheque.getText().toString().equals("")
                        && !paymentEditTextCredit.getText().toString().equals("")) {

//                           if(validSerial&&validOrder&&validReturn)
//                           {
                    if (Integer.parseInt(numOfCopy.getText().toString()) < 5) {
                        String link = linkEditText.getText().toString().trim();
                        String linkIp = ip_withPort.getText().toString().trim();
                        String conoText = cono.getText().toString().trim();
                        try {
                            numOfCopys = Integer.parseInt(numOfCopy.getText().toString());
                            invoice = Integer.parseInt(invoicEditText.getText().toString()) - 1;
                            return1 = Integer.parseInt(returnEditText.getText().toString()) - 1;
                            order = Integer.parseInt(orderEditText.getText().toString()) - 1;
                            paymentCash = Integer.parseInt(paymentEditTextCash.getText().toString()) - 1;
                            paymentCheque = Integer.parseInt(paymentEditTextCheque.getText().toString()) - 1;
                            paymentCredit = Integer.parseInt(paymentEditTextCredit.getText().toString()) - 1;
                            storeNo=storNo_text.getText().toString().trim();
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
                        int showImage=showItemImage_checkbox.isChecked()?1:0;
                        int approveAdm=approveAdmin_checkbox.isChecked()?1:0;
                        int saveOnly=asaveOnly_checkbox.isChecked()?1:0;
                        int showsolidQty= showSolidQty_checkbox.isChecked()?1:0;
                        int offerAdmin= offerFromAdmin_checkbox.isChecked()?1:0;
                        int qtyServer=checkQtyServer.isChecked()?1:0;
                        int continousReading=continousReading_checkbox.isChecked()?1:0;
                        int activeTotalDisc=totalDiscount_checkbox.isChecked()?1:0;
                        int item_unit=itemUnit_checkBox.isChecked()?1:0;

                        int sumCurren_Qty=sumCurentQty_checkbox.isChecked()?1:0;
                        int dontDuplicat_Item=dontDuplicateItems_checkbox.isChecked()?1:0;

                        double valueOfTotDisc=0;
                        try {
                            valueOfTotDisc=Double.parseDouble(valueTotalDiscount.getText().toString().trim());
                        }catch (Exception e){
                            valueOfTotDisc=0;
                        }

                        Log.e("valueOfTotDisc","addSetting00"+valueOfTotDisc);


                        int showTax=dontShowTax_checkbox.isChecked()?1:0;

                        Log.e("showsolidQty",""+showsolidQty);


                        String salesmanname=salesmanNmae.getText().toString();
                        Log.e("salesmanname",""+salesmanname);
                        mDbHandler.deleteAllSettings();
                        mDbHandler.addSetting(link, taxKind,     504, invoice,     priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);
                        mDbHandler.addSetting(link, taxKind,     506, return1,     priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);
                        mDbHandler.addSetting(link, taxKind,     508, order,       priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);
                        /*cash*/mDbHandler.addSetting(link, taxKind  ,    1    ,    paymentCash, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);
                        /*chequ*/mDbHandler.addSetting(link, taxKind  ,     4,       paymentCheque, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);
                /*credit card*/mDbHandler.addSetting(link, taxKind   , 2,         paymentCredit, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage,hideqty,lockcashReport,salesmanname,preventOrder,requiredNote,totalDiscPrevent,automaticCheque,tafqitCheckbox,preventChangPay,showCustlist,noReturnInvoice,workSerial,showImage,approveAdm,saveOnly,showsolidQty,offerAdmin,linkIp,qtyServer,showTax,conoText,continousReading,activeTotalDisc,valueOfTotDisc,storeNo,item_unit,sumCurren_Qty,dontDuplicat_Item);


                        finish();
//                        locationPermissionRequest.closeLocation();
                        startActivity(getIntent());
                        dialog.dismiss();

                            stopService(new Intent(MainActivity.this, MyServices.class));

                            startService(new Intent(MainActivity.this, MyServices.class));

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
                linkEditText.setError("Required");
            }
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    public void openCompanyInfoDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.company_info_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());



        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGrp);
        RadioButton radioTop =  dialog.findViewById(R.id.radioTop);
        RadioButton radioBottom =  dialog.findViewById(R.id.radioBottom);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup,
                                         int radioButtonID) {
                switch(radioButtonID) {
                    case R.id.radioTop:
                        position=0;
                        break;
                    case R.id.radioBottom:
                        position=1;
                        break;

                }
            }
        });








        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        LinearLayout mainLinear=dialog.findViewById(R.id.linearCompany);
        try{
            if(languagelocalApp.equals("ar"))
            {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        getLocationComp=false;
        final EditText name = (EditText) dialog.findViewById(R.id.com_name);
        final EditText tel = (EditText) dialog.findViewById(R.id.com_tel);
        final EditText tax = (EditText) dialog.findViewById(R.id.tax_no);
        final EditText noteInvoice = (EditText) dialog.findViewById(R.id.notes);
        final  TextView savecompanyLocation=(TextView) dialog.findViewById(R.id.savecompanyLocation);
        savecompanyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isNetworkAvailable())
                    {
                        getLocationComp=true;
                        saveCurrentLocation();
                    }
                    else {
                        Log.e("isNetworkAvailable","NOT");
                        Toast.makeText(MainActivity.this, ""+getResources().getString(R.string.enternetConnection), Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        logo = (ImageView) dialog.findViewById(R.id.logo);

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);
        try {
            if (mDbHandler.getAllCompanyInfo().size() != 0) {
                name.setText("" + mDbHandler.getAllCompanyInfo().get(0).getCompanyName());
                tel.setText("" + mDbHandler.getAllCompanyInfo().get(0).getcompanyTel());
                tax.setText("" + mDbHandler.getAllCompanyInfo().get(0).getTaxNo());
//            logo.setImageDrawable(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
                logo.setBackground(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
                noteInvoice.setText(""+mDbHandler.getAllCompanyInfo().get(0).getNoteForPrint());
                if(mDbHandler.getAllCompanyInfo().get(0).getNotePosition().equals("1"))
                {
                    radioBottom.setChecked(true);
                }
                else {
                    radioTop.setChecked(true);
                }

            }
            else {
                tax.setText(0+"");
            }
        }catch ( Exception e){

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
                if (!name.getText().toString().equals("") && !tel.getText().toString().equals("") && !tax.getText().toString().equals(""))
                {
                    String comName = name.getText().toString().trim();
                    int comTel = 0, taxNo = 0;
                    try {
                        comTel = Integer.parseInt(tel.getText().toString());
                        taxNo = Integer.parseInt(tax.getText().toString());
                        String companyNote = noteInvoice.getText().toString();

                        mDbHandler.deleteAllCompanyInfo();
                        if(itemBitmapPic!=null)
                        {
                            itemBitmapPic = getResizedBitmap(itemBitmapPic, 150, 150);
                        }




                        mDbHandler.addCompanyInfo(comName, comTel, taxNo, itemBitmapPic, companyNote,0,0,position);
                        try {
                            if(isNetworkAvailable())
                            {
                                getLocationComp=true;
                                saveCurrentLocation();
                            }
                            else {
                                Log.e("isNetworkAvailable","NOT");
                                Toast.makeText(MainActivity.this, ""+getResources().getString(R.string.enternetConnection), Toast.LENGTH_SHORT).show();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        if (bm != null){
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        }
        return null;
    }

    private void saveCurentLocation() throws InterruptedException {
        Log.e("updatecompanyInfo_1",""+latitudeCheckIn+longtudeCheckIn);
        getlocationForCheckIn();
      //  Thread.sleep(2000);
        Log.e("updatecompanyInfo_2",""+latitudeCheckIn+longtudeCheckIn);
        if(latitudeCheckIn!=0 &&longtudeCheckIn!=0)
        {
            if(mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany()== 0.0)
            {
                Log.e("updatecompanyInfo",""+mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany());
                mDbHandler.updatecompanyInfo(latitudeCheckIn,longtudeCheckIn );

            }
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openPrintSetting() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.printer_setting);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        LinearLayout mainLinear=dialog.findViewById(R.id.mainLinear);
        try{
            if(languagelocalApp.equals("ar"))
            {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        final RadioButton lk30, lk32, lk31, qs,dotMatrix,MTPPrinter,normalnam,large_name,innerPrinter;
        CheckBox short_Invoice,dontPrintHeader,altayee_checkbox;

        short_Invoice=(CheckBox) dialog.findViewById(R.id.shortInvoice);
        altayee_checkbox=(CheckBox) dialog.findViewById(R.id.altayee_checkbox);
        dontPrintHeader=dialog.findViewById(R.id.dontPrintheader_checkbox);
        lk30 = (RadioButton) dialog.findViewById(R.id.LK30);
        lk31 = (RadioButton) dialog.findViewById(R.id.LK31);

        lk32 = (RadioButton) dialog.findViewById(R.id.LK32);
        qs = (RadioButton) dialog.findViewById(R.id.QS);
        innerPrinter= (RadioButton) dialog.findViewById(R.id.innerPrinter);
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
        case 6:
            innerPrinter.setChecked(true);
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
    Log.e("addPrinterSeting",""+printer.get(0).getShortInvoice());
    if(printer.get(0).getShortInvoice()==0)
    {
        short_Invoice.setChecked(false);
    }
    else { short_Invoice.setChecked(true);}
    if(printer.get(0).getDontPrintHeader()==0){
        dontPrintHeader.setChecked(false);
    }
    else dontPrintHeader.setChecked(true);
    if(printer.get(0).getTayeeLayout()==1)
    {
        altayee_checkbox.setChecked(true);

    }else    altayee_checkbox.setChecked(false);
}else {
    lk30.setChecked(true);
    normalnam.setChecked(true);
    short_Invoice.setChecked(false);
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

                    Log.e("click ", "lk30");
                } else if (lk31.isChecked()) {
                    printerSetting.setPrinterName(1);

                    Log.e("click ", "lk31");
                } else if (lk32.isChecked()) {
                    printerSetting.setPrinterName(2);

                    Log.e("click ", "lk32");
                } else if (qs.isChecked()) {
                    printerSetting.setPrinterName(3);

                    Log.e("click ", "qs");
                }else if (dotMatrix.isChecked()) {
                    printerSetting.setPrinterName(4);

                    Log.e("click ", "dotMatrix");
                }else if (MTPPrinter.isChecked()) {
                printerSetting.setPrinterName(5);

                Log.e("click ", "mtp");
            }
                else if (innerPrinter.isChecked()) {
                    printerSetting.setPrinterName(6);

                    Log.e("click ", "mtp");
                }
                if(short_Invoice.isChecked())
                {
                    printerSetting.setShortInvoice(1);

                }
                else {
                    printerSetting.setShortInvoice(0);

                }
                if(dontPrintHeader.isChecked())
                {
                    printerSetting.setDontPrintHeader(1);

                }
                else {
                    printerSetting.setDontPrintHeader(0);

                }
                if(altayee_checkbox.isChecked())
                {
                    printerSetting.setTayeeLayout(1);

                }
                else {
                    printerSetting.setTayeeLayout(0);

                }

                mDbHandler.addPrinterSeting(printerSetting);
               // Log.e("printerSetting ", "setShortInvoice\t"+printerSetting.getShortInvoice());
                dialog.dismiss();
            }

        });


        dialog.show();


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openDeExportDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.de_export_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        LinearLayout mainLinear=dialog.findViewById(R.id.mainLinear);
        try{
            if(languagelocalApp.equals("ar"))
            {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

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
                                   Date textFromDate= formate(from_date.getText().toString());
                                    Date textToDate= formate(to_date.getText().toString());

                                    DeExportJason obj = new DeExportJason(MainActivity.this,from_date.getText().toString().trim() ,
                                            to_date.getText().toString().trim() , flag);

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

    private Date formate(String dateString) {
        Log.e("formate",""+dateString);
        Date d = new Date();
        SimpleDateFormat sdf;
        String myFormat = "dd/MM/yyyy";
        try {
            //In which you need put here
            sdf = new SimpleDateFormat(myFormat, Locale.US);
            d = sdf.parse(dateString);
        }
        catch (Exception e)
        {Log.e("","");}
        Log.e("formate",""+d+"\t"+d.toString());

        return d;

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
    public void locationOPen(){
//        LocationPermissionRequest locationPermissionRequest=new LocationPermissionRequest(MainActivity.this);
//        locationPermissionRequest.timerLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("startVoiceInput2","requestCode"+requestCode+"\t"+data+"\t"+resultCode);
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
        if(requestCode== REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                customerNameTextView.setText(result.get(0));
                Log.e("startVoiceInput2","result="+result);
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

        switch (requestCode) {
            case 10001:
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();

                    openDialog=true;
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

                    openDialog=false;
                    break;
                default:
                    break;
            }
                break;
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
 static void  ReSortList(){


        try {


            if (DB_salesManPlanList.get(0).getTypeOrder() == 0) {
                Collections.sort(DB_salesManPlanList);
                OrderTypeFlage = 0;

            } else {
                for (int i = 0; i < DB_salesManPlanList.size(); i++) {
                    Location locationA = new Location("point A");

                    locationA.setLatitude(DB_salesManPlanList.get(i).getLatitud());
                    locationA.setLongitude(DB_salesManPlanList.get(i).getLongtude());

                    Location locationB = new Location("point B");
                    //     31.973113861570397, 35.909562515675 الداخلية


                    // جرش 32.271743106492224, 35.88992632707304
                    // اربد 32.569163163418864, 35.84655082984946
                    // 32.02355606374721, 35.84556662133978 صويلح
                    //     29.6133995179976, 35.02148227477434  aqaba

                    databaseHandler.getSalsmanLoc();
                    if (DatabaseHandler.SalmnLat != null && DatabaseHandler.SalmnLong != null
                            && DatabaseHandler.SalmnLat.equals("") && DatabaseHandler.SalmnLong.equals("")
                    ) {
                        locationB.setLatitude(Double.parseDouble(DatabaseHandler.SalmnLat));
                        locationB.setLongitude(Double.parseDouble(DatabaseHandler.SalmnLong));
                    } else {



                        databaseHandler.getSalsmanLoc();
                        Log.e(" DatabaseHandler.SalmnLat", "" + DatabaseHandler.SalmnLat + "");
                        if (DatabaseHandler.SalmnLat == null && DatabaseHandler.SalmnLong == null) {

                            databaseHandler.setSalsemanLocation(latitudeCheckIn + "", longtudeCheckIn + "");
                        } else if (DatabaseHandler.SalmnLat.equals("") && DatabaseHandler.SalmnLong.equals(""))
                            databaseHandler.setSalsemanLocation(latitudeCheckIn + "", longtudeCheckIn + "");


                    }


                    float distance = locationA.distanceTo(locationB);
                    DB_salesManPlanList.get(i).setDistance(distance);
                    OrderTypeFlage = 1;

                    Log.e("distance===", DB_salesManPlanList.get(i).getCustName() + "  " + DB_salesManPlanList.get(i).getLatitud() + "   " + DB_salesManPlanList.get(i).getLongtude() + "    " + DB_salesManPlanList.get(i).getDistance() + "");


                }
                Collections.sort(DB_salesManPlanList, new Comparator<SalesManPlan>() {
                    @Override
                    public int compare(SalesManPlan c1, SalesManPlan c2) {
                        return Double.compare(c1.getDistance(), c2.getDistance());
                    }
                });
                for (int x = 0; x < DB_salesManPlanList.size(); x++)
                    Log.e("DB_salesManPlan===", DB_salesManPlanList.get(x).getCustName() + "       " + DB_salesManPlanList.get(x).getDistance());

            }

        }catch (Exception e){

        }
         }

    static  void getSalesmanPlan(Context context)   {
        currentTimeAndDate = Calendar.getInstance().getTime();
        df2 = new SimpleDateFormat("hh:mm:ss");
        curentTime=df2.format(currentTimeAndDate);
        df= new SimpleDateFormat("dd/MM/yyyy");
        curentDate = df.format(currentTimeAndDate);
        databaseHandler = new DatabaseHandler(  context);
        DB_salesManPlanList.clear();
                DB_salesManPlanList =  databaseHandler .getSalesmanPlan(curentDate);
     if(DB_salesManPlanList.size()!=0)
         ReSortList();
     else
         {

         }

    }
  boolean  IsDateInLocalDatabase(){
        boolean f=false;


        for(int i=0;i<DB_salesManPlanList.size();i++)
            if (DB_salesManPlanList.get(i).getDate().
                    equals(convertToEnglish(curentDate)))
            { f=true;
            break;
            }


    return f;}
 void   getLocation(){

     customerArrayList =databaseHandler. getAllCustomer();
    }
}
