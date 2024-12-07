package Entity;

import java.io.Serializable;

public class PizzaCrust implements Serializable {
    // Fields
    private int id;
    private String name;
    private double price;

    // Constructors
    public PizzaCrust(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public PizzaCrust() {}

    // Methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PizzaCrust{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
