package com.aidilab.ble.interfaces;

import static com.aidilab.ble.sensor.Fizzly.UUID_ACC_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_ALL_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_BAT_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_GYR_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_KEY_DATA;
import static com.aidilab.ble.sensor.Fizzly.UUID_MAG_DATA;

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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.aidilab.ble.R;
import com.aidilab.ble.common.GattInfo;
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
import com.aidilab.ble.fragment.DeviceViewFragment;
import com.aidilab.ble.fragment.DrumSimpleFragment;
import com.aidilab.ble.fragment.FizzlyViewFragment;
import com.aidilab.ble.gesture.GestureDetectorAlpha;
import com.aidilab.ble.sensor.BluetoothLeService;
=======
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java
import com.aidilab.ble.sensor.Fizzly;
import com.aidilab.ble.sensor.FizzlyBleService;
import com.aidilab.ble.sensor.FizzlySensor;
import com.aidilab.ble.utils.Point3D;
import com.aidilab.ble.utils.SensorsValues;
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
import com.aidilab.ble.R;
=======
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java

public abstract class FizzlyActivity extends FragmentActivity{
	
	private static String TAG = "FizzlyActivity";
	
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
	
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
	//private DeviceViewFragment mDeviceView         = null;
	private DrumSimpleFragment mDeviceView = null;

=======
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java
	// BLE
	private FizzlyBleService           mBtLeService     = null;
	private BluetoothDevice            mBtDevice        = null;
	private BluetoothGatt              mBtGatt          = null;
	private List<BluetoothGattService> mServiceList     = null;
	private static final int           GATT_TIMEOUT     = 300; // milliseconds
	private boolean                    mServicesRdy     = false;
	private boolean                    mIsReceiving     = false;

	// SensorTag
	private List<FizzlySensor>   mEnabledSensors     = new ArrayList<FizzlySensor>();
	private BluetoothGattService mOadService         = null;
	private BluetoothGattService mConnControlService = null;
	
	// Gesture Recognizer
	protected GestureDetector mGestureDetector = null;
	
	// Sensors parameter
	private int NOTIFICATIONS_PERIOD = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
	    getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green_fizzly)));
	    getActionBar().setIcon(android.R.color.transparent);
	    
	    // GUI - choosing fragment 
	    //mDeviceView = new DeviceViewFragment();
	    mDeviceView = new DrumSimpleFragment();
	    
	    if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mDeviceView).commit();
		}
=======
	    Intent intent = getIntent();
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java
	    
	    // BLE
	    mBtLeService = FizzlyBleService.getInstance();
	    mBtDevice = intent.getParcelableExtra(EXTRA_DEVICE);
	    mServiceList = new ArrayList<BluetoothGattService>();
	
	    // GATT database
	    Resources res = getResources();
	    XmlResourceParser xpp = res.getXml(R.xml.gatt_uuid);
	    new GattInfo(xpp);
	    
	    // clear sensor list
	    mEnabledSensors.clear();
	}
	
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
	public Fragment getFragment(){
		return mDeviceView;
	}

=======
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java
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
	  	fi.addAction(FizzlyBleService.ACTION_GATT_SERVICES_DISCOVERED);
	  	fi.addAction(FizzlyBleService.ACTION_DATA_NOTIFY);
	  	fi.addAction(FizzlyBleService.ACTION_DATA_WRITE);
	  	fi.addAction(FizzlyBleService.ACTION_DATA_READ);
	  	return fi;
	}

	public void onViewInflated(View view) {
	    Log.d(TAG, "Gatt view ready");
	
<<<<<<< HEAD:src/com/aidilab/ble/DeviceActivity.java
	    // Set title bar to device name
	    setTitle("Drum");
	
=======
>>>>>>> origin/master:src/com/aidilab/ble/interfaces/FizzlyActivity.java
	    // Create GATT object
	    mBtGatt = FizzlyBleService.getBtGatt();
	
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

	
	private void setError(String txt) {}
	private void setStatus(String txt) {}
	
	
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
		  		value = (byte) NOTIFICATIONS_PERIOD;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo di tutti i sensori : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}
	  		
	  		
			// FIZZLY: se e' accelerometro ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_ACC_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_ACC_PERI);
		  		value = (byte) NOTIFICATIONS_PERIOD;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell accelererometro : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}	
			
			// FIZZLY: se e' magnetometro ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_MAG_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_MAG_PERI);
		  		value = (byte) NOTIFICATIONS_PERIOD;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell magnetometro : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}	
			
			// FIZZLY: se e' GIRO ne setto il periodo dopo averlo attivato
			if (confUuid.equals(Fizzly.UUID_GYR_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_GYR_PERI);
		  		value = (byte) NOTIFICATIONS_PERIOD;
		  		mBtLeService.writeCharacteristic(charac, value);
		  		Log.i("DeviceActivity","Scrtitta la caratteristica del periodo dell GIROSCOPIO : " + value);
				mBtLeService.waitIdle(GATT_TIMEOUT);
			}
			
			// FIZZLY: se e' batteria
			if (confUuid.equals(Fizzly.UUID_BAT_CONF) && enable) {
				charac = serv.getCharacteristic(Fizzly.UUID_BAT_PERI);
		  		value = (byte) NOTIFICATIONS_PERIOD;
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
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	  	@Override
	  	public void onReceive(Context context, Intent intent) {
	  		final String action = intent.getAction();
	  		int status = intent.getIntExtra(FizzlyBleService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
	
	  		if (FizzlyBleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
	  			if (status == BluetoothGatt.GATT_SUCCESS) {
	  				displayServices();
	  			} else {
	  				Toast.makeText(getApplication(), "Service discovery failed", Toast.LENGTH_LONG).show();
	  				return;
	  			}
	  		} else if (FizzlyBleService.ACTION_DATA_NOTIFY.equals(action)) {
	  			// Notification
	  			byte[] value = intent.getByteArrayExtra(FizzlyBleService.EXTRA_DATA);
	  			String uuidStr = intent.getStringExtra(FizzlyBleService.EXTRA_UUID);
	  			onCharacteristicChanged(uuidStr, value);
	  		} else if (FizzlyBleService.ACTION_DATA_WRITE.equals(action)) {
	  			// Data written
	  			String uuidStr = intent.getStringExtra(FizzlyBleService.EXTRA_UUID);
	  			onCharacteristicWrite(uuidStr,status);
	  		} else if (FizzlyBleService.ACTION_DATA_READ.equals(action)) {
	  			// Data read
	  			String uuidStr = intent.getStringExtra(FizzlyBleService.EXTRA_UUID);
	  			byte  [] value = intent.getByteArrayExtra(FizzlyBleService.EXTRA_DATA);
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

	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D           v = null;
		SensorsValues    sv = null;
	  	Integer buttonState = null;
		
	  	// Process sensor packet
	  	if (uuidStr.equals(UUID_ALL_DATA.toString())) {
	  		
		  	sv = FizzlySensor.ACC_MAG_BUTT_BATT.unpack(rawValue);
		  	
		  	// Send data to gesture Recognizer
		  	this.detectSequence(sv);
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
	
	
	public void setSensorPeriod(int millis){
		NOTIFICATIONS_PERIOD = millis / 10;
	}
	
	public void detectSequence(SensorsValues sv){
		if (sv != null && mGestureDetector != null) {
			mGestureDetector.detectGesture(sv);
		}
	}
	
	// Settings methods	
	protected void setGestureDetector(GestureDetector mGestureDetector){
		this.mGestureDetector = mGestureDetector;
	}
	
	protected void enableSensors(FizzlySensor... sensors){
	    mEnabledSensors.clear();
	    
	    for(FizzlySensor fs : sensors){
	    	mEnabledSensors.add(fs);
	    }
	}
	
	// Abstracts Methods
	public abstract void onGestureDetected(int gestureId);
	

}
