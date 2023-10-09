package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
//import android.support.design.widget.TabLayout;
//import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Interface.Api_PendingInvoice;
import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.CustomersPerformance;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemSwitch;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.OfferGroupModel;
import com.dr7.salesmanmanager.Modles.OfferListMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Password;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Pending_Invoice;
import com.dr7.salesmanmanager.Modles.Pending_Serial;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SMItemAvailability;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.TargetDetalis;
import com.dr7.salesmanmanager.Modles.UnCollect_Modell;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.CustomersPerformanceReport;
import com.dr7.salesmanmanager.Reports.SalesMan;
import com.dr7.salesmanmanager.Reports.TargetReport;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.HttpHostConnectException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;


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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.dr7.salesmanmanager.AccountStatment.getAccountList_text;
import static com.dr7.salesmanmanager.Activities.totalBalance_text;

import static com.dr7.salesmanmanager.Activities.totalBalance_value;
import static com.dr7.salesmanmanager.Login.Purchase_Order;
import static com.dr7.salesmanmanager.Login.checkIpDevice;
import static com.dr7.salesmanmanager.Login.curentDate;
import static com.dr7.salesmanmanager.Login.dateFromToActive;
import static com.dr7.salesmanmanager.Login.getMaxVoucherServer;
import static com.dr7.salesmanmanager.Login.goMainText;
import static com.dr7.salesmanmanager.Login.headerDll;
import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.previousIp;
import static com.dr7.salesmanmanager.Login.salesMan;
import static com.dr7.salesmanmanager.Login.storeNo_edit;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.MainActivity.fill_Pending_inv;
import static com.dr7.salesmanmanager.MainActivity.openPendingTextView;
import static com.dr7.salesmanmanager.MainActivity.pdialog;
import static com.dr7.salesmanmanager.Methods.convertToEnglish;
import static com.dr7.salesmanmanager.Methods.getDecimal;
import static com.dr7.salesmanmanager.ReturnByVoucherNo.loadSerial;
import static com.dr7.salesmanmanager.UnCollectedData.resultData;

public class ImportJason extends AppCompatActivity {

    private String URL_TO_HIT;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;
    SweetAlertDialog pdValidation, pdPayments, getDataProgress,savingDialog3;
    public String curentIpDevice = "";
    int salesManInt=-1,validUser=0;


    int counter = 0, voucherTyp = 504,customerSales=0;
    public  GeneralMethod generalMethod;

    public static List<Customer> customerList = new ArrayList<>();
    public static List<ItemUnitDetails> itemUnitDetailsList = new ArrayList<>();
    public static List<SMItemAvailability> itemVisiblelsList = new ArrayList<>();
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
    public static List<OfferGroupModel> groupOfferList = new ArrayList<>();
    public static List<Account_Report> account_reportList = new ArrayList<>();
    public static List<OfferListMaster> offerListMasterArrayList = new ArrayList<>();
    public static ArrayList<Account__Statment_Model> listCustomerInfo = new ArrayList<Account__Statment_Model>();
    public static ArrayList<serialModel> itemSerialList = new ArrayList<>();
    public static ArrayList<UnCollect_Modell> unCollectlList = new ArrayList<>();
    public static ArrayList<Payment> paymentChequesList = new ArrayList<>();
    public static ArrayList<serialModel> returnListSerial = new ArrayList<>();
    public static ArrayList<SalesManPlan>salesManPlanList = new ArrayList<>();
    public static ArrayList<TargetDetalis>salesGoalsList = new ArrayList<>();
    public static ArrayList<TargetDetalis>ItemsGoalsList = new ArrayList<>();
    public static ArrayList<Item> listItemsReturn = new ArrayList<>();
    public static ArrayList<Item> CopyForServerVocger_listItemsReturn = new ArrayList<>();
    public static Voucher voucherReturn = new Voucher();
    private JsonArrayRequest loginRequest;
    private RequestQueue requestQueue;
    public String CONO = "";
    String userNo = "";
    boolean start = false;
    String ipAddress = "", ipWithPort = "", SalesManLogin,link;

    public  static  ArrayList<Pending_Invoice> list_pending_invoice=new ArrayList<>();
    public  static  ArrayList<Pending_Serial> list_pending_serial=new ArrayList<>();
    public Api_PendingInvoice apiPendingInvoice;
    public ImportJason(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
        List<Settings> settings = mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        this.requestQueue = Volley.newRequestQueue(context);

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
        counter = 0;
        generalMethod=new GeneralMethod(context);
        link="http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() +"/";
        if(link.trim().length()!=0)
        {
            try {
                Retrofit retrofit = RetrofitInstance.getInstance(link);
                apiPendingInvoice= retrofit.create(Api_PendingInvoice.class);
            }catch (Exception e){
                Log.e("apiPendingInvoice",""+e.getMessage());
            }

        }
        try {
            salesManInt=Integer.parseInt( salesMan);
            Log.e("salesManInt",""+salesManInt);
        }catch (Exception e){

        }

    }

    public void fetchCallData(int flag) {
        Log.e("fetchCallData","fetchCallData");
// 1 from  RE EXPORT DIALOG ----2 FROM IMPORT DATA
        Call<ArrayList<Pending_Invoice>> myData = apiPendingInvoice.getPendingInvoiceInfo(CONO,salesMan);

        myData.enqueue(new Callback<ArrayList<Pending_Invoice>>() {
            @Override
            public void onResponse(Call<ArrayList<Pending_Invoice>> call, retrofit2.Response<ArrayList<Pending_Invoice>> response) {

                if (!response.isSuccessful()) {
                    Log.e("fetchCallDataonResponse", "not=" + response.message());
                    pdialog.dismissWithAnimation();
                } else {
                    pdialog.dismissWithAnimation();
                    Log.e("getInitialDataPending","begin3");
                    list_pending_invoice.clear();
                    list_pending_invoice.addAll(response.body());
                    Log.e("list_pending_invoice","fill_inv="+list_pending_invoice.size());

                    if(flag!=2)
                    fill_Pending_inv.setText("fill_inv");
                    if(flag==2){
                        if(list_pending_invoice.size()!=0){
                            Log.e("list_pending_invoice","fill_inv="+list_pending_invoice.size());
//                            openExportDialog();
                        }
                    }

                    fetchCallData_serial(flag);

//                    fetchCashDetailData(from, toDat, pos);
                   Log.e("onResponse", "flag=" + flag);



                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pending_Invoice>> call, Throwable throwable) {
                Log.e("fetchCallDataonFailure", "=" + throwable.getMessage());
                list_pending_invoice.clear();
                pdialog.dismissWithAnimation();
                if(flag!=2)
                fill_Pending_inv.setText("fill_inv");


                fetchCallData_serial(flag);
            }
        });
    }

    public void fetchCallData_serial(int flag) {

        Call<ArrayList<Pending_Serial>> myData = apiPendingInvoice.getPendingSerialInfo(CONO,salesMan);

        myData.enqueue(new Callback<ArrayList<Pending_Serial>>() {
            @Override
            public void onResponse(Call<ArrayList<Pending_Serial>> call, retrofit2.Response<ArrayList<Pending_Serial>> response) {
                pdialog.dismissWithAnimation();
                if (!response.isSuccessful()) {
                    pdialog.dismissWithAnimation();
                    Log.e("fetchCallData_serialonResponse", "not=" + response.message());
                } else {
                    pdialog.dismissWithAnimation();
                    list_pending_serial.clear();
                    list_pending_serial.addAll(response.body());
                    if(flag!=2)
                    fill_Pending_inv.setText("fill_serial");
                    Log.e("getInitialDataPending","begin33");
                    if((list_pending_invoice.size()!=0&&flag==2)||(list_pending_serial.size()!=0&&flag==2)){
                        openPendingTextView.setText("open");
                    }
//                    fetchCashDetailData(from, toDat, pos);
                    Log.e("onResponse", "=" + response.body().get(0).getSERIALCODE());
                    Log.e("onResponse", "=" + list_pending_serial.size());


                }

            }

            @Override
            public void onFailure(Call<ArrayList<Pending_Serial>> call, Throwable throwable) {
                Log.e("onFailure", "=" + throwable.getMessage());
                pdialog.dismissWithAnimation();
                    list_pending_serial.clear();
                if(flag!=2)
                    fill_Pending_inv.setText("fill_serial");

                if((list_pending_invoice.size()!=0&&flag==2)||(list_pending_serial.size()!=0&&flag==2)){
                    openPendingTextView.setText("open");
                }
            }
        });
    }


    public  void getVoucherMReturnData(String voucherNo){

            new JSONTask_VoucherMReturnData(voucherNo).execute();

    }
    public void getCustomerInfo(int type, String fromDate, String toDate) {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            Log.e("getCustomerInfo", "*****");
            if (fromDate.equals("") && toDate.equals("")) {
                    JSONTask_AccountStatment n = new JSONTask_AccountStatment(CustomerListShow.Customer_Account, type, fromDate, toDate);
                n.execute();
//                Thread thread1 = new Thread(){
//                    public void run(){
//                        try {
//                            n.get(20000, TimeUnit.MILLISECONDS);  //set time in milisecond(in this timeout is 30 seconds
//
//                        } catch (Exception e) {
//                            n.cancel(true);
//                            ((Activity) context).runOnUiThread(new Runnable()
//                            {
//                                @SuppressLint("ShowToast")
//                                public void run()
//                                {
//                                    Toast.makeText(context, "Time Out.", Toast.LENGTH_LONG).show();
//                                    n.onCancelled();
//                                    finish(); //will close the current activity comment if you don't want to close current activity.
//                                }
//                            });
//                        }
//                    }
//                };
//                thread1.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {

                            if ( (n.getStatus() == AsyncTask.Status.RUNNING )|| (n.getStatus() ==AsyncTask.Status.PENDING ))
                                n.onCancelled();

                        }
                    }, 10000 );


        } else {
                JSONTask_AccountStatment_Withdate task=  new JSONTask_AccountStatment_Withdate(CustomerListShow.Customer_Account, type, fromDate, toDate);
                task.execute();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {

                        if ( (task.getStatus() == AsyncTask.Status.RUNNING )|| (task.getStatus() ==AsyncTask.Status.PENDING ))
                            task.onCancelled();

                    }
                }, 10000 );

        }
    }
        }



    public void getUnCollectedCheques(String fromDate, String toDate) {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            Log.e("getUnCollectedCheques", "*****");
            new JSONTask_UncollectedCheques(CustomerListShow.Customer_Account, fromDate, toDate).execute();
            //  new SyncRemark().execute();
        }

    }

    public void getAllcheques(String fromDate, String toDate) {
        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
            new JSONTask_GetAllCheques(CustomerListShow.Customer_Account, fromDate, toDate).execute();

        }
    }

    public void getMaxVoucherNo(int type) {
        // getDataVolley(salesMan,504 );
        try {
            new JSONTask_maxVoucherNo(salesMan, 504,type).execute();
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("getMaxVoucherNo",""+e.getMessage());
        }


    }
    public void GetmaxPaymentVoucherNo(int type,int activtyflage) {
        // getDataVolley(salesMan,504 );
        try {
            new JSONTask_maxPaymentVoucherNo(salesMan, type,activtyflage).execute();
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("getMaxVoucherNo",""+e.getMessage());
        }


    }


    public void getMaxVoucherNo2(String salesMan,int type) {
        // getDataVolley(salesMan,504 );
        try {
            new JSONTask_maxVoucherNo(salesMan, 504,type).execute();
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("getMaxVoucherNo",""+e.getMessage());
        }


    }
    public void getSerialData(String voucherNo) {

        new JSONTask_SerialReturnData(voucherNo).execute();
    }

    public void getVoucherNoFromServer(String srialCode) {

        new JSONTask_getVoucherNoForSerial(srialCode).execute();
    }
    public void getSaleGoalItems(String salnum,String month) {

        new JSONTask_GetSaleGoalItems(salnum,month).execute();
    }

    public void getSalesmanGoal(String salnum,String month) {

        new JSONTask_GetSalesmanGoal(salnum,month).execute();
    }
    private class JSONTask_getVoucherNoForSerial extends AsyncTask<String, String, String> {

        private String serialCode = "";


        public JSONTask_getVoucherNoForSerial(String voucNo) {
            this.serialCode = voucNo;

            Log.e("voucherNo", "JSONTask==" + serialCode);
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
//http://10.0.0.22:8085/GetVhfNoBySerial?CONO=295&SERIALNO=355020113133366
            try {


                if (!ipAddress.equals("")) {
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVhfNoBySerial?SERIALNO=" + serialCode.trim() + "&CONO=" + CONO;


                }
                Log.e("GetVhfNoBySerial=", "" + URL_TO_HIT.toString());
            } catch (Exception e) {
                pdValidation.dismissWithAnimation();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


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
                // Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

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
            String voucherNumber="";
            if (s != null) {
                if (s.contains("VHFNO")) {
                    //  [{"VHFNO":"1900000169","STORENO":"1","TRNSDATE":"14\/08\/2021","TRANSKIND":"504","ITEMNO":"4213000125","SERIAL_CODE":"359573266728966","QTY":"1","VSERIAL":"1","ISPOSTED":"1"},

                    try {
                        serialModel requestDetail;
                        JSONArray requestArray = null;


                        double totalBalance = 0;
                        requestArray = new JSONArray(s);
                        requestDetail = new serialModel();
                        //  Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);

                            requestDetail.setVoucherNo(infoDetail.get("VHFNO").toString());
                            requestDetail.setKindVoucher(infoDetail.get("TRANSKIND").toString());

                            voucherNumber=requestDetail.getVoucherNo();

                              // Log.e("vhfno==", "==" + requestDetail.getVoucherNo());


                        }
                        if(requestDetail.getKindVoucher().equals("504"))
                            loadSerial.setText("VHFNO"+voucherNumber);
                        else loadSerial.setText("returned");





                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                }
                //  Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }

        }

    }

    private class JSONTask_SerialReturnData extends AsyncTask<String, String, String> {

        private String voucherNo = "";


        public JSONTask_SerialReturnData(String voucNo) {
            this.voucherNo = voucNo;

            Log.e("voucherNo", "JSONTask==" + voucherNo);
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
//http://localhost:8085/GetVE_ITEMSERIAL?CONO=295&VHFNO=1900000169
            try {


                if (!ipAddress.equals("")) {
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVE_ITEMSERIAL?VHFNO=" + voucherNo + "&CONO=" + CONO;


                }
                Log.e("GetVE_ITEMSERIAL=", "" + URL_TO_HIT.toString());
            } catch (Exception e) {
                pdValidation.dismissWithAnimation();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


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
                // Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

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
            returnListSerial = new ArrayList<>();
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("VHFNO")) {
                    //  [{"VHFNO":"1900000169","STORENO":"1","TRNSDATE":"14\/08\/2021","TRANSKIND":"504","ITEMNO":"4213000125","SERIAL_CODE":"359573266728966","QTY":"1","VSERIAL":"1","ISPOSTED":"1"},

                    try {
                        serialModel requestDetail;
                        JSONArray requestArray = null;


                        double totalBalance = 0;
                        requestArray = new JSONArray(s);
                      //  Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            requestDetail = new serialModel();
                            requestDetail.setVoucherNo(infoDetail.get("VHFNO").toString());
                            requestDetail.setStoreNo(infoDetail.get("STORENO").toString());
                            requestDetail.setDateVoucher(infoDetail.get("TRNSDATE").toString());
                            requestDetail.setKindVoucher("506");
                            requestDetail.setItemName("");
                            requestDetail.setIsPosted("0");

                            try {
                                requestDetail.setKindVoucher((infoDetail.get("TRANSKIND").toString()));
                                requestDetail.setItemNo(infoDetail.get("ITEMNO").toString());
                                requestDetail.setIsBonus("0");
                            } catch (Exception e) {

                            }
                            requestDetail.setSerialCode(infoDetail.get("SERIAL_CODE").toString());

                            returnListSerial.add(requestDetail);

                         //   Log.e("returnListSerial", "==" + returnListSerial.size());


                        }
                      //  if(returnListSerial.size()!=0)
                            loadSerial.setText("fillSerial");






                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    if(s.contains("No Parameter Found"))
                    {
                        loadSerial.setText("No Parameter");
                    }
                }
              //  Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
          //if(returnListSerial.size()!=0)
            getVoucherMReturnData(voucherNo);


        }

    }

    private class JSONTask_VoucherMReturnData extends AsyncTask<String, String, String> {

        private String voucherNo = "";


        public JSONTask_VoucherMReturnData(String voucNo) {
            this.voucherNo = voucNo;

            Log.e("VoucherMReturnData", "JSONTask==" + voucherNo);
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
            //  http://10.0.0.22:8085/GetVE_M?CONO=295&VHFNO=6
            try {


                if (!ipAddress.equals("")) {
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVE_M?VHFNO=" + voucherNo + "&CONO=" + CONO;


                }
                Log.e("GetVE_M=", "" + URL_TO_HIT.toString());
            } catch (Exception e) {
                pdValidation.dismissWithAnimation();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


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
                // Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

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

            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("VOUCHERNO")) {
                    //[{"COMAPNYNO":"295","VOUCHERYEAR":"2021","VOUCHERNO":"6","VOUCHERTYPE":"504","VOUCHERDATE":"05\/10\/2021","SALESMANNO":"00006","CUSTOMERNO":"1110010059","VOUCHERDISCOUNT":"0","VOUCHERDISCOUNTPERCENT":"0","NOTES":" ","CACR":"1","ISPOSTED":"1","PAYMETHOD":"1","NETSALES":"37.12","REALDATEEXPOTED":"05\/10\/2021","REALTIMEEXPOTED":"02:10:50 م"}]

                    try {
                        Voucher voucher;
                        JSONArray requestArray = null;
                        voucherReturn = new Voucher();
                        requestArray = new JSONArray(s);
                       // Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            voucher = new Voucher();
                            try {


                                voucher.setCompanyNumber(Integer.parseInt(CONO));
                                voucher.setVoucherNumber(Integer.parseInt(infoDetail.get("VOUCHERNO").toString()));
                                voucher.setVoucherType(506);
                                //  voucher.setVoucherDate(infoDetail.get("VOUCHERNO").toString());
                                voucher.setSaleManNumber(Integer.parseInt(salesMan));
//                              voucher.setVoucherDiscount(infoDetail.get("VOUCHERNO").toString());
//                              voucher.setVoucherDiscountPercent(VOUCHERDISCOUNTPERCENT);
                                voucher.setRemark("");
                                voucher.setPayMethod(Integer.parseInt(infoDetail.get("PAYMETHOD").toString()));

                                voucher.setIsPosted(0);
                                voucher.setTotalVoucherDiscount(Integer.parseInt(infoDetail.get("VOUCHERDISCOUNT").toString()));
                                //  voucher.setSubTotal(Double.parseDouble(infoDetail.get("NETSALES").toString()));
                                //voucher.setTax(Integer.parseInt(infoDetail.get("VOUCHERNO").toString()));
                                voucher.setCustNumber(infoDetail.get("CUSTOMERNO").toString());
                                voucher.setNetSales(Double.parseDouble(infoDetail.get("NETSALES").toString()));
                                // voucher.setCustName(infoDetail.get("VOUCHERNO").toString());

                                Log.e("voucher",""+voucher.getCustNumber()+"\t"+voucher.getNetSales());
                                voucher.setVoucherYear(Integer.parseInt(infoDetail.get("VOUCHERYEAR").toString()));

                                ReturnByVoucherNo.VOCHdat=  infoDetail.get("VOUCHERDATE").toString();
                                Log.e("VOCHdat",""+ReturnByVoucherNo.VOCHdat);
                            } catch (Exception e) {
                                Log.e("voucher", "Exception=" + e.getMessage());
                            }
                            // voucher.setTime(timevocher);

                            voucherReturn = voucher;
                       //     Log.e("returnListSerial", "==" + voucherReturn);


                        }
//                        if(voucherReturn.getCustNumber().trim().equals(CustomerListShow.Customer_Account.trim()))
//                        {
                            loadSerial.setText("fillpayMethod");
                            new  JSONTask_VoucherDetailReturnData(voucherNo).execute();
//                        }

//                        else {
//                            loadSerial.setText("NotSameCustomer");
//                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    if(s.contains("No Parameter Found"))
                    {
                       loadSerial.setText("No Parameter Found");

                    }
                }
                Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }

        }

    }
    //http://10.0.0.22:8085/GetVE_M?CONO=295&VHFNO=6
    private class JSONTask_VoucherDetailReturnData extends AsyncTask<String, String, String> {

        private String voucherNo = "";


        public JSONTask_VoucherDetailReturnData(String voucNo) {
            this.voucherNo = voucNo;

            Log.e("VoucherMReturnData", "JSONTask==" + voucherNo);
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
            //  http://10.0.0.22:8085/GetVE_M?CONO=295&VHFNO=6
            try {


                if (!ipAddress.equals("")) {
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVE_D?VHFNO=" + voucherNo + "&CONO=" + CONO;


                }
                Log.e("GetVE_d=", "" + URL_TO_HIT.toString());
            } catch (Exception e) {
                pdValidation.dismissWithAnimation();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


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
                // Log.e("tag_CustomerAccount", "JsonResponse\t" + JsonResponse);

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
            listItemsReturn = new ArrayList<>();
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("VOUCHERNO")) {
//                    {"COMAPNYNO":"295","VOUCHERYEAR":"2021","VOUCHERNO":"6","VOUCHERTYPE":"504","ITEMNO":"30100002","UNIT":"1",
//                            "QTY":"15","BONUS":"0","UNITPRICE":"2","ITEMDISCOUNTVALUE":"0","ITEMDISCOUNTPRC":"0",
//                            "VOUCHERDISCOUNT":"0","TAXVALUE":"4.8","TAXPERCENT":"16","ISPOSTED":"0","ITEM_DESCRITION":"",
//                            "SERIAL_CODE":"0","ITEM_SERIAL_CODE":"","WHICHUNIT":"1","WHICHUNITSTR":"Test2","WHICHUQTY":"3.0",
//                            "ENTERQTY":"5","ENTERPRICE":"30.0","UNITBARCOD":"456","CALCQTY":"5"},

                    try {
                        Item voucher;
                        JSONArray requestArray = null;

                        requestArray =  new JSONArray(s);
                        Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            voucher = new Item();
                            try {
                                voucher.setCompanyNumber(Integer.parseInt(CONO));
                                voucher.setVoucherNumber(Integer.parseInt(infoDetail.get("VOUCHERNO").toString()));
                                voucher.setVoucherType(506);
                                voucher.setItemNo(infoDetail.get("ITEMNO").toString());
                                voucher.setSalesmanNo(salesMan);
                                voucher.setUnit(infoDetail.get("UNIT").toString());
                                voucher.setQty(Float.parseFloat(infoDetail.get("QTY").toString()));
                               // voucher.setQty(0);

                                voucher.setPrice(Float.parseFloat(infoDetail.get("UNITPRICE").toString()));
                                voucher.setBonus(Float.parseFloat(infoDetail.get("BONUS").toString()));
                                voucher.setDisc(Float.parseFloat(infoDetail.get("ITEMDISCOUNTVALUE").toString()));
                                voucher.setDiscPerc(infoDetail.get("ITEMDISCOUNTPRC").toString());

                                voucher.setIsPosted(0);
                                voucher.setVoucherDiscount(Float.parseFloat(infoDetail.get("VOUCHERDISCOUNT").toString()));
                              voucher.setTax(Float.parseFloat(infoDetail.get("TAXVALUE").toString()));
                                voucher.setTaxPercent(Float.parseFloat(infoDetail.get("TAXPERCENT").toString()));

                                voucher.setSerialCode("0");
                                voucher.setDescreption("");
                                voucher.setIsPosted(0);
                                voucher.setItemName("");
                                voucher.setWhich_unit(infoDetail.get("WHICHUNIT").toString());
                                voucher.setWhich_unit_str(infoDetail.get("WHICHUNITSTR").toString());
                                voucher.setWhichu_qty(infoDetail.get("WHICHUQTY").toString());
                                voucher.setEnter_qty(infoDetail.get("ENTERQTY").toString());
                                voucher.setEnter_price(infoDetail.get("ENTERPRICE").toString());
                                voucher.setUnit_barcode(infoDetail.get("UNITBARCOD").toString());
                              if(infoDetail.get("IS_RETURNED").toString().equals(""))
                                  voucher.setAvi_Qty(Float.parseFloat(infoDetail.get("ENTERQTY").toString()));
                             else     if(infoDetail.get("IS_RETURNED").toString().equals("0"))
                                      voucher.setAvi_Qty(Float.parseFloat(infoDetail.get("ENTERQTY").toString()));
                                   else
                                voucher.setAvi_Qty(Float.parseFloat(infoDetail.get("AVLQTY").toString()));

                                voucher.setYear(infoDetail.get("VOUCHERYEAR").toString());

                                if(infoDetail.get("IS_RETURNED").toString().equals("1") && voucher.getAvi_Qty()==0)
                                {

                                }else
                                {
                                    listItemsReturn.add(voucher);
                                    CopyForServerVocger_listItemsReturn.add(voucher);
                                }

                            }catch (Exception e){
                                Log.e("voucher", "Exception="+e.getMessage()  );
                            }
                            // voucher.setTime(timevocher);


                            //Log.e("listItemsReturn", "==" + listItemsReturn.size());


                        }

                        loadSerial.setText("fillItems");


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                }
                Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();

            }

        }
    }


    public void getCustomerData() {

        new JSONTaskDelphi_customer().execute();
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
            if (typaImport == 0)//mysql
            {
                new JSONTask(userNo).execute(URL_TO_HIT);
            } else if (typaImport == 1) {
                if (makeOrders == 1) {// store Number from setting
                    if (!userNo.equals(""))
                        new JSONTaskDelphi(userNo).execute(URL_TO_HIT);
                    else {
                        new JSONTaskDelphi(salesNo).execute(URL_TO_HIT);
                    }
                } else {
                    new JSONTaskDelphi(salesNo).execute(URL_TO_HIT);
                }

            }


        }


    }

    private void getDataVolley(String salesNo, int voucherType) {
        voucherTyp = voucherType;

        getDataProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        getDataProgress.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        getDataProgress.setTitleText(context.getResources().getString(R.string.process));
        getDataProgress.setCancelable(false);


//        getDataProgress.show();
        //ipAddress="http://10.0.0.22:8085/GetmaxNo?STRNO=1&CONO=295&VKIND=506";
//        ipAddress="10.0.0.22";
//        ipWithPort="8085";
        if (!ipAddress.equals("")) {

            if (ipAddress.contains(":")) {
                int ind = ipAddress.indexOf(":");
                ipAddress = ipAddress.substring(0, ind);
            }

            if (!salesNo.equals("")) {
                URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetmaxNo?STRNO=" + salesNo + "&CONO=" + CONO + "&VKIND=" + String.valueOf(voucherType).trim();

            } else {
                URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetmaxNo?STRNO=" + SalesManLogin + "&CONO=" + CONO + "&VKIND=" + String.valueOf(voucherType).trim();

            }

            Log.e("URL_TO_HIT1010", "" + URL_TO_HIT);
        }
        getDataProgress.show();
        loginRequest = new JsonArrayRequest(Request.Method.POST, URL_TO_HIT
                , null, new getMaxVoucher(), new getMaxVoucher());

        /*final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
    Method.POST,
    act.getString(R.string.CommentForUserURL),
    new JSONObject(params), new Response.Listener<JSONObject>() {*/


        requestQueue.add(loginRequest);
    }

    class getMaxVoucher implements Response.Listener<JSONArray>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            //Log.e("onErrorResponse",""+error.getMessage().toString());
            getDataProgress.dismissWithAnimation();
            if ((error instanceof ParseError)) {
                goMainText.setText("main");

            }
            if ((error instanceof NoConnectionError)) {
                goMainText.setText("main");
                Toast.makeText(context,
                        "تأكد من اتصال الانترنت",
                        Toast.LENGTH_SHORT).show();
            }
            if (error instanceof NetworkError) {
            } else if (error instanceof ServerError) {
            } else if (error instanceof AuthFailureError) {
            } else if (error instanceof ParseError) {
            } else if (error instanceof NoConnectionError) {
            } else if (error instanceof TimeoutError) {
                Toast.makeText(context,
                        "Check Connection!",
                        Toast.LENGTH_LONG).show();
                goMainText.setText("main");
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onResponse(JSONArray response) {
            // getDataProgress.dismissWithAnimation();
            Log.e("presenter/", "trackingCheque/" + response.toString());
//            if (response.toString().contains("MAXVHFNO")) {

            if (response != null) {
                if (response.length() != 0) {
                    try {
                        String maxVoucher = "";


                        JSONObject jresponse = response.getJSONObject(0);

                        maxVoucher = jresponse.getString("MAXVHFNO");
                        long maxVoucherLong = Long.parseLong(maxVoucher);

                        Toast.makeText(context,
                                "Sucsses VoucherNo" + voucherTyp,
                                Toast.LENGTH_SHORT).show();
                        if (voucherTyp == 504) {


                            mHandler.addSerialVoucherNo(maxVoucherLong, 0, 0);

                        } else if (voucherTyp == 506) {

                            mHandler.updateVoucherNo(maxVoucherLong, 506, 0);
                            goMainText.setText("main");
                        }
                        counter++;

                        if (voucherTyp == 504) {
                            getDataProgress.dismissWithAnimation();

                            getDataVolley(salesMan, 506);
                        } else {
                            getDataProgress.dismissWithAnimation();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                getDataProgress.dismissWithAnimation();
                goMainText.setText("main");
            }


        }


    }

    // new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
    //                        .setTitleText("**** Cheque Tracing ****")
    //                        .setContentText("Check Data not found")
    //                        .show();
    //*******************************************************************************
    public class JSONTask_maxVoucherNo extends AsyncTask<String, String, String> {

        private String salesMan_no = "", JsonResponse;
        int voucherTyp = 0,typeResponse=0;

        public JSONTask_maxVoucherNo(String salesMan_no, int voucherType,int type) {
            this.salesMan_no = salesMan_no;
            this.voucherTyp = voucherType;
            this.typeResponse=type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getDataProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            getDataProgress.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            getDataProgress.setTitleText(context.getResources().getString(R.string.process));
            getDataProgress.setCancelable(false);
            getDataProgress.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("doInBackground", "typeResponse" +typeResponse);
            try {
                if (!ipAddress.equals("")) {

                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    if (storeNo_edit.getText()!=null) {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetmaxNo?STRNO=" + storeNo_edit.getText().toString().trim() + "&CONO=" + CONO + "&VKIND=" + String.valueOf(voucherTyp).trim();

                    } else {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetmaxNo?STRNO=" + SalesManLogin + "&CONO=" + CONO + "&VKIND=" + String.valueOf(voucherTyp).trim();

                    }

                    Log.e("URL_TO_HIT1010", "" + URL_TO_HIT);
                }
            } catch (Exception e) {

            }

            try {

                //*************************************

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent())
                );

                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                // JsonResponse = sb.toString();

                String finalJson = sb.toString();
                Log.e("finalJson***Import", "maxVoucherNo=" + finalJson);


                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        getDataProgress.dismissWithAnimation();

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                        if(typeResponse==1)    SalesInvoice.voucherNumberTextView.setText("refresh");
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                getDataProgress.dismissWithAnimation();
                Log.e("Exception", "" + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Log.e("onPostExecute","maxVoucherNo"+result.toString());
            if (result != null) {

                if (result.length() != 0) {
                    Log.e("onPostExecute", "typeResponse" +typeResponse);
                    if (result.contains("MAXVHFNO")) {
                        try {
                            String maxVoucher = "";
                            JSONArray requestArray = null;
                            requestArray = new JSONArray(result);

                            JSONObject infoDetail = requestArray.getJSONObject(0);

                            maxVoucher = infoDetail.get("MAXVHFNO").toString();
                            // maxVoucher = jresponse.getString("MAXVHFNO");
                            long maxVoucherLong=0;
                            if(!maxVoucher.equals(""))
                            {
                                maxVoucherLong = Long.parseLong(maxVoucher);

                            }else {
                                maxVoucherLong=0;

                            }

                            Toast.makeText(context,
                                    "Sucsses VoucherNo" + voucherTyp,
                                    Toast.LENGTH_SHORT).show();
                            if (voucherTyp == 504) {


                                mHandler.addSerialVoucherNo(maxVoucherLong, 0, 0);

                            } else {
                                if (voucherTyp == 506)
                                {
                                    mHandler.updateVoucherNo(maxVoucherLong, 506, typeResponse);

                                   // goMainText.setText("main");
                                }else {
                                    mHandler.updateVoucherNo(maxVoucherLong, 508, typeResponse);

                                    if(typeResponse==0)
                                    goMainText.setText("main");

                                }



                            }
                            counter++;

                            if (voucherTyp == 504) {
                                getDataProgress.dismissWithAnimation();

//                                    getDataVolley(salesMan,506);
                                new JSONTask_maxVoucherNo(salesMan_no, 506,typeResponse).execute();
                            } else {
                                if (voucherTyp == 506){
                                    getDataProgress.dismissWithAnimation();
                                    new JSONTask_maxVoucherNo(salesMan_no, 508,typeResponse).execute();
                                }else
                                {
                                    getDataProgress.dismissWithAnimation();
                                    if(Login.MaxpaymentvochFromServ==1)
                                        GetmaxPaymentVoucherNo(1,0);
                                }
                            }
                            try {
                                if(typeResponse==1)
                                SalesInvoice.voucherNumberTextView.setText("refresh");
                            }
                            catch (Exception e){
                                Log.e("Exception","importVoucherNo"+e.getMessage());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getDataProgress.dismissWithAnimation();
                        if(typeResponse==0)
                        goMainText.setText("main");
                        else    if(typeResponse==4){}
                        else      SalesInvoice.voucherNumberTextView.setText("refresh");
                    }

                }


            } else {
                getDataProgress.dismissWithAnimation();
                if(typeResponse==0)
                goMainText.setText("main");
                else    if(typeResponse==4){}
                else     SalesInvoice.voucherNumberTextView.setText("refresh");
            }




        }
    }
    public class JSONTask_maxPaymentVoucherNo extends AsyncTask<String, String, String> {

        private String salesMan_no = "", JsonResponse;
        int voucherTyp;
      int  activtyflage;
        public JSONTask_maxPaymentVoucherNo(String salesMan_no, int voucherType, int activtyflage) {
            this.salesMan_no = salesMan_no;
            this.voucherTyp = voucherType;
this.activtyflage=activtyflage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getDataProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            getDataProgress.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            getDataProgress.setTitleText(context.getResources().getString(R.string.getReceiptSerials));
            getDataProgress.setCancelable(false);
            getDataProgress.show();
            String do_ = "my";

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("doInBackground", "JSONTask_maxPaymentVoucherNo" );
            try {
                if (!ipAddress.equals("")) {

                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }

                    if (!salesMan_no.equals("")) {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/getmaxrcpno?STRNO=" + salesMan_no + "&CONO=" + CONO + "&TKIND=" + String.valueOf(voucherTyp).trim();

                    } else {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/getmaxrcpno?STRNO=" + SalesManLogin + "&CONO=" + CONO + "&TKIND=" + String.valueOf(voucherTyp).trim();

                    }

                    Log.e("URL_TO_HIT1010", "" + URL_TO_HIT);
                }
            } catch (Exception e) {

            }

            try {

                //*************************************

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent())
                );

                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                // JsonResponse = sb.toString();

                String finalJson = sb.toString();
                Log.e("finalJson***Import", "maxVoucherNo=" + finalJson);


                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        getDataProgress.dismissWithAnimation();

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();

                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                getDataProgress.dismissWithAnimation();
                Log.e("Exception", "" + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Log.e("onPostExecute","maxVoucherNo"+result.toString());
            getDataProgress.dismissWithAnimation();
            if (result != null) {

                if (result.length() != 0) {
                    Log.e("onPostExecute", "result" +result);
                    if (result.contains("MAXNO")) {
                        try {
                            String maxVoucher = "";
                            JSONArray requestArray = null;
                            requestArray = new JSONArray(result);

                            JSONObject infoDetail = requestArray.getJSONObject(0);

                            maxVoucher = infoDetail.get("MAXNO").toString();
                            // maxVoucher = jresponse.getString("MAXVHFNO");
                            long maxVoucherLong=0;
                            if(!maxVoucher.equals(""))
                            {
                                maxVoucherLong = Long.parseLong(maxVoucher);

                            }else {
                                maxVoucherLong=0;

                            }

                            Toast.makeText(context,
                                    "Sucsses VoucherNo" + voucherTyp,
                                    Toast.LENGTH_SHORT).show();
                            if (voucherTyp == 1) {//CASH

                                 if(mHandler.getPaymentSerialsCount()==0)
                                mHandler.insertPaymentSerials(1,maxVoucherLong+"");
                                 else
                                     mHandler.UpdatePaymentSerials(1,maxVoucherLong+"");
                                new JSONTask_maxPaymentVoucherNo(salesMan, 2,activtyflage).execute();

                            } else {//others

                                {
                                    Log.e("maxVoucherLong","="+maxVoucherLong);
                                    if(mHandler.getPaymentSerialsCount()==0)
                                    mHandler.insertPaymentSerials(2,maxVoucherLong+"");
                                    else
                                    mHandler.UpdatePaymentSerials(2,maxVoucherLong+"");

                            }

                            }

                          if(activtyflage==1&&voucherTyp!=1) {
                              Log.e("activtyflage","activtyflage");
                              ReceiptVoucher.MAX_ServoucherNumberRespon.setText("fill");
                          }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getDataProgress.dismissWithAnimation();


                    }

                }


            } else {
                getDataProgress.dismissWithAnimation();

            }
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
        curentIpDevice = currentIp;
        Log.e("addCurentIp", "=" + curentIpDevice);
        //new JSONTask_AddIpDevice().execute();

    }

    public void getPriceFromAdmin() {
        new JSONTask_getPciceFromAdmin().execute();
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
        if(typaImport==0)
        new JSONTask_UpdateLocation(jsonObject).execute();
        else
        new JSONTask_UpdateLocation_IIS(jsonObject).execute();
    }

    void storeInDatabase() {
        new SQLTask().execute(URL_TO_HIT);

    }

    void storeInDatabase_part() {
        new SQLTask_part().execute(URL_TO_HIT);

    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        public String salesNo = "";

        public JSONTask(String sales) {
            this.salesNo = sales;
            Log.e("JSONTask", "salesNo" + salesNo);

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
                Log.e("import_mySql", URL_TO_HIT);

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
                    Log.e("itemSerialList", "" + itemSerialList.size());
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

                        } catch (Exception e) {
                            // Log.e("Exception",""+finalObject.getDouble("ItemL"));
                            item.setItemL(0.0);

                        }

                        try {
                            if (finalObject.getString("ITEMK") == "" || finalObject.getString("ITEMK") == null || finalObject.getString("ITEMK") == "null")
                                item.setKind_item("***");
                            else
                                item.setKind_item(finalObject.getString("ITEMK")); // here ?

                        } catch (Exception e) {
                            Log.e("ErrorImport", "Item_Kind_null");
                            item.setKind_item("***");

                        }
                        try {
                            item.setItemHasSerial(finalObject.getString("ITEMHASSERIAL"));
                            // Log.e("setItemHasSerialJSON", "" + finalObject.getString("ITEMHASSERIAL"));
                        } catch (Exception e) {
                        }
                        try {
                            if (finalObject.getString("ITEMPICSPATH") == "" || finalObject.getString("ITEMPICSPATH") == null || finalObject.getString("ITEMPICSPATH") == "null") {
                                item.setPhotoItem("");
                            } else {
                                item.setPhotoItem(finalObject.getString("ITEMPICSPATH"));
                            }


                        } catch (Exception e) {
                            item.setPhotoItem("");
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

                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }


                try {

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
                        } catch (Exception e) {
                            Log.e("setIsSuspended", "" + e.getMessage());
                            item.setIsSuspended(finalObject.getString("IsSuspended"));
                        }


                        //  item.setIpAddressDevice(finalObject.getString("IpAddressDevice"));


                        salesTeamList.add(item);
                    }
                    Log.e("ImportData", salesTeamList.size() + "");
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
                    JSONArray parentArraySalesMan_Items_Balance = parentObject.getJSONArray("SalesMan_Items_Balance");
                    salesManItemsBalanceList.clear();
                    int sales_Man=Integer.parseInt(salesMan);
                    Log.e("ejabi","salesNo="+"\t"+sales_Man);
                    for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                        JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);

                        SalesManItemsBalance item = new SalesManItemsBalance();
                        String salesNo=finalObject.getString("SalesManNo");

                        Log.e("salesNo",""+salesNo);

                        if(salesNo.trim().equals(sales_Man+""))
                        {
                            item.setCompanyNo(finalObject.getString("ComapnyNo"));
                            item.setSalesManNo(finalObject.getString("SalesManNo"));
                            item.setItemNo(finalObject.getString("ItemNo"));
                            item.setQty(finalObject.getDouble("Qty"));
                           // Log.e("salesManItemsBalan", "Gson" + item.getItemNo()+"\t"+item.getQty());
                            salesManItemsBalanceList.add(item);
                        }

                    }

                } catch (Exception e) {
                    Log.e("Exception", "Gson" + e.getMessage());
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
                } catch (JSONException e) {
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
                } catch (JSONException e) {
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
                        try {
                            int discType = Integer.parseInt(finalObject.getString("VN_DISCOUNT_TYPE"));
                            offer.setDiscountItemType(discType);
                        } catch (Exception e) {
                            offer.setDiscountItemType(0);
                        }

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

                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
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

                } catch (JSONException e) {
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
                try {
                    JSONArray parentArrayItemsQtyOffer = parentObject.getJSONArray("Group_Offer_List");
                    groupOfferList.clear();
                    ArrayList<OfferGroupModel> listOffer = new ArrayList<>();

                    for (int i = 0; i < parentArrayItemsQtyOffer.length(); i++) {
                        JSONObject finalObject = parentArrayItemsQtyOffer.getJSONObject(i);

                        OfferGroupModel acountReport = new OfferGroupModel();
                        acountReport.id_serial = (finalObject.getString("id_serial"));
                        acountReport.Name = finalObject.getString("ItemName");
                        acountReport.ItemNo = finalObject.getString("ItemNo");
                        acountReport.fromDate = finalObject.getString("From_Date");
                        acountReport.toDate = finalObject.getString("To_Date");
                        acountReport.discount = finalObject.getString("Discount");
                        acountReport.discountType = finalObject.getInt("Discount_Type");
                        acountReport.groupIdOffer = finalObject.getInt("GroupId");
                        acountReport.qtyItem = finalObject.getString("qty_item");

                        groupOfferList.add(acountReport);
                    }
                    Log.e("groupOfferList", "result2=" + groupOfferList.size());


                } catch (Exception e) {
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
                //  Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            }

            catch (Exception e){
                Log.e("Customer", "4Exception********finally"+e.getMessage());
            }
            finally {
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

        public String salesNo = "";

        public JSONTaskDelphi(String sales) {
            this.salesNo = sales;
            Log.e("JSONTask", "salesNo" + salesNo);

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
                        if (ipAddress.contains(":")) {
                            int ind = ipAddress.indexOf(":");
                            ipAddress = ipAddress.substring(0, ind);
                        }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                        //   URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetVanAllData?STRNO="+SalesManLogin+"&CONO="+CONO;

                        if (!salesNo.equals("")) {
                            URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVanAllData?STRNO=" + salesNo + "&CONO=" + CONO;

                        } else {
                            URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVanAllData?STRNO=" + SalesManLogin + "&CONO=" + CONO;

                        }

                        Log.e("URL_TO_HIT", "" + URL_TO_HIT);
                    }
                } catch (Exception e) {

                }


                String link = URL_TO_HIT;
                URL url = new URL(link);

                //*************************************

                String JsonResponse = null;
                StringBuffer sb = new StringBuffer("");
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));


                HttpResponse response = null;

                try {
                    response = client.execute(request);
                } catch (Exception e) {
                    // Log.e("response",""+response.toString());
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                }


                try {
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(response.getEntity().getContent()));


                    String line = "";
                    // Log.e("finalJson***Import", sb.toString());

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    in.close();
                } catch (Exception e) {
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                }


                // JsonResponse = sb.toString();

                String finalJson = sb.toString();
//                 Log.e("finalJson***Import", finalJson);
                int sales_Man = Integer.parseInt(salesMan);
                String rate_customer = "";
                String HideVal = "";
                customerList.clear();
                JSONObject parentObject = new JSONObject(finalJson);
                try {
                    JSONArray parentArrayItem_Visibility = parentObject.getJSONArray("ITEM_VISIBILATY");
                    itemVisiblelsList.clear();
                    for (int i = 0; i < parentArrayItem_Visibility.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Visibility.getJSONObject(i);

                        SMItemAvailability item = new SMItemAvailability();
                        try {
                            item.setSalManNo(finalObject.getInt("SALESMANNO"));
                        } catch (Exception e) {

                        }

                        Log.e("setSalManNo","="+item.getSalManNo()+"\tsales_Man="+sales_Man);
                        if (item.getSalManNo() == sales_Man) {


                        item.setItemOcode(finalObject.getString("ITEMOCODE"));
                        try {
                            item.setAvailability(finalObject.getInt("VISIBLE"));
                        } catch (Exception e) {
                            item.setAvailability(0);
                        }


                        Log.e("finalJson***Import", item.getItemOcode() + "\t" + item.getAvailability());

                        itemVisiblelsList.add(item);
                        Log.e("itemUnitDetailsList", "1" + itemUnitDetailsList.size());
                    }
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                //*************************************ItemVisible**************************************


                try {
                    JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS");
                    Log.e("parentArrayCustomers","==="+parentArrayCustomers.toString());

                    if (mHandler.getAllSettings().get(0).getSalesManCustomers() == 1)
                    {
                        customerSales=1;
                        validUser=0;

                    }else {
                        validUser=1;
                    }
                    int sales=1;
                    for (int i = 0; i < parentArrayCustomers.length(); i++) {
                        JSONObject finalObject = parentArrayCustomers.getJSONObject(i);



                        if (customerSales == 1) {
                            String salesNo = finalObject.getString("SALESMANNO");
                            try {
                                sales = Integer.parseInt(salesNo);
                            } catch (Exception e) {

                            }
//                            Log.e("validUser", " == " + sales+" \tsalesManInt="+salesManInt);
                            if (sales == salesManInt) {

                                validUser = 1;
                            }
                            else {
                                validUser = 0;
                            }
                        }else {
                            validUser = 1;
                        }
//                        validUser = 1;
//                        Log.e("validUser", "== " + validUser);
                        Customer Customer = new Customer();
                        if (validUser == 1)
                        {

                        Customer.setCompanyNumber(finalObject.getString("COMAPNYNO"));
                        Customer.setCustId(finalObject.getString("CUSTID"));
                        Customer.setCustName(finalObject.getString("CUSTNAME"));
//                        Log.e("setCustName","222"+Customer.getCustName());
                        Customer.setAddress(finalObject.getString("ADDRESS"));
//                    if (finalObject.getString("IsSuspended") == null)
                        Customer.setIsSuspended(0);
//                    else
//                        Customer.setIsSuspended(finalObject.getInt("IsSuspended"));
                        Customer.setPriceListId(finalObject.getString("PRICELISTID"));
                        try {
                            Customer.setCashCredit(finalObject.getInt("CASHCREDIT"));
                        } catch (Exception e) {
                            Customer.setCashCredit(0);
                        }
                        Customer.setSalesManNumber(finalObject.getString("SALESMANNO"));
                        try {
                            Customer.setCreditLimit(finalObject.getDouble("CREDITLIMIT"));
                        } catch (Exception e) {
                            Customer.setCreditLimit(0);
                        }
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
                            // Log.e("ImportError", "Null_ACCPRC" + e.getMessage());
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
                            // Log.e("ImportError", "Null_ACCPRC" + e.getMessage());
                            Customer.setACCPRC("0");

                        }
                            try {

                                Customer.seteMail(finalObject.getString("EMail"));
                                Customer.setFax(finalObject.getString("Fax"));
                                Customer.setZipCode(finalObject.getString("ZipCode"));
                                Customer.setC_THECATEG(finalObject.getString("C_THECATEG"));
                                Log.e("CUSTOMERSSS","=="+Customer.geteMail());
                            }catch (Exception e){
                                Customer.setC_THECATEG("");
                                Customer.seteMail("");
                                Customer.setFax("");
                                Customer.setZipCode("");
                                Log.e("CUSTOMERSSS","=Exception=="+e.getMessage());
                            }



                            customerList.add(Customer);

                    }
                        //*******************************


                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
//**************************************************************************
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
                        try {
                            item.setUnitPrice("");
                            item.setItemBarcode("");
                        } catch (Exception e) {
                            item.setUnitPrice("");

                            item.setItemBarcode("");
                        }


                        itemUnitDetailsList.add(item);
//                        Log.e("itemUnitDetailsList", "1" + itemUnitDetailsList.size());
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                //***************************************************************************
                try {
//                    //[{"ITEMU":"Test1","ITEMOCODE":"6251088000015","ITEMBARCODE":"123","CALCQTY":"5","SALEPRICE":"10"},
//                    // {"ITEMU":"Test2","ITEMOCODE":"30100002","ITEMBARCODE":"456","CALCQTY":"3","SALEPRICE":"6"}]
                    JSONArray parentArrayItem_Unit_Details = parentObject.getJSONArray("Item_Unit_Details2");
                    itemUnitDetailsList.clear();
                    for (int i = 0; i < parentArrayItem_Unit_Details.length(); i++) {
                        JSONObject finalObject = parentArrayItem_Unit_Details.getJSONObject(i);

                        ItemUnitDetails item = new ItemUnitDetails();
                        item.setCompanyNo("");
                        item.setItemNo(finalObject.getString("ITEMOCODE"));
                        item.setUnitId(finalObject.getString("ITEMU"));
                        item.setConvRate(finalObject.getDouble("CALCQTY"));
                        try {
                            item.setUnitPrice(finalObject.getString("SALEPRICE"));
                            item.setItemBarcode(finalObject.getString("ITEMBARCODE"));

                            item.setPriceClass_1(finalObject.getString("PCLASS1"));
                            item.setPriceClass_2(finalObject.getString("PCLASS2"));
                            item.setPriceClass_3(finalObject.getString("PCLASS3"));



                        } catch (Exception e) {
                            item.setUnitPrice("");

                            item.setItemBarcode("");
                            item.setPriceClass_1("");
                            item.setPriceClass_2("");
                            item.setPriceClass_3("");
                        }


                        itemUnitDetailsList.add(item);
                        Log.e("itemUnitDetailsList", "2====" + itemUnitDetailsList.size());
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
                    // Log.e("itemSerialList",""+itemSerialList.size());
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
                            Log.e("Exception", "ITEML" + finalObject.getDouble("ITEML"));
                        } catch (Exception e) {
                            item.setItemL(0.0);

                        }

                        try {
                            if (finalObject.getString("ItemK") == "" || finalObject.getString("ItemK") == null || finalObject.getString("ItemK") == "null")
                                item.setKind_item("***");
                            else
                                item.setKind_item(finalObject.getString("ItemK")); // here ?

                        } catch (Exception e) {
                            Log.e("ErrorImport", "Item_Kind_null");
                            item.setKind_item("***");

                        }
                        try {
                            item.setItemHasSerial(finalObject.getString("ITEMHASSERIAL"));
                            // Log.e("setItemHasSerialJSON", "" + finalObject.getString("ITEMHASSERIAL"));
                        } catch (Exception e) {
                        }
                        try {
                            if (finalObject.getString("ITEMPICSPATH") == "" || finalObject.getString("ITEMPICSPATH") == null || finalObject.getString("ITEMPICSPATH") == "null") {
                                item.setPhotoItem("");
                            } else {
                                item.setPhotoItem(finalObject.getString("ITEMPICSPATH"));
                            }


                        } catch (Exception e) {
                            item.setPhotoItem("");
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

                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }


                try {

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
                        } catch (Exception e) {
                            Log.e("setIsSuspended", "" + e.getMessage());
                            item.setIsSuspended(finalObject.getString("ISSUSPENDED"));
                        }


                        //  item.setIpAddressDevice(finalObject.getString("IpAddressDevice"));


                        salesTeamList.add(item);
                    }
                    Log.e("ImportData", salesTeamList.size() + "");
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
                        try {
                            item.setItemNo(finalObject.getString("ItemOCode"));
                        }catch (Exception exception)
                        {
                            item.setItemNo(finalObject.getString("ITEMNO"));
                        }
                        item.setSalesManNo(finalObject.getString("STOCK_CODE"));


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

                try {
                    JSONArray parentArraySalesMan = parentObject.getJSONArray("SALESMEN");
                    salesMenList.clear();
                    for (int i = 0; i < parentArraySalesMan.length(); i++) {
                        JSONObject finalObject = parentArraySalesMan.getJSONObject(i);

                        SalesMan salesMan = new SalesMan();
                        salesMan.setPassword(finalObject.getString("USER_PASSWORD"));
                        salesMan.setUserName(finalObject.getString("SALESNO"));
                        try {
                            salesMan.setUserType(finalObject.getInt("USERTYPE"));
                        }catch (Exception e){
                            salesMan.setUserType(0);
                        }


//                    Log.e("*******" , finalObject.getString("SALESNO"));
                        salesMenList.add(salesMan);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

                try {

                    JSONArray parentArrayCustomerPrice = parentObject.getJSONArray("Customer_prices");
                    Log.e("parentArrayCusto", "" + parentArrayCustomerPrice.length());
                    customerPricesList.clear();
                    String acc="";
                    for (int i = 0; i < parentArrayCustomerPrice.length(); i++) {
                        JSONObject finalObject = parentArrayCustomerPrice.getJSONObject(i);

                        CustomerPrice price = new CustomerPrice();
                        price.setItemNumber(finalObject.getString("ITEMCODE"));
                        try {
                             acc=finalObject.getString("CUSTACCNO");
                        }catch (Exception e){
                            Log.e("Exception","1="+e.getMessage());
                        }
                        if(acc.trim().length()!=0)
                        {try {
                            price.setCustomerNumber(Integer.parseInt(acc));
                        }catch ( Exception e){
                            price.setCustomerNumber(0);
                            Log.e("Exception","2="+e.getMessage());
                        }
                        }

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
                       // Log.e("customerPricesList", "" + customerPricesList.size());

                    }
                } catch (JSONException e) {
                    Log.e("ImportcustomerPr", e.getMessage().toString());
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
                        try {
                            int discType = Integer.parseInt(finalObject.getString("VN_DISCOUNT_TYPE"));
                            offer.setDiscountItemType(discType);
                        } catch (Exception e) {
                            offer.setDiscountItemType(0);
                        }

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

                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }
                try {
                    JSONArray parentArrayPrice_List_D = parentObject.getJSONArray("Price_List_D");
                    //  Log.e("itemUnitDetailsList","parentArrayPrice_List_D"+parentArrayPrice_List_D.toString());
                    priceListDpList.clear();
                    for (int i = 0; i < parentArrayPrice_List_D.length(); i++) {
                        JSONObject finalObject = parentArrayPrice_List_D.getJSONObject(i);

                        PriceListD item = new PriceListD();
                        item.setCompanyNo(finalObject.getString("COMAPNYNO"));
                        item.setPrNo(finalObject.getInt("PRNO"));
                        item.setItemNo(finalObject.getString("ITEMNO"));
                        // Log.e("itemUnit","itemno==="+item.getItemNo());
                        item.setUnitId(finalObject.getString("UNITID"));
                        item.setPrice(finalObject.getDouble("PRICE"));
                        try {
                            item.setTaxPerc(finalObject.getDouble("TAXPERC"));
                        } catch (Exception e) {
                            item.setTaxPerc(0);
                        }

                        try {
                            item.setMinSalePrice(Double.parseDouble(finalObject.getString("MINPRICE")));
                        } catch (Exception e) {
                            item.setMinSalePrice(0);
                        }


                        priceListDpList.add(item);
                        //  Log.e("itemUnitDetailsList","parentArrayPrice_List_D"+priceListDpList.size());

                    }

                } catch (JSONException e) {
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
                        Log.e("ImportDataitemsQtyOffe", "itemsQtyOfferList");
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
            }
            catch (Exception e){
                Log.e("Customer", "Exception********finally"+e.getMessage());
            }
            finally {
                Log.e("Customer", "********finally");
                progressDialog.dismiss();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        if (customerList.size() == 0) {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("check Connection")
                                    .show();
                        }


                    }
                });
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
                getPassowrdSetting("1");
                try {
                    if(typaImport==1&&getMaxVoucherServer==1&&Purchase_Order==0)//iis
                    {



                     getMaxVoucherNo2(salesNo,4);

                    }
                    else
                    if(Login.getMaxVoucherServer==0){
                        Log.e("getMaxVoucherServer","getMaxVoucherServer");
                            if(Login.MaxpaymentvochFromServ==1)
                                GetmaxPaymentVoucherNo(1,0);
                        }
                }catch (Exception exception){

                }

            } else {

                // Toast.makeText(context, "Not able to fetch Customer data from server.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JSONTaskDelphi_Data2 extends AsyncTask<String, String, List<SalesManItemsBalance>> {

        public String salesNo = "";

        public JSONTaskDelphi_Data2(String sales) {
            this.salesNo = sales;
            Log.e("JSONTask", "salesNo" + salesNo);

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

        @SuppressLint("LongLogTag")
        @Override
        protected List<SalesManItemsBalance> doInBackground(String... params) {
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
                        // http://localhost:8082/GetVanData2?STRNO=4&CONO=295
                        if (!salesNo.equals("")) {
                            URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVanData2?STRNO=" + salesNo + "&CONO=" + CONO;

                        } else {
                            URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetVanData2?STRNO=" + SalesManLogin + "&CONO=" + CONO;

                        }


                        Log.e("URL_TO_HIT", "" + URL_TO_HIT);
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
                    // Log.e("itemSerialList",""+itemSerialList.size());
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
                    Log.e("salesManItemsBalanceList", "" + salesManItemsBalanceList.size());

                } catch (Exception e) {
                    Log.e("Exception", "Gson" + e.getMessage());
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
            }
            catch (Exception e){
                Log.e("Customer", "2Exception********finally"+e.getMessage());
            }
            finally {
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
            return salesManItemsBalanceList;
        }


        @Override
        protected void onPostExecute(final List<SalesManItemsBalance> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null && result.size() != 0) {
                Log.e("Customerr", "*****************" + result.size());
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
            title_progresspar = (TextView) dialog.findViewById(R.id.title_progresspar);

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
            int storeNo = 1;
            mHandler.deleteAllCustomers();
            mHandler.deleteAllItemUnitDetails();
            mHandler.deleteAllItemsMaster();
            mHandler.deleteAllPriceListD();
            mHandler.deleteAllPriceListM();
            mHandler.deleteAllSalesTeam();
            mHandler.deleteAllSalesmanAndStoreLink();
            mHandler.deleteAllSalesmen();
            mHandler.deleteAllCustomerPrice_Current();
            mHandler.deleteAllCustomerPrice();
            mHandler.deleteAllOffers();
            mHandler.deleteAllSalesmenStations();
            mHandler.deleteAllOffersQty();
            mHandler.deletItemsOfferQty();
            mHandler.deletAcountReport();
            mHandler.deleteAllItemsSwitch();
            mHandler.deleteAllItemsSerialMaster();
            mHandler.deleteOfferMaster();
            mHandler.deletOfferGroup();
            mHandler.deleteFromItemVisiblty();
            try {
                storeNo = Integer.parseInt(userNo);
            } catch (Exception e) {
                storeNo = 1;
            }

            if (mHandler.getIsPosted(storeNo) == 1) {

//                if (mHandler.getIsPosted(Integer.parseInt(Login.salesMan)) == 1) {
                mHandler.deleteAllSalesManItemsBalance();
                mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        title_progresspar.setText("add_SalesMan_Items_Balance");

                    }
                });

            }
            mHandler.add_SerialMasteItems(itemSerialList);
            Log.e("In***", " inadd_SerialMasteItems");
            setText(title_progresspar, "add_Serial_Items");

            setText(title_progresspar, "add_Customer");
            mHandler.addCustomer(customerList);
            Log.e("In***", " inaddCustomer");


            setText(title_progresspar, "add_Item_Visible");
            mHandler.addSMitemAvilablity(itemVisiblelsList);
            Log.e("In***", " add_Item_Visible"+itemVisiblelsList.size());



            setText(title_progresspar, "add_Item_Unit_Details");
            mHandler.addItem_Unit_Details(itemUnitDetailsList);
            Log.e("In***", " inaddItem_Unit_Details");

            setText(title_progresspar, "add_items_Master");
            mHandler.addItemsMaster(itemsMasterList);
            Log.e("In***", " inaddItemsMaster");


            mHandler.addItemSwitch(itemsSwitchList);
            Log.e("In***", " inaaddItemSwitch");
            setText(title_progresspar, "add_items_Switch");

            mHandler.addPrice_List_D(priceListDpList);
            setText(title_progresspar, "add_price_ListD");

            mHandler.addPrice_List_M(priceListMpList);
            Log.e("In***", " in");
            setText(title_progresspar, "add_price_ListM");

            for (int i = 0; i < salesTeamList.size(); i++) {
                mHandler.addSales_Team(salesTeamList.get(i));
            }
            setText(title_progresspar, "add_salesTeam");
            Log.e("In***", " addSales_Teamin");


            for (int i = 0; i < salesManAndStoreLinksList.size(); i++) {
                mHandler.addSalesmanAndStoreLink(salesManAndStoreLinksList.get(i));
            }

            for (int i = 0; i < salesMenList.size(); i++) {
                mHandler.addSalesmen(salesMenList.get(i));
            }
            Log.e("In***", "inaddSalesmen");
            setText(title_progresspar, "add_Salesmen");

            mHandler.addCustomerPrice(customerPricesList);
            setText(title_progresspar, "add_customerPricesList");

            mHandler.add_OfferListMaster(offerListMasterArrayList);
            setText(title_progresspar, "add_OfferListMaster");
            for (int i = 0; i < offersList.size(); i++) {
                mHandler.addOffer(offersList.get(i));
            }
            setText(title_progresspar, "add_offersList");

            for (int i = 0; i < qtyOffersList.size(); i++) {
                mHandler.addQtyOffers(qtyOffersList.get(i));
            }
            setText(title_progresspar, "add_qtyOffers");


            for (int i = 0; i < itemsQtyOfferList.size(); i++) {
                mHandler.add_Items_Qty_Offer(itemsQtyOfferList.get(i));
            }
            setText(title_progresspar, "add_itemsQtyOffer");

            for (int i = 0; i < account_reportList.size(); i++) {
                mHandler.addAccount_report(account_reportList.get(i));
            }
            for (int i = 0; i < salesmanStationsList.size(); i++) {
                mHandler.addSalesmanStation(salesmanStationsList.get(i));
            }
            setText(title_progresspar, "add_salesmanStation");
            for (int i = 0; i < groupOfferList.size(); i++) {
                mHandler.add_GroupOffer(groupOfferList.get(i));
            }
            setText(title_progresspar, "add_GroupOffer");


            Log.e("In***", "addSalesmanStation_finish");

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
            Log.e("getInitialDataPending","begin");
            dialog.dismiss();
//            ************************Pending_Invoice*************************


        }

        private void setText(final TextView text, final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(value);
                }
            });
        }
    }
    public void getInitialDataPending(Context context) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                pdialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

                pdialog.getProgressHelper().setBarColor(Color.parseColor("#31AFB4"));
                pdialog.setTitleText("Loading ...1");
                pdialog.setCancelable(false);
                pdialog.show();

            }
        });

        try {
            Log.e("getInitialDataPending","begin2");
            fetchCallData(2);


        }catch (Exception e){
            pdialog.dismissWithAnimation();
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            title_progresspar = (TextView) dialog.findViewById(R.id.title_progresspar);

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
            int storeNo = 1;


            mHandler.deleteAllItemsSerialMaster();
            mHandler.deleteAllSalesManItemsBalance();
            mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList);
            Log.e("In***", " inaddSalesMan_Items_Balance");
            mHandler.add_SerialMasteItems(itemSerialList);
            Log.e("In***", " inadd_SerialMasteItems");
            setText(title_progresspar, "add_Serial_Items");


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
            getInitialDataPending(context);
        }

        private void setText(final TextView text, final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(value);
                }
            });
        }
    }

    private class JSONTask_UncollectedCheques extends AsyncTask<String, String, String> {

        private String custId = "", fromD, toD;

        public JSONTask_UncollectedCheques(String customerId, String fromDate, String toDate) {
            this.custId = customerId;
            fromD = fromDate;
            toD = toDate;
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
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }
//                    URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/Falcons/VAN.dll/GetTheUnCollectedCheques?ACCNO=1224";

                    //  http://localhost:8085/tGetTheUnCollectedChequesWithDate?CONO=295&ACCNO=1110010062&FROMDATE=01/01/2021&TODATE=31/12/2021

                    if (dateFromToActive == 1) {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetTheUnCollectedChequesWithDate?ACCNO=" + custId
                                + "&CONO=" + CONO + "&FROMDATE=" + fromD.trim() + "&TODATE=" + toD.trim();
                        Log.e("URL_TO_HIT", "GetTheUnCollectedChequesWithDate=" + URL_TO_HIT);

                    } else {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetTheUnCollectedCheques?ACCNO=" + custId + "&CONO=" + CONO;

                    }


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
            listCustomerInfo = new ArrayList<>();
            if (s != null) {
                if (s.contains("AccCode")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {
                        //[{"AccCode":"1224","RECVD":"3528","PAIDAMT":"0"}]
                        UnCollect_Modell requestDetail;


                        JSONArray requestArray = null;


                        requestArray = new JSONArray(s);
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
                        if (unCollectlList.size() != 0) {
                            resultData.setText("yes");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else if (s.contains("No Data Found")) {
                    resultData.setText("noData");
                }
                Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

    }

    private class JSONTask_GetAllCheques extends AsyncTask<String, String, String> {

        private String custId = "", fromD, toDat;

        public JSONTask_GetAllCheques(String customerId, String fromDate, String toDate) {
            this.custId = customerId;
            fromD = fromDate;
            toDat = toDate;
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
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }
                    if (dateFromToActive == 1) {
                        if(fromD.equals(""))
                        {
                            URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetAllTheCheques?ACCNO=" + custId + "&CONO=" + CONO;

                        }
                        else
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetAllTheChequesWithDate?ACCNO=" + custId +
                                "&CONO=" + CONO + "&FROMDATE=" + fromD.trim() + "&TODATE=" + toDat.trim();

                    } else {
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetAllTheCheques?ACCNO=" + custId + "&CONO=" + CONO;

                    }

                    //  http://localhost:8085/GetAllTheChequesWithDate?CONO=295&ACCNO=1110010062&FROMDATE=01/01/2021&TODATE=01/10/2021

                    Log.e("URL_TO_HIT", "GetAllTheChequesWithDate=" + URL_TO_HIT);


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
            paymentChequesList = new ArrayList<>();
            if (s != null) {
                if (s.contains("VHFNo")) {
                    // Log.e("CUSTOMER_INFO","onPostExecute\t"+s.toString());
                    //{"CUSTOMER_INFO":[{"VHFNo":"0","TransName":"ÞíÏ ÇÝÊÊÇÍí","VHFDATE":"31-DEC-19","DEBIT":"0","Credit":"16194047.851"}

                    try {

                        Payment requestDetail;
//                        ChequeNo


                        JSONArray requestArray = null;


                        requestArray = new JSONArray(s);
                        Log.e("requestArray", "" + requestArray.length());


                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject infoDetail = requestArray.getJSONObject(i);
                            requestDetail = new Payment();
                            try {
                                requestDetail.setCheckNumber(Integer.parseInt(infoDetail.get("ChequeNo").toString()));
                            } catch (Exception e) {
                                requestDetail.setCheckNumber(111);
                            }
                            //

                            requestDetail.setDueDate(infoDetail.get("DueDate").toString());
                            requestDetail.setBank(infoDetail.get("PAYEENAME").toString());
                            try {
                                requestDetail.setAmount(Double.parseDouble(infoDetail.get("CAmount").toString()));

                            } catch (Exception e) {
                                requestDetail.setAmount(0);
                            }


                            paymentChequesList.add(requestDetail);
                            Log.e("listRequest", "listCustomerInfo" + unCollectlList.size());


                        }
                        if (paymentChequesList.size() != 0) {
                            resultData.setText("payment");
                        } else {
                            resultData.setText("noData");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                    getUnCollectedCheques(fromD, toDat);
                } else {
                    if (s.contains("No Data Found")) {
                        resultData.setText("noData");
                    }
                }


                Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

    }

    public class JSONTask_AccountStatment extends AsyncTask<String, String, String> {

        private String custId = "";
        private int type = 0;
        public String from_Date, to_Date;

        public JSONTask_AccountStatment(String customerId, int typeImpo, String fromDate, String toDate) {
            this.custId = customerId;
            this.type = typeImpo;
            from_Date = fromDate;
            to_Date = toDate;
            Log.e("from_Date", "==" + from_Date + "to===" + to_Date);
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



                if (!ipAddress.equals("")) {
                    //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }
                    //  URL_TO_HIT="http://92.253.93.250/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=1110010143&CONO=295";
                    // http://localhost:8085/GetACCOUNTSTATMENT?CONO=295&ACCNO=1110010062&FROMDATE=01/01/2021&TODATE=01/10/2021
                    // custId="1110010062";

                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetACCOUNTSTATMENT?ACCNO=" + custId + "&CONO=" + CONO;


                }
                Log.e("urlAccount", "" + URL_TO_HIT.toString());
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

                        double totalBalance = 0;
                        requestArray = new JSONArray(s);
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
                            if (requestDetail.getDebit() != 0.0) {
                                totalBalance -= requestDetail.getDebit();// دائن
                            }

                            if (requestDetail.getCredit() != 0.0) {

                                totalBalance += requestDetail.getCredit();// مدين

                            }
                            try {

                            requestDetail.setBalance(Double.parseDouble(generalMethod.convertToEnglish(generalMethod.getDecimalFormat(totalBalance))));
                        }catch (Exception e){
                                requestDetail.setBalance(totalBalance);
                            }
                            Log.e("onBindViewHolder", "=total=" + totalBalance);


                            listCustomerInfo.add(requestDetail);
                            //Log.e("listRequest", "listCustomerInfo" + listCustomerInfo.size());


                        }
                        if (type == 0) {
                            getAccountList_text.setText("2");
                        } else {
                            if (listCustomerInfo.size() != 0)
                                totalBalance_text.setText(convertToEnglish(getDecimal(listCustomerInfo.get(listCustomerInfo.size() - 1).getBalance()) + ""));
                            totalBalance_value=convertToEnglish(getDecimal(listCustomerInfo.get(listCustomerInfo.size() - 1).getBalance()) + "");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    if (s.contains("No Parameter Found")) {
                        if (type == 0) {
                            getAccountList_text.setText("3");
                        }
                    }

                }
                Log.e("onPostExecute", "" + s.toString());
//                progressDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            if(pdValidation!=null)
            pdValidation.dismissWithAnimation();
            super.onCancelled();
        }

    }

    public class JSONTask_AccountStatment_Withdate extends AsyncTask<String, String, String> {

        private String custId = "";
        private int type = 0;
        public String from_Date, to_Date;

        public JSONTask_AccountStatment_Withdate(String customerId, int typeImpo, String fromDate, String toDate) {
            this.custId = customerId;
            this.type = typeImpo;
            from_Date = fromDate;
            to_Date = toDate;
            Log.e("from_Date", "==" + from_Date + "to===" + to_Date);
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
        protected void onCancelled() {
            if(pdValidation!=null)
                pdValidation.dismissWithAnimation();
            super.onCancelled();
        }
        @Override
        protected String doInBackground(String... params) {

            try {

                if (!ipAddress.equals("")) {
                    //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
                    if (ipAddress.contains(":")) {
                        int ind = ipAddress.indexOf(":");
                        ipAddress = ipAddress.substring(0, ind);
                    }


                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/GetACCOUNTSTATMENTWITHDATE?ACCNO=" + custId +
                            "&CONO=" + CONO + "&FROMDATE=" + from_Date.trim() + "&TODATE=" + to_Date.trim();


                }
                Log.e("urlAccount", "" + URL_TO_HIT.toString());
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

                        double totalBalance = 0;
                        requestArray = new JSONArray(s);
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
                            if (requestDetail.getDebit() != 0.0) {
                                totalBalance -= requestDetail.getDebit();// دائن
                            }

                            if (requestDetail.getCredit() != 0.0) {

                                totalBalance += requestDetail.getCredit();// مدين

                            }

                            requestDetail.setBalance(totalBalance);
                            Log.e("onBindViewHolder", "=total=" + totalBalance);


                            listCustomerInfo.add(requestDetail);
                            //Log.e("listRequest", "listCustomerInfo" + listCustomerInfo.size());


                        }
                        if (type == 0) {
                            getAccountList_text.setText("2");
                        } else {
                            if (listCustomerInfo.size() != 0)
                                totalBalance_text.setText(convertToEnglish(getDecimal(listCustomerInfo.get(listCustomerInfo.size() - 1).getBalance()) + ""));
                            totalBalance_value=convertToEnglish(getDecimal(listCustomerInfo.get(listCustomerInfo.size() - 1).getBalance()) + "");
                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    if (s.contains("No Parameter Found")) {
                        if (type == 0) {
                            getAccountList_text.setText("3");
                        }
                    }

                }
                Log.e("onPostExecute", "" + s.toString());
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

///ADMUpdateLocation                        JSONSTR={"JSN"                        --> LATITUDE,LONGITUDE,SALESNO
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
    private class JSONTask_UpdateLocation_IIS extends AsyncTask<String, String, String> {

        JSONObject jsonObject;

        public JSONTask_UpdateLocation_IIS(JSONObject jsonObject) {
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
                    URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + headerDll.trim() + "/ADMUpdateLocation";

                    Log.e("getAddedCustomer","locat-URL_TO_HIT="+URL_TO_HIT);
                }
            } catch (Exception e) {

            }

            try {
                 JSONArray jsonArrayRequest = new JSONArray();
                 jsonArrayRequest.put(jsonObject);
                JSONObject  vouchersObject=new JSONObject();
                vouchersObject.put("JSN", jsonArrayRequest);
               // Log.e("getAddedCustomer","JSN"+vouchersObject);

//ADMUpdateLocation                        JSONSTR={"JSN"                        --> LATITUDE,LONGITUDE,SALESNO
                String JsonResponse = null;

                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString()));

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
                Log.e("onPostExecute", "fff"+s.toString());

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
                        previousIp = infoDetail.get("IpAddressDevice").toString();
                        Log.e("requestArray", "previousIp" + previousIp);
                        if (previousIp.equals("")) {
                            checkIpDevice.setText("2");// to add ip
                        } else {
                            checkIpDevice.setText(previousIp);
                        }

                        pdValidation.dismissWithAnimation();
//                        getAccountList_text.setText("2");

                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    if (s.contains("Not definded id")) {
                        checkIpDevice.setText("-1");
                    }
                    Log.e("onPostExecute", "" + s.toString());

                    pdValidation.dismissWithAnimation();
                }
            } else pdValidation.dismissWithAnimation();
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
                nameValuePairs.add(new BasicNameValuePair("SALESNO", Login.salesMan));
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
                } else {
                    if (s.contains("UPDATE_SALES_MAN_FAIL"))

                        pdValidation.dismissWithAnimation();
                }
            } else pdValidation.dismissWithAnimation();
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
                } else {

                    pdValidation.dismissWithAnimation();
                }
            } else pdValidation.dismissWithAnimation();
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
                        Customer.setMaxD(finalObject.getString("MaxD"));


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
                        try {

                            Customer.seteMail(finalObject.getString("EMail"));
                            Customer.setFax(finalObject.getString("Fax"));
                            Customer.setZipCode(finalObject.getString("ZipCode"));
                            Customer.setC_THECATEG(finalObject.getString("C_THECATEG"));
                            Log.e("C_THECATEG", "1Exception" + Customer.geteMail());

                        }catch (Exception e){
                            Log.e("C_THECATEG", "Exception" + e.getMessage());
                            Customer.setC_THECATEG("");
                            Customer.seteMail("");
                            Customer.setFax("");
                            Customer.setZipCode("");
                        }
                        //*******************************

                        customerList.add(Customer);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
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
            }
            catch (Exception e){
                Log.e("Customer", "3Exception********finally"+e.getMessage());
            }
            finally {
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


                    storeInDatabase_customer();


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


    public void getSalesmanPlan(int SalesmanNum) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.progress_getPlan));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();

        Calendar  myCalendar = Calendar.getInstance();
        int dayOfWeek=myCalendar.get(Calendar.DAY_OF_WEEK);
        Log.e("dayOfWeek=",dayOfWeek+"");


        List<Settings> settings = mHandler.getAllSettings();
        if (settings.size() != 0) {
            ipAddress = settings.get(0).getIpAddress();
         if(Login.Plan_Kind ==1)new JSONTask_GetSalesmanPlan(SalesmanNum,dayOfWeek+"").execute();
             else new JSONTask_GetSalesmanPlan(SalesmanNum,curentDate).execute();

        }
    }
    private class JSONTask_GetSalesmanPlan extends AsyncTask<String, String, String> {
      int  SalesmanNum;
      String date;

        public JSONTask_GetSalesmanPlan(int salesmanNum, String date) {
            SalesmanNum = salesmanNum;
            this.date = date;
        }

        @Override
        protected void onPreExecute() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onPreExecute();

        }

       @Override
        protected String doInBackground(String... params) {

            try {
                if (!ipAddress.equals("")) {

                   // http://10.0.0.22:8085/ADMGetPlan?CONO=290&SALESNO=1&PDATE=17/01/2022
                    URL_TO_HIT =
                    "http://" + ipAddress + ":"+ ipWithPort.trim() + headerDll.trim() +"/ADMGetPlan?CONO="+CONO.trim()+"&SALESNO="+SalesmanNum+"&PDATE="+date;//+date;

                    Log.e("link", "" +  URL_TO_HIT );
                }
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            try {

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




                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex)

            {
                ex.printStackTrace();
//                progressDialog.dismiss();
                progressDialog.dismiss();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            }
            catch (Exception e)

            {
                e.printStackTrace();
                Log.e("Exception", "" + e.getMessage());
//                progressDialog.dismiss();
                progressDialog.dismiss();
                return null;
            }


            //***************************

        }

     /*   @Override
        protected void onPostExecute(String array) {
            super.onPostExecute(array);
            progressDialog.dismiss();
            JSONObject result = null;


            if (array != null ) {

                if (array.length() != 0) {
                if(array.contains("CUSNAME"))
                {
                    for (int i = 0; i < array.length(); i++) {
                        try {

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SalesManPlan plan = new      SalesManPlan();
                        try {
                            plan.setDate(result.getString("TRDATE"));
                            plan.setSaleManNumber(Integer.parseInt(result.getString("SALESNO")));
                            plan.setLatitud(Double.parseDouble(result.getString("LA")));
                            plan.setLongtude(Double.parseDouble(result.getString("LO")));
                            plan.setCustName(result.getString("CUSNAME"));
                            plan.setCustNumber(result.getString("CUSTNO"));
                            plan.setOrder(Integer.parseInt(result.getString("ORDERD")));
                            plan.setTypeOrder(Integer.parseInt(result.getString("TYPEORDER")));
                            salesManPlanList.add( plan);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    MainActivity.salesmanPlanRespon.setText(""); }

                }
            }
        }*/
     @Override
     protected void onPostExecute(String array) {
         super.onPostExecute(array);
         progressDialog.dismiss();
         JSONObject jsonObject1 = null;
         if (array != null) {
             if (array.contains("CUSNAME")) {

                 if (array.length() != 0) {
                     try {
                         JSONArray requestArray = null;
                         requestArray = new JSONArray(array);
                         salesManPlanList.clear();
                         Log.e("requestArray==",""+requestArray.length());
                         for (int i = 0; i < requestArray.length(); i++) {
                             Log.e("sss===","sssss");
                             SalesManPlan plan = new      SalesManPlan();
                             jsonObject1 = requestArray.getJSONObject(i);
                                 plan.setDate(  jsonObject1.getString("TRDATE"));
                                 plan.setSaleManNumber(Integer.parseInt(  jsonObject1.getString("SALESNO")));
                            if( !jsonObject1.getString("LO").equals(""))
                            {plan.setLatitud(Double.parseDouble(  jsonObject1.getString("LA")));
                                 plan.setLongtude(Double.parseDouble(  jsonObject1.getString("LO")));}
                            else
                            {
                                plan.setLatitud(0);
                                    plan.setLongtude(0);

                            }
                                 plan.setCustName(  jsonObject1.getString("CUSNAME"));
                                 plan.setCustNumber(  jsonObject1.getString("CUSTNO"));
                                 plan.setOrder(Integer.parseInt(  jsonObject1.getString("ORDERD")));
                                 plan.setTypeOrder(Integer.parseInt(  jsonObject1.getString("TYPEORDER")));
                                 salesManPlanList.add( plan);
                         }
                         Log.e("salesManPlanList==",""+salesManPlanList.size());
                         MainActivity.salesmanPlanRespon.setText("fill");



                     } catch (JSONException e) {
                         e.printStackTrace();
                     }


                 }


                 Toast.makeText(context, ""+context.getResources().getString(R.string.saveSuccessfuly), Toast.LENGTH_SHORT).show();
             }
             else  if(array.contains("No Data Found")){
                 Toast.makeText(context, ""+context.getResources().getString(R.string.no_plan), Toast.LENGTH_SHORT).show();
             }
         } else {

                      }
     }

    }
    private class JSONTask_GetSalesmanGoal extends AsyncTask<String, String, String> {
        String  SalesmanNum;
        String date;

        public JSONTask_GetSalesmanGoal(String salesmanNum, String date) {
            SalesmanNum = salesmanNum;
            this.date = date;
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
            progressDialog.setMessage(context.getResources().getString(R.string.process));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                if (!ipAddress.equals("")) {

                    // http://10.0.0.22:8085/ADMGetPlan?CONO=290&SALESNO=1&PDATE=17/01/2022
                    URL_TO_HIT =
                            "http://" + ipAddress + ":"+ ipWithPort.trim() + headerDll.trim() +"/GetGoal?CONO="+CONO.trim()+"&SALE_MAN_NUMBER="+SalesmanNum+"&SMONTH="+date;

                    Log.e("link", "" +  URL_TO_HIT );
                }
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            try {

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




                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex)

            {
                ex.printStackTrace();
//                progressDialog.dismiss();
                progressDialog.dismiss();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            }
            catch (Exception e)

            {
                e.printStackTrace();
                Log.e("Exception", "" + e.getMessage());
//                progressDialog.dismiss();
                progressDialog.dismiss();
                return null;
            }


            //***************************

        }


        @Override
        protected void onPostExecute(String array) {
            super.onPostExecute(array);
            progressDialog.dismiss();
            JSONObject jsonObject1 = null;
            if (array != null) {
                if (array.contains("TARGET_NET_SALE")) {

                    if (array.length() != 0) {
                        try {
                            JSONArray requestArray = null;
                            requestArray = new JSONArray(array);
                            salesGoalsList.clear();
                            Log.e("requestArray==",""+requestArray.length());
                            for (int i = 0; i < requestArray.length(); i++) {
                                Log.e("sss===","sssss");
                                TargetDetalis targetDetalis = new      TargetDetalis();
                                jsonObject1 = requestArray.getJSONObject(i);


                                targetDetalis.setTargetNetSale(jsonObject1.getString("TARGET_NET_SALE") );
                          targetDetalis.setOrignalNetSale( jsonObject1.getString("REAL_NET_SALE"));
                                targetDetalis.setSalManNo( jsonObject1.getString("SALE_MAN_NUMBER"));
                                targetDetalis.setSalManName(jsonObject1.getString("SALE_MAN_NUMBER"));
                                targetDetalis.setDate(jsonObject1.getString("SMONTH"));
                                targetDetalis.setPERC(jsonObject1.getString("PERC"));
                                salesGoalsList.add( targetDetalis);
                                Log.e("targetDetalis==",""+targetDetalis.getTargetNetSale()+"  "+ targetDetalis.getOrignalNetSale());
                            }

                            Log.e("salesGoalsList==",""+salesGoalsList.size());




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    TargetReport. NetsalTargetRespon.setText("TARGET_NET_SALE");


                }
                else  if(array.contains("No Data Found")){
                    Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }

    }
    private class JSONTask_GetSaleGoalItems extends AsyncTask<String, String, String> {
        String  SalesmanNum;
        String date;

        public JSONTask_GetSaleGoalItems(String salesmanNum, String date) {
            SalesmanNum = salesmanNum;
            this.date = date;
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
            progressDialog.setMessage(context.getResources().getString(R.string.process));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                if (!ipAddress.equals("")) {

                    // http://10.0.0.22:8085/ADMGetPlan?CONO=290&SALESNO=1&PDATE=17/01/2022
//                    URL_TO_HIT ="http://" + ipAddress + ":"+ ipWithPort.trim() + headerDll.trim() +"/GetGoalItems?CONO="+CONO.trim()+"&SALE_MAN_NUMBER="+SalesmanNum+"&SMONTH="+date;
                    URL_TO_HIT ="http://" + ipAddress + ":"+ ipWithPort.trim() + headerDll.trim() +"/GetCommItems?CONO="+CONO.trim()+"&SALE_MAN_NUMBER="+SalesmanNum+"&SMONTH="+date;

                    Log.e("link", "" +  URL_TO_HIT );
                }
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            try {

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




                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex)

            {
                ex.printStackTrace();
//                progressDialog.dismiss();
                progressDialog.dismiss();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            }
            catch (Exception e)

            {
                e.printStackTrace();
                Log.e("Exception", "" + e.getMessage());
//                progressDialog.dismiss();
                progressDialog.dismiss();
                return null;
            }


            //***************************

        }


        @Override
        protected void onPostExecute(String array) {
            super.onPostExecute(array);
            progressDialog.dismiss();
            JSONObject jsonObject1 = null;
            if (array != null) {
                if (array.contains("SALE_MAN_NUMBER")) {

                    if (array.length() != 0) {
                        try {
                            JSONArray requestArray = null;
                            requestArray = new JSONArray(array);
                            ItemsGoalsList.clear();
                            Log.e("requestArray==",""+requestArray.length());
                            for (int i = 0; i < requestArray.length(); i++) {
                                Log.e("sss===","sssss");
                                TargetDetalis targetDetalis = new      TargetDetalis();
                                jsonObject1 = requestArray.getJSONObject(i);
                                targetDetalis.setSalManName(  jsonObject1.getString("SALE_MAN_NAME"));
                                targetDetalis.setSalManNo(jsonObject1.getString("SALE_MAN_NUMBER"));
                                targetDetalis.setItemTarget(jsonObject1.getString("ITEMTARGET"));
                                targetDetalis.setItemName( jsonObject1.getString("ITEMNAME"));
                                targetDetalis.setItemNo( jsonObject1.getString("ITEMOCODE"));
                                targetDetalis.setItemNo( jsonObject1.getString("ITEMOCODE"));
                                targetDetalis.setOrignalNetSale(jsonObject1.getString("REAL_NET_SALE"));
                                targetDetalis.setPERC(jsonObject1.getString("PERC"));
                                ItemsGoalsList.add(targetDetalis);
                            }
                            Log.e("salesGoalsList==",""+salesGoalsList.size());




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                   TargetReport. itemTargetRespon.setText("ITEMTARGET");


                }
                else  if(array.contains("No Data Found")){
                    Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }

    }
    private void getSuccsesfuly() {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(context.getResources().getString(R.string.saveSuccessfuly))

                            .show();
    }

    public void getPassowrdSetting(String type) {
        try {
            mHandler.DeleteAdminPasswords();
//            new JSONTask_getPassword().execute();
            new JSONTask_IIsgetPassword(type).execute();

        }
        catch (Exception e)
        {

        }

    }
    private class JSONTask_IIsgetPassword extends AsyncTask<String, String, String> {

        public String passwordValue = "";
        String passtype;

        public JSONTask_IIsgetPassword(String passtype) {
            this.passtype = passtype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                if (!ipAddress.equals("")) {
                    URL_TO_HIT = "http://" + ipAddress + ":" + ipWithPort + headerDll.trim() + "/ADMGetPassword?CONO=" + CONO + "&PASSWORDTYPE=" + passtype;

                    Log.e("URL_TO_HIT", URL_TO_HIT);
                }
            } catch (Exception e) {


            }
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
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


                return null;
            } catch (Exception e) {

                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String respon) {
            super.onPostExecute(respon);

            JSONObject jsonObject1 = null;
            if (respon != null)
            {
                Log.e("respon", respon);
                if (respon.contains("PASSWORDTYPE")) {
                    try {

                        Password password  = new Password();;
                        JSONArray requestArray = null;
                        requestArray = new JSONArray(respon);

                        for (int i = 0; i < requestArray.length(); i++) {


                            jsonObject1 = requestArray.getJSONObject(i);
                            password.setPassword_type(Integer.parseInt(jsonObject1.getString("PASSWORDTYPE")));
                            password.setPassword_no(jsonObject1.getString("PASSWORDKEY"));
                            mHandler.insertAdminPasswords(password);

                        }

                        if (passtype.equals("2")) {
                            Login.    Secondpassword_setting=   password.getPassword_no();
                        //  Login.  passwordrespon.setText("PASSWORDTYPE");

                        }


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                }

            } else {



            }


            if (passtype.equals("1")) {

                new JSONTask_IIsgetPassword("2").execute();

            }
        }


    }
  public void  GetPerformanceInfo (String cusno, String month){
            savingDialog3 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                       savingDialog3.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
                       savingDialog3.setTitleText(context.getString(R.string.getingData));
                       savingDialog3.setCancelable(false);
                       savingDialog3.show();
                           new JSONTask_GetPerformanceInfo(cusno,month).execute();

}

    private class JSONTask_GetPerformanceInfo extends AsyncTask<String, String, String> {

        String cusno="";
        String month;

        public JSONTask_GetPerformanceInfo(String cusno, String month) {
            this.cusno = cusno;
            this.month = month;
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

//
//                if(replacementlist.size()!=0)
//                    VHFNO=replacementlist.get(0).getTransNumber();
                    link = "http://" + ipAddress + ":" + ipWithPort + headerDll.trim() + "/GetTargertNS?CONO=" + CONO.trim()+"&ACCNO=" +cusno+"&MONTH=" +(Integer.parseInt(month)+1)+"";

                    Log.e("link===", "" + link);
                }
            } catch (Exception e) {
                Log.e("getAllSto", e.getMessage());
                savingDialog3 .dismiss();
            }

            try {

                //*************************************

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));

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


                //JSONArray parentObject = new JSONArray(finalJson);

                return finalJson;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        savingDialog3.dismiss();
                        Toast.makeText(context,"Ip Connection Failed", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception===", "" + e.getMessage());
                savingDialog3.dismiss();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(context, "The target server failed to respond", Toast.LENGTH_SHORT).show();
                        } catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }
                    }
                });
//                progressDialog.dismiss();
                return null;
            }


            //***************************

        }

        @Override
        protected void onPostExecute(String array) {
            super.onPostExecute(array);
            savingDialog3.dismiss();
            if (array != null) {

                JSONObject jsonObject1 = null;
                if (array.contains("ACHIVED")) {
                    try {
                        CustomersPerformance customersPerformance=new CustomersPerformance();

                        JSONArray requestArray = null;
                        requestArray = new JSONArray(array);

                        for (int i = 0; i < requestArray.length(); i++) {
//,"TARGET":"430","ACHIVED":"7446.45","VAL_VARIANCE":"7016.45"
                             customersPerformance = new CustomersPerformance();
                            jsonObject1 = requestArray.getJSONObject(i);
                            customersPerformance.setDetective(jsonObject1.getString("ACHIVED"));
                            customersPerformance.setRange(jsonObject1.getString("TARGET"));
                            customersPerformance.setDifference(jsonObject1.getString("VAL_VARIANCE"));



                        }

                       CustomersPerformanceReport. differencevalue.setText(customersPerformance.getDifference());
                        CustomersPerformanceReport    . Rangevalue.setText(customersPerformance.getRange());
                        CustomersPerformanceReport.    detectivevalue.setText(customersPerformance.getDetective());


                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                }else
                {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No data Found")

                            .show();

                    CustomersPerformanceReport. differencevalue.setText("");
                    CustomersPerformanceReport    . Rangevalue.setText("");
                    CustomersPerformanceReport.    detectivevalue.setText("");

                }
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText( context.getResources().getString(R.string.checkinternetConnection))

                        .show();

                CustomersPerformanceReport. differencevalue.setText("");
                CustomersPerformanceReport    . Rangevalue.setText("");
                CustomersPerformanceReport.    detectivevalue.setText("");

            }
        }


    }
}


