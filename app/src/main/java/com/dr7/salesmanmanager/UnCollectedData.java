package com.dr7.salesmanmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Reports.CustomerLogReport;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.dr7.salesmanmanager.ImportJason.paymentChequesList;
import static com.dr7.salesmanmanager.ImportJason.unCollectlList;

public class UnCollectedData extends AppCompatActivity {

    public TextView recivedAmount_text, paidAmountText;
    public static TextView resultData;
    ArrayList<Payment> paymentArrayList;
    TableLayout tableCheckData;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_collected_data);
        initialView();
        ImportJason importJason = new ImportJason(UnCollectedData.this);
        importJason.getUnCollectedCheques();
        importJason.getAllcheques();
    }

    private void initialView() {
        paymentArrayList = new ArrayList<>();

        tableCheckData = (TableLayout) findViewById(R.id.TableCheckData);
        recivedAmount_text = findViewById(R.id.recivedAmount_text);
        paidAmountText = findViewById(R.id.paidAmountText);
        resultData = findViewById(R.id.result);
        resultData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (s.toString().equals("yes")) {
                        recivedAmount_text.setText(unCollectlList.get(0).getRECVD().toString());
                        paidAmountText.setText(unCollectlList.get(0).getPAIDAMT().toString());
                    }
                    if (s.toString().equals("payment")) {

                       paymentArrayList= paymentChequesList;
                        fillTable();
                    }
                }

            }
        });
       // getPayment();

    }

    private void getPayment() {
        Payment data = new Payment();
        data.setBank("Arab Bank");
        data.setDueDate("13/02/2021");
        data.setCheckNumber(20210210);
        data.setAmount(2500);

        paymentArrayList.add(data);
        data.setBank("Arab Bank");
        data.setDueDate("13/02/2021");
        data.setCheckNumber(20210210);
        data.setAmount(25200);
        paymentArrayList.add(data);
        paymentArrayList.add(data);
        fillTable();

    }

    private void fillTable() {
        TableRow row = null;

        for (int n = 0; n < paymentArrayList.size(); n++) {
            row = new TableRow(this);
            row.setPadding(5, 10, 5, 10);

            if (n % 2 == 0)
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer3));
            else
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.layer7));

            for (int i = 0; i < 4; i++) {

                String[] record = {paymentArrayList.get(n).getBank(), (paymentArrayList.get(n).getCheckNumber() + ""),
                        paymentArrayList.get(n).getDueDate(), (paymentArrayList.get(n).getAmount() + "")};

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView textView = new TextView(this);
                textView.setText(record[i]);
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorblue_dark));
                textView.setGravity(Gravity.CENTER);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp2);

                row.addView(textView);
            }
            tableCheckData.addView(row);
        }
    }
}
