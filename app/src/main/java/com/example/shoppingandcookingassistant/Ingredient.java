package com.example.shoppingandcookingassistant;

public class Ingredient {

    // TODO: figure out if I need to separate amount int & string

    String name;
    String amountString;
    String unit;
    int amountInt;

    public Ingredient(String name, String amountString) {
        this.name = name;
        this.amountString = amountString;
        parseUnit(amountString);
    }

    public String getName() {
        return name;
    }

    public String getAmountString() {
        return amountString;
    }

    public String getUnit() {
        return unit;
    }

    // TODO: Might be not be required...
    public String getUnitName() {
        switch (unit) {
            case "g":
                return "grams";
            case "mL":
                return "millilitres";
            case "":
                return "units";
            default:
                return "";
        }
    }

    public int getAmountInt() {
        return amountInt;
    }

    public void increaseAmount(int amount) {
        amountInt += amount;
        amountString = amountInt + unit;
    }

    public void decreaseAmount(int amount) {
        amountInt -= amount;
        amountString = amountInt + unit;
    }

    public void parseUnit(String amount) {
        if(amount.endsWith("g")) {
            unit = "g";
            amountInt = Integer.parseInt(amount.substring(0, amount.indexOf("g")-1));
        } else if (amount.endsWith("mL")) {
            unit = "mL";
            amountInt = Integer.parseInt(amount.substring(0, amount.indexOf("m")-1));
        } else {
            unit = "";
            amountInt = Integer.parseInt(amount);
        }
    }

}
