package com.incetarik.rccarcontrolling;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

public class SendData {
    private static BluetoothSocket btSocket = ConnectBluetooth.getBtSocket();
    private static OutputStream    stream   = getStream();

    public static boolean sendAsUnsignedBytes(double radius, double angle) {
        try {
            long millis = System.currentTimeMillis();
            stream.write(radiusReady(radius));
            stream.write(angleReady(angle));
            millis = System.currentTimeMillis() - millis;
            System.out.printf("DIFF: %.6f %n", (float) millis);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static byte angleReady(double angle) {
        return toUnsignedBytes((int) ((angle + 180) * 255.0 / 360));
    }

    private static byte radiusReady(double radius) {
        return toUnsignedBytes((int) ((radius) * 255.0 / 282));
    }

    private static byte toUnsignedBytes(int data) {
        return data < 128 ? (byte) data : (byte) (data - 256);
    }

    private static OutputStream getStream() {
        try {
            return btSocket.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}