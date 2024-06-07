package com.company.myplatformerfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GameDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PLATFORMS = "platforms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STAGE_ID = "stage_id";
    private static final String COLUMN_LEFT = "left";
    private static final String COLUMN_TOP = "top";
    private static final String COLUMN_RIGHT = "right";
    private static final String COLUMN_BOTTOM = "bottom";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLATFORMS_TABLE = "CREATE TABLE " + TABLE_PLATFORMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STAGE_ID + " INTEGER,"
                + COLUMN_LEFT + " INTEGER,"
                + COLUMN_TOP + " INTEGER,"
                + COLUMN_RIGHT + " INTEGER,"
                + COLUMN_BOTTOM + " INTEGER" + ")";
        db.execSQL(CREATE_PLATFORMS_TABLE);

        String CREATE_SCORES_TABLE = "CREATE TABLE scores (" +
                "stage_id INTEGER PRIMARY KEY," +
                "score LONG)";
        db.execSQL(CREATE_SCORES_TABLE);


        // Populate initial platforms after table creation
        insertInitialPlatforms(db);
    }

    private void insertInitialPlatforms(SQLiteDatabase db) {
        // Stage 1 - Further spacing and increased challenge
        insertPlatform(db, 1, 100, 1850, 300, 1880);
        insertPlatform(db, 1, 350, 1650, 550, 1680);
        insertPlatform(db, 1, 600, 1450, 800, 1480);
        insertPlatform(db, 1, 700, 1300, 900, 1330);
        insertPlatform(db, 1, 300, 1150, 500, 1180);
        insertPlatform(db, 1, 50, 1000, 250, 1030);
        insertPlatform(db, 1, 400, 850, 600, 880);
        insertPlatform(db, 1, 200, 700, 400, 730);
        insertPlatform(db, 1, 650, 550, 850, 580);
        insertPlatform(db, 1, 200, 400, 400, 430);
        insertPlatform(db, 1, 400, 200, 450, 230);


        // Stage 2
        insertPlatform(db, 2, 50, 1850, 250, 1880);
        insertPlatform(db, 2, 400, 1650, 600, 1680);
        insertPlatform(db, 2, 200, 1450, 400, 1480);
        insertPlatform(db, 2, 600, 1300, 800, 1330);
        insertPlatform(db, 2, 200, 1150, 400, 1180);
        insertPlatform(db, 2, 600, 1000, 800, 1030);
        insertPlatform(db, 2, 200, 850, 400, 880);
        insertPlatform(db, 2, 600, 700, 800, 730);
        insertPlatform(db, 2, 200, 550, 400, 580);
        insertPlatform(db, 2, 600, 400, 800, 430);
        insertPlatform(db, 2, 400, 200, 450, 230);


        // Stage 3
        insertPlatform(db, 3, 100, 1850, 300, 1880);
        insertPlatform(db, 3, 550, 1700, 750, 1730);
        insertPlatform(db, 3, 200, 1550, 400, 1580);
        insertPlatform(db, 3, 600, 1400, 800, 1430);
        insertPlatform(db, 3, 150, 1250, 350, 1280);
        insertPlatform(db, 3, 550, 1100, 750, 1130);
        insertPlatform(db, 3, 300, 950, 500, 980);
        insertPlatform(db, 3, 450, 800, 650, 830);
        insertPlatform(db, 3, 200, 650, 400, 680);
        insertPlatform(db, 3, 600, 500, 800, 530);
        insertPlatform(db, 3, 350, 350, 550, 380);
        insertPlatform(db, 3, 400, 150, 450, 180);

    }



    private void insertPlatform(SQLiteDatabase db, int stageId, int left, int top,
                                int right, int bottom) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAGE_ID, stageId);
        values.put(COLUMN_LEFT, left);
        values.put(COLUMN_TOP, top);
        values.put(COLUMN_RIGHT, right);
        values.put(COLUMN_BOTTOM, bottom);
        db.insert(TABLE_PLATFORMS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATFORMS);
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    public ArrayList<Platform> getPlatforms(int stageId) {
        ArrayList<Platform> platforms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLATFORMS, new String[]{COLUMN_LEFT, COLUMN_TOP,
                        COLUMN_RIGHT, COLUMN_BOTTOM},
                COLUMN_STAGE_ID + "=?", new String[]{String.valueOf(stageId)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Platform platform = new Platform(cursor.getInt(0), cursor.getInt(
                        1), cursor.getInt(2), cursor.getInt(3));
                platforms.add(platform);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return platforms;
    }

    public void saveScore(int stageId, long score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stage_id", stageId);
        values.put("score", score);
        db.replace("scores", null, values);
    }

    public long getScore(int stageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("scores", new String[]{"score"}, "stage_id = ?",
                new String[]{String.valueOf(stageId)}, null, null, null);
        if (cursor.moveToFirst()) {
            long score = cursor.getLong(0);
            cursor.close();
            db.close();
            return score;
        }
        cursor.close();
        db.close();
        return 0; // Default to 0 if no score found
    }
}
