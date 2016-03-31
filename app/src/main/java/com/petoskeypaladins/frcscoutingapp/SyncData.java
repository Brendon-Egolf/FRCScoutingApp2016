/*
Android Example to connect to and communicate with Bluetooth
In this exercise, the target is a Arduino Due + HC-06 (Bluetooth Module)

Ref:
- Make BlueTooth connection between Android devices
http://android-er.blogspot.com/2014/12/make-bluetooth-connection-between.html
- Bluetooth communication between Android devices
http://android-er.blogspot.com/2014/12/bluetooth-communication-between-android.html
 */
package com.petoskeypaladins.frcscoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;


public class SyncData extends android.support.v4.app.Fragment {

    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<String> pairedDeviceArrayList;

    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;

    final String SCOUTING_DIRECTORY = "/storage/emulated/0/scouting/";

    TextView textInfo, textStatus, dataStream;
    ListView listViewPairedDevice;
    LinearLayout inputPane;
    EditText inputField;
    Button btnSend;

    private BluetoothService bluetoothService;

    ArrayAdapter<String> pairedDeviceAdapter;

    String message;

    public SyncData() {
        bluetoothService = new BluetoothService(mHandler);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sync_data, container, false);

        textInfo = (TextView) view.findViewById(R.id.info);
        textStatus = (TextView) view.findViewById(R.id.status);
        listViewPairedDevice = (ListView) view.findViewById(R.id.pairedlist);

        btnSend = (Button) view.findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        //using the well-known SPP UUID

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        String stInfo = bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);

        dataStream = (TextView) view.findViewById(R.id.data_stream);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {
        final Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<String>();
            final ArrayList deviceList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device.getName());
            }

            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
            }

            pairedDeviceAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    BluetoothDevice device =
                            (BluetoothDevice) deviceList.get(position);
                    connectDevice(device.getAddress());
                }
            });
        }
        mOutStringBuffer = new StringBuffer("");
    }

    private void sendData() {
        message = "";

        try {
            File dir = new File(SCOUTING_DIRECTORY);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    if (file.exists()) {
                        message += file.getName() + ":";
//Toast.makeText(getContext(), file.getName(), Toast.LENGTH_SHORT).show();
                        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            message += line + "\n";
                        }
                        message += ":";
                    }
                }
                message = message.substring(0, message.length() - 2) + ";";
                dataStream.setText("Sending: \n" + message);
                sendMessage(message);
            } else {
                Toast.makeText(getContext(),
                        "you need to have some data first",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
    }

    private void receiveData(String receivedData) {
        dataStream.setText("Receiving: \n" + receivedData);
        File scoutingDir = new File(SCOUTING_DIRECTORY);
        if (!scoutingDir.exists()) {
            scoutingDir.mkdir();
        }
        final int ROW_LENGTH = 22;
        String[] rawData = receivedData.split(":");
        try {
            for (int i = 0; i < rawData.length; i += 2) {
                String filename = rawData[i];
                Log.d("team", filename);
//                Toast.makeText(getContext(), "before: " + filename, Toast.LENGTH_SHORT).show();
                filename = filename.replace("\n", "").replace("\r", "");
//                Toast.makeText(getContext(), "after: " + filename, Toast.LENGTH_SHORT).show();
                String[] lines = rawData[i + 1].split("\n");
                ArrayList<String[]> newData = new ArrayList<>();
                for (String line : lines) {
                    newData.add(line.split(","));
                }
                File file = new File(SCOUTING_DIRECTORY + filename);
                file.createNewFile();
                ArrayList<String[]> oldData = new ArrayList<>();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                String readLine;
                while ((readLine = bufferedReader.readLine()) != null) {
                    oldData.add(readLine.split(","));
                }
                ArrayList<String[]> mergedData = newData;
                mergedData.addAll(oldData);
                for (int j = 0; j < mergedData.size(); j++) {
                    for (int k = 0; k < mergedData.size(); k++) {
                        Log.d("remove j: ", Integer.toString(j));
                        Log.d("remove k: ", Integer.toString(k));
                        Log.d("length: ", Integer.toString(mergedData.get(k).length));
                        try {
                            if ((Integer.parseInt(mergedData.get(j)[0]) == Integer.parseInt(mergedData.get(k)[0]) && j != k) ||
                                    mergedData.get(k).length < ROW_LENGTH) {
                                Log.d("Removing row " + k + " from", filename);
                                mergedData.remove(k);
                                k--;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                Toast.makeText(getContext(), "Merged data length: " + mergedData.size(), Toast.LENGTH_SHORT).show();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                for (int j = 0; j < mergedData.size(); j++) {
                    boolean badRow = false;
                    for (int k = 0; k < ROW_LENGTH; k++) {
                        Log.d("write j: ", Integer.toString(j));
                        Log.d("write k: ", Integer.toString(k));
                        try {
                            if (k < ROW_LENGTH - 1) {
                                writer.write(mergedData.get(j)[k] + ",");
                                Log.d("writing,", mergedData.get(j)[k]);
                            } else {
                                writer.write(mergedData.get(j)[k]);
                                Log.d("writing", mergedData.get(j)[k]);
                            }
                        } catch (Exception e) {
                            badRow = true;
                        }
                    }
                    if (!badRow) {
                        writer.newLine();
                    }
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file.getAbsoluteFile(), false);
        writer.write("");
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bluetoothService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setStatus(CharSequence status) {
        textStatus.setText(status);
    }

    private final Handler mHandler = new Handler() {
        String receivedText = "";

        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(getString(R.string.title_connecting));
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus(getString(R.string.title_not_connected));
                            break;
                    }
                    dataStream.setText("Stream: \n");
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(getContext(),
                            "Sending data to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    receivedText += readMessage;
                    if (receivedText.substring(receivedText.length() - 1).equals(";")) {
                        receivedText = receivedText.substring(0, receivedText.length() - 1);
                        receiveData(receivedText);
                        Toast.makeText(getContext(),
                                "Recieving data from " + mConnectedDeviceName,
                                Toast.LENGTH_SHORT).show();
                        receivedText = "";
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * Establish connection with other divice
     *
     * @param address   A string of the address of the device to connect to.
     */
    private void connectDevice(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        bluetoothService.connect(device, true);
    }
}
