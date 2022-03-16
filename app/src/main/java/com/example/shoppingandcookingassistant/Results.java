package com.example.shoppingandcookingassistant;

/**
 * This is the Results class, defining a nested class ResultsHandler.
 * Each instance of this Results is a thread that downloads a recipe image.
 * Each instance of ResultsHandler is added to the RecyclerView adapter parameter list.
 * @author Anthony Bird
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Results extends Thread {
    // instance variables
    private String rID, recipeName, description, cuisine, meal;
    URL urlObj;
    Bitmap bitIn;
    InputStream in;
    List<ResultsHandler> rhList;

    public Results(String rID, String recipeName, String description, String cuisine, String meal, List<Results.ResultsHandler> rhList) {
        this.rID = rID;
        this.recipeName = recipeName;
        this.description = description;
        this.cuisine = cuisine;
        this.meal = meal;
        this.rhList = rhList;
    }

    public void run() {
        // all image files for the recipes are stored in the API and are named with the 'recipeID.jpg'
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/" + rID + ".jpg";
        urlObj = null;
        bitIn = null;
        try {
            urlObj = new URL(url);
            in = urlObj.openStream();
            // creating a bitmap of the image file
            bitIn = BitmapFactory.decodeStream(in);
            in.close();
            rhList.add(new ResultsHandler(recipeName, description, cuisine, meal, bitIn));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ResultsHandler {
        // instance variables
        private String recipeName, description, cuisine, meal;
        Bitmap bitIn;

        public ResultsHandler(String recipeName, String description, String cuisine, String meal, Bitmap bitIn) {
            this.recipeName = recipeName;
            this.description = description;
            this.cuisine = cuisine;
            this.meal = meal;
            this.bitIn = bitIn;
        }

        public String getRecipeName() { return recipeName; }
        public String getDescription() { return description; }
        public String getCuisine() { return cuisine; }
        public String getMeal() { return meal; }
        public Bitmap getBitIn() { return bitIn; }
    }
}
