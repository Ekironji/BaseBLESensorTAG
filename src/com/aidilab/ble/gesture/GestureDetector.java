package com.aidilab.ble.gesture;

import com.aidilab.ble.interfaces.FizzlyActivity;
import com.aidilab.ble.utils.SensorsValues;

public class GestureDetector extends com.aidilab.ble.interfaces.GestureDetector{

	private int CYCLE_LIMIT = 7;
	
	public static final int CENTER_HIT = 1;
	public static final int LEFT_HIT   = 2;
	public static final int RIGHT_HIT  = 3;

	private int cycles = 0;
	private boolean canPlay = true;
	
	public GestureDetector(FizzlyActivity mActivity) {
		super(mActivity);
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
				notifyGesture(CENTER_HIT);				
			}
			else if (sv.getAccelerometer().y > 17) {
				cycles = CYCLE_LIMIT;
				notifyGesture(LEFT_HIT);
			}
			else if (sv.getAccelerometer().y < -17) {
				cycles = CYCLE_LIMIT;
				notifyGesture(RIGHT_HIT);
			}
		}
	}
	
	
	
}
