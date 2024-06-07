package com.company.myplatformerfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Platform {
    private Rect rect;
    private Paint paint;

    public Platform(int left, int top, int right, int bottom) {
        rect = new Rect(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    public Rect getRect() {
        return rect;
    }
}
