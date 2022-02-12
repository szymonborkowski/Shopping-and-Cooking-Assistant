package com.example.shoppingandcookingassistant;

public class Meal {

    private String name;
    private String portions;
    private String daysLeft;

    public Meal(String name, String portions, String daysLeft) {
        this.name = name;
        this.portions = portions;
        this.daysLeft = daysLeft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        this.daysLeft = daysLeft;
    }
}
