package com.dr7.salesmanmanager;

import static com.dr7.salesmanmanager.ImportJason.listItemsReturn;
import static com.dr7.salesmanmanager.ImportJason.returnListSerial;
import static com.dr7.salesmanmanager.ImportJason.voucherReturn;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Adapters.ReturnItemAdapter;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReturnByVoucherNo extends AppCompatActivity {
    public static  List<Item> ReturnupdateditemList=new ArrayList<>();
    public static   RecyclerView recyclerView;
    public static List<serialModel> allseriallist =new ArrayList<>();
    public static List<serialModel> allitemsdata =new ArrayList<>();
    public static List<serialModel> Recoverallitemsdata =new ArrayList<>();
    public static String VOCHdat="";
    DatabaseHandler databaseHandler;
    public static ReturnItemAdapter adapter;
    EditText serial_text,voucherNo_text;
    public TextView date,getDataVoucher,payMethod_textView,textView_save,textView_cancel,getserialData,editSerial;
    int   max_voucherNumber=0;
    GeneralMethod generalMethod;
    public static String voucherNo_ReturnNo ="";
    ImportJason importJason;
    public  static   TextView loadSerial;
    List<String> listItemDeleted=new ArrayList<>();
    public  static   ArrayList<Item> listItemsMain = new ArrayList<>();
    public  static   List<Item> returblistItemsMain = new ArrayList<>();
    public  static   List<Item> LASTVOCHER = new ArrayList<>();
    public  static   List<Item> LASTVOCHER2 = new ArrayList<>();
    public  DatabaseHandler dataBase;
    float total=0;
    String curent="";
    CheckBox returnall;
    public RadioGroup paymentTermRadioGroup;
    CompanyInfo companyInfo;
    int typeTax=0;
    int[] listImageIcone=new int[]{R.drawable.ic_print_white_24dp,
            R.drawable.pdf_icon,R.drawable.excel_small
            };
    LinearLayout boomlin;
    public  static TextView  ScanSerialcode,checked;
    public  static EditText  Serialcode;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_by_voucher_no);


        initialView();

        ////B
        try {
            if (!generalMethod.checkDeviceDate()) {

                new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(getString(R.string.invalidDate))
                        .setContentText(getString(R.string.invalidDate_msg))
                        .setCustomImage(R.drawable.date_error)
                        .setConfirmButton(getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  boomlin=findViewById(R.id.boomlin);
        //boomlin.setVisibility(View.INVISIBLE);
        getVoucherNo();
     inflateBoomMenu();
        textView_save.setEnabled(false);
       // getLocalData();
// PDF
    }

    private void getVoucherNo() {
        max_voucherNumber = dataBase.getMaxSerialNumberFromVoucherMaster(506) + 1;
//        Log.e("saveVoucherMaster","voucherNumber="+max_voucherNumber);
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initialView() {
        Serialcode=findViewById(R.id.serialcode);
        checked = findViewById(R.id.checked);


        allitemsdata.clear();
        Recoverallitemsdata.clear();
        checked.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {

                    fillAdapterData(ReturnByVoucherNo.this, allitemsdata, 1);

                }
            }
        });
        dataBase = new DatabaseHandler(this);
        if (dataBase.getAllSettings().get(0).getContinusReading() == 1)// camera
        {
            Serialcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")) {
                        if (allitemsdata.size() != 0) {
                            for (int i = 0; i < allitemsdata.size(); i++) {
                                if (allitemsdata.get(i).getSerialCode().equals(s.toString().trim())) {
                                    allitemsdata.get(i).setIsClicked(1);
                                    fillAdapterData(ReturnByVoucherNo.this, allitemsdata, 0);
                                    Serialcode.setText("");
                                }

                            }
                        }

                    }
                }
            });
        } else {


            Serialcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                     @Override
                                                     public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                         if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH
                                                                 || actionId == EditorInfo.IME_NULL) {
                                                             if (!Serialcode.getText().toString().trim().equals("")) {
                                                                 if (allitemsdata.size() != 0) {
                                                                     for (int x = 0; x < allitemsdata.size(); x++) {
                                                                         if (allitemsdata.get(x).getSerialCode().
                                                                                 equals(Serialcode.getText().toString().trim())) {
                                                                             allitemsdata.get(x).setIsClicked(1);
                                                                             fillAdapterData(ReturnByVoucherNo.this, allitemsdata, 0);
                                                                             Serialcode.setText("");
                                                                         }

                                                                     }
                                                                 }

                                                             } else {

                                                             }

                                                         }
                                                         return false;
                                                     }

                                                 }

            );



            typeTax=dataBase.getAllSettings().get(0).getTaxClarcKind();
            Log.e("typeTax",""+typeTax);











//            Serialcode. setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                    if (i == KeyEvent.KEYCODE_BACK) {
//                        try {
//                            onBackPressed();
//                        }catch (Exception e){
//
//                        }
//
//
//                    }
//
//                    else  if (i != KeyEvent.KEYCODE_ENTER) {
//
//                        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                            if (!   Serialcode.getText().toString().trim().equals("")) {
//                                if(allitemsdata.size()!=0)
//                                {
//                                    for (int x=0;x<allitemsdata.size();x++){
//                                        if( allitemsdata.get(x).getSerialCode().
//                                                equals(Serialcode.getText().toString().trim()))
//                                        {
//                                            allitemsdata.get(x).setIsClicked(1);
//                                            fillAdapterData(ReturnByVoucherNo.this,allitemsdata,0);
//                                            Serialcode.setText("");
//                                        }
//
//                                    }
//                                }
//
//                            } else {
//
//                            }
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//            });


        }

        returnall=findViewById(R.id.returnall);
        ScanSerialcode=findViewById(R.id.scan);
        ScanSerialcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new IntentIntegrator(ReturnByVoucherNo.this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
                Intent i=new Intent(ReturnByVoucherNo.this,ScanActivity.class);
                i.putExtra("key","5");
                startActivity(i);
            }
        });

        returnall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked==","is");
                String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";
               // Toast.makeText(ReturnByVoucherNo.this, msg, Toast.LENGTH_SHORT).show();

            if(isChecked)
            {
              //  Toast.makeText(ReturnByVoucherNo.this, msg, Toast.LENGTH_SHORT).show();

                Log.e("isChecked2==","is"+isChecked);
                for(int i=0;i< allitemsdata.size();i++)
              if( dataBase.HASSERAIAL(allitemsdata.get(i).getItemNo())==1
                      &&allitemsdata.get(i).getSerialCode().equals(""))
              { allitemsdata.get(i). setIsClicked(0);}
                  else
                  allitemsdata.get(i). setIsClicked(1);


            }else{
                Log.e("isChecked2==","is"+isChecked);
                for(int i=0;i< allitemsdata.size();i++)
                    allitemsdata.get(i). setIsClicked(0);

            }
                fillAdapterData( ReturnByVoucherNo.this,allitemsdata,2);

            }
        });


/*
        returnall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((CompoundButton) view).isChecked()){
                    Log.e("isChecked==","is");
                    for(int i=0;i< allitemsdata.size();i++)
                        allitemsdata.get(i). setIsClicked(1);
                    fillAdapterData( allitemsdata);}
                else {
                    System.out.println("Un-Checked");
                }
            }
        });*/

        textView_save=findViewById(R.id.textView_save);
        textView_cancel =findViewById(R.id.textView_can);
        paymentTermRadioGroup=findViewById(R.id.paymentTermRadioGroup);
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                finish();
                Intent i=new Intent(ReturnByVoucherNo.this,Activities.class);
                startActivity(i);
            }
        });
        textView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //boomlin.setVisibility(View.VISIBLE);
                saveData();




            }
        });
        getserialData=findViewById(R.id.getserialData);
        getserialData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           allitemsdata.clear();
                Recoverallitemsdata.clear();
                readBarcode();
                textView_save.setEnabled(true);
               // getSerialVoucherNo();
            }
        });
        editSerial=findViewById(R.id.editSerial);
        editSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditSerialDialog();
            }
        });
        loadSerial=findViewById(R.id.loadSerial);
        importJason=new ImportJason(ReturnByVoucherNo.this);
        LinearLayout linearMain=findViewById(R.id.linearMain);
        generalMethod=new GeneralMethod(ReturnByVoucherNo.this);
        recyclerView=findViewById(R.id.recyclerView_returnItem);
        getDataVoucher=findViewById(R.id.getDataVoucher);
        payMethod_textView=findViewById(R.id.payMethod_textView);

        voucherNo_text=findViewById(R.id.voucherNo_text);
        serial_text=findViewById(R.id.serial_text);
        getDataVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(voucherNo_text.getText().toString().trim().length()!=0)
                {   allitemsdata.clear();
                    Recoverallitemsdata.clear();
                    returnListSerial.clear();
                    voucherNo_text.setError(null);
                    voucherNo_ReturnNo =voucherNo_text.getText().toString().trim();
                   // Log.e("voucherNo",""+voucherNo);
                    getDataForVoucherNo();
                    textView_save.setEnabled(true);


                }else {
                    voucherNo_text.setError(getResources().getString(R.string.reqired_filled));
                }
            }
        });
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
        loadSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length()!=0)
                {


                    if(editable.toString().equals("No Parameter"))
                    {
                        Log.e("editable1====","NO Serial");

                    }
                else    if(editable.toString().equals("fillSerial"))
                    {
                        Log.e("editable2====",""+listItemsReturn.size()+"");

               /*   if(returnListSerial.size()!=0)

                  {

                        fillAdapterData(returnListSerial);

                          }
                        else showNotFound();*/

                    }
                    else if(editable.toString().equals("fillpayMethod"))
                    {
                       Log.e("voucherReturn",""+voucherReturn.getPayMethod()+"\t"+voucherReturn);
                        if(returnListSerial.size()!=0)
                        {
                            if(voucherReturn.getPayMethod()==0)
                            {
                                payMethod_textView.setText(getResources().getString(R.string.app_credit));
                            }else {// cash
                                payMethod_textView.setText(getResources().getString(R.string.app_cash));
                            }
                        }


                    }else
                        if (editable.toString().equals("fillItems"))
                    {


                        Log.e("editable66====",""+returnListSerial.size()+"");

                        if(returnListSerial.size()!=0||listItemsReturn.size()!=0)

                        {
                            sumAllListData();

                    //        fillAdapterData(returnListSerial);




//
//                       Log.e("editable",""+editable.toString());
//                        if(listItemsReturn.size()!=0)
//
//                        {
////                            for(int i=0;i< returnListSerial.size();i++){
////                                for(int x=0;x<listItemsReturn.size();x++){
////                                    if(listItemsReturn.get(x).getItemNo().equals( returnListSerial.get(i).getItemNo()))
////                                    returnListSerial.get(i).setQty( listItemsReturn.get(x).getQty()+"");
////                                }
////
////                                Log.e("VOCHdat",""+ReturnByVoucherNo.VOCHdat);
////                            }
//
////                            for(int i=0;i<listItemsReturn.size();i++) {
////
////                            if (dataBase.HASSERAIAL(listItemsReturn.get(i).getItemNo()) == 0) {
////                                serialModel serialModel1 = new serialModel();
////                                serialModel1.setItemNo(listItemsReturn.get(i).getItemNo());
////                                serialModel1.setItemName(listItemsReturn.get(i).getItemName());
////                                serialModel1.setDateVoucher(returnListSerial.get(i).getDateVoucher());
////                                serialModel1.setPriceItem(listItemsReturn.get(i).getPrice());
////                                serialModel1.setQty(listItemsReturn.get(i).getQty() + "");
////
////                                serialModel1.setSerialCode("");
////                                returnListSerial.add(serialModel1);
////                            }
////
////                        }
//                            //   if(returnListSerial.size()!=0)
//
//
//
////                            fillAdapterData(returnListSerial);
//
//                        }
//                  fillitemNoPrc();
                        }
                        else showNotFound();
                    }
                        else if(editable.toString().equals("NotSameCustomer"))
                        {
                            Log.e("editable77====",""+returnListSerial.size()+"");
                            showDialogNotSameCustomer();
                            clearData();
                        }
                        else if(editable.toString().equals("No Parameter Found"))
                        {
                            Log.e("editable99====","editable99   "+voucherNo_ReturnNo);
                        showNotFound();
                        //    importJason.getVoucherMReturnData(voucherNo_ReturnNo);
                        }
                        else if(editable.toString().contains("VHFNO")){
                            Log.e("editable88====",""+returnListSerial.size()+"");
                           // Log.e("vouchN",""+"\t"+editable.toString());
                            String vouchN=editable.toString().substring(5);
                            //Log.e("vouchN",""+vouchN+"\t"+editable.toString());
                            if(!vouchN.equals(""))
                            fillDataForVoucher(vouchN);
                            else {
                                showInvalidSerial();
                            }

                        }
                        else if(editable.toString().contains("returned")){
                            Log.e("editable99====",""+returnListSerial.size()+"");
                            showReturnedSerial();
                        }
                }

            }
        });
        curent=generalMethod.getCurentTimeDate(1);
        serial_text.setEnabled(false);
    }

    private void sumAllListData() {

        for (int i = 0; i < listItemsReturn.size(); i++) {
            serialModel serialModel1 = new serialModel();
            serialModel recoverserialModel= new serialModel();
            serialModel1.setItemNo(listItemsReturn.get(i).getItemNo());
            serialModel1.setItemName(listItemsReturn.get(i).getItemName());
            serialModel1.setDateVoucher(voucherReturn.getVoucherDate());
            serialModel1.setPriceItem(listItemsReturn.get(i).getPrice());
            if(dataBase.HASSERAIAL(serialModel1.getItemNo())==0) {

                serialModel1.setQty(String.valueOf(listItemsReturn.get(i).getAvi_Qty()));


            } else
                serialModel1.setQty("0");

            serialModel1.setSerialCode("");
            allitemsdata.add(serialModel1);
            ///recovery
            recoverserialModel.setQty(serialModel1.getQty());
            recoverserialModel.setItemNo(serialModel1.getItemNo());
            Recoverallitemsdata.add( recoverserialModel);
            /////
            for (int x = 0; x < returnListSerial.size(); x++)
                if (listItemsReturn.get(i).getItemNo().equals(returnListSerial.get(x).getItemNo())) {
                    allitemsdata.add(returnListSerial.get(x));


                    Recoverallitemsdata.add(returnListSerial.get(x));
                }
        }
        fillitemNoPrc_allItemType();
    }

    private void showInvalidSerial() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.noVoucherForThisSerial))
                .show();
    }
    private void Dailog(){
        final Dialog dialog = new Dialog(ReturnByVoucherNo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.zone_search);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.show();
        ArrayList<String> nameOfEngi = new ArrayList<>();

        nameOfEngi.add(getResources().getString(R.string.export_to_pdf)) ;
        nameOfEngi.add("export_to_ecxel");
        nameOfEngi.add(getResources().getString(R.string.print)) ;
        final ListView list = dialog.findViewById(R.id.listViewEngineering);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nameOfEngi);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
               // String value=adapter.getItem(position);

                if(position==0)  printLayout();
                    else if(position==1) exportToPdf();
                        else if(position==2)exportToEx();


            }
        });




    }
    private void openEditSerialDialog() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        SweetAlertDialog sweetMessage = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(this.getResources().getString(R.string.enter_serial));
        sweetMessage.setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (!editText.getText().toString().equals("")) {

                    serial_text.setText(editText.getText().toString().trim());
                    getSerialVoucherNo();
                    sweetAlertDialog.dismissWithAnimation();


                } else {
                    editText.setError(getResources().getString(R.string.reqired_filled));
                }
            }
        })

                .show();
    }

    private void readBarcode() {
        new IntentIntegrator((Activity) this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();

    }

 /*   private void getDataForVoucherNo() {
        if(! getLocalDataBase())
            importJason.getSerialData(voucherNo);
        else {// exist in local list
            getVoucherLocal();
        }
    }*/
    private void getDataForVoucherNo() {
        Log.e(" getDataForVoucherNo", "getDataForVoucherNo");
            getVoucherLocal();

    }
    private void getSerialVoucherNo() {
        String srialCode=serial_text.getText().toString().trim();
        if(srialCode.length()!=0)
        {
           String voucherNoSerial=dataBase.getVoucherNoFromSerialTable(srialCode);
           Log.e("voucherNoSerial",""+voucherNoSerial);
           if(voucherNoSerial.equals("returned"))
           {
               showReturnedSerial();
           }else {
               if(!voucherNoSerial.equals("NotFound"))
               {
                   fillDataForVoucher(voucherNoSerial);

               }else {
                   importJason.getVoucherNoFromServer(srialCode);
                   serial_text.setError(getResources().getString(R.string.invalidSerial));
               }
           }

        }else {
            serial_text.setError(getResources().getString(R.string.reqired_filled));
        }
    }

    private void fillDataForVoucher(String voucherNoSerial) {
        serial_text.setError(null);
        voucherNo_text.setText(voucherNoSerial);
//        voucherNo_text.setEnabled(false);
        voucherNo_ReturnNo =voucherNoSerial;
        getDataForVoucherNo();
    }

  /*  private void getVoucherLocal() {
        if(returnListSerial.size()!=0)
        {
            loadSerial.setText("fillSerial");
            voucherReturn=dataBase.getAllVouchers_VoucherNo(Integer.parseInt(voucherNo),504);
            loadSerial.setText("fillpayMethod");
            listItemsReturn=dataBase.getAllItems_byVoucherNo(voucherNo);
            loadSerial.setText("fillItems");
        }else showNotFound();


      //  Log.e("getVoucherLocal","voucherReturn"+voucherReturn.getCustNumber());
      //  Log.e("getVoucherLocal","listItemsMain"+listItemsReturn.get(0).getItemNo()+"\tlistIt"+listItemsReturn.size());

    }*/
  private void getVoucherLocal() {
      //    loadSerial.setText("fillSerial");

      voucherReturn = dataBase.getAllVouchers_VoucherNo(Integer.parseInt(voucherNo_ReturnNo), 504);

      loadSerial.setText("fillpayMethod");
      listItemsReturn = dataBase.getAllItems_byVoucherNo(voucherNo_ReturnNo);
      if (voucherReturn.getCustName() != null) {

          if(voucherReturn.getCustNumber().equals(MainActivity.CusId))
          {
          returnListSerial = dataBase.getAllSerialItemsByVoucherNo(voucherNo_ReturnNo);
          sumAllListData();
          }else {
              new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                      .setTitleText(this.getString(R.string.noVoucherByThisCus))
                      .show();
          }

      } else {
          //showNotFound();
exportData(2);

      }



//       fillAdapterData( allitemsdata);
  }

    private boolean getLocalDataBase() {
        returnListSerial=dataBase.getAllSerialItemsByVoucherNo(voucherNo_ReturnNo);
        Log.e("getVoucherLocal","returnListSerial"+returnListSerial.size());
        if(returnListSerial.size()!=0)
        {
            return true;
        }
        else return  false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        clearData();
        Intent i=new Intent(this,Activities.class);
        startActivity(i);
    }


    private void showReturnedSerial() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.thisSerialIsReturnedBefor))
                .show();
    }
    private void showNotFound() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.noVoucherByThisNumber))
                .show();
    }
    private void saveSuccses() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(this.getString(R.string.saveSuccessfuly))
                .show();
    }

    private void showDialogNotSameCustomer() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.notSameCustomer))
                .show();
    }

    private boolean sameCustomer() {
        if(returnListSerial.size()!=0)
        {
            if(returnListSerial.get(0).getCustomerNo().trim().equals(CustomerListShow.Customer_Account.trim()))
                return true;
            else return false;
        }else {
            return true;
        }

    }

    private void saveData() {


          textView_save.setEnabled(false);
          listItemDeleted.clear();
          serial_text.setText("");
          deleteItemNotSelected();
          calculateTotalc();
        if(returnListSerial.size()!=0) {
          saveSerial();
          saveVoucherD();
          saveVoucherMaster();
          dataBase.updateVoucherNo(max_voucherNumber, 506, 0);
          clearData();
          saveSuccses();
          // exportData();
          showprintDialog();
//            exportData(1);
            textView_save.setEnabled(false);
            returnall.setChecked(false);
    }
    else{
          Toast.makeText(ReturnByVoucherNo.this, "Fill Your List Please", Toast.LENGTH_LONG).show();

      }

    }

    private void exportData(int type) {

       boolean isPosted = dataBase.isAllReceptposted();
        Log.e("isPostedExport","1"+isPosted);
        if (!isPosted) {

          //  Log.e("isPostedExport","2"+isPosted);

            ExportJason objJson = null;
            try {
                objJson = new ExportJason(ReturnByVoucherNo.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
//                        objJson.startExportDatabase();
                objJson.startExport(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            if(type==2)
            {
                importJason.getSerialData(voucherNo_ReturnNo);
            }

        }
    }

    private void clearData() {

     listItemsMain.clear();
        returnListSerial.clear();
        listItemDeleted.clear();
        voucherNo_text.setText("");
        getVoucherNo();
        fillAdapterData(ReturnByVoucherNo.this,returnListSerial,2);
        payMethod_textView.setText("");
        paymentTermRadioGroup.setVisibility(View.GONE);
        payMethod_textView.setVisibility(View.VISIBLE);

    }

    private void saveVoucherMaster() {
      Log.e("saveVoucherMaster",""+"saveVoucherMaster");
        if(voucherReturn.getPayMethod()==1)
        {
            if(paymentTermRadioGroup.getCheckedRadioButtonId()==R.id.creditRadioButton)
            {
                voucherReturn.setPayMethod(0);
            }
        }
      voucherReturn.setVoucherNumber(max_voucherNumber);
      voucherReturn.setVoucherType(506);
      voucherReturn.setIsPosted(0);
      voucherReturn.setRemark("");

      voucherReturn.setVoucherDate(generalMethod.getCurentTimeDate(1));
      voucherReturn.setCustNumber(CustomerListShow.Customer_Account);
      voucherReturn.setCustName(CustomerListShow.Customer_Name);
      voucherReturn.setSubTotal(total);
      voucherReturn.setNetSales(total);
        voucherReturn.setORIGINALvoucherNo( Integer.parseInt(voucherNo_text.getText().toString().trim()));
      voucherReturn.setTax(0);

      // calck netSales
      dataBase.addVoucher(voucherReturn);
     // Log.e("getPayMethod()",""+voucherReturn.getPayMethod());

    }

    private void saveVoucherD() {

        Log.e("saveVoucherD",""+"saveVoucherD  "+listItemsMain.size());
        String curent=generalMethod.getCurentTimeDate(1);

        for(int i=0;i<listItemsMain.size();i++)
        {
            Log.e("QTY==",""+"saveVoucherD  "+ listItemsMain.get(i).getQty());

            listItemsMain.get(i).setVoucherNumber(max_voucherNumber);
            listItemsMain.get(i).setVouchDate(curent);
            listItemsMain.get(i).setVoucherType(506);
            listItemsMain.get(i).setIsPosted(0);
            Log.e("ORIGINALvoucherNo",""+voucherNo_text.getText().toString().trim());

            listItemsMain.get(i).setORIGINALvoucherNo( Integer.parseInt(voucherNo_text.getText().toString().trim()));

            Log.e("serialModel1","2222salePrice="+listItemsMain.get(i).getPrice());
            if( dataBase.HASSERAIAL(listItemsMain.get(i).getItemNo())==0)
            dataBase.UpdateAvi_QtyInOrigenalVoch(voucherNo_text.getText().toString(),listItemsMain.get(i).getQty(),listItemsMain.get(i).getItemNo());
          else
            {
                if(dataBase.HASSERAIAL(listItemsMain.get(i).getItemNo())==1&&
                        listItemsMain.get(i).getSerialCode().equals(""))
                dataBase.UpdateAvi_QtyInOrigenalVoch(voucherNo_text.getText().toString(),listItemsMain.get(i).getQty(),listItemsMain.get(i).getItemNo());

            }

            Log.e("deat===",""+listItemsMain.get(i).getQty()+"{...}"+listItemsMain.get(i).getItemNo());
            Log.e("voucherNo_===",""+voucherNo_text.getText().toString());

            dataBase.addItem(listItemsMain.get(i));

              }
    }

    private void calculateTotalc() {
        for(int i=0;i<returnListSerial.size();i++)
        {
            for (int k=0;k<listItemsMain.size();k++)
            {
                if(returnListSerial.get(i).getItemNo().trim().equals(listItemsMain.get(k).getItemNo().trim()))
                {
                    if(!returnListSerial.get(i).getSerialCode().equals(""))
                    {
                        float qty=listItemsMain.get(k).getQty();
                        qty=qty+1;
                        listItemsMain.get(k).setQty(qty);
                        listItemsMain.get(k).setPrice(returnListSerial.get(i).getPriceItem());

                    }

                  //  Log.e("calculateTotalc","qqqq="+listItemsMain.get(k).getQty());
                }
            }

        }
        calcTotalVoucher();

    }

    private void calcTotalVoucher() {

        total=0;
        for (int i=0;i<listItemsMain.size();i++)
        {
            total+=listItemsMain.get(i).getQty()*listItemsMain.get(i).getPrice();
        }
       // Log.e("calcTotalVoucher","total"+total);
    }

    private void saveSerial() {
   Log.e("saveSeria,returnListSeria",""+"saveVoucherMaster"+returnListSerial.size());
        for(int i=0;i<returnListSerial.size();i++)
        {
           // Log.e("returnListSerial","getVoucherNo"+returnListSerial.get(i).getVoucherNo());
            dataBase.updateSerialReturnedInBaseInvoice(returnListSerial.get(i).getVoucherNo(),returnListSerial.get(i).getSerialCode());
             returnListSerial.get(i).setVoucherNo(max_voucherNumber+"");
           // Log.e("returnListSerial","getVoucherNo=after="+returnListSerial.get(i).getVoucherNo());
          if(dataBase.HASSERAIAL(returnListSerial.get(i).getItemNo())!=0)
              dataBase.add_Serial(returnListSerial.get(i));
        }

try {

   //for update itmes IsReturnd
        for(int i=0;i<returnListSerial.size();i++) {
         //   Log.e("VoucherNo==",returnListSerial.get(i).getVoucherNo()+"getItemNo=="+returnListSerial.get(i).getItemNo());
         //   Log.e("HASSERAIAL==",dataBase.HASSERAIAL(returnListSerial.get(i).getItemNo())+ "");
         if(dataBase.HASSERAIAL(returnListSerial.get(i).getItemNo())==0) {
             int m = dataBase.updateItemReturnedInVocherDetails(voucherNo_ReturnNo, returnListSerial.get(i).getItemNo());
            // Log.e("m==", m + "");
         }
           else
         {
             //when item have serial but not all serials returned

            /* Log.e("listItemDeleted==", listItemDeleted.get(0) + "");
             if(!listItemDeleted.contains(returnListSerial.get(i).getItemNo()))
             {
                 int m = dataBase.updateItemReturnedInVocherDetails(voucherNo, returnListSerial.get(i).getItemNo());
             Log.e("m2==", m + "");*/
       ArrayList<serialModel> returnListserial2 = new ArrayList<>();
           //  Log.e(" voucherNo==",  voucherNo_ReturnNo + "");
             returnListserial2=dataBase.getAllSerialItemsByVoucherNoAndItems(voucherNo_ReturnNo,returnListSerial.get(i).getItemNo());
            // Log.e(" returnListserial2==",  returnListserial2.size() + "");

             if( returnListserial2.size()==0)
        {
            int m = dataBase.updateItemReturnedInVocherDetails(voucherNo_ReturnNo, returnListSerial.get(i).getItemNo());
           //  Log.e("m2==", m + "");
        }

         }



        }
    }catch (Exception e){
    Log.e("e==",e.getMessage());
    }

  }

   /* private void saveSerial2() {

        for(int i=0;i<allseriallist.size();i++)
        {
              dataBase.updateSerialReturnedInBaseInvoice(allseriallist.get(i).getVoucherNo(),allseriallist.get(i).getSerialCode());

            returnListSerial.get(i).setVoucherNo(max_voucherNumber+"");
            // Log.e("returnListSerial","getVoucherNo=after="+returnListSerial.get(i).getVoucherNo());
            dataBase.add_Serial(returnListSerial.get(i));
        }
    }*/

   private void deleteItemNotSelected() {
     Log.e("deleteItemsDetail","1returnListSerial="+returnListSerial.size()+"\t  del="+listItemDeleted.size());
      if(allitemsdata.size()!=0)
      {

       returnListSerial.clear();
       returnListSerial.addAll(allitemsdata);

      }
     //  Log.e("deleteItemsDetail","2returnListSerial="+returnListSerial.size()+"\t  del="+listItemDeleted.size());


       for(int i=0;i<returnListSerial.size();i++)
        {
            if(returnListSerial.get(i).isClicked==0)
            {
                listItemDeleted.add(returnListSerial.get(i).getItemNo());

                returnListSerial.remove(i);
                if(i!=0)
                i--;
                else i=-1;
            }
            else {
                returnListSerial.get(i).setKindVoucher("506");
                returnListSerial.get(i).setDateVoucher(curent);

               // Log.e("deleteItemNotSelected","returnListSerial"+returnListSerial.get(i).getPriceItem());
            }
        }
   //Log.e("deleteItemsDetail","2returnListSerial="+returnListSerial.size()+"\t  del="+listItemDeleted.size());

        deleteItemsDetail();

    }

    private void deleteItemsDetail() {
        listItemsMain.clear();
        Log.e("deleteItemsDetail33",listItemsReturn.size()+"\t  del"+listItemDeleted.size());
        Log.e("deleteItemsDetail44",returnListSerial.size()+"\t  del"+listItemDeleted.size());


        for(int i=0;i<returnListSerial.size();i++)
        {
            for (int j=0;j<listItemsReturn.size();j++)
            {
                Log.e("vvv==", returnListSerial.get(i).getQty()+"  "+listItemsReturn.get(j).getItemNo().trim());
                if(returnListSerial.get(i).getItemNo().trim().equals(listItemsReturn.get(j).getItemNo().trim()))
                {
                    Log.e("returnListSerial","listItemsReturn.get(i)"+listItemsReturn.get(j).getQty());
                    if(!returnListSerial.get(i).getSerialCode().equals(""))
                    {
                        listItemsReturn.get(j).setQty(0);
                    }else {
                        listItemsReturn.get(j).setQty(Float.parseFloat(returnListSerial.get(i).getQty()));
                }


                    listItemsMain.add(listItemsReturn.get(j));

                }
            }


        }
        Log.e(" listItemsMain==", listItemsMain.size()+"");

        // Log.e("deleteItemsDetail","2listItemsMain"+listItemsMain.size());
        listItemsMain=removeDuplicates(listItemsMain);
       // Log.e("deleteItemsDetail","3listItemsMain"+listItemsMain.size());
        textView_save.setEnabled(true);
    }

    private void fillitemNoPrc() {
        for (int i=0;i<returnListSerial.size();i++)
        {
            for(int j=0;j<listItemsReturn.size();j++)
            {
                if(returnListSerial.get(i).getItemNo().toString().trim().equals(listItemsReturn.get(j).getItemNo().toString().trim()))
                {
                    float salePrice=1,oneDisc=0;
                    try {
                        oneDisc=listItemsReturn.get(j).getDisc()/listItemsReturn.get(j).getQty();
                        if(oneDisc!=0)
                            salePrice=listItemsReturn.get(j).getPrice()-oneDisc;
                        else salePrice=listItemsReturn.get(j).getPrice();
                     //   Log.e("salePrice",""+salePrice);
                    }catch (Exception e){
                       // Log.e("salePrice","Exception"+e.getMessage());
                        salePrice=listItemsReturn.get(j).getPrice();
                    }

                    returnListSerial.get(i).setPriceItem(salePrice);
                    returnListSerial.get(i).setPriceItemSales(salePrice+"");



                   String itemName=dataBase.getItemName(returnListSerial.get(i).getItemNo().toString().trim());
                           listItemsReturn.get(j).setItemName(itemName);


                    returnListSerial.get(i).setItemName( itemName);
                   // Log.e("getItemNo","returnListSerial.get(i).getItemNo()"+itemName);
                }

            }

        }
        fillZeroQty();
       // adapter.notifyDataSetChanged();
        fillAdapterData(ReturnByVoucherNo.this,returnListSerial,2);
        canChangePayMethod();
    }
    private void fillitemNoPrc_allItemType( ) {


        for (int i=0;i<allitemsdata.size();i++)
        {
            for(int j=0;j<listItemsReturn.size();j++)
            {
                if(allitemsdata.get(i).getItemNo().toString().trim().equals(listItemsReturn.get(j).getItemNo().toString().trim()))
                {
                    float salePrice=1,oneDisc=0,oneTax=0;
                    try {
                        if(listItemsReturn.get(j).getDisc()!=0)
                        oneDisc=listItemsReturn.get(j).getDisc()/listItemsReturn.get(j).getQty();
                        if(listItemsReturn.get(j).getTaxValue()!=0&&typeTax==0 )
                        {
                            oneTax= (float) (listItemsReturn.get(j).getTaxValue()/listItemsReturn.get(j).getQty());
                        }

//                        if(oneDisc!=0)
                            salePrice=listItemsReturn.get(j).getPrice()-oneDisc+oneTax;
//                       salePrice=listItemsReturn.get(j).getPrice();

                    }catch (Exception e){
                        // Log.e("salePrice","Exception"+e.getMessage());
                        salePrice=listItemsReturn.get(j).getPrice();
                    }

                allitemsdata.get(i).setPriceItem(salePrice);
                allitemsdata.get(i).setPriceItemSales(salePrice+"");



                    String itemName=dataBase.getItemName(allitemsdata.get(i).getItemNo().toString().trim());
                    listItemsReturn.get(j).setItemName(itemName);
                    listItemsReturn.get(j).setPrice(salePrice);
                listItemsReturn.get(j).setDisc(0);
                listItemsReturn.get(j).setDiscPerc("0");

               // Log.e("fillitemNoPrc_all==2=",allitemsdata.get(0).getPriceItem()+"");
                allitemsdata.get(i).setItemName( itemName);
                    // Log.e("getItemNo","returnListSerial.get(i).getItemNo()"+itemName);
                }
//                else {
//
//                }

            }

        }
        fillZeroQty();
        // adapter.notifyDataSetChanged();
        fillAdapterData(ReturnByVoucherNo.this,allitemsdata,2);
        canChangePayMethod();
    }
    private void canChangePayMethod() {
        if(voucherReturn.getPayMethod()==1)// cash
        {
            paymentTermRadioGroup.setVisibility(View.VISIBLE);
            payMethod_textView.setVisibility(View.GONE);
            paymentTermRadioGroup.check(R.id.cashRadioButton);

        }else {// credit
            paymentTermRadioGroup.setVisibility(View.GONE);
            payMethod_textView.setVisibility(View.VISIBLE);
        }
    }

    private void fillZeroQty() {
        for (int i=0;i<listItemsReturn.size();i++)
        {
           if(dataBase.HASSERAIAL(listItemsReturn.get(i).getItemNo())==1)
               listItemsReturn.get(i).setQty(0);
        }
    }


    public static void fillAdapterData(Context context, List<serialModel> serialModels,int flage) {
          Log.e("SerialReport2","SerialReport2");
          recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ReturnItemAdapter (serialModels,context,0 ,flage);
        recyclerView.setAdapter(adapter);


    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        Log.e("ReturnSerial", "" + requestCode);
        String serialBarcode = "";
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
               // Log.e("ReturnSerial", "cancelled scan");
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

               // Log.e("ReturnSerial", "onActivityResult" + Result.getContents());

                try {
                    serialBarcode = Result.getContents().trim();
                } catch (Exception e) {
                    serialBarcode = "";
                    Toast.makeText(this, "Error1" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                if(serialBarcode.length()!=0)
                {
                    serial_text.setText(serialBarcode.toString().trim());
                    getSerialVoucherNo();
                }else {
                    serial_text.setError(getResources().getString(R.string.invalidSerial));
                }

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void inflateBoomMenu() {
        String[] textListButtons=new String[]{};
        textListButtons=new String[]
                {

                        getResources().getString(R.string.print)

                        ,getResources().getString(R.string.export_to_pdf),
                        "export_to_ecxel"};


        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);

        for (int j = 0; j < 3; j++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(listImageIcone[j])
                    .textSize(12)
                    .normalText(textListButtons[j])
                    .textPadding(new Rect(5, 5, 5, 0))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index) {
                                case 0:
                                 printLayout();
                                    break;
                                case 1:
                                    exportToPdf();
                                    break;

                                case 2:
                                          exportToEx();
                                    break;

                            }
                        }
                    });
            bmb.addBuilder(builder);
        }
        // inflateMenuInsideText(view);

    }
    private void exportToEx() {



        LASTVOCHER.clear();
        LASTVOCHER2.clear();
        voucherNo_ReturnNo =dataBase.getLastVoucherNo(506)+"";
        LASTVOCHER=dataBase.getAllItemsBYVOCHER(voucherNo_ReturnNo,506);
        ExportToExcel exportToExcel=new ExportToExcel();
        exportToExcel.createExcelFile(ReturnByVoucherNo.this,"ReturnVocher.xls",14,LASTVOCHER);

    }
    public  void exportToPdf(){

        LASTVOCHER.clear();
        LASTVOCHER2.clear();
        voucherNo_ReturnNo =dataBase.getLastVoucherNo(506)+"";
        LASTVOCHER=dataBase.getAllItemsBYVOCHER(voucherNo_ReturnNo,506);


        PdfConverter pdf =new PdfConverter(ReturnByVoucherNo.this);

       pdf.exportListToPdf(LASTVOCHER,"ReturnVocher","",12);
       // Log.e("ReturnVocher",returnListSerial.size()+"");
    }

    private void printLayout() {
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
                                    Intent O1= new Intent(ReturnByVoucherNo.this, bMITP.class);
                                    O1.putExtra("printKey", "10");
                                    O1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    O1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(O1);
                                    break;


                            }
                        } else {
                            Toast.makeText(ReturnByVoucherNo.this, "please chose printer setting", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReturnByVoucherNo.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(ReturnByVoucherNo.this, "Please set Printer Setting", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Toast.makeText(ReturnByVoucherNo.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ReturnByVoucherNo.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();

                }
            } else {
                // hiddenDialog();
            }
        }
        catch(Exception e){
            Toast.makeText(ReturnByVoucherNo.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
        }

    }

    private void showprintDialog() {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run() {
                new SweetAlertDialog(ReturnByVoucherNo.this, SweetAlertDialog.BUTTON_CONFIRM)
                        .setTitleText("Confirm")
                        .setContentText(getResources().getString(R.string.action_print_voucher2))
                        .setConfirmButton(getResources().getString(R.string.app_yes), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                               printLayout();
                                returnListSerial.clear();

                                sweetAlertDialog.dismiss();


                            }

                        })
                        .setCancelButton(getResources().getString(R.string.no), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                returnListSerial.clear();
                                fillAdapterData(ReturnByVoucherNo.this,returnListSerial,1);
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });



    }

}
//VE_ITEMSERIALS
//
//http://localhost:8085/GetVE_ITEMSERIAL?CONO=295&VHFNO=1900000169
//
//----------------------------------------------------------------
//VE_SALES_VOUCHER_M
//
//http://localhost:8085/GetVE_M?CONO=295&VHFNO=6
//
//----------------------------------------------------------------
//VE_SALES_VOUCHER_D
//
//http://localhost:8085/GetVE_D?CONO=295&VHFNO=6




        /*    if (returnListSerial.size() != 0) {
                    fillitemNoPrc_allItemType(allitemsdata);
                    } else {
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
public void run() {
        new SweetAlertDialog(ReturnByVoucherNo.this, SweetAlertDialog.BUTTON_CONFIRM)
        .setTitleText("warning")
        .setContentText("No Items To Returned")
        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
@Override
public void onClick(SweetAlertDialog sweetAlertDialog) {
        sweetAlertDialog.dismiss();


        }

        })

        .show();
        }
        });
        }*/