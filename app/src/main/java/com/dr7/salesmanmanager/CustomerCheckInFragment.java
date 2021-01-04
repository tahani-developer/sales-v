package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Reports.Reports;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static com.dr7.salesmanmanager.LocationPermissionRequest.checkOutLat;
import static com.dr7.salesmanmanager.LocationPermissionRequest.checkOutLong;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

import static com.dr7.salesmanmanager.MainActivity.customerLocation_main;
import static com.dr7.salesmanmanager.MainActivity.latitudeCheckIn;
import static com.dr7.salesmanmanager.MainActivity.latitude_main;
import static com.dr7.salesmanmanager.MainActivity.location_main;
import static com.dr7.salesmanmanager.MainActivity.longitude_main;
import static com.dr7.salesmanmanager.MainActivity.longtudeCheckIn;
import static com.dr7.salesmanmanager.MainActivity.transactions;

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
                customerCheckInListener.displayCustomerListShow();
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
                        Log.e("getAllowOutOfRange",""+mDbHandler.getAllSettings().get(0).getAllowOutOfRange());
//                        if(inRoot) {
                            if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 0 ||
                                    isInRange(custObj.getCustLat(), custObj.getCustLong())) {

                                MainActivity mainActivity = new MainActivity();
                                mainActivity.settext2();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
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
                                        "01/01/19999", "0", 0,0));

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
                                Toast.makeText(getActivity(), "Not in range", Toast.LENGTH_SHORT).show();
                            }
//                        } else {
//                            Toast.makeText(getActivity(), "This customer is not in your root, please check your map", Toast.LENGTH_LONG).show();
//                        }

                    } else {
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.settext2();

                        Date currentTimeAndDate = Calendar.getInstance().getTime();
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                        String currentTime = tf.format(currentTimeAndDate);
                        String currentDate = df.format(currentTimeAndDate);

                        int salesMan = Integer.parseInt(Login.salesMan);

                        mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                                "01/01/19999", "Not Yet", 0,0));

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

        Toast.makeText(getActivity(), "distance"+distance, Toast.LENGTH_SHORT).show();

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

    public void editCheckOutTimeAndDate() {
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String currentTime = convertToEnglish(tf.format(currentTimeAndDate));
        String currentDate = convertToEnglish(df.format(currentTimeAndDate));
        Transaction transaction=new Transaction();
        transaction.setCheckInDate(currentDate);
        transaction.setCheckOutTime(currentTime);
        transaction.setCusCode(cusCode);

        mDbHandler.updateTransaction(cusCode,convertToEnglish(currentDate), convertToEnglish(currentTime));
        timerForRealLocation(transaction);

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
}
