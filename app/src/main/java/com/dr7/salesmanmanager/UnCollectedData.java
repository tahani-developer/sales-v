package com.dr7.salesmanmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
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
    int[] listImageIcone=new int[]{R.drawable.pdf_,R.drawable.excel_e};

    String type="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_collected_data);
        type = getIntent().getStringExtra("type");
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
        inflateBoomMenu();

    }
    private void inflateBoomMenu() {
        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
//        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();


        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])

                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    convertToPdf();

                                    break;
                                case 1:
                                    convertToExcel();
                                    break;


                            }
                        }
                    }));
//            bmb.addBuilder(builder);


        }
    }
    private void convertToPdf() {
        PdfConverter pdf =new PdfConverter(UnCollectedData.this);
       pdf.exportListToPdf(paymentArrayList,"UncollectedChequesReport","",9);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if(type.equals("1"))
        {
            Intent i=new Intent(UnCollectedData.this,MainActivity.class);
            startActivity(i);
        }
        else {
            if(type.equals("2"))
            {
                Intent in=new Intent(UnCollectedData.this,Activities.class);
                startActivity(in);
            }

        }

    }
    private void convertToExcel() {

        ExportToExcel exportToExcel=new ExportToExcel();
      exportToExcel.createExcelFile(UnCollectedData.this,"UncollectedChequesReport.xls",10,paymentArrayList);




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
