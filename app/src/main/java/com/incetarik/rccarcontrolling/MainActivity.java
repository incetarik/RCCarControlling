package com.incetarik.rccarcontrolling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //starting first activity with welcoming layout
        setContentView(R.layout.welcoming);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (button != null) return;

        button = ((Button) findViewById(R.id.btWelcomingStart));

        //on click listener for "start" button which shows the bluetooth devices on click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting the deviceList activity and pausing this activity
                Intent intent = new Intent(MainActivity.this, DeviceList.class);
                startActivity(intent);
            }
        });

    }
}