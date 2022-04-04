package com.example.shoppingandcookingassistant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.concurrent.TimeUnit;

// Uses Code scanner library created by Yuriy Budiyev
// link to library: https://github.com/yuriy-budiyev/code-scanner

public class BasketFragment extends Fragment {

    private CodeScanner codeScanner;
    private TextView barcodeDescription;
    private RecyclerView listOfScannedItemsRV;
    private BasketListRVAdapter rvAdapter;
    private ArrayList<String> basketContents;

    Result previousResult = null;
    boolean timerStarted = false;
    boolean timerComplete = false;
    long timeSeconds;

    private static final int CAMERA_REQUEST = 101;

    Activity activity;
    public BasketFragment() {
        // Required empty public constructor
    }

    public static BasketFragment newInstance() {
        return new BasketFragment();
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

        Button finishShopBtn = root.findViewById(R.id.finishShopBtn);
        finishShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add to inventory
                saveArray(basketContents.toArray(new String[0]), "barcodes");
                basketContents.clear();
                saveBasket();
                loadBasket();
                Toast.makeText(getActivity(), "Ingredients added", Toast.LENGTH_SHORT).show();
            }
        });

        // List of scanned items:
        listOfScannedItemsRV = root.findViewById(R.id.listOfScannedItemsRV);
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

        codeScanner.setDecodeCallback(new DecodeCallback() {
            // Method runs when a barcode is detected:
            @Override
            public void onDecoded(@NonNull Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: run SQL query on each barcode scanned to retrieve name and add name to list

                        // If the same barcode is scanned, check if the timer has been started
                        // if not, start the timer
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
                                String message = "Latest scanned barcode: " + result.toString();
                                barcodeDescription.setText(message);
                                getBarcodeName(result.toString());

                                Toast.makeText(getActivity(), result + " added", Toast.LENGTH_SHORT).show();

                                previousResult = result;
                        }
                    }
                });
            }
        });

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

    public void saveArray(String[] array, String arrayName) {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.INGREDIENTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(arrayName + "_size", array.length);
        for(int i = 0; i < array.length; i++)
            editor.putString(arrayName + "_" + i, array[i]);
        editor.apply();
    }

    public void saveBasket() {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.BASKET_CONTENTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("basket_number", basketContents.size());
        for(int i = 0; i < basketContents.size(); i++) {
            editor.putString("basket_item_" + i, basketContents.get(i));
        }
        editor.apply();
    }

    public void loadBasket() {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.BASKET_CONTENTS", Context.MODE_PRIVATE);
        int basketSize = preferences.getInt("basket_number", 0);
        basketContents = new ArrayList<>();
        rvAdapter = new BasketListRVAdapter(getActivity(), basketContents);
        for(int i = 0; i < basketSize; i++) {
            basketContents.add(preferences.getString("basket_item_" + i, ""));
        }
    }

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
                    rvAdapter.addItemToBasket(nameBuff);

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
}