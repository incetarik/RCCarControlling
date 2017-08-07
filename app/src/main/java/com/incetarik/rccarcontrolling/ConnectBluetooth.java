package com.incetarik.rccarcontrolling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

// UI thread
public class ConnectBluetooth extends AsyncTask<Void, Void, Void> {
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothSocket  btSocket;
    private final  String           address;
    private        ProgressDialog   progress;
    private        Activity         activity;
    private        BluetoothAdapter myBluetooth;
    private        boolean          btConnected;
    private        boolean          connectionFailed;

    public ConnectBluetooth(Activity activity, String address) {
        this.activity = activity;
        this.address = address;
    }

    public static BluetoothSocket getBtSocket() {
        return btSocket;
    }

    //while the progress dialog is shown, the connection is done in background
    @Override
    protected Void doInBackground(Void... devices) {
        try {
            if (btSocket == null || !btConnected) {
                //get the mobile bluetooth device
                myBluetooth = BluetoothAdapter.getDefaultAdapter();

                //connects to the device's address and checks if it's available
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);

                //create a RFCOMM (SPP) connection
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);

                myBluetooth.cancelDiscovery();

                //start connection
                btSocket.connect();
            }
        }
        catch (IOException e) {
            connectionFailed = true;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //show a progress dialog while connecting
        progress = ProgressDialog.show(activity, "Connecting...", "Please wait!!!");
    }

    //after the doInBackground, it checks if everything went fine
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        try {
            progress.dismiss();

            if (connectionFailed) {
                toast("Connection Failed :( Try again.");
                //activity.finish();
            }
            else {
                toast("Connected :)");
                btConnected = true;
            }
        }
        catch (Exception e) {

        }
    }

    private void toast(String message) {
        Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    // To be used when the connection is needed to be stopped
    private void disconnect() {
        if (btSocket == null) return;

        try {
            btSocket.close();
        }
        catch (IOException e) {
            toast("Error in Stopping Connection");
        }
    }
}