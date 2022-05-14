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
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.MedicineDetailPharmacy;
import com.fyp22.medkitpharmacy.R;

import java.util.List;

public class MedicineAdapterPharmacy extends RecyclerView.Adapter<MedicineAdapterPharmacy.MyViewHolder> {

    Context context;
    List<Medicine> medicineList;
    List<Integer> stockAvailablity;
    Customer customer;
    DatabaseHandler databaseHandler;

    public MedicineAdapterPharmacy(Context context, List<Medicine> medicineList, List<Integer> stockAvailability) {
        this.context = context;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
        this.stockAvailablity=stockAvailability;
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
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getName());
        holder.serial.setText(Integer.toString(position+1));
        holder.price.setText(Integer.toString(medicine.getPrice()));
        holder.available.setText(Integer.toString(stockAvailablity.get(position)));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, MedicineDetailPharmacy.class);
                    intent.putExtra(Utilities.intent_medicine, medicine);
                    intent.putExtra(Utilities.intent_availability, stockAvailablity.get(position));
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView serial;
        TextView price;
        TextView available;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            serial = itemView.findViewById(R.id.serial);
            price = itemView.findViewById(R.id.price);
            available = itemView.findViewById(R.id.available);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}