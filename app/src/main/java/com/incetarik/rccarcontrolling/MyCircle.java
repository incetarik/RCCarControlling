package com.incetarik.rccarcontrolling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;


public class MyCircle extends View implements IShape {
    private final int smallCircleSize = 500;
    private float viewCenter;
    private float x, y;
    private Bitmap circleControl = null;

    public MyCircle(Context context, float r) {
        super(context);

        if (circleControl == null) {
            circleControl = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.circle), smallCircleSize, smallCircleSize, false);
        }

        x = y = viewCenter = r - smallCircleSize / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
        canvas.drawBitmap(circleControl, x, y, null);
    }

    public void resitLocation() {
        x = y = viewCenter;
        invalidate();
    }

    public void setLocation(float[] coordinates) {
        x = coordinates[0] - smallCircleSize / 2;
        y = coordinates[1] - smallCircleSize / 2;
        invalidate();
    }

    @Override
    public String thisIsA(IShape self) {
        return "Circle";
    }
}