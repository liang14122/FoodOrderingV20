package com.example.a16004118.foodorderingv20.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a16004118.foodorderingv20.Object.ConfirmOrder;
import com.example.a16004118.foodorderingv20.R;

import java.util.ArrayList;

public class ConfirmOrderAdapter extends ArrayAdapter {

    private Context parent_context;
    private int layout_id;
    private ArrayList<ConfirmOrder> confirmOrderList;

    private static final String TAG = "ConfirmOrderAdapter";

    public ConfirmOrderAdapter(Context context,
                       int resource,
                       ArrayList<ConfirmOrder> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        confirmOrderList = objects;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            rowView = inflater.inflate(layout_id, parent, false);
        } else {
            rowView = convertView;
        }

        final TextView tvFoodConfirm = rowView.findViewById(R.id.tvFoodConfirm);
        final TextView tvDetailConfirm = rowView.findViewById(R.id.tvDetailConfirm);
        final TextView tvPriceConfirm = rowView.findViewById(R.id.tvPriceConfirm);

        ConfirmOrder currentConfirmOrder = confirmOrderList .get(position);

        tvFoodConfirm.setText(currentConfirmOrder.getName());
        tvDetailConfirm.setText("$" + currentConfirmOrder.getPrice() + "  *  " + currentConfirmOrder.getQuantity());
        double price = currentConfirmOrder.getPrice() * currentConfirmOrder.getQuantity();
        tvPriceConfirm.setText("$" + String.valueOf(price));

        return rowView;
    }
}
