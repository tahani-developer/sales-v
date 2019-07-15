package com.dr7.salesmanmanager.Reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.dr7.salesmanmanager.R;


public class Reports extends AppCompatActivity {


    Button customer_log_report,transactions_report,return_report;


    Button stock_request_report,cash_reoprt;
    Button Inventory_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

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
                    Intent intent6 = new Intent(Reports.this, CashReport.class);
                    startActivity(intent6);
                    break ;
            }

        }

    };
}

