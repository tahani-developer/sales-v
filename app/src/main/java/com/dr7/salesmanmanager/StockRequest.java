package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Voucher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockRequest extends Fragment {

    public ListView itemsListView;
    private ImageButton addItemImgButton, newImgBtn, SaveData;
    private EditText remarkEditText;
    private TextView totalQty, voucherNumberTextView;
    public List<Item> items;
    public ItemsListStockAdapter itemsListAdapter;
    private static DatabaseHandler mDbHandler;
    private int voucherNumber;

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
                            mDbHandler.addRequestVoucher(new Voucher(0, voucherNumber, voucherDate,
                                    salesMan, remark, total, 0));


                            for (int i = 0; i < items.size(); i++) {
                                mDbHandler.addRequestItems(new Item(0, voucherNumber, items.get(i).getItemNo(),
                                        items.get(i).getItemName(), items.get(i).getQty(), voucherDate));
                            }
                        }
                        clearLayoutData();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });


        return view;
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
        clearItemsList();
        calculateTotals();

        voucherNumber = mDbHandler.getMaxVoucherStockNumber() + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
    }

    private void clearItemsList() {
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
            qtyTextView.setText(String.valueOf(itemList.get(i).getQty() * Integer.parseInt(itemList.get(i).getUnit())));

            return myView;
        }

        @Nullable
        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }
}
