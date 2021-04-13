package com.dr7.salesmanmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemSwitch;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SalesMan;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.userNo;
import static com.dr7.salesmanmanager.MainActivity.password;
import static com.dr7.salesmanmanager.MainActivity.passwordFromAdmin;

public class ExportJason extends AppCompatActivity {

    public Context context;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments , jsonArrayPaymentsPaper , jsonArrayAddedCustomer,
            jsonArrayTransactions, jsonArrayBalance ,jsonArrayStockRequest,jsonArrayLocation,jsonArraySerial,jsonArrayStockRequestMaster;
    DatabaseHandler mHandler;
    JSONObject vouchersObject;
    public static  SweetAlertDialog pd,pdValidation;

    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();
    public static List<Voucher> requestVouchers = new ArrayList<>();
    public static List<Item> requestItems = new ArrayList<>();
     public  static  List<SalesManItemsBalance> salesManItemsBalanceList=new ArrayList<>();
    public  static  List<Item> stockRequestListList=new ArrayList<>();
    public  static  List<CustomerLocation> customerLocationList=new ArrayList<>();
    public  static  List<serialModel> serialModelList=new ArrayList<>();
    int  approveAdmin=-1,workOnLine=-1;
    SweetAlertDialog getDataProgress;
    private JsonObjectRequest exportVouMasterRequest;
    private RequestQueue requestQueue;
    public static String CONO="",SalesManLogin;
    SweetAlertDialog pdVoucher=null;

    boolean isPosted = true;
//    getCustomerLocation
    String ipAddress="",ipWithPort="",URL_TO_HIT="";
    String headerDll="";

    public ExportJason(Context context) throws JSONException {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
        ipWithPort=mHandler.getAllSettings().get(0).getIpPort();
        this.requestQueue = Volley.newRequestQueue(context);
        CONO=mHandler.getAllSettings().get(0).getCoNo();
        SalesManLogin= mHandler.getAllUserNo();
        fillIpAddressWithPort();
    }

    private void fillIpAddressWithPort() {
        if (!ipAddress.equals("")) {
            //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
            //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
            if (ipAddress.contains(":")) {
                int ind = ipAddress.indexOf(":");
                ipAddress = ipAddress.substring(0, ind);
            }
        }
        else {
            Toast.makeText(context, "Fill Ip adress", Toast.LENGTH_SHORT).show();
        }
    }


    void startExportDatabase() throws JSONException {
        //
//

        try {
            serialModelList = mHandler.getAllSerialItems();
            Log.e("serialModelList",""+serialModelList);
            jsonArraySerial = new JSONArray();
            for (int i = 0; i < serialModelList.size(); i++)
            {
                if(serialModelList.get(i).getIsPosted().equals("0")){
                    jsonArraySerial.put(serialModelList.get(i).getJSONObject());
                }
            }
            Log.e("jsonArraySerial",""+jsonArraySerial.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //*************************************************************
        customerLocationList = mHandler.getCustomerLocation();
        jsonArrayLocation = new JSONArray();
        for (int i = 0; i < customerLocationList.size(); i++)
        {
            customerLocationList.get(i).getCUS_NO();
            customerLocationList.get(i).getLATIT();

            customerLocationList.get(i).getLONG();

            if (customerLocationList.get(i).getIsPost() == 0) {
//                customerLocationList.get(i).setIsPost(1);
//

                jsonArrayLocation.put(customerLocationList.get(i).getJSONObject());
            }


        }

        //******************************************
        transactions = mHandler.getAlltransactions();

        jsonArrayTransactions = new JSONArray();
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getIsPosted() == 0) {
//                transactions.get(i).setIsPosted(1);
                jsonArrayTransactions.put(transactions.get(i).getJSONObject());
            }
        //******************************************
        try {

            jsonArrayBalance=new JSONArray();
            salesManItemsBalanceList=mHandler.getSalesManItemsBalance(Login.salesMan);
            for (int i = 0; i < salesManItemsBalanceList.size(); i++) {

                salesManItemsBalanceList.get(i).getSalesManNo();
                salesManItemsBalanceList.get(i).getItemNo();
                salesManItemsBalanceList.get(i).getQty();
                jsonArrayBalance.put(salesManItemsBalanceList.get(i).getJSONObject());
            }
        }
        catch ( Exception e)
        {

        }


        //******************************************
        stockRequestListList=mHandler.getAllStockRequestItems();
        jsonArrayStockRequest=new JSONArray();

        for (int i = 0; i < stockRequestListList.size(); i++)
        {
            if(stockRequestListList.get(i).getIsPosted()==0)
            {
                jsonArrayStockRequest.put(stockRequestListList.get(i).getJSONObject());
            }


        }

        //*************************************************
        requestVouchers=mHandler.getAllStockRequestVouchers();
        jsonArrayStockRequestMaster=new JSONArray();

        for (int i = 0; i < requestVouchers.size(); i++)
        {
            if(requestVouchers.get(i).getIsPosted()==0)
            {
                jsonArrayStockRequestMaster.put(requestVouchers.get(i).getJSONObject());
            }


        }

        //********************************************
        //********************************************

        vouchers = mHandler.getAllVouchers();// from voucher master
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
            {
                if (vouchers.get(i).getIsPosted() == 0) {
                    jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
                }
            }

        items = mHandler.getAllItems();
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
           {
               if (items.get(i).getIsPosted() == 0) {

                   jsonArrayItems.put(items.get(i).getJSONObject());

               }

            }

        payments = mHandler.getAllPayments();
        jsonArrayPayments = new JSONArray();
        for (int i = 0; i < payments.size(); i++)
     {
         if (payments.get(i).getIsPosted() == 0) {
//                payments.get(i).setIsPosted(1);
             jsonArrayPayments.put(payments.get(i).getJSONObject());
         }
            }

        paymentsPaper = mHandler.getAllPaymentsPaper();
        jsonArrayPaymentsPaper = new JSONArray();
        for (int i = 0; i < paymentsPaper.size(); i++)
        {
            if (paymentsPaper.get(i).getIsPosted() == 0) {
//                paymentsPaper.get(i).setIsPosted(1);
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
            }
            }

        addedCustomer = mHandler.getAllAddedCustomer();
        jsonArrayAddedCustomer = new JSONArray();
        for (int i = 0; i < addedCustomer.size(); i++)
        {
//                addedCustomer.get(i).setIsPosted(1);
                jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObject());
            }
       // getData();

       new ExportJason.JSONTask().execute();




    }
    void startExport()throws JSONException {
        headerDll="/Falcons/VAN.dll";
//        headerDll="";
//        startExportDatabase();
        exportSalesVoucherM();
//        savePayment();
//        exportVoucherDetail();
//        exportSerial();
//        exportTransaction();
//        exportPayment();
//        exportPaymentPaper();
//        exportAddedCustomer();
    }

    private void exportAddedCustomer() {
        getAddedCustomer();
        new JSONTaskDelphiAddedCustomer().execute();

    }

    private void getAddedCustomer() {
        addedCustomer = mHandler.getAllAddedCustomer();
        jsonArrayAddedCustomer = new JSONArray();
        for (int i = 0; i < addedCustomer.size(); i++)
        {
//                addedCustomer.get(i).setIsPosted(1);
            jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObjectDelphi());
        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayAddedCustomer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void exportPaymentPaper() {
        getPaymentPaper();
        new JSONTaskDelphiPaymentPaper().execute();
    }

    private void getPaymentPaper() {
        paymentsPaper = mHandler.getAllPaymentsPaper();
        jsonArrayPaymentsPaper = new JSONArray();
        for (int i = 0; i < paymentsPaper.size(); i++)
        {
            if (paymentsPaper.get(i).getIsPosted() == 0) {
//                paymentsPaper.get(i).setIsPosted(1);
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2Delphi());
            }
        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayPaymentsPaper);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void exportPayment() {
        getPayment();
        new JSONTaskDelphiPayment().execute();
    }

    private void getPayment() {
        payments = mHandler.getAllPayments();
        jsonArrayPayments = new JSONArray();
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getIsPosted() == 0) {
//                payments.get(i).setIsPosted(1);
                jsonArrayPayments.put(payments.get(i).getJSONObjectDelphi());
            }
        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayPayments);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void exportSalesVoucherM() {
        getVouchers();
        pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdVoucher.setTitleText(" Start export Vouchers");
        pdVoucher.setCancelable(false);
        pdVoucher.show();
        new JSONTaskDelphi().execute();

    }


    private void getVouchers() {

        vouchers = mHandler.getAllVouchers();// from voucher master
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
        {
            if (vouchers.get(i).getIsPosted() == 0) {
                jsonArrayVouchers.put(vouchers.get(i).getJSONObjectDelphi());
            }
        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayVouchers);
            Log.e("vouchersObject",""+vouchersObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class exportVoucherMasterClass implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter/", "trackingCheque/error/" + error.toString());
            getDataProgress.dismissWithAnimation();
            if ((error instanceof NoConnectionError)) {
                Toast.makeText(context,
                        "تأكد من اتصال الانترنت",
                        Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onResponse(JSONObject response) {
            // getDataProgress.dismissWithAnimation();
            Log.e("presenter/", "trackingCheque/" + response.toString());
           if (response.toString().contains("\"StatusCode\":28,\"StatusDescreption\":\"This User not have checks.\"")) {
//            Toast.makeText(singUpActivity, "No cheques found!", Toast.LENGTH_SHORT).show();
            } else if (response.toString().contains("\"StatusCode\":6,\"StatusDescreption\":\"Check Data not found\"")) {//{"StatusCode":6,"StatusDescreption":"Check Data not found"}

                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("**** Cheque Tracing ****")
                        .setContentText("Check Data not found")
                        .show();

            }
            getDataProgress.dismissWithAnimation();
        }
    }


    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_="my";
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
            Log.e("tagexPORT", "JsonResponse" );

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost  request = new HttpPost ();
                request.setURI(new URI("http://" + ipAddress.trim() + "/VANSALES_WEB_SERVICE/index.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "2"));
                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_M", jsonArrayVouchers.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_D", jsonArrayItems.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("Payments", jsonArrayPayments.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments_Checks", jsonArrayPaymentsPaper.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("TABLE_TRANSACTIONS", jsonArrayTransactions.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Added_Customers", jsonArrayAddedCustomer.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("LOAD_VAN", jsonArrayBalance.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CUSTOMER_LOCATION", jsonArrayLocation.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("ITEMSERIALS", jsonArraySerial.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_STOCK_M", jsonArrayStockRequestMaster.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_STOCK_D", jsonArrayStockRequest.toString().trim()));


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));



                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("JsonResponse","Export"+JsonResponse);


                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex)
            {
                ex.printStackTrace();
                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                progressDialog.dismiss();
                return null;
            }


//            String JsonResponse = null;
////            String JsonDATA = params[0];
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            try {
//                String ipAddress = mHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
//                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
//
//                URL url = new URL(URL_TO_HIT);
//
//                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
//                        URLEncoder.encode(String.valueOf('2'), "UTF-8");
//
//               /* String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
//                String table2 = data + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim();
//                String table3 = data + "&" + "Payments="        + jsonArrayPayments.toString().trim();
//                String table4 = data + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim();
//*/
//                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
//                table1 +="&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
//                        // + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
//                        + "&" + "Payments="        + jsonArrayPayments.toString().trim()
//                        + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim()
//                        + "&" + "Added_Customers=" + jsonArrayAddedCustomer.toString().trim()
//                        + "&" + "TABLE_TRANSACTIONS=" + jsonArrayTransactions.toString().trim()
//                        + "&" + "LOAD_VAN=" + jsonArrayBalance.toString().trim();//sales_man_item_balance
//
////                URLConnection conn = url.openConnection();
////
////                conn.setDoOutput(true);
////                conn.setDoInput(true);
//
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//                try {
////                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
////                    wr.write(table1);
////
////                    wr.flush();
//
//                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                    wr.writeBytes(table1);
//                    wr.flush();
//                    wr.close();
//
//                } catch (Exception e){
//                    Log.e("here****" , e.getMessage());
//                }
//
//
//                // get response
//                reader = new BufferedReader(new
//                        InputStreamReader(httpURLConnection.getInputStream()));
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                // Read Server Response
//                while((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                JsonResponse = sb.toString();
//                Log.e("tag", "" + JsonResponse);
//
//                return JsonResponse;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s != null) {
                if (s.contains("SUCCESS")) {
                    mHandler.updateVoucher();
                    mHandler.updateVoucherDetails();
                    mHandler.updatePayment();
                    mHandler.updatePaymentPaper();
                    mHandler.updateAddedCustomers();
                    mHandler.updateTransactions();
                    mHandler.updateCustomersMaster();
                    mHandler.updateSerialTableIsposted();
                    mHandler.updateRequestStockMaster();
                    mHandler.updateRequestStockDetail();
                    getData();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    if(s.contains("SAVING_ERRORDuplicate"))
                    {

                    progressDialog.dismiss();
                    try {
                        int indexError=s.indexOf("entry");

                        String errorMessage=s.substring(indexError,indexError+42);
                        showDialogError(errorMessage);
                    }
                    catch (Exception e)
                    {

                    }


                    }
//                    *********************************

                    Toast.makeText(context, "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                try {

                        Log.e("onPostExecute","");
                        Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();


                }
                catch (Exception e)
                {
                    progressDialog.dismiss();
                    Log.e("ExportException",""+e.getMessage().toString());

                }

            }
            progressDialog.dismiss();
        }
    }

    private void getData() {
        List<Settings> settings =mHandler.getAllSettings();
        if (settings.size() != 0) {
            workOnLine= settings.get(0).getWorkOnline();
            Log.e("workOnLine",""+workOnLine);
        }
        if(workOnLine==1) {
            isPosted=mHandler.isAllVoucher_posted();

                if(isPosted==true)
                {
                    ImportJason obj = new ImportJason(context);
                    obj.getItemBalance(userNo);
                }
                else{
                    Toast.makeText(context,R.string.failImpo_export_data , Toast.LENGTH_SHORT).show();
                }

        }

    }

    private class JSONTaskDelphi extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        public  String salesNo="",finalJson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

                try {
                    if (!ipAddress.equals("")) {



                      //  String data= "{\"JSN\":[{\"COMAPNYNO\":290,\"VOUCHERYEAR\":\"2021\",\"VOUCHERNO\":\"1212\",\"VOUCHERTYPE\":\"3\",\"VOUCHERDATE\":\"24/03/2020\",\"SALESMANNO\":\"5\",\"CUSTOMERNO\":\"123456\",\"VOUCHERDISCOUNT\":\"50\",\"VOUCHERDISCOUNTPERCENT\":\"10\",\"NOTES\":\"AAAAAA\",\"CACR\":\"1\",\"ISPOSTED\":\"0\",\"PAYMETHOD\":\"1\",\"NETSALES\":\"150.720\"}]}";

                            URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/ExportSALES_VOUCHER_M";



                        Log.e("URL_TO_HIT",""+URL_TO_HIT);
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();

                }

//********************************************************
            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                try {
                    request.setURI(new URI(URL_TO_HIT));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
               // Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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



                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecute",""+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
                    exportVoucherDetail();
                }else {
                    pdVoucher.dismissWithAnimation();
                }
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();


            } else {
                pdVoucher.dismissWithAnimation();
                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTaskDelphiPayment extends AsyncTask<String, String, String> {

        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;
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
                if (!ipAddress.equals("")) {

                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportPAYMENTS";

                }
            } catch (Exception e) {
                //progressDialog.dismiss();

            }



                String ipAddress = "",JsonResponse="";
                Log.e("tagexPORT", "JsonResponse");

                try {
                    ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                }

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost();
                    try {
                        request.setURI(new URI(URL_TO_HIT));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                    // Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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



                    //*******************************************


                } catch (Exception e) {
                }
                return JsonResponse;



        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecute","ExportPAYMENTS"+result);
            pdVoucher.setTitle("Export Payment");
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                exportPaymentPaper();
            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTaskDelphiPaymentPaper extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;
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

//            try {
            try {
                if (!ipAddress.equals("")) {

                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+ "/ExportPAYMENTS_CHECKS";



                    Log.e("URL_TO_HIT",""+URL_TO_HIT);
                }
            } catch (Exception e) {
                //progressDialog.dismiss();

            }

              //  http://localhost:8082/ExportPAYMENTS_CHECKS?CONO=290&JSONSTR={"JSN":[{"COMAPNYNO":"290","VOUYEAR":"2021","VOUNO":"15823","CHECKNO":"654987","BANK":"JORDANBANK","BRANCH":"AlHUSAIN","DUEDATE":"30/04/2021","CHECKAMOUNT":"520","ISPOSTED":"0"}]}

//                // Log.e("ipAdress", "ip -->" + ip);
//                String data = "CONO="+CONO.trim()+"&JSONSTR=" + URLEncoder.encode(vouchersObject.toString(), "UTF-8") ;
//                Log.e("tag_link", "ExportData -->" + link);
//                Log.e("tag_data", "ExportData -->" + data);

                String ipAddress = "",JsonResponse="";
                Log.e("tagexPORT", "JsonResponse");

                try {
                    ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                }

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost();
                    try {
                        request.setURI(new URI(URL_TO_HIT));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                    // Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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



                    //*******************************************


                } catch (Exception e) {
                }
                return JsonResponse;



        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecute","ExportPAYMENTS"+result);

            pdVoucher.setTitle("Export Payment Paper");
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                savePayment();
            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePayment() {
       new  JSONTaskSavePayment().execute();
    }

    private class JSONTaskDelphiAddedCustomer extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;
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

//            try {
            try {
                if (!ipAddress.equals("")) {


                    // URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+salesNo+"&CONO="+CONO;



                    Log.e("URL_TO_HIT",""+URL_TO_HIT);
                }
            } catch (Exception e) {
                //progressDialog.dismiss();

            }


            try {
                //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();
                //  http://localhost:8082/ExportPAYMENTS_CHECKS?CONO=290&JSONSTR={"JSN":[{"COMAPNYNO":"290","VOUYEAR":"2021","VOUNO":"15823","CHECKNO":"654987","BANK":"JORDANBANK","BRANCH":"AlHUSAIN","DUEDATE":"30/04/2021","CHECKAMOUNT":"520","ISPOSTED":"0"}]}

                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportADDED_CUSTOMERS";
                // Log.e("ipAdress", "ip -->" + ip);
                String data = "CONO="+CONO.trim()+"&JSONSTR=" + URLEncoder.encode(vouchersObject.toString(), "UTF-8") ;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);

////
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

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
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecute","ExportPAYMENTS"+result);

            pdVoucher.setTitle("Export Added Customer");

            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                exportTransaction();
            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class JSONTaskDelphiDetail extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
//        SweetAlertDialog pdItem=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdItem = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            pdItem.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pdItem.setTitleText("export");
//            pdItem.setCancelable(false);
//            pdItem.show();
        }

        @Override
        protected String doInBackground(String... params) {

//                try {
                  //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//
                    String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+ "/ExportSALES_VOUCHER_D";
//                   // Log.e("ipAdress", "ip -->" + ip);
//                    String data = "CONO="+CONO.trim()+"&JSONSTR=" + URLEncoder.encode(vouchersObject.toString(), "UTF-8") ;
//                    Log.e("tag_link", "ExportData -->" + link);
//                    Log.e("tag_data", "ExportData -->" + data);
//
//////
//                    URL url = new URL(link);
//
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setDoOutput(true);
//                    httpURLConnection.setDoInput(true);
//                    httpURLConnection.setRequestMethod("POST");
//
//
//
//                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                    wr.writeBytes(data);
//                    wr.flush();
//                    wr.close();
//
//                    InputStream inputStream = httpURLConnection.getInputStream();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    StringBuffer stringBuffer = new StringBuffer();
//
//                    while ((JsonResponse = bufferedReader.readLine()) != null) {
//                        stringBuffer.append(JsonResponse + "\n");
//                    }
//
//                    bufferedReader.close();
//                    inputStream.close();
//                    httpURLConnection.disconnect();
//
//                    Log.e("tag", "ExportData -->" + stringBuffer.toString());
//
//                    return stringBuffer.toString();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (final IOException e) {
//                            Log.e("tag", "Error closing stream", e);
//                        }
//                    }
//                }
//                return null;
            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                try {
                    request.setURI(new URI(link));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                // Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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
                Log.e("JsonResponse", "ExportSerial" + JsonResponse);



                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;
        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            pdItem.dismiss();
            Log.e("onPostExecute","ExportSALES_VOUCHER_D"+result);
            pdVoucher.setTitle("Export SALES_VOUCHER_Detail");
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
//                    Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();
                    saveExpot();
                }


            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveExpot() {
       // http://localhost:8082/SaveVouchers?CONO=290&STRNO=5
        new JSONTaskSaveVouchers().execute();

    }
    private void saveItemSerials() {
        // http://localhost:8082/SaveVouchers?CONO=290&STRNO=5
        new JSONTaskSerials().execute();

    }
    private class JSONTaskSaveVouchers extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
//        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdItem = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            pdItem.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pdItem.setTitleText("export");
//            pdItem.setCancelable(false);
//            pdItem.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            URLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//
//                try {
//                    if (!ipAddress.equals("")) {
                        // URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+salesNo+"&CONO="+CONO;
                        // http://localhost:8082/SaveVouchers?CONO=290&STRNO=5

                        //URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/SaveVouchers?CONO="+CONO+"&STRNO="+SalesManLogin;

            try {
                //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//LINK : http://localhost:8082/ExportITEMSERIALS?CONO=290&JSONSTR={"JSN":[{"VHFNO":"123","STORENO":"5","TRNSDATE":"01/01/2021","TRANSKIND":"1","ITEMNO":"321","SERIAL_CODE":"369258147852211","QTY":"1","VSERIAL":"1","ISPOSTED":"0"}]}
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/SaveVouchers";
                // Log.e("ipAdress", "ip -->" + ip);
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);

////
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

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
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            pdVoucher.setTitle("Saved Vouchers");
            Log.e("onPostExecute",""+result);

            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                exportSerial();
            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTaskSavePayment extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        //        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//LINK : http://localhost:8082/ExportITEMSERIALS?CONO=290&JSONSTR={"JSN":[{"VHFNO":"123","STORENO":"5","TRNSDATE":"01/01/2021","TRANSKIND":"1","ITEMNO":"321","SERIAL_CODE":"369258147852211","QTY":"1","VSERIAL":"1","ISPOSTED":"0"}]}
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+ "/SavePayment";
                // Log.e("ipAdress", "ip -->" + ip);
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);

////
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

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
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            pdVoucher.setTitle("Saved Payments");
            Log.e("onPostExecute",""+result);

            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                exportAddedCustomer();
            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTaskSerials extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        //        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdItem = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            pdItem.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pdItem.setTitleText("export");
//            pdItem.setCancelable(false);
//            pdItem.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/SaveItemSerials";
                // Log.e("ipAdress", "ip -->" + ip);
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

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
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

//            pdVoucher.setTitle("save Serials");
//            pdVoucher.setTitle("finish");
            Log.e("onPostExecute",""+result);
//            pdVoucher.dismissWithAnimation();
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                exportPayment();
            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exportVoucherDetail() {
        getVouchersDetail();
        new JSONTaskDelphiDetail().execute();
    }
    public void exportSerial(){
        getSerialTables();
       new  JSONTask_SerialDelphi().execute();
    }
    public void exportTransaction(){
        getTransactionTables();
        new  JSONTask_TransactionDelphi().execute();
    }

    private void getVouchersDetail() {
        items = mHandler.getAllItems();
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).getIsPosted() == 0) {

                jsonArrayItems.put(items.get(i).getJSONObjectDelphi());

            }

        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayItems);
            Log.e("getVouchersDetail",""+vouchersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getTransactionTables() {

            transactions = mHandler.getAlltransactions();

            jsonArrayTransactions = new JSONArray();
            for (int i = 0; i < transactions.size(); i++)
                if (transactions.get(i).getIsPosted() == 0) {
//                transactions.get(i).setIsPosted(1);
                    jsonArrayTransactions.put(transactions.get(i).getJSONObject());
                }

        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayTransactions);
            Log.e("getTransactionTables",""+vouchersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getSerialTables() {
        try {
            serialModelList = mHandler.getAllSerialItems();
            Log.e("serialModelList",""+serialModelList);
            jsonArraySerial = new JSONArray();
            for (int i = 0; i < serialModelList.size(); i++)
            {
                if(serialModelList.get(i).getIsPosted().equals("0")){
                    jsonArraySerial.put(serialModelList.get(i).getJSONObjectDelphi());
                }
            }
            Log.e("jsonArraySerial",""+jsonArraySerial.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArraySerial);
            Log.e("getSerialetail",""+vouchersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPassowrdSetting() {
        new JSONTask_getPassword().execute();
    }
    private class JSONTask_getPassword extends AsyncTask<String, String, String> {

        public  String passwordValue="";
        String ipAddress="",URL_TO_HIT="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdValidation.setTitleText(context.getResources().getString(R.string.validation));
            pdValidation.setCancelable(false);
            pdValidation.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
                if(!ipAddress.equals(""))
                {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";
                }
            }
            catch (Exception e)
            {
                passwordFromAdmin.setText("2021000");
            }
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                Log.e("rowId","BasicNameValuePair"+passwordValue);
                nameValuePairs.add(new BasicNameValuePair("_ID", "25"));

                nameValuePairs.add(new BasicNameValuePair("PasswordType", "1"));


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
                Log.e("tagUpdate", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }
            //org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();



                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText("2021000");
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                password.setError(null);
                passwordFromAdmin.setText("2021000");
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String impo = "";
            JSONObject result=null;
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("PasswordKeyValue")) {
                    try {
                        result = new JSONObject(s);
                        JSONArray notificationInfo = null;
                        notificationInfo = result.getJSONArray("PasswordKeyValue");
                        JSONObject infoDetail=null;
                        infoDetail = notificationInfo.getJSONObject(0);

                        Log.e("infoDetail","PasswordKeyValue"+infoDetail.get("passwordKey").toString());
                        passwordFromAdmin.setText(infoDetail.get("passwordKey").toString());







                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }



                }

                else {
                    password.setError(null);
                    passwordFromAdmin.setText("2021000");
                    if (s.contains("Not definded id")) {
                        Toast.makeText(context, "Check WebServices Id 25", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else {
                password.setError(null);
                passwordFromAdmin.setText("2021000");}
        }

    }
    private void showDialogError(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.duplicateddata))
                .setContentText(message)

                .show();
    }

    private class JSONTask_SerialDelphi extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;
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
            String link = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim()+"/ExportITEMSERIALS";
//            String link = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + "/ExportITEMSERIALS";

            //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//LINK : http://localhost:8082/ExportITEMSERIALS?CONO=290&JSONSTR={"JSN":[{"VHFNO":"123","STORENO":"5","TRNSDATE":"01/01/2021","TRANSKIND":"1","ITEMNO":"321","SERIAL_CODE":"369258147852211","QTY":"1","VSERIAL":"1","ISPOSTED":"0"}]}
//
//                // Log.e("ipAdress", "ip -->" + ip);
//                String data = "CONO="+CONO.trim()+"&JSONSTR=" + URLEncoder.encode(vouchersObject.toString(), "UTF-8") ;
//                Log.e("tag_link", "ExportData -->" + link);
//                Log.e("tag_data", "ExportData -->" + data);

////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "ExportData -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//********************************************************
            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                try {
                    request.setURI(new URI(link));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
//                Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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
                Log.e("JsonResponse", "ExportSerial" + JsonResponse);



                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;
        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
//            pdVoucher.setTitle("Send Serials");
            Log.e("onPostExecute",""+result);

            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

                saveItemSerials();
            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTask_TransactionDelphi extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

                //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

                URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportTRANSACTIONS";

                String ipAddress = "",JsonResponse="";
                Log.e("tagexPORT", "JsonResponse");

                try {
                    ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                }

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost();
                    try {
                        request.setURI(new URI(URL_TO_HIT));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                    // Log.e("nameValuePairs","JSONSTR"+vouchersObject.toString().trim());


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



                    //*******************************************


                } catch (Exception e) {
                }
                return JsonResponse;



        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecuteTrans",""+result);
            pdVoucher.setTitle("Export Transaction");
            pdVoucher.dismissWithAnimation();
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();
                updatePosted();

            } else {

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePosted() {
        mHandler.updateVoucher();
        mHandler.updateVoucherDetails();
        mHandler.updatePayment();
        mHandler.updatePaymentPaper();
        mHandler.updateAddedCustomers();
        mHandler.updateTransactions();
        mHandler.updateCustomersMaster();
        mHandler.updateSerialTableIsposted();
        mHandler.updateRequestStockMaster();
        mHandler.updateRequestStockDetail();
    }
}
