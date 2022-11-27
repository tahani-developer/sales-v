package com.dr7.salesmanmanager.Reports;

import android.content.Context;
import android.graphics.Color;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.dr7.salesmanmanager.R.color.layer4;


public class ListInventoryAdapter extends RecyclerView.Adapter<ListInventoryAdapter.ViewHolder>
{
    static List<inventoryReportItem> inventorylist;

    Context context;
  static int  totalQty_inventory=0;

    DatabaseHandler MHandler;

    public ListInventoryAdapter(List<inventoryReportItem> inventorylist, Context context) {
        this.inventorylist = inventorylist;
        this.context = context;
        MHandler =new DatabaseHandler(context);
        Log.e("","constructor adapter");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inventory_report, parent, false);
        Log.e("","onCreateViewHolder");
        return new ViewHolder(view);

    }
    public String[] mColors = {"#F3F8F8F7","#CFD8DC"};
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.setIsRecyclable(false);
        holder.itemName.setText(inventorylist.get(holder.getAdapterPosition()).getName());
        holder.itemNumber.setText(inventorylist.get(holder.getAdapterPosition()).getItemNo());
        holder.quantity.setText(inventorylist.get(holder.getAdapterPosition()).getQty()+"");
      //  holder.linearLayout.setBackgroundColor(android.R.color.holo_red_light));
        holder.linearLayout.setBackgroundColor(Color.parseColor(mColors[position % 2]));
        holder.linearLayout.setPadding(5 , 10, 5, 10);
//try {
//
//    List<ItemUnitDetails>detailsList=MHandler.getItemUnits(inventorylist.get(holder.getAdapterPosition()).getItemNo());
//    Log.e("detailsList",detailsList.size()+"");
//}catch (Exception e)
//{
//    Log.e("Exception--",e.getMessage()+"");
//}


        Log.e("","onBind");
        if(MainActivity.UNITFLAGE==1)// not enabled units
        {

            holder.unit_price.setVisibility(View.VISIBLE);
            holder.unit_name.setVisibility(View.VISIBLE);
            holder.unit_name.setText(inventorylist.get(holder.getAdapterPosition()).getUNITname());
            holder.unit_price.setText(inventorylist.get(holder.getAdapterPosition()).getUNITprice());
        }
        else
        {

            holder.unit_price.setVisibility(View.GONE);
            holder.unit_name.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {

        return inventorylist.size();
//
    }
    public  static int  TotalQtyInventoey()
    {
        for(int i=0;i<inventorylist.size();i++)
        {
            totalQty_inventory+=inventorylist.get(i).getQty();

        }
        return totalQty_inventory;

    }

    public  class  ViewHolder extends  RecyclerView.ViewHolder
   {
       LinearLayout linearLayout;

       TextView itemNumber, itemName,quantity,unit_name,unit_price;
       public ViewHolder(View itemView)
       {
           super(itemView);

           linearLayout = itemView.findViewById(R.id.liner_inventory);
           itemName = itemView.findViewById(R.id.Item_Name);
           itemNumber = itemView.findViewById(R.id.Item_number);
           quantity = itemView.findViewById(R.id.Quantity);
           unit_name =itemView.findViewById(R.id.unit_name);
           unit_price= itemView.findViewById(R.id.unit_price);
           Log.e("","ViewHolder const");

       }
   }




}
