package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dr7.salesmanmanager.Modles.Item;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by mohd darras on 29/12/2017.
 */

public class ItemsListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itemList,secondListItems;
    private DecimalFormat decimalFormat;
    int typeScreen=0;//0 landscap
    DatabaseHandler databaseHandler ;
    String rate="0";

    public ItemsListAdapter(Context context, List<Item> itemList,int type)
    {
        this.context = context;
        this.itemList = itemList;

        decimalFormat = new DecimalFormat("##.000");
        this.typeScreen=type;
        if(itemList.size()!=0)
        Log.e("itemList",""+itemList.get(0).getDisc()+"\t getDiscType"+itemList.get(0).getDiscType()+"\t getAmount="+itemList.get(0).getAmount());
        Log.e("typeScreen",""+typeScreen+"\t type"+type);
        databaseHandler=new DatabaseHandler(context);
        rate=databaseHandler.getRateOfCustomer();
    }

    public void setItemsList(List<Item> itemList)
    {
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
        @SuppressLint("ViewHolder") View myView = View.inflate(context, R.layout.items_list,null);
        TextView itemNoTextView = (TextView) myView.findViewById(R.id.itemNoTextView);
        TextView itemNameTextView = (TextView) myView.findViewById(R.id.DescTextView);
        TextView qtyTextView = (TextView) myView.findViewById(R.id.qtyTextView);
        TextView priceTextView = (TextView) myView.findViewById(R.id.priceTextView);
        TextView bonusTextView = (TextView) myView.findViewById(R.id.bonusTextView);
        TextView lineDescTextView = (TextView) myView.findViewById(R.id.lineDiscTextView);
        TextView amountTextView = (TextView) myView.findViewById(R.id.amountTextView);
        TextView itemNoTextView_detail = (TextView) myView.findViewById(R.id.itemNoTextView_detail);
        TextView bonusTextView_detail = (TextView) myView.findViewById(R.id.bonusTextView_detail);
        TextView lineDiscTextView_detail = (TextView) myView.findViewById(R.id.lineDiscTextView_detail);
        TextView showDetail = (TextView) myView.findViewById(R.id.showDetail);
        LinearLayout detailLayout=myView.findViewById(R.id.detailLayout);
        detailLayout.setVisibility(View.GONE);
        if(typeScreen==0)
        {
            showDetail.setVisibility(View.GONE);
            itemNoTextView.setVisibility(View.VISIBLE);
            bonusTextView.setVisibility(View.VISIBLE);
            lineDescTextView.setVisibility(View.VISIBLE);
        }
        else {
            showDetail.setVisibility(View.VISIBLE);
            itemNoTextView.setVisibility(View.GONE);
            bonusTextView.setVisibility(View.GONE);
            lineDescTextView.setVisibility(View.GONE);


        }

        showDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailLayout.getVisibility()==View.VISIBLE)
                {
                    detailLayout.setVisibility(View.GONE);
                }
                else if(detailLayout.getVisibility()==View.GONE)
                {
                    detailLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        Log.e("itemList",""+itemList.get(i).getDisc()+"\t getDiscType"+itemList.get(i).getDiscType()+"\t getAmount="+itemList.get(i).getAmount());

        itemNoTextView.setText(itemList.get(i).getItemNo());
        itemNoTextView_detail.setText(itemList.get(i).getItemNo());
        itemNameTextView.setText(itemList.get(i).getItemName());
        if(databaseHandler.getAllSettings().get(0).getItemUnit()==1)
        {
            int itemUnit=databaseHandler.getUnitForItem(itemList.get(i).getItemNo());
            if(itemList.get(i).getOneUnitItem().equals("1"))
                qtyTextView.setText(itemList.get(i).getQty()+"");
            else
            qtyTextView.setText((itemList.get(i).getQty()/itemUnit)+"");
            //Log.e("itemUnit","1="+itemUnit);
          //  Log.e("itemUnit","2="+itemList.get(i).getQty()/itemUnit);

        }else {
            qtyTextView.setText(""+itemList.get(i).getQty());
        }

        if(databaseHandler.getAllSettings().get(0).getItemUnit()==1 &&itemList.get(i).getOneUnitItem().equals("0"))
        {
            int itemUnit=databaseHandler.getUnitForItem(itemList.get(i).getItemNo());
            String itemUnitPrice=databaseHandler.getUnitPrice(itemList.get(i).getItemNo().trim(),rate);
           // Log.e("priceTextView","1="+itemUnitPrice);
            if(!itemUnitPrice.equals(""))
            {
                if(itemList.get(i).getOneUnitItem().equals("1"))
                priceTextView.setText(String.valueOf(itemList.get(i).getPrice()));
                else priceTextView.setText(itemList.get(i).getEnter_price());
//                priceTextView.setText(String.valueOf(itemList.get(i).getPrice()));
            }else {
                priceTextView.setText(String.valueOf(itemList.get(i).getPrice()));
            }





        }else {
            priceTextView.setText(String.valueOf(itemList.get(i).getPrice()));
        }



        bonusTextView.setText(String.valueOf(itemList.get(i).getBonus()));
        bonusTextView_detail.setText(String.valueOf(itemList.get(i).getBonus()));
        lineDescTextView.setText(String.valueOf(itemList.get(i).getDisc()));
        lineDiscTextView_detail.setText(String.valueOf(itemList.get(i).getDisc()));

        amountTextView.setText(convertToEnglish(decimalFormat.format(itemList.get(i).getAmount())));

        return myView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

}
