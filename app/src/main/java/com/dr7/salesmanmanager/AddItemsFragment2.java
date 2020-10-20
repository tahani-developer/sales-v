package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import com.dr7.salesmanmanager.Modles.serialModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter;
import com.google.zxing.common.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.MainActivity.PICK_IMAGE;
//import static com.dr7.salesmanmanager.SalesInvoice.jsonItemsList;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.item_serial;
import static com.dr7.salesmanmanager.SalesInvoice.listItemImage;
import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;


public class AddItemsFragment2 extends DialogFragment {
    public static  List<Item> jsonItemsList;
    public static List<Item> jsonItemsList2;
    public static List<Item> jsonItemsList_intermidiate;
    public static List<Item> List;
    public  static  int size_customerpriceslist=0;
    public  List<Item> itemsList_forFilter;
    Context context;


    public  String voucherDate="";
    public static final int REQUEST_Camera_Barcode = 1;
    private Item item;
    Button addToListButton, doneButton;
    SearchView search;
   public static EditText barcode;
    ImageView barcodebtn;
    private ArrayList<String> itemsList;
//    public static  List<Item> jsonItemsList;
//    public static List<Item> jsonItemsList2;
//    public static List<Item> jsonItemsList_intermidiate;
    RecyclerView recyclerView;
    ListView verticalList;
    public  static   int total_items_quantity=0;
    private float descPerc;
    boolean added = false;
    double  flagBonus=0,amountBonus=0;
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";
    final String result="";
    int indexfirstpeces=0;
    String  firstString="";
    String secondString="";
    String lower="";
    String upper="";
    int size_firstlist=0;
     public   static String s="";
    SimpleDateFormat df, df2;
    Date currentTimeAndDate;
    private static DatabaseHandler mDbHandler;

    public AddItemsInterface getListener() {
        return listener;
    }

    public interface AddItemsInterface {
        public void addItemsToList(List<Item> itemsList);
    }

    private AddItemsInterface listener;

    public AddItemsFragment2() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDbHandler = new DatabaseHandler(getActivity());

//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2= new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        List = new ArrayList<Item>();
        List.clear();

//        voucherDate = convertToEnglish(voucherDate);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        int size_firstlist=0;

        final View view = inflater.inflate(R.layout.add_items_dialog2, container, false);

        LinearLayout add_item = view.findViewById(R.id.add_item);
        try {
            if (languagelocalApp.equals("ar")) {
                add_item.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    add_item.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch (Exception e)
        {
            add_item.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        DatabaseHandler mHandler = new DatabaseHandler(getActivity());

        fillListItemJson();

//        String rate_customer=mHandler.getRateOfCustomer();  // customer rate to display price of this customer
//
//        if (mHandler.getAllSettings().get(0).getPriceByCust() == 0)
//            jsonItemsList = mHandler.getAllJsonItems(rate_customer);
//        else {
//
//            jsonItemsList2 = mHandler.getAllJsonItems2(rate_customer);
//            size_firstlist=jsonItemsList2.size();
//
//            jsonItemsList=mHandler.getAllJsonItemsNotInCustomerPrices(rate_customer);
//            int count=jsonItemsList2.size()+jsonItemsList.size();
//
//            for(int i=0;i<count;i++)
//            {
//                if(i<size_firstlist)
//                {
//                    jsonItemsList_intermidiate.add(jsonItemsList2.get(i));
//                }
//                else
//                jsonItemsList_intermidiate.add(jsonItemsList.get(i-size_firstlist));
//            }
//            jsonItemsList=jsonItemsList_intermidiate;
//            Log.e("size after ",""+jsonItemsList.size());
//
//        }

        //    test
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);

        final Spinner categorySpinner = view.findViewById(R.id.cat);
        List<String> categories = mHandler.getAllExistingCategories();
        categories.add(0, getResources().getString(R.string.all_item));

        final ArrayAdapter<String> ad = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, categories);
        categorySpinner.setAdapter(ad);

//        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
//        recyclerView.setAdapter(adapter);

        // ****************************** Kind Item Spenner*****************************************************

        final Spinner Kind_item_Spinner = view.findViewById(R.id.spinner_kind_item);
        List<String> Kind_item=new ArrayList<>();
        try {
            Kind_item = mHandler.getAllKindItems();

        }
        catch (Exception e)
        {
            Kind_item.add(0 ,getResources().getString(R.string.all_item));


        }
        Kind_item.add(0 ,getResources().getString(R.string.all_item));

        final  ArrayAdapter<String> adapter_kind = new ArrayAdapter<>(getActivity() , R.layout.spinner_style, Kind_item);
        Kind_item_Spinner.setAdapter(adapter_kind);

        //kind item
        Kind_item_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int j = 0; j < jsonItemsList.size(); j++) {
                        Log.e("llog",jsonItemsList.get(j).getKind_item() + "     *    "  +Kind_item_Spinner.getSelectedItem().toString() );
                        if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(j));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
                recyclerView.setAdapter(adapter);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int k = 0; k < jsonItemsList.size(); k++) {
                        if (jsonItemsList.get(k).getCategory().equals(categorySpinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(k));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList,AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
                recyclerView.setAdapter(adapter);

            }
        });

        search = view.findViewById(R.id.mSearch);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
//                adapter.getFilter().filter(query);

                if (query != null && query.length() > 0) {
                    String[] arrOfStr = query.split(" ");
                    int [] countResult=new int[arrOfStr.length];


                    ArrayList<Item> filteredList = new ArrayList<>();

//                    "jkgb".matches()

                    boolean isFound=false;
                    for(int i=0;i<jsonItemsList.size();i++){
                        for(int j=0;j<arrOfStr.length;j++){
                        String lowers=arrOfStr[j].toLowerCase();
                        String uppers=arrOfStr[j].toUpperCase();

                            if(jsonItemsList.get(i).getItemName().toLowerCase().contains(lowers)||jsonItemsList.get(i).getItemName().toUpperCase().contains(uppers)){

                                isFound=true;

                            }else {
                                isFound=false;
                                break;
                            }


                        }
                        if(isFound){
                            filteredList.add(jsonItemsList.get(i));
                        }


                    }

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);



                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList,AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        //***************************************************************************************
        barcode=(EditText)view.findViewById(R.id.barcode);
//        barcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                barcode.setText("");
////                 Intent i=new Intent(getActivity(),ScanActivity.class);
////                 startActivity(i);
//            }
//        });
        barcodebtn=(ImageView)view.findViewById(R.id.searchBarcode);
        barcodebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                 s=barcode.getText().toString();
                if(!s.equals("")) {
                    searchByBarcodeNo(s + "");
                }
                else{
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CAMERA}, REQUEST_Camera_Barcode);
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        {//just for first time
                            Log.e("requestresult" ,"PERMISSION_GRANTED");
                            Intent i=new Intent(getActivity(),ScanActivity.class);
                            i.putExtra("key","1");
                            startActivity(i);
                            searchByBarcodeNo(s + "");
                        }
                    } else {
                        Intent i=new Intent(getActivity(),ScanActivity.class);
                        i.putExtra("key","1");
                        startActivity(i);
                        searchByBarcodeNo(s + "");
                    }


                }

            }
        });
        barcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

//                if(event.getRawX() <= (barcode.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))
//                {
//                    // your action here
//                    return true;
//                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (barcode.getRight() - barcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {   barcode.setText("");
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
                        recyclerView.setAdapter(adapter);
                        return true;

                    }

                }
                return false;
            }
        });

        //***************************************************************************************


        Button done = (Button) view.findViewById(R.id.done);

        Button cancel = (Button) view.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder2.setCancelable(false);
                builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder2.setIcon(android.R.drawable.ic_dialog_alert);
                builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float count=0;

//                        total_items_quantity -= List.size();
//                        totalQty_textView.setText("+"+0);
//                        total_items_quantity=0;
                        for(int j=0;j<List.size();j++)
                        {
                            count+=List.get(j).getQty();
                        }
//                        Log.e("count",""+count);
//                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
                        total_items_quantity-=count;
                        totalQty_textView.setText(total_items_quantity+"");
                        List.clear();
                       int vouch=Integer.parseInt(voucherNumberTextView.getText().toString());
                        mDbHandler.deletSerialItems_byVoucherNo(vouch);


//                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
                        AddItemsFragment2.this.dismiss();


                    }
                });

                builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                builder2.create().show();
            }
        });

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listener.addItemsToList(List);
                    AddItemsFragment2.this.dismiss();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Re Select Items Please", Toast.LENGTH_SHORT).show();
                    Log.e("AddItemException",""+e.getMessage());
                }

          }
        });
        return view;
    }
    /*//                new com.google.zxing.integration.android.IntentIntegrator(getActivity()).initiateScan();
//
//                barcode.setText("");
//                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//
//                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
//                intentIntegrator.setBeepEnabled(true);
//                intentIntegrator.setCameraId(0);
//                intentIntegrator.setOrientationLocked(true);
//                intentIntegrator.setPrompt("SCAN");
////                intentIntegrator.setBarcodeImageEnabled(false);
//                intentIntegrator.initiateScan();
////                IntentIntegrator.forSupportFragment(AddItemsFragment2.this);*/
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//        if (Result != null) {
//            String barcodeValue;
//
//            Log.e("bar","found"+data);
//            if (Result.getContents() == null) {
//                Log.d("MainActivity", "cancelled scan");
//                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
//                barcodeValue = "cancelled";
//            } else {
//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(getActivity(), "Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
//
//                barcodeValue = Result.getContents();
////                String[] arrayString = barcodeValue.split(" ");
//
////Log.e("barcode_value ",""+barcodeValue+"\n"+"th ="+arrayString[0]+"\n"+"w ="+arrayString[1]+"\n"+"l ="
////        +arrayString[2]+"\n"+"grad ="+arrayString[3]);
////                searchByBundleNo(barcodeValue);
//
//                searchByBarcodeNo(barcodeValue);
//                barcode.setText(barcodeValue+"");
//                Log.e("bar","found"+barcodeValue);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
   public  void searchByBarcodeNo(String barcodeValue) {
        if(!barcodeValue.equals(""))
        {
                ArrayList<Item> filteredList = new ArrayList<>();
                for (int k = 0; k < jsonItemsList.size(); k++) {
                    if (jsonItemsList.get(k).getItemNo().equals(barcodeValue)){
                        filteredList.add(jsonItemsList.get(k));
                    }
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList,AddItemsFragment2.this);
                recyclerView.setAdapter(adapter);
                Log.e("filteredList=","" + filteredList.size());
                if(filteredList.size()==0)
                {
                    Toast.makeText(getActivity(), barcodeValue+"\tNot Found", Toast.LENGTH_LONG).show();
                }



        } else {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList,AddItemsFragment2.this);
            recyclerView.setAdapter(adapter);


        }
    }
    /*//        int no = 0;
//
//        if (!barcodeValue.equals("cancelled")) {
//            for (int k = 0; k < bundles.size(); k++) {
//                if ((bundles.get(k).getBundleNo()).equals(Bundul)) {
//                    no = k;
//                    items.setSelection(no);
//                    items.requestFocusFromTouch();
//                    items.setSelection(no);
//
//                    break;
//                }
//            }
//
//        } else {
//            ItemsListAdapter adapter = new ItemsListAdapter(LoadingOrder.this, bundles);
//            items.setAdapter(adapter);
//        }*/

    public void setListener(AddItemsInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
//    @Override
//    public void onBackPressed() {
//
//    }

    private void fillListItemJson() {// test
        String s = "";
        jsonItemsList = new ArrayList<>();
        jsonItemsList2 = new ArrayList<>();
        jsonItemsList_intermidiate = new ArrayList<>();
        String rate_customer = mDbHandler.getRateOfCustomer();
        if(rate_customer.equals(""))
        {
            rate_customer="0";

        }
        Log.e("fillListItemJson",""+rate_customer);// customer rate to display price of this customer

        if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 0) {
            jsonItemsList = mDbHandler.getAllJsonItems(rate_customer);
           // Log.e("jsonItemsList", "zero"+jsonItemsList.get(0).getItemName()+"\t"+jsonItemsList.get(0).getItemHasSerial());
        }

        else {
            List<String> itemNoList = mDbHandler.getItemNumbersNotInPriceListD();// difference itemNo between tow table (CustomerPricess and priceListD)

            jsonItemsList2 = mDbHandler.getAllJsonItems2(rate_customer);//from customers pricess


            size_firstlist = jsonItemsList2.size();
            if (size_firstlist != 0) {
                size_customerpriceslist = size_firstlist;

                for (int k = 0; k < size_firstlist; k++) {
                    jsonItemsList_intermidiate.add(jsonItemsList2.get(k));
                }
                //****************************************************************************************

                jsonItemsList = mDbHandler.getAllJsonItems(rate_customer); // from price list d


                for (int i = 0; i < jsonItemsList.size(); i++) {
                    for (int j = 0; j < itemNoList.size(); j++)
                        if (jsonItemsList.get(i).getItemNo().equals(itemNoList.get(j).toString())) {
                            jsonItemsList_intermidiate.add(size_firstlist, jsonItemsList.get(i));
                            size_firstlist++;



                        } else {

                        }

                }

                jsonItemsList = jsonItemsList_intermidiate;


            } else {//  (Customer Pricesfor this customer==0)    ====== >>>>>     get data from priceListD

                jsonItemsList = mDbHandler.getAllJsonItems(rate_customer);
            }

//            Collections.sort(jsonItemsList<itemNoList>);

        }
    }


    @SuppressLint("ResourceAsColor")
    public boolean addItem(String itemNumber, String itemName, String tax, String unit, String qty,
                           String price, String bonus, String discount, RadioGroup discTypeRadioGroup,
                           String category, String posPrice, CheckBox useWeight, Context context, String descriptRemark, ArrayList<serialModel > itemSerialList,int hasSerial) {
        boolean itemInlocalList=false;
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        voucherDate = df.format(currentTimeAndDate);
        SalesInvoice obj = new SalesInvoice();
        String itemGroup;
        boolean existItem = false;
        Log.e("itemSerialList",""+itemSerialList.size());


            for(int i = 0 ; i< obj.getItemsList().size() ; i++){
                Log.e("***" , obj.getItemsList().get(i).getItemNo() + " " + itemNumber);
                if(obj.getItemsList().get(i).getItemNo().equals(itemNumber)){
                    existItem = true;
                    break;
                }
            }


        if(existItem) {
            Toast toast = Toast.makeText(context, "This item has been added before !", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(15);
            toast.show();

            return false;
        }


            item = new Item();
            item.setItemNo(itemNumber);
            item.setItemName(itemName);
            item.setTax(Float.parseFloat(tax.trim()));
            item.setCategory(category);
            item.setDescreption(descriptRemark);
//            item.setSerialCode(serialNo);
//            Log.e("addItem","\t"+serialNo);
// test new order
            try {
                item.setUnit(unit);
                //****************************
//                if(voucherType==508)
//                {
//                    item.setQty(Float.parseFloat("0.0"));
//                }
//                else {
//                    item.setQty(Float.parseFloat(qty));
//                }

                item.setQty(Float.parseFloat(qty));
                item.setItemHasSerial(hasSerial+"");

                item.setPrice(Float.parseFloat(price.trim()));
                if (bonus == "")
                    item.setBonus(Float.parseFloat("0.0"));
                else
                    item.setBonus(Float.parseFloat(bonus));
                item.setTax(Float.parseFloat(tax.trim()));
                item.setPosPrice(Float.parseFloat(posPrice.trim()));

            } catch (NumberFormatException e) {
                item.setUnit("");
                item.setQty(0);
                item.setPrice(0);
                item.setBonus(0);
                item.setDisc(0);
                item.setDiscPerc("0");
                item.setAmount(0);
                Log.e("Add new item error", e.getMessage().toString());
            }


            if (discTypeRadioGroup.getCheckedRadioButtonId() == R.id.discPercRadioButton) {
                item.setDiscType(1);// error for discount promotion // percent discount
            } else {
                item.setDiscType(0);// value Discount
            }

            try {
                if (item.getDiscType() == 0) {
                    item.setDisc(Float.parseFloat(discount.trim()));
                    item.setDiscPerc((item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim()) / 100)) + "");

                } else {
                    item.setDiscPerc(Float.parseFloat(discount.trim()) + "");
                    item.setDisc(item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim())) / 100);
                }
                descPerc = ((item.getQty() * item.getPrice() *
                        (Float.parseFloat(discount.trim()) / 100)));


            } catch (NumberFormatException e) {
                item.setDisc(0);
                item.setDiscPerc("0");
            }

            try {
                if (item.getDiscType() == 0) {

                    itemGroup = item.getCategory();

                /*if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE) )
                    item.setAmount(item.getQty() * (float)item.getPosPrice()  - item.getDisc());
                else*/


                    item.setAmount(item.getQty() * item.getPrice() - item.getDisc());


                    Log.e("log =", item.getQty() + " * " + item.getPrice() + " -" + item.getDisc());
//                    item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - item.getDisc());
                } else {
//                item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - descPerc);
                    item.setAmount(item.getQty() * item.getPrice() - descPerc);
                    Log.e("log ==", item.getQty() + " * " + item.getPrice() + " -" + descPerc);
                }
            } catch (NumberFormatException e) {
                item.setAmount(0);
            }
//        }


        if ((!item.getItemName().equals("")) && item.getAmount() > 0 || item.getDiscType()==0 ) {
            if (item.getItemName().equals("(bonus)")) {
                flagBonus = List.get(List.size() - 1).getQty();
                total_items_quantity -= flagBonus;
                Log.e("flagBonus", "" + flagBonus);
//               ?     amountBonus = items.get(i).getQty();
//                    totalQty = totalQty - flagBonus;
            }
            else {
                total_items_quantity += item.getQty();
                totalQty_textView.setText("+ " + total_items_quantity);
                Log.e("setQty", "" + Float.parseFloat(qty));
                Log.e("total_items_quantity", "" + total_items_quantity);
            }


                List.add(item);
                Toast toast = Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 180);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(15);
                toast.show();
                String storeNo=Login.salesMan;
                voucherDate=convertToEnglish(voucherDate);
            if(item.getItemHasSerial().equals("1"))
            {
                Log.e("itemSerialList",""+itemSerialList.size()+itemSerialList.get(0).getSerialCode());
                for(int i=0;i<itemSerialList.size();i++)
                {
                    itemSerialList.get(i).setItemNo(itemNumber);
                    itemSerialList.get(i).setDateVoucher(voucherDate);
                    itemSerialList.get(i).setKindVoucher("");
                    itemSerialList.get(i).setVoucherNo(voucherNumberTextView.getText().toString());
                    itemSerialList.get(i).setStoreNo(storeNo);
                    Log.e("voucherNumberTextView",""+voucherNumberTextView.getText().toString());

                }
                for(int j=0;j<itemSerialList.size();j++)
                {
                    mDbHandler.add_Serial(itemSerialList.get(j));
                }

            }


            return true;

        } else {
            Toast toast = Toast.makeText(context, "Items has not been added, insure your entries", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(15);
            toast.show();
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","================");
    }

    @Override
    public void onStop() {
        super.onStop();
//        int vouch=Integer.parseInt(voucherNumberTextView.getText().toString());
//        mDbHandler.deletSerialItems_byVoucherNo(vouch);
        Log.e("onStop","================"+voucherNumberTextView.getText().toString());
    }

    public void readB(){
        Log.e("barcode_099", "in");
//        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
//        intentIntegrator.setBeepEnabled(false);
//        intentIntegrator.setCameraId(0);
//        intentIntegrator.setPrompt("SCAN");
//        intentIntegrator.setBarcodeImageEnabled(false);
//        intentIntegrator.initiateScan();

//            IntentIntegrator integrator = new IntentIntegrator(getActivity());
//            integrator.setOrientationLocked(false);
//            integrator.setCaptureActivity(SmallCaptureActivity.class);
//            integrator.initiateScan();


            //*********************************************************
        new IntentIntegrator(getActivity()).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
//        public void scanToolbar(View view) {
//            new IntentIntegrator(getActivity()).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
//        }

    }


    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
    }




