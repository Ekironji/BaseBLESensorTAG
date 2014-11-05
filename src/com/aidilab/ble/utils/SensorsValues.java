package com.aidilab.ble.utils;

import com.aidilab.ble.sensor.BatteryData;

public class SensorsValues {
	
	private Point3D accelerometer = null;
	private Point3D magnetometer  = null;
	private Point3D gyroscope     = null;
	private int button = 0;
	float batteryVoltage   = 0;
	int batteryStatus  = 0;
	
	
	public SensorsValues(float accX, float accY, float accZ,
						 float magX, float magY, float magZ,
						 float gyrX, float gyrY, float gyrZ,
						 int button,
						 float batteryLevel, int batteryStatus) {
		super();
		this.accelerometer = new Point3D(accX, accY, accZ);
		this.magnetometer  = new Point3D(magX, magY, magZ);
		this.gyroscope  = new Point3D(gyrX, gyrY, gyrZ);
		this.button = button;
		this.batteryVoltage = batteryLevel;
		this.batteryStatus = batteryStatus;
	}


	public Point3D getAccelerometer() {
		return accelerometer;
	}


	public Point3D getMagnetometer() {
		return magnetometer;
	}


	public int getButton() {
		return button;
	}


	public float getBatteryVoltage() {
		return batteryVoltage;
	}
	
	public float getBatteryPercentage(){
		return BatteryData.getBatteryPercentage(batteryVoltage);
	}


	public int getBatteryStatus() {
		return batteryStatus;
	}
	
	
	
	
	
	
}
