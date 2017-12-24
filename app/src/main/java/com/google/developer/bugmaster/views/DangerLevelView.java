package com.google.developer.bugmaster.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

//TODO: This class should be used in the insect list to display danger level
public class DangerLevelView extends TextView {

    private float strokeWidth;
    int strokeColor,solidColor;
    private String[] colors;

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDangerLevel(int dangerLevel) {
        //TODO: Update the view appropriately based on the level input
        colors = getContext().getResources().getStringArray(R.array.dangerColors);

        String color = colors[dangerLevel-1];
        setSolidColor(color);
    }

    public int getDangerLevel() {
        //TODO: Report the current level back as an integer
        //colors = getContext().getResources().getStringArray(R.array.dangerColors);
        return solidColor;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }


    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {
        solidColor = Color.parseColor(color);

    }
}
