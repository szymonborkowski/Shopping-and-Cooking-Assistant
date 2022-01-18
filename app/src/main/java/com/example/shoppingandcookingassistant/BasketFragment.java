package com.example.shoppingandcookingassistant;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

// Uses Code scanner library created by Yuriy Budiyev
// link to library: https://github.com/yuriy-budiyev/code-scanner

public class BasketFragment extends Fragment {

    private CodeScanner codeScanner;
    private TextView barcodeDescription;

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
                        barcodeDescription.setText(result.toString());  // displays result
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
}