package com.fyp22.medkitpharmacy.Customer.Medicine;

import static com.fyp22.medkitpharmacy.Common.Utilities.SAVE;
import static com.fyp22.medkitpharmacy.Common.Utilities.SAVED;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.CartItem;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicineDetail extends AppCompatActivity {

    CircleImageView imageView;
    TextView name;
    TextView name2;
    TextView price;
    TextView type;
    TextView description;
    TextView available;
    ImageView saveBtn;

    Intent intent;
    Medicine medicine;
    StockItem stockItem;
    String savedStatus;
    DatabaseHandler databaseHandler;
    MyCustomerPreferences mphp;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail2);
        getSupportActionBar().setTitle("Medicine Detail");
        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.name_val);
        name2=findViewById(R.id.name_val2);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        type =findViewById(R.id.type_val);
        saveBtn =findViewById(R.id.save_button);
        available=findViewById(R.id.amount_available);

        intent=getIntent();
        mphp= new MyCustomerPreferences(this);
        customer =mphp.getCustomer();

        medicine = (Medicine) intent.getSerializableExtra(Utilities.intent_medicine);
        stockItem = (StockItem) intent.getSerializableExtra(Utilities.intent_stock_item);
        savedStatus=intent.getStringExtra(Utilities.intent_saved_status);
        databaseHandler= new DatabaseHandler(this);

        Picasso.get().load(medicine.getImage()).into(imageView);
        name.setText(medicine.getName());
        name2.setText(medicine.getName());
        type.setText(medicine.getType());
        description.setText(medicine.getDescription());
        price.setText(Integer.toString(medicine.getPrice()));

        if(savedStatus.equals(SAVED)){
            saveBtn.setTag(SAVED);
            saveBtn.setImageResource(R.drawable.ic_saved);
        }else if(savedStatus.equals(SAVE)){
            saveBtn.setTag(SAVE);
            saveBtn.setImageResource(R.drawable.ic_save);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(saveBtn.getTag()).equals(Utilities.SAVE)) {
                    databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot cartSnapshot) {

                            for (DataSnapshot snapshot : cartSnapshot.getChildren()) {
                                CartItem cartItemSnap = snapshot.getValue(CartItem.class);
                                if (cartItemSnap.getStockItemId().equals(medicine.getId()) && cartItemSnap.getPharmacyId().equals(stockItem.getPharmacyId())) {
                                    Toast.makeText(MedicineDetail.this, "ALREADY IN CART!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                            CartItem cartItem = new CartItem();
                            cartItem.setStockItemId(stockItem.getMedicineId());
                            cartItem.setCustomerId(MyCustomerPreferences.getCustomer().getId());
                            cartItem.setPharmacyId(stockItem.getPharmacyId());
                            databaseHandler.getCartReference().push().setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseHandler.getCartReference().addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                CartItem cartItem1 = snapshot.getValue(CartItem.class);
                                                cartItem1.setId(snapshot.getKey());
                                                databaseHandler.getCartReference().child(cartItem1.getId()).setValue(cartItem1);
                                                Toast.makeText(MedicineDetail.this, "ADDED TO CART!", Toast.LENGTH_LONG).show();
                                                saveBtn.setImageResource(R.drawable.ic_saved);
                                                saveBtn.setTag(Utilities.SAVED);
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Utilities.createAlertDialog(MedicineDetail.this, Utilities.FAILED, task.getException().getLocalizedMessage().toString());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (String.valueOf(saveBtn.getTag()).equals(Utilities.SAVED)) {
                    databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CartItem cartItem = snapshot.getValue(CartItem.class);
                                if (cartItem.getCustomerId().equals(customer.getId()) && cartItem.getStockItemId().equals(medicine.getId()) && cartItem.getPharmacyId().equals(stockItem.getPharmacyId())) {
                                    databaseHandler.getCartReference().child(cartItem.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MedicineDetail.this, "REMOVED FROM CART!", Toast.LENGTH_LONG).show();
                                                saveBtn.setImageResource(R.drawable.ic_save);
                                                saveBtn.setTag(Utilities.SAVE);
                                            } else {
                                                Utilities.createAlertDialog(MedicineDetail.this, Utilities.FAILED, task.getException().getLocalizedMessage().toString());
                                            }
                                        }
                                    });
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
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MedicineDetail.this, ViewMedicine.class));
    }
}