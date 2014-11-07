package com.aidilab.ble;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.aidilab.ble.fragment.FizzlyViewFragment;
import com.aidilab.ble.gesture.GestureDetector;
import com.aidilab.ble.interfaces.FizzlyActivity;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.utils.Effect;
import com.aidilab.ble.utils.SensorsValues;

public class TestFizzlyActivity extends FizzlyActivity{


	private FizzlyViewFragment mFragmentView = null;
	
	// test di ora TOGLIEREEEE
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setGestureDetector(new GestureDetector(this));	
		setSensorPeriod(100);
		
		// Gui custom settings
		setContentView(R.layout.activity_device);	    
	    getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green_fizzly)));
	    getActionBar().setIcon(android.R.color.transparent);
	    
	    mFragmentView = new FizzlyViewFragment();
	    
	    if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mFragmentView).commit();
		}
				
	    
	    
	    
		enableSensors(FizzlySensor.ACC_MAG_BUTT_BATT, FizzlySensor.GYROSCOPE);		

		effects.add(new Effect(this, R.raw.drum_a_1));
		effects.add(new Effect(this, R.raw.drum_a_2));
		effects.add(new Effect(this, R.raw.drum_a_3));
	}
	
	@Override
	public void onGestureDetected(int gestureId) {
		
		// test di ora TOGLIEREEEE
		switch(gestureId){
		case GestureDetector.CENTER_HIT:
			effects.get(0).play();
			break;
		case GestureDetector.LEFT_HIT:
			effects.get(1).play();
			break;
		case GestureDetector.RIGHT_HIT:
			effects.get(2).play();
			break;
		}
		
		
	}



}
