package com.example.a16004118.foodorderingv20.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.a16004118.foodorderingv20.Adapter.MostSelleAdapter;
import com.example.a16004118.foodorderingv20.HttpRequest;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnStartOrder;

    private ListView lvMostSeller;
    private ArrayList<Menu> alMostSeller = new ArrayList<>();
    private ArrayAdapter msa ;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartOrder = findViewById(R.id.btnStartOrder);
        lvMostSeller = findViewById(R.id.lvMostSeller);

        msa = new MostSelleAdapter(MainActivity.this, R.layout.most_seller_row, alMostSeller);
        lvMostSeller.setAdapter(msa);

//        alMostSeller.add(new Menu(1, 1,"aa", 1.1, "bb", "cc", true));
//        msa.notifyDataSetChanged();

        mAuth= FirebaseAuth.getInstance();

        //get most sellers
        String url = "https://16004118.000webhostapp.com/getMenuMostSellers.php";

        HttpRequest request = new HttpRequest(url);

        request.setOnHttpResponseListener(mHttpResponseListenerGetMenuMostSellers);
        request.setMethod("GET");

        request.execute();

        btnStartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerGetMenuMostSellers =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("getUserByUid Results: ", response);

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
                                alMostSeller.add(currentMenu);
                            }
                            msa.notifyDataSetChanged();
                            Log.e(TAG, "alMostSeller Size: " + alMostSeller.size() );
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };
}
