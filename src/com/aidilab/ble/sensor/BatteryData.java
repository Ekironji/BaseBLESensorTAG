package com.aidilab.ble.sensor;

import android.util.Log;

public class BatteryData {
	
	public final static int VOLTAGE_LIMIT         = 415; 
	public final static int VOLTAGE_LOW_LIMIT     = 365;
	public final static int VOLTAGE_LOW_LOW_LIMIT = 350;  
	
	public final static int NO_CHARGING = 0x00; 
	public final static int CHARGING   = 0x01; 	

	public final static int BATTERY_USAGE          = 0x00;
	public final static int BATTERY_FULL_USB_POWER = 0x01; 
	public final static int BATTERY_CIRCUIT_ERROR  = 0x02; 
	public final static int BATTERY_CHARGING       = 0x03; 
	
	public final static String BATTERY_USAGE_MSG          = "Battery mode";
	public final static String BATTERY_FULL_USB_POWER_MSG = "Battery full"; 
	public final static String BATTERY_CIRCUIT_ERROR_MSG  = "Battery error"; 
	public final static String BATTERY_CHARGING_MSG       = "Battery charging"; 
	
		
	public int voltage = 0;
	public byte status = 0x00;
	public byte action = 0x00;
	
	public static int getBatteryPercentage(float voltage){
		voltage = voltage*100;
		float volt = voltage - VOLTAGE_LOW_LOW_LIMIT;
		float range = VOLTAGE_LIMIT - VOLTAGE_LOW_LOW_LIMIT;
		int perc = (int)((volt / range) * 100F);
		// Log.i("",voltage + " " + volt + " " + range + " " + perc);
		
		return (int)((volt / range) * 100F);
	}

	public static String getBatteryAction(float voltage, int status){	
				
		if(status == NO_CHARGING){
			if(voltage*100 < VOLTAGE_LIMIT){
				return BATTERY_USAGE_MSG;
			}
			else{
				return BATTERY_USAGE_MSG;
			}			
		}
		else{
			if(voltage*100 < VOLTAGE_LIMIT){
				return BATTERY_CHARGING_MSG;
			}
			else{
				return BATTERY_FULL_USB_POWER_MSG;
			}	
		}
	}

}
