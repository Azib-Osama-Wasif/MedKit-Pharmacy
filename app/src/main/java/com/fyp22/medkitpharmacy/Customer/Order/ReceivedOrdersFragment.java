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

public class ReceivedOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    RecyclerView recyclerView;
    List<Order> orderList;
    List<Medicine> medicineList;
    OrdersAdapter adapter;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public ReceivedOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler= new DatabaseHandler(this.getContext());
        mcp= new MyCustomerPreferences(this.getContext());
        orderList= new ArrayList<>();
        medicineList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recieved_orders, container, false);
        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        recyclerView=view.findViewById(R.id.received_orders_recycler_view);
        adapter= new OrdersAdapter(this.getContext(), orderList, medicineList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if(order.getCustomerId().equals(mcp.getCustomer().getId())
                            && order.getStatus().equals(Utilities.order_complete)){
                        orderList.add(order);
                        rowCount++;
                    }
                }

                databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                            for (DataSnapshot snapshot2: snapshot.getChildren()) {

                                Medicine product = snapshot2.getValue(Medicine.class);

                                for (Order order : orderList) {
                                    if(product.getId().equals(order.getMedicineId())
                                            && order.getCustomerId().equals(mcp.getCustomer().getId())
                                            && order.getStatus().equals(Utilities.order_complete)){
                                        medicineList.add(product);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        if(rowCount==0){
                            empty.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}