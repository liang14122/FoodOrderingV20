package com.example.a16004118.foodorderingv20.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private ListView lvConfirmOrder;
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

        Intent intent = getIntent();
//        alMenu = intent.getArr
        try {
            JSONArray orderDetailArr = new JSONArray(intent.getExtras().getString("orderDetail"));

            for (int i = 0; i < orderDetailArr.length(); i++){
                JSONObject currentOrder = (JSONObject) orderDetailArr.get(i);
                String foodName = "";
                double price = 0.0;

//                for(int )
//                ConfirmOrder confirmOrder = new ConfirmOrder(currentOrder.getInt("menuId"), )
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
