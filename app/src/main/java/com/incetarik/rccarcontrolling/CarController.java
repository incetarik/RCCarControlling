package com.incetarik.rccarcontrolling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CarController extends AppCompatActivity {
    private TextView tvTouchX, tvTouchY, tvCircleX, tvCircleY, tvRadius, tvAngle;
    private ImageView ivCar;
    private ViewGroup layoutControl, information;
    private MyCircle circleObject;
    private int      bigCircleRadius;
    private double   angle;
    private double   distance;
    private float    touchingX, touchingY;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startConnection();
    }

    private void startConnection() {
        Intent intent  = getIntent();
        String address = intent.getStringExtra("MAC");
        new ConnectBluetooth(this, address).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initVariables();
        bindActions();
        addCircleToCenter();

        //may throw an exception if it tris to animate before connection is established
        animate();
    }

    private void animate() {
        new ReceiveData(new Animator(this, ivCar.getTop(), ivCar.getLeft())).start();
    }

    private void initVariables() {
        if (ivCar != null) return;

        ivCar = ((ImageView) findViewById(R.id.image_car));
        layoutControl = (RelativeLayout) findViewById(R.id.layout_analog_control);
        information = (LinearLayout) findViewById(R.id.llInformation);

        tvTouchX = (TextView) findViewById(R.id.tvTouchX);
        tvTouchY = (TextView) findViewById(R.id.tvTouchY);
        tvCircleX = (TextView) findViewById(R.id.tvCircleX);
        tvCircleY = (TextView) findViewById(R.id.tvCircleY);
        tvRadius = (TextView) findViewById(R.id.tvRadius);
        tvAngle = (TextView) findViewById(R.id.tvAngle);

        initThread();
    }

    public void initThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isWorking = true;
                try {
                    while (isWorking) {
                        SendData.sendAsUnsignedBytes(distance, Math.toDegrees(angle));
                        Thread.sleep((long) 124);
                    }
                }
                catch (Exception e) {
                    isWorking = false;
                }
            }
        });
    }

    private void bindActions() {
        ivCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchInfoVisibility();
            }
        });
        layoutControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onCircleTouch(event);
                return true;
            }
        });

    }

    private void onCircleTouch(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                thread.start();
                break;

            case MotionEvent.ACTION_UP:
                resitLocation();
                break;

            default:
                updateLocation(e.getX(), e.getY());
                break;
        }
    }

    private void updateLocation(float x, float y) {
        circleObject.setLocation(valuesControl(x, y));
        updateInfo();
    }

    private float[] valuesControl(float x, float y) {
        float x2 = x - bigCircleRadius;
        float y2 = y - bigCircleRadius;

        double distance = Math.sqrt(x2 * x2 + y2 * y2);
        angle = StrictMath.atan2(x2, y2);

        if (distance > bigCircleRadius) {
            float[] array = toNearestCircumference(angle);

            x2 = array[0] - bigCircleRadius;
            y2 = array[1] - bigCircleRadius;

            this.distance = Math.sqrt(x2 * x2 + y2 * y2);
            return array;
        }

        this.distance = distance;
        this.touchingX = x;
        this.touchingY = y;
        return new float[] { x, y };
    }

    private float[] toNearestCircumference(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new float[] { (float) ((bigCircleRadius) * (1 + sin)), (float) ((bigCircleRadius) * (1 + cos)) };
    }

    private void updateInfo() {
        if (information.getVisibility() == 8) return;
        tvTouchX.setText(String.format("%.3f", touchingX));
        tvTouchY.setText(String.format("%.3f", touchingY));
        tvCircleX.setText(String.format("%.3f", touchingX - bigCircleRadius));
        tvCircleY.setText(String.format("%.3f", -touchingY + bigCircleRadius));
        tvRadius.setText(String.format("%.3f", distance > 564 ? 564 : distance));
        tvAngle.setText(String.format("%.3f", angle));
    }

    private void resitLocation() {
        circleObject.resitLocation();
        angle = 0;
        distance = 0;
        thread.interrupt();
        initThread();
    }

    private void switchInfoVisibility() {
        information.setVisibility(information.getVisibility() == 0 ? 8 : 0);
    }

    private void addCircleToCenter() {
        bigCircleRadius = layoutControl.getLayoutParams().width / 2;
        circleObject = new MyCircle(this, bigCircleRadius);
        layoutControl.addView(circleObject);
    }

    public int getTop() {
        return ivCar.getTop();
    }

    public int getLeft() {
        return ivCar.getLeft();
    }
}