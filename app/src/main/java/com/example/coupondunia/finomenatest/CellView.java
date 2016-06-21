package com.example.coupondunia.finomenatest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class CellView extends View {

    public static int PLAYER_1 = 1, PLAYER_2 = 2;
    private int type = PLAYER_1;
    Random r = new Random();

    public interface OnToggledListener {
        void OnToggled(CellView v, boolean touchOn, int type);
    }

    public interface OnTouchRemove {
        void onTouchRemove(CellView v, MotionEvent touchOff, int type);
    }

    private OnToggledListener toggledListener;
    private OnTouchRemove onTouchRemove;
    int idX = 0; //default
    int idY = 0; //default
    boolean onRandomGenerated;

    public CellView(Context context, int x, int y) {
        super(context);
        idX = x;
        idY = y;
    }

    public CellView(Context context) {
        this(context, null);
    }

    public CellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (onRandomGenerated) {
            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(25);
            paint2.setAntiAlias(true);
            paint2.setTextAlign(Paint.Align.CENTER);

            if (type == PLAYER_1) {
                canvas.drawColor(Color.RED);
                canvas.drawText("F1", this.getMeasuredWidth() / 2, this.getMeasuredHeight() / 2, paint2);
            }
            else {
                canvas.drawColor(Color.BLUE);
                canvas.drawText("F2", this.getMeasuredWidth() / 2, this.getMeasuredHeight() / 2, paint2);
            }
        }
        else {
//            int color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            canvas.drawColor(Color.parseColor("#9acc9a"));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                invalidate();

                if (toggledListener != null) {
                    toggledListener.OnToggled(this, true, type);
                }

                return true;

            case MotionEvent.ACTION_UP:
                setColor(false);
                invalidate();

                onTouchRemove.onTouchRemove(this, event, type);
                return true;

        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void setOnToggledListener(OnToggledListener listener) {
        toggledListener = listener;
    }

    public void setOnTouchRemoveListener(OnTouchRemove listener) {
        onTouchRemove = listener;
    }

    public int getIdX() {
        return idX;
    }

    public int getIdY() {
        return idY;
    }

    public void setColor(boolean onRandomGenerated) {
        this.onRandomGenerated = onRandomGenerated;
    }

    public void setPlayer(int player) {
        this.type = player;
    }
}