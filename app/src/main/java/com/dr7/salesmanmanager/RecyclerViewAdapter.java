package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Cheque;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.LinearLayout.VERTICAL;

import static com.dr7.salesmanmanager.AddItemsFragment2.total_items_quantity;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.SalesInvoice.discountRequest;
import static com.dr7.salesmanmanager.SalesInvoice.size_customerpriceslist;
import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.Serial_Adapter.errorData;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    SalesInvoice.SalesInvoiceInterface salesInvoiceInterfaceListener;
    private List<Item> items;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context cont;
    int current_itemHasSerial=0;
    private AddItemsFragment2 context;
    Date currentTimeAndDate;
    SimpleDateFormat df, df2,formatTime;
    String voucherDate, voucherYear,time;
    CompanyInfo companyInfo;
    boolean added = false;
    DatabaseHandler MHandler;
    DecimalFormat threeDForm ;
    int settingPriceCus=0,showItemImageSetting=0;
    List<String> localItemNumber;
    boolean itemInlocalList=false;
    public static EditText Serial_No,item_serial;
    public static final int REQUEST_Camera_Serial = 22;
    public  boolean isFoundSerial=false;
    public   static  int flag=0,counterSerial=0,counterBonus=0;
     RecyclerView serial_No_recyclerView;
   public static ArrayList<serialModel> serialListitems= new ArrayList<>();
    public static ArrayList<serialModel> listSerialAllItems= new ArrayList<>();
    public static serialModel serial;
    public static EditText unitQty, bonus;
    public String exist="";
    public  static  String curentSerial="";
    public  static   String araySerial[];
    Bitmap  itemBitmap;
    public PhotoView photoView, photoDetail;
    public  static TextView checkState_recycler;
    requestAdmin request;
    int typeRequest=0,haveResult=0,approveAdmin=0;
    LinearLayout mainRequestLinear;;

    public RecyclerViewAdapter(List<Item> items, AddItemsFragment2 context) {
        this.items = items;
        this.filterList = items;
        this.context = context;
        for (int i = 0; i <= items.size(); i++) {
            isClicked.add(0);
        }
         cont=context.getActivity();
        MHandler = new DatabaseHandler(cont);
        settingPriceCus=MHandler.getAllSettings().get(0).getPriceByCust();
        showItemImageSetting=MHandler.getAllSettings().get(0).getShowItemImage();
        localItemNumber= new ArrayList<>();
//        this.listBitmap=listItemImage;

        Log.e("settingPriceCus",""+settingPriceCus);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_listview, parent, false);

        return new viewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {

        holder.setIsRecyclable(false);
//        if (isClicked.get(position) == 0)
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#455A64"));
//        else
//            holder.linearLayout.setBackgroundColor(R.color.done_button);

        holder.itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());

        holder.itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.itemName.setMovementMethod(new ScrollingMovementMethod());
        holder.tradeMark.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.category.setText("" + items.get(holder.getAdapterPosition()).getCategory());

        if(MHandler.getAllSettings().get(0).getHide_qty()==1) {
            holder.row_qty.setVisibility(View.GONE);
//            holder.unitQty.setVisibility(View.GONE);
        }
        else{
            holder.unitQty.setText("" + items.get(holder.getAdapterPosition()).getQty());
        }
//        if(settingPriceCus==1)
//        {
//            if(checkTypePriceTable(items.get(holder.getAdapterPosition()).getItemNo())){
//                holder.imagespecial.setVisibility(View.VISIBLE);
//            }
//            else{
//                holder.imagespecial.setVisibility(View.GONE);
//            }
//        }
//        else{
//            holder.imagespecial.setVisibility(View.GONE);
//        }
        // second solution is you can set the path inside decodeFile function

        holder.imagespecial.setVisibility(View.VISIBLE);
        if(showItemImageSetting==1)
        {
            if(items.get(position).getItemPhoto()!= null) {
//            itemBitmap = StringToBitMap(items.get(position).getItemPhoto());
                holder.imagespecial.setImageBitmap(items.get(position).getItemPhoto());
            }
            else {
                holder.imagespecial.setImageDrawable(this.context.getResources().getDrawable(R.drawable.specialred));
            }

//
            holder.imagespecial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    itemBitmap=StringToBitMap(items.position).getItemPhoto()get(holder.getAdapterPosition()).getItemPhoto());
                    showImageOfCheck(items.get(position).getItemPhoto());
                }
            });
        }
        else {
            holder.imagespecial.setVisibility(View.GONE);
        }




        holder.price.setText(convertToEnglish( threeDForm.format(items.get(holder.getAdapterPosition()).getPrice()))+"\t\tJD");

        Log.e("format",""+ threeDForm.format(items.get(holder.getAdapterPosition()).getPrice()));
//       *******************************//////////////////////*
        holder.tax.setText("" + items.get(holder.getAdapterPosition()).getTaxPercent());
        holder.barcode.setText(items.get(holder.getAdapterPosition()).getBarcode());
        holder.posprice.setText(items.get(holder.getAdapterPosition()).getPosPrice()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(final View view) {
                typeRequest=0;
                haveResult=0;
                itemInlocalList=false;

                serialListitems=new ArrayList<>();
                listSerialAllItems= new ArrayList<>();
                counterSerial=0;
                counterBonus=0;
                for (int i = 0; i < localItemNumber.size(); i++) {
                    if (localItemNumber.get(i).equals(items.get(holder.getAdapterPosition()).getItemNo())) {
                        showAlertDialog();
                        itemInlocalList = true;
                        break;
                    }
                }
                if (itemInlocalList == false) {
                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);



                    final LinearLayout resultLinear;
                    LinearLayout mainLinear ;
                    ImageView acceptDiscount,rejectDiscount;
                    TextView checkStateResult=dialog.findViewById(R.id.checkStateResult);






                    try{

                        if((items.get(holder.getAdapterPosition()).getItemHasSerial().equals("1"))&& voucherType!=508)
                        {  current_itemHasSerial=1;
                            dialog.setContentView(R.layout.add_item_serial_dialog);

                            mainRequestLinear=dialog.findViewById(R.id.mainRequestLinear);

                            mainRequestLinear.setVisibility(View.GONE);

                             mainLinear=dialog.findViewById(R.id.mainLinearAddItem);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());

                            lp.gravity = Gravity.CENTER;
                            lp.windowAnimations = R.style.DialogAnimation;
                            dialog.getWindow().setAttributes(lp);
                            bonus = dialog.findViewById(R.id.bonus);
                            bonus.setEnabled(false);
                            serial_No_recyclerView=dialog.findViewById(R.id.serial_No_recyclerView);
                            final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                            unitWeightLinearLayout.setVisibility(View.GONE);
                            item_serial= dialog.findViewById(R.id.item_serial);
                            final ImageView serialScanBunos = dialog.findViewById(R.id.serialScanBunos);
                            TextView  generateSerial=(TextView)dialog.findViewById(R.id.generateSerial);

                            generateSerial.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("generateSerial","generateSerial");
                                    int qtySerial=0;
                                    Log.e("generateSerial",""+serialListitems.size());
                                    if(!unitQty.getText().toString().equals("")&&serialListitems.size()==0)
                                    {

                                        qtySerial=Integer.parseInt(unitQty.getText().toString());
                                        counterSerial=qtySerial;
                                        if(qtySerial!=0)
                                        {flag = 1;
//                                            counterSerial++;
//                                            unitQty.setText(counterSerial+"");
                                            final LinearLayoutManager layoutManager;
                                            layoutManager = new LinearLayoutManager(view.getContext());
//                                            layoutManager.setOrientation(VERTICAL);

                                            for (int i=1;i<=qtySerial;i++)
                                            {
                                                serial= new serialModel();
                                                serial.setCounterSerial(i);
                                                serial.setSerialCode("");
                                                serial.setIsBonus("0");
                                                serialListitems.add(serial);
                                                listSerialAllItems.add(serial);
                                            }


                                            serial_No_recyclerView.setLayoutManager(layoutManager);

                                            serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, view.getContext(),context));
                                            unitQty.setEnabled(false);
                                            generateSerial.setEnabled(false);
                                        }
                                        else {
                                            unitQty.setError("Invalid Zero");
                                        }
                                    }





                                }
                            });
                            item_serial.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @SuppressLint("WrongConstant")
                                @Override
                                public void afterTextChanged(Editable s) {
//
//                                    flag = 1;
                                    isFoundSerial=false;

                                    for(int h=0;h<serialListitems.size();h++)
                                    {

                                        if(serialListitems.get(h).getSerialCode().equals(s.toString()))
                                        {
                                            isFoundSerial=true;
                                        }
                                    }
                                    Log.e("isFoundSerial", "" + isFoundSerial + s.toString());
                                    if(isFoundSerial==false)
                                    {
                                        List<serialModel> listitems_adapter = new ArrayList<>();
                                        int id = ((Serial_Adapter)serial_No_recyclerView.getAdapter()).selectedBarcode;
                                        listitems_adapter = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).list;

                                        Log.e("curentSerial", "" + id + s.toString());
                                        final LinearLayoutManager layoutManager;
                                        layoutManager = new LinearLayoutManager(view.getContext());
                                        layoutManager.setOrientation(VERTICAL);
                                        serial_No_recyclerView.setLayoutManager(layoutManager);
                                        if(s.toString().contains(","))//  update old data and add new data
                                        {
                                            serialModel serialMod;
                                            araySerial= s.toString().split(",");
                                            listitems_adapter.get(id).setSerialCode(araySerial[0]);
                                            String isbonus=listitems_adapter.get(id).getIsBonus();
                                            for (int i=1;i<araySerial.length;i++)
                                            {
                                                serialMod=new serialModel();
                                                serialMod.setSerialCode(araySerial[i]);
                                                if(isbonus.equals("0"))
                                                {
                                                    serialMod.setCounterSerial(++counterSerial);
                                                    unitQty.setText(counterSerial+"");


                                                }
                                                else {
                                                    serialMod.setCounterSerial(++counterBonus);
                                                    bonus.setText(counterBonus+"");
                                                }

                                                serialMod.setIsBonus( isbonus+"");
                                                listitems_adapter.add(serialMod);
                                                Log.e("listitems_adapter",""+s.toString());
                                            }

                                        }
                                        else {// just update on old data row

                                            listitems_adapter.get(id).setSerialCode(s.toString());
                                            Log.e("listitems_adapter",""+s.toString());
                                        }

                                        serial_No_recyclerView.setAdapter(new Serial_Adapter(listitems_adapter, view.getContext(), context));

                                    }
                                    else {
                                        new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText(view.getContext().getString(R.string.warning_message))
                                                .setContentText(view.getContext().getString(R.string.itemadedbefor))
                                                .show();
                                    }
//                                    exist=  MHandler.isSerialCodeExist(s.toString());
//                                    Log.e("exist",""+exist);

//                                    if(!isFoundSerial && (exist.equals("not")))
//                                    {
//                                        listitems_adapter.get(id).setSerialCode(s.toString());
//
//
//
//
//                                    }
//                                    else {
//                                        listitems_adapter.get(id).setSerialCode("");
//                                        new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
//                                                .setTitleText(view.getContext().getString(R.string.warning_message))
//                                                .setContentText(view.getContext().getString(R.string.itemadedbefor))
//                                                .show();
//                                    }

                                }
                            });
                            //******************************************************************************************
                            serialScanBunos.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onClick(View v) {
//                            *********************************

                                    flag = 1;
                                    counterBonus++;
                                    bonus.setText(""+counterBonus);
                                    bonus.setEnabled(false);
//                            counterSerial++;
//                            unitQty.setText(counterSerial+"");
                                    final LinearLayoutManager layoutManager;
                                    layoutManager = new LinearLayoutManager(view.getContext());
                                    layoutManager.setOrientation(VERTICAL);

                                    serial= new serialModel();
                                    serial.setCounterSerial(counterBonus);
                                    serial.setSerialCode("");
                                    serial.setIsBonus("1");
                                    serialListitems.add(serial);


                                    serial_No_recyclerView.setLayoutManager(layoutManager);

                                    serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, view.getContext(),context));
//

                                }
                            });
                        }
                        else {
                            current_itemHasSerial=0;
                            dialog.setContentView(R.layout.add_item_dialog_small);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());

                            lp.gravity = Gravity.CENTER;
                            lp.windowAnimations = R.style.DialogAnimation;
                            dialog.getWindow().setAttributes(lp);
                        }
                    }catch (Exception e)
                    {

                    }
                    resultLinear=dialog.findViewById(R.id.resultLinear);
                    acceptDiscount=dialog.findViewById(R.id.acceptDiscount);
                    rejectDiscount=dialog.findViewById(R.id.rejectDiscount);

                    mainRequestLinear=dialog.findViewById(R.id.mainRequestLinear);
                    TextView discount_text=dialog.findViewById(R.id.discount_text);
                    TextView bonuss_text=dialog.findViewById(R.id.bonuss_text);
                    checkState_recycler=dialog.findViewById(R.id.checkState);
                    mainLinear=dialog.findViewById(R.id.mainLinearAddItem);

                    try {
                        if(languagelocalApp.equals("ar"))
                        {
                            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        }
                        else{
                            if(languagelocalApp.equals("en"))
                            {
                                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            }

                        }
//
                    }catch (Exception e)
                    { mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);}

                    final TextView itemNumber = dialog.findViewById(R.id.item_number);
//                  final TextView categoryTextView =  dialog.findViewById(R.id.item_number);
                    final TextView itemName = dialog.findViewById(R.id.item_name);
                    final EditText price = dialog.findViewById(R.id.price);
                    final Spinner unit = dialog.findViewById(R.id.unit);
                    final TextView textQty = dialog.findViewById(R.id.textQty);
                      unitQty = dialog.findViewById(R.id.unitQty);
                    final EditText unitWeight = dialog.findViewById(R.id.unitWeight);
                    final CheckBox useWeight = dialog.findViewById(R.id.use_weight);
                    bonus = dialog.findViewById(R.id.bonus);
                    final EditText discount = dialog.findViewById(R.id.discount);
                    final RadioGroup radioGroup = dialog.findViewById(R.id.discTypeRadioGroup);
                    final LinearLayout discountLinearLayout = dialog.findViewById(R.id.discount_linear);
                    final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                    final LinearLayout bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
                    final LinearLayout discribtionItem_linear = dialog.findViewById(R.id.discribtionItem_linear);
                    final LinearLayout serialNo_linear= dialog.findViewById(R.id.serialNo_linear);
                    final EditText item_remark = dialog.findViewById(R.id.item_note);
                    final ImageView serialScan = dialog.findViewById(R.id.serialScan);

                    serialScan.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(View v) {
//                            context.readB();

                            flag = 1;
                                counterSerial++;
                                unitQty.setText(counterSerial+"");
                                final LinearLayoutManager layoutManager;
                                layoutManager = new LinearLayoutManager(view.getContext());
                                layoutManager.setOrientation(VERTICAL);

                                serial= new serialModel();
                                serial.setCounterSerial(counterSerial);
                                serial.setSerialCode("");
                                serial.setIsBonus("0");
                                serialListitems.add(serial);
                                listSerialAllItems.add(serial);

                                serial_No_recyclerView.setLayoutManager(layoutManager);

                                serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, view.getContext(),context));
//

                        }
                    });

                    //***************************************************************************************
                    Button addToList = dialog.findViewById(R.id.addToList);
                    Button cancel = dialog.findViewById(R.id.cancelAdd);
                    if(MHandler.getAllSettings().get(0).getRequiNote()==1)
                    {
                        discribtionItem_linear.setVisibility(View.VISIBLE);

                    }
                    else{
                        discribtionItem_linear.setVisibility(View.INVISIBLE);

                    }
                    approveAdmin=MHandler.getAllSettings().get(0).getApproveAdmin();

                    //***********************************Request Discount ****************************************************

                    checkState_recycler.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s)
                        {
                            Log.e("afterTextChanged",""+s.toString());
                            if(s.toString().equals("0"))
                            {

                            }
                            else
                            if(s.toString().equals("1"))
                            {
                                haveResult=1;
                                resultLinear.setVisibility(View.VISIBLE);
                                mainRequestLinear.setVisibility(View.GONE);
                                checkStateResult.setText(cont.getResources().getString(R.string.acceptedRequest));
                                acceptDiscount.setVisibility(View.VISIBLE);
                                rejectDiscount.setVisibility(View.GONE);
                                addToList.setEnabled(true);

                            }
                            else if(s.toString().equals("2"))
                            {
                                haveResult=2;
                                resultLinear.setVisibility(View.VISIBLE);
                                mainRequestLinear.setVisibility(View.GONE);
                                checkStateResult.setText(cont.getResources().getString(R.string.rejectedRequest));
                                acceptDiscount.setVisibility(View.GONE);
                                rejectDiscount.setVisibility(View.VISIBLE);
                                discount.setText("");
                                bonus.setText("");
                                addToList.setEnabled(true);

                            }

                        }
                    });
                    LinearLayout _linear_switch=dialog.findViewById(R.id._linear_switch);
                    ImageView requestDiscount=dialog.findViewById(R.id.requestDiscount);
                    request=new requestAdmin(cont);
                    if(MHandler.getAllSettings().size()!=0)
                    {
                        if (MHandler.getAllSettings().get(0).getBonusNotAlowed() == 0) {//you can  add bonus
                            bonusLinearLayout.setVisibility(View.VISIBLE);
//                        bonusLinearLayout.setVisibility(View.GONE);//test Fro serial
                        } else {
                            bonus.setText("0");
                            bonusLinearLayout.setVisibility(View.INVISIBLE);
                            bonus.setEnabled(false);

                        }

                        if(MHandler.getAllSettings().get(0).getApproveAdmin()==0)
                        {
                            mainRequestLinear.setVisibility(View.GONE);
                            _linear_switch.setVisibility(View.GONE);

//                          okButton.setVisibility(View.VISIBLE);
                        }
                        else {// you need to approve admin for discount or bunos
                            typeRequest=1;
                            if(items.get(holder.getAdapterPosition()).getItemHasSerial().equals("1")){
                                mainRequestLinear.setVisibility(View.GONE);
                                discountLinearLayout.setVisibility(View.VISIBLE);
                                if(MHandler.getAllSettings().get(0).getBonusNotAlowed() == 0)// you can add bunos
                                {
                                    bonusLinearLayout.setVisibility(View.VISIBLE);
                                }
                                else {
                                    bonus.setText("0");
                                    bonusLinearLayout.setVisibility(View.INVISIBLE);
                                    bonus.setEnabled(false);
                                }

                            }
                            else {
                                mainRequestLinear.setVisibility(View.VISIBLE);
                                discountLinearLayout.setVisibility(View.VISIBLE);
                                bonusLinearLayout.setVisibility(View.GONE);
                            }



                            discount_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    typeRequest=1;
                                    discountLinearLayout.setVisibility(View.VISIBLE);
                                    discount_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_dark));
                                    bonuss_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_shape));
                                    bonusLinearLayout.setVisibility(View.GONE);
                                }
                            });
                            bonuss_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    typeRequest=2;
                                    discountLinearLayout.setVisibility(View.GONE);
                                    bonuss_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_dark));
                                    discount_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_shape));
                                    bonusLinearLayout.setVisibility(View.VISIBLE);
                                    bonus.setText("");
                                    bonus.setEnabled(true);

                                }
                            });
                            requestDiscount.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addToList.setEnabled(false);
                                    if((typeRequest==1&& !discount.getText().toString().equals("")))// discount
                                    {
                                        Log.e("request",""+typeRequest);
                                        if(!discount.getText().toString().equals(""))
                                        {
                                            try {
                                                getDataForDiscountTotal(items.get(holder.getAdapterPosition()).getItemName(),"0",items.get(holder.getAdapterPosition()).getPrice()+"",discount.getText().toString());
                                                addToList.setEnabled(false);
                                                discount.setEnabled(false);
                                                request.startParsing();
                                            } catch (Exception e) {
                                                Log.e("request",""+e.getMessage());

                                            }

                                        }
                                        else {
                                            discount.setError("required");
                                        }

                                    }
                                    else if((typeRequest==2&& !bonus.getText().toString().equals(""))){
                                        Log.e("request",""+typeRequest);
                                        if(!bonus.getText().toString().equals(""))
                                        {
                                            try {
                                                getDataForDiscountTotal(items.get(holder.getAdapterPosition()).getItemName(),"2",items.get(holder.getAdapterPosition()).getPrice()+"",bonus.getText().toString());
                                                addToList.setEnabled(false);
                                                bonus.setEnabled(false);
                                                request.startParsing();
                                            } catch (Exception e) {
                                                Log.e("request",""+e.getMessage());

                                            }

                                        }
                                        else {
                                            bonus.setError("required");
                                        }

                                    }// end else

                                }
                            });

                        }

                    }
                    //***************************************************************************************

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(serialListitems.size()!=0) {
                                // delete serial if exist and alert screen if full list
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context.getActivity());
                                builder2.setTitle(context.getResources().getString(R.string.app_confirm_dialog));
                                builder2.setCancelable(false);
                                builder2.setMessage(context.getResources().getString(R.string.app_confirm_dialog_clear));
                                builder2.setIcon(android.R.drawable.ic_dialog_alert);
                                builder2.setPositiveButton(context.getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        int vouch = Integer.parseInt(voucherNumberTextView.getText().toString());
                                        MHandler.deletSerialItems_byVoucherNo(vouch);
                                        float count=0;
                                        // delete from main list
//                                        for(int j=0;j< List.size();j++)
//                                        {
//                                            count+= List.get(j).getQty();
//                                        }
//                                             Log.e("count",""+count);
//                                           Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
                                        total_items_quantity-=count;
                                        totalQty_textView.setText(total_items_quantity+"");
                                        dialog.dismiss();


                                    }
                                });

                                builder2.setNegativeButton(context.getResources().getString(R.string.app_no), null);
                                builder2.create().show();

                            }
                            else {

                            dialog.dismiss();
                            }


                        }
                    });
                    itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
                    itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
                    final DatabaseHandler mHandler = new DatabaseHandler(cont);
                    //*********************************** change Price with customer or not accourding to setting  ************************************
                    if (mHandler.getAllSettings().get(0).getCanChangePrice() == 0) {
                        price.setText("" + items.get(holder.getAdapterPosition()).getPrice());
                        price.setEnabled(false);
                        //    price.setText("Desable");

                    } else {
                        price.setText("" + items.get(holder.getAdapterPosition()).getPrice());
                    }

                    if (mHandler.getAllSettings().get(0).getTaxClarcKind() == 1)
//                    discountLinearLayout.setVisibility(View.INVISIBLE);

                        if (mHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1) {
                            discountLinearLayout.setVisibility(View.VISIBLE);
                        }


                    if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                        unitWeightLinearLayout.setVisibility(View.GONE);// For Serial
                        textQty.setText(view.getContext().getResources().getString(R.string.app_qty));
                        useWeight.setChecked(false);
                    } else
                        unitQty.setText("" + items.get(holder.getAdapterPosition()).getItemL());


                    List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

                    ArrayAdapter<String> unitsList = new ArrayAdapter<String>(cont, R.layout.spinner_style, units);
                    unit.setAdapter(unitsList);
                    if((items.get(holder.getAdapterPosition()).getItemHasSerial().equals("1")))
                    {
                        unit.setVisibility(View.GONE);
                    }


                    addToList.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(View v) {
                            String qtyText = "",discountText="",bunosText="";
                            int countInvalidSerial=0;
                            if(serialListitems.size()!=0)
                            {
                                 countInvalidSerial =checkSerialDB();
                            }

                            qtyText = unitQty.getText().toString();
                            discountText=discount.getText().toString();
                            bunosText=bonus.getText().toString();

                            Log.e("addTolist",""+approveAdmin+"\t"+haveResult);
                            if (current_itemHasSerial == 0 ||( current_itemHasSerial == 1 && countInvalidSerial==0))
                            {

                                if (!TextUtils.isEmpty(qtyText) || mHandler.getAllSettings().get(0).getWork_serialNo() == 0) {
                                    if (!price.getText().toString().equals("") && !price.getText().toString().equals("0") &&
                                            !(unitQty.getText().toString()).equals("")) {
                                        if(( approveAdmin==0)|| (approveAdmin==1 && TextUtils.isEmpty(bunosText)&&typeRequest==2)|| (approveAdmin==1 && TextUtils.isEmpty(discountText) &&typeRequest==1)|| (approveAdmin==1 && !TextUtils.isEmpty(bunosText) && haveResult!=0)||(approveAdmin==1 && !TextUtils.isEmpty(discountText) && haveResult!=0))
                                        {
                                            if (Double.parseDouble(unitQty.getText().toString()) != 0.0) {

                                                Boolean check = check_Discount(unitWeight, unitQty, price, bonus, discount, radioGroup);
                                                if (!check)
                                                    Toast.makeText(view.getContext(), "Invalid Disco" +
                                                            "unt Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                                                else {

                                                    String unitValue;
                                                    if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                                        unitValue = unit.getSelectedItem().toString();
                                                        Log.e("unitValue",""+unitValue);

                                                        if (items.get(holder.getAdapterPosition()).getQty() >= Double.parseDouble(unitQty.getText().toString())
                                                                || mHandler.getAllSettings().get(0).getAllowMinus() == 1
                                                                || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508) {

                                                            if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                                    Double.parseDouble(price.getText().toString()) >= items.get(holder.getAdapterPosition()).getMinSalePrice())) {


                                                                AddItemsFragment2 obj = new AddItemsFragment2();
                                                                List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                                Offers appliedOffer = null;
//                                                            Log.e("offer",""+offer.size()+"\t"+offer.get(0).getPromotionType());

                                                                if (offer.size() != 0) {

                                                                    if (offer.get(0).getPromotionType() == 0) {// bonus promotion

                                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                                bonus.getText().toString(),
                                                                                discount.getText().toString(),
                                                                                radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight,
                                                                                view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);

                                                                        appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 0);
                                                                        if (appliedOffer != null) {
                                                                            double bonus_calc = ((int) (Double.parseDouble(unitQty.getText().toString()) / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();
                                                                            Log.e("bonus_calc=", "" + bonus_calc);
                                                                            added = obj.addItem(offer.get(0).getBonusItemNo(), "(bonus)",
                                                                                    "0", "1", "" + bonus_calc, "0",
                                                                                    "0", "0", radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);
                                                                            Log.e("bonus_calc", "" + bonus_calc);

                                                                        }
                                                                    } else {
                                                                        //(appliedOffer.getBonusQty()*Double.parseDouble(unitQty.getText().toString()))   //******calculate discount item before 11/9
                                                                        double disount_totalnew = 0, unitQty_double = 0;

                                                                        appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 1);
                                                                        if (appliedOffer != null) {
                                                                            Log.e("appliedOffer","appliedOffer.getItemQty()"+appliedOffer.getItemQty()+appliedOffer.getBonusQty());
                                                                            unitQty_double = Double.parseDouble(unitQty.getText().toString());
                                                                            // get discount from offers not from text
                                                                            disount_totalnew = ((int) (unitQty_double / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();

                                                                            String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());

                                                                            radioGroup.check(R.id.discValueRadioButton);// just if promotion is discount

                                                                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                    holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                                    bonus.getText().toString(), "" + disount_totalnew, radioGroup
                                                                                    , items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);
                                                                        }
                                                                    }
                                                                } else {
                                                                    double totalQty = 0;
                                                                    totalQty = Double.parseDouble(unitQty.getText().toString()) + Double.parseDouble(bonus.getText().toString());
                                                                    Log.e("totalQty+recyclerview", "" + totalQty+ "\t bomus"+bonus.getText().toString());
                                                                    added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                            holder.tax.getText().toString(), unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                            bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                            items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);
                                                                }
                                                                if (added) {
                                                                    if (offer.size() != 0)
                                                                        openOfferDialog(appliedOffer);

                                                                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.layer5));
                                                                    isClicked.set(holder.getAdapterPosition(), 1);
                                                                    localItemNumber.add(items.get(holder.getAdapterPosition()).getItemNo());
                                                                    itemInlocalList = false;
                                                                }
                                                            } else
                                                                Toast.makeText(view.getContext(), "Item hasn't been added, Min sale price for this item is " + items.get(holder.getAdapterPosition()).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                            Log.e("bonus not added ", "" + items.get(holder.getAdapterPosition()).getMinSalePrice());
                                                        } else
                                                            Toast.makeText(view.getContext(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        if (unitWeight.getText().toString().equals(""))
                                                            Toast.makeText(view.getContext(), "please enter unit weight", Toast.LENGTH_LONG).show();
                                                        else {
                                                            unitValue = unitWeight.getText().toString();
//                                        String qtyValue = "" + (Double.parseDouble(unitWeight.getText().toString()) * Double.parseDouble(unitQty.getText().toString()));
                                                            String qty = (useWeight.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString()) * Double.parseDouble(unitValue)) : unitQty.getText().toString());

                                                            Log.e("here**", "" + holder.getAdapterPosition());
                                                            if (holder.getAdapterPosition() > -1) {
                                                                if (items.get(holder.getAdapterPosition()).getQty() >= Double.parseDouble(qty)
                                                                        || mHandler.getAllSettings().get(0).getAllowMinus() == 1
                                                                        || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508) {
                                                                    if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                                            Double.parseDouble(price.getText().toString()) >= items.get(holder.getAdapterPosition()).getMinSalePrice())) {

                                                                        AddItemsFragment2 obj = new AddItemsFragment2();
                                                                        List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                                        Offers appliedOffer = null;

                                                                        if (offer.size() != 0) {
                                                                            if (offer.get(0).getPromotionType() == 0) {

                                                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                        holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                        bonus.getText().toString(), discount.getText().toString(),
                                                                                        radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);

                                                                                appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 0);
                                                                                if (appliedOffer != null)
                                                                                    added = obj.addItem(appliedOffer.getBonusItemNo(), "(bonus)",
                                                                                            "0", "1", "" + appliedOffer.getBonusQty(), "0",
                                                                                            "0", "0", radioGroup
                                                                                            , items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + ""
                                                                                            , useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);

                                                                            } else {
                                                                                appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 1);
                                                                                if (appliedOffer != null) {
                                                                                    String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());
                                                                                    added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                            holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                            bonus.getText().toString(), "" + (appliedOffer.getBonusQty() * Double.parseDouble(qty)), radioGroup,
                                                                                            items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + ""
                                                                                            , useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);
                                                                                }
                                                                            }
                                                                        } else {
                                                                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                    holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                    bonus.getText().toString(), discount.getText().toString(),
                                                                                    radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial);
                                                                        }
                                                                        if (added) {
                                                                            if (offer.size() != 0)
                                                                                openOfferDialog(appliedOffer);
                                                                            holder.linearLayout.setBackgroundColor(R.color.done_button);
                                                                            isClicked.set(holder.getAdapterPosition(), 1);
                                                                        }
                                                                    } else
                                                                        Toast.makeText(view.getContext(), "Item hasn't been added, Min sale price for this item is " + items.get(holder.getAdapterPosition()).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                                } else
                                                                    Toast.makeText(view.getContext(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                                            } else
                                                                Toast.makeText(view.getContext(), "Please enter the item again", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                }

                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(view.getContext(), "Invalid  Qty", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else {
                                            if(haveResult==0)
                                            {
                                                new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText(view.getContext().getString(R.string.warning_message))
                                                        .setContentText(view.getContext().getString(R.string.youHaveRequestToAdmin))
                                                        .show();
                                            }


                                        }


                                    } else
                                        Toast.makeText(view.getContext(), "Invalid price or Qty", Toast.LENGTH_LONG).show();
                                } else {
                                    unitQty.setError("*Required");
                                }
                        }else{// item  serial_No is duplcate +++++ dont save

                                if(countInvalidSerial==-1)
                                {
                                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(view.getContext().getString(R.string.warning_message))
                                            .setContentText(view.getContext().getString(R.string.reqired_filled))
                                            .show();
                                }
                                else {
                                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(view.getContext().getString(R.string.warning_message))
                                            .setContentText(view.getContext().getString(R.string.itemadedbefor))
                                            .show();
                                }



                            }




                        }// end on click
                    });
                    dialog.show();

                }
            }
        }//on click

        );

    }

    private void getDataForDiscountTotal(String itemName,String type,String price,String amount) {
        Log.e("getDataForDiscountTotal",""+itemName+"type"+type+price);
        discountRequest=new RequestAdmin();
        if(MHandler.getAllSettings().size()!=0)
        {
            discountRequest.setSalesman_name( MHandler.getAllSettings().get(0).getSalesMan_name());
        }
        else {
            discountRequest.setSalesman_name("");
        }
        discountRequest.setSalesman_no(Login.salesMan);
        discountRequest.setCustomer_no(CustomerListShow.Customer_Account);
        discountRequest.setCustomer_name(CustomerListShow.Customer_Name);
        discountRequest.setAmount_value(amount);
        discountRequest.setTotal_voucher(price+"");// if request for item not for all voucher
        discountRequest.setVoucher_no(voucherNumberTextView.getText().toString()+"");

        discountRequest.setKey_validation("");
        discountRequest.setNote(itemName);
        discountRequest.setRequest_type(type);
        discountRequest.setStatus("0");
        getTimeAndDate();
        discountRequest.setTime(time);
        discountRequest.setDate(voucherDate);
        discountRequest.setSeen_row("0");
    }
    private void getTimeAndDate() {
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

    private int checkSerialDB() {
        int counter=0;
        for(int i=0;i<serialListitems.size();i++)
        {
            if(serialListitems.get(i).getSerialCode().equals(""))
            {counter=-1;

                return counter;
            }

            if(!MHandler.isSerialCodeExist(serialListitems.get(i).getSerialCode()).equals("not"))
            {
                counter++;

            }
        }
        Log.e("counter",""+counter);
        return  counter;

    }
    public void showImageOfCheck(Bitmap bitmap) {
        final Dialog dialog = new Dialog(context.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.show_image);
        photoDetail = (PhotoView) dialog.findViewById(R.id.image_check);
          final ImageView imageView = (ImageView) dialog.findViewById(R.id.image_check);
        photoDetail.setImageBitmap(bitmap);
        dialog.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle(R.string.app_alert);
        builder.setCancelable(true);
        builder.setMessage(R.string.itemadedbefor);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkTypePriceTable(String itemNumber) {
       if( MHandler.checkItemNoTableCustomerPricess(itemNumber))
           return true;
       return false;
    }

    private List<Offers> checkOffers(String itemNo) {

        Offers offer = null;
        List<Offers> Offers = new ArrayList<>();
        try {
            Date currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(currentTimeAndDate);
            date = convertToEnglish(date);


            List<Offers> offers = MHandler.getAllOffers();


            for (int i = 0; i < offers.size(); i++) {
                Log.e("log2 " , date + "  " + offers.get(i).getFromDate() + " " + offers.get(i).getToDate());
                if (itemNo.equals(offers.get(i).getItemNo()) &&
                        (formatDate(date).after(formatDate(offers.get(i).getFromDate())) || formatDate(date).equals(formatDate(offers.get(i).getFromDate()))) &&
                        (formatDate(date).before(formatDate(offers.get(i).getToDate())) || formatDate(date).equals(offers.get(i).getToDate()))) {

                    offer = offers.get(i);
                    Offers.add(offer);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Offers;
    }

    private Offers getAppliedOffer(String itemNo, String qty, int flag) {

        double qtyy = Double.parseDouble(qty);
        List<Offers> offer = checkOffers(itemNo);

        List<Double> itemQtys = new ArrayList<>();
        for (int i = 0; i < offer.size(); i++) {
            itemQtys.add(offer.get(i).getItemQty());
        }
        Collections.sort(itemQtys);

        double iq = itemQtys.get(0);
        for (int i = 0; i < itemQtys.size(); i++) {
            if (qtyy >= itemQtys.get(i))
                iq = itemQtys.get(i);
        }

        for (int i = 0; i < offer.size(); i++) {
            if (iq == offer.get(i).getItemQty())
                return offer.get(i);
        }

        return null;
    }

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void openOfferDialog(Offers offers) {
        final Dialog dialog = new Dialog(cont);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.offer_dialog);
        LinearLayout mainLinear=dialog.findViewById(R.id.mainLinear);
        if (languagelocalApp.equals("ar")) {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            if (languagelocalApp.equals("en")) {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

        final TextView offerType = (TextView) dialog.findViewById(R.id.offerType);
        final TextView bonusItem = (TextView) dialog.findViewById(R.id.bonusItem);
        final TextView bonusItemName = (TextView) dialog.findViewById(R.id.bonusItemName);

        final TextView discount_value = (TextView) dialog.findViewById(R.id.discount_value);
        final TextView from = (TextView) dialog.findViewById(R.id.from);
        final TextView to = (TextView) dialog.findViewById(R.id.to);
        LinearLayout linearBunos= (LinearLayout) dialog.findViewById(R.id.linearBunos);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        String offType = offers.getPromotionType() == 0 ? context.getResources().getString(R.string.app_bonus) : context.getResources().getString(R.string.app_disc_);

        if(offers.getPromotionType() == 0)// bunos
        {
            discount_value.setVisibility(View.GONE);
        }
        else {
            linearBunos.setVisibility(View.GONE);
        }
//        String offType =context.getResources().getString(R.string.app_bonus);
        offerType.setText(offerType.getText().toString() + "  :       " + offType);

        String itemBonusName= MHandler.getItemNameBonus(offers.getBonusItemNo());
        String bonusItm = offers.getBonusItemNo().equals("-1") ? "none" : offers.getBonusQty()+""  ;
        bonusItem.setText(bonusItem.getText().toString() + " :     " + bonusItm);
        bonusItemName.setText(bonusItemName.getText().toString()+" :  " +itemBonusName);
        String disc = offers.getPromotionType() == 0 ? "0" : "" + offers.getBonusQty();
        discount_value.setText(discount_value.getText().toString() + " : " + disc);

        from.setText(from.getText().toString() + " :  " + offers.getFromDate());
        to.setText(to.getText().toString() + " :  " + offers.getToDate());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        CardView cardView;
        TableRow row_qty;
        TextView itemNumber, itemName, tradeMark, category, unitQty, price, tax, barcode,posprice;
        ImageView imagespecial;


        public viewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linear);
            itemNumber = itemView.findViewById(R.id.textViewItemNumber);
            itemName = itemView.findViewById(R.id.textViewItemName);
            tradeMark = itemView.findViewById(R.id.textViewTradeMark);
            category = itemView.findViewById(R.id.textViewCategory);
            unitQty = itemView.findViewById(R.id.textViewUnit_qty);
            price = itemView.findViewById(R.id.textViewPrice);
            tax = itemView.findViewById(R.id.textViewTax);
            barcode = itemView.findViewById(R.id.textViewBarcode);
            posprice= itemView.findViewById(R.id.textViewPosPrice);
            row_qty=itemView.findViewById(R.id.row_qty);
            imagespecial=itemView.findViewById(R.id.imagespecial);
            threeDForm = new DecimalFormat("00.000");
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }



    private Boolean check_Discount(EditText unitEditText, EditText qtyEditText, TextView priceEditText,
                                   EditText bonusEditText, EditText discEditText, RadioGroup discTypeRadioGroup) {
        Boolean check = true;

        if (unitEditText.getText().toString().equals(""))
            unitEditText.setText("0");
        else
            unitEditText.setText(convertToEnglish(unitEditText.getText().toString()));

        if (qtyEditText.getText().toString().equals(""))
            qtyEditText.setText("0");
        else
            qtyEditText.setText(convertToEnglish(qtyEditText.getText().toString()));

        if (priceEditText.getText().toString().equals(""))
            priceEditText.setText("0");
        else
            priceEditText.setText(convertToEnglish(priceEditText.getText().toString()));

        if (discEditText.getText().toString().equals(""))
            discEditText.setText("0");
        else
            discEditText.setText(convertToEnglish(discEditText.getText().toString()));

        if (bonusEditText.getText().toString().equals(""))
            bonusEditText.setText("0");
        else
            bonusEditText.setText(convertToEnglish(bonusEditText.getText().toString()));

        Float totalValue = (Float.parseFloat(qtyEditText.getText().toString())) * (Float.parseFloat(priceEditText.getText().toString()));
        Float discount = Float.parseFloat(discEditText.getText().toString());
        int radioId = discTypeRadioGroup.getCheckedRadioButtonId();

        if (radioId == R.id.discValueRadioButton) {
            if (discount > totalValue)
                return false;
        } else {
            if (discount > 100)
                return false;
        }

        return check;
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }

}
