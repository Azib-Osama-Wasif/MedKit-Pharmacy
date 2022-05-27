package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.textfield.TextInputEditText;

public class QuantityDialogueFragment extends DialogFragment{

    private QuantityInputListener listener;

    TextInputEditText quantityET;
    Button submit;

    String quantity;
    String index;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quantity =getArguments().getString(Utilities.intent_quantity);
        index =getArguments().getString(Utilities.intent_index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quantity_input, container, false);
        quantityET =view.findViewById(R.id.quantityET);
        quantityET.setText(quantity);
        submit=view.findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityET.getText().equals("")){
                    Toast.makeText(getContext(), "Please Enter a Quantity", Toast.LENGTH_LONG).show();
                }else{
                    listener.onFinishEditDialog(quantityET.getText().toString(), index);
                    dismiss();
                }
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (QuantityInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement DialogListener");
        }
    }
}