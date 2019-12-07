package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.BluetoothConnectMenu;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.PrintPic;
import com.dr7.salesmanmanager.PrinterCommands;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.bMITP;
import com.ganesh.intermecarabic.Arabic864;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

import static com.dr7.salesmanmanager.ReceiptVoucher.savebitmap;

//import com.dr7.salesmanmanager.Pos;

public class CashReport  extends AppCompatActivity {
    List<Payment> payments;
    public  static EditText date;
    private Button preview,print;
    Calendar myCalendar;
    Bitmap testB;
    PrintPic printPic;
    boolean isFinishPrint=false;
    byte[] printIm;
    TextView cash_sal, credit_sale, total_sale;
    TextView cash_paymenttext, creditPaymenttext, nettext,total_cashtext;
    List<Voucher> voucher;
     public static double cash = 0, credit = 0, total = 0;
    public static double cashPayment=0,creditPayment=0,net=0,total_cash=0;
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
    public static double counter,returnCash=0,returnCridet=0;
    volatile boolean stopWorker;
    DatabaseHandler obj;
    private ImageView pic;
    CompanyInfo companyInfo;
    List<Item> vouchersales;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report);
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //************************* initial *************************************
        decimalFormat = new DecimalFormat("##.00");
        payments = new ArrayList<Payment>();
        vouchersales=new ArrayList<Item>();


        try {
            obj = new DatabaseHandler(CashReport.this);
            payments = obj.getAllPayments();
            voucher = obj.getAllVouchers();
        }catch (Exception e)
        {
            Toast.makeText(this, "Empty Data base", Toast.LENGTH_SHORT).show();
        }
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
         companyInfo=new CompanyInfo();


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                if (!date.getText().toString().equals("")) {
                    cash = 0;      credit = 0;   total = 0;
                    returnCridet=0;returnCash=0;
                    for (int n = 0; n < voucher.size(); n++) {
                        if (filters(n)) {
                            switch (voucher.get(n).getPayMethod()) {
                                case 0:
                                    paymethod = 0;
                                            if(voucher.get(n).getVoucherType()==506)
                                            {      returnCridet+=voucher.get(n).getNetSales();

                                            }
                                            else if(voucher.get(n).getVoucherType()==504) {
                                                credit += voucher.get(n).getNetSales();
                                            }
                                    break;
                                case 1:
                                    paymethod = 1;

                                    if(voucher.get(n).getVoucherType()==506)
                                    {      returnCash+=voucher.get(n).getNetSales();

                                    }
                                    else if(voucher.get(n).getVoucherType()==504) {
                                        cash += voucher.get(n).getNetSales();
                                    }
                                    break;
                            }
                        }
                    }

                    total = cash + credit-returnCash-returnCridet;
                    double T_cash=cash-returnCash;
                    double T_credit=credit-returnCridet;
                    cash_sal.setText(convertToEnglish(decimalFormat.format(T_cash ))+ "");
                    credit_sale.setText(convertToEnglish(decimalFormat.format(T_credit ))+ "");
                    total_sale.setText(convertToEnglish(decimalFormat.format(total)));
                    //  clearPayment();
                    cashPayment=0;creditPayment=0;net=0;total_cash=0;
                    for (int i = 0; i < payments.size(); i++) {
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
                    cash_paymenttext.setText(convertToEnglish(decimalFormat.format(cashPayment))+"");
                    creditPaymenttext.setText(convertToEnglish(decimalFormat.format(creditPayment))+"");
                    nettext.setText(convertToEnglish(decimalFormat.format(net)));
                    total_cash=net+cash-returnCash;
                    total_cashtext.setText(convertToEnglish(decimalFormat.format((total_cash))));
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
                try{
                if (obj.getAllSettings().get(0).getPrintMethod() == 0) {

                    try {
                        int printer = obj.getPrinterSetting();
                        companyInfo = obj.getAllCompanyInfo().get(0);
                        if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                            if (printer != -1) {
                                switch (printer) {
                                    case 0:

                                        Intent i = new Intent(CashReport.this, BluetoothConnectMenu.class);
                                        i.putExtra("printKey", "3");
                                        startActivity(i);

//                                                             lk30.setChecked(true);
                                        break;
                                    case 1:

                                        try {
                                            findBT();
                                            openBT(1);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//                                                             lk31.setChecked(true);
                                        break;
                                    case 2:

//                                        try {
//                                            findBT();
//                                            openBT(2);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                                             lk32.setChecked(true);

                                        convertLayoutToImage();

                                        Intent O1= new Intent(CashReport.this, bMITP.class);
                                        O1.putExtra("printKey", "3");
                                        startActivity(O1);

                                        break;
                                    case 3:

                                        try {
                                            findBT();
                                            openBT(3);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//                                                             qs.setChecked(true);
                                        break;
                                    case 4:
                                        printTally();
                                        break;
                                    case 5:
                                        convertLayoutToImage();
                                        Intent O= new Intent(CashReport.this, bMITP.class);
                                        O.putExtra("printKey", "3");
                                        startActivity(O);

                                        break;


                                }
                            } else {
                                Toast.makeText(CashReport.this, "please chose printer setting", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CashReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(CashReport.this, "Please set Printer Setting", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        Toast.makeText(CashReport.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(CashReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();

                    }
                } else {
                    // hiddenDialog();
                }
            }
                catch(Exception e){
                    Toast.makeText(CashReport.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                }

            }
        });
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


    // Tries to open a connection to the bluetooth printer device
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void openBT(int casePrinter) throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            switch (casePrinter){

                case 1:
                    sendData();
                    break;
                case 2:
                    Settings settings = obj.getAllSettings().get(0);
                    for(int i=0;i<settings.getNumOfCopy();i++)
                    {send_dataSewoo();}

                    break;
                case 3:
                    sendData2();
                    break;




            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void printTally() {
        Bitmap bitmap = convertLayoutToImageTally();
        try {
            Settings settings = obj.getAllSettings().get(0);
            File file = savebitmap(bitmap, settings.getNumOfCopy());
            Log.e("save image ", "" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File savebitmap(Bitmap bmp, int numCope) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSaleS/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < numCope; i++) {
            String targetPdf = directory_path + "testimageCash" + i + ".png";
            f = new File(targetPdf);
//        f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        return f;
    }

    private Bitmap convertLayoutToImageTally() {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(CashReport.this);
        dialogs.setContentView(R.layout.print_layout_talley_printer);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//*******************************************initial ***************************************************************
        TextView  tel,taxNo,compname,datepri, cash_sal, credit_sale, total_sale,
                cash_paymenttext, cheque_Paymenttext, nettext,total_cashtext;

        LinearLayout reportprint_linear=(LinearLayout)dialogs.findViewById(R.id.linear_print_cash_report) ;

        compname=(TextView)dialogs.findViewById(R.id.textView_companey_Name);
        taxNo=(TextView)dialogs.findViewById(R.id.taxNo);
        tel=(TextView)dialogs.findViewById(R.id.tel);
        datepri=(TextView)dialogs.findViewById(R.id.date_editReport_cash);
        cash_sal=(TextView)dialogs.findViewById(R.id.text_cash_sales);
        credit_sale=(TextView)dialogs.findViewById(R.id.text_credit_sales);
        total_sale=(TextView)dialogs.findViewById(R.id.text_total_sales);

        cash_paymenttext=(TextView)dialogs.findViewById(R.id.text_cash_PaymentReport);
        cheque_Paymenttext =(TextView)dialogs.findViewById(R.id.text_cheque_paymentReport);
        nettext=(TextView)dialogs.findViewById(R.id.text_net_paymentReport);

        total_cashtext=(TextView)dialogs.findViewById(R.id.text_total_cash);

//        TableLayout tableCheque=(TableLayout)dialogs.findViewById(R.id.table_bank_info);
//        ImageView companey_logo=(ImageView)dialogs.findViewById(R.id.imageCompaney_logo);


        TextView doneinsewooprint =(TextView) dialogs.findViewById(R.id.done);
        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });

//************************************************fill layout *********************************************************************
//        if(!companyInfo.getLogo().equals(null))
//        {
//            companey_logo.setImageBitmap(companyInfo.getLogo());
//
//        }
        taxNo.setText(companyInfo.getTaxNo() +"");
        tel.setText(companyInfo.getcompanyTel()+"");
        compname.setText(companyInfo.getCompanyName() );
        datepri.setText(date.getText().toString());
        cash_sal.setText(convertToEnglish(decimalFormat.format(( cash-returnCash ))));
        credit_sale.setText(convertToEnglish(decimalFormat.format((credit-returnCridet) )));
        total_sale.setText(convertToEnglish(decimalFormat.format(total)));

        cash_paymenttext.setText(convertToEnglish(decimalFormat.format( cashPayment )));
        cheque_Paymenttext.setText(convertToEnglish(decimalFormat.format(creditPayment )));
        nettext.setText(convertToEnglish(decimalFormat.format(net )));
        total_cashtext.setText(convertToEnglish(decimalFormat.format( total_cash )));
//        dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView  = (LinearLayout)dialogs.findViewById(R.id.linear_print_cash_report);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = linearView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        linearView.draw(canvas);

        Log.e("size of img ","width="+ linearView.getMeasuredWidth()+"      higth ="+linearView.getHeight());
//
//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();
        return bitmap;// creates bitmap and returns the same
        // creates bitmap and returns the same
    }
    void send_dataSewoo() throws IOException {
        try {
            Log.e("send","'yes");
            testB =convertLayoutToImage();

            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm= printPic.printDraw();
            mmOutputStream.write(printIm);
            isFinishPrint=true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Bitmap convertLayoutToImage() {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(CashReport.this);
        dialogs.setContentView(R.layout.print_cash_report);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//*******************************************initial ***************************************************************
        TextView  tel,taxNo,compname,datepri, cash_sal, credit_sale, total_sale,
        cash_paymenttext, cheque_Paymenttext, nettext,total_cashtext;

        LinearLayout reportprint_linear=(LinearLayout)dialogs.findViewById(R.id.linear_print_cash_report) ;

        compname=(TextView)dialogs.findViewById(R.id.textView_companey_Name);
        taxNo=(TextView)dialogs.findViewById(R.id.taxNo);
        tel=(TextView)dialogs.findViewById(R.id.tel);
        datepri=(TextView)dialogs.findViewById(R.id.date_editReport_cash);
        cash_sal=(TextView)dialogs.findViewById(R.id.text_cash_sales);
        credit_sale=(TextView)dialogs.findViewById(R.id.text_credit_sales);
        total_sale=(TextView)dialogs.findViewById(R.id.text_total_sales);

        cash_paymenttext=(TextView)dialogs.findViewById(R.id.text_cash_PaymentReport);
        cheque_Paymenttext =(TextView)dialogs.findViewById(R.id.text_cheque_paymentReport);
        nettext=(TextView)dialogs.findViewById(R.id.text_net_paymentReport);

        total_cashtext=(TextView)dialogs.findViewById(R.id.text_total_cash);

//        TableLayout tableCheque=(TableLayout)dialogs.findViewById(R.id.table_bank_info);
//        ImageView companey_logo=(ImageView)dialogs.findViewById(R.id.imageCompaney_logo);


        TextView doneinsewooprint =(TextView) dialogs.findViewById(R.id.done);
        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });

//************************************************fill layout *********************************************************************
//        if(!companyInfo.getLogo().equals(null))
//        {
//            companey_logo.setImageBitmap(companyInfo.getLogo());
//
//        }
        taxNo.setText(companyInfo.getTaxNo() +"");
        tel.setText(companyInfo.getcompanyTel()+"");
        compname.setText(companyInfo.getCompanyName() );
        datepri.setText(date.getText().toString());
        cash_sal.setText(convertToEnglish(decimalFormat.format( cash-returnCash )));
        credit_sale.setText(convertToEnglish(decimalFormat.format(credit -returnCridet)));
        total_sale.setText(convertToEnglish(decimalFormat.format(total)));

        cash_paymenttext.setText(convertToEnglish(decimalFormat.format( cashPayment )));
        cheque_Paymenttext.setText(convertToEnglish(decimalFormat.format(creditPayment )));
        nettext.setText(convertToEnglish(decimalFormat.format(net )));
        total_cashtext.setText(convertToEnglish(decimalFormat.format( total_cash )));
         dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView  = (LinearLayout)dialogs.findViewById(R.id.linear_print_cash_report);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ","width="+ linearView.getMeasuredWidth()+"      higth ="+linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        return bit;// creates bitmap and returns the same
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

                    printCustom(companyInfo.getCompanyName() + "     \n     ", 1, 0);
                    printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("التاريخ :       " + date.getText() + " : " + " \n ", 1, 0);
                    printCustom("المبيعات نقدا " + convertToEnglish(decimalFormat.format((cash-returnCash) ))+ " : " + " \n ", 1, 0);
                    printCustom("المبيعات ذمم   " +convertToEnglish(decimalFormat.format( (credit-returnCridet) ))+ " : " + " \n ", 1, 0);
                    printCustom("إجمالي المبيعات   " + convertToEnglish(decimalFormat.format(total)) + " : " + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("الدفع نقدا " + convertToEnglish(decimalFormat.format(cashPayment ))+ " : " + " \n ", 1, 0);
                    printCustom("الدفع شيك   " +convertToEnglish(decimalFormat.format( creditPayment ))+ " : " + " \n ", 1, 0);
                    printCustom("الاجمالي   " + convertToEnglish(decimalFormat.format(net)) + " : " + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("اجمالي المقبوضات   " + convertToEnglish(decimalFormat.format(total_cash ))+ " : " + " \n ", 1, 0);


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

            Arabic864 arabic = new Arabic864();
            byte[] arabicArr = arabic.Convert(msg, false);
            mmOutputStream.write(arabicArr);
//               mmOutputStream.write(msg.getBytes());

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
            CompanyInfo companyInfo = new CompanyInfo();
            Log.e("******", "here");
            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();

            try {
                companyInfo = obj.getAllCompanyInfo().get(0);
            } catch (Exception e) {
                companyInfo.setCompanyName("Companey Name");
                companyInfo.setTaxNo(0);
                companyInfo.setCompanyTel(00000);
//                companyInfo.setLogo();
            }
//&& !companyInfo.getLogo().equals(null)
            if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != 0) {
//            String nameCompany=companyInfo.getCompanyName().toString();

                // the text typed by the user
                String msg;

                for (int i = 1; i <= numOfCopy; i++) {
                    msg = "       " + "\n" +

                            "       " + "\n" +
                            "اجمالي المقبوضات   " + convertToEnglish(decimalFormat.format(total_cash)) +"\n" +
                            "الاجمالي   " + convertToEnglish(decimalFormat.format(net)) +"\n" +
                            "الدفع شيك   " + convertToEnglish(decimalFormat.format(creditPayment)) +"\n" +
                            "الدفع نقدا " + convertToEnglish(decimalFormat.format(cashPayment)) +"\n" +
                            "----------------------------------------------" + "\n" +
                            "إجمالي المبيعات : " + convertToEnglish(decimalFormat.format(total ))+ "\n" +
                            "المبيعات ذمم : " +convertToEnglish(decimalFormat.format( (credit-returnCridet)) )+ "\n" +
                            "المبيعات نقدا: " +convertToEnglish(decimalFormat.format( (cash-returnCash))) + "\n" +
                            "----------------------------------------------" + "\n" +
                            "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
                            "                          "+   companyInfo.getCompanyName() + "\n           " +
                            "       " + "\n" +
                            "       ";

                    printCustom(msg + "\n", 1, 2);
                }
            }

                closeBT();

            } catch(NullPointerException e){
                closeBT();
                e.printStackTrace();
            } catch(Exception e){
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
                if (( formatDate(date).equals(formatDate(date_text))))
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
