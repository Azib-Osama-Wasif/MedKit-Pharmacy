package com.fyp22.medkitpharmacy.Admin.Complaints;

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

import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Models.Complaint;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerComplaintsFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    RecyclerView recyclerView;
    List<Complaint> complaintList;
    List<Customer> customerList;
    ComplaintsAdapter adapter;

    DatabaseHandler databaseHandler;
    MyAdminPreferences map;

    public CustomerComplaintsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler= new DatabaseHandler(getActivity().getApplicationContext());
        map = new MyAdminPreferences(getActivity().getApplicationContext());
        complaintList = new ArrayList<>();
        customerList = new ArrayList<>();
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
        adapter= new ComplaintsAdapter(this.getContext(), complaintList, customerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

//        databaseHandler.getComplaintsReference().addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
//                    Complaint order = snapshot.getValue(Complaint.class);
//                    System.out.println("ORDER CUST ID:"+order.getCustomerId());
//                    System.out.println("CUSTOMER ID:"+ map.getCustomer().getId());
//                    if(order.getCustomerId().equals(map.getCustomer().getId()) && order.getStatus().equals(Utilities.order_pending)){
//                        complaintList.add(order);
//                        rowCount++;
//                        System.out.println("MATCHED");
//
//                        databaseHandler.getCustomersReference().addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
//                                    for (DataSnapshot snapshot2: snapshot.getChildren()) {
//                                        Customer customer = snapshot2.getValue(Customer.class);
//                                        System.out.println("PRODUCT ID:"+customer.getId());
//                                        if(customer.getId().equals(order.getCustomerId())
//                                                && order.getCustomerId().equals(map.getA().getId())
//                                                && (order.getStatus().equals(Utilities.order_pending) || order.getStatus().equals(Utilities.order_dispatched))) {
//                                            customerList.add(customer);
//                                            System.out.println("MATCHED");
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                }
//                if(rowCount==0){
//                    empty.setVisibility(View.VISIBLE);
//                }
//                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.INVISIBLE);
//
////                databaseHandler.getCustomersReference().addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////                        for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
////
////                            for (DataSnapshot snapshot2: snapshot.getChildren()) {
////
////                                Customer customer = snapshot2.getValue(Customer.class);
////
////                                for (Complaint order : orderList) {
////                                    if(customer.getId().equals(order.getCustomerId())
////                                            && order.getCustomerId().equals(mcp.getCustomer().getId())
////                                            && order.getStatus().equals(Utilities.order_pending)){
////                                        customerList.add(customer);
////                                        adapter.notifyDataSetChanged();
////                                    }
////                                }
////                            }
////                        }
////                        if(rowCount==0){
////                            empty.setVisibility(View.VISIBLE);
////                        }
////
////                        progressBar.setVisibility(View.INVISIBLE);
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////
////                    }
////                });
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }
//
//    void updateRecyclerView(){
//        orderList =new ArrayList<>();
//        adapter= new ComplaintsAdapter(getActivity().getApplicationContext(), orderList, customerList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));
//
//        for (Complaint order : orderList) {
//            orderList.add(order);
//        }
//        adapter.notifyDataSetChanged();
//    }
}