package com.aidilab.ble;

import android.content.Intent;
import android.os.Bundle;

import com.aidilab.ble.interfaces.FizzlyDeviceScanActivity;

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
