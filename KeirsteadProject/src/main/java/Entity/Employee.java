package Entity;

import java.io.Serializable;
import java.lang.invoke.StringConcatFactory;

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

}

