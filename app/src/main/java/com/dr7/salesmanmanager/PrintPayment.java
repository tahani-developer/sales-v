package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Port.AlertView;
import com.dr7.salesmanmanager.Reports.Reports;
import com.ganesh.intermecarabic.Arabic864;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class PrintPayment extends AppCompatActivity {
    Bitmap testB;
    PrintPic printPic;
    byte[] printIm;

    private Thread hThread;
    private String lastConnAddr;
    private BluetoothPort bluetoothPort;
//    List<Voucher> vouchers;
    public static List<Item> items;
    public static List<Payment> paymentPaper;

    List<CompanyInfo> companeyinfo;
    TextView textSubTotal, textTax, textNetSales;
    EditText from_date, to_date;
    Button preview;
    ImageView pic;
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
    TextView doneinsewooprint;
    boolean isFinishPrint = false;
    public  static List <Payment> payment;
    public  static List <Payment> paymentPrinter;

    //Voucher addvou;
    static Payment pay1;
    Bitmap bitmap;
    String itemsString;
    String itemsString2 = "";
    DatabaseHandler obj;
    static double TOTAL=0;
    LinearLayout linearMain;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(PrintPayment.this);
        setContentView(R.layout.print_payment);
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        linearMain=findViewById(R.id.linearMain);
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
        payment = new ArrayList<>();
//        vouchers = new ArrayList<Voucher>();
        items = new ArrayList<Item>();
        companeyinfo = new ArrayList<CompanyInfo>();

        obj = new DatabaseHandler(PrintPayment.this);
        payment = obj.getAllPayments();
//        vouchers = obj.getAllVouchers();
        paymentPaper = obj.getAllPaymentsPaper();
//        items = obj.getAllItems();
        companeyinfo = obj.getAllCompanyInfo();
        bluetoothSetup();

        TableTransactionsReport = (TableLayout) findViewById(R.id.TableTransactionsReport);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        preview = (Button) findViewById(R.id.preview);
        pic = (ImageView) findViewById(R.id.pic_ptint_voucher);

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
                new DatePickerDialog(PrintPayment.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PrintPayment.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clear();

                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {

                    for (int n = 0; n < payment.size(); n++) {
                        final Payment pay;
                        pay = payment.get(n);

                        if (filters(n)) {

                            final TableRow row = new TableRow(PrintPayment.this);
                            row.setPadding(5, 10, 5, 5);

                            if (n % 2 == 0)
                                row.setBackgroundColor(getResources().getColor(R.color.layer3));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(PrintPayment.this, R.color.layer5));

                            for (int i = 0; i < 8; i++) {
                                String[] record = {payment.get(n).getVoucherNumber() + "",
                                        payment.get(n).getPayDate() + "",
                                        payment.get(n).getCustName() + "",
                                        payment.get(n).getAmount() + "",
                                        payment.get(n).getRemark() + "",
                                        payment.get(n).getSaleManNumber() + "",
                                        payment.get(n).getPayMethod() + ""};
//                                Log.e("paymethod",""+vouchers.get(n).getPayMethod());


                                switch (payment.get(n).getPayMethod()) {
                                    case 0:
                                        record[6] = getResources().getString(R.string.app_cheque);

                                        break;
                                    case 1:
                                        record[6] = getResources().getString(R.string.cash);
                                        break;
                                    case 2:
                                        record[6] = getResources().getString(R.string.app_creditCard);
                                        break;
                                }


                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                row.setLayoutParams(lp);

                                if (i != 7) {
                                    TextView textView = new TextView(PrintPayment.this);
                                    textView.setText(record[i]);
//                                    textView.setTextSize(12);
                                    textView.setTextColor(ContextCompat.getColor(PrintPayment.this, R.color.colorPrimary));
                                    textView.setGravity(Gravity.CENTER);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    textView.setLayoutParams(lp2);


                                    row.addView(textView);

                                } else {
                                    TextView textView = new TextView(PrintPayment.this);
                                    textView.setText(getResources().getString(R.string.print));
//                                    textView.setTextSize(12);
//                                    TableRow.LayoutParams lp22 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
//                                    textView.setLayoutParams(lp22);

                                    textView.setTextColor(ContextCompat.getColor(PrintPayment.this, R.color.layer5));
                                    textView.setBackgroundColor(ContextCompat.getColor(PrintPayment.this, R.color.colorAccent));
                                    textView.setGravity(Gravity.CENTER);


                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView textView = (TextView) row.getChildAt(0);
//                                            voucherInfoDialog(Integer.parseInt(textView.getText().toString()));

//                                            if (!obj.getAllCompanyInfo().get(0).getCompanyName().equals("") && obj.getAllCompanyInfo().get(0).getcompanyTel() != 0 && obj.getAllCompanyInfo().get(0).getTaxNo() != -1) {
                                                if (obj.getAllSettings().get(0).getPrintMethod() == 0) {
//                                                     try {
                                                    Log.e("pay", "  " + pay.getVoucherNumber());
                                                    try {

                                                        int printer = obj.getPrinterSetting();


                                                        switch (printer) {
                                                            case 0:
                                                                pay1 = pay;
                                                                paymentPrinter=obj.getRequestedPaymentsPaper(Integer.parseInt(textView.getText().toString()));
                                                                Intent i = new Intent(PrintPayment.this, BluetoothConnectMenu.class);
                                                                i.putExtra("printKey", "4");
                                                                startActivity(i);

//                                                             lk30.setChecked(true);
                                                                break;
                                                            case 1:

//                                                             lk31.setChecked(true);

                                                            case 2:

//                                                                try {
//                                                                    findBT(Integer.parseInt(textView.getText().toString()));
//                                                                    openBT(pay, 2);
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//


//                                                             lk32.setChecked(true);
                                                            case 3:

                                                                paymentPrinter = obj.getRequestedPaymentsPaper(Integer.parseInt(textView.getText().toString()));
                                                                pay1 = pay;
//                                                                convertLayoutToImage(pay);
                                                                Intent O12 = new Intent(PrintPayment.this, bMITP.class);
                                                                O12.putExtra("printKey", "4");
                                                                startActivity(O12);
                                                                Log.e("Pay 0000 ==>", "" + pay1.getPayMethod());

//                                                             qs.setChecked(true);
                                                                break;

                                                            case 4:
                                                                printTally(pay);
                                                                break;


                                                            case 5:
                                                            case 6:
                                                            case 7:

                                                                paymentPrinter = obj.getRequestedPaymentsPaper(Integer.parseInt(textView.getText().toString()));
                                                                pay1 = pay;
//                                                                convertLayoutToImage(pay);
                                                                Intent O1 = new Intent(PrintPayment.this, bMITP.class);
                                                                O1.putExtra("printKey", "4");
                                                                startActivity(O1);
                                                                Log.e("Pay 0000 ==>", "" + pay1.getPayMethod());
                                                                break;
//                                                                MTP.setChecked(true);

                                                        }
                                                    }
                                                    catch(Exception e){
                                                        Toast.makeText(PrintPayment.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();

                                                    }


//
//                                                         master(vouch);
//                                                     testB =convertLayoutToImage(v);


//                                                     } catch (IOException ex) {
//                                                     }
                                                } else {
                                                    hiddenDialog(pay);
                                                }
//                                            } else {
//                                                Toast.makeText(PrintPayment.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
//                                            }
                                        }
                                    });


                                    TableRow.LayoutParams lp25 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
                                    textView.setLayoutParams(lp25);
                                    row.addView(textView);
                                }
                            }
                            TableTransactionsReport.addView(row);
                        }

                    }

                } else
                {
                    Toast.makeText(PrintPayment.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
                    from_date.setError("Required");
                    to_date.setError("Required");
                }

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
    String beginDate = from_date.getText().toString();
    String toDate = to_date.getText().toString();

    String date = payment.get(n).getPayDate();
    Log.e("date",""+date);

    try {
            if ((formatDate(date).after(formatDate(beginDate)) || formatDate(date).equals(formatDate(beginDate))) &&
                    (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))))
            {
                return true;
            }


//        }
    } catch (ParseException e) {
        e.printStackTrace();
    }

    return false;
}

    @SuppressLint("SetTextI18n")
    public void hiddenDialog(Payment pay) {
         convertLayoutToImage(pay);
//        PrintHelper photoPrinter = new PrintHelper(PrintPayment.this);
//        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        linearLayout.setDrawingCacheEnabled(true);
//        bitmap = linearLayout.getDrawingCache();
//        photoPrinter.printBitmap("invoice.jpg", testB);


//        final Dialog dialog = new Dialog(PrintPayment.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.print);
//
//        final Button okButton = dialog.findViewById(R.id.print1);
//        final LinearLayout linearLayout = dialog.findViewById(R.id.linear1);
//        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);
//
//        TextView companyName = dialog.findViewById(R.id.company);
//        TextView phone = dialog.findViewById(R.id.phone);
//        TextView taxNo = dialog.findViewById(R.id.tax_no);
//        TextView date = dialog.findViewById(R.id.date);
//        TextView vouch_no = dialog.findViewById(R.id.voucher_no);
//        TextView vouchType = dialog.findViewById(R.id.voucher_type);
//        TextView payMethod = dialog.findViewById(R.id.payMethod);
//        TextView cust = dialog.findViewById(R.id.cust_);
//        TextView remark = dialog.findViewById(R.id.remark_);
//        TextView totalNoTax = dialog.findViewById(R.id.total_noTax);
//        TextView discount = dialog.findViewById(R.id.discount);
//        TextView tax = dialog.findViewById(R.id.tax);
//        TextView netSale = dialog.findViewById(R.id.net_sales_);
//
//        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//
//        companyName.setText(companyInfo.getCompanyName());
//        phone.setText(phone.getText().toString() + companyInfo.getcompanyTel());
//        taxNo.setText(taxNo.getText().toString() + companyInfo.getTaxNo());
//        date.setText(date.getText().toString() + voucher.getVoucherDate());
//        vouch_no.setText(vouch_no.getText().toString() + voucher.getVoucherNumber());
//        remark.setText(remark.getText().toString() + voucher.getRemark());
//        cust.setText(cust.getText().toString() + voucher.getCustName());
//        totalNoTax.setText(totalNoTax.getText().toString() + voucher.getSubTotal());
//        discount.setText(discount.getText().toString() + voucher.getVoucherDiscount());
//        tax.setText(tax.getText().toString() + voucher.getTax());
//        netSale.setText(netSale.getText().toString() + voucher.getNetSales());
//
//        String voucherTyp = "";
//        switch (voucher.getVoucherType()) {
//            case 504:
//                voucherTyp = "فاتورة بيع";
//                break;
//            case 506:
//                voucherTyp = "فاتورة مرتجعات";
//                break;
//            case 508:
//                voucherTyp = "طلب جديد";
//                break;
//        }
//        vouchType.setText(vouchType.getText().toString() + voucherTyp);
//        payMethod.setText(payMethod.getText().toString() + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//
//        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);
//        lp2.setMargins(0, 7, 0, 0);
//        lp3.setMargins(0, 7, 0, 0);
//
//        if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
//
//            final TableRow headerRow = new TableRow(PrintPayment.this);
//
//            TextView headerView7 = new TextView(PrintPayment.this);
//            headerView7.setGravity(Gravity.CENTER);
//            headerView7.setText("المجموع");
//            headerView7.setLayoutParams(lp2);
//            headerView7.setTextSize(12);
//            headerRow.addView(headerView7);
//
//            TextView headerView6 = new TextView(PrintPayment.this);
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("الخصم");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(PrintPayment.this);
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("المجاني");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(PrintPayment.this);
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("سعر الوحدة");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(PrintPayment.this);
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("الوزن");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(PrintPayment.this);
//            headerView2.setGravity(Gravity.CENTER);
//            headerView2.setText("العدد");
//            headerView2.setLayoutParams(lp2);
//            headerView2.setTextSize(12);
//            headerRow.addView(headerView2);
//
//            TextView headerView1 = new TextView(PrintPayment.this);
//            headerView1.setGravity(Gravity.CENTER);
//            headerView1.setText("السلعة");
//            headerView1.setLayoutParams(lp3);
//            headerView1.setTextSize(12);
//            headerRow.addView(headerView1);
//
//            tabLayout.addView(headerRow);
//        } else {
//            final TableRow headerRow = new TableRow(PrintPayment.this);
//            TextView headerView1 = new TextView(PrintPayment.this);
//
//            TextView headerView6 = new TextView(PrintPayment.this);
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("المجموع");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(PrintPayment.this);
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("الخصم");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(PrintPayment.this);
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("المجاني");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(PrintPayment.this);
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("سعر الوحدة");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(PrintPayment.this);
//            headerView2.setGravity(Gravity.CENTER);
//            headerView2.setText("العدد");
//            headerView2.setLayoutParams(lp2);
//            headerView2.setTextSize(12);
//            headerRow.addView(headerView2);
//
//            headerView1.setGravity(Gravity.CENTER);
//            headerView1.setText("السلعة");
//            headerView1.setLayoutParams(lp3);
//            headerView1.setTextSize(12);
//            headerRow.addView(headerView1);
//
//            tabLayout.addView(headerRow);
//        }
//
//        for (int j = 0; j < items.size(); j++) {
//
//            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
//                final TableRow row = new TableRow(PrintPayment.this);
//
//                if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
//
//                    for (int i = 0; i <= 7; i++) {
//                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(0, 10, 0, 0);
//                        row.setLayoutParams(lp);
//
//                        TextView textView = new TextView(PrintPayment.this);
//                        textView.setGravity(Gravity.CENTER);
//                        textView.setTextSize(10);
//
//                        switch (i) {
//                            case 6:
//                                textView.setText(items.get(j).getItemName());
//                                textView.setLayoutParams(lp3);
//                                break;
//
//                            case 5:
//                                textView.setText(items.get(j).getUnit());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 4:
//                                textView.setText("" + items.get(j).getQty());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 3:
//                                textView.setText("" + items.get(j).getPrice());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 2:
//                                textView.setText("" + items.get(j).getBonus());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 1:
//                                textView.setText("" + items.get(j).getDisc());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 0:
//                                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                                amount = convertToEnglish(amount);
//                                textView.setText(amount);
//                                textView.setLayoutParams(lp2);
//                                break;
//                        }
//                        row.addView(textView);
//                    }
//
//                } else {
//                    for (int i = 0; i <= 6; i++) {
//                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(0, 10, 0, 0);
//                        row.setLayoutParams(lp);
//
//                        TextView textView = new TextView(PrintPayment.this);
//                        textView.setGravity(Gravity.CENTER);
//                        textView.setTextSize(10);
//
//                        switch (i) {
//                            case 5:
//                                textView.setText(items.get(j).getItemName());
//                                textView.setLayoutParams(lp3);
//                                break;
//
//                            case 4:
//                                textView.setText(items.get(j).getUnit());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 3:
//                                textView.setText("" + items.get(j).getPrice());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 2:
//                                textView.setText("" + items.get(j).getBonus());
//                                textView.setLayoutParams(lp2);
//                                break;
//
//                            case 1:
//                                textView.setText("" + items.get(j).getDisc());
//                                textView.setLayoutParams(lp2);
//
//                            case 0:
//                                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                                amount = convertToEnglish(amount);
//                                textView.setText(amount);
//                                textView.setLayoutParams(lp2);
//                                break;
//                        }
//                        row.addView(textView);
//                    }
//                }
//                tabLayout.addView(row);
//            }
//        }
//
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PrintHelper photoPrinter = new PrintHelper(PrintPayment.this);
//                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//                linearLayout.setDrawingCacheEnabled(true);
//                bitmap = linearLayout.getDrawingCache();
//                photoPrinter.printBitmap("invoice.jpg", bitmap);
//
//            }
//        });
//        dialog.show();
    }

    void findBT(int voucherNo) {
//        try {
//            /*  very important **********************************************************/
//            closeBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        List<Payment> payments = obj.getRequestedPaymentsPaper(voucherNo);
        itemsString = "";
        for (int i = 0; i < payments.size(); i++) {

            String row = ".                                             ";
            row = row.substring(0, 13) + payments.get(i).getCheckNumber() + row.substring(13, row.length());
            row = row.substring(0, 24) + payments.get(i).getDueDate() + row.substring(24, row.length());
            row = row.substring(0, 38) + payments.get(i).getAmount() + row.substring(38, row.length()) + "\n";
//                                    row = row.substring(0, 42) + payments.get(i).getAmount() + row.substring(42, row.length());
            row = row.trim();
            row += "\n" + " " + payments.get(i).getBank();//test + =

            itemsString = "\n" + itemsString + "\n" + row;
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
    void openBT(Payment pay, int casePrinter) throws IOException {
        try {
            Log.e("open", "'yes");
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();


            beginListenForData();
            Settings settings = obj.getAllSettings().get(0);


            switch (casePrinter){

                case 1:
                    sendData(pay);
                    break;
                case 2:
                    for(int i=0;i<settings.getNumOfCopy();i++)
                    {send_dataSewoo(pay);}

                    break;
                case 3:
                    sendData2(pay);
                    break;



            }

//            myLabel.setText("Bluetooth Opened");

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
    public void sendDataTest() throws UnsupportedEncodingException {
//        int nLineWidth = 384;
//        try {
//            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//            pic.setImageBitmap(companyInfo.getLogo());
//            pic.setDrawingCacheEnabled(true);
//            Bitmap bitmap = pic.getDrawingCache();
//          //  posPtr.printBitmap(bitmap, LKPrint.LK_ALIGNMENT_LEFT, 1);
//
//
//            PrintPic printPic = PrintPic.getInstance();
//            printPic.init(bitmap);
//            byte[] bitmapdata = printPic.printDraw();
//         //   mmOutputStream.write(bitmap);
//          //  posPtr.printAndroidFont("فاتورة بيع ", nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ESCPSample2 i=new ESCPSample2();
//        i.printMultilingualFont();
    }


    void sendData(Payment pay) throws IOException {
        try {
            Log.e("send", "'yes");

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
            Log.e("printer_pay", "'yes"+companyInfo.getCompanyName());

//            if (companyInfo != null) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();
                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();

                String msg;
                for (int i = 1; i <= numOfCopy; i++) {
                    if (pay.getPayMethod() == 1) {
                        msg = "       " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "       " + "\n" +
                                "       " + "\n" +
                                "       " + "\n" +
                                "طريقة الدفع: " + (pay.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
                                "المبلغ المقبوض: " + pay.getAmount() + "\n" +
                                "ملاحظة: "+ pay.getRemark() + "\n" +
                                pay.getCustName() + "\n" +
                                "وصلني من السيد/السادة: "   +
                                "       " + "\n" +
                                "رقم السند: "+ pay.getVoucherNumber() + "         التاريخ: " + pay.getPayDate() + "\n" +
                                " سند قبض " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "هاتف : " + companyInfo.getcompanyTel() +"    الرقم الضريبي : "  + companyInfo.getTaxNo() + "\n" +
                                companyInfo.getCompanyName() + "\n" +
                                "       " + "\n" +
                                "       ";
                    } else {
                        msg = "       " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "       " + "\n" +
                                "       " + "\n" +
                                itemsString + "\n" +
                                "       " + "\n" +
                                "   البنك     " +"  رقم الشيك  "+ "  التاريخ       " + "  القيمة  " + "\n" +
                                "       " + "\n" +
                                "طريقة الدفع: " + (pay.getPayMethod() == 1? "نقدا" : "شيك") + "\n" +
                                "المبلغ المقبوض: " + pay.getAmount() + "\n" +
                                "ملاحظة: " + pay.getRemark() + "\n" +
                                pay.getCustName() + "\n" +
                                "وصلني من السيد/السادة: " + "\n" + "       " + "\n" +
                                "رقم السند: " + pay.getVoucherNumber() + "         التاريخ: "+ pay.getPayDate() + "\n" +
                                " سند قبض "  + "\n" +
                                "----------------------------------------------" + "\n" +
                                "هاتف : "  + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
                                companyInfo.getCompanyName() + "\n" +
                                "       " + "\n" +
                                "       ";
                    }
                    printCustom(msg + "\n", 1, 2);

                }
                closeBT();
                // tell the user data were sent
//                myLabel.setText("Data Sent");
//            } else
//                Toast.makeText(PrintPayment.this, " please enter company information", Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void send_dataSewoo(Payment pay) throws IOException {
        try {
            Log.e("send", "'yes");
            testB = convertLayoutToImage(pay);

            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm = printPic.printDraw();
//            mmOutputStream.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
            mmOutputStream.write(printIm);

//            dialogs.show();
            isFinishPrint = true;

            ImageView iv = (ImageView) findViewById(R.id.ivw);
////                iv.setLayoutParams(layoutParams);
            iv.setBackgroundColor(Color.TRANSPARENT);
            iv.setImageBitmap(testB);
//                iv.setMaxHeight(100);
//                iv.setMaxWidth(100);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void printTally(Payment pay) {

        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        List<Payment> items1=new ArrayList<>();
        for (int j = 0; j < paymentPaper.size(); j++) {

            if (pay.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                items1.add(paymentPaper.get(j));

            }
        }

        Log.e("Items1__",""+items1.size()+"    "+(items1.size()<=17));

        if(items1.size()<=17){
            bitmap = convertLayoutToImageTally(pay,1,0,items1.size(),items1);
            try {
                Settings settings = obj.getAllSettings().get(0);
                File file = savebitmap(bitmap, settings.getNumOfCopy(),"org");
                Log.e("save image ", "" + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {

            Settings settings = obj.getAllSettings().get(0);
            for(int i=0;i<settings.getNumOfCopy();i++) {
                bitmap = convertLayoutToImageTally(pay, 0,0,17,items1);
                bitmap2 = convertLayoutToImageTally(pay, 1,17,items1.size(),items1);


                try {

                    File file = savebitmap(bitmap, 1,"fir"+""+i);
                    File file2 = savebitmap(bitmap2, 1,"sec"+""+i);

                    Log.e("save image ", "" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }




    }

    public static File savebitmap(Bitmap bmp, int numCope,String next) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSaleS/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < numCope; i++) {
            String targetPdf = directory_path + "testimageVoucher" + i+""+next + ".png";
            f = new File(targetPdf);


//        f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        return f;
    }


    private Bitmap convertLayoutToImage(Payment pay) {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(PrintPayment.this);
        dialogs.setContentView(R.layout.print_payment_cash);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

//*******************************************initial ***************************************************************
        TextView compname,tel,taxNo,cashNo,date,custname,note,paytype,ammont;
        LinearLayout cheque_print_linear=(LinearLayout)dialogs.findViewById(R.id.cheque_payment_layout) ;
        compname=(TextView)dialogs.findViewById(R.id.textView_companey_Name);
        tel=(TextView)dialogs.findViewById(R.id.telephone);
        taxNo=(TextView)dialogs.findViewById(R.id.tax_no);
        cashNo=(TextView)dialogs.findViewById(R.id.textView_cashNo);
        date=(TextView)dialogs.findViewById(R.id.textVie_date);
        custname=(TextView)dialogs.findViewById(R.id.textView_customerName);
        note=(TextView)dialogs.findViewById(R.id.textView_remark);
        ammont=(TextView)dialogs.findViewById(R.id.textView_amount_ofMoney);
        paytype=(TextView)dialogs.findViewById(R.id.textView_payMethod);
        TableLayout tableCheque=(TableLayout)dialogs.findViewById(R.id.table_bank_info);
        ImageView companey_logo=(ImageView)dialogs.findViewById(R.id.imageCompaney_logo);


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
        try {
            companey_logo.setImageBitmap(companyInfo.getLogo());
        }catch ( Exception e){}


//        }
        compname.setText(companyInfo.getCompanyName() );
        tel.setText(""+companyInfo.getcompanyTel());
        taxNo.setText(""+companyInfo.getTaxNo() );
        ammont.setText(pay.getAmount()+"");
        note.setText(pay.getRemark());
        if(pay.getPayMethod()==1) {
            paytype.setText(" نقدا ");
            cheque_print_linear.setVisibility(View.GONE);
        }
        else {
            paytype.setText(" ذمم  ");
            cheque_print_linear.setVisibility(View.VISIBLE);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            lp.setMargins(0, 7, 0, 0);
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(0, 7, 0, 0);

            List<Payment> payments = obj.getRequestedPaymentsPaper(pay.getVoucherNumber());

            for (int n = 0; n < payments.size(); n++) {
//                if (payments.get(n).getVoucherNumber() == mDbHandler.getMaxSerialNumber(4)) {
                final TableRow row = new TableRow(PrintPayment.this);
                row.setPadding(0, 10, 0, 10);
                Log.e("paymentprint",""+payments.size());
                for (int i = 0; i < 4; i++) {

                    String[] record = {
                            payments.get(n).getBank() + "",
                            payments.get(n).getCheckNumber() + "",
                            payments.get(n).getDueDate() + "",
                            payments.get(n).getAmount() + "",
                    };

                    row.setLayoutParams(lp);
                    TextView textView = new TextView(PrintPayment.this);
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(PrintPayment.this, R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);

                }

                tableCheque.addView(row);
            }
//            }
        }


        custname.setText(pay.getCustName());
        date.setText(pay.getPayDate());
        cashNo.setText(pay.getVoucherNumber()+"");
        dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView  = (LinearLayout)dialogs.findViewById(R.id.print_invoice);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ","width="+ linearView.getMeasuredWidth()+"      higth ="+linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(PrintPayment.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        linearView.setDrawingCacheEnabled(true);
        bitmap = linearView.getDrawingCache();
        photoPrinter.printBitmap("pay.jpg", bitmap);
        return bit;// creates bitmap and returns the same
    }

    private Bitmap convertLayoutToImageTally(Payment pay,int okShow,int start,int end,List<Payment>items) {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(PrintPayment.this);
        dialogs.setContentView(R.layout.print_payment_cash_tally);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//*******************************************initial ***************************************************************
        TextView compname,tel,taxNo,cashNo,date,custname,note,paytype,ammont;
        LinearLayout cheque_print_linear=(LinearLayout)dialogs.findViewById(R.id.cheque_payment_layout) ;
        compname=(TextView)dialogs.findViewById(R.id.textView_companey_Name);
        tel=(TextView)dialogs.findViewById(R.id.telephone);
        taxNo=(TextView)dialogs.findViewById(R.id.tax_no);
        cashNo=(TextView)dialogs.findViewById(R.id.textView_cashNo);
        date=(TextView)dialogs.findViewById(R.id.textVie_date);
        custname=(TextView)dialogs.findViewById(R.id.textView_customerName);
        note=(TextView)dialogs.findViewById(R.id.textView_remark);
        ammont=(TextView)dialogs.findViewById(R.id.textView_amount_ofMoney);
        paytype=(TextView)dialogs.findViewById(R.id.textView_payMethod);
        TableLayout tableCheque=(TableLayout)dialogs.findViewById(R.id.table_bank_info);
        ImageView companey_logo=(ImageView)dialogs.findViewById(R.id.imageCompaney_logo);


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

        companey_logo.setVisibility(View.INVISIBLE);
        compname.setVisibility(View.INVISIBLE);

        if(!companyInfo.getLogo().equals(null))
        {
            companey_logo.setImageBitmap(companyInfo.getLogo());

        }
        compname.setText(companyInfo.getCompanyName() );
        tel.setText(""+companyInfo.getcompanyTel());
        taxNo.setText(""+companyInfo.getTaxNo() );
        ammont.setText(pay.getAmount()+"");
        note.setText(pay.getRemark());
        if(pay.getPayMethod()==1) {
            paytype.setText(" نقدا ");
            cheque_print_linear.setVisibility(View.GONE);
        }
        else {
            paytype.setText(" ذمم  ");
            cheque_print_linear.setVisibility(View.VISIBLE);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            lp.setMargins(0, 7, 0, 0);
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(0, 7, 0, 0);

            List<Payment> payments = obj.getRequestedPaymentsPaper(pay.getVoucherNumber());

            for (int n = 0; n < payments.size(); n++) {
//                if (payments.get(n).getVoucherNumber() == mDbHandler.getMaxSerialNumber(4)) {
                final TableRow row = new TableRow(PrintPayment.this);
                row.setPadding(0, 10, 0, 10);
                Log.e("paymentprint",""+payments.size());
                for (int i = 0; i < 4; i++) {

                    String[] record = {
                            payments.get(n).getBank() + "",
                            payments.get(n).getCheckNumber() + "",
                            payments.get(n).getDueDate() + "",
                            payments.get(n).getAmount() + "",
                    };

                    row.setLayoutParams(lp);
                    TextView textView = new TextView(PrintPayment.this);
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(PrintPayment.this, R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);

                }

                tableCheque.addView(row);
            }
//            }
        }


        custname.setText(pay.getCustName());
        date.setText(pay.getPayDate());
        cashNo.setText(pay.getVoucherNumber()+"");
//        dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView  = (LinearLayout)dialogs.findViewById(R.id.print_invoice);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ","width="+ linearView.getMeasuredWidth()+"      higth ="+linearView.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = linearView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        linearView.draw(canvas);

        return bitmap;// creates bitmap and returns the same
    }

    private Bitmap convertLayoutToImageTallySub(Voucher voucher) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(PrintPayment.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.printdialog_tally);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);

        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        TableLayout sumLayout = (TableLayout) dialogs.findViewById(R.id.table);

        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                dialogs.dismiss();
//                }
            }
        });


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
        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText("" + voucher.getVoucherDiscount());
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());


        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < items.size(); j++) {

            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(PrintPayment.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintPayment.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + items.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);
            }
        }


//        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = linearView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        linearView.draw(canvas);

        return bitmap;// creates bitmap and returns the same
    }

    void sendData2(Payment pay) throws IOException {
        try {

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            Log.e("nocopy",""+numOfCopy);
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
            if(pic!=null) {
//                pic.setImageBitmap(companyInfo.getLogo());
                String s="tahani test invoice ";
                byte [] encodeByte= Base64.decode(s,Base64.DEFAULT);

                InputStream inputStream  = new ByteArrayInputStream(encodeByte);
                Bitmap bitmap2  = BitmapFactory.decodeStream(inputStream);
                pic.setImageBitmap(bitmap2);
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();
                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();

                for (int i = 1; i <= numOfCopy; i++) {
                    Thread.sleep(1000);
                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
//                    printCustom(" \n ", 1, 0);
                    }
                    if (pay.getPayMethod() == 1) {

                        printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);
                        printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                        printCustom("------------------------------------------" + " \n ", 1, 0);
                        printCustom("التاريخ        " + pay.getPayDate() + " : " + " \n ", 1, 0);
                        printCustom("رقم السند      " + pay.getVoucherNumber() + " : " + " \n ", 1, 0);
                        printCustom("اسم العميل " + " : " + pay.getCustName() + " \n ", 1, 0);
                        printCustom("ملاحظة " + " : " + pay.getRemark() + " \n ", 1, 0);
                        printCustom("المبلغ المقبوض " + pay.getAmount() + " : " + " \n ", 1, 0);
                        printCustom("طريقة الدفع    " + " : " + "نقدا" + " \n ", 1, 0);
                        //  printCustom("طريقة الدفع    " + " : " + (payment.getPayMethod() == 0 ?  "شيك" : "نقدا") +  " \n ", 1, 0);
                        printCustom("\n", 1, 0);
                    } else {
                        printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);
                        printCustom("الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                        printCustom("------------------------------------------" + " \n ", 1, 0);
                        printCustom("التاريخ        " + pay.getPayDate() + " : " + " \n ", 1, 0);
                        printCustom("رقم السند      " + pay.getVoucherNumber() + " : " + " \n ", 1, 0);
                        printCustom("اسم العميل " + " : " + pay.getCustName() + " \n ", 1, 0);
                        printCustom("ملاحظة " + " : " + pay.getRemark() + " \n ", 1, 0);
                        printCustom("المبلغ المقبوض " + pay.getAmount() + " : " + " \n ", 1, 0);
                        printCustom("طريقة الدفع    " + " : " + "شيك" + " \n ", 1, 0);
                        printCustom("\n", 1, 0);

                        List<Payment> payments = obj.getRequestedPaymentsPaper(pay.getVoucherNumber());

                        int serial = 1;
                        Log.e("payments ", "" + payments.size());
                        for (int j = 0; j < payments.size(); j++) {

                            Log.e("payments 11", "" + payments.get(j).getBank());
                            Log.e("kk", " " + payments.get(j).getCheckNumber());

                            printCustom("(" + serial + "" + "\n", 1, 0);
                            printCustom("رقم الشيك " + payments.get(j).getCheckNumber() + " : " + " \n ", 1, 0);
                            printCustom("البنك     " + payments.get(j).getBank() + " : " + " \n ", 1, 0);
                            printCustom("التاريخ     " + payments.get(j).getDueDate() + " : " + " \n ", 1, 0);
                            printCustom("القيمة      " + payments.get(j).getAmount() + " : " + " \n ", 1, 0);
                            printCustom("* * * * * * * * * * * * * " + " \n ", 1, 0);
                        }

                    }

                }

//            Arabic864 arabic = new Arabic864();
//            byte[] arabicArr = arabic.Convert(msg, false);

                closeBT();
//                printPic.
            }
        } catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
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
//             mmOutputStream.write(msg.getBytes());

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
//            mmSocket=null;
            //            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void master(Voucher voucher) {
//        addPairedDevices();

        if (!bluetoothPort.isConnected()) {
            try {
                // "00:13:7B:58:37:9A"
                mmDevice = mBluetoothAdapter.getRemoteDevice("00:13:7B:58:37:9A");
                btConn(mmDevice);
                Log.e("mac address ", "" + mmDevice);
            } catch (IllegalArgumentException var3) {
                Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
                return;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                return;
            }
        } else {
//                btDisconn();
        }

        String strCount = "1";
        int count = Integer.parseInt(strCount);
        Log.d("NUM", String.valueOf(count));
        CPCLSample2 sample = new CPCLSample2(PrintPayment.this);
        sample.selectGapPaper();
        try {
//            sample.printMultilingualFont(count);
//            testB = convertLayoutToImage(pay);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clear);

            sample.dmStamp(1, bitmap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void btConn(BluetoothDevice btDev) throws IOException {
        (new connTask()).execute(new BluetoothDevice[]{btDev});
    }

    class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(PrintPayment.this);

        connTask() {
        }

        protected void onPreExecute() {
            this.dialog.setTitle("ddddd");
            this.dialog.setMessage("vvv");
            this.dialog.show();
            super.onPreExecute();
        }

        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;

            try {
                bluetoothPort.connect(mmDevice);
                lastConnAddr = mmDevice.getAddress();
                retVal = 0;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu", var4.getMessage());
                retVal = -1;
            }

            return retVal;
        }

        protected void onPostExecute(Integer result) {
            if (result == 0) {
                RequestHandler rh = new RequestHandler();
                hThread = new Thread(rh);
                hThread.start();

                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                Toast toast = Toast.makeText(PrintPayment.this, "connect post", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("post alert", "post ,,,.", PrintPayment.this);
            }

            super.onPostExecute(result);
        }
    }


    private void bluetoothSetup() {
        this.clearBtDevData();
        this.bluetoothPort = BluetoothPort.getInstance();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBtIntent, 2);
            }

        }
    }

    private void clearBtDevData() {
//        this.mmDevice = null;
    }


    private void addPairedDevices() {
        Iterator iter = this.mBluetoothAdapter.getBondedDevices().iterator();

        while (iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice) iter.next();
            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                mmDevice = pairedDevice;
                Log.e("device ", "" + mmDevice);

            }
        }

    }


    protected void onDestroy() {
        try {
//            if (this.bluetoothPort.isConnected() && this.chkDisconnect.isChecked()) {
//                this.unregisterReceiver(disconnectReceiver);
//            }

            this.bluetoothPort.disconnect();
        } catch (IOException var2) {
            Log.e("BluetoothConnectMenu", var2.getMessage(), var2);
        } catch (InterruptedException var3) {
            Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
            this.hThread = null;
        }

//        this.unregisterReceiver(this.searchFinish);
//        this.unregisterReceiver(this.searchStart);
//        this.unregisterReceiver(this.discoveryResult);
        super.onDestroy();
    }


}


