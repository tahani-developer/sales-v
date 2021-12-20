package com.dr7.salesmanmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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
import org.apache.http.client.methods.HttpGet;
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
import java.net.URISyntaxException;
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

import static com.dr7.salesmanmanager.Activities.currentKey;
import static com.dr7.salesmanmanager.Activities.keyCreditLimit;
import static com.dr7.salesmanmanager.DiscountFragment.checkState;
import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;

import static com.dr7.salesmanmanager.DiscountFragment.noteRequest;
import static com.dr7.salesmanmanager.DiscountFragment.stateZero;
import static com.dr7.salesmanmanager.Login.headerDll;
import static com.dr7.salesmanmanager.Login.salesMan;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.addToList;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.checkState_recycler;

import static com.dr7.salesmanmanager.ReturnByVoucherNo.loadSerial;
import static com.dr7.salesmanmanager.SalesInvoice.checkState_LimitCredit;
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
    String ipAddress="", ipWithPort = "", SalesManLogin,CONO="";
    JSONObject vouchersObject;



    //        public static List<Customer> customerList = new ArrayList<>();
//        public static List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
    public static ArrayList<RequestAdmin> requestList = new ArrayList<>();

    public requestAdmin(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
        List<Settings> settings = mHandler.getAllSettings();
        ipWithPort = settings.get(0).getIpPort();
        CONO = mHandler.getAllSettings().get(0).getCoNo();

    }


    public void startParsing() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            getData();
            String ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";

            if(typaImport==0)
            new JSONTask().execute(URL_TO_HIT);
            else {
                getRequestObject();
                new JSONTask_AddRequest_IIS().execute();
            }

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


        Log.e("RequestAdmin", "currentKey" + currentKey);

        // for total Discount 1 ===> percent     10===> value
        if(discountRequest.getRequest_type().equals("1")||discountRequest.getRequest_type().equals("10"))
        {
            discountRequest.setNote(noteRequest);
            currentKeyTotalDiscount=discountRequest.getKey_validation();
        }
        else if(discountRequest.getRequest_type().equals("100"))
        {
            keyCreditLimit=discountRequest.getKey_validation();
        }
        else  if(discountRequest.getRequest_type().equals("0")){
            currentKey=discountRequest.getKey_validation();
        }



        requestList.add(discountRequest);
        if(typaImport==0)
        jsonArrayRequest.put(requestList.get(0).getJSONObject());
      else
        jsonArrayRequest.put(requestList.get(0).getJSONObjectDelphi());

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
                    if(requestType.equals("1")||requestType.equals("10"))
                    {
                        checkState.setText("0");
                        stateZero=true;
                    }
                    else {
                        if(requestType.equals("0"))
                        checkState_recycler.setText("0");
                        else   if(requestType.equals("100"))
                            checkState_LimitCredit.setText("0");
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
        if( discountRequest!=null)
        {
            requestType=discountRequest.getRequest_type();
            Log.e("checkStatuseRequest","firstJSONTask_checkStateRequest");
            if(requestType.equals("1")||requestType.equals("10"))
            {
                if(checkState.getText().toString().equals("0"))
                {
//                    new  JSONTask_checkStateRequest().execute();
                   new  JSONTask_checkStateRequest_IIS().execute();
                }
            }
            else {
                if(requestType.equals("0"))
                {
                    if(checkState_recycler.getText().toString().equals("0"))
                    {
//                        new  JSONTask_checkStateRequest().execute();
                        new  JSONTask_checkStateRequest_IIS().execute();
                    }
                }
                else if(requestType.equals("100"))
                {
                    if(checkState_LimitCredit.getText().toString().equals("0")){
//                        new  JSONTask_checkStateRequest().execute();
                        new  JSONTask_checkStateRequest_IIS().execute();
                    }
                }



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
                request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php"));

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

                        Log.e("key_validation","keyCreditLimit= "+keyCreditLimit+"\tcurrentKey="+currentKey+"\tcurrentKeyTotalDiscount="+currentKeyTotalDiscount);
                        for (int i = 0; i < notificationInfo.length(); i++) {
                             infoDetail = notificationInfo.getJSONObject(i);
                            Log.e("infoDetail",""+infoDetail.get("key_validation").toString());

                            if(infoDetail.get("key_validation").toString().equals(currentKey))// for item request
                            {
                                if(infoDetail.get("status").toString().equals("1")){
                                    addToList.setEnabled(true);
                                    discountRequest=null;
                                    stopTimer();

                                }
                                else if(infoDetail.get("status").toString().equals("2"))
                                { discountRequest=null;
                                    stopTimer();
                                   // addToList.setEnabled(false);
                                }



                                    checkState_recycler.setText(infoDetail.get("status").toString());


                            }
                                else
                                    if(infoDetail.get("key_validation").toString().equals(currentKeyTotalDiscount)){// for all voucher Discount
                                    if(infoDetail.get("status").toString().equals("1")){
                                        addToList.setEnabled(true);
                                        discountRequest=null;
                                        stopTimer();

                                    }
                                    else if(infoDetail.get("status").toString().equals("2"))
                                    {
                                        discountRequest=null;
                                        stopTimer();
                                        // addToList.setEnabled(false);
                                    }

                                        checkState.setText(infoDetail.get("status").toString());

                                }
                                else if(infoDetail.get("key_validation").toString().equals(keyCreditLimit)){// for accsed limit credit
                                    Log.e("key_validation","==="+keyCreditLimit);
                                    if(infoDetail.get("status").toString().equals("1")){
                                        discountRequest=null;
                                        stopTimer();

                                    }
                                    else if(infoDetail.get("status").toString().equals("2"))
                                    {
                                        discountRequest=null;
                                        stopTimer();

                                    }


                                        checkState_LimitCredit.setText(infoDetail.get("status").toString());

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
    private class JSONTask_checkStateRequest_IIS extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_ = "my";
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/ADMSTATUSE_REQUEST";



                try {
                    request.setURI(new URI(URL_TO_HIT));
                }
                catch (URISyntaxException e) {
                    e.printStackTrace();
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("check Connection")
                                    .show();


//                        Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


                nameValuePairs.add(new BasicNameValuePair("CONO", CONO));
                nameValuePairs.add(new BasicNameValuePair("SALESMAN_NO",discountRequest.getSalesman_no()));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_TYPE", discountRequest.getRequest_type()));
                nameValuePairs.add(new BasicNameValuePair("VOUCHER_NO", discountRequest.getVoucher_no()));
                Log.e("jsonArrayRequest", "" + discountRequest.getSalesman_no());
                Log.e("jsonArrayRequest", "getVoucher_no=" +discountRequest.getVoucher_no());



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
            Log.e("STATUSE_REQUEST",""+s.toString());
            if (s != null) {
                if (s.contains("STATUS")) {
                    stateZero=false;
//KEY_VALIDATION
                    try {
                        JSONArray notificationInfo = null;
                        notificationInfo = new JSONArray(s);

                        JSONObject infoDetail=null;

                        Log.e("key_validation","keyCreditLimit= "+keyCreditLimit+"\tcurrentKey="+currentKey+"\tcurrentKeyTotalDiscount="+currentKeyTotalDiscount);
                        for (int i = 0; i < notificationInfo.length(); i++) {
                            infoDetail = notificationInfo.getJSONObject(i);
                            Log.e("infoDetail",""+infoDetail.get("KEY_VALIDATION").toString());

                            if(infoDetail.get("KEY_VALIDATION").toString().equals(currentKey))// for item request
                            {
                                if(infoDetail.get("STATUS").toString().equals("1")){
                                    addToList.setEnabled(true);
                                    discountRequest=null;
                                    stopTimer();

                                }
                                else if(infoDetail.get("STATUS").toString().equals("2"))
                                { discountRequest=null;
                                    stopTimer();
                                    // addToList.setEnabled(false);
                                }



                                checkState_recycler.setText(infoDetail.get("STATUS").toString());


                            }
                            else
                            if(infoDetail.get("KEY_VALIDATION").toString().equals(currentKeyTotalDiscount)){// for all voucher Discount
                                if(infoDetail.get("STATUS").toString().equals("1")){
                                    addToList.setEnabled(true);
                                    discountRequest=null;
                                    stopTimer();

                                }
                                else if(infoDetail.get("STATUS").toString().equals("2"))
                                {
                                    discountRequest=null;
                                    stopTimer();
                                    // addToList.setEnabled(false);
                                }

                                checkState.setText(infoDetail.get("STATUS").toString());

                            }
                            else if(infoDetail.get("KEY_VALIDATION").toString().equals(keyCreditLimit)){// for accsed limit credit
                                Log.e("key_validation","==="+keyCreditLimit);
                                if(infoDetail.get("STATUS").toString().equals("1")){
                                    discountRequest=null;
                                    stopTimer();

                                }
                                else if(infoDetail.get("STATUS").toString().equals("2"))
                                {
                                    discountRequest=null;
                                    stopTimer();

                                }


                                checkState_LimitCredit.setText(infoDetail.get("STATUS").toString());

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
    private void getRequestObject() {

        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayRequest);
            Log.e("getAddedCustomer","JSN"+jsonArrayRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class JSONTask_AddRequest_IIS extends AsyncTask<String, String, String> {

        private String voucherNo = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pdValidation.setTitleText(context.getResources().getString(R.string.process));
//            pdValidation.setCancelable(false);
//            pdValidation.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {
            //  http://10.0.0.22:8085/GetVE_M?CONO=295&VHFNO=6
            try {


///ADMREQUEST_ADMIN
//
//                        JSONSTR={"JSN"
//
//                        --> SALESMAN_NAME,SALESMAN_NO,CUSTOMER_NAME,
//                        CUSTOMER_NO,AMOUNT_VALUE,DATE,TIME,NOTE,REQUEST_TYPE,
//                        VOUCHER_NO,STATUS ,KEY_VALIDATION,SEEN_ROW,ROW_ID,TOTAL_VOUCHER

                if (!ipAddress.equals("")) {

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/ADMREQUEST_ADMIN";


                }
                Log.e("ADMREQUEST_ADMIN=", "" + URL_TO_HIT.toString());
            } catch (Exception e) {
//                pdValidation.dismissWithAnimation();
            }

            String JsonResponse="";

                try {

                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost();
                    try {
                        request.setURI(new URI(URL_TO_HIT));
                    }
                    catch (URISyntaxException e) {
                        e.printStackTrace();
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            public void run() {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("check Connection")
                                        .show();


//                        Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                    Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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
                    Log.e("JsonResponse", "ExporVoucher" + JsonResponse);


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
//                        pdValidation.dismissWithAnimation();
                        Toast.makeText(context, "Ip Connection Failed AccountStatment", Toast.LENGTH_LONG).show();
                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
            return JsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("onPostExecute", "admin" + s.toString());
//            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("Saved Successfully")) {
                    if(requestType.equals("1")||requestType.equals("10"))
                    {
                        checkState.setText("0");
                        stateZero=true;
                    }
                    else {
                        if(requestType.equals("0"))
                            checkState_recycler.setText("0");
                        else   if(requestType.equals("100"))
                            checkState_LimitCredit.setText("0");
                    }



                    task=new TimerTask(context);
                    task.startTimer();
//                    Log.e("tag", "****SuccessText"+checkState.getText().toString());
//                    progressDialog.dismiss();

                }
//                progressDialog.dismiss();

//                progressDialog.dismiss();
            }

        }

    }

}

