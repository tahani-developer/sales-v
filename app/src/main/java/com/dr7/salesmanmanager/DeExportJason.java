package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.ExportJason.CONO;
import static com.dr7.salesmanmanager.ExportJason.SalesManLogin;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.Methods.convertToEnglish;

public class DeExportJason extends AppCompatActivity {
    SweetAlertDialog pdVoucher=null,pdPayment=null;
    private Context context;
    private String fromDate, toDate;
    private int flag;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArraySerial, jsonArrayItems, jsonArrayPayments, jsonArrayPaymentsPaper, jsonArrayAddedCustomer;
    DatabaseHandler mHandler;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    JSONObject vouchersObject;
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<Payment> paymentsPaperCopy = new ArrayList<>();
    public static List<serialModel> serialModelList = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();
    String ipAddress="",ipWithPort="",URL_TO_HIT="";
    String headerDll="";

    public DeExportJason(Context context, String fromDate, String toDate, int flag) {
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.flag = flag;
        this.mHandler = new DatabaseHandler(context);
        Log.e("DeExportJason", "" + fromDate + "\t" + toDate);
        fillIpAddressWithPort();

    }


    void startExportDatabase() {

        jsonArrayVouchers = new JSONArray();
        jsonArrayItems = new JSONArray();
        jsonArrayPayments = new JSONArray();
        jsonArrayPaymentsPaper = new JSONArray();
        jsonArraySerial = new JSONArray();

        if (flag == 0) {
            vouchers = mHandler.getAllVouchers();
            items = mHandler.getAllItems();
            filterInvoice();

            for (int i = 0; i < vouchers.size(); i++) {
                if(typaImport==1)//IIs
                {
                    jsonArrayVouchers.put(vouchers.get(i).getJSONObjectDelphi());
                }
                else jsonArrayVouchers.put(vouchers.get(i).getJSONObject());

            }

            for (int i = 0; i < items.size(); i++) {
                if(typaImport==1)//IIs
                {
                    jsonArrayItems.put(items.get(i).getJSONObjectDelphi());
                }else jsonArrayItems.put(items.get(i).getJSONObject());
            }

            if(typaImport==1)//IIs
            {
                exportSalesVoucherM();
            }


                Log.e("export****", "");


        } else if (flag == 1) {
            payments = mHandler.getAllPayments();
            paymentsPaper = mHandler.getAllPaymentsPaper();
            filterPayment();

            for (int i = 0; i < payments.size(); i++) {
                if(typaImport==1)//IIs
                     { jsonArrayPayments.put(payments.get(i).getJSONObjectDelphi());}
                else  jsonArrayPayments.put(payments.get(i).getJSONObject());

            }

            for (int i = 0; i < paymentsPaper.size(); i++) {
                if(typaImport==1)//IIs
                {
                    jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObjectDelphi());
                }
                else {
                    jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
                }
            }
            if(typaImport==1)//IIs
            {
                exportPayment();

            }


        } else {

            vouchers = mHandler.getAllVouchers();
            items = mHandler.getAllItems();
            payments = mHandler.getAllPayments();
            paymentsPaper = mHandler.getAllPaymentsPaper();
//            serialModelList = mHandler.getAllSerialItems();
            addedCustomer = mHandler.getAllAddedCustomer();


            filterInvoice();
            filterPayment();

            for (int i = 0; i < vouchers.size(); i++) {
                jsonArrayVouchers.put(vouchers.get(i).getJSONObjectDelphi());
            }

            for (int i = 0; i < items.size(); i++) {
                jsonArrayItems.put(items.get(i).getJSONObjectDelphi());
            }

            for (int i = 0; i < payments.size(); i++) {
                payments.get(i).setIsPosted(1);
                jsonArrayPayments.put(payments.get(i).getJSONObjectDelphi());
            }

            for (int i = 0; i < paymentsPaper.size(); i++) {
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2Delphi());
            }

            jsonArrayAddedCustomer = new JSONArray();
            for (int i = 0; i < addedCustomer.size(); i++) {
                jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObjectDelphi());
            }
            if(typaImport==1)//IIs
            {
                exportSalesVoucherM();
               // exportPayment();
            }
//                for (int i = 0; i < serialModelList.size(); i++)
//                {
//                    jsonArraySerial.put(serialModelList.get(i).getJSONObject());
//
//                }
        }
        if(typaImport==0)//mysql
        {
            new DeExportJason.JSONTask().execute();

        }



    }
    private void exportPayment() {
        getVouchers(jsonArrayPayments);
        pdPayment = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdPayment.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdPayment.setTitleText(" Start export Payment");
        pdPayment.setCancelable(false);
        pdPayment.show();
        new JSONTaskDelphiPayment().execute();
    }
    private class JSONTaskDelphiPayment extends AsyncTask<String, String, String> {

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
                Toast.makeText(DeExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
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
            pdPayment.setTitle("Export Payment");
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
                    exportPaymentPaper();

                }else {
                    pdVoucher.dismissWithAnimation();
                }

            } else {
                pdPayment.dismiss();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exportPaymentPaper() {
        getVouchers(jsonArrayPaymentsPaper);
        new JSONTaskDelphiPaymentPaper().execute();
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

            pdPayment.setTitle("Export Payment Paper");
            if (result != null && !result.equals("")) {
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();
                if(result.contains("Saved Successfully"))
                {
                    savePayment();
                }else {
                    pdPayment.dismiss();
                }

            } else {
                pdPayment.dismiss();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void savePayment() {
        new JSONTaskSavePayment().execute();
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
            pdPayment.dismiss();
        }
    }
    private void getVouchers(JSONArray jsonArrayVouchers) {
        try {
            vouchersObject=new JSONObject();
            vouchersObject.put("JSN",jsonArrayVouchers);
            Log.e("vouchersObject",""+vouchersObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillIpAddressWithPort() {
        ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
        ipWithPort=mHandler.getAllSettings().get(0).getIpPort();
        CONO=mHandler.getAllSettings().get(0).getCoNo();
        SalesManLogin= mHandler.getAllUserNo();
//        headerDll="/Falcons/VAN.dll";
        headerDll="";
        if (!ipAddress.equals("")) {
            //http://10.0.0.22:8082/GetTheUnCollectedCheques?ACCNO=1224
            //  URL_TO_HIT = "http://" + ipAddress +"/Falcons/VAN.dll/GetACCOUNTSTATMENT?ACCNO=402001100";
            if (ipAddress.contains(":")) {
                int ind = ipAddress.indexOf(":");
                ipAddress = ipAddress.substring(0, ind);
            }
        } else {
            Toast.makeText(context, "Fill Ip adress", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportSalesVoucherM() {
        getVouchers(jsonArrayVouchers);
        pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdVoucher.setTitleText(" Start export Vouchers");
        pdVoucher.setCancelable(false);
        pdVoucher.show();
        new JSONTaskDelphi().execute();

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

            try {                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {

                Toast.makeText(DeExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
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


            } else {
                pdVoucher.dismissWithAnimation();
                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exportVoucherDetail() {
        getVouchers(jsonArrayItems);
        new JSONTaskDelphiDetail().execute();
    }
    private class JSONTaskDelphiDetail extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
//        SweetAlertDialog pdItem=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

//                try {
            //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//
            String link = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +headerDll.trim()+ "/ExportSALES_VOUCHER_D";

            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {

                Toast.makeText(DeExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
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
           // Log.e("onPostExecute","ExportSALES_VOUCHER_D"+result);
            pdVoucher.setTitle("Export SALES_VOUCHER_Detail");
            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully"))
                {
                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    saveExpot();
                }
                else {
                    pdVoucher.dismiss();
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
    private class JSONTaskSaveVouchers extends AsyncTask<String, String, String> {
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

            pdVoucher.dismiss();
            if (result != null && !result.equals("")) {

//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();

            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JSONTask extends AsyncTask<String, String, String> {

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
        protected String doInBackground(String... params) {


//            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String ipAddress = mHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
//                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
//
//                URL url = new URL(URL_TO_HIT);
//
//                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
//                        URLEncoder.encode(String.valueOf('2'), "UTF-8");


                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();

                try {
                    request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "2"));
                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_M", jsonArrayVouchers.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_D", jsonArrayItems.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments", jsonArrayPayments.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments_Checks", jsonArrayPaymentsPaper.toString().trim()));
                // nameValuePairs.add(new BasicNameValuePair("Added_Customers", jsonArrayAddedCustomer.toString().trim()));
                // nameValuePairs.add(new BasicNameValuePair("ITEMSERIALS", jsonArraySerial.toString().trim()));

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
                Log.e("tag", "" + JsonResponse);

                return JsonResponse;


//
//                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
//                table1 += "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
//                        + "&" + "Payments=" + jsonArrayPayments.toString().trim()
//                        + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim()
//                        + "&" + "added_customers=" + jsonArrayAddedCustomer.toString().trim();
//                URLConnection conn = url.openConnection();
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
//                try {
//                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                    wr.write(table1);
//
//                    wr.flush();
//                } catch (Exception e) {
//                    Log.e("here****", e.getMessage());
//                }


                // get response
//                reader = new BufferedReader(new
//                        InputStreamReader(conn.getInputStream()));
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                JsonResponse = sb.toString();
//                Log.e("tag", "" + JsonResponse);

//                return JsonResponse;

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
            try {
                if (s != null && s.contains("SUCCESS")) {
                    if (flag == 0 || flag == 2) {
                        mHandler.updateVoucher();
                        mHandler.updateVoucherDetails();
                    }
                    if (flag == 1 || flag == 2) {
                        mHandler.updatePayment();
                        mHandler.updatePaymentPaper();
                    }
                    if (flag == 2) {
                        mHandler.updateAddedCustomers();
                        mHandler.updateSerialTableIsposted();
                    }


                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    Toast.makeText(context, "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
                progressDialog.dismiss();

            } catch (Exception e) {
                Toast.makeText(context, "check Enternt connection ", Toast.LENGTH_SHORT).show();

            }


        }
    }

    public void filterInvoice() {

        Date dateFormat = null;
        String date = "";

        Log.e("vouchers", "" + vouchers.size());
        for (int i = 0; i < vouchers.size(); i++) {
            String myDate = vouchers.get(i).getVoucherDate();
            date = formatNew(myDate);

            try {
                if ((formatDate(date.toString()).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else {
                    vouchers.remove(i);
                    i--;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.e("vouchers2", "" + vouchers.size());
        Log.e("items", "" + items.size());
        for (int i = 0; i < items.size(); i++) {
            String myDate = items.get(i).getDate();

            date = formatNew(myDate);


            try {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else {
                    items.remove(i);
                    i--;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.e("items2", "" + items.size());
    }

    public void filterPayment() {
        paymentsPaperCopy = new ArrayList<>();
        Date dateFormat = null;
        String date = "";
        Log.e("payments1", "" + payments.size());
        for (int i = 0; i < payments.size(); i++) {
            String mydate = payments.get(i).getPayDate().toString().trim();
            date = formatNew(mydate);


            try {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else {
                    payments.remove(i);
                    i--;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < paymentsPaper.size(); i++) {
            for (int j = 0; j < payments.size(); j++) {
                if (paymentsPaper.get(i).getVoucherNumber() == payments.get(j).getVoucherNumber()) {
                    paymentsPaperCopy.add(paymentsPaper.get(i));
                }

            }

        }
        paymentsPaper.clear();
        paymentsPaper = paymentsPaperCopy;

    }

    private String formatNew(String mydate) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = fmt.parse(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        return convertToEnglish(fmtOut.format(date));
    }

    public Date formatDate(String date) throws ParseException {
        Date d = new Date();
        SimpleDateFormat sdf;
        String myFormat = "dd/MM/yyyy";
        try {
            //In which you need put here
            sdf = new SimpleDateFormat(myFormat, Locale.US);
            d = sdf.parse(date);
        } catch (Exception e) {
            Log.e("", "");
        }


        return d;
    }
}
