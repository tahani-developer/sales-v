package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Item;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<Item> items;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context context;
    boolean added = false;


    public RecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
        this.filterList = items;
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

                final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
                final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
                final EditText price = (EditText) dialog.findViewById(R.id.price);
                final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
                final EditText unitQty = (EditText) dialog.findViewById(R.id.unitQty);
                final EditText unitWeight = (EditText) dialog.findViewById(R.id.unitWeight);
                final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
                final EditText discount = (EditText) dialog.findViewById(R.id.discount);
                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.discTypeRadioGroup);
                final LinearLayout discountLinearLayout = (LinearLayout) dialog.findViewById(R.id.discount_linear);
                final LinearLayout unitWeightLinearLayout = (LinearLayout) dialog.findViewById(R.id.linearWeight);
                Button addToList = (Button) dialog.findViewById(R.id.addToList);

                itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
                itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
                price.setText("" + items.get(holder.getAdapterPosition()).getPrice());

                final DatabaseHandler mHandler = new DatabaseHandler(context);

                if (mHandler.getAllSettings().get(0).getTaxClarcKind() == 1)
                    discountLinearLayout.setVisibility(View.INVISIBLE);

                if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0)
                    unitWeightLinearLayout.setVisibility(View.INVISIBLE);

                List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

                ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
                unit.setAdapter(unitsList);

                addToList.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {

                        if (!price.getText().toString().equals("") && !price.getText().toString().equals("0")) {

                            Boolean check = check_Discount(unit, unitQty, price, bonus, discount, radioGroup);
                            if (!check)
                                Toast.makeText(view.getContext(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                            else {

                                String unitValue;
                                if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                    unitValue = unit.getSelectedItem().toString();

                                    AddItemsFragment2 obj = new AddItemsFragment2();
                                    added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                            holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                            bonus.getText().toString(), discount.getText().toString(), radioGroup, view.getContext());

                                    if (added) {
                                        holder.linearLayout.setBackgroundColor(R.color.done_button);
                                        isClicked.set(holder.getAdapterPosition(), 1);
                                    }
                                } else {
                                    if (unitWeight.getText().toString() == "")
                                        Toast.makeText(view.getContext(), "please enter unit weight", Toast.LENGTH_LONG).show();
                                    else {
                                        unitValue = unitWeight.getText().toString();
                                        AddItemsFragment2 obj = new AddItemsFragment2();
                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                bonus.getText().toString(), discount.getText().toString(), radioGroup, view.getContext());

                                        if (added) {
                                            holder.linearLayout.setBackgroundColor(R.color.done_button);
                                            isClicked.set(holder.getAdapterPosition(), 1);
                                        }
                                    }
                                }

                            }

                            dialog.dismiss();
                        } else
                            Toast.makeText(view.getContext(), "Invalid price", Toast.LENGTH_LONG).show();


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

    private Boolean check_Discount(Spinner unitEditText, EditText qtyEditText, TextView priceEditText,
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
