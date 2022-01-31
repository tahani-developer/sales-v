package com.dr7.salesmanmanager.Modles;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Adapters.AccountStatmentAdapter;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalesManPlanAdapter extends  RecyclerView.Adapter<SalesManPlanAdapter.SalesManPlanAdapterViewHolder>   {

     ArrayList<SalesManPlan>salesManPlans;
     Context context;

    public SalesManPlanAdapter(ArrayList<SalesManPlan> salesManPlans, Context context) {
        this.salesManPlans = salesManPlans;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesManPlanAdapter.SalesManPlanAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salman_plan_row, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new SalesManPlanAdapter.SalesManPlanAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesManPlanAdapter.SalesManPlanAdapterViewHolder holder, int position) {
     holder.order.setText(position+"");
        holder.cusname.setText(salesManPlans.get(position).getCustName()+"");
 holder.cuslocation.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Log.e("onClick","onClick");
         if(salesManPlans.get(position).getLatitud()!=0) {

             if(salesManPlans.get(position).getLatitud()!=0) {
                 String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
                        salesManPlans.get(position).getLatitud(),
                   salesManPlans.get(position).getLongtude());
                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                 context.startActivity(intent);
             }
             else {
                 new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                         .setTitleText(context.getResources().getString(R.string.Noloction))
                         .setContentText("")
                         .show();
             }
         }else{
             new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                     .setTitleText(context.getResources().getString(R.string.Noloction))
                     .setContentText("")
                     .show();
         }
     }
 });
    }

    @Override
    public int getItemCount() {
        return salesManPlans.size();
    }


  public   class SalesManPlanAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView order,cusname,cuslocation;
        public SalesManPlanAdapterViewHolder(View itemView) {
            super(itemView);

            order = itemView.findViewById(R.id.order);
            cusname = itemView.findViewById(R.id.cusname);
            cuslocation = itemView.findViewById(R.id.cuslocation);



            //Log.e("", "ViewHolder const");

        }
    }



}
