package com.example.shoppingandcookingassistant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Uses Code scanner library created by Yuriy Budiyev
// link to library: https://github.com/yuriy-budiyev/code-scanner

public class BasketFragment extends Fragment {

    private CodeScanner codeScanner;
    private TextView barcodeDescription;
    private BasketListRVAdapter rvAdapter;
    private ArrayList<String> basketContents;
    private ArrayList<Ingredient> basketIngredients;
    private HashMap<String, String> barcodes;
    Result previousResult = null;
    private static final int CAMERA_REQUEST = 101;

    Activity activity;
    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveBasket();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBasket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_basket, container, false);

        barcodes = new HashMap<>();
        basketIngredients = new ArrayList<>();

        Button finishShopBtn = root.findViewById(R.id.finishShopBtn);
        finishShopBtn.setOnClickListener(view -> {
            // Adds all of the scanned items into the users inventory and saves them.
            saveArray(basketContents.toArray(new String[0]), "barcodes");
            basketContents.clear();
            saveBasket();
            loadBasket();
            Toast.makeText(getActivity(), "Ingredients added", Toast.LENGTH_SHORT).show();
        });

        // List of scanned items:
        RecyclerView listOfScannedItemsRV = root.findViewById(R.id.listOfScannedItemsRV);
        listOfScannedItemsRV.setAdapter(rvAdapter);
        listOfScannedItemsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Barcode scanner:
        barcodeDescription = root.findViewById(R.id.barcode_textView);  // set a value to the TV
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        CodeScannerView scannerView = root.findViewById(R.id.barcode_scanner_view);
        codeScanner = new CodeScanner(activity, scannerView);

        // Setting the options of the barcode scanner object:
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);

        // Method runs when a barcode is detected:
        codeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            // If the same barcode is scanned ignore result. This is done as the barcode scanner
            // continuously scans. To avoid the item being added multiple times I prevent the
            // item being added if it is the same item.
            if(Objects.isNull(previousResult)) {
                // add the item to the list
                String message = "Latest scanned barcode: " + result;
                barcodeDescription.setText(message);  // displays result
                getBarcodeName(result.toString());

                // display a message to the user
                Toast.makeText(getActivity(), result + " added", Toast.LENGTH_SHORT).show();
                previousResult = result;
            }

            // if the scanned barcode is different from the previous one
            // add it to the list immediately
            else if(!result.toString().equals(previousResult.toString())) {
                    String message = "Latest scanned barcode: " + result;
                    barcodeDescription.setText(message);
                    getBarcodeName(result.toString());

                    Toast.makeText(getActivity(), result + " added", Toast.LENGTH_SHORT).show();

                    previousResult = result;
            }
        }));

        // Initialise the barcode scanner
        scannerView.setOnClickListener(view -> codeScanner.startPreview());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    // When the shop is finished the user can save the ingredients they bought into their inventory.
    // This method saves the ingredients in shared preferences.
    public void saveArray(String[] array, String arrayName) {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.INGREDIENTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_size", array.length);
        for(int i = 0; i < array.length; i++) {
            editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_" + i + "_name", array[i]);
            editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_" + i + "_amount", basketIngredients.get(i).getAmountString());
        }
        editor.apply();
    }

    // This method saves the current items in the basket so that if the user changes screen
    // the items that were in the basket can be reloaded.
    public void saveBasket() {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.BASKET_CONTENTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_basket_number", basketContents.size());
        for(int i = 0; i < basketContents.size(); i++) {
            editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_basket_item_" + i, basketContents.get(i));
        }
        editor.apply();
        for(Ingredient ingredient : basketIngredients) {
            uploadIngredientsToDB(LogInScreenActivity.LOGGED_IN_USER_ID, barcodes.get(ingredient.getName()), ingredient.getAmountString());
        }
    }

    // When the user reopens the screen this method will load the previously saved basket if
    // there is one.
    public void loadBasket() {
        SharedPreferences preferences = getContext().getSharedPreferences(
                "com.example.shoppingandcookingassistant.BASKET_CONTENTS", Context.MODE_PRIVATE);
        int basketSize = preferences.getInt(LogInScreenActivity.LOGGED_IN_USER_ID
                                            + "_basket_number", 0);
        basketContents = new ArrayList<>();
        rvAdapter = new BasketListRVAdapter(getActivity(), basketContents);
        for(int i = 0; i < basketSize; i++) {
            basketContents.add(preferences.getString(LogInScreenActivity.LOGGED_IN_USER_ID
                                                     + "_basket_item_" + i, ""));
        }
    }

    // This method converts barcodes to the name of the product.
    public void getBarcodeName(String barcode) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/barcodeScan.php";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("name0") == null) {
                    Toast.makeText(getActivity(), "No item for this barcode found.", Toast.LENGTH_SHORT).show();
                } else {

                    String nameBuff = jsonObject.getString("name0");
                    String amountBuff = jsonObject.getString("capacity0");
                    rvAdapter.addItemToBasket(nameBuff);
                    basketIngredients.add(new Ingredient(nameBuff, amountBuff));
                    barcodes.put(nameBuff, barcode);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), "ERR: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("ingID", barcode);
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }

    // This method updates what ingredients the user has in the online dB.
    public void uploadIngredientsToDB(String userID, String ingredientID, String amount) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/addIngredient.php";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fail to submit new ingredients - " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // creating a map for storing values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // input parameter(s) for the HTTP POST request
                params.put("userID", userID);
                params.put("contentID", ingredientID);
                params.put("amount", amount);
                return params;
            }
        };
        queue.add(request);
    }
}