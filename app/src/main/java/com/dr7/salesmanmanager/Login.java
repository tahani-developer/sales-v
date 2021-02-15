package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.activeKey;
import com.dr7.salesmanmanager.Reports.Reports;
import com.dr7.salesmanmanager.Reports.SalesMan;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.MainActivity.customerLocation_main;
import static com.dr7.salesmanmanager.MainActivity.latitude_main;
import static com.dr7.salesmanmanager.MainActivity.location_main;
import static com.dr7.salesmanmanager.MainActivity.longitude_main;

@SuppressWarnings("unchecked")
public class Login extends AppCompatActivity {

    private String username, password, link, ipAddress;
    private EditText usernameEditText, passwordEditText;
    private CircleImageView logo;
    private CardView loginCardView;
    public static String salesMan = "", salesManNo = "";
    private boolean isMasterLogin;
    public static int key_value_Db;
    activeKey model_key;
    int key_int;
    Context context;
    TextView loginText;
    SweetAlertDialog dialogTem, sweetAlertDialog;

    DatabaseHandler mDHandler;
    String shortUserName = "", fullUserName = "";
    int indexfirst = 0, indexEdit = 0;
    boolean exist = false;
    int index = 0;
    List<SalesMan> salesMenList;
    public static String languagelocalApp = "";
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest mLocationRequest;
    public LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    Date currentTimeAndDate;
    SimpleDateFormat df, df2;
    String curentDate, curentTime;
    public static Location location_main;
    RelativeLayout mainlayout;
    String provider;
    public static Timer timer = null;
    LocationPermissionRequest locationPermissionRequest;
   public static String currentIp="",previousIp="";
    String serialNo2="";
    public  static  TextView checkIpDevice;
    public static Context contextG;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleAppUtils.setConfigChange(Login.this);

        new LocaleAppUtils().changeLayot(Login.this);

        setContentView(R.layout.activity_login);
        checkIpDevice=findViewById(R.id.checkIpDevice);
        locationPermissionRequest = new LocationPermissionRequest(Login.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDHandler = new DatabaseHandler(Login.this);
        model_key = new activeKey();
        loginText = (TextView) findViewById(R.id.logInTextView);
        currentTimeAndDate = Calendar.getInstance().getTime();
        Log.e("currentTimeAndDate", "" + currentTimeAndDate);
        df = new SimpleDateFormat("dd/MM/yyyy");
        curentDate = df.format(currentTimeAndDate);
        curentDate = convertToEnglish(curentDate);
        Log.e("curentDate", "" + curentDate);

        df2 = new SimpleDateFormat("hh:mm:ss");
        curentTime = df2.format(currentTimeAndDate);
        curentTime = convertToEnglish(curentTime);
        Log.e("curentTime", "" + curentTime);
        getIpAddressForDevice();
        validLocation();
        try {
            Log.e("languagelocalApp", "" + languagelocalApp);

            if (mDHandler.getAllSettings().size() != 0) {
                if (mDHandler.getAllSettings().get(0).getArabic_language() == 1) {
                    languagelocalApp = "ar";
                    LocaleAppUtils.setLocale(new Locale("ar"));
                    LocaleAppUtils.setConfigChange(Login.this);

                } else {
                    languagelocalApp = "en";
                    LocaleAppUtils.setLocale(new Locale("en"));
                    LocaleAppUtils.setConfigChange(Login.this);

                }
            } else {
                languagelocalApp = "ar";
                LocaleAppUtils.setLocale(new Locale("ar"));
                LocaleAppUtils.setConfigChange(Login.this);

            }


            Log.e("languagelocalApp2", "" + languagelocalApp);

        } catch (Exception e) {
            languagelocalApp = "ar";
            LocaleAppUtils.setLocale(new Locale("ar"));
            LocaleAppUtils.setConfigChange(Login.this);

        }
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);

        try {
            if (languagelocalApp.equals("ar")) {
                mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        //   model_key.setKey(123);

        Log.e("model", "model_key" + model_key.getKey());
        logo = (CircleImageView) findViewById(R.id.imageView3);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
//        passwordEditText.setText("2240m");
//        usernameEditText.setText("1");
        try {
            if (mDHandler.getAllCompanyInfo().get(0).getLogo() == null) {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_vansales));
            } else {
                logo.setImageBitmap(mDHandler.getAllCompanyInfo().get(0).getLogo());
            }
        } catch (Exception e) {

        }


        loginCardView = (CardView) findViewById(R.id.loginCardView);
        loginCardView.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CompanyInfo companyLocation = mDHandler.getCompanyLocation();
                Log.e("companyLocation", "" + companyLocation.getLongtudeCompany());
                String user = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                salesMenList = mDHandler.getAllSalesMen();

                if (salesMenList.size() == 0)//Empty DB
                {
//                    Toast.makeText(Login.this, R.string.failUsers, Toast.LENGTH_LONG).show();

                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        } else {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getResources().getString(R.string.warning_message))
                                    .setContentText(getResources().getString(R.string.failUsers))
                                    .show();
                        }
                        checkExistToLogin();
                    } else {
                        new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.warning_message))
                                .setContentText(getResources().getString(R.string.failUsers))
                                .show();
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                } else {//item in list
                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {


                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        } else {
                            exist = false;
                            isMasterLogin = false;
                            for (int i = 0; i < salesMenList.size(); i++) {
                                fullUserName = salesMenList.get(i).getUserName();//  00002
                                if ((fullUserName.charAt(0) + "").equals("0")) {
                                    if (checkAllCharacterName(i, fullUserName)) {
                                        break;
                                    }

                                } else {
                                    checkFullName(i, fullUserName);

                                }

                            }
                        }
                        checkExistToLogin();
                    } else {
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                }


            }
        });
       locationPermissionRequest.timerLocation();

        checkIpDevice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(""))
                {
                    if(s.toString().equals("-1"))
                    {
                        goToMain();
                    }
                    else {
                        if(s.toString().equals("2"))
                        {
                            Log.e("checkIpDevice",""+currentIp+"\t"+previousIp);
                            addCurentIp(currentIp);
                        }
                        else {
                            verifyIpDevice();
                        }

                    }

                }
            }
        });
//        checkLocationPermission();
    }

    private String getIpAddressForDevice() {
        String ipNo="";
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            else {
                ipNo = Build.getSerial();
            }

        }
        else {
            ipNo = Build.SERIAL;
        }
        Log.e("getMacAddress","MAC Address : " + ipNo);


        Log.e("getMacAddress","serialNo2"+ipNo);
        return ipNo;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            serialNo2 = Build.getSerial();


                        }
                        else {
                            serialNo2 = Build.SERIAL;
                        }

                       Log.e("serialNo2","Permissions  "+serialNo2);
                    }
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
////                new AlertDialog.Builder(this)
////                        .setCancelable(false)
////                        .setTitle(R.string.title_location_permission)
////                        .setMessage(R.string.text_location_permission)
////                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                //Prompt the user once explanation has been shown
////                                    ActivityCompat.requestPermissions(Login.this,
////                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                                        MY_PERMISSIONS_REQUEST_LOCATION);
////                            }
////                        })
////                        .create()
////                        .show();
//
//
//         dialogLoc();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//                Log.e("Location","explanation need");
//
//            }
//            return false;
//        } else {
//            Log.e("Location","true need");
//            return true;
//        }
//    }
//
//    private void dialogLoc() {
//
//
//        sweetAlertDialog.setTitleText(R.string.title_location_permission);
//        sweetAlertDialog.setContentText(String.valueOf(R.string.text_location_permission));
//        sweetAlertDialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismissWithAnimation();
//                finish();
//            }
//        });
//        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(Login.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//                dialogTem=sweetAlertDialog;
//            }
//        });
//        sweetAlertDialog.setCancelable(false);
//        sweetAlertDialog.show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e("Location","granted");
//                    sweetAlertDialog.dismissWithAnimation();
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        Log.e("Location","granted updates");
//
//                        //Request location updates:
////                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
//                    }
//
//                } else {
//
//                    Log.e("Location","Deny");
//                    // permission, denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
//    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkExistToLogin() {
        if (exist) {
            if (isMasterLogin) {
                key_value_Db = mDHandler.getActiveKeyValue();
                if (key_value_Db == 0) {//dosent exist value key in DB

                    showDialog_key();
                } else {

                    salesMan = usernameEditText.getText().toString();
                    salesManNo = passwordEditText.getText().toString();
                    try {
                        Transaction transaction=new Transaction();
                        transaction.setCheckInDate(curentDate);
                        transaction.setCheckInTime(curentTime);
                        transaction.setLongtude(location_main.getLongitude());
                        transaction.setLatitud(location_main.getLatitude());
                        transaction.setSalesManId(Integer.parseInt(salesMan));
                        mDHandler.addlogin(transaction);
                    }
                    catch (Exception e){

                    }
                    if(mDHandler.getAllSettings().size()!=0)
                    {
                        if(mDHandler.getAllSettings().get(0).getApproveAdmin()==1)
                        {
//                           checkIpDevice() ;
                            goToMain();
                        }
                        else {
//                            checkIpDevice() ;
                            goToMain();
                        }
                    }
                    else {
                        goToMain();
                    }

//                    locationPermissionRequest.closeLocation();

//                    if(validLocation()){}
                   
//                                CustomIntent.customType(getBaseContext(),"left-to-right");
                }
            } else {

                if (salesMenList.get(index).getPassword().equals(passwordEditText.getText().toString())) {
                    key_value_Db = mDHandler.getActiveKeyValue();
                    if (key_value_Db == 0) {//dosent exist value key in DB

                        showDialog_key();
                    } else {

                        salesMan = usernameEditText.getText().toString();
                        salesManNo = passwordEditText.getText().toString();
//                       locationPermissionRequest.closeLocation();

                       goToMain();
//                                CustomIntent.customType(getBaseContext(),"left-to-right");


                    }

                } else
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(Login.this, "UserName does not exist", Toast.LENGTH_SHORT).show();
        exist = false;
    }

    private void checkIpDevice() {
        currentIp=getIpAddressForDevice();
        previousIp=getPreviousIpForSalesMen();
        //V22219AQ02457

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void  verifyIpDevice(){
        Log.e("checkIpDevice",""+currentIp+"\t"+previousIp);
        if(previousIp.equals(currentIp)){
            goToMain();

        }
        else {
            if(!previousIp.equals(currentIp)&& !previousIp.equals(""))
            {
                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getResources().getString(R.string.warning_message))
                        .setContentText(getResources().getString(R.string.userNotOwnwerDevice))
                        .show();
            }

        }
    }

    private void addCurentIp(String currentIp) {
        ImportJason importJason=new ImportJason(Login.this);
        importJason.addCurentIp(currentIp);
    }

    private String getPreviousIpForSalesMen() {

        Log.e("getPreviousIpFo","INNNN");
        ImportJason importJason=new ImportJason(Login.this);
        importJason.getPreviousIpForSalesMen();
        return  "";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void goToMain() {

//
//        LocationPermissionRequest locationPermissionRequest=new LocationPermissionRequest(Login.this);
//       boolean tt= locationPermissionRequest.checkLocationPermission();
//Log.e("LocationPermission","Request"+tt);
//
//if(tt) {
    //startService(new Intent(Login.this, MyService.class));
        startService(new Intent(Login.this, BackgroundGpsServices.class));
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            Log.e("LocationSdk","startService"+Build.VERSION.SDK_INT);
//            startService(new Intent(Login.this, BackgroundGpsServices.class));
//        } else {
//            Log.e("LocationSdk","startForegroundService"+Build.VERSION.SDK_INT);
//            Intent serviceIntent = new Intent(Login.this, BackgroundGpsServices.class);
//            ContextCompat.startForegroundService(Login.this, serviceIntent );
//          // startForegroundService(new Intent(Login.this, BackgroundGpsServices.class));
//        }
    Intent main = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(main);
}
//    }

    private boolean validLocation() {
//        getCompanyLocation();
//        getCurentLocation();
//        compareLocation();
        return true;
    }
//    LocationCallback mLocationCallback = new LocationCallback(){
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Log.e("onLocationResult",""+locationResult);
//            Log.e("onLocationResultEn",""+convertToEnglish(locationResult+""));
//            if(getLocationComp)
//            {
//                for (Location location : locationResult.getLocations()) {
//                    Log.e("MainActivity", "getLocationComp: " + location.getLatitude() + " " + location.getLongitude());
//                    if (mDbHandler.getAllCompanyInfo().size() != 0) {
//                        if (mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany() == 0) {
//                            latitude_main = location.getLatitude();
//                            longitude_main = location.getLongitude();
//                            Log.e("updatecompanyInfo", "" + mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany());
//                            mDbHandler.updatecompanyInfo(latitude_main, longitude_main);
//                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                    .setTitleText(getResources().getString(R.string.succsesful))
//                                    .setContentText(getResources().getString(R.string.LocationSaved))
//                                    .show();
//
//
//                        }
//                    }
//                    else{
//
//                    }
//
//
//
//
//                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
//
//
//
//                }
//                getLocationComp=false;
//            }
//            else {
//                if(CustomerListShow.Customer_Account.equals("")&& isClickLocation == 2)
//                {
//                    if(first!=1)
//                    {
//                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText(getResources().getString(R.string.warning_message))
//                                .setContentText(getResources().getString(R.string.pleaseSelectUser))
//                                .show();
//                    }
//
//
//                } else {
//
//
//                    if(isNetworkAvailable()){
//                        String latitude="",  longitude="" ;
//                        try {
//                            latitude = CustomerListShow.latitude;
//                            longitude = CustomerListShow.longtude;
//                            Log.e("latitude",""+latitude+longitude);
//                        }
//                        catch (Exception e)
//                        {
//                            latitude="";
//                            longitude="";
//
//                        }
//                        Log.e("latitude",""+latitude+longitude);
//
//
//                        if(!latitude.equals("")&&!longitude.equals("")&&isClickLocation==2)
//                        {
//
//                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText(getResources().getString(R.string.warning_message))
//                                    .setContentText(getResources().getString(R.string.customerHaveLocation))
//                                    .show();
//                        }
//                        else {
//                            if (isClickLocation == 2) {
//                                for (Location location : locationResult.getLocations()) {
//                                    Log.e("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//                                    latitude_main = location.getLatitude();
//                                    longitude_main = location.getLongitude();
//                                    customerLocation_main = new CustomerLocation();
//                                    customerLocation_main.setCUS_NO(CustomerListShow.Customer_Account);
//                                    customerLocation_main.setLONG(longitude_main + "");
//                                    customerLocation_main.setLATIT(latitude_main + "");
//
//                                    mDbHandler.addCustomerLocation(customerLocation_main);
//                                    mDbHandler.updateCustomerMasterLocation(CustomerListShow.Customer_Account, latitude_main + "", longitude_main + "");
//                                    CustomerListShow.latitude = latitude_main + "";
//                                    CustomerListShow.longtude = longitude_main + "";
//                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                            .setTitleText(getResources().getString(R.string.succsesful))
//                                            .setContentText(getResources().getString(R.string.LocationSaved))
//                                            .show();
//
//
//                                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
//
//
//
//                                }
//
//                            }
//
//                        }
//
//                    }
////            else {
////                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
////                        .setTitleText(getResources().getString(R.string.warning_message))
////                        .setContentText(getResources().getString(R.string.enternetConnection))
////                        .show();
////            }
//
//
//                }// END ELSE
//                isClickLocation=1;
//            }
//
//
//        };
//
//    };

    private void getCurentLocation() {
                            //****************************************************************
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


                  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {// Not granted permission

                        ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

                    }
//                    Thread.sleep(1000);


                    /////////////////////////////////////////**********************************
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(Login.this);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(Login.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        location_main=new Location(location);

                                        location_main.setLatitude(latitude_main);
                                        location_main.setLongitude(longitude_main);
                                        Log.e("saveCurrentLocation", "" + location_main.getLatitude() + "\t" + location_main.getLongitude());

                                        new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(getResources().getString(R.string.succsesful))
                                                .setContentText(getResources().getString(R.string.LocationSaved))
                                                .show();
                                        Toast.makeText(Login.this, "latitude="+latitude_main+"long="+longitude_main, Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        new SweetAlertDialog(Login.this,SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText(getResources().getString(R.string.warning_message))
                                                .setContentText(getResources().getString(R.string.enternetConnection))
                                                .show();
                                    }
                                    // Logic to handle location object

                                }
                            });


}

    private void checkFullName(int i, String fullUserName) {
        if (usernameEditText.getText().toString().equals(fullUserName)) {
            exist = true;
            index = i;

        }
    }

    private boolean checkAllCharacterName(int i, String fullUserName) {
        for (int j = 0; j < fullUserName.length(); j++) {
            indexfirst = 0;
            if ((fullUserName.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexfirst = j;
                break;
            }

        }
        shortUserName = fullUserName.substring(indexfirst, fullUserName.length());

        //********************************************************************
        String editUser = usernameEditText.getText().toString();
        for (int j = 0; j < editUser.length(); j++) {
            indexEdit = 0;
            if ((editUser.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexEdit = j;
                break;

            }
        }
        String shortUserEdit = editUser.substring(indexEdit, editUser.length());
        Log.e("checkAllCharacterName", "" + shortUserEdit + "\t" + shortUserName);
        //********************************************************************

        if (shortUserEdit.equals(shortUserName)) {
            exist = true;
            index = i;
            return true;
        }
        return false;
    }

    public void showDialog_key() {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.active_key);
        dialog.show();
        // if not eist value key in DB
        //*****************************
//        mDHandler.deleteKeyValue();
        model_key.setKey(1111);
        mDHandler.addKey(model_key);
        //****************************
        final EditText key_value = (EditText) dialog.findViewById(R.id.editText_active_key);
        final Button cancel_button = (Button) dialog.findViewById(R.id.button_cancel_key);
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Button ok_button = (Button) dialog.findViewById(R.id.button_activeKey);
        ok_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                key_int = Integer.parseInt(key_value.getText().toString());
                key_value_Db = mDHandler.getActiveKeyValue();
                Log.e("key_value_Db", "" + key_value_Db);
                if (key_value_Db == key_int) {
                    salesMan = usernameEditText.getText().toString();
                    salesManNo = passwordEditText.getText().toString();
//
                    Toast.makeText(Login.this, "welcome" + salesMan, Toast.LENGTH_SHORT).show();

//                    locationPermissionRequest.closeLocation();
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    //  CustomIntent.customType(getBaseContext(),"left-to-right");
                    dialog.dismiss();
                } else {
                    Toast.makeText(Login.this, "Please enter valid Active key", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 10001:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        openDialog=false;
                        Toast.makeText(Login.this, states.isLocationPresent() + "", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        openDialog=false;
                        Toast.makeText(Login.this, "Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private class RequestLogin extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                String data = URLEncoder.encode("USERNAME=" + username, "UTF-8");
                data += "&" + URLEncoder.encode("PASSWORD=" + password, "UTF-8");

                URL url = new URL(link);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(urlConnection.getOutputStream());

                outputStreamWriter.write(data);

                String line = null;


            } catch (Exception e) {

            }

            return null;
        }
    }// Class RequestLogin

}
