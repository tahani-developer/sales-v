package com.dr7.salesmanmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
//import android.support.design.widget.TabLayout;
//import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Reports.SalesMan;
import com.google.gson.Gson;
import com.sewoo.request.android.RequestHandler;

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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.AccountStatment.getAccountList_text;

public class ImportJason extends AppCompatActivity {

    private String URL_TO_HIT;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;

    public static List<Customer> customerList = new ArrayList<>();
    public static List<ItemUnitDetails> itemUnitDetailsList = new ArrayList<>();
    public static List<ItemsMaster> itemsMasterList = new ArrayList<>();
    public static List<PriceListD> priceListDpList = new ArrayList<>();
    public static List<PriceListM> priceListMpList = new ArrayList<>();
    public static List<SalesTeam> salesTeamList = new ArrayList<>();
    public static List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
    public static List<SalesManAndStoreLink> salesManAndStoreLinksList = new ArrayList<>();
    public static List<SalesMan> salesMenList = new ArrayList<>();
    public static List<CustomerPrice> customerPricesList = new ArrayList<>();
    public static List<SalesmanStations> salesmanStationsList = new ArrayList<>();
    public static List<Offers> offersList = new ArrayList<>();
    public static List<QtyOffers> qtyOffersList = new ArrayList<>();
    public static List<ItemsQtyOffer> itemsQtyOfferList = new ArrayList<>();
    public static List<Account_Report> account_reportList = new ArrayList<>();
    public static ArrayList<Account__Statment_Model> listCustomerInfo = new ArrayList<Account__Statment_Model>();

    boolean start = false;
    String ipAddress = "";

    public ImportJason(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
        }
    }

    public void getCustomerInfo() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            Log.e("getCustomerInfo", "*****");
            new JSONTask_AccountStatment(CustomerListShow.Customer_Account).execute();
        }

    }

    public void startParsing() {

        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
//            if(mHandler.getAllSettings().get(0).getAllowOutOfRange()==1)// validate customer location
//            {
//                checkUnpostedCustomer();
//
//
//            }
//            else {
            new JSONTask().execute(URL_TO_HIT);
//            }
        }


    }

    private void checkUnpostedCustomer() {
        new SQLTask_unpostVoucher().execute(URL_TO_HIT);
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
            String ipAddress = "";
            String finalJson = "";

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                Toast.makeText(ImportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

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

                finalJson = sb.toString();
                Log.e("finalJson", "********ex1" + finalJson);

            } catch (MalformedURLException e) {
                Log.e("import_unpostvoucher", "********ex1" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return finalJson;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.contains("FAIL")) {
                start = false;
            } else if (s.contains("SUCCESS")) {
                start = true;
                new JSONTask().execute(URL_TO_HIT);

            }
//            if(start==true)
//            {
//                new JSONTask().execute(URL_TO_HIT);
//            }
//            else{
//                Toast.makeText(context, R.string.failStockSoft_export_data, Toast.LENGTH_SHORT).show();
//
//            }
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        }
    }

    public void updateLocation(JSONObject jsonObject) {
        new JSONTask_UpdateLocation(jsonObject).execute();
    }

    void storeInDatabase() {
        new SQLTask().execute(URL_TO_HIT);
    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        @Override
        protected void onPreExecute() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                URL url = new URL(link);

                //*************************************
                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
//                test= " still good";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data= URLEncoder.encode("username ", "UTF-8")+"="+URLEncoder.encode(username , "UTF-8")+"&"
////                        +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password , "UTF-8");
                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('1'), "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                reader = new BufferedReader(new
                        InputStreamReader(httpsURLConnection.getInputStream()));


                StringBuffer sb = new StringBuffer();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                //*************************************
//                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
//                        URLEncoder.encode(String.valueOf('1'), "UTF-8");
//
//                URL url = new URL(link);
//
//                URLConnection conn = url.openConnection();
//
//
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(data);
//                wr.flush();
//
//
//                reader = new BufferedReader(new
//                        InputStreamReader(conn.getInputStream()));
//
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                // Read Server Response
//                while((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }

                String finalJson = sb.toString();
                Log.e("finalJson***Import", finalJson);
                String rate_customer = "";
                String HideVal = "";

                JSONObject parentObject = new JSONObject(finalJson);
                try {
                    JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS");
                    customerList.clear();
                    for (int i = 0; i < parentArrayCustomers.length(); i++) {
                        JSONObject finalObject = parentArrayCustomers.getJSONObject(i);

                        Customer Customer = new Customer();
                        Customer.setCompanyNumber(finalObject.getInt("ComapnyNo"));
                        Customer.setCustId(finalObject.getString("CustID"));
                        Customer.setCustName(finalObject.getString("CustName"));
                        Customer.setAddress(finalObject.getString("Address"));
//                    if (finalObject.getString("IsSuspended") == null)
                        Customer.setIsSuspended(0);
//                    else
//                        Customer.setIsSuspended(finalObject.getInt("IsSuspended"));
                        Customer.setPriceListId(finalObject.getString("PriceListID"));
                        Customer.setCashCredit(finalObject.getInt("CashCredit"));
                        Customer.setSalesManNumber(finalObject.getString("SalesManNo"));
                        Customer.setCreditLimit(finalObject.getDouble("CreditLimit"));
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
                            Customer.setCustomerIdText(finalObject.getString("CustID"));
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
                    JSONArray parentArrayItem_Unit_Details = parentObject.getJSONArray("Item_Unit_Details");
                    itemUnitDetailsList.clear();
                    for (int i = 0; i < parentArrayItem_Unit_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Unit_Details.getJSONObject(i);

                        ItemUnitDetails item = new ItemUnitDetails();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setItemNo(finalObject.getString("ItemNo"));
                        item.setUnitId(finalObject.getString("UnitID"));
                        item.setConvRate(finalObject.getDouble("ConvRate"));

                        itemUnitDetailsList.add(item);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
//                    `ITEMPICSPATH`
                    JSONArray parentArrayItems_Master = parentObject.getJSONArray("Items_Master");
//                Log.e("parentArrayItems_Master",""+parentArrayItems_Master.getString(""));
                    itemsMasterList.clear();
                    for (int i = 0; i < parentArrayItems_Master.length(); i++) {
                        JSONObject finalObject = parentArrayItems_Master.getJSONObject(i);
                        ItemsMaster item = new ItemsMaster();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setItemNo(finalObject.getString("ItemNo"));
                        item.setName(finalObject.getString("Name"));
                        item.setCategoryId(finalObject.getString("CateogryID"));
                        item.setBarcode(finalObject.getString("Barcode"));
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));
                    item.setPosPrice(finalObject.getDouble("F_D"));
                    item.setIsSuspended(0);
                    try {
                        item.setItemL(finalObject.getDouble("ItemL"));
                        Log.e("Exception",""+finalObject.getDouble("ItemL"));
                    }
                    catch (Exception e)
                    {
                        item.setItemL(0.0);

                    }

                    try {
                      if(  finalObject.getString("ITEMK") == "" ||  finalObject.getString("ITEMK") == null || finalObject.getString("ITEMK") == "null")
                          item.setKind_item("***");
                      else
                        item.setKind_item(finalObject.getString("ITEMK")); // here ?

                        } catch (Exception e) {
                            Log.e("ErrorImport", "Item_Kind_null");
                            item.setKind_item("***");

                        }
                        try {
                            item.setItemHasSerial(finalObject.getString("ITEMHASSERIAL"));
                            Log.e("setItemHasSerialJSON", "" + finalObject.getString("ITEMHASSERIAL"));
                        } catch (Exception e) {
                        }
                        try {
                            if (finalObject.getString("ITEMPICSPATH") == "" || finalObject.getString("ITEMPICSPATH") == null || finalObject.getString("ITEMPICSPATH") == "null") {
                                item.setPhotoItem("");
                            } else {
                                item.setPhotoItem(finalObject.getString("ITEMPICSPATH"));
                            }




                    }
                    catch (Exception e)
                    {                            item.setPhotoItem( "");
                    }
//                    ITEMPICSPATH
                        itemsMasterList.add(item);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }


                try {
                    JSONArray parentArrayPrice_List_M = parentObject.getJSONArray("Price_List_M");
                    priceListMpList.clear();
                    for (int i = 0; i < parentArrayPrice_List_M.length(); i++) {
                        JSONObject finalObject = parentArrayPrice_List_M.getJSONObject(i);

                        PriceListM item = new PriceListM();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setPrNo(finalObject.getInt("PrNo"));
                        item.setDescribtion(finalObject.getString("Description"));
                        item.setIsSuspended(0);
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));

                    priceListMpList.add(item);
                }

            }catch (JSONException e)
            {
                Log.e("Import Data", e.getMessage().toString());
            }


            try
            {

                    JSONArray parentArraySales_Team = parentObject.getJSONArray("Sales_Team");
                    salesTeamList.clear();
                    for (int i = 0; i < parentArraySales_Team.length(); i++) {
                        JSONObject finalObject = parentArraySales_Team.getJSONObject(i);

                        SalesTeam item = new SalesTeam();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setSalesManNo(finalObject.getString("SalesManNo"));
                        item.setSalesManName(finalObject.getString("SalesManName"));
                        item.setIsSuspended(0);
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));

                    salesTeamList.add(item);
                }
                Log.e("ImportData", salesTeamList.size()+"");
            }
            catch (JSONException e)
            {
                Log.e("Import Data", e.getMessage().toString());
            }
            try {
                JSONArray parentArraySalesMan_Items_Balance = parentObject.getJSONArray("SalesMan_Items_Balance");
                salesManItemsBalanceList.clear();
                for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                    JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);

                        SalesManItemsBalance item = new SalesManItemsBalance();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setSalesManNo(finalObject.getString("SalesManNo"));
                        item.setItemNo(finalObject.getString("ItemNo"));
                        item.setQty(finalObject.getDouble("Qty"));

                    salesManItemsBalanceList.add(item);
                }

            }
            catch ( Exception e)
            {
                Log.e("Exception","Gson"+e.getMessage());
            }


//                try {
//                    Gson gson = new Gson();
//
//                    SalesManItemsBalance gsonObj = gson.fromJson(String.valueOf(parentObject), SalesManItemsBalance.class);
//                    salesManItemsBalanceList.clear();
//                    salesManItemsBalanceList.addAll(gsonObj.getSalesItemBalance());
//                    Log.e("salesManItemsBalance",""+salesManItemsBalanceList.size());
//                }
//                catch (Exception e)
//                {
//                    Log.e("Exception","Gson"+e.getMessage());
//                }



//                JSONArray parentArraySalesmanAndStoreLink = parentObject.getJSONArray("SalesmanAndStoreLink");
//                salesManAndStoreLinksList.clear();
//                for (int i = 0; i < parentArraySalesmanAndStoreLink.length(); i++) {
//                    JSONObject finalObject = parentArraySalesmanAndStoreLink.getJSONObject(i);
//
//                    SalesManAndStoreLink item = new SalesManAndStoreLink();
//                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
//                    item.setSalesManNo(finalObject.getInt("SalesmanNo"));
//                    item.setStoreNo(finalObject.getInt("StoreNo"));
//
//                    salesManAndStoreLinksList.add(item);
//                }
                try {
                    JSONArray parentArraySalesMan = parentObject.getJSONArray("SALESMEN");
                    salesMenList.clear();
                    for (int i = 0; i < parentArraySalesMan.length(); i++) {
                        JSONObject finalObject = parentArraySalesMan.getJSONObject(i);

                        SalesMan salesMan = new SalesMan();
                        salesMan.setPassword(finalObject.getString("USER_PASSWORD"));
                        salesMan.setUserName(finalObject.getString("SALESNO"));

//                    Log.e("*******" , finalObject.getString("SALESNO"));
                    salesMenList.add(salesMan);
                }
                }catch (JSONException e)
                {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {

                JSONArray parentArrayCustomerPrice = parentObject.getJSONArray("customer_prices");
                customerPricesList.clear();

                for (int i = 0; i < parentArrayCustomerPrice.length(); i++) {
                    JSONObject finalObject = parentArrayCustomerPrice.getJSONObject(i);

                    CustomerPrice price = new CustomerPrice();
                    price.setItemNumber(finalObject.getString("ITEMNO"));
                    price.setCustomerNumber(finalObject.getInt("CUSTOMER_NO"));
                    price.setPrice(finalObject.getDouble("PRICE"));
                    price.setDiscount(finalObject.getDouble("DISCOUNT"));
                    customerPricesList.add(price);

                }
                }catch (JSONException e)
                {
                    Log.e("Import Data", e.getMessage().toString());
                }


                try {
                    JSONArray parentArrayOffers = parentObject.getJSONArray("VN_PROMOTION");
                    offersList.clear();
                    for (int i = 0; i < parentArrayOffers.length(); i++) {
                        JSONObject finalObject = parentArrayOffers.getJSONObject(i);

                        Offers offer = new Offers();
                        offer.setPromotionID(finalObject.getInt("PROMOID"));
                        offer.setPromotionType(finalObject.getInt("PROMOTYPE"));
                        offer.setFromDate(finalObject.getString("BDTAE"));
                        offer.setToDate(finalObject.getString("PEDTAE"));
                        offer.setItemNo(finalObject.getString("ITEMCODE"));
                        offer.setItemQty(finalObject.getDouble("PQTY"));
                        offer.setBonusQty(finalObject.getDouble("BQTY"));
                        offer.setBonusItemNo(finalObject.getString("BITEMCODE"));

                        offersList.add(offer);
                    }


                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {
                    JSONArray parentArraySalesmanStations = parentObject.getJSONArray("SALESMEN_STATIONS");
                    salesmanStationsList.clear();
                    for (int i = 0; i < parentArraySalesmanStations.length(); i++) {
                        JSONObject finalObject = parentArraySalesmanStations.getJSONObject(i);

                        SalesmanStations station = new SalesmanStations();
                        station.setSalesmanNo(finalObject.getString("SALESMAN_NO"));
                        station.setDate(finalObject.getString("DATE_"));
                        station.setLatitude(finalObject.getString("LATITUDE"));
                        station.setLongitude(finalObject.getString("LONGITUDE"));
                        station.setSerial(finalObject.getInt("SERIAL"));
                        station.setCustNo(finalObject.getString("ACCCODE"));
                        station.setCustName(finalObject.getString("ACCNAME"));

                        salesmanStationsList.add(station);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayQuantityOffers = parentObject.getJSONArray("QTY_OFFERS");
                    qtyOffersList.clear();
                    for (int i = 0; i < parentArrayQuantityOffers.length(); i++) {
                        JSONObject finalObject = parentArrayQuantityOffers.getJSONObject(i);

                        QtyOffers qtyOffers = new QtyOffers();
                        qtyOffers.setQTY(finalObject.getDouble("QTY"));
                        qtyOffers.setDiscountValue(finalObject.getDouble("DISC_VALUE"));
                        qtyOffers.setPaymentType(finalObject.getInt("PAYMENT_TYPE"));

                        qtyOffersList.add(qtyOffers);

                    }

                }catch (JSONException e)
                {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try
                {
                    JSONArray parentArrayPrice_List_D = parentObject.getJSONArray("Price_List_D");

                    priceListDpList.clear();
                    for (int i = 0; i < parentArrayPrice_List_D.length(); i++) {
                        JSONObject finalObject = parentArrayPrice_List_D.getJSONObject(i);

                        PriceListD item = new PriceListD();
                        item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                        item.setPrNo(finalObject.getInt("PrNo"));
                        item.setItemNo(finalObject.getString("ItemNo"));
                        item.setUnitId(finalObject.getString("UnitID"));
                        item.setPrice(finalObject.getDouble("Price"));
                        item.setTaxPerc(finalObject.getDouble("TaxPerc"));
                        item.setMinSalePrice(finalObject.getDouble("MINPRICE"));

                        priceListDpList.add(item);
                    }

                }
                catch (JSONException e)
                {
                    Log.e("Import Data", e.getMessage().toString());
                }


                try {
                    JSONArray parentArrayItemsQtyOffer = parentObject.getJSONArray("ITEMS_QTY_OFFER");
                    itemsQtyOfferList.clear();
                    for (int i = 0; i < parentArrayItemsQtyOffer.length(); i++) {
                        JSONObject finalObject = parentArrayItemsQtyOffer.getJSONObject(i);

                        ItemsQtyOffer qtyOffers = new ItemsQtyOffer();
                        qtyOffers.setItem_name(finalObject.getString("ITEMNAME"));
                        qtyOffers.setItem_no(finalObject.getString("ITEMNO"));
                        qtyOffers.setItemQty(finalObject.getDouble("AMOUNTQTY"));
                        qtyOffers.setFromDate(finalObject.getString("FROMDATE"));
                        qtyOffers.setToDate(finalObject.getString("TODATE"));
                        qtyOffers.setDiscount_value(finalObject.getDouble("DISCOUNT"));
                        itemsQtyOfferList.add(qtyOffers);

                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                /*
                 *
                 * [{"ITEMNAME":"جلواز أزرق","ITEMNO":"3258170924337","AMOUNTQTY":"20","DISCOUNT":"0.2","FROMDATE":"03\/10\/2019","TODATE":"30\/10\/2019"}]*/
                try {
                    JSONArray parentArrayAccountReport = parentObject.getJSONArray("ACOUNT_REPORT");
                    account_reportList.clear();
                    for (int i = 0; i < parentArrayAccountReport.length(); i++) {
                        JSONObject finalObject = parentArrayAccountReport.getJSONObject(i);

                        Account_Report acountReport = new Account_Report();
                        acountReport.setDate(finalObject.getString("DATE"));
                        acountReport.setTransfer_name(finalObject.getString("TRANSFER_NAME"));
                        acountReport.setDebtor(finalObject.getString("DEBTOR"));
                        acountReport.setCreditor(finalObject.getString("CREDITOR"));
                        acountReport.setCust_balance(finalObject.getString("CUS_BALANCE"));
                        acountReport.setCust_no(finalObject.getString("CUS_NO"));

                        account_reportList.add(acountReport);
                        Log.e("acountReport", "=" + account_reportList.size());
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }


            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("CustomerIOException", e.getMessage().toString());
                progressDialog.dismiss();
//                Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");
//                if (connection != null) {
//                    Log.e("Customer", "********ex4");
//                    // connection.disconnect();
//                }
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

            if (result != null && result.size() != 0) {
                Log.e("Customerr", "*****************" + customerList.size());
                storeInDatabase();
            } else {

                Toast.makeText(context, "Not able to fetch Customer data from server.", Toast.LENGTH_SHORT).show();
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
            window.setLayout(500, 250);

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

            mHandler.deleteAllCustomers();
            mHandler.deleteAllItemUnitDetails();
            mHandler.deleteAllItemsMaster();
            mHandler.deleteAllPriceListD();
            mHandler.deleteAllPriceListM();
            mHandler.deleteAllSalesTeam();
            mHandler.deleteAllSalesmanAndStoreLink();
            mHandler.deleteAllSalesmen();
            mHandler.deleteAllCustomerPrice();
            mHandler.deleteAllOffers();
            mHandler.deleteAllSalesmenStations();
            mHandler.deleteAllOffersQty();
            mHandler.deletItemsOfferQty();
            mHandler.deletAcountReport();

            if (mHandler.getIsPosted(Integer.parseInt(Login.salesMan)) == 1) {


                mHandler.deleteAllSalesManItemsBalance();
                mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList);
                Log.e("In***" , " inaddSalesMan_Items_Balance");
            }


//            for (int i = 0; i < customerList.size(); i++) {
                mHandler.addCustomer(customerList);
            Log.e("In***" , " inaddCustomer");
//            }

//            for (int i = 0; i < itemUnitDetailsList.size(); i++) {
                mHandler.addItem_Unit_Details(itemUnitDetailsList);
            Log.e("In***" , " inaddItem_Unit_Details");
//            }

//            for (int i = 0; i < itemsMasterList.size(); i++) {
                mHandler.addItemsMaster(itemsMasterList);
            Log.e("In***" , " inaddItemsMaster");
//            }

//            for (int i = 0; i < priceListDpList.size(); i++) {
                mHandler.addPrice_List_D(priceListDpList);
//            }

//            for (int i = 0; i < priceListMpList.size(); i++) {
                mHandler.addPrice_List_M(priceListMpList);
            Log.e("In***" , " in");
//            }
            for (int i = 0; i < salesTeamList.size(); i++) {
                mHandler.addSales_Team(salesTeamList.get(i));
            }
            Log.e("In***" , " addSales_Teamin");



            for (int i = 0; i < salesManAndStoreLinksList.size(); i++) {
                mHandler.addSalesmanAndStoreLink(salesManAndStoreLinksList.get(i));
            }

            for (int i = 0; i < salesMenList.size(); i++) {
                mHandler.addSalesmen(salesMenList.get(i));
            }
            Log.e("In***" , "inaddSalesmen");

//            for (int i = 0; i < customerPricesList.size(); i++) {
                mHandler.addCustomerPrice(customerPricesList);
//            }

            for (int i = 0; i < offersList.size(); i++) {
                mHandler.addOffer(offersList.get(i));
            }
            for (int i = 0; i < qtyOffersList.size(); i++) {
                mHandler.addQtyOffers(qtyOffersList.get(i));
            }

            for (int i = 0; i < itemsQtyOfferList.size(); i++) {
                mHandler.add_Items_Qty_Offer(itemsQtyOfferList.get(i));
            }
            for (int i = 0; i < account_reportList.size(); i++) {
                mHandler.addAccount_report(account_reportList.get(i));
            }
            for (int i = 0; i < salesmanStationsList.size(); i++) {
                mHandler.addSalesmanStation(salesmanStationsList.get(i));
            }
            Log.e("In***" , "addSalesmanStation_finish");

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

    private class JSONTask_AccountStatment extends AsyncTask<String, String, String> {

        private String custId = "";

        public JSONTask_AccountStatment(String customerId) {
            this.custId = customerId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            try {


                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin_oracle.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                Log.e("BasicNameValuePair", "" + custId);
                nameValuePairs.add(new BasicNameValuePair("FLAG", "1"));
                nameValuePairs.add(new BasicNameValuePair("customerNo", custId));


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
                Log.e("tag_Customer", "JsonResponse\t" + JsonResponse);

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

            JSONObject result = null;
            String impo = "";
            if (s != null) {
                if (s.contains("CUSTOMER_Account_Statment")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {
                        result = new JSONObject(s);
                        Account__Statment_Model requestDetail;


                        JSONArray requestArray = null;
                        listCustomerInfo = new ArrayList<>();

                        requestArray = result.getJSONArray("CUSTOMER_Account_Statment");
                        Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            requestDetail = new Account__Statment_Model();
                            requestDetail.setVoucherNo(infoDetail.get("VHFNo").toString());
                            requestDetail.setTranseNmae(infoDetail.get("TransName").toString());
                            requestDetail.setDate_voucher(infoDetail.get("VHFDATE").toString());

                            try {
                                requestDetail.setDebit(Double.parseDouble(infoDetail.get("DEBIT").toString()));
                                requestDetail.setCredit(Double.parseDouble(infoDetail.get("Credit").toString()));
                            } catch (Exception e) {
                                requestDetail.setDebit(0);
                                requestDetail.setCredit(0);
                            }


                            listCustomerInfo.add(requestDetail);
                            Log.e("listRequest", "listCustomerInfo" + listCustomerInfo.size());


                        }
                        getAccountList_text.setText("2");

                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

    }

    private class JSONTask_UpdateLocation extends AsyncTask<String, String, String> {

        JSONObject jsonObject;

        public JSONTask_UpdateLocation(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {


                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin_oracle.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("FLAG", "6"));
                nameValuePairs.add(new BasicNameValuePair("UPDATE_LOCATION_SALES_MAN", jsonObject.toString()));

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                Log.e("tag_CustomerInfo", "jsonObject.toString()\t" + jsonObject.toString());

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
                Log.e("tag_CustomerInfo", "JsonResponse\t" + JsonResponse);

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

            if (s != null) {

                if (s.contains("UPDATE_SALES_MAN_SUCCESS")) {

                    Toast.makeText(context, "UPDATE_SALES_MAN_SUCCESS", Toast.LENGTH_SHORT).show();
                    Log.e("onPostExecute", "UPDATE_SALES_MAN_SUCCESS");

                } else if (s.contains("UPDATE_SALES_MAN_FAIL")) {
                    Toast.makeText(context, "UPDATE_SALES_MAN_FAIL", Toast.LENGTH_SHORT).show();
                    Log.e("onPostExecute", "UPDATE_SALES_MAN_FAIL");

                }


            } else {
                Log.e("onPostExecute", "fff");
            }
//                progressDialog.dismiss();
        }
    }

}

