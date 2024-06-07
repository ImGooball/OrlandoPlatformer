package com.company.myplatformerfinal;

import android.graphics.Rect;

import java.util.ArrayList;

public class Player {
    private int x, y;
    private int velocity;
    private boolean isJumping;
    private boolean isBouncing = false; // Flag to keep the player bouncing once started
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -18;
    private int moveDirection = 0; // 0 = no movement, -1 = left, 1 = right
    private final int PLAYER_SIZE = 50; // For now the player is a 50x50 square
    private int screenWidth;
    private int screenHeight;
    private int floorY; // Maximum y-coordinate the player can descend to
    private boolean isFalling = false; // Indicates if the player is falling

    public Player(int startX, int startY, int screenWidth, int screenHeight, int floorY) {
        x = startX;
        y = startY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.floorY = floorY - PLAYER_SIZE; // Ensure the player stays above the floor
        velocity = 0;
        isJumping = false;
    }

    public void moveLeft() {
        moveDirection = -1;
        startBouncing();
    }

    public void moveRight() {
        moveDirection = 1;
        startBouncing();
    }

    public void stopMoving() {
        moveDirection = 0;
    }

    private void startBouncing() {
        if (!isBouncing) {
            isBouncing = true;
            jump(); // Ensure the jump starts immediately when moving
        }
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            velocity = JUMP_STRENGTH;
        }
    }

    public void update(ArrayList<Platform> platforms) {
        // Update x position within screen boundaries
        int newX = x + moveDirection * 10;
        if (newX >= 0 && newX + PLAYER_SIZE <= screenWidth) {
            x = newX;
        }

        // Update y position for bouncing effect
        if (isBouncing) {
            y += velocity;
            velocity += GRAVITY;
            isFalling = velocity > 0; // Player is falling if velocity is positive
            if (y + PLAYER_SIZE >= this.floorY) {
                y = this.floorY - PLAYER_SIZE; // Hit the floor
                isJumping = false; // Stop jumping
                jump(); // Immediately start a new jump
            }
            if (y < 0) {
                y = 0; // Prevent going above the screen
                velocity = JUMP_STRENGTH; // Start falling down
            }
            for (Platform platform : platforms) {
                if (rectBelow().intersect(platform.getRect()) && isFalling) {
                    y = platform.getRect().top - PLAYER_SIZE; // Adjust player's position to platform top
                    isJumping = false;
                    jump(); // Start jumping again
                }
            }
        }
    }

    private Rect rectBelow() {
        return new Rect(x, y + PLAYER_SIZE, x + PLAYER_SIZE, y + PLAYER_SIZE + 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}