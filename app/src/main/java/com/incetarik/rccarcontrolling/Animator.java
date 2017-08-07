package com.incetarik.rccarcontrolling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public final class Animator implements android.os.Handler.Callback {
    private static final double spanCM = 50;

    private final ImageView ivTop, ivBottom, ivLeft, ivRight;
    private final Handler       handler;
    private final CarController carController;
    private       int           carTopMargin, carLeftMargin;

    Animator(CarController activity, int carTopMargin, int carLeftMargin) {
        ivTop = (ImageView) activity.findViewById(R.id.image_top);
        ivBottom = (ImageView) activity.findViewById(R.id.image_bottom);
        ivRight = (ImageView) activity.findViewById(R.id.image_right);
        ivLeft = (ImageView) activity.findViewById(R.id.image_left);
        carController = activity;
        handler = new Handler(this);

        this.carLeftMargin = carLeftMargin;
        this.carTopMargin = carTopMargin;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle it = msg.getData();

        ivTop.animate().translationY(it.getInt("F")).start();
        ivLeft.animate().translationX(it.getInt("L")).start();
        ivRight.animate().translationX(-it.getInt("R")).start();
        ivBottom.animate().translationY(-it.getInt("B")).start();
        return true;
    }

    public final void update(int f, int b, int r, int l) {
        if (carTopMargin == 0 || carLeftMargin == 0) {
            carTopMargin = carController.getTop();
            carLeftMargin = carController.getLeft();
        }

        f = (int) (carTopMargin / spanCM * (spanCM - f));
        b = (int) (carTopMargin / spanCM * (spanCM - b));
        l = (int) (carLeftMargin / spanCM * (spanCM - l));
        r = (int) (carLeftMargin / spanCM * (spanCM - r));

        Bundle bundle = new Bundle(4);
        bundle.putInt("F", f);
        bundle.putInt("B", b);
        bundle.putInt("L", l);
        bundle.putInt("R", r);

        Message ms = new Message();
        ms.setData(bundle);

        handler.sendMessage(ms);
    }
}