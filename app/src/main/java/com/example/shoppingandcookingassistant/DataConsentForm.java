package com.example.shoppingandcookingassistant;

/**
 * This is the activity that will display the Terms & Conditions for usage of the app.
 * The activity is dismissed when the user presses the 'I consent' button.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DataConsentForm extends AppCompatActivity {

    Button button;
    TextView desc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consent_form);

        desc = findViewById(R.id.description);
        desc.setText(getWelcomeString());

        button = findViewById(R.id.dismiss_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // this method returns the consent form that appears when a user first installs the application.
    public String getWelcomeString() {
        String str = "Welcome to the Shopping And Cooking Assistant application." +
                "\nBefore you begin creating your account, there are a few things we must inform you of" +
                "\n\t\t\t1. As part of creating your own SACA account, you will be asked to provide information that you may deemed sensitive and private. This will include:" +
                "\n\t\t\t-\tNames" +
                "\n\t\t\t-\tEmail Address" +
                "\n\t\t\t-\tFood Allergies" +
                "\n\t\t\t2. To access all features on this application, we will require permission to access your device's camera" +
                "\n\t\t\t3. SACA complies with GDPR standards and all information provided on this application is stored securely and transported over encrypted, secure connections" +
                "\nDo you consent to the above terms and conditions?";
        return str;
    }
}
