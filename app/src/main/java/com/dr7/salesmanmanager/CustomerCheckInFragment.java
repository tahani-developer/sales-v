package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.dr7.salesmanmanager.Interface.DaoRequsts;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

import static com.dr7.salesmanmanager.Login.Plan_Kind;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

import static com.dr7.salesmanmanager.MainActivity.dayOfWeek;
import static com.dr7.salesmanmanager.MainActivity.latitudeCheckIn;
import static com.dr7.salesmanmanager.MainActivity.longtudeCheckIn;
import static com.dr7.salesmanmanager.MyServices.checkOutLat;
import static com.dr7.salesmanmanager.MyServices.checkOutLong;

//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCheckInFragment extends DialogFragment {
    private static final String TAG = "CustomerCheckInFragment";
    static TextView customerNameTextView, Customer_Name;
    ImageButton findButton;
    Button cancelButton, okButton;
    static TextView Customer_Account;
    public static String customernametest;
    public static int checkOutIn;
    double lat, lon;
    double currentLat, currentLon;
    JSONObject obj;
    private ProgressDialog progressDialog;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    static String cusCode;
    String cusName;
    int status;
    String today;
    Customer custObj = null;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;

    private static DatabaseHandler mDbHandler;
    CountDownTimer countDownTimer;

    public Context context;
    LinearLayout discLayout;
    int  approveAdmin=-1;
    int requstIndix=0;
    public interface CustomerCheckInInterface {
        public void showCustomersList();

        public void displayCustomerListShow();
    }

    CustomerCheckInInterface customerCheckInListener;

    public CustomerCheckInFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public CustomerCheckInFragment(Context cont) {
        this.context=cont;
        // Required empty public constructor
    }

    public static void settext1() {
        Customer_Name.setText(CustomerListShow.Customer_Name.toString());
        Customer_Account.setText(CustomerListShow.Customer_Account.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
//        new LocaleAppUtils().changeLayot(context);

        View view = inflater.inflate(R.layout.fragment_customer_check_in, container, false);
        //selectButton = (ImageButton) view.findViewById(R.id.check_img_button);
        //checkButton = (ImageButton) view.findViewById(R.id.check_img_button);
        discLayout = (LinearLayout) view.findViewById(R.id.discLayout);

        try {
            if (languagelocalApp.equals("ar"))
            {
                discLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else
            {
                if (languagelocalApp.equals("en")) {
                    discLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch (Exception e){
            discLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        mDbHandler = new DatabaseHandler(getActivity());
//        if(mDbHandler.getAllSettings().get(0).getAllowOutOfRange()==1)// validate customer location
//        {
////            getCurrentLocation();
//        }


        findButton = (ImageButton) view.findViewById(R.id.find_img_button);
        Customer_Name = (TextView) view.findViewById(R.id.checkInCustomerName);
        Customer_Account = (TextView) view.findViewById(R.id.customerAccountNo);


        Customer_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    customerCheckInListener.displayCustomerListShow();
                }
                catch (Exception e){}

            }
        });
        customernametest = CustomerListShow.Customer_Name.toString();

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        today = df.format(currentTimeAndDate);
        today = convertToEnglish(today);



        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Customer_Account.getText().toString().equals(""))) {

                    cusCode = Customer_Account.getText().toString();

                    cusName = Customer_Name.getText().toString();
                    status = 0;

                    List<Customer> customers = mDbHandler.getAllCustomers();

                    for (int i = 0; i < customers.size(); i++) {
                        if (customers.get(i).getCustId().equals(cusCode)) {
                            custObj = customers.get(i);
                            break;
                        }
                    }


                    if (custObj != null) {

                        List<SalesmanStations> stations = new DatabaseHandler(getActivity()).getAllSalesmanSatation(Login.salesMan, today);
                        boolean inRoot = false;
                        for (int i = 0; i < stations.size(); i++) {
                            if (stations.get(i).getCustNo().equals(cusCode)) {
                                inRoot = true;
                                break;
                            }
                        }
                        Log.e("getAllowOutOfRange",""+new DatabaseHandler(getActivity()).getAllSettings().get(0).getAllowOutOfRange());
//                        if(inRoot) { 55555555555

                        //1. getlocation from location tabel
                        String lat="",longat="";
                        CustomerLocation customerLocation=new DatabaseHandler(getActivity()).getCustomerLocationBYNUMBER(custObj.getCustId());
                        lat= customerLocation.getLATIT();
                        longat= customerLocation.getLONG();
                        //2. getlocation from master tabel if not in  location tabel
                        if(lat==null||longat==null) {

                            lat = custObj.getCustLat();
                            longat = custObj.getCustLong();
                        }
                        //3. if customer have not location
                        if(lat==null||longat==null)
                        {
                            lat = "0";
                            longat ="0";
                        }else if(lat.equals("")||longat.equals("")){
                            lat = "0";
                            longat ="0";
                        }

                        if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 0
                                    ||
                                    isInRange(lat,longat)
                            ) {

                                MainActivity mainActivity = new MainActivity();
                                mainActivity.settext2();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                String currentTime = tf.format(currentTimeAndDate);
                                String currentDate = df.format(currentTimeAndDate);
                                currentDate=convertToEnglish(currentDate);
                                currentTime=convertToEnglish(currentTime);
                                int salesMan =1;
                                try {
                                    salesMan = Integer.parseInt(Login.salesMan);
                                }
                                catch (NumberFormatException e)
                                {
                                    Log.e("NumberFormatException",""+e.getMessage());
                                    salesMan=1;
                                }



                                mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                                        currentDate, "0", 0,0));

//                              if(mDbHandler.getAllSettings().get(0).getPriceByCust()==1){
//                                  String rate_customer = mDbHandler.getRateOfCustomer();
//                                  mDbHandler.addPriceCurrent(cusCode);
//                                  mDbHandler.deletePriceListDCustomerRate(rate_customer);
//                              }

//                                MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
//                                MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
                                dismiss();
//                                if(mDbHandler.getAllSettings().get(0).getApproveAdmin()==1)
//                                {
//                                    Intent intent = new Intent(context, AccountStatment.class);
//                                    context.startActivity(intent);
//                                }
//                                else {
                                    Intent intent = new Intent(context, Activities.class);
                                    context.startActivity(intent);
//                                }


                            } else {
                                //show requst dailog
                                Log.e("AllowedCut_List==",""+DaoRequsts.AllowedCut_List.size());
                              //  Log.e("notallowedAllowedCut_List==",""+DaoRequsts.NotAllowedCut_List.size());


                                if(contains(custObj.getCustId())==1)
                                {   Log.e("AllowedCut_List","true");

                                    DaoRequsts daoRequsts=new DaoRequsts(getActivity());
                                    daoRequsts.deleteRequst(DaoRequsts.AllowedCut_List.get(requstIndix).getKey_validation());

                                    MainActivity mainActivity = new MainActivity();
                                    mainActivity.settext2();

                                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                    String currentTime = tf.format(currentTimeAndDate);
                                    String currentDate = df.format(currentTimeAndDate);
                                    currentDate=convertToEnglish(currentDate);
                                    currentTime=convertToEnglish(currentTime);
                                    int salesMan =1;
                                    try {
                                        salesMan = Integer.parseInt(Login.salesMan);
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        Log.e("NumberFormatException",""+e.getMessage());
                                        salesMan=1;
                                    }



                                    mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                                            currentDate, "0", 0,0));

//                              if(mDbHandler.getAllSettings().get(0).getPriceByCust()==1){
//                                  String rate_customer = mDbHandler.getRateOfCustomer();
//                                  mDbHandler.addPriceCurrent(cusCode);
//                                  mDbHandler.deletePriceListDCustomerRate(rate_customer);
//                              }

//                                MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
//                                MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
                                    dismiss();
//                                if(mDbHandler.getAllSettings().get(0).getApproveAdmin()==1)
//                                {
//                                    Intent intent = new Intent(context, AccountStatment.class);
//                                    context.startActivity(intent);
//                                }
//                                else {
                                    Intent intent = new Intent(context, Activities.class);
                                    context.startActivity(intent);
                                }
                                else if (contains(custObj.getCustId())==2) {
                                    GeneralMethod.showSweetDialog(getActivity(),3,""+getResources().getString(R.string.rejectedRequest2),"");

                                    DaoRequsts daoRequsts=new DaoRequsts(getActivity());
                                    daoRequsts.deleteRequst(DaoRequsts.AllowedCut_List.get(requstIndix).getKey_validation());

                                  //  Toast.makeText(getActivity(), "Not in range", Toast.LENGTH_SHORT).show();
                                }
                                else if (contains(custObj.getCustId())==0){
                                    GeneralMethod.showSweetDialog(getActivity(),3,""+getResources().getString(R.string.adminallowed_Wait),"");

                                }
                                    else

                                {

                                    Log.e("AllowedCut_List","false");
                               ShowRequstApprovaldailog();

                                }

                            //    Toast.makeText(getActivity(), "Not in range", Toast.LENGTH_SHORT).show();
                            }
//                        } else {
//                            Toast.makeText(getActivity(), "This customer is not in your root, please check your map", Toast.LENGTH_LONG).show();
//                        }

                    } else {
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.settext2();

                        Date currentTimeAndDate = Calendar.getInstance().getTime();
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                        String currentTime = tf.format(currentTimeAndDate);
                        String currentDate = df.format(currentTimeAndDate);

                        int salesMan = Integer.parseInt(Login.salesMan);

                        mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                                currentDate, "Not Yet", 0,0));

                        saveCustLocation(cusCode);
                        new JSONTask().execute();

//                        MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
//                        MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
                        dismiss();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
                }
            }
        }
      );
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.find_img_button:
                        customerCheckInListener.displayCustomerListShow();
//                        startVoiceInput(1);
                        break;
                }

            }
        };


        findButton.setOnClickListener(onClickListener);
//        Customer_Account.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerListShow.Customer_Name = customernametest;
                MainActivity mainActivity = new MainActivity();
                mainActivity.menuItemState = 0;
                dismiss();
            }
        });


        return view;
    }

    private void startVoiceInput(int flag) {
        Log.e("startVoiceInput",""+flag);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            if(flag==1)
            {

                Log.e("startVoiceInput2",""+flag);
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            }

        } catch (ActivityNotFoundException a) {
            Log.e("ActivityNotFoundExcep",""+a.getMessage());

        }
        catch (Exception e){
            Log.e("ActivityNotFoundExcep",""+e.getMessage());
        }
    }
    void saveCustLocation(String custId) {

        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {ACCESS_FINE_LOCATION}, 1);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
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

        mDbHandler.updateCustomersMaster("" + lat, "" + lon, custId);

        obj = new JSONObject();
        try {
            obj.put("CustID", custId);
            obj.put("CustLat", lat);
            obj.put("CustLong", lon);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
    }
    void saveRealCheckOutLocation(Transaction transaction) {

        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {ACCESS_FINE_LOCATION}, 1);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
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

        Log.e("timerOff","nnnnn"+lon+"    "+lat+"    "+"seconds remaining: ");

        mDbHandler.updateTransactionLocationReal(transaction.getCusCode(),""+lon,""+lat,transaction.getCheckOutTime(),transaction.getCheckInDate());

//                mTextField.setText("done!");
        Log.e("timerOff","finish");


    }


    boolean isInRange(String cusLat, String cusLong) {
        Log.e("ggg","cusid"+ cusLat+""+cusLong);
        float distance=0;
        if( !isNetworkAvailable())
        {
            return  false;
        }
        if(cusLat.equals(""))
            return true;


        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, 1);
        }

        Location loc1 = new Location("");
        loc1.setLatitude(Double.parseDouble(cusLat));
        loc1.setLongitude(Double.parseDouble(cusLong));

        Location loc2 = new Location("");
        Log.e("ggg2","cusid"+ latitudeCheckIn+""+longtudeCheckIn);
        if(latitudeCheckIn!=0&&longtudeCheckIn!=0)
        {


            loc2.setLatitude(latitudeCheckIn);
            loc2.setLongitude(longtudeCheckIn);
          //  Toast.makeText(getActivity(), "loc1=="+loc1.getLatitude()+"\t  "+ loc1.getLongitude()+"\tloc2"+loc2.getLatitude()+"\t "+ loc2.getLongitude(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), "loc2"+loc2.getLatitude()+"  "+ loc2.getLatitude(), Toast.LENGTH_LONG).show();

             distance = loc2.distanceTo(loc1);

        }
        else {
            getCurrentLocation();
            Log.e("ggg3","cusid"+ latitudeCheckIn+""+longtudeCheckIn);

            loc2.setLatitude(latitudeCheckIn);
            loc2.setLongitude(longtudeCheckIn);
            distance = loc2.distanceTo(loc1);
            Toast.makeText(getActivity(), "Check Internet Connection"+latitudeCheckIn, Toast.LENGTH_SHORT).show();
        }

      //  Toast.makeText(getActivity(), "distance"+distance, Toast.LENGTH_SHORT).show();
if (cusLat.equals("0")|| cusLong.equals("0")) return true;

        return distance <= 50;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private  void getCurrentLocation() {
        latitudeCheckIn=0;longtudeCheckIn=0;
        LocationListener locationListener;

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {// Not granted permission
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        }
                locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitudeCheckIn = location.getLatitude();
                longtudeCheckIn = location.getLongitude();

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
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);//test

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (Exception e)
        {
            Log.e("locationManager",""+e.getMessage());
        }




    }//end

    public void setListener(CustomerCheckInInterface listener) {
        this.customerCheckInListener = listener;
    }

    public void editCheckOutTimeAndDate(Context context) {
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String currentTime = convertToEnglish(tf.format(currentTimeAndDate));
        String currentDate = convertToEnglish(df.format(currentTimeAndDate));
        Transaction transaction=new Transaction();
        transaction.setCheckInDate(currentDate);
        transaction.setCheckOutTime(currentTime);
        transaction.setCusCode(cusCode);
     String  CheckInTime=  mDbHandler.getlastTransaction().getCheckInTime();


              Log.e("ditCheckOutTimeAndDate,CheckInTime",CheckInTime+"")  ;
        today = df.format(currentTimeAndDate);
        today = convertToEnglish(today);
        transaction.setVOUCHERCOUNT(getVouchcount(CheckInTime,today,cusCode));
        mDbHandler.updateTransaction(cusCode,convertToEnglish(currentDate), convertToEnglish(currentTime),transaction.getVOUCHERCOUNT());

        if(Login.SalsManPlanFlage==1){
if(Plan_Kind==0)
            mDbHandler.updateLogStatusInPlan(cusCode,convertToEnglish(currentDate));
else
    mDbHandler.updateLogStatusInPlan(cusCode,convertToEnglish(dayOfWeek+""));
        MainActivity.getSalesmanPlanFromDB(context);}

        List<Settings> settings = mDbHandler.getAllSettings();
        if (settings.size() != 0) {
            approveAdmin= settings.get(0).getApproveAdmin();
            Log.e("timerOff111",""+approveAdmin);
        }
        if(approveAdmin==1) {
            Log.e("timerOff00",""+approveAdmin);

            timerForRealLocation(transaction);
        }
    }

    public void timerForRealLocation(Transaction transaction){

       CountDownTimer countDownTimer= new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
              Log.e("timerOff",longtudeCheckIn+"    "+latitudeCheckIn+"    "+millisUntilFinished+"seconds remaining: " + millisUntilFinished / 1000);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Log.e("timerOff","nnnnn"+checkOutLong+"    "+checkOutLat+"    "+"seconds remaining: ");

                mDbHandler.updateTransactionLocationReal(transaction.getCusCode(),""+checkOutLong,""+checkOutLat,transaction.getCheckOutTime(),convertToEnglish(transaction.getCheckInDate()));

//                mTextField.setText("done!");
                Log.e("timerOff","finish");
//                saveRealCheckOutLocation(transaction);
                cancelTimer();

            }

        };
        countDownTimer.start();


    }

    void cancelTimer() {
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }

//    public void getLoc(){
//
//        LocationManager locationManager;
//        LocationListener locationListener;
//        Log.e("locationtim1","    la= "+latitudeCheckIn +"  lo = "+longtudeCheckIn);
//
//        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions((Activity) context, new String[]
//                    {ACCESS_FINE_LOCATION}, 1);
//        }
//
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                latitudeCheckIn = location.getLatitude();
//                longtudeCheckIn = location.getLongitude();
//
//
//                Log.e("locationtim","    la= "+latitudeCheckIn +"  lo = "+longtudeCheckIn);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//
//
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//    }

    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            JSONArray custLocation = new JSONArray();
            custLocation.put(obj);

            try {
                String ipAddress = mDbHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

                URL url = new URL(URL_TO_HIT);

                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('2'), "UTF-8");

                String table1 = data + "&" + "Sales_Voucher_M={}";
                table1 += "&" + "Sales_Voucher_D={}"
                        + "&" + "Payments={}"
                        + "&" + "Payments_Checks={}"
                        + "&" + "added_customers={}"
                        + "&" + "CUSTOMERS=" + custLocation.toString().trim();

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                try {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(table1);

                    wr.flush();
                } catch (Exception e) {
                    Log.e("here****", e.getMessage());
                }


                // get response
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonResponse = sb.toString();
                Log.e("tag", "" + JsonResponse);

                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.contains("SUCCESS")) {
//                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(getActivity(), "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch ( requestCode)
        {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    isInRange(custObj.getCustLat(), custObj.getCustLong());

                } else {
                    Toast.makeText(getActivity(), "check permission location ", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }

    }
    void GetObjToAddInFirebase(String cust_num,String cust_name){
        try{
            //ayah
            Log.e("GetObjToAddInFirebase==","GetObjToAddInFirebase");
            DaoRequsts daoRequsts=new DaoRequsts(context);

            RequstTest requestAdmin1=new RequstTest();
            requestAdmin1.setSalesman_no(Login.salesMan);
            String salesName=mDbHandler.getSalesmanName_fromSalesTeam();
            requestAdmin1.setSalesman_name(salesName);
            requestAdmin1.setCustomer_no(cust_num);
            requestAdmin1.setVoucher_no("-1");


            Date    currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat   df= new SimpleDateFormat("dd/MM/yyyy");
           String curentDate = df.format(currentTimeAndDate);
            SimpleDateFormat    df2 = new SimpleDateFormat("hh:mm:ss");
            String  curentTime=df2.format(currentTimeAndDate);

            Log.e("curentDate==",curentDate+"");
            requestAdmin1.setDate(curentDate);
            requestAdmin1.setTime(curentTime);
            requestAdmin1.setCustomer_name(cust_name);
            requestAdmin1.setRequest_type("5");
            requestAdmin1.setAmount_value("0");
            requestAdmin1.setKey_validation(getRandomNumberString() + "");
            Log.e("requestAdmin1.getKey", requestAdmin1.getKey_validation()+"");
           DaoRequsts. LogedReq_Key=requestAdmin1.getKey_validation();
            requestAdmin1.setStatus("0");
            requestAdmin1.setSeen_row("0");
            requestAdmin1.setNote(getResources().getString(R.string.log_in_cus_outrange));
            requestAdmin1.setTotal_voucher("0");
            daoRequsts.addRequst(requestAdmin1);
            //SalesInvoice.lastrequst=requestAdmin1.getKey_validation();

        }catch (Exception e){
            Log.e("Exception==", e.getMessage()+"");
        }

    }
    public void ShowRequstApprovaldailog(){
        Log.e("ShowRequstApprovaldailog","ShowRequstApprovaldailog");
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.location_requstdailog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView closeBtn = dialog.findViewById(R.id.cancel);

        Button okBtn = dialog.findViewById(R.id.done);
        dialog.show();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetObjToAddInFirebase(custObj.getCustId(),custObj.getCustName());
                dialog.dismiss();



            }
        });




    }
    public int getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        Log.e("Random", "" + number);
        // this will convert any number sequence into 6 character.
//        return String.format("%06d", number);
        return number;
    }
    public int contains(String cusCode){
        Log.e("contains,AllowedCut_List==",""+DaoRequsts.AllowedCut_List.size());
        for (int i=0;i<DaoRequsts.AllowedCut_List.size();i++){
            Log.e("contains", "" + DaoRequsts.AllowedCut_List.get(i).getCustomer_name()+"  "+DaoRequsts.AllowedCut_List.get(i).getStatus());
            if (DaoRequsts.AllowedCut_List.get(i).getCustomer_no().equals(cusCode))

        if (DaoRequsts.AllowedCut_List.get(i).getStatus().equals("0"))
            { requstIndix=i;
                return 0;

            }

            else if (DaoRequsts.AllowedCut_List.get(i).getStatus().equals("1")) {
                requstIndix=i;
                return 1;

            }
            else if (DaoRequsts.AllowedCut_List.get(i).getStatus().equals("2")) {
                requstIndix=i;
                return 2;


            }
        }
        return -1;
    }
   int getVouchcount(String checkintime,String date,String cus_num){
        int count =0;
      List<String>  vouchers=mDbHandler.gettodayVoucher(cus_num,date);
       Log.e("vouchers==",vouchers.size()+"");
      try {
          for(int i=0;i<vouchers.size();i++)
              if(chechTransctionstime(checkintime,vouchers.get(i)))
                  count++;

              Log.e("getVouchcount==",count+"");
          return count;
      }
   catch (Exception exception){
       Log.e("exception==",exception.getMessage()+"");
       return 0;
   }

   }

    public static boolean chechTransctionstime(String checkintime, String vochertime) throws ParseException {

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        Log.e("checkintime==",checkintime);
        Log.e("vochertime==",vochertime);
        Log.e("match1==",""+checkintime.matches(reg));
        Log.e("match2==",""+vochertime.matches(reg));
        if (checkintime.matches(reg) && vochertime.matches(reg) )

        {
            boolean valid = false;
            //check Time
            //all times are from java.util.Date
            Date inTime = new SimpleDateFormat("HH:mm:ss").parse(checkintime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);
            Log.e("calendar1==",""+calendar1.getTime());

            //voch Time
            Date finTime = new SimpleDateFormat("HH:mm:ss").parse(vochertime);
            Calendar Vochcalendar = Calendar.getInstance();
            Vochcalendar.setTime(finTime);
            Log.e("Vochcalendar==",""+Vochcalendar.getTime()+" calendar1==    "+calendar1.getTime());
//            if (vochertime.compareTo(checkintime) < 0)
//            {
//                Vochcalendar.add(Calendar.DATE, 1);
//
//            }


            if (calendar1.getTime().before(Vochcalendar.getTime()))
            {
                Log.e("true==","true");

                valid = true;
                return valid;
            } else {
                Log.e("false==","false");
                return false;
            }
        }
        return false;    }

}
