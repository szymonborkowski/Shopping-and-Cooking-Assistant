package com.example.shoppingandcookingassistant;

import java.util.ArrayList;

/**
 * This class will be used to store what the user currently has in their inventory.
 */
public class Inventory {

    // TODO: Figure out how the Object will be stored locally

    ArrayList<Ingredient> usersIngredients;

    public Inventory() {
        usersIngredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getUsersIngredients() {
        return usersIngredients;
    }

}
