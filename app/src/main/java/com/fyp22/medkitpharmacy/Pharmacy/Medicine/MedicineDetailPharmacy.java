
package com.fyp22.medkitpharmacy.Pharmacy.Medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.StockDialogueFragment;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.StockInputListener;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.ViewMedicinePharmacy;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicineDetailPharmacy extends AppCompatActivity implements StockInputListener {

    CircleImageView imageView;
    TextView name;
    TextView name2;
    TextView price;
    TextView type;
    TextView description;
    TextView available;
    TextView editBtn;
    ImageView deleteBtn;

    Intent intent;
    Medicine medicine;
    StockItem stockItem;
    int availability;
    DatabaseHandler databaseHandler;
    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail_pharmacy);
        getSupportActionBar().setTitle("Medicine Detail");
        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.name_val);
        name2=findViewById(R.id.name_val2);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        type =findViewById(R.id.type_val);
        editBtn =findViewById(R.id.edit_button);
        available=findViewById(R.id.amount_available);
        deleteBtn =findViewById(R.id.delete_button);

        intent=getIntent();
        mphp= new MyPharmacyPreferences(this);
        pharmacy=mphp.getPharmacy();

        medicine = (Medicine) intent.getSerializableExtra(Utilities.intent_medicine);
        stockItem = (StockItem) intent.getSerializableExtra(Utilities.intent_stock_item);
        availability = stockItem.getAvailableAmount();
        databaseHandler= new DatabaseHandler(this);

        Picasso.get().load(medicine.getImage()).into(imageView);
        name.setText(medicine.getName());
        name2.setText(medicine.getName());
        type.setText(medicine.getType());
        description.setText(medicine.getDescription());
        price.setText(Integer.toString(medicine.getPrice()));
        available.setText("Total Available: "+Integer.toString(availability));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockDialogueFragment dialogueFragment = new StockDialogueFragment(MedicineDetailPharmacy.this);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utilities.intent_stock_item, stockItem);
                dialogueFragment.setArguments(bundle);
                dialogueFragment.show(getSupportFragmentManager(),"");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MedicineDetailPharmacy.this)
                        .setTitle("Confirm Operation")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                // Continue with delete operation
                                databaseHandler.getStockItemsReference().child(stockItem.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MedicineDetailPharmacy.this, "DELETION SUCCESSFUL", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MedicineDetailPharmacy.this, ViewMedicinePharmacy.class));
                                        }
                                        else{
                                            Toast.makeText(MedicineDetailPharmacy.this, "DELETION FAILED", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MedicineDetailPharmacy.this, ViewMedicinePharmacy.class));
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        availability = Integer.parseInt(inputText);
        available.setText("Total Available: "+Integer.toString(availability));
    }
}