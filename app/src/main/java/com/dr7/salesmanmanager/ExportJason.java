package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExportJason extends AppCompatActivity {

    private Context context;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments , jsonArrayPaymentsPaper;
    DatabaseHandler mHandler;

    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<Voucher> requestVouchers = new ArrayList<>();
    public static List<Item> requestItems = new ArrayList<>();

    public ExportJason(Context context) {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
    }

    void startExportDatabase() {

//        transactions = mHandler.getAlltransactions();
//        JSONArray jsonArrayTransactions = new JSONArray();
//        for (int i = 0; i < transactions.size(); i++)
//            jsonArrayTransactions.put(transactions.get(i).getJSONObject());
//
        vouchers = mHandler.getAllVouchers();
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
            if (vouchers.get(i).getIsPosted() == 0) {
                vouchers.get(i).setIsPosted(1);
                jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
            }
        Log.e("hhhhh","**************************" +jsonArrayVouchers.toString());

        items = mHandler.getAllItems();
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getIsPosted() == 0) {
                items.get(i).setIsPosted(1);
                jsonArrayItems.put(items.get(i).getJSONObject());
            }

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

        new ExportJason.JSONTask().execute();



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

            String JsonResponse = null;
//            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String ipAddress = mHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

                URL url = new URL(URL_TO_HIT);

                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('2'), "UTF-8");

               /* String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
                String table2 = data + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim();
                String table3 = data + "&" + "Payments="        + jsonArrayPayments.toString().trim();
                String table4 = data + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim();
*/
                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
                table1 +="&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
                        // + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
                        + "&" + "Payments="        + jsonArrayPayments.toString().trim()
                        + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(table1);

                wr.flush();

                // get response
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonResponse = sb.toString();

                return JsonResponse;

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

            if(s.contains("SUCCESS")){
                mHandler.updateVoucher();
                mHandler.updateVoucherDetails();
                mHandler.updatePayment();
                mHandler.updatePaymentPaper();
                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(ExportJason.this , "Failed to export data" , Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
