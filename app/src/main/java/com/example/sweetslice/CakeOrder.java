
package com.example.sweetslice;

public class CakeOrder {

    private int id;
    private String customerName;
    private String cakeName;
    private int quantity;
    private String deliveryDate;
    private double price;
    private double total;

    public CakeOrder(int id, String customerName, String cakeName,
                     int quantity, String deliveryDate,
                     double price, double total) {

        this.id = id;
        this.customerName = customerName;
        this.cakeName = cakeName;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.price = price;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCakeName() {
        return cakeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }
}
