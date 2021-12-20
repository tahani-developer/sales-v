package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
////import android.support.v7.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.LocaleAppUtils;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.PdfConverter;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.ReturnByVoucherNo;
import com.dr7.salesmanmanager.bMITP;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class VouchersReport extends AppCompatActivity {

    List<Voucher> vouchers;
    public static    Voucher   VocherToPrint;
    List<Item> items;
    public static   List<Item> itemsToPrint;
    TextView Customer_nameSales , textSubTotal , textTax , textNetSales;
    RadioGroup paymentTermRadioGroup, voucherTypeRadioGroup;
    EditText from_date, to_date, cust_number;
    Button preview;
    TableLayout TableTransactionsReport ;
    TableLayout TableItemInfo;
    Calendar myCalendar ;
    private DecimalFormat decimalFormat;
    int payMethod = 1;
    int voucherType = 504;

    double subTotal = 0 , tax = 0 , netSales = 0;
    int[] listImageIcone=new int[]{R.drawable.pdf_icon,R.drawable.excel_small};
   CompanyInfo companyInfo;
    GeneralMethod generalMethod;
    public static int type;
    DatabaseHandler obj;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(VouchersReport.this);
        setContentView(R.layout.vouchers_report);
       LinearLayout linearMain=findViewById(R.id.linearMain);
        itemsToPrint=new ArrayList<>();
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
        inflateBoomMenu();
        generalMethod=new GeneralMethod(VouchersReport.this);

        vouchers = new ArrayList<Voucher>();
        items = new ArrayList<Item>();

        obj   = new DatabaseHandler(VouchersReport.this);
        vouchers = obj.getAllVouchers();
        items = obj.getAllItems();

        TableTransactionsReport = (TableLayout) findViewById(R.id.TableTransactionsReport);
        Customer_nameSales = (TextView) findViewById(R.id.invoiceCustomerName);
        paymentTermRadioGroup = (RadioGroup) findViewById(R.id.paymentTermRadioGroup);
        voucherTypeRadioGroup = (RadioGroup) findViewById(R.id.transKindRadioGroup);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        cust_number = (EditText) findViewById(R.id.customer_number);
        preview = (Button) findViewById(R.id.preview);
        textSubTotal = (TextView) findViewById(R.id.subTotalTextView);
        textTax = (TextView) findViewById(R.id.taxTextView);
        textNetSales = (TextView) findViewById(R.id.netSalesTextView1);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        from_date.setText(today);
        to_date.setText(today);

        cust_number.requestFocus();

        myCalendar = Calendar.getInstance();

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(VouchersReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(VouchersReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        paymentTermRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.creditRadioButton: payMethod = 0; break;
                    case R.id.cashRadioButton: payMethod = 1; break;
                }
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
                if(!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {
                    subTotal = 0 ; tax = 0 ; netSales = 0;
                    type=voucherType;
                    for (int n = 0; n < vouchers.size(); n++) {

                        if (filters(n)) {

                            final TableRow row = new TableRow(VouchersReport.this);
                            row.setPadding(5, 5, 5, 5);

                            if (n % 2 == 0)
                                row.setBackgroundColor(ContextCompat.getColor(VouchersReport.this, R.color.layer7));
                            else
                                row.setBackgroundColor(ContextCompat.getColor(VouchersReport.this, R.color.layer5));

                            for (int i = 0; i < 9; i++) {
                                String[] record = {vouchers.get(n).getCustName() + "",
                                        vouchers.get(n).getVoucherNumber() + "",
                                        vouchers.get(n).getVoucherDate() + "",
                                        vouchers.get(n).getPayMethod() + "",
                                        vouchers.get(n).getRemark() + "",
                                        vouchers.get(n).getVoucherDiscount() + "",
                                        vouchers.get(n).getSubTotal() + "",
                                        vouchers.get(n).getTax() + "",
                                        vouchers.get(n).getNetSales() + ""

                                };


                                switch (vouchers.get(n).getPayMethod()) {
                                    case 0: record[3] = "credit"; break;
                                    case 1: record[3] = "cash"; break;
                                }


                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                if (i != 4) {
                                    TextView textView = new TextView(VouchersReport.this);
                                    textView.setText(record[i]);
                                    textView.setTextColor(ContextCompat.getColor(VouchersReport.this, R.color.colorPrimary));
                                    textView.setGravity(Gravity.CENTER);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    textView.setLayoutParams(lp2);

                                    row.addView(textView);

                                } else {
                                    TextView textView = new TextView(VouchersReport.this);
                                    textView.setText(getResources().getString(R.string.show));

                                    textView.setTextColor(ContextCompat.getColor(VouchersReport.this, R.color.colorblue_dark));
                                    textView.setBackgroundColor(ContextCompat.getColor(VouchersReport.this, R.color.white));
                                    TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    textView.setLayoutParams(lp3);
                                    textView.setGravity(Gravity.CENTER);

                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView textView = (TextView) row.getChildAt(1);
                                            voucherInfoDialog(Integer.parseInt(textView.getText().toString()) , voucherType);
                                        }
                                    });
                                    row.addView(textView);
                                }
                            }

                            subTotal = subTotal + vouchers.get(n).getSubTotal();
                            tax = tax + vouchers.get(n).getTax() ;
                            netSales = netSales + vouchers.get(n).getNetSales() ;

                            TableTransactionsReport.addView(row);
                        }

                    }

                    textSubTotal.setText(subTotal+"");
                    textTax.setText( convertToEnglish(decimalFormat.format(tax)));
                    textNetSales.setText( convertToEnglish(decimalFormat.format(netSales)));

                } else
                    Toast.makeText(VouchersReport.this, "Please fill the requested fields", Toast.LENGTH_LONG).show();
            }
        });

//        preview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
//                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
//                }
//                return false;
//            }
//        });

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    private void inflateBoomMenu() {
        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_2);
//        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();


        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])

                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    exportToPdf();

                                    break;
                                case 1:
                                    exportToEx();
                                    break;


                            }
                        }
                    }));
//            bmb.addBuilder(builder);


        }
    }
    private void exportToEx() {
        ExportToExcel exportToExcel=new ExportToExcel();
        exportToExcel.createExcelFile(VouchersReport.this,"VouchersReport.xls",3,vouchers);

    }
    public  void exportToPdf(){
        Log.e("exportToPdf",""+vouchers.size());
        PdfConverter pdf =new PdfConverter(VouchersReport.this);
        pdf.exportListToPdf(vouchers,"VouchersReport",from_date.getText().toString(),3);
    }

    public void voucherInfoDialog(int voucherNumber , int voucherType) {

        final Dialog dialog = new Dialog(VouchersReport.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.voucher_info_dialog2);
        Window window = dialog.getWindow();
        EditText VochNum,PayMth,CusName,VochDate;
        TextView subTotal,tax,netSales;
        subTotal=dialog.findViewById(R.id.subTotalTextView);
        tax=dialog.findViewById(R.id.taxTextView);
        netSales=dialog.findViewById(R.id.netSalesTextView1);
        VochNum =dialog.findViewById(R.id.vochernum);
                PayMth=dialog.findViewById(R.id.paymethod);
        CusName=dialog.findViewById(R.id.customername);
                VochDate=dialog.findViewById(R.id.vocherdate);
        dialog.findViewById(R.id.Print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLayout(obj);
            }
        });
//        window.setLayout(800, 400);


        for (int k = 0; k < vouchers.size(); k++) {
if( voucherNumber == vouchers.get(k).getVoucherNumber() &&
                    voucherType == vouchers.get(k).getVoucherType()) {
VocherToPrint=vouchers.get(k);
    VochNum.setText(vouchers.get(k).getVoucherNumber() + "");
  if(vouchers.get(k).getPayMethod()==1)
      PayMth.setText(getResources().getString(R.string.cash)+"");
  else  if(vouchers.get(k).getPayMethod()==0)
      PayMth.setText(getResources().getString(R.string.credit)+"");
        CusName.setText(vouchers.get(k).getCustName());
    VochDate.setText(vouchers.get(k).getVoucherDate() + "");
    subTotal.setText(vouchers.get(k).getSubTotal() + "");

    tax.setText(vouchers.get(k).getTax() + "");
    netSales.setText(vouchers.get(k).getNetSales() + "");

}
        }




        TableItemInfo = (TableLayout) dialog.findViewById(R.id.TableItemsInfo1);

        for (int k = 0; k < items.size(); k++) {

            if (voucherNumber == items.get(k).getVoucherNumber() && voucherType == items.get(k).getVoucherType()) {

                itemsToPrint.add(items.get(k));
                TableRow row = new TableRow(VouchersReport.this);
                row.setPadding(5, 10, 5, 10);

                if (k % 2 == 0)
                    row.setBackgroundColor(ContextCompat.getColor(VouchersReport.this, R.color.layer7));
                else
                    row.setBackgroundColor(ContextCompat.getColor(VouchersReport.this, R.color.layer5));

                for (int i = 0; i < 5; i++) {
                    String[] record = {
                            items.get(k).getItemNo() + "",
                            items.get(k).getItemName() + "",
                            items.get(k).getQty() + "",
                            items.get(k).getBonus() + "",
                            ""};

                    double calTotalSales = (items.get(k).getQty() * items.get(k).getPrice()) - items.get(k).getDisc();
                    record[4] = convertToEnglish(generalMethod.getDecimalFormat(calTotalSales ));

                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(VouchersReport.this);
                    textView.setText(record[i]);
                    textView.setTextSize(12);
                    textView.setTextColor(ContextCompat.getColor(VouchersReport.this, R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);
                }

                TableItemInfo.addView(row);
            }
        }
        Button okButton = (Button) dialog.findViewById(R.id.button11);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void clear (){
        int childCount = TableTransactionsReport.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            TableTransactionsReport.removeViews(1, childCount - 1);
            textSubTotal.setText("0.000");
            textTax.setText("0.000");
            textNetSales.setText("0.000");
        }
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

    public  Date formatDate (String date) throws ParseException {

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date d = sdf.parse(date);
            return d ;
    }

    public boolean filters (int n){


        String fromDate = from_date.getText().toString().trim();
        String toDate = to_date.getText().toString();

        String customerNumber = vouchers.get(n).getCustNumber() ;
        String date = vouchers.get(n).getVoucherDate() ;
        int vType = vouchers.get(n).getVoucherType() ;
        int pMethod = vouchers.get(n).getPayMethod() ;

        try {
            if (!cust_number.getText().toString().equals("")) {
                String textCompanyNumber =cust_number.getText().toString();
                if ((customerNumber.contains(textCompanyNumber) )&&
                        (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))) &&
                        vType == voucherType && pMethod == payMethod)
                    return true;
            } else {
                Log.e("tag" , "*****" + date + "***" + fromDate);
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))) &&
                        vType == voucherType && pMethod == payMethod)
                    return true;
            }

        } catch (ParseException e) {  e.printStackTrace(); }

        return false ;
    }
    private void printLayout(DatabaseHandler dataBase) {
        // getVoucherLocal();
        try{
            if (dataBase.getAllSettings().get(0).getPrintMethod() == 0) {

                try {
                    int printer = dataBase.getPrinterSetting();
                    companyInfo = dataBase.getAllCompanyInfo().get(0);
                    if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                        if (printer != -1) {
                            switch (printer) {
                                case 0:
//
//                                    Intent i = new Intent(ReturnByVoucherNo.this, BluetoothConnectMenu.class);
//                                    i.putExtra("printKey", "11");
//                                    startActivity(i);
                                    break;
//                                                             lk30.setChecked(true);

                                case 1:

//                                    try {
//                                        findBT();
//                                        openBT(1);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                                             lk31.setChecked(true);

                                case 2:

//                                        try {
//                                            findBT();
//                                            openBT(2);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                                             lk32.setChecked(true);

//                                    convertLayoutToImage();

//                                    Intent O1= new Intent(InventoryReport.this, bMITP.class);
//                                    O1.putExtra("printKey", "9");
//                                    startActivity(O1);


                                case 3:

//                                    try {
//                                        findBT();
//                                        openBT(3);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                                             qs.setChecked(true);

                                case 4:
//                                    Intent O= new Intent(ReturnByVoucherNo.this, bMITP.class);
//                                    O.putExtra("printKey", "11");
//                                    startActivity(O);

                                case 5:
//                                    convertLayoutToImage();
//                                    Intent O= new Intent(ReturnByVoucherNo.this, bMITP.class);
//                                    O.putExtra("printKey", "11");
//                                    startActivity(O);
                                case 6:
//                                    convertLayoutToImage();
                                    Intent O1= new Intent(VouchersReport.this, bMITP.class);
                                    O1.putExtra("printKey", "12");
                                    startActivity(O1);
                                    break;


                            }
                        } else {
                            Toast.makeText(VouchersReport.this, "please chose printer setting", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(VouchersReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(VouchersReport.this, "Please set Printer Setting", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Toast.makeText(VouchersReport.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(VouchersReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();

                }
            } else {
                // hiddenDialog();
            }
        }
        catch(Exception e){
            Toast.makeText(VouchersReport.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
        }

    }
}
