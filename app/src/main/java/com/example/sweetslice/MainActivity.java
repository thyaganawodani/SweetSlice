
package com.example.sweetslice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    Spinner spCake;
    ImageView imgCake;
    TextView tvCakePrice, tvMessage;

    EditText etCustomerName, etQuantity, etDeliveryDate;

    Button btnPlaceOrder, btnViewOrders;

    DatabaseHelper databaseHelper;

    String[] cakeNames = {
            "Chocolate Cake",
            "Vanilla Cake",
            "Red Velvet Cake",
            "Black Forest Cake",
            "Strawberry Cake"
    };

    double[] cakePrices = {
            3500, 2500, 4000, 3800, 3000
    };

    int[] cakeImages = {
            R.drawable.chocolate_cake,
            R.drawable.vanilla_cake,
            R.drawable.red_velvet_cake,
            R.drawable.black_forest_cake,
            R.drawable.strawberry_cake
    };

    int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        spCake = findViewById(R.id.spCake);
        imgCake = findViewById(R.id.imgCake);
        tvCakePrice = findViewById(R.id.tvCakePrice);
        tvMessage = findViewById(R.id.tvMessage);

        etCustomerName = findViewById(R.id.etCustomerName);
        etQuantity = findViewById(R.id.etQuantity);
        etDeliveryDate = findViewById(R.id.etDeliveryDate);

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnViewOrders = findViewById(R.id.btnViewOrders);

        // Load cake names into Spinner
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        cakeNames
                );

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCake.setAdapter(spinnerAdapter);

        // Change cake image and price when selecting a cake
        spCake.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        selectedPosition = position;

                        imgCake.setImageResource(cakeImages[position]);

                        tvCakePrice.setText(
                                "Price: Rs. " + cakePrices[position]
                        );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    OrdersActivity.class
            );
            startActivity(intent);
        });
    }

    private void placeOrder() {

        String customerName =
                etCustomerName.getText().toString().trim();

        String quantityText =
                etQuantity.getText().toString().trim();

        String deliveryDate =
                etDeliveryDate.getText().toString().trim();

        // Validate inputs
        if (customerName.isEmpty() ||
                quantityText.isEmpty() ||
                deliveryDate.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Enter a valid quantity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (quantity <= 0) {

            Toast.makeText(
                    this,
                    "Quantity must be greater than zero",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String cakeName = cakeNames[selectedPosition];
        double price = cakePrices[selectedPosition];

        double total = price * qua-ntity;

        CakeOrder order = new CakeOrder(
                0,
                customerName,
                cakeName,
                quantity,
                deliveryDate,
                price,
                total
        );

        boolean result = databaseHelper.insertOrder(order);

        if (result) {

            tvMessage.setText(
                    "Order placed successfully!\nTotal: Rs. " + total
            );

            Toast.makeText(
                    this,
                    "Cake order saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            etCustomerName.setText("");
            etQuantity.setText("");
            etDeliveryDate.setText("");

            spCake.setSelection(0);

        } else {

            tvMessage.setText("Order could not be saved.");
        }
    }
}
