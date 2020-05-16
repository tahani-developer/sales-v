package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Offers;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.SalesInvoice.size_customerpriceslist;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<Item> items;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context context;
    boolean added = false;
    DatabaseHandler MHandler;
    DecimalFormat threeDForm ;
    int settingPriceCus=0;
    List<String> localItemNumber;
    boolean itemInlocalList=false;

    public RecyclerViewAdapter(List<Item> items, Context context) {
        this.items = items;
        this.filterList = items;
        this.context = context;
        for (int i = 0; i <= items.size(); i++) {
            isClicked.add(0);
        }
        MHandler = new DatabaseHandler(context);
        settingPriceCus=MHandler.getAllSettings().get(0).getPriceByCust();
        localItemNumber= new ArrayList<>();
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
        if (isClicked.get(position) == 0)
            holder.linearLayout.setBackgroundColor(Color.parseColor("#455A64"));
        else
            holder.linearLayout.setBackgroundColor(R.color.done_button);

        holder.itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());

        holder.itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.tradeMark.setText(items.get(holder.getAdapterPosition()).getItemName());
        holder.category.setText("" + items.get(holder.getAdapterPosition()).getCategory());

        if(MHandler.getAllSettings().get(0).getHide_qty()==1) {
            holder.row_qty.setVisibility(View.GONE);
//            holder.unitQty.setVisibility(View.GONE);
        }
        else{
            holder.unitQty.setText("" + items.get(holder.getAdapterPosition()).getQty());
        }
        if(settingPriceCus==1)
        {
            if(checkTypePriceTable(items.get(holder.getAdapterPosition()).getItemNo())){
                holder.imagespecial.setVisibility(View.VISIBLE);
            }
            else{
                holder.imagespecial.setVisibility(View.GONE);
            }
        }
        else{
            holder.imagespecial.setVisibility(View.GONE);
        }


        holder.price.setText(convertToEnglish( threeDForm.format(items.get(holder.getAdapterPosition()).getPrice())));

        Log.e("format",""+ threeDForm.format(items.get(holder.getAdapterPosition()).getPrice()));
//       *******************************//////////////////////*
        holder.tax.setText("" + items.get(holder.getAdapterPosition()).getTaxPercent());
        holder.barcode.setText(items.get(holder.getAdapterPosition()).getBarcode());
        holder.posprice.setText(items.get(holder.getAdapterPosition()).getPosPrice()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                itemInlocalList=false;
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
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.add_item_dialog_small);
                    final TextView itemNumber = dialog.findViewById(R.id.item_number);
//                final TextView categoryTextView =  dialog.findViewById(R.id.item_number);
                    final TextView itemName = dialog.findViewById(R.id.item_name);
                    final EditText price = dialog.findViewById(R.id.price);
                    final Spinner unit = dialog.findViewById(R.id.unit);
                    final TextView textQty = dialog.findViewById(R.id.textQty);
                    final EditText unitQty = dialog.findViewById(R.id.unitQty);
                    final EditText unitWeight = dialog.findViewById(R.id.unitWeight);
                    final CheckBox useWeight = dialog.findViewById(R.id.use_weight);
                    final EditText bonus = dialog.findViewById(R.id.bonus);
                    final EditText discount = dialog.findViewById(R.id.discount);
                    final RadioGroup radioGroup = dialog.findViewById(R.id.discTypeRadioGroup);
                    final LinearLayout discountLinearLayout = dialog.findViewById(R.id.discount_linear);
                    final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
                    final LinearLayout bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
                    final LinearLayout discribtionItem_linear = dialog.findViewById(R.id.discribtionItem_linear);
                    final EditText item_remark = dialog.findViewById(R.id.item_note);
                    if(MHandler.getAllSettings().get(0).getRequiNote()==1)
                    {
                        discribtionItem_linear.setVisibility(View.VISIBLE);

                    }
                    else{
                        discribtionItem_linear.setVisibility(View.GONE);

                    }

                    Button addToList = dialog.findViewById(R.id.addToList);
                    itemNumber.setText(items.get(holder.getAdapterPosition()).getItemNo());
                    itemName.setText(items.get(holder.getAdapterPosition()).getItemName());
                    final DatabaseHandler mHandler = new DatabaseHandler(context);
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
                            discountLinearLayout.setVisibility(View.INVISIBLE);
                        }
                    if (mHandler.getAllSettings().get(0).getBonusNotAlowed() == 0) {//you can  add bonus
                        bonusLinearLayout.setVisibility(View.VISIBLE);
                    } else {
//                 bonus.setText("0");
//                 bonusLinearLayout.setVisibility(View.INVISIBLE);
                        bonus.setEnabled(false);
                    }

                    if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                        unitWeightLinearLayout.setVisibility(View.INVISIBLE);
                        textQty.setText(view.getContext().getResources().getString(R.string.app_qty));
                        useWeight.setChecked(false);
                    } else
                        unitQty.setText("" + items.get(holder.getAdapterPosition()).getItemL());

                    List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

                    ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
                    unit.setAdapter(unitsList);

                    addToList.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(View v) {

                            if (!price.getText().toString().equals("") && !price.getText().toString().equals("0") && !(unitQty.getText().toString()).equals("")) {
                                if (Double.parseDouble(unitQty.getText().toString()) != 0) {

                                    Boolean check = check_Discount(unitWeight, unitQty, price, bonus, discount, radioGroup);
                                    if (!check)
                                        Toast.makeText(view.getContext(), "Invalid Disco" +
                                                "unt Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                                    else {

                                        String unitValue;
                                        if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                            unitValue = unit.getSelectedItem().toString();

                                            if (items.get(holder.getAdapterPosition()).getQty() >= Double.parseDouble(unitQty.getText().toString())
                                                    || mHandler.getAllSettings().get(0).getAllowMinus() == 1
                                                    || SalesInvoice.voucherType == 506 || SalesInvoice.voucherType == 508) {

                                                if (mHandler.getAllSettings().get(0).getMinSalePric() == 0 || (mHandler.getAllSettings().get(0).getMinSalePric() == 1 &&
                                                        Double.parseDouble(price.getText().toString()) >= items.get(holder.getAdapterPosition()).getMinSalePrice())) {


                                                    AddItemsFragment2 obj = new AddItemsFragment2();
                                                    List<Offers> offer = checkOffers(itemNumber.getText().toString());
                                                    Offers appliedOffer = null;

                                                    if (offer.size() != 0) {

                                                        if (offer.get(0).getPromotionType() == 0) {// bonus promotion

                                                            added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                    holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                    bonus.getText().toString(),
                                                                    discount.getText().toString(),
                                                                    radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight,
                                                                    view.getContext(),item_remark.getText().toString());

                                                            appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 0);
                                                            if (appliedOffer != null) {
                                                                double bonus_calc = ((int) (Double.parseDouble(unitQty.getText().toString()) / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();
                                                                Log.e("bonus_calc=", "" + bonus_calc);
                                                                added = obj.addItem(offer.get(0).getBonusItemNo(), "(bonus)",
                                                                        "0", "1", "" + bonus_calc, "0",
                                                                        "0", "0", radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(),item_remark.getText().toString());
                                                                Log.e("bonus_calc", "" + bonus_calc);

                                                            }
                                                        } else {
                                                            //(appliedOffer.getBonusQty()*Double.parseDouble(unitQty.getText().toString()))   //******calculate discount item before 11/9
                                                            double disount_totalnew = 0, unitQty_double = 0;

                                                            appliedOffer = getAppliedOffer(itemNumber.getText().toString(), unitQty.getText().toString(), 1);
                                                            if (appliedOffer != null) {
                                                                unitQty_double = Double.parseDouble(unitQty.getText().toString());
                                                                disount_totalnew = ((int) (unitQty_double / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();

                                                                String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());

                                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                        holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                                        bonus.getText().toString(), "" + disount_totalnew, radioGroup
                                                                        , items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(),item_remark.getText().toString());
                                                            }
                                                        }
                                                    } else {
                                                        double totalQty = 0;
                                                        totalQty = Double.parseDouble(unitQty.getText().toString()) + Double.parseDouble(bonus.getText().toString());
                                                        Log.e("totalQty+recyclerview", "" + totalQty);
                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                holder.tax.getText().toString(), unitValue, unitQty.getText().toString() + "", price.getText().toString(),
                                                                bonus.getText().toString(), discount.getText().toString(), radioGroup,
                                                                items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(),item_remark.getText().toString());
                                                    }
                                                    if (added) {
                                                        if (offer.size() != 0)
                                                            openOfferDialog(appliedOffer);

                                                        holder.linearLayout.setBackgroundColor(R.color.done_button);
                                                        isClicked.set(holder.getAdapterPosition(), 1);
                                                        localItemNumber.add(items.get(holder.getAdapterPosition()).getItemNo());
                                                        itemInlocalList=false;
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
                                                                            radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(),item_remark.getText().toString());

                                                                    appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 0);
                                                                    if (appliedOffer != null)
                                                                        added = obj.addItem(appliedOffer.getBonusItemNo(), "(bonus)",
                                                                                "0", "1", "" + appliedOffer.getBonusQty(), "0",
                                                                                "0", "0", radioGroup
                                                                                , items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + ""
                                                                                , useWeight, view.getContext(),item_remark.getText().toString());

                                                                } else {
                                                                    appliedOffer = getAppliedOffer(itemNumber.getText().toString(), qty, 1);
                                                                    if (appliedOffer != null) {
                                                                        String priceAfterDiscount = "" + (Double.parseDouble(price.getText().toString()) - appliedOffer.getBonusQty());
                                                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                                holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                                bonus.getText().toString(), "" + (appliedOffer.getBonusQty() * Double.parseDouble(qty)), radioGroup,
                                                                                items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + ""
                                                                                , useWeight, view.getContext(),item_remark.getText().toString());
                                                                    }
                                                                }
                                                            } else {
                                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                                        holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                                        bonus.getText().toString(), discount.getText().toString(),
                                                                        radioGroup, items.get(holder.getAdapterPosition()).getCategory(), items.get(holder.getAdapterPosition()).getPosPrice() + "", useWeight, view.getContext(),item_remark.getText().toString());
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

                            } else
                                Toast.makeText(view.getContext(), "Invalid price or Qty", Toast.LENGTH_LONG).show();


                        }
                    });
                    dialog.show();

                }
            }
        }//on click

        );

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    void openOfferDialog(Offers offers) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.offer_dialog);

        final TextView offerType = (TextView) dialog.findViewById(R.id.offerType);
        final TextView bonusItem = (TextView) dialog.findViewById(R.id.bonusItem);
        final TextView discount_value = (TextView) dialog.findViewById(R.id.discount_value);
        final TextView from = (TextView) dialog.findViewById(R.id.from);
        final TextView to = (TextView) dialog.findViewById(R.id.to);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        String offType = offers.getPromotionType() == 0 ? context.getResources().getString(R.string.app_bonus) : context.getResources().getString(R.string.app_disc_);
        offerType.setText(offerType.getText().toString() + "  :       " + offType);

        String bonusItm = offers.getBonusItemNo().equals("-1") ? "none" : offers.getBonusQty() + " " + context.getResources().getString(R.string.of) + " " + offers.getBonusItemNo();
        bonusItem.setText(bonusItem.getText().toString() + " :     " + bonusItm);

        String disc = offers.getPromotionType() == 0 ? "0" : "" + offers.getBonusQty();
        discount_value.setText(discount_value.getText().toString() + " : " + disc);

        from.setText(from.getText().toString() + " : " + offers.getFromDate());
        to.setText(to.getText().toString() + " : " + offers.getToDate());

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
