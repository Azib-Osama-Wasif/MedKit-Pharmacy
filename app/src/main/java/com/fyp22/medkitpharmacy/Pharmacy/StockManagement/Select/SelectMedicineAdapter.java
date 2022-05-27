package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Select;

import static com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Select.SelectMedicine.totalSelected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectMedicineAdapter extends RecyclerView.Adapter<SelectMedicineAdapter.MyViewHolder> {

    Context context;
    FragmentManager fragmentManager;
    List<Medicine> medicineList;
    List<Medicine> selectedMedicine;
    Pharmacy pharmacy;
    DatabaseHandler databaseHandler;


    public SelectMedicineAdapter(Context context, FragmentManager fragmentManager, List<Medicine> medicineList, List<Medicine> selectedMedicine) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.medicineList = medicineList;
        this.selectedMedicine = selectedMedicine;
        this.databaseHandler = new DatabaseHandler(context);
        this.pharmacy = MyPharmacyPreferences.getPharmacy();
    }

    @NonNull
    @Override
    public SelectMedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_medicine_list_item, parent, false);
        SelectMedicineAdapter.MyViewHolder myViewHolder = new SelectMedicineAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMedicineAdapter.MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getName());
        holder.manufacturer.setText(medicine.getManufacturer());
        holder.price.setText("Rs."+Integer.toString(medicine.getPrice()));
        Picasso.get().load(medicine.getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    StockDialogueFragment dialogueFragment = new StockDialogueFragment(context);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(Utilities.intent_stock_item, stockItem);
//                    dialogueFragment.setArguments(bundle);
//                    dialogueFragment.show(fragmentManager,"");
                } catch (Exception e) {
                    Utilities.createAlertDialog(context, "Error", e.getLocalizedMessage());
                }
            }
        });

        holder.addStockET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Medicine medicineX : selectedMedicine) {
                    if (medicineX.equals(medicine)){
                        Toast.makeText(context, "ALREADY SELECTED", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(context, "MEDICINE SELECTED", Toast.LENGTH_LONG).show();
                selectedMedicine.add(medicine);
                totalSelected.setText(Integer.toString(selectedMedicine.size()));
                notifyDataSetChanged();
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
        TextView manufacturer;
        TextView price;
        ImageView image;
        TextView addStockET;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_name);
            manufacturer = itemView.findViewById(R.id.manufacturerET);
            price = itemView.findViewById(R.id.medicine_price);
            image = itemView.findViewById(R.id.image);
            addStockET = itemView.findViewById(R.id.add_to_stock);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
