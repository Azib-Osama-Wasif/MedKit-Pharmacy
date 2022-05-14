package com.fyp22.medkitpharmacy.Customer.Order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class OrderDetail extends AppCompatActivity {

    Button confirmReceivedBtn;
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
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.imageView);

        date=findViewById(R.id.date_val);
        confirmReceivedBtn =findViewById(R.id.confirm_received);
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

        //CONFIRM RECEIVED button will only be visible if the customer is yet to receive the product
        //ADD RATING button will only be visible if the customer has received the product
        if(order.getStatus().equals(Utilities.order_pending)){
            confirmReceivedBtn.setVisibility(View.INVISIBLE);
            waiting.setVisibility(View.VISIBLE);
        }

        if(order.getStatus().equals(Utilities.order_dispatched)){
            confirmReceivedBtn.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.INVISIBLE);
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

        confirmReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(OrderDetail.this)
                        .setTitle("Order Confirmation")
                        .setMessage("Confirm Order Received?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                order.setStatus(Utilities.order_complete);
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);
                                startActivity(new Intent(OrderDetail.this, OrderDetail.class));
                                finish();
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
        finish();
//        startActivity(new Intent(MedicineDetail.this, ViewMedicines.class));
    }
}