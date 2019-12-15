package com.dr7.salesmanmanager.Reports;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportJason;
import com.dr7.salesmanmanager.ImportJason;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.R;

import org.json.JSONException;


public class Reports extends AppCompatActivity {


    Button customer_log_report,transactions_report,return_report;


    Button stock_request_report,cash_reoprt;
    Button Inventory_report;
    DatabaseHandler MHandler;
    LinearLayout inventory_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        MHandler = new DatabaseHandler(Reports.this);

        customer_log_report = (Button) findViewById(R.id.customer_log_report);
        transactions_report = (Button) findViewById(R.id.transactions_report);
        return_report = (Button) findViewById(R.id.return_report);
        Inventory_report = (Button) findViewById(R.id.inventory_report);
        stock_request_report = (Button) findViewById(R.id.stock_request_report);
        cash_reoprt = (Button) findViewById(R.id.cash_report);
        customer_log_report.setOnClickListener(onClickListener);
        cash_reoprt.setOnClickListener(onClickListener);
        transactions_report.setOnClickListener(onClickListener);
        return_report.setOnClickListener(onClickListener);
        stock_request_report.setOnClickListener(onClickListener);
        Inventory_report.setOnClickListener(onClickListener);
        inventory_layout=(LinearLayout) findViewById(R.id.inventory_layout);
        if(MHandler.getAllSettings().get(0).getHide_qty()==1) {
            inventory_layout.setVisibility(View.GONE);
        }


    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.customer_log_report:
                    Intent intent1 = new Intent(Reports.this, CustomerLogReport.class);
                    startActivity(intent1);
                    break ;

                case R.id.transactions_report:
                    Intent intent2 = new Intent(Reports.this, TransactionsReport.class);
                    startActivity(intent2);
                    break ;

                case R.id.return_report:
                    Intent intent3 = new Intent(Reports.this, PaymentDetailsReport.class);
                    startActivity(intent3);
                    break ;

                case R.id.stock_request_report:
                    Intent intent4 = new Intent(Reports.this, StockRequestReport.class);
                    startActivity(intent4);
                    break ;
                case R.id.inventory_report:
                    Intent intent5 = new Intent(Reports.this, InventoryReport.class);
                    startActivity(intent5);
                    break ;
                case R.id.cash_report:
                    if(MHandler.getAllSettings().get(0).getLock_cashreport()==1) {
                        openPassowrdDialog();

                    }
                    else{
                        Intent intent6 = new Intent(Reports.this, CashReport.class);
                        startActivity(intent6);
                    }




                    break ;
            }

        }

    };

    private void openPassowrdDialog() {

        final Dialog dialog = new Dialog(Reports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.password_dialog);

        final EditText password = (EditText) dialog.findViewById(R.id.editText1);
        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);
        final CheckBox cb_show = (CheckBox) dialog.findViewById(R.id.checkBox_showpass);
//        EditText et1 = (EditText) this.findViewById(R.id.editText1);
        cb_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_show.isChecked()) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(129);
                }
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("301190")) {
                    dialog.dismiss();
                    Intent intent6 = new Intent(Reports.this, CashReport.class);
                    startActivity(intent6);


                    } else
                        Toast.makeText(Reports.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    public void onCheckBox(View v2)
    {



        CheckBox cb = (CheckBox)this.findViewById(R.id.checkBox_showpass);
        EditText et1=(EditText)this.findViewById(R.id.editText1);
        if(cb.isChecked())
        {
            et1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        else
        {
            et1.setInputType(129);
        }

    }
    }

