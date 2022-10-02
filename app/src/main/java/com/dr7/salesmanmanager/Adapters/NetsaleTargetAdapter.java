package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.TargetDetalis;
import com.dr7.salesmanmanager.R;

import java.util.List;

public class NetsaleTargetAdapter extends RecyclerView.Adapter<NetsaleTargetAdapter.ViewHolder> {

   List<TargetDetalis> list;

   Context context;

   public NetsaleTargetAdapter(List<TargetDetalis> list, Context context) {
      this.list = list;
      this.context = context;
   }

   @NonNull
   @Override
   public NetsaleTargetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(context)
              .inflate(R.layout.netsaltargetrow, parent, false);

      return new NetsaleTargetAdapter.ViewHolder(view);
   }


   @Override
   public void onBindViewHolder(@NonNull NetsaleTargetAdapter.ViewHolder holder, int position) {
      Log.e("TARGET_NET_SALE",list.get(position).getTargetNetSale()+"");
      holder.TARGET_NET_SALE.setText(list.get(position).getTargetNetSale());

      holder.REAL_NET_SALE.setText(list.get(position).getOrignalNetSale());
      holder.PERC.setText(list.get(position).getPERC());


   }
   @Override
   public int getItemCount() {
      return list.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder {

      TextView TARGET_NET_SALE,REAL_NET_SALE,PERC;
      LinearLayout linearLayout;
      public ViewHolder(View itemView) {
         super(itemView);
         TARGET_NET_SALE =itemView.findViewById(R.id.TARGET_NET_SALE);
         REAL_NET_SALE =itemView.findViewById(R.id.REAL_NET_SALE);
         PERC=itemView.findViewById(R.id.PERC);



      }


   }
}
