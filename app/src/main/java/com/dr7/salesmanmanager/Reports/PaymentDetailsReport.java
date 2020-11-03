package com.dr7.salesmanmanager.Reports;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class PaymentDetailsReport extends AppCompatActivity {

    List<Payment> payments;
    private EditText from_date, to_date;
    private TableLayout TablePaymentsDetailsReport;
    private Spinner paymentKindSpinner;
    private Button preview;
    Calendar myCalendar;
    int payMethod;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_details_report);
        LinearLayout linearMain=findViewById(R.id.linearMain);
        try{
            if(languagelocalApp.equals("ar"))
            {
                linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        payments = new ArrayList<Payment>();
        DatabaseHandler obj = new DatabaseHandler(PaymentDetailsReport.this);
        payments = obj.getAllPayments();

        from_date = (EditText) findViewById(R.id.from_date_r);
        to_date = (EditText) findViewById(R.id.to_date_r);
        paymentKindSpinner = (Spinner) findViewById(R.id.paymentTypeSpinner_r);
        preview = (Button) findViewById(R.id.preview_but);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        from_date.setText(today);
        to_date.setText(today);

        myCalendar = Calendar.getInstance();

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PaymentDetailsReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PaymentDetailsReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayList<String> kinds = new ArrayList<>();
        kinds.add("Cash");
        kinds.add("Cheque");
        kinds.add("All");

        ArrayAdapter<String> paymentKind = new ArrayAdapter<String>(PaymentDetailsReport.this, R.layout.spinner_style, kinds);
        paymentKindSpinner.setAdapter(paymentKind);

        paymentKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    payMethod = 1;
                } else if (i == 1) {
                    payMethod = 0;

                } else payMethod = 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TablePaymentsDetailsReport = (TableLayout) findViewById(R.id.TablePaymentsDetailsReport);

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("paymentdetail",""+payments);

                clear();
                for (int n = 0; n < payments.size(); n++) {
                    if (filters(n)) {
                        TableRow row = new TableRow(PaymentDetailsReport.this);
                        row.setPadding(5, 10, 5, 10);

                        if (n % 2 == 0)
                            row.setBackgroundColor(ContextCompat.getColor(PaymentDetailsReport.this, R.color.layer4));
                        else
                            row.setBackgroundColor(ContextCompat.getColor(PaymentDetailsReport.this, R.color.layer5));

                        for (int i = 0; i < 7; i++) {

                            String[] record = {payments.get(n).getVoucherNumber() + "",
                                    payments.get(n).getPayDate(),
                                    payments.get(n).getCustName() + "",
                                    payments.get(n).getAmount() + "",
                                    payments.get(n).getRemark(),
                                    payments.get(n).getSaleManNumber() + "",
                                    payments.get(n).getPayMethod() + ""};

                            switch (record[6]) {
                                case "0":
                                    record[6] = "Cheque";
                                    break;
                                case "1":

                                    record[6] = "Cash";
                                    break;
                            }

                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);

                            TextView textView = new TextView(PaymentDetailsReport.this);
                            textView.setText(record[i]);
                            textView.setTextColor(ContextCompat.getColor(PaymentDetailsReport.this, R.color.colorPrimary));
                            textView.setGravity(Gravity.CENTER);

                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            textView.setLayoutParams(lp2);

                            row.addView(textView);
                        }
                        TablePaymentsDetailsReport.addView(row);
                    }

                }
            }
        });


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

    public void clear() {
        int childCount = TablePaymentsDetailsReport.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            TablePaymentsDetailsReport.removeViews(1, childCount - 1);
        }
    }

    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            from_date.setText(sdf.format(myCalendar.getTime()));
        else
            to_date.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean filters(int n) {
        String fromDate = from_date.getText().toString().trim();
        String toDate = to_date.getText().toString();

        String date = payments.get(n).getPayDate();
        int pMethod = payments.get(n).getPayMethod();

        try {
            if (payMethod != 2) {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))) &&
                        pMethod == payMethod)
                    return true;
            } else {
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))))
                    return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

}
