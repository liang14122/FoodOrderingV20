package com.example.a16004118.foodorderingv20.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a16004118.foodorderingv20.Adapter.MenuAdapter;
import com.example.a16004118.foodorderingv20.CurrentOrder;
import com.example.a16004118.foodorderingv20.HttpRequest;
import com.example.a16004118.foodorderingv20.ImageTransformation;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnConfirmOrder;

    private ListView lvMenu;
    private ArrayList<Menu> alMenu = new ArrayList<>();
    private ArrayAdapter ma ;
    private JSONArray orderDetailArr = new JSONArray();

    private static final String TAG = "OrderActivity";
    private TextView tvItemNum, tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setTitle("Begin Your Order");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String fbId = currentUser.getUid();

        String urlGetCurrentUSer = "https://16004118.000webhostapp.com/getUserByUid.php?fbId=" + fbId;

        HttpRequest requestGetCurrentUSer = new HttpRequest(urlGetCurrentUSer);

        requestGetCurrentUSer.setOnHttpResponseListener(mHttpResponseListenerGetCurrentUSer);
        requestGetCurrentUSer.setMethod("GET");

        requestGetCurrentUSer.execute();

        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        lvMenu = findViewById(R.id.lvMenu);
        tvItemNum = findViewById(R.id.tvItemNum);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        ma = new MenuAdapter(OrderActivity.this, R.layout.menu_row, alMenu);
        lvMenu.setAdapter(ma);

        String urlGetMenu = "https://16004118.000webhostapp.com/getMenu.php";

        HttpRequest requestGetMenu = new HttpRequest(urlGetMenu);

        requestGetMenu.setOnHttpResponseListener(mHttpResponseListenerGetMenu);
        requestGetMenu.setMethod("GET");

        requestGetMenu.execute();

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderDetailArr.length()>0){
                    Intent intent = new Intent(OrderActivity.this, ConfirmActivity.class);
                    //pass arrayList alMenu
                    intent.putExtra("alMenu", alMenu);
                    //pass orderDetail
                    Bundle bundle = new Bundle();
                    bundle.putString("orderDetail", orderDetailArr.toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(OrderActivity.this, "Please select at least one item", Toast.LENGTH_LONG).show();
                }

            }
        });

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu menuOnClick = alMenu.get(position);

                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OrderActivity.this);
                LayoutInflater factory = LayoutInflater.from(OrderActivity.this);

                final View viewAlert = factory.inflate(R.layout.layout_alert, null);

                ImageView ivFood = viewAlert.findViewById(R.id.ivFoodAlert);
                TextView tvFoodAlert = viewAlert.findViewById(R.id.tvFoodAlert);
                TextView tvPriceAlert = viewAlert.findViewById(R.id.tvPriceAlert);
                TextView tvDescriptionAlert = viewAlert.findViewById(R.id.tvDescriptionAlert);

                Picasso.get().load(menuOnClick.getImageURL())
                        .error(android.R.drawable.stat_notify_error)
                        .transform(ImageTransformation.getTransformation(ivFood))
                        .into(ivFood);
                tvFoodAlert.setText(menuOnClick.getFoodName());
                tvPriceAlert.setText(String.valueOf(menuOnClick.getPrice()));
                tvDescriptionAlert.setText(menuOnClick.getDescription());

                alertBuilder.setView(viewAlert);
                alertBuilder.setCancelable(true);

//                alertBuilder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        btnAddToCart.performClick();
//                    }
//                });
                alertBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertBuilder.create();
                alertBuilder.show();
            }
        });

    }

    public void updateOrderUI(int orderId){

        String urlGetOrderDetail = "https://16004118.000webhostapp.com/getOrderDetailById.php?orderId=" + orderId;

        HttpRequest requestGetOrderDetail = new HttpRequest(urlGetOrderDetail);

        requestGetOrderDetail.setOnHttpResponseListener(mHttpResponseListenerGetOrderDetail);
        requestGetOrderDetail.setMethod("GET");

        requestGetOrderDetail.execute();

    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerGetMenu =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("getMenu Results: ", response);

                        JSONArray jsonArr = new JSONArray(response);

                        if (jsonArr.length() > 0){
                            for (int i = 0; i < jsonArr.length(); i++){
                                JSONObject currentObj = (JSONObject) jsonArr.get(i);
                                Menu currentMenu = new Menu(currentObj.getInt("menuId"),
                                        currentObj.getInt("categoryId"),
                                        currentObj.getString("foodName"),
                                        currentObj.getDouble("price"),
                                        currentObj.getString("description"),
                                        (currentObj.getInt("mostSeller") == 1),
                                        currentObj.getString("imageURL"));
                                Log.e(TAG, "addMemu: " + currentObj.getInt("price") );
                                alMenu.add(currentMenu);
                            }
                            ma.notifyDataSetChanged();
                            Log.e(TAG, "alMostSeller Size: " + alMenu.size() );
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerGetCurrentUSer =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("getUserByUid Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.has("success")){
                            if (!jsonObj.getBoolean("success")){
                                orderFailed();
                            }
                        } else{

                            String userId = jsonObj.getString("userId");

                            String urlCreateOrder = "https://16004118.000webhostapp.com/createOrder.php?userId=" + userId;

                            HttpRequest requestCreateUser = new HttpRequest(urlCreateOrder);

                            requestCreateUser.setOnHttpResponseListener(mHttpResponseListenerCreateOrder);
                            requestCreateUser.setMethod("POST");

                            requestCreateUser.execute();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerCreateOrder =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("createUser Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (!jsonObj.getString("orderId").isEmpty()){

                            Log.e(TAG, "createOrder: Successfully");

                            CurrentOrder.currentOrderId = jsonObj.getString("orderId");
                        }else{
                            orderFailed();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };


    private HttpRequest.OnHttpResponseListener mHttpResponseListenerGetOrderDetail =
            new HttpRequest.OnHttpResponseListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("getMenu Results: ", response);

                         JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getBoolean("success")){
                            JSONArray recordArray = jsonObject.getJSONArray("record");
                            orderDetailArr = recordArray;
                            if (recordArray.length() > 0){

                                int totalQuantity = 0;
                                double totalPrice = 0.0;

                                for (int i = 0; i < recordArray.length(); i++){
                                    JSONObject currentObj = (JSONObject) recordArray.get(i);

                                    //can be improved
                                    int menuId = currentObj.getInt("menuId");
                                    double currentPrice = 0.0;
                                    for(int j = 0; j < alMenu.size(); j++){
                                        Menu currentMenu = alMenu.get(j);
                                        if (currentMenu.getMenuId() == menuId){
                                            currentPrice = currentMenu.getPrice();
                                        }
                                    }

                                    totalQuantity += currentObj.getInt("quantity");
                                    totalPrice += currentObj.getInt("quantity") * currentPrice;
                                }
                                tvItemNum.setText(totalQuantity + " Items");
                                tvTotalPrice.setText("Total: $" + totalPrice);
                            }
                        }else{

                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private void orderFailed() {
        Log.e(TAG, "createOrder: Failed");
        Toast.makeText(OrderActivity.this, "Failed to start your order", Toast.LENGTH_LONG).show();
        finish();
    }
}
