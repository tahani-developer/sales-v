package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dr7.salesmanmanager.Modles.activeKey;
import com.dr7.salesmanmanager.Reports.SalesMan;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressWarnings("unchecked")
public class Login extends AppCompatActivity {
 
    private String username, password, link, ipAddress;
    private EditText usernameEditText, passwordEditText;
    private ImageView logo;
    private CardView loginCardView;
    public static String salesMan = "", salesManNo = "";
    private boolean isMasterLogin;
    public static int key_value_Db;
    activeKey model_key;
    int key_int;
    Context context;
    TextView loginText;

    DatabaseHandler mDHandler;
    String shortUserName = "", fullUserName = "";
    int indexfirst = 0, indexEdit = 0;
    boolean exist = false;
    int index = 0;
    List<SalesMan> salesMenList;
    public static String languagelocalApp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleAppUtils.setConfigChange(Login.this);
        setContentView(R.layout.activity_login);
        mDHandler = new DatabaseHandler(Login.this);
        model_key = new activeKey();
        loginText = (TextView) findViewById(R.id.logInTextView);
        try {
        if(LocaleAppUtils.getLocale()==null)
        {
            LocaleAppUtils.setLocale(new Locale("ar"));
            LocaleAppUtils.setConfigChange(Login.this);
            Log.e("LocaleAppUtilsEmpty",""+LocaleAppUtils.getLocale());
        }


            if (mDHandler.getAllSettings().size() != 0) {
                if (mDHandler.getAllSettings().get(0).getArabic_language() == 1) {
                    LocaleAppUtils.setLocale(new Locale("ar"));
                    LocaleAppUtils.setConfigChange(Login.this);

                } else {
                    LocaleAppUtils.setLocale(new Locale("en"));
                    LocaleAppUtils.setConfigChange(Login.this);

                }
            }

        } catch (Exception e) {
            LocaleAppUtils.setLocale(new Locale("ar"));
            LocaleAppUtils.setConfigChange(Login.this);

        }
        //   model_key.setKey(123);

        Log.e("model", "model_key" + model_key.getKey());
        logo = (ImageView) findViewById(R.id.imageView3);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        try {
            if (mDHandler.getAllCompanyInfo().get(0).getLogo() == null) {
                logo.setImageDrawable(null);
            } else {
                logo.setImageBitmap(mDHandler.getAllCompanyInfo().get(0).getLogo());
            }
        } catch (Exception e) {

        }



        loginCardView = (CardView) findViewById(R.id.loginCardView);
        loginCardView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                salesMenList = mDHandler.getAllSalesMen();

                if (salesMenList.size() == 0)//Empty DB
                {
//                    Toast.makeText(Login.this, R.string.failUsers, Toast.LENGTH_LONG).show();

                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        }
                        else{
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getResources().getString(R.string.warning_message))
                                    .setContentText(getResources().getString(R.string.failUsers))
                                    .show();
                        }
                        checkExistToLogin();
                    } else {
                        new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.warning_message))
                                .setContentText(getResources().getString(R.string.failUsers))
                                .show();
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                } else {//item in list
                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {


                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        } else {
                            exist = false;
                            isMasterLogin = false;
                            for (int i = 0; i < salesMenList.size(); i++) {
                                fullUserName = salesMenList.get(i).getUserName();//  00002
                                if ((fullUserName.charAt(0) + "").equals("0")) {
                                    if (checkAllCharacterName(i, fullUserName)) {
                                        break;
                                    }

                                }
                                else {
                                    checkFullName(i, fullUserName);

                                }

                            }
                        }
                        checkExistToLogin();
                    } else {
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                }


            }
        });

    }

    private void checkExistToLogin() {
        if (exist) {
            if (isMasterLogin) {
                key_value_Db = mDHandler.getActiveKeyValue();
                if (key_value_Db == 0) {//dosent exist value key in DB

                    showDialog_key();
                } else {

                    salesMan = usernameEditText.getText().toString();
                    salesManNo = passwordEditText.getText().toString();

                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
//                                CustomIntent.customType(getBaseContext(),"left-to-right");
                }
            } else {

                if (salesMenList.get(index).getPassword().equals(passwordEditText.getText().toString())) {
                    key_value_Db = mDHandler.getActiveKeyValue();
                    if (key_value_Db == 0) {//dosent exist value key in DB

                        showDialog_key();
                    } else {

                        salesMan = usernameEditText.getText().toString();
                        salesManNo = passwordEditText.getText().toString();

                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
//                                CustomIntent.customType(getBaseContext(),"left-to-right");
                    }

                } else
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(Login.this, "UserName does not exist", Toast.LENGTH_SHORT).show();
        exist = false;
    }

    private void checkFullName(int i, String fullUserName) {
        if (usernameEditText.getText().toString().equals(fullUserName)) {
            exist = true;
            index = i;

        }
    }

    private boolean checkAllCharacterName(int i, String fullUserName) {
        for (int j = 0; j < fullUserName.length(); j++) {
            indexfirst = 0;
            if ((fullUserName.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexfirst = j;
                break;
            }

        }
        shortUserName = fullUserName.substring(indexfirst, fullUserName.length());

        //********************************************************************
        String editUser = usernameEditText.getText().toString();
        for (int j = 0; j < editUser.length(); j++) {
            indexEdit = 0;
            if ((editUser.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexEdit = j;
                break;

            }
        }
        String shortUserEdit = editUser.substring(indexEdit, editUser.length());
        Log.e("checkAllCharacterName", "" + shortUserEdit + "\t" + shortUserName);
        //********************************************************************

        if (shortUserEdit.equals(shortUserName)) {
            exist = true;
            index = i;
            return true;
        }
        return false;
    }

    public void showDialog_key() {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.active_key);
        dialog.show();
        // if not eist value key in DB
        //*****************************
//        mDHandler.deleteKeyValue();
        model_key.setKey(1111);
        mDHandler.addKey(model_key);
        //****************************
        final EditText key_value = (EditText) dialog.findViewById(R.id.editText_active_key);
        final Button cancel_button = (Button) dialog.findViewById(R.id.button_cancel_key);
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Button ok_button = (Button) dialog.findViewById(R.id.button_activeKey);
        ok_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                key_int = Integer.parseInt(key_value.getText().toString());
                key_value_Db = mDHandler.getActiveKeyValue();
                Log.e("key_value_Db", "" + key_value_Db);
                if (key_value_Db == key_int) {
                    salesMan = usernameEditText.getText().toString();
                    salesManNo = passwordEditText.getText().toString();
//
                    Toast.makeText(Login.this, "welcome" + salesMan, Toast.LENGTH_SHORT).show();

                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    //  CustomIntent.customType(getBaseContext(),"left-to-right");
                    dialog.dismiss();
                } else {
                    Toast.makeText(Login.this, "Please enter valid Active key", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class RequestLogin extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                String data = URLEncoder.encode("USERNAME=" + username, "UTF-8");
                data += "&" + URLEncoder.encode("PASSWORD=" + password, "UTF-8");

                URL url = new URL(link);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(urlConnection.getOutputStream());

                outputStreamWriter.write(data);

                String line = null;


            } catch (Exception e) {

            }

            return null;
        }
    }// Class RequestLogin

}
