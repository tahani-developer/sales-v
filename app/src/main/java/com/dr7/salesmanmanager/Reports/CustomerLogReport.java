package com.dr7.salesmanmanager.Reports;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.Modles.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CustomerLogReport extends AppCompatActivity {
    private static final String TAG = "CustomerLogReport";

    List<Transaction> transactionList ;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_log_report);

        transactionList = new ArrayList<Transaction>();
        DatabaseHandler obj = new DatabaseHandler(CustomerLogReport.this);
        transactionList = obj.getAlltransactions();

        TableLayout TableCustomerLogReport = (TableLayout) findViewById(R.id.TableCustomerLogReport);

        for (int n = 0; n <transactionList.size() ; n++) {
            TableRow row = new TableRow(this);
            row.setPadding(5 , 10 , 5 , 10);

            if (n % 2 == 0)
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer4));
            else
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer5));

            for (int i = 0; i < 8; i++) {

                String [] record = {transactionList.get(n).getSalesManId()+"" ,
                        transactionList.get(n).getCusCode()+"",
                        transactionList.get(n).getCusName(),
                        transactionList.get(n).getCheckInDate(),
                        transactionList.get(n).getCheckInTime(),
                        transactionList.get(n).getCheckOutDate(),
                        transactionList.get(n).getCheckOutTime(),
                        transactionList.get(n).getStatus()+""};

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView textView = new TextView(this);
                textView.setText(record[i]);
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                textView.setGravity(Gravity.CENTER);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp2);

                row.addView(textView);
            }
            TableCustomerLogReport.addView(row);
        }



        int k = transactionList.get(1).getCusCode() ;
        Log.i(TAG, "***************************************" + k);
       // Toast.makeText(CustomerLogReport.this, transactionList.get(1).cusCode, Toast.LENGTH_LONG).show();

    }

}
