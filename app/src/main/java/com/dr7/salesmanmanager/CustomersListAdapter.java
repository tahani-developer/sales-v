package com.dr7.salesmanmanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dr7.salesmanmanager.Modles.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class CustomersListAdapter extends BaseAdapter {

    private Context context;
    private List<Customer> custList = new ArrayList<>();

    public CustomersListAdapter(Context context, List<Customer> custList)
    {
        this.context = context;
        this.custList = custList;
    }

    public void setItemsList(List<Customer> custList)
    {
        this.custList = custList;
    }

    @Override
    public int getCount() {
        return custList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View myView = View.inflate(context, R.layout.customers_item,null);
        TextView custAccountTextView = (TextView) myView.findViewById(R.id.custAccTextView);
        TextView custNameTextView = (TextView) myView.findViewById(R.id.custNameTextView);


        custAccountTextView.setText(""+custList.get(i).getCustId());
        custNameTextView.setText(custList.get(i).getCustName());

        return myView;
    }
}
