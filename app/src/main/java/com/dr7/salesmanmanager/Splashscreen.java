package com.dr7.salesmanmanager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dr7.salesmanmanager.Reports.SalesMan;

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


public class Splashscreen extends Activity {

    private final String URL_TO_HIT = "http://10.0.0.115/VANSALES_WEB_SERVICE/index.php";
    private ProgressDialog progressDialog;

    public static List<SalesMan> salesMenList = new ArrayList<>();

    public void onAttachedToWindow() {

        super.onAttachedToWindow();

        Window window = getWindow();

        window.setFormat(PixelFormat.RGBA_8888);

    }

    Thread splashTread;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

//        DatabaseHandler mHandler = new DatabaseHandler(Splashscreen.this);
//        String ipAddress = mHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
//        URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

//        new JSONTask().execute(URL_TO_HIT);

        StartAnimations();

    }

    private class JSONTask extends AsyncTask<String, String, List<SalesMan>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Splashscreen.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected List<SalesMan> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                String link= URL_TO_HIT;
                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('1'), "UTF-8");

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
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String finalJson = sb.toString();
                Log.e("finalJson*********" , finalJson);

                JSONObject parentObject = new JSONObject(finalJson);

                JSONArray parentArrayCustomers = parentObject.getJSONArray("SALESMEN");
                salesMenList.clear();
                for (int i = 0; i < parentArrayCustomers.length(); i++) {
                    JSONObject finalObject = parentArrayCustomers.getJSONObject(i);

                    SalesMan salesMan = new SalesMan();
                    salesMan.setUserName(finalObject.getString("SALESNO"));
                    salesMan.setPassword(finalObject.getString("USER_PASSWORD"));

                    salesMenList.add(salesMan);
                }

            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Customer", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3");
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");
                if (connection != null) {
                    Log.e("Customer", "********ex4");
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
            return salesMenList;
        }


        @Override
        protected void onPostExecute(final List<SalesMan> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
                Log.e("SalesMan", "*****************" + result.size());
            } else {
                Toast.makeText(Splashscreen.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);

        anim.reset();

        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);

        l.clearAnimation();

        l.startAnimation(anim);


        anim = AnimationUtils.loadAnimation(this, R.anim.translate);

        anim.reset();

        ImageView iv = (ImageView) findViewById(R.id.splash);

        iv.clearAnimation();

        iv.startAnimation(anim);


        splashTread = new Thread() {

            @Override

            public void run() {

                try {

                    int waited = 0;

                    // Splash screen pause time

                    while (waited < 3500) {

                        sleep(100);

                        waited += 100;

                    }

                    Intent intent = new Intent(Splashscreen.this,

                            Login.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);

                    Splashscreen.this.finish();

                } catch (InterruptedException e) {

                    // do nothing

                } finally {

                    Splashscreen.this.finish();

                }


            }

        };

        splashTread.start();


    }
}



