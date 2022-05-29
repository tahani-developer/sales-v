package com.dr7.salesmanmanager;

import static com.dr7.salesmanmanager.Login.headerDll;
import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.salesMan;

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

import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

// public class RefreshData extends AppCompatActivity {
public class RefreshData {

    private String URL_TO_HIT;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;
    boolean start=false;
    String ipAddress = "", ipWithPort = "", SalesManLogin,userNo,CONO;
    public static List<Customer> customerList = new ArrayList<>();
    public static List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();

    public RefreshData(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");

        SalesManLogin = mHandler.getAllUserNo();
        //Log.e("SalesManLogin", "" + SalesManLogin);
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            ipWithPort = settings.get(0).getIpPort();
            Log.e("ipWithPort", "" + ipWithPort);
            if (makeOrders == 1) {

                userNo = mHandler.getAllSettings().get(0).getStoreNo();
                Log.e("userNo", "getAllSettings==" + userNo);
            } else {
                userNo = mHandler.getAllUserNo();
            }

            CONO = mHandler.getAllSettings().get(0).getCoNo();
        } else {
            Toast.makeText(context, "Check Setting Ip", Toast.LENGTH_SHORT).show();
        }

//        generalMethod=new GeneralMethod(context);
    }

    public void startParsing() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
//            if(settings.get(0).get)
            String ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

          new   JSONTaskDelphi_customer().execute();
//            new SQLTask_unpostVoucher().execute(URL_TO_HIT);
//            new JSONTask().execute(URL_TO_HIT);



        }
    }


    private class SQLTask_unpostVoucher extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {


                String link = URL_TO_HIT;


                String data = null;
                try {
                    data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                            URLEncoder.encode(String.valueOf('4'), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

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
                Log.e("finalJson'4'", finalJson);
                if(finalJson.contains("FAIL"))
                {
                    start=false;
                }
                else
                if(finalJson.contains("SUCCESS"))
                {start=true;}
            } catch (MalformedURLException e) {
                Log.e("import_unpostvoucher", "********ex1"+e.getMessage());
                e.printStackTrace();
            }  catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                        if(start==true) {
            new JSONTask().execute(URL_TO_HIT);
            }
            else{
                Toast.makeText(context, R.string.failStockSoft_export_data, Toast.LENGTH_SHORT).show();

            }

        }
    }





    void storeInDatabase() {
        new SQLTask().execute(URL_TO_HIT);
    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected List<Customer> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                //             URL url = new URL(URL_TO_HIT);
//                connection = url.openConnection();
//                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder buffer = new StringBuilder();
//                String line = null;
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                    break;
//                }

                String link = URL_TO_HIT;


                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('3'), "UTF-8");

                URL url = new URL(link);

                URLConnection conn = url.openConnection();

              /*  HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                try {
                    request.setURI(new URI(link));
                }catch (Exception e)
                {

                }

                HttpResponse response = client.execute(request);*/


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
                try {

                    JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS_BALANCE");
                    customerList.clear();
                    for (int i = 0; i < parentArrayCustomers.length(); i++) {
                        JSONObject finalObject = parentArrayCustomers.getJSONObject(i);
                        Customer Customer = new Customer();
                        Customer.setCustId(finalObject.getString("CUSTID"));
                        Customer.setCashCredit(finalObject.getInt("CASHCREDIT"));
                        Customer.setCreditLimit(finalObject.getDouble("CREDITLIMIT"));
                        customerList.add(Customer);
                    }
                } catch (Exception e) {
                    Log.e("Refresh_data", "" + e.getMessage().toString());
                }

                try {

                    JSONArray parentArrayItemQty = parentObject.getJSONArray("SalesMan_Items_Balance");
                    salesManItemsBalanceList.clear();
                    for (int i = 0; i < parentArrayItemQty.length(); i++) {
                        JSONObject finalObject = parentArrayItemQty.getJSONObject(i);
                        SalesManItemsBalance salesManItemsBalance = new SalesManItemsBalance();
                        salesManItemsBalance.setCompanyNo(finalObject.getString("ComapnyNo"));
                        salesManItemsBalance.setSalesManNo(finalObject.getString("SalesManNo"));
                        salesManItemsBalance.setItemNo(finalObject.getString("ItemNo"));
                        salesManItemsBalance.setQty(finalObject.getDouble("Qty"));

                        salesManItemsBalanceList.add(salesManItemsBalance);
                    }
                } catch (Exception e) {
                    Log.e("Refresh_salesmanItem", "" + e.getMessage().toString());
                }


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
            return customerList;
        }


        @Override
        protected void onPostExecute(final List<Customer> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
//                    Log.e("Customerr", "*****************" + customerList.size());
                storeInDatabase();
            } else {
                Toast.makeText(context, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SQLTask extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            Window window = dialog.getWindow();
            window.setLayout(500, 200);

            pb = (ProgressBar) dialog.findViewById(R.id.progress);

            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            for (int i = 0; i < customerList.size(); i++) {
                mHandler.updateCustomersPayment_Info(customerList.get(i).getCreditLimit(), customerList.get(i).getCashCredit(), customerList.get(i).getCustId());
            }

//
//            List<Item> items = mHandler.getUnPostedItems();
//            for (int i = 0; i < items.size(); i++) {
//                double currentQty_sal = 0;
//                double currentQty_ret = 0;
//                double currentQty=0;
//                for (int k = 0; k < salesManItemsBalanceList.size(); k++) {
//
//                    if (salesManItemsBalanceList.get(k).getItemNo().equals("76178245"))
//                        Log.e("*********azraq", "" + salesManItemsBalanceList.get(k).getQty());
//
//                    if (items.get(i).getItemNo().equals(salesManItemsBalanceList.get(k).getItemNo())
//                            && items.get(i).getSalesmanNo().equals(salesManItemsBalanceList.get(k).getSalesManNo())) {
//
//                        double stockQty = salesManItemsBalanceList.get(k).getQty();
//                        if (items.get(i).getVoucherType() == 504) {
//                            currentQty_sal += items.get(i).getQty();
////                            currentQty = stockQty - items.get(i).getQty();
//                        }
//                        else if (items.get(i).getVoucherType() == 506) {
//                            currentQty_ret += items.get(i).getQty();
////                            currentQty = stockQty + items.get(i).getQty();
////                        salesManItemsBalanceList.get(k).setQty(currentQty);
//                        }
//                        currentQty=stockQty+currentQty_sal-currentQty_ret;
//                        Log.e("qtyQty", "" + currentQty);
//
//                        mHandler.updateSalesManItemBalance(salesManItemsBalanceList.get(k).getSalesManNo(), salesManItemsBalanceList.get(k).getItemNo(), currentQty);
//
//                        break;
//                    }
//
//                }
                for(int k=0 ; k<salesManItemsBalanceList.size( ) ; k++) {
//                    mHandler.updateSalesManItemBalance(salesManItemsBalanceList.get(k).getSalesManNo(),"76178245",salesManItemsBalanceList.get(k).getQty());
                    Log.e("list",""+salesManItemsBalanceList.get(k).getQty());
                    mHandler.updateSalesManItemBalance(salesManItemsBalanceList.get(k).getSalesManNo(),
                            salesManItemsBalanceList.get(k).getItemNo(),salesManItemsBalanceList.get(k).getQty());
            }

            return "Finish Store";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            pb.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }



    private class JSONTaskDelphi_customer extends AsyncTask<String, String, List<Customer>> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(context.getResources().getString(R.string.refresh_customerData));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected List<Customer> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                try {


                    //+custId

                    if (!ipAddress.equals("")) {
                        //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
                        //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                        if (ipAddress.contains(":")) {
                            int ind = ipAddress.indexOf(":");
                            ipAddress = ipAddress.substring(0, ind);
                        }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                        //   URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVanAllData?STRNO=" + SalesManLogin + "&CONO=" + CONO;

                        //URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

                        Log.e("URL_TO_HIT", "getCustomerList=" + URL_TO_HIT);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();

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


                String link = URL_TO_HIT;
                URL url = new URL(link);

                //*************************************

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));

//

                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";
//                Log.e("finalJson***Import", sb.toString());

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                // JsonResponse = sb.toString();

                String finalJson = sb.toString();
//                Log.e("finalJson***Import", finalJson);
                String rate_customer = "";
                String HideVal = "";

                JSONObject parentObject = new JSONObject(finalJson);
                try {
                    JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS");
                    customerList.clear();
                    for (int i = 0; i < parentArrayCustomers.length(); i++) {
                        JSONObject finalObject = parentArrayCustomers.getJSONObject(i);

                        Customer Customer = new Customer();
                        Customer.setCompanyNumber(finalObject.getString("COMAPNYNO"));
                        Customer.setCustId(finalObject.getString("CUSTID"));
                        Customer.setCustName(finalObject.getString("CUSTNAME"));
                        Customer.setAddress(finalObject.getString("ADDRESS"));
//                    if (finalObject.getString("IsSuspended") == null)
                        Customer.setIsSuspended(0);
//                    else
//                        Customer.setIsSuspended(finalObject.getInt("IsSuspended"));
                        Customer.setPriceListId(finalObject.getString("PRICELISTID"));
                        Customer.setCashCredit(finalObject.getInt("CASHCREDIT"));
                        Customer.setSalesManNumber(finalObject.getString("SALESMANNO"));
                        Customer.setCreditLimit(finalObject.getDouble("CREDITLIMIT"));
                        try {
                            Customer.setPayMethod(finalObject.getInt("PAYMETHOD"));
                        } catch (Exception e) {
                            Customer.setPayMethod(-1);

                        }
                        Customer.setCustLat(finalObject.getString("LATITUDE"));
                        Customer.setCustLong(finalObject.getString("LONGITUDE"));


                        try {
                            rate_customer = finalObject.getString("ACCPRC");
                            if (!rate_customer.equals("null"))
                                Customer.setACCPRC(rate_customer);
                            else {
                                Customer.setACCPRC("0");

                            }
                        } catch (Exception e) {
                            Log.e("ImportError", "Null_ACCPRC" + e.getMessage());
                            Customer.setACCPRC("0");

                        }
                        //*******************************
                        try {
                            HideVal = finalObject.getString("HIDE_VAL");
                            if (!HideVal.equals("null") && !HideVal.equals("") && !HideVal.equals("NULL"))
                                Customer.setHide_val(Integer.parseInt(HideVal));
                            else {
                                Customer.setACCPRC("0");

                            }
                            Customer.setCustomerIdText(finalObject.getString("CUSTID"));
                        } catch (Exception e) {
                            Log.e("ImportError", "Null_ACCPRC" + e.getMessage());
                            Customer.setACCPRC("0");

                        }
                        //*******************************

                        customerList.add(Customer);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {

                    JSONArray parentArraySalesMan_Items_Balance = parentObject.getJSONArray("SalesMan_Items_Balance");
                    salesManItemsBalanceList.clear();

                    for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                        JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);
                        // Log.e("salesManItems","GsonSalesMan_Items_Balance"+finalObject.toString());
                        String qty = "";
                        SalesManItemsBalance item = new SalesManItemsBalance();
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setSalesManNo(finalObject.getString("STOCK_CODE"));
                        item.setItemNo(finalObject.getString("ItemOCode"));

                        qty = finalObject.getString("QTY");
                        try {
                            double qtydoubl = Double.parseDouble(qty);
                            item.setQty(qtydoubl);

                        } catch (Exception e) {
                            item.setQty(0);
                            Log.e("Exception", "" + qty);
                        }
//                        item.setQty(finalObject.getDouble("QTY"));

                        salesManItemsBalanceList.add(item);
                    }

                } catch (Exception e) {
                    Log.e("Exception", "GsonSalesMan_Items_Balance" + e.getMessage());
                }


            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                progressDialog.dismiss();
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("CustomerIOException", e.getMessage().toString());
                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("check Connection")
                                .show();


//                        Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            return customerList;
        }


        @Override
        protected void onPostExecute(final List<Customer> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                if (result.size() != 0) {
                    // Log.e("result","storeInDatabase_customer="+result.size());


//                    storeInDatabase_customer();

                    new SQLTask().execute();

                    Toast.makeText(context, "Customers list is ready" + customerList.size(), Toast.LENGTH_SHORT).show();
                } else {

                }
            } else {
                Toast.makeText(context, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void storeInDatabase_customer() {
        // Log.e("storeInDatabase_cust",""+customerList.size());
        if (customerList.size() != 0) {
            mHandler.deleteAllCustomers();
            mHandler.addCustomer(customerList);
        }


    }
}

