package com.example.a16004118.foodorderingv20.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.a16004118.foodorderingv20.Adapter.ConfirmOrderAdapter;
import com.example.a16004118.foodorderingv20.Object.ConfirmOrder;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.R;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ConfirmActivity extends AppCompatActivity implements OnDateSetListener{

    private ArrayList<ConfirmOrder> alConfirmOrder = new ArrayList<>();
    private ConfirmOrderAdapter coa;
    private TextView tvItemNumConfirm, tvTotalPriceConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        tvItemNumConfirm = findViewById(R.id.tvItemNumConfirm);
        tvTotalPriceConfirm = findViewById(R.id.tvTotalPriceConfirm);
        Button btnConfirmOrderConfirm = findViewById(R.id.btnConfirmOrderConfirm);

        SwipeMenuListView lvConfirmOrder = findViewById(R.id.lvConfirmOrder);
        coa = new ConfirmOrderAdapter(ConfirmActivity.this, R.layout.confirm_order_row, alConfirmOrder);
        lvConfirmOrder.setAdapter(coa);
//
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(ConfirmActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(244, 171, 172)));
                deleteItem.setWidth(dp2px(60));
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        };
        lvConfirmOrder.setMenuCreator(swipeMenuCreator);
        lvConfirmOrder.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        lvConfirmOrder.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        // delete item

                        alConfirmOrder.remove(position);
                        coa.notifyDataSetChanged();
                        updateUI();
                        break;
                }

                return false;
            }
        });
        Intent intent = getIntent();

        try {
            Bundle bundleOrderDetail = intent.getBundleExtra("bundleOrderDetail");
            JSONArray orderDetailArr = new JSONArray(bundleOrderDetail.getString("orderDetail"));

            //get alMenu as ArrayList
            Bundle bundleAlMenu = intent.getBundleExtra("bundleAlMenu");
            ArrayList<Menu> alMenu = (ArrayList<Menu>) bundleAlMenu.getSerializable("alMenu");

            for (int i = 0; i < orderDetailArr.length(); i++){

                JSONObject currentOrder = (JSONObject) orderDetailArr.get(i);
                String foodName = "";
                double price = 0.0;
                int currentMenuId = currentOrder.getInt("menuId");
                int quantity = currentOrder.getInt("quantity");

                assert alMenu != null;
                for(int j = 0; j < alMenu.size(); j++){
                    Menu currentMenu = alMenu.get(j);
                    if (currentMenu.getMenuId() == currentMenuId){
                        foodName = currentMenu.getFoodName();
                        price = currentMenu.getPrice();
                        ConfirmOrder confirmOrder = new ConfirmOrder(currentMenuId, foodName, price, quantity);
                        alConfirmOrder.add(confirmOrder);

                        updateUI();
                    }
                }
                coa.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TimePickerDialog mDialogHourMinute = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setCallBack(this)
                .setCancelStringId("Cancel")
                .setSureStringId("Confirm")
                .setMinMillseconds(System.currentTimeMillis())
                .setTitleStringId("Choose PickUp Time")
                .setHourText("H")
                .setMinuteText("M")
                .setCyclic(false)
                .build();

        btnConfirmOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogHourMinute.show(getSupportFragmentManager(), "Choose PickUp Time");

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {

        double totalPrice = 0;
        int totalQuantity = 0;

        for (int i = 0; i < alConfirmOrder.size(); i++){

            double price = alConfirmOrder.get(i).getPrice();
            int quantity = alConfirmOrder.get(i).getQuantity();
            totalPrice += price*quantity;
            totalQuantity += quantity;

        }

        if (totalPrice > 0 && totalQuantity > 0){
            tvItemNumConfirm.setText(String.valueOf(totalQuantity) + " Items");
            tvTotalPriceConfirm.setText("Total: $" + String.valueOf(totalPrice));
        }

    }

    public int dp2px(float dpValue) {
        final float scale = ConfirmActivity.this.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Toast.makeText(ConfirmActivity.this, "Data Set", Toast.LENGTH_LONG).show();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:MM");
        String pickUpTime = simpleDateFormat.format(millseconds);

        Intent intent = new Intent(ConfirmActivity.this, FinalActivity.class);

        intent.putExtra("pickUpTime", pickUpTime);

        //pass arrayList alMenu
        Bundle bundleAlConfirmOrder = new Bundle();
        bundleAlConfirmOrder.putSerializable("alConfirmOrder", alConfirmOrder);
        intent.putExtra("bundleAlConfirmOrder",bundleAlConfirmOrder);
        startActivity(intent);

    }
}
