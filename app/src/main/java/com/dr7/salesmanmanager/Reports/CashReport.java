package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;
//import com.dr7.salesmanmanager.Pos;
import com.dr7.salesmanmanager.PrintPic;
import com.dr7.salesmanmanager.PrinterCommands;
import com.dr7.salesmanmanager.R;
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

public class CashReport  extends AppCompatActivity {
    List<Payment> payments;
    private EditText date;
    private Button preview,print;
    Calendar myCalendar;
    TextView cash_sal, credit_sale, total_sale;
    TextView cash_paymenttext, creditPaymenttext, nettext,total_cashtext;
    List<Voucher> voucher;
    double cash = 0, credit = 0, total = 0;
    double cashPayment=0,creditPayment=0,net=0,total_cash=0;
    int paymethod=0;
    private DecimalFormat decimalFormat;
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
    DatabaseHandler obj;
    private ImageView pic;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report);
        //************************* initial *************************************
        decimalFormat = new DecimalFormat("##.000");
        payments = new ArrayList<Payment>();
       obj = new DatabaseHandler(CashReport.this);
        payments = obj.getAllPayments();
        voucher = obj.getAllVouchers();
        date = (EditText) findViewById(R.id.date_editReport_cash);
        preview = (Button) findViewById(R.id.preview_cash_report);
        print = (Button) findViewById(R.id.print_cash_report);
        cash_sal = (TextView) findViewById(R.id.text_cash_sales);
        credit_sale = (TextView) findViewById(R.id.text_credit_sales);
        total_sale = (TextView) findViewById(R.id.text_total_sales);
        cash_paymenttext = (TextView) findViewById(R.id.text_cash_PaymentReport);
        creditPaymenttext = (TextView) findViewById(R.id.text_cheque_paymentReport);
        nettext = (TextView) findViewById(R.id.text_net_paymentReport);
        total_cashtext=(TextView) findViewById(R.id.text_total_cash);
        pic=(ImageView)findViewById(R.id.pic_reportCash);
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
                    total_cash=net+cash;
                    total_cashtext.setText(convertToEnglish(decimalFormat.format((total_cash))));
                    Log.e("totalcash","="+net+cash);

                    }

                else
                    Toast.makeText(CashReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
            }
        });

//        preview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
//                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
//                }
//                return false;
//            }
//        });
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
        print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (obj.getAllSettings().get(0).getPrintMethod() == 0) {
                    try {
                        findBT();
                        openBT();
                    } catch (IOException ex) {
                    }
                } else {
                   // hiddenDialog();
                }
            }
        });
    }


    void findBT() {

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
//            myLabel.setText("Bluetooth Device Found");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Tries to open a connection to the bluetooth printer device
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");
             sendData2();

          //  sendData();
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

    private void clear() {
    }
    void sendData2() throws IOException {
        try {

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            Log.e("nocopy", "" + numOfCopy);
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
            if (pic != null) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);

                Bitmap bitmap = pic.getDrawingCache();

                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();

                for (int i = 1; i <= numOfCopy; i++) {
                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
//                    printCustom(" \n ", 1, 0);
                    }

                    printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);
                    printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("التاريخ :       " + date.getText() + " : " + " \n ", 1, 0);
                    printCustom("المبيعات نقدا " + cash + " : " + " \n ", 1, 0);
                    printCustom("المبيعات ذمم   " + credit + " : " + " \n ", 1, 0);
                    printCustom("إجمالي المبيعات   " + convertToEnglish(decimalFormat.format(total)) + " : " + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("الدفع نقدا " + cashPayment + " : " + " \n ", 1, 0);
                    printCustom("الدفع شيك   " + creditPayment + " : " + " \n ", 1, 0);
                    printCustom("الاجمالي   " + net + " : " + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("اجمالي المقبوضات   " + total_cash + " : " + " \n ", 1, 0);


                }

            }
            closeBT();
        }
     catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
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
//            Pos pos=new Pos();
//           pos.POS_SetCharSetAndCodePage(0x00,0x00);

//            Arabic864 arabic = new Arabic864();
//            byte[] arabicArr = arabic.Convert(msg, false);
//            mmOutputStream.write(arabicArr);
               mmOutputStream.write(msg.getBytes());

//            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void sendData() throws IOException {
        try {

            Log.e("******", "here");
            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
            String nameCompany=companyInfo.getCompanyName().toString();

            // the text typed by the user
            String msg;

            for (int i = 1; i <= numOfCopy; i++) {
                    msg = "       " + "\n" +
                            "----------------------------------------------" + "\n" +
                            "       " + "\n" +
                            "       " + "\n" +
                            "       " + "\n" +

                            "----------------------------------------------" + "\n" +
                            "إجمالي المبيعات : "+total + "\n" +
                            "المبيعات ذمم : " +credit+ "\n" +
                            "المبيعات نقدا: " +cash + "\n" +
                            "----------------------------------------------" + "\n" +
                            "هاتف : " + companyInfo.getcompanyTel() +"    الرقم الضريبي : "  + companyInfo.getTaxNo() + "\n" +
                            companyInfo.getCompanyName() + "\n" +
                            "       " + "\n" +
                            "       ";

                printCustom(msg + "\n", 1, 2);
            }

            closeBT();

        } catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
            e.printStackTrace();
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
