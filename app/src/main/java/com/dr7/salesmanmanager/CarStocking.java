package com.dr7.salesmanmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dr7.salesmanmanager.Adapters.CarStockAdapter;
import com.dr7.salesmanmanager.Adapters.ItemsdeffAdapter;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.ImportJason.voucherReturn;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.salesManNo;

public class CarStocking extends AppCompatActivity {
    float Sumsubtotal=0;
    float    Sumnettotal=0;
    double SumTax=0;
    Date currentTimeAndDate;
    SimpleDateFormat df, df2,formatTime;
    private int salesMan;
    public static   List<SalesManItemsBalance> getSalesManItemsQTY=new ArrayList<>();
    public static   List<SalesManItemsBalance> listOfDefferntsItem=new ArrayList<>();
    public int voucherNumber;
   Voucher voucher;
    public  static  String voucherDate, voucherYear,time,timevocher;
    DatabaseHandler databaseHandler;
    RecyclerView recyclerView;
    TextInputLayout search;
  RecyclerView.LayoutManager layoutManager,layoutManager1;
  public static   EditText searchItemsEdt;
AppCompatButton save,cancel;
    public static int highligtedItemPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_stocking);
        layoutManager = new LinearLayoutManager(CarStocking.this);

        init();
        getSalesManItemsQTY.clear();
        listOfDefferntsItem.clear();
        salesMan = Integer.parseInt(Login.salesMan.trim());

        getSalesManItemsQTY=databaseHandler.getSalesManItemsQTY(Login.salesMan);
        recyclerView. setLayoutManager(layoutManager);
        highligtedItemPosition=-1;
        Log.e("getSalesManItemsQTY.size",getSalesManItemsQTY.size()+"");
        recyclerView.setAdapter(new CarStockAdapter(CarStocking.this,getSalesManItemsQTY));


        search.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count =0;
                for (int i = 0; i < getSalesManItemsQTY.size(); i++)
                    if (searchItemsEdt.getText().toString().equals(getSalesManItemsQTY.get(i).getItemNo()))
                    {

                        count =1;
                        getSalesManItemsQTY.add(0, getSalesManItemsQTY.get(i));
                        getSalesManItemsQTY.remove(i+1);
                        Log.e("getSalesManItemsQTY.size",getSalesManItemsQTY.size()+"");
                        CarStockAdapter.  respon.setText("a");
                        highligtedItemPosition = 0;
                        recyclerView.setAdapter(new CarStockAdapter(CarStocking.this,getSalesManItemsQTY));

                    }
          if(count ==0) Toast.makeText(CarStocking.this, "this ItemNumber does not exists", Toast.LENGTH_SHORT).show();
            }

        });
        search.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CarStocking.this,ScanActivity.class);
                i.putExtra("key","7");
                startActivity(i);
            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getSalesManItemsQTY.size(); i++)
                   if( getSalesManItemsQTY.get(i).getAct_Qty()!=0)
                    if (getSalesManItemsQTY.get(i).getAct_Qty() < getSalesManItemsQTY.get(i).getQty()) {
                        listOfDefferntsItem.add(getSalesManItemsQTY.get(i));

                    }
          Log.e("listOfDefferntsItem==",listOfDefferntsItem.size()+"") ;


                if(listOfDefferntsItem.size()!=0) {

                    OpenDailog();
                }else
                    {
                        getSalesManItemsQTY.clear();
                        listOfDefferntsItem.clear();
                        recyclerView.setAdapter(new CarStockAdapter(CarStocking.this,getSalesManItemsQTY));

                        new SweetAlertDialog(CarStocking.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("")
                                .setContentText(getResources().getString(R.string.succsesful))
                                .show();

                    }

            }


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CarStocking.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    void init(){
        getSalesManItemsQTY.clear();
        listOfDefferntsItem.clear();
        getTimeAndDate();
        save=findViewById(R.id.save);
            cancel=findViewById(R.id.cancel);
        searchItemsEdt=findViewById(R.id.searchItemsEdt8);
      search=findViewById(R.id.searchItems_textField);
        recyclerView=findViewById(R.id.rec);
        databaseHandler=new DatabaseHandler(CarStocking.this);
        LinearLayout linearMain=findViewById(R.id.linearMain);
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


    }
    private void OpenDailog() {

        final Dialog dialog = new Dialog(CarStocking.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.items_dailog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        layoutManager1 = new LinearLayoutManager(CarStocking.this);
    LinearLayout    main=dialog.findViewById(R.id.main);
        try{
            if(languagelocalApp.equals("ar"))
            {
                main.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    main.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            main.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }




        RecyclerView recyclerViewdialog=dialog.findViewById(R.id.rec);

        recyclerViewdialog. setLayoutManager(layoutManager1);
        recyclerViewdialog.setAdapter(new ItemsdeffAdapter(CarStocking.this,listOfDefferntsItem));

        List<Settings> settings=    databaseHandler.getAllSettings();
        voucherNumber = databaseHandler.getMaxSerialNumberFromVoucherMaster(504) + 1;
        timevocher=getCurentTimeDate(2);







        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getSalesManItemsQTY.clear();
                listOfDefferntsItem.clear();

                recyclerView.setAdapter(new CarStockAdapter(CarStocking.this,getSalesManItemsQTY));
            }
        });
        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    voucher = new Voucher();
                    voucher.setCompanyNumber(Integer.parseInt(settings.get(0).getCoNo()));
                    voucher.setVoucherNumber(voucherNumber);
                    voucher.setVoucherType(504);
                    voucher.setVoucherDate(voucherDate);
                    voucher.setSaleManNumber(salesMan);
                    voucher.setVoucherDiscount(0);
                    voucher.setVoucherDiscountPercent(0);
                    voucher.setRemark("");
                    voucher.setPayMethod(0);
                    voucher.setIsPosted(0);
                    voucher.setTotalVoucherDiscount(0);

                    // voucher.setSubTotal(subTotal);
                    voucher.setTax(0);
                    float netSales = 0;
                    voucher.setNetSales(netSales);
                    voucher.setCustName("");
                    voucher.setCustNumber(salesMan + "");
                    voucher.setVoucherYear(Integer.parseInt(voucherYear));
                    voucher.setTime(timevocher);
                    voucher.setORIGINALvoucherNo(voucherNumber);
                    voucherReturn.setTaxTypa(0);
                    //////////

                    for (int i = 0; i < listOfDefferntsItem.size(); i++) {
                        ItemsMaster itemsMaster = databaseHandler.getItemMasterForItem(listOfDefferntsItem.get(i).getItemNo());
                        ItemUnitDetails itemUnitDetails = databaseHandler.getItemUnitDetails(listOfDefferntsItem.get(i).getItemNo());


                        Item item = new Item();
                        item.setVoucherNumber(voucherNumber);
                        item.setVoucherType(504);
                        item.setItemNo(listOfDefferntsItem.get(i).getItemNo());

                        item.setItemName(itemsMaster.getName());

                        float Act_Qty = (float) listOfDefferntsItem.get(i).getAct_Qty();
                        float ConvRate = (float) itemUnitDetails.getConvRate();
                        float Qty = (float) listOfDefferntsItem.get(i).getQty();
                        item.setQty((Qty - Act_Qty) * ConvRate);
                        Log.e("getPosPrice====", "  " + itemsMaster.getPosPrice());
                        float Pric = (float) itemsMaster.getPosPrice();
                        Log.e("Pric====", Pric + "  " + item.getQty());
                        item.setPrice(Pric);
                        item.setAmount(item.getQty() * Pric);
                        item.setBonus(0);
                        item.setDisc(0);
                        item.setDiscPerc("0");
                        item.setVoucherDiscount(0);

                        item.setCompanyNumber(Integer.parseInt(settings.get(0).getCoNo()));
                        item.setYear(voucherYear);
                        item.setIsPosted(0);
                        item.setDescreption("");
                        item.setORIGINALvoucherNo(voucherNumber);

                        if (itemUnitDetails.getConvRate() == 1) {
                            item.setWhich_unit("0");
                            item.setUnit("0");
                        } else {
                            item.setWhich_unit("1");
                            item.setUnit("1");
                        }

                        item.setWhichu_qty(itemUnitDetails.getConvRate() + "");
                        item.setWhich_unit_str(itemUnitDetails.getUnitId());
                        item.setEnter_qty(String.valueOf(Qty - Act_Qty));
                        item.setEnter_price(itemsMaster.getPosPrice() + "");
                        item.setUnit_barcode("");

                        float taxperc = Float.parseFloat(databaseHandler.gettaxpercforItem(listOfDefferntsItem.get(i).getItemNo())) / 100;
                        float tax = Pric * taxperc;

                        item.setTaxPercent(taxperc);
                        item.setTax(tax);
                        item.setTaxValue(tax * item.getQty());


                        double subtotal = 0;
                        int taxkind = settings.get(0).getTaxClarcKind();
                        if (taxkind == 1)//شامل
                        {
                            subtotal = item.getAmount() - item.getTaxValue();
                            Sumsubtotal += subtotal;
                        } else {
                            // ضريبة خاضعة
                            subtotal = item.getAmount();
                            Sumsubtotal += subtotal;
                        }

                        double nettotal = 0;
                        nettotal = subtotal + item.getTaxValue();
                        Sumnettotal += nettotal;
                        SumTax += item.getTaxValue();
                        voucher.setSubTotal(Sumsubtotal);
                        voucher.setNetSales(Sumnettotal);
                        voucher.setTax(SumTax);

                        databaseHandler.addItem(item);
                        databaseHandler.updateSalesManItemsBalance1((Qty - Act_Qty), salesMan, item.getItemNo());

                    }

                    Log.e("listOfDefferntsItem", listOfDefferntsItem.size() + "");
                    databaseHandler.addVoucher(voucher);
                    databaseHandler.updateVoucherNo(voucherNumber, 504, 1);

                    getSalesManItemsQTY.clear();
                    listOfDefferntsItem.clear();
                    recyclerView.setAdapter(new CarStockAdapter(CarStocking.this, getSalesManItemsQTY));
                    dialog.dismiss();
                }catch(Exception exception){

                }
            }

        });



    }
    public void getTimeAndDate() {
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        formatTime=new SimpleDateFormat("hh:mm:ss");

        voucherDate = df.format(currentTimeAndDate);
        voucherDate = convertToEnglish(voucherDate);
        time=formatTime.format(currentTimeAndDate);
        time=convertToEnglish(time);
        Log.e("time",""+time);
        df2 = new SimpleDateFormat("yyyy");
        voucherYear = df2.format(currentTimeAndDate);
        voucherYear = convertToEnglish(voucherYear);
    }
    public String getCurentTimeDate(int flag){
        String dateCurent,timeCurrent,dateTime="";
        Date currentTimeAndDate;
        SimpleDateFormat dateFormat, timeformat;
        currentTimeAndDate = Calendar.getInstance().getTime();
        if(flag==1)// return date
        {

            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateCurent = dateFormat.format(currentTimeAndDate);
            dateTime=convertToEnglish(dateCurent);

        }
        else {
            if(flag==2)// return time
            {
                timeformat = new SimpleDateFormat("hh:mm:ss");
                dateCurent = timeformat.format(currentTimeAndDate);
                dateTime=convertToEnglish(dateCurent);
            }
        }
        return dateTime;

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

}