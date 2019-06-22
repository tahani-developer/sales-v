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
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemsReport extends AppCompatActivity {

    List<Item> items;
    RadioGroup voucherTypeRadioGroup;
    EditText from_date, to_date, item_number;
    Button preview;
    TextView texttotalSold , textTotalBonus , texttotalSales ;
    TableLayout TableItemsReport;
    Calendar myCalendar;
    int voucherType = 504;

    double totalSold = 0 , totalBonus = 0 , totalSales = 0 ;
    private DecimalFormat decimalFormat;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_report);
        decimalFormat = new DecimalFormat("##.00");

        items = new ArrayList<Item>();
        DatabaseHandler obj = new DatabaseHandler(ItemsReport.this);
        items = obj.getAllItems();

        TableItemsReport = (TableLayout) findViewById(R.id.TableItemsBalanceReport);
        voucherTypeRadioGroup = (RadioGroup) findViewById(R.id.transKindRadioGroup);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        item_number = (EditText) findViewById(R.id.item_number);
        preview = (Button) findViewById(R.id.preview);
        texttotalSold = (TextView) findViewById(R.id.totalSoldTextView) ;
        textTotalBonus = (TextView) findViewById(R.id.totalBonusTextView) ;
        texttotalSales = (TextView) findViewById(R.id.totalSalesTextView1) ;

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
                new DatePickerDialog(ItemsReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ItemsReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        voucherTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.salesRadioButton: voucherType = 504; break;
                    case R.id.retSalesRadioButton: voucherType = 506; break;
                    case R.id.orderRadioButton: voucherType = 508; break;
                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {
                    totalSold = 0 ; totalBonus = 0 ; totalSales = 0 ;
                    for (int n = 0; n < items.size(); n++) {
                        if (filters(n))
                        {
                            TableRow row = new TableRow(ItemsReport.this);
                            row.setPadding(5, 10, 5, 10);

                            if (n % 2 == 0)
                                row.setBackgroundColor(ContextCompat.getColor(ItemsReport.this, R.color.layer4));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(ItemsReport.this, R.color.layer5));

                            float calTotalSales = 0;
                            for (int i = 0; i < 5; i++) {

                                String[] record = {
                                        items.get(n).getItemNo() + "",
                                        items.get(n).getItemName() + "",
                                        items.get(n).getQty() + "",
                                        items.get(n).getBonus() + "",
                                        ""};

                                calTotalSales = (items.get(n).getQty() * items.get(n).getPrice()) - items.get(n).getDisc();
                                record[4] = calTotalSales + "";

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                TextView textView = new TextView(ItemsReport.this);
                                textView.setText(record[i]);
                                textView.setTextColor(ContextCompat.getColor(ItemsReport.this, R.color.colorPrimary));
                                textView.setGravity(Gravity.CENTER);

                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                textView.setLayoutParams(lp2);

                                row.addView(textView);
                            }

                            totalSold = totalSold + items.get(n).getQty();
                            totalBonus = totalBonus + items.get(n).getBonus();
                            totalSales = totalSales + calTotalSales ;

                            TableItemsReport.addView(row);
                        }
                    }

                    texttotalSold.setText(totalSold+"");
                    textTotalBonus.setText(totalBonus+"");
                    texttotalSales.setText(convertToEnglish(decimalFormat.format(totalSales)));
                } else
                    Toast.makeText(ItemsReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
            }
        });


        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
                }
                return false;
            }
        });

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
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
            texttotalSold.setText("0.000");
            textTotalBonus.setText("0.000");
            texttotalSales.setText("0.000");
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

        String textItemNumber = item_number.getText().toString();

        String fromDate = from_date.getText().toString().trim();
        String toDate = to_date.getText().toString();

        String itemNumber = items.get(n).getItemNo();
        String date = items.get(n).getDate() ;
        int vType = items.get(n).getVoucherType();

        try {
            if (!textItemNumber.equals("")) {
                if ((itemNumber.equals(textItemNumber)) && vType == voucherType &&
                    (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate)) ) &&
                            (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {

                    return true;}
            } else {
                if (vType == voucherType  &&
                        (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate)) ) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {

                    return true;}
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
