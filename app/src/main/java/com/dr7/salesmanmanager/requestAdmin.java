package com.dr7.salesmanmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.DiscountFragment.checkState;
import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;

import static com.dr7.salesmanmanager.DiscountFragment.noteRequest;
import static com.dr7.salesmanmanager.DiscountFragment.stateZero;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.addToList;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.checkState_recycler;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.currentKey;
import static com.dr7.salesmanmanager.SalesInvoice.discountRequest;

public class requestAdmin {
    private String URL_TO_HIT;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;
    boolean start = false;
    private JSONArray jsonArrayRequest;
    RequestAdmin requestData;
    TimerTask task;
    String requestType;


    //        public static List<Customer> customerList = new ArrayList<>();
//        public static List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
    public static ArrayList<RequestAdmin> requestList = new ArrayList<>();

    public requestAdmin(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);


    }


    public void startParsing() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            getData();
            String ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";

            new JSONTask().execute(URL_TO_HIT);

        }
    }
    public void getState() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
//            getData();
            String ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";

           new JSONTask_checkStateRequest().execute(URL_TO_HIT);

        }
    }

    private void getData() {
        requestList = new ArrayList<>();
        jsonArrayRequest = new JSONArray();
        requestType=discountRequest.getRequest_type();
        Log.e("RequestAdmin", "" + discountRequest+requestType);
        discountRequest.setKey_validation(getRandomNumberString() + "");
        currentKey=discountRequest.getKey_validation();

        Log.e("RequestAdmin", "currentKey" + currentKey);

        if(discountRequest.getRequest_type().equals("1"))
        {
            discountRequest.setNote(noteRequest);
            currentKeyTotalDiscount=discountRequest.getKey_validation();
        }



        requestList.add(discountRequest);

        jsonArrayRequest.put(requestList.get(0).getJSONObject());
        Log.e("getData",""+requestList.get(0).getJSONObject());
    }

    public static int getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        Log.e("Random", "" + number);
        // this will convert any number sequence into 6 character.
//        return String.format("%06d", number);
        return number;
    }

    void storeInDatabase() {
//            new SQLTask().execute(URL_TO_HIT);
    }

    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_ = "my";
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String ipAddress = "";

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                Toast.makeText(context, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "5"));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_ADMIN", jsonArrayRequest.toString().trim()));
                Log.e("jsonArrayRequest", "" + jsonArrayRequest.getString(0).toString());


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tagadmin", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

//                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("SUCCESS")) {
                    if(requestType.equals("1"))
                    {
                        checkState.setText("0");
                        stateZero=true;
                    }
                    else {
                        checkState_recycler.setText("0");
                    }



                    task=new TimerTask(context);
                    task.startTimer();
//                    Log.e("tag", "****SuccessText"+checkState.getText().toString());
                    progressDialog.dismiss();

                }
                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
            }

        }

    }


    public void checkRequestState() {
        requestType=discountRequest.getRequest_type();
        Log.e("checkStatuseRequest","firstJSONTask_checkStateRequest");
        if(requestType.equals("1"))
        {
            if(checkState.getText().toString().equals("0"))
            {
                new  JSONTask_checkStateRequest().execute();
            }
        }
        else {
            if(checkState_recycler.getText().toString().equals("0"))
            {
                new  JSONTask_checkStateRequest().execute();
            }

        }



    }

    private class JSONTask_checkStateRequest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_ = "my";
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String ipAddress = "";

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                Toast.makeText(context, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "6"));
                nameValuePairs.add(new BasicNameValuePair("SalesManNo",discountRequest.getSalesman_no()));
                nameValuePairs.add(new BasicNameValuePair("RequestType", discountRequest.getRequest_type()));
                nameValuePairs.add(new BasicNameValuePair("VoucherNo", discountRequest.getVoucher_no()));
                Log.e("jsonArrayRequest", "" + discountRequest.getSalesman_no());
                Log.e("jsonArrayRequest", "" +discountRequest.getRequest_type());



                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_requestState", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject result=null;
            String impo = "";
            if (s != null) {
                if (s.contains("STATUSE_REQUEST")) {
                    stateZero=false;

                    try {
                        result = new JSONObject(s);


                        JSONArray notificationInfo = null;

                        notificationInfo = result.getJSONArray("STATUSE_REQUEST");
                        JSONObject infoDetail=null;

                        Log.e("notificationInfo",""+notificationInfo.length());
                        for (int i = 0; i < notificationInfo.length(); i++) {
                             infoDetail = notificationInfo.getJSONObject(i);
                                if(infoDetail.get("key_validation").toString().equals(currentKey))
                            {
                                if(infoDetail.get("status").toString().equals("1")){
                                    addToList.setEnabled(true);
                                    stopTimer();

                                }
                                else if(infoDetail.get("status").toString().equals("2"))
                                {
                                    stopTimer();
                                   // addToList.setEnabled(false);
                                }
                                if(requestType.equals("1"))
                                {

                                    checkState.setText(infoDetail.get("status").toString());
                                }
                                else {


                                    checkState_recycler.setText(infoDetail.get("status").toString());

                                }
                            }
                                else if(infoDetail.get("key_validation").toString().equals(currentKeyTotalDiscount)){
                                    if(infoDetail.get("status").toString().equals("1")){
                                        addToList.setEnabled(true);
                                        stopTimer();

                                    }
                                    else if(infoDetail.get("status").toString().equals("2"))
                                    {
                                        stopTimer();
                                        // addToList.setEnabled(false);
                                    }
                                    if(requestType.equals("1"))
                                    {

                                        checkState.setText(infoDetail.get("status").toString());
                                    }
                                }




                        }






                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                            e.printStackTrace();
                        }
                }
//                progressDialog.dismiss();
            }
        }

    }

    private void showMessage(int flag) {
        if(flag==1)
        {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(context.getResources().getString(R.string.succsesful))
                    .setContentText(context.getResources().getString(R.string.acceptedRequest))
                    .show();
        }
        else if(flag==2)
        {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(context.getResources().getString(R.string.sorry))
                    .setContentText(context.getResources().getString(R.string.rejectedRequest))
                    .show();
        }

    }
    void stopTimer() {
        Log.e("stopTimerRequest","stopTimer");
        task=new TimerTask(context);
        task.stopTimer();
    }
}

