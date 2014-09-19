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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.R;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.sensor.gui.HSVColorPickerDialog;
import com.aidilab.ble.sensor.gui.HSVColorPickerDialog.OnColorSelectedListener;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SimpleKeysStatus;

// Fragment for Device View
public class DeviceViewFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "DeviceViewFragment";
	
	public static DeviceViewFragment mInstance = null;
	
	// GUI
	private TextView mStatus;
	private TextView mAcc;
	private TextView mMag;
	private TextView mGyr;
	private ImageButton mRgbButton;
	private ImageButton mPlayRgbButton;
	
	private ImageButton mHighToneButton;
	private ImageButton mLowToneButton;
	
	private EditText mRgbPeriodEditText;
	private EditText mBeepPeriodEditText;
	private EditText mBeepNumberEditText;

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
	    mMag = (TextView) view.findViewById(R.id.mag_textView);
	    mGyr = (TextView) view.findViewById(R.id.gyr_textView);
	    mRgbButton = (ImageButton) view.findViewById(R.id.rgbButton);
	    mPlayRgbButton = (ImageButton) view.findViewById(R.id.playRgbButton);
	    
	    mHighToneButton = (ImageButton) view.findViewById(R.id.highToneButton);
	    mLowToneButton = (ImageButton) view.findViewById(R.id.lowToneButton);
	    
	    mRgbPeriodEditText = (EditText) view.findViewById(R.id.rgbPeriodEditText);
	    mBeepNumberEditText = (EditText) view.findViewById(R.id.beepNumberEditText);
	    mBeepPeriodEditText = (EditText) view.findViewById(R.id.beepPeriodrEditText);
	    
	    mRgbButton.setOnClickListener(this);
	    mPlayRgbButton.setOnClickListener(this);
	    mHighToneButton.setOnClickListener(this);
	    mLowToneButton.setOnClickListener(this);
	    
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
  		v = FizzlySensor.ACCELEROMETER.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		mAcc.setText(msg);
  	} 
  
  	if (uuidStr.equals(UUID_MAG_DATA.toString())) {
  		v = FizzlySensor.MAGNETOMETER.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		mMag.setText(msg);
  	} 

  	if (uuidStr.equals(UUID_GYR_DATA.toString())) {
  		v = FizzlySensor.GYROSCOPE.convert(rawValue);
  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
  		mGyr.setText(msg);
  	} 

  	if (uuidStr.equals(UUID_KEY_DATA.toString())) {
  		SimpleKeysStatus s;
  		s = FizzlySensor.SIMPLE_KEYS.convertKeys(rawValue);
  		
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

  
  
  private int lastColorSelected = Color.GREEN;
  
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rgbButton:
			HSVColorPickerDialog cpd = new HSVColorPickerDialog(mActivity, lastColorSelected, new OnColorSelectedListener() {
			    @Override
			    public void colorSelected(Integer color) {
			    	lastColorSelected = color;
					Log.i("ScanViewFragmanet.onClick()", "rgb " + lastColorSelected);
					mActivity.playColor(Integer.parseInt(mRgbPeriodEditText.getText().toString()), lastColorSelected);	
			    }
			});
			cpd.setTitle( "Pick a color" );
			cpd.show();			
			break;
		case R.id.playRgbButton:
			mActivity.playColor(Integer.parseInt(mRgbPeriodEditText.getText().toString()), lastColorSelected);	
			break;
		case R.id.highToneButton:
			Log.i("ScanViewFragmanet.onClick()", "high");
			break;
		case R.id.lowToneButton:
			Log.i("ScanViewFragmanet.onClick()", "low");
			break;
		default:
			break;
		}
	}

  
  
}
