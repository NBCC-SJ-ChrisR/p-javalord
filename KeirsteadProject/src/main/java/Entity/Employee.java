package Entity;

import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {
    //Fields
    private int id;
    private String username;
    private String password;

    //Constructors
    public Employee(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public Employee() {}

    //Methods
    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String pass) {
        this.password = pass;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID= " + id;
    }

    public boolean checkMatch(Employee e1, Employee e2) {
        return e1.getPassword().equals(e2.getPassword()) && e1.getUsername().equals(e2.getUsername());
    }

}

