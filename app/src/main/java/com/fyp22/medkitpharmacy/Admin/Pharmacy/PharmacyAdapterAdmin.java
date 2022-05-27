package com.fyp22.medkitpharmacy.Admin.Pharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Pharmacies.PharmacyDetail;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PharmacyAdapterAdmin extends RecyclerView.Adapter<PharmacyAdapterAdmin.MyViewHolder> {

    Context context;
    List<Pharmacy> pharmacyList;

    public PharmacyAdapterAdmin(Context context, List<Pharmacy> pharmacyList) {
        this.context=context;
        this.pharmacyList = pharmacyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_list_item_admin, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pharmacy pharmacy = pharmacyList.get(position);
        holder.name.setText(pharmacy.getName());
        Picasso.get().load(pharmacy.getImage()).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PharmacyDetailAdmin.class);
                intent.putExtra(Utilities.intent_pharmacy, pharmacy);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    public void setFilter(List<Pharmacy> newList){
        pharmacyList = new ArrayList<>();
        pharmacyList.addAll(newList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            name =itemView.findViewById(R.id.name_title);
            cardView=itemView.findViewById(R.id.card_view);
        }
    }
}
