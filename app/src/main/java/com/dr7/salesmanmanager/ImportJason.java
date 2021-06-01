package com.dr7.salesmanmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
//import android.support.design.widget.TabLayout;
//import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.ItemSwitch;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.OfferListMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.UnCollect_Modell;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SalesMan;
import com.google.gson.Gson;
import com.sewoo.request.android.RequestHandler;

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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.AccountStatment.getAccountList_text;
import static com.dr7.salesmanmanager.Activities.totalBalance_text;

import static com.dr7.salesmanmanager.Login.checkIpDevice;
import static com.dr7.salesmanmanager.Login.previousIp;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.UnCollectedData.resultData;

public class ImportJason extends AppCompatActivity {

    private String URL_TO_HIT;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;
    SweetAlertDialog pdValidation,pdPayments,getDataProgress;
    public  String curentIpDevice="";
    String headerDll="";

    public static List<Customer> customerList = new ArrayList<>();
    public static List<ItemUnitDetails> itemUnitDetailsList = new ArrayList<>();
    public static List<ItemsMaster> itemsMasterList = new ArrayList<>();
    public static List<ItemSwitch> itemsSwitchList = new ArrayList<>();
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
    public static List<OfferListMaster> offerListMasterArrayList = new ArrayList<>();
    public static ArrayList<Account__Statment_Model> listCustomerInfo = new ArrayList<Account__Statment_Model>();
    public static ArrayList<serialModel> itemSerialList=new ArrayList<>();
    public static ArrayList<UnCollect_Modell> unCollectlList=new ArrayList<>();
    public static ArrayList<Payment> paymentChequesList=new ArrayList<>();
    private JsonObjectRequest loginRequest;
    private RequestQueue requestQueue;
    public  String CONO="";
    String userNo= "";
    boolean start = false;
    String ipAddress = "",ipWithPort="",SalesManLogin;

    public ImportJason(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        this.requestQueue = Volley.newRequestQueue(context);
        SalesManLogin= mHandler.getAllUserNo();
        headerDll="/Falcons/VAN.dll";
//        headerDll="";
        Log.e("SalesManLogin",""+SalesManLogin);
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            ipWithPort=settings.get(0).getIpPort();
            Log.e("ipWithPort",""+ipWithPort);
            userNo= mHandler.getAllUserNo();
            CONO=mHandler.getAllSettings().get(0).getCoNo();
        }
        else {
            Toast.makeText(context, "Check Setting Ip", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCustomerInfo(int type) {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            Log.e("getCustomerInfo", "*****");
            new JSONTask_AccountStatment(CustomerListShow.Customer_Account,type).execute();
          //  new SyncRemark().execute();
        }

    }

    public void getUnCollectedCheques() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            Log.e("getUnCollectedCheques", "*****");
            new JSONTask_UncollectedCheques(CustomerListShow.Customer_Account).execute();
            //  new SyncRemark().execute();
        }

    }

    public void getAllcheques() {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            new JSONTask_GetAllCheques(CustomerListShow.Customer_Account).execute();

        }
    }

//    public float getAvailableQty(String itemNoSelected) {
//        return
//    }

    private class SyncRemark extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context,R.style.MyTheme);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();

//            pd.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pd.setTitleText("يتم استيراد Remark");

        }

        @Override
        protected String doInBackground(String... params) {
            try {


//                final List<MainSetting>mainSettings=dbHandler.getAllMainSetting();
//                String ip="";
//                if(mainSettings.size()!=0) {
//                    ip=mainSettings.get(0).getIP();
//                }
                //http://localhost:8082/GetACCOUNTSTATMENT?ACCNO=402001100
                //http://localhost:8082/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100
                String link = "http://10.0.0.22:8081/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
              //  String link = "http://" + ipAddress + "/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
//                String link = "http://"+ip + "/GetNotes";
                Log.e("ipAdress", "ip -->" + link);

                // ITEM_CARD
//                String max=dbHandler.getMaxInDate("ITEM_SWITCH");
//                String maxInDate="";
//                if(max.equals("-1")) {
//                    maxInDate="05/03/2020";
//                }else{
//                    maxInDate=max.substring(0,10);
//                    String date[]=maxInDate.split("-");
//                    maxInDate=date[2]+"/"+date[1]+"/"+date[0];
//                    Log.e("splitSwitch ",""+maxInDate);
//                }
//                String data = "MAXDATE=" + URLEncoder.encode(maxInDate, "UTF-8");
////
                URL url = new URL(link);


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");

//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//                Log.e("url____",""+link+data);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "TAG_itemSwitch -->" + stringBuffer.toString());

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
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

//            if (JsonResponse != null && JsonResponse.contains("REMARKBODY")) {
                JSONObject result = null;
                String impo = "";
                if (JsonResponse != null) {
                    if (JsonResponse.contains("VHFNo")) {
                        // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                        //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                        try {
//                            result = new JSONObject(s);
                            Account__Statment_Model requestDetail;


                            JSONArray requestArray = null;
                            listCustomerInfo = new ArrayList<>();

                            requestArray = result.getJSONArray(JsonResponse);
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
                    }
//                    else
//                        Log.e("onPostExecute", "" + .toString());
//                progressDialog.dismiss();
                }

//            }
            else if (JsonResponse != null && JsonResponse.contains("No Data Found.")){
//                new SyncItemUnite().execute();
//                pd.dismissWithAnimation();

            }else {
                Log.e("TAG_itemSwitch", "****Failed to export data");
//                progressDialog.dismiss();
//                if(pd!=null) {
//                    pd.dismiss();
//                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("استيراد Remark")
//                            .setContentText("فشل استيراد Remark")
//                            .show();
//                }
            }

        }
    }

    public void startParsing(String salesNo) {

        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
           // http://10.0.0.22:8082/GetVanAllData
         // URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

        // new JSONTask(salesNo).execute(URL_TO_HIT);
//            if(isNetworkAvailable()){
//                new JSONTaskDelphi(salesNo).execute(URL_TO_HIT);
//            }
//            else {
//                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(context.getResources().getString(R.string.checkinternetConnection))
//
//                            .show();
//
//            }
            if(typaImport==0)//mysql
            {
             new JSONTask(userNo).execute(URL_TO_HIT);
            }else if(typaImport==1)
            {
                new JSONTaskDelphi(salesNo).execute(URL_TO_HIT);
            }






        }


    }

    private void getDataVolley(String salesNo ) {
        getDataProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        getDataProgress.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        getDataProgress.setTitleText(context.getResources().getString(R.string.process));
        getDataProgress.setCancelable(false);
//        getDataProgress.show();
        if (!ipAddress.equals("")) {

            if(ipAddress.contains(":"))
            {
                int ind=ipAddress.indexOf(":");
                ipAddress=ipAddress.substring(0,ind);
            }

            if(!salesNo.equals(""))
            {
                URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetVanAllData?STRNO="+salesNo+"&CONO="+CONO;

            }else {
                URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

            }

            Log.e("URL_TO_HIT",""+URL_TO_HIT);
        }
        getDataProgress.show();
        loginRequest = new JsonObjectRequest(Request.Method.GET, URL_TO_HIT
                , null, new getDataClass(), new getDataClass());
        requestQueue.add(loginRequest);
    }
    class getDataClass implements Response.Listener<JSONObject>, Response.ErrorListener {

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
            if (response.toString().contains("CUSTOMERS")) {
                try {
                    String rate_customer = "";
                    String HideVal = "";
                    JSONObject parentObject = new JSONObject(String.valueOf(response));
                    try {
                        JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS");
                        getDataProgress.setTitle("CUSTOMERS");
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
                        JSONArray parentArrayItem_Serial_Details = parentObject.getJSONArray("ITEMS_SERIALs");
                        itemSerialList.clear();
                        getDataProgress.setTitle("ITEMS_SERIALs");
                        for (int i = 0; i < parentArrayItem_Serial_Details.length(); i++) {
                            JSONObject finalObject = parentArrayItem_Serial_Details.getJSONObject(i);

                            serialModel item = new serialModel();
                            item.setStoreNo(finalObject.getString("STORENO"));
                            item.setItemNo(finalObject.getString("ITEMOCODE"));
                            item.setSerialCode(finalObject.getString("SERIALCODE"));
                            item.setQty(finalObject.getString("QTY"));

                            itemSerialList.add(item);
                        }
                        Log.e("itemSerialList",""+itemSerialList.size());
                    } catch (JSONException e) {
                        Log.e("Import Data", e.getMessage().toString());
                    }
                    try {
                        JSONArray parentArrayItem_Switch = parentObject.getJSONArray("item_swich");
                        itemsSwitchList.clear();
                        getDataProgress.setTitle("item_swich");
                        for (int i = 0; i < parentArrayItem_Switch.length(); i++) {
                            JSONObject finalObject = parentArrayItem_Switch.getJSONObject(i);

                            ItemSwitch item = new ItemSwitch();
                            item.setItem_NAMEA(finalObject.getString("ITEMNAMEA"));
                            item.setItem_OCODE(finalObject.getString("ITEMOCODE"));
                            item.setItem_NCODE(finalObject.getString("ITEMNCODE"));

                            itemsSwitchList.add(item);
                        }
                    } catch (JSONException e) {
                        Log.e("Import Data", e.getMessage().toString());
                    }




                    try {
//                    `ITEMPICSPATH`
                        JSONArray parentArrayItems_Master = parentObject.getJSONArray("Items_Master");
//                Log.e("parentArrayItems_Master",""+parentArrayItems_Master.getString(""));
                        itemsMasterList.clear();
                        getDataProgress.setTitle("Items_Master");
                        for (int i = 0; i < parentArrayItems_Master.length(); i++) {
                            JSONObject finalObject = parentArrayItems_Master.getJSONObject(i);
                            ItemsMaster item = new ItemsMaster();
                            item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                            item.setItemNo(finalObject.getString("ITEMNO"));
                            item.setName(finalObject.getString("NAME"));
                            item.setCategoryId(finalObject.getString("CATEOGRYID"));
                            item.setBarcode(finalObject.getString("BARCODE"));
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));MINPRICE
                            item.setPosPrice(finalObject.getDouble("F_D"));
                            item.setIsSuspended(0);
                            try {
                                item.setItemL(finalObject.getDouble("ITEML"));
                                Log.e("Exception",""+finalObject.getDouble("ITEML"));
                            }
                            catch (Exception e)
                            {
                                item.setItemL(0.0);

                            }

                            try {
                                if(  finalObject.getString("ItemK") == "" ||  finalObject.getString("ItemK") == null || finalObject.getString("ItemK") == "null")
                                    item.setKind_item("***");
                                else
                                    item.setKind_item(finalObject.getString("ItemK")); // here ?

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
                        getDataProgress.setTitle("Price_List_M");
                        for (int i = 0; i < parentArrayPrice_List_M.length(); i++) {
                            JSONObject finalObject = parentArrayPrice_List_M.getJSONObject(i);

                            PriceListM item = new PriceListM();
                            item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                            item.setPrNo(finalObject.getInt("PRNO"));
                            item.setDescribtion(finalObject.getString("DESCRIPTION"));
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
                        getDataProgress.setTitle("Sales_Team");
                        for (int i = 0; i < parentArraySales_Team.length(); i++) {
                            JSONObject finalObject = parentArraySales_Team.getJSONObject(i);

                            SalesTeam item = new SalesTeam();
                            item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                            item.setSalesManNo(finalObject.getString("SALESMANNO"));
                            item.setSalesManName(finalObject.getString("SALESMANNAME"));
                            try {
                                item.setIsSuspended(finalObject.getString("ISSUSPENDED"));
                            }catch (Exception e)
                            {
                                Log.e("setIsSuspended",""+e.getMessage());
                                item.setIsSuspended(finalObject.getString("ISSUSPENDED"));
                            }


                            //  item.setIpAddressDevice(finalObject.getString("IpAddressDevice"));


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
                        Log.e("salesManItemsB",""+parentArraySalesMan_Items_Balance);
                        getDataProgress.setTitle("SalesMan_Items_Balance");
                        for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                            JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);

                            SalesManItemsBalance item = new SalesManItemsBalance();
                            item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                            item.setSalesManNo(finalObject.getString("STOCK_CODE"));
                            item.setItemNo(finalObject.getString("ItemOCode"));
                            item.setQty(finalObject.getDouble("QTY"));

                            salesManItemsBalanceList.add(item);
                            Log.e("salesManItemsB",""+finalObject);
                        }


                    }
                    catch ( Exception e)
                    {
                        Log.e("Exception","Gson"+e.getMessage());
                    }

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

                        JSONArray parentArrayCustomerPrice = parentObject.getJSONArray("Customer_prices");
                        customerPricesList.clear();
                        getDataProgress.setTitle("Customer_prices");

                        for (int i = 0; i < parentArrayCustomerPrice.length(); i++) {
                            JSONObject finalObject = parentArrayCustomerPrice.getJSONObject(i);

                            CustomerPrice price = new CustomerPrice();
                            price.setItemNumber(finalObject.getString("ITEMCODE"));
                            price.setCustomerNumber(finalObject.getInt("CUSTACCNO"));
                            price.setPrice(finalObject.getDouble("PRICE"));
                            price.setDiscount(finalObject.getDouble("DISCOUNT"));

//                    try {
//                        price.setOther_Discount(finalObject.getString("OTHER_DISCOUNT"));
//                        price.setFromDate(finalObject.getString("FROM_DATE"));
//                        price.setToDate(finalObject.getString("TO_DATE"));
//                        price.setListNo(finalObject.getString("LIST_NO"));
//                        price.setListType(finalObject.getString("LIST_TYPE"));
//                    } catch (Exception e) {
//                        price.setOther_Discount("");
//                        price.setFromDate("");
//                        price.setToDate("");
//                        price.setListNo("");
//                        price.setListType("");
//                        Log.e("ImportData","Exception_customer_prices");
//
//                    }

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
                        getDataProgress.setTitle("Price_List_D");
                        for (int i = 0; i < parentArrayPrice_List_D.length(); i++) {
                            JSONObject finalObject = parentArrayPrice_List_D.getJSONObject(i);

                            PriceListD item = new PriceListD();
                            item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                            item.setPrNo(finalObject.getInt("PRNO"));
                            item.setItemNo(finalObject.getString("ITEMNO"));
                            item.setUnitId(finalObject.getString("UNITID"));
                            item.setPrice(finalObject.getDouble("PRICE"));
                            item.setTaxPerc(finalObject.getDouble("TAXPERC"));
                            try {
                                item.setMinSalePrice(Double.parseDouble(finalObject.getString("MINPRICE")));
                            }
                            catch (Exception e){
                                item.setMinSalePrice(0);
                            }


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

                    getDataProgress.dismissWithAnimation();
                    storeInDatabase();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (response.toString().contains("\"StatusCode\":28,\"StatusDescreption\":\"This User not have checks.\"")) {
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

    public void getItemBalance(String salesNo) {

        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();

            new JSONTaskDelphi_Data2(salesNo).execute(URL_TO_HIT);


        }


    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkUnpostedCustomer() {
        new SQLTask_unpostVoucher().execute(URL_TO_HIT);
    }

    public void getPreviousIpForSalesMen() {
     new JSONTask_PreviousIp().execute();
    }

    public void addCurentIp(String currentIp) {
        curentIpDevice=currentIp;
        Log.e("addCurentIp","="+curentIpDevice);
        //new JSONTask_AddIpDevice().execute();

    }

    public void getPriceFromAdmin() {
      new  JSONTask_getPciceFromAdmin().execute();
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

                new JSONTask(userNo).execute(URL_TO_HIT);

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
    void storeInDatabase_part() {
        new SQLTask_part().execute(URL_TO_HIT);

    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        public  String salesNo="";
        public  JSONTask(String sales){
            this.salesNo=sales;
            Log.e("JSONTask","salesNo"+salesNo);

        }
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
                URL_TO_HIT = "http://" + ipAddress.trim() + "/VANSALES_WEB_SERVICE/index.php";

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
                //+"&"+ URLEncoder.encode("SalesManNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(salesNo), "UTF-8");
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
                        Customer.setCompanyNumber(finalObject.getString("ComapnyNo"));
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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
                        item.setItemNo(finalObject.getString("ItemNo"));
                        item.setUnitId(finalObject.getString("UnitID"));
                        item.setConvRate(finalObject.getDouble("ConvRate"));

                        itemUnitDetailsList.add(item);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayItem_Serial_Details = parentObject.getJSONArray("ITEMS_SERIALs");
                    itemSerialList.clear();
                    for (int i = 0; i < parentArrayItem_Serial_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Serial_Details.getJSONObject(i);

                        serialModel item = new serialModel();
                        item.setStoreNo(finalObject.getString("STORENO"));
                        item.setItemNo(finalObject.getString("ITEMOCODE"));
                        item.setSerialCode(finalObject.getString("SERIALCODE"));
                        item.setQty(finalObject.getString("QTY"));

                        itemSerialList.add(item);
                    }
                    Log.e("itemSerialList",""+itemSerialList.size());
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
                    JSONArray parentArrayItem_Switch = parentObject.getJSONArray("item_swich");
                    itemsSwitchList.clear();
                    for (int i = 0; i < parentArrayItem_Switch.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Switch.getJSONObject(i);

                        ItemSwitch item = new ItemSwitch();
                        item.setItem_NAMEA(finalObject.getString("ITEMNAMEA"));
                        item.setItem_OCODE(finalObject.getString("ITEMOCODE"));
                        item.setItem_NCODE(finalObject.getString("ITEMNCODE"));

                        itemsSwitchList.add(item);
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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
                        item.setSalesManNo(finalObject.getString("SalesManNo"));
                        item.setSalesManName(finalObject.getString("SalesManName"));
                        try {
                        item.setIsSuspended(finalObject.getString("IsSuspended"));
                        }catch (Exception e)
                        {
                        Log.e("setIsSuspended",""+e.getMessage());
                        item.setIsSuspended(finalObject.getString("IsSuspended"));
                        }


                      //  item.setIpAddressDevice(finalObject.getString("IpAddressDevice"));


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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
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

//                    try {
//                        price.setOther_Discount(finalObject.getString("OTHER_DISCOUNT"));
//                        price.setFromDate(finalObject.getString("FROM_DATE"));
//                        price.setToDate(finalObject.getString("TO_DATE"));
//                        price.setListNo(finalObject.getString("LIST_NO"));
//                        price.setListType(finalObject.getString("LIST_TYPE"));
//                    } catch (Exception e) {
//                        price.setOther_Discount("");
//                        price.setFromDate("");
//                        price.setToDate("");
//                        price.setListNo("");
//                        price.setListType("");
//                        Log.e("ImportData","Exception_customer_prices");
//
//                    }

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
                        item.setCompanyNo(finalObject.getString("ComapnyNo"));
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

                //*********************************************************************
//                try {
//                    JSONArray parentArrayOfferMaster = parentObject.getJSONArray("price_offer_list_master");
//                    offerListMasterArrayList.clear();
//                    for (int i = 0; i < parentArrayOfferMaster.length(); i++) {
//                        JSONObject finalObject = parentArrayOfferMaster.getJSONObject(i);
//
//                        OfferListMaster offerListMaster = new OfferListMaster();
//                       int openList= finalObject.getInt("CLOSE_OPEN_LIST");
//                       int activeList=finalObject.getInt("ACTIVATE_LIST");
//                       if(openList==0 && activeList==0)
//                       {
//                           offerListMaster.setPO_LIST_NO(finalObject.getInt("PO_LIST_NO"));
//                           offerListMaster.setPO_LIST_NAME(finalObject.getString("PO_LIST_NAME"));
//                           offerListMaster.setPO_LIST_TYPE(finalObject.getInt("PO_LIST_TYPE"));
//                           offerListMaster.setFROM_DATE(finalObject.getString("FROM_DATE"));
//                           offerListMaster.setTO_DATE(finalObject.getString("TO_DATE"));
//
//                           offerListMasterArrayList.add(offerListMaster);
//                       }
//
//                    }
//                } catch (JSONException e) {
//                    Log.e("Import Data", e.getMessage().toString());
//                }


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
    private class JSONTaskDelphi extends AsyncTask<String, String, List<Customer>> {

        public  String salesNo="";
        public  JSONTaskDelphi(String sales){
            this.salesNo=sales;
            Log.e("JSONTask","salesNo"+salesNo);

        }
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

                try {

                    //+custId

                    if (!ipAddress.equals("")) {
                        //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
                        //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                        if(ipAddress.contains(":"))
                        {
                            int ind=ipAddress.indexOf(":");
                            ipAddress=ipAddress.substring(0,ind);
                        }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                     //   URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

                        if(!salesNo.equals(""))
                        {
                            URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetVanAllData?STRNO="+salesNo+"&CONO="+CONO;

                        }else {
                            URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

                        }

                        Log.e("URL_TO_HIT",""+URL_TO_HIT);
                    }
                } catch (Exception e) {

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
                Log.e("finalJson***Import", sb.toString());

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    in.close();


                   // JsonResponse = sb.toString();

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
                    JSONArray parentArrayItem_Unit_Details = parentObject.getJSONArray("Item_Unit_Details");
                    itemUnitDetailsList.clear();
                    for (int i = 0; i < parentArrayItem_Unit_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Unit_Details.getJSONObject(i);

                        ItemUnitDetails item = new ItemUnitDetails();
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setItemNo(finalObject.getString("ITEMNO"));
                        item.setUnitId(finalObject.getString("UNITID"));
                        item.setConvRate(finalObject.getDouble("CONVRATE"));

                        itemUnitDetailsList.add(item);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayItem_Serial_Details = parentObject.getJSONArray("ITEMS_SERIALs");
                    itemSerialList.clear();
                    for (int i = 0; i < parentArrayItem_Serial_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Serial_Details.getJSONObject(i);

                        serialModel item = new serialModel();
                        item.setStoreNo(finalObject.getString("STORENO"));
                        item.setItemNo(finalObject.getString("ITEMOCODE"));
                        item.setSerialCode(finalObject.getString("SERIALCODE"));
                        item.setQty(finalObject.getString("QTY"));

                        itemSerialList.add(item);
                    }
                    Log.e("itemSerialList",""+itemSerialList.size());
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
                    JSONArray parentArrayItem_Switch = parentObject.getJSONArray("item_swich");
                    itemsSwitchList.clear();
                    for (int i = 0; i < parentArrayItem_Switch.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Switch.getJSONObject(i);

                        ItemSwitch item = new ItemSwitch();
                        item.setItem_NAMEA(finalObject.getString("ITEMNAMEA"));
                        item.setItem_OCODE(finalObject.getString("ITEMOCODE"));
                        item.setItem_NCODE(finalObject.getString("ITEMNCODE"));

                        itemsSwitchList.add(item);
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
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setItemNo(finalObject.getString("ITEMNO"));
                        item.setName(finalObject.getString("NAME"));
                        item.setCategoryId(finalObject.getString("CATEOGRYID"));
                        item.setBarcode(finalObject.getString("BARCODE"));
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));MINPRICE
                        item.setPosPrice(finalObject.getDouble("F_D"));
                        item.setIsSuspended(0);
                        try {
                            item.setItemL(finalObject.getDouble("ITEML"));
                            Log.e("Exception",""+finalObject.getDouble("ITEML"));
                        }
                        catch (Exception e)
                        {
                            item.setItemL(0.0);

                        }

                        try {
                            if(  finalObject.getString("ItemK") == "" ||  finalObject.getString("ItemK") == null || finalObject.getString("ItemK") == "null")
                                item.setKind_item("***");
                            else
                                item.setKind_item(finalObject.getString("ItemK")); // here ?

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
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setPrNo(finalObject.getInt("PRNO"));
                        item.setDescribtion(finalObject.getString("DESCRIPTION"));
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
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setSalesManNo(finalObject.getString("SALESMANNO"));
                        item.setSalesManName(finalObject.getString("SALESMANNAME"));
                        try {
                            item.setIsSuspended(finalObject.getString("ISSUSPENDED"));
                        }catch (Exception e)
                        {
                            Log.e("setIsSuspended",""+e.getMessage());
                            item.setIsSuspended(finalObject.getString("ISSUSPENDED"));
                        }


                        //  item.setIpAddressDevice(finalObject.getString("IpAddressDevice"));


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
                        Log.e("salesManItems","GsonSalesMan_Items_Balance"+finalObject.toString());
                        String qty="";
                        SalesManItemsBalance item = new SalesManItemsBalance();
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setSalesManNo(finalObject.getString("STOCK_CODE"));
                        item.setItemNo(finalObject.getString("ItemOCode"));

                            qty=finalObject.getString("QTY");
                     try {
                         double qtydoubl=Double.parseDouble(qty);
                         item.setQty(qtydoubl);

                     }
                     catch (Exception e){
                         item.setQty(0);
                         Log.e("Exception",""+qty);
                     }
//                        item.setQty(finalObject.getDouble("QTY"));

                        salesManItemsBalanceList.add(item);
                    }

                }
                catch ( Exception e)
                {
                    Log.e("Exception","GsonSalesMan_Items_Balance"+e.getMessage());
                }

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

                    JSONArray parentArrayCustomerPrice = parentObject.getJSONArray("Customer_prices");
                    customerPricesList.clear();

                    for (int i = 0; i < parentArrayCustomerPrice.length(); i++) {
                        JSONObject finalObject = parentArrayCustomerPrice.getJSONObject(i);

                        CustomerPrice price = new CustomerPrice();
                        price.setItemNumber(finalObject.getString("ITEMCODE"));
                        price.setCustomerNumber(finalObject.getInt("CUSTACCNO"));
                        price.setPrice(finalObject.getDouble("PRICE"));
                        price.setDiscount(finalObject.getDouble("DISCOUNT"));

//                    try {
//                        price.setOther_Discount(finalObject.getString("OTHER_DISCOUNT"));
//                        price.setFromDate(finalObject.getString("FROM_DATE"));
//                        price.setToDate(finalObject.getString("TO_DATE"));
//                        price.setListNo(finalObject.getString("LIST_NO"));
//                        price.setListType(finalObject.getString("LIST_TYPE"));
//                    } catch (Exception e) {
//                        price.setOther_Discount("");
//                        price.setFromDate("");
//                        price.setToDate("");
//                        price.setListNo("");
//                        price.setListType("");
//                        Log.e("ImportData","Exception_customer_prices");
//
//                    }

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
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setPrNo(finalObject.getInt("PRNO"));
                        item.setItemNo(finalObject.getString("ITEMNO"));
                        item.setUnitId(finalObject.getString("UNITID"));
                        item.setPrice(finalObject.getDouble("PRICE"));
                        item.setTaxPerc(finalObject.getDouble("TAXPERC"));
                        try {
                            item.setMinSalePrice(Double.parseDouble(finalObject.getString("MINPRICE")));
                        }
                        catch (Exception e){
                            item.setMinSalePrice(0);
                        }


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

                //*********************************************************************
//                try {
//                    JSONArray parentArrayOfferMaster = parentObject.getJSONArray("price_offer_list_master");
//                    offerListMasterArrayList.clear();
//                    for (int i = 0; i < parentArrayOfferMaster.length(); i++) {
//                        JSONObject finalObject = parentArrayOfferMaster.getJSONObject(i);
//
//                        OfferListMaster offerListMaster = new OfferListMaster();
//                       int openList= finalObject.getInt("CLOSE_OPEN_LIST");
//                       int activeList=finalObject.getInt("ACTIVATE_LIST");
//                       if(openList==0 && activeList==0)
//                       {
//                           offerListMaster.setPO_LIST_NO(finalObject.getInt("PO_LIST_NO"));
//                           offerListMaster.setPO_LIST_NAME(finalObject.getString("PO_LIST_NAME"));
//                           offerListMaster.setPO_LIST_TYPE(finalObject.getInt("PO_LIST_TYPE"));
//                           offerListMaster.setFROM_DATE(finalObject.getString("FROM_DATE"));
//                           offerListMaster.setTO_DATE(finalObject.getString("TO_DATE"));
//
//                           offerListMasterArrayList.add(offerListMaster);
//                       }
//
//                    }
//                } catch (JSONException e) {
//                    Log.e("Import Data", e.getMessage().toString());
//                }


            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                progressDialog.dismiss();
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("CustomerIOException", e.getMessage().toString());
                progressDialog.dismiss();
//                Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");
                progressDialog.dismiss();
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
                    progressDialog.dismiss();
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
    private class JSONTaskDelphi_Data2 extends AsyncTask<String, String, List<serialModel>> {

        public  String salesNo="";
        public  JSONTaskDelphi_Data2(String sales){
            this.salesNo=sales;
            Log.e("JSONTask","salesNo"+salesNo);

        }
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
        protected List<serialModel> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                try {

                    //+custId

                    if (!ipAddress.equals("")) {
                        //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
                        //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                        if(ipAddress.contains(":"))
                        {
                            int ind=ipAddress.indexOf(":");
                            ipAddress=ipAddress.substring(0,ind);
                        }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                        //   URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;
                       // http://localhost:8082/GetVanData2?STRNO=4&CONO=295
                        URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetVanData2?STRNO="+SalesManLogin+"&CONO="+CONO;

                        Log.e("URL_TO_HIT",""+URL_TO_HIT);
                    }
                } catch (Exception e) {

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
                Log.e("finalJson***Import2", sb.toString());

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                // JsonResponse = sb.toString();

                String finalJson = sb.toString();
                Log.e("finalJson***Import2", finalJson);
                String rate_customer = "";
                String HideVal = "";

                JSONObject parentObject = new JSONObject(finalJson);

//                try {
//                    JSONArray parentArrayItem_Unit_Details = parentObject.getJSONArray("Item_Unit_Details");
//                    itemUnitDetailsList.clear();
//                    for (int i = 0; i < parentArrayItem_Unit_Details.length(); i++) {
//                        JSONObject finalObject = parentArrayItem_Unit_Details.getJSONObject(i);
//
//                        ItemUnitDetails item = new ItemUnitDetails();
//                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
//                        item.setItemNo(finalObject.getString("ITEMNO"));
//                        item.setUnitId(finalObject.getString("UNITID"));
//                        item.setConvRate(finalObject.getDouble("CONVRATE"));
//
//                        itemUnitDetailsList.add(item);
//                    }
//                } catch (JSONException e) {
//                    Log.e("Import Data", e.getMessage().toString());
//                }

                try {
                    JSONArray parentArrayItem_Serial_Details = parentObject.getJSONArray("ITEMS_SERIALs");
                    itemSerialList.clear();
                    for (int i = 0; i < parentArrayItem_Serial_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Serial_Details.getJSONObject(i);

                        serialModel item = new serialModel();
                        item.setStoreNo(finalObject.getString("STORENO"));
                        item.setItemNo(finalObject.getString("ITEMOCODE"));
                        item.setSerialCode(finalObject.getString("SERIALCODE"));
                        item.setQty(finalObject.getString("QTY"));

                        itemSerialList.add(item);
                    }
                    Log.e("itemSerialList",""+itemSerialList.size());
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {
                    JSONArray parentArraySalesMan_Items_Balance = parentObject.getJSONArray("SalesMan_Items_Balance");
                    salesManItemsBalanceList.clear();
                    for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                        JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);

                        SalesManItemsBalance item = new SalesManItemsBalance();
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setSalesManNo(finalObject.getString("STOCK_CODE"));
                        item.setItemNo(finalObject.getString("ItemOCode"));
                        item.setQty(finalObject.getDouble("QTY"));

                        salesManItemsBalanceList.add(item);
                    }

                }
                catch ( Exception e)
                {
                    Log.e("Exception","Gson"+e.getMessage());
                }



            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                progressDialog.dismiss();
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("CustomerIOException", e.getMessage().toString());
                progressDialog.dismiss();
//                Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");
                progressDialog.dismiss();
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
                    progressDialog.dismiss();
                }
            }
            return itemSerialList;
        }


        @Override
        protected void onPostExecute(final List<serialModel> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null && result.size() != 0) {
                Log.e("Customerr", "*****************" + customerList.size());
                storeInDatabase_part();
            } else {

                Toast.makeText(context, "Not able to fetch Customer data from server.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class SQLTask extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        Dialog dialog;
        TextView title_progresspar;

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
            title_progresspar= (TextView) dialog.findViewById(R.id.title_progresspar);

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
            int   storeNo=1;
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
            mHandler.deleteAllItemsSwitch();
            mHandler.deleteAllItemsSerialMaster();
            mHandler.deleteOfferMaster();
            try {
                storeNo=Integer.parseInt(userNo);
            }
            catch (Exception e){storeNo=1;}

            if (mHandler.getIsPosted(storeNo) == 1) {

//                if (mHandler.getIsPosted(Integer.parseInt(Login.salesMan)) == 1) {
                mHandler.deleteAllSalesManItemsBalance();
                mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList);
                Log.e("In***" , " inaddSalesMan_Items_Balance");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        title_progresspar.setText("add_SalesMan_Items_Balance");

                    }
                });

            }
            mHandler.add_SerialMasteItems(itemSerialList);
            Log.e("In***" , " inadd_SerialMasteItems");
            setText(title_progresspar,"add_Serial_Items");


            mHandler.addCustomer(customerList);
            Log.e("In***" , " inaddCustomer");
            setText(title_progresspar,"add_Customer");

            mHandler.addItem_Unit_Details(itemUnitDetailsList);
            Log.e("In***" , " inaddItem_Unit_Details");
            setText(title_progresspar,"add_Item_Unit_Details");

            mHandler.addItemsMaster(itemsMasterList);
            Log.e("In***" , " inaddItemsMaster");
            setText(title_progresspar,"add_items_Master");

            mHandler.addItemSwitch(itemsSwitchList);
            Log.e("In***" , " inaaddItemSwitch");
            setText(title_progresspar,"add_items_Switch");

            mHandler.addPrice_List_D(priceListDpList);
            setText(title_progresspar,"add_price_ListD");

            mHandler.addPrice_List_M(priceListMpList);
            Log.e("In***" , " in");
            setText(title_progresspar,"add_price_ListM");

            for (int i = 0; i < salesTeamList.size(); i++) {
                mHandler.addSales_Team(salesTeamList.get(i));
            }
            setText(title_progresspar,"add_salesTeam");
            Log.e("In***" , " addSales_Teamin");



            for (int i = 0; i < salesManAndStoreLinksList.size(); i++) {
                mHandler.addSalesmanAndStoreLink(salesManAndStoreLinksList.get(i));
            }

            for (int i = 0; i < salesMenList.size(); i++) {
                mHandler.addSalesmen(salesMenList.get(i));
            }
            Log.e("In***" , "inaddSalesmen");
            setText(title_progresspar,"add_Salesmen");

            mHandler.addCustomerPrice(customerPricesList);
            setText(title_progresspar,"add_customerPricesList");

            mHandler.add_OfferListMaster(offerListMasterArrayList);
            setText(title_progresspar,"add_OfferListMaster");
            for (int i = 0; i < offersList.size(); i++) {
                mHandler.addOffer(offersList.get(i));
            }
            setText(title_progresspar,"add_offersList");

            for (int i = 0; i < qtyOffersList.size(); i++) {
                mHandler.addQtyOffers(qtyOffersList.get(i));
            }
            setText(title_progresspar,"add_qtyOffers");


            for (int i = 0; i < itemsQtyOfferList.size(); i++) {
                mHandler.add_Items_Qty_Offer(itemsQtyOfferList.get(i));
            }
            setText(title_progresspar,"add_itemsQtyOffer");

            for (int i = 0; i < account_reportList.size(); i++) {
                mHandler.addAccount_report(account_reportList.get(i));
            }
            for (int i = 0; i < salesmanStationsList.size(); i++) {
                mHandler.addSalesmanStation(salesmanStationsList.get(i));
            }
            setText(title_progresspar,"add_salesmanStation");

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
        private void setText(final TextView text,final String value){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(value);
                }
            });
        }
    }
    private class SQLTask_part extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        Dialog dialog;
        TextView title_progresspar;

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
            title_progresspar= (TextView) dialog.findViewById(R.id.title_progresspar);

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
            int   storeNo=1;

            mHandler.deleteAllItemsSerialMaster();
            mHandler.deleteAllSalesManItemsBalance();
            mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList);
            Log.e("In***" , " inaddSalesMan_Items_Balance");
            mHandler.add_SerialMasteItems(itemSerialList);
            Log.e("In***" , " inadd_SerialMasteItems");
            setText(title_progresspar,"add_Serial_Items");


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
        private void setText(final TextView text,final String value){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(value);
                }
            });
        }
    }

    private class JSONTask_UncollectedCheques extends AsyncTask<String, String, String> {

        private String custId = "";

        public JSONTask_UncollectedCheques(String customerId) {
            this.custId = customerId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdValidation.setTitleText(context.getResources().getString(R.string.process));
            pdValidation.setCancelable(false);
            pdValidation.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                //+custId

                if (!ipAddress.equals("")) {
                    //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
                  //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                    if(ipAddress.contains(":"))
                    {
                        int ind=ipAddress.indexOf(":");
                        ipAddress=ipAddress.substring(0,ind);
                    }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetTheUnCollectedCheques?ACCNO="+custId+"&CONO="+CONO;
                }
            } catch (Exception e) {

            }

            try {

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

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed AccountStatment", Toast.LENGTH_LONG).show();
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
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("AccCode")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {
                        //[{"AccCode":"1224","RECVD":"3528","PAIDAMT":"0"}]
                        UnCollect_Modell requestDetail;


                        JSONArray requestArray = null;
                        listCustomerInfo = new ArrayList<>();

                        requestArray =  new JSONArray(s);
                        Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            requestDetail = new UnCollect_Modell();
                            requestDetail.setAccCode(infoDetail.get("AccCode").toString());
                            requestDetail.setRECVD(infoDetail.get("RECVD").toString());
                            requestDetail.setPAIDAMT(infoDetail.get("PAIDAMT").toString());



                            unCollectlList.add(requestDetail);
                            Log.e("listRequest", "listCustomerInfo" + unCollectlList.size());


                        }
                        if(unCollectlList.size()!=0)
                        {
                            resultData.setText("yes");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

    }
    private class JSONTask_GetAllCheques extends AsyncTask<String, String, String> {

        private String custId = "";

        public JSONTask_GetAllCheques(String customerId) {
            this.custId = customerId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdPayments = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdPayments.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdPayments.setTitleText(context.getResources().getString(R.string.process));
            pdPayments.setCancelable(false);
            pdPayments.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                //+custId

                if (!ipAddress.equals("")) {
                    //http://10.0.0.22:8082/GetAllTheCheques?ACCNO=1224
                    //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
                    if(ipAddress.contains(":"))
                    {
                        int ind=ipAddress.indexOf(":");
                        ipAddress=ipAddress.substring(0,ind);
                    }

                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetAllTheCheques?ACCNO="+custId+"&ACCNO="+CONO;
                }
            } catch (Exception e) {
                pdPayments.dismissWithAnimation();
            }

            try {

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

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_allcheques", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        pdPayments.dismissWithAnimation();
                        Toast.makeText(context, "Ip Connection Failed AccountStatment", Toast.LENGTH_LONG).show();
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
            pdPayments.dismissWithAnimation();
            if (s != null) {
                if (s.contains("VHFNo")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {

                        Payment requestDetail;
//                        ChequeNo


                        JSONArray requestArray = null;
                        paymentChequesList = new ArrayList<>();

                        requestArray =  new JSONArray(s);
                        Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            requestDetail = new Payment();
                            try {
                                requestDetail.setCheckNumber(Integer.parseInt(infoDetail.get("ChequeNo").toString()));
                            }
                            catch (Exception e){ requestDetail.setCheckNumber(111);}
                            //

                            requestDetail.setDueDate(infoDetail.get("DueDate").toString());
                            requestDetail.setBank("Jordan Bank");
                            try {
                                requestDetail.setAmount(Double.parseDouble(infoDetail.get("CAmount").toString()));

                            }catch (Exception e){requestDetail.setAmount(0);}



                            paymentChequesList.add(requestDetail);
                            Log.e("listRequest", "listCustomerInfo" + unCollectlList.size());


                        }
                        if(paymentChequesList.size()!=0)
                        {
                            resultData.setText("payment");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

    }
    private class JSONTask_AccountStatment extends AsyncTask<String, String, String> {

        private String custId = "";
        private  int type=0;

        public JSONTask_AccountStatment(String customerId,int typeImpo) {
            this.custId = customerId;
            this.type=typeImpo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdValidation.setTitleText(context.getResources().getString(R.string.process));
            pdValidation.setCancelable(false);
            pdValidation.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                //+custId
                headerDll="/Falcons/VAN.dll";

                if (!ipAddress.equals("")) {
                    //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                    if(ipAddress.contains(":"))
                    {
                        int ind=ipAddress.indexOf(":");
                        ipAddress=ipAddress.substring(0,ind);
                    }
                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+"/GetACCOUNTSTATMENT?ACCNO="+custId+"&ACCNO="+CONO;
                }
            } catch (Exception e) {
                pdValidation.dismissWithAnimation();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("FLAG", "1"));
//                nameValuePairs.add(new BasicNameValuePair("customerNo", custId));


                //  request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


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
                Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        pdValidation.dismissWithAnimation();
                        Toast.makeText(context, "Ip Connection Failed AccountStatment", Toast.LENGTH_LONG).show();
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
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("VHFNo")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {
//                        result = new JSONObject(s);
                        Account__Statment_Model requestDetail;


                        JSONArray requestArray = null;
                        listCustomerInfo = new ArrayList<>();

                        double totalBalance=0;
                        requestArray =  new JSONArray(s);
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
                            if(requestDetail.getDebit()!=0.0)
                            {
                                totalBalance-=requestDetail.getDebit();// دائن
                            }

                            if(requestDetail.getCredit()!=0.0)
                            {

                                totalBalance+=requestDetail.getCredit();// مدين

                            }

                            requestDetail.setBalance(totalBalance);
                            Log.e("onBindViewHolder","=total="+totalBalance);


                            listCustomerInfo.add(requestDetail);
                            //Log.e("listRequest", "listCustomerInfo" + listCustomerInfo.size());


                        }
                        if(type==0)
                        {
                            getAccountList_text.setText("2");
                        }
                        else {
                            if(listCustomerInfo.size()!=0)
                            totalBalance_text.setText(listCustomerInfo.get(listCustomerInfo.size()-1).getBalance()+"");
                        }


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
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "15"));
                nameValuePairs.add(new BasicNameValuePair("UPDATE_LOCATION_SALES_MAN", jsonObject.toString()));

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
              //  Log.e("tag_CustomerInfo", "jsonObject.toString()\t" + jsonObject.toString());

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

                        //Toast.makeText(context, "Ip Connection Failed UPDATE_LOCATION", Toast.LENGTH_LONG).show();
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

//                    Toast.makeText(context, "UPDATE_SALES_MAN_SUCCESS", Toast.LENGTH_SHORT).show();
                    Log.e("onPostExecute", "UPDATE_SALES_MAN_SUCCESS");

                } else if (s.contains("UPDATE_SALES_MAN_FAIL")) {
//                    Toast.makeText(context, "UPDATE_SALES_MAN_FAIL", Toast.LENGTH_SHORT).show();
                    Log.e("onPostExecute", "UPDATE_SALES_MAN_FAIL");

                }


            } else {
                Log.e("onPostExecute", "fff");
            }
//                progressDialog.dismiss();
        }
    }
    private class JSONTask_PreviousIp extends AsyncTask<String, String, String> {

        private String custId = "";

        //public JSONTask_AccountStatment(String customerId) {
//            this.custId = customerId;
//        }

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


                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                Log.e("JSONTask_PreviousIp", "" + Login.salesMan);
                nameValuePairs.add(new BasicNameValuePair("_ID", "10"));
                nameValuePairs.add(new BasicNameValuePair("SalesManNo", Login.salesMan));


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

                        Toast.makeText(context, "Ip Connection Failed PreviousIp", Toast.LENGTH_LONG).show();
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
                if (s.contains("IpAdressForSalesMan")) {

                    try {
                        result = new JSONObject(s);
                        Account__Statment_Model requestDetail;


                        JSONArray requestArray = null;
                        listCustomerInfo = new ArrayList<>();

                        requestArray = result.getJSONArray("IpAdressForSalesMan");

                        JSONObject infoDetail = requestArray.getJSONObject(0);
                        previousIp=infoDetail.get("IpAddressDevice").toString();
                        Log.e("requestArray", "previousIp" +previousIp );
                        if(previousIp.equals(""))
                        {
                            checkIpDevice.setText("2");// to add ip
                        }
                        else {
                            checkIpDevice.setText(previousIp);
                        }

                        pdValidation.dismissWithAnimation();
//                        getAccountList_text.setText("2");

                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else
                {
                    if(s.contains("Not definded id"))
                    {
                        checkIpDevice.setText("-1");
                    }
                    Log.e("onPostExecute", "" + s.toString());

                    pdValidation.dismissWithAnimation();
                }
            }else pdValidation.dismissWithAnimation();
        }

    }
    private class JSONTask_AddIpDevice extends AsyncTask<String, String, String> {

        private String custId = "";

        //public JSONTask_AccountStatment(String customerId) {
//            this.custId = customerId;
//        }

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


                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                Log.e("JSONTask_PreviousIp", "" + curentIpDevice);
                nameValuePairs.add(new BasicNameValuePair("_ID", "11"));
                nameValuePairs.add(new BasicNameValuePair("SALESNO",Login.salesMan));
                nameValuePairs.add(new BasicNameValuePair("IpDevice", curentIpDevice));



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
                Log.e("tag_addIpDevice", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed AddIpDevice", Toast.LENGTH_LONG).show();
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
                if (s.contains("UPDATE_SALES_MAN_SUCCESS")) {
                    checkIpDevice.setText("-1");

                        pdValidation.dismissWithAnimation();
//
                } else
                {if(s.contains("UPDATE_SALES_MAN_FAIL"))

                    pdValidation.dismissWithAnimation();
                }
            }else pdValidation.dismissWithAnimation();
        }

    }
    private class JSONTask_getPciceFromAdmin extends AsyncTask<String, String, String> {

        private String custId = "";

        //public JSONTask_AccountStatment(String customerId) {
//            this.custId = customerId;
//        }

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


                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";
                }
            } catch (Exception e) {

            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("_ID", "21"));



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
                Log.e("getPciceFromAdmin ", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed PciceFromAdmin", Toast.LENGTH_LONG).show();
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
                Log.e("getPciceFromAdmin ", "JsonResponse\t" + s.toString());
                if (!s.contains("notupDate")) {
                    pdValidation.dismissWithAnimation();
                   startParsing("");
//
                } else
                {

                    pdValidation.dismissWithAnimation();
                }
            }else pdValidation.dismissWithAnimation();
        }

    }
    }

