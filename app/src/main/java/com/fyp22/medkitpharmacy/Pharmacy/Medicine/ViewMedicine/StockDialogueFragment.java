package com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.MedicineDetailPharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class StockDialogueFragment extends DialogFragment{

    private StockInputListener listener;

    Context context;
    TextInputEditText stockET;
    Button submit;
    ProgressBar progressBar;
    StockItem stockItem;

    DatabaseHandler databaseHandler;
    MyPharmacyPreferences pharmacyPreferences;
    Pharmacy pharmacy;

    public StockDialogueFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockItem = (StockItem) getArguments().getSerializable(Utilities.intent_stock_item);
        databaseHandler= new DatabaseHandler(context);
        pharmacyPreferences = new MyPharmacyPreferences(context);
        pharmacy = pharmacyPreferences.getPharmacy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_input, container, false);
        stockET =view.findViewById(R.id.stockET);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        stockET.setText(Integer.toString(stockItem.getAvailableAmount()));
        submit=view.findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stockET.getText().equals("") || stockET.getText().equals("0")){
                    Toast.makeText(context, "Please Enter a Value", Toast.LENGTH_LONG).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    stockItem.setAvailableAmount(Integer.parseInt(stockET.getText().toString()));
                    System.out.println("STOCK ITEM ID:"+stockItem.getId());
                    databaseHandler.getStockItemsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(snapshot.getKey().equals(stockItem.getId())){
                                    databaseHandler.getStockItemsReference().child(snapshot.getKey()).setValue(stockItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(context, ViewMedicinePharmacy.class);
                                            context.startActivity(intent);
                                        }
                                    });
                                    listener.onFinishEditDialog(stockET.getText().toString());
                                    dismiss();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (StockInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement DialogListener");
        }
    }
}