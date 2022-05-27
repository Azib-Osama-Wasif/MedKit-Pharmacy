package com.fyp22.medkitpharmacy.Customer.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.CartItem;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    TextView empty;
    ProgressBar progressBar;

    public static TextView total;
    Button completeBtn;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    CartItemAdapter adapter;
    List<Medicine> medicineList;
    List<CartItem> cartItemList;

    RecyclerView recyclerView;

    int totalAmount=0;
    public int itemCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.cart_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        empty=findViewById(R.id.empty);
        total=findViewById(R.id.total_val);
        completeBtn =findViewById(R.id.complete_sale_button);

        databaseHandler=new DatabaseHandler(this);
        mcp = new MyCustomerPreferences(this);

        medicineList = new ArrayList<>();
        cartItemList = new ArrayList<>();

        adapter = new CartItemAdapter(this, medicineList, cartItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empty.setVisibility(View.INVISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if(cartItem.getCustomerId().equals(mcp.getCustomer().getId())){
                        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                                        Medicine medicine = snapshot2.getValue(Medicine.class);
                                        if(medicine.getId().equals(cartItem.getStockItemId())){
                                            medicineList.add(medicine);
                                            cartItemList.add(cartItem);
                                            System.out.println("CART ITEM:"+cartItem.getId());

                                            itemCount++;
                                            totalAmount += medicine.getPrice();
                                            total.setText(Integer.toString(totalAmount));
                                            adapter.notifyDataSetChanged();
                                            if(medicineList.size()==0){
                                                empty.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Utilities.createAlertDialog(CartActivity.this, "Error", error.getMessage().toString());
                            }
                        });
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Utilities.createAlertDialog(CartActivity.this, "Error", error.getMessage().toString());
            }
        });


        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}