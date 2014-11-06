package com.aidilab.ble;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.aidilab.ble.R;
import com.aidilab.ble.common.BleDeviceInfo;
import com.aidilab.ble.fragment.ScanViewFragment;
import com.aidilab.ble.interfaces.FizzlyDeviceScanActivity;
import com.aidilab.ble.sensor.FizzlyBleService;

public class FizzlyMainActivity extends FizzlyDeviceScanActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void startDeviceActivity() {
		mDeviceIntent = new Intent(this, TestFizzlyActivity.class); 
		mDeviceIntent.putExtra(TestFizzlyActivity.EXTRA_DEVICE, mBluetoothDevice); 
		startActivityForResult(mDeviceIntent, REQ_DEVICE_ACT);
	}
	  

}
