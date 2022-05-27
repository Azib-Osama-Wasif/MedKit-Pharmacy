package com.fyp22.medkitpharmacy.Customer.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Medicine.MedicineDetail;
import com.fyp22.medkitpharmacy.Models.CartItem;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder>{
    Context context;
    List<Medicine> medicineList;
    List<CartItem> cartItemList;
    DatabaseHandler databaseHandler;
    static MyCustomerPreferences mcp;

    public CartItemAdapter(Context context, List<Medicine> medicineList, List<CartItem> cartItemList) {
        this.context=context;
        this.medicineList = medicineList;
        this.cartItemList = cartItemList;
        databaseHandler= new DatabaseHandler(context);
        mcp= new MyCustomerPreferences(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Medicine medicine = medicineList.get(position);
        CartItem cartItem = cartItemList.get(position);
        holder.name.setText(medicine.getName());
        holder.price.setText("Rs."+Integer.toString(medicine.getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MedicineDetail.class);
                intent.putExtra(Utilities.intent_medicine, (Serializable)medicine);
                context.startActivity(intent);
            }
        });

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Order order = new Order();
                order.setCustomerId(mcp.getCustomer().getId().toString());
                order.setMedicineId(medicine.getId());
                order.setTotalAmount(medicine.getPrice());

                //FORMATTING DATE PROPERLY
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.DATE_TIME_FORMAT, Locale.ENGLISH);
                String currentDateTime = LocalDateTime.now().format(formatter);
//                LocalDateTime dateTime = LocalDateTime.parse(currentDateTime, formatter);
                order.setDateTime(currentDateTime);
                order.setPharmacyId(cartItem.getPharmacyId());
                order.setStatus(Utilities.order_pending);
                databaseHandler.getOrdersReference().push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseHandler.getOrdersReference().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                Order order = snapshot.getValue(Order.class);
                                order.setId(snapshot.getKey());
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);

                                databaseHandler.getCartReference().child(cartItem.getId()).removeValue();
                                notifyDataSetChanged();
                                context.startActivity(new Intent(context,CartActivity.class));
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
                    }
                });

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cartItemList.remove(position);
//                notifyDataSetChanged();
                databaseHandler.getCartReference().child(cartItemList.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseHandler.getCartReference().child(cartItemList.get(position).getId()).removeValue();
                            Toast.makeText(context, "Medicine removed from cart", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context,CartActivity.class));
                            notifyDataSetChanged();
                        }
                        else {
                            Utilities.createAlertDialog(context,"Error","Something went wrong. Please try again!");
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView price;
        CardView confirm;
        CardView delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            confirm =itemView.findViewById(R.id.yes);
            delete =itemView.findViewById(R.id.no);
            cardView=itemView.findViewById(R.id.cardView);

        }
    }
}
