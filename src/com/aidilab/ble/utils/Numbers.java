package com.aidilab.ble.utils;

import android.graphics.Color;

public class Numbers {
	
	
	public static int degreeAngleToColor(double angle){
		int r = 0, g = 0, b = 0;
		
		if(angle >= 0 && angle < 120){
			r = 0;
			g = (int) map(angle, 0, 120, 0, 255);
			b = (int) map(angle, 0, 120, 255, 0);
		}
		else if (angle >= 120 && angle < 240){
			r = (int) map(angle, 120, 240, 0, 255);
			g = (int) map(angle, 120, 240, 255, 0);
			b = 0;
			
		}
		else if (angle >= 240 && angle < 360){
			r = (int) map(angle, 240, 360, 255, 0);
			g = 0;
			b = (int) map(angle, 240, 360, 0, 255);
		}
		
		return Color.rgb(r, g, b);
	}	
	
	
	public static double map(double x, double in_min, double in_max, double out_min, double out_max){
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public static long map(long x, long in_min, long in_max, long out_min, long out_max){
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}	
}
