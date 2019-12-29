package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Voucher;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockRequest extends Fragment {

    public ListView itemsListView;
    private ImageButton addItemImgButton, newImgBtn, SaveData;
    private EditText remarkEditText;
    private TextView  voucherNumberTextView;
    public static TextView totalQty;
    public List<Item> items;
    public ItemsListStockAdapter itemsListAdapter;
    private static DatabaseHandler mDbHandler;
    public static  int voucherNumber;
  public static List<Item> jsonItemsList;
    CompanyInfo companyInfo;
   public static List<Item> listItemStock;
    public static  Voucher voucherStock;


    public static Voucher voucherStockItem;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_stock_request, container, false);
        mDbHandler = new DatabaseHandler(getActivity());
        jsonItemsList = new ArrayList<>();
        String rate_customer=mDbHandler.getRateOfCustomer();
        companyInfo=new CompanyInfo();
        Log.e("rate addItem",""+rate_customer);
            jsonItemsList = mDbHandler.getAllJsonItems(rate_customer);

        voucherNumber = mDbHandler.getMaxVoucherStockNumber() + 1;

        addItemImgButton = (ImageButton) view.findViewById(R.id.addItemImgButton);
        newImgBtn = (ImageButton) view.findViewById(R.id.newImgBtn);
        SaveData = (ImageButton) view.findViewById(R.id.saveInvoiceData);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        totalQty = (TextView) view.findViewById(R.id.total_qty);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumberTextView);

        voucherNumberTextView.setText(voucherNumber + "");

        addItemImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockInterFace.displayFindItemStockFragment();
            }
        });


        items = new ArrayList<>();
        listItemStock= new ArrayList<>();
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

                            Date currentTimeAndDate = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            String voucherDate = df.format(currentTimeAndDate);

                            if (mDbHandler.getMaxVoucherStockNumber() == voucherNumber) { // if we clicked on save twice
                                mDbHandler.deleteVoucher(voucherNumber);
                            }

                            int salesMan = Integer.parseInt(Login.salesMan);

                            double total = Double.parseDouble(totalQty.getText().toString());
                            voucherStock= new Voucher(0, voucherNumber, voucherDate,
                                    salesMan, remark, total, 0);
//                            mDbHandler.addRequestVoucher(new Voucher(0, voucherNumber, voucherDate,
//                                    salesMan, remark, total, 0));
                            mDbHandler.addRequestVoucher(voucherStock);




                            for (int i = 0; i < items.size(); i++) {
                                mDbHandler.addRequestItems(new Item(0, voucherNumber, items.get(i).getItemNo(),
                                        items.get(i).getItemName(), items.get(i).getQty(), voucherDate));
                            }
                            printStock();

                        }

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });


        return view;
    }

    private void printStock() {
        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
            try {
                int printer = mDbHandler.getPrinterSetting();
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                    switch (printer) {
                        case 0:
                            listItemStock=items;
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

//                                                             MTP.setChecked(true);
//                            voucherShow = voucher;
//                            convertLayoutToImage(voucher);
                            listItemStock=items;
                            voucherStockItem=voucherStock;
                            Intent O = new Intent(getActivity().getBaseContext(), bMITP.class);
                            O.putExtra("printKey", "6");
                            startActivity(O);
                            clearLayoutData();


                            break;

                    }
                } else {
//                   Toast.makeText(SalesInvoice.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
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
        totalQty.setText("");
        calculateTotals();

        voucherNumber = mDbHandler.getMaxVoucherStockNumber() + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
    }

    public void clearItemsList() {
        items.clear();
        itemsListAdapter.setItemsList(items);
        itemsListAdapter.notifyDataSetChanged();
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
