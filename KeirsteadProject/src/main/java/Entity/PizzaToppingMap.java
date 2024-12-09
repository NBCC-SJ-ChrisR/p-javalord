package Entity;

import java.io.Serializable;

public class PizzaToppingMap implements Serializable {
    // Fields
    private int id;
    private int pizzaId;
    private int toppingId;

    // Constructors
    public PizzaToppingMap(int id, int pizzaId, int toppingId) {
        this.id = id;
        this.pizzaId = pizzaId;
        this.toppingId = toppingId;
    }
    public PizzaToppingMap() {}

    // Methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPizzaId() {
        return pizzaId;
    }
    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getToppingId() {
        return toppingId;
    }
    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    @Override
    public String toString() {
        return "PizzaToppingMap{" +
                "id=" + id +
                ", pizzaId=" + pizzaId +
                ", toppingId=" + toppingId +
                '}';
    }
}
