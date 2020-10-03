package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeExportJason extends AppCompatActivity {

    private Context context;
    private String fromDate, toDate;
    private int flag;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments, jsonArrayPaymentsPaper, jsonArrayAddedCustomer;
    DatabaseHandler mHandler;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();

    public DeExportJason(Context context, String fromDate, String toDate, int flag) {
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.flag = flag;
        this.mHandler = new DatabaseHandler(context);
    }

    void startExportDatabase() {

        jsonArrayVouchers = new JSONArray();
        jsonArrayItems = new JSONArray();
        jsonArrayPayments = new JSONArray();
        jsonArrayPaymentsPaper = new JSONArray();

        if (flag == 0) {
            vouchers = mHandler.getAllVouchers();
            items = mHandler.getAllItems();
            filterInvoice();

            for (int i = 0; i < vouchers.size(); i++) {
                jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
            }

            for (int i = 0; i < items.size(); i++) {
                jsonArrayItems.put(items.get(i).getJSONObject());
            }

            try {
                Log.e("export****", jsonArrayItems.get(jsonArrayItems.length() - 1).toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (flag == 1) {
            payments = mHandler.getAllPayments();
            paymentsPaper = mHandler.getAllPaymentsPaper();
            filterPayment();

            for (int i = 0; i < payments.size(); i++) {
                jsonArrayPayments.put(payments.get(i).getJSONObject());
            }

            for (int i = 0; i < paymentsPaper.size(); i++) {
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
            }

        } else {
            vouchers = mHandler.getAllVouchers();
            items = mHandler.getAllItems();
            payments = mHandler.getAllPayments();
            paymentsPaper = mHandler.getAllPaymentsPaper();
            filterInvoice();
            filterPayment();

            for (int i = 0; i < vouchers.size(); i++) {
                jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
            }

            for (int i = 0; i < items.size(); i++) {
                jsonArrayItems.put(items.get(i).getJSONObject());
            }

            for (int i = 0; i < payments.size(); i++) {
                payments.get(i).setIsPosted(1);
                jsonArrayPayments.put(payments.get(i).getJSONObject());
            }

            for (int i = 0; i < paymentsPaper.size(); i++) {
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
            }
            addedCustomer = mHandler.getAllAddedCustomer();
            jsonArrayAddedCustomer = new JSONArray();
            for (int i = 0; i < addedCustomer.size(); i++)
                {
                    jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObject());
                }

        }



//        new DeExportJason.JSONTask().execute();

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
                HttpPost request = new HttpPost ();

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
                nameValuePairs.add(new BasicNameValuePair("Added_Customers", jsonArrayAddedCustomer.toString().trim()));
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
                if (s!= null && s.contains("SUCCESS")) {
                    if(flag==0||flag==2)
                    {
                        mHandler.updateVoucher();
                        mHandler.updateVoucherDetails();
                    }
                    if(flag==1||flag==2)
                    {
                        mHandler.updatePayment();
                        mHandler.updatePaymentPaper();
                    }
                    if(flag==2)
                    {
                        mHandler.updateAddedCustomers();
                    }


                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    Toast.makeText(context, "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
                progressDialog.dismiss();

            }
            catch (Exception e)
            {
                Toast.makeText(context, "check Enternt connection ", Toast.LENGTH_SHORT).show();

            }



        }
    }


    public void filterInvoice() {

        Date dateFormat = null;
        String date="";

        for (int i = 0; i < vouchers.size(); i++) {
            String myDate = vouchers.get(i).getVoucherDate();
            try {
                dateFormat = sdf.parse(myDate);
                date=dateFormat.toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                if ((formatDate(date.toString()).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else
                    vouchers.remove(i);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < items.size(); i++) {
            String myDate = items.get(i).getDate();
            try {
                dateFormat = sdf.parse(myDate);
                date=dateFormat.toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else
                    items.remove(i);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void filterPayment() {
        Date dateFormat = null;
        String date="";
        for (int i = 0; i < payments.size(); i++) {
            String mydate = payments.get(i).getPayDate();
            try {
                dateFormat = sdf.parse(mydate);
                date=dateFormat.toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else
                    payments.remove(i);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < paymentsPaper.size(); i++) {
            String mydate = paymentsPaper.get(i).getPayDate();

            try {
                dateFormat = sdf.parse(mydate);
                date=dateFormat.toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                    // do nothing
                } else
                    paymentsPaper.remove(i);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public Date formatDate(String date) throws ParseException {
        Date d = new Date();
        SimpleDateFormat sdf;
        String myFormat = "dd/MM/yyyy";
        try {
           //In which you need put here
             sdf = new SimpleDateFormat(myFormat, Locale.US);
             d = sdf.parse(date);
        }
        catch (Exception e)
        {Log.e("","");}


        return d;
    }
}
