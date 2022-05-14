package com.fyp22.medkitpharmacy.Pharmacy.Medicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.ViewMedicinePharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UpdateStockDialogFragment extends DialogFragment {

    NumberPicker numberPicker;
    TextView available;
    Button save;

    Pharmacy pharmacy;
    Medicine medicine;
    int availability;
    Bundle bundle;
    DatabaseHandler databaseHandler;
    public UpdateStockDialogFragment() {
        databaseHandler = new DatabaseHandler(getContext());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_stock, container, false);

        pharmacy = (Pharmacy) bundle.getSerializable(Utilities.intent_pharmacy);
        medicine = (Medicine) bundle.getSerializable(Utilities.intent_medicine);
        availability = bundle.getInt(Utilities.intent_availability);

        available=view.findViewById(R.id.total);
        numberPicker = view.findViewById(R.id.number_picker);
        save =view.findViewById(R.id.save);

        available.setText(Integer.toString(availability));
        numberPicker.setValue(availability);
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                available.setText(Integer.toString(numberPicker.getValue()));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHandler.getPharmacyReference().child(pharmacy.getId()).child("stockItemList").child(medicine.getId()).child("availableAmount").setValue(numberPicker.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), ViewMedicinePharmacy.class);
                        startActivity(intent);
                    }
                });
            }
        });
        return view;
    }
}