package com.aidilab.ble.interfaces;

import android.content.Context;

import com.aidilab.ble.interfaces.FizzlyActivity;
import com.aidilab.ble.utils.SensorsValues;

public abstract class GestureDetector {

	private int CYCLE_LIMIT = 7;
	private int cycles      = 0;

	// Gesture List
	public static final int NULL_GESTURE = 0;

	private FizzlyActivity mFizzlyActivity = null;
	private Context mContext               = null;
	

	public GestureDetector(FizzlyActivity mActivity) {
		this.mFizzlyActivity = mActivity;
		this.mContext = mActivity.getBaseContext();
	}
	
	
	/**
	 * 		if ( checkTimeout() ) {
	 *		if (sv.getAccelerometer().z > 17) {
	 *			mFizzlyActivity.onGestureDetected(NULL_GESTURE);		
	 *			lockGestureUntilTimeout();
	 *		}
	 *	}
	 * 
	 * @param sv
	 */	
	public abstract void detectGesture(SensorsValues sv);
	
	
	public void setCyclesTimeout(int cycles){
		CYCLE_LIMIT = cycles;
	}
	
	protected boolean checkTimeout(){
		if(cycles > 0){
			cycles--;
			return false;
		}
		else
			return true;
	}
	
	
	protected void lockGestureUntilTimeout(){
		cycles = CYCLE_LIMIT;
	}
	
	protected void notifyGesture(int gestureId){
		mFizzlyActivity.onGestureDetected(gestureId);
	}
	
}
