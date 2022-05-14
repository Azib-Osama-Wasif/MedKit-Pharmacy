package com.fyp22.medkitpharmacy.Pharmacy.Medicine.SelectMedicine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.MedicineDetailPharmacy;
import com.fyp22.medkitpharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class SelectMedicineAdapter extends RecyclerView.Adapter<SelectMedicineAdapter.MyViewHolder> {

    Context context;
    List<Medicine> medicineList;
    List<StockItem> selectedItemList;
    SelectListener selectListener;
    Customer customer;
    DatabaseHandler databaseHandler;

    public SelectMedicineAdapter(Context context, List<Medicine> medicineList, SelectListener selectListener) {
        this.context = context;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
        this.selectListener=selectListener;
        this.selectedItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SelectMedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_medicine_list_item, parent, false);
        SelectMedicineAdapter.MyViewHolder myViewHolder = new SelectMedicineAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMedicineAdapter.MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getName());
        holder.serial.setText(Integer.toString(position+1));
        holder.price.setText("Rs."+Integer.toString(medicine.getPrice()));

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent = new Intent(context, MedicineDetailPharmacy.class);
//                    intent.putExtra(Utilities.intent_medicine, medicine);
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    Utilities.createAlertDialog(context, "Error", e.getLocalizedMessage());
//                }
//            }
//        });


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    StockItem stockItem = new StockItem();
                    stockItem.setMedicineId(medicine.getId());
                    stockItem.setPharmacyId(MyPharmacyPreferences.getPharmacy().getId());
                    stockItem.setAvailableAmount(Integer.parseInt(holder.available.getText().toString()));

                    selectedItemList.add(stockItem);
                }
                else{
                    StockItem stockItem = new StockItem();
                    stockItem.setMedicineId(medicine.getId());
                    stockItem.setPharmacyId(MyPharmacyPreferences.getPharmacy().getId());
                    stockItem.setAvailableAmount(Integer.parseInt(holder.available.getText().toString()));


                    if(selectedItemList.contains(stockItem)){
                        selectedItemList.remove(stockItem);
                        Toast.makeText(context, "REMOVED", Toast.LENGTH_SHORT).show();
                    }
                }

                selectListener.onSelect(selectedItemList);
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
        CheckBox checkBox;
        EditText available;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            serial = itemView.findViewById(R.id.serial);
            price = itemView.findViewById(R.id.price);
            checkBox= itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
            available = itemView.findViewById(R.id.amount_available);
        }
    }
}
