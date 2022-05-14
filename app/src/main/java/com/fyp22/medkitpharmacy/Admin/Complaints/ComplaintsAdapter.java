package com.fyp22.medkitpharmacy.Admin.Complaints;

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
import com.fyp22.medkitpharmacy.Models.Complaint;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.MyViewHolder> {

    Context context;
    List<Customer> customerList;
    List<Complaint> complaintList;
    DatabaseHandler databaseHandler;

    public ComplaintsAdapter(Context context, List<Complaint> complaintList, List<Customer> customerList) {
        this.context=context;
        this.customerList = customerList;
        this.complaintList = complaintList;
        this.databaseHandler= new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(complaintList.size() > 0 && customerList.size() > 0) {
            Complaint complaint = complaintList.get(position);
            Customer customer = customerList.get(position);
            holder.name.setText(customer.getName());
            holder.description.setText(complaint.getDescription());
            holder.customerName.setText(customer.getName());
            holder.date.setText(complaint.getDate());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComplaintDetail.class);
                    intent.putExtra(Utilities.intent_complaint, complaint);
                    intent.putExtra(Utilities.intent_customer, customer);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView description;
        TextView customerName;
        TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            description =itemView.findViewById(R.id.description);
            date=itemView.findViewById(R.id.date);
            customerName =itemView.findViewById(R.id.customer_name);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
