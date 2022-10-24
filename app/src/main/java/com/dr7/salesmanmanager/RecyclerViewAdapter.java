package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Interface.DaoRequsts;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.LinearLayout.VERTICAL;

import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;
import static com.dr7.salesmanmanager.Activities.keyCreditLimit;
import static com.dr7.salesmanmanager.AddItemsFragment2.barcode;
import static com.dr7.salesmanmanager.AddItemsFragment2.endAddItem;
import static com.dr7.salesmanmanager.AddItemsFragment2.total_items_quantity;
import static com.dr7.salesmanmanager.Login.OfferCakeShop;

import static com.dr7.salesmanmanager.Login.POS_ACTIVE;
import static com.dr7.salesmanmanager.Login.Purchase_Order;
import static com.dr7.salesmanmanager.Login.Separation_of_the_serial;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.offerQasion;
import static com.dr7.salesmanmanager.Login.offerTalaat;

import static com.dr7.salesmanmanager.SalesInvoice.discountRequest;
import static com.dr7.salesmanmanager.SalesInvoice.itemNoSelected;
import static com.dr7.salesmanmanager.SalesInvoice.items;
import static com.dr7.salesmanmanager.SalesInvoice.listMasterSerialForBuckup;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;

import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;
import static com.dr7.salesmanmanager.SalesInvoice.unitsItems_select;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;
import static com.dr7.salesmanmanager.Serial_Adapter.errorData;

import static com.dr7.salesmanmanager.Activities.currentKey;
import static com.dr7.salesmanmanager.StockRequest.clearData;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    SalesInvoice.SalesInvoiceInterface salesInvoiceInterfaceListener;
    private List<Item> allItemsList;
public static     int CountOfItems=1;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context cont;
    int current_itemHasSerial = 0;
    private AddItemsFragment2 context;
    Date currentTimeAndDate;
    public static EditText price;
    SimpleDateFormat df, df2, formatTime;
    String voucherDate, voucherYear, time;
    CompanyInfo companyInfo;
    String ipAddress = "",ipWithPort="";
    boolean added = false, haveCstomerDisc = false, haveChangeCustDisc = false;
    public static  DatabaseHandler MHandler;
    DecimalFormat threeDForm;
    int settingPriceCus = 0, showItemImageSetting = 0;
    List<String> localItemNumber;
    boolean itemInlocalList = false;
    public static EditText Serial_No, item_serial;
    public static final int REQUEST_Camera_Serial = 22;
    public boolean isFoundSerial = false;
    SweetAlertDialog pdValidation;
    public static int flag = 0, counterSerial = 0, counterBonus = 0;
    RecyclerView serial_No_recyclerView;
    public static ArrayList<serialModel> serialListitems = new ArrayList<>();

    LinearLayoutManager layoutManager;

    List<String> listSerialValue = new ArrayList<>();
    public static serialModel serial;
    public static EditText unitQty, bonus;
    LinearLayout bonusLinearLayout,linearPrice;
    String discountCustomer = "", updateDiscountValue = "";
    public static Button addToList;
    public String exist = "";
    public static String curentSerial = "";
    public static String araySerial[];
    Bitmap itemBitmap;
    public PhotoView photoView, photoDetail;
    public static TextView checkState_recycler, checkStateResult;
    requestAdmin request;


    int typeRequest = 0, haveResult = 0, approveAdmin = 0, dontDuplicateItems = 0, sumCurentQty = 0,ItemUnitsFlage=0;
    LinearLayout UnitSpinnerLinear;
    LinearLayout mainRequestLinear;
    LinearLayout resultLinear;
    LinearLayout mainLinear,linearUnit;
    ImageView acceptDiscount, rejectDiscount;
    public static EditText serialValue;
    public static int numberBarcodsScanner = 0;
    int discountPerVal = 0, showSolidQty = 0;
    int useWeight = 0;
    public int sunmiDevice = 0, dontShowTax = 0, contiusReading = 0;

    int vouch, kindVoucher = 504;
    ImageView addEditBarcode;
    GeneralMethod generalMethod;
    int itemUnit = 0, oneUnit = 0;
    float priceUnit = 0;
    String rate_customer = "0";


    public RecyclerViewAdapter(List<Item> items, AddItemsFragment2 context) {
        this.allItemsList = items;
        this.filterList = items;
        this.context = context;
        cont = context.getActivity();
        try {
            for (int i = 0; i <= allItemsList.size(); i++) {//******************
                isClicked.add(0);
            }
        } catch (Exception e) {
            Toast.makeText(cont, "Items Empty", Toast.LENGTH_SHORT).show();
        }


        vouch = Integer.parseInt(voucherNumberTextView.getText().toString());
        kindVoucher = voucherType;
        MHandler = new DatabaseHandler(cont);
        settingPriceCus = MHandler.getAllSettings().get(0).getPriceByCust();
        showItemImageSetting = MHandler.getAllSettings().get(0).getShowItemImage();
        localItemNumber = new ArrayList<>();
        if (MHandler.getAllSettings().get(0).getContinusReading() == 1) {

            contiusReading = 1;
            Log.e("sunmiDevice", "" + sunmiDevice);
        }
        generalMethod = new GeneralMethod(cont);
//        this.listBitmap=listItemImage;
        ipAddress = MHandler.getAllSettings().get(0).getIpAddress();
        ipWithPort = MHandler.getAllSettings().get(0).getIpPort();
        itemUnit = MHandler.getAllSettings().get(0).getItemUnit();
        rate_customer = MHandler.getRateOfCustomer();
        dontDuplicateItems = MHandler.getAllSettings().get(0).getDontduplicateItem();
        sumCurentQty = MHandler.getAllSettings().get(0).getSumCurrentQty();
        getTimeAndDate();


    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_listview, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setIsRecyclable(false);

//        if (isClicked.get(position) == 0)
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#455A64"));
//        else
//            holder.linearLayout.setBackgroundColor(R.color.done_button);
        Log.e("getVivible", "" + allItemsList.get(position).getItemNo() + "\t" + allItemsList.size()
        );
        Log.e("getVivible", "" + allItemsList.get(position).getVivible());
        if ((allItemsList.get(position).getVivible() == 1)||(Separation_of_the_serial==1&&allItemsList.get(position).getItemHasSerial().equals("1"))) {

            holder.cardView.setVisibility(View.GONE);
        } else holder.cardView.setVisibility(View.VISIBLE);

        holder.itemNumber.setText(allItemsList.get(position).getItemNo());
        holder.itemName.setText(allItemsList.get(position).getItemName());
        holder.itemName.setMovementMethod(new ScrollingMovementMethod());
        holder.tradeMark.setText(allItemsList.get(position).getItemName());
        holder.category.setText("" + allItemsList.get(position).getCategory());

        if (MHandler.getAllSettings().get(0).getHide_qty() == 1) {
            holder.row_qty.setVisibility(View.GONE);
//            holder.unitQty.setVisibility(View.GONE);
        } else {
            if (sumCurentQty == 1) {
                try {
                    String solidQty = MHandler.getSolidQtyForItem(allItemsList.get(position).getItemNo(), voucherDate);
                    // Log.e("sumCurentQty","solidQty=="+solidQty);
                    float qtyCurent = Float.parseFloat(solidQty);
                    holder.unitQty.setText("" + (allItemsList.get(position).getQty() - qtyCurent));
                } catch (Exception e) {

                }


            } else {
                holder.unitQty.setText("" + allItemsList.get(position).getQty());
            }

        }
        holder.unitQty.setEnabled(false);
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
        if (showItemImageSetting == 1) {
//            if (items.get(position).getItemPhoto() != null) {
////            itemBitmap = StringToBitMap(items.get(position).getItemPhoto());
//                holder.imagespecial.setImageBitmap(items.get(position).getItemPhoto());
//            } else {
//                holder.imagespecial.setImageDrawable(this.context.getResources().getDrawable(R.drawable.specialred));
//            }

//
            holder.imagespecial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    itemBitmap=StringToBitMap(items.position).getItemPhoto()get(holder.getAdapterPosition()).getItemPhoto());
                    //   showImageOfCheck(items.get(position).getItemPhoto());
                    String url = allItemsList.get(position).getItemPhoto();
                    Log.e("url", "imagespecial" + url);
                    if (url.contains("Products")) {
                        int index = url.indexOf("Products");
                        url = url.substring(index);
                    }
                    Log.e("url", "imagespecial2" + url);
                    getImage(url);
                }
            });
        } else {
            holder.imagespecial.setVisibility(View.GONE);
        }

//        if(voucherType==506)
//        {
////            holder.price.setText(convertToEnglish(threeDForm.format(items.get(position).getPrice())) + "\t\tJD");
//            holder.price.setText("300");
//
//        }else {
        if (itemUnit == 1&&unitsItems_select==0) {
//            if(items.get(position).getPrice()!=0)
//            {
//                holder.price.setText("" + items.get(position).getPrice());
//            }else{

            Log.e("rate_customer", "" + rate_customer);
            String postPriceUniteValue = MHandler.getUnitPrice(allItemsList.get(position).getItemNo(), rate_customer,0);
            if (!postPriceUniteValue.equals("")) {
                try {
                    priceUnit = Float.parseFloat(postPriceUniteValue);
                } catch (Exception e) {
                }
            } else {
                priceUnit = allItemsList.get(position).getPrice();
            }

            holder.price.setText(convertToEnglish(threeDForm.format(priceUnit)) + "\t\tJD");

//            }
        } else {
            holder.price.setText(convertToEnglish(threeDForm.format(allItemsList.get(position).getPrice())) + "\t\tJD");

        }
        if (MHandler.getAllSettings().get(0).getPriceByCust() == 1) {// for ejabi ***** test
            String priceCus = MHandler.getItemPrice(allItemsList.get(position).getItemNo());
            if (!priceCus.equals("")) {
                holder.price.setText(priceCus);
                allItemsList.get(position).setPrice(Float.parseFloat(priceCus));
            }

        }


//       *******************************//////////////////////
        if (showSolidQty == 1) {
            holder.table_solidQty.setVisibility(View.VISIBLE);
            holder.textViewsolidQty.setText("" + MHandler.getSolidQtyForItem(allItemsList.get(position).getItemNo(), voucherDate));


        } else {
            holder.table_solidQty.setVisibility(View.GONE);
        }
        holder.tax.setText("" + allItemsList.get(position).getTaxPercent());

        holder.barcode.setText(allItemsList.get(position).getBarcode());

        holder.posprice.setText(allItemsList.get(position).getPosPrice() + "");
        if (allItemsList.size() == 1 && POS_ACTIVE == 1) {
            Log.e("Else", "Yes" + allItemsList.size());
            clearDataCurrent();
            getTimeAndDate();
            checkDuplicate(position);
            checkDuplicatInAllItems(position);
            openDialogQtyItem(position, context);

        } else {
            Log.e("Else", "" + allItemsList.size());


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                                                   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                                   @Override
                                                   public void onClick(final View view) {
                                                       clearDataCurrent();
                                                       holder.cardView.setEnabled(false);

                                                       getTimeAndDate();
//                                                   checkDuplicate(position);
                                                       for (int i = 0; i < localItemNumber.size(); i++) {
                                                           if (localItemNumber.get(i).equals(allItemsList.get(position).getItemNo())) {
//                                                           if(canChangePrice==0)
//                                                           {
                                                               // showAlertDialog();
                                                               itemInlocalList = true;
                                                               break;
//                                                           }

                                                           }
                                                       }


//                                                   checkDuplicatInAllItems(position);
                                                       if (dontDuplicateItems == 1) {
                                                           for (int i = 0; i < items.size(); i++) {
                                                               if (items.get(i).getItemNo().equals(allItemsList.get(position).getItemNo())) {
                                                                   itemInlocalList = true;
                                                                   break;
//                                                           }

                                                               }
                                                           }
//                                                       Log.e("items",""+items.size());
//                                                       Log.e("allItemsList",""+allItemsList.size());
                                                       }
//                                                   openDialogQtyItem(position,view.getContext());

                                                       if (itemInlocalList == false) {
                                                           final Dialog dialog = new Dialog(view.getContext());
                                                           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                           dialog.setCancelable(false);
                                                           itemNoSelected = allItemsList.get(position).getItemNo();

                                                           try {


                                                               if ((allItemsList.get(position).getItemHasSerial().equals("1")) && voucherType != 508) {
                                                                   current_itemHasSerial = 1;

                                                                   dialog.setContentView(R.layout.add_item_serial_dialog);
                                                                   serialValue = dialog.findViewById(R.id.serialValue);
                                                                   UnitSpinnerLinear= dialog.findViewById(R.id.linearUnit);
                                                                   UnitSpinnerLinear.setVisibility(View.GONE);
                                                                   if(MHandler.getAllSettings().get(0).getItems_Unit()==1)UnitSpinnerLinear.setVisibility(View.VISIBLE);
                                                                   mainRequestLinear = dialog.findViewById(R.id.mainRequestLinear);
                                                                   checkStateResult = dialog.findViewById(R.id.checkStateResult);
                                                                   rejectDiscount = dialog.findViewById(R.id.rejectDiscount);
                                                                   mainRequestLinear.setVisibility(View.VISIBLE);
                                                                   unitQty = dialog.findViewById(R.id.unitQty);

                                                                   unitQty.setEnabled(false);

                                                                   if (contiusReading == 0) {
                                                                       serialValue.requestFocus();
                                                                       serialValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                                                                                 @Override
                                                                                                                 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                                                                                     if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH
                                                                                                                             || actionId == EditorInfo.IME_NULL) {
                                                                                                                         if (!serialValue.getText().toString().equals("")) {
                                                                                                                             barcodeValue = serialValue.getText().toString().trim();
//                                                                               serialValue_Model.setText(s.toString().trim());
                                                                                                                             updateValue(barcodeValue, serialListitems);
                                                                                                                             new Handler().post(new Runnable() {
                                                                                                                                 @Override
                                                                                                                                 public void run() {

                                                                                                                                     serialValue.requestFocus();

                                                                                                                                 }
                                                                                                                             });


                                                                                                                         }

                                                                                                                     }
                                                                                                                     return false;
                                                                                                                 }

                                                                                                             }

                                                                       );
                                                                   } else {
                                                                       try {
                                                                           serialValue.addTextChangedListener(new TextWatcher() {
                                                                               @Override
                                                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                               }

                                                                               @Override
                                                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                                               }

                                                                               @Override
                                                                               public void afterTextChanged(Editable s) {
                                                                                   if (!s.toString().equals("")) {
                                                                                       barcodeValue = s.toString().trim();
//                                                                               serialValue_Model.setText(s.toString().trim());
                                                                                       updateValue(barcodeValue, serialListitems);

                                                                                   }


                                                                               }
                                                                           });
                                                                       } catch (Exception e) {
                                                                       }
                                                                   }


                                                                   mainLinear = dialog.findViewById(R.id.mainLinearAddItem);
                                                                   bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
                                                                   bonusLinearLayout.setVisibility(View.GONE);
                                                                   WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                                   lp.copyFrom(dialog.getWindow().getAttributes());

                                                                   lp.gravity = Gravity.CENTER;
                                                                   lp.windowAnimations = R.style.DialogAnimation;
                                                                   dialog.getWindow().setAttributes(lp);
                                                                   bonus = dialog.findViewById(R.id.bonus);
                                                                   addToList = dialog.findViewById(R.id.addToList);
                                                                   addToList.setVisibility(View.INVISIBLE);
                                                                   bonus.setEnabled(false);
                                                                   addToList.setEnabled(false);
                                                                   serial_No_recyclerView = dialog.findViewById(R.id.serial_No_recyclerView);
                                                                   final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                                                                   unitWeightLinearLayout.setVisibility(View.GONE);
                                                                   item_serial = dialog.findViewById(R.id.item_serial);
                                                                   final ImageView serialScanBunos = dialog.findViewById(R.id.serialScanBunos);
                                                                   serialScanBunos.setVisibility(View.GONE);
                                                                   TextView generateSerial = (TextView) dialog.findViewById(R.id.generateSerial);

                                                                   generateSerial.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {

                                                                           int qtySerial = 0;

                                                                           if (!unitQty.getText().toString().equals("") && serialListitems.size() == 0) {
                                                                               try {
                                                                                   qtySerial = (int) Double.parseDouble(unitQty.getText().toString());
                                                                               } catch (Exception e) {
                                                                                   qtySerial = Integer.parseInt(unitQty.getText().toString());
                                                                               }
                                                                               if (qtySerial <= 100) {
                                                                                   counterSerial = qtySerial;
                                                                                   if (qtySerial != 0) {
                                                                                       flag = 1;
                                                                                       addToList.setEnabled(true);
                                                                                       addToList.setVisibility(View.VISIBLE);
//                                            counterSerial++;
//                                            unitQty.setText(counterSerial+"");
                                                                                       final LinearLayoutManager layoutManager;
                                                                                       layoutManager = new LinearLayoutManager(view.getContext());
//                                            layoutManager.setOrientation(VERTICAL);

                                                                                       for (int i = 1; i <= qtySerial; i++) {
                                                                                           serial = new serialModel();
                                                                                           serial.setCounterSerial(i);
                                                                                           serial.setSerialCode("");
                                                                                           serial.setIsBonus("0");
                                                                                           serial.setIsDeleted("0");
                                                                                           serial.setVoucherNo(vouch + "");
                                                                                           serial.setKindVoucher(kindVoucher + "");
                                                                                           serial.setStoreNo(Login.salesMan);
                                                                                           serial.setDateVoucher(voucherDate);
                                                                                           serial.setItemNo(itemNoSelected);
                                                                                           serialListitems.add(serial);

                                                                                       }


                                                                                       serial_No_recyclerView.setLayoutManager(layoutManager);

                                                                                       serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
                                                                                       unitQty.setEnabled(false);
                                                                                       generateSerial.setEnabled(false);
                                                                                   } else {
                                                                                       unitQty.setError("Invalid Zero");
                                                                                   }
                                                                               } else {
                                                                                   unitQty.setError("Invalid");
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
                                                                           isFoundSerial = false;

                                                                           for (int h = 0; h < serialListitems.size(); h++) {

                                                                               if (serialListitems.get(h).getSerialCode().equals(s.toString())) {
                                                                                   isFoundSerial = true;
                                                                               }
                                                                           }
                                                                           Log.e("isFoundSerial", "" + isFoundSerial + s.toString());
                                                                           if (isFoundSerial == false) {
                                                                               List<serialModel> listitems_adapter = new ArrayList<>();
                                                                               int id = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).selectedBarcode;
                                                                               listitems_adapter = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).list;

                                                                               Log.e("curentSerial", "" + id + s.toString());
                                                                               final LinearLayoutManager layoutManager;
                                                                               layoutManager = new LinearLayoutManager(view.getContext());
                                                                               layoutManager.setOrientation(VERTICAL);
                                                                               serial_No_recyclerView.setLayoutManager(layoutManager);
                                                                               if (s.toString().contains(","))//  update old data and add new data
                                                                               {
                                                                                   serialModel serialMod;
                                                                                   araySerial = s.toString().split(",");
                                                                                   listitems_adapter.get(id).setSerialCode(araySerial[0]);
                                                                                   String isbonus = listitems_adapter.get(id).getIsBonus();
                                                                                   for (int i = 1; i < araySerial.length; i++) {
                                                                                       serialMod = new serialModel();
                                                                                       serialMod.setSerialCode(araySerial[i]);
                                                                                       if (isbonus.equals("0")) {
                                                                                           serialMod.setCounterSerial(++counterSerial);
                                                                                           serialMod.setVoucherNo(vouch + "");
                                                                                           serialMod.setKindVoucher(kindVoucher + "");
                                                                                           serialMod.setStoreNo(Login.salesMan);
                                                                                           serialMod.setDateVoucher(voucherDate);
                                                                                           serialMod.setKindVoucher(voucherType + "");
                                                                                           serialMod.setItemNo(itemNoSelected);
                                                                                           unitQty.setText(counterSerial + "");


                                                                                       } else {
                                                                                           serialMod.setCounterSerial(++counterBonus);
                                                                                           serialMod.setVoucherNo(vouch + "");
                                                                                           serialMod.setKindVoucher(kindVoucher + "");
                                                                                           serialMod.setStoreNo(Login.salesMan);
                                                                                           serialMod.setDateVoucher(voucherDate);
                                                                                           serialMod.setItemNo(itemNoSelected);
                                                                                           bonus.setText(counterBonus + "");
                                                                                       }

                                                                                       serialMod.setIsBonus(isbonus + "");
                                                                                       listitems_adapter.add(serialMod);
                                                                                       Log.e("listitems_adapter", "" + s.toString());
                                                                                   }

                                                                               } else {// just update on old data row

                                                                                   listitems_adapter.get(id).setSerialCode(s.toString());
                                                                                   Log.e("listitems_adapter", "" + s.toString());
                                                                               }


                                                                               serial_No_recyclerView.setAdapter(new Serial_Adapter(listitems_adapter, cont));

                                                                           } else {
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
                                                                           unitQty.setEnabled(false);
                                                                           counterBonus++;
                                                                           bonus.setText("" + counterBonus);
                                                                           bonus.setEnabled(false);
                                                                           addToList.setVisibility(View.VISIBLE);
                                                                           addToList.setEnabled(true);
                                                                           //                            counterSerial++;
                                                                           //                            unitQty.setText(counterSerial+"");
                                                                           final LinearLayoutManager layoutManager;
                                                                           layoutManager = new LinearLayoutManager(view.getContext());
                                                                           layoutManager.setOrientation(VERTICAL);

                                                                           serial = new serialModel();
                                                                           serial.setCounterSerial(counterBonus);
                                                                           serial.setSerialCode("");
                                                                           serial.setIsBonus("1");
                                                                           serial.setIsDeleted("0");
                                                                           serial.setVoucherNo(vouch + "");
                                                                           serial.setKindVoucher(kindVoucher + "");
                                                                           serialListitems.add(serial);


                                                                           serial_No_recyclerView.setLayoutManager(layoutManager);

                                                                           serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
//

                                                                       }
                                                                   });
                                                                   addEditBarcode = dialog.findViewById(R.id.addEditBarcode);
                                                                   addEditBarcode.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View view) {
                                                                           openeditDialog();
                                                                       }
                                                                   });


                                                               } else {
                                                                   current_itemHasSerial = 0;
                                                                   dialog.setContentView(R.layout.add_item_dialog_small);
                                                                   WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                                   lp.copyFrom(dialog.getWindow().getAttributes());

                                                                   UnitSpinnerLinear= dialog.findViewById(R.id.linearUnit);
                                                                   UnitSpinnerLinear.setVisibility(View.GONE);
                                                                   if(MHandler.getAllSettings().get(0).getItems_Unit()==1)UnitSpinnerLinear.setVisibility(View.VISIBLE);
                                                                   lp.gravity = Gravity.CENTER;
                                                                   lp.windowAnimations = R.style.DialogAnimation;
                                                                   dialog.getWindow().setAttributes(lp);
                                                               }
                                                           } catch (Exception e) {

                                                           }
                                                           serialValue = dialog.findViewById(R.id.serialValue);
                                                           resultLinear = dialog.findViewById(R.id.resultLinear);
                                                           acceptDiscount = dialog.findViewById(R.id.acceptDiscount);
                                                           rejectDiscount = dialog.findViewById(R.id.rejectDiscount);
                                                           checkStateResult = dialog.findViewById(R.id.checkStateResult);
                                                           mainRequestLinear = dialog.findViewById(R.id.mainRequestLinear);
                                                           TextView discount_text = dialog.findViewById(R.id.discount_text);
                                                           TextView bonuss_text = dialog.findViewById(R.id.bonuss_text);
                                                           checkState_recycler = dialog.findViewById(R.id.checkState);
                                                           mainLinear = dialog.findViewById(R.id.mainLinearAddItem);

                                                           try {
                                                               if (languagelocalApp.equals("ar")) {
                                                                   mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                                               } else {
                                                                   if (languagelocalApp.equals("en")) {
                                                                       mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                                                   }

                                                               }
//
                                                           } catch (Exception e) {
                                                               mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                                           }

                                                           final TextView itemNumber = dialog.findViewById(R.id.item_number);
//                  final TextView categoryTextView =  dialog.findViewById(R.id.item_number);
                                                           final TextView itemName = dialog.findViewById(R.id.item_name);
                                                           price = dialog.findViewById(R.id.price);

                                                           final Spinner unit = dialog.findViewById(R.id.unit);
                                                           final Spinner   Item_unit= dialog.findViewById(R.id.Item_unit);
                                                           final TextView textQty = dialog.findViewById(R.id.textQty);
                                                           unitQty = dialog.findViewById(R.id.unitQty);
                                                           final EditText unitWeight = dialog.findViewById(R.id.unitWeight);
                                                           final CheckBox useWeightValue = dialog.findViewById(R.id.use_weight);

                                                           final CheckBox use_OneUnit = dialog.findViewById(R.id.use_OneUnit);

                                                           bonus = dialog.findViewById(R.id.bonus);
                                                           final EditText discount = dialog.findViewById(R.id.discount);
                                                           final RadioGroup radioGroup = dialog.findViewById(R.id.discTypeRadioGroup);
                                                           radioGroup.setVisibility(View.VISIBLE);
                                                           final LinearLayout discountLinearLayout = dialog.findViewById(R.id.discount_linear);
                                                           final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                                                           bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
                                                           final LinearLayout discribtionItem_linear = dialog.findViewById(R.id.discribtionItem_linear);
                                                           final LinearLayout serialNo_linear = dialog.findViewById(R.id.serialNo_linear);
                                                           final EditText item_remark = dialog.findViewById(R.id.item_note);
                                                           final ImageView serialScan = dialog.findViewById(R.id.serialScan);
                                                           RadioButton discPercRadioButton = dialog.findViewById(R.id.discPercRadioButton);
                                                           RadioButton discValueRadioButton = dialog.findViewById(R.id.discValueRadioButton);
                                                           linearPrice = dialog.findViewById(R.id.linearPrice);
                                                           Log.e("Purchase_Order","222="+Purchase_Order);
                                                           if(Purchase_Order==1)
                                                           {
                                                               discountLinearLayout.setVisibility(View.GONE);
                                                               bonusLinearLayout.setVisibility(View.GONE);
                                                               if(CustomerListShow.paymentTerm==0) {
                                                                   price.setVisibility(View.GONE);
                                                                   linearPrice.setVisibility(View.GONE);
                                                               }
                                                           }
//                                                       use_OneUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                                           @Override
//                                                           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                                                             String priceFromList_oneUnit=MHandler.getPriceItem_forUser(items.get(position).getItemNo(),rate_customer);
//                                                           }
//                                                       });

                                                           serialScan.setOnClickListener(new View.OnClickListener() {
                                                               @SuppressLint("WrongConstant")
                                                               @Override
                                                               public void onClick(View v) {
                                                                   //                            context.readB();
                                                                   openSmallScanerTextView();
//                                                               addToList.setVisibility(View.VISIBLE);
//                                                               addToList.setEnabled(true);
//                                                               unitQty.setEnabled(false);
//                                                               flag = 1;
//                                                               counterSerial++;
//                                                               unitQty.setText(counterSerial + "");
//                                                               final LinearLayoutManager layoutManager;
//                                                               layoutManager = new LinearLayoutManager(view.getContext());
//                                                               layoutManager.setOrientation(VERTICAL);
//
//                                                               serial = new serialModel();
//                                                               serial.setCounterSerial(counterSerial);
//                                                               serial.setSerialCode("");
//                                                               serial.setIsBonus("0");
//                                                               serial.setIsDeleted("0");
//                                                               serial.setVoucherNo(vouch+"");
//                                                               serial.setKindVoucher(kindVoucher+"");
//                                                               serial.setStoreNo(Login.salesMan);
//                                                               serial.setDateVoucher(voucherDate);
//                                                               serial.setItemNo(itemNoSelected);
//                                                               serialListitems.add(serial);
//
//
//
//                                                               serial_No_recyclerView.setLayoutManager(layoutManager);
//
//                                                               serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
////
//                                                               if(contiusReading==1)
//                                                               openSmallScanerTextView();

                                                               }
                                                           });


                                                           //***************************************************************************************
                                                           addToList = dialog.findViewById(R.id.addToList);
                                                           Button cancel = dialog.findViewById(R.id.cancelAdd);
                                                           if (MHandler.getAllSettings().get(0).getRequiNote() == 1) {
                                                               discribtionItem_linear.setVisibility(View.VISIBLE);

                                                           } else {
                                                               discribtionItem_linear.setVisibility(View.INVISIBLE);

                                                           }


                                                           approveAdmin = MHandler.getAllSettings().get(0).getApproveAdmin();
                                                           //**********************************************************************************************
                                                           if (MHandler.getAllSettings().get(0).getPriceByCust() == 1) {
                                                               if (allItemsList.get(position).getDiscountCustomer() != 0.0) {
                                                                   haveCstomerDisc = true;
                                                                   discountCustomer = allItemsList.get(position).getDiscountCustomer() + "";

                                                                   discount.setText(allItemsList.get(position).getDiscountCustomer() + "");
//                                                               discount.setEnabled(false);
                                                                   radioGroup.check(R.id.discPercRadioButton);
                                                                   radioGroup.setEnabled(false);
                                                                   discPercRadioButton.setEnabled(false);
                                                                   discValueRadioButton.setEnabled(false);

//                                                               radioGroup.setVisibility(View.GONE);

                                                               }
                                                           }

                                                           //************************************************************************************************

                                                           //***********************************Request Discount ****************************************************

                                                           checkState_recycler.addTextChangedListener(new TextWatcher() {
                                                               @Override
                                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                               }

                                                               @Override
                                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                               }

                                                               @Override
                                                               public void afterTextChanged(Editable s) {
                                                                   Log.e("afterTextChanged", "haveResult" + s.toString());
                                                                   if (s.toString().equals("0")) {

                                                                   } else if ((s.toString().charAt(0) + "").equals("1")) {
                                                                       Log.e("afterTextChanged", "haveResult" + s.toString());
                                                                       haveResult = 1;
                                                                       String key = s.toString().substring(1, s.length());
//                                                                   if(key.equals(currentKey))
//                                                                   {
                                                                       resultLinear.setVisibility(View.VISIBLE);
                                                                       mainRequestLinear.setVisibility(View.GONE);
                                                                       checkStateResult.setVisibility(View.VISIBLE);
                                                                       checkStateResult.setText("Accepted Request");
                                                                       acceptDiscount.setVisibility(View.VISIBLE);
                                                                       rejectDiscount.setVisibility(View.GONE);
                                                                       addToList.setEnabled(true);
//                                                                   }


                                                                   } else if ((s.toString().charAt(0) + "").equals("2")) {
                                                                       haveResult = 2;
                                                                       mainRequestLinear.setVisibility(View.GONE);
                                                                       resultLinear.setVisibility(View.VISIBLE);

                                                                       checkStateResult.setVisibility(View.VISIBLE);
                                                                       checkStateResult.setText("Rejected Request");

                                                                       acceptDiscount.setVisibility(View.GONE);
                                                                       rejectDiscount.setVisibility(View.VISIBLE);
                                                                       discount.setText("");
                                                                       bonus.setText("");
                                                                       addToList.setEnabled(true);
                                                                       unitQty.setEnabled(true);
                                                                       if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                                                           if (voucherType == 506) {
                                                                               price.setEnabled(true);
                                                                           }else {  price.setEnabled(false);}
                                                                       } else {
                                                                           if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                                                               price.setEnabled(true);
                                                                           }
                                                                       }


                                                                       discPercRadioButton.setEnabled(true);
                                                                       discValueRadioButton.setEnabled(true);


                                                                   }

                                                               }
                                                           });
                                                           LinearLayout _linear_switch = dialog.findViewById(R.id._linear_switch);
                                                           ImageView requestDiscount = dialog.findViewById(R.id.requestDiscount);
                                                           request = new requestAdmin(cont);
                                                           if (MHandler.getAllSettings().size() != 0) {
                                                               if (MHandler.getAllSettings().get(0).getBonusNotAlowed() == 0) {//you can  add bonus
                                                                   bonusLinearLayout.setVisibility(View.VISIBLE);
//                        bonusLinearLayout.setVisibility(View.GONE);//test Fro serial
                                                               } else {
                                                                   bonus.setText("0");
                                                                   bonusLinearLayout.setVisibility(View.INVISIBLE);
                                                                   bonus.setEnabled(false);

                                                               }

                                                               if (MHandler.getAllSettings().get(0).getApproveAdmin() == 0) {
                                                                   mainRequestLinear.setVisibility(View.GONE);
                                                                   _linear_switch.setVisibility(View.GONE);

//                          okButton.setVisibility(View.VISIBLE);
                                                               } else {// you need to approve admin for discount or bunos
                                                                   typeRequest = 1;
                                                                   if (allItemsList.get(position).getItemHasSerial().equals("1")) {
                                                                       mainRequestLinear.setVisibility(View.VISIBLE);
                                                                       _linear_switch.setVisibility(View.VISIBLE);
                                                                       discountLinearLayout.setVisibility(View.VISIBLE);
                                                                       bonusLinearLayout.setVisibility(View.GONE);
//                                                                   if (MHandler.getAllSettings().get(0).getBonusNotAlowed() == 0)// you can add bunos
//                                                                   {
//                                                                       bonusLinearLayout.setVisibility(View.VISIBLE);
//                                                                   } else {
//                                                                       bonus.setText("0");
//                                                                       bonusLinearLayout.setVisibility(View.INVISIBLE);
//                                                                       bonus.setEnabled(false);
//                                                                   }

                                                                   } else {
                                                                       mainRequestLinear.setVisibility(View.VISIBLE);
                                                                       discountLinearLayout.setVisibility(View.VISIBLE);
                                                                       bonusLinearLayout.setVisibility(View.GONE);
                                                                   }


                                                                   discount_text.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           typeRequest = 1;
                                                                           discountLinearLayout.setVisibility(View.VISIBLE);
                                                                           discount_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_dark));
                                                                           bonuss_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_shape));
                                                                           bonusLinearLayout.setVisibility(View.GONE);
                                                                       }
                                                                   });
                                                                   bonuss_text.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           if(MHandler.getAllSettings().get(0).getBonusNotAlowed()==0)
                                                                           {
                                                                               typeRequest = 2;
                                                                               discountLinearLayout.setVisibility(View.GONE);
                                                                               bonuss_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_dark));
                                                                               discount_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_shape));
                                                                               bonusLinearLayout.setVisibility(View.VISIBLE);
                                                                               bonus.setText("");
                                                                               bonus.setEnabled(true);
                                                                           }


                                                                       }
                                                                   });
                                                                   requestDiscount.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {

                                                                           String discountText = "";
                                                                           discountPerVal = 0;
                                                                           if ((typeRequest == 1 && !discount.getText().toString().equals("")))// discount
                                                                           {
                                                                               bonus.setText("0");
                                                                               Log.e("request", "" + typeRequest);
                                                                               if (!discount.getText().toString().equals("")) {
                                                                                   if (discPercRadioButton.isChecked()) {
                                                                                       discountPerVal = 1;
                                                                                   } else {
                                                                                       if (discValueRadioButton.isChecked()) {
                                                                                           discountPerVal = 2;
                                                                                       }
                                                                                   }
                                                                                   if (MHandler.getAllSettings().get(0).getPriceByCust() == 1) {
                                                                                       if (allItemsList.get(position).getDiscountCustomer() != 0.0) {
                                                                                           discountText = discount.getText().toString();
                                                                                           haveCstomerDisc = true;
                                                                                           addToList.setEnabled(false);
                                                                                           price.setEnabled(false);
                                                                                           discountCustomer = allItemsList.get(position).getDiscountCustomer() + "";

                                                                                           if (haveCstomerDisc) {

                                                                                               if (!discountCustomer.equals(discountText)) {

                                                                                                   try {
                                                                                                       currentKey = "";
                                                                                                       requestDiscount.setEnabled(false);
                                                                                                       getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                                                                                       addToList.setEnabled(false);
                                                                                                       discount.setEnabled(false);
                                                                                                       unitQty.setEnabled(false);
                                                                                                       price.setEnabled(false);
                                                                                                       discPercRadioButton.setEnabled(false);
                                                                                                       discValueRadioButton.setEnabled(false);
                                                                                                       request.startParsing();
                                                                                                   } catch (Exception e) {
                                                                                                       Log.e("request", "" + e.getMessage());

                                                                                                   }
                                                                                               } else {
                                                                                                   new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                                                                                           .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                                                           .setContentText(view.getContext().getString(R.string.YouHaveItemDiscount))
                                                                                                           .show();
                                                                                               }


                                                                                           }


                                                                                       } else {
                                                                                           try {
                                                                                               getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                                                                               addToList.setEnabled(false);
                                                                                               discount.setEnabled(false);
                                                                                               currentKey = "";
                                                                                               unitQty.setEnabled(false);
                                                                                               price.setEnabled(false);
                                                                                               requestDiscount.setEnabled(false);
                                                                                               discPercRadioButton.setEnabled(false);
                                                                                               discValueRadioButton.setEnabled(false);
                                                                                               request.startParsing();
                                                                                           } catch (Exception e) {
                                                                                               Log.e("request", "" + e.getMessage());

                                                                                           }
                                                                                       }
                                                                                   } else {
                                                                                       try {
                                                                                           getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                                                                           addToList.setEnabled(false);
                                                                                           discount.setEnabled(false);
                                                                                           currentKey = "";
                                                                                           unitQty.setEnabled(false);
                                                                                           price.setEnabled(false);
                                                                                           requestDiscount.setEnabled(false);
                                                                                           discPercRadioButton.setEnabled(false);
                                                                                           discValueRadioButton.setEnabled(false);
                                                                                           request.startParsing();
                                                                                       } catch (Exception e) {
                                                                                           Log.e("request", "" + e.getMessage());

                                                                                       }
                                                                                   }


                                                                               } else {
                                                                                   discount.setError("required");
                                                                                   discount.requestFocus();
                                                                                   unitQty.setEnabled(true);
                                                                                   if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                                                                       if (voucherType == 506) {
                                                                                           price.setEnabled(true);
                                                                                       }else {  price.setEnabled(false);}
                                                                                   } else {


                                                                                       if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                                                                           price.setEnabled(true);
                                                                                       }
                                                                                   }
                                                                               }

                                                                           } else if ((typeRequest == 2 && !bonus.getText().toString().equals(""))) {

                                                                               discount.setText("0");
                                                                               if (!bonus.getText().toString().equals("")) {
                                                                                   try {
                                                                                       Log.e("bonus_request=", "" + bonus.getText().toString());
                                                                                       getDataForDiscountTotal(allItemsList.get(position).getItemName(), "2", allItemsList.get(position).getPrice() + "", bonus.getText().toString(), unitQty.getText().toString());
                                                                                       addToList.setEnabled(false);
                                                                                       bonus.setEnabled(false);
                                                                                       unitQty.setEnabled(false);
                                                                                       price.setEnabled(false);
                                                                                       currentKey = "";
                                                                                       discPercRadioButton.setEnabled(false);
                                                                                       discValueRadioButton.setEnabled(false);
                                                                                       requestDiscount.setEnabled(false);
                                                                                       request.startParsing();
                                                                                   } catch (Exception e) {
                                                                                       Log.e("request", "" + e.getMessage());

                                                                                   }

                                                                               } else {
                                                                                   bonus.setError("required");
                                                                                   bonus.requestFocus();
                                                                                   unitQty.setEnabled(true);
                                                                                   if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                                                                       if (voucherType == 506) {
                                                                                           price.setEnabled(true);
                                                                                       }else {  price.setEnabled(false);}
                                                                                   } else {
                                                                                       if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                                                                           price.setEnabled(true);
                                                                                       }
                                                                                   }
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
                                                                   holder.cardView.setEnabled(true);
                                                                   if (serialListitems.size() != 0) {
                                                                       // delete serial if exist and alert screen if full list
                                                                       AlertDialog.Builder builder2 = new AlertDialog.Builder(context.getActivity());
                                                                       builder2.setTitle(context.getResources().getString(R.string.app_confirm_dialog));
                                                                       builder2.setCancelable(false);
                                                                       builder2.setMessage(context.getResources().getString(R.string.app_confirm_dialog_clear));
                                                                       builder2.setIcon(android.R.drawable.ic_dialog_alert);
                                                                       builder2.setPositiveButton(context.getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                                                                           @Override
                                                                           public void onClick(DialogInterface dialogInterface, int i) {

                                                                               // int vouch = Integer.parseInt(voucherNumberTextView.getText().toString());
                                                                               try {
                                                                                   Log.e("cancel3", "serialListitemsInCardView" + serialListitems.size());
                                                                                   for (int k = 0; k < listMasterSerialForBuckup.size(); k++) {
                                                                                       MHandler.add_SerialBackup(listMasterSerialForBuckup.get(k), 1);
                                                                                   }
                                                                                   Log.e("listMasterSerialFor", "addToList=" + listMasterSerialForBuckup.size());
                                                                               } catch (Exception e) {
                                                                               }

                                                                               serialListitems.clear();
                                                                               Log.e("cancel3", "" + serialListitems.size());
//                                                                           MHandler.deletSerialItems_byVoucherNo(vouch);

                                                                               float count = 0;
                                                                               // delete from main list
//
                                                                               total_items_quantity -= count;
                                                                               totalQty_textView.setText(total_items_quantity + "");
                                                                               dialog.dismiss();


                                                                           }
                                                                       });

                                                                       builder2.setNegativeButton(context.getResources().getString(R.string.app_no), null);
                                                                       builder2.create().show();

                                                                   } else {

                                                                       dialog.dismiss();
                                                                   }


                                                               }
                                                           });
                                                           itemNumber.setText(allItemsList.get(position).getItemNo());
                                                           itemName.setText(allItemsList.get(position).getItemName());
                                                           final DatabaseHandler mHandler = new DatabaseHandler(cont);
                                                           //*********************************** change Price with customer or not accourding to setting  ************************************

                                                           if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                                               price.setText("" + allItemsList.get(position).getPrice());
                                                               if (voucherType == 506) {
                                                                   price.setEnabled(true);
                                                               }else {  price.setEnabled(false);}
                                                           } else {
                                                               if (mHandler.getAllSettings().get(0).getCanChangePrice() == 0) {
                                                               price.setText("" + allItemsList.get(position).getPrice());
                                                               price.setEnabled(false);
                                                               //    price.setText("Desable");

                                                           } else {
                                                               price.setText("" + allItemsList.get(position).getPrice());
                                                           }
                                                       }
                                                           if (mHandler.getAllSettings().get(0).getItemUnit() == 1&&unitsItems_select==0) {
//                                                           if(items.get(position).getPrice()!=0)
//                                                           {
//                                                               price.setText("" + items.get(position).getPrice());
//                                                           }else {
                                                               String itemUnitPrice = mHandler.getUnitPrice(allItemsList.get(position).getItemNo(), rate_customer,0);
                                                               if (!itemUnitPrice.equals(""))
                                                                   price.setText(itemUnitPrice);
//                                                           }

                                                           }

//                                                       if(voucherType==506)
//                                                       {
//                                                           price.setEnabled(false);
//                                                           Log.e("getpreviusePriceSale",""+items.get(position).getItemNo());
//                                                           price.setText(mHandler.getpreviusePriceSale(items.get(position).getItemNo()));
//                                                       }

                                                           if (mHandler.getAllSettings().get(0).getTaxClarcKind() == 1)
//                    discountLinearLayout.setVisibility(View.INVISIBLE);

                                                               if (mHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1) {
                                                                   discountLinearLayout.setVisibility(View.VISIBLE);
                                                               }


                                                           if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                                               unitWeightLinearLayout.setVisibility(View.GONE);// For Serial
                                                               textQty.setText(view.getContext().getResources().getString(R.string.app_qty));
                                                               // useWeight.setChecked(false);
                                                           } else
                                                               unitQty.setText("" + allItemsList.get(position).getItemL());


                                                           List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());
                                                           List<String> ITEMS_units = mHandler.getItemsUnits(itemNumber.getText().toString());
                                                           ITEMS_units.add(0,"");
                                                           ArrayAdapter<String> unitsList = new ArrayAdapter<String>(cont, R.layout.spinner_style, units);
                                                           unit.setAdapter(unitsList);

                                                           ArrayAdapter<String> itemsunitsList = new ArrayAdapter<String>(cont, R.layout.spinner_style, ITEMS_units);
                                                        Item_unit.setAdapter(itemsunitsList);

                                                        try {
                                                            CountOfItems=getCountOfItems(itemNumber.getText().toString(),Item_unit.getSelectedItem().toString());
                                                 Log.e("CountOfItems",CountOfItems+"");
                                                        }catch (Exception e){}

                                                           Item_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                               @Override
                                                               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                 CountOfItems=getCountOfItems(itemNumber.getText().toString(),Item_unit.getSelectedItem().toString());
                                                                 if(!Item_unit.getSelectedItem().toString().equals(""))
                                                                 price.setText(mHandler.getUnitPrice(itemNumber.getText().toString(),"-1",CountOfItems));
                                                                   Log.e("CountOfItems",CountOfItems+"");

                                                               }

                                                               @Override
                                                               public void onNothingSelected(AdapterView<?> adapterView) {

                                                               }
                                                           });

                                                           if ((allItemsList.get(position).getItemHasSerial().equals("1"))) {
                                                               unit.setVisibility(View.GONE);
                                                           }
                                                           unit.setVisibility(View.GONE);
                                                           //****************************************************************************************************


                                                           addToList.setOnClickListener(new View.OnClickListener() {
                                                               @SuppressLint("ResourceAsColor")
                                                               @Override
                                                               public void onClick(View v) {

                                                                 Log.e("orderRadioButton", "orderRadioButton");
                                                                 holder.cardView.setEnabled(true);
                                                                 //listMasterSerialForBuckup.addAll(serialListitems);
                                                                 for (int i = 0; i < listMasterSerialForBuckup.size(); i++) {
                                                                     mHandler.add_SerialBackup(listMasterSerialForBuckup.get(i), 0);
                                                                 }
                                                                 Log.e("listMasterSerialFor", "addToList=" + listMasterSerialForBuckup.size());
                                                                 String qtyText = "", discountText = "", bunosText = "", priceText = "";
                                                                 int countInvalidSerial = 0;
                                                                 if (serialListitems.size() != 0) {
                                                                     countInvalidSerial = checkSerialDB();
                                                                 }

                                                                 qtyText = unitQty.getText().toString();
                                                                 discountText = discount.getText().toString();
                                                                 bunosText = bonus.getText().toString();
                                                                 priceText = price.getText().toString();
                                                                 oneUnit = (use_OneUnit.isChecked()) ? 1 : 0;
                                                                 Log.e("oneUnit", "" + oneUnit);

                                                                 if (haveCstomerDisc) {

                                                                     if (!discountCustomer.equals(discountText)) {

                                                                         haveChangeCustDisc = true;
                                                                     } else {
                                                                         haveResult = 1;
                                                                     }


                                                                 }

                                                                 if (current_itemHasSerial == 0 || (current_itemHasSerial == 1 && countInvalidSerial == 0)) {

                                                                     if (!TextUtils.isEmpty(qtyText) || mHandler.getAllSettings().get(0).getWork_serialNo() == 0) {
                                                                         if (!price.getText().toString().equals("") && !(unitQty.getText().toString()).equals("")) {
                                                                             if (Double.parseDouble(price.getText().toString()) != 0) {
                                                                                 if ((approveAdmin == 0) || (approveAdmin == 1 && TextUtils.isEmpty(bunosText) && typeRequest == 2)
                                                                                         || (approveAdmin == 1 && TextUtils.isEmpty(discountText) && typeRequest == 1)
                                                                                         || (approveAdmin == 1 && !TextUtils.isEmpty(bunosText) && haveResult != 0)
                                                                                         || (approveAdmin == 1 && !TextUtils.isEmpty(discountText) && haveResult != 0 && !haveChangeCustDisc)) {
                                                                                     if (Double.parseDouble(unitQty.getText().toString()) != 0.0) {

                                                                                         Boolean check = check_Discount(unitWeight, unitQty, price, bonus, discount, radioGroup);
                                                                                         if (!check)
                                                                                             Toast.makeText(view.getContext(), "Invalid Disco" +
                                                                                                     "unt Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                                                                                         else {

                                                                                             String unitValue;
                                                                                             Log.e("useWeightValue", "" + useWeightValue.isChecked());
                                                                                             if (useWeightValue.isChecked()) {
                                                                                                 useWeight = 1;
                                                                                             } else {
                                                                                                 useWeight = 0;
                                                                                             }
                                                                                             currentKey = "";
                                                                                             if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                                                                                 unitValue = unit.getSelectedItem().toString();
                                                                                                 // Log.e("unitValue", "" + unitValue);
//                                                                                               if (items.get(holder.getAdapterPosition()).getQty() >= Double.parseDouble(unitQty.getText().toString())
//                                                                                                       || mHandler.getAllSettings().get(0).getAllowMinus() == 1
//                                                                                                       || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508)

                                                                                                 if (validQty(allItemsList.get(position).getQty(), Float.parseFloat(unitQty.getText().toString()))) {
                                                                                                     Log.e("ayah,price", "" + price.getText().toString()+" ,, "+allItemsList.get(position).getMinSalePrice());
                                                                                                     double dis__;
                                                                                                     if(!discountText.toString().toString().equals(""))
                                                                                                         dis__=Double.parseDouble(discountText.toString().trim());
                                                                                                     else
                                                                                                         dis__=0;


                                                                                                     double priceafterdis=0;
                                                                                                     if (radioGroup.getCheckedRadioButtonId() == R.id.discPercRadioButton) {
                                                                                                        // priceafterdis=Double.parseDouble(price.getText().toString())-   ( Double.parseDouble(price.getText().toString())* (Double.parseDouble(dis__)*0.01));
                                                                                                         double  disforOneItem= (dis__*0.01)/Double.parseDouble(unitQty.getText().toString());
                                                                                                         Log.e("ayah,disforOneItem", "" +disforOneItem);
                                                                                                         priceafterdis=Double.parseDouble(price.getText().toString())-(Double.parseDouble(price.getText().toString())*disforOneItem);

                                                                                                     } else {
                                                                                                         double  disforOneItem= (dis__)/Double.parseDouble(unitQty.getText().toString());
                                                                                                         Log.e("ayah,disforOneItem", "" +disforOneItem);
                                                                                                         priceafterdis=Double.parseDouble(price.getText().toString())-    disforOneItem;


                                                                                                     }
                                                                                                     Log.e("ayah,priceafterdis", "" +priceafterdis);

                                                                                                     if ( voucherType==506 || mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1   &&
                                                                                                                priceafterdis >= allItemsList.get(position).getMinSalePrice())) {

                                                                                                         AddItemsFragment2 obj = new AddItemsFragment2();
                                                                                                         List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                                                                         Offers appliedOffer = null;
                                                                                                         Log.e("appliedOffer", "1111===" + offer.size());
////&& voucherType==504
                                                                                                         if (offer.size() != 0) {

                                                                                                             if (offer.get(0).getPromotionType() == 0) {// bonus promotion

                                                                                                                 added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                         holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                                                                         bonus.getText().toString(),
                                                                                                                         discount.getText().toString(),
                                                                                                                         radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "", useWeight,
                                                                                                                         view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);

                                                                                                                 appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 0);

                                                                                                                 if (!appliedOffer.getItemNo().equals("-1")) {
                                                                                                                     double bonus_calc = 0;

                                                                                                                     if (
                                                                                                                             OfferCakeShop == 0) {
                                                                                                                         bonus_calc = ((int) (Double.parseDouble(unitQty.getText().toString()) / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();

                                                                                                                     } else {
                                                                                                                         bonus_calc = appliedOffer.getBonusQty();
                                                                                                                     }
                                                                                                                     // Log.e("bonus_calc=", "added1" + added);
                                                                                                                     Log.e("bonus_calc=", "" + bonus_calc);
                                                                                                                     added = obj.addItem(offer.get(0).getBonusItemNo(), "(bonus)",
                                                                                                                             "0", "1", "" + bonus_calc, "0",
                                                                                                                             "0", "0", radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "", useWeight, view.getContext()
                                                                                                                             , item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);

                                                                                                                     Log.e("bonus_calc=", "added2" + added);
                                                                                                                 }

                                                                                                             } else {
                                                                                                                 //  Log.e("getPromotionType", "" + offer.get(0).getPromotionType());
                                                                                                                 //(appliedOffer.getBonusQty()*Double.parseDouble(unitQty.getText().toString()))   //******calculate discount item before 11/9
                                                                                                                 double disount_totalnew = 0, unitQty_double = 0;

                                                                                                                 appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 1);
                                                                                                                 Log.e("appliedOffer", "" + appliedOffer.getItemNo());
                                                                                                                 if (!appliedOffer.getItemNo().equals("-1")) {
//                                                                                                               if (appliedOffer != null) {
                                                                                                                     Log.e("appliedOffer", "appliedOffer.getItemQty()" + appliedOffer.getItemQty() + appliedOffer.getBonusQty());
                                                                                                                     unitQty_double = Double.parseDouble(unitQty.getText().toString());
                                                                                                                     if (offerTalaat == 1) {
                                                                                                                         disount_totalnew = ((int) (unitQty_double / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();
                                                                                                                     } else {
                                                                                                                         disount_totalnew = (unitQty_double * appliedOffer.getBonusQty());
                                                                                                                     }
                                                                                                                     if (offerQasion == 1) {
                                                                                                                         disount_totalnew = (unitQty_double * appliedOffer.getBonusQty());
                                                                                                                     }
                                                                                                                     // get discount from offers not from text


                                                                                                                     String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());


                                                                                                                     Log.e("appliedOffer11=", "" + disount_totalnew);

                                                                                                                     if (appliedOffer.getDiscountItemType() == 0)
                                                                                                                         radioGroup.check(R.id.discValueRadioButton);// just if promotion is discount
                                                                                                                     else if (appliedOffer.getDiscountItemType() == 1)
                                                                                                                         radioGroup.check(R.id.discPercRadioButton);// just if promotion is discount


                                                                                                                     added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                             holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                                                                             bonus.getText().toString(), "" + disount_totalnew, radioGroup
                                                                                                                             , allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                                                             useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                             current_itemHasSerial, oneUnit);
                                                                                                                 } else {// not exist offer for this qty
                                                                                                                     added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                             holder.tax.getText().toString(), unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                                                                             bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                                                                             allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                                                             useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                             current_itemHasSerial, oneUnit);


                                                                                                                     Log.e("else", "appliedOffer==null");
                                                                                                                 }
                                                                                                             }
                                                                                                         } else {
                                                                                                             double totalQty = 0;
                                                                                                             totalQty = Double.parseDouble(unitQty.getText().toString()) + Double.parseDouble(bonus.getText().toString());
                                                                                                             // Log.e("totalQty+recyclerview", "" + totalQty + "\t bomus" + bonus.getText().toString());

                                                                                                             added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                     holder.tax.getText().toString(), unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                                                                     bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                                                                     allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                                                     useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                     current_itemHasSerial, oneUnit);
                                                                                                         }
                                                                                                         if (added) {
                                                                                                             if (offer.size() != 0 && !appliedOffer.getItemNo().equals("-1"))
                                                                                                                 if(ActiveStateOfOffers())    openOfferDialog(appliedOffer);

                                                                                                             holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.layer5));
                                                                                                             isClicked.set(position, 1);
                                                                                                             localItemNumber.add(allItemsList.get(position).getItemNo());
                                                                                                             itemInlocalList = false;
                                                                                                             dialog.dismiss();
                                                                                                         }
                                                                                                     } else {
                                                                                                         Toast.makeText(view.getContext(), "Item hasn't been added, Min sale price for this item is " + allItemsList.get(position).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                                                                         Log.e("bonus not added ", "" + allItemsList.get(position).getMinSalePrice());
                                                                                                         price.setError(view.getResources().getString(R.string.invalidValue));

                                                                                                     }
                                                                                                 } else {
                                                                                                     unitQty.setError(view.getContext().getResources().getString(R.string.insufficientQuantity));
                                                                                                     Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.insufficientQuantity), Toast.LENGTH_LONG).show();

                                                                                                 }

                                                                                             } else {
                                                                                                 if (unitWeight.getText().toString().equals(""))
                                                                                                     Toast.makeText(view.getContext(), "please enter unit weight", Toast.LENGTH_LONG).show();
                                                                                                 else {
                                                                                                     unitValue = unitWeight.getText().toString();
//                                        String qtyValue = "" + (Double.parseDouble(unitWeight.getText().toString()) * Double.parseDouble(unitQty.getText().toString()));
                                                                                                     String qty = (useWeightValue.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString())) : unitQty.getText().toString());
//                                                                                                   String qty = (useWeightValue.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString()) * Double.parseDouble(unitValue)) : unitQty.getText().toString());

                                                                                                     Log.e("here**", "" + position);
                                                                                                     if (position > -1) {
                                                                                                         if (allItemsList.get(position).getQty() >= Double.parseDouble(qty)
                                                                                                                 || mHandler.getAllSettings().get(0).getAllowMinus() == 1
                                                                                                                 || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508) {
                                                                                                             if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                                                                                     Double.parseDouble(price.getText().toString()) >= allItemsList.get(position).getMinSalePrice())) {

                                                                                                                 AddItemsFragment2 obj = new AddItemsFragment2();
                                                                                                                 List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                                                                                 Offers appliedOffer = null;

                                                                                                                 if (offer.size() != 0 ) {
                                                                                                                     if (offer.get(0).getPromotionType() == 0) {

                                                                                                                         added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                                 holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                                                                 bonus.getText().toString(), discount.getText().toString(),
                                                                                                                                 radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                                                                 useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                                 current_itemHasSerial, oneUnit);

                                                                                                                         appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 0);
                                                                                                                         if (!appliedOffer.getItemNo().equals("-1")) {


                                                                                                                             added = obj.addItem(appliedOffer.getBonusItemNo(), "(bonus)",
                                                                                                                                     "0", "1", "" + appliedOffer.getBonusQty(), "0",
                                                                                                                                     "0", "0", radioGroup
                                                                                                                                     , allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + ""
                                                                                                                                     , useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                                     current_itemHasSerial, oneUnit);
                                                                                                                         }

                                                                                                                     } else {
                                                                                                                         appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 1);
                                                                                                                         if (!appliedOffer.getItemNo().equals("-1")) {
                                                                                                                             String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());
                                                                                                                             added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                                     holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                                                                     bonus.getText().toString(), "" + (appliedOffer.getBonusQty() * Double.parseDouble(qty)), radioGroup,
                                                                                                                                     allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + ""
                                                                                                                                     , useWeight, view.getContext(), item_remark.getText().toString(), serialListitems,
                                                                                                                                     current_itemHasSerial, oneUnit);
                                                                                                                         }
                                                                                                                     }
                                                                                                                 } else {
                                                                                                                     added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                                                             holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                                                             bonus.getText().toString(), discount.getText().toString(),
                                                                                                                             radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                                                             useWeight, view.getContext(), item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);
                                                                                                                 }
                                                                                                                 if (added) {
                                                                                                                     if (offer.size() != 0 && !appliedOffer.getItemNo().equals("-1"))
                                                                                                                         if(ActiveStateOfOffers())       openOfferDialog(appliedOffer);
                                                                                                                     holder.linearLayout.setBackgroundColor(R.color.done_button);
                                                                                                                     isClicked.set(position, 1);
                                                                                                                     dialog.dismiss();
                                                                                                                 }
                                                                                                             } else
                                                                                                                 Toast.makeText(view.getContext(), "Item hasn't been added, Min sale price for this item is " + allItemsList.get(position).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                                                                         } else
                                                                                                             Toast.makeText(view.getContext(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                                                                                     } else
                                                                                                         Toast.makeText(view.getContext(), "Please enter the item again", Toast.LENGTH_LONG).show();
                                                                                                 }
                                                                                             }

                                                                                         }

//                                                                                       dialog.dismiss();
                                                                                       } else {
                                                                                           Toast.makeText(view.getContext(), "Invalid  Qty", Toast.LENGTH_LONG).show();
                                                                                       }
                                                                                   } else {
                                                                                       if (haveResult == 0) {
                                                                                           new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                                                   .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                                                   .setContentText(view.getContext().getString(R.string.youHaveRequestToAdmin))
                                                                                                   .show();
                                                                                       }


                                                                                 }

                                                                             } else {
                                                                                 price.setError(view.getResources().getString(R.string.invalidValue));
                                                                                 price.requestFocus();
                                                                             }
                                                                         } else {
                                                                             if (TextUtils.isEmpty(qtyText)) {
                                                                                 unitQty.setError("*Required");
                                                                             }

                                                                             if (TextUtils.isEmpty(priceText)) {
                                                                                 price.setError("*Required");
                                                                             }
                                                                         }

                                                                     } else {
                                                                         unitQty.setError("*Required");
                                                                         price.requestFocus();
                                                                     }
                                                                 } else {// item  serial_No is duplcate +++++ dont save

                                                                     if (countInvalidSerial == -1) {
                                                                         new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                                 .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                                 .setContentText(view.getContext().getString(R.string.reqired_filled))
                                                                                 .show();
                                                                     } else {
                                                                         if (countInvalidSerial == 100) {
                                                                             new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                                     .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                                     .setContentText(view.getContext().getString(R.string.thereIsduplicatedSerial))
                                                                                     .show();

                                                                         } else if (countInvalidSerial == 1000) {
                                                                             new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                                     .setTitleText(context.getString(R.string.warning_message))
                                                                                     .setContentText(context.getString(R.string.duplicate) + "\t" + context.getResources().getString(R.string.inThisVoucher))

                                                                                     .show();
                                                                         } else {
                                                                             new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                                     .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                                     .setContentText(view.getContext().getString(R.string.itemadedbefor))
                                                                                     .show();
                                                                         }

                                                                     }


                                                                 }
                                                                 haveChangeCustDisc = false;

                                                               }// end on click
                                                           });
                                                           dialog.show();

                                                       } else {
                                                           new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                                   .setTitleText(view.getContext().getString(R.string.warning_message))
                                                                   .setContentText(view.getContext().getString(R.string.itemadedbefor))
                                                                   .show();
                                                       }
                                                   }
                                               }//on click

            );
        }

    }

    private void openDialogQtyItem(int position, AddItemsFragment2 context) {
        if (itemInlocalList == false) {
            final Dialog dialog = new Dialog(context.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            itemNoSelected = allItemsList.get(position).getItemNo();

            try {

                if ((allItemsList.get(position).getItemHasSerial().equals("1")) && voucherType != 508) {
                    current_itemHasSerial = 1;

                    dialog.setContentView(R.layout.add_item_serial_dialog);
                    serialValue = dialog.findViewById(R.id.serialValue);
                    linearPrice = dialog.findViewById(R.id.linearPrice);
                    linearPrice.setVisibility(View.VISIBLE);
                    mainRequestLinear = dialog.findViewById(R.id.mainRequestLinear);
                    checkStateResult = dialog.findViewById(R.id.checkStateResult);
                    rejectDiscount = dialog.findViewById(R.id.rejectDiscount);
                    mainRequestLinear.setVisibility(View.VISIBLE);
                    unitQty = dialog.findViewById(R.id.unitQty);
                    unitQty.setEnabled(false);

                    if (contiusReading == 0) {
                        serialValue.requestFocus();
                        serialValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                                  @Override
                                                                  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                                      if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH
                                                                              || actionId == EditorInfo.IME_NULL) {
                                                                          if (!serialValue.getText().toString().equals("")) {
                                                                              barcodeValue = serialValue.getText().toString().trim();
//                                                                               serialValue_Model.setText(s.toString().trim());
                                                                              updateValue(barcodeValue, serialListitems);
                                                                              new Handler().post(new Runnable() {
                                                                                  @Override
                                                                                  public void run() {

                                                                                      serialValue.requestFocus();

                                                                                  }
                                                                              });


                                                                          }

                                                                      }
                                                                      return false;
                                                                  }

                                                              }

                        );
                    } else {
                        try {
                            serialValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!s.toString().equals("")) {
                                        barcodeValue = s.toString().trim();
//                                                                               serialValue_Model.setText(s.toString().trim());
                                        updateValue(barcodeValue, serialListitems);

                                    }


                                }
                            });
                        } catch (Exception e) {
                        }
                    }


                    mainLinear = dialog.findViewById(R.id.mainLinearAddItem);
                    bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
                    bonusLinearLayout.setVisibility(View.GONE);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());

                    lp.gravity = Gravity.CENTER;
                    lp.windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setAttributes(lp);
                    bonus = dialog.findViewById(R.id.bonus);
                    addToList = dialog.findViewById(R.id.addToList);
                    addToList.setVisibility(View.INVISIBLE);
                    bonus.setEnabled(false);
                    addToList.setEnabled(false);

                    serial_No_recyclerView = dialog.findViewById(R.id.serial_No_recyclerView);
                    final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                    unitWeightLinearLayout.setVisibility(View.GONE);
                    item_serial = dialog.findViewById(R.id.item_serial);
                    final ImageView serialScanBunos = dialog.findViewById(R.id.serialScanBunos);
                    serialScanBunos.setVisibility(View.GONE);
                    TextView generateSerial = (TextView) dialog.findViewById(R.id.generateSerial);

                    generateSerial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int qtySerial = 0;

                            if (!unitQty.getText().toString().equals("") && serialListitems.size() == 0) {
                                try {
                                    qtySerial = (int) Double.parseDouble(unitQty.getText().toString());
                                } catch (Exception e) {
                                    qtySerial = Integer.parseInt(unitQty.getText().toString());
                                }
                                if (qtySerial <= 100) {
                                    counterSerial = qtySerial;
                                    if (qtySerial != 0) {
                                        flag = 1;
                                        addToList.setEnabled(true);
                                        addToList.setVisibility(View.VISIBLE);
//                                            counterSerial++;
//                                            unitQty.setText(counterSerial+"");
                                        final LinearLayoutManager layoutManager;
                                        layoutManager = new LinearLayoutManager(context.getActivity());
//                                            layoutManager.setOrientation(VERTICAL);

                                        for (int i = 1; i <= qtySerial; i++) {
                                            serial = new serialModel();
                                            serial.setCounterSerial(i);
                                            serial.setSerialCode("");
                                            serial.setIsBonus("0");
                                            serial.setIsDeleted("0");
                                            serial.setVoucherNo(vouch + "");
                                            serial.setKindVoucher(kindVoucher + "");
                                            serial.setStoreNo(Login.salesMan);
                                            serial.setDateVoucher(voucherDate);
                                            serial.setItemNo(itemNoSelected);
                                            serialListitems.add(serial);

                                        }


                                        serial_No_recyclerView.setLayoutManager(layoutManager);

                                        serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
                                        unitQty.setEnabled(false);
                                        generateSerial.setEnabled(false);
                                    } else {
                                        unitQty.setError("Invalid Zero");
                                    }
                                } else {
                                    unitQty.setError("Invalid");
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
                            isFoundSerial = false;

                            for (int h = 0; h < serialListitems.size(); h++) {

                                if (serialListitems.get(h).getSerialCode().equals(s.toString())) {
                                    isFoundSerial = true;
                                }
                            }
                            Log.e("isFoundSerial", "" + isFoundSerial + s.toString());
                            if (isFoundSerial == false) {
                                List<serialModel> listitems_adapter = new ArrayList<>();
                                int id = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).selectedBarcode;
                                listitems_adapter = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).list;

                                Log.e("curentSerial", "" + id + s.toString());
                                final LinearLayoutManager layoutManager;
                                layoutManager = new LinearLayoutManager(context.getActivity());
                                layoutManager.setOrientation(VERTICAL);
                                serial_No_recyclerView.setLayoutManager(layoutManager);
                                if (s.toString().contains(","))//  update old data and add new data
                                {
                                    serialModel serialMod;
                                    araySerial = s.toString().split(",");
                                    listitems_adapter.get(id).setSerialCode(araySerial[0]);
                                    String isbonus = listitems_adapter.get(id).getIsBonus();
                                    for (int i = 1; i < araySerial.length; i++) {
                                        serialMod = new serialModel();
                                        serialMod.setSerialCode(araySerial[i]);
                                        if (isbonus.equals("0")) {
                                            serialMod.setCounterSerial(++counterSerial);
                                            serialMod.setVoucherNo(vouch + "");
                                            serialMod.setKindVoucher(kindVoucher + "");
                                            serialMod.setStoreNo(Login.salesMan);
                                            serialMod.setDateVoucher(voucherDate);
                                            serialMod.setKindVoucher(voucherType + "");
                                            serialMod.setItemNo(itemNoSelected);
                                            unitQty.setText(counterSerial + "");


                                        } else {
                                            serialMod.setCounterSerial(++counterBonus);
                                            serialMod.setVoucherNo(vouch + "");
                                            serialMod.setKindVoucher(kindVoucher + "");
                                            serialMod.setStoreNo(Login.salesMan);
                                            serialMod.setDateVoucher(voucherDate);
                                            serialMod.setItemNo(itemNoSelected);
                                            bonus.setText(counterBonus + "");
                                        }

                                        serialMod.setIsBonus(isbonus + "");
                                        listitems_adapter.add(serialMod);
                                        Log.e("listitems_adapter", "" + s.toString());
                                    }

                                } else {// just update on old data row

                                    listitems_adapter.get(id).setSerialCode(s.toString());
                                    Log.e("listitems_adapter", "" + s.toString());
                                }


                                serial_No_recyclerView.setAdapter(new Serial_Adapter(listitems_adapter, cont));

                            } else {
                                new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(context.getString(R.string.warning_message))
                                        .setContentText(context.getString(R.string.itemadedbefor))
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
                            unitQty.setEnabled(false);
                            counterBonus++;
                            bonus.setText("" + counterBonus);
                            bonus.setEnabled(false);
                            addToList.setVisibility(View.VISIBLE);
                            addToList.setEnabled(true);
                            //                            counterSerial++;
                            //                            unitQty.setText(counterSerial+"");
                            final LinearLayoutManager layoutManager;
                            layoutManager = new LinearLayoutManager(context.getActivity());
                            layoutManager.setOrientation(VERTICAL);

                            serial = new serialModel();
                            serial.setCounterSerial(counterBonus);
                            serial.setSerialCode("");
                            serial.setIsBonus("1");
                            serial.setIsDeleted("0");
                            serial.setVoucherNo(vouch + "");
                            serial.setKindVoucher(kindVoucher + "");
                            serialListitems.add(serial);


                            serial_No_recyclerView.setLayoutManager(layoutManager);

                            serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
//

                        }
                    });
                    addEditBarcode = dialog.findViewById(R.id.addEditBarcode);
                    addEditBarcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openeditDialog();
                        }
                    });


                } else {
                    current_itemHasSerial = 0;
                    dialog.setContentView(R.layout.add_item_dialog_small);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());


                    lp.gravity = Gravity.CENTER;
                    lp.windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setAttributes(lp);
                }
            } catch (Exception e) {

            }
            linearPrice = dialog.findViewById(R.id.linearPrice);
            linearPrice.setVisibility(View.VISIBLE);//**************اثقث
            serialValue = dialog.findViewById(R.id.serialValue);
            resultLinear = dialog.findViewById(R.id.resultLinear);
            acceptDiscount = dialog.findViewById(R.id.acceptDiscount);
            rejectDiscount = dialog.findViewById(R.id.rejectDiscount);
            checkStateResult = dialog.findViewById(R.id.checkStateResult);
            mainRequestLinear = dialog.findViewById(R.id.mainRequestLinear);
            TextView discount_text = dialog.findViewById(R.id.discount_text);
            TextView bonuss_text = dialog.findViewById(R.id.bonuss_text);
            checkState_recycler = dialog.findViewById(R.id.checkState);
            mainLinear = dialog.findViewById(R.id.mainLinearAddItem);
            unitQty.requestFocus();

            try {
                if (languagelocalApp.equals("ar")) {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                } else {
                    if (languagelocalApp.equals("en")) {
                        mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }

                }
//
            } catch (Exception e) {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

            final TextView itemNumber = dialog.findViewById(R.id.item_number);
//                  final TextView categoryTextView =  dialog.findViewById(R.id.item_number);
            final TextView itemName = dialog.findViewById(R.id.item_name);
            price = dialog.findViewById(R.id.price);

            final Spinner unit = dialog.findViewById(R.id.unit);
            final TextView textQty = dialog.findViewById(R.id.textQty);
            unitQty = dialog.findViewById(R.id.unitQty);
            final EditText unitWeight = dialog.findViewById(R.id.unitWeight);
            final CheckBox useWeightValue = dialog.findViewById(R.id.use_weight);

            final CheckBox use_OneUnit = dialog.findViewById(R.id.use_OneUnit);

            bonus = dialog.findViewById(R.id.bonus);
            final EditText discount = dialog.findViewById(R.id.discount);
            final RadioGroup radioGroup = dialog.findViewById(R.id.discTypeRadioGroup);
            radioGroup.setVisibility(View.VISIBLE);
            final LinearLayout discountLinearLayout = dialog.findViewById(R.id.discount_linear);
            final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
            bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
            Log.e("Purchase_Order","==****"+Purchase_Order);
            if(Purchase_Order==1)
            {
                discountLinearLayout.setVisibility(View.GONE);
                bonusLinearLayout.setVisibility(View.GONE);
                if(CustomerListShow.paymentTerm==0)
                linearPrice.setVisibility(View.GONE);
            }

            final LinearLayout discribtionItem_linear = dialog.findViewById(R.id.discribtionItem_linear);
            final LinearLayout serialNo_linear = dialog.findViewById(R.id.serialNo_linear);
            final EditText item_remark = dialog.findViewById(R.id.item_note);
            final ImageView serialScan = dialog.findViewById(R.id.serialScan);
            RadioButton discPercRadioButton = dialog.findViewById(R.id.discPercRadioButton);
            RadioButton discValueRadioButton = dialog.findViewById(R.id.discValueRadioButton);

//                                                       use_OneUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                                           @Override
//                                                           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                                                             String priceFromList_oneUnit=MHandler.getPriceItem_forUser(items.get(position).getItemNo(),rate_customer);
//                                                           }
//                                                       });

            serialScan.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    openSmallScanerTextView();


                }
            });


            //***************************************************************************************
            addToList = dialog.findViewById(R.id.addToList);
            Button cancel = dialog.findViewById(R.id.cancelAdd);
            if (MHandler.getAllSettings().get(0).getRequiNote() == 1) {
                discribtionItem_linear.setVisibility(View.VISIBLE);

            } else {
                discribtionItem_linear.setVisibility(View.INVISIBLE);

            }


            approveAdmin = MHandler.getAllSettings().get(0).getApproveAdmin();
            //**********************************************************************************************
            if (MHandler.getAllSettings().get(0).getPriceByCust() == 1) {
                if (allItemsList.get(position).getDiscountCustomer() != 0.0) {
                    haveCstomerDisc = true;
                    discountCustomer = allItemsList.get(position).getDiscountCustomer() + "";

                    discount.setText(allItemsList.get(position).getDiscountCustomer() + "");
//                                                               discount.setEnabled(false);
                    radioGroup.check(R.id.discPercRadioButton);
                    radioGroup.setEnabled(false);
                    discPercRadioButton.setEnabled(false);
                    discValueRadioButton.setEnabled(false);

//                                                               radioGroup.setVisibility(View.GONE);

                }
            }

            //************************************************************************************************

            //***********************************Request Discount ****************************************************

            checkState_recycler.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("afterTextChanged", "haveResult" + s.toString());
                    if (s.toString().equals("0")) {

                    } else if ((s.toString().charAt(0) + "").equals("1")) {
                        Log.e("afterTextChanged", "haveResult" + s.toString());
                        haveResult = 1;
                        String key = s.toString().substring(1, s.length());
//                                                                   if(key.equals(currentKey))
//                                                                   {
                        resultLinear.setVisibility(View.VISIBLE);
                        mainRequestLinear.setVisibility(View.GONE);
                        checkStateResult.setVisibility(View.VISIBLE);
                        checkStateResult.setText("Accepted Request");
                        acceptDiscount.setVisibility(View.VISIBLE);
                        rejectDiscount.setVisibility(View.GONE);
                        addToList.setEnabled(true);
//                                                                   }


                    } else if ((s.toString().charAt(0) + "").equals("2")) {
                        haveResult = 2;
                        mainRequestLinear.setVisibility(View.GONE);
                        resultLinear.setVisibility(View.VISIBLE);

                        checkStateResult.setVisibility(View.VISIBLE);
                        checkStateResult.setText("Rejected Request");

                        acceptDiscount.setVisibility(View.GONE);
                        rejectDiscount.setVisibility(View.VISIBLE);
                        discount.setText("");
                        bonus.setText("");
                        addToList.setEnabled(true);
                        unitQty.setEnabled(true);
                        if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                            if (voucherType == 506) {
                                price.setEnabled(true);
                            }else {  price.setEnabled(false);}
                        } else {
                            if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                price.setEnabled(true);
                            }
                        }

                        discPercRadioButton.setEnabled(true);
                        discValueRadioButton.setEnabled(true);


                    }

                }
            });
            LinearLayout _linear_switch = dialog.findViewById(R.id._linear_switch);
            ImageView requestDiscount = dialog.findViewById(R.id.requestDiscount);
            request = new requestAdmin(cont);
            if (MHandler.getAllSettings().size() != 0) {
                if (MHandler.getAllSettings().get(0).getBonusNotAlowed() == 0) {//you can  add bonus
                    bonusLinearLayout.setVisibility(View.VISIBLE);
//                        bonusLinearLayout.setVisibility(View.GONE);//test Fro serial
                } else {
                    bonus.setText("0");
                    bonusLinearLayout.setVisibility(View.INVISIBLE);
                    bonus.setEnabled(false);

                }

                if (MHandler.getAllSettings().get(0).getApproveAdmin() == 0) {
                    mainRequestLinear.setVisibility(View.GONE);
                    _linear_switch.setVisibility(View.GONE);

//                          okButton.setVisibility(View.VISIBLE);
                } else {// you need to approve admin for discount or bunos
                    typeRequest = 1;
                    if (allItemsList.get(position).getItemHasSerial().equals("1")) {
                        mainRequestLinear.setVisibility(View.VISIBLE);
                        _linear_switch.setVisibility(View.VISIBLE);
                        discountLinearLayout.setVisibility(View.VISIBLE);
                        bonusLinearLayout.setVisibility(View.GONE);


                    } else {
                        mainRequestLinear.setVisibility(View.VISIBLE);
                        discountLinearLayout.setVisibility(View.VISIBLE);
                        bonusLinearLayout.setVisibility(View.GONE);
                    }


                    discount_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            typeRequest = 1;
                            discountLinearLayout.setVisibility(View.VISIBLE);
                            discount_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_dark));
                            bonuss_text.setBackground(context.getResources().getDrawable(R.drawable.back_border_shape));
                            bonusLinearLayout.setVisibility(View.GONE);
                        }
                    });
                    bonuss_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            typeRequest = 2;
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
                            currentKey = "";
                            currentKeyTotalDiscount="";
                            keyCreditLimit="";


                            String discountText = "";
                            discountPerVal = 0;
                            if ((typeRequest == 1 && !discount.getText().toString().equals("")))// discount
                            {
                                Log.e("request", "" + typeRequest);
                                if (!discount.getText().toString().equals("")) {
                                    if (discPercRadioButton.isChecked()) {
                                        discountPerVal = 1;
                                    } else {
                                        if (discValueRadioButton.isChecked()) {
                                            discountPerVal = 2;
                                        }
                                    }
                                    if (MHandler.getAllSettings().get(0).getPriceByCust() == 1) {
                                        if (allItemsList.get(position).getDiscountCustomer() != 0.0) {
                                            discountText = discount.getText().toString();
                                            haveCstomerDisc = true;
                                            addToList.setEnabled(false);
                                            price.setEnabled(false);
                                            discountCustomer = allItemsList.get(position).getDiscountCustomer() + "";

                                            if (haveCstomerDisc) {

                                                if (!discountCustomer.equals(discountText)) {

                                                    try {
                                                        currentKey = "";
                                                        requestDiscount.setEnabled(false);
                                                        getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                                        addToList.setEnabled(false);
                                                        discount.setEnabled(false);
                                                        unitQty.setEnabled(false);
                                                        price.setEnabled(false);
                                                        discPercRadioButton.setEnabled(false);
                                                        discValueRadioButton.setEnabled(false);
                                                        request.startParsing();
                                                    } catch (Exception e) {
                                                        Log.e("request", "" + e.getMessage());

                                                    }
                                                } else {
                                                    new SweetAlertDialog(context.getActivity(), SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText(context.getString(R.string.warning_message))
                                                            .setContentText(context.getString(R.string.YouHaveItemDiscount))
                                                            .show();
                                                }


                                            }


                                        } else {
                                            try {
                                                getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                                addToList.setEnabled(false);
                                                discount.setEnabled(false);
                                                currentKey = "";
                                                unitQty.setEnabled(false);
                                                price.setEnabled(false);
                                                requestDiscount.setEnabled(false);
                                                discPercRadioButton.setEnabled(false);
                                                discValueRadioButton.setEnabled(false);
                                                request.startParsing();
                                            } catch (Exception e) {
                                                Log.e("request", "" + e.getMessage());

                                            }
                                        }
                                    } else {
                                        try {
                                            getDataForDiscountTotal(allItemsList.get(position).getItemName(), "0", allItemsList.get(position).getPrice() + "", discount.getText().toString(), unitQty.getText().toString());
                                            addToList.setEnabled(false);
                                            discount.setEnabled(false);
                                            currentKey = "";
                                            unitQty.setEnabled(false);
                                            price.setEnabled(false);
                                            requestDiscount.setEnabled(false);
                                            discPercRadioButton.setEnabled(false);
                                            discValueRadioButton.setEnabled(false);
                                            request.startParsing();
                                        } catch (Exception e) {
                                            Log.e("request", "" + e.getMessage());

                                        }
                                    }


                                } else {
                                    discount.setError("required");
                                    discount.requestFocus();
                                    unitQty.setEnabled(true);
                                    if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                        if (voucherType == 506) {
                                            price.setEnabled(true);
                                        }else {  price.setEnabled(false);}
                                    } else {
                                        if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                            price.setEnabled(true);
                                        }
                                    }
                                }

                            } else if ((typeRequest == 2 && !bonus.getText().toString().equals(""))) {

                                if (!bonus.getText().toString().equals("")) {
                                    try {
                                        Log.e("bonus_request=", "" + bonus.getText().toString());
                                        getDataForDiscountTotal(allItemsList.get(position).getItemName(), "2", allItemsList.get(position).getPrice() + "", bonus.getText().toString(), unitQty.getText().toString());
                                        addToList.setEnabled(false);
                                        bonus.setEnabled(false);
                                        unitQty.setEnabled(false);
                                        price.setEnabled(false);
                                        currentKey = "";
                                        discPercRadioButton.setEnabled(false);
                                        discValueRadioButton.setEnabled(false);
                                        requestDiscount.setEnabled(false);
                                        request.startParsing();
                                    } catch (Exception e) {
                                        Log.e("request", "" + e.getMessage());

                                    }

                                } else {
                                    bonus.setError("required");
                                    bonus.requestFocus();
                                    unitQty.setEnabled(true);
                                    if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                                        if (voucherType == 506) {
                                            price.setEnabled(true);
                                        }else {  price.setEnabled(false);}
                                    } else {
                                        if (MHandler.getAllSettings().get(0).getCanChangePrice() == 1) {
                                            price.setEnabled(true);
                                        }
                                    }
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
//                    holder.cardView.setEnabled(true);
                    if (serialListitems.size() != 0) {
                        // delete serial if exist and alert screen if full list
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context.getActivity());
                        builder2.setTitle(context.getResources().getString(R.string.app_confirm_dialog));
                        builder2.setCancelable(false);
                        builder2.setMessage(context.getResources().getString(R.string.app_confirm_dialog_clear));
                        builder2.setIcon(android.R.drawable.ic_dialog_alert);
                        builder2.setPositiveButton(context.getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // int vouch = Integer.parseInt(voucherNumberTextView.getText().toString());
                                try {
                                    Log.e("cancel3", "serialListitemsInCardView" + serialListitems.size());
                                    for (int k = 0; k < listMasterSerialForBuckup.size(); k++) {
                                        MHandler.add_SerialBackup(listMasterSerialForBuckup.get(k), 1);
                                    }
                                    Log.e("listMasterSerialFor", "addToList=" + listMasterSerialForBuckup.size());
                                } catch (Exception e) {
                                }

                                serialListitems.clear();
                                Log.e("cancel3", "" + serialListitems.size());
//                                                                           MHandler.deletSerialItems_byVoucherNo(vouch);

                                float count = 0;
                                // delete from main list
//
                                total_items_quantity -= count;
                                totalQty_textView.setText(total_items_quantity + "");
                                dialog.dismiss();


                            }
                        });

                        builder2.setNegativeButton(context.getResources().getString(R.string.app_no), null);
                        builder2.create().show();

                    } else {

                        if (POS_ACTIVE == 1) {

                            dialog.dismiss();
                            barcode.setText("");
                            endAddItem = 1;
                            barcode.requestFocus();

                        }


                    }


                }
            });
            itemNumber.setText(allItemsList.get(position).getItemNo());
            itemName.setText(allItemsList.get(position).getItemName());
            final DatabaseHandler mHandler = new DatabaseHandler(cont);
            //*********************************** change Price with customer or not accourding to setting  ************************************
            if (MHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
                price.setText("" + allItemsList.get(position).getPrice());
                if (voucherType == 506) {
                    price.setEnabled(true);
                }else {  price.setEnabled(false);}
            } else {
            if (mHandler.getAllSettings().get(0).getCanChangePrice() == 0) {
                price.setText("" + allItemsList.get(position).getPrice());
                price.setEnabled(false);
                //    price.setText("Desable");

            } else {
                price.setText("" + allItemsList.get(position).getPrice());
            }
        }
            if (mHandler.getAllSettings().get(0).getItemUnit() == 1&&unitsItems_select==0) {
//                                                           if(items.get(position).getPrice()!=0)
//                                                           {
//                                                               price.setText("" + items.get(position).getPrice());
//                                                           }else {
                String itemUnitPrice = mHandler.getUnitPrice(allItemsList.get(position).getItemNo(), rate_customer,0);
                if (!itemUnitPrice.equals(""))
                    price.setText(itemUnitPrice);
//                                                           }

            }

//                                                       if(voucherType==506)
//                                                       {
//                                                           price.setEnabled(false);
//                                                           Log.e("getpreviusePriceSale",""+items.get(position).getItemNo());
//                                                           price.setText(mHandler.getpreviusePriceSale(items.get(position).getItemNo()));
//                                                       }

            if (mHandler.getAllSettings().get(0).getTaxClarcKind() == 1)
//                    discountLinearLayout.setVisibility(View.INVISIBLE);

                if (mHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1) {
                    discountLinearLayout.setVisibility(View.VISIBLE);
                }


            if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                unitWeightLinearLayout.setVisibility(View.GONE);// For Serial
                textQty.setText(context.getResources().getString(R.string.app_qty));
                // useWeight.setChecked(false);
            } else
                unitQty.setText("" + allItemsList.get(position).getItemL());


            List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

            ArrayAdapter<String> unitsList = new ArrayAdapter<String>(cont, R.layout.spinner_style, units);
            unit.setAdapter(unitsList);
            if ((allItemsList.get(position).getItemHasSerial().equals("1"))) {
                unit.setVisibility(View.GONE);
            }
            unit.setVisibility(View.GONE);
            //****************************************************************************************************

            bonusLinearLayout.setVisibility(View.INVISIBLE);
            addToList.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
//                    holder.cardView.setEnabled(true);
                    //listMasterSerialForBuckup.addAll(serialListitems);
                    for (int i = 0; i < listMasterSerialForBuckup.size(); i++) {
                        mHandler.add_SerialBackup(listMasterSerialForBuckup.get(i), 0);
                    }
                    Log.e("listMasterSerialFor", "addToList=" + listMasterSerialForBuckup.size());
                    String qtyText = "", discountText = "", bunosText = "", priceText = "";
                    int countInvalidSerial = 0;
                    if (serialListitems.size() != 0) {
                        countInvalidSerial = checkSerialDB();
                    }

                    qtyText = unitQty.getText().toString();
                    discountText = discount.getText().toString();
                    bunosText = bonus.getText().toString();
                    priceText = price.getText().toString();
                    oneUnit = (use_OneUnit.isChecked()) ? 1 : 0;
                    Log.e("oneUnit", "" + oneUnit);

                    if (haveCstomerDisc) {

                        if (!discountCustomer.equals(discountText)) {

                            haveChangeCustDisc = true;
                        } else {
                            haveResult = 1;
                        }


                    }

                    if (current_itemHasSerial == 0 || (current_itemHasSerial == 1 && countInvalidSerial == 0)) {

                        if (!TextUtils.isEmpty(qtyText) || mHandler.getAllSettings().get(0).getWork_serialNo() == 0) {
                            if (!price.getText().toString().equals("") && !(unitQty.getText().toString()).equals("")) {
                                if (Double.parseDouble(price.getText().toString()) != 0) {
                                    if ((approveAdmin == 0) || (approveAdmin == 1 && TextUtils.isEmpty(bunosText) && typeRequest == 2)
                                            || (approveAdmin == 1 && TextUtils.isEmpty(discountText) && typeRequest == 1)
                                            || (approveAdmin == 1 && !TextUtils.isEmpty(bunosText) && haveResult != 0)
                                            || (approveAdmin == 1 && !TextUtils.isEmpty(discountText) && haveResult != 0 && !haveChangeCustDisc)) {
                                        if (Double.parseDouble(unitQty.getText().toString()) != 0.0) {

                                            Boolean check = check_Discount(unitWeight, unitQty, price, bonus, discount, radioGroup);
                                            if (!check)
                                                Toast.makeText(context.getActivity(), "Invalid Disco" +
                                                        "unt Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                                            else {

                                                String unitValue;
                                                Log.e("useWeightValue", "" + useWeightValue.isChecked());
                                                if (useWeightValue.isChecked()) {
                                                    useWeight = 1;
                                                } else {
                                                    useWeight = 0;
                                                }
                                                currentKey = "";
                                                if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                                    unitValue = unit.getSelectedItem().toString();
                                                    // Log.e("unitValue", "" + unitValue);
//                                                                                               if (items.get(holder.getAdapterPosition()).getQty() >= Double.parseDouble(unitQty.getText().toString())
//                                                                                                       || mHandler.getAllSettings().get(0).getAllowMinus() == 1
//                                                                                                       || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508)
                                                    if (validQty(allItemsList.get(position).getQty(), Float.parseFloat(unitQty.getText().toString()))) {

                                                        if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                                Double.parseDouble(price.getText().toString()) >= allItemsList.get(position).getMinSalePrice())) {


                                                            AddItemsFragment2 obj = new AddItemsFragment2();
                                                            List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                            Offers appliedOffer = null;
                                                            Log.e("appliedOffer", "1111===" + offer.size());
//
                                                            if (offer.size() != 0 ) {

                                                                if (offer.get(0).getPromotionType() == 0) {// bonus promotion

                                                                    added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                            allItemsList.get(position).getTaxPercent() + "", unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                            bonus.getText().toString(),
                                                                            discount.getText().toString(),
                                                                            radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "", useWeight,
                                                                            context.getActivity(), item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);

                                                                    appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 0);

                                                                    if (!appliedOffer.getItemNo().equals("-1")) {
                                                                        double bonus_calc = 0;

                                                                        if (
                                                                                OfferCakeShop == 0) {
                                                                            bonus_calc = ((int) (Double.parseDouble(unitQty.getText().toString()) / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();

                                                                        } else {
                                                                            bonus_calc = appliedOffer.getBonusQty();
                                                                        }
                                                                        // Log.e("bonus_calc=", "added1" + added);
                                                                         Log.e("bonus_calc=", "" + bonus_calc);
                                                                        added = obj.addItem(offer.get(0).getBonusItemNo(), "(bonus)",
                                                                                "0", "1", "" + bonus_calc, "0",
                                                                                "0", "0", radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "", useWeight, context.getActivity()
                                                                                , item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);

                                                                        Log.e("bonus_calc=", "added2" + added);
                                                                    }

                                                                } else {
                                                                    //  Log.e("getPromotionType", "" + offer.get(0).getPromotionType());
                                                                    //(appliedOffer.getBonusQty()*Double.parseDouble(unitQty.getText().toString()))   //******calculate discount item before 11/9
                                                                    double disount_totalnew = 0, unitQty_double = 0;

                                                                    appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 1);
                                                                    Log.e("appliedOffer", "" + appliedOffer.getItemNo());
                                                                    if (!appliedOffer.getItemNo().equals("-1")) {
//                                                                                                               if (appliedOffer != null) {
                                                                        Log.e("appliedOffer", "appliedOffer.getItemQty()" + appliedOffer.getItemQty() + appliedOffer.getBonusQty());
                                                                        unitQty_double = Double.parseDouble(unitQty.getText().toString());
                                                                        if (offerTalaat == 1) {
                                                                            disount_totalnew = ((int) (unitQty_double / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();
                                                                        } else {
                                                                            disount_totalnew = (unitQty_double * appliedOffer.getBonusQty());
                                                                        }
                                                                        if (offerQasion == 1) {
                                                                            disount_totalnew = (unitQty_double * appliedOffer.getBonusQty());
                                                                        }
                                                                        // get discount from offers not from text


                                                                        String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());


                                                                        Log.e("appliedOffer11=", "" + disount_totalnew);

                                                                        if (appliedOffer.getDiscountItemType() == 0)
                                                                            radioGroup.check(R.id.discValueRadioButton);// just if promotion is discount
                                                                        else if (appliedOffer.getDiscountItemType() == 1)
                                                                            radioGroup.check(R.id.discPercRadioButton);// just if promotion is discount


                                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                allItemsList.get(position).getTaxPercent() + "", unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                                bonus.getText().toString(), "" + disount_totalnew, radioGroup
                                                                                , allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                                current_itemHasSerial, oneUnit);
                                                                    } else {// not exist offer for this qty
                                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                allItemsList.get(position).getTaxPercent() + "", unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                                bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                                allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                                current_itemHasSerial, oneUnit);


                                                                        Log.e("else", "appliedOffer==null");
                                                                    }
                                                                }
                                                            } else {
                                                                double totalQty = 0;
                                                                totalQty = Double.parseDouble(unitQty.getText().toString()) + Double.parseDouble(bonus.getText().toString());
                                                                // Log.e("totalQty+recyclerview", "" + totalQty + "\t bomus" + bonus.getText().toString());

                                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                        allItemsList.get(position).getTaxPercent() + "", unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                        bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                        allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                        useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                        current_itemHasSerial, oneUnit);
                                                            }
                                                            if (added) {
                                                                if (offer.size() != 0 && !appliedOffer.getItemNo().equals("-1"))
                                                                    if(ActiveStateOfOffers())      openOfferDialog(appliedOffer);

//                                                                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.layer5));
                                                                isClicked.set(position, 1);
                                                                localItemNumber.add(allItemsList.get(position).getItemNo());
                                                                itemInlocalList = false;
                                                                dialog.dismiss();
                                                                barcode.setText("");
                                                                endAddItem = 1;
                                                                barcode.requestFocus();
                                                                Log.e("Runnable","1**"+barcode.getText().toString());

//                                                                new Handler().post(new Runnable() {
//                                                                    @Override
//                                                                    public void run() {
////                                                                        Log.e("Runnable","--"+barcode.getText().toString());
//
//                                                                        barcode.requestFocus();
////                                                                        barcode.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//
//
//                                                                    }
//                                                                });//                                                                barcode.requestFocus();
//                                                                dialog.dismiss();
                                                            }
                                                        } else {
                                                            Toast.makeText(context.getActivity(), "Item hasn't been added, Min sale price for this item is " + allItemsList.get(position).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                            Log.e("bonus not added ", "" + allItemsList.get(position).getMinSalePrice());
                                                            price.setError(context.getActivity().getResources().getString(R.string.invalidValue));

                                                        }
                                                    } else {
                                                        unitQty.setError(context.getActivity().getResources().getString(R.string.insufficientQuantity));
                                                        Toast.makeText(context.getActivity(), context.getResources().getString(R.string.insufficientQuantity), Toast.LENGTH_LONG).show();

                                                    }

                                                } else {
                                                    if (unitWeight.getText().toString().equals(""))
                                                        Toast.makeText(context.getActivity(), "please enter unit weight", Toast.LENGTH_LONG).show();
                                                    else {
                                                        unitValue = unitWeight.getText().toString();
//                                        String qtyValue = "" + (Double.parseDouble(unitWeight.getText().toString()) * Double.parseDouble(unitQty.getText().toString()));
                                                        String qty = (useWeightValue.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString())) : unitQty.getText().toString());
//                                                                                                   String qty = (useWeightValue.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString()) * Double.parseDouble(unitValue)) : unitQty.getText().toString());

                                                        Log.e("here**", "" + position);
                                                        if (position > -1) {
                                                            if (allItemsList.get(position).getQty() >= Double.parseDouble(qty)
                                                                    || mHandler.getAllSettings().get(0).getAllowMinus() == 1
                                                                    || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508) {
                                                                if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                                        Double.parseDouble(price.getText().toString()) >= allItemsList.get(position).getMinSalePrice())) {

                                                                    AddItemsFragment2 obj = new AddItemsFragment2();
                                                                    List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                                    Offers appliedOffer = null;

                                                                    if (offer.size() != 0 ) {
                                                                        if (offer.get(0).getPromotionType() == 0) {

                                                                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                    allItemsList.get(position).getTaxPercent() + "", unitValue, qty, price.getText().toString(),
                                                                                    bonus.getText().toString(), discount.getText().toString(),
                                                                                    radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                    useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                                    current_itemHasSerial, oneUnit);

                                                                            appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 0);
                                                                            if (!appliedOffer.getItemNo().equals("-1")) {


                                                                                added = obj.addItem(appliedOffer.getBonusItemNo(), "(bonus)",
                                                                                        "0", "1", "" + appliedOffer.getBonusQty(), "0",
                                                                                        "0", "0", radioGroup
                                                                                        , allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + ""
                                                                                        , useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                                        current_itemHasSerial, oneUnit);
                                                                            }

                                                                        } else {
                                                                            appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 1);
                                                                            if (!appliedOffer.getItemNo().equals("-1")) {
                                                                                String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());
                                                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                        allItemsList.get(position).getTaxPercent() + "", unitValue, qty, price.getText().toString(),
                                                                                        bonus.getText().toString(), "" + (appliedOffer.getBonusQty() * Double.parseDouble(qty)), radioGroup,
                                                                                        allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + ""
                                                                                        , useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems,
                                                                                        current_itemHasSerial, oneUnit);
                                                                            }
                                                                        }
                                                                    } else {
                                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                allItemsList.get(position).getTaxPercent() + "", unitValue, qty, price.getText().toString(),
                                                                                bonus.getText().toString(), discount.getText().toString(),
                                                                                radioGroup, allItemsList.get(position).getCategory(), allItemsList.get(position).getPosPrice() + "",
                                                                                useWeight, context.getActivity(), item_remark.getText().toString(), serialListitems, current_itemHasSerial, oneUnit);
                                                                    }
                                                                    if (added) {
                                                                        if (offer.size() != 0  && !appliedOffer.getItemNo().equals("-1"))
                                                                            if(ActiveStateOfOffers())   openOfferDialog(appliedOffer);
//                                                                        holder.linearLayout.setBackgroundColor(R.color.done_button);
                                                                        isClicked.set(position, 1);
                                                                        barcode.setText("");
                                                                        endAddItem = 1;
                                                                        barcode.requestFocus();
                                                                        Log.e("Runnable","2***"+barcode.getText().toString());

//                                                                        new Handler().post(new Runnable() {
//                                                                            @Override
//                                                                            public void run() {
//                                                                                Log.e("Runnable",""+barcode.getText().toString());
////                                                                                barcode.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//
//                                                                                barcode.requestFocus();
//
//                                                                            }
//                                                                        });
                                                                        dialog.dismiss();
                                                                    }
                                                                } else
                                                                    Toast.makeText(context.getActivity(), "Item hasn't been added, Min sale price for this item is " + allItemsList.get(position).getMinSalePrice(), Toast.LENGTH_LONG).show();
                                                            } else
                                                                Toast.makeText(context.getActivity(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                                        } else
                                                            Toast.makeText(context.getActivity(), "Please enter the item again", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }

//                                                                                       dialog.dismiss();
                                        } else {
                                            Toast.makeText(context.getActivity(), "Invalid  Qty", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        if (haveResult == 0) {
                                            new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText(context.getString(R.string.warning_message))
                                                    .setContentText(context.getString(R.string.youHaveRequestToAdmin))
                                                    .show();
                                        }


                                    }

                                } else {
                                    price.setError(context.getString(R.string.invalidValue));
                                    price.requestFocus();
                                }
                            } else {
                                if (TextUtils.isEmpty(qtyText)) {
                                    unitQty.setError("*Required");
                                }

                                if (TextUtils.isEmpty(priceText)) {
                                    price.setError("*Required");
                                }
                            }

                        } else {
                            unitQty.setError("*Required");
                            price.requestFocus();
                        }
                    } else {// item  serial_No is duplcate +++++ dont save

                        if (countInvalidSerial == -1) {
                            new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(context.getString(R.string.warning_message))
                                    .setContentText(context.getString(R.string.reqired_filled))
                                    .show();
                        } else {
                            if (countInvalidSerial == 100) {
                                new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(context.getString(R.string.warning_message))
                                        .setContentText(context.getString(R.string.thereIsduplicatedSerial))
                                        .show();

                            } else if (countInvalidSerial == 1000) {
                                new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(context.getString(R.string.warning_message))
                                        .setContentText(context.getString(R.string.duplicate) + "\t" + context.getResources().getString(R.string.inThisVoucher))

                                        .show();
                            } else {
                                new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(context.getString(R.string.warning_message))
                                        .setContentText(context.getString(R.string.itemadedbefor))
                                        .show();
                            }

                        }


                    }
                    haveChangeCustDisc = false;

                }// end on click
            });
            dialog.show();

        } else {
            new SweetAlertDialog(context.getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(context.getString(R.string.warning_message))
                    .setContentText(context.getString(R.string.itemadedbefor))
                    .show();
        }
    }

    private void checkDuplicatInAllItems(int position) {
        if (dontDuplicateItems == 1) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItemNo().equals(allItemsList.get(position).getItemNo())) {
                    itemInlocalList = true;
                    break;
//                                                           }

                }
            }
//                                                       Log.e("items",""+items.size());
//                                                       Log.e("allItemsList",""+allItemsList.size());
        }
    }

    private void checkDuplicate(int position) {
        for (int i = 0; i < localItemNumber.size(); i++) {
            if (localItemNumber.get(i).equals(allItemsList.get(position).getItemNo())) {
//                                                           if(canChangePrice==0)
//                                                           {
                // showAlertDialog();
                itemInlocalList = true;
                break;
//                                                           }

            }
        }
    }

    private void clearDataCurrent() {
        typeRequest = 0;
        haveResult = 0;
        itemInlocalList = false;
        haveCstomerDisc = false;
        haveChangeCustDisc = false;
        discountCustomer = "";
        currentKey = "";
        serialListitems = new ArrayList<>();
        itemNoSelected = "";
        counterSerial = 0;
        counterBonus = 0;
    }

    private void getImage(String urlImage) {
        pdValidation = new SweetAlertDialog(cont, SweetAlertDialog.PROGRESS_TYPE);
        pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdValidation.setTitleText(context.getResources().getString(R.string.process));
        pdValidation.setCancelable(false);
        pdValidation.show();

        //  http://46.185.138.63//falcons/Products%20Photo%202021/Be%20Clean/4-Be%20Clean%20Foam%20Hand%20Sanitizer.jpg
        //  http://46.185.138.63//falcons/ProductsPhoto2021/BeClean/4-BeCleanFoamHandSanitizer.jpg
        Log.e("getImage", "yyyy");
        new SendHttpRequestTask(urlImage).execute();


    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        String imgUrl = "";

        SendHttpRequestTask(String url) {
            this.imgUrl = url;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String urlStr = "http://" + ipAddress.trim() +":"+ipWithPort.trim()+ "//FALCONS/" + imgUrl.trim();
                Log.e("urlTask", "" + urlStr);

//                URL url = new URL("http://46.185.138.63//falcons/Products%20Photo%202021/Be%20Clean/4-Be%20Clean%20Foam%20Hand%20Sanitizer.jpg");
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Log.e("myBitmap", "" + myBitmap.toString());
                return myBitmap;
            } catch (Exception e) {
                Log.d("TAG", e.getMessage());
                pdValidation.dismissWithAnimation();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
//            ImageView imageView = (ImageView) findViewById(ID OF YOUR IMAGE VIEW);
//            imageView.setImageBitmap(result);
            pdValidation.dismissWithAnimation();
            if (result != null)
                showImageOfCheck(result);
            else {
                new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(cont.getResources().getString(R.string.warning_message))
                        .setContentText(cont.getResources().getString(R.string.imageNotFound))
                        .show();
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void updateValue(String barcodeValue, ArrayList<serialModel> serialListitems) {
        // Log.e("updateValue","barcodeValue="+barcodeValue+"\tlist"+serialListitems.size()+"\tcounter"+numberBarcodsScanner);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(cont);
        layoutManager.setOrientation(VERTICAL);
        if ((validbarcodeValue(barcodeValue, serialListitems))) {
            addSerialToList(barcodeValue, serialListitems);
            serialValue.setError(null);
            addToList.setVisibility(View.VISIBLE);
            addToList.setEnabled(true);
            unitQty.setEnabled(false);
            flag = 1;
            counterSerial++;
            unitQty.setText(counterSerial + "");

            unitQty.setEnabled(false);
            numberBarcodsScanner++;
            serialValue.setText("");

            Log.e("contiusReading", "" + contiusReading);
            if (contiusReading == 1) {
                openSmallScanerTextView();
            } else {
                serialValue.requestFocus();
                Log.e("contiusReading", "HandlerElse");
//                   new Handler().post(new Runnable() {
//                       @Override
//                       public void run() {
//                           Log.e("contiusReading","Handler");
//                           serialValue.requestFocus();
//
//                       }
//                   });

            }
            serial_No_recyclerView.setLayoutManager(layoutManager);
//
            serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
        } else {
            serialValue.setError("invalid");
            if (serialListitems.size() != 0) {
                serial_No_recyclerView.setLayoutManager(layoutManager);

                serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
            }

            unitQty.setEnabled(false);


        }
//        }
//        if(validbarcodeValue(barcodeValue,serialListitems))
//        {
//            serialValue.setError(null);
//            //serialListitems.get(numberBarcodsScanner).setSerialCode(barcodeValue);
//            serial_No_recyclerView.setLayoutManager(layoutManager);
//
//            serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
//            unitQty.setEnabled(false);
//            numberBarcodsScanner++;
//            serialValue.setText("");
//            if(numberBarcodsScanner<serialListitems.size())
//            {openSmallScanerTextView();}
//        }else {
//            serialValue.setError("invalid");
//            serial_No_recyclerView.setLayoutManager(layoutManager);
//
//            serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, cont));
//            unitQty.setEnabled(false);
//
//        }


    }

    private void addSerialToList(String barcodeValue, ArrayList<serialModel> serialListitems) {
        String previusPrice = "0";
        float prcFloat = 0;
        String prc = "";
        if (voucherType == 506) {
            previusPrice = MHandler.getpreviusePriceSale(barcodeValue);
            prcFloat = Float.parseFloat(previusPrice);
            if (prcFloat != 0 && contiusReading == 0)
                price.setText(previusPrice);


            prc = generalMethod.convertToEnglish(generalMethod.getDecimalFormat(prcFloat));

        }

        //  Log.e("addSerialToList","previusPrice="+previusPrice);

        addToList.setEnabled(true);
        addToList.setVisibility(View.VISIBLE);
        serial = new serialModel();
        serial.setCounterSerial(numberBarcodsScanner);
        serial.setSerialCode(barcodeValue.trim());
        serial.setIsBonus("0");
        serial.setIsDeleted("0");
        serial.setVoucherNo(vouch + "");
        serial.setKindVoucher(kindVoucher + "");
        serial.setStoreNo(Login.salesMan);
        serial.setDateVoucher(voucherDate);
        serial.setItemNo(itemNoSelected);
        serial.setPriceItemSales(prc);
        serialListitems.add(serial);

        unitQty.setEnabled(false);

    }

    private boolean validbarcodeValue(String barcode, ArrayList<serialModel> serialListitems) {
        String data = barcode.toString().trim();
        Log.e("updateListCheque", "afterTextChanged" + "position\t" + numberBarcodsScanner + data + "\tdontValidate=" + barcode);
        try {

            if (data.toString().trim().length() != 0) {
                if (data.toString().trim().equals("error")) {
                    // Log.e("errorSerial2", "afterTextChanged" +"position\t"+data);
                    serialListitems.get(numberBarcodsScanner).setSerialCode("");
                    listMasterSerialForBuckup.get(numberBarcodsScanner).setIsDeleted("1");

                    // isFoundSerial=true;

                    return false;
                } else {
                    isFoundSerial = false;

                    for (int h = 0; h < serialListitems.size(); h++) {

                        if (serialListitems.get(h).getSerialCode().equals(data)) {
                            isFoundSerial = true;
                            break;
                        }
                    }
                    if (isFoundSerial == true) {// FOUND  IN CURRENT LIST
                        new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(cont.getString(R.string.warning_message))
                                .setContentText(cont.getString(R.string.duplicate) + "\t" + cont.getResources().getString(R.string.inThisVoucher))
                                .setConfirmButton(cont.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (contiusReading == 1) {
                                            openSmallScanerTextView();
                                        } else {
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    serialValue.setText("");
                                                    serialValue.requestFocus();

                                                }
                                            });
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        Toast.makeText(cont, cont.getResources().getString(R.string.duplicate) + "\t" + cont.getResources().getString(R.string.inThisVoucher), Toast.LENGTH_SHORT).show();

                        return false;
                    }
                }


                //Log.e("errorSerial2", "isFoundSerial" +"position\t"+isFoundSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
                if (MHandler.isSerialCodeExist(data).equals("not") || voucherType == 506 || MHandler.getLastTransactionOfSerial(data.trim()).equals("506")) {
                    if ((MHandler.isSerialCodePaied(data + "").equals("not") && voucherType == 504) ||
                            (!MHandler.isSerialCodePaied(data + "").equals("not") && voucherType == 506)) {
                        errorData = false;

                        // serialListitems.get(numberBarcodsScanner).setSerialCode(data);test
                        //  listMasterSerialForBuckup.get(numberBarcodsScanner).setSerialCode(data);test
//                            currentUpdate = position;
//
//                            isClicked.set(position, 0);
//                            isClicked.set(position + 1, 1);

                        // notifyDataSetChanged();

                        return true;

                    } else {// duplicated
                        if (voucherType == 506) {
                            new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(cont.getString(R.string.warning_message))
                                    .setContentText(cont.getString(R.string.serialIsNotPaied))
                                    .setConfirmButton(cont.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            if (contiusReading == 1) {
                                                openSmallScanerTextView();
                                            } else {
                                                new Handler().post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        serialValue.setText("");
                                                        serialValue.requestFocus();

                                                    }
                                                });
                                            }
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            String voucherNo = MHandler.isSerialCodePaied(data + "");
                            String voucherDate = voucherNo.substring(voucherNo.indexOf("&") + 1);
                            voucherNo = voucherNo.substring(0, voucherNo.indexOf("&"));

                            Log.e("Activities3", "onActivityResult+false+isSerialCodePaied" + voucherNo + "\t" + voucherDate);
                            new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText(context.getString(R.string.duplicate) + "\t" + data + "\t" + cont.getString(R.string.forVoucherNo) + "\t" + voucherNo + "\n" + context.getString(R.string.voucher_date) + "\t" + voucherDate)
                                    .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            if (contiusReading == 1) {
                                                openSmallScanerTextView();
                                            } else {
                                                new Handler().post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        serialValue.setText("");
                                                        serialValue.requestFocus();

                                                    }
                                                });
                                            }
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }


                        return false;
                    }

                } else {
                    errorData = true;
                    // Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();
                    String ItemNo = MHandler.isSerialCodeExist(data + "");
                    if (!ItemNo.equals("")) {
                        new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(cont.getString(R.string.warning_message))
                                .setContentText(cont.getString(R.string.invalidSerial) + "\t" + data + "\t" + cont.getString(R.string.forItemNo) + ItemNo)
                                .setConfirmButton(cont.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (contiusReading == 1) {
                                            openSmallScanerTextView();
                                        } else {
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    serialValue.setText("");
                                                    serialValue.requestFocus();

                                                }
                                            });
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(cont.getString(R.string.warning_message))
                                .setContentText(cont.getString(R.string.invalidSerial) + "\t" + data)
                                .setConfirmButton(cont.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (contiusReading == 1) {
                                            openSmallScanerTextView();
                                        } else {
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    serialValue.setText("");
                                                    serialValue.requestFocus();

                                                }
                                            });
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }


                    return false;


                }
            } else {
                return true;
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
            }


        } catch (Exception e) {
            return false;
        }

    }

    public void openSmallScanerTextView() {

        new IntentIntegrator((Activity) cont).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();


    }

    private void openeditDialog() {
        final EditText editText = new EditText(cont);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        SweetAlertDialog sweetMessage = new SweetAlertDialog(cont, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(cont.getResources().getString(R.string.enter_serial));
        sweetMessage.setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(cont.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (!editText.getText().toString().equals("")) {
//                    if(sunmiDevice!=1)
//                    {
//                        editTextSerialCode.setText(editText.getText().toString());
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//
//                    else {
                    serialValue.setText(editText.getText().toString().trim());
                    sweetAlertDialog.dismissWithAnimation();


                } else {
                    editText.setError(context.getResources().getString(R.string.reqired_filled));
                }
            }
        })

                .show();
    }

    private boolean validQty(float qtyCurrent, float qtyRequired) {
        //Log.e("validQty", "qtyCurrent=" + qtyCurrent + "\tqtyRequired=" + qtyRequired);
        if (MHandler.getAllSettings().get(0).getAllowMinus() == 1
                || SalesInvoice.voucherType == 506
                || (SalesInvoice.voucherType == 508&&  MainActivity.checkQtyForOrdersFlage==0)

                ||(Purchase_Order==1)) {
            return true;
        }
//        if(MHandler.getAllSettings().get(0).getQtyServer()==1)
//        {
//            qtyCurrent=importJason.getAvailableQty(itemNoSelected);
//            if (qtyCurrent >= qtyRequired)
//            {
//                return  true;
//            }
//
//            return  true;
//        }
//        else {
        if (qtyRequired <= qtyCurrent) {
            return true;
        }
//        }


        return false;

    }


    private void getDataForDiscountTotal(String itemName, String type, String price, String amount, String qty) {
        Log.e("getDataForDiscountTotal", "" + itemName + "type" + type + price + amount);
        double priceValue = 0, amountValue = 0, totalValue = 0;
        try {
            priceValue = Double.parseDouble(price);
            amountValue = Double.parseDouble(qty);
            totalValue = priceValue * amountValue;
        } catch (Exception e) {

        }
        discountRequest = new RequestAdmin();
        if (MHandler.getAllSettings().size() != 0) {
            discountRequest.setSalesman_name(MHandler.getAllSettings().get(0).getSalesMan_name());
        } else {
            discountRequest.setSalesman_name("");
        }
        discountRequest.setSalesman_no(Login.salesMan);
        discountRequest.setCustomer_no(CustomerListShow.Customer_Account);
        discountRequest.setCustomer_name(CustomerListShow.Customer_Name);
        discountRequest.setAmount_value(amount);
        discountRequest.setTotal_voucher(totalValue + "");// if request for item not for all voucher
        discountRequest.setVoucher_no(voucherNumberTextView.getText().toString() + "");

        discountRequest.setKey_validation("");
        if (type.equals("0")) {

            discountRequest.setNote(discountPerVal + itemName);
        } else {
            discountRequest.setNote(itemName);
        }

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
        formatTime = new SimpleDateFormat("hh:mm:ss");

        voucherDate = df.format(currentTimeAndDate);
        voucherDate = convertToEnglish(voucherDate);
        time = formatTime.format(currentTimeAndDate);
        time = convertToEnglish(time);
        Log.e("time", "" + time);
        df2 = new SimpleDateFormat("yyyy");
        voucherYear = df2.format(currentTimeAndDate);
        voucherYear = convertToEnglish(voucherYear);
    }

    private int checkSerialDB() {
        listSerialValue = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < serialListitems.size(); i++) {
            listSerialValue.add(serialListitems.get(i).getSerialCode());
            if (serialListitems.get(i).getSerialCode().equals("")) {// for empty serial
                counter = -1;

                return counter;
            }

            if (!MHandler.isSerialCodeExist(serialListitems.get(i).getSerialCode()).equals("not") && voucherType == 504 && !((MHandler.getLastTransactionOfSerial(serialListitems.get(i).getSerialCode().trim()).equals("506")) && voucherType == 504)) {// serial not valid
//                if(MHandler.isSerialCodePaied(serialListitems.get(i).getSerialCode()).equals("not"))
//                {
                counter++;
                String ItemNo = MHandler.isSerialCodeExist(serialListitems.get(i).getSerialCode() + "");

                if (!ItemNo.equals("")) {
                    new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(cont.getString(R.string.warning_message))
                            .setContentText(cont.getString(R.string.invalidSerial) + "\t" + serialListitems.get(i).getSerialCode() + "\t" + cont.getString(R.string.forItemNo) + ItemNo)
                            .show();
                } else {
                    new SweetAlertDialog(cont, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(cont.getString(R.string.warning_message))
                            .setContentText(cont.getString(R.string.invalidSerial) + "\t" + serialListitems.get(i).getSerialCode())
                            .show();

                }
//                }


            }
            if (listSerialTotal.size() != 0) {
                for (int j = 0; j < listSerialTotal.size(); j++) {
                    if (listSerialTotal.get(j).getSerialCode().equals(serialListitems.get(i).getSerialCode())) {
                        counter = 1000;
                    }

                }
            }

        }


        if (hasDuplicate(listSerialValue)) {
            counter = 100;
        }

        return counter;

    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) if (!set.add(each)) return true;
        return false;
    }

    public void showImageOfCheck(Bitmap bitmap) {
        final Dialog dialog = new Dialog(context.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.show_image);
        photoDetail = (PhotoView) dialog.findViewById(R.id.image_check);
        TextView close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.image_check);
        photoDetail.setImageBitmap(bitmap);
        dialog.show();
    }

    private void showAlertDialog() {
        Log.e("showAlertDialog", "truuuuu");
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle(R.string.app_alert);
        builder.setCancelable(true);
        builder.setMessage(R.string.itemadedbefor);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkTypePriceTable(String itemNumber) {
        if (MHandler.checkItemNoTableCustomerPricess(itemNumber))
            return true;
        return false;
    }

    private List<Offers> checkOffers(String itemNo) {

        Offers offer = null;
        List<Offers> offers;
        List<Offers> Offers = new ArrayList<>();
        Log.e("checkOffers","OffersJustForSalsFlag=="+MainActivity.OffersJustForSalsFlag);

                if( MainActivity.OffersJustForSalsFlag ==0||(MainActivity.OffersJustForSalsFlag == 1 &&SalesInvoice.voucherType == 504)) {
                    try {
                        Date currentTimeAndDate = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String date = df.format(currentTimeAndDate);
                        date = convertToEnglish(date);
                        date = date.trim();
                        offers = MHandler.getAllOffers();
                        if (MHandler.getAllSettings().get(0).getReadOfferFromAdmin() == 0) {
                            offers = MHandler.getAllOffers();
                        } else {
                            offers = MHandler.getAllOffersFromCustomerPrices();

                        }


                        for (int i = 0; i < offers.size(); i++) {
                            // Log.e("checkOffers2", date + "  " + offers.get(i).getFromDate() + " " + offers.get(i).getToDate());
                            if (itemNo.equals(offers.get(i).getItemNo()) &&
                                    (formatDate(date).after(formatDate(offers.get(i).getFromDate())) || formatDate(date).compareTo(formatDate(offers.get(i).getFromDate())) == 0) &&
                                    (formatDate(date).before(formatDate(offers.get(i).getToDate().trim())) ||

                                            formatDate(date).compareTo(formatDate(offers.get(i).getToDate().trim())) == 0
                                    )
                            ) {

                                offer = offers.get(i);
                                Offers.add(offer);
                                Log.e("Offers", "" + Offers.size() + "\t" + offers.get(i).getBonusQty());
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
        return Offers;
    }

    private Offers getAppliedOffer(String itemNo, String qty, int flag) {

        double qtyy = Double.parseDouble(qty);
        List<Offers> offer = checkOffers(itemNo);
        //  Log.e("getAppliedOffer", "offer" +offer.size());

        List<Double> itemQtys = new ArrayList<>();
        for (int i = 0; i < offer.size(); i++) {
            itemQtys.add(offer.get(i).getItemQty());
        }
        Collections.sort(itemQtys);

        double iq = itemQtys.get(0);// 12
        iq = 0;
        for (int i = 0; i < itemQtys.size(); i++) {
            //  Log.e("getAppliedOffer", "itemQtys="+"i=="+i +"==="+ itemQtys.get(i));
            if (qtyy >= itemQtys.get(i))
                iq = itemQtys.get(i);

        }

        for (int i = 0; i < offer.size(); i++) {
            if (iq == offer.get(i).getItemQty()) {
                return offer.get(i);
            }

        }
        Offers offerEmpty = new Offers();
        offerEmpty.setItemNo("-1");

        return offerEmpty;
    }

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void openOfferDialog(Offers offers) {
        Toast.makeText(cont, "openOfferDialog", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(cont);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.offer_dialog);
                LinearLayout mainLinear = dialog.findViewById(R.id.mainLinear);
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
                LinearLayout linearBunos = (LinearLayout) dialog.findViewById(R.id.linearBunos);
                Button ok = (Button) dialog.findViewById(R.id.ok);

                String offType = offers.getPromotionType() == 0 ? context.getResources().getString(R.string.app_bonus) : context.getResources().getString(R.string.app_disc_);

                if (offers.getPromotionType() == 0)// bunos
                {
                    discount_value.setVisibility(View.GONE);
                } else {
                    linearBunos.setVisibility(View.GONE);
                }
//        String offType =context.getResources().getString(R.string.app_bonus);


                offerType.setText(offerType.getText().toString() + "  :       " + offType);

                String itemBonusName = MHandler.getItemNameBonus(offers.getBonusItemNo());
                String bonusItm = offers.getBonusItemNo().equals("-1") ? "none" : offers.getBonusQty() + "";
                bonusItem.setText(bonusItem.getText().toString() + " :     " + bonusItm);
                bonusItemName.setText(bonusItemName.getText().toString() + " :  " + itemBonusName);
                String disc = offers.getPromotionType() == 0 ? "0" : "" + offers.getBonusQty();
                if (offers.getPromotionType() == 1) {
                    if (offers.getDiscountItemType() == 0) {
                        discount_value.setText(discount_value.getText().toString() + " : " + disc);
                    } else {
                        discount_value.setText(discount_value.getText().toString() + " : " + disc + "\t %");
                    }
                } else {
                    discount_value.setText(discount_value.getText().toString() + " : " + disc);
                }


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
        return allItemsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        CardView cardView;
        TableRow row_qty, table_solidQty;
        TextView itemNumber, itemName, tradeMark, category, unitQty, price, tax, barcode, posprice, textViewTax, textViewsolidQty;
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
            if(Purchase_Order==1)
            {
                if(CustomerListShow.paymentTerm==0)
                price.setVisibility(View.GONE);
            }
            tax = itemView.findViewById(R.id.textViewTax);
            barcode = itemView.findViewById(R.id.textViewBarcode);

            posprice = itemView.findViewById(R.id.textViewPosPrice);
            row_qty = itemView.findViewById(R.id.row_qty);
            table_solidQty = itemView.findViewById(R.id.table_solidQty);
            textViewsolidQty = itemView.findViewById(R.id.textViewsolidQty);
            imagespecial = itemView.findViewById(R.id.imagespecial);
            threeDForm = new DecimalFormat("00.000");
            textViewTax = itemView.findViewById(R.id.textTitleTax);
            showSolidQty = MHandler.getAllSettings().get(0).getShow_quantity_sold();
        }
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
 boolean ActiveStateOfOffers(){
     return ( (MainActivity.OffersJustForSalsFlag == 0)
             ||
             (MainActivity.OffersJustForSalsFlag == 1 &&SalesInvoice.voucherType == 504));
 }
  public static   int getCountOfItems(String itemcode,String unitid ){
        String CountOfItems=MHandler.getConvRate(itemcode, unitid);
        int resule=1;
        if(CountOfItems!=null && !CountOfItems.equals(""))
        {
            try {
                resule=Integer.parseInt(CountOfItems);
            }catch (Exception e){
                Log.e("getCountOfItems",""+e.getMessage());
            }

            return resule;
        }

        else
            return 1;
    }
}
