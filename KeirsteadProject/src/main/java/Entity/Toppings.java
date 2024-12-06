package Entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Toppings implements Serializable {
    //Fields
    private int id;
    private String name;
    private double price;
    private Timestamp createDate;
    private int empAdd;
    private int active;



    //Constructors
    public Toppings(int id, String name, double price, Timestamp createDate, int empAdd, int active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createDate = createDate;
        this.empAdd = empAdd;
        this.active = active;

    }
    public Toppings() {}

    //Methods
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

    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public int getEmpAdd() {
        return empAdd;
    }
    public void setEmpAdd(int empAdd) {
        this.empAdd = empAdd;
    }

    public int isActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Toppings{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", createDate=" + createDate +
                ", empAdd=" + empAdd +
                ", active=" + active +
                '}';
    }
}
