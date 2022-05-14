package com.fyp22.medkitpharmacy.Admin.Complaints;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Complaint;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComplaintDetail extends AppCompatActivity {

    Button writeReviewBtn;
    Button confirmReceivedBtn;
    CircleImageView imageView;

    TextView date;
    TextView status;
    TextView amount;

    TextView name;
    TextView price;
    TextView category;
    TextView description;

//    ProgressBar progressBar;

    Intent intent;
    Customer customer;
    Complaint complaint;
    DatabaseHandler databaseHandler;
    FirebaseUser user;
    FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setTitle("Complaint Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);

//        date=findViewById(R.id.date_val);
//        writeReviewBtn =findViewById(R.id.write_review);
//        confirmReceivedBtn =findViewById(R.id.confirm_received);
//        amount=findViewById(R.id.amount_val);
//        status=findViewById(R.id.status_val);
//
//        name=findViewById(R.id.name_val);
//        price=findViewById(R.id.price_val);
//        description=findViewById(R.id.description_val);
//        category=findViewById(R.id.category_val);
////        progressBar.setVisibility(View.VISIBLE);
//
//        intent=getIntent();
//        customer = (Customer) intent.getSerializableExtra(Utilities.intent_customer);
//        complaint = (Complaint) intent.getSerializableExtra(Utilities.intent_order);
//        databaseHandler= new DatabaseHandler(this);
//        auth = FirebaseAuth.getInstance();
//        user=auth.getCurrentUser();
//
//        if(complaint.getStatus().equals(Utilities.order_pending)){
//            writeReviewBtn.setVisibility(View.INVISIBLE);
//            confirmReceivedBtn.setVisibility(View.VISIBLE);
//        }
//        date.setText(complaint.getDateTime());
//
//        amount.setText(Integer.toString(complaint.getAmount()));
//        status.setText(complaint.getStatus());
//
//        Picasso.get().load(customer.getImage()).into(imageView);
//        name.setText(customer.getName());
//        category.setText(customer.getCategory());
//        description.setText(customer.getDescription());
//        price.setText(Integer.toString(customer.getPrice()));
//
//        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(ComplaintDetail.this, AddReview.class);
//                intent.putExtra(Utilities.intent_product, customer);
//                intent.putExtra(Utilities.intent_store, store);
//                startActivity(intent);
//            }
//        });
//
//        confirmReceivedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog alertDialog = new AlertDialog.Builder(ComplaintDetail.this)
//                        .setTitle("Complaint Confirmation")
//                        .setMessage("Confirm Complaint Receival?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                complaint.setStatus(Utilities.order_complete);
//                                databaseHandler.getComplaintsReference().child(complaint.getId()).setValue(complaint);
//                                startActivity(new Intent(ComplaintDetail.this, ComplaintDetail.class));
//                                finish();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });
//
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
////        startActivity(new Intent(CustomerDetail.this, ViewCustomers.class));
//    }
    }
}