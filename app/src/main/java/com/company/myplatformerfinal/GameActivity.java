package com.company.myplatformerfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private GameDatabaseHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHelper = new GameDatabaseHelper(this);

        FrameLayout container = findViewById(R.id.gameContainer);

        // Get the stage ID passed from StartActivity
        int stageId = getIntent().getIntExtra("STAGE_ID", 1);  // Default to stage 1 if no ID is passed

        // Initialize the music player
        musicPlayer = new MusicPlayer();

        // Select the music resource based on the stage ID
        int musicResId = getMusicResourceIdForStage(stageId);
        musicPlayer.playMusic(this, musicResId);

        // Pass the stage ID to the GameView
        GameView gameView = new GameView(this, stageId);
        gameView.setOnStageCompleteListener(timeTaken -> {
            dbHelper.saveScore(stageId, timeTaken);
            runOnUiThread(() -> {
                double timeInSeconds = timeTaken / 1000.0;

                //  Display a toast message signaling stage complete
                Toast.makeText(GameActivity.this, "Stage Complete! Time: " +
                        String.format("%.2f seconds", timeInSeconds), Toast.LENGTH_LONG).show();
            });
        });
        container.addView(gameView);
    }

    private int getMusicResourceIdForStage(int stageId) {
        switch (stageId) {
            case 1:
                return R.raw.stage1_music;
            case 2:
                return R.raw.stage2_music;
            case 3:
                return R.raw.stage3_music;
            default:
                return R.raw.default_music;  // Default music if stage ID doesn't match
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // need to figure out how to handle resuming music, not just playing it again
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }
}