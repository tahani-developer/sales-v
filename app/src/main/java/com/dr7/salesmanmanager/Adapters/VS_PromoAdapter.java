package com.dr7.salesmanmanager.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class VS_PromoAdapter extends RecyclerView.Adapter<VS_PromoAdapter.ViewHolder> {
   private List<Offers> offersList=new ArrayList<>();
   Context context;

   public VS_PromoAdapter(List<Offers> offersList, Context context) {
      this.offersList = offersList;
      this.context = context;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vs_promorow, parent, false);
      return new VS_PromoAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       try {
           if(offersList.get(position).getPromotionType()==0)
               holder.PROMOTION_TYPE.setText(context.getResources().getString(R.string.app_bonus));
           else{
               holder.PROMOTION_TYPE.setText(context.getResources().getString(R.string.appDiscount));
               if(offersList.get(position).getDiscountItemType()==0)
                   holder.DISCOUNT_ITEM_TYPE.setText("خصم قيمة");
               else
                   holder.DISCOUNT_ITEM_TYPE.setText("خصم نسبة");

           }
       }catch (Exception exception){

       }
       holder.PROMOTION_ID.setText(offersList.get(position).getPromotionID()+"");

   //  holder.PROMOTION_TYPE.setText(offersList.get(position).getPromotionType()+"");
//
     holder.BONUS_ITEM_NO.setText(offersList.get(position).getBonusItemNo());
     holder.BONUS_QTY.setText(offersList.get(position).getBonusQty()+"");
     holder.item_number.setText(offersList.get(position).getItemNo());
      holder.ITEM_QTY.setText(offersList.get(position).getItemQty()+"");
//      holder.DISCOUNT_ITEM_TYPE.setText(offersList.get(position).getDiscountItemType());

      holder.FROM_DATE.setText(offersList.get(position).getFromDate());
      holder.TO_DATE.setText(offersList.get(position).getToDate());


   }

   @Override
   public int getItemCount() {
      return offersList.size();
   }

   class ViewHolder extends RecyclerView.ViewHolder {
      TextView PROMOTION_ID,PROMOTION_TYPE,FROM_DATE,TO_DATE,DISCOUNT_ITEM_TYPE,BONUS_ITEM_NO,BONUS_QTY
              ,ITEM_QTY,item_number;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);
         PROMOTION_ID=itemView.findViewById(R.id.PROMOTION_ID);
         PROMOTION_TYPE=itemView.findViewById(R.id.PROMOTION_TYPE);
         FROM_DATE=itemView.findViewById(R.id.FROM_DATE);
         TO_DATE=itemView.findViewById(R.id.TO_DATE);
         DISCOUNT_ITEM_TYPE=itemView.findViewById(R.id.DISCOUNT_ITEM_TYPE);
         BONUS_ITEM_NO=itemView.findViewById(R.id.BONUS_ITEM_NO);
         BONUS_QTY=itemView.findViewById(R.id.BONUS_QTY);
         ITEM_QTY=itemView.findViewById(R.id.ITEM_QTY);
         item_number=itemView.findViewById(R.id.item_number);
      }
   }
}
