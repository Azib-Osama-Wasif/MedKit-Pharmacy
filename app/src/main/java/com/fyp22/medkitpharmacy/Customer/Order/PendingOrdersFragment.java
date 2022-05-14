package com.fyp22.medkitpharmacy.Customer.Order;

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
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    RecyclerView recyclerView;
    List<Order> orderList;
    List<Medicine> medicineList;
    OrdersAdapter adapter;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public PendingOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler= new DatabaseHandler(getActivity().getApplicationContext());
        mcp= new MyCustomerPreferences(getActivity().getApplicationContext());
        orderList= new ArrayList<>();
        medicineList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        recyclerView=view.findViewById(R.id.pending_orders_recycler_view);
        adapter= new OrdersAdapter(this.getContext(), orderList, medicineList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if(order.getCustomerId().equals(mcp.getCustomer().getId()) && (order.getStatus().equals(Utilities.order_pending) || order.getStatus().equals(Utilities.order_dispatched))){
                        orderList.add(order);
                        rowCount++;

                        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot2: dataSnapshot.getChildren()) {
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            Medicine medicine = snapshot3.getValue(Medicine.class);
                                            if (medicine.getId().equals(order.getMedicineId())
                                                    && order.getCustomerId().equals(mcp.getCustomer().getId())
                                                    && (order.getStatus().equals(Utilities.order_pending) || order.getStatus().equals(Utilities.order_dispatched))) {
                                                medicineList.add(medicine);
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
                if(rowCount==0){
                    empty.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}