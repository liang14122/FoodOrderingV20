package com.example.a16004118.foodorderingv20.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a16004118.foodorderingv20.Object.ConfirmOrder;
import com.example.a16004118.foodorderingv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FinalActivity extends AppCompatActivity {

    private TextView tvMessage;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "FinalActivity";
    private FirebaseUser currentUser;
    private StringBuilder msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        tvMessage = findViewById(R.id.tvMessage);
        Button btnNewOrder = findViewById(R.id.btnNewOrder);

        firebaseAuth = FirebaseAuth.getInstance();

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {

        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {

            msg = new StringBuilder("Dear Customer, Your Order :\n");
            Intent i = getIntent();

            String pickUpTime = i.getStringExtra("pickUpTime");
            Bundle bundleAlMenu = i.getBundleExtra("bundleAlConfirmOrder");
            ArrayList<ConfirmOrder> alConfirmOrder = (ArrayList<ConfirmOrder>) bundleAlMenu.getSerializable("alConfirmOrder");

            assert alConfirmOrder != null;
            for (int j = 0; j < alConfirmOrder.size(); j++) {
                //order detail
                ConfirmOrder currentConfirmOrder = alConfirmOrder.get(j);
                String menuName = currentConfirmOrder.getName();
                int quantity = currentConfirmOrder.getQuantity();
                msg.append(menuName).append(" * ").append(quantity).append("\n");
                //pick up time
            }
            msg.append("PickUp Time: ").append(pickUpTime).append("\n");
            msg.append("Please pickUp your items on time.").append("\n");

            String messageTV = "Dear Customer, \nYour Order Detail have been send to your phone,\n Please pickUp your items on time.";
            tvMessage.setText(messageTV);

            new SendSMSTask().execute();

        } else {
            Intent intent = new Intent(FinalActivity.this, AuthActivity.class);
            startActivity(intent);
        }

        super.onStart();
    }

    private class SendSMSTask extends AsyncTask<URL, Integer, Long> {

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }

        @Override
        protected Long doInBackground(URL... urls) {

            try {
                // Construct data
                String apiKey = "apikey=" + "zID4Y8jYp9g-pbKZQwDcdqT2nrncO4mU3iak3quSaq";
                String message = "&message=" + String.valueOf(msg);
                String sender = "&sender=" + "Food Order";
                String numbers = "&numbers=" + currentUser.getPhoneNumber();

                // Send data
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                String data = apiKey + numbers + message + sender;
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                }
                rd.close();
                Log.e(TAG, "onStart: " + stringBuffer.toString() );
            } catch (Exception e) {
                Log.e(TAG, "Error SMS "+e );
            }
            return null;
        }
    }
}
