package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.AddItemsFragment2;
import com.dr7.salesmanmanager.AddItemsStockFragment;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.List;

public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.viewHolder> {

    private List<Item> items = new ArrayList<>();
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private Context context;
    boolean added = false;


    public StockRecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
        for (int i = 0; i <= items.size(); i++) {
            isClicked.add(0);
        }
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_listview, parent, false);

        return new viewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {

        if (isClicked.get(position) == 0)
            holder.linearLayout.setBackgroundColor(Color.parseColor("#455A64"));
        else
            holder.linearLayout.setBackgroundColor(R.color.done_button);

        holder.itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
        holder.itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.tradeMark.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.category.setText("" + items.get(holder.getAdapterPosition()).getCategory());
        holder.unitQty.setText("" + items.get(holder.getAdapterPosition()).getQty());
        holder.price.setText("" + items.get(holder.getAdapterPosition()).getPrice());
        holder.tax.setText("" + items.get(holder.getAdapterPosition()).getTaxPercent());
        holder.barcode.setText(items.get(holder.getAdapterPosition()).getBarcode());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_item_dialog_small);
                Window window = dialog.getWindow();

                final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
                final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
                final TextView price = (TextView) dialog.findViewById(R.id.price);
                final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
                final EditText unitQty = (EditText) dialog.findViewById(R.id.unitQty);
                final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
                final EditText discount = (EditText) dialog.findViewById(R.id.discount);
                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.discTypeRadioGroup);
                Button addToList = (Button) dialog.findViewById(R.id.addToList);

                itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
                itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
                price.setText("" + items.get(holder.getAdapterPosition()).getPrice());

                DatabaseHandler mHandler = new DatabaseHandler(context);
                List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

                ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
                unit.setAdapter(unitsList);

                addToList.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {

                        Boolean check = check_Discount(unit, unitQty, price, bonus, discount, radioGroup);
                        if (!check)
                            Toast.makeText(view.getContext(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                        else {
                            AddItemsStockFragment obj = new AddItemsStockFragment();
                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                    holder.tax.getText().toString(), unit.getSelectedItem().toString(), unitQty.getText().toString(), price.getText().toString(),
                                    bonus.getText().toString(), discount.getText().toString(), radioGroup, view.getContext());

                            if (added) {
                                holder.linearLayout.setBackgroundColor(R.color.done_button);
                                isClicked.set(holder.getAdapterPosition() , 1);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        CardView cardView;
        TextView itemNumber, itemName, tradeMark, category, unitQty, price, tax, barcode;

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
        }
    }


    private Boolean check_Discount(Spinner unitEditText, EditText qtyEditText,  TextView priceEditText,
                                   EditText bonusEditText, EditText discEditText, RadioGroup discTypeRadioGroup) {
        Boolean check = true;
        if (qtyEditText.getText().toString().equals(""))
            qtyEditText.setText("0");

        if (priceEditText.getText().toString().equals(""))
            priceEditText.setText("0");

        if (discEditText.getText().toString().equals(""))
            discEditText.setText("0");

        if (bonusEditText.getText().toString().equals(""))
            bonusEditText.setText("0");

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
}
