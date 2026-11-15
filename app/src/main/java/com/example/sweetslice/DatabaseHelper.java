
package com.example.sweetslice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SweetSlice.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "orders";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_name TEXT NOT NULL, " +
                "cake_name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "delivery_date TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "total REAL NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CREATE - Insert a new order
    public boolean insertOrder(CakeOrder order) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("customer_name", order.getCustomerName());
        values.put("cake_name", order.getCakeName());
        values.put("quantity", order.getQuantity());
        values.put("delivery_date", order.getDeliveryDate());
        values.put("price", order.getPrice());
        values.put("total", order.getTotal());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result != -1;
    }

    // READ - Get all orders
    public ArrayList<CakeOrder> getAllOrders() {

        ArrayList<CakeOrder> orderList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {
                CakeOrder order = new CakeOrder(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("customer_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cake_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("delivery_date")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
                );

                orderList.add(order);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderList;
    }

    // UPDATE - Update an order
    public boolean updateOrder(CakeOrder order) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("customer_name", order.getCustomerName());
        values.put("cake_name", order.getCakeName());
        values.put("quantity", order.getQuantity());
        values.put("delivery_date", order.getDeliveryDate());
        values.put("price", order.getPrice());
        values.put("total", order.getTotal());

        int result = db.update(
                TABLE_NAME,
                values,
                "id=?",
                new String[]{String.valueOf(order.getId())}
        );

        db.close();

        return result > 0;
    }

    // DELETE - Delete an order
    public boolean deleteOrder(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                "id=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result > 0;
    }
}
