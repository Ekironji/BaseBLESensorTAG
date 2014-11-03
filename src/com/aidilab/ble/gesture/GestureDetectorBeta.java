package com.aidilab.ble.gesture;

import java.util.ArrayList;

import android.content.Context;

import com.aidilab.ble.utils.SensorsValues;

public class GestureDetectorBeta {

	private Context mContext = null;
	
	/**
	 * Zero e' il piu vecchio, MAX_SAMPLE_SIZE e' piu recente
	 */	
	private ArrayList<SensorsValues> mSamples = new ArrayList<SensorsValues>();
	private int MAX_SAMPLE_SIZE = 50;
	
	private ArrayList<Integer> enabledGestures = new ArrayList<Integer>();
	
	
	public GestureDetectorBeta(Context ctx) {
		mContext = ctx;
	}
	
	
	public void detectGesture(SensorsValues sv){
		addSample(sv);
		
		for(int gesture=0; gesture<enabledGestures.size(); gesture++){
			
			switch(gesture){
			case Gestures.HIT:
				break;
			case Gestures.RIGHT_SLIDE:
				break;
			case Gestures.LEFT_SLIDE:
				break;
			case Gestures.LAUNCH:
				break;
			case Gestures.FALL:
				break;
			case Gestures.SHAKE:
				break;
			case Gestures.ROTATION:
				break;			
			}
		}	
	}
	
	private void addSample(SensorsValues sv){
		 mSamples.add(sv);
		 if(mSamples.size() >= MAX_SAMPLE_SIZE){
			 mSamples.remove(0);
		 }
	}
	
	
	private boolean checkHit(){
		return false;
	}
	
	private boolean checkRightSlide(){
		return false;
	}
	
	private boolean checkLeftSlide(){
		return false;
	}
	
	private boolean checkLaunch(){
		return false;
	}
	
	private boolean checkFall(){
		return false;
	}
	
	private boolean checkShake(){
		return false;
	}
		
	private boolean checkRotation(){
		return false;
	}
	
	
	
}
