package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CashReport  extends AppCompatActivity {
    List<Payment> payments;
    private EditText date;
    private Button preview;
    Calendar myCalendar;
    TextView cash_sal, credit_sale, total_sale;
    TextView cash_paymenttext, creditPaymenttext, nettext,total_cashtext;
    List<Voucher> voucher;
    double cash = 0, credit = 0, total = 0;
    double cashPayment=0,creditPayment=0,net=0,total_cash;
    int paymethod=0;

    private DecimalFormat decimalFormat;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report);
        //************************* initial *************************************
        decimalFormat = new DecimalFormat("##.000");
        payments = new ArrayList<Payment>();
        DatabaseHandler obj = new DatabaseHandler(CashReport.this);
        payments = obj.getAllPayments();
        voucher = obj.getAllVouchers();
        date = (EditText) findViewById(R.id.date_editReport_cash);
        preview = (Button) findViewById(R.id.preview_cash_report);
        cash_sal = (TextView) findViewById(R.id.text_cash_sales);
        credit_sale = (TextView) findViewById(R.id.text_credit_sales);
        total_sale = (TextView) findViewById(R.id.text_total_sales);
        cash_paymenttext = (TextView) findViewById(R.id.text_cash_PaymentReport);
        creditPaymenttext = (TextView) findViewById(R.id.text_cheque_paymentReport);
        nettext = (TextView) findViewById(R.id.text_net_paymentReport);
        total_cashtext=(TextView) findViewById(R.id.text_total_cash);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                if (!date.getText().toString().equals("")) {
                    cash = 0;
                    credit = 0;
                    total = 0;
                    for (int n = 0; n < voucher.size(); n++) {
                        if (filters(n)) {
                            switch (voucher.get(n).getPayMethod()) {
                                case 0:
                                    paymethod = 0;
                                    credit += voucher.get(n).getNetSales();
                                    break;
                                case 1:
                                    paymethod = 1;
                                    cash += voucher.get(n).getNetSales();
                                    break;
                            }
                        }
                    }

                    total = cash + credit;
                    cash_sal.setText(cash + "");
                    credit_sale.setText(credit + "");
                    total_sale.setText(convertToEnglish(decimalFormat.format(total)));
                    Log.e("cash", "" + cash + "\t credit= " + credit + "total=" + total);
                    //  clearPayment();
                    for (int i = 0; i < payments.size(); i++) {
                        Log.e("paym",""+payments.get(i).getAmount());
                        if (filters_payment(i)) {

                                switch ( payments.get(i).getPayMethod() ) {
                                    case 0:
                                    creditPayment+=payments.get(i).getAmount();
                                        break;
                                    case 1:
                                        cashPayment+=payments.get(i).getAmount();
                                        break;
                                }

                            }
                        }
                    net=cashPayment+creditPayment;
                    cash_paymenttext.setText(cashPayment+"");
                    creditPaymenttext.setText(creditPayment+"");
                    nettext.setText(convertToEnglish(decimalFormat.format(net)));
                    Log.e("cash_p", "" + cashPayment + "\t creditPaymenttext= " + creditPayment + "nettext=" + net);
                    total_cashtext.setText(convertToEnglish(decimalFormat.format((net+cash))));
                    Log.e("totalcash","="+net+cash);

                    }

                else
                    Toast.makeText(CashReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
            }
        });

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
                }
                return false;
            }
        });
        //******************************date*****************************************
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        date.setText(today);
        myCalendar = Calendar.getInstance();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CashReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void clear() {

//            int childCount = TableTransactionsReport.getChildCount();
//            // Remove all rows except the first one
//            if (childCount > 1) {
//                TableTransactionsReport.removeViews(1, childCount - 1);
//                textSubTotal.setText("0.000");
//                textTax.setText("0.000");
//                textNetSales.setText("0.000");

    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(flag);
            }

        };
        return date;
    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            date.setText(sdf.format(myCalendar.getTime()));

    }

    public boolean filters (int n){


        String date_text = date.getText().toString().trim();
        int pMethod = voucher.get(n).getPayMethod() ;
        String date = voucher.get(n).getVoucherDate() ;

        try {
                if ((formatDate(date).after(formatDate(date_text)) || formatDate(date).equals(formatDate(date_text))))
                    return true;
            }

        catch (ParseException e) {  e.printStackTrace(); }

        return false ;
    }
    public boolean filters_payment (int n){


        String date_text = date.getText().toString().trim();
        int pMethod = payments.get(n).getPayMethod() ;
        String date = payments.get(n).getPayDate() ;

        try {
            if ((formatDate(date).after(formatDate(date_text)) || formatDate(date).equals(formatDate(date_text))))
                return true;
        }

        catch (ParseException e) {  e.printStackTrace(); }

        return false ;
    }

    public Date formatDate (String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d ;
    }
}
