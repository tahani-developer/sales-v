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
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.R;

import java.util.List;

public class ItemsdeffAdapter  extends RecyclerView.Adapter<ItemsdeffAdapter.ViewHolder> {
   Context context;
   List<SalesManItemsBalance> list;
   public static TextView respon;
   public ItemsdeffAdapter(Context context, List<SalesManItemsBalance> list) {
      this.context = context;
      this.list = list;
   }

   @NonNull
   @Override
   public ItemsdeffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = LayoutInflater.from(context).inflate(R.layout.itemdiffrow, viewGroup, false);

      return new ItemsdeffAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ItemsdeffAdapter.ViewHolder viewHolder, int i) {
      viewHolder. Num.setText(String.valueOf(i+1));
      viewHolder.  itemnum.setText(list.get(i).getItemNo());
      viewHolder.  itemname.setText("");
      viewHolder.  diff.setText(list.get(i).getQty()-list.get(i).getAct_Qty()+"");



   }

   @Override
   public int getItemCount() {
      return list.size();
   }


   class ViewHolder extends RecyclerView.ViewHolder {
      TextView Num,itemnum,itemname,diff;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);
         Num=itemView.findViewById(R.id.Num);
         itemname=itemView.findViewById(R.id.item_name);
         respon=itemView.findViewById(R.id.respon);
         itemnum=itemView.findViewById(R.id.itemNum);
         diff=itemView.findViewById(R.id.diff);
      }
   }
}
