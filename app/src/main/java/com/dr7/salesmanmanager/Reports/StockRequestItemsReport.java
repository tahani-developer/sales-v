package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockRequestItemsReport extends AppCompatActivity {

    List<Item> items;
    EditText from_date, to_date;
    Button preview;
    TableLayout TableItemsReport;
    Calendar myCalendar;
    TextView totalQtyTextView;
    double sumQty = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_items_report);

        items = new ArrayList<Item>();
        DatabaseHandler obj = new DatabaseHandler(StockRequestItemsReport.this);
        items = obj.getStockRequestItems();

        TableItemsReport = (TableLayout) findViewById(R.id.TableItemsBalanceReport);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        preview = (Button) findViewById(R.id.preview);
        totalQtyTextView = (TextView) findViewById(R.id.total);

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
                new DatePickerDialog(StockRequestItemsReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(StockRequestItemsReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {
                    sumQty = 0;
                    for (int n = 0; n < items.size(); n++) {
                        if (filters(n)) {
                            TableRow row = new TableRow(StockRequestItemsReport.this);
                            row.setPadding(5, 10, 5, 10);

                            if (n % 2 == 0)
                                row.setBackgroundColor(ContextCompat.getColor(StockRequestItemsReport.this, R.color.layer4));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(StockRequestItemsReport.this, R.color.layer5));

                            for (int i = 0; i < 3; i++) {

                                String[] record = {
                                        items.get(n).getItemNo() + "",
                                        items.get(n).getItemName() + "",
                                        items.get(n).getQty() + ""};

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                TextView textView = new TextView(StockRequestItemsReport.this);
                                textView.setText(record[i]);
                                textView.setTextColor(ContextCompat.getColor(StockRequestItemsReport.this, R.color.colorPrimary));
                                textView.setGravity(Gravity.CENTER);

                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                textView.setLayoutParams(lp2);

                                row.addView(textView);
                            }
                            sumQty = sumQty + items.get(n).getQty();
                            TableItemsReport.addView(row);

                        }
                    }

                    totalQtyTextView.setText(sumQty + "");
                } else
                    Toast.makeText(StockRequestItemsReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
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
        int childCount = TableItemsReport.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            TableItemsReport.removeViews(1, childCount - 1);
            totalQtyTextView.setText("0.000");
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

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    public boolean filters(int n) {

        String fromDate = from_date.getText().toString().trim();
        String toDate = to_date.getText().toString();

        String date = items.get(n).getDate();
        try {
            if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                    (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
