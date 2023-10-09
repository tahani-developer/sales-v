package com.dr7.salesmanmanager.Adapters;



import static com.dr7.salesmanmanager.MainActivity.EndTrip_Report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.CustomerCheckInFragment;
import com.dr7.salesmanmanager.CustomerListShow;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.SalesManCustomerNoteAdapter;
import com.dr7.salesmanmanager.R;

import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class CustomersListAdapterR extends RecyclerView.Adapter<CustomersListAdapterR.CustomersListAdapterRViewHolder> {
//        implements Filterable {

    private Context context;
    private List<Customer> mOriginalValues;
    private List<Customer> custList;
    private CustomerListShow customerListShow;
    public  int showCustomerLoc_sett;
    DatabaseHandler mDHandler;
    int flagPlan=Login.SalsManPlanFlage;
    int flagEndDay =EndTrip_Report;

    public CustomersListAdapterR(CustomerListShow customerListShow, Context context, List<Customer> custList, int showCustomerLoc) {
        this.context = context;
        this.mOriginalValues = custList;
        this.custList = custList;
        this.customerListShow = customerListShow;
        this.showCustomerLoc_sett=showCustomerLoc;
        this. mDHandler = new DatabaseHandler(context);
    }

    public void setItemsList(List<Customer> custList) {
        this.custList = custList;
    }
    public List<Customer> getItemsList() {
       return custList;
    }

//    @Override
//    public int getCount() {
//        return custList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }

    @NonNull
    @Override
    public CustomersListAdapterRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_item, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new CustomersListAdapterR.CustomersListAdapterRViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomersListAdapterRViewHolder holder, int i) {
        if(showCustomerLoc_sett==0)
        {
            holder. showloction.setVisibility(View.INVISIBLE);
        }else {
            holder. showloction.setVisibility(View.VISIBLE);
        }
//
        holder. showloction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLocation customerLocation=mDHandler.getCustomerLocationBYNUMBER(custList.get(i).getCustId());

                Log.e("location==", customerLocation.getLONG()+"   "+customerLocation.getLATIT()+"");
                if(customerLocation.getLATIT()!=null
                        && customerLocation.getLONG()!=null) {
                    if (!customerLocation.getLATIT().equals("")) {

                        if (Double.parseDouble(customerLocation.getLATIT()) != 0) {
                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
                                    Double.parseDouble(customerLocation.getLATIT()),
                                    Double.parseDouble(customerLocation.getLONG()));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            context.startActivity(intent);

                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getResources().getString(R.string.Noloction))
                                    .setContentText("")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getResources().getString(R.string.Noloction))
                                .setContentText("")
                                .show();
                    }

                }else
                {
                    Customer customer=mDHandler.getcustomerinfo(custList.get(i).getCustId());
                    if(customer.getCustLat()!=null &&customer.getCustLong()!=null) {
                        if (!customer.getCustLat().equals("")) {

                            if (Double.parseDouble(customer.getCustLat()) != 0) {
                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
                                        Double.parseDouble(customer.getCustLat()),
                                        Double.parseDouble(customer.getCustLong()));
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                context.startActivity(intent);

                            } else {
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getResources().getString(R.string.Noloction))
                                        .setContentText("")
                                        .show();
                            }
                        }else {
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getResources().getString(R.string.Noloction))
                                    .setContentText("")
                                    .show();
                        }
                    }
                    else   new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getResources().getString(R.string.Noloction))
                            .setContentText("")
                            .show();
                }



                //   MapsActivity.showLocations1(Double.parseDouble(custList.get(i).getCustLat()),Double.parseDouble(custList.get(i).getCustLong()),custList.get(i).getCustName());
            }
        });
        holder.custAccountTextView.setText("" + custList.get(i).getCustId());
        holder.custNameTextView.setText(custList.get(i).getCustName());
        //  Log.e("getView2===",i+"");
        if(flagEndDay==1)
        {
            holder.acc_nTextView.setText(custList.get(i).getZipCode());
            holder.calssTextView.setText(custList.get(i).getFax());
            holder.spicialityTextView.setText(custList.get(i).geteMail());
            holder.categTextView.setText(custList.get(i).getC_THECATEG());
            holder.LinearMedical.setVisibility(View.VISIBLE);
        }else  holder.LinearMedical.setVisibility(View.GONE);

        if(flagPlan==1) {
            //    Log.e("case1","case1");
            if(MainActivity.plantype==2){
                //  Log.e("case2","case2");
                holder.linearLayout.setEnabled(true);
            }else
            {   Log.e("DB_salesManPlanLi",MainActivity.DB_salesManPlanList.size()+"");
                if (MainActivity.DB_salesManPlanList.size() == 0) {
                    holder.linearLayout.setEnabled(true);
                } else {
                    //  Log.e("custList====",custList.get(i).getCustId()+"");
                    if (IsCkeckOut(custList.get(i).getCustId()))
                        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red_background));

                    holder.showloction.setVisibility(View.VISIBLE);
                    if (custList.get(i).getCustId().equals( getAllowedCust()) )
                    {
                        holder.linearLayout.setEnabled(true);
                        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorblue_dark));
                    } else
                        holder.linearLayout.setEnabled(false);
                }
            }   }else

        {
            // Log.e("case3","case no salesman plan");
            // case no salesman plan
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(custList.get(i).getIsSuspended()!=1)
                {
                    MainActivity.CusId=  custList.get(i).getCustId();
                    CustomerListShow.Customer_Name = custList.get(i).getCustName();
//                CustomerListShow.Customer_No=custList.get(i).getCustId();
                    CustomerListShow.Customer_Account = custList.get(i).getCustId() + "";
                    CustomerListShow.CashCredit = custList.get(i).getCashCredit();
                    CustomerListShow.PriceListId = custList.get(i).getPriceListId();
                    CustomerListShow.paymentTerm = custList.get(i).getPayMethod();
                    if((custList.get(i).getCustLat() == null)||(custList.get(i).getCustLong() == null))
                    {
                        CustomerListShow.latitude="";
                        CustomerListShow.longtude="";
                    }
                    else {
                        CustomerListShow.latitude=custList.get(i).getCustLat();
                        CustomerListShow.longtude=custList.get(i).getCustLong();
                    }



                    CustomerListShow.CreditLimit = custList.get(i).getCreditLimit();
                    CustomerListShow.Max_Discount_value = custList.get(i).getMax_discount();
                    if (custList.get(i).getIsSuspended() == 1)
                    {
                        Toast toast = Toast.makeText(context, "This customer is susbended", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 180);
                        ViewGroup group = (ViewGroup) toast.getView();
                        TextView messageTextView = (TextView) group.getChildAt(0);
                        messageTextView.setTextSize(25);
                        toast.show();
                    } else
                    {
                        CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment();
                        customerCheckInFragment.settext1();

                    }

                    CustomerListShow.CustHideValu=custList.get(i).getHide_val();
                    customerListShow.dismiss();


                }
                else{
                    Log.e("cust",""+custList.get(i).getIsSuspended());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.cusSuspended));
                    builder.setTitle(context.getResources().getString(R.string.warning_message));
                    builder.setPositiveButton(context.getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }


                    });
                    builder.create().show();
                }
            }
        });
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }

//    private class ViewHolder {
//        LinearLayout linearLayout,LinearMedical;
//        TextView custAccountTextView,showloction;
//        TextView custNameTextView,calssTextView,acc_nTextView,spicialityTextView,categTextView;
//
//
//    }

//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//       // Log.e("getView1===",i+"");
//        final ViewHolder holder = new ViewHolder();
//        view = View.inflate(context, R.layout.customers_item, null);
//
//        holder.linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout01);
//        holder.LinearMedical = (LinearLayout) view.findViewById(R.id.LinearMedical);
//
//        holder.custAccountTextView = (TextView) view.findViewById(R.id.custAccTextView);
//        holder.custNameTextView = (TextView) view.findViewById(R.id.custNameTextView);
//        holder.calssTextView = (TextView) view.findViewById(R.id.calssTextView);
//        holder.acc_nTextView = (TextView) view.findViewById(R.id.acc_nTextView);
//        holder.spicialityTextView = (TextView) view.findViewById(R.id.spicialityTextView);
//        holder. showloction= (TextView) view.findViewById(R.id.showloction);
//        holder.categTextView = (TextView) view.findViewById(R.id.categTextView);
//
//
//        if(showCustomerLoc_sett==0)
//        {
//            holder. showloction.setVisibility(View.INVISIBLE);
//        }else {
//            holder. showloction.setVisibility(View.VISIBLE);
//        }
////
//        holder. showloction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomerLocation customerLocation=mDHandler.getCustomerLocationBYNUMBER(custList.get(i).getCustId());
//
//               Log.e("location==", customerLocation.getLONG()+"   "+customerLocation.getLATIT()+"");
//                if(customerLocation.getLATIT()!=null
//                && customerLocation.getLONG()!=null) {
//                    if (!customerLocation.getLATIT().equals("")) {
//
//                        if (Double.parseDouble(customerLocation.getLATIT()) != 0) {
//                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
//                                    Double.parseDouble(customerLocation.getLATIT()),
//                                    Double.parseDouble(customerLocation.getLONG()));
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                            context.startActivity(intent);
//
//                        } else {
//                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                    .setTitleText(context.getResources().getString(R.string.Noloction))
//                                    .setContentText("")
//                                    .show();
//                        }
//                    } else {
//                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText(context.getResources().getString(R.string.Noloction))
//                                .setContentText("")
//                                .show();
//                    }
//
//                }else
//                {
//                    Customer customer=mDHandler.getcustomerinfo(custList.get(i).getCustId());
//                    if(customer.getCustLat()!=null &&customer.getCustLong()!=null) {
//                        if (!customer.getCustLat().equals("")) {
//
//                            if (Double.parseDouble(customer.getCustLat()) != 0) {
//                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
//                                        Double.parseDouble(customer.getCustLat()),
//                                        Double.parseDouble(customer.getCustLong()));
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                                context.startActivity(intent);
//
//                            } else {
//                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                        .setTitleText(context.getResources().getString(R.string.Noloction))
//                                        .setContentText("")
//                                        .show();
//                            }
//                        }else {
//                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                    .setTitleText(context.getResources().getString(R.string.Noloction))
//                                    .setContentText("")
//                                    .show();
//                        }
//                    }
//                 else   new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText(context.getResources().getString(R.string.Noloction))
//                            .setContentText("")
//                            .show();
//                }
//
//
//
//             //   MapsActivity.showLocations1(Double.parseDouble(custList.get(i).getCustLat()),Double.parseDouble(custList.get(i).getCustLong()),custList.get(i).getCustName());
//            }
//        });
//        holder.custAccountTextView.setText("" + custList.get(i).getCustId());
//        holder.custNameTextView.setText(custList.get(i).getCustName());
//      //  Log.e("getView2===",i+"");
//        if(flagEndDay==1)
//        {
//            holder.acc_nTextView.setText(custList.get(i).getZipCode());
//        holder.calssTextView.setText(custList.get(i).getFax());
//        holder.spicialityTextView.setText(custList.get(i).geteMail());
//        holder.categTextView.setText(custList.get(i).getC_THECATEG());
//            holder.LinearMedical.setVisibility(View.VISIBLE);
//        }else  holder.LinearMedical.setVisibility(View.GONE);
//
//        if(flagPlan==1) {
//        //    Log.e("case1","case1");
//            if(MainActivity.plantype==2){
//              //  Log.e("case2","case2");
//                holder.linearLayout.setEnabled(true);
//            }else
//            {   Log.e("DB_salesManPlanLi",MainActivity.DB_salesManPlanList.size()+"");
//                if (MainActivity.DB_salesManPlanList.size() == 0) {
//                holder.linearLayout.setEnabled(true);
//            } else {
//                  //  Log.e("custList====",custList.get(i).getCustId()+"");
//                if (IsCkeckOut(custList.get(i).getCustId()))
//                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red_background));
//
//                holder.showloction.setVisibility(View.VISIBLE);
//                if (custList.get(i).getCustId().equals( getAllowedCust()) )
//                {
//                    holder.linearLayout.setEnabled(true);
//                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorblue_dark));
//                } else
//                    holder.linearLayout.setEnabled(false);
//            }
//        }   }else
//
//            {
//               // Log.e("case3","case no salesman plan");
//            // case no salesman plan
//        }
//
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(custList.get(i).getIsSuspended()!=1)
//                {
//                    MainActivity.CusId=  custList.get(i).getCustId();
//                CustomerListShow.Customer_Name = custList.get(i).getCustName();
////                CustomerListShow.Customer_No=custList.get(i).getCustId();
//                CustomerListShow.Customer_Account = custList.get(i).getCustId() + "";
//                CustomerListShow.CashCredit = custList.get(i).getCashCredit();
//                CustomerListShow.PriceListId = custList.get(i).getPriceListId();
//                CustomerListShow.paymentTerm = custList.get(i).getPayMethod();
//                    if((custList.get(i).getCustLat() == null)||(custList.get(i).getCustLong() == null))
//                    {
//                        CustomerListShow.latitude="";
//                                CustomerListShow.longtude="";
//                    }
//                    else {
//                        CustomerListShow.latitude=custList.get(i).getCustLat();
//                        CustomerListShow.longtude=custList.get(i).getCustLong();
//                    }
//
//
//
//                CustomerListShow.CreditLimit = custList.get(i).getCreditLimit();
//                CustomerListShow.Max_Discount_value = custList.get(i).getMax_discount();
//                if (custList.get(i).getIsSuspended() == 1)
//                {
//                    Toast toast = Toast.makeText(context, "This customer is susbended", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 180);
//                    ViewGroup group = (ViewGroup) toast.getView();
//                    TextView messageTextView = (TextView) group.getChildAt(0);
//                    messageTextView.setTextSize(25);
//                    toast.show();
//                } else
//                    {
//                    CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment();
//                    customerCheckInFragment.settext1();
//
//                }
//
//                    CustomerListShow.CustHideValu=custList.get(i).getHide_val();
//                    customerListShow.dismiss();
//
//
//            }
//                else{
//                    Log.e("cust",""+custList.get(i).getIsSuspended());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage(context.getResources().getString(R.string.cusSuspended));
//                    builder.setTitle(context.getResources().getString(R.string.warning_message));
//                    builder.setPositiveButton(context.getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//
//
//                    });
//                    builder.create().show();
//                }
//            }
//        });
//
//        return view;
//    }



//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
//
//                custList = (ArrayList<Customer>) results.values; // has the filtered values
//                notifyDataSetChanged();  // notifies the data with new filtered values
////                Log.e("here" , "*********1" + custList.get(0).getCustName());
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<Customer> FilteredArrList = new ArrayList<Customer>();
//
//                if (mOriginalValues == null) {
//                    mOriginalValues = new ArrayList<Customer>(custList); // saves the original data in mOriginalValues
//                }
//
//                /********
//                 *
//                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//                 *  else does the Filtering and returns FilteredArrList(Filtered)
//                 *
//                 ********/
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = mOriginalValues.size();
//                    results.values = mOriginalValues;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getCustName();
//                        String dataCustNo = mOriginalValues.get(i).getCustId();
//                        int paymetho = mOriginalValues.get(i).getPayMethod();
//
//                      //  Log.e("mOriginalpaymetho" , "paymetho"+paymetho);
//                        if (data.toLowerCase().contains(constraint.toString())||dataCustNo.contains(constraint.toString())) {
//                            FilteredArrList.add(new Customer(mOriginalValues.get(i).getCustId(),mOriginalValues.get(i).getCustName(),mOriginalValues.get(i).getPayMethod()));
//                          //  Log.e("mOriginalValues" , "*********2" + constraint + "*" + mOriginalValues.get(i).getPayMethod());
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//
////                    Log.e("here" , "*********3" + FilteredArrList.get(0).getCustName());
//                }
//                return results;
//            }
//        };
//        return filter;
//    }

    String getAllowedCust() {


        String CusNum = "-1";

        for (int i = 0; i < MainActivity.DB_salesManPlanList.size(); i++)
            if (MainActivity.DB_salesManPlanList.get(i).getLogoutStatus() == 0) {
                CusNum = MainActivity.DB_salesManPlanList.get(i).getCustNumber();
                break;

            }


        return CusNum;
    }
   boolean IsHaveOrder(String id){


      boolean f=false;

        for(int i=0;i< MainActivity.DB_salesManPlanList .size();i++)
            if(MainActivity.DB_salesManPlanList .get(i).getCustNumber().equals(id)) {
               f=true;
                break;

            }


        return  f;   }

    boolean IsCkeckOut(String id){


        boolean f=false;

        for(int i=0;i< MainActivity.DB_salesManPlanList .size();i++)
            if(MainActivity.DB_salesManPlanList .get(i).getCustNumber().equals(id)

        &&MainActivity.DB_salesManPlanList .get(i).getLogoutStatus()==1) {
                f=true;
                break;

            }


        return  f;   }


    public   class CustomersListAdapterRViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout,LinearMedical;
        TextView custAccountTextView,showloction;
        TextView custNameTextView,calssTextView,acc_nTextView,spicialityTextView,categTextView;

        public CustomersListAdapterRViewHolder(View view) {
            super(view);

            linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout01);
            LinearMedical = (LinearLayout) view.findViewById(R.id.LinearMedical);

            custAccountTextView = (TextView) view.findViewById(R.id.custAccTextView);
            custNameTextView = (TextView) view.findViewById(R.id.custNameTextView);
            calssTextView = (TextView) view.findViewById(R.id.calssTextView);
            acc_nTextView = (TextView) view.findViewById(R.id.acc_nTextView);
            spicialityTextView = (TextView) view.findViewById(R.id.spicialityTextView);
             showloction= (TextView) view.findViewById(R.id.showloction);
            categTextView = (TextView) view.findViewById(R.id.categTextView);


            //Log.e("", "ViewHolder const");

        }
    }



}