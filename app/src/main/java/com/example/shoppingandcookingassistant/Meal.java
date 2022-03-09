package com.example.shoppingandcookingassistant;

public class Meal {

    private String name;
    private String portions;
    private String daysLeft;
    private String ingredients;

    public Meal(String name, String portions, String daysLeft, String ingredients) {
        this.name = name;
        this.portions = portions;
        this.daysLeft = daysLeft;
        this.ingredients = ingredients;
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

    public String getInstructions() {
        return ingredients;
    }
}
