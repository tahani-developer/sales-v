package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.annotation.RequiresApi;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Cheque;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Settings;
import com.ganesh.intermecarabic.Arabic864;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.ByteArrayInputStream;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.SalesInvoice.voucherForPrint;
import static java.util.Calendar.JANUARY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptVoucher extends Fragment {
    private CircleImageView rePrintimage;
    private static DatabaseHandler mDbHandler;
    private int voucherNumber;
    PrintPic printPic;
    Bitmap testB;
    byte[] printIm;
    Context context;
    int position = 0;
    public static List<Payment> payments;
    public static List<Payment> paymentsforPrint;
    Animation animZoomIn;
    private LinearLayout chequeLayout,linear_checkNo,mailLinearRecipt;
    private ScrollView scrollView;
    private Spinner paymentKindSpinner;
    private ImageView pic;
    private ImageButton custInfoImgButton, clearImgButton, saveData;
    private Button addCheckButton;
    private TextView voucherNo, paymentTerm;
    EditText chequNo_EditText;
    int voucherType = 1;


    private double total = 0.0;

    String itemsString = "";
    boolean isFinishPrint = false;

    private EditText amountEditText, remarkEditText;
    private TableLayout tableCheckData;
    TableRow tableHeader;
    Calendar myCalendar;
    String chequamount="";
    double totalAmount=0;

    private TextView chequeTotal;

    public static TextView customername;

    public static Payment payment;


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
    int voucherNoReprint = 0;
    public static Payment payment_rePrint;

    public static List<Payment> paymentPrinter;
    static Payment pay1;
    public  static  boolean isArabicBank=false;
    String checkValue="";
    int  amountValue=0;
    RecyclerView recycler_check;
    FloatingActionButton editChech;
    int checkNo=0;// to count the cheks for  different data  display
    int serialNo=0;//it is the serial no entered by the user
    double curentAmount=0;
    double valuecheck=0;
    String previusDate="";
    Date currentTimeAndDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String today = df.format(currentTimeAndDate);
    Date dateConverter;
    int id_Bank;
    int[] listImageIcone=new int[]{ R.drawable.ic_save_black_24dp,R.drawable.ic_print_white_24dp,R.drawable.ic_delete_forever_black_24dp };

    String[] textListButtons=new String[]{};
    public interface ReceiptInterFace {
        public void displayCustInfoFragment();
    }

    ReceiptInterFace receiptInterFace;



    public ReceiptVoucher() {
       // this.context=getContext();
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_receipt_voucher, container, false);
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDbHandler = new DatabaseHandler(getActivity());
        payments = new ArrayList<Payment>();
        mailLinearRecipt = (LinearLayout) view.findViewById(R.id.mailLinearRecipt);
        try {
            if (languagelocalApp.equals("ar")) {
                mailLinearRecipt.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    mailLinearRecipt.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
}
        catch (Exception e){
            mailLinearRecipt.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);}

        chequeLayout = (LinearLayout) view.findViewById(R.id.cheques_totals);
        linear_checkNo= (LinearLayout) view.findViewById(R.id.linear_checkNo);
        paymentKindSpinner = (Spinner) view.findViewById(R.id.paymentTypeSpinner);
        custInfoImgButton = (ImageButton) view.findViewById(R.id.custInfoImgBtn);
        saveData = (ImageButton) view.findViewById(R.id.SaveData);
        clearImgButton = (ImageButton) view.findViewById(R.id.ClearForm);
        scrollView = (ScrollView) view.findViewById(R.id.chequesScroll);
        chequeTotal = (TextView) view.findViewById(R.id.chequesTotalsEditText);
        amountEditText = (EditText) view.findViewById(R.id.amountEditText);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        customername = (TextView) view.findViewById(R.id.customer_nameVoucher);
        voucherNo = (TextView) view.findViewById(R.id.voucher_no);
        chequNo_EditText= (EditText) view.findViewById(R.id.chequNo_EditText);
        paymentTerm = (TextView) view.findViewById(R.id.payment_term);
        addCheckButton = (Button) view.findViewById(R.id.addCheck);
        animZoomIn = (Animation) AnimationUtils.loadAnimation(this.getActivity().getBaseContext(), R.anim.zoom_in);
        tableCheckData = (TableLayout) view.findViewById(R.id.TableCheckData);
        tableHeader= (TableRow) view.findViewById(R.id.tableHeader);
        pic = (ImageView) view.findViewById(R.id.pic_receipt);
        paymentsforPrint = new ArrayList<>();
        rePrintimage = (CircleImageView) view.findViewById(R.id.pic_Re_print);
        editChech= (FloatingActionButton) view.findViewById(R.id.edit_floating);
        editChech.setOnClickListener(onClickEditCheck);
        textListButtons=new String[]{getResources().getString(R.string.save),getResources().getString(R.string.print),getResources().getString(R.string.delet)};

        //*************************** BoomMenu*******************************
        BoomMenuButton bmb = (BoomMenuButton)view.findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_2);



        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
//            bmb.addBuilder(new SimpleCircleButton.Builder()
//                    .normalImageRes(listImageIcone[i]));
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])
                    .textSize(8)
                    .normalText(textListButtons[i])
                    .textPadding(new Rect(5, 16, 5, 0))
                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    saveChecks();
                                    break;
                                case 1:
                                    try {
                                        voucherNoReprint = mDbHandler.getLastVoucherNo_payment();
                                        Log.e("voucherNoRe", "" + voucherNoReprint);
                                        if (voucherNoReprint != 0) {
                                            printLastPaymentpaper(voucherNoReprint);
                                        } else {
                                            Toast.makeText(context, "No Payment For This Customer", Toast.LENGTH_SHORT).show();
                                        }

//
                                    } catch (Exception e) {
                                        Log.e("ExcepvoucherNoRe", "" + e.getMessage());
                                        voucherNoReprint = 0;
                                    }
                                    break;
                                case 2:
                                    clearDialog();
                                    break;

                            }
                        }
                    });
            bmb.addBuilder(builder);


        }
        //**********************************************************
//
//        vouchers = obj.getAllVouchers();

        rePrintimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    voucherNoReprint = mDbHandler.getLastVoucherNo_payment();
                    Log.e("voucherNoRe", "" + voucherNoReprint);
                    if (voucherNoReprint != 0) {
                        printLastPaymentpaper(voucherNoReprint);
                    } else {
                        Toast.makeText(context, "No Payment For This Customer", Toast.LENGTH_SHORT).show();
                    }

//
                } catch (Exception e) {
                    Log.e("ExcepvoucherNoRe", "" + e.getMessage());
                    voucherNoReprint = 0;
                }
            }
        });

        voucherNumber = mDbHandler.getMaxSerialNumber(1) + 1;//for test 1
        Log.e("voucherNumber", "onCreateView" +voucherNumber);
        voucherNo.setText(getResources().getString(R.string.payment_number) + " : " + voucherNumber);

        String payMethod = "";
        switch (CustomerListShow.paymentTerm) {
            case 0:
                payMethod = getResources().getString(R.string.app_credit);
                break;
            case 1:
                payMethod = getResources().getString(R.string.cash);

                break;
        }

        if (MainActivity.checknum == 1) {
            customername.setText(getResources().getString(R.string.cust_name) + " : " + CustomerListShow.Customer_Name);
            paymentTerm.setText(getResources().getString(R.string.payment_term) + " : " + payMethod);
        } else {
            customername.setText(getResources().getString(R.string.cust_name));
            paymentTerm.setText(getResources().getString(R.string.payment_term));
        }

        addCheckButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {


                     chequamount=amountEditText.getText().toString();



                   if(!TextUtils.isEmpty( chequamount)) {
                       try {
                           totalAmount=Double.parseDouble(chequamount);

                       }catch (Exception e)
                       {
                           amountEditText.setError("Error");

                       }

                       if (mDbHandler.getAllSettings().get(0).getAutomaticCheque() == 1) {

                           editChech.setVisibility(View.GONE);
                           displayManulaCheck();
//                           editChech.setVisibility(View.VISIBLE);
//                          if(! TextUtils.isEmpty( Counter)){
//                              try {
//
//                                  counter = Integer.parseInt(chequCounter);
//                                  chequamountValue = Float.parseFloat(chequamount);
//
//                              } catch (NumberFormatException e) {
//                                  chequNo_EditText.setError("Error Input");
//                                  Log.e("NumberFormatException", "" + e.getMessage());
//                                  counter = 0;
//
//                              }
//                              if(counter!=0)
//                              {
//                                 // displayAutoCheck(counter,1);// just for ejabi
//
//                              }
//                              else {
//                                  chequNo_EditText.setError("Error Zero Value");
//
//                              }
//                          }
//                          else{
//                              chequNo_EditText.requestFocus();
//                              chequNo_EditText.setError("Required");
//
//                          }


                       }// end automatic check
                       else{
                           editChech.setVisibility(View.GONE);
                           displayManulaCheck();

                       }
                   }

                       else {
                           amountEditText.setError("Required");
                           amountEditText.requestFocus();
                       }




            } // end of button preview

        });

        if (MainActivity.checknum == 1)
            customername.setText(CustomerListShow.Customer_Name.toString());
        else
            customername.setText("Customer Name");


        ArrayList<String> kinds = new ArrayList<>();
        kinds.add(getResources().getString(R.string.cash));
        kinds.add(getResources().getString(R.string.app_cheque));
        kinds.add(getResources().getString(R.string.app_creditCard));
        ArrayAdapter<String> paymentKind = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, kinds);
        paymentKindSpinner.setAdapter(paymentKind);
        clearImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearImgButton.setAnimation(animZoomIn);
                clearDialog();

            }
        });


        saveData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveChecks();
            }
        });


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.custInfoImgBtn:
                        receiptInterFace.displayCustInfoFragment();
                        break;
                }
            }
        };

        // custInfoImgButton.setOnClickListener(onClickListener);

        paymentKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i==2) {//cash
                    chequeLayout.setVisibility(View.INVISIBLE);
                    linear_checkNo.setVisibility(View.GONE);
                    scrollView.setVisibility(View.INVISIBLE);
                    addCheckButton.setVisibility(View.INVISIBLE);
                    tableCheckData.setVisibility(View.INVISIBLE);
                    tableHeader.setVisibility(View.INVISIBLE);
                    if(i==0)
                    {
                        voucherType = 1;//cash
                    }
                    else if (i==2)
                    {
                        voucherType = 2;//credit

                    }


                    voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                 //for test 1
                    voucherNo.setText(getResources().getString(R.string.payment_number) + " : " + voucherNumber);
                }


                else {// cheque payment
                    if(mDbHandler.getAllSettings().get(0).getAutomaticCheque()==1)//for ejabi checque
                    {
                                            linear_checkNo.setVisibility(View.VISIBLE);
//                        displaychequeTable();
                        displaychequeTable();
                        linear_checkNo.setVisibility(View.GONE);

                    }
                    else{
                        displaychequeTable();
                        linear_checkNo.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void clearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
        builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearForm();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
        builder.create().show();
    }

    private void saveChecks() {
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));

        builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int l) {

                String s = amountEditText.getText().toString();
                String spinner = paymentKindSpinner.getSelectedItem().toString();
                if (spinner == getResources().getString(R.string.cash)) {
                    if (!s.equals("") && Double.parseDouble(s) != 0) {
                        voucherType = 1;
                        saveAmount(voucherType);

                    } else {
                        Toast.makeText(getActivity(), "Please Enter amount value", Toast.LENGTH_LONG).show();
                    }
                } else if (spinner == getResources().getString(R.string.app_creditCard)) {
                    if (!s.equals("") && Double.parseDouble(s) != 0) {
                        voucherType = 2;
                        saveAmount(voucherType);

                    } else {
                        Toast.makeText(getActivity(), "Please Enter amount value", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!checkValue())
                        Toast.makeText(getActivity(), "Amount Value not matches Cheque Total", Toast.LENGTH_SHORT).show();
                    else {

                        saveChequ();

//                                if(!Login.salesMan.equals(""))
//                                {

//                                }
//                                else{
//                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                                            .setTitleText(getResources().getString(R.string.warning_message))
//                                            .setContentText(getResources().getString(R.string.pleaseSelectUser))
//                                            .show();
//                                }


                    }
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
        builder.create().show();

    }

    private View.OnClickListener onClickEditCheck=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(tableCheckData.getChildCount()>0) {
                List<Cheque> Listitems_adapter = new ArrayList<>();
                Listitems_adapter = ((CheckAdapter) recycler_check.getAdapter()).list;


                displayEditCheck(Listitems_adapter);
            }
            else{
                Toast.makeText(getActivity(), "Must have data", Toast.LENGTH_SHORT).show();
            }


        }
    };


    @SuppressLint("RestrictedApi")
    private void displayManulaCheck() {
        editChech.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.check_info_dialog);
        Window window = dialog.getWindow();

        final EditText chNum = (EditText) dialog.findViewById(R.id.editText1);
        final Spinner bank = (Spinner) dialog.findViewById(R.id.editText2);
        final EditText chDate = (EditText) dialog.findViewById(R.id.editText3);
        final EditText chValue = (EditText) dialog.findViewById(R.id.editText4);
        chNum.requestFocus();
        chValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chValue.selectAll();

            }
        });
        if(checkNo!=0)
        {
            chNum.setText((++serialNo)+"");
            chValue.requestFocus();
            chValue.setText((totalAmount-curentAmount)+"");
            bank.setSelection( id_Bank);
            df = new SimpleDateFormat( "dd/MM/yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                 dateConverter = sdf.parse(previusDate);
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
            try {
                currentTimeAndDate=addOneMonth(dateConverter);
                today = df.format(currentTimeAndDate);
                chDate.setText(convertToEnglish(today));
            }
            catch ( Exception e)
            {

            }

        }

        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);

        myCalendar = Calendar.getInstance();
        chDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), openDatePickerDialog(chDate), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TableRow row = new TableRow(getActivity());
                row.setPadding(5, 5, 5, 5);

                if (checkDialogFields(chNum.getText().toString(), bank.getSelectedItem().toString(),
                        chDate.getText().toString(), chValue.getText().toString())) {
                    if ((Double.parseDouble(chValue.getText().toString()) > 0.0)) {

                        if (checkTotal(chValue.getText().toString())) {
                            checkNo++;
                            total = total + Double.parseDouble(chValue.getText().toString());
                            chequeTotal.setText(convertToEnglish(new DecimalFormat("##.###").format(total)) + "");
                            Payment check = new Payment();
                            check.setCheckNumber(Integer.parseInt(chNum.getText().toString()));
                            serialNo=(Integer.parseInt(chNum.getText().toString()));
                            check.setBank(bank.getSelectedItem().toString());
                            check.setDueDate(chDate.getText().toString());
                            previusDate=chDate.getText().toString();
                            check.setAmount(Double.parseDouble(chValue.getText().toString()));
                            valuecheck=Double.parseDouble(chValue.getText().toString());
                            curentAmount+=valuecheck;
                            id_Bank= bank.getSelectedItemPosition();

                            payments.add(check);
                            paymentsforPrint.add(check);
                            row.setTag(position);
                            for (int i = 0; i < 4; i++) {

                                String[] record = {chNum.getText().toString(), bank.getSelectedItem().toString(),
                                        chDate.getText().toString(), chValue.getText().toString()};

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                TextView textView = new TextView(getActivity());

                                textView.setHint(record[i]);
                                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_view_color));
                                textView.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.text_view_color));
                                textView.setGravity(Gravity.CENTER);

                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                textView.setLayoutParams(lp2);

                                row.addView(textView);


                            }

                            row.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int tag = Integer.parseInt(row.getTag().toString());
                                            Log.e("tagDelet",""+tag+"\tpayments"+payments.size());
                                            payments.remove(tag);
                                            paymentsforPrint.remove(tag);
                                            tableCheckData.removeView(row);
                                            total = total - Double.parseDouble(chValue.getText().toString());
                                            curentAmount-=Double.parseDouble(chValue.getText().toString());
                                            chequeTotal.setText(convertToEnglish(new DecimalFormat("##.###").format(total)) + "");
                                            position--;
                                            for (int k = 0; k < tableCheckData.getChildCount(); k++) {
                                                TableRow tableRow = (TableRow) tableCheckData.getChildAt(k);
                                                tableRow.setTag(k);
                                            }
                                        }
                                    });

                                    builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                                    builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear_item));
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                    return true;
                                }
                            });
                            tableCheckData.addView(row);
                            position++;

                            dialog.dismiss();

                        } else {
                            Toast.makeText(getActivity(), "Cheque Total is greater than Amount value", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter Amount greater than  0 ", Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(getActivity(), "Please Enter all values", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNo--;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //@SuppressLint("RestrictedApi")
//    private void displayAutoCheck(int counter, int flag) {
//        editChech.setVisibility(View.VISIBLE);
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.auto_check_dialog);
//        Window window = dialog.getWindow();
//        final LinearLayoutManager layoutManager;
//        layoutManager = new LinearLayoutManager(this.getActivity());
//        layoutManager.setOrientation(VERTICAL);
//         recycler_check = (RecyclerView)dialog.findViewById(R.id.recycler_check);
//        List<Cheque> chequeListitems= new ArrayList<>();
//
//        chequeListitems = filllistOfCheque(counter);
//        Log.e("chequeListitems",""+chequeListitems.size());
//
//        recycler_check.setLayoutManager(layoutManager);
//        recycler_check.setAdapter(new CheckAdapter(chequeListitems, this.getActivity()));
//        Button okButton = (Button) dialog.findViewById(R.id.button_save);
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double totalCheque=0,amountTotal=0;
//                amountTotal=Double.parseDouble(amountEditText.getText().toString());
//
//                List<Cheque> Listitems_adapter= new ArrayList<>();
//                Listitems_adapter =  ((CheckAdapter) recycler_check.getAdapter()).list;
//                for(int h=0;h<Listitems_adapter.size();h++)
//                {
//                    totalCheque+=Listitems_adapter.get(h).getChequeValue();
//
//                }
//                totalCheque=Math.round(totalCheque);
//                if(totalCheque == amountTotal )
//                {
//                    tableCheckData.removeAllViews();
//                    total=0;
//                    payments.clear();
//                    paymentsforPrint.clear();
//                    for(int j=0;j<Listitems_adapter.size();j++)
//                    {
//                        fiiTable(Listitems_adapter,j);
//                    }
//                    chequeTotal.setText(Math.round(total)+"");
//                    dialog.dismiss();
//                }
//                else{
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                    builder.setTitle(getResources().getString(R.string.app_alert));
//                                    builder.setCancelable(true);
//                                    builder.setMessage("Cheques amount must be  equal to Total amount \t"+amountTotal);
//                                    builder.show();
//
//                }
//
//
//
//
//
//
//            }
//        });
//        Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);
//
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//
//    }
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    private void displayAutoCheck(int counter, int flag) {
        editChech.setVisibility(View.VISIBLE);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.auto_check_dialog);
        Window window = dialog.getWindow();
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(VERTICAL);
        recycler_check = (RecyclerView)dialog.findViewById(R.id.recycler_check);
        List<Cheque> chequeListitems= new ArrayList<>();

        chequeListitems = filllistOfCheque(1);
        Log.e("chequeListitems",""+chequeListitems.size());

        recycler_check.setLayoutManager(layoutManager);
        recycler_check.setAdapter(new CheckAdapter(chequeListitems, this.getActivity()));
        Button okButton = (Button) dialog.findViewById(R.id.button_save);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalCheque=0,amountTotal=0;
                amountTotal=Double.parseDouble(amountEditText.getText().toString());

                List<Cheque> Listitems_adapter= new ArrayList<>();
                Listitems_adapter =  ((CheckAdapter) recycler_check.getAdapter()).list;
                for(int h=0;h<Listitems_adapter.size();h++)
                {
                    totalCheque+=Listitems_adapter.get(h).getChequeValue();

                }
                totalCheque=Math.round(totalCheque);
                if(totalCheque == amountTotal )
                {
                    tableCheckData.removeAllViews();
                    total=0;
                    payments.clear();
                    paymentsforPrint.clear();
                    for(int j=0;j<Listitems_adapter.size();j++)
                    {
                        fiiTable(Listitems_adapter,j);
                    }
                    chequeTotal.setText(Math.round(total)+"");
                    dialog.dismiss();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_alert));
                    builder.setCancelable(true);
                    builder.setMessage("Cheques amount must be  equal to Total amount \t"+amountTotal);
                    builder.show();

                }






            }
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void fiiTable(final List<Cheque> listitems_adapter, final int j) {
                        final TableRow row = new TableRow(getActivity());
                row.setPadding(5, 5, 5, 5);

                if (listitems_adapter.size()!=0) {
                 {
                        {
                            total = total + Double.parseDouble(listitems_adapter.get(j).getChequeValue()+"");
                            chequeTotal.setText(convertToEnglish(new DecimalFormat("##.###").format(total)) + "");
                            Payment check = new Payment();
                            check.setCheckNumber(Integer.parseInt(listitems_adapter.get(j).getChequeNo()));
                            check.setBank(listitems_adapter.get(j).getBankName());
                            check.setDueDate(listitems_adapter.get(j).getChequeDate());
                            check.setAmount((listitems_adapter.get(j).getChequeValue()));
                            payments.add(check);
                            paymentsforPrint.add(check);

                            row.setTag(position);
                            for (int i = 0; i < 4; i++) {

                                String[] record = {listitems_adapter.get(j).getChequeNo(),listitems_adapter.get(j).getBankName(),
                                        listitems_adapter.get(j).getChequeDate(),( listitems_adapter.get(j).getChequeValue()+"")};

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                TextView textView = new TextView(getActivity());

                                textView.setHint(record[i]);
                                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_view_color));
                                textView.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.text_view_color));
                                textView.setGravity(Gravity.CENTER);

                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                textView.setLayoutParams(lp2);

                                row.addView(textView);


                            }

                            row.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if(mDbHandler.getAllSettings().get(0).getAutomaticCheque()==1){
////                                        displayAutoCheck(counter,2);
//                                        List<Cheque> Listitems_adapter= new ArrayList<>();
//                                        Listitems_adapter =  ((CheckAdapter) recycler_check.getAdapter()).list;
//
//                                        Log.e("Listitems_adapter",""+Listitems_adapter.size());
//                                        displayEditCheck(Listitems_adapter);
                                    }
                                    else {  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                int tag = Integer.parseInt(row.getTag().toString());
                                                Log.e("tagrow",""+tag);
                                                Log.e("payments",""+payments.size());
                                                payments.remove(tag - 1);
                                                tableCheckData.removeView(row);
                                                total = total - Double.parseDouble(listitems_adapter.get(j).getChequeValue()+"");
                                                chequeTotal.setText(convertToEnglish(new DecimalFormat("##.###").format(total)) + "");
                                                position--;
                                                for (int k = 0; k < tableCheckData.getChildCount(); k++) {
                                                    TableRow tableRow = (TableRow) tableCheckData.getChildAt(k);
                                                    tableRow.setTag(k);
                                                }
                                            }
                                        });

                                        builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                                        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear_item));
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();}



                                    return true;
                                }
                            });
                            tableCheckData.addView(row);
                            position++;



                        }

//                    } else {
//                        Toast.makeText(getActivity(), "Please Enter Amount greater than  0 ", Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(getActivity(), "Please Enter all values", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("WrongConstant")
    private void displayEditCheck(List<Cheque> Listitems) {
        Log.e("displayEditCheck",""+Listitems.size());
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.auto_check_dialog);
        Window window = dialog.getWindow();
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(VERTICAL);
        recycler_check = (RecyclerView)dialog.findViewById(R.id.recycler_check);
        List<Cheque> chequeListitems= new ArrayList<>();

        recycler_check.setLayoutManager(layoutManager);

        recycler_check.setAdapter(new CheckAdapter(Listitems, this.getActivity()));
        Button okButton = (Button) dialog.findViewById(R.id.button_save);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalCheque=0,amountTotal=0;
                amountTotal=Double.parseDouble(amountEditText.getText().toString());

                List<Cheque> Listitems_adapter= new ArrayList<>();
                Listitems_adapter =  ((CheckAdapter) recycler_check.getAdapter()).list;
                for(int h=0;h<Listitems_adapter.size();h++)
                {
                    totalCheque+=Listitems_adapter.get(h).getChequeValue();

                }
                totalCheque=Math.round(totalCheque);
                if(totalCheque == amountTotal )
                {
                    tableCheckData.removeAllViews();
                    total=0;
                    payments.clear();
                    paymentsforPrint.clear();
                    for(int j=0;j<Listitems_adapter.size();j++)
                    {
                        fiiTable(Listitems_adapter,j);
                    }
                    chequeTotal.setText(Math.round(total)+"");
                    dialog.dismiss();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_alert));
                    builder.setCancelable(true);
                    builder.setMessage("Cheques amount must be  equal to Total amount \t"+amountTotal);
                    builder.show();

                }






            }
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private List<Cheque> filllistOfCheque(int flag) {
        String today="";
        List <Cheque> chequeList=new ArrayList<>();
        String amount= amountEditText.getText().toString();
        float totalAmount=0;
        float valueOfOneCheck=0;
        try {

             totalAmount=Float.parseFloat(amount);
             valueOfOneCheck=totalAmount/counter;
        }
        catch (Exception e)
        {

        }
        // create date plus one
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        today = df.format(currentTimeAndDate);

        if(flag==1)
        {
            Cheque itemCheq=new Cheque();
            itemCheq.setChequeDate(today);
            itemCheq.setChequeNo("");
            itemCheq.setChequeSerial(1);
           // String valueOfOneCheck_str = convertToEnglish(new DecimalFormat("##.###").format(valueOfOneCheck));
           // valueOfOneCheck=Float.parseFloat(valueOfOneCheck_str);
            //itemCheq.setChequeValue(valueOfOneCheck);
            itemCheq.setChequeValue(1.0f);
            chequeList.add(itemCheq);
            //////////////////////////////////////////////////////
//            df = new SimpleDateFormat("dd/MM/yyyy");
//            currentTimeAndDate=addOneMonth(currentTimeAndDate);
//            today = df.format(currentTimeAndDate);

        }
//        for(int i=0;i<counter;i++)
//        {
//            Cheque itemCheq=new Cheque();
//            itemCheq.setChequeDate(today);
//            itemCheq.setChequeNo((int)totalAmount+""+(i+1));
//            itemCheq.setChequeSerial(i);
//            String valueOfOneCheck_str = convertToEnglish(new DecimalFormat("##.###").format(valueOfOneCheck));
//            valueOfOneCheck=Float.parseFloat(valueOfOneCheck_str);
//            itemCheq.setChequeValue(valueOfOneCheck);
//            chequeList.add(itemCheq);
//            //////////////////////////////////////////////////////
//             df = new SimpleDateFormat("dd/MM/yyyy");
//            currentTimeAndDate=addOneMonth(currentTimeAndDate);
//            today = df.format(currentTimeAndDate);
//        }

        return  chequeList;
    }
    private List<Cheque> getDataFromTableCheque() {

        List <Cheque> chequeList=new ArrayList<>();

        float valueOfOneCheck=0;


        for(int i=0;i<tableCheckData.getChildCount();i++)
        {  Cheque itemCheq = new Cheque();
            TableRow mRow = (TableRow) tableCheckData.getChildAt(i);
            TextView mTextView = (TextView) mRow.getChildAt(0);
            itemCheq.setChequeNo(mTextView.getText().toString());
                            Log.e("mTextView",""+mTextView.getText().toString());

//            for(int j=0;j<4;j++)
//            {
//                TextView mTextView = (TextView) mRow.getChildAt(j);
//                itemCheq.setChequeNo(mTextView.getText().toString());
//                Log.e("mTextView",""+mTextView.getText().toString());
//                itemCheq=new Cheque();
//                if(j==0)
//                {
//                    itemCheq.setChequeNo(mTextView.getText().toString());
//                }
//                if(j==1)
//                {
//                    itemCheq.setBankName(mTextView.getText().toString());
//                }
//                if(j==2)
//                {
//                    itemCheq.setChequeDate(mTextView.getText().toString());
//
//                }
//                if(j==3)
//                {
//                    //                valueOfOneCheck=Float.parseFloat(new DecimalFormat("##.###").format(mTextView.getText().toString()));
////                    itemCheq.setChequeValue(Float.parseFloat(mTextView.getText().toString()));
//
//                }
//
//
//
//            }



            chequeList.add(itemCheq);

        }

        return  chequeList;
    }
    public static Date addOneMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private void displaychequeTable() {
        chequeLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        addCheckButton.setVisibility(View.VISIBLE);
        tableCheckData.setVisibility(View.VISIBLE);
        tableHeader.setVisibility(View.VISIBLE);
        voucherType = 4;//chequ
        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        voucherNo.setText(getResources().getString(R.string.payment_number) + " : " + voucherNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveChequ() {
        int salesMan=0;
        Toast.makeText(getActivity(), "Amount Saved", Toast.LENGTH_LONG).show();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String payDate = convertToEnglish(df.format(currentTimeAndDate));

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
        String paymentYear = df2.format(currentTimeAndDate);

        String cusNumber = CustomerListShow.Customer_Account;
        String cusName = CustomerListShow.Customer_Name;
        Double amount = Double.parseDouble(amountEditText.getText().toString());
        String remark = remarkEditText.getText().toString();
        try {
             salesMan = Integer.parseInt(mDbHandler.getAllUserNo().toString().trim());
        }
        catch (Exception e)
        {

        }



        payment = new Payment(0, voucherNumber, salesMan, payDate,
                remark, amount, 0, cusNumber, cusName, 0, Integer.parseInt(paymentYear));
        mDbHandler.addPayment(payment);

        itemsString = "";
        for (int i = 0; i < payments.size(); i++) {
            mDbHandler.addPaymentPaper(new Payment(0, voucherNumber, payments.get(i).getCheckNumber(),
                    payments.get(i).getBank(), payments.get(i).getDueDate(), payments.get(i).getAmount(),
                    0, Integer.parseInt(paymentYear)));


            String row = ".                                             ";
            row = row.substring(0, 13) + payments.get(i).getCheckNumber() + row.substring(13, row.length());
            row = row.substring(0, 24) + payments.get(i).getDueDate() + row.substring(24, row.length());
            row = row.substring(0, 38) + payments.get(i).getAmount() + row.substring(38, row.length()) + "\n";
//                                    row = row.substring(0, 42) + payments.get(i).getAmount() + row.substring(42, row.length());
            row = row.trim();
            row += "\n" + " " + payments.get(i).getBank();//test + =
            isArabicBank=textContainsArabic(payments.get(0).getBank());
            Log.e("isArabicBank",""+isArabicBank+""+payments.get(0).getBank());

            itemsString = "\n" + itemsString + "\n" + row;

            mDbHandler.setMaxSerialNumber(4, voucherNumber);
        }


        int printer = mDbHandler.getPrinterSetting();

        switch (printer) {
            case 0:

                Intent i = new Intent(getActivity(), BluetoothConnectMenu.class);
                i.putExtra("printKey", "2");
                startActivity(i);

//                                                             lk30.setChecked(true);
                break;
            case 1:
                convertLayoutToImage(getActivity());
                Intent gg = new Intent(getActivity(), bMITP.class);
                gg.putExtra("printKey", "2");
                startActivity(gg);

//                try {
//                    findBT();
//                    openBT(1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
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

                convertLayoutToImage(getActivity());
                Intent O = new Intent(getActivity(), bMITP.class);
                O.putExtra("printKey", "2");
                startActivity(O);

                break;
            case 3:
                convertLayoutToImage(getActivity());
                Intent ll = new Intent(getActivity(), bMITP.class);
                ll.putExtra("printKey", "2");
                startActivity(ll);
//                try {
//                    findBT();
//                    openBT(3);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                                                             qs.setChecked(true);
                break;
            case 4:
                printTally();
                break;

            case 5://                                                             MTP.setChecked(true);

            case 6:// inner printer
                convertLayoutToImage(getActivity());
                Intent inte = new Intent(getActivity(), bMITP.class);
                inte.putExtra("printKey", "2");
                startActivity(inte);
                break;

        }


        clearForm();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveAmount(int voucherType) {


        Toast.makeText(getActivity(), "Amount Saved***", Toast.LENGTH_LONG).show();

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String payDate = convertToEnglish(df.format(currentTimeAndDate));


        SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
        String paymentYear = df2.format(currentTimeAndDate);

        String cusNumber = (CustomerListShow.Customer_Account);
        String cusName = CustomerListShow.Customer_Name;
        String remark = remarkEditText.getText().toString();
        // if(!(amountEditText.getText().toString().equals(""))) {
        Double amount = Double.parseDouble(amountEditText.getText().toString());


        int salesMan = Integer.parseInt(Login.salesMan);

        payment = new Payment(0, voucherNumber, salesMan, payDate,
                remark, amount, 0, cusNumber, cusName, voucherType, Integer.parseInt(paymentYear));
        mDbHandler.addPayment(payment);
        CompanyInfo companyInfo = new CompanyInfo();
        try {
            if (mDbHandler.getAllSettings().get(0).getSaveOnly() == 0) {// save and print
                int printer = mDbHandler.getPrinterSetting();
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {

                    switch (printer) {
                        case 0:
                            Intent i = new Intent(getActivity(), BluetoothConnectMenu.class);
                            i.putExtra("printKey", "2");
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

//                                                try {
//                                                    findBT();
//                                                    openBT(2);
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                            convertLayoutToImage(getActivity());
                            Intent O1 = new Intent(getActivity(), bMITP.class);
                            O1.putExtra("printKey", "2");
                            startActivity(O1);
//                                                             lk32.setChecked(true);
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


//                                                             MTP.setChecked(true);

                        case 6:// inner printer
//                            convertLayoutToImage(getActivity());
                            Intent O = new Intent(getActivity(), bMITP.class);
                            O.putExtra("printKey", "2");
                            startActivity(O);
                            break;

                    }
                } else {
                    Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_LONG).show();

                }
            }
            else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getContext().getString(R.string.succsesful))
                        .show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();

        }

        mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);
        clearForm();
    }


    //    pay = payment.get(n);
    private void printLastPaymentpaper(int voucherNo) {
        payment_rePrint = mDbHandler.getPayments_voucherNo(voucherNo);
        Log.e("payment_rePrint", "" + payment_rePrint.getCustName());
        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
//                                                     try {
//            Log.e("pay", "  " + pay.getVoucherNumber());
            try {

                int printer = mDbHandler.getPrinterSetting();


                switch (printer) {
                    case 0:
                        pay1 = payment_rePrint;
                        paymentPrinter = mDbHandler.getRequestedPaymentsPaper(voucherNo);
                        Intent i = new Intent(getActivity(), BluetoothConnectMenu.class);
                        i.putExtra("printKey", "8");
                        startActivity(i);

//                                                             lk30.setChecked(true);
                        break;
                    case 1:

//                        try {
//                            findBT(voucherNo);
//                            openBT(payment_rePrint, 1);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
////                                                             lk31.setChecked(true);
//                        break;
                    case 2:

//                                                                try {
//                                                                    findBT(Integer.parseInt(textView.getText().toString()));
//                                                                    openBT(pay, 2);
//                                                                } catch (IOException e) {
//                                                                    e.printStackTrace();
//                                                                }

                        paymentPrinter = mDbHandler.getRequestedPaymentsPaper(voucherNo);
                        pay1 = payment_rePrint;
//                        convertLayoutToImage(payment_rePrint);
                        Intent O = new Intent(getActivity(), bMITP.class);
                        O.putExtra("printKey", "8");
                        startActivity(O);
                        Log.e("Pay 0000 ==>", "" + pay1.getPayMethod());


//                                                             lk32.setChecked(true);
                        break;
                    case 3:

//                        try {
//                            findBT(Integer.parseInt(voucherNo);
//                            openBT(pay, 3);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                                                             qs.setChecked(true);
                        break;

                    case 4:
//                        printTally();
//                        break;
//

                    case 5:


                        paymentPrinter = mDbHandler.getRequestedPaymentsPaper(voucherNo);
                        pay1 = payment_rePrint;
//                        convertLayoutToImage(pay);
                        Intent O1 = new Intent(getActivity(), bMITP.class);
                        O1.putExtra("printKey", "8");
                        startActivity(O1);
                        Log.e("Pay 0000 ==>", "" + pay1.getPayMethod());

//                                                                MTP.setChecked(true);

                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();

            }


//
//                                                         master(vouch);
//                                                     testB =convertLayoutToImage(v);


//                                                     } catch (IOException ex) {
//                                                     }
        } else {
//            hiddenDialog(pay);
        }
    }

    @SuppressLint("RestrictedApi")
    public void clearForm() {
        tableCheckData.removeAllViews();
        amountEditText.setText("");
        remarkEditText.setText("");
        paymentKindSpinner.setSelection(0);
        chequeTotal.setText("0.00");
        total = 0.0;
        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;//test 0
        // voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        voucherNo.setText(getResources().getString(R.string.voucher_number) + " : " + voucherNumber);
        payments.clear();
        chequNo_EditText.setText("");
        editChech.setVisibility(View.GONE);
        checkNo=0;
        serialNo=0;
        curentAmount=0;
        valuecheck=0;
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }


    private Boolean checkTotal(String s) {
        if (!amountEditText.getText().toString().equals("")) {
            if (total + Double.parseDouble(s) > Double.parseDouble(amountEditText.getText().toString())) {
                return false;
            }
            return true;
        }
        return false;
    }

    private Boolean checkValue() {
        String total_convert = convertToEnglish(new DecimalFormat("##.###").format(total));
        Double total_double = Double.parseDouble(total_convert);
        if (total_double != Double.parseDouble(amountEditText.getText().toString())) {
            return false;
        }
        return true;
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final EditText editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }


    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    public void setListener(ReceiptInterFace listener) {
        this.receiptInterFace = listener;
    }

    public boolean checkDialogFields(String field1, String field2, String field3, String field4) {
        if (!field1.equals("") && !field2.equals("") && !field3.equals("") && !field4.equals(""))
            return true;
        return false;
    }


    void findBT() {
        try {
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


            switch (casePrinter) {

                case 1:
                    sendData();
                    break;
                case 2:
                    Settings settings = mDbHandler.getAllSettings().get(0);
                    for (int i = 0; i < settings.getNumOfCopy(); i++) {
                        Thread.sleep(1000);
                        send_dataSewoo();
                    }
                    break;
                case 3:
                    sendData2();
                    break;


            }

//            myLabel.setText("Bluetooth Opened");
//

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void send_dataSewoo() throws IOException {
        try {
            Log.e("send", "'yes");
            testB = convertLayoutToImage(getActivity());

            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm = printPic.printDraw();
            mmOutputStream.write(printIm);
            isFinishPrint = true;


//            dialogs.show();
//            ImageView iv = (ImageView)findViewById(R.id.ivw);
////            iv.setLayoutParams(layoutParams);
//            iv.setBackgroundColor(Color.TRANSPARENT);
//            iv.setImageBitmap(testB);
//                iv.setMaxHeight(100);
//                iv.setMaxWidth(100);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void printTally() {

        Bitmap bitmap = convertLayoutToImageTally();

        try {
            Settings settings = mDbHandler.getAllSettings().get(0);
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
            String targetPdf = directory_path + "testimageReceipt" + i + ".png";
            f = new File(targetPdf);


//        f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        return f;
    }


    public Bitmap convertLayoutToImage(Context context) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(getActivity());
        dialogs.setContentView(R.layout.print_payment_cash);
        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
//*******************************************initial ***************************************************************
        TextView compname, tel, taxNo, cashNo, date, custname, note, paytype, ammont;
        LinearLayout cheque_print_linear = (LinearLayout) dialogs.findViewById(R.id.cheque_payment_layout);
        compname = (TextView) dialogs.findViewById(R.id.textView_companey_Name);
        tel = (TextView) dialogs.findViewById(R.id.telephone);
        taxNo = (TextView) dialogs.findViewById(R.id.tax_no);
        cashNo = (TextView) dialogs.findViewById(R.id.textView_cashNo);
        date = (TextView) dialogs.findViewById(R.id.textVie_date);
        custname = (TextView) dialogs.findViewById(R.id.textView_customerName);
        note = (TextView) dialogs.findViewById(R.id.textView_remark);
        ammont = (TextView) dialogs.findViewById(R.id.textView_amount_ofMoney);
        paytype = (TextView) dialogs.findViewById(R.id.textView_payMethod);
        TableLayout tableCheque = (TableLayout) dialogs.findViewById(R.id.table_bank_info);
        ImageView companey_logo = (ImageView) dialogs.findViewById(R.id.imageCompaney_logo);


        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);
        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFinishPrint) {
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
        if (companyInfo.getLogo() != null) {
            companey_logo.setImageBitmap(companyInfo.getLogo());

        }
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        ammont.setText(payment.getAmount() + "");
        note.setText(payment.getRemark());
        if (payment.getPayMethod() == 1) {
            paytype.setText(" نقدا ");
            cheque_print_linear.setVisibility(View.GONE);
        } else {
            paytype.setText(" ذمم  ");
            cheque_print_linear.setVisibility(View.VISIBLE);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp.setMargins(0, 7, 0, 0);
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(0, 7, 0, 0);

            for (int n = 0; n < payments.size(); n++) {
//                if (payments.get(n).getVoucherNumber() == mDbHandler.getMaxSerialNumber(4)) {
                final TableRow row = new TableRow(this.getActivity());
                row.setPadding(0, 10, 0, 10);
                Log.e("paymentprint", "" + payments.size());
                for (int i = 0; i < 4; i++) {

                    String[] record = {
                            payments.get(n).getBank() + "",
                            payments.get(n).getCheckNumber() + "",
                            payments.get(n).getDueDate() + "",
                            payments.get(n).getAmount() + "",
                    };

                    row.setLayoutParams(lp);
                    TextView textView = new TextView(this.getActivity());
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);

                }

                tableCheque.addView(row);
            }
//            }
        }


        custname.setText(payment.getCustName());
        date.setText(payment.getPayDate());
        cashNo.setText(payment.getVoucherNumber() + "");
        dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.print_invoice);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit = linearView.getDrawingCache();
        return bit;// creates bitmap and returns the same
    }

    private Bitmap convertLayoutToImageTally() {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(getActivity());
        dialogs.setContentView(R.layout.print_payment_cash_tally);
        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
//*******************************************initial ***************************************************************
        TextView compname, tel, taxNo, cashNo, date, custname, note, paytype, ammont;
        LinearLayout cheque_print_linear = (LinearLayout) dialogs.findViewById(R.id.cheque_payment_layout);
        compname = (TextView) dialogs.findViewById(R.id.textView_companey_Name);
        tel = (TextView) dialogs.findViewById(R.id.telephone);
        taxNo = (TextView) dialogs.findViewById(R.id.tax_no);
        cashNo = (TextView) dialogs.findViewById(R.id.textView_cashNo);
        date = (TextView) dialogs.findViewById(R.id.textVie_date);
        custname = (TextView) dialogs.findViewById(R.id.textView_customerName);
        note = (TextView) dialogs.findViewById(R.id.textView_remark);
        ammont = (TextView) dialogs.findViewById(R.id.textView_amount_ofMoney);
        paytype = (TextView) dialogs.findViewById(R.id.textView_payMethod);
        TableLayout tableCheque = (TableLayout) dialogs.findViewById(R.id.table_bank_info);
        ImageView companey_logo = (ImageView) dialogs.findViewById(R.id.imageCompaney_logo);


        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);
        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFinishPrint) {
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

        if (!companyInfo.getLogo().equals(null)) {
            companey_logo.setImageBitmap(companyInfo.getLogo());

        }
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        ammont.setText(payment.getAmount() + "");
        note.setText(payment.getRemark());
        if (payment.getPayMethod() == 1) {
            paytype.setText(" نقدا ");
            cheque_print_linear.setVisibility(View.GONE);
        } else {
            paytype.setText(" ذمم  ");
            cheque_print_linear.setVisibility(View.VISIBLE);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp.setMargins(0, 7, 0, 0);
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(0, 7, 0, 0);

            for (int n = 0; n < payments.size(); n++) {
//                if (payments.get(n).getVoucherNumber() == mDbHandler.getMaxSerialNumber(4)) {
                final TableRow row = new TableRow(this.getActivity());
                row.setPadding(0, 10, 0, 10);
                Log.e("paymentprint", "" + payments.size());
                for (int i = 0; i < 4; i++) {

                    String[] record = {
                            payments.get(n).getBank() + "",
                            payments.get(n).getCheckNumber() + "",
                            payments.get(n).getDueDate() + "",
                            payments.get(n).getAmount() + "",
                    };

                    row.setLayoutParams(lp);
                    TextView textView = new TextView(this.getActivity());
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);

                }

                tableCheque.addView(row);
            }
//            }
        }


        custname.setText(payment.getCustName());
        date.setText(payment.getPayDate());
        cashNo.setText(payment.getVoucherNumber() + "");
//        dialogs.show();
//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.print_invoice);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void sendData() throws IOException {
        try {

//************************************ alban alsaa *************************

            Log.e("******", "here");
            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            String nameCompany = companyInfo.getCompanyName().toString();

            // the text typed by the user
            String msg;

            for (int i = 1; i <= numOfCopy; i++) {
                if (payment.getPayMethod() == 1) {
                    msg = "       " + "\n" +
                            "----------------------------------------------" + "\n" +
                            "       " + "\n" +
                            "       " + "\n" +
                            "       " + "\n" +
                            "طريقة الدفع: " + (payment.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
                            "المبلغ المقبوض: " + payment.getAmount() + "\n" +
                            "ملاحظة: " + payment.getRemark() + "\n" +
                            payment.getCustName() + "\n" +
                            "وصلني من السيد/السادة: " + "\n" +
                            "       " + "\n" +
                            "رقم السند: " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n" +
                            " سند قبض " + "\n" +
                            "----------------------------------------------" + "\n" +
                            "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
                            companyInfo.getCompanyName() + "\n" +
                            "       " + "\n" +
                            "       ";
                } else {
                    Log.e("isArabicBank",""+isArabicBank);
                    if(isArabicBank){
                        msg = "       " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "       " + "\n" +
                                "       " + "\n" +
                                itemsString + "\n" +
                                "       " + "\n" +
                                "  البنك     " + "  رقم الشيك  " + "  التاريخ       " + "  القيمة  " + "\n" +
                                "       " + "\n" +
                                "طريقة الدفع: " + (payment.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
                                "المبلغ المقبوض: " + payment.getAmount() + "\n" +
                                "ملاحظة: " + payment.getRemark() + "\n" +
                                payment.getCustName() + "\n" +
                                "وصلني من السيد/السادة: " + "\n" + "       " + "\n" +
                                "رقم السند: " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n" +
                                " سند قبض " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
                                companyInfo.getCompanyName() + "\n" +
                                "       " + "\n" +
                                "       ";
                    }
                    else {
                        msg = "       " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "       " + "\n" +
                                "       " + "\n" +
                                itemsString + "\n" +
                                "       " + "\n" +
                                "  البنك     " + "  رقم الشيك  " + "  التاريخ       " + "  القيمة  " + "\n" +
                                "       " + "\n" +
                                "طريقة الدفع: " + (payment.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
                                "المبلغ المقبوض: " + payment.getAmount() + "\n" +
                                "ملاحظة: " + payment.getRemark() + "\n" +
                                payment.getCustName() + "\n" +
                                "وصلني من السيد/السادة: " + "\n" + "       " + "\n" +
                                "رقم السند: " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n" +
                                " سند قبض " + "\n" +
                                "----------------------------------------------" + "\n" +
                                "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
                                companyInfo.getCompanyName() + "\n" +
                                "       " + "\n" +
                                "       ";
                    }

                }
                printCustom(msg + "\n", 1, 2);
//**************************************************************************************************************************************************************
//            Log.e("******", "here");
//            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
//            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
//              String nameCompany=companyInfo.getCompanyName().toString();
///*"       " + "\n" +
//                            "----------------------------------------------" + "\n" +
//                            "       " + "\n" +
//                            "       " + "\n" +
//                            "       " + "\n" +*/
//            // the text typed by the user
//            // the text typed by the user
//            String msg;
//
//            for (int i = 1; i <= numOfCopy; i++) {
//                if (payment.getPayMethod() == 1) {
//                    msg = "       " + "\n" +
//                            "----------------------------------------------" + "\n" +
//                            "       " + "\n" +
//                            "       " + "\n" +
//                            "       " + "\n" +
//                            "طريقة الدفع: " + (payment.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
//                            "المبلغ المقبوض: " + payment.getAmount() + "\n" +
//                            "ملاحظة: " + payment.getRemark() + "\n" +
//                            payment.getCustName() + "\n" +
//                            "وصلني من السيد/السادة: " + "\n" +
//                            "       " + "\n" +
//                            "رقم السند: " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n" +
//                            " سند قبض " + "\n" +
//                            "----------------------------------------------" + "\n" +
//                            "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
//                            companyInfo.getCompanyName() + "\n" +
//                            "       " + "\n" +
//                            "       ";
//                } else {
//                    msg = "       " + "\n" +
//                            "----------------------------------------------" + "\n" +
//                            "       " + "\n" +
//                            "       " + "\n" +
//                            itemsString + "\n" +
//                            "       " + "\n" +
//                            "رقم الشيك  " + "البنك     " + "التاريخ       " + "القيمة  " + "\n" +
//                            "       " + "\n" +
//                            "طريقة الدفع: " + (payment.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" +
//                            "المبلغ المقبوض: " + payment.getAmount() + "\n" +
//                            "ملاحظة: " + payment.getRemark() + "\n" +
//                            payment.getCustName() + "\n" +
//                            "وصلني من السيد/السادة: " + "\n" + "       " + "\n" +
//                            "رقم السند: " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n" +
//                            " سند قبض " + "\n" +
//                            "----------------------------------------------" + "\n" +
//                            "هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n" +
//                            companyInfo.getCompanyName() + "\n" +
//                            "       " + "\n" +
//                            "       ";
//                }
//                printCustom(msg + "\n", 1, 2);

//******************************************************************************************

//            Arabic864 arabic = new Arabic864();
//                //   byte[] arabicArr = arabic.Convert(  new StringBuilder(msg).reverse().toString(),false);
//                //    byte[] arabicArr = arabic.Convert("الاسم",false);
//                Log.e("mymsg", "" + msg);
//
//                Bidi bidi = new Bidi(msg, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
//
//                if (bidi.isLeftToRight()) {
//
//                    // it's LTR
//                } else {
//                    Log.e("Not ---LtoR",""+msg);
//                    Log.e("LtoR",""+msg);
//                    byte[] arabicArr = arabic.Convert(new StringBuilder(msg).reverse().toString(), false);
//                    Log.e("byte", "" + arabicArr.toString());
//                    mmOutputStream.write(arabicArr);
//
//                    // it's RTL
//                }

//
////                viewImage.setImageBitmap(BitmapFactory.decodeFile("your iamge path"));
//             //   mmOutputStream.write(arabicArr);

//                printCustom(msg+ "\n", 1, 0);

//            }
//            Arabic864 arabic = new Arabic864();
//            byte[] arabicArr = arabic.Convert(msg, false);


//            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
//            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
//
//            for (int i = 1; i <= numOfCopy; i++) {
//                printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
//                printCustom("هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", 1, 2);
//                printCustom("----------------------------------------------" + "\n" , 1, 2);
//                printCustom("رقم الفاتورة : " + payment.getVoucherNumber() + "         التاريخ: " + payment.getPayDate() + "\n", 1, 2);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                printCustom("اسم العميل   : " + payment.getCustName() + "\n", 1, 2);
//                printCustom("الكمية: " + payment.getAmount() + "\n", 1, 2);
//                printCustom("طريقة الدفع  : " + (payment.getPayMethod() == 0 ? "نقدا" : "شيك") + "\n" , 1, 2);
//                printCustom("ملاحظة: " + payment.getRemark() + "\n", 1, 2);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                printCustom("المستلم : ________________ التوقيع : __________" +"\n", 1, 2);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                printCustom("----------------------------------------------" + "\n" , 1, 2);
//                printCustom("\n" , 1, 2);
//                printCustom("\n" , 1, 2);
//
//                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//            }
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
    public static boolean textContainsArabic(String text) {
        for (char charac : text.toCharArray()) {
            if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
                return true;
            }
        }
        return false;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String makeLtr(String string) {
        if (checkRtl(string)) {
            /* prepend the string with an LTR control sign (so
               that Android's RTL check returns false) and an RTL
               control sign (so that the string itself is printed in
               RTL) and append an LTR control sign (so that if we
               append another String it is laid out LTR). */
            Log.e("makeLTR", "" + "\u200E" + "\u200F" + string + "\u200E");
            return "\u200E" + "\u200F" + string + "\u200E";

        } else {
            return string;
        }
    }

    public static boolean checkRtl(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        char c = string.charAt(0);
        Log.e("check", " " + (c >= 0x590 && c <= 0x6ff));
        return c >= 0x590 && c <= 0x6ff;
    }

    public String printStringtLtr(String sb) {
        if (sb.length() == 0)
            return "";
        StringBuilder b = new StringBuilder();
//        for (String c : sb) {
        for (int i = 0; i < sb.length(); i++) {
            b.append('\u200e').append(sb.charAt(i));
        }
        return b.substring(0, b.length() - 2);
    }

    // PrintPic printPic = PrintPic.getInstance();
    void sendData2() throws IOException {
        try {

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            Log.e("nocopy", "" + numOfCopy);
            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            if (pic != null) {
//                pic.setImageBitmap(companyInfo.getLogo());
                String s = "tahani test invoice ";
                byte[] encodeByte = Base64.decode(s, Base64.DEFAULT);

                InputStream inputStream = new ByteArrayInputStream(encodeByte);
                Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
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
                    if (payment.getPayMethod() == 1) {

                        printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);
                        printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                        printCustom("------------------------------------------" + " \n ", 1, 0);
                        printCustom("التاريخ        " + payment.getPayDate() + " : " + " \n ", 1, 0);
                        printCustom("رقم السند      " + payment.getVoucherNumber() + " : " + " \n ", 1, 0);
                        printCustom("اسم العميل " + " : " + payment.getCustName() + " \n ", 1, 0);
                        printCustom("ملاحظة " + " : " + payment.getRemark() + " \n ", 1, 0);
                        printCustom("المبلغ المقبوض " + payment.getAmount() + " : " + " \n ", 1, 0);
                        printCustom("طريقة الدفع    " + " : " + "نقدا" + " \n ", 1, 0);
                        //  printCustom("طريقة الدفع    " + " : " + (payment.getPayMethod() == 0 ?  "شيك" : "نقدا") +  " \n ", 1, 0);
                        printCustom("\n", 1, 0);
                    } else {
                        printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);
                        printCustom("الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                        printCustom("------------------------------------------" + " \n ", 1, 0);
                        printCustom("التاريخ        " + payment.getPayDate() + " : " + " \n ", 1, 0);
                        printCustom("رقم السند      " + payment.getVoucherNumber() + " : " + " \n ", 1, 0);
                        printCustom("اسم العميل " + " : " + payment.getCustName() + " \n ", 1, 0);
                        printCustom("ملاحظة " + " : " + payment.getRemark() + " \n ", 1, 0);
                        printCustom("المبلغ المقبوض " + payment.getAmount() + " : " + " \n ", 1, 0);
                        printCustom("طريقة الدفع    " + " : " + "شيك" + " \n ", 1, 0);
                        printCustom("\n", 1, 0);

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
//            mmOutputStream.write(msg.getBytes());

//            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Close the connection to bluetooth printer.
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

}
