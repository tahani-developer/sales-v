package com.dr7.salesmanmanager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

   // public class RefreshData extends AppCompatActivity {
        public class RefreshData {

        private String URL_TO_HIT ;
        private Context context;
        private ProgressDialog progressDialog;
        DatabaseHandler mHandler;

        public static List<Customer> customerList = new ArrayList<>();
        public  static  List<SalesManItemsBalance> salesManItemsBalanceList= new ArrayList<>();
        public RefreshData(Context context){
            this.context = context ;
            this.mHandler = new DatabaseHandler(context);
        }

        public void startParsing(){
            List<Settings> settings =  mHandler.getAllSettings();
            if(settings.size() != 0) {
                String ipAddress = settings.get(0).getIpAddress();
                URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
                new JSONTask().execute(URL_TO_HIT);
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

                    String link= URL_TO_HIT;


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
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    String finalJson = sb.toString();
                    Log.e("finalJson*********" , finalJson);

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
                    }catch (Exception e)
                    {Log.e("Refresh_data",""+e.getMessage().toString());}

                    try {

                        JSONArray parentArrayItemQty = parentObject.getJSONArray("SalesMan_Items_Balance");
                        salesManItemsBalanceList.clear();
                        for (int i = 0; i < parentArrayItemQty.length(); i++) {
                            JSONObject finalObject = parentArrayItemQty.getJSONObject(i);
                            SalesManItemsBalance salesManItemsBalance=new SalesManItemsBalance();
                            salesManItemsBalance.setCompanyNo(finalObject.getInt("ComapnyNo"));
                            salesManItemsBalance.setSalesManNo(finalObject.getString("SalesManNo"));
                            salesManItemsBalance.setItemNo(finalObject.getString("ItemNo"));
                            salesManItemsBalance.setQty( finalObject.getDouble("Qty"));

                            salesManItemsBalanceList.add(salesManItemsBalance);
                        }
                    }catch (Exception e)
                    {Log.e("Refresh_salesmanItem",""+e.getMessage().toString());}





                } catch (MalformedURLException e) {
                    Log.e("Refresh_data", "********ex1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Refresh_data", e.getMessage().toString());
                    e.printStackTrace();

                } catch (JSONException e) {
                    Log.e("Refresh_data", "********ex3  "+e.toString());
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
                    Log.e("Customerr", "*****************" + customerList.size());
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
                     mHandler.updateCustomersPayment_Info(customerList.get(i).getCreditLimit(),customerList.get(i).getCashCredit(),customerList.get(i).getCustId());
                }
                for(int i=0;i<salesManItemsBalanceList.size();i++)
                {
//                    mHandler.updateSalesManItemBalance("1","1144",100);
                    mHandler.updateSalesManItemBalance(salesManItemsBalanceList.get(i).getSalesManNo(),
                            salesManItemsBalanceList.get(i).getItemNo(),salesManItemsBalanceList.get(i).getQty());

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
                Toast.makeText(context , s , Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }
    }

