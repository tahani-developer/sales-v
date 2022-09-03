package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Pending_Invoice;
import com.dr7.salesmanmanager.Modles.Pending_Serial;
import com.dr7.salesmanmanager.R;

import java.text.DecimalFormat;
import java.util.List;

public class Pending_seriak_adapter extends RecyclerView.Adapter<Pending_seriak_adapter.ViewHolder> {
    static List<Pending_Serial> inventorylist;
    public double totalBalance = 0;

    Context context;
    private DecimalFormat decimalFormat;
    static int totalQty_inventory = 0;


    public Pending_seriak_adapter(List<Pending_Serial> inventorylist, Context context) {
        this.inventorylist = inventorylist;
        this.context = context;
        decimalFormat = new DecimalFormat("00.000");
    }

    @Override
    public Pending_seriak_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_inv_row, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new Pending_seriak_adapter.ViewHolder(view);

    }

    public String[] mColors = {"#ffffff", "#E8BCE4F7"};

    @Override
    public void onBindViewHolder(Pending_seriak_adapter.ViewHolder holder, int position) {
        // Log.e("onBindViewHolder", "" + totalBalance);

        //totalBalance=0;
        //  holder.setIsRecyclable(false);
//        holder.voucherNo.setText(inventorylist.get(holder.getAdapterPosition()).getVoucherNo());

//        holder.balance.setText(decimalFormat.format(totalBalance));

        holder. transeKind.setText(inventorylist.get(holder.getAdapterPosition()).getSERIALCODE());
        holder.       van_code.setText(inventorylist.get(holder.getAdapterPosition()).getSTORENO());
        if(inventorylist.get(holder.getAdapterPosition()).getTRNSKIND().equals("504"))
        {
            holder.net_total.setText(context.getResources().getString(R.string.app_sales_inv2));
        }
       else if(inventorylist.get(holder.getAdapterPosition()).getTRNSKIND().equals("506"))
        {
            holder.net_total.setText(context.getResources().getString(R.string.app_ret_inv2));
        }
      else   if(inventorylist.get(holder.getAdapterPosition()).getTRNSKIND().equals("508"))
        {
            holder.net_total.setText(context.getResources().getString(R.string.order));
        }
//        holder.net_total.setText(inventorylist.get(holder.getAdapterPosition()).getTRNSKIND());


        holder.        trans_date.setText(inventorylist.get(holder.getAdapterPosition()).getTRNSDATE());

        holder.        voucher_no.setText(inventorylist.get(holder.getAdapterPosition()).getVHFNO());




    }

    @Override
    public int getItemCount() {

        return inventorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transeName, voucher_no,transeKind,van_code,trans_date,net_total;

        public ViewHolder(View itemView) {
            super(itemView);

            transeKind = itemView.findViewById(R.id.transeKind);
            van_code   = itemView.findViewById(R.id.van_code);
            net_total  = itemView.findViewById(R.id.net_total);
            trans_date = itemView.findViewById(R.id.trans_date);
            transeName = itemView.findViewById(R.id.transeName);
            voucher_no = itemView.findViewById(R.id.voucher_no);
            //Log.e("", "ViewHolder const");

        }
    }
}
