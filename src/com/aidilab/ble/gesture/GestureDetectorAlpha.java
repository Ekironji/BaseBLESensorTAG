package com.aidilab.ble.gesture;

import java.util.ArrayList;

import android.content.Context;

import com.aidilab.ble2.R;
import com.aidilab.ble.utils.Effect;
import com.aidilab.ble.utils.SensorsValues;

public class GestureDetectorAlpha {

	int CYCLE_LIMIT = 7;
	
	private Context mContext = null;
	ArrayList<Effect> effects = new ArrayList<Effect>();
	int cycles = 0;
	boolean canPlay = true;
	
	
	
	public GestureDetectorAlpha(Context ctx) {
		mContext = ctx;
		effects.add(new Effect(mContext, R.raw.laser1));
		effects.add(new Effect(mContext, R.raw.laser2));
		effects.add(new Effect(mContext, R.raw.laser3));
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
				cycles = CYCLE_LIMIT;
			}
			if (sv.getAccelerometer().y > 17) {
				effects.get(1).play();
				cycles = CYCLE_LIMIT;
			}
			if (sv.getAccelerometer().y < -17) {
				effects.get(1).play();
				cycles = CYCLE_LIMIT;
			}
		}
	}
	
}
