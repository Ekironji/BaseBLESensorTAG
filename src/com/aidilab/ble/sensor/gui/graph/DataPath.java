package com.aidilab.ble.sensor.gui.graph;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Point;

public class DataPath {

	private String name = null;
	
	private ArrayList<Float> datas = new ArrayList<Float>();
	private ArrayList<Point> pixelData = new ArrayList<Point>();
	
	private float min =  0;
	private float max =  0;
	
	private float localMin = 0;
	private float localMax = 0;
	
	private int color = Color.RED;
	
	private int MAX_POINTS = 0;
	private int MAX_HEIGHT = 0;
	private int MAX_WIDTH = 0;

	
	public DataPath(int color, int MAX_POINTS, int MAX_HEIGHT, int MAX_WIDTH) {
		this.color      = color;
		this.MAX_POINTS = MAX_POINTS;
		this.MAX_HEIGHT = MAX_HEIGHT;
		this.MAX_WIDTH  = MAX_WIDTH;
		
//		for(int i=0; i<10; i++){
//			datas.add(0.0f);
//			pixelData.add(new Point(0,0));			
//		}		
	}
	
	
	public int getColor(){
		return color;
	}

	public void addData(float data){

		checkMinMax(data);
		
		if(datas.size() < MAX_POINTS){
			datas.add(data);
		}
		else {
			datas.remove(0);
			datas.add(data);
		}
		
	}
	
	public ArrayList<Float> getData(){
		return datas;
	}
		
	
	
	public float getMin() {
		return min;
	}


	public float getMax() {
		return max;
	}
	
	public float getLocalMin() {
		return localMin;
	}


	public float getLocalMax() {
		return localMax;
	}
	
	public float getSumOnDatas(){
		float sum = 0;
		
		for(Float f : datas){
			sum += f;
		}
		
		return sum;
	}


	private void checkMinMax(float f){
		if(f > max || max==0.0f)
			max = f;

		if(f<min || min==0.0f)
			min = f;
		
		localMin = 9999;
		localMax = -9999;
		
		for(int i = 0; i<datas.size(); i++){
			if(datas.get(i) < localMin)
				localMin = datas.get(i);

			if(datas.get(i) > localMax)
				localMax = datas.get(i);
		}
	}
	

}
