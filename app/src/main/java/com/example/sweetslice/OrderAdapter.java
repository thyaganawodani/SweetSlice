
package com.example.sweetslice;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<CakeOrder> orderList;
    DatabaseHelper databaseHelper;

    public OrderAdapter(
            Context context,
            ArrayList<CakeOrder> orderList,
            DatabaseHelper databaseHelper) {

        this.context = context;
        this.orderList = orderList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.item_order,
                parent,
                false
        );

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            OrderViewHolder holder,
            int position) {

        CakeOrder order = orderList.get(position);

        holder.tvCustomer.setText(
                "Customer: " + order.getCustomerName()
        );

        holder.tvCakeName.setText(order.getCakeName());

        holder.tvQuantity.setText(
                "Quantity: " + order.getQuantity()
        );

        holder.tvDate.setText(
                "Delivery Date: " + order.getDeliveryDate()
        );

        holder.tvTotal.setText(
                "Total: Rs. " + order.getTotal()
        );

        holder.imgOrderCake.setImageResource(
                getCakeImage(order.getCakeName())
        );

        holder.btnEdit.setOnClickListener(v -> editOrder(order));

        holder.btnDelete.setOnClickListener(v -> deleteOrder(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Get cake image using cake name
    private int getCakeImage(String cakeName) {

        switch (cakeName) {

            case "Chocolate Cake":
                return R.drawable.chocolate_cake;

            case "Vanilla Cake":
                return R.drawable.vanilla_cake;

            case "Red Velvet Cake":
                return R.drawable.red_velvet_cake;

            case "Black Forest Cake":
                return R.drawable.black_forest_cake;

            case "Strawberry Cake":
                return R.drawable.strawberry_cake;

            default:
                return R.drawable.chocolate_cake;
        }
    }

    // UPDATE
    private void editOrder(CakeOrder order) {

        View dialogView = LayoutInflater.from(context).inflate(
                R.layout.dialog_edit_order,
                null
        );

        EditText etEditName =
                dialogView.findViewById(R.id.etEditName);

        EditText etEditQuantity =
                dialogView.findViewById(R.id.etEditQuantity);

        EditText etEditDate =
                dialogView.findViewById(R.id.etEditDate);

        etEditName.setText(order.getCustomerName());

        etEditQuantity.setText(
                String.valueOf(order.getQuantity())
        );

        etEditDate.setText(order.getDeliveryDate());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Cake Order")
                .setView(dialogView)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {

            Button updateButton =
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            updateButton.setOnClickListener(v -> {

                String name =
                        etEditName.getText().toString().trim();

                String quantityText =
                        etEditQuantity.getText().toString().trim();

                String date =
                        etEditDate.getText().toString().trim();

                if (name.isEmpty() ||
                        quantityText.isEmpty() ||
                        date.isEmpty()) {

                    Toast.makeText(
                            context,
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
                            context,
                            "Enter a valid quantity",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                if (quantity <= 0) {

                    Toast.makeText(
                            context,
                            "Quantity must be greater than zero",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                double total = order.getPrice() * quantity;

                CakeOrder updatedOrder = new CakeOrder(
                        order.getId(),
                        name,
                        order.getCakeName(),
                        quantity,
                        date,
                        order.getPrice(),
                        total
                );

                boolean result =
                        databaseHelper.updateOrder(updatedOrder);

                if (result) {

                    Toast.makeText(
                            context,
                            "Order updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    refreshOrders();
                    dialog.dismiss();

                } else {

                    Toast.makeText(
                            context,
                            "Update failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });

        dialog.show();
    }

    // DELETE
    private void deleteOrder(CakeOrder order) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Order")
                .setMessage("Are you sure you want to delete this order?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    boolean result =
                            databaseHelper.deleteOrder(order.getId());

                    if (result) {

                        Toast.makeText(
                                context,
                                "Order deleted successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        refreshOrders();

                    } else {

                        Toast.makeText(
                                context,
                                "Delete failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Refresh RecyclerView data
    private void refreshOrders() {

        orderList.clear();

        orderList.addAll(databaseHelper.getAllOrders());

        notifyDataSetChanged();
    }

    // ViewHolder
    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgOrderCake;

        TextView tvCustomer;
        TextView tvCakeName;
        TextView tvQuantity;
        TextView tvDate;
        TextView tvTotal;

        Button btnEdit;
        Button btnDelete;

        public OrderViewHolder(View itemView) {
            super(itemView);

            imgOrderCake = itemView.findViewById(R.id.imgOrderCake);

            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvCakeName = itemView.findViewById(R.id.tvCakeName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotal = itemView.findViewById(R.id.tvTotal);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
