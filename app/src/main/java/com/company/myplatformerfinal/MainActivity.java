package com.company.myplatformerfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText zipCodeEditText;
    private Button submitZipCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipCodeEditText = findViewById(R.id.zipCodeEditText);
        submitZipCodeButton = findViewById(R.id.submitZipCodeButton);

        submitZipCodeButton.setOnClickListener(v -> {
            String zipCode = zipCodeEditText.getText().toString();
            if (!zipCode.isEmpty()) {
                Intent intent = new Intent(MainActivity.this,
                        StartActivity.class);
                intent.putExtra("ZIP_CODE", zipCode);  // Pass the ZIP code as an extra
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a ZIP code",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
