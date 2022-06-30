package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.InventoryShelf;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialListitems;

import static com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter.serialListitems_stock;
import static com.dr7.salesmanmanager.Stock_Activity.intentData;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockRequest extends Fragment {

    public ListView itemsListView;
    public static ImageButton addItemImgButton, newImgBtn ;
    FloatingActionButton SaveData,deletAllData;
    private EditText remarkEditText;
    public static TextView voucherNumberTextView;
    public static TextView totalQty;
    public  static  List<Item> items;
    public ItemsListStockAdapter itemsListAdapter;
    private static DatabaseHandler mDbHandler;
    public static int voucherNumber;
//    public static List<Item> jsonItemsList;
    CompanyInfo companyInfo;
    public static List<Item> listItemStock;
    public static Voucher voucherStock;
    ProgressDialog dialog_progress;
    int itemCountTable;
    LinearLayout mainLinear;
    public static List<Item> itemsRequiredList = new ArrayList<>();
    SweetAlertDialog getPdValidationItemCard;
    public static Voucher voucherStockItem;
    DecimalFormat threeDForm;
    double curentQ=0;
    public  static TextView clearData;
    public  static  String voucherDate="";
    public  static  GeneralMethod generalMethod;
    public  static  ArrayList<InventoryShelf> listSerialInventory=new ArrayList<>();
    public  static  List<String> itemsNoList=new ArrayList<>();

    public List<Item> getItemsStockList() {
        return this.items;
    }

    public interface StockInterFace {
        public void displayFindItemStockFragment();
    }

    StockInterFace stockInterFace;

    public StockRequest() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        new LocaleAppUtils().changeLayot(MainActivity.this);
        final View view = inflater.inflate(R.layout.fragment_stock_request, container, false);
        mDbHandler = new DatabaseHandler(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        itemCountTable=mDbHandler.getCountItemsMaster();
        generalMethod=new GeneralMethod(getActivity());


        threeDForm = new DecimalFormat("00.00");

//        Log.e("itemsRequiredList",""+itemsRequiredList.size()+"\t"+itemsRequiredList.get(0).getQty());
        mainLinear=view.findViewById(R.id.mainLinear);
        try {
            if (languagelocalApp.equals("ar"))
            {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else
            {
                if (languagelocalApp.equals("en")) {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch (Exception e){
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
//        jsonItemsList = new ArrayList<>();
        String rate_customer = mDbHandler.getRateOfCustomer();
        companyInfo = new CompanyInfo();
//        jsonItemsList = mDbHandler.getAllJsonItemsStock();
        if(intentData.equals("read"))
        {
            voucherNumber=mDbHandler.getmaxSerialInventoryShelf()+1;
        }else {
            voucherNumber = mDbHandler.getMaxVoucherStockNumber() + 1;
        }

        addItemImgButton = (ImageButton) view.findViewById(R.id.addItemImgButton);
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        voucherDate = df.format(currentTimeAndDate);
        newImgBtn = (ImageButton) view.findViewById(R.id.newImgBtn);
        SaveData = (FloatingActionButton) view.findViewById(R.id.saveInvoiceData);
        deletAllData=(FloatingActionButton) view.findViewById(R.id.deletAllData);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        totalQty = (TextView) view.findViewById(R.id.total_qty);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumberTextView);
        voucherNumberTextView.setText(voucherNumber + "");
        clearData=(TextView) view.findViewById(R.id.clearData);
        clearData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(""))
                {
                    if(s.toString().equals("1"))
                    {
                        clearItemsList();
                    }
                    else if(s.toString().equals("2"))
                    {
                        getPdValidationItemCard = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        getPdValidationItemCard.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
                        getPdValidationItemCard.setTitleText(getActivity().getResources().getString(R.string.process) + "3");
                        getPdValidationItemCard.setCancelable(false);
                        getPdValidationItemCard.show();
                    }
                    else if(s.toString().equals("3"))
                    {
                        getPdValidationItemCard.dismissWithAnimation();
                    }
                }

            }
        });
        addItemImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemImgButton.setEnabled(false);
                if(itemCountTable>=500)
                {
//                    new TaskStock().execute();
                    stockInterFace.displayFindItemStockFragment();
                }
                else
                {
                    stockInterFace.displayFindItemStockFragment();


                }





                // here

            }
        });


        getData();

//        itemsRequiredList= mDbHandler.getAllJsonItemsStock(2);
        items = new ArrayList<>();
        listItemStock = new ArrayList<>();
        itemsListView = (ListView) view.findViewById(R.id.itemsListViewFragment);
        itemsListAdapter = new ItemsListStockAdapter(getActivity(), items);
        itemsListView.setAdapter(itemsListAdapter);
        itemsListView.setOnItemLongClickListener(onItemLongClickListener);

        newImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearLayoutData();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });
        deletAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClearDialog();


            }
        });
        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {


                        int listSize = itemsListView.getCount();
                        if (listSize == 0)
                            Toast.makeText(getActivity(), "Fill Your List Please", Toast.LENGTH_LONG).show();
                        else {

                            String remark = " " + remarkEditText.getText().toString();
                            int salesMan = Integer.parseInt(Login.salesMan);

                            double total = Double.parseDouble(totalQty.getText().toString());
                            SaveData.setEnabled(false);
                            if(intentData.equals("read")){// add to inventory

                                for(int i=0;i<listSerialInventory.size();i++)
                                {
                                    Log.e("listSerialInventory",""+listSerialInventory.size()+"\tgetSERIAL_NO="+listSerialInventory.get(i).getSERIAL_NO());
                                    mDbHandler.add_inventoryShelf(listSerialInventory.get(i));
                                }


                            }else {// add to stock

                                if (mDbHandler.getMaxVoucherStockNumber() == voucherNumber) { // if we clicked on save twice
                                    mDbHandler.deleteVoucher(voucherNumber);
                                }


                                voucherStock = new Voucher(0, voucherNumber, convertToEnglish(voucherDate),
                                        salesMan, remark, total, 0);
//                            mDbHandler.addRequestVoucher(new Voucher(0, voucherNumber, voucherDate,
//                                    salesMan, remark, total, 0));
                                mDbHandler.addRequestVoucher(voucherStock);


                                for (int i = 0; i < items.size(); i++) {
                                    String cureQty = convertToEnglish(threeDForm.format(items.get(i).getCurrentQty()));
                                    curentQ = Double.parseDouble(cureQty);
                                    mDbHandler.addRequestItems(new Item(0, voucherNumber, items.get(i).getItemNo(),
                                            items.get(i).getItemName(), items.get(i).getQty(), convertToEnglish(voucherDate), curentQ));
                                }
                            }
                                try {
                                    if((mDbHandler.getAllSettings().get(0).getSaveOnly()!=1)&&(!intentData.equals("read")))
                                    {  printStock();}
                                    else {
                                        clearLayoutData();
                                        clearItemsList();
                                        SaveData.setEnabled(true);
                                        new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(view.getContext().getString(R.string.saveSuccessfuly))
                                                .show();
                                    }

                                }
                                catch (Exception e)
                                {
                                    clearLayoutData();
                                }






                        }

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });


        return view;
    }

    private void getData() {
        if(intentData.equals("read"))
        {
            itemsRequiredList= mDbHandler.getAllJsonItemsStock(200);
        }
        else {
            if(mDbHandler.getFlagSettings().get(0).getMake_Order()==1)
            {
                int store=1;
                try {
                    store= Integer.parseInt( mDbHandler.getAllSettings().get(0).getStoreNo());
                }catch (Exception e){
                    store=1;
                }

                itemsRequiredList= mDbHandler.getAllJsonItemsStock(store);

            }else {
                itemsRequiredList= mDbHandler.getAllJsonItemsStock(0);
            }

        }
    }

    private void openClearDialog() {


        SweetAlertDialog sweetMessage= new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.ClearAll));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);

        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                clearItemsList();
                clearLayoutData();

                sweetMessage.dismissWithAnimation();
            }
        })

                .show();
    }


    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    class TaskStock extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                publishProgress(i);
//            }
            stockInterFace.displayFindItemStockFragment();
            return "items";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            super.onPreExecute();
            dialog_progress = new ProgressDialog(getActivity());
            dialog_progress.setCancelable(false);
            dialog_progress.setMessage(getResources().getString(R.string.loadingItem));
            dialog_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog_progress.show();
        }
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            dialog_progress.dismiss();

            if (result != null) {

            } else {
                Toast.makeText(getActivity(), "Not able to fetch data ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void printStock() {
        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
            try {
                int printer = mDbHandler.getPrinterSetting();
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                if (!companyInfo.getCompanyName().equals("") && !companyInfo.getcompanyTel() .equals("") && companyInfo.getTaxNo() != -1) {
                    switch (printer) {
                        case 0:
                            listItemStock = items;
                            Intent i = new Intent(getActivity().getBaseContext(), BluetoothConnectMenu.class);
                            i.putExtra("printKey", "6");
                            startActivity(i);
                            clearLayoutData();
//                                                             lk30.setChecked(true);
                            break;
                        case 1:

//                            try {
//                                findBT();
//                                openBT(1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
////                                                             lk31.setChecked(true);
//                            break;
                        case 2:

//                               try {
//                                   findBT();
//                                   openBT(2);
//                               } catch (IOException e) {
//                                   e.printStackTrace();
//                               }
////                                                             lk32.setChecked(true);
//                            voucherShow = voucher;
//
//                            convertLayoutToImagew(getActivity());
//                            Intent O1 = new Intent(getActivity().getBaseContext(), bMITP.class);
//                            O1.putExtra("printKey", "1");
//                            startActivity(O1);


//                            break;
                        case 3:

//                            try {
//                                findBT();
//                                openBT(3);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                                                             qs.setChecked(true);
//                            break;
                        case 4:
//                            printTally(voucher);
//                            break;


                        case 5:
                        case 6:

//                                                             MTP.setChecked(true);
//                            voucherShow = voucher;
//                            convertLayoutToImage(voucher);
                            listItemStock = items;
                            voucherStockItem = voucherStock;
                            clearLayoutData();
                            Intent O = new Intent(getActivity().getBaseContext(), bMITP.class);
                            O.putExtra("printKey", "6");
                            startActivity(O);



                            break;

                    }
                } else {
                    clearLayoutData();
                    clearItemsList();
//                   Toast.makeText(SalesInvoice.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                clearLayoutData();
                clearItemsList();
                Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();

            }


//                                                } catch (IOException ex) {
//                                                }
        } else {
//            hiddenDialog();
        }
    }

    public AdapterView.OnItemLongClickListener onItemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            items.remove(position);
                            itemsRequiredList.get(position).setQty(0);
                            itemsListView.setAdapter(itemsListAdapter);
                            calculateTotals();
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                    builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear_item));
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;

                }
            };

    public void setListener(StockInterFace listener) {
        this.stockInterFace = listener;
    }

    public void calculateTotals() {
        double sum = 0.0;
        for (int i = 0; i < items.size(); i++) {
            sum = sum + (items.get(i).getQty() * Integer.parseInt(items.get(i).getUnit()));
        }
        totalQty.setText(sum + "");


    }

    private void clearLayoutData() {
        remarkEditText.setText(" ");

        //calculateTotals();
        if(intentData.equals("read"))
        {
            voucherNumber=mDbHandler.getmaxSerialInventoryShelf()+1;
        }else {
            voucherNumber = mDbHandler.getMaxVoucherStockNumber() + 1;
        }
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
        totalQty.setText("00.00");

    }

    public void clearItemsList() {
       // items.clear();
        itemsNoList.clear();
        items=new ArrayList<>();
        itemsListAdapter.setItemsList(items);
        itemsListAdapter.notifyDataSetChanged();
//        for(int i=0;i<itemsRequiredList.size();i++)
//        {
//            itemsRequiredList.get(i).setQty(0);
//            itemsRequiredList.get(i).setCurrentQty(0);
//        }


        try {
//            itemsRequiredList= mDbHandler.getAllJsonItemsStock(200);
            getData();
            listItemStock = new ArrayList<>();
            listSerialInventory.clear();
            serialListitems_stock.clear();

        }catch (Exception e){Log.e("serialListitems_stock","Exception+clear");}

    }

    public class ItemsListStockAdapter extends BaseAdapter {

        private Context context;
        private List<Item> itemList;

        public ItemsListStockAdapter(Context context, List<Item> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        public void setItemsList(List<Item> itemList) {
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View myView = View.inflate(context, R.layout.items_stock_list, null);
            TextView itemNoTextView = (TextView) myView.findViewById(R.id.itemNoTextView);
            TextView itemNameTextView = (TextView) myView.findViewById(R.id.DescTextView);
            TextView qtyTextView = (TextView) myView.findViewById(R.id.qtyTextView);

            itemNoTextView.setText(itemList.get(i).getItemNo());
            itemNameTextView.setText(itemList.get(i).getItemName());
            qtyTextView.setText(String.valueOf(itemList.get(i).getQty()));
            //  qtyTextView.setText(String.valueOf(itemList.get(i).getQty() * Integer.parseInt(itemList.get(i).getUnit())));

            return myView;
        }

        @Nullable
        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }
}
