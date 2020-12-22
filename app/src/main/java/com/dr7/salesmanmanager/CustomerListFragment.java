package com.dr7.salesmanmanager;


import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dr7.salesmanmanager.Modles.Customer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerListFragment extends DialogFragment {

    private EditText customerNameTextView;
    public ListView itemsListView;
    public List<Customer> customerList;
    private TextView t1,t2,t3;
   // public static String Customer_Name="No Customer Selected !",Customer_Account="";



 /*   public interface CustomerListFragmentInterface
    {
        public void showCustomersList();
    }

    CustomerListFragment.CustomerListFragmentInterface customerListFragmentInterface;*/

    public CustomerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customers_list, container, false);
      /*  customerNameTextView = (EditText) view.findViewById(R.id.customerNameTextView);
        itemsListView = (ListView) view.findViewById(R.id.customersList);
       // t1=(TextView)findViewById(R.id.checkInCustomerName);




        final ArrayList<String> names=new ArrayList<>();
        final ArrayList<String> CustomerAccount=new ArrayList<>();

        customerList = new ArrayList<>();
      //  final ArrayList<String> names=new ArrayList<>();
     //   final ArrayList<String> AccountNo=new ArrayList<>();
        Customer customer = new Customer("1122001111","Customer 1") ;
        customerList.add(customer);
        names.add(customer.getCustomerName().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
        customer = new Customer("1122001111", "Customer 2") ;
        customerList.add(customer);
        names.add(customer.getCustomerName().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
        customer = new Customer("1122001111", "Customer 3");
        customerList.add(customer);
        names.add(customer.getCustomerName().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
        CustomerAccount.add(customer.getCustomerAccount().toString());
      /*customerList.add(new Customer("1122001111", "Customer 3"));
        customerList.add(new Customer("1122001111", "Customer 4"));
        customerList.add(new Customer("1122001111", "Customer 5"));
        customerList.add(new Customer("1122001111", "Customer 6"));
        customerList.add(new Customer("1122001111", "Customer 1"));
        customerList.add(new Customer("1122001111", "Customer 2"));
        customerList.add(new Customer("1122001111", "Customer 3"));
        customerList.add(new Customer("1122001111", "Customer 4"));
        customerList.add(new Customer("1122001111", "Customer 5"));
        customerList.add(new Customer("1122001111", "Customer 6"));
        customerList.add(new Customer("1122001111", "Customer 1"));
        customerList.add(new Customer("1122001111", "Customer 2"));
        customerList.add(new Customer("1122001111", "Customer 3"));
        customerList.add(new Customer("1122001111", "Customer 4"));
        customerList.add(new Customer("1122001111", "Customer 5"));
        customerList.add(new Customer("1122001111", "Customer 7"));*/
/*
        CustomersListAdapter customersListAdapter = new
                CustomersListAdapter(getActivity(), customerList);
        itemsListView.setAdapter(customersListAdapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer_Name=names.get(position).toString();
                Customer_Account=CustomerAccount.get(position).toString();

                CustomerCheckInFragment customerCheckInFragment=new CustomerCheckInFragment();
                customerCheckInFragment.settext1();


                dismiss();
            }
        });*/




        return view;
    }

}
