package com.dr7.salesmanmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.Reports;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter.serialValueStock;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;
import static com.dr7.salesmanmanager.SalesInvoice.serialValueUpdated;
import static com.dr7.salesmanmanager.SalesInvoice.updatedSerial;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.StockRequest.listSerialInventory;

public class Stock_Activity extends AppCompatActivity implements    StockRequest.StockInterFace, AddItemsStockFragment.AddItemsInterface {
    private StockRequest stockRequest;
   public static String intentData="";
    DatabaseHandler databaseHandler;
    public  static  List <serialModel> copyListSerial,listMasterSerialForBuckup_stock;

    @Override
    public void addItemsStockToList(List<Item> itemsList) {
        stockRequest.getItemsStockList().addAll(itemsList);
        // salesInvoice.itemsListAdapter.ite setItemsList(itemsList);
        stockRequest.itemsListAdapter.notifyDataSetChanged();
        stockRequest.calculateTotals();
    }

    public interface StockInterFace {
        public void displayFindItemStockFragment();
    }

    StockRequest.StockInterFace stockInterFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(Stock_Activity.this);
        setContentView(R.layout.activity_stock_);
        listMasterSerialForBuckup_stock=new ArrayList<>();
        intentData = getIntent().getStringExtra("serial");
        Log.e("intentData",""+intentData);
        if(intentData.equals("read"))
        {
            this.setTitle("Inventory Shelf");
        }
        databaseHandler=new DatabaseHandler(Stock_Activity.this);
        displayStockRequest();
    }
    private void displayStockRequest() {

        Log.e("displayStockRequest","yes");
        FragmentManager fragmentManager = getSupportFragmentManager();
        stockRequest = new StockRequest();
        stockRequest.setListener(this);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        getFragmentManager().popBackStack();
        transaction.addToBackStack(null);
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragmentContainer, stockRequest);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    @Override
    public void displayFindItemStockFragment() {
        try {
            AddItemsStockFragment addItemsStockFragment = new AddItemsStockFragment();
            addItemsStockFragment.setCancelable(true);
            addItemsStockFragment.setListener(this);
            addItemsStockFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            String x = e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i= new Intent( Stock_Activity.this,MainActivity.class);
        startActivity(i);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("MainActivity", ""+requestCode);
        String serialBarcode="";
//        if (requestCode == 0x0000c0de) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.e("MainActivity", "cancelled scan");
                Toast.makeText(Stock_Activity.this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("Stock_Activity", "onActivityResult" + Result.getContents());

                try {
                    serialBarcode = Result.getContents().trim();
                }
                catch (Exception e){
                    serialBarcode="";
                    Toast.makeText(Stock_Activity.this, "Error1"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
              //  serialValueStock.setText(serialBarcode.toString().trim());

//                try {
//                    String ItemNo=databaseHandler.isSerialCodeExist(serialBarcode.trim());
//
//                        if(ItemNo.trim().equals("not"))
//                        {

                                if(checkInTotalList(serialBarcode.trim()))
                                {
                            Log.e("Stock_Activity", "onActivityResult" +serialBarcode.toString().trim());
                                    serialValueStock.setText(serialBarcode.toString().trim());


                                }
                                else {
                                    new SweetAlertDialog(Stock_Activity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(Stock_Activity.this.getString(R.string.warning_message))
                                            .setContentText(Stock_Activity.this.getString(R.string.duplicate)+"\t"+Stock_Activity.this.getResources().getString(R.string.inThisVoucher))

                                            .show();

                                }
//
//
//
//
//                        }
//                        else {
//
//
//                            if(!ItemNo.equals("")){
//                                new SweetAlertDialog(Stock_Activity.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText(Stock_Activity.this.getString(R.string.warning_message))
//                                        .setContentText(Stock_Activity.this.getString(R.string.invalidSerial)+"\t"+serialBarcode+"\t"+Stock_Activity.this.getString(R.string.forItemNo)+ItemNo)
//                                        .setConfirmButton(Stock_Activity.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                                openSmallScanerTextView();
//                                                sweetAlertDialog.dismissWithAnimation();
//                                            }
//                                        })
//                                        .show();
//                            }else {
//                                new SweetAlertDialog(Stock_Activity.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText(Stock_Activity.this.getString(R.string.warning_message))
//                                        .setContentText(Stock_Activity.this.getString(R.string.invalidSerial)+"\t"+serialBarcode)
//                                        .setConfirmButton(Stock_Activity.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//
////                                                openSmallScanerTextView();
//                                                sweetAlertDialog.dismissWithAnimation();
//
//                                            }
//                                        })
//                                        .show();
//
//                            }
//
//                        }
//
//
//
//
//
//                }catch (Exception e){
//                    Toast.makeText(Stock_Activity.this, "Error2"+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }





            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
//        }


    }
    public boolean checkInTotalList(String s) {
        boolean existInTotal=false;
        if(listSerialInventory.size()!=0){
            // Log.e("checkInTotalList","indexOf"+listSerialTotal.indexOf(s.toString().trim()));
            for(int j=0;j<listSerialInventory.size();j++)
            {
                if(listSerialInventory.get(j).getSERIAL_NO().equals(s.toString().trim()))
                {
                    return  false;
                }

            }

        }
        return  true;
    }
}
