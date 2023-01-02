package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Response_Link;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ExportResultAdapter extends   RecyclerView.Adapter<ExportResultAdapter.ViewHolder >{
   private List<Response_Link> list;
   Context context;

   public ExportResultAdapter(List<Response_Link> list, Context context) {
      this.list = list;
      this.context = context;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.export_result_row, parent, false);
      return new ExportResultAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Log.e("link_url==",list.get(position).link_url+"");
      holder.responlink.setText(list.get(position).link_url);
      holder.responstatus.setText(list.get(position).state+"");
   }

   @Override
   public int getItemCount() {

         return list.size();
   }


   class ViewHolder extends RecyclerView.ViewHolder {
      TextView  responlink, responstatus;


      public ViewHolder(@NonNull View itemView) {

         super(itemView);

         responlink = itemView.findViewById(R.id.responlink);
         responstatus= itemView.findViewById(R.id.responstatus);



      }


   }
}

