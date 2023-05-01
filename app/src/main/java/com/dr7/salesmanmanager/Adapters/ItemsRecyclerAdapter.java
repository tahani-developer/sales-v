package com.dr7.salesmanmanager.Adapters;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
   private static final int VIEW_TYPE_LOADING = 0;
   private static final int VIEW_TYPE_NORMAL = 1;
   private boolean isLoaderVisible = false;
   private List<Item> items;
   public ItemsRecyclerAdapter(List<Item> list) {
      this.items = list;
   }
   @NonNull
   @Override
   public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      switch (viewType) {
         case VIEW_TYPE_NORMAL:
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_listview, parent, false));
         case VIEW_TYPE_LOADING:
            return new ProgressHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
         default:
            return null;
      }
   }
   @Override
   public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

       Log.e("onBindViewHolder","onBindViewHolder");
      holder.onBind(position);
   }
   @Override
   public int getItemViewType(int position) {
      if (isLoaderVisible) {
         return position == items.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
      } else {
         return VIEW_TYPE_NORMAL;
      }
   }
   @Override
   public int getItemCount() {
      return items == null ? 0 : items.size();
   }
   public void addItems(List<Item> postItems) {
      items.addAll(postItems);
      notifyDataSetChanged();
   }
   public void addLoading() {
      isLoaderVisible = true;
items.add(new Item());
      Log.e("addLoading","addLoading"+items.size());
      notifyItemInserted(items.size() - 1);
       Log.e("addLoading2","addLoading"+items.size());
   }
   public void removeLoading() {
       Log.e("removeLoading","removeLoading"+items.size());
      isLoaderVisible = false;
      int position = items.size() - 1;
   Item item = getItem(position);
      if (item != null) {
         items.remove(position);
         notifyItemRemoved(position);
      }
   }
   public void clear() {
       items.clear();
      notifyDataSetChanged();
   }
  Item getItem(int position) {
      return items.get(position);
   }
   public class ViewHolder extends BaseViewHolder {
       @BindView(R.id.textViewItemNumber)
       TextView itemNumber;
       @BindView(R.id.textViewItemName)
       TextView itemName;
      ViewHolder(View itemView) {
         super(itemView);
         ButterKnife.bind(this, itemView);
      }
      protected void clear() {
      }
      public void onBind(int position) {
         super.onBind(position);
          Log.e("ItemsRecyclerAdapter,onBind","onBind");
        Item item =items.get(position);




          itemNumber.setText(items.get(position).getItemNo());
       itemName.setText(items.get(position).getItemName());

          Log.e("ItemsRecyclerAdapter22,onBind","onBind");
      }
   }
   public class ProgressHolder extends BaseViewHolder {
      ProgressHolder(View itemView) {
         super(itemView);
         ButterKnife.bind(this, itemView);
      }
      @Override
      protected void clear() {
      }
   }
}
