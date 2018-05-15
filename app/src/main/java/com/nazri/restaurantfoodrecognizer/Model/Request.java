package com.nazri.restaurantfoodrecognizer.Model;

/**
 * Created by Matjeri on 2/12/2017.
 */


import java.util.List;

public class Request {
    private String ic;
    private String name;
    private String table_number;
    private String total;
    private List<Order> foods;  //List of food order

    public Request() {
    }

    public Request(String ic, String name, String table_number, String total, List<Order> foods) {
        this.ic = ic;
        this.name = name;
        this.table_number = table_number;
        this.total = total;
        this.foods = foods;
    }

    public String getIc() {
        return ic;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
