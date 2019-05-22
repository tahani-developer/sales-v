package com.dr7.salesmanmanager;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Transaction;

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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

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

    private static DatabaseHandler mDbHandler;


    public interface CustomerCheckInInterface {
        public void showCustomersList();

        public void displayCustomerListShow();
    }

    CustomerCheckInInterface customerCheckInListener;

    public CustomerCheckInFragment() {
        // Required empty public constructor
    }

    public static void settext1() {
        Customer_Name.setText(CustomerListShow.Customer_Name.toString());
        Customer_Account.setText(CustomerListShow.Customer_Account.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_check_in, container, false);
        //selectButton = (ImageButton) view.findViewById(R.id.check_img_button);
        //checkButton = (ImageButton) view.findViewById(R.id.check_img_button);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        findButton = (ImageButton) view.findViewById(R.id.find_img_button);
        Customer_Name = (TextView) view.findViewById(R.id.checkInCustomerName);
        Customer_Account = (TextView) view.findViewById(R.id.customerAccountNo);

        customernametest = CustomerListShow.Customer_Name.toString();

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String today = df.format(currentTimeAndDate);

        mDbHandler = new DatabaseHandler(getActivity());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Customer_Account.getText().toString().equals(""))) {

                    cusCode = Customer_Account.getText().toString();

                    cusName = Customer_Name.getText().toString();
                    status = 0;

                    List<Customer> customers = mDbHandler.getAllCustomers();
                    Customer custObj = null;
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

                        if(inRoot) {
                            if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1 ||
                                    isInRange(custObj.getCustLat(), custObj.getCustLong())) {

                                MainActivity mainActivity = new MainActivity();
                                mainActivity.settext2();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                String currentTime = tf.format(currentTimeAndDate);
                                String currentDate = df.format(currentTimeAndDate);

                                int salesMan = Integer.parseInt(Login.salesMan);

                                mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                                        "01/01/19999", "0", 0,0));

                                MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
                                MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
                                dismiss();
                            } else {
                                Toast.makeText(getActivity(), "Not in range", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "This customer is not in your root, please check your map", Toast.LENGTH_LONG).show();
                        }

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

                        saveCustLocation(Integer.parseInt(cusCode));
                        new JSONTask().execute();

                        MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
                        MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
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

    void saveCustLocation(int custId) {

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

    boolean isInRange(String cusLat, String cusLong) {

        if(cusLat.equals(""))
            return true;

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
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
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

        Location loc1 = new Location("");
        loc1.setLatitude(Double.parseDouble(cusLat));
        loc1.setLongitude(Double.parseDouble(cusLong));

        Location loc2 = new Location("");
        loc2.setLatitude(currentLat);
        loc2.setLongitude(currentLon);

        float distance = loc1.distanceTo(loc2);

        Log.e("dist  " , "" + distance);

        return distance <= 200;
    }

    public void setListener(CustomerCheckInInterface listener) {
        this.customerCheckInListener = listener;
    }

    public void editCheckOutTimeAndDate() {
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String currentTime = tf.format(currentTimeAndDate);
        String currentDate = df.format(currentTimeAndDate);

        mDbHandler.updateTransaction(cusCode, currentDate, currentTime);

    }

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

}
