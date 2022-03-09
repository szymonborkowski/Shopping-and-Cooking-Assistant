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

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.util.ArrayList;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Toast.makeText(getActivity(), "Ingredients added", Toast.LENGTH_SHORT).show();
            }
        });

        // List of scanned items:
        basketContents = new ArrayList<>();

        listOfScannedItemsRV = root.findViewById(R.id.listOfScannedItemsRV);

        rvAdapter = new BasketListRVAdapter(getActivity(), basketContents);

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
                            barcodeDescription.setText(result.toString());  // displays result
                            rvAdapter.addItemToBasket(result.toString());

                            // display a message to the user
                            Toast.makeText(getActivity(), result.toString() + " added", Toast.LENGTH_SHORT).show();
                            previousResult = result;
                        }

                        else if(result.toString().equals(previousResult.toString()) && !timerStarted) {
                            timerStarted = true;
                            long timeStartMillis = System.currentTimeMillis();
                            timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeStartMillis);

                            if(timerComplete) {
                                // add the item to the list
                                barcodeDescription.setText(result.toString());  // displays result
                                rvAdapter.addItemToBasket(result.toString());

                                // display a message to the user
                                Toast.makeText(getActivity(), result.toString() + " added", Toast.LENGTH_SHORT).show();

                                timerComplete = false;
                            }
                        }

                        // if the scanned barcode is different from the previous one
                        // add it to the list immediately
                        else if(!result.toString().equals(previousResult.toString())) {
                                barcodeDescription.setText(result.toString());  // displays result
                                rvAdapter.addItemToBasket(result.toString());

                                Toast.makeText(getActivity(), result.toString() + " added", Toast.LENGTH_SHORT).show();

                                previousResult = result;
                        }

                        // if the timer has been running for 5 seconds,
                        // stop the timer to allow the item to be added to the list again
                        if((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - timeSeconds >= 5) && timerStarted) {
                            timerComplete = true;
                            timerStarted = false;
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
}