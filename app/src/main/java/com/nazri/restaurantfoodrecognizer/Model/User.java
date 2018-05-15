package com.nazri.restaurantfoodrecognizer.Model;

/**
 * Created by Matjeri on 30/11/2017.
 */

public class User {
    private String Name;
    private String Password;
    private String Ic;

    public User(){

    }

    public User(String name, String password, String ic) {
        Name = name;
        Password = password;
        Ic = ic;
    }

    public String getIc() {
        return Ic;
    }

    public void setIc(String ic) {
        Ic = ic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
