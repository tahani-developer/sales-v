package com.dr7.salesmanmanager.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.CustomerListShow;
import com.dr7.salesmanmanager.Modles.TransactionsInfo;
import com.dr7.salesmanmanager.Modles.visitMedicalModel;
import com.dr7.salesmanmanager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VisitMedicalAdapter  extends RecyclerView.Adapter<VisitMedicalAdapter.ViewHolder> {
    List<visitMedicalModel> transactionsInfos;
    Context context;
    List<visitMedicalModel> transactionsInfos_noDuplicate;

    public VisitMedicalAdapter(List<visitMedicalModel> transactionsInfos, Context context,List<visitMedicalModel> listNoItems) {
//        this.transactionsInfos = transactionsInfos;
        this.transactionsInfos_noDuplicate=transactionsInfos;
        this.transactionsInfos=listNoItems;
        this.context = context;
    }




    @NonNull
    @Override
    public VisitMedicalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.visit_report_row, viewGroup, false);

        return new VisitMedicalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitMedicalAdapter.ViewHolder viewHolder, int i) {
        Log.e("onBindViewHolder",""+transactionsInfos.get(i).getCustname());
        viewHolder.cust_name.setText(transactionsInfos.get(i).getCustname());

        viewHolder.tool.setText(transactionsInfos.get(i).getTool().equals("1")?"Y":"N");
        viewHolder.doubleVisit.setText(transactionsInfos.get(i).getDoubleVisit().equals("1")?"Y":"N");
        viewHolder.adoption.setText(transactionsInfos.get(i).getAdoption());
        viewHolder.remark.setText(transactionsInfos.get(i).getRemark());
        viewHolder.itemname.setText(transactionsInfos.get(i).getNameItem());
        viewHolder.itemqty.setText(transactionsInfos.get(i).getQtyItem());

        viewHolder.info_items.setOnClickListener(view ->{

            showItemsDialog(transactionsInfos.get(i).getCustname(),transactionsInfos.get(i).getVoucherNo(),transactionsInfos.get(i).getHaveItem());
        });
    }

    private void showItemsDialog(String custName,String voucher,String haveItems) {
        double discount_voucher = 0;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_visit_medical_dialog);

        TextView name = (TextView) dialog.findViewById(R.id.textViewcustomerName);

        TextView textViewNoData= (TextView) dialog.findViewById(R.id.textViewNoData);
        ImageView okButton=dialog.findViewById(R.id.button3);
        name.setText(custName);

        ListView listItems=dialog.findViewById(R.id.listItems);

        List<String> listItemsString=getItemsFor(voucher);
        Log.e("haveItems","="+haveItems);
        if(haveItems.equals("0")){
            textViewNoData.setVisibility(View.VISIBLE);
            listItems.setVisibility(View.GONE);
        }else {
            textViewNoData.setVisibility(View.GONE);
            listItems.setVisibility(View.VISIBLE);
            ArrayAdapter adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,listItemsString);
            listItems.setAdapter(adapter);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private List<String> getItemsFor(String voucher) {
        List<String> items=new ArrayList<>();
        for(int i=0;i<transactionsInfos_noDuplicate.size();i++){
            if(transactionsInfos_noDuplicate.get(i).getVoucherNo().equals(voucher))
                items.add(transactionsInfos_noDuplicate.get(i).getNameItem()+"\t"+transactionsInfos_noDuplicate.get(i).getQtyItem());
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return transactionsInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cust_name,adoption,tool,doubleVisit,remark,itemname,itemqty;
        ImageView info_items;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name=itemView.findViewById(R.id.cust_name);
            adoption=itemView.findViewById(R.id.adoption);
            tool=itemView.findViewById(R.id.tool);
            doubleVisit=itemView.findViewById(R.id.doubleVisit);
            remark=itemView.findViewById(R.id.remark);
            itemname=itemView.findViewById(R.id.itemname);
            itemqty  =itemView.findViewById(R.id.itemqty);
            info_items=itemView.findViewById(R.id.info_items);

        }


    }
}
