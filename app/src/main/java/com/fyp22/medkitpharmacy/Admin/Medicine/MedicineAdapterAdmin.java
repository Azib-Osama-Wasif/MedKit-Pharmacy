package com.fyp22.medkitpharmacy.Admin.Medicine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import androidx.cardview.widget.CardView;

import java.util.List;

public class MedicineAdapterAdmin extends RecyclerView.Adapter<MedicineAdapterAdmin.MyViewHolder> {

    Context context;
    List<Medicine> medicineList;
    Customer customer;
    DatabaseHandler databaseHandler;

    public MedicineAdapterAdmin(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
    }

    @NonNull
    @Override
    public MedicineAdapterAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_admin_list_item, parent, false);
        MedicineAdapterAdmin.MyViewHolder myViewHolder = new MedicineAdapterAdmin.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapterAdmin.MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getName());
        holder.serial.setText(Integer.toString(position+1));
        holder.price.setText(Integer.toString(medicine.getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, MedicineDetailAdmin.class);
                    intent.putExtra(Utilities.intent_medicine, medicine);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            serial = itemView.findViewById(R.id.serial);
            price = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}