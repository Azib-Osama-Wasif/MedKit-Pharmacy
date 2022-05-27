package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddToStockAdapter extends RecyclerView.Adapter<AddToStockAdapter.MyViewHolder> {

    Context context;
    FragmentManager fragmentManager;
    ArrayList<Medicine> medicineList;
    List<StockItem> stockItemList;
    Pharmacy pharmacy;
    DatabaseHandler databaseHandler;

    public AddToStockAdapter(Context context, FragmentManager fragmentManager, ArrayList<Medicine> medicineList, List<StockItem> stockItemList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.medicineList = medicineList;
        this.stockItemList = stockItemList;
        this.databaseHandler = new DatabaseHandler(context);
        this.pharmacy = MyPharmacyPreferences.getPharmacy();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_medicine_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Medicine medicine = medicineList.get(position);
        StockItem stockItem = stockItemList.get(position);
        holder.name.setText(medicine.getName());
        holder.price.setText("Rs."+Integer.toString(medicine.getPrice()));
        holder.quantity.setText(Integer.toString(stockItem.getAvailableAmount()));
        Picasso.get().load(medicine.getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    QuantityDialogueFragment dialogueFragment = new QuantityDialogueFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Utilities.intent_quantity, Integer.toString(stockItem.getAvailableAmount()));
                    bundle.putString(Utilities.intent_index, Integer.toString(position));
                    dialogueFragment.setArguments(bundle);
                    dialogueFragment.show(fragmentManager,"");
                } catch (Exception e) {
                    Utilities.createAlertDialog(context, "Error", e.getLocalizedMessage());
                }
            }
        });

        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantityDialogueFragment dialogueFragment = new QuantityDialogueFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Utilities.intent_quantity, Integer.toString(stockItem.getAvailableAmount()));
                bundle.putString(Utilities.intent_index, Integer.toString(position));
                dialogueFragment.setArguments(bundle);
                dialogueFragment.show(fragmentManager,"");
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = stockItem.getAvailableAmount();
                stockItem.setAvailableAmount(++value);
                holder.quantity.setText(Integer.toString(stockItem.getAvailableAmount()));
                notifyDataSetChanged();
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stockItem.getAvailableAmount() > 1){
                    int value = stockItem.getAvailableAmount();
                    stockItem.setAvailableAmount(--value);
                    holder.quantity.setText(Integer.toString(stockItem.getAvailableAmount()));
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView price;
        ImageView image;
        TextView quantity;
        ImageView plus;
        ImageView minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_name);
            price = itemView.findViewById(R.id.medicine_price);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.quantity);
            quantity=itemView.findViewById(R.id.quantity_val);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
        }
    }
}
