package com.dr7.salesmanmanager.Reports;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import android.text.Editable;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.AddItemsStockFragment;
import com.dr7.salesmanmanager.AddItemsStockFragment.*;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.List;

import static com.dr7.salesmanmanager.StockRequest.itemsRequiredList;

public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.viewHolder> {

    private List<Item> items = new ArrayList<>();

    private ArrayList<Integer> isClicked = new ArrayList<>();
    private Context context;
    boolean added = false;


    public StockRecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
//        this.itemsRequiredList=items;
        this.context = context;
        for (int i = 0; i < items.size(); i++) {
            isClicked.add(0);

        }
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
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                final Dialog dialog = new Dialog(view.getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.add_item_dialog_small_stock);
//                Window window = dialog.getWindow();
//
//                final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
//                final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
//                final TextView price = (TextView) dialog.findViewById(R.id.price);
//                final TextView pricee = (TextView) dialog.findViewById(R.id.pricee);
//                final TextView bonuss = (TextView) dialog.findViewById(R.id.bonuss);
//
//                //******************************************************invisible weight text view in stock request*************************
//                final TextView texViewtQty = (TextView) dialog.findViewById(R.id.textQty);
//                final TextView weight = (TextView) dialog.findViewById(R.id.textview_wieght);
//                final EditText weight_editText = (EditText) dialog.findViewById(R.id.unitWeight);
//                final CheckBox use_weight = (CheckBox) dialog.findViewById(R.id.use_weight);
//                //****************************************************************************************************************************
//                final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
//                final EditText unitQty = (EditText) dialog.findViewById(R.id.unitQty);
//                final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
//                final EditText discount = (EditText) dialog.findViewById(R.id.discount);
//                LinearLayout linearPrice,discount_linear,discribtionItem_linear,serialNo_linear;
//                linearPrice= dialog.findViewById(R.id.linearPrice);
//                linearPrice.setVisibility(View.GONE);
//                discount_linear= dialog.findViewById(R.id.discount_linear);
//                discount_linear.setVisibility(View.GONE);
//                discribtionItem_linear= dialog.findViewById(R.id.discribtionItem_linear);
//                discribtionItem_linear.setVisibility(View.GONE);
//                serialNo_linear= dialog.findViewById(R.id.serialNo_linear);
//                serialNo_linear.setVisibility(View.GONE);
//
//                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.discTypeRadioGroup);
//                Button addToList = (Button) dialog.findViewById(R.id.addToList);
//
//                itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
//                itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
//
//                price.setVisibility(View.INVISIBLE);
//                pricee.setVisibility(View.INVISIBLE);
//                bonus.setVisibility(View.INVISIBLE);
//                discount.setVisibility(View.INVISIBLE);
//                radioGroup.setVisibility(View.INVISIBLE);
//                bonuss.setVisibility(View.INVISIBLE);
//                //******************************************
//                weight.setVisibility(View.INVISIBLE);
//                weight_editText.setVisibility(View.INVISIBLE);
//                texViewtQty.setText("Unit Qty");
//                use_weight.setVisibility(View.INVISIBLE);
//                //******************************************************
//
//                DatabaseHandler mHandler = new DatabaseHandler(context);
//                List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());
//
//                ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
//                unit.setAdapter(unitsList);
//
//                addToList.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceAsColor")
//                    @Override
//                    public void onClick(View v) {
//
//                        Boolean check = check_Discount(unit, unitQty, price, bonus, discount, radioGroup);
//                        if (!check)
//                            Toast.makeText(view.getContext(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
//                        else {
//
////                            AddItemsStockFragment obj = new AddItemsStockFragment();
////                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
////                                    holder.tax.getText().toString(), unit.getSelectedItem().toString(), unitQty.getText().toString(),
////                                    "1"+items.get(holder.getAdapterPosition()).getPrice(),
////                                    bonus.getText().toString(), discount.getText().toString(), radioGroup, view.getContext());
//
//                            if (added) {
//                                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.layer5));
//                                isClicked.set(holder.getAdapterPosition() , 1);
//                            }
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//            }
//        });

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
    }


}
