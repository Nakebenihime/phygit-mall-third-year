package com.example.dilemma_mobile.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    private Paint paint = new Paint();
    int row1;
    int column1;
    int row2;
    int column2;
    private int strokeWidth = 8;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // super.onDraw(canvas);
        canvas.drawLine(row1*300, column1*50, row2*300, column2*50, paint);
        System.out.println("TEST");
    }

}