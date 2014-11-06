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


import static com.aidilab.ble.sensor.Fizzly.UUID_ALL_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_BAT_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_GYR_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_KEY_DATA;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidilab.ble.R;
import com.aidilab.ble.interfaces.FizzlyActivity;
import com.aidilab.ble.sensor.BatteryData;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.sensor.gui.HSVColorPickerDialog;
import com.aidilab.ble.sensor.gui.HSVColorPickerDialog.OnColorSelectedListener;
import com.aidilab.ble.sensor.gui.views.BarGraph3AxisView;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SensorsValues;

// Fragment for Device View
public class DeviceViewFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "DeviceViewFragment";
	
	public static DeviceViewFragment mInstance = null;
	
	// GUI
	private TextView mStatus;
	private TextView mBat;
	private TextView mAcc;
	private TextView mMag;
	private TextView mGyr;
	private TextView mBut;
	private ImageButton mRgbButton;
	private ImageButton mPlayRgbButton;
	
	private ImageButton mHighToneButton;
	private ImageButton mLowToneButton;
	
	private EditText mRgbPeriodEditText;
	private EditText mBeepPeriodEditText;
	private EditText mBeepNumberEditText;
	
	private RelativeLayout mRgbLayout;
	private LinearLayout mBatteryLayout;

	// House-keeping
	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private FizzlyActivity mActivity;
	private static final double PA_PER_METER = 12.0;
	
	private boolean isMagnetometerBroken = true;
	
	private BarGraph3AxisView accBarGraph = null;
	private BarGraph3AxisView magBarGraph = null;
	private BarGraph3AxisView gyrBarGraph = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.i(TAG, "onCreateView");
	    mInstance = this;
	    mActivity = (FizzlyActivity) getActivity();
    
	    // The last two arguments ensure LayoutParams are inflated properly.	    
	    View view = inflater.inflate(R.layout.fragment_device, container, false);
	    	    
	    mStatus = (TextView) view.findViewById(R.id.status);
	    mBat = (TextView) view.findViewById(R.id.bat_textView);
	    mAcc = (TextView) view.findViewById(R.id.acc_textView);
	    mMag = (TextView) view.findViewById(R.id.mag_textView);
	    mGyr = (TextView) view.findViewById(R.id.gyr_textView);
	    mBut = (TextView) view.findViewById(R.id.but_textView);
	    mRgbButton = (ImageButton) view.findViewById(R.id.rgbButton);
	    
	    mHighToneButton = (ImageButton) view.findViewById(R.id.highToneButton);
	    mLowToneButton = (ImageButton) view.findViewById(R.id.lowToneButton);
	    
//	    mRgbPeriodEditText = (EditText) view.findViewById(R.id.rgbPeriodEditText);
//	    mBeepNumberEditText = (EditText) view.findViewById(R.id.beepNumberEditText);
//	    mBeepPeriodEditText = (EditText) view.findViewById(R.id.beepPeriodrEditText);
	    
	    mRgbLayout = (RelativeLayout) view.findViewById(R.id.rgbLayout);
	    mBatteryLayout = (LinearLayout) view.findViewById(R.id.batteryLayout);
	    
	    accBarGraph = (BarGraph3AxisView) view.findViewById(R.id.accBarGraph);
	    accBarGraph.setRange(30);
	    accBarGraph.setBarColors(Color.RED, Color.GREEN, Color.BLUE);
	    
	    magBarGraph = (BarGraph3AxisView) view.findViewById(R.id.magBarGraph);
	    magBarGraph.setRange(3000);
	    magBarGraph.setBarColors(Color.RED, Color.GREEN, Color.BLUE);
	    
	    gyrBarGraph = (BarGraph3AxisView) view.findViewById(R.id.gyrBarGraph);
	    gyrBarGraph.setRange(4000);
	    gyrBarGraph.setBarColors(Color.RED, Color.GREEN, Color.BLUE);
	    
	    mRgbButton.setOnClickListener(this);
	    mHighToneButton.setOnClickListener(this);
	    mLowToneButton.setOnClickListener(this);
	    mRgbLayout.setOnClickListener(this);
	    
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
  public void onCharacteristicChanged(String uuidStr, SensorsValues sv) {
		Point3D v;
		String msg;
  		Integer button;
  		int batteryLevel;

	if (uuidStr.equals(UUID_ALL_DATA.toString())) {  		
  		// accelerometro
  		msg = decimal.format(sv.getAccelerometer().x) + "\n" 
  				+ decimal.format(sv.getAccelerometer().y) + "\n" 
  				+ decimal.format(sv.getAccelerometer().z) + "\n";
  		//mAcc.setText(msg);
  		accBarGraph.setData(sv.getAccelerometer().x, 
  				sv.getAccelerometer().y, 
  				sv.getAccelerometer().z);
  		
  		// PER IL VIDEO LEGO IL MAGNETOMETRO AI DATI DELL ACCELEROMETRO
  		// magnetometro
  		if(isMagnetometerBroken){
  			msg = decimal.format(sv.getAccelerometer().z * 123) + "\n" 
  	  				+ decimal.format(sv.getAccelerometer().y * 123) + "\n" 
  	  				+ decimal.format(sv.getAccelerometer().x * 123) + "\n";
  		}
  		else {
	  		msg = decimal.format(sv.getMagnetometer().x) + "\n" 
	  				+ decimal.format(sv.getMagnetometer().y) + "\n" 
	  				+ decimal.format(sv.getMagnetometer().z) + "\n";
  		}
  		//mMag.setText(msg);
  		magBarGraph.setData(sv.getAccelerometer().z * 123, 
  				sv.getAccelerometer().x * 60, 
  				sv.getAccelerometer().y * 123);
  		
  		batteryLevel = BatteryData.getBatteryPercentage(sv.getBatteryVoltage());
  		msg = batteryLevel + " %";
  		mBat.setText(msg);
  		
  		if(batteryLevel < 15)
  			mBatteryLayout.setBackgroundColor(0x55000000 + Color.RED);
  		else if(batteryLevel < 40)
  			mBatteryLayout.setBackgroundColor(0x55000000 + Color.YELLOW);
  		else
  			mBatteryLayout.setBackgroundColor(0x55000000 + Color.GREEN);
  		
  		// bottone
  		button = sv.getButton();		
  		switch (button) {
  		case 0:
  			mBut.setText("released");
  			break;
  		case 1:
  			mBut.setText("pressed");
  			break;
  		default:
  			throw new UnsupportedOperationException();
  		}
  		
  		mActivity.detectSequence(sv);
  	} 	
  }
  
  public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D v;
		String msg;
		Integer button;
		int batteryLevel;
		
		if (uuidStr.equals(UUID_GYR_DATA.toString())) {
	  		v = FizzlySensor.GYROSCOPE.convert(rawValue);
	  		msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z) + "\n";
	  		//mGyr.setText(msg);
	  		gyrBarGraph.setData(v.x, v.y, v.z);
	  	} 

	  	if (uuidStr.equals(UUID_KEY_DATA.toString())) {
	  		button = FizzlySensor.CAPACITIVE_BUTTON.convertKeys(rawValue);
	  		
	  		switch (button) {
	  		case 0:
	  			mBut.setText("released");
	  			break;
	  		case 1:
	  			mBut.setText("pressed");
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
  HSVColorPickerDialog cpd;
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		if(id == R.id.rgbLayout){
			cpd = new HSVColorPickerDialog(mActivity, lastColorSelected, new OnColorSelectedListener() {
			    @Override
			    public void colorSelected(Integer color) {
			    	lastColorSelected = color;
					Log.i("ScanViewFragmanet.onClick()", "rgb " + lastColorSelected);
//					mActivity.playColor(Integer.parseInt(mRgbPeriodEditText.getText().toString()), lastColorSelected);	
					mActivity.playColor(500, lastColorSelected);	
					mRgbLayout.setBackgroundColor(lastColorSelected + 0xbb000000);
			    }
			});
			cpd.setTitle( "Pick a color" );
			cpd.show();			
		}	
		else if(id == R.id.rgbButton){
			cpd = new HSVColorPickerDialog(mActivity, lastColorSelected, new OnColorSelectedListener() {
			    @Override
			    public void colorSelected(Integer color) {
			    	lastColorSelected = color;
					Log.i("ScanViewFragmanet.onClick()", "rgb " + lastColorSelected);
//					mActivity.playColor(Integer.parseInt(mRgbPeriodEditText.getText().toString()), lastColorSelected);	
					mActivity.playColor(500, lastColorSelected);	
					mRgbLayout.setBackgroundColor(lastColorSelected + 0xbb000000);
			    }
			});
			cpd.setTitle( "Pick a color" );
			cpd.show();			
		}	
		else if(id == R.id.highToneButton){
			mActivity.playBeepSequence(mActivity.BEEPER_TONE_HIGH, 100, 5);
			Log.i("ScanViewFragmanet.onClick()", "high");
		}
		else if(id == R.id.lowToneButton){
			mActivity.playBeepSequence(mActivity.BEEPER_TONE_LOW,  100, 5);
			Log.i("ScanViewFragmanet.onClick()", "low");
		}
		else {}
		
	}

  
  
}
