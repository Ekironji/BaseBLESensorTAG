/**************************************************************************************************
  Filename:       Sensor.java
  Revised:        $Date: 2013-08-30 11:44:31 +0200 (fr, 30 aug 2013) $
  Revision:       $Revision: 27454 $

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
package com.aidilab.ble.sensor;

//import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static com.aidilab.ble.sensor.Fizzly.*;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.aidilab.ble.utils.Point3D;


/**
 * This enum encapsulates the differences amongst the sensors. The differences include UUID values and how to interpret the
 * characteristic-containing-measurement.
 */
public enum FizzlySensor {

  ACCELEROMETER(UUID_ACC_SERV, UUID_ACC_DATA, UUID_ACC_CONF) {
  	@Override
  	public Point3D convert(final byte[] value) {  		
  		float x = (float) ((short) ((value[0] << 8) | (value[1] & 0xff))) / 100;
  		float y = (float) ((short) ((value[2] << 8) | (value[3] & 0xff))) / 100;
  		float z = (float) ((short) ((value[4] << 8) | (value[5] & 0xff))) / 100;

  		return new Point3D(x , y , z );
  	}
  },

 
  MAGNETOMETER(UUID_MAG_SERV, UUID_MAG_DATA, UUID_MAG_CONF) {
    @Override
    public Point3D convert(final byte [] value) {
      // Multiply x and y with -1 so that the values correspond with the image in the app
      float x = (float) ((short) ((value[0] << 8) | (value[1] & 0xff))) / 6842;
	  float y = (float) ((short) ((value[2] << 8) | (value[3] & 0xff))) / 6842;
	  float z = (float) ((short) ((value[4] << 8) | (value[5] & 0xff))) / 6842;
      
	  return new Point3D(x , y , z);
    }
  },

  GYROSCOPE(UUID_GYR_SERV, UUID_GYR_DATA, UUID_GYR_CONF) {
    @Override
    public Point3D convert(final byte [] value) {

      float x = (float) ((short) ((value[0] << 8) | (value[1] & 0xff))) / 8.75F;
  	  float y = (float) ((short) ((value[2] << 8) | (value[3] & 0xff))) / 8.75F;
  	  float z = (float) ((short) ((value[4] << 8) | (value[5] & 0xff))) / 8.75F;
      
      return new Point3D(x, y, z);      
    }
  },


  BATTERY(UUID_BAT_SERV, UUID_BAT_DATA, UUID_BAT_CONF) {
	  	@Override
	  	public Point3D convert(final byte[] value) {  		
	  		float level = (float) ((short) ((value[0] << 8) | (value[1] & 0xff))) / 100;
	  		int status = value[2];

	  		Log.v("FizzlySensor", "battery raw data size: " + value[0]+ "  " + value[1]+ "  " +value[2]);
	  		
	  		return new Point3D(level , status , 0 );
	  	}
	  },
  

  CAPACITIVE_BUTTON(UUID_KEY_SERV, UUID_KEY_DATA, null) {
    @Override
    public Integer convertKeys(final byte [] value) {
      Integer encodedInteger = (int) value[0];

      return encodedInteger;
    }
  };

  /**
   * Gyroscope, Magnetometer, Barometer, IR temperature all store 16 bit two's complement values in the awkward format LSB MSB, which cannot be directly parsed
   * as getIntValue(FORMAT_SINT16, offset) because the bytes are stored in the "wrong" direction.
   * 
   * This function extracts these 16 bit two's complement values.
   * */
  private static Integer shortSignedAtOffset(byte[] c, int offset) {
    Integer lowerByte = (int) c[offset] & 0xFF; 
    Integer upperByte = (int) c[offset+1]; // // Interpret MSB as signed
    return (upperByte << 8) + lowerByte;
  }

  private static Integer shortUnsignedAtOffset(byte[] c, int offset) {
    Integer lowerByte = (int) c[offset] & 0xFF; 
    Integer upperByte = (int) c[offset+1] & 0xFF; // // Interpret MSB as signed
    return (upperByte << 8) + lowerByte;
  }

  public void onCharacteristicChanged(BluetoothGattCharacteristic c) {
    throw new UnsupportedOperationException("Programmer error, the individual enum classes are supposed to override this method.");
  }

  public Integer convertKeys(byte[] value) {
    throw new UnsupportedOperationException("Programmer error, the individual enum classes are supposed to override this method.");
  }

  public Point3D convert(byte[] value) {
    throw new UnsupportedOperationException("Programmer error, the individual enum classes are supposed to override this method.");
  }

	private final UUID service, data, config;
	private byte enableCode; // See getEnableSensorCode for explanation.
	public static final byte DISABLE_SENSOR_CODE = 0;
	public static final byte ENABLE_SENSOR_CODE = 1;
	public static final byte CALIBRATE_SENSOR_CODE = 2;

//	/**
//	 * Constructor called by the Gyroscope because he needs a different enable
//	 * code.
//	 */
//  private FizzlySensor(UUID service, UUID data, UUID config, byte enableCode) {
//    this.service = service;
//    this.data = data;
//    this.config = config;
//    this.enableCode = enableCode;
//  }

  /**
   * Constructor
   * */
  private FizzlySensor(UUID service, UUID data, UUID config) {
    this.service = service;
    this.data = data;
    this.config = config;
    this.enableCode = ENABLE_SENSOR_CODE; // This is the sensor enable code for all sensors except the gyroscope
  }

  /**
   * @return the code which, when written to the configuration characteristic, turns on the sensor.
   * */
  public byte getEnableSensorCode() {
    return enableCode;
  }

  public UUID getService() {
    return service;
  }

  public UUID getData() {
    return data;
  }

  public UUID getConfig() {
    return config;
  }

  public static FizzlySensor getFromDataUuid(UUID uuid) {
    for (FizzlySensor s : FizzlySensor.values()) {
      if (s.getData().equals(uuid)) {
        return s;
      }
    }
    throw new RuntimeException("Programmer error, unable to find uuid.");
  }
  
  public static final FizzlySensor[] SENSOR_LIST = { ACCELEROMETER, MAGNETOMETER, GYROSCOPE, BATTERY, CAPACITIVE_BUTTON};
}
