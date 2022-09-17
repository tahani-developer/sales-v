package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.R;

import java.util.List;

public class CustomerselectedAdapter extends RecyclerView.Adapter<CustomerselectedAdapter.ViewHolder> {
Context context;
List<Customer>customerList;

    public CustomerselectedAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.select_cust_row, viewGroup, false);

        return new CustomerselectedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.  custAccTextView.setText(""+customerList.get(i).getCustId());
        viewHolder   .custNameTextView.setText(""+customerList.get(i).getCustName());
        viewHolder   . LinearLayout01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.   mainTextView.setText(context.getResources().getString(R.string.tripisstarting2)+" "+customerList.get(viewHolder.getAdapterPosition()).getCustName());
             MainActivity. NewCustomerSelecteddialog.dismiss();
                MainActivity.    starttripText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView custAccTextView,custNameTextView;
        LinearLayout LinearLayout01;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            custAccTextView=itemView.findViewById(R.id.custAccTextView);
            custNameTextView=itemView.findViewById(R.id.custNameTextView);

            LinearLayout01=itemView.findViewById(R.id.LinearLayout01);




        }


    }
}
