package com.company.myplatformerfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

    private Paint paint;
    private Paint buttonPaint;  // Separate paint object for buttons
    private Player player;
    private Rect leftButton, rightButton;
    private Stage currentStage;
    private GameDatabaseHelper dbHelper;
    private int stageId;

    private Bitmap background;private long startTime;
    private boolean timerRunning;
    private OnStageCompleteListener onStageCompleteListener;

    public interface OnStageCompleteListener {
        void onStageComplete(long timeTaken);
    }

    public void setOnStageCompleteListener(OnStageCompleteListener listener) {
        this.onStageCompleteListener = listener;
    }

    public GameView(Context context, int stageId) {
        super(context);
        this.stageId = stageId;  // Store the stage ID
        initializeBackground(context, stageId);
        initializePaints();
        dbHelper = new GameDatabaseHelper(context);
        currentStage = new Stage();
        loadPlatformsForStage(this.stageId);  // Load platforms based on the stage ID

        startTimer();

    }

    private void initializeBackground(Context context, int stageId) {
        int resId;
        switch (stageId) {
            case 1:
                resId = R.drawable.i4eyesore;

                break;
            case 2:
                resId = R.drawable.eola;
                break;
            case 3:
                resId = R.drawable.daytona;
                break;
            default:
                resId = R.drawable.orlando;  // A default background
                break;
        }
        background = BitmapFactory.decodeResource(context.getResources(), resId);
    }

    private void initializePaints() {
        paint = new Paint();
        buttonPaint = new Paint();  // Initialize button paint
        buttonPaint.setColor(Color.RED);  // Set a distinct color for buttons
        buttonPaint.setStyle(Paint.Style.FILL);  // style is fill for visibility
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background first
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, null);
        }
        // Draw player
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(player.getX(), player.getY(), player.getX() + 50, player.getY() + 50, paint);

        // Draw platforms
        for (Platform platform : currentStage.getPlatforms()) {
            platform.draw(canvas);
        }

        // Draw control buttons
        canvas.drawRect(leftButton, buttonPaint);
        canvas.drawRect(rightButton, buttonPaint);

        player.update(currentStage.getPlatforms());
        invalidate(); // Redraw the view

        if (player.getY() <= 0 && timerRunning) {
            stopTimer();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Setup the UI elements based on the screen size
        setupUI(w, h);

        // Load platforms and initialize player now that we have dimensions
        initializePlayer(w, h, leftButton.top);  // Initialize player with screen dimensions and floorY
    }

    private void setupUI(int width, int height) {
        int buttonHeight = 100;
        int buttonMarginVertical = 10;
        int buttonMarginHorizontal = 20;
        int buttonWidth = (width - 3 * buttonMarginHorizontal) / 2;

        // Initialize buttons
        leftButton = new Rect(buttonMarginHorizontal, height - buttonHeight - buttonMarginVertical,
                buttonMarginHorizontal + buttonWidth, height - buttonMarginVertical);
        rightButton = new Rect(2 * buttonMarginHorizontal + buttonWidth, height - buttonHeight - buttonMarginVertical,
                width - buttonMarginHorizontal, height - buttonMarginVertical);
    }

    private void initializePlayer(int width, int height, int floorY) {
        player = new Player(100, 1800, width, height, floorY);
    }

    private void loadPlatformsForStage(int stageId) {
        ArrayList<Platform> platforms = dbHelper.getPlatforms(stageId);
        Log.d("GameView", "Loaded " + platforms.size() + " platforms for stage " + stageId);
        for (Platform platform : platforms) {
            Log.d("GameView", "Platform coordinates: " + platform.getRect().toShortString());
            currentStage.addPlatform(platform);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (leftButton.contains((int) x, (int) y)) {
                    player.moveLeft();
                } else if (rightButton.contains((int) x, (int) y)) {
                    player.moveRight();
                }
                break;
            case MotionEvent.ACTION_UP:
                player.stopMoving();
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (background != null) {
            background.recycle();  // Help clear the bitmap from memory
            background = null;
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerRunning = true;
    }

    private void stopTimer() {
        if (timerRunning) {
            long endTime = System.currentTimeMillis();
            long elapsedMillis = endTime - startTime;
            timerRunning = false;
            if (onStageCompleteListener != null) {
                onStageCompleteListener.onStageComplete(elapsedMillis);
            }
        }
    }
}
