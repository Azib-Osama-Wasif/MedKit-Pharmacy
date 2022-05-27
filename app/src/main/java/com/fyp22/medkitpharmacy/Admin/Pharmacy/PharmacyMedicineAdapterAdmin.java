package com.fyp22.medkitpharmacy.Admin.Pharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Admin.Medicine.MedicineDetailAdmin;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Medicine.MedicineDetail;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class PharmacyMedicineAdapterAdmin extends RecyclerView.Adapter<PharmacyMedicineAdapterAdmin.MyViewHolder> {

    Context context;
    List<StockItem> stockItemList;
    List<Medicine> medicineList;
    Customer customer;
    DatabaseHandler databaseHandler;

    public PharmacyMedicineAdapterAdmin(Context context, List<Medicine> medicineList, List<StockItem> stockItemList) {
        this.context = context;
        this.stockItemList = stockItemList;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_madicine_admin_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        StockItem stockItem = stockItemList.get(position);
        holder.name.setText(medicine.getName());
        holder.pharmacy_name.setText(stockItemList.get(position).getPharmacyId());
        Picasso.get().load(medicine.getImage()).into(holder.imageView);


        databaseHandler.getPharmacyReference().child(stockItem.getPharmacyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.pharmacy_name.setText(snapshot.getValue(Pharmacy.class).getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    databaseHandler.getPharmacyReference().child(stockItem.getPharmacyId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Pharmacy pharmacy = snapshot.getValue(Pharmacy.class);
                            Intent intent = new Intent(context, MedicineDetailAdmin.class);
                            intent.putExtra(Utilities.intent_medicine, (Serializable)medicine);
                            intent.putExtra(Utilities.intent_pharmacy,pharmacy);
                            intent.putExtra(Utilities.intent_stock_item, stockItem);
                            intent.putExtra(Utilities.activity_name, context.getClass().getSimpleName());
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

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
        MaterialCardView cardView;
        TextView name;
        TextView pharmacy_name;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            pharmacy_name = itemView.findViewById(R.id.pharmacy_name);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
