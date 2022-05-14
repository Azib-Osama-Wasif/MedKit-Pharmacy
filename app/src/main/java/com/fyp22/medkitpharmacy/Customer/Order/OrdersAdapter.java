package com.fyp22.medkitpharmacy.Customer.Order;

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
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    Context context;
    List<Medicine> medicineList;
    List<Order> orderList;
    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public OrdersAdapter(Context context, List<Order> orderList, List<Medicine> medicineList) {
        this.context=context;
        this.medicineList = medicineList;
        this.orderList = orderList;
        this.databaseHandler= new DatabaseHandler(context);
        this.mcp= new MyCustomerPreferences(context);
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        OrdersAdapter.MyViewHolder myViewHolder = new OrdersAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.MyViewHolder holder, int position) {

        if(orderList.size() > 0 && medicineList.size() > 0) {
            Order order = orderList.get(position);
            Medicine medicine = medicineList.get(position);
            holder.name.setText(medicine.getName());
            holder.price.setText("Rs." + Integer.toString(medicine.getPrice()));
            holder.pharmacy.setText("Pharmaacy name");
            holder.date.setText(order.getDateTime());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetail.class);
                    intent.putExtra(Utilities.intent_medicine, medicine);
                    intent.putExtra(Utilities.intent_order, order);
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
        TextView name;
        TextView price;
        TextView pharmacy;
        TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            date=itemView.findViewById(R.id.order_date);
            pharmacy =itemView.findViewById(R.id.product_store);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
