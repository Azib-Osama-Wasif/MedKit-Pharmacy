package com.fyp22.medkitpharmacy.Pharmacy.Orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveredOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;
    Context context;

    Pharmacy pharmacy;
    RecyclerView recyclerView;
    List<Order> orderList;
    List<Medicine> medicineList;
    OrdersAdapterPharmacy adapter;

    DatabaseHandler databaseHandler;

    public DeliveredOrdersFragment(Pharmacy pharmacy, Context context) {
        this.pharmacy = pharmacy;
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler= new DatabaseHandler(context);
        orderList= new ArrayList<>();
        medicineList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);

        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        recyclerView=view.findViewById(R.id.delivered_orders_recycler_view);
        adapter= new OrdersAdapterPharmacy(this.getContext(), pharmacy, orderList, medicineList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if(order.getPharmacyId().equals(pharmacy.getId()) && order.getStatus().equals(Utilities.order_complete)){
                        orderList.add(order);
                        rowCount++;
                        adapter.notifyDataSetChanged();

                        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot2: snapshot.getChildren()) {
                                        Medicine product = snapshot2.getValue(Medicine.class);
                                        if(product.getId().equals(order.getMedicineId())
                                                && order.getPharmacyId().equals(pharmacy.getId())
                                                && (order.getStatus().equals(Utilities.order_complete))) {
                                            medicineList.add(product);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                if(rowCount==0){
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}