/**************************************************************************************************
  Filename:       DeviceViewFragment.java
  Revised:        $Date: 2013-08-30 12:02:37 +0200 (fr, 30 aug 2013) $
  Revision:       $Revision: 27470 $

  Copyright 2013 Texas Instruments Incorporated. All rights reserved.
 
  IMPORTANT: Your use of this Software is limited to those specific rights
  granted under the terms of a software license agreement between the user
  who downloaded the software, his/her employer (which must be your employer)
  and Texas Instruments Incorporated (the "License").  You may not use this
  Software unless you agree to abide by the terms of the License. 
  The License limits your use, and you acknowledge, that the Software may not be 
  modified, copied or distributed unless used solely and exclusively in conjunction 
  with a Texas Instruments Bluetooth device. Other than for the foregoing purpose, 
  you may not use, reproduce, copy, prepare derivative works of, modify, distribute, 
  perform, display or sell this Software and/or its documentation for any purpose.
 
  YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE
  PROVIDED �AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,
  INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
  NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL
  TEXAS INSTRUMENTS OR ITS LICENSORS BE LIABLE OR OBLIGATED UNDER CONTRACT,
  NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER
  LEGAL EQUITABLE THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES
  INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT, PUNITIVE
  OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT
  OF SUBSTITUTE GOODS, TECHNOLOGY, SERVICES, OR ANY CLAIMS BY THIRD PARTIES
  (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.
 
  Should you have any questions regarding your right to use this Software,
  contact Texas Instruments Incorporated at www.TI.com

 **************************************************************************************************/
package com.aidilab.ble.fragment;


import static com.aidilab.ble.sensor.Fizzly.UUID_ACC_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_ALL_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_BAT_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_GYR_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_KEY_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_MAG_DATA;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.R;
import com.aidilab.ble.sensor.BatteryData;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SensorsValues;

// Empty Fragment for Fizzly Device View
public class DrumSimpleFragment extends Fragment{
	
	private static final String TAG = "DrumSimpleFragment";
	
	public static DrumSimpleFragment mInstance = null;
	
	// Views elements
	private TextView mStatus;
	private ImageView fizzlyImage;
	private DeviceActivity mActivity;
	
	TranslateAnimation leftAnimation;
	TranslateAnimation rightAnimation;
	TranslateAnimation downAnimation;
	
	int count = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.i(TAG, "onCreateView");
	    mInstance = this;
	    mActivity = (DeviceActivity) getActivity();
    
	    // The last two arguments ensure LayoutParams are inflated properly.	    
	    View view = inflater.inflate(R.layout.fragment_drumsimple, container, false);
	    	
		//	    mStatus    = (TextView) view.findViewById(R.id.status); 
	    fizzlyImage = (ImageView) view.findViewById(R.id.imageViewFizzly);
//	    fizzlyImage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				switch(count){
//				case 0:
//					Log.i(TAG, "0 left");
//					startLeftAnimation();
//					count++;
//					break;
//				case 1:
//					Log.i(TAG, "1 right");
//					startRightAnimation();
//					count++;
//					break;
//				case 2:
//					Log.i(TAG, "2 down");
//					startDownAnimation();
//					count=0;
//					break;
//				}
//			}
//		});
	    
	    leftAnimation = new TranslateAnimation(0f, -180F, 0f, 0f);
	    leftAnimation.setDuration(250);
//	    leftAnimation.setInterpolator(new BounceInterpolator());
	    
	    rightAnimation = new TranslateAnimation(0f, 180F, 0f, 0f);
	    rightAnimation.setDuration(250);
//	    rightAnimation.setInterpolator(new BounceInterpolator());
	    
	    downAnimation = new TranslateAnimation(0f, 0F, 0f, 200f);
	    downAnimation.setDuration(250);
//	    downAnimation.setInterpolator(new BounceInterpolator());
	    
	    // Notify activity that UI has been inflated
	    mActivity.onViewInflated(view);
	
	    return view;
	}
	
	private int getDisplayHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	public void startLeftAnimation(){
		fizzlyImage.startAnimation(leftAnimation);
	}

	public void startRightAnimation(){
		fizzlyImage.startAnimation(rightAnimation);
	}
	
	public void startDownAnimation(){
		fizzlyImage.startAnimation(downAnimation);
	}

	@Override
	public void onResume() {
	    super.onResume();
	}


	@Override
    public void onPause() {
		super.onPause();
	}
	
	/**
	 * Handle changes in sensor values
	 * */
	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D           v;
		SensorsValues    sv;
	  	Integer buttonState;
	  	int    batteryLevel;
		
	  	// Process sensor packet
	  	if (uuidStr.equals(UUID_ALL_DATA.toString())) {

		    Log.v(TAG, "onCharacteristicChanged() - packettone");
		    
		  	sv = FizzlySensor.ACC_MAG_BUTT_BATT.unpack(rawValue);
		  	batteryLevel = BatteryData.getBatteryPercentage(sv.getBatteryLevel());
		
		  	buttonState = sv.getButton();		
		  	switch (buttonState) {
		  		case 0:
		  			// Log.i("", "released");
		  			break;
		  		case 1:
		  			Log.i("", "pressed");
		  			break;
		  		default:
		  			throw new UnsupportedOperationException();
		  	}
		  	
		  	// Send data to gesture Recognizer
		  	mActivity.detectSequence(sv);
	  	} 	
				
	  	if (uuidStr.equals(UUID_ACC_DATA.toString())) {
	  		v = FizzlySensor.ACCELEROMETER.convert(rawValue);
	  	} 
	  
	  	if (uuidStr.equals(UUID_MAG_DATA.toString())) {
	  		v = FizzlySensor.MAGNETOMETER.convert(rawValue);
	  	} 
	
	  	if (uuidStr.equals(UUID_GYR_DATA.toString())) {
	  		v = FizzlySensor.GYROSCOPE.convert(rawValue);
	  	} 
	  	
	  	if (uuidStr.equals(UUID_BAT_DATA.toString())) {
	  		v = FizzlySensor.BATTERY.convert(rawValue);
	  	} 
	
	  	if (uuidStr.equals(UUID_KEY_DATA.toString())) {
	  		buttonState = FizzlySensor.CAPACITIVE_BUTTON.convertKeys(rawValue);
	  		
	  		switch (buttonState) {
	  		case 0:
	  			//Log.i("", "released");
	  			break;
	  		case 1:
	  			Log.i("", "pressed");
	  			break;
	  		default:
	  			throw new UnsupportedOperationException();
	  		}
	  	}
	}

 
	public void setStatus(String txt) {
//	  	mStatus.setText(txt);
//	  	mStatus.setTextAppearance(mActivity, R.style.statusStyle_Success);
	}
	
	public void setError(String txt) {
//	  	mStatus.setText(txt);
//	  	mStatus.setTextAppearance(mActivity, R.style.statusStyle_Failure);
	}
 
  
}
