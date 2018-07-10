package com.example.a16004118.foodorderingv20.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a16004118.foodorderingv20.Activity.AuthActivity;
import com.example.a16004118.foodorderingv20.Activity.MainActivity;
import com.example.a16004118.foodorderingv20.Activity.OrderActivity;
import com.example.a16004118.foodorderingv20.CurrentOrder;
import com.example.a16004118.foodorderingv20.HttpRequest;
import com.example.a16004118.foodorderingv20.ImageTransformation;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.Object.Order;
import com.example.a16004118.foodorderingv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter{

    Context parent_context;
    int layout_id;
    ArrayList<Menu> menuList;

    private static Menu currentMenu;
    private static final String TAG = "MenuAdapter";
    private int menuPosition = 0;

    public MenuAdapter(Context context,
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

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        } else {
            rowView = convertView;
        }

        final ImageView ivFoodImg = rowView.findViewById(R.id.ivFoodImg);
        final TextView tvFoodName = rowView.findViewById(R.id.tvFoodName);
        TextView tvPrice = rowView.findViewById(R.id.tvPrice);
        final Button btnAddToCart = rowView.findViewById(R.id.btnAddToCart);

        currentMenu = menuList.get(position);

        tvFoodName.setText(currentMenu.getFoodName());
        tvPrice.setText("$" + currentMenu.getPrice() + "");
        ivFoodImg.setImageResource(R.drawable.verification_code);

        String imageURL = currentMenu.getImageURL();

        Log.w("TAG", "ivFoodImg: " + ivFoodImg.getWidth());

        Picasso.get().load(imageURL)
                .error(android.R.drawable.stat_notify_error)
                .transform(ImageTransformation.getTransformation(ivFoodImg))
                .into(ivFoodImg);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlCheckODExist = "https://16004118.000webhostapp.com/getOrderDetailByIds.php?orderId="
                        + CurrentOrder.currentOrderId + "&menuId=" + menuList.get(position).getMenuId();
                menuPosition = position;
                HttpRequest requestCheckODExist = new HttpRequest(urlCheckODExist);

                requestCheckODExist.setOnHttpResponseListener(mHttpResponseListenerCheckODExist);
                requestCheckODExist.setMethod("GET");

                requestCheckODExist.execute();
            }
        });
        return rowView;
    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerCheckODExist =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (!jsonObj.getBoolean("success")) {

                            //create new orderDetail record
                            String urlCreateRecord = "https://16004118.000webhostapp.com/createOrderDetail.php?orderId="
                                    + CurrentOrder.currentOrderId + "&menuId=" + menuList.get(menuPosition).getMenuId();

                            HttpRequest requestCreateRecord = new HttpRequest(urlCreateRecord);

                            requestCreateRecord.setOnHttpResponseListener(mHttpResponseListenerCreateRecord);
                            requestCreateRecord.setMethod("POST");

                            requestCreateRecord.execute();
                        } else {

                            //fetch current quantity
                            int quantity = jsonObj.getInt("quantity") + 1;
                            int orderDetailId = jsonObj.getInt("orderDetailId");
                            //update quantity
                            String urlUpdateQuantity = "https://16004118.000webhostapp.com/updateOrderDetailById.php?orderDetailId="
                                    + orderDetailId + "&quantity=" + quantity;

                            HttpRequest requestUpdateQuantity = new HttpRequest(urlUpdateQuantity);

                            requestUpdateQuantity.setOnHttpResponseListener(mHttpResponseListenerUpdateQuantity);
                            requestUpdateQuantity.setMethod("POST");

                            requestUpdateQuantity.execute();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerCreateRecord =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getBoolean("success")){
                           //new orderDetail created successfully
                            Log.e(TAG, "createOrderDetail: Successful" );
                            if(getContext() instanceof OrderActivity){
                                ((OrderActivity)getContext()).updateOrderUI(Integer.parseInt(CurrentOrder.currentOrderId));
                            }
                        }else{
                            //Failed to create orderDetail
                            orderDetailFailed();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerUpdateQuantity =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getBoolean("success")){
                            //new orderDetail created successfully
                            Log.e(TAG, "createOrderDetail: Successful" );

                            if(getContext() instanceof OrderActivity){
                                ((OrderActivity)getContext()).updateOrderUI(Integer.parseInt(CurrentOrder.currentOrderId));
                            }
                        }else{
                            //Failed to create orderDetail
                            orderDetailFailed();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private void orderDetailFailed() {
        Log.e(TAG, "createOrderDetail: Failed");
        Toast.makeText(getContext(), "Failed to continue your order", Toast.LENGTH_LONG).show();
    }
}
