package com.fyp22.medkitpharmacy.Pharmacy.Orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Order;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailPharmacy extends AppCompatActivity {

    Button confirmDispatch;
    CircleImageView imageView;

    TextView date;
    TextView status;
    TextView amount;

    TextView name;
    TextView name2;
    TextView price;
    TextView type;
    TextView description;
    TextView waiting;

    Intent intent;
    Medicine medicine;
    Order order;
    Pharmacy pharmacy;
    DatabaseHandler databaseHandler;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_pharmacy);
        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.imageView);

        date=findViewById(R.id.date_val);
        confirmDispatch =findViewById(R.id.confirm_dispatch);
        amount=findViewById(R.id.amount_val);
        status=findViewById(R.id.status_val);
        waiting=findViewById(R.id.waiting_dispatch);

        name=findViewById(R.id.name_val);
        name2=findViewById(R.id.name_val2);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        type =findViewById(R.id.type_val);

        intent=getIntent();
        medicine = (Medicine) intent.getSerializableExtra(Utilities.intent_medicine);
        order= (Order) intent.getSerializableExtra(Utilities.intent_order);
        pharmacy = (Pharmacy) intent.getSerializableExtra(Utilities.intent_pharmacy);
        databaseHandler= new DatabaseHandler(this);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //CONFIRM RECEIVED button will only be visible if the customer is yet to receive the medicine
        //ADD RATING button will only be visible if the customer has received the medicine
        if(order.getStatus().equals(Utilities.order_pending)){
            confirmDispatch.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.INVISIBLE);
        }

        if(order.getStatus().equals(Utilities.order_dispatched)){
            confirmDispatch.setVisibility(View.INVISIBLE);
            waiting.setVisibility(View.VISIBLE);
        }

        if(order.getStatus().equals(Utilities.order_complete)){
            confirmDispatch.setVisibility(View.INVISIBLE);
            waiting.setText("ORDER DELIVERED");
            waiting.setVisibility(View.VISIBLE);
        }

        date.setText(order.getDateTime());

        amount.setText(Integer.toString(order.getAmount()));
        status.setText(order.getStatus());

        Picasso.get().load(medicine.getImage()).into(imageView);
        name.setText(medicine.getName());
        name2.setText(medicine.getName());
        type.setText(medicine.getType());
        description.setText(medicine.getDescription());
        price.setText(Integer.toString(medicine.getPrice()));

        if(order.getStatus().equals(Utilities.order_pending)){
            confirmDispatch.setVisibility(View.VISIBLE);
        }else{
            confirmDispatch.setVisibility(View.GONE);
        }
        confirmDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(OrderDetailPharmacy.this)
                        .setTitle("Dispatch Confirmation")
                        .setMessage("Confirm Order Dispatch?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                order.setStatus(Utilities.order_dispatched);
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);
                                Intent intent = new Intent(OrderDetailPharmacy.this, OrderDetailPharmacy.class);
                                intent.putExtra(Utilities.intent_medicine, medicine);
                                intent.putExtra(Utilities.intent_order, order);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrderDetailPharmacy.this, ViewOrdersPharmacy.class).putExtra(Utilities.intent_pharmacy, pharmacy));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(OrderDetailPharmacy.this, ViewOrdersPharmacy.class).putExtra(Utilities.intent_pharmacy, pharmacy));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}