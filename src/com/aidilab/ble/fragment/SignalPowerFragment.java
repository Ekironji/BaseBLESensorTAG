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


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.R;
import com.aidilab.ble.sensor.FizzlyBleService;

// Empty Fragment for Fizzly Device View
public class SignalPowerFragment extends Fragment {
	
	private static final String TAG = "SignalPowerFragment";
	
	public static SignalPowerFragment mInstance = null;
	
	// Views elements
	private TextView mStatus;

	// House-keeping
	private DeviceActivity mActivity;
	
	private BluetoothGatt 		mBtGatt		 = null;
	private FizzlyBleService  mBtLeService = null;
	
	private Handler mHandler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.i(TAG, "onCreateView");
	    mInstance = this;
	    mActivity = (DeviceActivity) getActivity();
    
	    // The last two arguments ensure LayoutParams are inflated properly.	    
	    View view = inflater.inflate(R.layout.fragment_signalpower, container, false);

		mStatus    = (TextView) view.findViewById(R.id.textViewSignal);
				
	    mBtGatt = mActivity.getBtLeGatt();
	    mBtLeService = mActivity.getBtLeSerivice();
	    
	    // Notify activity that UI has been inflated
	    mActivity.onViewInflated(view);
	
	    mHandler.postDelayed(runnable, 1000);
	    
	    return view;
	}

	final Runnable runnable = new Runnable() {  
        @Override
        public void run() {
        	Log.i(TAG, "runnable run");
        	mBtGatt.readRemoteRssi();
        	Log.i(TAG, "rssi: " + mBtLeService.getRSSI());   
            mHandler.postDelayed(this, 1000);                 
        }
    };
    
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
    public void onPause() {
		super.onPause();
		mHandler.removeCallbacks(runnable);
	}
	
	
	
  
}
