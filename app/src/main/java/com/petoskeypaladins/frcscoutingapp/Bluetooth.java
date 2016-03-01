package com.petoskeypaladins.frcscoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by begolf on 2/29/16.
 */
public class Bluetooth {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothServerSocket serverSocket;
    ArrayAdapter<String> pairedDeviceNames;

    public Bluetooth() {
        //test for bluetooth called earlier

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                pairedDeviceNames.add(device.getName() + "\n" + device.getAddress());
            }
        }

        
    }


}
