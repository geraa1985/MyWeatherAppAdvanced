package com.example.myweatherappadvanced.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myweatherappadvanced.R;

public class Thermometer extends View {

    private int thermColor;
    private int temperatureColor;

    private int temperature;

    private RectF thermRectangle = new RectF();
    private RectF tempRectangle = new RectF();

    private Paint thermPaint;
    private Paint tempPaint;

    private int width;
    private int height;
    private float radius;

    private int padding;
    private final static int round = 2;

    public Thermometer(Context context) {
        super(context);
        init();
    }

    public Thermometer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public Thermometer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Thermometer, 0,
                0);

        thermColor = typedArray.getColor(R.styleable.Thermometer_battery_color, Color.GRAY);
        temperatureColor = typedArray.getColor(R.styleable.Thermometer_level_color, Color.GREEN);
        temperature = typedArray.getInteger(R.styleable.Thermometer_currentTemp, 0);
        typedArray.recycle();
    }

    private void init() {
        thermPaint = new Paint();
        thermPaint.setColor(thermColor);
        thermPaint.setStyle(Paint.Style.FILL);
        tempPaint = new Paint();
        tempPaint.setColor(temperatureColor);
        tempPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w - getPaddingLeft() - getPaddingRight();
        padding = width/10;
        height = h - getPaddingTop() - getPaddingBottom();
        radius = width / 2.f;

        thermRectangle.set(width / 4.f, padding,
                width - width / 4.f,
                height - padding - radius);

        tempRectangle.set(width / 4.f + padding, (height - 2 * padding) / 2.f - ((height - 2 * padding) / 2.f) * temperature / 50 + 2 * padding,
                width - width / 4.f - padding,
                height - 2 * padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2.f, height - width / 2.f, radius, thermPaint);
        canvas.drawRoundRect(thermRectangle, round, round, thermPaint);
        canvas.drawCircle(width / 2.f, height - width / 2.f, radius - padding, tempPaint);
        canvas.drawRect(tempRectangle, tempPaint);
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        tempRectangle.set(width / 4.f + padding, (height - 2 * padding) / 2.f - ((height - 2 * padding) / 2.f) * temperature / 50 + 2 * padding,
                width - width / 4.f - padding,
                height - 2 * padding);
        invalidate();
    }
}
