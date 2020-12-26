package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Customer;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;
import static com.dr7.salesmanmanager.CustomerListShow.Customer_Name;
import static com.dr7.salesmanmanager.ImportJason.listCustomerInfo;


public class AccountStatment extends AppCompatActivity {

    List<Customer> customerInfoList=new ArrayList<>();
    ArrayList<Account__Statment_Model> listAccountBalance;
    RecyclerView recyclerView_report;
    LinearLayoutManager layoutManager;
    public  static TextView getAccountList_text;
    Button preview_button_account;
     Spinner customerSpinner;
     String customerId="";
     TextView name;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statment);

        ImportJason importJason =new ImportJason(AccountStatment.this);
        importJason.getCustomerInfo();

        initialView();
//        Log.e("customername",""+customername.size());

//        preview_button_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!customerId.equals(""))
//                {
//                    ImportData importData= new ImportData(AccountStatment.this);
//                    importData.getCustomerAccountStatment(customerId);
//                }
////
//            }
//        });
//        if(customername.size()!=0)
//        {
//            Log.e("customername",""+customername.size());
//            fillCustomerSpenner();
//        }


        getAccountList_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.e("customername",""+s.toString()+"\t"+Customer.size());
//                if(s.toString().equals("1"))
//                {
//                    if(customername.size()!=0)
//                    {
//                        Log.e("customername",""+customername.size());
//                        fillCustomerSpenner();
//                    }
//                }
                Log.e("customername",""+s.toString());
                if(s.toString().equals("2"))
                {
                    if(listCustomerInfo.size()!=0)
                {
                    fillAdapter();
                }
                }
            }
        });

//        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                customerId= listCustomer.get(position).getCustomerNumber();
//                Log.e("onItemSelected",""+customerId);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    @SuppressLint("WrongConstant")
    private void initialView() {
        recyclerView_report=findViewById(R.id.recyclerView_report);
        getAccountList_text=findViewById(R.id.getAccountList_text);
        preview_button_account=findViewById(R.id.preview_button_account);
        name=findViewById(R.id.customerName);
        name.setText(Customer_Name);
        customerSpinner = (Spinner) findViewById(R.id.cat);
        listAccountBalance=new ArrayList<>();
        layoutManager = new LinearLayoutManager(AccountStatment.this);
        layoutManager.setOrientation(VERTICAL);
        recyclerView_report.setLayoutManager(layoutManager);
    }

//    private void fillCustomerSpenner() {
//        final ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customername);
//        customerSpinner.setAdapter(ad);
//    }

    private void fillAdapter() {


//        Account__Statment_Model accBalance=new Account__Statment_Model();
//        accBalance.setVoucherNo("1002");
//        accBalance.setTranseNmae("sale");
//        accBalance.setDate_voucher("17/12");
//        accBalance.setDebit(50);
//        accBalance.setCredit(0);
//        accBalance.setBalance(0);
//        listAccountBalance.add(accBalance);
//
//        accBalance=new Account__Statment_Model();
//        accBalance.setVoucherNo("1003");
//        accBalance.setTranseNmae("sale");
//        accBalance.setDate_voucher("17/12");
//        accBalance.setDebit(100);
//        accBalance.setCredit(0);
//        accBalance.setBalance(0);
//        listAccountBalance.add(accBalance);
//
//        accBalance=new Account__Statment_Model();
//        accBalance.setVoucherNo("1003");
//        accBalance.setTranseNmae("payment");
//        accBalance.setDate_voucher("17/12");
//        accBalance.setDebit(0);
//        accBalance.setCredit(100);
//        accBalance.setBalance(0);
//        listAccountBalance.add(accBalance);
//        accBalance=new Account__Statment_Model();
//        accBalance.setVoucherNo("1003");
//        accBalance.setTranseNmae("payment");
//        accBalance.setDate_voucher("17/12");
//        accBalance.setDebit(0);
//        accBalance.setCredit(50);
//        accBalance.setBalance(0);
//        listAccountBalance.add(accBalance);
        AccountStatmentAdapter adapter = new AccountStatmentAdapter(listCustomerInfo, AccountStatment.this);
        recyclerView_report.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(AccountStatment.this,MainActivity.class);
        startActivity(i);
    }
}
