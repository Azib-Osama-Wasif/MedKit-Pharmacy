package com.fyp22.medkitpharmacy.Customer.Medicine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.CartItem;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    Context context;
    List<StockItem> stockItemList;
    List<Medicine> medicineList;
    Customer customer;
    DatabaseHandler databaseHandler;

    public MedicineAdapter(Context context, List<Medicine> medicineList, List<StockItem> stockItemList) {
        this.context = context;
        this.stockItemList = stockItemList;
        this.medicineList = medicineList;
        this.databaseHandler = new DatabaseHandler(context);
        this.customer = MyCustomerPreferences.getCustomer();
    }

    @NonNull
    @Override
    public MedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_customer_list_item, parent, false);
        MedicineAdapter.MyViewHolder myViewHolder = new MedicineAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        StockItem stockItem = stockItemList.get(position);
        holder.name.setText(medicine.getName());
        holder.pharmacy_name.setText(stockItemList.get(position).getPharmacyId());
        Picasso.get().load(medicine.getImage()).into(holder.imageView);

        databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem.getStockItemId().equals(medicine.getId()) &&
                                    cartItem.getPharmacyId().equals(stockItem.getPharmacyId()) &&
                                    cartItem.getCustomerId().equals(customer.getId())
                    ) {
                        holder.saveBtn.setImageResource(R.drawable.ic_saved);
                        holder.saveBtn.setTag(Utilities.SAVED);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Intent intent = new Intent(context, MedicineDetail.class);
                    intent.putExtra(Utilities.intent_medicine, (Serializable)medicine);
                    intent.putExtra(Utilities.intent_stock_item, stockItem);
                    intent.putExtra(Utilities.intent_saved_status, holder.saveBtn.getTag().toString());
                    intent.putExtra(Utilities.activity_name, context.getClass().getSimpleName());
                    context.startActivity(intent);
                } catch (Exception e) {
                    Utilities.createAlertDialog(context, "Error", e.getLocalizedMessage());
                }
            }
        });

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "CLICKED "+ holder.saveBtn.getTag().toString(), Toast.LENGTH_SHORT).show();
                if (String.valueOf(holder.saveBtn.getTag()).equals(Utilities.SAVE)) {
                    databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot cartSnapshot) {
                            for (DataSnapshot snapshot : cartSnapshot.getChildren()) {
                                CartItem cartItemSnap = snapshot.getValue(CartItem.class);
                                if (cartItemSnap.getStockItemId().equals(medicine.getId()) && cartItemSnap.getPharmacyId().equals(stockItem.getPharmacyId())) {
                                    Toast.makeText(context, "ALREADY IN CART!", Toast.LENGTH_LONG).show();
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
                                                Toast.makeText(context, "ADDED TO CART!", Toast.LENGTH_LONG).show();
                                                holder.saveBtn.setImageResource(R.drawable.ic_saved);
                                                holder.saveBtn.setTag(Utilities.SAVED);
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
                                        Utilities.createAlertDialog(context, Utilities.FAILED, task.getException().getLocalizedMessage().toString());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (String.valueOf(holder.saveBtn.getTag()).equals(Utilities.SAVED)) {
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
                                                Toast.makeText(context, "REMOVED FROM CART!", Toast.LENGTH_LONG).show();
                                                holder.saveBtn.setImageResource(R.drawable.ic_save);
                                                holder.saveBtn.setTag(Utilities.SAVE);
                                            } else {
                                                Utilities.createAlertDialog(context, Utilities.FAILED, task.getException().getLocalizedMessage().toString());
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
    public int getItemCount() {
        return medicineList.size();
    }

    public void setFilter(List<Medicine> newList){
        medicineList = new ArrayList<>();
        medicineList.addAll(newList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView name;
        TextView pharmacy_name;
        ImageView imageView;
        ImageView saveBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            pharmacy_name = itemView.findViewById(R.id.pharmacy_name);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
            saveBtn = itemView.findViewById(R.id.saveBtn);
        }
    }
}
