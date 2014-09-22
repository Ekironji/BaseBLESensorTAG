package com.aidilab.ble.sensor;

import com.aidilab.ble.utils.Point3D;

public class BatteryData {
	
	public final static int VOLTAGE_LIMIT = 420; 
	
	public final static int BAT_STATO_NOCHG = 0x00; 
	public final static int BAT_STATO_CHG   = 0x01; 	

	public final static int BATTERY_USAGE          = 0x00;
	public final static int BATTERY_FULL_USB_POWER = 0x01; 
	public final static int BATTERY_CIRCUIT_ERROR  = 0x02; 
	public final static int BATTERY_CHARGING       = 0x03; 
	
	public final static String BATTERY_USAGE_MSG         = "Battery mode";
	public final static String BATTERY_FULL_USB_POWER_MSG = "Battery full"; 
	public final static String BATTERY_CIRCUIT_ERROR_MSG  = "Battery error"; 
	public final static String BATTERY_CHARGING_MSG       = "Batteri charging"; 
	
		
	public int voltage = 0;
	public byte status = 0x00;
	public byte action = 0x00;
	
	public String getBatteryAction(){
		if(status == 0x00){
			if(voltage < VOLTAGE_LIMIT){
				action = BATTERY_USAGE;
				return BATTERY_USAGE_MSG;
			}
			else{
				action = BATTERY_FULL_USB_POWER;
				return BATTERY_FULL_USB_POWER_MSG;
			}			
		}
		else{
			if(voltage < VOLTAGE_LIMIT){
				action = BATTERY_CIRCUIT_ERROR;
				return BATTERY_CIRCUIT_ERROR_MSG;
			}
			else{
				action = BATTERY_CHARGING;
				return BATTERY_CHARGING_MSG;
			}	
		}	
	}
	
	
	public static String getBatteryAction(Point3D values){	
		if(values.y == 0){
			if(values.x < VOLTAGE_LIMIT){
				return BATTERY_USAGE_MSG;
			}
			else{
				return BATTERY_FULL_USB_POWER_MSG;
			}			
		}
		else{
			if(values.x < VOLTAGE_LIMIT){
				return BATTERY_CIRCUIT_ERROR_MSG;
			}
			else{
				return BATTERY_CHARGING_MSG;
			}	
		}
	}

}
