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

import com.dr7.salesmanmanager.ImportJason;
import com.dr7.salesmanmanager.Modles.TargetDetalis;
import com.dr7.salesmanmanager.R;

import java.util.List;

public class ItemsTargetAdapter extends RecyclerView.Adapter<ItemsTargetAdapter.ViewHolder> {

    List<TargetDetalis> list;

    Context context;

    public ItemsTargetAdapter(List<TargetDetalis> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsTargetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.itemtargetrow, parent, false);

        return new ItemsTargetAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemsTargetAdapter.ViewHolder holder, int position) {
        Log.e("onBindViewHolder","onBindViewHolder"+ ImportJason.salesGoalsList.size());
        holder.Perceofunfulfilledgoal.setText(list.get(position).getPERC());
        holder.goalachievementrate.setText(list.get(position).getOrignalNetSale());
        holder.item_number.setText(list.get(position).getItemNo());
        holder.item_name.setText(list.get(position).getItemName());
        holder.target.setText(list.get(position).getItemTarget() + "");

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView target,item_name,Perceofunfulfilledgoal,goalachievementrate,item_number;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            item_name =itemView.findViewById(R.id.item_name);
            item_number =itemView.findViewById(R.id.item_number);
            target=itemView.findViewById(R.id.target);
            goalachievementrate=itemView.findViewById(R.id.goalachievementrate);
            Perceofunfulfilledgoal=itemView.findViewById(R.id.Perceofunfulfilledgoal);



        }


    }
}
