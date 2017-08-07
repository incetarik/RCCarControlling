package com.incetarik.rccarcontrolling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {
    private Button               btnDeviceRefresh;
    private ListView             deviceList;
    private BluetoothAdapter     myBluetooth;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList            list;
    private ArrayAdapter         adapter;

    private static final String HC_MAC_ADDRESS = "20:16:03:30:76:11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeComponents();
        ensureBluetoothAvailable();
        ensureBluetoothEnabled();
        bindListeners();
    }

    private void initializeComponents() {
        btnDeviceRefresh = (Button) findViewById(R.id.btDeviceRefresh);
        deviceList = (ListView) findViewById(R.id.listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    //checking if the device supports bluetooth
    private void ensureBluetoothAvailable() {
        if (myBluetooth != null) return;

        toast("Bluetooth Device Not Available");

        //finish this activity and going back to the previous activity
        finish();
    }

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //checking if the bluetooth is enabled,
    // should not be invoked if the device does not support bluetooth,
    //Otherwise it gonna throw Java nullPointerException
    private void ensureBluetoothEnabled() {
        //Ask user to turn the bluetooth on if not
        if (!myBluetooth.isEnabled())
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
    }

    private void bindListeners() {

        //Listener for Refresh button to refresh the paired devices list
        btnDeviceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDevicesList();
            }
        });

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                String address = getMACAddress(v);
                startCarControlActivity(address);
            }
        });
    }

    // Get the selected device MAC address, the last 17 chars in the View which is TextView
    private String getMACAddress(View v) {
        String info = ((TextView) v).getText().toString();
        return info.substring(info.length() - 17);
    }

    //send device mac address by intent to CarControlActivity while starting it
    private void startCarControlActivity(String macAddress) {
        // Make an intent to start Car Control Activity.
        Intent i = new Intent(this, CarController.class);

        //this will be received at Car Control Activity
        i.putExtra("MAC", macAddress);

        //go to Car Control Activity
        startActivity(i);
    }

    private void updateDevicesList() {

        //clear data from list
        list.clear();

        //get phone's bluetooth bounded devices
        pairedDevices = myBluetooth.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                if (bt.getAddress().equals(HC_MAC_ADDRESS)) {
                    startCarControlActivity(HC_MAC_ADDRESS);
                    return;
                }

                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else toast("No Paired Bluetooth Devices Found.");

        adapter.notifyDataSetChanged();
        pairedDevices = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDevicesList();
    }

    //setting the
    private void setDevicesList() {
        list = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        updateDevicesList();
    }
}