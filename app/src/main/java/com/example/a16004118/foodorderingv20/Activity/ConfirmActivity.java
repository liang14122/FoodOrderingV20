package com.example.a16004118.foodorderingv20.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.a16004118.foodorderingv20.Adapter.ConfirmOrderAdapter;
import com.example.a16004118.foodorderingv20.Adapter.MenuAdapter;
import com.example.a16004118.foodorderingv20.Object.ConfirmOrder;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmActivity extends AppCompatActivity {

    private SwipeMenuListView lvConfirmOrder;
    private ArrayList<ConfirmOrder> alConfirmOrder = new ArrayList<>();
    private ConfirmOrderAdapter coa;
    private ArrayList<Menu> alMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        lvConfirmOrder = findViewById(R.id.lvConfirmOrder);
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
            alMenu = (ArrayList<Menu>) bundleAlMenu.getSerializable("alMenu");

            for (int i = 0; i < orderDetailArr.length(); i++){
                JSONObject currentOrder = (JSONObject) orderDetailArr.get(i);
                String foodName = "";
                double price = 0.0;
                int currentMenuId = currentOrder.getInt("menuId");
                int quantity = currentOrder.getInt("quantity");

                for(int j = 0; j < alMenu.size(); j++){
                    Menu currentMenu = alMenu.get(j);
                    if (currentMenu.getMenuId() == currentMenuId){
                        foodName = currentMenu.getFoodName();
                        price = currentMenu.getPrice();
                        ConfirmOrder confirmOrder = new ConfirmOrder(currentMenuId, foodName, price, quantity);
                        alConfirmOrder.add(confirmOrder);
                    }
                }
                coa.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int dp2px(float dpValue) {
        final float scale = ConfirmActivity.this.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
