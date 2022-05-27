package com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.MedicineDetailPharmacy;
import com.fyp22.medkitpharmacy.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapterPharmacy extends RecyclerView.Adapter<MedicineAdapterPharmacy.MyViewHolder> {

    Context context;
    List<Medicine> medicineList;
    List<StockItem> stockItemList;
    Customer customer;
    DatabaseHandler databaseHandler;

    public MedicineAdapterPharmacy(Context context, List<Medicine> medicineList, List<StockItem> stockItemList) {
        this.context = context;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
        this.stockItemList = stockItemList;
    }

    @NonNull
    @Override
    public MedicineAdapterPharmacy.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_pharmacy_list_item, parent, false);
        MedicineAdapterPharmacy.MyViewHolder myViewHolder = new MedicineAdapterPharmacy.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapterPharmacy.MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(holder.getAdapterPosition());
        StockItem stockItem = stockItemList.get(holder.getAdapterPosition());

        holder.name.setText(medicine.getName());
        holder.serial.setText(Integer.toString(position+1));
        holder.price.setText("Rs."+Integer.toString(medicine.getPrice()));
        holder.inStock.setText(Integer.toString(stockItem.getAvailableAmount()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, MedicineDetailPharmacy.class);
                    intent.putExtra(Utilities.intent_medicine, (Serializable)medicine);
                    intent.putExtra(Utilities.intent_stock_item, (Serializable) stockItemList.get(position));

                    context.startActivity(intent);
                } catch (Exception e) {
                    Utilities.createAlertDialog(context, "Error", e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void setFilter(List<Medicine> newList, List<StockItem> stockItemList){
        medicineList = new ArrayList<>();
        stockItemList = new ArrayList<>();
        medicineList.addAll(newList);
        stockItemList.addAll(stockItemList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView serial;
        TextView price;
        TextView inStock;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            serial = itemView.findViewById(R.id.serial);
            price = itemView.findViewById(R.id.price);
            inStock = itemView.findViewById(R.id.available);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}