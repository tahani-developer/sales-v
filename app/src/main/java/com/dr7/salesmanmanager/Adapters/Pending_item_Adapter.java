package com.dr7.salesmanmanager.Adapters;

import static com.dr7.salesmanmanager.AccountStatment.total_qty_text;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Pending_Invoice;
import com.dr7.salesmanmanager.R;

import java.text.DecimalFormat;
import java.util.List;

public class Pending_item_Adapter extends RecyclerView.Adapter<Pending_item_Adapter.ViewHolder> {
    static List<Pending_Invoice> inventorylist;
    public double totalBalance = 0;

    Context context;
    private DecimalFormat decimalFormat;
    static int totalQty_inventory = 0;


    public Pending_item_Adapter(List<Pending_Invoice> inventorylist, Context context) {
        this.inventorylist = inventorylist;
        this.context = context;
        decimalFormat = new DecimalFormat("00.000");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_inv_row, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new ViewHolder(view);

    }

    public String[] mColors = {"#ffffff", "#E8BCE4F7"};

    @Override
    public void onBindViewHolder(Pending_item_Adapter.ViewHolder holder, int position) {
        // Log.e("onBindViewHolder", "" + totalBalance);

        //totalBalance=0;
        //  holder.setIsRecyclable(false);
//        holder.voucherNo.setText(inventorylist.get(holder.getAdapterPosition()).getVoucherNo());

//        holder.balance.setText(decimalFormat.format(totalBalance));
        if(inventorylist.get(holder.getAdapterPosition()).getTRANSKIND().equals("504"))
        {
            holder.transeKind.setText(context.getResources().getString(R.string.app_sales_inv2));
        }
        else if(inventorylist.get(holder.getAdapterPosition()).getTRANSKIND().equals("506"))
        {
            holder.transeKind.setText(context.getResources().getString(R.string.app_ret_inv2));
        }
        else   if(inventorylist.get(holder.getAdapterPosition()).getTRANSKIND().equals("508"))
        {
            holder.transeKind.setText(context.getResources().getString(R.string.order));
        }
        else  if(inventorylist.get(holder.getAdapterPosition()).getTRANSKIND().equals("505"))
        {
            holder.transeKind.setText(context.getResources().getString(R.string.purchaseOrder));
        }
//        holder. transeKind.setText(inventorylist.get(holder.getAdapterPosition()).getTRANSKIND());
        holder.       van_code.setText(inventorylist.get(holder.getAdapterPosition()).getVANCODE());
        holder.net_total.setText(inventorylist.get(holder.getAdapterPosition()).getNETTOT());
        holder.        trans_date.setText(inventorylist.get(holder.getAdapterPosition()).getTRDATE());

        holder.        voucher_no.setText(inventorylist.get(holder.getAdapterPosition()).getVHFI());




    }

    @Override
    public int getItemCount() {

        return inventorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  transeName, voucher_no,transeKind,van_code,trans_date,net_total;

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