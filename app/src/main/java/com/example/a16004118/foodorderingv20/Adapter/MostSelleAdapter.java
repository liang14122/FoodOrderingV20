package com.example.a16004118.foodorderingv20.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a16004118.foodorderingv20.ImageTransformation;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MostSelleAdapter extends ArrayAdapter{

    Context parent_context;
    int layout_id;
    ArrayList<Menu> menuList;

    public MostSelleAdapter(Context context,
                                int resource,
                                ArrayList<Menu> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        menuList = objects;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        }else{
            rowView = convertView;
        }

        final ImageView ivFoodImg = rowView.findViewById(R.id.ivFoodImg);
        TextView tvFoodName = rowView.findViewById(R.id.tvFoodName);
        TextView tvPrice = rowView.findViewById(R.id.tvPrice);
        TextView tvDescription = rowView.findViewById(R.id.tvDescription);

        Menu currentMenu = menuList.get(position);

        tvFoodName.setText(currentMenu.getFoodName());
        tvPrice.setText("$" + currentMenu.getPrice()+"");
        tvDescription.setText(currentMenu.getDescription());
        ivFoodImg.setImageResource(R.drawable.verification_code);

        String imageURL = currentMenu.getImageURL();

        Log.w("TAG", "ivFoodImg: " + ivFoodImg.getWidth() );

        Picasso.get().load(imageURL)
                .error(android.R.drawable.stat_notify_error)
                .transform(ImageTransformation.getTransformation(ivFoodImg))
                .into(ivFoodImg);

        return rowView;
    }
}
