package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.ganesh.intermecarabic.Arabic864;

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
import java.util.UUID;

public class PrintVoucher extends AppCompatActivity {

    List<Voucher> vouchers;
    List<Item> items;
    TextView textSubTotal, textTax, textNetSales;
    EditText from_date, to_date;
    Button preview;
    TableLayout TableTransactionsReport;
    TableLayout TableItemInfo;
    Calendar myCalendar;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    String itemsString;
    String itemsString2 = "";
    DatabaseHandler obj;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_vouchers);

        vouchers = new ArrayList<Voucher>();
        items = new ArrayList<Item>();

        obj = new DatabaseHandler(PrintVoucher.this);
        vouchers = obj.getAllVouchers();
        items = obj.getAllItems();

        TableTransactionsReport = (TableLayout) findViewById(R.id.TableTransactionsReport);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        preview = (Button) findViewById(R.id.preview);

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
                new DatePickerDialog(PrintVoucher.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PrintVoucher.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear();
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {

                    for (int n = 0; n < vouchers.size(); n++) {
                        final Voucher vouch = vouchers.get(n);

                        if (filters(n)) {

                            final TableRow row = new TableRow(PrintVoucher.this);
                            row.setPadding(2, 10, 2, 10);

                            if (n % 2 == 0)
                                row.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer4));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer5));

                            for (int i = 0; i < 9; i++) {
                                String[] record = {vouchers.get(n).getCustName() + "",
                                        vouchers.get(n).getVoucherNumber() + "",
                                        vouchers.get(n).getVoucherDate() + "",
                                        vouchers.get(n).getPayMethod() + "",
                                        vouchers.get(n).getVoucherDiscount() + "",
                                        vouchers.get(n).getSubTotal() + "",
                                        vouchers.get(n).getTax() + "",
                                        vouchers.get(n).getNetSales() + ""};


                                switch (vouchers.get(n).getPayMethod()) {
                                    case 0:
                                        record[3] = getResources().getString(R.string.app_credit);
                                        break;
                                    case 1:
                                        record[3] = getResources().getString(R.string.cash);
                                        break;
                                }


                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                if (i != 8) {
                                    TextView textView = new TextView(PrintVoucher.this);
                                    textView.setText(record[i]);
                                    textView.setTextColor(ContextCompat.getColor(PrintVoucher.this, R.color.colorPrimary));
                                    textView.setGravity(Gravity.CENTER);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, 30, 1f);
                                    textView.setLayoutParams(lp2);


                                    row.addView(textView);

                                } else {
                                    TextView textView = new TextView(PrintVoucher.this);
                                    textView.setText(getResources().getString(R.string.print));
                                    textView.setTextSize(12);

                                    textView.setTextColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer5));
                                    textView.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.colorAccent));
                                    textView.setGravity(Gravity.CENTER);

                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView textView = (TextView) row.getChildAt(1);
//                                            voucherInfoDialog(Integer.parseInt(textView.getText().toString()));

                                            if (obj.getAllSettings().get(0).getPrintMethod() == 0) {
                                                try {
                                                    findBT(Integer.parseInt(textView.getText().toString()));
                                                    openBT(vouch);
                                                } catch (IOException ex) { }
                                            } else {
                                                hiddenDialog(vouch);
                                            }
                                        }
                                    });

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, 30, 0.7f);


                                    textView.setLayoutParams(lp2);
                                    row.addView(textView);
                                }
                            }
                            TableTransactionsReport.addView(row);
                        }

                    }

                } else
                    Toast.makeText(PrintVoucher.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
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

    public void clear() {
        int childCount = TableTransactionsReport.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            TableTransactionsReport.removeViews(1, childCount - 1);
        }
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


    private void updateLabel(int flag) {
        String myFormat = "yyy/MM/dd"; //In which you need put here
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

        String date = vouchers.get(n).getVoucherDate();

        try {
            Log.e("tag", "*****" + date + "***" + fromDate);
            if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                    (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))))
                return true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressLint("SetTextI18n")
    public void hiddenDialog(Voucher voucher) {
        final Dialog dialog = new Dialog(PrintVoucher.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.print);

        final Button okButton = dialog.findViewById(R.id.print1);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linear1);
        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);

        TextView companyName = dialog.findViewById(R.id.company);
        TextView phone = dialog.findViewById(R.id.phone);
        TextView taxNo = dialog.findViewById(R.id.tax_no);
        TextView date = dialog.findViewById(R.id.date);
        TextView vouch_no = dialog.findViewById(R.id.voucher_no);
        TextView vouchType = dialog.findViewById(R.id.voucher_type);
        TextView payMethod = dialog.findViewById(R.id.payMethod);
        TextView cust = dialog.findViewById(R.id.cust_);
        TextView remark = dialog.findViewById(R.id.remark_);
        TextView totalNoTax = dialog.findViewById(R.id.total_noTax);
        TextView discount = dialog.findViewById(R.id.discount);
        TextView tax = dialog.findViewById(R.id.tax);
        TextView netSale = dialog.findViewById(R.id.net_sales_);

        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

        companyName.setText(companyInfo.getCompanyName());
        phone.setText(phone.getText().toString() + companyInfo.getcompanyTel());
        taxNo.setText(taxNo.getText().toString() + companyInfo.getTaxNo());
        date.setText(date.getText().toString() + voucher.getVoucherDate());
        vouch_no.setText(vouch_no.getText().toString() + voucher.getVoucherNumber());
        remark.setText(remark.getText().toString() + voucher.getRemark());
        cust.setText(cust.getText().toString() + voucher.getCustName());
        totalNoTax.setText(totalNoTax.getText().toString() + voucher.getSubTotal());
        discount.setText(discount.getText().toString() + voucher.getVoucherDiscount());
        tax.setText(tax.getText().toString() + voucher.getTax());
        netSale.setText(netSale.getText().toString() + voucher.getNetSales());

        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
            case 504:
                voucherTyp = "فاتورة بيع";
                break;
            case 506:
                voucherTyp = "فاتورة مرتجعات";
                break;
            case 508:
                voucherTyp = "طلب جديد";
                break;
        }
        vouchType.setText(vouchType.getText().toString() + voucherTyp);
        payMethod.setText(payMethod.getText().toString() + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));

        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {

            final TableRow headerRow = new TableRow(PrintVoucher.this);

            TextView headerView7 = new TextView(PrintVoucher.this);
            headerView7.setGravity(Gravity.CENTER);
            headerView7.setText("المجموع");
            headerView7.setLayoutParams(lp2);
            headerView7.setTextSize(12);
            headerRow.addView(headerView7);

            TextView headerView6 = new TextView(PrintVoucher.this);
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("الخصم");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(PrintVoucher.this);
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("المجاني");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(PrintVoucher.this);
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("سعر الوحدة");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(PrintVoucher.this);
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("الوزن");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(PrintVoucher.this);
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            TextView headerView1 = new TextView(PrintVoucher.this);
            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        } else {
            final TableRow headerRow = new TableRow(PrintVoucher.this);
            TextView headerView1 = new TextView(PrintVoucher.this);

            TextView headerView6 = new TextView(PrintVoucher.this);
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("المجموع");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(PrintVoucher.this);
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("الخصم");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(PrintVoucher.this);
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("المجاني");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(PrintVoucher.this);
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("سعر الوحدة");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(PrintVoucher.this);
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        }

        for (int j = 0; j < items.size(); j++) {

            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(PrintVoucher.this);

                if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {

                    for (int i = 0; i <= 7; i++) {
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 10, 0, 0);
                        row.setLayoutParams(lp);

                        TextView textView = new TextView(PrintVoucher.this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(10);

                        switch (i) {
                            case 6:
                                textView.setText(items.get(j).getItemName());
                                textView.setLayoutParams(lp3);
                                break;

                            case 5:
                                textView.setText(items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                                break;

                            case 4:
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                break;

                            case 3:
                                textView.setText("" + items.get(j).getPrice());
                                textView.setLayoutParams(lp2);
                                break;

                            case 2:
                                textView.setText(""+items.get(j).getBonus());
                                textView.setLayoutParams(lp2);
                                break;

                            case 1:
                                textView.setText(""+items.get(j).getDisc());
                                textView.setLayoutParams(lp2);
                                break;

                            case 0:
                                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                                amount = convertToEnglish(amount);
                                textView.setText(amount);
                                textView.setLayoutParams(lp2);
                                break;
                        }
                        row.addView(textView);
                    }

                } else {
                    for (int i = 0; i <= 6; i++) {
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 10, 0, 0);
                        row.setLayoutParams(lp);

                        TextView textView = new TextView(PrintVoucher.this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(10);

                        switch (i) {
                            case 5:
                                textView.setText(items.get(j).getItemName());
                                textView.setLayoutParams(lp3);
                                break;

                            case 4:
                                textView.setText(items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                                break;

                            case 3:
                                textView.setText("" + items.get(j).getPrice());
                                textView.setLayoutParams(lp2);
                                break;

                            case 2:
                                textView.setText(""+items.get(j).getBonus());
                                textView.setLayoutParams(lp2);
                                break;

                            case 1:
                                textView.setText(""+items.get(j).getDisc());
                                textView.setLayoutParams(lp2);

                            case 0:
                                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                                amount = convertToEnglish(amount);
                                textView.setText(amount);
                                textView.setLayoutParams(lp2);
                                break;
                        }
                        row.addView(textView);
                    }
                }
                tabLayout.addView(row);
            }
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                linearLayout.setDrawingCacheEnabled(true);
                Bitmap bitmap = linearLayout.getDrawingCache();
                photoPrinter.printBitmap("invoice.jpg", bitmap);

            }
        });
        dialog.show();
    }

    void findBT(int voucherNo) {
        itemsString = "";
        for (int j = 0; j < items.size(); j++) {

            if (voucherNo == items.get(j).getVoucherNumber()) {
                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                amount = convertToEnglish(amount);

                String row = items.get(j).getItemName() + "                                             ";
                row = row.substring(0, 21) + items.get(j).getUnit() + row.substring(21, row.length());
                row = row.substring(0, 31) + items.get(j).getQty() + row.substring(31, row.length());
                row = row.substring(0, 41) + items.get(j).getPrice() + row.substring(41, row.length());
                row = row.substring(0, 52) + new DecimalFormat("#.##").format(Double.valueOf(amount));
                row = row.trim();
                itemsString = itemsString + "\n" + row;

                String row2 = items.get(j).getItemName() + "                                             ";
                row2 = row2.substring(0, 21) + items.get(j).getUnit() + row2.substring(21, row2.length());
                row2 = row2.substring(0, 31) + items.get(j).getPrice() + row2.substring(31, row2.length());
                row2 = row2.substring(0, 42) + new DecimalFormat("#.##").format(Double.valueOf(amount));
                row2 = row2.trim();
                itemsString2 = itemsString2 + "\n" + row2;
            }
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

                    // MP300 is the name of the bluetooth printer device07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ C4:73:1E:67:29:6C
                    /*07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ E8:99:C4:FF:B1:AC
                    07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ 0C:A6:94:35:11:27*/

                    /*Log.e("sex",device.getName());*/
//                    if (device.getName().equals("mobile printer")) { // PR3-30921446556
                    /*Log.e("sex1",device.getAddress());*/
                    mmDevice = device;
//                        break;
//                    }
                }
            }
//            myLabel.setText("Bluetooth Device Found");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tries to open a connection to the bluetooth printer device
    void openBT(Voucher voucher) throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");
            sendData2(voucher);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    void sendData(Voucher voucher) throws IOException {
        try {

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

            for (int i = 1; i <= numOfCopy; i++) {
                String voucherTyp = "";
                switch (voucher.getVoucherType()) {
                    case 504:
                        voucherTyp = "فاتورة بيع";
                        break;
                    case 506:
                        voucherTyp = "فاتورة مرتجعات";
                        break;
                    case 508:
                        voucherTyp = "طلب جديد";
                        break;
                }
                printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
                printCustom("هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", 1, 2);
                printCustom("----------------------------------------------" + "\n", 1, 2);
                printCustom("رقم الفاتورة : " + voucher.getVoucherNumber() + "          التاريخ: " + voucher.getVoucherDate() + "\n", 1, 2);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("اسم العميل   : " + voucher.getCustName() + "\n", 1, 2);
                printCustom("ملاحظة        : " + voucher.getRemark() + "\n", 1, 2);
                printCustom("نوع الفاتورة : " + voucherTyp + "\n", 1, 2);
                printCustom("طريقة الدفع  : " + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", 1, 2);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("----------------------------------------------" + "\n", 1, 2);

                if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                    printCustom(" السلعة              " + "العدد    " + "الوزن    " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom(itemsString + "\n", 0, 2);
                } else {
                    printCustom(" السلعة              " + "العدد   " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                    printCustom("----------------------------------------------" + "\n", 1, 2);

                    printCustom(itemsString2 + "\n", 0, 2);
                }
                printCustom("----------------------------------------------" + "\n", 1, 2);

                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("المجموع  : " + voucher.getSubTotal() + "\n", 1, 2);
                printCustom("الخصم    : " + voucher.getVoucherDiscount() + "\n", 1, 2);
                printCustom("الضريبة  : " + voucher.getTax() + "\n", 1, 2);
                printCustom("الصافي   : " + voucher.getNetSales() + "\n", 1, 2);
                printCustom("استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n", 1, 2);
                printCustom("اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n", 1, 2);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("المستلم : ________________ التوقيع : __________" + "\n", 1, 2);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("----------------------------------------------" + "\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);

                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);

            }
            closeBT();
            // tell the user data were sent
//                myLabel.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData2(Voucher voucher) throws IOException {
        try {

            double totalQty = 0;
            double totalPrice = 0;
            double totalDisc = 0;
            double totalNet = 0;
            double totalTax = 0;
            double totalTotal = 0;

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

            for (int i = 1; i <= numOfCopy; i++) {

                printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("الرقم الضريبي  : " + companyInfo.getTaxNo() + "\n", 1, 2);
                printCustom("----------------------------------------------" + "\n", 1, 2);
                printCustom("التاريخ        : " + voucher.getVoucherDate() + "\n", 1, 2);
                printCustom("رقم الفاتورة   : " + voucher.getVoucherNumber() + "\n", 1, 2);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("رقم العميل     : " + voucher.getCustNumber() + "\n", 1, 2);
                printCustom("اسم العميل     : " + voucher.getCustName() + "\n", 1, 2);
                printCustom("مندوب المبيعات : " + voucher.getSaleManNumber() + "\n", 1, 2);
                printCustom("----------------------------------------------" + "\n", 1, 2);
                mmOutputStream.write(PrinterCommands.FEED_LINE);

                for (int j = 0; j < items.size(); j++) {

                    String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                    String amountATax = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc()+ items.get(j).getTaxValue());
                    amount = convertToEnglish(amount);
                    amountATax = convertToEnglish(amountATax);

                    printCustom("" + (j+1) +"(" + "\n", 1, 2);
                    printCustom("رقم الصنف : " + items.get(j).getItemNo() + "\n", 1, 2);
                    printCustom("الصنف     : " + items.get(j).getItemName() + "\n", 1, 2);
                    printCustom("الكمية    : " + items.get(j).getQty() + "\n", 1, 2);
                    printCustom("السعر     : " + items.get(j).getPrice() + "\n", 1, 2);
                    printCustom("الخصم     : " + items.get(j).getDisc() + "\n", 1, 2);
                    printCustom("الصافي    : " + new DecimalFormat("#.##").format(Double.valueOf(amount)) + "\n", 1, 2);
                    printCustom("الضريبة   : " + items.get(j).getTaxValue() + "\n", 1, 2);
                    printCustom("الاجمالي   : " + amountATax + "\n", 1, 2);

                    printCustom("* * * * * * * * * * * * * " + "\n", 1, 2);

                    totalQty += items.get(j).getQty();
                    totalPrice += items.get(j).getPrice();
                    totalDisc += items.get(j).getDisc();
                    totalNet += (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                    totalTax += items.get(j).getTaxValue();
                    totalTotal += items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc() + items.get(j).getTaxValue() ;

                }

                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("اجمالي الكمية  : " + totalQty + "\n", 1, 2);
                printCustom("اجمالي السعر   : " + totalPrice + "\n", 1, 2);
                printCustom("اجمالي الخصم   : " + totalDisc + "\n", 1, 2);
                printCustom("اجمالي الصافي  : " + totalNet + "\n", 1, 2);
                printCustom("اجمالي الضريبة : " + totalTax + "\n", 1, 2);
                printCustom("اجمالي الإجمالي : " + totalTotal + "\n", 1, 2);

                mmOutputStream.write(PrinterCommands.FEED_LINE);
                if (voucher.getVoucherType() != 506) {
                    printCustom("استلمت البضاعة خالية من اي عيب او توالف" + "\n", 1, 2);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("توقيع العميل : _______________" + "\n", 1, 2);
                }
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                printCustom("----------------------------------------------" + "\n", 1, 2);
                printCustom("\n", 1, 2);
                printCustom("\n", 1, 2);

                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            }
            closeBT();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }

            Arabic864 arabic = new Arabic864();
            byte[] arabicArr = arabic.Convert(msg, false);
            mmOutputStream.write(arabicArr);

//            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    // Close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
