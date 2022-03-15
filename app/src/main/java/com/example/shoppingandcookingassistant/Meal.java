package com.example.shoppingandcookingassistant;

import java.util.ArrayList;

public class Meal {

    private String name;
    private String portions;
    private String daysLeft;
    private ArrayList<Ingredient> ingredients;

    public Meal(String name, String portions, String daysLeft, ArrayList<Ingredient> ingredients) {
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

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        String ingredientList = "";

        for(Ingredient ingredient : ingredients) {
            ingredientList += ingredient.name + " - ";
            ingredientList += ingredient.amountString + "\n";
        }

        return ingredientList;
    }
}
