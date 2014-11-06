package com.aidilab.ble.gesture;

import android.content.Context;

import com.aidilab.ble.interfaces.FizzlyActivity;
import com.aidilab.ble.utils.SensorsValues;

public class GestureDetector {

private int CYCLE_LIMIT = 7;
	

	public static final int CENTER_HIT = 1;
	public static final int LEFT_HIT   = 2;
	public static final int RIGHT_HIT  = 3;


	private FizzlyActivity mFizzlyActivity = null;
	private Context mContext = null;
	private int cycles = 0;
	private boolean canPlay = true;
	
	
	
	public GestureDetector(FizzlyActivity mDeviceActivity) {
		this.mFizzlyActivity = mDeviceActivity;
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
				cycles = CYCLE_LIMIT;
				mFizzlyActivity.onGestureDetected(CENTER_HIT);				
			}
			else if (sv.getAccelerometer().y > 17) {
				cycles = CYCLE_LIMIT;
				mFizzlyActivity.onGestureDetected(LEFT_HIT);
			}
			else if (sv.getAccelerometer().y < -17) {
				cycles = CYCLE_LIMIT;
				mFizzlyActivity.onGestureDetected(RIGHT_HIT);
			}
		}
	}
	
	
	
}
