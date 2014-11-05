package com.aidilab.ble.gesture;

import java.util.ArrayList;

import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.graphics.Color;

import com.aidilab.ble.R;
import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.utils.Effect;
import com.aidilab.ble.utils.SensorsValues;

public class GestureDetectorAlpha {

	private int CYCLE_LIMIT = 7;
	
	private DeviceActivity mDeviceActivity = null;
	private Context mContext = null;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private int cycles = 0;
	private boolean canPlay = true;
	
	
	
	public GestureDetectorAlpha(DeviceActivity mDeviceActivity) {
		this.mDeviceActivity = mDeviceActivity;
		this.mContext = mDeviceActivity.getBaseContext();
		effects.add(new Effect(mContext, R.raw.drum_a_1));
		effects.add(new Effect(mContext, R.raw.drum_a_2));
		effects.add(new Effect(mContext, R.raw.drum_a_3));
	}
	
	
	public void detectGesture(SensorsValues sv){
		
		if(cycles > 0){
			cycles--;
			canPlay = false;
		}
		else
			canPlay = true;
		
		
		if (canPlay) {
			if (sv.getAccelerometer().z > 17) {
				effects.get(0).play();
				mDeviceActivity.playColorBlink(200, 1 , Color.RED);
				cycles = CYCLE_LIMIT;
			}
			else if (sv.getAccelerometer().y > 17) {
				effects.get(1).play();
				mDeviceActivity.playColorBlink(200, 1 , Color.GREEN);
				cycles = CYCLE_LIMIT;
			}
			else if (sv.getAccelerometer().y < -17) {
				effects.get(2).play();
				mDeviceActivity.playColorBlink(200, 1 ,Color.BLUE);
				cycles = CYCLE_LIMIT;
			}
		}
	}
	
}
