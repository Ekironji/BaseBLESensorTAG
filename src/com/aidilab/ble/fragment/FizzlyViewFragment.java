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

import java.text.DecimalFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.sensor.BatteryData;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.sensor.gui.HSVColorPickerDialog;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SensorsValues;
import com.aidilab.ble.R;

// Empty Fragment for Fizzly Device View
public class FizzlyViewFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "FizzlyViewFragment";
	
	public static FizzlyViewFragment mInstance = null;
	
	// Views elements
	private TextView mStatus;

	
	// House-keeping
	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private DeviceActivity mActivity;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.i(TAG, "onCreateView");
	    mInstance = this;
	    mActivity = (DeviceActivity) getActivity();
    
	    // The last two arguments ensure LayoutParams are inflated properly.	    
	    View view = inflater.inflate(R.layout.fragment_device, container, false);
	    	
	    /** 
	     *  GUI initialization
	     */
		//	    mStatus    = (TextView) view.findViewById(R.id.status);
		//	    mRgbButton = (ImageButton) view.findViewById(R.id.rgbButton);	    
		//	    mRgbButton.setOnClickListener(this);	    
		//	 
		//	    accBarGraph = (BarGraph3AxisView) view.findViewById(R.id.accBarGraph);
		//	    accBarGraph.setRange(30);
		//	    accBarGraph.setBarColors(Color.RED, Color.GREEN, Color.BLUE);
	    
	    
	    
	    // Notify activity that UI has been inflated
	    mActivity.onViewInflated(view);
	
	    return view;
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
	  	mStatus.setText(txt);
	  	mStatus.setTextAppearance(mActivity, R.style.statusStyle_Success);
	}
	
	public void setError(String txt) {
	  	mStatus.setText(txt);
	  	mStatus.setTextAppearance(mActivity, R.style.statusStyle_Failure);
	}
	
	void setBusy(boolean f) {
	  	if (f)
	  		mStatus.setTextAppearance(mActivity, R.style.statusStyle_Busy);
	  	else
	  		mStatus.setTextAppearance(mActivity, R.style.statusStyle);  		
	}

  
    private int lastColorSelected = Color.BLACK;
  
    HSVColorPickerDialog mColorPickerDialog;
  
    // Manage GUI touch event
	@Override
	public void onClick(View v) {
		switch(v.getId()){			
		case R.id.rgbButton:
			mActivity.playColor(500, lastColorSelected);	
			break;		
		case R.id.highToneButton:
			mActivity.playBeepSequence(DeviceActivity.BEEPER_TONE_HIGH, 100, 5);
			break;
		default:
			break;
		}
	}

  
  
}
