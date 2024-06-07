package com.company.myplatformerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private TextView stageNameTextView;
    private TextView bestScoreTextView;
    private GameDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dbHelper = new GameDatabaseHelper(this);

        stageNameTextView = findViewById(R.id.stageNameTextView);
        bestScoreTextView = findViewById(R.id.bestScoreTextView);

        // Retrieve the ZIP code passed from MainActivity
        String zipCode = getIntent().getStringExtra("ZIP_CODE");

        // Load the stage and score data based on the ZIP code
        loadStageAndScoreData(zipCode);

        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, GameActivity.class);
            intent.putExtra("STAGE_ID", determineStage(zipCode)); // Pass the stage ID to GameActivity
            startActivity(intent);
        });
    }

    private void loadStageAndScoreData(String zipCode) {
        int stageId = determineStage(zipCode);
        String stageName = "Stage: ";

        switch (stageId) {
            case 1:
                stageName += "I-4 Eyesore";
                break;
            case 2:
                stageName += "Lake Eola";
                break;
            case 3:
                stageName += "Daytona Beach";
                break;
            default:
                stageName += "Unknown";  // Fallback for an unknown stage
                break;
        }
        long lastScoreMillis = getLastScoreForStage(stageId);
        double lastScoreSeconds = lastScoreMillis / 1000.0;

        stageNameTextView.setText(stageName);
        bestScoreTextView.setText("Last Score: " + lastScoreSeconds + " Seconds");
    }

    private int determineStage(String zipCode) {
        // Simple logic to determine stage based on ZIP code
        int numericZip = Integer.parseInt(zipCode);
        if (numericZip % 3 == 0) {
            return 1;
        } else if (numericZip % 3 == 1) {
            return 2;
        } else {
            return 3;
        }
    }

    private long getLastScoreForStage(int stageId) {
        return dbHelper.getScore(stageId); // This calls the database helper to fetch the score
    }
}
