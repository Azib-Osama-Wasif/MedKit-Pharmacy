package com.fyp22.medkitpharmacy.Pharmacy.Orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrdersAdapterPharmacy extends RecyclerView.Adapter<OrdersAdapterPharmacy.MyViewHolder> {

    Context context;
    Pharmacy pharmacy;
    Customer customer;
    List<Medicine> medicineList;
    List<Order> orderList;
    DatabaseHandler databaseHandler;
    MyPharmacyPreferences myPharmacyPreferences;

    public OrdersAdapterPharmacy(Context context, Pharmacy pharmacy, List<Order> orderList, List<Medicine> medicineList) {
        this.context=context;
        this.pharmacy = pharmacy;
        this.medicineList = medicineList;
        this.orderList = orderList;
        this.databaseHandler= new DatabaseHandler(context);
        this.myPharmacyPreferences = new MyPharmacyPreferences(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_pharmacy_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        if(medicineList.size()!=0){
            Medicine medicine = medicineList.get(position);

            databaseHandler.getCustomersReference().child(order.getCustomerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    customer = snapshot.getValue(Customer.class);
                    holder.city.setText(customer.getCity());
                    holder.customerName.setText(customer.getName());
                    holder.customer_address.setText(customer.getAddress());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.medicineName.setText(medicine.getName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetailPharmacy.class);
                    intent.putExtra(Utilities.intent_medicine, medicine);
                    intent.putExtra(Utilities.intent_order, order);
                    intent.putExtra(Utilities.intent_pharmacy, pharmacy);
                    intent.putExtra(Utilities.intent_customer, customer);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView medicineName;
        TextView city;
        TextView customerName;
        TextView customer_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName =itemView.findViewById(R.id.medicine_name);
            city =itemView.findViewById(R.id.city_name);
            customer_address =itemView.findViewById(R.id.customer_address);
            customerName =itemView.findViewById(R.id.customer_name);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
