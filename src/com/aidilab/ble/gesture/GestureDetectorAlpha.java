package com.aidilab.ble.gesture;

import java.util.ArrayList;

import android.content.Context;

import com.aidilab.ble2.R;
import com.aidilab.ble.utils.Effect;
import com.aidilab.ble.utils.SensorsValues;

public class GestureDetectorAlpha {

	int CYCLE_LIMIT = 30;
	
	private Context mContext = null;
	ArrayList<Effect> effects = new ArrayList<Effect>();
	int cyclesNoSound = 0;
	
	
	public GestureDetectorAlpha(Context ctx) {
		mContext = ctx;
		effects.add(new Effect(mContext, R.raw.laser1));
		effects.add(new Effect(mContext, R.raw.laser2));
		effects.add(new Effect(mContext, R.raw.laser3));
	}
	
	
	public void detectGesture(SensorsValues sv){
		if(sv.getAccelerometer().z > 17){
			effects.get(0).play();
		}
		if(sv.getButton() > 0){
			effects.get(1).play();
		}
	}
	
}
