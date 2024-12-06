package Entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Orders implements Serializable {
    // Fields
    private int id;
    private int customerId;
    private double subtotal;
    private double hst;
    private double orderTotal;
    private String orderStatus;
    private Timestamp deliveryDate;
    private Timestamp orderPlacedDate;

    // Constructors
    public Orders(int id, int customerId, double subtotal, double hst, double orderTotal,
                  String orderStatus, Timestamp deliveryDate, Timestamp orderPlacedDate) {
        this.id = id;
        this.customerId = customerId;
        this.subtotal = subtotal;
        this.hst = hst;
        this.orderTotal = orderTotal;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.orderPlacedDate = orderPlacedDate;
    }

    public Orders() {}

    // Methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getHst() {
        return hst;
    }
    public void setHst(double hst) {
        this.hst = hst;
    }

    public double getOrderTotal() {
        return orderTotal;
    }
    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Timestamp getOrderPlacedDate() {
        return orderPlacedDate;
    }
    public void setOrderPlacedDate(Timestamp orderPlacedDate) {
        this.orderPlacedDate = orderPlacedDate;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", subtotal=" + subtotal +
                ", hst=" + hst +
                ", orderTotal=" + orderTotal +
                ", deliveryDate=" + deliveryDate +
                ", orderPlacedDate=" + orderPlacedDate +
                '}';
    }
}
