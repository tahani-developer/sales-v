package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.CarStocking;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.R;

import java.util.List;

public class CarStockAdapter extends RecyclerView.Adapter<CarStockAdapter.ViewHolder> {
   Context context;
   List<SalesManItemsBalance> list;
    public static TextView  respon;
   public CarStockAdapter(Context context, List<SalesManItemsBalance> list) {
      this.context = context;
      this.list = list;
   }

   @NonNull
   @Override
   public CarStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = LayoutInflater.from(context).inflate(R.layout.carstockrow, viewGroup, false);

      return new CarStockAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull CarStockAdapter.ViewHolder viewHolder, int i) {

      viewHolder. Num.setText(String.valueOf(i+1));
      viewHolder.  itemnum.setText(list.get(i).getItemNo());
      viewHolder.  itemname.setText("");
      viewHolder.  cur_aty.setText(list.get(i).getQty()+"");
    viewHolder.  act_qty.setText(list.get(i).getAct_Qty()+"");

       if (viewHolder.getAdapterPosition() == CarStocking.highligtedItemPosition) {
      //    CarStocking.highligtedItemPosition=-1;
           Log.e("highligtedItemPosition==",CarStocking.highligtedItemPosition+" i== "+i);
           viewHolder.  row.setBackgroundColor(context.getResources().getColor(R.color.yelow));
          viewHolder.  act_qty.requestFocus();

       }else
          viewHolder.  row.setBackgroundColor(context.getResources().getColor(R.color.layer7));
       viewHolder.  act_qty.addTextChangedListener(new TextWatcher() {
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
                    try {
                       CarStocking.getSalesManItemsQTY.get(viewHolder.getAdapterPosition()).setAct_Qty(  Double.parseDouble(viewHolder. act_qty.getText().toString()));

                    }catch (Exception exception)
                    {

                    }
                       }
          }
       });
   }

   @Override
   public int getItemCount() {
      return list.size();
   }


   class ViewHolder extends RecyclerView.ViewHolder {
TextView Num,itemnum,itemname,cur_aty;
EditText act_qty;
LinearLayout row;
      public ViewHolder(@NonNull View itemView) {
         super(itemView);
         Num=itemView.findViewById(R.id.Num);
         row=itemView.findViewById(R.id.row);
         respon=itemView.findViewById(R.id.respon);
         itemnum=itemView.findViewById(R.id.itemNum);
                 itemname=itemView.findViewById(R.id.item_name);
                 cur_aty=itemView.findViewById(R.id.cur_qty);
         act_qty=itemView.findViewById(R.id.act_qty);
      }
   }
}
