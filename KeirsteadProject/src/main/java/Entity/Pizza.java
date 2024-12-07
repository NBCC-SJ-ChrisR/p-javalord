package Entity;

import java.io.Serializable;

public class Pizza implements Serializable {
    // Fields
    private int id;
    private int orderId;
    private int sizeId;
    private int crustId;
    private int quantity;
    private double priceEach;
    private double totalPrice;

    // Constructors
    public Pizza(int id, int orderId, int sizeId, int crustId, int quantity, double priceEach, double totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.sizeId = sizeId;
        this.crustId = crustId;
        this.quantity = quantity;
        this.priceEach = priceEach;
        this.totalPrice = totalPrice;
    }
    public Pizza() {}

    // Methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSizeId() {
        return sizeId;
    }
    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getCrustId() {
        return crustId;
    }
    public void setCrustId(int crustId) {
        this.crustId = crustId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceEach() {
        return priceEach;
    }
    public void setPriceEach(double priceEach) {
        this.priceEach = priceEach;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", sizeId=" + sizeId +
                ", crustId=" + crustId +
                ", quantity=" + quantity +
                ", priceEach=" + priceEach +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
