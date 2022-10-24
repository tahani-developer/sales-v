package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.InventoryShelf;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import static com.dr7.salesmanmanager.Login.headerDll;

import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.google.gson.JsonObject;

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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.offerTalaat;
import static com.dr7.salesmanmanager.Login.rawahneh;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.Login.userNo;
import static com.dr7.salesmanmanager.MainActivity.fill_Pending_inv;
import static com.dr7.salesmanmanager.MainActivity.password;
import static com.dr7.salesmanmanager.MainActivity.passwordFromAdmin;
import static com.dr7.salesmanmanager.ReturnByVoucherNo.ReturnupdateditemList;
import static com.dr7.salesmanmanager.ReturnByVoucherNo.voucherNo_ReturnNo;

public class ExportJason extends AppCompatActivity {

    public Context context;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments , jsonArrayPaymentsPaper , jsonArrayAddedCustomer,
            jsonArrayTransactions, jsonArrayBalance ,jsonArrayStockRequest,jsonArrayLocation,jsonArraySerial,
            jsonArrayStockRequestMaster,jsonArrayInventory,UpdateloadvanArray,VanRequstsjsonArray;
    DatabaseHandler mHandler;
    JSONObject vouchersObject,VanRequstsjsonobject,Updateloadvanobject;
    public static  SweetAlertDialog pd,pdValidation;
    int taxType=0,importDataAfter=0;
    JSONObject ReturnUpdateObject;
    private JSONArray jsonArrayReturnUpdate;
    SweetAlertDialog pdReturnUpdate;
    public static List<Item>ReturnItemsarrayList=new ArrayList<>();


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
    public  static  List<InventoryShelf> shelflList=new ArrayList<>();
    int  approveAdmin=-1,workOnLine=-1;
    SweetAlertDialog getDataProgress;
    private JsonObjectRequest exportVouMasterRequest;
    private RequestQueue requestQueue;
    public static String CONO="",SalesManLogin;
    SweetAlertDialog pdVoucher=null;
    SweetAlertDialog pdStosk=null,progressSave=null,pdexpo_serial=null;

    boolean isPosted = true;
//    getCustomerLocation
    String ipAddress="",ipWithPort="",URL_TO_HIT="";

    GeneralMethod generalMethod;
    public int exportJustCustomer=0;

    public ExportJason(Context context) throws JSONException {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        exportJustCustomer=0;
        if(mHandler.getAllSettings().size() != 0) {
            ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
            ipWithPort=mHandler.getAllSettings().get(0).getIpPort();
            CONO=mHandler.getAllSettings().get(0).getCoNo();
            taxType=mHandler.getAllSettings().get(0).getTaxClarcKind();
            Log.e("taxType",""+taxType);
        }

        this.requestQueue = Volley.newRequestQueue(context);

        SalesManLogin= mHandler.getAllUserNo();
        fillIpAddressWithPort();
        generalMethod=new GeneralMethod(context);
    }
    public void exportReturnUpdateList() {

        Log.e("exportReturnUpdateList","exportReturnUpdateList");
        getReturnUpdateObject();
        updateVoucherExported();// 3
        pdReturnUpdate = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdReturnUpdate.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdReturnUpdate.setTitleText("EXPORT_RETURN_UPDATE");
        pdReturnUpdate.setCancelable(false);
        pdReturnUpdate.show();

        new JSONTask_ReturnVocherDeatials().execute();
    }


    private void getReturnUpdateObject() {
        ReturnItemsarrayList.clear();
        Log.e("ReturnItemsarrayList===",ReturnItemsarrayList.size()+"getReturnUpdateObject");

        Log.e("getReturnUpdateObject","getReturnUpdateObject");
        ReturnItemsarrayList.clear();
        mHandler.getvocherNumforItemsReturnd();
        jsonArrayReturnUpdate = new JSONArray();
        for (int i = 0; i < ReturnItemsarrayList .size(); i++)        {

                jsonArrayReturnUpdate.put(ReturnItemsarrayList.get(i).getJSONObject3());


        }
        try {
            ReturnUpdateObject =new JSONObject();
            ReturnUpdateObject.put("JSN", jsonArrayReturnUpdate);
            Log.e("ReturnUpdateObject",""+ ReturnUpdateObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void  getVanRequstObject() {
        VanRequstsjsonArray = new JSONArray();
        List<Item>itemsRequsts=mHandler.getStockRequestItems_temp();
        for (int i = 0; i < itemsRequsts.size(); i++)
        {

            VanRequstsjsonArray.put(itemsRequsts.get(i).getJSONObject_VanLoad());

        }
        try {

            VanRequstsjsonobject =new JSONObject();
            VanRequstsjsonobject.put("JSN", VanRequstsjsonArray);
            Log.e("Object","getVanRequstObjec="+ VanRequstsjsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void IIs_SaveVanRequst(){
       getVanRequstObject();
        new JSONTaskIIs_SaveVanRequst().execute();
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
        Log.e("jsonArrayTransactions",""+jsonArrayTransactions.toString());
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

        vouchers = mHandler.getAllVouchers_NotPosted();// from voucher master
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
            {

                    jsonArrayVouchers.put(vouchers.get(i).getJSONObject());

            }

        items = mHandler.getAllItems(0);
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
           {


                   jsonArrayItems.put(items.get(i).getJSONObject());



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


        //****************************************************************
        shelflList = mHandler.getAllINVENTORY_SHELF();
        Log.e("shelflList",""+shelflList.toString());
        jsonArrayInventory = new JSONArray();
        for (int i = 0; i < shelflList.size(); i++)
        {

            jsonArrayInventory.put(shelflList.get(i).getJSONObjectDelphi());

        }
        // Log.e("jsonArrayInventory","="+jsonArrayInventory.getJSONObject(0));

        //*************************************************
       // getData();

       new ExportJason.JSONTask().execute();




    }
    public  void startExport(int expoAndImpo) throws JSONException {
        Log.e("startExport","typaImport"+typaImport);
        importDataAfter=expoAndImpo;//1- from activity balance 0- from all export

        if(typaImport==0)//mysql
        {
            startExportDatabase();
        }
        else if(typaImport==1)
        {
            startExportDelPhi();
        }

    }
    public  void startExportCustomer(){

        exportJustCustomer=1;
        exportAddedCustomer();
    }
    void startExportDelPhi()throws JSONException {

//        startExportDatabase();

        exportVoucherDetail();// 2
//        savePayment();
//        exportVoucherDetail();
//        exportSerial();
//        exportTransaction();
//        exportPayment();
//        exportPaymentPaper();
//        exportAddedCustomer();
    }

    private void exportAddedCustomer() {// 9
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
//            Log.e("getAddedCustomer","JSN"+jsonArrayAddedCustomer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void exportPaymentPaper() {//7
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

    private void exportPayment() {// 6
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

//        pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//        pdVoucher.setTitleText(" Start export Vouchers");
//        pdVoucher.setCancelable(false);
//        pdVoucher.show();
        new JSONTaskDelphi().execute();

    }


    private void getVouchers() {

        vouchers = mHandler.getAllVouchers_NotPosted();// from voucher master
//        Log.e("getVouchersDetail",""+vouchers.size());
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
        {

                jsonArrayVouchers.put(vouchers.get(i).getJSONObjectDelphi());

        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayVouchers);
//            Log.e("vouchersObject",""+vouchersObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getVouchersStockRequest_M() {
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
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayStockRequestMaster);
            Log.e("StockRequestMaster","="+jsonArrayStockRequestMaster.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void getStockRequest_D() {
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

        //********************************************
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayStockRequest);
            Log.e("StockRequestDetail","="+jsonArrayStockRequest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void saveVouchersAndExport() {
        Log.e("saveVouchersAndExport","saved");
        saveExpot();
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
                nameValuePairs.add(new BasicNameValuePair("SHELF_INVENTORY", jsonArrayInventory.toString().trim()));

                Log.e("SHELF_INVENTORY",""+jsonArrayInventory.toString());


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

            Log.e("onPostExecute","onPostExecute");
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
                    mHandler.updateInventoryShelf();
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
        if(workOnLine==1&&typaImport==1) {
            isPosted=mHandler.isAllVoucher_posted();
            if(makeOrders==1)
            {   userNo=  mHandler.getAllSettings().get(0).getStoreNo();}

                if(isPosted==true)
                {
                    ImportJason obj = new ImportJason(context);
                    if(mHandler.getAllSettings().get(0).getItemUnit()==1)
                    {
                        obj.startParsing(userNo);

                    }else {
                        obj.getItemBalance(userNo);
                    }


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
//                Log.e("JsonResponse", "ExporVoucher" + JsonResponse);



                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecute","---1---"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {

                    exportReturnUpdateList();
                }else {
                    pdVoucher.dismissWithAnimation();
                }
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();


            } else {
                pdVoucher.dismissWithAnimation();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("check Connection")
                                .show();

                    }
                });
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
            pdVoucher.setTitle("Export Payment");
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

            Log.e("onPostExecute","ExportPAYMENTS---6--"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {
                    mHandler.updatePayment();
                }



            } else {
                pdVoucher.dismissWithAnimation();

            }
            exportPaymentPaper();// 7
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
                     Log.e("nameValuePairs","Payments=_JSONSTR"+vouchersObject.toString().trim());


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
            Log.e("onPostExecute","ExportPAYMENTS---7---"+result);

            pdVoucher.setTitle("Export Payment Paper");
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();
                if(result.contains("Saved Successfully")) {

                   // pdVoucher.setTitle("Saved Serial");

                    updatePayment();// 8


                }


            } else {
                pdVoucher.dismissWithAnimation();
            }
            exportAddedCustomer(); // 9
        }
    }

    private void savePayment() {// 17
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
            if(exportJustCustomer==1)
            {
                pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
                pdVoucher.setTitleText("export Customers");
                pdVoucher.setCancelable(false);
                pdVoucher.show();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            BufferedReader reader = null;

                String  JsonResponse="",link="";


                try {
                     link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportADDED_CUSTOMERS";
                    Log.e("tagexPORT", "Added==Jsonlink"+link);
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
                  //  Log.e("nameValuePairs","added=_JSONSTR"+vouchersObject.toString().trim());


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
        protected void onPostExecute( String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();


            pdVoucher.setTitle("Export Added Customer");
//"ErrorCode":"1","ErrorDesc":"ORA-12899: value too large for column "A2022_295"."VE_ADDED_CUSTOMERS"."REMARK" (actual: 51, maximum: 45)"}
            if (result != null && !result.equals("")) {


                if(result.contains("Saved Successfully")) {
                    updateAddedCustomer();// 10

                   // Toast.makeText(context, context.getResources().getString(R.string.addCusttomerSucssesfuly), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context, "Error in Saving  Customer ="+result, Toast.LENGTH_LONG).show();
                }


            } else {
                pdVoucher.dismissWithAnimation();

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
            if(exportJustCustomer==1)
            {
                pdVoucher.dismissWithAnimation();
                exportJustCustomer=0;
                if (typaImport == 1)//IIOs
                {
                    ImportJason importJason =new ImportJason(context);
                    importJason.getCustomerData();
                }

            }else {
                exportTransaction(); // 11
            }

        }
    }
    public String convertStandardJSONString(String data_json){
        data_json = data_json.replace("\"", "");
//        Log.e("");
//        data_json = data_json.replace("\"{", "{");
//        data_json = data_json.replace("}\",", "},");
//        data_json = data_json.replace("}\"", "}");
        return data_json;
    }
    private void updateAddedCustomer() {
        mHandler.updateAddedCustomers();
        Log.e("onPostExecute","updateAddedCustomer---10---");
    }


    private class JSONTaskDelphiDetail extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
//        SweetAlertDialog pdItem=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdVoucher.setTitleText(" Start export Vouchers");
        pdVoucher.setCancelable(false);
        pdVoucher.show();
        }

        @Override
        protected String doInBackground(String... params) {

                    String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+ "/ExportSALES_VOUCHER_D";
            Log.e("link==", link+"");

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
                    Log.e("ExportSALES_VOUCHER_DEURISyntaxException",e.getMessage()+"");
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                 Log.e("nameValuePairs","Details=JSONSTR"+vouchersObject.toString().trim());


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
                Log.e("ExportSALES_VOUCHER_DException",e.getMessage()+"");
            }
            return JsonResponse;
        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            Log.e("onPostExecute","---2---__");
            Log.e("onPostExecute,result",result+"");
//            pdItem.dismiss();
           // Log.e("onPostExecute","ExportSALES_VOUCHER_D"+result);
            pdVoucher.setTitle("Export SALES_VOUCHER_Detail");
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
//                    Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();


//                    updateVoucherExported();// 3

                    exportSalesVoucherM();// 1


                }
                else {
                    Log.e("elseExportSALE","---3---__");
                    pdVoucher.dismissWithAnimation();
                Toast.makeText(context, "Export SALES_VOUCHER_Detail= "+result.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.e("elseExportSALES_VOUCHER_D","---3---__");
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void saveExpot() {//15
       // http://localhost:8082/SaveVouchers?CONO=290&STRNO=5
        new JSONTaskSaveVouchers().execute();

    }
    private void saveLoadVan() {//15
        // http://localhost:8082/SaveVouchers?CONO=290&STRNO=5
        new JSONTaskSaveLoadVan().execute();

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
            progressSave = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressSave.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            progressSave.setTitleText(" Save Vouchers");
            progressSave.setCancelable(false);
            progressSave.show();

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
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin+"&VHFTYPE="+taxType;
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
            progressSave.setTitle("Saved Vouchers");
            Log.e("onPostExecute","---15----"+result);

            if (result != null && !result.equals("")) {


            } else {
                progressSave.dismissWithAnimation();

            }
            saveItemSerials();// 16
        }
    }
    private class JSONTaskSaveLoadVan extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        //        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    progressSave = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    progressSave.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
                    progressSave.setTitleText(" Save Load");
                    progressSave.setCancelable(false);
                    progressSave.show();
                }
            });
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
                String link="";
//                if(offerTalaat==0)
                 link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/SaveLoadVan";
//                else
//                  link = "http://" + ipAddress+":"+ipWithPort +  headerDll.trim() +"/SaveTempLoadVan";

                // Log.e("ipAdress", "ip -->" + ip);
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;
                Log.e("JSONTaskSaveLoadVan", "ExportData -->" + link);
                Log.e("JSONTaskSaveLoadVan", "ExportData -->" + data);

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
            progressSave.setTitle("Saved Load");
            progressSave.dismissWithAnimation();
            Log.e("onPostExecute","---15----"+result);

            if (result != null && !result.equals("")) {


            } else {
                progressSave.dismissWithAnimation();

            }
            if(generalMethod.isNetworkAvailable())
                exportStock(0);// 18
            else {
                generalMethod.showSweetDialog(context,0,""+context.getResources().getString(R.string.checkinternetConnection),"");
            }

        }
    }
    private void updateVoucherExported() {// 3
        Log.e("updateVoucherExported","trueee");
        mHandler.updateVoucher();
        mHandler.updateVoucherDetails();
        Log.e("onPostExecute","updateVoucherExported---3---");
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
            try {
                progressSave.setTitle(" save Payment");
            }catch (Exception e){}

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
                progressSave.dismissWithAnimation();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        progressSave.dismissWithAnimation();
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            Log.e("onPostExecute","---17----"+result);
            progressSave.dismissWithAnimation();
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();
                if(result.contains("Saved Successfully")) {


                }
            } else {
                progressSave.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
            if(offerTalaat==0)
            {
                exportLoadVan_temp();

            }else// لمحمد طلعت  من ال item balance
            exportLoadVan();

        }
    }

    private void updatePayment() {// 8

        mHandler.updatePaymentPaper();
        Log.e("onPostExecute","updateSerialTableIsposted---8---");
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
            try {
                progressSave.setTitle(" save Serials");
            }catch (Exception e){}

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
            Log.e("onPostExecute","---16----"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {


                }
            } else {
                progressSave.dismissWithAnimation();

//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
            savePayment();// 17
        }
    }

    private void updateSerial() {// 5
        mHandler.updateSerialTableIsposted();
        Log.e("onPostExecute","updateSerialTableIsposted---5---");
    }

    private void exportVoucherDetail() {//2
        getVouchersDetail();
        new JSONTaskDelphiDetail().execute();
    }
    public void exportSerial(){ // 4
//        pdVoucher.setTitle("Export Serial");
        getSerialTables();
       new  JSONTask_SerialDelphi().execute();
    }
    public void exportTransaction(){ // 11
        getTransactionTables();
        new  JSONTask_TransactionDelphi().execute();
    }
    public void exportInventorySh(){// 13
        getInventoryShelfTables();
        new  JSONTask_InventoryShelfDelphi().execute();
    }
    public void exportLoadVan(){// 13
        getLoadVanBalance();


        new  JSONTask_LoadVanDelphi().execute(); //14
    }
    public void exportLoadVan_temp(){// 13
        getVanRequstObject();
        new  JSONTask_LoadVanDelphi().execute(); //14
    }

    private void getLoadVanBalance() {
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
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayBalance);
            Log.e("salesManItemsBalan",""+jsonArrayBalance.length()+"\t"+jsonArrayBalance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void exportStockRequestMaster(){// 13
        getVouchersStockRequest_M();
        new  JSONTask_InventoryShelfDelphi().execute();
    }

    private void getVouchersDetail() {
        items = mHandler.getAllItems(0);
//        Log.e("getVouchersDetail",""+items.size());
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
        {

                jsonArrayItems.put(items.get(i).getJSONObjectDelphi());

        }
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayItems);

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

    private void getInventoryShelfTables() {
        shelflList = mHandler.getAllINVENTORY_SHELF();
        Log.e("shelflList",""+shelflList);
        jsonArrayInventory = new JSONArray();
        for (int i = 0; i < shelflList.size(); i++)
        {

            jsonArrayInventory.put(shelflList.get(i).getJSONObjectDelphi());

        }

        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayInventory);
//            Log.e("getSerialetail",""+vouchersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPassowrdSetting() {
        try {

//            new JSONTask_getPassword().execute();
            new JSONTask_IIsgetPassword().execute();

        }
        catch (Exception e)
        {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    password.setError(null);
                    passwordFromAdmin.setText(Login.password_setting);
                    Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                }
            });
        }

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
            String JsonResponse = null;
            try {

                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
                if(!ipAddress.equals("")) {
                    if (typaImport == 0)
                        URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";
                    else {
                        URL_TO_HIT = "http://" + ipAddress + ":" + ipWithPort + headerDll.trim() + "/ADMGetPassword?CONO=" + CONO + "&PASSWORDTYPE=1";

                    }
                }
                Log.e("URL_TO_HIT","ADMGetPassword="+URL_TO_HIT);
            }
            catch (Exception e)
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText(Login.password_setting);

                    }
                });
            }
            try {


                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));




                Log.e("rowId","BasicNameValuePair"+passwordValue);
                if (typaImport == 0)
                {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("_ID", "25"));

                    nameValuePairs.add(new BasicNameValuePair("PasswordType", "1"));
                    request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                }

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




            }
            //org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();



                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText(Login.password_setting);
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText(Login.password_setting);
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return JsonResponse;
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

                        Log.e("infoDetail","2-PasswordKeyValue"+infoDetail.get("passwordKey").toString());
                        passwordFromAdmin.setText(infoDetail.get("passwordKey").toString());


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }



                }
                else {

                    password.setError(null);
                    passwordFromAdmin.setText(Login.password_setting);
                    if (s.contains("Not definded id")) {
                        Toast.makeText(context, "Check WebServices Id 25", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else {
                password.setError(null);
                passwordFromAdmin.setText(Login.password_setting);}
        }

    }


    private class JSONTask_IIsgetPassword extends AsyncTask<String, String, String> {

        public  String passwordValue="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdValidation.setTitleText(context.getResources().getString(R.string.process));
            pdValidation.setCancelable(false);
            pdValidation.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                if(!ipAddress.equals(""))
                {  URL_TO_HIT = "http://" + ipAddress+":"+ipWithPort +  headerDll.trim() +"/ADMGetPassword?CONO="+CONO+"&PASSWORDTYPE=1";

                    Log.e("URL_TO_HIT",URL_TO_HIT);
                }
            }
            catch (Exception e)
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText(Login.password_setting);

                    }
                });
            }
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new  HttpGet();
                request.setURI(new URI(URL_TO_HIT));
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
                        passwordFromAdmin.setText(Login.password_setting);
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();


                    }
                });


                return null;
            } catch (Exception e) {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText(Login.password_setting);
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();


                    }
                });

                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String respon) {
            super.onPostExecute(respon);
            pdValidation.dismissWithAnimation();
            if (respon!= null) {
                Log.e("respon",respon);
                            if (respon.contains("PASSWORDTYPE")) {
                                try {
                                    JSONArray notificationInfo = null;
                                    notificationInfo = new JSONArray(respon);
                                    JSONObject infoDetail=null;
                                    infoDetail = notificationInfo.getJSONObject(0);
                                    Log.e("infoDetail","2-PasswordKeyValue"+infoDetail.get("PASSWORDKEY").toString());
                                    passwordFromAdmin.setText(infoDetail.get("PASSWORDKEY").toString());


                                } catch (JSONException e) {
//                        progressDialog.dismiss();
                                    e.printStackTrace();
                                }

                            }
                            else {

                                password.setError(null);
                                passwordFromAdmin.setText(Login.password_setting);

                            }

            }
            else {
                password.setError(null);
                passwordFromAdmin.setText(Login.password_setting);
            }
        }


    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
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

        }

        @Override
        protected String doInBackground(String... params) {

            String link = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim()+"/ExportITEMSERIALS";

            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                pdVoucher.dismissWithAnimation();
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
            Log.e("onPostExecute","updateVoucherExported---4---");
          //  Log.e("onPostExecute","Serial updateVoucherExported---4---"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
                    pdVoucher.setTitle("Saved Serial");


                    updateSerial();// 5

                  //  saveItemSerials();
                }


            } else {
                pdVoucher.dismissWithAnimation();

            }
            exportPayment();// 6
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
            Log.e("onPostExecuteTrans","JSONTask_TransactionDelphi---11---"+result);
            pdVoucher.setTitle("Export Transaction");

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {

                    updateExporttransaction(); // 12

                }


            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
            exportInventorySh();// 13
        }
    }

    private void updateExporttransaction() {
        mHandler.updateTransactions();
        Log.e("onPostExecute","updateExporttransaction---12---");

    }

    private class JSONTask_InventoryShelfDelphi extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/EXPORTDROPPRICE";

            String JsonResponse="";
            Log.e("tagexPORT", "JsonResponseEXPORT_DROPPRICE");
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
            Log.e("onPostExecuteTrans","JSONTask_InventoryShelfDelphi---13"+result);
            pdVoucher.setTitle("Export Transaction");
//            pdVoucher.dismissWithAnimation();
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
                    mHandler.updateInventoryShelf();
                }


            }
            updatePosted(); //14
//            exportLoadVan();

        }
    }
    private class JSONTask_LoadVanDelphi extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/EXPORTLOADVAN";

            String JsonResponse="";
            Log.e("tagexPORT", "EXPORT_LOADVAN");
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
                if(offerTalaat==0){
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", VanRequstsjsonobject.toString().trim()));
                }else {
                    nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                }
//



                 Log.e("nameValuePairs","JSONSTR1"+VanRequstsjsonobject.toString().trim());
                Log.e("nameValuePairs","JSONSTR2"+vouchersObject.toString().trim());



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
            Log.e("onPostExecuteTrans","JSONTask_EXPORTLOADVAN---132"+result);
//            pdVoucher.setTitle("Export LOADVAN");
//            pdVoucher.dismissWithAnimation();
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {



                        mHandler.updateRequestStockMaster();
                        mHandler.updateRequestStockDetail();
                        //   RequstReport. exportrespon.setText("Saved Successfully");


                }


            }
                saveLoadVan();


//            updatePosted(); //14
        }
    }
    private void updatePosted() {


        mHandler.updateCustomersMaster();

       // mHandler.updateRequestStockMaster();
       // mHandler.updateRequestStockDetail();
        IIs_SaveVanRequst();

        Log.e("onPostExecute","updatePostedAll---14---");


    }
     public  void exportStock(int flag){//18
                if(rawahneh==1)
        {

         if(flag==0)
            new JSONTaskEXPORT_STOCK(flag).execute();
         else {
             new JSONTask_RE_EXPORT_STOCK(flag).execute();

         }
        }
         else {
                    if(importDataAfter==0)
                    {
                        if(workOnLine==1)
                        {
                            getData();
                        }else {
                            showSavedSucces();
                        }
                    }  else if(importDataAfter==2)
                    {
                        ImportJason obj = new ImportJason(context);
                        obj.getSerialData(voucherNo_ReturnNo);
                    }
                    if(flag==2)
                    {
                        new JSONTask_RE_EXPORT_STOCK(flag).execute();

                    }
                }
    }
    private class JSONTask_RE_EXPORT_STOCK extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        int flag_export=0;//0 from normal export /// 2 from dialog  re export

        public JSONTask_RE_EXPORT_STOCK(int floag) {
            this.flag_export=floag;
        }

        @Override
        protected void onPreExecute() {

            pdStosk = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdStosk.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdStosk.setTitleText(" Export Stock ");
            pdStosk.setCancelable(false);
            pdStosk.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //http://localhost:8082/EXPORTTOSTOCK?CONO=295&STRNO=4
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/EXPORTTOSTOCK";
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin+"&VHFTYPE="+taxType;
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

            Log.e("onPostExecute","EXPORT_STOCK---18----"+result);
            pdStosk.dismissWithAnimation();
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {
                    Log.e("Re_EXPORT_STOCK","result_start");
                    fill_Pending_inv.setText("refresh");

                }


            } else {
                pdStosk.dismissWithAnimation();
            }


        }
    }
    private class JSONTaskEXPORT_STOCK extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        int flag_export=0;//0 from normal export /// 2 from dialog  re export

        public JSONTaskEXPORT_STOCK(int floag) {
            this.flag_export=floag;
        }

        @Override
        protected void onPreExecute() {

            pdStosk = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdStosk.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdStosk.setTitleText(" Export Stock ");
            pdStosk.setCancelable(false);
            pdStosk.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //http://localhost:8082/EXPORTTOSTOCK?CONO=295&STRNO=4
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/EXPORTTOSTOCK";
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin+"&VHFTYPE="+taxType;
                Log.e("tag_link", "ExportData -->" + link);Log.e("tag_data", "ExportData -->" + data);


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

            Log.e("onPostExecute","EXPORT_STOCK---18----"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {
                    Log.e("EXPORT_STOCK","result_start");
                    pdStosk.setTitle("export Payment start");

                }


            } else {
                pdStosk.dismissWithAnimation();
            }


            if(generalMethod.isNetworkAvailable())
                new  JSONTaskExportItem_Serial(flag_export).execute();// 19
            else {
                generalMethod.showSweetDialog(context,0,""+context.getResources().getString(R.string.checkinternetConnection),"");
            }



        }
    }
    public  void  exportSerial_stock(){// just for serial
        new  JSONTaskExportItem_Serial(1).execute();// 19
    }

    private class JSONTaskExportItem_Serial extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        public  int typeExport=0;//2 from dialog


        public JSONTaskExportItem_Serial(int type) {
            this.typeExport=type;
        }

        @Override
        protected void onPreExecute() {


            if(typeExport==0||typeExport==2)
            pdStosk.setTitleText(" Export Item_Serial ");
            else {
                pdexpo_serial= new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pdexpo_serial.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
                pdexpo_serial.setTitleText(" Export Serial Stock ");
                pdexpo_serial.setCancelable(false);
                pdexpo_serial.show();

            }

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //http://localhost:8082/EXPORTTOSTOCK?CONO=295&STRNO=4
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportItem_Serial";
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;
                Log.e("ExportItem_Serial", "ExportData -->" + link);

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

            Log.e("onPostExecute","EXPORT_STOCK---18----"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {

                    Log.e("EXPORT_STOCK","result_start");
                    if(typeExport==0||typeExport==2)
                    pdStosk.setTitle("export Payment start");
                    else {
                        fill_Pending_inv.setText("refresh");
//                        showSavedSucces();
                    }

                }


            } else {
                if(typeExport==0||typeExport==2)
                pdStosk.dismissWithAnimation();
            }
            if(typeExport==0||typeExport==2)
            new  JSONTaskEXPORT_STOCK_Payment(typeExport).execute();// 19
            else pdexpo_serial.dismissWithAnimation();

        }
    }
    private class JSONTaskEXPORT_STOCK_Payment extends AsyncTask<String, String, String> {// 19
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        int type_Export=0;// 0 fromexport pending data //1 from normal

        public JSONTaskEXPORT_STOCK_Payment(int expo) {
            this.type_Export=expo;
            Log.e("JSONTaskEXPORT_STOCK_Payment",""+type_Export);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //http://localhost:8082/EXPORTTOSTOCK?CONO=295&STRNO=4
                //ExportPaymentToSTK?CONO=295&STRNO=4
                String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() + headerDll.trim()+"/ExportPaymentToSTK";
                String data = "CONO="+CONO.trim()+"&STRNO=" +SalesManLogin;


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
                pdStosk.dismissWithAnimation();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        pdStosk.dismissWithAnimation();
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            Log.e("onPostExecute","EXPORTpayment=19---"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {
                    pdStosk.setTitle("EXPORT_payment Successfully");
                    pdStosk.dismissWithAnimation();
                    if(importDataAfter!=2)
                    showSavedSucces();
                }

            } else {
            }
            pdStosk.dismissWithAnimation();
            Log.e("importDataAfter",""+importDataAfter);
            if(importDataAfter==0&&type_Export==0)
            getData();
            else if(importDataAfter==2&&type_Export==0)
            {Log.e("importDataAfter","else"+importDataAfter);
                ImportJason obj = new ImportJason(context);
                obj.getSerialData(voucherNo_ReturnNo);
            }
        }
    }

    private void showSavedSucces() {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getResources().getString(R.string.saveSuccessfuly))
                .show();
        ImportJason obj = new ImportJason(context);

            obj.getInitialDataPending(context);

    }
    public class JSONTask_ReturnVocherDeatials extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected String doInBackground(String... params) {
            String  link="";
            URLConnection connection = null;
            BufferedReader reader = null;

            try {
                if (!ipAddress.equals("")) {

               link = "http://"+ipAddress.trim()+":"+ipWithPort+headerDll.trim()+"/UpdateReturn";



                    Log.e("URL_TO_HIT",""+link);
                }
            } catch (Exception e) {
                //progressDialog.dismiss();
              pdReturnUpdate.dismiss();

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
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", ReturnUpdateObject.toString().trim()));
                Log.e("URL_TO_HIT222",""+ ReturnUpdateObject.toString());
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("JsonResponse", "Exporship" + JsonResponse);


            }
            catch (Exception e) {
                pdReturnUpdate.dismiss();
            }
            return JsonResponse ;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Log.e("onPostExecuteReturn",""+result);


            pdReturnUpdate.dismiss();
            if (result != null && !result.equals("")) {

                if(result.contains("Internal Application Error")){




                }else

                if(result.contains("Saved Successfully"))
                {




                }
                else

                {

                }

                // exportReplacementList(listAllReplacment);


            }
            else{

            }

            exportSerial();// 4

        }
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();




    }

    private class JSONTaskIIs_SaveVanRequst extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

//            ipAddress = "";
            try {


                if (!ipAddress.equals("")) {

//                    URL_TO_HIT = "http://" + ipAddress+":"+ipWithPort +  headerDll.trim() +"/SaveTempLoadVan";
                    URL_TO_HIT = "http://" + ipAddress+":"+ipWithPort +  headerDll.trim() +"/EXPORTLOADVAN";
                    Log.e("URL_TO_HIT",URL_TO_HIT);
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));
//
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR",VanRequstsjsonobject.toString().trim()));


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
            pdVoucher.dismissWithAnimation();
            if (s != null) {
                if (s.contains("Saved Successfully")) {


//                    mHandler.updateRequestStockMaster();
//                    mHandler.updateRequestStockDetail();
                 //   RequstReport. exportrespon.setText("Saved Successfully");

                }
                else {

                }
//                progressDialog.dismiss();
            }
            saveExpot(); //15
        }

    }
}
