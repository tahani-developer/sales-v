package com.dr7.salesmanmanager.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.BluetoothConnectMenu;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.PrintPic;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.bMITP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.apache.http.impl.cookie.DateUtils.formatDate;

public class AccountReport extends AppCompatActivity implements View.OnClickListener {

    Calendar myCalendar;
   public static  EditText from_date;
  public static   EditText to_date;
    DatabaseHandler mDbHandler;
    Button preview_report,printReport;
   public static  List<Account_Report> acount_report_list;
    Date currentTimeAndDate;
    SimpleDateFormat df;
    String today;
    TableLayout table_Account_Report;
    TextView account_balance;
    CompanyInfo companyInfo;
    private DecimalFormat decimalFormat;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    public static double counter,returnCash=0,returnCridet=0;
    volatile boolean stopWorker;
    DatabaseHandler obj;
    private ImageView pic;
    Bitmap testB;
    PrintPic printPic;
    boolean isFinishPrint=false;
    byte[] printIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_report);
        mDbHandler = new DatabaseHandler(AccountReport.this);
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        today = df.format(currentTimeAndDate);
        myCalendar = Calendar.getInstance();
        table_Account_Report = (TableLayout) findViewById(R.id.tableAccountReport);

        companyInfo = new CompanyInfo();

        acount_report_list = new ArrayList<Account_Report>();
        acount_report_list = mDbHandler.getِAccountReport();
        from_date = (EditText) findViewById(R.id.fromDate_accountReport);
        to_date = (EditText) findViewById(R.id.toDate_accountReport);
        account_balance = (TextView) findViewById(R.id.account_balance);
        from_date.setText(today);
        to_date.setText(today);
        preview_report = (Button) findViewById(R.id.preview_account_report);
        printReport = (Button) findViewById(R.id.print_account_report);
        printReport.setOnClickListener(this);
        preview_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {
//                        totalSold = 0 ; totalBonus = 0 ; totalSales = 0 ;
//                        for (int n = 0; n < acount_report_list.size(); n++) {
                    for (int n = 0; n < 3; n++) {
//                            if (filters(n)) {
                        TableRow row = new TableRow(AccountReport.this);
                        row.setPadding(5, 10, 5, 10);

                        if (n % 2 == 0)
                            row.setBackgroundColor(ContextCompat.getColor(AccountReport.this, R.color.layer2));
                        else
                            row.setBackgroundColor(ContextCompat.getColor(AccountReport.this, R.color.layer3));

                        float calTotalSales = 0;
                        for (int i = 0; i < 5; i++) {

                            String[] record = {
//                                            items.get(n).getItemNo() + "",
//                                            items.get(n).getItemName() + "",
//                                            items.get(n).getQty() + "",
//                                            items.get(n).getBonus() + "",
                                    "26/11/2019", "sales", "500", "0", "650"};

//                                    calTotalSales = (items.get(n).getQty() * items.get(n).getPrice()) - items.get(n).getDisc();
//                                    record[4] = calTotalSales + "";
//                                    record[4] = calTotalSales + "";

                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);

                            TextView textView = new TextView(AccountReport.this);
                            textView.setText(record[i]);
                            textView.setTextColor(ContextCompat.getColor(AccountReport.this, R.color.colorPrimary));
                            textView.setGravity(Gravity.CENTER);

                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            textView.setLayoutParams(lp2);

                            row.addView(textView);
                        }

//                                totalSold = totalSold + items.get(n).getQty();
//                                totalBonus = totalBonus + items.get(n).getBonus();
//                                totalSales = totalSales + calTotalSales ;

                        table_Account_Report.addView(row);
//                            }// end filters
                    }

//                        texttotalSold.setText(totalSold+"");
//                        textTotalBonus.setText(totalBonus+"");
//                        texttotalSales.setText(convertToEnglish(decimalFormat.format(totalSales)));
                } else {
                    Toast.makeText(AccountReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
                }
            }
        });


            preview_report.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        preview_report.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
                    } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        preview_report.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
                    }
                    return false;
                }
            });

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
                    new DatePickerDialog(AccountReport.this, openDatePickerDialog(0), myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            to_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(AccountReport.this, openDatePickerDialog(1), myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

        }


        public DatePickerDialog.OnDateSetListener openDatePickerDialog (final int flag){
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
        String date = acount_report_list.get(n).getDate() ;

        try {
                if ( (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate)) ) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {

                    return true;}
            else {
                if (   (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate)) ) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))) {

                    return true;}
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void clear() {
        int childCount = table_Account_Report.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            table_Account_Report.removeViews(1, childCount - 1);
            account_balance.setText("0.00");

        }
    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    void findBT() {
        try {
            /*  very important **********************************************************/
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    mmDevice = device;
//
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void closeBT() throws IOException {
        try {
            // pic.setImageBitmap(null);
            stopWorker = true;
            mmInputStream.close();
            mmOutputStream.close();
            mmSocket.close();

//            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.print_account_report: {
                try {
                    if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {

                        try {
                            int printer = mDbHandler.getPrinterSetting();
                            companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                            if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                                if (printer != -1) {
                                    switch (printer) {
                                        case 0:

                                            Intent i = new Intent(AccountReport.this, BluetoothConnectMenu.class);
                                            i.putExtra("printKey", "5");
                                            startActivity(i);

//                                                             lk30.setChecked(true);
                                            break;
                                        case 1:

                                            Intent printer1 = new Intent(AccountReport.this, bMITP.class);
                                            printer1.putExtra("printKey", "5");
                                            startActivity(printer1);
//                                                             lk31.setChecked(true);
                                            break;
                                        case 2:
//                                                             lk32.setChecked(true);
                                            Intent O1 = new Intent(AccountReport.this, bMITP.class);
                                            O1.putExtra("printKey", "5");
                                            startActivity(O1);

                                            break;
                                        case 3:

//                                                try {
//                                                    findBT();
//                                                    openBT(3);
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                             qs.setChecked(true);
                                            break;
                                        case 4:
//                                                printTally();
                                            break;
                                        case 5:// printer.setChecked(true)
//                                                convertLayoutToImage();
                                            Intent printer5 = new Intent(AccountReport.this, bMITP.class);
                                            printer5.putExtra("printKey", "5");
                                            startActivity(printer5);

                                            break;


                                    }
                                } else {
                                    Toast.makeText(AccountReport.this, "please chose printer setting", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AccountReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(AccountReport.this, "Please set Printer Setting", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            Toast.makeText(AccountReport.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AccountReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();

                        }
                    } else {
                        // hiddenDialog();
                    }
                } catch (Exception e) {
                    Toast.makeText(AccountReport.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                }

            }
            break;
        }

    }
}


