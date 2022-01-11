package com.dr7.salesmanmanager.Reports;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.AddItemsStockFragment;
import com.dr7.salesmanmanager.AddItemsStockFragment.*;
import com.dr7.salesmanmanager.CustomScannerActivity;
import com.dr7.salesmanmanager.CustomerListShow;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.Modles.InventoryShelf;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.Serial_Adapter;
import com.dr7.salesmanmanager.Stock_Activity;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.LinearLayout.VERTICAL;
import static com.dr7.salesmanmanager.SalesInvoice.itemNoSelected;
import static com.dr7.salesmanmanager.SalesInvoice.listMasterSerialForBuckup;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;
import static com.dr7.salesmanmanager.Serial_Adapter.errorData;
import static com.dr7.salesmanmanager.StockRequest.generalMethod;
import static com.dr7.salesmanmanager.StockRequest.itemsNoList;
import static com.dr7.salesmanmanager.StockRequest.itemsRequiredList;
import static com.dr7.salesmanmanager.StockRequest.listSerialInventory;
import static com.dr7.salesmanmanager.StockRequest.voucherNumber;
import static com.dr7.salesmanmanager.StockRequest.voucherDate;

import static com.dr7.salesmanmanager.Stock_Activity.intentData;

public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.viewHolder> {

    private List<Item> items = new ArrayList<>();

    private ArrayList<Integer> isClicked = new ArrayList<>();
    private Context context;
    boolean added = false;
    public static int flag = 0, counterSerial = 0, counterBonus = 0,selectedId=-1;
    public static TextView serialValueStock;
    public static ArrayList<serialModel> serialListitems_stock = new ArrayList<>();

    public  static Button addToList;
    RecyclerView serial_No_recyclerView;
    private static DatabaseHandler mDbHandler;
     public  static  EditText unitQtyStock;
     public static String itemNoStock="",barcodeValue_inventory="";
     boolean isFoundSerial=false;

    public StockRecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
//        this.itemsRequiredList=items;
        this.context = context;
        for (int i = 0; i < items.size(); i++) {
            isClicked.add(0);

        }
        mDbHandler = new DatabaseHandler(context);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//item_horizontal_listview_stock

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_rwo_, parent, false);

        return new viewHolder(view);
    }
    public String[] mColors = {"#F3F8F8F7","#CFD8DC"};
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        holder.setIsRecyclable(false);
//        if (isClicked.get(position) == 0)
//            holder.cardView.setCardBackgroundColor(R.color.layer7);
//        else
//            holder.cardView.setCardBackgroundColor(R.color.layer5);

        holder.editQty.setTag(position);
        holder.editQty.setText(itemsRequiredList.get(holder.getAdapterPosition()).getQty()+"");

        holder.itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
        holder.itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.tradeMark.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.category.setText("" + items.get(holder.getAdapterPosition()).getCategory());
        holder.unitQty.setText("" + itemsRequiredList.get(holder.getAdapterPosition()).getCurrentQty());
        holder.tax.setText("" + items.get(holder.getAdapterPosition()).getTaxPercent());
        holder.barcode.setText(items.get(holder.getAdapterPosition()).getBarcode());
        holder.price.setVisibility(View.GONE);
        holder.pricee.setVisibility(View.GONE);
        holder.cardView.setBackgroundColor(Color.parseColor(mColors[position % 2]));
        holder.cardView.setPadding(5 , 10, 5, 10);
        if(intentData.equals("read")){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    String itemNo=items.get(position).getItemNo().trim();
                    Log.e("notExistInTotalList","itemNo"+itemNo);
                    if(notExistInTotalList(itemNo)) {


                        final Dialog dialog = new Dialog(view.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.add_item_serial_dialog);
                        Window window = dialog.getWindow();
                        serialListitems_stock = new ArrayList<>();
                        final TextView textQty = dialog.findViewById(R.id.textQty);
                        textQty.setText(view.getContext().getResources().getString(R.string.app_qty));
                        selectedId = position;
                        counterSerial = 0;
                        Log.e("selectedId", "" + selectedId);

                        final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
                        unitQtyStock = (EditText) dialog.findViewById(R.id.unitQty);
                        final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
                        final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
                        final ImageView addEditBarcode = (ImageView) dialog.findViewById(R.id.addEditBarcode);

                        itemName.setText(items.get(position).getItemName());
                        itemNumber.setText(items.get(position).getItemNo());
                        final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
                        final EditText discount = (EditText) dialog.findViewById(R.id.discount);
                        LinearLayout linearPrice, discount_linear, discribtionItem_linear, _linear_switch;
                        _linear_switch = dialog.findViewById(R.id._linear_switch);
                        linearPrice = dialog.findViewById(R.id.linearPrice);
                        linearPrice.setVisibility(View.GONE);
                        _linear_switch.setVisibility(View.GONE);
                        addToList = dialog.findViewById(R.id.addToList);
                        Button cancelAdd= dialog.findViewById(R.id.cancelAdd);
                        cancelAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        itemNoStock = items.get(holder.getAdapterPosition()).getItemNo().toString().trim();
                        serial_No_recyclerView = dialog.findViewById(R.id.serial_No_recyclerView);
                        final ImageView serialScanBunos = dialog.findViewById(R.id.serialScanBunos);
                        serialScanBunos.setVisibility(View.GONE);
                        discount_linear = dialog.findViewById(R.id.discount_linear);
                        discount_linear.setVisibility(View.GONE);
                        discribtionItem_linear = dialog.findViewById(R.id.discribtionItem_linear);
                        discribtionItem_linear.setVisibility(View.GONE);
                        final ImageView serialScan = dialog.findViewById(R.id.serialScan);
                        unitQtyStock.setEnabled(false);
                        serialScan.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("WrongConstant")
                            @Override
                            public void onClick(View v) {

                                openSmallScanerTextView();


                            }
                        });
                        addEditBarcode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openEditSerial();
                            }
                        });
                        serialValueStock = dialog.findViewById(R.id.serialValue);
                        try {
                            serialValueStock.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!s.toString().equals("")) {
                                        barcodeValue_inventory = s.toString().trim();
//                                                                               serialValue_Model.setText(s.toString().trim());
                                        updateValue(barcodeValue_inventory, serialListitems_stock);

                                    }


                                }
                            });
                        } catch (Exception e) {
                        }

                        addToList.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(View v) {

                                AddItemsStockFragment obj = new AddItemsStockFragment();
                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                        "1", "1", unitQtyStock.getText().toString(),
                                        "1" + items.get(holder.getAdapterPosition()).getPrice(),
                                        "0", "0", view.getContext(), -10);
//
//                                if (added) {
//                                    holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.layer5));
//                                    isClicked.set(holder.getAdapterPosition() , 1);
//                                }
                                dialog.dismiss();
                            }


                        });
                        dialog.show();
                    } else {
                        Toast.makeText(context, "" + context.getResources().getString(R.string.itemadedbefor), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

    private boolean notExistInTotalList(String itemNo) {
        Log.e("notExistInTotalList","itemNosize="+itemsNoList.size());
        for(int i=0;i<itemsNoList.size();i++)
        {

            if(itemsNoList.get(i).trim().equals(itemNo.trim()))
            {
                Log.e("notExistInTotalList","itemNo"+i+"\t"+itemsNoList.get(i));
                return false;
            }

        }
        return true;
    }

    private void openEditSerial() {
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(context.getResources().getString(R.string.enter_serial));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(!editText.getText().toString().equals(""))
                {
                    if(checkInTotalList(editText.getText().toString().trim()))
                    {
                        serialValueStock.setText(editText.getText().toString().trim());
                        sweetAlertDialog.dismissWithAnimation();


                    }
                    else {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(context.getString(R.string.warning_message))
                                .setContentText(context.getString(R.string.duplicate)+"\t"+context.getResources().getString(R.string.inThisVoucher))

                                .show();

                    }


                }
                else {
                    editText.setError(context.getResources().getString(R.string.reqired_filled));
                }
            }
        })

                .show();



    }

    private boolean checkInTotalList(String s) {
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


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout,cardView;
//        CardView ;
        EditText editQty;
        TextView itemNumber, itemName, tradeMark, category, unitQty, pricee, price, tax, barcode;

        public viewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linear);
            itemNumber = itemView.findViewById(R.id.textViewItemNumber);
            itemName = itemView.findViewById(R.id.textViewItemName);
            tradeMark = itemView.findViewById(R.id.textViewTradeMark);
            category = itemView.findViewById(R.id.textViewCategory);
            unitQty = itemView.findViewById(R.id.textViewUnit_qty);
            pricee = itemView.findViewById(R.id.textViewPricee);
            price = itemView.findViewById(R.id.textViewPrice);
            tax = itemView.findViewById(R.id.textViewTax);
            barcode = itemView.findViewById(R.id.textViewBarcode);

            editQty = itemView.findViewById(R.id.editQty);
            editQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int tagPosition=Integer.parseInt(editQty.getTag().toString());
                    Log.e("tagPosition",""+tagPosition);
                    if(!s.toString().equals("")&&!s.toString().equals("0"))
                    {
                       updateList(tagPosition,s.toString());

                    }

                }
            });

            if(intentData.equals("read"))
            {
                editQty.setVisibility(View.GONE);
                unitQty.setVisibility(View.GONE);
            }


        }


    }
    private void updateList(int tagPosition, String qty) {
        float qt=0;
        try {
            qt=Float.parseFloat(qty);
        }
        catch (Exception e){qt=0;}
        float oldQty=0,updateQty=0;
        oldQty=itemsRequiredList.get(tagPosition).getQty();
        updateQty=qt;
        if(oldQty!=updateQty)
        {
            itemsRequiredList.get(tagPosition).setQty(qt);
        }

//            AddItemsStockFragment obj = new AddItemsStockFragment();
//            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
//                    "0","1", qty,
//                    "1"+items.get(tagPosition).getPrice(),
//                    "0", "0", context,items.get(tagPosition).getCurrentQty());
    }
    public void openSmallScanerTextView() {

        new IntentIntegrator((Activity) context).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();



    }
    @SuppressLint("WrongConstant")
    private void updateValue(String barcodeValue, ArrayList<serialModel> serialListitems) {
        // Log.e("updateValue","barcodeValue="+barcodeValue+"\tlist"+serialListitems.size()+"\tcounter"+numberBarcodsScanner);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(VERTICAL);
            addSerialToList(barcodeValue, serialListitems);

            openSmallScanerTextView();
            serialValueStock.setError(null);


    }

    @SuppressLint("WrongConstant")
    private void addSerialToList(String barcodeValue, ArrayList<serialModel> serialListitems) {
        addToList.setVisibility(View.VISIBLE);
        addToList.setEnabled(true);
        unitQtyStock.setEnabled(false);
        // flag = 1;
        counterSerial++;
        serialModel serial = new serialModel();
        serial.setCounterSerial(counterSerial);
        serial.setSerialCode(barcodeValue.trim());
        serial.setIsBonus("0");
        serial.setIsDeleted("0");
        Log.e("voucherNo",""+voucherNumber);
        serial.setVoucherNo(voucherNumber+"");
        serial.setKindVoucher(voucherType+"");
        serial.setStoreNo(generalMethod.getSalesManLogin());
        serial.setDateVoucher(voucherDate);
        serial.setItemNo(itemNoStock);
        serial.setPriceItem(items.get(selectedId).getPrice());
        serialListitems.add(serial);
        InventoryShelf inventoryShelf=new InventoryShelf();
        inventoryShelf.setITEM_NO(itemNoStock);
        inventoryShelf.setQTY_ITEM(listSerialInventory.size());
        inventoryShelf.setSALESMAN_NUMBER(generalMethod.getSalesManLogin());
        inventoryShelf.setCUSTOMER_NO(CustomerListShow.Customer_Account);
        inventoryShelf.setSERIAL_NO(barcodeValue);
        inventoryShelf.setVoucherNo(voucherNumber);
        inventoryShelf.setTRANS_DATE(generalMethod.getCurentTimeDate(1));
        listSerialInventory.add(inventoryShelf);
        unitQtyStock.setText(counterSerial + "");


        //addQtyTotal(1);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(VERTICAL);





        serial_No_recyclerView.setLayoutManager(layoutManager);

        serial_No_recyclerView.setAdapter(new Serial_Adapter(serialListitems, context));



    }

    private boolean validbarcodeValue(String barcode,ArrayList<serialModel> serialListitems) {
        String data = barcode.toString().trim();
        // Log.e("updateListCheque", "afterTextChanged" +"position\t"+numberBarcodsScanner+data+"\tdontValidate="+barcode);
        try {

            if (data.toString().trim().length() != 0) {
                if (data.toString().trim().equals("error")) {

                } else {
                    isFoundSerial = false;

                    for (int h = 0; h < serialListitems.size(); h++) {

                        if (serialListitems.get(h).getSerialCode().equals(data)) {
                            isFoundSerial = true;
                            break;
                        }
                    }
                    if (isFoundSerial == true) {// FOUND  IN CURRENT LIST
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(context.getString(R.string.warning_message))
                                .setContentText(context.getString(R.string.duplicate) + "\t" + context.getResources().getString(R.string.inThisVoucher))
                                .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        return false;
                    }
                }


                //Log.e("errorSerial2", "isFoundSerial" +"position\t"+isFoundSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
                String ItemNo = mDbHandler.isSerialCodeExist(data.trim() + "");
                if(ItemNo.trim().equals("not"))
                {


                } else {
                    errorData = true;
                    // Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();
                  //  String ItemNo = mDbHandler.isSerialCodeExist(data + "");
                    if (!ItemNo.equals("")) {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(context.getString(R.string.warning_message))
                                .setContentText(context.getString(R.string.invalidSerial) + "\t" + data + "\t" + context.getString(R.string.forItemNo) + ItemNo)
                                .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(context.getString(R.string.warning_message))
                                .setContentText(context.getString(R.string.invalidSerial) + "\t" + data)
                                .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }


                    return false;


                }
            } else {
                return false;
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
            }


        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
/*//                    final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
//                    final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
//                    final TextView price = (TextView) dialog.findViewById(R.id.price);
//                    final TextView pricee = (TextView) dialog.findViewById(R.id.pricee);
//                    final TextView bonuss = (TextView) dialog.findViewById(R.id.bonuss);
//
//                    //******************************************************invisible weight text view in stock request*************************
//                    final TextView texViewtQty = (TextView) dialog.findViewById(R.id.textQty);
//                    final TextView weight = (TextView) dialog.findViewById(R.id.textview_wieght);
//                    final EditText weight_editText = (EditText) dialog.findViewById(R.id.unitWeight);
//                    final CheckBox use_weight = (CheckBox) dialog.findViewById(R.id.use_weight);
//                    //****************************************************************************************************************************
//                    final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
//                    final EditText unitQty = (EditText) dialog.findViewById(R.id.unitQty);
//                    final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
//                    final EditText discount = (EditText) dialog.findViewById(R.id.discount);
//                    LinearLayout linearPrice,discount_linear,discribtionItem_linear,serialNo_linear;
//                    linearPrice= dialog.findViewById(R.id.linearPrice);
//                    linearPrice.setVisibility(View.GONE);
//                    discount_linear= dialog.findViewById(R.id.discount_linear);
//                    discount_linear.setVisibility(View.GONE);
//                    discribtionItem_linear= dialog.findViewById(R.id.discribtionItem_linear);
//                    discribtionItem_linear.setVisibility(View.GONE);
//                    serialNo_linear= dialog.findViewById(R.id.serialNo_linear);
//                    serialNo_linear.setVisibility(View.GONE);
//
//                    final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.discTypeRadioGroup);
//                    Button addToList = (Button) dialog.findViewById(R.id.addToList);
//
//                    itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
//                    itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
//
//                    price.setVisibility(View.INVISIBLE);
//                    pricee.setVisibility(View.INVISIBLE);
//                    bonus.setVisibility(View.INVISIBLE);
//                    discount.setVisibility(View.INVISIBLE);
//                    radioGroup.setVisibility(View.INVISIBLE);
//                    bonuss.setVisibility(View.INVISIBLE);
//                    //******************************************
//                    weight.setVisibility(View.INVISIBLE);
//                    weight_editText.setVisibility(View.INVISIBLE);
//                    texViewtQty.setText("Unit Qty");
//                    use_weight.setVisibility(View.INVISIBLE);
//                    //******************************************************
//
//                    DatabaseHandler mHandler = new DatabaseHandler(context);
//                    List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());
//
//                    ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
//                    unit.setAdapter(unitsList);

//                    addToList.setOnClickListener(new View.OnClickListener() {
//                        @SuppressLint("ResourceAsColor")
//                        @Override
//                        public void onClick(View v) {
//
//////                            Boolean check = check_Discount(unit, unitQty, price, bonus, discount, radioGroup);
////                            if (!check)
////                                Toast.makeText(view.getContext(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
////                            else {
////
//////                            AddItemsStockFragment obj = new AddItemsStockFragment();
//////                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
//////                                    holder.tax.getText().toString(), unit.getSelectedItem().toString(), unitQty.getText().toString(),
//////                                    "1"+items.get(holder.getAdapterPosition()).getPrice(),
//////                                    bonus.getText().toString(), discount.getText().toString(), radioGroup, view.getContext());
////
////                                if (added) {
////                                    holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.layer5));
////                                    isClicked.set(holder.getAdapterPosition() , 1);
////                                }
////                            }
////                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();*/
