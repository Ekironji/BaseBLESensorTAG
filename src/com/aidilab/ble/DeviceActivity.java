/**************************************************************************************************
  Filename:       DeviceActivity.java
  Revised:        $Date: 2013-09-05 07:58:48 +0200 (to, 05 sep 2013) $
  Revision:       $Revision: 27616 $

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
package com.aidilab.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.aidilab.ble.common.GattInfo;
import com.aidilab.ble.fragment.DeviceViewFragment;
import com.aidilab.ble.fragment.DrumSimpleFragment;
import com.aidilab.ble.fragment.FizzlyViewFragment;
import com.aidilab.ble.gesture.GestureDetectorAlpha;
import com.aidilab.ble.sensor.BluetoothLeService;
import com.aidilab.ble.sensor.Fizzly;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.utils.SensorsValues;
import com.aidilab.ble.R;

public class DeviceActivity extends FragmentActivity {
	// Log
	private static String TAG = "DeviceActivity";
    final byte CHANGE_COLOR = 0x00;
    final byte BLINK 		= 0x01;
    
	public static final int BEEPER_ON_OFF_MODE = 0x00;
	public static final int BEEPER_BLINK_MODE  = 0x01;	
	public static final int BEEPER_TONE_LOW    = 0x00;
	public static final int BEEPER_TONE_HIGH   = 0x01;		
	public static final int BEEPER_OFF         = 0x00;
	public static final int BEEPER_ON          = 0xff;	

	// Activity
	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
	
	//private DeviceViewFragment mDeviceView         = null;
	private DrumSimpleFragment mDeviceView = null;

	// BLE
	private BluetoothLeService         mBtLeService     = null;
	private BluetoothDevice            mBtDevice        = null;
	private BluetoothGatt              mBtGatt          = null;
	private List<BluetoothGattService> mServiceList     = null;
	private static final int           GATT_TIMEOUT     = 300; // milliseconds
	private boolean                    mServicesRdy     = false;
	private boolean                    mIsReceiving     = false;

	// SensorTag
	private List<FizzlySensor>  mEnabledSensors = new ArrayList<FizzlySensor>();
	private BluetoothGattService mOadService = null;
	private BluetoothGattService mConnControlService = null;
	
	// Gesture Recognizer
	GestureDetectorAlpha mGestureDetector = null;
  
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.activity_device);
	    super.onCreate(savedInstanceState);
	    Intent intent = getIntent();
	    
	    getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green_fizzly)));
	    getActionBar().setIcon(android.R.color.transparent);
	    
	    // GUI - choosing fragment 
	    //mDeviceView = new DeviceViewFragment();
	    mDeviceView = new DrumSimpleFragment();
	    
	    if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mDeviceView).commit();
		}
	    
	    // BLE
	    mBtLeService = BluetoothLeService.getInstance();
	    mBtDevice = intent.getParcelableExtra(EXTRA_DEVICE);
	    mServiceList = new ArrayList<BluetoothGattService>();
	
	    // GATT database
	    Resources res = getResources();
	    XmlResourceParser xpp = res.getXml(R.xml.gatt_uuid);
	    new GattInfo(xpp);
	    
	    // Initialize sensor list
	    mEnabledSensors.clear();
	    
	    //es. ABILITARE UN SENSORE
	    mEnabledSensors.add(FizzlySensor.ACC_MAG_BUTT_BATT);	
	    mEnabledSensors.add(FizzlySensor.GYROSCOPE);	
//	    mEnabledSensors.add(FizzlySensor.BATTERY);
//	    mEnabledSensors.add(FizzlySensor.CAPACITIVE_BUTTON);
//	    mEnabledSensors.add(FizzlySensor.ACCELEROMETER);
//	    mEnabledSensors.add(FizzlySensor.MAGNETOMETER);        
	    
	    mGestureDetector = new GestureDetectorAlpha(this);
	}
	
	public Fragment getFragment(){
		return mDeviceView;
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	}

	@Override 
	protected void onResume() {
	  	Log.d(TAG,"onResume");
	    super.onResume();
	    if (!mIsReceiving) {
	    	registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	    	mIsReceiving = true;
	    }
	}

	@Override
	protected void onPause() {
		Log.d(TAG,"onPause");
		super.onPause();
		if (mIsReceiving) {
			unregisterReceiver(mGattUpdateReceiver);
			mIsReceiving = false;
		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
	  	final IntentFilter fi = new IntentFilter();
	  	fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
	  	fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
	  	fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
	  	fi.addAction(BluetoothLeService.ACTION_DATA_READ);
	  	return fi;
	}

	public void onViewInflated(View view) {
	    Log.d(TAG, "Gatt view ready");
	
	    // Set title bar to device name
	    setTitle("Drum");
	
	    // Create GATT object
	    mBtGatt = BluetoothLeService.getBtGatt();
	
	    // Start service discovery
	    if (!mServicesRdy && mBtGatt != null) {
	      if (mBtLeService.getNumServices() == 0)
	        discoverServices();
	      else
	        displayServices();
	    }
	}


	// Application implementation
	//
	BluetoothGattService getOadService() {
		return mOadService;
	}

	BluetoothGattService getConnControlService() {
		return mConnControlService;
	}

	private void discoverServices() {
	    if (mBtGatt.discoverServices()) {
	      Log.i(TAG, "START SERVICE DISCOVERY");
	      mServiceList.clear();
	      setStatus("Service discovery started");
	    } else {
	      setError("Service discovery start failed");
	    }
	}

	private void displayServices() {
	    mServicesRdy = true;
	
	    try {
	      mServiceList = mBtLeService.getSupportedGattServices();
	    } catch (Exception e) {
	      e.printStackTrace();
	      mServicesRdy = false;
	    }

	    // Characteristics descriptor readout done
	    if (mServicesRdy) {
	      setStatus("Service discovery complete");
	      enableSensors(true);
	      enableNotifications(true);
	    } else {
	      setError("Failed to read services");
	    }
	}

	private void setError(String txt) {
	  	if (mDeviceView != null)
	  		mDeviceView.setError(txt);
	}

	private void setStatus(String txt) {
	  	if (mDeviceView != null)
	  		mDeviceView.setStatus(txt);
	}

	private void enableSensors(boolean enable) {
	  	for (FizzlySensor sensor : mEnabledSensors) {
	  		UUID servUuid = sensor.getService();
	  		UUID confUuid = sensor.getConfig();
	  		
	  		// Skip keys 
	  		if (confUuid == null)
	  			break;
	
	  		BluetoothGattService serv = null;
	  		BluetoothGattCharacteristic charac = null;
	  		
	  		byte value;
	  		try {
				serv = mBtGatt.getService(servUuid);
				charac = serv.getCharacteristic(confUuid);
				value = enable ? sensor.getEnableSensorCode()
						: FizzlySensor.DISABLE_SENSOR_CODE;
				mBtLeService.writeCharacteristic(charac, value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			} catch (Exception e) {
				Log.e("DeviceActivity.enableSensors()","service uuid: " + servUuid.toString());
			}
	  		
	  	// FIZZLY: se e' tutti i sensori ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_ALL_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_ALL_PERI);
		  		value = (byte) 5;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo di tutti i sensori : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}
	  		
	  		
			// FIZZLY: se e' accelerometro ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_ACC_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_ACC_PERI);
		  		value = (byte) 5;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell accelererometro : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}	
			
			// FIZZLY: se e' magnetometro ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_MAG_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_MAG_PERI);
		  		value = (byte) 10;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell magnetometro : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}	
			
			// FIZZLY: se e' GIRO ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_GYR_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_GYR_PERI);
		  		value = (byte) 5;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell GIROSCOPIO : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}
			
			// FIZZLY: se e' batteria
			if (confUuid.equals(Fizzly.UUID_BAT_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_BAT_PERI);
		  		value = (byte) 50;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell batteria : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}
			
			
		
	  	}
  	
	}

	private void enableNotifications(boolean enable) {
	  	for (FizzlySensor sensor : mEnabledSensors) {
	  		UUID servUuid = sensor.getService();
	  		UUID dataUuid = sensor.getData();
	  		BluetoothGattService serv = mBtGatt.getService(servUuid);
	  		
	  		Log.i(TAG, "service "+ servUuid.toString() + " is null: " + (serv == null) );
	  		
	  		BluetoothGattCharacteristic charac = serv.getCharacteristic(dataUuid);
	  		
	  		mBtLeService.setCharacteristicNotification(charac,enable);
			mBtLeService.waitIdle(GATT_TIMEOUT);
	  	}
	} 	
	
	public void calibrateHeight() {
		//mHeightCalibrateRequest = true;		
	}

	
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	  	@Override
	  	public void onReceive(Context context, Intent intent) {
	  		final String action = intent.getAction();
	  		int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
	
	  		if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
	  			if (status == BluetoothGatt.GATT_SUCCESS) {
	  				displayServices();
	  			} else {
	  				Toast.makeText(getApplication(), "Service discovery failed", Toast.LENGTH_LONG).show();
	  				return;
	  			}
	  		} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
	  			// Notification
	  			byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
	  			String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
	  			onCharacteristicChanged(uuidStr, value);
	  		} else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
	  			// Data written
	  			String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
	  			onCharacteristicWrite(uuidStr,status);
	  		} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
	  			// Data read
	  			String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
	  			byte  [] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
	  			onCharacteristicsRead(uuidStr,value,status);
	  		}
	
	  		if (status != BluetoothGatt.GATT_SUCCESS) {
	  			setError("GATT error code: " + status);
	  		}
	  	}
	};

	private void onCharacteristicWrite(String uuidStr, int status) {
	  Log.d(TAG,"onCharacteristicWrite: " + uuidStr);
	}

	// Arrivano i dati dal sensore
	private void onCharacteristicChanged(String uuidStr, byte[] value) {
		if (mDeviceView != null) {
			mDeviceView.onCharacteristicChanged(uuidStr, value);
		}
	}

	private void onCharacteristicsRead(String uuidStr, byte [] value, int status) {
		Log.i(TAG, "onCharacteristicsRead: " + uuidStr);		
	}

	
	// Action methods
	public void playColor(int millis, int color){
  		BluetoothGattService serv = null;
  		BluetoothGattCharacteristic charac = null;		

  		serv   = mBtGatt.getService(Fizzly.UUID_LED_SERV);
		charac = serv.getCharacteristic(Fizzly.UUID_LED_CMND);
		byte[] msg = new byte[6];
        msg[0] = (byte)CHANGE_COLOR;
        msg[1] = (byte)Color.red(color);
    	msg[2] = (byte)Color.green(color);
    	msg[3] = (byte)Color.blue(color);
    	msg[4] = (byte)(millis/10);
    	msg[5] = (byte)0x00;
  		mBtLeService.writeCharacteristic(charac, msg);
  		Log.i("DeviceActivity","Scrtitta la caratteristica dei led : " + msg);
		mBtLeService.waitIdle(GATT_TIMEOUT);		
	}
	
	public void playColorBlink(int millis, int blinkNumber, int color){
  		BluetoothGattService serv = null;
  		BluetoothGattCharacteristic charac = null;		

  		serv   = mBtGatt.getService(Fizzly.UUID_LED_SERV);
		charac = serv.getCharacteristic(Fizzly.UUID_LED_CMND);
		byte[] msg = new byte[6];
        msg[0] = (byte)BLINK;
        msg[1] = (byte)Color.red(color);
    	msg[2] = (byte)Color.green(color);
    	msg[3] = (byte)Color.blue(color);
    	msg[4] = (byte)(millis/10);
    	msg[5] = (byte)blinkNumber;
  		mBtLeService.writeCharacteristic(charac, msg);
  		Log.i("DeviceActivity","Scrtitta la caratteristica dei led : " + msg);
		mBtLeService.waitIdle(GATT_TIMEOUT);		
	}
	
	public void playBeepSequence(int tone, int millisPeriod, int beepNumber){
		// abilito il servizio
		BluetoothGattService serv = null;
  		BluetoothGattCharacteristic charac = null;		

  		serv   = mBtGatt.getService(Fizzly.UUID_BEP_SERV);
		charac = serv.getCharacteristic(Fizzly.UUID_BEP_CMND);
		
		if(tone != BEEPER_TONE_LOW && tone != BEEPER_TONE_HIGH){		
			Log.e("FizzlyDevice","Wrong tone value. Must be 0x01 or 0x02");		
			return;
		}		
		
		if(millisPeriod < 1){		
			Log.e("FizzlyDevice","Wrong millis period value. Must be greater than zero");		
			return;
		}	
		
		if(beepNumber < 1){		
			Log.e("FizzlyDevice","Wrong beep numbers value. Must be greater than zero");		
			return;
		}	
		
		byte[] msg = {(byte)BEEPER_BLINK_MODE, (byte)tone, (byte)(millisPeriod/10), (byte)(beepNumber) };	

		mBtLeService.writeCharacteristic(charac, msg);
	}
	
	public void detectSequence(SensorsValues sv){
		mGestureDetector.detectGesture(sv);
	}

}
