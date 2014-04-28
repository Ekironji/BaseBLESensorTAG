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


import static com.aidilab.ble.sensor.SensorTag.UUID_ACC_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_BAR_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_GYR_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_HUM_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_IRT_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_KEY_DATA;
import static com.aidilab.ble.sensor.SensorTag.UUID_MAG_DATA;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.R;
import com.aidilab.ble.sensor.Sensor;
import com.aidilab.ble.utils.BarometerCalibrationCoefficients;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SimpleKeysStatus;

// Fragment for Device View
public class DeviceViewFragment extends Fragment {
	
	private static final String TAG = "DeviceViewFragment";
	
	public static DeviceViewFragment mInstance = null;
	
	// GUI
	private TextView mStatus;
	private TextView mAcc;

	// House-keeping
	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private DeviceActivity mActivity;
	private static final double PA_PER_METER = 12.0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.i(TAG, "onCreateView");
	    mInstance = this;
	    mActivity = (DeviceActivity) getActivity();
    
	    // The last two arguments ensure LayoutParams are inflated properly.
	    
	    View view = inflater.inflate(R.layout.fragment_device, container, false);
	    	    
	    mStatus = (TextView) view.findViewById(R.id.status);
	    mAcc = (TextView) view.findViewById(R.id.acc_textView);
	    
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
		Point3D v;
		String msg;

  	if (uuidStr.equals(UUID_ACC_DATA.toString())) {
  		v = Sensor.ACCELEROMETER.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		mAcc.setText(msg);
  	} 
  
  	if (uuidStr.equals(UUID_MAG_DATA.toString())) {
  		v = Sensor.MAGNETOMETER.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		//mMagValue.setText(msg);
  	} 

  	if (uuidStr.equals(UUID_GYR_DATA.toString())) {
  		v = Sensor.GYROSCOPE.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		//mGyrValue.setText(msg);
  	} 

  	if (uuidStr.equals(UUID_IRT_DATA.toString())) {
  		v = Sensor.IR_TEMPERATURE.convert(rawValue);
  		// temperatura ambiente
  		msg = decimal.format(v.x) + "\n";
  		//mAmbValue.setText(msg); 
  		
  		// temperatura oggetto
  		msg = decimal.format(v.y) + "\n";
  		//mObjValue.setText(msg);
  	}
  	
  	if (uuidStr.equals(UUID_HUM_DATA.toString())) {
  		v = Sensor.HUMIDITY.convert(rawValue);
  		msg = decimal.format(v.x) + "\n";
  		//mHumValue.setText(msg);
  	}

  	if (uuidStr.equals(UUID_BAR_DATA.toString())) {
  		v = Sensor.BAROMETER.convert(rawValue);
  		double h = (v.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
  		h = (double)Math.round(-h * 10.0) / 10.0;
  		msg = decimal.format(v.x/100) + "\n" + h;
  		//mBarValue.setText(msg);
  	}

  	if (uuidStr.equals(UUID_KEY_DATA.toString())) {
  		SimpleKeysStatus s;
  		s = Sensor.SIMPLE_KEYS.convertKeys(rawValue);
  		
  		switch (s) {
  		case OFF_OFF:
  			//nessuno premuto
  			break;
  		case OFF_ON:
  			// premuto il secondo
  			break;
  		case ON_OFF:
  			// premuto il primo
  			break;
  		case ON_ON:
  			// premuti entrambi
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

}
