package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExportJason extends AppCompatActivity {

    private Context context;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments , jsonArrayPaymentsPaper , jsonArrayAddedCustomer,
            jsonArrayTransactions, jsonArrayBalance ,jsonArrayStockRequest,jsonArrayLocation;
    DatabaseHandler mHandler;

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
//    getCustomerLocation

    public ExportJason(Context context) throws JSONException {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
    }


    void startExportDatabase() {
        customerLocationList = mHandler.getCustomerLocation();
        jsonArrayLocation = new JSONArray();
        for (int i = 0; i < customerLocationList.size(); i++)
        {
            customerLocationList.get(i).getCUS_NO();
            customerLocationList.get(i).getLATIT();

            customerLocationList.get(i).getLONG();
            jsonArrayLocation.put(customerLocationList.get(i).getJSONObject());
        }

        //******************************************
        transactions = mHandler.getAlltransactions();
        jsonArrayTransactions = new JSONArray();
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getIsPosted() == 0) {
                transactions.get(i).setIsPosted(1);
                jsonArrayTransactions.put(transactions.get(i).getJSONObject());
            }
        //******************************************
        salesManItemsBalanceList=mHandler.getSalesManItemsBalance(Login.salesMan);
        jsonArrayBalance=new JSONArray();

        for (int i = 0; i < salesManItemsBalanceList.size(); i++)
        {
            salesManItemsBalanceList.get(i).getSalesManNo();
            salesManItemsBalanceList.get(i).getItemNo();
            salesManItemsBalanceList.get(i).getQty();
            jsonArrayBalance.put(salesManItemsBalanceList.get(i).getJSONObject());
        }
        //******************************************
        stockRequestListList=mHandler.getAllStockRequestItems();
        jsonArrayStockRequest=new JSONArray();

        for (int i = 0; i < stockRequestListList.size(); i++)
        {

            jsonArrayStockRequest.put(stockRequestListList.get(i).getJSONObject());
        }
        //********************************************

        vouchers = mHandler.getAllVouchers();
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
            if (vouchers.get(i).getIsPosted() == 0)
            {
                vouchers.get(i).setIsPosted(1);
//                vouchers.get(i).setRemark("\\%");
                jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
            }

        items = mHandler.getAllItems();
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getIsPosted() == 0) {
                items.get(i).setIsPosted(1);
               // Log.e("getDescription",""+items.get(i).getDescription());
                jsonArrayItems.put(items.get(i).getJSONObject());
                //Log.e("getJSONObject",""+items.get(i).getJSONObject());
            }

//        try {
//            Log.e("export****" , jsonArrayItems.get(jsonArrayItems.length()-1).toString().trim());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        payments = mHandler.getAllPayments();
        jsonArrayPayments = new JSONArray();
        for (int i = 0; i < payments.size(); i++)
            if (payments.get(i).getIsPosted() == 0) {
                payments.get(i).setIsPosted(1);
                jsonArrayPayments.put(payments.get(i).getJSONObject());
            }

        paymentsPaper = mHandler.getAllPaymentsPaper();
        jsonArrayPaymentsPaper = new JSONArray();
        for (int i = 0; i < paymentsPaper.size(); i++)
            if (paymentsPaper.get(i).getIsPosted() == 0) {
                paymentsPaper.get(i).setIsPosted(1);
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
            }

        addedCustomer = mHandler.getAllAddedCustomer();
        jsonArrayAddedCustomer = new JSONArray();
        for (int i = 0; i < addedCustomer.size(); i++)
            if (addedCustomer.get(i).getIsPosted() == 0) {
                addedCustomer.get(i).setIsPosted(1);
                jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObject());
            }

        new ExportJason.JSONTask().execute();



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

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost  request = new HttpPost ();
                request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "2"));
                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_M", jsonArrayVouchers.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_D", jsonArrayItems.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments", jsonArrayPayments.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments_Checks", jsonArrayPaymentsPaper.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Added_Customers", jsonArrayAddedCustomer.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("TABLE_TRANSACTIONS", jsonArrayTransactions.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("LOAD_VAN", jsonArrayBalance.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CUSTOMER_LOCATION", jsonArrayLocation.toString().trim()));

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
                Log.e("tag", "" + JsonResponse);

                return JsonResponse;


            }catch (Exception e)
            {
                e.printStackTrace();
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

            String impo="";
            if(s != null) {
                if (s.contains("SUCCESS")) {
                    mHandler.updateVoucher();
                    mHandler.updateVoucherDetails();
                    mHandler.updatePayment();
                    mHandler.updatePaymentPaper();
                    mHandler.updateAddedCustomers();
                    mHandler.updateTransactions();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
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
                    Log.e("ImpoException",""+e.getMessage().toString());

                }

            }
            progressDialog.dismiss();
        }
    }
}
