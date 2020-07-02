package com.dr7.salesmanmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CustomerListShow extends DialogFragment {
    private final String URL_TO_HIT = "http://10.0.0.115/VANSALES_WEB_SERVICE/index.php";

    public ListView itemsListView;
    public List<Customer> customerList;
    public List<Customer> emptyCustomerList;
    private Button update;
    private EditText customerNameTextView;
    public static String Customer_Name = "No Customer Selected !", Customer_Account = "", PriceListId = "";
    public static int CashCredit , paymentTerm = 1;
    public static double CreditLimit=0;
    public  static  String latitude="",longtude="";
    public static double Max_Discount_value=0;
    public static int CustHideValu=0;
    CustomersListAdapter customersListAdapter;
    DatabaseHandler mHandler;
    private ProgressDialog progressDialog;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(getResources().getString(R.string.app_select_customer));
        final View view = inflater.inflate(R.layout.customers_list, container, false);
        initialize(view);

        customerList = new ArrayList<>();
        emptyCustomerList=new ArrayList<>();

        mHandler = new DatabaseHandler(getActivity());
        if(mHandler.getAllSettings().size() != 0) {

            if (mHandler.getAllSettings().get(0).getSalesManCustomers() == 1)
            {
                customerList = mHandler.getCustomersBySalesMan(Login.salesMan);
                Log.e("getSalesManCustomers",""+customerList.size());
            }

            else
            {customerList = mHandler.getAllCustomers();
                Log.e("getAllCustomers",""+customerList.size());

            }


            if (mHandler.getAllSettings().get(0).getShowCustomerList() == 1) {
                customerList.get(29).getPayMethod();
                Log.e("customerListSho",""+customerList.get(29).getPayMethod());
                customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList);
                itemsListView.setAdapter(customersListAdapter);

            } else {
                customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), emptyCustomerList);
                itemsListView.setAdapter(customersListAdapter);
            }
        }
        else {
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

        customerNameTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                {
                    customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList);
                    itemsListView.setAdapter(customersListAdapter);
                    // Call back the Adapter with current character to Filter
                    customersListAdapter.getFilter().filter(s.toString());


                }
                else {
                    if (mHandler.getAllSettings().size() != 0) {
                        if (mHandler.getAllSettings().get(0).getShowCustomerList() == 1) {
//
                            customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList);
                            itemsListView.setAdapter(customersListAdapter);
                            //customersListAdapter.notifyDataSetChanged();
                        } else {
                            customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), emptyCustomerList);
                            itemsListView.setAdapter(customersListAdapter);
                            // customersListAdapter.notifyDataSetChanged();
                        }


                    }
                    else{

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }


    public void setListener(CustomerListShow_interface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }


    public void storeInDatabase() {

        mHandler.deleteAllCustomers();

        for (int i = 0; i < customerList.size(); i++) {
            mHandler.addCustomer(customerList.get(i));
        }
    }

    void initialize(View view) {
        update = (Button) view.findViewById(R.id.update);
        customerNameTextView = (EditText) view.findViewById(R.id.customerNameTextView);
        itemsListView = (ListView) view.findViewById(R.id.customersList);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetAccessed()) {
                    new JSONTask().execute(URL_TO_HIT);
                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                    Customer.setMax_discount(finalObject.getDouble("MAXDISC"));

                    customerList.add(Customer);
                }

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
            // Now the data is ready in the CustomerList ;
            return customerList;
        }


        @Override
        protected void onPostExecute(final List<Customer> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
                customersListAdapter = new CustomersListAdapter(CustomerListShow.this, getActivity(), customerList);
                itemsListView.setAdapter(customersListAdapter);
                storeInDatabase();
                Toast.makeText(getActivity(), "Customers list is ready" + customerList.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
