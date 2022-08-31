package com.dr7.salesmanmanager.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.TransactionsInfo;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;

public class CustomertransAdapter extends RecyclerView.Adapter<CustomertransAdapter.ViewHolder> {
    ArrayList<TransactionsInfo>transactionsInfos;
    Context context;

    public CustomertransAdapter(ArrayList<TransactionsInfo> transactionsInfos, Context context) {
        this.transactionsInfos = transactionsInfos;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.cust_trans_reportrow, viewGroup, false);

        return new CustomertransAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.cust_name.setText(transactionsInfos.get(i).getCust_name());
        viewHolder.NameofPerson.setText(transactionsInfos.get(i).getPersonname());
        viewHolder.phoneNum.setText(transactionsInfos.get(i).getPhoneNum());
        viewHolder.reson.setText(transactionsInfos.get(i).getReson());

    }

    @Override
    public int getItemCount() {
        return transactionsInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
TextView cust_name,reson,NameofPerson,phoneNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name=itemView.findViewById(R.id.cust_name);
            reson=itemView.findViewById(R.id.reson);
            NameofPerson=itemView.findViewById(R.id.NameofPerson);
            phoneNum=itemView.findViewById(R.id.phoneNum);





        }


    }
}
