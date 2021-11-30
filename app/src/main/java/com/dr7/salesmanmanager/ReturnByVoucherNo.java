package com.dr7.salesmanmanager;

import static com.dr7.salesmanmanager.ImportJason.itemSerialList;
import static com.dr7.salesmanmanager.ImportJason.listCustomerInfo;
import static com.dr7.salesmanmanager.ImportJason.listItemsReturn;
import static com.dr7.salesmanmanager.ImportJason.returnListSerial;
import static com.dr7.salesmanmanager.ImportJason.voucherReturn;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Adapters.ReturnItemAdapter;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SerialReport;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReturnByVoucherNo extends AppCompatActivity {
    RecyclerView recyclerView;
    public static List<serialModel> allseriallist =new ArrayList<>();

    DatabaseHandler databaseHandler;
    public static ReturnItemAdapter adapter;
    EditText serial_text,voucherNo_text;
    public TextView date,getDataVoucher,payMethod_textView,textView_save,textView_cancel,getserialData,editSerial;
    int   max_voucherNumber=0;
    GeneralMethod generalMethod;
    String voucherNo="";
    ImportJason importJason;
    public  static   TextView loadSerial;
    List<String> listItemDeleted=new ArrayList<>();
    public  ArrayList<Item> listItemsMain = new ArrayList<>();
    public  DatabaseHandler dataBase;
    float total=0;
    String curent="";
    public RadioGroup paymentTermRadioGroup;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_by_voucher_no);
        initialView();
        getVoucherNo();
       // getLocalData();

    }

    private void getVoucherNo() {
        max_voucherNumber = dataBase.getMaxSerialNumberFromVoucherMaster(506) + 1;
        Log.e("saveVoucherMaster","voucherNumber="+max_voucherNumber);
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initialView() {
        dataBase=new DatabaseHandler(this);
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
                saveData();
            }
        });
        getserialData=findViewById(R.id.getserialData);
        getserialData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readBarcode();
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
                {
                    voucherNo_text.setError(null);
                    voucherNo=voucherNo_text.getText().toString().trim();
                   // Log.e("voucherNo",""+voucherNo);
                    getDataForVoucherNo();


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
                    if(editable.toString().equals("fillSerial"))
                    {

                        if(returnListSerial.size()!=0)
                        fillAdapterData(returnListSerial);
                        else showNotFound();

                    }
                    else if(editable.toString().equals("fillpayMethod"))
                    {
                      //  Log.e("voucherReturn",""+voucherReturn.getPayMethod()+"\t"+voucherReturn);
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
                       // Log.e("editable",""+editable.toString());
                        fillitemNoPrc();
                    }
                        else if(editable.toString().equals("NotSameCustomer"))
                        {
                            showDialogNotSameCustomer();
                            clearData();
                        }
                        else if(editable.toString().equals("No Parameter Found"))
                        {
                            showNotFound();
                        }
                        else if(editable.toString().contains("VHFNO")){
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
                            showReturnedSerial();
                        }
                }

            }
        });
        curent=generalMethod.getCurentTimeDate(1);
        serial_text.setEnabled(false);
    }

    private void showInvalidSerial() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.noVoucherForThisSerial))
                .show();
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

    private void getDataForVoucherNo() {
        if(! getLocalDataBase())
            importJason.getSerialData(voucherNo);
        else {// exist in local list
            getVoucherLocal();
        }
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
        voucherNo=voucherNoSerial;
        getDataForVoucherNo();
    }

    private void getVoucherLocal() {
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

    }

    private boolean getLocalDataBase() {
        returnListSerial=dataBase.getAllSerialItemsByVoucherNo(voucherNo);
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

       saveSerial();
      saveVoucherD();
      saveVoucherMaster();
     dataBase.updateVoucherNo(max_voucherNumber, 506, 0);
    clearData();
        saveSuccses();
       // exportData();

    }

    private void exportData() {

       boolean isPosted = dataBase.isAllposted();
      //  Log.e("isPostedExport","1"+isPosted);
        if (!isPosted) {

          //  Log.e("isPostedExport","2"+isPosted);
            ExportJason objJson = null;
            try {
                objJson = new ExportJason(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
//                        objJson.startExportDatabase();
                objJson.startExport();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(this, getResources().getString(R.string.saveSuccessfuly), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData() {

        listItemsMain.clear();
        returnListSerial.clear();
        listItemDeleted.clear();
        voucherNo_text.setText("");
        getVoucherNo();
        fillAdapterData(returnListSerial);
        payMethod_textView.setText("");
        paymentTermRadioGroup.setVisibility(View.GONE);
        payMethod_textView.setVisibility(View.VISIBLE);

    }

    private void saveVoucherMaster() {
       // Log.e("getPayMethod()",""+voucherReturn.getPayMethod());
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

      voucherReturn.setVoucherDate(generalMethod.getCurentTimeDate(1));
      voucherReturn.setCustNumber(CustomerListShow.Customer_Account);
      voucherReturn.setCustName(CustomerListShow.Customer_Name);
      voucherReturn.setSubTotal(total);
      voucherReturn.setNetSales(total);
      // calck netSales
      dataBase.addVoucher(voucherReturn);
     // Log.e("getPayMethod()",""+voucherReturn.getPayMethod());

    }

    private void saveVoucherD() {
        String curent=generalMethod.getCurentTimeDate(1);

        for(int i=0;i<listItemsMain.size();i++)
        {
            listItemsMain.get(i).setVoucherNumber(max_voucherNumber);
            listItemsMain.get(i).setVouchDate(curent);
            listItemsMain.get(i).setVoucherType(506);
            listItemsMain.get(i).setIsPosted(0);
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
                    float qty=listItemsMain.get(k).getQty();
                    qty=qty+1;
                    listItemsMain.get(k).setQty(qty);
                    listItemsMain.get(k).setPrice(returnListSerial.get(i).getPriceItem());
                  //  Log.e("calculateTotalc","qqqq="+listItemsMain.get(k).getQty());
                }
            }

        }
        calcTotalVoucher();

    }

    private void calcTotalVoucher() {

        for (int i=0;i<listItemsMain.size();i++)
        {
            total+=listItemsMain.get(i).getQty()*listItemsMain.get(i).getPrice();
        }
       // Log.e("calcTotalVoucher","total"+total);
    }

    private void saveSerial() {
        for(int i=0;i<returnListSerial.size();i++)
        {
            Log.e("returnListSerial","getVoucherNo"+returnListSerial.get(i).getVoucherNo());
            dataBase.updateSerialReturnedInBaseInvoice(returnListSerial.get(i).getVoucherNo(),returnListSerial.get(i).getSerialCode());

            returnListSerial.get(i).setVoucherNo(max_voucherNumber+"");
            Log.e("returnListSerial","getVoucherNo=after="+returnListSerial.get(i).getVoucherNo());
            dataBase.add_Serial(returnListSerial.get(i));
        }
    }

    private void deleteItemNotSelected() {
      //  Log.e("deleteItemsDetail","1returnListSerial="+returnListSerial.size()+"\t  del="+listItemDeleted.size());

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
               // returnListSerial.get(i).setVoucherNo(max_voucherNumber+"");
            }
        }
        Log.e("deleteItemsDetail","2returnListSerial="+returnListSerial.size()+"\t  del="+listItemDeleted.size());

        deleteItemsDetail();

    }

    private void deleteItemsDetail() {
        listItemsMain.clear();
       // Log.e("deleteItemsDetail","1listItemsReturn"+listItemsReturn.size()+"\t  del"+listItemDeleted.size());
        for(int i=0;i<returnListSerial.size();i++)
        {
            for (int j=0;j<listItemsReturn.size();j++)
            {
                if(returnListSerial.get(i).getItemNo().trim().equals(listItemsReturn.get(j).getItemNo().trim()))
                {
                    listItemsReturn.get(j).setQty(0);
                    listItemsMain.add(listItemsReturn.get(j));

                }
            }

        }
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
                        Log.e("salePrice",""+salePrice);
                    }catch (Exception e){
                        Log.e("salePrice","Exception"+e.getMessage());
                        salePrice=listItemsReturn.get(j).getPrice();
                    }

                    returnListSerial.get(i).setPriceItem(salePrice);
                    returnListSerial.get(i).setPriceItemSales(salePrice+"");



                   String itemName=dataBase.getItemName(returnListSerial.get(i).getItemNo().toString().trim());
                           listItemsReturn.get(j).setItemName(itemName);


                    returnListSerial.get(i).setItemName( itemName);
                    Log.e("getItemNo","returnListSerial.get(i).getItemNo()"+itemName);
                }

            }

        }
        fillZeroQty();
       // adapter.notifyDataSetChanged();
        fillAdapterData(returnListSerial);
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
            listItemsReturn.get(i).setQty(0);
        }
    }


    public void fillAdapterData( List<serialModel> serialModels) {
        Log.e("SerialReport2","SerialReport2");
        recyclerView.setLayoutManager(new LinearLayoutManager(ReturnByVoucherNo.this));
        adapter = new ReturnItemAdapter (serialModels,ReturnByVoucherNo.this,0 );
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
                Log.e("ReturnSerial", "cancelled scan");
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("ReturnSerial", "onActivityResult" + Result.getContents());

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