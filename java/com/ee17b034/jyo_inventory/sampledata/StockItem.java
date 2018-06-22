package com.ee17b034.jyo_inventory.sampledata;

public class StockItem {
    private final String productName;
    private final String price;
    private final int quantity;

    public StockItem(String productName,String price,int quantity) {
        this.productName = productName;
        this.price =price;
        this.quantity =quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity + '\'' + '}';
    }
}
