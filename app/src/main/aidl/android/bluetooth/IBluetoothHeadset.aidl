package android.bluetooth;

import android.bluetooth.BluetoothDevice;

interface IBluetoothHeadset {
    int getState();
    BluetoothDevice getCurrentHeadset();
    boolean connectHeadset(in BluetoothDevice device);
    void disconnectHeadset();
    boolean isConnected(in BluetoothDevice device);
    boolean startVoiceRecognition();
    boolean stopVoiceRecognition();
    boolean setPriority(in BluetoothDevice device, int priority);
    int getPriority(in BluetoothDevice device);
    int getBatteryUsageHint();
}
