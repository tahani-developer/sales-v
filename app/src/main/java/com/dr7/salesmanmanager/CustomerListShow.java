package com.dr7.salesmanmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.dr7.salesmanmanager.Adapters.CustomersListAdapter;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Settings;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.zxing.integration.android.IntentIntegrator;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.CustomerCheckInFragment.customerList;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.MainActivity.EndTrip_Report;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomerListShow extends DialogFragment {
    private String URL_TO_HIT = "";

    public int showCustomerLoc = 0;
    public ListView itemsListView;
    //    public List<Customer> customerList;
    public List<Customer> emptyCustomerList;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private Button update;
    public static EditText customerNameTextView;
    public static String Customer_Name = "No Customer Selected !", Customer_Account = "", PriceListId = "";
    public static int CashCredit, paymentTerm = 1;
    public static double CreditLimit = 0;
    public static String latitude = "", longtude = "";

    public static double Max_Discount_value = 0;
    public static int CustHideValu = 0;

    CustomersListAdapter customersListAdapter;
    DatabaseHandler mHandler;
    private ProgressDialog progressDialog;
    LinearLayout mainlayout, linearFilter;
    TextView mSpeakBtn, btnScan;
    String ipAddress = "", ipWithPort, SalesManLogin, CONO;

    Spinner classificatSp, accSp, spiciliSp, categSp;
    List<String> classifi_items = new ArrayList<>();
    List<String> accn_items = new ArrayList<>();
    List<String> spicilisty_items = new ArrayList<>();
    List<String> categ_items = new ArrayList<>();
    int showCustomerList = 0;
    List<Settings> settings;
    final Handler handler = new Handler();
    TextView loadingText;

    public CustomerListShow.CustomerListShow_interface getListener() {
        return listener;
    }

    public interface CustomerListShow_interface {
        public void displayCustomerListShow();
    }

    public CustomerListShow.CustomerListShow_interface listener;

    CustomerListShow_interface customerListShow_interface;

    public CustomerListShow() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        new LocaleAppUtils().changeLayot(getContext());
        getDialog().setTitle(getResources().getString(R.string.app_select_customer));
        final View view = inflater.inflate(R.layout.customers_list, container, false);
        mainlayout = (LinearLayout) view.findViewById(R.id.discLayout);
        mSpeakBtn = view.findViewById(R.id.btnSpeak);
        linearFilter = (LinearLayout) view.findViewById(R.id.linearFilter);
        loadingText= (TextView) view.findViewById(R.id.loadingText);
        loadingText.setVisibility(View.VISIBLE);
        if (EndTrip_Report == 1)
            linearFilter.setVisibility(View.VISIBLE);
        else linearFilter.setVisibility(View.GONE);
        spiciliSp = (Spinner) view.findViewById(R.id.spiciliSp);
        accSp = (Spinner) view.findViewById(R.id.accSp);
        classificatSp = (Spinner) view.findViewById(R.id.classificatSp);
        categSp = (Spinner) view.findViewById(R.id.categSp);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("startVoiceInput2", "on");
                startVoiceInput(1);
            }
        });
        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> {
            openSmallScanerTextView();
        });
        try {
            if (languagelocalApp.equals("ar")) {
                mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


//        customerList = new ArrayList<>();
        emptyCustomerList = new ArrayList<>();

        mHandler = new DatabaseHandler(getActivity());
        initialize(view);

        if (settings.size() != 0)

            showCustomerList = settings.get(0).getShowCustomerList();
        if (settings.size() != 0) {
//
//            if (settings.get(0).getSalesManCustomers() == 1)
//            {
//                customerList = mHandler.getCustomersBySalesMan(Login.salesMan);
//            }
//
//            else
//            {
//                customerList = mHandler.getAllCustomers();
//
//            }


            showCustomerLoc = settings.get(0).getShowCustomerLocation();

            handler.post(new Runnable() {
                public void run() {

                    if (settings.get(0).getShowCustomerList() == 1) {
                        Log.e("c1", "c1");
                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
                        itemsListView.setAdapter(customersListAdapter);
                        loadingText.setVisibility(View.GONE);
                        itemsListView.setVisibility(View.VISIBLE);


                    } else
                    {
                        Log.e("c2", "c2");
                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), emptyCustomerList, showCustomerLoc);
                        itemsListView.setAdapter(customersListAdapter);
                        loadingText.setVisibility(View.GONE);
                        itemsListView.setVisibility(View.VISIBLE);
                    }
                }
            });


            if (EndTrip_Report == 1)
                fillSpiners();

/*
        for(int i=0;i< customerList .size();i++)
            for(int j=0;j<  MainActivity.customerArrayList .size();j++)
                if( MainActivity.customerArrayList.get(j).getCustName().equals
                        (customerList.get(i).getCustName()))
                {
                    customerList.get(i).setCustLat(String.valueOf(MainActivity.customerArrayList.get(j).getLatitude()));
                    customerList.get(i).setCustLat(String.valueOf(MainActivity.customerArrayList.get(j).getLongtitude()));
                     Log.e("",customerList.get(i).getCustName()+" "+MainActivity.customerArrayList.get(j).getCustName());
                         break;
                }


*/


        } else {
            Toast.makeText(getActivity(), "Empty Data", Toast.LENGTH_SHORT).show();
        }


        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                LinearLayout linearLayout = (LinearLayout) parent.getChildAt(1);
//                EditText editText = (EditText) linearLayout.getChildAt(1) ;
//                Customer_Name = editText.getText().toString();
//                Customer_Account = customerList.get(position).getCustId() + "";
//                CashCredit = customerList.get(position).getCashCredit();
//                PriceListId = customerList.get(position).getPriceListId();
//
//                if(customerList.get(position).getIsSuspended() == 1){
//                    Toast toast = Toast.makeText(getActivity(), "This customer is susbended", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 180);
//                    ViewGroup group = (ViewGroup) toast.getView();
//                    TextView messageTextView = (TextView) group.getChildAt(0);
//                    messageTextView.setTextSize(25);
//                    toast.show();
//                } else {
//                    CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment();
//                    customerCheckInFragment.settext1();
//                }

//                dismiss();
            }
        });

        Log.e("customerList555===", customerList.size() + "");
        if (Login.SalsManPlanFlage == 1) {

            if (MainActivity.DB_salesManPlanList.size() != 0) {
                int k = 1;
                Log.e("customerList===", customerList.size() + "");
                // remove customer not in plan
                for (int i = 0; i < customerList.size(); i++)
                    if (IsInPlan(customerList.get(i).getCustId().trim())) {

                    } else {
                        customerList.remove(i);
                        i--;

                    }
            }

            Log.e("customerList=", customerList.size() + "");



          /*  for (int i = 0; i < customerList.size(); i++)
                for (int x = 0; x < MainActivity .customerArrayList.size(); x++)
                    if(customerList.get(i).getCustId().
                            equals(MainActivity .customerArrayList.get(x).getc)) {
                        Log.e("customerLocation",MainActivity .customerArrayList.get(x).getCustId()+",,"+customerList.get(x).getCustId());
                        customerList.get(i).setCustLat(MainActivity .customerArrayList.get(x).getCustLat());
                        customerList.get(i).setCustLong(MainActivity .customerArrayList.get(x).getCustLong());
                    }*/

        }

        customerNameTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
//
                    // Call back the Adapter with current character to Filter
//                    customersListAdapter.getFilter().filter(s.toString());
                    searchNormal(s.toString());


                } else {
//                    if (showCustomerList == 1) {
////
//                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
//                        itemsListView.setAdapter(customersListAdapter);
//                        //customersListAdapter.notifyDataSetChanged();
//                    } else {
//                        Log.e("c5", "c5");
//                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), emptyCustomerList, showCustomerLoc);
//                        itemsListView.setAdapter(customersListAdapter);
//                        // customersListAdapter.notifyDataSetChanged();
//                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
//                    if (showCustomerList == 1) {
////
//                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
//                        itemsListView.setAdapter(customersListAdapter);
//                        //customersListAdapter.notifyDataSetChanged();
//                    } else {
//                        Log.e("c5", "c5");
//                        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), emptyCustomerList, showCustomerLoc);
//                        itemsListView.setAdapter(customersListAdapter);
//                        // customersListAdapter.notifyDataSetChanged();
//                    }
                }
            }
        });

        return view;
    }

    private void searchNormal(String querey) {
        List<Customer> filteredList=new ArrayList<>();
        for (int i=0;i<customerList.size();i++){
            if(customerList.get(i).getCustName().toLowerCase().contains(querey))
                filteredList.add(customerList.get(i));
        }
        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), filteredList, showCustomerLoc);
        itemsListView.setAdapter(customersListAdapter);
    }

    private void fillSpiners() {
        try {

            classifi_items = mHandler.getAllFilterMedical(0);
            accn_items = mHandler.getAllFilterMedical(1);
            spicilisty_items = mHandler.getAllFilterMedical(2);
            categ_items = mHandler.getAllFilterMedical(3);
            classifi_items.add(0, "");
            accn_items.add(0, "");
            spicilisty_items.add(0, "");
            categ_items.add(0, "");
        } catch (Exception e) {
            classifi_items.add("");
            accn_items.add("");
            spicilisty_items.add("");
            categ_items.add(0, "");
        }
        final ArrayAdapter<String> adapter_spici = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, classifi_items);
        spiciliSp.setAdapter(adapter_spici);

        final ArrayAdapter<String> accSpAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, accn_items);
        accSp.setAdapter(accSpAdapter);

        final ArrayAdapter<String> spicilistyAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, spicilisty_items);
        classificatSp.setAdapter(spicilistyAdapter);

        final ArrayAdapter<String> categAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, categ_items);
        categSp.setAdapter(categAdapter);
        //kind item
        categSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterAllSp();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spiciliSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterAllSp();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        accSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterAllSp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        classificatSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterAllSp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void filterAllSp() {
        customerList = mHandler.getAllCustomersFilters(spiciliSp.getSelectedItem().toString(), accSp.getSelectedItem().toString(), classificatSp.getSelectedItem().toString(), categSp.getSelectedItem().toString());

        customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
        itemsListView.setAdapter(customersListAdapter);
    }


    public void setListener(CustomerListShow_interface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    public void openSmallScanerTextView() {

        new IntentIntegrator(getActivity()).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    public void storeInDatabase() {

        mHandler.deleteAllCustomers();

//        for (int i = 0; i < customerList.size(); i++) {
        mHandler.addCustomer(customerList);
//        }
    }

    private void startVoiceInput(int flag) {
        Log.e("startVoiceInput", "" + flag);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            if (flag == 1) {

                Log.e("startVoiceInput2", "" + flag);
                getActivity().startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            }

        } catch (ActivityNotFoundException a) {
            String appPackageName = "com.dr7.salesmanmanager";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }
    }

    void initialize(View view) {
        update = (Button) view.findViewById(R.id.update);
        customerNameTextView = (EditText) view.findViewById(R.id.customerNameTextView);
        itemsListView = (ListView) view.findViewById(R.id.customersList);
        settings = mHandler.getAllSettings();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settings.size() != 0) {
                    ipAddress = settings.get(0).getIpAddress();
                    ipWithPort = settings.get(0).getIpPort();
                    SalesManLogin = mHandler.getAllUserNo();
                    CONO = mHandler.getAllSettings().get(0).getCoNo();
                    Log.e("SalesManLogin", "" + SalesManLogin);
                    // URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
                    if (isInternetAccessed()) {
                        try {
                            if (typaImport == 0)//mysql
                            {
                                new JSONTask().execute(URL_TO_HIT);
                            } else {
                                if (typaImport == 1)//IIOs
                                {
                                    new JSONTaskDelphi().execute(URL_TO_HIT);
                                }
                            }
//


                        } catch (Exception e) {
                            Log.e("updateCustomer", "" + e.getMessage());
                        }

                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    }

    boolean isInternetAccessed() {

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

    private class JSONTaskDelphi extends AsyncTask<String, String, List<Customer>> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
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
                        http:
//localhost:8082/GetVanCUSTOMERS?CONO=295&STRNO=66
                        URL_TO_HIT = "http://" + ipAddress.trim() + ":" + ipWithPort.trim() + "/Falcons/VAN.dll/GetVanCUSTOMERS?STRNO=" + SalesManLogin + "&CONO=" + CONO;

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
                        try {
                            Customer.setC_THECATEG(finalObject.getString("C_THECATEG"));
                            Customer.seteMail(finalObject.getString("EMail"));
                            Customer.setFax(finalObject.getString("Fax"));
                            Customer.setZipCode(finalObject.getString("ZipCode"));

                        } catch (Exception e) {
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
            if (result != null) {
                if (result.size() != 0) {


                    storeInDatabase();
                    if (mHandler.getAllSettings().size() != 0) {

                        if (mHandler.getAllSettings().get(0).getSalesManCustomers() == 1) {
                            customerList = mHandler.getCustomersBySalesMan(Login.salesMan);
                        }

                    }
                    customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
                    itemsListView.setAdapter(customersListAdapter);

                    Toast.makeText(getActivity(), "Customers list is ready" + customerList.size(), Toast.LENGTH_SHORT).show();
                } else {

                }
            } else {
                Toast.makeText(getActivity(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
//            Toast.makeText(getActivity(), "Moment...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<Customer> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(URL_TO_HIT);
                connection = url.openConnection();


                reader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));
                StringBuilder buffer = new StringBuilder();
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


                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    break;
                }

                Log.e("Customer", "buffer.toString********" + buffer.toString());
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("CUSTOMERS");

                customerList.clear();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String rate_customer = "";
                    String HideVal = "";
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
                    try {
                        Customer.setC_THECATEG(finalObject.getString("C_THECATEG"));
                        Customer.seteMail(finalObject.getString("EMail"));
                        Customer.setFax(finalObject.getString("Fax"));
                        Customer.setZipCode(finalObject.getString("ZipCode"));

                    } catch (Exception e) {
                        Customer.setC_THECATEG("");
                        Customer.seteMail("");
                        Customer.setFax("");
                        Customer.setZipCode("");
                    }
                    //*******************************

                    customerList.add(Customer);
                }
                Log.e("customerListRefresh", "" + customerList.size());

            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Customer", "********ex2");
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3");
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
            // Now the data is ready in the CustomerList ;
            return customerList;
        }


        @Override
        protected void onPostExecute(final List<Customer> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
                if (result.size() != 0) {


                    storeInDatabase();
                    if (mHandler.getAllSettings().size() != 0) {

                        if (mHandler.getAllSettings().get(0).getSalesManCustomers() == 1) {
                            customerList = mHandler.getCustomersBySalesMan(Login.salesMan);
                        }

                    }
                    customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList, showCustomerLoc);
                    itemsListView.setAdapter(customersListAdapter);

                    Toast.makeText(getActivity(), "Customers list is ready" + customerList.size(), Toast.LENGTH_SHORT).show();
                } else {

                }
            } else {
                Toast.makeText(getActivity(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean IsInPlan(String id) {


        boolean f = false;

        for (int i = 0; i < MainActivity.DB_salesManPlanList.size(); i++)
            if (MainActivity.DB_salesManPlanList.get(i).getCustNumber().equals(id.trim())) {
                f = true;
                break;

            }


        return f;
    }
}
