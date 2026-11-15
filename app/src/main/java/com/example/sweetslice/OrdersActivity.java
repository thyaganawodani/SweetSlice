
package com.example.sweetslice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersActivity extends Activity {

    RecyclerView recyclerOrders;
    TextView tvEmpty;
    Button btnBack;

    DatabaseHelper databaseHelper;

    ArrayList<CakeOrder> orderList;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnBack = findViewById(R.id.btnBack);

        databaseHelper = new DatabaseHelper(this);

        orderList = new ArrayList<>();

        recyclerOrders.setLayoutManager(
                new LinearLayoutManager(this)
        );

        orderAdapter = new OrderAdapter(
                this,
                orderList,
                databaseHelper
        );

        recyclerOrders.setAdapter(orderAdapter);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadOrders() {

        orderList.clear();

        orderList.addAll(databaseHelper.getAllOrders());

        orderAdapter.notifyDataSetChanged();

        if (orderList.isEmpty()) {

            tvEmpty.setVisibility(TextView.VISIBLE);
            recyclerOrders.setVisibility(RecyclerView.GONE);

        } else {

            tvEmpty.setVisibility(TextView.GONE);
            recyclerOrders.setVisibility(RecyclerView.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
