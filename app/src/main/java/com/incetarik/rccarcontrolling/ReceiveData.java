package com.incetarik.rccarcontrolling;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;

public class ReceiveData extends Thread {
    private static BluetoothSocket btSocket = ConnectBluetooth.getBtSocket();
    private static InputStream     stream   = getStream();
    private final Animator animator;

    ReceiveData(Animator animator) {
        this.animator = animator;
    }

    private static InputStream getStream() {
        try {
            return btSocket.getInputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                while (true) {
                    if (stream.read() == 1) break;

                    Thread.sleep(30);
                }

                int f = stream.read();
                int b = stream.read();
                int r = stream.read();
                int l = stream.read();

                animator.update(f, b, r, l);

                Thread.sleep(25);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
