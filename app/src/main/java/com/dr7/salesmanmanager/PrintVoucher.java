package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.print.PrintHelper;
//import android.support.v7.app.AppCompatActivity;
import android.print.PrintDocumentAdapter;
import android.util.Log;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.dr7.salesmanmanager.Modles.PdfDocumentAdapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Port.AlertView;
import com.dr7.salesmanmanager.Reports.Reports;
import com.ganesh.intermecarabic.Arabic864;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;
import android.graphics.pdf.PdfDocument;
import android.graphics.Color;
import android.graphics.Paint;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.print.PrintManager;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class PrintVoucher extends AppCompatActivity {
    private String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/myCamera/";

    Bitmap testB;
    PrintPic printPic;
    byte[] printIm;

    private Thread hThread;
    private String lastConnAddr;
    private BluetoothPort bluetoothPort;
    List<Voucher> vouchers;
    public static List<Item> items;

    public static ArrayList<Item> voch_items=new ArrayList<>();
    public static List<Item> sub_items=new ArrayList<>();
    Voucher voucherPrint;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SweetAlertDialog pd;
    volatile boolean stopWorker;
    TextView doneinsewooprint;
    boolean isFinishPrint = false;

    //Voucher addvou;
    static Voucher vouch1;
    static Voucher vouchPrinted;
    Bitmap bitmap;
    String itemsString;
    String itemsString2 = "";
    DatabaseHandler obj;
     static double TOTAL=0;
    DecimalFormat decimalFormat;
    RadioGroup  voucherTypeRadioGroup;
    int voucherType = 504;

    LinearLayout linearMain;
GeneralMethod generalMethod;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        new LocaleAppUtils().changeLayot(PrintVoucher.this);
        setContentView(R.layout.print_vouchers);




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
        decimalFormat = new DecimalFormat("##.000");
        vouchers = new ArrayList<Voucher>();
        items = new ArrayList<Item>();
        companeyinfo = new ArrayList<CompanyInfo>();
//        verifyStoragePermissions(PrintVoucher.this);

        obj = new DatabaseHandler(PrintVoucher.this);
        vouchers = obj.getAllVouchers();
        items = obj.getAllItems(1);
        try {
            companeyinfo = obj.getAllCompanyInfo();
        }catch(Exception e){}


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
        voucherTypeRadioGroup = (RadioGroup) findViewById(R.id.transKindRadioGroup);
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

                if (!from_date.getText().toString().equals("")
                        && !to_date.getText().toString().equals("")) {

                    for (int n = 0; n < vouchers.size(); n++) {
                        final Voucher vouch;
                        vouch = vouchers.get(n);

                        if (filters(n)) {

                            final TableRow row = new TableRow(PrintVoucher.this);
                           row.setPadding(2, 2, 2, 2);
                          //  row.setPadding(10, 10, 10, 10);

                            if (n % 2 == 0)
                                row.setBackgroundColor(getResources().getColor(R.color.layer3));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer5));

                            for (int i = 0; i < 10; i++) {
                                String[] record = {vouchers.get(n).getCustName() + "\t\t\t\t\t\t",
                                        vouchers.get(n).getVoucherNumber() + "",
                                        vouchers.get(n).getVoucherDate() + "",
                                        vouchers.get(n).getPayMethod() + "",
                                        vouchers.get(n).getVoucherDiscount() + "",
                                        vouchers.get(n).getSubTotal() + "",
                                        vouchers.get(n).getTax() + "",
                                        vouchers.get(n).getNetSales() + ""};
//                                Log.e("paymethod",""+vouchers.get(n).getPayMethod());


                                switch (vouchers.get(n).getPayMethod()) {
                                    case 0:
                                        record[3] = getResources().getString(R.string.app_credit);

                                        break;
                                    case 1:
                                        record[3] = getResources().getString(R.string.cash);
                                        break;
                                }


                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                row.setLayoutParams(lp);

                                if (i != 8 && i != 9) {
                                    TextView textView = new TextView(PrintVoucher.this);
                                    textView.setText(record[i]);
//                                    textView.setTextSize(12);
                                    textView.setTextColor(ContextCompat.getColor(PrintVoucher.this, R.color.colorPrimary));
                                    textView.setGravity(Gravity.CENTER);
                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                //  lp2.height=70;
                                    textView.setLayoutParams(lp2);



                                    row.addView(textView);

                                }
                                else if(i == 8) {
                                    Button textView = new  Button(PrintVoucher.this);
                                    //textView.setText(getResources().getString(R.string.print));
                                    textView.setClickable(true);
                                    textView.setBackground(getResources().getDrawable(R.drawable.ic_print_black_24dp));
                                 //   textView.setTextColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer5));
                                  //  textView.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.colorAccent));
                                    textView.setGravity(Gravity.CENTER);


                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.R)
                                        @Override
                                        public void onClick(View v) {

                                            TextView textView = (TextView) row.getChildAt(1);
//                                            voucherInfoDialog(Integer.parseInt(textView.getText().toString()));
                                            String s="";
                                            Log.e("voucher2==", "  " +"gggg");
                                            try{

                                                if (!obj.getAllCompanyInfo().get(0).getCompanyName().equals("")
                                                        && !obj.getAllCompanyInfo().get(0).getcompanyTel() .equals("0")
                                                        && obj.getAllCompanyInfo().get(0).getTaxNo() != -1) {
                                                    Log.e("voucher3==", " voucher3== " );

                                                if (obj.getAllSettings().get(0).getPrintMethod() == 0) {
//                                                     try {
                                                    Log.e("voucher==", "  " + vouch.getVoucherNumber());
                                                    try {

                                                        int printer = obj.getPrinterSetting();

                                                        Log.e("printer==", "  " + printer);
                                                        switch (printer) {
                                                            case 0:
                                                                vouch1 = vouch;
                                                                Intent i = new Intent(PrintVoucher.this, BluetoothConnectMenu.class);
                                                                i.putExtra("printKey", "0");
                                                                startActivity(i);
                                                                Log.e("hheere11", "hheere11");
//                                                             lk30.setChecked(true);
                                                                break;
                                                            case 1:

//                                                                try {
//                                                                    findBT(Integer.parseInt(textView.getText().toString()),vouch);
//                                                                    openBT(vouch, 1);
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                             lk31.setChecked(true);
//                                                                break;
                                                            case 2:

//                                                                try {
//                                                                    findBT(Integer.parseInt(textView.getText().toString()));
//                                                                    openBT(vouch, 2);
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                             lk32.setChecked(true);

//


//
                                                            case 3:
                                                                Log.e("hheere", "hheere");
                                                                vouch1 = vouch;
                                                                voucherPrint = vouch;

                                                                try {
//
//                                                                    if(!Environment.isExternalStorageManager()){
//                                                                   Log.e("SDK_INT",""+Build.VERSION.SDK_INT+"\t"+Environment.isExternalStorageManager());
//                                                                        ActivityCompat.requestPermissions(PrintVoucher.this,
//                                                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
//                                                                                1);
//                                                                    }else {
//                                                                        Intent o = new Intent(PrintVoucher.this, bMITP.class);
//                                                                        o.putExtra("printKey", "0");
//                                                                        o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                        o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                        startActivity(o);
//                                                                    }
                                                                    Intent o = new Intent(PrintVoucher.this, bMITP.class);
                                                                        o.putExtra("printKey", "0");
//                                                                        o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                        o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        startActivity(o);


                                                        }
                                                                catch (Exception e){
                                                                    Log.e("bMITP",""+e.getMessage());
                                                                }

//                                                                try {
//                                                                    findBT(Integer.parseInt(textView.getText().toString()),vouch);
//                                                                    openBT(vouch, 3);
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                             qs.setChecked(true);
                                                                break;

                                                            case 4:
                                                                printTally(vouch);
                                                                break;

                                                            case 5:

                                                            case 6:
                                                            case 7:
                                                                vouch1 = vouch;
                                                                voucherPrint=vouch;
                                                                Log.e("hheere44","hheere");
//                                                                convertLayoutToImageW();
                                                                Intent o12 = new Intent(PrintVoucher.this, bMITP.class);
                                                                o12.putExtra("printKey", "0");
                                                                startActivity(o12);


                                                                break;

                                                        }
                                                    }
                                                    catch(Exception e){
                                                        Toast.makeText(PrintVoucher.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();

                                                    }


//
//                                                         master(vouch);
//                                                     testB =convertLayoutToImage(v);


//                                                     } catch (IOException ex) {
//                                                     }
                                                } else {

                                                    Log.e("voucher==", " LASTCASE " +"");
                                                    hiddenDialog(vouch);
                                                }
                                            } else {
                                                Toast.makeText(PrintVoucher.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                                            }

                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(PrintVoucher.this, ""+getResources().getString(R.string.error_companey_info), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                    TableRow.LayoutParams lp25 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
                                   lp25.width=50;
                                   lp25.height=60;
                                    textView.setLayoutParams(lp25);

                                    row.addView(textView);
                                }
                                else if(i == 9){
                                    Log.e("i == 9","i == 9");
                                    Button textView = new  Button(PrintVoucher.this);
                                    //textView.setText(getResources().getString(R.string.print));
                                    textView.setClickable(true);
                                    textView.setBackground(getResources().getDrawable(R.drawable.ic_baseline_share_24));
                                    //   textView.setTextColor(ContextCompat.getColor(PrintVoucher.this, R.color.layer5));
                                    //  textView.setBackgroundColor(ContextCompat.getColor(PrintVoucher.this, R.color.colorAccent));
                                    textView.setGravity(Gravity.CENTER);


                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.e("share","share");
                                            shareWhatsApp(vouch);
                                        }
                                    });
                                    TableRow.LayoutParams lp25 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
                                    lp25.width=60;
                                    lp25.height=60;
                                    textView.setLayoutParams(lp25);

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
        try {
            if (Build.VERSION.SDK_INT >= 30){
                if (!Environment.isExternalStorageManager()){
                    Intent getpermission = new Intent();
                    getpermission.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(getpermission);
                }
            }
        }catch (Exception e){

        }

//
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

    private void printVoucher() {

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
        Log.e("filtersvoucher",""+voucherType);


        String fromDate = from_date.getText().toString().trim();
        String toDate = to_date.getText().toString();

        String date = vouchers.get(n).getVoucherDate();
        int  vouchType=vouchers.get(n).getVoucherType();

        try {
            Log.e("tag", "*****" + date + "***" + fromDate+"\t vouchtype"+vouchType);
            if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                    (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))) && (vouchType==voucherType))
                return true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public  void   shareWhatsApp(Voucher voucher) {

        vouchPrinted = voucher;
        List<Item> itemVOCHER = new ArrayList<>();
        itemVOCHER = obj.getAllItemsBYVOCHER(String.valueOf(voucher.getVoucherNumber()), voucher.getVoucherType());

        Log.e("itemVOCHER==", "" + itemVOCHER.size());
        PdfConverter pdf = new PdfConverter(PrintVoucher.this);
 pdf.exportListToPdf(items, "Vocher", "", 17);


    }
        public  void exportToPdf(Voucher voucher){

      vouchPrinted=voucher;
  List<Item> itemVOCHER = new ArrayList<>();
        itemVOCHER=obj.getAllItemsBYVOCHER(String.valueOf(voucher.getVoucherNumber()),voucher.getVoucherType());

      Log.e("itemVOCHER==",""+itemVOCHER.size());
        PdfConverter pdf =new PdfConverter(PrintVoucher.this);
 pdf.exportListToPdf(    items,"Vocher","",13);


  }
    @SuppressLint("SetTextI18n")
    public void hiddenDialog(Voucher voucher) {
  exportToPdf( voucher);

        //convertLayoutToImage(voucher);

//  final Dialog dialog = new Dialog(PrintVoucher.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//       dialog.setContentView(R.layout.print);
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
//            final TableRow headerRow = new TableRow(PrintVoucher.this);
//
//            TextView headerView7 = new TextView(PrintVoucher.this);
//            headerView7.setGravity(Gravity.CENTER);
//            headerView7.setText("المجموع");
//            headerView7.setLayoutParams(lp2);
//            headerView7.setTextSize(12);
//            headerRow.addView(headerView7);
//
//            TextView headerView6 = new TextView(PrintVoucher.this);
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("الخصم");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(PrintVoucher.this);
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("المجاني");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(PrintVoucher.this);
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("سعر الوحدة");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(PrintVoucher.this);
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("الوزن");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(PrintVoucher.this);
//            headerView2.setGravity(Gravity.CENTER);
//            headerView2.setText("العدد");
//            headerView2.setLayoutParams(lp2);
//            headerView2.setTextSize(12);
//            headerRow.addView(headerView2);
//
//            TextView headerView1 = new TextView(PrintVoucher.this);
//            headerView1.setGravity(Gravity.CENTER);
//            headerView1.setText("السلعة");
//            headerView1.setLayoutParams(lp3);
//            headerView1.setTextSize(12);
//            headerRow.addView(headerView1);
//
//            tabLayout.addView(headerRow);
//        } else {
//            final TableRow headerRow = new TableRow(PrintVoucher.this);
//            TextView headerView1 = new TextView(PrintVoucher.this);
//
//            TextView headerView6 = new TextView(PrintVoucher.this);
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("المجموع");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(PrintVoucher.this);
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("الخصم");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(PrintVoucher.this);
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("المجاني");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(PrintVoucher.this);
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("سعر الوحدة");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(PrintVoucher.this);
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
//
//        for (int j = 0; j < items.size(); j++) {
//
//            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
//                final TableRow row = new TableRow(PrintVoucher.this);
//
//                if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
//
//                    for (int i = 0; i <= 7; i++) {
//                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(0, 10, 0, 0);
//                        row.setLayoutParams(lp);
//
//                        TextView textView = new TextView(PrintVoucher.this);
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
//                        TextView textView = new TextView(PrintVoucher.this);
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
//                                textView.setText(items.get(j).getQty()+"");
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
//                Log.e(" okButton"," okButton");
//                PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
//                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//                linearLayout.setDrawingCacheEnabled(true);
//                bitmap = linearLayout.getDrawingCache();
//                Log.e(" bitmap",bitmap+"");
//                photoPrinter.printBitmap("invoice.jpg", bitmap);
////////////////////////////////////////////////
//               /* linearView.setDrawingCacheEnabled(true);
//                linearView.buildDrawingCache();
//                Bitmap bit =linearView.getDrawingCache();
//                PrintHelper photoPrinter = new PrintHelper(PrintPayment.this);
//                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//                linearView.setDrawingCacheEnabled(true);
//                bitmap = linearView.getDrawingCache();
//                photoPrinter.printBitmap("pay.jpg", bitmap);
//                return bit;// creates bitmap and returns the same*/
//            }
//        });
//        dialog.show();
    }

    void findBT(int voucherNo,Voucher voucher) {
//        try {
//            /*  very important **********************************************************/
//            closeBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        itemsString = "";
        itemsString2="";
        for (int j = 0; j < items.size(); j++) {
//            ((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType()))
            if ((voucherNo == items.get(j).getVoucherNumber())&&(items.get(j).getVoucherType()== voucher.getVoucherType())) {
                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                amount = convertToEnglish(amount);

                String row = items.get(j).getItemName() + "                                             ";
                row = row.substring(0, 21) + items.get(j).getUnit() + row.substring(21, row.length());
                row = row.substring(0, 31) + items.get(j).getQty() + row.substring(31, row.length());
                row = row.substring(0, 41) + items.get(j).getPrice() + row.substring(41, row.length());
                row = row.substring(0, 52) +convertToEnglish(decimalFormat.format(Double.valueOf(amount)));
                row = row.trim();
                itemsString = itemsString + "\n" + row;

                String row2 = items.get(j).getItemName() + "                                             ";
                row2 = row2.substring(0, 21) + items.get(j).getUnit() + row2.substring(21, row2.length());
                row2 = row2.substring(0, 31) + items.get(j).getPrice() + row2.substring(31, row2.length());

                row2 = row2.substring(0, 42) +convertToEnglish(decimalFormat.format(Double.valueOf(amount)));
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
    void openBT(Voucher voucher, int casePrinter) throws IOException {
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
                    sendData(voucher);
                    break;
                case 2:
                    for(int i=0;i<settings.getNumOfCopy();i++)
                    {send_dataSewoo(voucher);}

                    break;
                case 3:
                    sendData2(voucher);
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


    void sendData(Voucher voucher) throws IOException {
        try {
            Log.e("send", "'yes");

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

            if (companyInfo != null) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();
                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();


                for (int i = 1; i <= numOfCopy; i++) {
                    Thread.sleep(1000);
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

                    if (companyInfo.getLogo() != null) {

//                        mmOutputStream.write(bitmapdata);

                    }

                    printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
                    printCustom("هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", 1, 2);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom("رقم الفاتورة : " + voucher.getVoucherNumber() + "   التاريخ: " + voucher.getVoucherDate() + "\n", 1, 2);
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
            } else
                Toast.makeText(PrintVoucher.this, " please enter company information", Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void send_dataSewoo(Voucher voucher) throws IOException {
        try {
            Log.e("send", "'yes");
            testB = convertLayoutToImage(voucher);

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


    void printTally(Voucher voucher) {
        pd = new SweetAlertDialog(PrintVoucher.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pd.setTitleText(PrintVoucher.this.getResources().getString(R.string.Printing));
        pd.setCancelable(false);
        pd.show();
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        List<Item> items1=new ArrayList<>();
        for (int j = 0; j < items.size(); j++) {

            if((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType())) {
                items1.add(items.get(j));

            }
        }

        Log.e("Items1__",""+items1.size()+"    "+(items1.size()<=17));

        if(items1.size()<=17){
             bitmap = convertLayoutToImageTally(voucher,1,0,items1.size(),items1);
            try {
                Settings settings = obj.getAllSettings().get(0);
                File file = savebitmap(bitmap, settings.getNumOfCopy(),"org");
                pd.dismissWithAnimation();
//                Log.e("save image ", "" + file.getAbsolutePath());
            } catch (IOException e) {
                pd.dismissWithAnimation();
                e.printStackTrace();
            }

        }else {

            Settings settings = obj.getAllSettings().get(0);
            for(int i=0;i<settings.getNumOfCopy();i++) {
                bitmap = convertLayoutToImageTally(voucher, 0,0,17,items1);
                bitmap2 = convertLayoutToImageTally(voucher, 1,17,items1.size(),items1);


                try {

                    File file = savebitmap(bitmap, 1,"fir"+""+i);
                    pd.dismissWithAnimation();
                    File file2 = savebitmap(bitmap2, 1,"sec"+""+i);

                    Log.e("save image ", "" + file.getAbsolutePath());
                } catch (IOException e) {
                    pd.dismissWithAnimation();
                    e.printStackTrace();
                }
            }

        }




    }

    public  File savebitmap(Bitmap bmp, int numCope,String next) throws IOException {
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
            try {
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            }
            catch (Exception e)
            {
                pd.dismissWithAnimation();
//                verifyStoragePermissions(PrintVoucher.this);


            }




        }

        return f;
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.e("permission",""+permission);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions( activity,
//                    new String[]{
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.MANAGE_EXTERNAL_STORAGE
//                    }, 1
//            );
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
        ActivityCompat.requestPermissions( activity,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );
    }
    public Bitmap convertLayoutToImage(Voucher voucher) {
        LinearLayout linearView = null;
        Log.e(" convertLayoutToImage", voucher.getVoucherNumber() +"");
        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.printdialog);
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
        discount.setText("" + voucher.getVoucherDiscount());///
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());


        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }
        for (int j = 0; j <20; j++)
            items.add( items.get( items.size()-1));


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp2.weight=1;
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < items.size(); j++) {

           if ((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType())) {
                final TableRow row = new TableRow(PrintVoucher.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintVoucher.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);

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
 //  linearView.layout(0, 0,1420, 1573);
        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());



   //   linearView.setDrawingCacheEnabled(true);
  //      linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FILL);
    linearView.setDrawingCacheEnabled(true);
 bitmap = linearView.getDrawingCache(true);
        //convertButton(bitmap );
    //    bitmap=  loadBitmapFromView( linearView,0,0);
        photoPrinter.printBitmap("pay.jpg", bitmap);


        return bit;// creates bitmap and returns the same
    }









    public Bitmap convertLayoutToImage2(Voucher voucher,ArrayList<Item>voch_items) {
        LinearLayout linearView = null;
        Log.e(" convertLayoutToImage2", voucher.getVoucherNumber() +"");
        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.printlay);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
//
//        compname = (TextView) dialogs.findViewById(R.id.compname);
//        tel = (TextView) dialogs.findViewById(R.id.tel);
//        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
//        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
//        date = (TextView) dialogs.findViewById(R.id.date);
//        custname = (TextView) dialogs.findViewById(R.id.custname);
//        note = (TextView) dialogs.findViewById(R.id.note);
//        vhType = (TextView) dialogs.findViewById(R.id.vhType);
//        paytype = (TextView) dialogs.findViewById(R.id.paytype);
//        total = (TextView) dialogs.findViewById(R.id.total);
//        discount = (TextView) dialogs.findViewById(R.id.discount);
//        tax = (TextView) dialogs.findViewById(R.id.tax);
//        ammont = (TextView) dialogs.findViewById(R.id.ammont);
      textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//
//        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(0, 100);
//        lp6.weight =1;
//        tabLayout.setLayoutParams(lp6);

//        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    dialogs.dismiss();
//                }
//            }
//        });


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
//        img.setImageBitmap(companyInfo.getLogo());
//        compname.setText(companyInfo.getCompanyName());
//        tel.setText("" + companyInfo.getcompanyTel());
//        taxNo.setText("" + companyInfo.getTaxNo());
//        vhNo.setText("" + voucher.getVoucherNumber());
//        date.setText(voucher.getVoucherDate());
//        custname.setText(voucher.getCustName());
//        note.setText(voucher.getRemark());
//        vhType.setText(voucherTyp);
//
//        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//        total.setText("" + voucher.getSubTotal());
//        discount.setText("" + voucher.getVoucherDiscount());///
//        tax.setText("" + voucher.getTax());
//        ammont.setText("" + voucher.getNetSales());


//        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
//            textW.setVisibility(View.GONE);
//        } else {
//            textW.setVisibility(View.VISIBLE);
//        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);


            for (int j = 0; j < voch_items.size(); j++) {

                 final TableRow row = new TableRow(PrintVoucher.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintVoucher.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(9);

                    switch (i) {
                        case 0:
                            textView.setText(voch_items.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + voch_items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + voch_items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + voch_items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + voch_items.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (voch_items.get(j).getQty() * voch_items.get(j).getPrice() - voch_items.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);

        }


//        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
 linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
// linearView.layout(0, 0,1420, 1573);
        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());




        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        linearView.setDrawingCacheEnabled(true);
        bitmap = linearView.getDrawingCache();

        photoPrinter.printBitmap("pay.jpg", bitmap);
   //  printheader(voucher);

        return bit;// creates bitmap and returns the same
    }

    public Bitmap printheader(Voucher voucher) {
        LinearLayout linearView = null;
        Log.e(" convertLayoutToImage", voucher.getVoucherNumber() +"");
        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.voch_header);
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
//        total = (TextView) dialogs.findViewById(R.id.total);
//        discount = (TextView) dialogs.findViewById(R.id.discount);
//        tax = (TextView) dialogs.findViewById(R.id.tax);
//        ammont = (TextView) dialogs.findViewById(R.id.ammont);
//        textW = (TextView) dialogs.findViewById(R.id.wa1);
//        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//
//        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(0, 100);
//        lp6.weight =1;
//        tabLayout.setLayoutParams(lp6);

//        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    dialogs.dismiss();
//                }
//            }
//        });


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

//        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//        total.setText("" + voucher.getSubTotal());
//        discount.setText("" + voucher.getVoucherDiscount());///
//        tax.setText("" + voucher.getTax());
//        ammont.setText("" + voucher.getNetSales());


//        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
//            textW.setVisibility(View.GONE);
//        } else {
//            textW.setVisibility(View.VISIBLE);
//        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);




//        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
        //linearView.layout(0, 0,1420, 1573);
        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());




        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        linearView.setDrawingCacheEnabled(true);
        bitmap = linearView.getDrawingCache();
        photoPrinter.printBitmap("pay.jpg", bitmap);

        return bit;// creates bitmap and returns the same
    }
    public Bitmap printfooter(Voucher voucher) {
        LinearLayout linearView = null;
        Log.e(" convertLayoutToImage", voucher.getVoucherNumber() +"");
        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.voch_footer);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
   //     doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;






        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//
//        LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(0, 100);
//        lp6.weight =1;
//        tabLayout.setLayoutParams(lp6);

//        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    dialogs.dismiss();
//                }
//            }
//        });


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

        total.setText("" + voucher.getSubTotal());
        discount.setText("" + voucher.getVoucherDiscount());///
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());


//        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
//            textW.setVisibility(View.GONE);
//        } else {
//            textW.setVisibility(View.VISIBLE);
//        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);




//        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
        //linearView.layout(0, 0,1420, 1573);
        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());




        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(PrintVoucher.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        linearView.setDrawingCacheEnabled(true);
        bitmap = linearView.getDrawingCache();
        photoPrinter.printBitmap("pay.jpg", bitmap);


        return bit;// creates bitmap and returns the same
    }
    public Bitmap convertLayoutToImageW() {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(true);
        dialogs.setCanceledOnTouchOutside(true);
        dialogs.setContentView(R.layout.printdialog);
//            fill_theVocher( voucherPrint);


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
//


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


        String voucherTyp = "";
        switch (voucherPrint.getVoucherType()) {
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
        vhNo.setText("" + voucherPrint.getVoucherNumber());
        date.setText(voucherPrint.getVoucherDate());
        custname.setText(voucherPrint.getCustName());
        note.setText(voucherPrint.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucherPrint.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucherPrint.getSubTotal());
        discount.setText("" + voucherPrint.getVoucherDiscount());///
        tax.setText("" + voucherPrint.getTax());
        ammont.setText("" + voucherPrint.getNetSales());


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

            if (voucherPrint.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(PrintVoucher.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintVoucher.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);

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


        dialogs.show();


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

    private Bitmap convertLayoutToImageTally(Voucher voucher,int okShow,int start,int end,List<Item>items) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(PrintVoucher.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.printdialog_tally);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW,noteLast;
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
        noteLast =(TextView) dialogs.findViewById(R.id.notelast);
        TableRow sing=(TableRow) dialogs.findViewById(R.id.sing);
//

        if(okShow==0){
            sumLayout.setVisibility(View.GONE);
            noteLast.setVisibility(View.GONE);
            sing.setVisibility(View.GONE);
        }else{
            sumLayout.setVisibility(View.VISIBLE);
            noteLast.setVisibility(View.VISIBLE);
            sing.setVisibility(View.VISIBLE);
        }
        img.setVisibility(View.INVISIBLE);
        compname.setVisibility(View.INVISIBLE);


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
        try {
            img.setImageBitmap(companyInfo.getLogo());
        }
        catch (Exception e)
        {}
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
        discount.setText("" + voucher.getTotalVoucherDiscount());
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

        for (int j = start; j <end; j++) {

            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(PrintVoucher.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintVoucher.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);
                    Log.e("LayoutParams",""+items.get(j).getItemName());

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


//       dialogs.show();


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

    private Bitmap convertLayoutToImageTallySub(Voucher voucher) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(PrintVoucher.this);
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
                final TableRow row = new TableRow(PrintVoucher.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(PrintVoucher.this);
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

    void sendData2(Voucher voucher) throws IOException {
        try {

            double totalQty = 0;
            double totalPrice = 0;
            double totalDisc = 0;
            double totalNet = 0;
            double totalTax = 0;
            double totalTotal = 0;

            int numOfCopy = obj.getAllSettings().get(0).getNumOfCopy();
            Log.e("nocopy", "" + numOfCopy);
            CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

            if (!companyInfo.getCompanyName().equals("")&& !companyInfo.getcompanyTel().equals("0")&& !companyInfo.getLogo().equals(null)&&companyInfo.getTaxNo()!=0) {
            pic.setImageBitmap(companyInfo.getLogo());
            pic.setDrawingCacheEnabled(true);
            bitmap = pic.getDrawingCache();
            PrintPic printPic = PrintPic.getInstance();
            printPic.init(bitmap);
            byte[] bitmapdata = printPic.printDraw();


                for (int i = 1; i <= numOfCopy; i++) {
                    Thread.sleep(1000);
                    //   printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);

                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
                    }

                    printCustom(companyInfo.getCompanyName() + " \n ", 1, 1);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("التاريخ        " + voucher.getVoucherDate() + " : " + " \n ", 1, 0);
                    printCustom("رقم الفاتورة   " + voucher.getVoucherNumber() + " : " + "\n", 1, 0);
                    printCustom("رقم العميل     " + voucher.getCustNumber() + " : " + "\n", 1, 0);
                    printCustom("اسم العميل " + " : " + voucher.getCustName() + "\n", 1, 0);
                    printCustom("مندوب المبيعات " + voucher.getSaleManNumber() + " : " + "\n", 1, 0);
                    printCustom("------------------------------------------" + "\n", 1, 0);

                    int serial = 1;
                    DecimalFormat threeDForm = new DecimalFormat("00.000");
                    for (int j = 0; j < items.size(); j++) {
                        if((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType())) {

                            String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                            String amountATax = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc() + items.get(j).getTaxValue());
                            amount = convertToEnglish(amount);
                            amountATax = convertToEnglish(amountATax);

                            printCustom("(" + serial + "" + "\n", 1, 0);
                            printCustom("رقم الصنف " + items.get(j).getItemNo() + " : " + " \n ", 1, 0);
                            printCustom("الصنف " + " : " + items.get(j).getItemName() + " \n ", 1, 0);
                            printCustom("الكمية    " + items.get(j).getQty() + " : " + " \n ", 1, 0);
                            printCustom("المجاني    " + items.get(j).getBonus() + " : " + " \n ", 1, 0);
                            printCustom("السعر     " + " JD " + items.get(j).getPrice() + " : " + " \n ", 1, 0);
                            printCustom("الخصم     " + " JD " + items.get(j).getDisc() + " : " + " \n ", 1, 0);
                            printCustom("الصافي    " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amount))) + " : " + "\n", 1, 0);
                            printCustom("الضريبة   " + " JD " + convertToEnglish(threeDForm.format(items.get(j).getTaxValue())) + " : " + " \n ", 1, 0);
                            printCustom("الاجمالي   " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amountATax))) + " : " + " \n ", 1, 0);

                            printCustom("* * * * * * * * * * * * * " + " \n ", 1, 0);

                            serial++;
                            totalQty += items.get(j).getQty() + items.get(j).getBonus();
                            totalPrice += items.get(j).getPrice();
                            totalDisc += items.get(j).getDisc();
                            totalNet += (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
                            totalTax += items.get(j).getTaxValue();
                            totalTotal += items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc() + items.get(j).getTaxValue();
                        }
                    }


                    printCustom("اجمالي الكمية  " + convertToEnglish("" + totalQty) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي السعر   " + " JD " + convertToEnglish(threeDForm.format(totalPrice)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الخصم   " + " JD " + convertToEnglish(threeDForm.format(totalDisc)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الصافي  " + " JD " + convertToEnglish(threeDForm.format(totalNet)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الضريبة " + " JD " + convertToEnglish(threeDForm.format(totalTax)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الإجمالي " + " JD " + convertToEnglish(threeDForm.format(totalTotal)) + " : " + " \n ", 1, 0);

                    if (voucher.getVoucherType() != 506) {
                        printCustom("استلمت البضاعة خالية من اي عيب او توالف" + " \n ", 1, 0);
                        printCustom("توقيع العميل" + "  " + "_______________" + " \n ", 1, 0);
                    }
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("\n", 1, 0);

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);

                }
                closeBT();



        }
            else{   Toast.makeText(PrintVoucher.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();}
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
        CPCLSample2 sample = new CPCLSample2(PrintVoucher.this);
        sample.selectGapPaper();
        try {
//            sample.printMultilingualFont(count);
            testB = convertLayoutToImage(voucher);

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
        private final ProgressDialog dialog = new ProgressDialog(PrintVoucher.this);

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

                Toast toast = Toast.makeText(PrintVoucher.this, "connect post", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("post alert", "post ,,,.", PrintVoucher.this);
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
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
//    public void convertButton(Context context,Bitmap bitmap){
//
//        String file = directory + "3.jpg";
//       //bitmap = BitmapFactory.decodeFile(file);
//
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(960,1280,1).create();
//        PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
//
//        page.getCanvas().drawBitmap(bitmap,0,0, null);
//        pdfDocument.finishPage(page);
//
//        String pdfFile = directory +"/myPDFFile_3.pdf";
//        File myPDFFile = new File(pdfFile);
//
//        try {
//            pdfDocument.writeTo(new FileOutputStream(myPDFFile));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        pdfDocument.close();
//        try
//        {
//            PrintManager printManager=(PrintManager) context.getSystemService(Context.PRINT_SERVICE);
//
//
//            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(android.provider.Settings.sharedPref.context,filePath );
//
//        }
//
//        catch (Exception e)
//        {
//            //   Logger.logError(e);
//        }
//
//    }
private ArrayList<Item> subArray(ArrayList<Item> A, int start,
                                       int end) {
    Log.e("start==",start+"");
    Log.e("end==",end+"");
    ArrayList toReturn = new ArrayList();
    for (int i = start; i <= end; i++) {
        toReturn.add(A.get(i));
    }
    return toReturn;
}
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

}


